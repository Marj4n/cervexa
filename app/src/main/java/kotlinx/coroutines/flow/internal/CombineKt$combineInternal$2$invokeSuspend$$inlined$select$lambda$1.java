package kotlinx.coroutines.flow.internal;

import com.google.zxing.client.result.optional.NDEFRecord;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.internal.Symbol;

/* compiled from: Combine.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u008a@¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"<anonymous>", "", "R", NDEFRecord.TEXT_WELL_KNOWN_TYPE, "value", "", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx/coroutines/flow/internal/CombineKt$combineInternal$2$1$2"}, k = 3, mv = {1, 4, 0})
/* loaded from: classes2.dex */
final class CombineKt$combineInternal$2$invokeSuspend$$inlined$select$lambda$1 extends SuspendLambda implements Function2<Object, Continuation<? super Unit>, Object> {
    final /* synthetic */ Ref.ObjectRef $channels$inlined;
    final /* synthetic */ int $i;
    final /* synthetic */ Boolean[] $isClosed$inlined;
    final /* synthetic */ Object[] $latestValues$inlined;
    final /* synthetic */ Ref.IntRef $nonClosed$inlined;
    final /* synthetic */ Ref.IntRef $remainingNulls$inlined;
    final /* synthetic */ int $size$inlined;
    Object L$0;
    Object L$1;
    int label;
    private Object p$0;
    final /* synthetic */ CombineKt$combineInternal$2 this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    CombineKt$combineInternal$2$invokeSuspend$$inlined$select$lambda$1(int i, Continuation continuation, CombineKt$combineInternal$2 combineKt$combineInternal$2, int i2, Boolean[] boolArr, Ref.ObjectRef objectRef, Object[] objArr, Ref.IntRef intRef, Ref.IntRef intRef2) {
        super(2, continuation);
        this.$i = i;
        this.this$0 = combineKt$combineInternal$2;
        this.$size$inlined = i2;
        this.$isClosed$inlined = boolArr;
        this.$channels$inlined = objectRef;
        this.$latestValues$inlined = objArr;
        this.$remainingNulls$inlined = intRef;
        this.$nonClosed$inlined = intRef2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        CombineKt$combineInternal$2$invokeSuspend$$inlined$select$lambda$1 combineKt$combineInternal$2$invokeSuspend$$inlined$select$lambda$1 = new CombineKt$combineInternal$2$invokeSuspend$$inlined$select$lambda$1(this.$i, continuation, this.this$0, this.$size$inlined, this.$isClosed$inlined, this.$channels$inlined, this.$latestValues$inlined, this.$remainingNulls$inlined, this.$nonClosed$inlined);
        combineKt$combineInternal$2$invokeSuspend$$inlined$select$lambda$1.p$0 = obj;
        return combineKt$combineInternal$2$invokeSuspend$$inlined$select$lambda$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((CombineKt$combineInternal$2$invokeSuspend$$inlined$select$lambda$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Object obj2 = this.p$0;
            if (this.$latestValues$inlined[this.$i] == null) {
                r1.element--;
                int i2 = this.$remainingNulls$inlined.element;
            }
            this.$latestValues$inlined[this.$i] = obj2;
            if (this.$remainingNulls$inlined.element != 0) {
                return Unit.INSTANCE;
            }
            Object[] objArr = (Object[]) this.this$0.$arrayFactory.invoke();
            int i3 = this.$size$inlined;
            for (int i4 = 0; i4 < i3; i4++) {
                Symbol symbol = NullSurrogateKt.NULL;
                Object obj3 = this.$latestValues$inlined[i4];
                if (obj3 == symbol) {
                    obj3 = null;
                }
                objArr[i4] = obj3;
            }
            Function3 function3 = this.this$0.$transform;
            FlowCollector flowCollector = this.this$0.$this_combineInternal;
            if (objArr == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            this.L$0 = obj2;
            this.L$1 = objArr;
            this.label = 1;
            InlineMarker.mark(6);
            Object invoke = function3.invoke(flowCollector, objArr, this);
            InlineMarker.mark(7);
            if (invoke == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else {
            if (i != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
        }
        return Unit.INSTANCE;
    }
}
