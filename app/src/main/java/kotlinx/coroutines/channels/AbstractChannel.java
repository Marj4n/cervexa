package kotlinx.coroutines.channels;

import java.util.ArrayList;
import java.util.concurrent.CancellationException;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.ContinuationKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CancelHandler;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CancellableContinuationImplKt;
import kotlinx.coroutines.CancellableContinuationKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.DebugStringsKt;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.channels.ChannelIterator;
import kotlinx.coroutines.channels.ValueOrClosed;
import kotlinx.coroutines.internal.AtomicKt;
import kotlinx.coroutines.internal.InlineList;
import kotlinx.coroutines.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.internal.LockFreeLinkedListKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.LockFreeLinkedList_commonKt;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.intrinsics.UndispatchedKt;
import kotlinx.coroutines.selects.SelectClause1;
import kotlinx.coroutines.selects.SelectInstance;
import kotlinx.coroutines.selects.SelectKt;

/* compiled from: AbstractChannel.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0080\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0003\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\b \u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u0006MNOPQRB\u0005¢\u0006\u0002\u0010\u0004J\u0012\u0010\u0017\u001a\u00020\u00062\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0007J\u0016\u0010\u0017\u001a\u00020\u001a2\u000e\u0010\u0018\u001a\n\u0018\u00010\u001bj\u0004\u0018\u0001`\u001cJ\u0017\u0010\u001d\u001a\u00020\u00062\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0000¢\u0006\u0002\b\u001eJ\u000e\u0010\u001f\u001a\b\u0012\u0004\u0012\u00028\u00000 H\u0004J\u0016\u0010!\u001a\u00020\u00062\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0002J\u0016\u0010$\u001a\u00020\u00062\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0014JR\u0010%\u001a\u00020\u0006\"\u0004\b\u0001\u0010&2\f\u0010'\u001a\b\u0012\u0004\u0012\u0002H&0(2$\u0010)\u001a \b\u0001\u0012\u0006\u0012\u0004\u0018\u00010+\u0012\n\u0012\b\u0012\u0004\u0012\u0002H&0,\u0012\u0006\u0012\u0004\u0018\u00010+0*2\u0006\u0010-\u001a\u00020.H\u0002ø\u0001\u0000¢\u0006\u0002\u0010/J\u000f\u00100\u001a\b\u0012\u0004\u0012\u00028\u000001H\u0086\u0002J\u0010\u00102\u001a\u00020\u001a2\u0006\u00103\u001a\u00020\u0006H\u0014J\b\u00104\u001a\u00020\u001aH\u0014J\b\u00105\u001a\u00020\u001aH\u0014J\r\u00106\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u00107J\n\u00108\u001a\u0004\u0018\u00010+H\u0014J\u0016\u00109\u001a\u0004\u0018\u00010+2\n\u0010'\u001a\u0006\u0012\u0002\b\u00030(H\u0014J\u0011\u0010\"\u001a\u00028\u0000H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010:J\u001f\u0010;\u001a\b\u0012\u0004\u0012\u00028\u00000\u0013H\u0086@ø\u0001\u0000ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b<\u0010:J\u0013\u0010=\u001a\u0004\u0018\u00018\u0000H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010:J\u0019\u0010>\u001a\u0004\u0018\u00018\u00002\b\u0010?\u001a\u0004\u0018\u00010+H\u0002¢\u0006\u0002\u0010@J\u001f\u0010A\u001a\u0002H&\"\u0004\b\u0001\u0010&2\u0006\u0010-\u001a\u00020.H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010BJR\u0010C\u001a\u00020\u001a\"\u0004\b\u0001\u0010&2\f\u0010'\u001a\b\u0012\u0004\u0012\u0002H&0(2\u0006\u0010-\u001a\u00020.2$\u0010)\u001a \b\u0001\u0012\u0006\u0012\u0004\u0018\u00010+\u0012\n\u0012\b\u0012\u0004\u0012\u0002H&0,\u0012\u0006\u0012\u0004\u0018\u00010+0*H\u0002ø\u0001\u0000¢\u0006\u0002\u0010DJ \u0010E\u001a\u00020\u001a2\n\u0010F\u001a\u0006\u0012\u0002\b\u00030G2\n\u0010\"\u001a\u0006\u0012\u0002\b\u00030#H\u0002J\u0010\u0010H\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010IH\u0014JX\u0010J\u001a\u00020\u001a\"\u0004\b\u0001\u0010&* \b\u0001\u0012\u0006\u0012\u0004\u0018\u00010+\u0012\n\u0012\b\u0012\u0004\u0012\u0002H&0,\u0012\u0006\u0012\u0004\u0018\u00010+0*2\f\u0010'\u001a\b\u0012\u0004\u0012\u0002H&0(2\u0006\u0010-\u001a\u00020.2\b\u0010K\u001a\u0004\u0018\u00010+H\u0002ø\u0001\u0000¢\u0006\u0002\u0010LR\u0014\u0010\u0005\u001a\u00020\u00068DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0012\u0010\t\u001a\u00020\u0006X¤\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\bR\u0012\u0010\n\u001a\u00020\u0006X¤\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\bR\u0014\u0010\u000b\u001a\u00020\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\bR\u0014\u0010\f\u001a\u00020\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\bR\u0014\u0010\r\u001a\u00020\u00068DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\bR\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u000f8F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R \u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00130\u000f8Fø\u0001\u0000¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0011R\u0019\u0010\u0015\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u000f8F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0011\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006S"}, d2 = {"Lkotlinx/coroutines/channels/AbstractChannel;", "E", "Lkotlinx/coroutines/channels/AbstractSendChannel;", "Lkotlinx/coroutines/channels/Channel;", "()V", "hasReceiveOrClosed", "", "getHasReceiveOrClosed", "()Z", "isBufferAlwaysEmpty", "isBufferEmpty", "isClosedForReceive", "isEmpty", "isEmptyImpl", "onReceive", "Lkotlinx/coroutines/selects/SelectClause1;", "getOnReceive", "()Lkotlinx/coroutines/selects/SelectClause1;", "onReceiveOrClosed", "Lkotlinx/coroutines/channels/ValueOrClosed;", "getOnReceiveOrClosed", "onReceiveOrNull", "getOnReceiveOrNull", "cancel", "cause", "", "", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "cancelInternal", "cancelInternal$kotlinx_coroutines_core", "describeTryPoll", "Lkotlinx/coroutines/channels/AbstractChannel$TryPollDesc;", "enqueueReceive", "receive", "Lkotlinx/coroutines/channels/Receive;", "enqueueReceiveInternal", "enqueueReceiveSelect", "R", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "block", "Lkotlin/Function2;", "", "Lkotlin/coroutines/Continuation;", "receiveMode", "", "(Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function2;I)Z", "iterator", "Lkotlinx/coroutines/channels/ChannelIterator;", "onCancelIdempotent", "wasClosed", "onReceiveDequeued", "onReceiveEnqueued", "poll", "()Ljava/lang/Object;", "pollInternal", "pollSelectInternal", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "receiveOrClosed", "receiveOrClosed-ZYPwvRU", "receiveOrNull", "receiveOrNullResult", "result", "(Ljava/lang/Object;)Ljava/lang/Object;", "receiveSuspend", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "registerSelectReceiveMode", "(Lkotlinx/coroutines/selects/SelectInstance;ILkotlin/jvm/functions/Function2;)V", "removeReceiveOnCancel", "cont", "Lkotlinx/coroutines/CancellableContinuation;", "takeFirstReceiveOrPeekClosed", "Lkotlinx/coroutines/channels/ReceiveOrClosed;", "tryStartBlockUnintercepted", "value", "(Lkotlin/jvm/functions/Function2;Lkotlinx/coroutines/selects/SelectInstance;ILjava/lang/Object;)V", "Itr", "ReceiveElement", "ReceiveHasNext", "ReceiveSelect", "RemoveReceiveOnCancel", "TryPollDesc", "kotlinx-coroutines-core"}, k = 1, mv = {1, 4, 0})
/* loaded from: classes2.dex */
public abstract class AbstractChannel<E> extends AbstractSendChannel<E> implements Channel<E> {
    protected abstract boolean isBufferAlwaysEmpty();

