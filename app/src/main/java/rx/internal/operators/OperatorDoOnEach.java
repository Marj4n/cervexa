package rx.internal.operators;

import java.util.Arrays;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;

/* loaded from: classes2.dex */
public class OperatorDoOnEach<T> implements Observable.Operator<T, T> {
    final Observer<? super T> doOnEachObserver;

    public OperatorDoOnEach(Observer<? super T> observer) {
        this.doOnEachObserver = observer;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorDoOnEach.1
            private boolean done = false;

            @Override // rx.Observer
            public void onCompleted() {
                if (this.done) {
                    return;
                }
                try {
                    OperatorDoOnEach.this.doOnEachObserver.onCompleted();
                    this.done = true;
                    subscriber.onCompleted();
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, this);
                }
            }

            @Override // rx.Observer
            public void onError(Throwable th) {
                Exceptions.throwIfFatal(th);
                if (this.done) {
                    return;
                }
                this.done = true;
                try {
                    OperatorDoOnEach.this.doOnEachObserver.onError(th);
                    subscriber.onError(th);
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    subscriber.onError(new CompositeException(Arrays.asList(th, th2)));
                }
            }

            @Override // rx.Observer
            public void onNext(T t) {
                if (this.done) {
                    return;
                }
                try {
                    OperatorDoOnEach.this.doOnEachObserver.onNext(t);
                    subscriber.onNext(t);
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, this, t);
                }
            }
        };
    }
}
