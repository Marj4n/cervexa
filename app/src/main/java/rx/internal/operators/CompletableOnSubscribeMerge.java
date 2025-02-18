package rx.internal.operators;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import rx.Completable;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.CompositeException;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.CompositeSubscription;

/* loaded from: classes2.dex */
public final class CompletableOnSubscribeMerge implements Completable.CompletableOnSubscribe {
    final boolean delayErrors;
    final int maxConcurrency;
    final Observable<Completable> source;

    /* JADX WARN: Multi-variable type inference failed */
    public CompletableOnSubscribeMerge(Observable<? extends Completable> observable, int i, boolean z) {
        this.source = observable;
        this.maxConcurrency = i;
        this.delayErrors = z;
    }

    @Override // rx.functions.Action1
    public void call(Completable.CompletableSubscriber completableSubscriber) {
        CompletableMergeSubscriber completableMergeSubscriber = new CompletableMergeSubscriber(completableSubscriber, this.maxConcurrency, this.delayErrors);
        completableSubscriber.onSubscribe(completableMergeSubscriber);
        this.source.subscribe((Subscriber<? super Completable>) completableMergeSubscriber);
    }

    static final class CompletableMergeSubscriber extends Subscriber<Completable> {
        final Completable.CompletableSubscriber actual;
        final boolean delayErrors;
        volatile boolean done;
        final int maxConcurrency;
        final CompositeSubscription set = new CompositeSubscription();
        final AtomicInteger wip = new AtomicInteger(1);
        final AtomicBoolean once = new AtomicBoolean();
        final AtomicReference<Queue<Throwable>> errors = new AtomicReference<>();

        public CompletableMergeSubscriber(Completable.CompletableSubscriber completableSubscriber, int i, boolean z) {
            this.actual = completableSubscriber;
            this.maxConcurrency = i;
            this.delayErrors = z;
            if (i == Integer.MAX_VALUE) {
                request(LongCompanionObject.MAX_VALUE);
            } else {
                request(i);
            }
        }

        Queue<Throwable> getOrCreateErrors() {
            Queue<Throwable> queue = this.errors.get();
            if (queue != null) {
                return queue;
            }
            ConcurrentLinkedQueue concurrentLinkedQueue = new ConcurrentLinkedQueue();
            return this.errors.compareAndSet(null, concurrentLinkedQueue) ? concurrentLinkedQueue : this.errors.get();
        }

        @Override // rx.Observer
        public void onNext(Completable completable) {
            if (this.done) {
                return;
            }
            this.wip.getAndIncrement();
            completable.unsafeSubscribe(new Completable.CompletableSubscriber() { // from class: rx.internal.operators.CompletableOnSubscribeMerge.CompletableMergeSubscriber.1
                Subscription d;
                boolean innerDone;

                @Override // rx.Completable.CompletableSubscriber
                public void onSubscribe(Subscription subscription) {
                    this.d = subscription;
                    CompletableMergeSubscriber.this.set.add(subscription);
                }

                @Override // rx.Completable.CompletableSubscriber
                public void onError(Throwable th) {
                    if (this.innerDone) {
                        RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
                        return;
                    }
                    this.innerDone = true;
                    CompletableMergeSubscriber.this.set.remove(this.d);
                    CompletableMergeSubscriber.this.getOrCreateErrors().offer(th);
                    CompletableMergeSubscriber.this.terminate();
                    if (!CompletableMergeSubscriber.this.delayErrors || CompletableMergeSubscriber.this.done) {
                        return;
                    }
                    CompletableMergeSubscriber.this.request(1L);
                }

                @Override // rx.Completable.CompletableSubscriber
                public void onCompleted() {
                    if (this.innerDone) {
                        return;
                    }
                    this.innerDone = true;
                    CompletableMergeSubscriber.this.set.remove(this.d);
                    CompletableMergeSubscriber.this.terminate();
                    if (CompletableMergeSubscriber.this.done) {
                        return;
                    }
                    CompletableMergeSubscriber.this.request(1L);
                }
            });
        }

        @Override // rx.Observer
        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
                return;
            }
            getOrCreateErrors().offer(th);
            this.done = true;
            terminate();
        }

        @Override // rx.Observer
        public void onCompleted() {
            if (this.done) {
                return;
            }
            this.done = true;
            terminate();
        }

        void terminate() {
            Queue<Throwable> queue;
            if (this.wip.decrementAndGet() == 0) {
                Queue<Throwable> queue2 = this.errors.get();
                if (queue2 == null || queue2.isEmpty()) {
                    this.actual.onCompleted();
                    return;
                }
                Throwable collectErrors = CompletableOnSubscribeMerge.collectErrors(queue2);
                if (this.once.compareAndSet(false, true)) {
                    this.actual.onError(collectErrors);
                    return;
                } else {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(collectErrors);
                    return;
                }
            }
            if (this.delayErrors || (queue = this.errors.get()) == null || queue.isEmpty()) {
                return;
            }
            Throwable collectErrors2 = CompletableOnSubscribeMerge.collectErrors(queue);
            if (this.once.compareAndSet(false, true)) {
                this.actual.onError(collectErrors2);
            } else {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(collectErrors2);
            }
        }
    }

    public static Throwable collectErrors(Queue<Throwable> queue) {
        ArrayList arrayList = new ArrayList();
        while (true) {
            Throwable poll = queue.poll();
            if (poll == null) {
                break;
            }
            arrayList.add(poll);
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        if (arrayList.size() == 1) {
            return (Throwable) arrayList.get(0);
        }
        return new CompositeException(arrayList);
    }
}
