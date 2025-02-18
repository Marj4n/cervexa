package kotlinx.coroutines.channels;

import com.google.zxing.client.result.optional.NDEFRecord;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Channel.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0087@\u0018\u0000 \u001f*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u0002:\u0002\u001e\u001fB\u0016\b\u0000\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\u0013\u0010\u0016\u001a\u00020\r2\b\u0010\u0017\u001a\u0004\u0018\u00010\u0002HÖ\u0003J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001J\u000f\u0010\u001a\u001a\u00020\u001bH\u0016¢\u0006\u0004\b\u001c\u0010\u001dR\u0019\u0010\u0006\u001a\u0004\u0018\u00010\u00078F¢\u0006\f\u0012\u0004\b\b\u0010\t\u001a\u0004\b\n\u0010\u000bR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\r8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0010\u001a\u00028\u00008F¢\u0006\f\u0012\u0004\b\u0011\u0010\t\u001a\u0004\b\u0012\u0010\u0005R\u0019\u0010\u0013\u001a\u0004\u0018\u00018\u00008F¢\u0006\f\u0012\u0004\b\u0014\u0010\t\u001a\u0004\b\u0015\u0010\u0005ø\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006 "}, d2 = {"Lkotlinx/coroutines/channels/ValueOrClosed;", NDEFRecord.TEXT_WELL_KNOWN_TYPE, "", "holder", "constructor-impl", "(Ljava/lang/Object;)Ljava/lang/Object;", "closeCause", "", "getCloseCause$annotations", "()V", "getCloseCause-impl", "(Ljava/lang/Object;)Ljava/lang/Throwable;", "isClosed", "", "isClosed-impl", "(Ljava/lang/Object;)Z", "value", "getValue$annotations", "getValue-impl", "valueOrNull", "getValueOrNull$annotations", "getValueOrNull-impl", "equals", "other", "hashCode", "", "toString", "", "toString-impl", "(Ljava/lang/Object;)Ljava/lang/String;", "Closed", "Companion", "kotlinx-coroutines-core"}, k = 1, mv = {1, 4, 0})
/* loaded from: classes2.dex */
public final class ValueOrClosed<T> {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final Object holder;

    /* renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ ValueOrClosed m1347boximpl(Object obj) {
        return new ValueOrClosed(obj);
    }

    /* renamed from: constructor-impl, reason: not valid java name */
    public static Object m1348constructorimpl(Object obj) {
        return obj;
    }

    /* renamed from: equals-impl, reason: not valid java name */
    public static boolean m1349equalsimpl(Object obj, Object obj2) {
        return (obj2 instanceof ValueOrClosed) && Intrinsics.areEqual(obj, ((ValueOrClosed) obj2).getHolder());
    }

    /* renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m1350equalsimpl0(Object obj, Object obj2) {
        return Intrinsics.areEqual(obj, obj2);
    }

    public static /* synthetic */ void getCloseCause$annotations() {
    }

    public static /* synthetic */ void getValue$annotations() {
    }

    public static /* synthetic */ void getValueOrNull$annotations() {
    }

    /* renamed from: hashCode-impl, reason: not valid java name */
    public static int m1354hashCodeimpl(Object obj) {
        if (obj != null) {
            return obj.hashCode();
        }
        return 0;
    }

    public boolean equals(Object other) {
        return m1349equalsimpl(this.holder, other);
    }

    public int hashCode() {
        return m1354hashCodeimpl(this.holder);
    }

    public String toString() {
        return m1356toStringimpl(this.holder);
    }

    /* renamed from: unbox-impl, reason: not valid java name and from getter */
    public final /* synthetic */ Object getHolder() {
        return this.holder;
    }

    private /* synthetic */ ValueOrClosed(Object obj) {
        this.holder = obj;
    }

    /* renamed from: isClosed-impl, reason: not valid java name */
    public static final boolean m1355isClosedimpl(Object obj) {
        return obj instanceof Closed;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* renamed from: getValue-impl, reason: not valid java name */
    public static final T m1352getValueimpl(Object obj) {
        if (obj instanceof Closed) {
            throw new IllegalStateException(ChannelsKt.DEFAULT_CLOSE_MESSAGE.toString());
        }
        return obj;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* renamed from: getValueOrNull-impl, reason: not valid java name */
    public static final T m1353getValueOrNullimpl(Object obj) {
        if (obj instanceof Closed) {
            return null;
        }
        return obj;
    }

    /* renamed from: getCloseCause-impl, reason: not valid java name */
    public static final Throwable m1351getCloseCauseimpl(Object obj) {
        if (obj instanceof Closed) {
            return ((Closed) obj).cause;
        }
        throw new IllegalStateException("Channel was not closed".toString());
    }

    /* renamed from: toString-impl, reason: not valid java name */
    public static String m1356toStringimpl(Object obj) {
        if (obj instanceof Closed) {
            return obj.toString();
        }
        return "Value(" + obj + ')';
    }

    /* compiled from: Channel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004J\u0013\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\b\u001a\u00020\tH\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016R\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/channels/ValueOrClosed$Closed;", "", "cause", "", "(Ljava/lang/Throwable;)V", "equals", "", "other", "hashCode", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 4, 0})
    public static final class Closed {
        public final Throwable cause;

        public Closed(Throwable th) {
            this.cause = th;
        }

        public boolean equals(Object other) {
            return (other instanceof Closed) && Intrinsics.areEqual(this.cause, ((Closed) other).cause);
        }

        public int hashCode() {
            Throwable th = this.cause;
            if (th != null) {
                return th.hashCode();
            }
            return 0;
        }

        public String toString() {
            return "Closed(" + this.cause + ')';
        }
    }

    /* compiled from: Channel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0006\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J,\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0001\u0010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0080\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\b\u0010\tJ*\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0001\u0010\u00052\u0006\u0010\n\u001a\u0002H\u0005H\u0080\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\u000b\u0010\f\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/channels/ValueOrClosed$Companion;", "", "()V", "closed", "Lkotlinx/coroutines/channels/ValueOrClosed;", "E", "cause", "", "closed-ZYPwvRU$kotlinx_coroutines_core", "(Ljava/lang/Throwable;)Ljava/lang/Object;", "value", "value-ZYPwvRU$kotlinx_coroutines_core", "(Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 4, 0})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* renamed from: value-ZYPwvRU$kotlinx_coroutines_core, reason: not valid java name */
        public final <E> Object m1359valueZYPwvRU$kotlinx_coroutines_core(E value) {
            return ValueOrClosed.m1348constructorimpl(value);
        }

        /* renamed from: closed-ZYPwvRU$kotlinx_coroutines_core, reason: not valid java name */
        public final <E> Object m1358closedZYPwvRU$kotlinx_coroutines_core(Throwable cause) {
            return ValueOrClosed.m1348constructorimpl(new Closed(cause));
        }
    }
}
