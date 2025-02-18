package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.plugins.RxJavaPlugins;

/* loaded from: classes2.dex */
public final class OperatorDoAfterTerminate<T> implements Observable.Operator<T, T> {
    final Action0 action;

    public OperatorDoAfterTerminate(Action0 action0) {
        if (action0 == null) {
            throw new NullPointerException("Action can not be null");
        }
        this.action = action0;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorDoAfterTerminate.1
            @Override // rx.Observer
            public void onNext(T t) {
                subscriber.onNext(t);
            }

            @Override // rx.Observer
            public void onError(Throwable th) {
                try {
                    subscriber.onError(th);
                } finally {
                    callAction();
                }
            }

            @Override // rx.Observer
            public void onCompleted() {
                try {
                    subscriber.onCompleted();
                } finally {
                    callAction();
                }
            }

            void callAction() {
                try {
                    OperatorDoAfterTerminate.this.action.call();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
                }
            }
        };
    }
}
