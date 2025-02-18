package rx.subjects;

import java.util.ArrayList;
import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.internal.operators.NotificationLite;
import rx.internal.producers.SingleProducer;
import rx.subjects.SubjectSubscriptionManager;

/* loaded from: classes2.dex */
public final class AsyncSubject<T> extends Subject<T, T> {
    volatile Object lastValue;
    private final NotificationLite<T> nl;
    final SubjectSubscriptionManager<T> state;

    public static <T> AsyncSubject<T> create() {
        final SubjectSubscriptionManager subjectSubscriptionManager = new SubjectSubscriptionManager();
        subjectSubscriptionManager.onTerminated = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() { // from class: rx.subjects.AsyncSubject.1
            @Override // rx.functions.Action1
            public void call(SubjectSubscriptionManager.SubjectObserver<T> subjectObserver) {
                Object latest = SubjectSubscriptionManager.this.getLatest();
                NotificationLite<T> notificationLite = SubjectSubscriptionManager.this.nl;
                if (latest == null || notificationLite.isCompleted(latest)) {
                    subjectObserver.onCompleted();
                } else if (notificationLite.isError(latest)) {
                    subjectObserver.onError(notificationLite.getError(latest));
                } else {
                    subjectObserver.actual.setProducer(new SingleProducer(subjectObserver.actual, notificationLite.getValue(latest)));
                }
            }
        };
        return new AsyncSubject<>(subjectSubscriptionManager, subjectSubscriptionManager);
    }

    protected AsyncSubject(Observable.OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> subjectSubscriptionManager) {
        super(onSubscribe);
        this.nl = NotificationLite.instance();
        this.state = subjectSubscriptionManager;
    }

    @Override // rx.Observer
    public void onCompleted() {
        if (this.state.active) {
            Object obj = this.lastValue;
            if (obj == null) {
                obj = this.nl.completed();
            }
            for (SubjectSubscriptionManager.SubjectObserver<T> subjectObserver : this.state.terminate(obj)) {
                if (obj == this.nl.completed()) {
                    subjectObserver.onCompleted();
                } else {
                    subjectObserver.actual.setProducer(new SingleProducer(subjectObserver.actual, this.nl.getValue(obj)));
                }
            }
        }
    }

    @Override // rx.Observer
    public void onError(Throwable th) {
        if (this.state.active) {
            ArrayList arrayList = null;
            for (SubjectSubscriptionManager.SubjectObserver<T> subjectObserver : this.state.terminate(this.nl.error(th))) {
                try {
                    subjectObserver.onError(th);
                } catch (Throwable th2) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(th2);
                }
            }
            Exceptions.throwIfAny(arrayList);
        }
    }

    @Override // rx.Observer
    public void onNext(T t) {
        this.lastValue = this.nl.next(t);
    }

    @Override // rx.subjects.Subject
    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }

    public boolean hasValue() {
        return !this.nl.isError(this.state.getLatest()) && this.nl.isNext(this.lastValue);
    }

    public boolean hasThrowable() {
        return this.nl.isError(this.state.getLatest());
    }

    public boolean hasCompleted() {
        Object latest = this.state.getLatest();
        return (latest == null || this.nl.isError(latest)) ? false : true;
    }

    public T getValue() {
        Object obj = this.lastValue;
        if (this.nl.isError(this.state.getLatest()) || !this.nl.isNext(obj)) {
            return null;
        }
        return this.nl.getValue(obj);
    }

    public Throwable getThrowable() {
        Object latest = this.state.getLatest();
        if (this.nl.isError(latest)) {
            return this.nl.getError(latest);
        }
        return null;
    }
}
