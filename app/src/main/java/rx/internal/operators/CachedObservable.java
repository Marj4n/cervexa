package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;
import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorThrowable;
import rx.internal.util.LinkedArrayList;
import rx.subscriptions.SerialSubscription;

/* loaded from: classes2.dex */
public final class CachedObservable<T> extends Observable<T> {
    private final CacheState<T> state;

    public static <T> CachedObservable<T> from(Observable<? extends T> observable) {
        return from(observable, 16);
    }

    public static <T> CachedObservable<T> from(Observable<? extends T> observable, int i) {
        if (i < 1) {
            throw new IllegalArgumentException("capacityHint > 0 required");
        }
        CacheState cacheState = new CacheState(observable, i);
        return new CachedObservable<>(new CachedSubscribe(cacheState), cacheState);
    }

    private CachedObservable(Observable.OnSubscribe<T> onSubscribe, CacheState<T> cacheState) {
        super(onSubscribe);
        this.state = cacheState;
    }

    boolean isConnected() {
        return this.state.isConnected;
    }

    boolean hasObservers() {
        return this.state.producers.length != 0;
    }

    static final class CacheState<T> extends LinkedArrayList implements Observer<T> {
        static final ReplayProducer<?>[] EMPTY = new ReplayProducer[0];
        final SerialSubscription connection;
        volatile boolean isConnected;
        final NotificationLite<T> nl;
        volatile ReplayProducer<?>[] producers;
        final Observable<? extends T> source;
        boolean sourceDone;

        public CacheState(Observable<? extends T> observable, int i) {
            super(i);
            this.source = observable;
            this.producers = EMPTY;
            this.nl = NotificationLite.instance();
            this.connection = new SerialSubscription();
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void addProducer(ReplayProducer<T> replayProducer) {
            synchronized (this.connection) {
                ReplayProducer<?>[] replayProducerArr = this.producers;
                int length = replayProducerArr.length;
                ReplayProducer<?>[] replayProducerArr2 = new ReplayProducer[length + 1];
                System.arraycopy(replayProducerArr, 0, replayProducerArr2, 0, length);
                replayProducerArr2[length] = replayProducer;
                this.producers = replayProducerArr2;
            }
        }

        public void removeProducer(ReplayProducer<T> replayProducer) {
            synchronized (this.connection) {
                ReplayProducer<?>[] replayProducerArr = this.producers;
                int length = replayProducerArr.length;
                int i = -1;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    if (replayProducerArr[i2].equals(replayProducer)) {
                        i = i2;
                        break;
                    }
                    i2++;
                }
                if (i < 0) {
                    return;
                }
                if (length == 1) {
                    this.producers = EMPTY;
                    return;
                }
                ReplayProducer<?>[] replayProducerArr2 = new ReplayProducer[length - 1];
                System.arraycopy(replayProducerArr, 0, replayProducerArr2, 0, i);
                System.arraycopy(replayProducerArr, i + 1, replayProducerArr2, i, (length - i) - 1);
                this.producers = replayProducerArr2;
            }
        }

        public void connect() {
            Subscriber<T> subscriber = new Subscriber<T>() { // from class: rx.internal.operators.CachedObservable.CacheState.1
                @Override // rx.Observer
                public void onNext(T t) {
                    CacheState.this.onNext(t);
                }

                @Override // rx.Observer
                public void onError(Throwable th) {
                    CacheState.this.onError(th);
                }

                @Override // rx.Observer
                public void onCompleted() {
                    CacheState.this.onCompleted();
                }
            };
            this.connection.set(subscriber);
            this.source.unsafeSubscribe(subscriber);
            this.isConnected = true;
        }

        @Override // rx.Observer
        public void onNext(T t) {
            if (this.sourceDone) {
                return;
            }
            add(this.nl.next(t));
            dispatch();
        }

        @Override // rx.Observer
        public void onError(Throwable th) {
            if (this.sourceDone) {
                return;
            }
            this.sourceDone = true;
            add(this.nl.error(th));
            this.connection.unsubscribe();
            dispatch();
        }

        @Override // rx.Observer
        public void onCompleted() {
            if (this.sourceDone) {
                return;
            }
            this.sourceDone = true;
            add(this.nl.completed());
            this.connection.unsubscribe();
            dispatch();
        }

        void dispatch() {
            for (ReplayProducer<?> replayProducer : this.producers) {
                replayProducer.replay();
            }
        }
    }

    static final class CachedSubscribe<T> extends AtomicBoolean implements Observable.OnSubscribe<T> {
        private static final long serialVersionUID = -2817751667698696782L;
        final CacheState<T> state;

        public CachedSubscribe(CacheState<T> cacheState) {
            this.state = cacheState;
        }

