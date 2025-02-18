package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.functions.Func2;

/* loaded from: classes2.dex */
public final class OperatorTakeWhile<T> implements Observable.Operator<T, T> {
    final Func2<? super T, ? super Integer, Boolean> predicate;

    public OperatorTakeWhile(final Func1<? super T, Boolean> func1) {
        this(new Func2<T, Integer, Boolean>() { // from class: rx.internal.operators.OperatorTakeWhile.1
            @Override // rx.functions.Func2
            public /* bridge */ /* synthetic */ Boolean call(Object obj, Integer num) {
                return call2((AnonymousClass1) obj, num);
            }

            /* renamed from: call, reason: avoid collision after fix types in other method */
            public Boolean call2(T t, Integer num) {
                return (Boolean) Func1.this.call(t);
            }
        });
    }

    public OperatorTakeWhile(Func2<? super T, ? super Integer, Boolean> func2) {
        this.predicate = func2;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        Subscriber<T> subscriber2 = new Subscriber<T>(subscriber, false) { // from class: rx.internal.operators.OperatorTakeWhile.2
            private int counter = 0;
            private boolean done = false;

            @Override // rx.Observer
            public void onNext(T t) {
                try {
                    Func2<? super T, ? super Integer, Boolean> func2 = OperatorTakeWhile.this.predicate;
                    int i = this.counter;
                    this.counter = i + 1;
                    if (func2.call(t, Integer.valueOf(i)).booleanValue()) {
                        subscriber.onNext(t);
                        return;
                    }
                    this.done = true;
                    subscriber.onCompleted();
                    unsubscribe();
                } catch (Throwable th) {
                    this.done = true;
                    Exceptions.throwOrReport(th, subscriber, t);
                    unsubscribe();
                }
            }

            @Override // rx.Observer
            public void onCompleted() {
                if (this.done) {
                    return;
                }
                subscriber.onCompleted();
            }

            @Override // rx.Observer
            public void onError(Throwable th) {
                if (this.done) {
                    return;
                }
                subscriber.onError(th);
            }
        };
        subscriber.add(subscriber2);
        return subscriber2;
    }
}
