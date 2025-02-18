package rx.internal.util;

import rx.Scheduler;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.internal.schedulers.EventLoopsScheduler;

/* loaded from: classes2.dex */
public final class ScalarSynchronousSingle<T> extends Single<T> {
    final T value;

    public static final <T> ScalarSynchronousSingle<T> create(T t) {
        return new ScalarSynchronousSingle<>(t);
    }

    protected ScalarSynchronousSingle(final T t) {
        super(new Single.OnSubscribe<T>() { // from class: rx.internal.util.ScalarSynchronousSingle.1
            @Override // rx.functions.Action1
            public void call(SingleSubscriber<? super T> singleSubscriber) {
                singleSubscriber.onSuccess((Object) t);
            }
        });
        this.value = t;
    }

    public T get() {
        return this.value;
    }

    public Single<T> scalarScheduleOn(Scheduler scheduler) {
        if (scheduler instanceof EventLoopsScheduler) {
            return create((Single.OnSubscribe) new DirectScheduledEmission((EventLoopsScheduler) scheduler, this.value));
        }
        return create((Single.OnSubscribe) new NormalScheduledEmission(scheduler, this.value));
    }

    static final class DirectScheduledEmission<T> implements Single.OnSubscribe<T> {
        private final EventLoopsScheduler es;
        private final T value;

        DirectScheduledEmission(EventLoopsScheduler eventLoopsScheduler, T t) {
            this.es = eventLoopsScheduler;
            this.value = t;
        }

        @Override // rx.functions.Action1
        public void call(SingleSubscriber<? super T> singleSubscriber) {
            singleSubscriber.add(this.es.scheduleDirect(new ScalarSynchronousSingleAction(singleSubscriber, this.value)));
        }
    }

    static final class NormalScheduledEmission<T> implements Single.OnSubscribe<T> {
        private final Scheduler scheduler;
        private final T value;

        NormalScheduledEmission(Scheduler scheduler, T t) {
            this.scheduler = scheduler;
            this.value = t;
        }

        @Override // rx.functions.Action1
        public void call(SingleSubscriber<? super T> singleSubscriber) {
            Scheduler.Worker createWorker = this.scheduler.createWorker();
            singleSubscriber.add(createWorker);
            createWorker.schedule(new ScalarSynchronousSingleAction(singleSubscriber, this.value));
        }
    }

    static final class ScalarSynchronousSingleAction<T> implements Action0 {
        private final SingleSubscriber<? super T> subscriber;
        private final T value;

        ScalarSynchronousSingleAction(SingleSubscriber<? super T> singleSubscriber, T t) {
            this.subscriber = singleSubscriber;
            this.value = t;
        }

        @Override // rx.functions.Action0
        public void call() {
            try {
                this.subscriber.onSuccess(this.value);
            } catch (Throwable th) {
                this.subscriber.onError(th);
            }
        }
    }

    public <R> Single<R> scalarFlatMap(final Func1<? super T, ? extends Single<? extends R>> func1) {
        return create((Single.OnSubscribe) new Single.OnSubscribe<R>() { // from class: rx.internal.util.ScalarSynchronousSingle.2
            @Override // rx.functions.Action1
            public void call(final SingleSubscriber<? super R> singleSubscriber) {
                Single single = (Single) func1.call(ScalarSynchronousSingle.this.value);
                if (single instanceof ScalarSynchronousSingle) {
                    singleSubscriber.onSuccess(((ScalarSynchronousSingle) single).value);
                    return;
                }
                Subscriber<R> subscriber = new Subscriber<R>() { // from class: rx.internal.util.ScalarSynchronousSingle.2.1
                    @Override // rx.Observer
                    public void onCompleted() {
                    }

                    @Override // rx.Observer
                    public void onError(Throwable th) {
                        singleSubscriber.onError(th);
                    }

                    @Override // rx.Observer
                    public void onNext(R r) {
                        singleSubscriber.onSuccess(r);
                    }
                };
                singleSubscriber.add(subscriber);
                single.unsafeSubscribe(subscriber);
            }
        });
    }
}
