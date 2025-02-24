package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observers.Subscribers;

/* loaded from: classes2.dex */
public final class OnSubscribeUsing<T, Resource> implements Observable.OnSubscribe<T> {
    private final Action1<? super Resource> dispose;
    private final boolean disposeEagerly;
    private final Func1<? super Resource, ? extends Observable<? extends T>> observableFactory;
    private final Func0<Resource> resourceFactory;

    public OnSubscribeUsing(Func0<Resource> func0, Func1<? super Resource, ? extends Observable<? extends T>> func1, Action1<? super Resource> action1, boolean z) {
        this.resourceFactory = func0;
        this.observableFactory = func1;
        this.dispose = action1;
        this.disposeEagerly = z;
    }

    @Override // rx.functions.Action1
    public void call(Subscriber<? super T> subscriber) {
        Observable<? extends T> doAfterTerminate;
        try {
            Resource call = this.resourceFactory.call();
            DisposeAction disposeAction = new DisposeAction(this.dispose, call);
            subscriber.add(disposeAction);
            try {
                Observable<? extends T> call2 = this.observableFactory.call(call);
                if (this.disposeEagerly) {
                    doAfterTerminate = call2.doOnTerminate(disposeAction);
                } else {
                    doAfterTerminate = call2.doAfterTerminate(disposeAction);
                }
                try {
                    doAfterTerminate.unsafeSubscribe(Subscribers.wrap(subscriber));
                } catch (Throwable th) {
                    Throwable dispose = dispose(disposeAction);
                    Exceptions.throwIfFatal(th);
                    Exceptions.throwIfFatal(dispose);
                    if (dispose != null) {
                        subscriber.onError(new CompositeException(th, dispose));
                    } else {
                        subscriber.onError(th);
                    }
                }
            } catch (Throwable th2) {
                Throwable dispose2 = dispose(disposeAction);
                Exceptions.throwIfFatal(th2);
                Exceptions.throwIfFatal(dispose2);
                if (dispose2 != null) {
                    subscriber.onError(new CompositeException(th2, dispose2));
                } else {
                    subscriber.onError(th2);
                }
            }
        } catch (Throwable th3) {
            Exceptions.throwOrReport(th3, subscriber);
        }
    }

    private Throwable dispose(Action0 action0) {
        try {
            action0.call();
            return null;
        } catch (Throwable th) {
            return th;
        }
    }

    private static final class DisposeAction<Resource> extends AtomicBoolean implements Action0, Subscription {
        private static final long serialVersionUID = 4262875056400218316L;
        private Action1<? super Resource> dispose;
        private Resource resource;

        DisposeAction(Action1<? super Resource> action1, Resource resource) {
            this.dispose = action1;
            this.resource = resource;
            lazySet(false);
        }

        /* JADX WARN: Type inference failed for: r0v2, types: [Resource, rx.functions.Action1<? super Resource>] */
        @Override // rx.functions.Action0
        public void call() {
            if (compareAndSet(false, true)) {
                Resource r0 = (Resource) null;
                try {
                    this.dispose.call(this.resource);
                } finally {
                    this.resource = null;
                    this.dispose = null;
                }
            }
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return get();
        }

        @Override // rx.Subscription
        public void unsubscribe() {
            call();
        }
    }
}
