package kotlinx.coroutines.flow;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.internal.Symbol;

/* compiled from: StateFlow.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u0000\b\u0002\u0018\u00002\u00020\rB\u0007¢\u0006\u0004\b\u0001\u0010\u0002J\r\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0013\u0010\u0007\u001a\u00020\u0006H\u0086@ø\u0001\u0000¢\u0006\u0004\b\u0007\u0010\bJ\r\u0010\t\u001a\u00020\u0006¢\u0006\u0004\b\t\u0010\u0002J\r\u0010\n\u001a\u00020\u0006¢\u0006\u0004\b\n\u0010\u0002J\r\u0010\u000b\u001a\u00020\u0003¢\u0006\u0004\b\u000b\u0010\u0005\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/flow/StateFlowSlot;", "<init>", "()V", "", "allocate", "()Z", "", "awaitPending", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "free", "makePending", "takePending", "kotlinx-coroutines-core", ""}, k = 1, mv = {1, 4, 0})
/* loaded from: classes2.dex */
final class StateFlowSlot {
    static final AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(StateFlowSlot.class, Object.class, "_state");
    volatile Object _state = null;

    public final boolean allocate() {
        Symbol symbol;
        if (this._state != null) {
            return false;
        }
        symbol = StateFlowKt.NONE;
        this._state = symbol;
        return true;
    }

    public final void free() {
        this._state = null;
    }

    public final boolean takePending() {
        Symbol symbol;
        Symbol symbol2;
        AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = _state$FU;
        symbol = StateFlowKt.NONE;
        Object andSet = atomicReferenceFieldUpdater.getAndSet(this, symbol);
        Intrinsics.checkNotNull(andSet);
        if (DebugKt.getASSERTIONS_ENABLED() && !(!(andSet instanceof CancellableContinuationImpl))) {
            throw new AssertionError();
        }
        symbol2 = StateFlowKt.PENDING;
        return andSet == symbol2;
    }

    public final void makePending() {
        Symbol symbol;
        Symbol symbol2;
        Symbol symbol3;
        Symbol symbol4;
        while (true) {
            Object obj = this._state;
            if (obj == null) {
                return;
            }
            symbol = StateFlowKt.PENDING;
            if (obj == symbol) {
                return;
            }
            symbol2 = StateFlowKt.NONE;
            if (obj == symbol2) {
                AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = _state$FU;
                symbol3 = StateFlowKt.PENDING;
                if (atomicReferenceFieldUpdater.compareAndSet(this, obj, symbol3)) {
                    return;
                }
            } else {
                AtomicReferenceFieldUpdater atomicReferenceFieldUpdater2 = _state$FU;
                symbol4 = StateFlowKt.NONE;
                if (atomicReferenceFieldUpdater2.compareAndSet(this, obj, symbol4)) {
                    Unit unit = Unit.INSTANCE;
                    Result.Companion companion = Result.INSTANCE;
                    ((CancellableContinuationImpl) obj).resumeWith(Result.m21constructorimpl(unit));
                    return;
                }
            }
        }
    }

    public final Object awaitPending(Continuation<? super Unit> continuation) {
        Symbol symbol;
        Symbol symbol2;
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 1);
        cancellableContinuationImpl.initCancellability();
        CancellableContinuationImpl cancellableContinuationImpl2 = cancellableContinuationImpl;
        if (DebugKt.getASSERTIONS_ENABLED() && !Boxing.boxBoolean(!(this._state instanceof CancellableContinuationImpl)).booleanValue()) {
            throw new AssertionError();
        }
        AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = _state$FU;
        symbol = StateFlowKt.NONE;
        if (!atomicReferenceFieldUpdater.compareAndSet(this, symbol, cancellableContinuationImpl2)) {
            if (DebugKt.getASSERTIONS_ENABLED()) {
                Object obj = this._state;
                symbol2 = StateFlowKt.PENDING;
                if (!Boxing.boxBoolean(obj == symbol2).booleanValue()) {
                    throw new AssertionError();
                }
            }
            Unit unit = Unit.INSTANCE;
            Result.Companion companion = Result.INSTANCE;
            cancellableContinuationImpl2.resumeWith(Result.m21constructorimpl(unit));
        }
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }
}