    protected abstract boolean isBufferEmpty();

    protected void onReceiveDequeued() {
    }

    protected void onReceiveEnqueued() {
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Since 1.2.0, binary compatibility with versions <= 1.1.x")
    public /* synthetic */ void cancel() {
        cancel((CancellationException) null);
    }

    protected Object pollInternal() {
        Send takeFirstSendOrPeekClosed;
        Symbol tryResumeSend;
        do {
            takeFirstSendOrPeekClosed = takeFirstSendOrPeekClosed();
            if (takeFirstSendOrPeekClosed == null) {
                return AbstractChannelKt.POLL_FAILED;
            }
            tryResumeSend = takeFirstSendOrPeekClosed.tryResumeSend(null);
        } while (tryResumeSend == null);
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(tryResumeSend == CancellableContinuationImplKt.RESUME_TOKEN)) {
                throw new AssertionError();
            }
        }
        takeFirstSendOrPeekClosed.completeResumeSend();
        return takeFirstSendOrPeekClosed.getPollResult();
    }

    protected Object pollSelectInternal(SelectInstance<?> select) {
        TryPollDesc<E> describeTryPoll = describeTryPoll();
        Object performAtomicTrySelect = select.performAtomicTrySelect(describeTryPoll);
        if (performAtomicTrySelect != null) {
            return performAtomicTrySelect;
        }
        describeTryPoll.getResult().completeResumeSend();
        return describeTryPoll.getResult().getPollResult();
    }

    protected final boolean getHasReceiveOrClosed() {
        return getQueue().getNextNode() instanceof ReceiveOrClosed;
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public boolean isClosedForReceive() {
        return getClosedForReceive() != null && isBufferEmpty();
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public boolean isEmpty() {
        return isEmptyImpl();
    }

    protected final boolean isEmptyImpl() {
        return !(getQueue().getNextNode() instanceof Send) && isBufferEmpty();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public final Object receive(Continuation<? super E> continuation) {
        Object pollInternal = pollInternal();
        return (pollInternal == AbstractChannelKt.POLL_FAILED || (pollInternal instanceof Closed)) ? receiveSuspend(0, continuation) : pollInternal;
    }

    protected boolean enqueueReceiveInternal(Receive<? super E> receive) {
        int tryCondAddNext;
        LockFreeLinkedListNode prevNode;
        if (isBufferAlwaysEmpty()) {
            LockFreeLinkedListHead queue = getQueue();
            do {
                prevNode = queue.getPrevNode();
                if (!(!(prevNode instanceof Send))) {
                    return false;
                }
            } while (!prevNode.addNext(receive, queue));
        } else {
            LockFreeLinkedListHead queue2 = getQueue();
            final Receive<? super E> receive2 = receive;
            LockFreeLinkedListNode.CondAddOp condAddOp = new LockFreeLinkedListNode.CondAddOp(receive2) { // from class: kotlinx.coroutines.channels.AbstractChannel$enqueueReceiveInternal$$inlined$addLastIfPrevAndIf$1
                @Override // kotlinx.coroutines.internal.AtomicOp
                public Object prepare(LockFreeLinkedListNode affected) {
                    if (this.isBufferEmpty()) {
                        return null;
                    }
                    return LockFreeLinkedListKt.getCONDITION_FALSE();
                }
            };
            do {
                LockFreeLinkedListNode prevNode2 = queue2.getPrevNode();
                if (!(!(prevNode2 instanceof Send))) {
                    return false;
                }
                tryCondAddNext = prevNode2.tryCondAddNext(receive2, queue2, condAddOp);
                if (tryCondAddNext != 1) {
                }
            } while (tryCondAddNext != 2);
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean enqueueReceive(Receive<? super E> receive) {
        boolean enqueueReceiveInternal = enqueueReceiveInternal(receive);
        if (enqueueReceiveInternal) {
            onReceiveEnqueued();
        }
        return enqueueReceiveInternal;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public final Object receiveOrNull(Continuation<? super E> continuation) {
        Object pollInternal = pollInternal();
        return (pollInternal == AbstractChannelKt.POLL_FAILED || (pollInternal instanceof Closed)) ? receiveSuspend(1, continuation) : pollInternal;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final E receiveOrNullResult(Object result) {
        if (!(result instanceof Closed)) {
            return result;
        }
        Closed closed = (Closed) result;
        if (closed.closeCause == null) {
            return null;
        }
        throw StackTraceRecoveryKt.recoverStackTrace(closed.closeCause);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0038  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0024  */
    @Override // kotlinx.coroutines.channels.ReceiveChannel
    /* renamed from: receiveOrClosed-ZYPwvRU, reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object mo1345receiveOrClosedZYPwvRU(kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.ValueOrClosed<? extends E>> r5) {
        /*
            r4 = this;
            boolean r0 = r5 instanceof kotlinx.coroutines.channels.AbstractChannel$receiveOrClosed$1
            if (r0 == 0) goto L14
            r0 = r5
            kotlinx.coroutines.channels.AbstractChannel$receiveOrClosed$1 r0 = (kotlinx.coroutines.channels.AbstractChannel$receiveOrClosed$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r5 = r0.label
            int r5 = r5 - r2
            r0.label = r5
            goto L19
        L14:
            kotlinx.coroutines.channels.AbstractChannel$receiveOrClosed$1 r0 = new kotlinx.coroutines.channels.AbstractChannel$receiveOrClosed$1
            r0.<init>(r4, r5)
        L19:
            java.lang.Object r5 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L38
            if (r2 != r3) goto L30
            java.lang.Object r1 = r0.L$1
            java.lang.Object r0 = r0.L$0
            kotlinx.coroutines.channels.AbstractChannel r0 = (kotlinx.coroutines.channels.AbstractChannel) r0
            kotlin.ResultKt.throwOnFailure(r5)
            goto L6c
        L30:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r0)
            throw r5
        L38:
            kotlin.ResultKt.throwOnFailure(r5)
            java.lang.Object r5 = r4.pollInternal()
            java.lang.Object r2 = kotlinx.coroutines.channels.AbstractChannelKt.POLL_FAILED
            if (r5 == r2) goto L5e
            boolean r0 = r5 instanceof kotlinx.coroutines.channels.Closed
            if (r0 == 0) goto L57
            kotlinx.coroutines.channels.ValueOrClosed$Companion r0 = kotlinx.coroutines.channels.ValueOrClosed.INSTANCE
            kotlinx.coroutines.channels.Closed r5 = (kotlinx.coroutines.channels.Closed) r5
            java.lang.Throwable r5 = r5.closeCause
            kotlinx.coroutines.channels.ValueOrClosed$Closed r0 = new kotlinx.coroutines.channels.ValueOrClosed$Closed
            r0.<init>(r5)
            java.lang.Object r5 = kotlinx.coroutines.channels.ValueOrClosed.m1348constructorimpl(r0)
            goto L5d
        L57:
            kotlinx.coroutines.channels.ValueOrClosed$Companion r0 = kotlinx.coroutines.channels.ValueOrClosed.INSTANCE
            java.lang.Object r5 = kotlinx.coroutines.channels.ValueOrClosed.m1348constructorimpl(r5)
        L5d:
            return r5
        L5e:
            r2 = 2
            r0.L$0 = r4
            r0.L$1 = r5
            r0.label = r3
            java.lang.Object r5 = r4.receiveSuspend(r2, r0)
            if (r5 != r1) goto L6c
            return r1
        L6c:
            kotlinx.coroutines.channels.ValueOrClosed r5 = (kotlinx.coroutines.channels.ValueOrClosed) r5
            java.lang.Object r5 = r5.getHolder()
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.AbstractChannel.mo1345receiveOrClosedZYPwvRU(kotlin.coroutines.Continuation):java.lang.Object");
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public final E poll() {
        Object pollInternal = pollInternal();
        if (pollInternal == AbstractChannelKt.POLL_FAILED) {
            return null;
        }
        return receiveOrNullResult(pollInternal);
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public final void cancel(CancellationException cause) {
        if (cause == null) {
            cause = new CancellationException(DebugStringsKt.getClassSimpleName(this) + " was cancelled");
        }
        cancel(cause);
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    /* renamed from: cancelInternal$kotlinx_coroutines_core, reason: merged with bridge method [inline-methods] */
    public final boolean cancel(Throwable cause) {
        boolean close = cancel(cause);
        onCancelIdempotent(close);
        return close;
    }

    protected void onCancelIdempotent(boolean wasClosed) {
        Closed<?> closedForSend = getClosedForSend();
        if (closedForSend == null) {
            throw new IllegalStateException("Cannot happen".toString());
        }
        Object m1368constructorimpl$default = InlineList.m1368constructorimpl$default(null, 1, null);
        while (true) {
            LockFreeLinkedListNode prevNode = closedForSend.getPrevNode();
            if (prevNode instanceof LockFreeLinkedListHead) {
                if (m1368constructorimpl$default == null) {
                    return;
                }
                if (!(m1368constructorimpl$default instanceof ArrayList)) {
                    ((Send) m1368constructorimpl$default).resumeSendClosed(closedForSend);
                    return;
                }
                if (m1368constructorimpl$default != null) {
                    ArrayList arrayList = (ArrayList) m1368constructorimpl$default;
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        ((Send) arrayList.get(size)).resumeSendClosed(closedForSend);
                    }
                    return;
                }
                throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.ArrayList<E> /* = java.util.ArrayList<E> */");
            }
            if (DebugKt.getASSERTIONS_ENABLED() && !(prevNode instanceof Send)) {
                throw new AssertionError();
            }
            if (!prevNode.remove()) {
                prevNode.helpRemove();
            } else {
                if (prevNode == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlinx.coroutines.channels.Send");
                }
                m1368constructorimpl$default = InlineList.m1373plusUZ7vuAc(m1368constructorimpl$default, (Send) prevNode);
            }
        }
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public final ChannelIterator<E> iterator() {
        return new Itr(this);
    }

    protected final TryPollDesc<E> describeTryPoll() {
        return new TryPollDesc<>(getQueue());
    }

    /* compiled from: AbstractChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0004\u0018\u0000*\u0004\b\u0001\u0010\u00012\u0012\u0012\u0004\u0012\u00020\u00030\u0002j\b\u0012\u0004\u0012\u00020\u0003`\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0012\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u000bH\u0014J\u0016\u0010\f\u001a\u0004\u0018\u00010\t2\n\u0010\r\u001a\u00060\u000ej\u0002`\u000fH\u0016¨\u0006\u0010"}, d2 = {"Lkotlinx/coroutines/channels/AbstractChannel$TryPollDesc;", "E", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$RemoveFirstDesc;", "Lkotlinx/coroutines/channels/Send;", "Lkotlinx/coroutines/internal/RemoveFirstDesc;", "queue", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "(Lkotlinx/coroutines/internal/LockFreeLinkedListHead;)V", "failure", "", "affected", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "onPrepare", "prepareOp", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$PrepareOp;", "Lkotlinx/coroutines/internal/PrepareOp;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 4, 0})
    protected static final class TryPollDesc<E> extends LockFreeLinkedListNode.RemoveFirstDesc<Send> {
        public TryPollDesc(LockFreeLinkedListHead lockFreeLinkedListHead) {
            super(lockFreeLinkedListHead);
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode.RemoveFirstDesc, kotlinx.coroutines.internal.LockFreeLinkedListNode.AbstractAtomicDesc
        protected Object failure(LockFreeLinkedListNode affected) {
            if (affected instanceof Closed) {
                return affected;
            }
            if (affected instanceof Send) {
                return null;
            }
            return AbstractChannelKt.POLL_FAILED;
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode.AbstractAtomicDesc
        public Object onPrepare(LockFreeLinkedListNode.PrepareOp prepareOp) {
            LockFreeLinkedListNode lockFreeLinkedListNode = prepareOp.affected;
            if (lockFreeLinkedListNode == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlinx.coroutines.channels.Send");
            }
            Symbol tryResumeSend = ((Send) lockFreeLinkedListNode).tryResumeSend(prepareOp);
            if (tryResumeSend == null) {
                return LockFreeLinkedList_commonKt.REMOVE_PREPARED;
            }
            if (tryResumeSend == AtomicKt.RETRY_ATOMIC) {
                return AtomicKt.RETRY_ATOMIC;
            }
            if (!DebugKt.getASSERTIONS_ENABLED()) {
                return null;
            }
            if (tryResumeSend == CancellableContinuationImplKt.RESUME_TOKEN) {
                return null;
            }
            throw new AssertionError();
        }
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public final SelectClause1<E> getOnReceive() {
        return new SelectClause1<E>() { // from class: kotlinx.coroutines.channels.AbstractChannel$onReceive$1
            @Override // kotlinx.coroutines.selects.SelectClause1
            public <R> void registerSelectClause1(SelectInstance<? super R> select, Function2<? super E, ? super Continuation<? super R>, ? extends Object> block) {
                AbstractChannel abstractChannel = AbstractChannel.this;
                if (block == null) {
                    throw new NullPointerException("null cannot be cast to non-null type suspend (kotlin.Any?) -> R");
                }
                abstractChannel.registerSelectReceiveMode(select, 0, block);
            }
        };
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public final SelectClause1<E> getOnReceiveOrNull() {
        return new SelectClause1<E>() { // from class: kotlinx.coroutines.channels.AbstractChannel$onReceiveOrNull$1
            @Override // kotlinx.coroutines.selects.SelectClause1
            public <R> void registerSelectClause1(SelectInstance<? super R> select, Function2<? super E, ? super Continuation<? super R>, ? extends Object> block) {
                AbstractChannel abstractChannel = AbstractChannel.this;
                if (block == null) {
                    throw new NullPointerException("null cannot be cast to non-null type suspend (kotlin.Any?) -> R");
                }
                abstractChannel.registerSelectReceiveMode(select, 1, block);
            }
        };
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public final SelectClause1<ValueOrClosed<E>> getOnReceiveOrClosed() {
        return new SelectClause1<ValueOrClosed<? extends E>>() { // from class: kotlinx.coroutines.channels.AbstractChannel$onReceiveOrClosed$1
            @Override // kotlinx.coroutines.selects.SelectClause1
            public <R> void registerSelectClause1(SelectInstance<? super R> select, Function2<? super ValueOrClosed<? extends E>, ? super Continuation<? super R>, ? extends Object> block) {
                AbstractChannel abstractChannel = AbstractChannel.this;
                if (block == null) {
                    throw new NullPointerException("null cannot be cast to non-null type suspend (kotlin.Any?) -> R");
                }
                abstractChannel.registerSelectReceiveMode(select, 2, block);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final <R> void registerSelectReceiveMode(SelectInstance<? super R> select, int receiveMode, Function2<Object, ? super Continuation<? super R>, ? extends Object> block) {
        while (!select.isSelected()) {
            if (isEmptyImpl()) {
                if (enqueueReceiveSelect(select, block, receiveMode)) {
                    return;
                }
            } else {
                Object pollSelectInternal = pollSelectInternal(select);
                if (pollSelectInternal == SelectKt.getALREADY_SELECTED()) {
                    return;
                }
                if (pollSelectInternal != AbstractChannelKt.POLL_FAILED && pollSelectInternal != AtomicKt.RETRY_ATOMIC) {
                    tryStartBlockUnintercepted(block, select, receiveMode, pollSelectInternal);
                }
            }
        }
    }

    private final <R> void tryStartBlockUnintercepted(Function2<Object, ? super Continuation<? super R>, ? extends Object> function2, SelectInstance<? super R> selectInstance, int i, Object obj) {
        Object m1348constructorimpl;
        boolean z = obj instanceof Closed;
        if (!z) {
            if (i != 2) {
                UndispatchedKt.startCoroutineUnintercepted(function2, obj, selectInstance.getCompletion());
                return;
            }
            ValueOrClosed.Companion companion = ValueOrClosed.INSTANCE;
            if (z) {
                m1348constructorimpl = ValueOrClosed.m1348constructorimpl(new ValueOrClosed.Closed(((Closed) obj).closeCause));
            } else {
                m1348constructorimpl = ValueOrClosed.m1348constructorimpl(obj);
            }
            UndispatchedKt.startCoroutineUnintercepted(function2, ValueOrClosed.m1347boximpl(m1348constructorimpl), selectInstance.getCompletion());
            return;
        }
        if (i == 0) {
            throw StackTraceRecoveryKt.recoverStackTrace(((Closed) obj).getReceiveException());
        }
        if (i != 1) {
            if (i == 2 && selectInstance.trySelect()) {
                ValueOrClosed.Companion companion2 = ValueOrClosed.INSTANCE;
                UndispatchedKt.startCoroutineUnintercepted(function2, ValueOrClosed.m1347boximpl(ValueOrClosed.m1348constructorimpl(new ValueOrClosed.Closed(((Closed) obj).closeCause))), selectInstance.getCompletion());
                return;
            }
            return;
        }
        Closed closed = (Closed) obj;
        if (closed.closeCause == null) {
            if (selectInstance.trySelect()) {
                UndispatchedKt.startCoroutineUnintercepted(function2, null, selectInstance.getCompletion());
                return;
            }
            return;
        }
        throw StackTraceRecoveryKt.recoverStackTrace(closed.getReceiveException());
    }

    private final <R> boolean enqueueReceiveSelect(SelectInstance<? super R> select, Function2<Object, ? super Continuation<? super R>, ? extends Object> block, int receiveMode) {
        ReceiveSelect receiveSelect = new ReceiveSelect(this, select, block, receiveMode);
        boolean enqueueReceive = enqueueReceive(receiveSelect);
        if (enqueueReceive) {
            select.disposeOnSelect(receiveSelect);
        }
        return enqueueReceive;
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected ReceiveOrClosed<E> takeFirstReceiveOrPeekClosed() {
        ReceiveOrClosed<E> takeFirstReceiveOrPeekClosed = super.takeFirstReceiveOrPeekClosed();
        if (takeFirstReceiveOrPeekClosed != null && !(takeFirstReceiveOrPeekClosed instanceof Closed)) {
            onReceiveDequeued();
        }
        return takeFirstReceiveOrPeekClosed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void removeReceiveOnCancel(CancellableContinuation<?> cont, Receive<?> receive) {
        cont.invokeOnCancellation(new RemoveReceiveOnCancel(receive));
    }

    /* compiled from: AbstractChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0011\u0012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003¢\u0006\u0002\u0010\u0004J\u0013\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0096\u0002J\b\u0010\t\u001a\u00020\nH\u0016R\u0012\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lkotlinx/coroutines/channels/AbstractChannel$RemoveReceiveOnCancel;", "Lkotlinx/coroutines/CancelHandler;", "receive", "Lkotlinx/coroutines/channels/Receive;", "(Lkotlinx/coroutines/channels/AbstractChannel;Lkotlinx/coroutines/channels/Receive;)V", "invoke", "", "cause", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 4, 0})
    private final class RemoveReceiveOnCancel extends CancelHandler {
        private final Receive<?> receive;

        public RemoveReceiveOnCancel(Receive<?> receive) {
            this.receive = receive;
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
            invoke2(th);
            return Unit.INSTANCE;
        }

        @Override // kotlinx.coroutines.CancelHandlerBase
        /* renamed from: invoke, reason: avoid collision after fix types in other method */
        public void invoke2(Throwable cause) {
            if (this.receive.remove()) {
                AbstractChannel.this.onReceiveDequeued();
            }
        }

        public String toString() {
            return "RemoveReceiveOnCancel[" + this.receive + ']';
        }
    }

    /* compiled from: AbstractChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004¢\u0006\u0002\u0010\u0005J\u0011\u0010\u000e\u001a\u00020\u000fH\u0096Bø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u0012\u0010\u0011\u001a\u00020\u000f2\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0002J\u0011\u0010\u0012\u001a\u00020\u000fH\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u000e\u0010\u0013\u001a\u00028\u0001H\u0096\u0002¢\u0006\u0002\u0010\u000bR\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001c\u0010\b\u001a\u0004\u0018\u00010\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\r\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0014"}, d2 = {"Lkotlinx/coroutines/channels/AbstractChannel$Itr;", "E", "Lkotlinx/coroutines/channels/ChannelIterator;", "channel", "Lkotlinx/coroutines/channels/AbstractChannel;", "(Lkotlinx/coroutines/channels/AbstractChannel;)V", "getChannel", "()Lkotlinx/coroutines/channels/AbstractChannel;", "result", "", "getResult", "()Ljava/lang/Object;", "setResult", "(Ljava/lang/Object;)V", "hasNext", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "hasNextResult", "hasNextSuspend", "next", "kotlinx-coroutines-core"}, k = 1, mv = {1, 4, 0})
    private static final class Itr<E> implements ChannelIterator<E> {
        private final AbstractChannel<E> channel;
        private Object result = AbstractChannelKt.POLL_FAILED;

        public Itr(AbstractChannel<E> abstractChannel) {
            this.channel = abstractChannel;
        }

        public final AbstractChannel<E> getChannel() {
            return this.channel;
        }

        @Override // kotlinx.coroutines.channels.ChannelIterator
        @Deprecated(level = DeprecationLevel.HIDDEN, message = "Since 1.3.0, binary compatibility with versions <= 1.2.x")
        public /* synthetic */ Object next(Continuation<? super E> continuation) {
            return ChannelIterator.DefaultImpls.next(this, continuation);
        }

        public final Object getResult() {
            return this.result;
        }

        public final void setResult(Object obj) {
            this.result = obj;
        }

        @Override // kotlinx.coroutines.channels.ChannelIterator
        public Object hasNext(Continuation<? super Boolean> continuation) {
            if (this.result != AbstractChannelKt.POLL_FAILED) {
                return Boxing.boxBoolean(hasNextResult(this.result));
            }
            Object pollInternal = this.channel.pollInternal();
            this.result = pollInternal;
            return pollInternal != AbstractChannelKt.POLL_FAILED ? Boxing.boxBoolean(hasNextResult(this.result)) : hasNextSuspend(continuation);
        }

        private final boolean hasNextResult(Object result) {
            if (!(result instanceof Closed)) {
                return true;
            }
            Closed closed = (Closed) result;
            if (closed.closeCause == null) {
                return false;
            }
            throw StackTraceRecoveryKt.recoverStackTrace(closed.getReceiveException());
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlinx.coroutines.channels.ChannelIterator
        public E next() {
            E e = (E) this.result;
            if (e instanceof Closed) {
                throw StackTraceRecoveryKt.recoverStackTrace(((Closed) e).getReceiveException());
            }
            if (e != AbstractChannelKt.POLL_FAILED) {
                this.result = AbstractChannelKt.POLL_FAILED;
                return e;
            }
            throw new IllegalStateException("'hasNext' should be called prior to 'next' invocation");
        }

        final /* synthetic */ Object hasNextSuspend(Continuation<? super Boolean> continuation) {
            CancellableContinuationImpl orCreateCancellableContinuation = CancellableContinuationKt.getOrCreateCancellableContinuation(IntrinsicsKt.intercepted(continuation));
            CancellableContinuationImpl cancellableContinuationImpl = orCreateCancellableContinuation;
            ReceiveHasNext receiveHasNext = new ReceiveHasNext(this, cancellableContinuationImpl);
            while (true) {
                ReceiveHasNext receiveHasNext2 = receiveHasNext;
                if (getChannel().enqueueReceive(receiveHasNext2)) {
                    getChannel().removeReceiveOnCancel(cancellableContinuationImpl, receiveHasNext2);
                    break;
                }
                Object pollInternal = getChannel().pollInternal();
                setResult(pollInternal);
                if (pollInternal instanceof Closed) {
                    Closed closed = (Closed) pollInternal;
                    if (closed.closeCause == null) {
                        Boolean boxBoolean = Boxing.boxBoolean(false);
                        Result.Companion companion = Result.INSTANCE;
                        cancellableContinuationImpl.resumeWith(Result.m21constructorimpl(boxBoolean));
                    } else {
                        Throwable receiveException = closed.getReceiveException();
                        Result.Companion companion2 = Result.INSTANCE;
                        cancellableContinuationImpl.resumeWith(Result.m21constructorimpl(ResultKt.createFailure(receiveException)));
                    }
                } else if (pollInternal != AbstractChannelKt.POLL_FAILED) {
                    Boolean boxBoolean2 = Boxing.boxBoolean(true);
                    Result.Companion companion3 = Result.INSTANCE;
                    cancellableContinuationImpl.resumeWith(Result.m21constructorimpl(boxBoolean2));
                    break;
                }
            }
            Object result = orCreateCancellableContinuation.getResult();
            if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            return result;
        }
    }

    /* compiled from: AbstractChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001d\u0012\u000e\u0010\u0003\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0015\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010\fJ\u0014\u0010\r\u001a\u00020\n2\n\u0010\u000e\u001a\u0006\u0012\u0002\b\u00030\u000fH\u0016J\u0015\u0010\u0010\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00028\u0001¢\u0006\u0002\u0010\u0011J\b\u0010\u0012\u001a\u00020\u0013H\u0016J!\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u000b\u001a\u00028\u00012\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0016¢\u0006\u0002\u0010\u0018R\u0018\u0010\u0003\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0019"}, d2 = {"Lkotlinx/coroutines/channels/AbstractChannel$ReceiveElement;", "E", "Lkotlinx/coroutines/channels/Receive;", "cont", "Lkotlinx/coroutines/CancellableContinuation;", "", "receiveMode", "", "(Lkotlinx/coroutines/CancellableContinuation;I)V", "completeResumeReceive", "", "value", "(Ljava/lang/Object;)V", "resumeReceiveClosed", "closed", "Lkotlinx/coroutines/channels/Closed;", "resumeValue", "(Ljava/lang/Object;)Ljava/lang/Object;", "toString", "", "tryResumeReceive", "Lkotlinx/coroutines/internal/Symbol;", "otherOp", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$PrepareOp;", "(Ljava/lang/Object;Lkotlinx/coroutines/internal/LockFreeLinkedListNode$PrepareOp;)Lkotlinx/coroutines/internal/Symbol;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 4, 0})
    private static final class ReceiveElement<E> extends Receive<E> {
        public final CancellableContinuation<Object> cont;
        public final int receiveMode;

        public ReceiveElement(CancellableContinuation<Object> cancellableContinuation, int i) {
            this.cont = cancellableContinuation;
            this.receiveMode = i;
        }

        public final Object resumeValue(E value) {
            if (this.receiveMode != 2) {
                return value;
            }
            ValueOrClosed.Companion companion = ValueOrClosed.INSTANCE;
            return ValueOrClosed.m1347boximpl(ValueOrClosed.m1348constructorimpl(value));
        }

        @Override // kotlinx.coroutines.channels.ReceiveOrClosed
        public Symbol tryResumeReceive(E value, LockFreeLinkedListNode.PrepareOp otherOp) {
            Object tryResume = this.cont.tryResume(resumeValue(value), otherOp != null ? otherOp.desc : null);
            if (tryResume == null) {
                return null;
            }
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (!(tryResume == CancellableContinuationImplKt.RESUME_TOKEN)) {
                    throw new AssertionError();
                }
            }
            if (otherOp != null) {
                otherOp.finishPrepare();
            }
            return CancellableContinuationImplKt.RESUME_TOKEN;
        }

        @Override // kotlinx.coroutines.channels.ReceiveOrClosed
        public void completeResumeReceive(E value) {
            this.cont.completeResume(CancellableContinuationImplKt.RESUME_TOKEN);
        }

        @Override // kotlinx.coroutines.channels.Receive
        public void resumeReceiveClosed(Closed<?> closed) {
            if (this.receiveMode == 1 && closed.closeCause == null) {
                CancellableContinuation<Object> cancellableContinuation = this.cont;
                Result.Companion companion = Result.INSTANCE;
                cancellableContinuation.resumeWith(Result.m21constructorimpl(null));
            } else {
                if (this.receiveMode != 2) {
                    CancellableContinuation<Object> cancellableContinuation2 = this.cont;
                    Throwable receiveException = closed.getReceiveException();
                    Result.Companion companion2 = Result.INSTANCE;
                    cancellableContinuation2.resumeWith(Result.m21constructorimpl(ResultKt.createFailure(receiveException)));
                    return;
                }
                CancellableContinuation<Object> cancellableContinuation3 = this.cont;
                ValueOrClosed.Companion companion3 = ValueOrClosed.INSTANCE;
                ValueOrClosed m1347boximpl = ValueOrClosed.m1347boximpl(ValueOrClosed.m1348constructorimpl(new ValueOrClosed.Closed(closed.closeCause)));
                Result.Companion companion4 = Result.INSTANCE;
                cancellableContinuation3.resumeWith(Result.m21constructorimpl(m1347boximpl));
            }
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
        public String toString() {
            return "ReceiveElement@" + DebugStringsKt.getHexAddress(this) + "[receiveMode=" + this.receiveMode + ']';
        }
    }

    /* compiled from: AbstractChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B!\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006¢\u0006\u0002\u0010\bJ\u0015\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010\fJ\u0014\u0010\r\u001a\u00020\n2\n\u0010\u000e\u001a\u0006\u0012\u0002\b\u00030\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J!\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u000b\u001a\u00028\u00012\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016¢\u0006\u0002\u0010\u0016R\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00068\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lkotlinx/coroutines/channels/AbstractChannel$ReceiveHasNext;", "E", "Lkotlinx/coroutines/channels/Receive;", "iterator", "Lkotlinx/coroutines/channels/AbstractChannel$Itr;", "cont", "Lkotlinx/coroutines/CancellableContinuation;", "", "(Lkotlinx/coroutines/channels/AbstractChannel$Itr;Lkotlinx/coroutines/CancellableContinuation;)V", "completeResumeReceive", "", "value", "(Ljava/lang/Object;)V", "resumeReceiveClosed", "closed", "Lkotlinx/coroutines/channels/Closed;", "toString", "", "tryResumeReceive", "Lkotlinx/coroutines/internal/Symbol;", "otherOp", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$PrepareOp;", "(Ljava/lang/Object;Lkotlinx/coroutines/internal/LockFreeLinkedListNode$PrepareOp;)Lkotlinx/coroutines/internal/Symbol;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 4, 0})
    private static final class ReceiveHasNext<E> extends Receive<E> {
        public final CancellableContinuation<Boolean> cont;
        public final Itr<E> iterator;

        /* JADX WARN: Multi-variable type inference failed */
        public ReceiveHasNext(Itr<E> itr, CancellableContinuation<? super Boolean> cancellableContinuation) {
            this.iterator = itr;
            this.cont = cancellableContinuation;
        }

        @Override // kotlinx.coroutines.channels.ReceiveOrClosed
        public Symbol tryResumeReceive(E value, LockFreeLinkedListNode.PrepareOp otherOp) {
            Object tryResume = this.cont.tryResume(true, otherOp != null ? otherOp.desc : null);
            if (tryResume == null) {
                return null;
            }
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (!(tryResume == CancellableContinuationImplKt.RESUME_TOKEN)) {
                    throw new AssertionError();
                }
            }
            if (otherOp != null) {
                otherOp.finishPrepare();
            }
            return CancellableContinuationImplKt.RESUME_TOKEN;
        }

        @Override // kotlinx.coroutines.channels.ReceiveOrClosed
        public void completeResumeReceive(E value) {
            this.iterator.setResult(value);
            this.cont.completeResume(CancellableContinuationImplKt.RESUME_TOKEN);
        }

        @Override // kotlinx.coroutines.channels.Receive
        public void resumeReceiveClosed(Closed<?> closed) {
            Object tryResumeWithException;
            if (closed.closeCause == null) {
                tryResumeWithException = CancellableContinuation.DefaultImpls.tryResume$default(this.cont, false, null, 2, null);
            } else {
                CancellableContinuation<Boolean> cancellableContinuation = this.cont;
                Throwable receiveException = closed.getReceiveException();
                CancellableContinuation<Boolean> cancellableContinuation2 = this.cont;
                if (DebugKt.getRECOVER_STACK_TRACES() && (cancellableContinuation2 instanceof CoroutineStackFrame)) {
                    receiveException = StackTraceRecoveryKt.recoverFromStackFrame(receiveException, (CoroutineStackFrame) cancellableContinuation2);
                }
                tryResumeWithException = cancellableContinuation.tryResumeWithException(receiveException);
            }
            if (tryResumeWithException != null) {
                this.iterator.setResult(closed);
                this.cont.completeResume(tryResumeWithException);
            }
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
        public String toString() {
            return "ReceiveHasNext@" + DebugStringsKt.getHexAddress(this);
        }
    }

    /* compiled from: AbstractChannel.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u0001*\u0004\b\u0002\u0010\u00022\b\u0012\u0004\u0012\u0002H\u00020\u00032\u00020\u0004BR\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00020\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00010\b\u0012$\u0010\t\u001a \b\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\f\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\n\u0012\u0006\u0010\r\u001a\u00020\u000eø\u0001\u0000¢\u0006\u0002\u0010\u000fJ\u0015\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00028\u0002H\u0016¢\u0006\u0002\u0010\u0014J\b\u0010\u0015\u001a\u00020\u0012H\u0016J\u0014\u0010\u0016\u001a\u00020\u00122\n\u0010\u0017\u001a\u0006\u0012\u0002\b\u00030\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u001aH\u0016J!\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u0013\u001a\u00028\u00022\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016¢\u0006\u0002\u0010\u001fR3\u0010\t\u001a \b\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\f\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\n8\u0006X\u0087\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0010R\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u00020\u000e8\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00010\b8\u0006X\u0087\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006 "}, d2 = {"Lkotlinx/coroutines/channels/AbstractChannel$ReceiveSelect;", "R", "E", "Lkotlinx/coroutines/channels/Receive;", "Lkotlinx/coroutines/DisposableHandle;", "channel", "Lkotlinx/coroutines/channels/AbstractChannel;", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "block", "Lkotlin/Function2;", "", "Lkotlin/coroutines/Continuation;", "receiveMode", "", "(Lkotlinx/coroutines/channels/AbstractChannel;Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function2;I)V", "Lkotlin/jvm/functions/Function2;", "completeResumeReceive", "", "value", "(Ljava/lang/Object;)V", "dispose", "resumeReceiveClosed", "closed", "Lkotlinx/coroutines/channels/Closed;", "toString", "", "tryResumeReceive", "Lkotlinx/coroutines/internal/Symbol;", "otherOp", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$PrepareOp;", "(Ljava/lang/Object;Lkotlinx/coroutines/internal/LockFreeLinkedListNode$PrepareOp;)Lkotlinx/coroutines/internal/Symbol;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 4, 0})
    private static final class ReceiveSelect<R, E> extends Receive<E> implements DisposableHandle {
        public final Function2<Object, Continuation<? super R>, Object> block;
        public final AbstractChannel<E> channel;
        public final int receiveMode;
        public final SelectInstance<R> select;

        /* JADX WARN: Multi-variable type inference failed */
        public ReceiveSelect(AbstractChannel<E> abstractChannel, SelectInstance<? super R> selectInstance, Function2<Object, ? super Continuation<? super R>, ? extends Object> function2, int i) {
            this.channel = abstractChannel;
            this.select = selectInstance;
            this.block = function2;
            this.receiveMode = i;
        }

        @Override // kotlinx.coroutines.channels.ReceiveOrClosed
        public Symbol tryResumeReceive(E value, LockFreeLinkedListNode.PrepareOp otherOp) {
            return (Symbol) this.select.trySelectOther(otherOp);
        }

        @Override // kotlinx.coroutines.channels.ReceiveOrClosed
        public void completeResumeReceive(E value) {
            Function2<Object, Continuation<? super R>, Object> function2 = this.block;
            if (this.receiveMode == 2) {
                ValueOrClosed.Companion companion = ValueOrClosed.INSTANCE;
                value = (E) ValueOrClosed.m1347boximpl(ValueOrClosed.m1348constructorimpl(value));
            }
            ContinuationKt.startCoroutine(function2, value, this.select.getCompletion());
        }

        @Override // kotlinx.coroutines.channels.Receive
        public void resumeReceiveClosed(Closed<?> closed) {
            if (this.select.trySelect()) {
                int i = this.receiveMode;
                if (i == 0) {
                    this.select.resumeSelectWithException(closed.getReceiveException());
                    return;
                }
                if (i == 1) {
                    if (closed.closeCause == null) {
                        ContinuationKt.startCoroutine(this.block, null, this.select.getCompletion());
                        return;
                    } else {
                        this.select.resumeSelectWithException(closed.getReceiveException());
                        return;
                    }
                }
                if (i != 2) {
                    return;
                }
                Function2<Object, Continuation<? super R>, Object> function2 = this.block;
                ValueOrClosed.Companion companion = ValueOrClosed.INSTANCE;
                ContinuationKt.startCoroutine(function2, ValueOrClosed.m1347boximpl(ValueOrClosed.m1348constructorimpl(new ValueOrClosed.Closed(closed.closeCause))), this.select.getCompletion());
            }
        }

        @Override // kotlinx.coroutines.DisposableHandle
        public void dispose() {
            if (remove()) {
                this.channel.onReceiveDequeued();
            }
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
        public String toString() {
            return "ReceiveSelect@" + DebugStringsKt.getHexAddress(this) + '[' + this.select + ",receiveMode=" + this.receiveMode + ']';
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    final /* synthetic */ <R> Object receiveSuspend(int i, Continuation<? super R> continuation) {
        CancellableContinuationImpl orCreateCancellableContinuation = CancellableContinuationKt.getOrCreateCancellableContinuation(IntrinsicsKt.intercepted(continuation));
        CancellableContinuationImpl cancellableContinuationImpl = orCreateCancellableContinuation;
        if (cancellableContinuationImpl != null) {
            ReceiveElement receiveElement = new ReceiveElement(cancellableContinuationImpl, i);
            while (true) {
                ReceiveElement receiveElement2 = receiveElement;
                if (enqueueReceive(receiveElement2)) {
                    removeReceiveOnCancel(cancellableContinuationImpl, receiveElement2);
                    break;
                }
                Object pollInternal = pollInternal();
                if (pollInternal instanceof Closed) {
                    receiveElement.resumeReceiveClosed((Closed) pollInternal);
                    break;
                }
                if (pollInternal != AbstractChannelKt.POLL_FAILED) {
                    Object resumeValue = receiveElement.resumeValue(pollInternal);
                    Result.Companion companion = Result.INSTANCE;
                    cancellableContinuationImpl.resumeWith(Result.m21constructorimpl(resumeValue));
                    break;
                }
            }
            Object result = orCreateCancellableContinuation.getResult();
            if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            return result;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlinx.coroutines.CancellableContinuation<kotlin.Any?>");
    }
}
