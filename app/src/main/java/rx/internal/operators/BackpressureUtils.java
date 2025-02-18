package rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import rx.Subscriber;
import rx.functions.Func1;
import rx.internal.util.UtilityFunctions;

/* loaded from: classes2.dex */
public final class BackpressureUtils {
    static final long COMPLETED_MASK = Long.MIN_VALUE;
    static final long REQUESTED_MASK = Long.MAX_VALUE;

    public static long addCap(long j, long j2) {
        long j3 = j + j2;
        if (j3 < 0) {
            return Long.MAX_VALUE;
        }
        return j3;
    }

    private BackpressureUtils() {
        throw new IllegalStateException("No instances!");
    }

    @Deprecated
    public static <T> long getAndAddRequest(AtomicLongFieldUpdater<T> atomicLongFieldUpdater, T t, long j) {
        long j2;
        do {
            j2 = atomicLongFieldUpdater.get(t);
        } while (!atomicLongFieldUpdater.compareAndSet(t, j2, addCap(j2, j)));
        return j2;
    }

    public static long getAndAddRequest(AtomicLong atomicLong, long j) {
        long j2;
        do {
            j2 = atomicLong.get();
        } while (!atomicLong.compareAndSet(j2, addCap(j2, j)));
        return j2;
    }

    public static long multiplyCap(long j, long j2) {
        long j3 = j * j2;
        if (((j | j2) >>> 31) == 0 || j2 == 0 || j3 / j2 == j) {
            return j3;
        }
        return Long.MAX_VALUE;
    }

    public static <T> void postCompleteDone(AtomicLong atomicLong, Queue<T> queue, Subscriber<? super T> subscriber) {
        postCompleteDone(atomicLong, queue, subscriber, UtilityFunctions.identity());
    }

    public static <T> boolean postCompleteRequest(AtomicLong atomicLong, long j, Queue<T> queue, Subscriber<? super T> subscriber) {
        return postCompleteRequest(atomicLong, j, queue, subscriber, UtilityFunctions.identity());
    }

    public static <T, R> void postCompleteDone(AtomicLong atomicLong, Queue<T> queue, Subscriber<? super R> subscriber, Func1<? super T, ? extends R> func1) {
        long j;
        do {
            j = atomicLong.get();
            if ((j & Long.MIN_VALUE) != 0) {
                return;
            }
        } while (!atomicLong.compareAndSet(j, Long.MIN_VALUE | j));
        if (j != 0) {
            postCompleteDrain(atomicLong, queue, subscriber, func1);
        }
    }

    public static <T, R> boolean postCompleteRequest(AtomicLong atomicLong, long j, Queue<T> queue, Subscriber<? super R> subscriber, Func1<? super T, ? extends R> func1) {
        long j2;
        long j3;
        if (j < 0) {
            throw new IllegalArgumentException("n >= 0 required but it was " + j);
        }
        if (j == 0) {
            return (atomicLong.get() & Long.MIN_VALUE) == 0;
        }
        do {
            j2 = atomicLong.get();
            j3 = j2 & Long.MIN_VALUE;
        } while (!atomicLong.compareAndSet(j2, addCap(Long.MAX_VALUE & j2, j) | j3));
        if (j2 != Long.MIN_VALUE) {
            return j3 == 0;
        }
        postCompleteDrain(atomicLong, queue, subscriber, func1);
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0064, code lost:
    
        r0 = r10.addAndGet(-(r6 & Long.MAX_VALUE));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static <T, R> void postCompleteDrain(java.util.concurrent.atomic.AtomicLong r10, java.util.Queue<T> r11, rx.Subscriber<? super R> r12, rx.functions.Func1<? super T, ? extends R> r13) {
        /*
            long r0 = r10.get()
            r2 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 != 0) goto L26
        Ld:
            boolean r10 = r12.isUnsubscribed()
            if (r10 == 0) goto L14
            return
        L14:
            java.lang.Object r10 = r11.poll()
            if (r10 != 0) goto L1e
            r12.onCompleted()
            return
        L1e:
            java.lang.Object r10 = r13.call(r10)
            r12.onNext(r10)
            goto Ld
        L26:
            r4 = -9223372036854775808
        L28:
            r6 = r4
        L29:
            int r8 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r8 == 0) goto L49
            boolean r8 = r12.isUnsubscribed()
            if (r8 == 0) goto L34
            return
        L34:
            java.lang.Object r8 = r11.poll()
            if (r8 != 0) goto L3e
            r12.onCompleted()
            return
        L3e:
            java.lang.Object r8 = r13.call(r8)
            r12.onNext(r8)
            r8 = 1
            long r6 = r6 + r8
            goto L29
        L49:
            if (r8 != 0) goto L5c
            boolean r0 = r12.isUnsubscribed()
            if (r0 == 0) goto L52
            return
        L52:
            boolean r0 = r11.isEmpty()
            if (r0 == 0) goto L5c
            r12.onCompleted()
            return
        L5c:
            long r0 = r10.get()
            int r8 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r8 != 0) goto L29
            long r0 = r6 & r2
            long r0 = -r0
            long r0 = r10.addAndGet(r0)
            int r6 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r6 != 0) goto L28
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.BackpressureUtils.postCompleteDrain(java.util.concurrent.atomic.AtomicLong, java.util.Queue, rx.Subscriber, rx.functions.Func1):void");
    }

    public static long produced(AtomicLong atomicLong, long j) {
        long j2;
        long j3;
        do {
            j2 = atomicLong.get();
            if (j2 == Long.MAX_VALUE) {
                return Long.MAX_VALUE;
            }
            j3 = j2 - j;
            if (j3 < 0) {
                throw new IllegalStateException("More produced than requested: " + j3);
            }
        } while (!atomicLong.compareAndSet(j2, j3));
        return j3;
    }
}
