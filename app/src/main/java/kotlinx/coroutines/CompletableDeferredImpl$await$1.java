package kotlinx.coroutines;

import com.google.zxing.client.result.optional.NDEFRecord;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

/* compiled from: CompletableDeferred.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u0004\u0018\u00010\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u0096@"}, d2 = {"await", "", NDEFRecord.TEXT_WELL_KNOWN_TYPE, "continuation", "Lkotlin/coroutines/Continuation;"}, k = 3, mv = {1, 4, 0})
@DebugMetadata(c = "kotlinx.coroutines.CompletableDeferredImpl", f = "CompletableDeferred.kt", i = {0}, l = {87}, m = "await", n = {"this"}, s = {"L$0"})
/* loaded from: classes2.dex */
final class CompletableDeferredImpl$await$1 extends ContinuationImpl {
    Object L$0;
    int label;
    /* synthetic */ Object result;
    final /* synthetic */ CompletableDeferredImpl this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    CompletableDeferredImpl$await$1(CompletableDeferredImpl completableDeferredImpl, Continuation continuation) {
        super(continuation);
        this.this$0 = completableDeferredImpl;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return this.this$0.await(this);
    }
}
