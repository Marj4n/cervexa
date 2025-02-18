package rx.internal.operators;

import java.util.concurrent.atomic.AtomicInteger;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;

/* loaded from: classes2.dex */
public class OperatorTakeLastOne<T> implements Observable.Operator<T, T> {

    private static class Holder {
        static final OperatorTakeLastOne<Object> INSTANCE = new OperatorTakeLastOne<>();

        private Holder() {
        }
    }

    public static <T> OperatorTakeLastOne<T> instance() {
        return (OperatorTakeLastOne<T>) Holder.INSTANCE;
    }

    OperatorTakeLastOne() {
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        final ParentSubscriber parentSubscriber = new ParentSubscriber(subscriber);
        subscriber.setProducer(new Producer() { // from class: rx.internal.operators.OperatorTakeLastOne.1
            @Override // rx.Producer
            public void request(long j) {
                parentSubscriber.requestMore(j);
            }
        });
        subscriber.add(parentSubscriber);
        return parentSubscriber;
    }

    private static class ParentSubscriber<T> extends Subscriber<T> {
        private static final Object ABSENT = new Object();
        private static final int NOT_REQUESTED_COMPLETED = 1;
        private static final int NOT_REQUESTED_NOT_COMPLETED = 0;
        private static final int REQUESTED_COMPLETED = 3;
        private static final int REQUESTED_NOT_COMPLETED = 2;
        private final Subscriber<? super T> child;
        private T last = (T) ABSENT;
        private final AtomicInteger state = new AtomicInteger(0);

        ParentSubscriber(Subscriber<? super T> subscriber) {
            this.child = subscriber;
        }

        void requestMore(long j) {
            if (j <= 0) {
                return;
            }
            while (true) {
                int i = this.state.get();
                if (i == 0) {
                    if (this.state.compareAndSet(0, 2)) {
                        return;
                    }
                } else {
                    if (i != 1) {
                        return;
                    }
                    if (this.state.compareAndSet(1, 3)) {
                        emit();
                        return;
                    }
                }
            }
        }

        @Override // rx.Observer
        public void onCompleted() {
            if (this.last == ABSENT) {
                this.child.onCompleted();
                return;
            }
            while (true) {
                int i = this.state.get();
                if (i == 0) {
                    if (this.state.compareAndSet(0, 1)) {
                        return;
                    }
                } else {
                    if (i != 2) {
                        return;
                    }
                    if (this.state.compareAndSet(2, 3)) {
                        emit();
                        return;
                    }
                }
            }
        }

        private void emit() {
            if (isUnsubscribed()) {
                this.last = null;
                return;
            }
            T t = this.last;
            this.last = null;
            if (t != ABSENT) {
                try {
                    this.child.onNext(t);
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, this.child);
                    return;
                }
            }
            if (isUnsubscribed()) {
                return;
            }
            this.child.onCompleted();
        }

        @Override // rx.Observer
        public void onError(Throwable th) {
            this.child.onError(th);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            this.last = t;
        }
    }
}
