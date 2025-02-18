package rx.internal.operators;

import rx.Notification;
import rx.Observable;
import rx.Subscriber;

/* loaded from: classes2.dex */
public final class OperatorDematerialize<T> implements Observable.Operator<T, Notification<T>> {

    private static final class Holder {
        static final OperatorDematerialize<Object> INSTANCE = new OperatorDematerialize<>();

        private Holder() {
        }
    }

    public static OperatorDematerialize instance() {
        return Holder.INSTANCE;
    }

    OperatorDematerialize() {
    }

    @Override // rx.functions.Func1
    public Subscriber<? super Notification<T>> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<Notification<T>>(subscriber) { // from class: rx.internal.operators.OperatorDematerialize.1
            boolean terminated;

            @Override // rx.Observer
            public void onNext(Notification<T> notification) {
                int i = AnonymousClass2.$SwitchMap$rx$Notification$Kind[notification.getKind().ordinal()];
                if (i == 1) {
                    if (this.terminated) {
                        return;
                    }
                    subscriber.onNext(notification.getValue());
                } else if (i == 2) {
                    onError(notification.getThrowable());
                } else {
                    if (i != 3) {
                        return;
                    }
                    onCompleted();
                }
            }

            @Override // rx.Observer
            public void onError(Throwable th) {
                if (this.terminated) {
                    return;
                }
                this.terminated = true;
                subscriber.onError(th);
            }

            @Override // rx.Observer
            public void onCompleted() {
                if (this.terminated) {
                    return;
                }
                this.terminated = true;
                subscriber.onCompleted();
            }
        };
    }

    /* renamed from: rx.internal.operators.OperatorDematerialize$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$rx$Notification$Kind;

        static {
            int[] iArr = new int[Notification.Kind.values().length];
            $SwitchMap$rx$Notification$Kind = iArr;
            try {
                iArr[Notification.Kind.OnNext.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$rx$Notification$Kind[Notification.Kind.OnError.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$rx$Notification$Kind[Notification.Kind.OnCompleted.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }
}