        @Override // rx.functions.Action1
        public void call(Subscriber<? super T> subscriber) {
            ReplayProducer<T> replayProducer = new ReplayProducer<>(subscriber, this.state);
            this.state.addProducer(replayProducer);
            subscriber.add(replayProducer);
            subscriber.setProducer(replayProducer);
            if (get() || !compareAndSet(false, true)) {
                return;
            }
            this.state.connect();
        }
    }

    static final class ReplayProducer<T> extends AtomicLong implements Producer, Subscription {
        private static final long serialVersionUID = -2557562030197141021L;
        final Subscriber<? super T> child;
        Object[] currentBuffer;
        int currentIndexInBuffer;
        boolean emitting;
        int index;
        boolean missed;
        final CacheState<T> state;

        public ReplayProducer(Subscriber<? super T> subscriber, CacheState<T> cacheState) {
            this.child = subscriber;
            this.state = cacheState;
        }

        @Override // rx.Producer
        public void request(long j) {
            long j2;
            long j3;
            do {
                j2 = get();
                if (j2 < 0) {
                    return;
                }
                j3 = j2 + j;
                if (j3 < 0) {
                    j3 = LongCompanionObject.MAX_VALUE;
                }
            } while (!compareAndSet(j2, j3));
            replay();
        }

        public long produced(long j) {
            return addAndGet(-j);
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return get() < 0;
        }

        @Override // rx.Subscription
        public void unsubscribe() {
            if (get() < 0 || getAndSet(-1L) < 0) {
                return;
            }
            this.state.removeProducer(this);
        }

        public void replay() {
            boolean z;
            synchronized (this) {
                boolean z2 = true;
                if (this.emitting) {
                    this.missed = true;
                    return;
                }
                this.emitting = true;
                try {
                    NotificationLite<T> notificationLite = this.state.nl;
                    Subscriber<? super T> subscriber = this.child;
                    while (true) {
                        long j = get();
                        if (j < 0) {
                            return;
                        }
                        int size = this.state.size();
                        try {
                            if (size != 0) {
                                Object[] objArr = this.currentBuffer;
                                if (objArr == null) {
                                    objArr = this.state.head();
                                    this.currentBuffer = objArr;
                                }
                                int length = objArr.length - 1;
                                int i = this.index;
                                int i2 = this.currentIndexInBuffer;
                                if (j == 0) {
                                    Object obj = objArr[i2];
                                    if (notificationLite.isCompleted(obj)) {
                                        subscriber.onCompleted();
                                        unsubscribe();
                                        return;
                                    } else if (notificationLite.isError(obj)) {
                                        subscriber.onError(notificationLite.getError(obj));
                                        unsubscribe();
                                        return;
                                    }
                                } else if (j > 0) {
                                    int i3 = 0;
                                    while (i < size && j > 0) {
                                        if (subscriber.isUnsubscribed()) {
                                            return;
                                        }
                                        if (i2 == length) {
                                            objArr = (Object[]) objArr[length];
                                            i2 = 0;
                                        }
                                        Object obj2 = objArr[i2];
                                        try {
                                            if (notificationLite.accept(subscriber, obj2)) {
                                                try {
                                                    unsubscribe();
                                                    return;
                                                } catch (Throwable th) {
                                                    th = th;
                                                    z = true;
                                                    try {
                                                        Exceptions.throwIfFatal(th);
                                                        unsubscribe();
                                                        if (notificationLite.isError(obj2) || notificationLite.isCompleted(obj2)) {
                                                            return;
                                                        }
                                                        subscriber.onError(OnErrorThrowable.addValueAsLastCause(th, notificationLite.getValue(obj2)));
                                                        return;
                                                    } catch (Throwable th2) {
                                                        th = th2;
                                                        z2 = z;
                                                        if (!z2) {
                                                            synchronized (this) {
                                                                this.emitting = false;
                                                            }
                                                        }
                                                        throw th;
                                                    }
                                                }
                                            }
                                            i2++;
                                            i++;
                                            j--;
                                            i3++;
                                        } catch (Throwable th3) {
                                            th = th3;
                                            z = false;
                                        }
                                    }
                                    if (subscriber.isUnsubscribed()) {
                                        return;
                                    }
                                    this.index = i;
                                    this.currentIndexInBuffer = i2;
                                    this.currentBuffer = objArr;
                                    produced(i3);
                                }
                            }
                            try {
                                synchronized (this) {
                                    try {
                                        if (!this.missed) {
                                            this.emitting = false;
                                            return;
                                        }
                                        this.missed = false;
                                    } catch (Throwable th4) {
                                        th = th4;
                                        throw th;
                                    }
                                }
                            } catch (Throwable th5) {
                                th = th5;
                            }
                        } catch (Throwable th6) {
                            th = th6;
                        }
                    }
                } catch (Throwable th7) {
                    th = th7;
                    z2 = false;
                }
            }
        }
    }
}
