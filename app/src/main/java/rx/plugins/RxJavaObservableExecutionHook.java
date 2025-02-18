package rx.plugins;

import rx.Observable;
import rx.Subscription;

/* loaded from: classes2.dex */
public abstract class RxJavaObservableExecutionHook {
    public <T> Observable.OnSubscribe<T> onCreate(Observable.OnSubscribe<T> onSubscribe) {
        return onSubscribe;
    }

    public <T, R> Observable.Operator<? extends R, ? super T> onLift(Observable.Operator<? extends R, ? super T> operator) {
        return operator;
    }

    public <T> Throwable onSubscribeError(Throwable th) {
        return th;
    }

    public <T> Subscription onSubscribeReturn(Subscription subscription) {
        return subscription;
    }

    public <T> Observable.OnSubscribe<T> onSubscribeStart(Observable<? extends T> observable, Observable.OnSubscribe<T> onSubscribe) {
        return onSubscribe;
    }
}
