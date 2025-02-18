package rx.internal.operators;

import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.internal.util.RxJavaPluginUtils;

/* loaded from: classes2.dex */
public final class OperatorMapPair<T, U, R> implements Observable.Operator<Observable<? extends R>, T> {
    final Func1<? super T, ? extends Observable<? extends U>> collectionSelector;
    final Func2<? super T, ? super U, ? extends R> resultSelector;

    public static <T, U> Func1<T, Observable<U>> convertSelector(final Func1<? super T, ? extends Iterable<? extends U>> func1) {
        return new Func1<T, Observable<U>>() { // from class: rx.internal.operators.OperatorMapPair.1
            @Override // rx.functions.Func1
            public /* bridge */ /* synthetic */ Object call(Object obj) {
                return call((AnonymousClass1) obj);
            }

            @Override // rx.functions.Func1
            public Observable<U> call(T t) {
                return Observable.from((Iterable) Func1.this.call(t));
            }
        };
    }

    public OperatorMapPair(Func1<? super T, ? extends Observable<? extends U>> func1, Func2<? super T, ? super U, ? extends R> func2) {
        this.collectionSelector = func1;
        this.resultSelector = func2;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super Observable<? extends R>> subscriber) {
        MapPairSubscriber mapPairSubscriber = new MapPairSubscriber(subscriber, this.collectionSelector, this.resultSelector);
        subscriber.add(mapPairSubscriber);
        return mapPairSubscriber;
    }

    static final class MapPairSubscriber<T, U, R> extends Subscriber<T> {
        final Subscriber<? super Observable<? extends R>> actual;
        final Func1<? super T, ? extends Observable<? extends U>> collectionSelector;
        boolean done;
        final Func2<? super T, ? super U, ? extends R> resultSelector;

        public MapPairSubscriber(Subscriber<? super Observable<? extends R>> subscriber, Func1<? super T, ? extends Observable<? extends U>> func1, Func2<? super T, ? super U, ? extends R> func2) {
            this.actual = subscriber;
            this.collectionSelector = func1;
            this.resultSelector = func2;
        }

        @Override // rx.Observer
        public void onNext(T t) {
            try {
                this.actual.onNext(this.collectionSelector.call(t).map(new OuterInnerMapper(t, this.resultSelector)));
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                unsubscribe();
                onError(OnErrorThrowable.addValueAsLastCause(th, t));
            }
        }

        @Override // rx.Observer
        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPluginUtils.handleException(th);
            } else {
                this.done = true;
                this.actual.onError(th);
            }
        }

        @Override // rx.Observer
        public void onCompleted() {
            if (this.done) {
                return;
            }
            this.actual.onCompleted();
        }

        @Override // rx.Subscriber
        public void setProducer(Producer producer) {
            this.actual.setProducer(producer);
        }
    }

    static final class OuterInnerMapper<T, U, R> implements Func1<U, R> {
        final T outer;
        final Func2<? super T, ? super U, ? extends R> resultSelector;

        public OuterInnerMapper(T t, Func2<? super T, ? super U, ? extends R> func2) {
            this.outer = t;
            this.resultSelector = func2;
        }

        @Override // rx.functions.Func1
        public R call(U u) {
            return this.resultSelector.call(this.outer, u);
        }
    }
}
