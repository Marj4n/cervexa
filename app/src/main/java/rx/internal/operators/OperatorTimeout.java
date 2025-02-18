package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.operators.OperatorTimeoutBase;

/* loaded from: classes2.dex */
public final class OperatorTimeout<T> extends OperatorTimeoutBase<T> {
    @Override // rx.internal.operators.OperatorTimeoutBase
    public /* bridge */ /* synthetic */ Subscriber call(Subscriber subscriber) {
        return super.call(subscriber);
    }

    public OperatorTimeout(final long j, final TimeUnit timeUnit, Observable<? extends T> observable, Scheduler scheduler) {
        super(new OperatorTimeoutBase.FirstTimeoutStub<T>() { // from class: rx.internal.operators.OperatorTimeout.1
            @Override // rx.functions.Func3
            public Subscription call(final OperatorTimeoutBase.TimeoutSubscriber<T> timeoutSubscriber, final Long l, Scheduler.Worker worker) {
                return worker.schedule(new Action0() { // from class: rx.internal.operators.OperatorTimeout.1.1
                    @Override // rx.functions.Action0
                    public void call() {
                        timeoutSubscriber.onTimeout(l.longValue());
                    }
                }, j, timeUnit);
            }
        }, new OperatorTimeoutBase.TimeoutStub<T>() { // from class: rx.internal.operators.OperatorTimeout.2
            @Override // rx.functions.Func4
            public /* bridge */ /* synthetic */ Subscription call(Object obj, Long l, Object obj2, Scheduler.Worker worker) {
                return call((OperatorTimeoutBase.TimeoutSubscriber<Long>) obj, l, (Long) obj2, worker);
            }

            public Subscription call(final OperatorTimeoutBase.TimeoutSubscriber<T> timeoutSubscriber, final Long l, T t, Scheduler.Worker worker) {
                return worker.schedule(new Action0() { // from class: rx.internal.operators.OperatorTimeout.2.1
                    @Override // rx.functions.Action0
                    public void call() {
                        timeoutSubscriber.onTimeout(l.longValue());
                    }
                }, j, timeUnit);
            }
        }, observable, scheduler);
    }
}
