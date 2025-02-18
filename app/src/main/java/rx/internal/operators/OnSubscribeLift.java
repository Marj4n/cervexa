package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.plugins.RxJavaObservableExecutionHook;
import rx.plugins.RxJavaPlugins;

/* loaded from: classes2.dex */
public final class OnSubscribeLift<T, R> implements Observable.OnSubscribe<R> {
    static final RxJavaObservableExecutionHook hook = RxJavaPlugins.getInstance().getObservableExecutionHook();
    final Observable.Operator<? extends R, ? super T> operator;
    final Observable.OnSubscribe<T> parent;

    public OnSubscribeLift(Observable.OnSubscribe<T> onSubscribe, Observable.Operator<? extends R, ? super T> operator) {
        this.parent = onSubscribe;
        this.operator = operator;
    }

    @Override // rx.functions.Action1
    public void call(Subscriber<? super R> subscriber) {
        try {
            Subscriber subscriber2 = (Subscriber) hook.onLift(this.operator).call(subscriber);
            try {
                subscriber2.onStart();
                this.parent.call(subscriber2);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                subscriber2.onError(th);
            }
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            subscriber.onError(th2);
        }
    }
}
