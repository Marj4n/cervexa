package rx.internal.operators;

import java.io.Serializable;
import rx.Notification;
import rx.Observer;

/* loaded from: classes2.dex */
public final class NotificationLite<T> {
    private static final NotificationLite INSTANCE = new NotificationLite();
    private static final Object ON_COMPLETED_SENTINEL = new Serializable() { // from class: rx.internal.operators.NotificationLite.1
        private static final long serialVersionUID = 1;

        public String toString() {
            return "Notification=>Completed";
        }
    };
    private static final Object ON_NEXT_NULL_SENTINEL = new Serializable() { // from class: rx.internal.operators.NotificationLite.2
        private static final long serialVersionUID = 2;

        public String toString() {
            return "Notification=>NULL";
        }
    };

    private NotificationLite() {
    }

    public static <T> NotificationLite<T> instance() {
        return INSTANCE;
    }

    private static class OnErrorSentinel implements Serializable {
        private static final long serialVersionUID = 3;
        final Throwable e;

        public OnErrorSentinel(Throwable th) {
            this.e = th;
        }

        public String toString() {
            return "Notification=>Error:" + this.e;
        }
    }

    public Object next(T t) {
        return t == null ? ON_NEXT_NULL_SENTINEL : t;
    }

    public Object completed() {
        return ON_COMPLETED_SENTINEL;
    }

    public Object error(Throwable th) {
        return new OnErrorSentinel(th);
    }

    public boolean accept(Observer<? super T> observer, Object obj) {
        if (obj == ON_COMPLETED_SENTINEL) {
            observer.onCompleted();
            return true;
        }
        if (obj == ON_NEXT_NULL_SENTINEL) {
            observer.onNext(null);
            return false;
        }
        if (obj != null) {
            if (obj.getClass() == OnErrorSentinel.class) {
                observer.onError(((OnErrorSentinel) obj).e);
                return true;
            }
            observer.onNext(obj);
            return false;
        }
        throw new IllegalArgumentException("The lite notification can not be null");
    }

    public boolean isCompleted(Object obj) {
        return obj == ON_COMPLETED_SENTINEL;
    }

    public boolean isError(Object obj) {
        return obj instanceof OnErrorSentinel;
    }

    public boolean isNull(Object obj) {
        return obj == ON_NEXT_NULL_SENTINEL;
    }

    public boolean isNext(Object obj) {
        return (obj == null || isError(obj) || isCompleted(obj)) ? false : true;
    }

    public Notification.Kind kind(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("The lite notification can not be null");
        }
        if (obj == ON_COMPLETED_SENTINEL) {
            return Notification.Kind.OnCompleted;
        }
        if (obj instanceof OnErrorSentinel) {
            return Notification.Kind.OnError;
        }
        return Notification.Kind.OnNext;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public T getValue(Object obj) {
        if (obj == ON_NEXT_NULL_SENTINEL) {
            return null;
        }
        return obj;
    }

    public Throwable getError(Object obj) {
        return ((OnErrorSentinel) obj).e;
    }
}
