package rx.observers;

import rx.Completable;
import rx.Subscription;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.exceptions.OnCompletedFailedException;
import rx.exceptions.OnErrorFailedException;
import rx.internal.util.RxJavaPluginUtils;

/* loaded from: classes2.dex */
public final class SafeCompletableSubscriber implements Completable.CompletableSubscriber, Subscription {
    final Completable.CompletableSubscriber actual;
    boolean done;
    Subscription s;

    public SafeCompletableSubscriber(Completable.CompletableSubscriber completableSubscriber) {
        this.actual = completableSubscriber;
    }

    @Override // rx.Completable.CompletableSubscriber
    public void onCompleted() {
        if (this.done) {
            return;
        }
        this.done = true;
        try {
            this.actual.onCompleted();
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw new OnCompletedFailedException(th);
        }
    }

    @Override // rx.Completable.CompletableSubscriber
    public void onError(Throwable th) {
        RxJavaPluginUtils.handleException(th);
        if (this.done) {
            return;
        }
        this.done = true;
        try {
            this.actual.onError(th);
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            throw new OnErrorFailedException(new CompositeException(th, th2));
        }
    }

    @Override // rx.Completable.CompletableSubscriber
    public void onSubscribe(Subscription subscription) {
        this.s = subscription;
        try {
            this.actual.onSubscribe(this);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            subscription.unsubscribe();
            onError(th);
        }
    }

    @Override // rx.Subscription
    public void unsubscribe() {
        this.s.unsubscribe();
    }

    @Override // rx.Subscription
    public boolean isUnsubscribed() {
        return this.done || this.s.isUnsubscribed();
    }
}
