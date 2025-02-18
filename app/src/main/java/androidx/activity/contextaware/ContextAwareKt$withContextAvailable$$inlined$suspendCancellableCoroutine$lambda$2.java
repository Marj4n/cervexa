package androidx.activity.contextaware;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

/* compiled from: ContextAware.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\n¢\u0006\u0002\b\u0005¨\u0006\u0006"}, d2 = {"<anonymous>", "", "R", "it", "", "invoke", "androidx/activity/contextaware/ContextAwareKt$withContextAvailable$2$1"}, k = 3, mv = {1, 4, 0})
/* loaded from: classes.dex */
public final class ContextAwareKt$withContextAvailable$$inlined$suspendCancellableCoroutine$lambda$2 extends Lambda implements Function1<Throwable, Unit> {
    final /* synthetic */ ContextAwareKt$withContextAvailable$$inlined$suspendCancellableCoroutine$lambda$1 $listener;
    final /* synthetic */ Function1 $onContextAvailable$inlined;
    final /* synthetic */ ContextAware $this_withContextAvailable$inlined;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ContextAwareKt$withContextAvailable$$inlined$suspendCancellableCoroutine$lambda$2(ContextAwareKt$withContextAvailable$$inlined$suspendCancellableCoroutine$lambda$1 contextAwareKt$withContextAvailable$$inlined$suspendCancellableCoroutine$lambda$1, ContextAware contextAware, Function1 function1) {
        super(1);
        this.$listener = contextAwareKt$withContextAvailable$$inlined$suspendCancellableCoroutine$lambda$1;
        this.$this_withContextAvailable$inlined = contextAware;
        this.$onContextAvailable$inlined = function1;
    }

    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
        invoke2(th);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke, reason: avoid collision after fix types in other method */
    public final void invoke2(Throwable th) {
        this.$this_withContextAvailable$inlined.removeOnContextAvailableListener(this.$listener);
    }
}
