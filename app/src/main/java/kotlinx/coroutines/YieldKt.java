package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;

/* compiled from: Yield.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u0001H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0002\u001a\f\u0010\u0003\u001a\u00020\u0001*\u00020\u0004H\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0005"}, d2 = {"yield", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checkCompletion", "Lkotlin/coroutines/CoroutineContext;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 4, 0})
/* loaded from: classes2.dex */
public final class YieldKt {
    public static final Object yield(Continuation<? super Unit> continuation) {
        Object obj;
        CoroutineContext coroutineContext = continuation.get$context();
        checkCompletion(coroutineContext);
        Continuation intercepted = IntrinsicsKt.intercepted(continuation);
        if (!(intercepted instanceof DispatchedContinuation)) {
            intercepted = null;
        }
        DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) intercepted;
        if (dispatchedContinuation == null) {
            obj = Unit.INSTANCE;
        } else {
            if (dispatchedContinuation.dispatcher.isDispatchNeeded(coroutineContext)) {
                dispatchedContinuation.dispatchYield$kotlinx_coroutines_core(coroutineContext, Unit.INSTANCE);
            } else {
                YieldContext yieldContext = new YieldContext();
                dispatchedContinuation.dispatchYield$kotlinx_coroutines_core(coroutineContext.plus(yieldContext), Unit.INSTANCE);
                if (yieldContext.dispatcherWasUnconfined) {
                    obj = DispatchedContinuationKt.yieldUndispatched(dispatchedContinuation) ? IntrinsicsKt.getCOROUTINE_SUSPENDED() : Unit.INSTANCE;
                }
            }
            obj = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        }
        if (obj == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return obj == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? obj : Unit.INSTANCE;
    }

    public static final void checkCompletion(CoroutineContext coroutineContext) {
        Job job = (Job) coroutineContext.get(Job.INSTANCE);
        if (job != null && !job.isActive()) {
            throw job.getCancellationException();
        }
    }
}
