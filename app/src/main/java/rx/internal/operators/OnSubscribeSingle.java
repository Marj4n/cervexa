package rx.internal.operators;

import java.util.NoSuchElementException;
import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;

/* loaded from: classes2.dex */
public class OnSubscribeSingle<T> implements Single.OnSubscribe<T> {
    private final Observable<T> observable;

    public OnSubscribeSingle(Observable<T> observable) {
        this.observable = observable;
    }

    @Override // rx.functions.Action1
    public void call(final SingleSubscriber<? super T> singleSubscriber) {
        Subscriber<T> subscriber = new Subscriber<T>() { // from class: rx.internal.operators.OnSubscribeSingle.1
            private boolean emittedTooMany = false;
            private boolean itemEmitted = false;
            private T emission = null;

            @Override // rx.Subscriber
            public void onStart() {
                request(2L);
            }

            @Override // rx.Observer
            public void onCompleted() {
                if (this.emittedTooMany) {
                    return;
                }
                if (this.itemEmitted) {
                    singleSubscriber.onSuccess(this.emission);
                } else {
                    singleSubscriber.onError(new NoSuchElementException("Observable emitted no items"));
                }
            }

            @Override // rx.Observer
            public void onError(Throwable th) {
                singleSubscriber.onError(th);
                unsubscribe();
            }

            @Override // rx.Observer
            public void onNext(T t) {
                if (this.itemEmitted) {
                    this.emittedTooMany = true;
                    singleSubscriber.onError(new IllegalArgumentException("Observable emitted too many elements"));
                    unsubscribe();
                } else {
                    this.itemEmitted = true;
                    this.emission = t;
                }
            }
        };
        singleSubscriber.add(subscriber);
        this.observable.unsafeSubscribe(subscriber);
    }

    public static <T> OnSubscribeSingle<T> create(Observable<T> observable) {
        return new OnSubscribeSingle<>(observable);
    }
}
