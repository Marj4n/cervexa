package rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Producer;
import rx.Scheduler;
import rx.Subscriber;
import rx.exceptions.MissingBackpressureException;
import rx.functions.Action0;
import rx.internal.schedulers.ImmediateScheduler;
import rx.internal.schedulers.TrampolineScheduler;
import rx.internal.util.RxRingBuffer;
import rx.internal.util.atomic.SpscAtomicArrayQueue;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;

/* loaded from: classes2.dex */
public final class OperatorObserveOn<T> implements Observable.Operator<T, T> {
    private final int bufferSize;
    private final boolean delayError;
    private final Scheduler scheduler;

    public OperatorObserveOn(Scheduler scheduler, boolean z) {
        this(scheduler, z, RxRingBuffer.SIZE);
    }

    public OperatorObserveOn(Scheduler scheduler, boolean z, int i) {
        this.scheduler = scheduler;
        this.delayError = z;
        this.bufferSize = i <= 0 ? RxRingBuffer.SIZE : i;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Scheduler scheduler = this.scheduler;
        if ((scheduler instanceof ImmediateScheduler) || (scheduler instanceof TrampolineScheduler)) {
            return subscriber;
        }
        ObserveOnSubscriber observeOnSubscriber = new ObserveOnSubscriber(scheduler, subscriber, this.delayError, this.bufferSize);
        observeOnSubscriber.init();
        return observeOnSubscriber;
    }

    public static <T> Observable.Operator<T, T> rebatch(final int i) {
        return new Observable.Operator<T, T>() { // from class: rx.internal.operators.OperatorObserveOn.1
            @Override // rx.functions.Func1
            public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
                ObserveOnSubscriber observeOnSubscriber = new ObserveOnSubscriber(Schedulers.immediate(), subscriber, false, i);
                observeOnSubscriber.init();
                return observeOnSubscriber;
            }
        };
    }

    private static final class ObserveOnSubscriber<T> extends Subscriber<T> implements Action0 {
        final Subscriber<? super T> child;
        final boolean delayError;
        long emitted;
        Throwable error;
        volatile boolean finished;
        final int limit;
        final Queue<Object> queue;
        final Scheduler.Worker recursiveScheduler;
        final AtomicLong requested = new AtomicLong();
        final AtomicLong counter = new AtomicLong();
        final NotificationLite<T> on = NotificationLite.instance();

        public ObserveOnSubscriber(Scheduler scheduler, Subscriber<? super T> subscriber, boolean z, int i) {
            this.child = subscriber;
            this.recursiveScheduler = scheduler.createWorker();
            this.delayError = z;
            i = i <= 0 ? RxRingBuffer.SIZE : i;
            this.limit = i - (i >> 2);
            if (UnsafeAccess.isUnsafeAvailable()) {
                this.queue = new SpscArrayQueue(i);
            } else {
                this.queue = new SpscAtomicArrayQueue(i);
            }
            request(i);
        }

        void init() {
            Subscriber<? super T> subscriber = this.child;
            subscriber.setProducer(new Producer() { // from class: rx.internal.operators.OperatorObserveOn.ObserveOnSubscriber.1
                @Override // rx.Producer
                public void request(long j) {
                    if (j > 0) {
                        BackpressureUtils.getAndAddRequest(ObserveOnSubscriber.this.requested, j);
                        ObserveOnSubscriber.this.schedule();
                    }
                }
            });
            subscriber.add(this.recursiveScheduler);
            subscriber.add(this);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            if (isUnsubscribed() || this.finished) {
                return;
            }
            if (!this.queue.offer(this.on.next(t))) {
                onError(new MissingBackpressureException());
            } else {
                schedule();
            }
        }

        @Override // rx.Observer
        public void onCompleted() {
            if (isUnsubscribed() || this.finished) {
                return;
            }
            this.finished = true;
            schedule();
        }

        @Override // rx.Observer
        public void onError(Throwable th) {
            if (isUnsubscribed() || this.finished) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
                return;
            }
            this.error = th;
            this.finished = true;
            schedule();
        }

        protected void schedule() {
            if (this.counter.getAndIncrement() == 0) {
                this.recursiveScheduler.schedule(this);
            }
        }

        @Override // rx.functions.Action0
        public void call() {
            long j = this.emitted;
            Queue<Object> queue = this.queue;
            Subscriber<? super T> subscriber = this.child;
            NotificationLite<T> notificationLite = this.on;
            long j2 = 1;
            do {
                long j3 = this.requested.get();
                while (j3 != j) {
                    boolean z = this.finished;
                    Object poll = queue.poll();
                    boolean z2 = poll == null;
                    if (checkTerminated(z, z2, subscriber, queue)) {
                        return;
                    }
                    if (z2) {
                        break;
                    }
                    subscriber.onNext(notificationLite.getValue(poll));
                    j++;
                    if (j == this.limit) {
                        j3 = BackpressureUtils.produced(this.requested, j);
                        request(j);
                        j = 0;
                    }
                }
                if (j3 == j && checkTerminated(this.finished, queue.isEmpty(), subscriber, queue)) {
                    return;
                }
                this.emitted = j;
                j2 = this.counter.addAndGet(-j2);
            } while (j2 != 0);
        }

        boolean checkTerminated(boolean z, boolean z2, Subscriber<? super T> subscriber, Queue<Object> queue) {
            if (subscriber.isUnsubscribed()) {
                queue.clear();
                return true;
            }
            if (!z) {
                return false;
            }
            if (this.delayError) {
                if (!z2) {
                    return false;
                }
                Throwable th = this.error;
                try {
                    if (th != null) {
                        subscriber.onError(th);
                    } else {
                        subscriber.onCompleted();
                    }
                    return false;
                } finally {
                }
            }
            Throwable th2 = this.error;
            if (th2 != null) {
                queue.clear();
                try {
                    subscriber.onError(th2);
                    return true;
                } finally {
                }
            }
            if (!z2) {
                return false;
            }
            try {
                subscriber.onCompleted();
                return true;
            } finally {
            }
        }
    }
}
