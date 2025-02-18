package kotlin.collections;

import java.util.Collection;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.UIntArray;
import kotlin.ULong;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: _UCollections.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000F\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u0002H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0007*\b\u0012\u0004\u0012\u00020\u00070\u0002H\u0007ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\n0\u0002H\u0007ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\u0005\u001a\u001a\u0010\f\u001a\u00020\r*\b\u0012\u0004\u0012\u00020\u00030\u000eH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a\u001a\u0010\u0010\u001a\u00020\u0011*\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a\u001a\u0010\u0013\u001a\u00020\u0014*\b\u0012\u0004\u0012\u00020\u00070\u000eH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0015\u001a\u001a\u0010\u0016\u001a\u00020\u0017*\b\u0012\u0004\u0012\u00020\n0\u000eH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0018\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0019"}, d2 = {"sum", "Lkotlin/UInt;", "", "Lkotlin/UByte;", "sumOfUByte", "(Ljava/lang/Iterable;)I", "sumOfUInt", "Lkotlin/ULong;", "sumOfULong", "(Ljava/lang/Iterable;)J", "Lkotlin/UShort;", "sumOfUShort", "toUByteArray", "Lkotlin/UByteArray;", "", "(Ljava/util/Collection;)[B", "toUIntArray", "Lkotlin/UIntArray;", "(Ljava/util/Collection;)[I", "toULongArray", "Lkotlin/ULongArray;", "(Ljava/util/Collection;)[J", "toUShortArray", "Lkotlin/UShortArray;", "(Ljava/util/Collection;)[S", "kotlin-stdlib"}, k = 5, mv = {1, 4, 0}, xi = 1, xs = "kotlin/collections/UCollectionsKt")
/* loaded from: classes2.dex */
class UCollectionsKt___UCollectionsKt {
    public static final byte[] toUByteArray(Collection<UByte> toUByteArray) {
        Intrinsics.checkNotNullParameter(toUByteArray, "$this$toUByteArray");
        byte[] m82constructorimpl = UByteArray.m82constructorimpl(toUByteArray.size());
        Iterator<UByte> it = toUByteArray.iterator();
        int i = 0;
        while (it.hasNext()) {
            UByteArray.m93setVurrAj0(m82constructorimpl, i, it.next().getData());
            i++;
        }
        return m82constructorimpl;
    }

    public static final int[] toUIntArray(Collection<UInt> toUIntArray) {
        Intrinsics.checkNotNullParameter(toUIntArray, "$this$toUIntArray");
        int[] m152constructorimpl = UIntArray.m152constructorimpl(toUIntArray.size());
        Iterator<UInt> it = toUIntArray.iterator();
        int i = 0;
        while (it.hasNext()) {
            UIntArray.m163setVXSXFK8(m152constructorimpl, i, it.next().getData());
            i++;
        }
        return m152constructorimpl;
    }

    public static final long[] toULongArray(Collection<ULong> toULongArray) {
        Intrinsics.checkNotNullParameter(toULongArray, "$this$toULongArray");
        long[] m222constructorimpl = ULongArray.m222constructorimpl(toULongArray.size());
        Iterator<ULong> it = toULongArray.iterator();
        int i = 0;
        while (it.hasNext()) {
            ULongArray.m233setk8EXiF4(m222constructorimpl, i, it.next().getData());
            i++;
        }
        return m222constructorimpl;
    }

    public static final short[] toUShortArray(Collection<UShort> toUShortArray) {
        Intrinsics.checkNotNullParameter(toUShortArray, "$this$toUShortArray");
        short[] m318constructorimpl = UShortArray.m318constructorimpl(toUShortArray.size());
        Iterator<UShort> it = toUShortArray.iterator();
        int i = 0;
        while (it.hasNext()) {
            UShortArray.m329set01HTLdE(m318constructorimpl, i, it.next().getData());
            i++;
        }
        return m318constructorimpl;
    }

    public static final int sumOfUInt(Iterable<UInt> sum) {
        Intrinsics.checkNotNullParameter(sum, "$this$sum");
        Iterator<UInt> it = sum.iterator();
        int i = 0;
        while (it.hasNext()) {
            i = UInt.m107constructorimpl(i + it.next().getData());
        }
        return i;
    }

    public static final long sumOfULong(Iterable<ULong> sum) {
        Intrinsics.checkNotNullParameter(sum, "$this$sum");
        Iterator<ULong> it = sum.iterator();
        long j = 0;
        while (it.hasNext()) {
            j = ULong.m177constructorimpl(j + it.next().getData());
        }
        return j;
    }

    public static final int sumOfUByte(Iterable<UByte> sum) {
        Intrinsics.checkNotNullParameter(sum, "$this$sum");
        Iterator<UByte> it = sum.iterator();
        int i = 0;
        while (it.hasNext()) {
            i = UInt.m107constructorimpl(i + UInt.m107constructorimpl(it.next().getData() & UByte.MAX_VALUE));
        }
        return i;
    }

    public static final int sumOfUShort(Iterable<UShort> sum) {
        Intrinsics.checkNotNullParameter(sum, "$this$sum");
        Iterator<UShort> it = sum.iterator();
        int i = 0;
        while (it.hasNext()) {
            i = UInt.m107constructorimpl(i + UInt.m107constructorimpl(it.next().getData() & UShort.MAX_VALUE));
        }
        return i;
    }
}
