package rx.internal.operators;

import rx.Observable;
import rx.Producer;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;

/* loaded from: classes2.dex */
public final class OperatorSubscribeOn<T> implements Observable.OnSubscribe<T> {
    final Scheduler scheduler;
    final Observable<T> source;

    public OperatorSubscribeOn(Observable<T> observable, Scheduler scheduler) {
        this.scheduler = scheduler;
        this.source = observable;
    }

    @Override // rx.functions.Action1
    public void call(Subscriber<? super T> subscriber) {
        Scheduler.Worker createWorker = this.scheduler.createWorker();
        subscriber.add(createWorker);
        createWorker.schedule(new AnonymousClass1(subscriber, createWorker));
    }

    /* renamed from: rx.internal.operators.OperatorSubscribeOn$1, reason: invalid class name */
    class AnonymousClass1 implements Action0 {
        final /* synthetic */ Scheduler.Worker val$inner;
        final /* synthetic */ Subscriber val$subscriber;

        AnonymousClass1(Subscriber subscriber, Scheduler.Worker worker) {
            this.val$subscriber = subscriber;
            this.val$inner = worker;
        }

        @Override // rx.functions.Action0
        public void call() {
            final Thread currentThread = Thread.currentThread();
            OperatorSubscribeOn.this.source.unsafeSubscribe(new Subscriber<T>(this.val$subscriber) { // from class: rx.internal.operators.OperatorSubscribeOn.1.1
                @Override // rx.Observer
                public void onNext(T t) {
                    AnonymousClass1.this.val$subscriber.onNext(t);
                }

                @Override // rx.Observer
                public void onError(Throwable th) {
                    try {
                        AnonymousClass1.this.val$subscriber.onError(th);
                    } finally {
                        AnonymousClass1.this.val$inner.unsubscribe();
                    }
                }

                @Override // rx.Observer
                public void onCompleted() {
                    try {
                        AnonymousClass1.this.val$subscriber.onCompleted();
                    } finally {
                        AnonymousClass1.this.val$inner.unsubscribe();
                    }
                }

                @Override // rx.Subscriber
                public void setProducer(final Producer producer) {
                    AnonymousClass1.this.val$subscriber.setProducer(new Producer() { // from class: rx.internal.operators.OperatorSubscribeOn.1.1.1
                        @Override // rx.Producer
                        public void request(final long j) {
                            if (currentThread == Thread.currentThread()) {
                                producer.request(j);
                            } else {
                                AnonymousClass1.this.val$inner.schedule(new Action0() { // from class: rx.internal.operators.OperatorSubscribeOn.1.1.1.1
                                    @Override // rx.functions.Action0
                                    public void call() {
                                        producer.request(j);
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }
}
