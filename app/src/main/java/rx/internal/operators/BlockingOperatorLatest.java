package rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

/* loaded from: classes2.dex */
public final class BlockingOperatorLatest {
    private BlockingOperatorLatest() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> latest(final Observable<? extends T> observable) {
        return new Iterable<T>() { // from class: rx.internal.operators.BlockingOperatorLatest.1
            @Override // java.lang.Iterable
            public Iterator<T> iterator() {
                LatestObserverIterator latestObserverIterator = new LatestObserverIterator();
                Observable.this.materialize().subscribe((Subscriber<? super Notification<T>>) latestObserverIterator);
                return latestObserverIterator;
            }
        };
    }

    static final class LatestObserverIterator<T> extends Subscriber<Notification<? extends T>> implements Iterator<T> {
        Notification<? extends T> iNotif;
        final Semaphore notify = new Semaphore(0);
        final AtomicReference<Notification<? extends T>> value = new AtomicReference<>();

        @Override // rx.Observer
        public void onCompleted() {
        }

        @Override // rx.Observer
        public void onError(Throwable th) {
        }

        LatestObserverIterator() {
        }

        @Override // rx.Observer
        public void onNext(Notification<? extends T> notification) {
            if (this.value.getAndSet(notification) == null) {
                this.notify.release();
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            Notification<? extends T> notification = this.iNotif;
            if (notification != null && notification.isOnError()) {
                throw Exceptions.propagate(this.iNotif.getThrowable());
            }
            Notification<? extends T> notification2 = this.iNotif;
            if ((notification2 == null || !notification2.isOnCompleted()) && this.iNotif == null) {
                try {
                    this.notify.acquire();
                    Notification<? extends T> andSet = this.value.getAndSet(null);
                    this.iNotif = andSet;
                    if (andSet.isOnError()) {
                        throw Exceptions.propagate(this.iNotif.getThrowable());
                    }
                } catch (InterruptedException e) {
                    unsubscribe();
                    Thread.currentThread().interrupt();
                    this.iNotif = Notification.createOnError(e);
                    throw Exceptions.propagate(e);
                }
            }
            return !this.iNotif.isOnCompleted();
        }

        @Override // java.util.Iterator
        public T next() {
            if (hasNext() && this.iNotif.isOnNext()) {
                T value = this.iNotif.getValue();
                this.iNotif = null;
                return value;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Read-only iterator.");
        }
    }
}
