package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;

/* loaded from: classes2.dex */
public enum NeverObservableHolder implements Observable.OnSubscribe<Object> {
    INSTANCE;

    static final Observable<Object> NEVER = Observable.create(INSTANCE);

    @Override // rx.functions.Action1
    public void call(Subscriber<? super Object> subscriber) {
    }

    public static <T> Observable<T> instance() {
        return (Observable<T>) NEVER;
    }
}
