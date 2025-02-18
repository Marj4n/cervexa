package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import rx.Completable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.CompositeSubscription;

/* loaded from: classes2.dex */
public final class CompletableOnSubscribeTimeout implements Completable.CompletableOnSubscribe {
    final Completable other;
    final Scheduler scheduler;
    final Completable source;
    final long timeout;
    final TimeUnit unit;

    public CompletableOnSubscribeTimeout(Completable completable, long j, TimeUnit timeUnit, Scheduler scheduler, Completable completable2) {
        this.source = completable;
        this.timeout = j;
        this.unit = timeUnit;
        this.scheduler = scheduler;
        this.other = completable2;
    }

    @Override // rx.functions.Action1
    public void call(final Completable.CompletableSubscriber completableSubscriber) {
        final CompositeSubscription compositeSubscription = new CompositeSubscription();
        completableSubscriber.onSubscribe(compositeSubscription);
        final AtomicBoolean atomicBoolean = new AtomicBoolean();
        Scheduler.Worker createWorker = this.scheduler.createWorker();
        compositeSubscription.add(createWorker);
        createWorker.schedule(new Action0() { // from class: rx.internal.operators.CompletableOnSubscribeTimeout.1
            @Override // rx.functions.Action0
            public void call() {
                if (atomicBoolean.compareAndSet(false, true)) {
                    compositeSubscription.clear();
                    if (CompletableOnSubscribeTimeout.this.other == null) {
                        completableSubscriber.onError(new TimeoutException());
                    } else {
                        CompletableOnSubscribeTimeout.this.other.unsafeSubscribe(new Completable.CompletableSubscriber() { // from class: rx.internal.operators.CompletableOnSubscribeTimeout.1.1
                            @Override // rx.Completable.CompletableSubscriber
                            public void onSubscribe(Subscription subscription) {
                                compositeSubscription.add(subscription);
                            }

                            @Override // rx.Completable.CompletableSubscriber
                            public void onError(Throwable th) {
                                compositeSubscription.unsubscribe();
                                completableSubscriber.onError(th);
                            }

                            @Override // rx.Completable.CompletableSubscriber
                            public void onCompleted() {
                                compositeSubscription.unsubscribe();
                                completableSubscriber.onCompleted();
                            }
                        });
                    }
                }
            }
        }, this.timeout, this.unit);
        this.source.unsafeSubscribe(new Completable.CompletableSubscriber() { // from class: rx.internal.operators.CompletableOnSubscribeTimeout.2
            @Override // rx.Completable.CompletableSubscriber
            public void onSubscribe(Subscription subscription) {
                compositeSubscription.add(subscription);
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onError(Throwable th) {
                if (atomicBoolean.compareAndSet(false, true)) {
                    compositeSubscription.unsubscribe();
                    completableSubscriber.onError(th);
                } else {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
                }
            }

            @Override // rx.Completable.CompletableSubscriber
            public void onCompleted() {
                if (atomicBoolean.compareAndSet(false, true)) {
                    compositeSubscription.unsubscribe();
                    completableSubscriber.onCompleted();
                }
            }
        });
    }
}
