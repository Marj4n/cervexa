package kotlinx.coroutines.flow;

import com.google.zxing.client.result.optional.NDEFRecord;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.InlineMarker;

/* JADX INFO: Add missing generic type declarations: [R] */
/* compiled from: SafeCollector.common.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001f\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0007¸\u0006\b"}, d2 = {"kotlinx/coroutines/flow/internal/SafeCollector_commonKt$unsafeFlow$1", "Lkotlinx/coroutines/flow/Flow;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core", "kotlinx/coroutines/flow/FlowKt__TransformKt$unsafeTransform$$inlined$unsafeFlow$5"}, k = 1, mv = {1, 4, 0})
/* loaded from: classes2.dex */
public final class FlowKt__TransformKt$map$$inlined$unsafeTransform$1<R> implements Flow<R> {
    final /* synthetic */ Flow $this_unsafeTransform$inlined;
    final /* synthetic */ Function2 $transform$inlined$1;

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* compiled from: Collect.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u0000H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0005\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0006¸\u0006\b"}, d2 = {"kotlinx/coroutines/flow/FlowKt__CollectKt$collect$3", "Lkotlinx/coroutines/flow/FlowCollector;", "emit", "", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core", "kotlinx/coroutines/flow/FlowKt__TransformKt$$special$$inlined$collect$5", "kotlinx/coroutines/flow/FlowKt__TransformKt$unsafeTransform$$inlined$unsafeFlow$5$lambda$1"}, k = 1, mv = {1, 4, 0})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1$2, reason: invalid class name */
    public static final class AnonymousClass2<T> implements FlowCollector<T> {
        final /* synthetic */ FlowCollector $this_unsafeFlow$inlined;
        final /* synthetic */ FlowKt__TransformKt$map$$inlined$unsafeTransform$1 this$0;

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u0004\u0018\u00010\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u00022\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0096@¨\u0006\t"}, d2 = {"emit", "", NDEFRecord.TEXT_WELL_KNOWN_TYPE, "value", "continuation", "Lkotlin/coroutines/Continuation;", "", "kotlinx/coroutines/flow/FlowKt__CollectKt$collect$3$emit$1", "kotlinx/coroutines/flow/FlowKt__TransformKt$$special$$inlined$collect$5$1", "kotlinx/coroutines/flow/FlowKt__TransformKt$unsafeTransform$$inlined$unsafeFlow$5$lambda$1$1"}, k = 3, mv = {1, 4, 0})
        @DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1$2", f = "Transform.kt", i = {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1}, l = {134, 134}, m = "emit", n = {"this", "value", "continuation", "value", "continuation", "value", "$receiver", "this", "value", "continuation", "value", "continuation", "value", "$receiver"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6"})
        /* renamed from: kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1$2$1, reason: invalid class name */
        public static final class AnonymousClass1 extends ContinuationImpl {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
            Object L$5;
            Object L$6;
            Object L$7;
            int label;
            /* synthetic */ Object result;

            public AnonymousClass1(Continuation continuation) {
                super(continuation);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj) {
                this.result = obj;
                this.label |= Integer.MIN_VALUE;
                return AnonymousClass2.this.emit(null, this);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x00ad A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:20:0x006a  */
        /* JADX WARN: Removed duplicated region for block: B:8:0x0025  */
        @Override // kotlinx.coroutines.flow.FlowCollector
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public java.lang.Object emit(java.lang.Object r11, kotlin.coroutines.Continuation r12) {
            /*
                r10 = this;
                boolean r0 = r12 instanceof kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1.AnonymousClass2.AnonymousClass1
                if (r0 == 0) goto L14
                r0 = r12
                kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1$2$1 r0 = (kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1.AnonymousClass2.AnonymousClass1) r0
                int r1 = r0.label
                r2 = -2147483648(0xffffffff80000000, float:-0.0)
                r1 = r1 & r2
                if (r1 == 0) goto L14
                int r12 = r0.label
                int r12 = r12 - r2
                r0.label = r12
                goto L19
            L14:
                kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1$2$1 r0 = new kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1$2$1
                r0.<init>(r12)
            L19:
                java.lang.Object r12 = r0.result
                java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                int r2 = r0.label
                r3 = 2
                r4 = 1
                if (r2 == 0) goto L6a
                if (r2 == r4) goto L4c
                if (r2 != r3) goto L44
                java.lang.Object r11 = r0.L$6
                kotlinx.coroutines.flow.FlowCollector r11 = (kotlinx.coroutines.flow.FlowCollector) r11
                java.lang.Object r11 = r0.L$5
                java.lang.Object r11 = r0.L$4
                kotlin.coroutines.Continuation r11 = (kotlin.coroutines.Continuation) r11
                java.lang.Object r11 = r0.L$3
                java.lang.Object r11 = r0.L$2
                kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1$2$1 r11 = (kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1.AnonymousClass2.AnonymousClass1) r11
                java.lang.Object r11 = r0.L$1
                java.lang.Object r11 = r0.L$0
                kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1$2 r11 = (kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1.AnonymousClass2) r11
                kotlin.ResultKt.throwOnFailure(r12)
                goto Lae
            L44:
                java.lang.IllegalStateException r11 = new java.lang.IllegalStateException
                java.lang.String r12 = "call to 'resume' before 'invoke' with coroutine"
                r11.<init>(r12)
                throw r11
            L4c:
                java.lang.Object r11 = r0.L$7
                kotlinx.coroutines.flow.FlowCollector r11 = (kotlinx.coroutines.flow.FlowCollector) r11
                java.lang.Object r2 = r0.L$6
                kotlinx.coroutines.flow.FlowCollector r2 = (kotlinx.coroutines.flow.FlowCollector) r2
                java.lang.Object r4 = r0.L$5
                java.lang.Object r5 = r0.L$4
                kotlin.coroutines.Continuation r5 = (kotlin.coroutines.Continuation) r5
                java.lang.Object r6 = r0.L$3
                java.lang.Object r7 = r0.L$2
                kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1$2$1 r7 = (kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1.AnonymousClass2.AnonymousClass1) r7
                java.lang.Object r8 = r0.L$1
                java.lang.Object r9 = r0.L$0
                kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1$2 r9 = (kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1.AnonymousClass2) r9
                kotlin.ResultKt.throwOnFailure(r12)
                goto L97
            L6a:
                kotlin.ResultKt.throwOnFailure(r12)
                kotlinx.coroutines.flow.FlowCollector r12 = r10.$this_unsafeFlow$inlined
                r5 = r0
                kotlin.coroutines.Continuation r5 = (kotlin.coroutines.Continuation) r5
                kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1 r2 = r10.this$0
                kotlin.jvm.functions.Function2 r2 = r2.$transform$inlined$1
                r0.L$0 = r10
                r0.L$1 = r11
                r0.L$2 = r0
                r0.L$3 = r11
                r0.L$4 = r5
                r0.L$5 = r11
                r0.L$6 = r12
                r0.L$7 = r12
                r0.label = r4
                java.lang.Object r2 = r2.invoke(r11, r0)
                if (r2 != r1) goto L8f
                return r1
            L8f:
                r9 = r10
                r4 = r11
                r6 = r4
                r8 = r6
                r11 = r12
                r7 = r0
                r12 = r2
                r2 = r11
            L97:
                r0.L$0 = r9
                r0.L$1 = r8
                r0.L$2 = r7
                r0.L$3 = r6
                r0.L$4 = r5
                r0.L$5 = r4
                r0.L$6 = r2
                r0.label = r3
                java.lang.Object r11 = r11.emit(r12, r0)
                if (r11 != r1) goto Lae
                return r1
            Lae:
                kotlin.Unit r11 = kotlin.Unit.INSTANCE
                return r11
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1.AnonymousClass2.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
        }

        public AnonymousClass2(FlowCollector flowCollector, FlowKt__TransformKt$map$$inlined$unsafeTransform$1 flowKt__TransformKt$map$$inlined$unsafeTransform$1) {
            this.$this_unsafeFlow$inlined = flowCollector;
            this.this$0 = flowKt__TransformKt$map$$inlined$unsafeTransform$1;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Object emit$$forInline(Object obj, Continuation continuation) {
            InlineMarker.mark(4);
            new AnonymousClass1(continuation);
            InlineMarker.mark(5);
            FlowCollector flowCollector = this.$this_unsafeFlow$inlined;
            Object invoke = this.this$0.$transform$inlined$1.invoke(obj, continuation);
            InlineMarker.mark(0);
            Object emit = flowCollector.emit(invoke, continuation);
            InlineMarker.mark(2);
            InlineMarker.mark(1);
            return emit;
        }
    }

    public FlowKt__TransformKt$map$$inlined$unsafeTransform$1(Flow flow, Function2 function2) {
        this.$this_unsafeTransform$inlined = flow;
        this.$transform$inlined$1 = function2;
    }

    @Override // kotlinx.coroutines.flow.Flow
    public Object collect(FlowCollector flowCollector, Continuation continuation) {
        Object collect = this.$this_unsafeTransform$inlined.collect(new AnonymousClass2(flowCollector, this), continuation);
        return collect == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? collect : Unit.INSTANCE;
    }

    public Object collect$$forInline(FlowCollector flowCollector, Continuation continuation) {
        InlineMarker.mark(4);
        new ContinuationImpl(continuation) { // from class: kotlinx.coroutines.flow.FlowKt__TransformKt$map$$inlined$unsafeTransform$1.1
            int label;
            /* synthetic */ Object result;

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj) {
                this.result = obj;
                this.label |= Integer.MIN_VALUE;
                return FlowKt__TransformKt$map$$inlined$unsafeTransform$1.this.collect(null, this);
            }
        };
        InlineMarker.mark(5);
        Flow flow = this.$this_unsafeTransform$inlined;
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(flowCollector, this);
        InlineMarker.mark(0);
        flow.collect(anonymousClass2, continuation);
        InlineMarker.mark(2);
        InlineMarker.mark(1);
        return Unit.INSTANCE;
    }
}
