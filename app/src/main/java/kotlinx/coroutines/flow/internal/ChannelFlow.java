package kotlinx.coroutines.flow.internal;

import com.google.zxing.client.result.optional.NDEFRecord;
import com.tencent.connect.common.Constants;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.DebugStringsKt;
import kotlinx.coroutines.channels.BroadcastChannel;
import kotlinx.coroutines.channels.BroadcastKt;
import kotlinx.coroutines.channels.ProduceKt;
import kotlinx.coroutines.channels.ProducerScope;
import kotlinx.coroutines.channels.ReceiveChannel;
import kotlinx.coroutines.flow.FlowCollector;

/* compiled from: ChannelFlow.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\b'\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\u001e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00028\u00000\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\u001f\u0010\u001b\u001a\u00020\f2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00028\u00000\u001dH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u001eJ\u001f\u0010\u001f\u001a\u00020\f2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\nH¤@ø\u0001\u0000¢\u0006\u0002\u0010 J\u001e\u0010!\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H$J\u001e\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0016\u0010#\u001a\b\u0012\u0004\u0012\u00028\u00000$2\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010%\u001a\u00020\u0014H\u0016R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R9\u0010\b\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b\u0012\u0006\u0012\u0004\u0018\u00010\r0\t8@X\u0080\u0004ø\u0001\u0000¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\u00020\u00068BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006&"}, d2 = {"Lkotlinx/coroutines/flow/internal/ChannelFlow;", NDEFRecord.TEXT_WELL_KNOWN_TYPE, "Lkotlinx/coroutines/flow/internal/FusibleFlow;", "context", "Lkotlin/coroutines/CoroutineContext;", "capacity", "", "(Lkotlin/coroutines/CoroutineContext;I)V", "collectToFun", "Lkotlin/Function2;", "Lkotlinx/coroutines/channels/ProducerScope;", "Lkotlin/coroutines/Continuation;", "", "", "getCollectToFun$kotlinx_coroutines_core", "()Lkotlin/jvm/functions/Function2;", "produceCapacity", "getProduceCapacity", "()I", "additionalToStringProps", "", "broadcastImpl", "Lkotlinx/coroutines/channels/BroadcastChannel;", Constants.PARAM_SCOPE, "Lkotlinx/coroutines/CoroutineScope;", "start", "Lkotlinx/coroutines/CoroutineStart;", "collect", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "collectTo", "(Lkotlinx/coroutines/channels/ProducerScope;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "create", "fuse", "produceImpl", "Lkotlinx/coroutines/channels/ReceiveChannel;", "toString", "kotlinx-coroutines-core"}, k = 1, mv = {1, 4, 0})
/* loaded from: classes2.dex */
public abstract class ChannelFlow<T> implements FusibleFlow<T> {
    public final int capacity;
    public final CoroutineContext context;

    public String additionalToStringProps() {
        return "";
    }

    @Override // kotlinx.coroutines.flow.Flow
    public Object collect(FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation) {
        return collect$suspendImpl(this, flowCollector, continuation);
    }

    protected abstract Object collectTo(ProducerScope<? super T> producerScope, Continuation<? super Unit> continuation);

    protected abstract ChannelFlow<T> create(CoroutineContext context, int capacity);

    public ChannelFlow(CoroutineContext coroutineContext, int i) {
        this.context = coroutineContext;
        this.capacity = i;
    }

    public final Function2<ProducerScope<? super T>, Continuation<? super Unit>, Object> getCollectToFun$kotlinx_coroutines_core() {
        return new ChannelFlow$collectToFun$1(this, null);
    }

    private final int getProduceCapacity() {
        int i = this.capacity;
        if (i == -3) {
            return -2;
        }
        return i;
    }

    @Override // kotlinx.coroutines.flow.internal.FusibleFlow
    public FusibleFlow<T> fuse(CoroutineContext context, int capacity) {
        CoroutineContext plus = context.plus(this.context);
        int i = this.capacity;
        if (i != -3) {
            if (capacity != -3) {
                if (i != -2) {
                    if (capacity != -2) {
                        if (i == -1 || capacity == -1) {
                            capacity = -1;
                        } else {
                            if (DebugKt.getASSERTIONS_ENABLED()) {
                                if (!(this.capacity >= 0)) {
                                    throw new AssertionError();
                                }
                            }
                            if (DebugKt.getASSERTIONS_ENABLED()) {
                                if (!(capacity >= 0)) {
                                    throw new AssertionError();
                                }
                            }
                            capacity += this.capacity;
                            if (capacity < 0) {
                                capacity = Integer.MAX_VALUE;
                            }
                        }
                    }
                }
            }
            capacity = i;
        }
        return (Intrinsics.areEqual(plus, this.context) && capacity == this.capacity) ? this : create(plus, capacity);
    }

    public BroadcastChannel<T> broadcastImpl(CoroutineScope scope, CoroutineStart start) {
        return BroadcastKt.broadcast$default(scope, this.context, getProduceCapacity(), start, null, getCollectToFun$kotlinx_coroutines_core(), 8, null);
    }

    public ReceiveChannel<T> produceImpl(CoroutineScope scope) {
        return ProduceKt.produce$default(scope, this.context, getProduceCapacity(), CoroutineStart.ATOMIC, null, getCollectToFun$kotlinx_coroutines_core(), 8, null);
    }

    static /* synthetic */ Object collect$suspendImpl(ChannelFlow channelFlow, FlowCollector flowCollector, Continuation continuation) {
        Object coroutineScope = CoroutineScopeKt.coroutineScope(new ChannelFlow$collect$2(channelFlow, flowCollector, null), continuation);
        return coroutineScope == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? coroutineScope : Unit.INSTANCE;
    }

    public String toString() {
        return DebugStringsKt.getClassSimpleName(this) + '[' + additionalToStringProps() + "context=" + this.context + ", capacity=" + this.capacity + ']';
    }
}
