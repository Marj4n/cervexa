package kotlinx.coroutines.internal;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.Segment;

/* compiled from: ConcurrentLinkedList.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0080@\u0018\u0000*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u0003B\u0014\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006J\u0013\u0010\u0010\u001a\u00020\b2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0003HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001R\u0011\u0010\u0007\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0017\u0010\u000b\u001a\u00028\u00008F¢\u0006\f\u0012\u0004\b\f\u0010\r\u001a\u0004\b\u000e\u0010\u000fR\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000ø\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0016"}, d2 = {"Lkotlinx/coroutines/internal/SegmentOrClosed;", "S", "Lkotlinx/coroutines/internal/Segment;", "", "value", "constructor-impl", "(Ljava/lang/Object;)Ljava/lang/Object;", "isClosed", "", "isClosed-impl", "(Ljava/lang/Object;)Z", "segment", "getSegment$annotations", "()V", "getSegment-impl", "(Ljava/lang/Object;)Lkotlinx/coroutines/internal/Segment;", "equals", "other", "hashCode", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 4, 0})
/* loaded from: classes2.dex */
public final class SegmentOrClosed<S extends Segment<S>> {
    private final Object value;

    /* renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ SegmentOrClosed m1378boximpl(Object obj) {
        return new SegmentOrClosed(obj);
    }

    /* renamed from: constructor-impl, reason: not valid java name */
    public static Object m1379constructorimpl(Object obj) {
        return obj;
    }

    /* renamed from: equals-impl, reason: not valid java name */
    public static boolean m1380equalsimpl(Object obj, Object obj2) {
        return (obj2 instanceof SegmentOrClosed) && Intrinsics.areEqual(obj, ((SegmentOrClosed) obj2).getValue());
    }

    /* renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m1381equalsimpl0(Object obj, Object obj2) {
        return Intrinsics.areEqual(obj, obj2);
    }

    public static /* synthetic */ void getSegment$annotations() {
    }

    /* renamed from: hashCode-impl, reason: not valid java name */
    public static int m1383hashCodeimpl(Object obj) {
        if (obj != null) {
            return obj.hashCode();
        }
        return 0;
    }

    /* renamed from: toString-impl, reason: not valid java name */
    public static String m1385toStringimpl(Object obj) {
        return "SegmentOrClosed(value=" + obj + ")";
    }

    public boolean equals(Object other) {
        return m1380equalsimpl(this.value, other);
    }

    public int hashCode() {
        return m1383hashCodeimpl(this.value);
    }

    public String toString() {
        return m1385toStringimpl(this.value);
    }

    /* renamed from: unbox-impl, reason: not valid java name and from getter */
    public final /* synthetic */ Object getValue() {
        return this.value;
    }

    private /* synthetic */ SegmentOrClosed(Object obj) {
        this.value = obj;
    }

    /* renamed from: isClosed-impl, reason: not valid java name */
    public static final boolean m1384isClosedimpl(Object obj) {
        return obj == ConcurrentLinkedListKt.CLOSED;
    }

    /* renamed from: getSegment-impl, reason: not valid java name */
    public static final S m1382getSegmentimpl(Object obj) {
        if (obj == ConcurrentLinkedListKt.CLOSED) {
            throw new IllegalStateException("Does not contain segment".toString());
        }
        if (obj != null) {
            return (S) obj;
        }
        throw new NullPointerException("null cannot be cast to non-null type S");
    }
}
