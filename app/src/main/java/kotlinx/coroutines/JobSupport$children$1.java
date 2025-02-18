package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.SequenceScope;

/* compiled from: JobSupport.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Lkotlinx/coroutines/ChildJob;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 4, 0})
@DebugMetadata(c = "kotlinx.coroutines.JobSupport$children$1", f = "JobSupport.kt", i = {0, 0, 1, 1, 1, 1, 1, 1}, l = {949, 951}, m = "invokeSuspend", n = {"$this$sequence", "state", "$this$sequence", "state", "list", "this_$iv", "cur$iv", "it"}, s = {"L$0", "L$1", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5"})
/* loaded from: classes2.dex */
final class JobSupport$children$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super ChildJob>, Continuation<? super Unit>, Object> {
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    Object L$5;
    int label;
    private SequenceScope p$;
    final /* synthetic */ JobSupport this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    JobSupport$children$1(JobSupport jobSupport, Continuation continuation) {
        super(2, continuation);
        this.this$0 = jobSupport;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        JobSupport$children$1 jobSupport$children$1 = new JobSupport$children$1(this.this$0, continuation);
        jobSupport$children$1.p$ = (SequenceScope) obj;
        return jobSupport$children$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(SequenceScope<? super ChildJob> sequenceScope, Continuation<? super Unit> continuation) {
        return ((JobSupport$children$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x0083  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:10:0x0085 -> B:6:0x00a1). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:12:0x009e -> B:6:0x00a1). Please report as a decompilation issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r13) {
        /*
            r12 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r12.label
            r2 = 2
            r3 = 1
            if (r1 == 0) goto L3b
            if (r1 == r3) goto L32
            if (r1 != r2) goto L2a
            java.lang.Object r1 = r12.L$5
            kotlinx.coroutines.ChildHandleNode r1 = (kotlinx.coroutines.ChildHandleNode) r1
            java.lang.Object r1 = r12.L$4
            kotlinx.coroutines.internal.LockFreeLinkedListNode r1 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r1
            java.lang.Object r4 = r12.L$3
            kotlinx.coroutines.internal.LockFreeLinkedListHead r4 = (kotlinx.coroutines.internal.LockFreeLinkedListHead) r4
            java.lang.Object r5 = r12.L$2
            kotlinx.coroutines.NodeList r5 = (kotlinx.coroutines.NodeList) r5
            java.lang.Object r6 = r12.L$1
            java.lang.Object r7 = r12.L$0
            kotlin.sequences.SequenceScope r7 = (kotlin.sequences.SequenceScope) r7
            kotlin.ResultKt.throwOnFailure(r13)
            r13 = r12
            goto La1
        L2a:
            java.lang.IllegalStateException r13 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r13.<init>(r0)
            throw r13
        L32:
            java.lang.Object r0 = r12.L$0
            kotlin.sequences.SequenceScope r0 = (kotlin.sequences.SequenceScope) r0
            kotlin.ResultKt.throwOnFailure(r13)
            goto Lae
        L3b:
            kotlin.ResultKt.throwOnFailure(r13)
            kotlin.sequences.SequenceScope r13 = r12.p$
            kotlinx.coroutines.JobSupport r1 = r12.this$0
            java.lang.Object r1 = r1.getState$kotlinx_coroutines_core()
            boolean r4 = r1 instanceof kotlinx.coroutines.ChildHandleNode
            if (r4 == 0) goto L5c
            r2 = r1
            kotlinx.coroutines.ChildHandleNode r2 = (kotlinx.coroutines.ChildHandleNode) r2
            kotlinx.coroutines.ChildJob r2 = r2.childJob
            r12.L$0 = r13
            r12.L$1 = r1
            r12.label = r3
            java.lang.Object r13 = r13.yield(r2, r12)
            if (r13 != r0) goto Lae
            return r0
        L5c:
            boolean r4 = r1 instanceof kotlinx.coroutines.Incomplete
            if (r4 == 0) goto Lae
            r4 = r1
            kotlinx.coroutines.Incomplete r4 = (kotlinx.coroutines.Incomplete) r4
            kotlinx.coroutines.NodeList r4 = r4.getList()
            if (r4 == 0) goto Lae
            r5 = r4
            kotlinx.coroutines.internal.LockFreeLinkedListHead r5 = (kotlinx.coroutines.internal.LockFreeLinkedListHead) r5
            java.lang.Object r6 = r5.getNext()
            if (r6 == 0) goto La6
            kotlinx.coroutines.internal.LockFreeLinkedListNode r6 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r6
            r7 = r13
            r13 = r12
            r10 = r6
            r6 = r1
            r1 = r10
            r11 = r5
            r5 = r4
            r4 = r11
        L7c:
            boolean r8 = kotlin.jvm.internal.Intrinsics.areEqual(r1, r4)
            r8 = r8 ^ r3
            if (r8 == 0) goto Lae
            boolean r8 = r1 instanceof kotlinx.coroutines.ChildHandleNode
            if (r8 == 0) goto La1
            r8 = r1
            kotlinx.coroutines.ChildHandleNode r8 = (kotlinx.coroutines.ChildHandleNode) r8
            kotlinx.coroutines.ChildJob r9 = r8.childJob
            r13.L$0 = r7
            r13.L$1 = r6
            r13.L$2 = r5
            r13.L$3 = r4
            r13.L$4 = r1
            r13.L$5 = r8
            r13.label = r2
            java.lang.Object r8 = r7.yield(r9, r13)
            if (r8 != r0) goto La1
            return r0
        La1:
            kotlinx.coroutines.internal.LockFreeLinkedListNode r1 = r1.getNextNode()
            goto L7c
        La6:
            java.lang.NullPointerException r13 = new java.lang.NullPointerException
        */
        //  java.lang.String r0 = "null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */"
        /*
            r13.<init>(r0)
            throw r13
        Lae:
            kotlin.Unit r13 = kotlin.Unit.INSTANCE
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport$children$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
