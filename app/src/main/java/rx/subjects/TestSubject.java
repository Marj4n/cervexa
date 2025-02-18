package rx.subjects;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.internal.operators.NotificationLite;
import rx.schedulers.TestScheduler;
import rx.subjects.SubjectSubscriptionManager;

/* loaded from: classes2.dex */
public final class TestSubject<T> extends Subject<T, T> {
    private final Scheduler.Worker innerScheduler;
    private final SubjectSubscriptionManager<T> state;

    public static <T> TestSubject<T> create(TestScheduler testScheduler) {
        final SubjectSubscriptionManager subjectSubscriptionManager = new SubjectSubscriptionManager();
        subjectSubscriptionManager.onAdded = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() { // from class: rx.subjects.TestSubject.1
            @Override // rx.functions.Action1
            public void call(SubjectSubscriptionManager.SubjectObserver<T> subjectObserver) {
                subjectObserver.emitFirst(SubjectSubscriptionManager.this.getLatest(), SubjectSubscriptionManager.this.nl);
            }
        };
        subjectSubscriptionManager.onTerminated = subjectSubscriptionManager.onAdded;
        return new TestSubject<>(subjectSubscriptionManager, subjectSubscriptionManager, testScheduler);
    }

    protected TestSubject(Observable.OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> subjectSubscriptionManager, TestScheduler testScheduler) {
        super(onSubscribe);
        this.state = subjectSubscriptionManager;
        this.innerScheduler = testScheduler.createWorker();
    }

    @Override // rx.Observer
    public void onCompleted() {
        onCompleted(0L);
    }

    void _onCompleted() {
        if (this.state.active) {
            for (SubjectSubscriptionManager.SubjectObserver<T> subjectObserver : this.state.terminate(NotificationLite.instance().completed())) {
                subjectObserver.onCompleted();
            }
        }
    }

    public void onCompleted(long j) {
        this.innerScheduler.schedule(new Action0() { // from class: rx.subjects.TestSubject.2
            @Override // rx.functions.Action0
            public void call() {
                TestSubject.this._onCompleted();
            }
        }, j, TimeUnit.MILLISECONDS);
    }

    @Override // rx.Observer
    public void onError(Throwable th) {
        onError(th, 0L);
    }

    void _onError(Throwable th) {
        if (this.state.active) {
            for (SubjectSubscriptionManager.SubjectObserver<T> subjectObserver : this.state.terminate(NotificationLite.instance().error(th))) {
                subjectObserver.onError(th);
            }
        }
    }

    public void onError(final Throwable th, long j) {
        this.innerScheduler.schedule(new Action0() { // from class: rx.subjects.TestSubject.3
            @Override // rx.functions.Action0
            public void call() {
                TestSubject.this._onError(th);
            }
        }, j, TimeUnit.MILLISECONDS);
    }

    @Override // rx.Observer
    public void onNext(T t) {
        onNext(t, 0L);
    }

    void _onNext(T t) {
        for (SubjectSubscriptionManager.SubjectObserver<T> subjectObserver : this.state.observers()) {
            subjectObserver.onNext(t);
        }
    }

    public void onNext(final T t, long j) {
        this.innerScheduler.schedule(new Action0() { // from class: rx.subjects.TestSubject.4
            /* JADX WARN: Multi-variable type inference failed */
            @Override // rx.functions.Action0
            public void call() {
                TestSubject.this._onNext(t);
            }
        }, j, TimeUnit.MILLISECONDS);
    }

    @Override // rx.subjects.Subject
    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }
}
