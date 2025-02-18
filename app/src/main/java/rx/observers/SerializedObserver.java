package rx.observers;

import rx.Observer;
import rx.exceptions.Exceptions;
import rx.internal.operators.NotificationLite;

/* loaded from: classes2.dex */
public class SerializedObserver<T> implements Observer<T> {
    private static final int MAX_DRAIN_ITERATION = 1024;
    private final Observer<? super T> actual;
    private boolean emitting;
    private final NotificationLite<T> nl = NotificationLite.instance();
    private FastList queue;
    private volatile boolean terminated;

    static final class FastList {
        Object[] array;
        int size;

        FastList() {
        }

        public void add(Object obj) {
            int i = this.size;
            Object[] objArr = this.array;
            if (objArr == null) {
                objArr = new Object[16];
                this.array = objArr;
            } else if (i == objArr.length) {
                Object[] objArr2 = new Object[(i >> 2) + i];
                System.arraycopy(objArr, 0, objArr2, 0, i);
                this.array = objArr2;
                objArr = objArr2;
            }
            objArr[i] = obj;
            this.size = i + 1;
        }
    }

    public SerializedObserver(Observer<? super T> observer) {
        this.actual = observer;
    }

    /* JADX WARN: Code restructure failed: missing block: B:47:0x006d, code lost:
    
        continue;
     */
    @Override // rx.Observer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onNext(T r10) {
        /*
            r9 = this;
            boolean r0 = r9.terminated
            if (r0 == 0) goto L5
            return
        L5:
            monitor-enter(r9)
            boolean r0 = r9.terminated     // Catch: java.lang.Throwable -> L7c
            if (r0 == 0) goto Lc
            monitor-exit(r9)     // Catch: java.lang.Throwable -> L7c
            return
        Lc:
            boolean r0 = r9.emitting     // Catch: java.lang.Throwable -> L7c
            if (r0 == 0) goto L26
            rx.observers.SerializedObserver$FastList r0 = r9.queue     // Catch: java.lang.Throwable -> L7c
            if (r0 != 0) goto L1b
            rx.observers.SerializedObserver$FastList r0 = new rx.observers.SerializedObserver$FastList     // Catch: java.lang.Throwable -> L7c
            r0.<init>()     // Catch: java.lang.Throwable -> L7c
            r9.queue = r0     // Catch: java.lang.Throwable -> L7c
        L1b:
            rx.internal.operators.NotificationLite<T> r1 = r9.nl     // Catch: java.lang.Throwable -> L7c
            java.lang.Object r10 = r1.next(r10)     // Catch: java.lang.Throwable -> L7c
            r0.add(r10)     // Catch: java.lang.Throwable -> L7c
            monitor-exit(r9)     // Catch: java.lang.Throwable -> L7c
            return
        L26:
            r0 = 1
            r9.emitting = r0     // Catch: java.lang.Throwable -> L7c
            monitor-exit(r9)     // Catch: java.lang.Throwable -> L7c
            rx.Observer<? super T> r1 = r9.actual     // Catch: java.lang.Throwable -> L73
            r1.onNext(r10)     // Catch: java.lang.Throwable -> L73
        L2f:
            r1 = 0
            r2 = 0
        L31:
            r3 = 1024(0x400, float:1.435E-42)
            if (r2 >= r3) goto L2f
            monitor-enter(r9)
            rx.observers.SerializedObserver$FastList r3 = r9.queue     // Catch: java.lang.Throwable -> L70
            if (r3 != 0) goto L3e
            r9.emitting = r1     // Catch: java.lang.Throwable -> L70
            monitor-exit(r9)     // Catch: java.lang.Throwable -> L70
            return
        L3e:
            r4 = 0
            r9.queue = r4     // Catch: java.lang.Throwable -> L70
            monitor-exit(r9)     // Catch: java.lang.Throwable -> L70
            java.lang.Object[] r3 = r3.array
            int r4 = r3.length
            r5 = 0
        L46:
            if (r5 >= r4) goto L6d
            r6 = r3[r5]
            if (r6 != 0) goto L4d
            goto L6d
        L4d:
            rx.internal.operators.NotificationLite<T> r7 = r9.nl     // Catch: java.lang.Throwable -> L5d
            rx.Observer<? super T> r8 = r9.actual     // Catch: java.lang.Throwable -> L5d
            boolean r6 = r7.accept(r8, r6)     // Catch: java.lang.Throwable -> L5d
            if (r6 == 0) goto L5a
            r9.terminated = r0     // Catch: java.lang.Throwable -> L5d
            return
        L5a:
            int r5 = r5 + 1
            goto L46
        L5d:
            r1 = move-exception
            r9.terminated = r0
            rx.exceptions.Exceptions.throwIfFatal(r1)
            rx.Observer<? super T> r0 = r9.actual
            java.lang.Throwable r10 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r1, r10)
            r0.onError(r10)
            return
        L6d:
            int r2 = r2 + 1
            goto L31
        L70:
            r10 = move-exception
            monitor-exit(r9)     // Catch: java.lang.Throwable -> L70
            throw r10
        L73:
            r1 = move-exception
            r9.terminated = r0
            rx.Observer<? super T> r0 = r9.actual
            rx.exceptions.Exceptions.throwOrReport(r1, r0, r10)
            return
        L7c:
            r10 = move-exception
            monitor-exit(r9)     // Catch: java.lang.Throwable -> L7c
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.observers.SerializedObserver.onNext(java.lang.Object):void");
    }

    @Override // rx.Observer
    public void onError(Throwable th) {
        Exceptions.throwIfFatal(th);
        if (this.terminated) {
            return;
        }
        synchronized (this) {
            if (this.terminated) {
                return;
            }
            this.terminated = true;
            if (this.emitting) {
                FastList fastList = this.queue;
                if (fastList == null) {
                    fastList = new FastList();
                    this.queue = fastList;
                }
                fastList.add(this.nl.error(th));
                return;
            }
            this.emitting = true;
            this.actual.onError(th);
        }
    }

    @Override // rx.Observer
    public void onCompleted() {
        if (this.terminated) {
            return;
        }
        synchronized (this) {
            if (this.terminated) {
                return;
            }
            this.terminated = true;
            if (this.emitting) {
                FastList fastList = this.queue;
                if (fastList == null) {
                    fastList = new FastList();
                    this.queue = fastList;
                }
                fastList.add(this.nl.completed());
                return;
            }
            this.emitting = true;
            this.actual.onCompleted();
        }
    }
}
