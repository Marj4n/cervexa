package rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

/* loaded from: classes2.dex */
public final class BlockingOperatorMostRecent {
    private BlockingOperatorMostRecent() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> mostRecent(final Observable<? extends T> observable, final T t) {
        return new Iterable<T>() { // from class: rx.internal.operators.BlockingOperatorMostRecent.1
            @Override // java.lang.Iterable
            public Iterator<T> iterator() {
                MostRecentObserver mostRecentObserver = new MostRecentObserver(t);
                observable.subscribe((Subscriber) mostRecentObserver);
                return mostRecentObserver.getIterable();
            }
        };
    }

    private static final class MostRecentObserver<T> extends Subscriber<T> {
        final NotificationLite<T> nl;
        volatile Object value;

        MostRecentObserver(T t) {
            NotificationLite<T> instance = NotificationLite.instance();
            this.nl = instance;
            this.value = instance.next(t);
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.value = this.nl.completed();
        }

        @Override // rx.Observer
        public void onError(Throwable th) {
            this.value = this.nl.error(th);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            this.value = this.nl.next(t);
        }

        public Iterator<T> getIterable() {
            return new Iterator<T>() { // from class: rx.internal.operators.BlockingOperatorMostRecent.MostRecentObserver.1
                private Object buf = null;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    this.buf = MostRecentObserver.this.value;
                    return !MostRecentObserver.this.nl.isCompleted(this.buf);
                }

                @Override // java.util.Iterator
                public T next() {
                    try {
                        if (this.buf == null) {
                            this.buf = MostRecentObserver.this.value;
                        }
                        if (MostRecentObserver.this.nl.isCompleted(this.buf)) {
                            throw new NoSuchElementException();
                        }
                        if (MostRecentObserver.this.nl.isError(this.buf)) {
                            throw Exceptions.propagate(MostRecentObserver.this.nl.getError(this.buf));
                        }
                        return MostRecentObserver.this.nl.getValue(this.buf);
                    } finally {
                        this.buf = null;
                    }
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException("Read only iterator");
                }
            };
        }
    }
}
