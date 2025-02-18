package kotlinx.coroutines.scheduling;

import com.jieli.lib.dv.control.utils.TopicKey;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.LockSupport;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.LongCompanionObject;
import kotlin.random.Random;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.DebugStringsKt;
import kotlinx.coroutines.TimeSourceKt;
import kotlinx.coroutines.internal.Symbol;

/* compiled from: CoroutineScheduler.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b0\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\b\u0000\u0018\u0000 \\2\u00020`2\u00020a:\u0003\\]^B+\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0001\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006¢\u0006\u0004\b\b\u0010\tJ\u0017\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\nH\u0002¢\u0006\u0004\b\r\u0010\u000eJ\u0018\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0004H\u0086\b¢\u0006\u0004\b\u0010\u0010\u0011J\u0018\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0004H\u0082\b¢\u0006\u0004\b\u0012\u0010\u0011J\u000f\u0010\u0014\u001a\u00020\u0013H\u0016¢\u0006\u0004\b\u0014\u0010\u0015J\u000f\u0010\u0016\u001a\u00020\u0001H\u0002¢\u0006\u0004\b\u0016\u0010\u0017J#\u0010\u001f\u001a\u00020\n2\n\u0010\u001a\u001a\u00060\u0018j\u0002`\u00192\u0006\u0010\u001c\u001a\u00020\u001bH\u0000¢\u0006\u0004\b\u001d\u0010\u001eJ\u0018\u0010 \u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0004H\u0082\b¢\u0006\u0004\b \u0010\u0011J\u0015\u0010\"\u001a\b\u0018\u00010!R\u00020\u0000H\u0002¢\u0006\u0004\b\"\u0010#J\u0010\u0010$\u001a\u00020\u0013H\u0082\b¢\u0006\u0004\b$\u0010\u0015J\u0010\u0010%\u001a\u00020\u0001H\u0082\b¢\u0006\u0004\b%\u0010\u0017J-\u0010'\u001a\u00020\u00132\n\u0010\u001a\u001a\u00060\u0018j\u0002`\u00192\b\b\u0002\u0010\u001c\u001a\u00020\u001b2\b\b\u0002\u0010&\u001a\u00020\f¢\u0006\u0004\b'\u0010(J\u001b\u0010*\u001a\u00020\u00132\n\u0010)\u001a\u00060\u0018j\u0002`\u0019H\u0016¢\u0006\u0004\b*\u0010+J\u0010\u0010,\u001a\u00020\u0004H\u0082\b¢\u0006\u0004\b,\u0010-J\u0010\u0010.\u001a\u00020\u0001H\u0082\b¢\u0006\u0004\b.\u0010\u0017J\u001b\u00100\u001a\u00020\u00012\n\u0010/\u001a\u00060!R\u00020\u0000H\u0002¢\u0006\u0004\b0\u00101J\u0015\u00102\u001a\b\u0018\u00010!R\u00020\u0000H\u0002¢\u0006\u0004\b2\u0010#J\u001b\u00105\u001a\u00020\f2\n\u0010/\u001a\u00060!R\u00020\u0000H\u0000¢\u0006\u0004\b3\u00104J+\u0010:\u001a\u00020\u00132\n\u0010/\u001a\u00060!R\u00020\u00002\u0006\u00106\u001a\u00020\u00012\u0006\u00107\u001a\u00020\u0001H\u0000¢\u0006\u0004\b8\u00109J\u0010\u0010;\u001a\u00020\u0004H\u0082\b¢\u0006\u0004\b;\u0010-J\u0015\u0010<\u001a\u00020\u00132\u0006\u0010\u000b\u001a\u00020\n¢\u0006\u0004\b<\u0010=J\u0015\u0010?\u001a\u00020\u00132\u0006\u0010>\u001a\u00020\u0004¢\u0006\u0004\b?\u0010@J\u0017\u0010B\u001a\u00020\u00132\u0006\u0010A\u001a\u00020\fH\u0002¢\u0006\u0004\bB\u0010CJ\u000f\u0010E\u001a\u00020\u0013H\u0000¢\u0006\u0004\bD\u0010\u0015J\u000f\u0010F\u001a\u00020\u0006H\u0016¢\u0006\u0004\bF\u0010GJ\u0010\u0010H\u001a\u00020\fH\u0082\b¢\u0006\u0004\bH\u0010IJ\u0019\u0010J\u001a\u00020\f2\b\b\u0002\u0010\u000f\u001a\u00020\u0004H\u0002¢\u0006\u0004\bJ\u0010KJ\u000f\u0010L\u001a\u00020\fH\u0002¢\u0006\u0004\bL\u0010IJ+\u0010M\u001a\u0004\u0018\u00010\n*\b\u0018\u00010!R\u00020\u00002\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010&\u001a\u00020\fH\u0002¢\u0006\u0004\bM\u0010NR\u0017\u0010\u0010\u001a\u00020\u00018Â\u0002@\u0002X\u0082\u0004¢\u0006\u0006\u001a\u0004\bO\u0010\u0017R\u0016\u0010\u0002\u001a\u00020\u00018\u0006@\u0007X\u0087\u0004¢\u0006\u0006\n\u0004\b\u0002\u0010PR\u0017\u0010 \u001a\u00020\u00018Â\u0002@\u0002X\u0082\u0004¢\u0006\u0006\u001a\u0004\bQ\u0010\u0017R\u0016\u0010S\u001a\u00020R8\u0006@\u0007X\u0087\u0004¢\u0006\u0006\n\u0004\bS\u0010TR\u0016\u0010U\u001a\u00020R8\u0006@\u0007X\u0087\u0004¢\u0006\u0006\n\u0004\bU\u0010TR\u0016\u0010\u0005\u001a\u00020\u00048\u0006@\u0007X\u0087\u0004¢\u0006\u0006\n\u0004\b\u0005\u0010VR\u0013\u0010W\u001a\u00020\f8F@\u0006¢\u0006\u0006\u001a\u0004\bW\u0010IR\u0016\u0010\u0003\u001a\u00020\u00018\u0006@\u0007X\u0087\u0004¢\u0006\u0006\n\u0004\b\u0003\u0010PR\u0016\u0010\u0007\u001a\u00020\u00068\u0006@\u0007X\u0087\u0004¢\u0006\u0006\n\u0004\b\u0007\u0010XR\"\u0010Z\u001a\u000e\u0012\n\u0012\b\u0018\u00010!R\u00020\u00000Y8\u0006@\u0007X\u0087\u0004¢\u0006\u0006\n\u0004\bZ\u0010[¨\u0006_"}, d2 = {"Lkotlinx/coroutines/scheduling/CoroutineScheduler;", "", "corePoolSize", "maxPoolSize", "", "idleWorkerKeepAliveNs", "", "schedulerName", "<init>", "(IIJLjava/lang/String;)V", "Lkotlinx/coroutines/scheduling/Task;", "task", "", "addToGlobalQueue", "(Lkotlinx/coroutines/scheduling/Task;)Z", "state", "availableCpuPermits", "(J)I", "blockingTasks", "", "close", "()V", "createNewWorker", "()I", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "block", "Lkotlinx/coroutines/scheduling/TaskContext;", "taskContext", "createTask$kotlinx_coroutines_core", "(Ljava/lang/Runnable;Lkotlinx/coroutines/scheduling/TaskContext;)Lkotlinx/coroutines/scheduling/Task;", "createTask", "createdWorkers", "Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;", "currentWorker", "()Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;", "decrementBlockingTasks", "decrementCreatedWorkers", "tailDispatch", "dispatch", "(Ljava/lang/Runnable;Lkotlinx/coroutines/scheduling/TaskContext;Z)V", "command", "execute", "(Ljava/lang/Runnable;)V", "incrementBlockingTasks", "()J", "incrementCreatedWorkers", "worker", "parkedWorkersStackNextIndex", "(Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;)I", "parkedWorkersStackPop", "parkedWorkersStackPush$kotlinx_coroutines_core", "(Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;)Z", "parkedWorkersStackPush", "oldIndex", "newIndex", "parkedWorkersStackTopUpdate$kotlinx_coroutines_core", "(Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;II)V", "parkedWorkersStackTopUpdate", "releaseCpuPermit", "runSafely", "(Lkotlinx/coroutines/scheduling/Task;)V", TopicKey.TIMEOUT, "shutdown", "(J)V", "skipUnpark", "signalBlockingWork", "(Z)V", "signalCpuWork$kotlinx_coroutines_core", "signalCpuWork", "toString", "()Ljava/lang/String;", "tryAcquireCpuPermit", "()Z", "tryCreateWorker", "(J)Z", "tryUnpark", "submitToLocalQueue", "(Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;Lkotlinx/coroutines/scheduling/Task;Z)Lkotlinx/coroutines/scheduling/Task;", "getAvailableCpuPermits", "I", "getCreatedWorkers", "Lkotlinx/coroutines/scheduling/GlobalQueue;", "globalBlockingQueue", "Lkotlinx/coroutines/scheduling/GlobalQueue;", "globalCpuQueue", "J", "isTerminated", "Ljava/lang/String;", "Ljava/util/concurrent/atomic/AtomicReferenceArray;", "workers", "Ljava/util/concurrent/atomic/AtomicReferenceArray;", "Companion", "Worker", "WorkerState", "kotlinx-coroutines-core", "Ljava/util/concurrent/Executor;", "Ljava/io/Closeable;"}, k = 1, mv = {1, 4, 0})
/* loaded from: classes2.dex */
public final class CoroutineScheduler implements Executor, Closeable {
    private static final long BLOCKING_MASK = 4398044413952L;
    private static final int BLOCKING_SHIFT = 21;
    private static final int CLAIMED = 0;
    private static final long CPU_PERMITS_MASK = 9223367638808264704L;
    private static final int CPU_PERMITS_SHIFT = 42;
    private static final long CREATED_MASK = 2097151;
    public static final int MAX_SUPPORTED_POOL_SIZE = 2097150;
    public static final int MIN_SUPPORTED_POOL_SIZE = 1;
    private static final int PARKED = -1;
    private static final long PARKED_INDEX_MASK = 2097151;
    private static final long PARKED_VERSION_INC = 2097152;
    private static final long PARKED_VERSION_MASK = -2097152;
    private static final int TERMINATED = 1;
    private volatile int _isTerminated;
    volatile long controlState;
    public final int corePoolSize;
    public final GlobalQueue globalBlockingQueue;
    public final GlobalQueue globalCpuQueue;
    public final long idleWorkerKeepAliveNs;
    public final int maxPoolSize;
    private volatile long parkedWorkersStack;
    public final String schedulerName;
    public final AtomicReferenceArray<Worker> workers;
    public static final Symbol NOT_IN_STACK = new Symbol("NOT_IN_STACK");
    private static final AtomicLongFieldUpdater parkedWorkersStack$FU = AtomicLongFieldUpdater.newUpdater(CoroutineScheduler.class, "parkedWorkersStack");
    static final AtomicLongFieldUpdater controlState$FU = AtomicLongFieldUpdater.newUpdater(CoroutineScheduler.class, "controlState");
    private static final AtomicIntegerFieldUpdater _isTerminated$FU = AtomicIntegerFieldUpdater.newUpdater(CoroutineScheduler.class, "_isTerminated");

    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 4, 0})
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[WorkerState.values().length];
            $EnumSwitchMapping$0 = iArr;
            iArr[WorkerState.PARKING.ordinal()] = 1;
            $EnumSwitchMapping$0[WorkerState.BLOCKING.ordinal()] = 2;
            $EnumSwitchMapping$0[WorkerState.CPU_ACQUIRED.ordinal()] = 3;
            $EnumSwitchMapping$0[WorkerState.DORMANT.ordinal()] = 4;
            $EnumSwitchMapping$0[WorkerState.TERMINATED.ordinal()] = 5;
        }
    }

    /* compiled from: CoroutineScheduler.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;", "", "(Ljava/lang/String;I)V", "CPU_ACQUIRED", "BLOCKING", "PARKING", "DORMANT", "TERMINATED", "kotlinx-coroutines-core"}, k = 1, mv = {1, 4, 0})
    public enum WorkerState {
        CPU_ACQUIRED,
        BLOCKING,
        PARKING,
        DORMANT,
        TERMINATED
    }

    private final int blockingTasks(long state) {
        return (int) ((state & BLOCKING_MASK) >> 21);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int createdWorkers(long state) {
        return (int) (state & 2097151);
    }

    public final int availableCpuPermits(long state) {
        return (int) ((state & CPU_PERMITS_MASK) >> 42);
    }

    public CoroutineScheduler(int i, int i2, long j, String str) {
        this.corePoolSize = i;
        this.maxPoolSize = i2;
        this.idleWorkerKeepAliveNs = j;
        this.schedulerName = str;
        if (!(i >= 1)) {
            throw new IllegalArgumentException(("Core pool size " + this.corePoolSize + " should be at least 1").toString());
        }
        if (!(this.maxPoolSize >= this.corePoolSize)) {
            throw new IllegalArgumentException(("Max pool size " + this.maxPoolSize + " should be greater than or equals to core pool size " + this.corePoolSize).toString());
        }
        if (!(this.maxPoolSize <= 2097150)) {
            throw new IllegalArgumentException(("Max pool size " + this.maxPoolSize + " should not exceed maximal supported number of threads 2097150").toString());
        }
        if (!(this.idleWorkerKeepAliveNs > 0)) {
            throw new IllegalArgumentException(("Idle worker keep alive time " + this.idleWorkerKeepAliveNs + " must be positive").toString());
        }
        this.globalCpuQueue = new GlobalQueue();
        this.globalBlockingQueue = new GlobalQueue();
        this.parkedWorkersStack = 0L;
        this.workers = new AtomicReferenceArray<>(this.maxPoolSize + 1);
        this.controlState = this.corePoolSize << 42;
        this._isTerminated = 0;
    }

    public /* synthetic */ CoroutineScheduler(int i, int i2, long j, String str, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, i2, (i3 & 4) != 0 ? TasksKt.IDLE_WORKER_KEEP_ALIVE_NS : j, (i3 & 8) != 0 ? TasksKt.DEFAULT_SCHEDULER_NAME : str);
    }

    public final boolean parkedWorkersStackPush$kotlinx_coroutines_core(Worker worker) {
        long j;
        long j2;
        int indexInArray;
        if (worker.getNextParkedWorker() != NOT_IN_STACK) {
            return false;
        }
        do {
            j = this.parkedWorkersStack;
            int i = (int) (2097151 & j);
            j2 = (2097152 + j) & PARKED_VERSION_MASK;
            indexInArray = worker.getIndexInArray();
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (!(indexInArray != 0)) {
                    throw new AssertionError();
                }
            }
            worker.setNextParkedWorker(this.workers.get(i));
        } while (!parkedWorkersStack$FU.compareAndSet(this, j, indexInArray | j2));
        return true;
    }

    private final int parkedWorkersStackNextIndex(Worker worker) {
        Object nextParkedWorker = worker.getNextParkedWorker();
        while (nextParkedWorker != NOT_IN_STACK) {
            if (nextParkedWorker == null) {
                return 0;
            }
            Worker worker2 = (Worker) nextParkedWorker;
            int indexInArray = worker2.getIndexInArray();
            if (indexInArray != 0) {
                return indexInArray;
            }
            nextParkedWorker = worker2.getNextParkedWorker();
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int getCreatedWorkers() {
        return (int) (this.controlState & 2097151);
    }

    private final int getAvailableCpuPermits() {
        return (int) ((this.controlState & CPU_PERMITS_MASK) >> 42);
    }

    private final int incrementCreatedWorkers() {
        return (int) (controlState$FU.incrementAndGet(this) & 2097151);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int decrementCreatedWorkers() {
        return (int) (controlState$FU.getAndDecrement(this) & 2097151);
    }

    private final long incrementBlockingTasks() {
        return controlState$FU.addAndGet(this, 2097152L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void decrementBlockingTasks() {
        controlState$FU.addAndGet(this, PARKED_VERSION_MASK);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final long releaseCpuPermit() {
        return controlState$FU.addAndGet(this, 4398046511104L);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [boolean, int] */
    public final boolean isTerminated() {
        return this._isTerminated;
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable command) {
        dispatch$default(this, command, null, false, 6, null);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        shutdown(10000L);
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x0070, code lost:
    
        if (r9 != null) goto L36;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void shutdown(long r9) {
        /*
            r8 = this;
            java.util.concurrent.atomic.AtomicIntegerFieldUpdater r0 = kotlinx.coroutines.scheduling.CoroutineScheduler._isTerminated$FU
            r1 = 0
            r2 = 1
            boolean r0 = r0.compareAndSet(r8, r1, r2)
            if (r0 != 0) goto Lb
            return
        Lb:
            kotlinx.coroutines.scheduling.CoroutineScheduler$Worker r0 = r8.currentWorker()
            java.util.concurrent.atomic.AtomicReferenceArray<kotlinx.coroutines.scheduling.CoroutineScheduler$Worker> r3 = r8.workers
            monitor-enter(r3)
            long r4 = r8.controlState     // Catch: java.lang.Throwable -> Lbc
            r6 = 2097151(0x1fffff, double:1.0361303E-317)
            long r4 = r4 & r6
            int r5 = (int) r4
            monitor-exit(r3)
            if (r2 > r5) goto L60
            r3 = 1
        L1d:
            java.util.concurrent.atomic.AtomicReferenceArray<kotlinx.coroutines.scheduling.CoroutineScheduler$Worker> r4 = r8.workers
            java.lang.Object r4 = r4.get(r3)
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
            kotlinx.coroutines.scheduling.CoroutineScheduler$Worker r4 = (kotlinx.coroutines.scheduling.CoroutineScheduler.Worker) r4
            if (r4 == r0) goto L5b
        L2a:
            boolean r6 = r4.isAlive()
            if (r6 == 0) goto L3a
            r6 = r4
            java.lang.Thread r6 = (java.lang.Thread) r6
            java.util.concurrent.locks.LockSupport.unpark(r6)
            r4.join(r9)
            goto L2a
        L3a:
            kotlinx.coroutines.scheduling.CoroutineScheduler$WorkerState r6 = r4.state
            boolean r7 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()
            if (r7 == 0) goto L54
            kotlinx.coroutines.scheduling.CoroutineScheduler$WorkerState r7 = kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.TERMINATED
            if (r6 != r7) goto L48
            r6 = 1
            goto L49
        L48:
            r6 = 0
        L49:
            if (r6 == 0) goto L4c
            goto L54
        L4c:
            java.lang.AssertionError r9 = new java.lang.AssertionError
            r9.<init>()
            java.lang.Throwable r9 = (java.lang.Throwable) r9
            throw r9
        L54:
            kotlinx.coroutines.scheduling.WorkQueue r4 = r4.localQueue
            kotlinx.coroutines.scheduling.GlobalQueue r6 = r8.globalBlockingQueue
            r4.offloadAllWorkTo(r6)
        L5b:
            if (r3 == r5) goto L60
            int r3 = r3 + 1
            goto L1d
        L60:
            kotlinx.coroutines.scheduling.GlobalQueue r9 = r8.globalBlockingQueue
            r9.close()
            kotlinx.coroutines.scheduling.GlobalQueue r9 = r8.globalCpuQueue
            r9.close()
        L6a:
            if (r0 == 0) goto L73
            kotlinx.coroutines.scheduling.Task r9 = r0.findTask(r2)
            if (r9 == 0) goto L73
            goto L7b
        L73:
            kotlinx.coroutines.scheduling.GlobalQueue r9 = r8.globalCpuQueue
            java.lang.Object r9 = r9.removeFirstOrNull()
            kotlinx.coroutines.scheduling.Task r9 = (kotlinx.coroutines.scheduling.Task) r9
        L7b:
            if (r9 == 0) goto L7e
            goto L86
        L7e:
            kotlinx.coroutines.scheduling.GlobalQueue r9 = r8.globalBlockingQueue
            java.lang.Object r9 = r9.removeFirstOrNull()
            kotlinx.coroutines.scheduling.Task r9 = (kotlinx.coroutines.scheduling.Task) r9
        L86:
            if (r9 == 0) goto L8c
            r8.runSafely(r9)
            goto L6a
        L8c:
            if (r0 == 0) goto L93
            kotlinx.coroutines.scheduling.CoroutineScheduler$WorkerState r9 = kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.TERMINATED
            r0.tryReleaseCpu$kotlinx_coroutines_core(r9)
        L93:
            boolean r9 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()
            if (r9 == 0) goto Lb5
            long r9 = r8.controlState
            r3 = 9223367638808264704(0x7ffffc0000000000, double:NaN)
            long r9 = r9 & r3
            r0 = 42
            long r9 = r9 >> r0
            int r10 = (int) r9
            int r9 = r8.corePoolSize
            if (r10 != r9) goto Laa
            r1 = 1
        Laa:
            if (r1 == 0) goto Lad
            goto Lb5
        Lad:
            java.lang.AssertionError r9 = new java.lang.AssertionError
            r9.<init>()
            java.lang.Throwable r9 = (java.lang.Throwable) r9
            throw r9
        Lb5:
            r9 = 0
            r8.parkedWorkersStack = r9
            r8.controlState = r9
            return
        Lbc:
            r9 = move-exception
            monitor-exit(r3)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.scheduling.CoroutineScheduler.shutdown(long):void");
    }

    public static /* synthetic */ void dispatch$default(CoroutineScheduler coroutineScheduler, Runnable runnable, TaskContext taskContext, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            taskContext = NonBlockingContext.INSTANCE;
        }
        if ((i & 4) != 0) {
            z = false;
        }
        coroutineScheduler.dispatch(runnable, taskContext, z);
    }

    public final void dispatch(Runnable block, TaskContext taskContext, boolean tailDispatch) {
        kotlinx.coroutines.TimeSource timeSource = TimeSourceKt.getTimeSource();
        if (timeSource != null) {
            timeSource.trackTask();
        }
        Task createTask$kotlinx_coroutines_core = createTask$kotlinx_coroutines_core(block, taskContext);
        Worker currentWorker = currentWorker();
        Task submitToLocalQueue = submitToLocalQueue(currentWorker, createTask$kotlinx_coroutines_core, tailDispatch);
        if (submitToLocalQueue != null && !addToGlobalQueue(submitToLocalQueue)) {
            throw new RejectedExecutionException(this.schedulerName + " was terminated");
        }
        boolean z = tailDispatch && currentWorker != null;
        if (createTask$kotlinx_coroutines_core.taskContext.getTaskMode() != 0) {
            signalBlockingWork(z);
        } else {
            if (z) {
                return;
            }
            signalCpuWork$kotlinx_coroutines_core();
        }
    }

    public final Task createTask$kotlinx_coroutines_core(Runnable block, TaskContext taskContext) {
        long nanoTime = TasksKt.schedulerTimeSource.nanoTime();
        if (block instanceof Task) {
            Task task = (Task) block;
            task.submissionTime = nanoTime;
            task.taskContext = taskContext;
            return task;
        }
        return new TaskImpl(block, nanoTime, taskContext);
    }

    public final void signalCpuWork$kotlinx_coroutines_core() {
        if (tryUnpark() || tryCreateWorker$default(this, 0L, 1, null)) {
            return;
        }
        tryUnpark();
    }

    static /* synthetic */ boolean tryCreateWorker$default(CoroutineScheduler coroutineScheduler, long j, int i, Object obj) {
        if ((i & 1) != 0) {
            j = coroutineScheduler.controlState;
        }
        return coroutineScheduler.tryCreateWorker(j);
    }

    private final boolean tryCreateWorker(long state) {
        if (RangesKt.coerceAtLeast(((int) (2097151 & state)) - ((int) ((state & BLOCKING_MASK) >> 21)), 0) < this.corePoolSize) {
            int createNewWorker = createNewWorker();
            if (createNewWorker == 1 && this.corePoolSize > 1) {
                createNewWorker();
            }
            if (createNewWorker > 0) {
                return true;
            }
        }
        return false;
    }

    private final boolean tryUnpark() {
        Worker parkedWorkersStackPop;
        do {
            parkedWorkersStackPop = parkedWorkersStackPop();
            if (parkedWorkersStackPop == null) {
                return false;
            }
        } while (!Worker.workerCtl$FU.compareAndSet(parkedWorkersStackPop, -1, 0));
        LockSupport.unpark(parkedWorkersStackPop);
        return true;
    }

    private final int createNewWorker() {
        synchronized (this.workers) {
            if (isTerminated()) {
                return -1;
            }
            long j = this.controlState;
            int i = (int) (j & 2097151);
            int coerceAtLeast = RangesKt.coerceAtLeast(i - ((int) ((j & BLOCKING_MASK) >> 21)), 0);
            if (coerceAtLeast >= this.corePoolSize) {
                return 0;
            }
            if (i >= this.maxPoolSize) {
                return 0;
            }
            int i2 = ((int) (this.controlState & 2097151)) + 1;
            if (!(i2 > 0 && this.workers.get(i2) == null)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            Worker worker = new Worker(this, i2);
            this.workers.set(i2, worker);
            if (!(i2 == ((int) (2097151 & controlState$FU.incrementAndGet(this))))) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            worker.start();
            return coerceAtLeast + 1;
        }
    }

    private final Task submitToLocalQueue(Worker worker, Task task, boolean z) {
        if (worker == null || worker.state == WorkerState.TERMINATED) {
            return task;
        }
        if (task.taskContext.getTaskMode() == 0 && worker.state == WorkerState.BLOCKING) {
            return task;
        }
        worker.mayHaveLocalTasks = true;
        return worker.localQueue.add(task, z);
    }

    private final Worker currentWorker() {
        Thread currentThread = Thread.currentThread();
        if (!(currentThread instanceof Worker)) {
            currentThread = null;
        }
        Worker worker = (Worker) currentThread;
        if (worker == null || !Intrinsics.areEqual(CoroutineScheduler.this, this)) {
            return null;
        }
        return worker;
    }

    public String toString() {
        ArrayList arrayList = new ArrayList();
        int length = this.workers.length();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 1; i6 < length; i6++) {
            Worker worker = this.workers.get(i6);
            if (worker != null) {
                int size$kotlinx_coroutines_core = worker.localQueue.getSize$kotlinx_coroutines_core();
                int i7 = WhenMappings.$EnumSwitchMapping$0[worker.state.ordinal()];
                if (i7 == 1) {
                    i3++;
                } else if (i7 == 2) {
                    i2++;
                    arrayList.add(String.valueOf(size$kotlinx_coroutines_core) + "b");
                } else if (i7 == 3) {
                    i++;
                    arrayList.add(String.valueOf(size$kotlinx_coroutines_core) + "c");
                } else if (i7 == 4) {
                    i4++;
                    if (size$kotlinx_coroutines_core > 0) {
                        arrayList.add(String.valueOf(size$kotlinx_coroutines_core) + "d");
                    }
                } else if (i7 == 5) {
                    i5++;
                }
            }
        }
        long j = this.controlState;
        return this.schedulerName + '@' + DebugStringsKt.getHexAddress(this) + "[Pool Size {core = " + this.corePoolSize + ", max = " + this.maxPoolSize + "}, Worker States {CPU = " + i + ", blocking = " + i2 + ", parked = " + i3 + ", dormant = " + i4 + ", terminated = " + i5 + "}, running workers queues = " + arrayList + ", global CPU queue size = " + this.globalCpuQueue.getSize() + ", global blocking queue size = " + this.globalBlockingQueue.getSize() + ", Control State {created workers= " + ((int) (2097151 & j)) + ", blocking tasks = " + ((int) ((BLOCKING_MASK & j) >> 21)) + ", CPUs acquired = " + (this.corePoolSize - ((int) ((CPU_PERMITS_MASK & j) >> 42))) + "}]";
    }

    public final void runSafely(Task task) {
        try {
            task.run();
        } catch (Throwable th) {
            try {
                Thread currentThread = Thread.currentThread();
                currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, th);
                kotlinx.coroutines.TimeSource timeSource = TimeSourceKt.getTimeSource();
                if (timeSource == null) {
                }
            } finally {
                kotlinx.coroutines.TimeSource timeSource2 = TimeSourceKt.getTimeSource();
                if (timeSource2 != null) {
                    timeSource2.unTrackTask();
                }
            }
        }
    }

    /* compiled from: CoroutineScheduler.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\b\u0080\u0004\u0018\u00002\u00020IB\u0011\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0001¢\u0006\u0004\b\u0003\u0010\u0004B\t\b\u0002¢\u0006\u0004\b\u0003\u0010\u0005J\u0017\u0010\b\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0001H\u0002¢\u0006\u0004\b\b\u0010\tJ\u0017\u0010\n\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0001H\u0002¢\u0006\u0004\b\n\u0010\tJ\u0017\u0010\r\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u000bH\u0002¢\u0006\u0004\b\r\u0010\u000eJ\u0019\u0010\u0011\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0010\u001a\u00020\u000fH\u0002¢\u0006\u0004\b\u0011\u0010\u0012J\u0017\u0010\u0013\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0010\u001a\u00020\u000f¢\u0006\u0004\b\u0013\u0010\u0012J\u0017\u0010\u0015\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u0001H\u0002¢\u0006\u0004\b\u0015\u0010\tJ\u000f\u0010\u0016\u001a\u00020\u000fH\u0002¢\u0006\u0004\b\u0016\u0010\u0017J\u0017\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\u0001H\u0000¢\u0006\u0004\b\u0019\u0010\u001aJ\u000f\u0010\u001c\u001a\u00020\u0007H\u0002¢\u0006\u0004\b\u001c\u0010\u001dJ\u0011\u0010\u001e\u001a\u0004\u0018\u00010\u000bH\u0002¢\u0006\u0004\b\u001e\u0010\u001fJ\u000f\u0010 \u001a\u00020\u0007H\u0016¢\u0006\u0004\b \u0010\u001dJ\u000f\u0010!\u001a\u00020\u0007H\u0002¢\u0006\u0004\b!\u0010\u001dJ\u000f\u0010\"\u001a\u00020\u000fH\u0002¢\u0006\u0004\b\"\u0010\u0017J\u000f\u0010#\u001a\u00020\u0007H\u0002¢\u0006\u0004\b#\u0010\u001dJ\u0017\u0010(\u001a\u00020\u000f2\u0006\u0010%\u001a\u00020$H\u0000¢\u0006\u0004\b&\u0010'J\u0019\u0010*\u001a\u0004\u0018\u00010\u000b2\u0006\u0010)\u001a\u00020\u000fH\u0002¢\u0006\u0004\b*\u0010\u0012J\u000f\u0010+\u001a\u00020\u0007H\u0002¢\u0006\u0004\b+\u0010\u001dR*\u0010,\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00018\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b,\u0010-\u001a\u0004\b.\u0010/\"\u0004\b0\u0010\tR\u0016\u00102\u001a\u0002018\u0006@\u0007X\u0087\u0004¢\u0006\u0006\n\u0004\b2\u00103R\u0016\u00104\u001a\u00020\u000f8\u0006@\u0006X\u0087\u000e¢\u0006\u0006\n\u0004\b4\u00105R\u0016\u00107\u001a\u0002068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b7\u00108R$\u0010:\u001a\u0004\u0018\u0001098\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b:\u0010;\u001a\u0004\b<\u0010=\"\u0004\b>\u0010?R\u0016\u0010@\u001a\u00020\u00018\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b@\u0010-R\u0014\u0010D\u001a\u00020A8Æ\u0002@\u0006¢\u0006\u0006\u001a\u0004\bB\u0010CR\u0016\u0010E\u001a\u00020$8\u0006@\u0006X\u0087\u000e¢\u0006\u0006\n\u0004\bE\u0010FR\u0016\u0010G\u001a\u0002068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bG\u00108¨\u0006H"}, d2 = {"Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;", "", "index", "<init>", "(Lkotlinx/coroutines/scheduling/CoroutineScheduler;I)V", "(Lkotlinx/coroutines/scheduling/CoroutineScheduler;)V", "taskMode", "", "afterTask", "(I)V", "beforeTask", "Lkotlinx/coroutines/scheduling/Task;", "task", "executeTask", "(Lkotlinx/coroutines/scheduling/Task;)V", "", "scanLocalQueue", "findAnyTask", "(Z)Lkotlinx/coroutines/scheduling/Task;", "findTask", "mode", "idleReset", "inStack", "()Z", "upperBound", "nextInt$kotlinx_coroutines_core", "(I)I", "nextInt", "park", "()V", "pollGlobalQueues", "()Lkotlinx/coroutines/scheduling/Task;", "run", "runWorker", "tryAcquireCpuPermit", "tryPark", "Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;", "newState", "tryReleaseCpu$kotlinx_coroutines_core", "(Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;)Z", "tryReleaseCpu", "blockingOnly", "trySteal", "tryTerminateWorker", "indexInArray", "I", "getIndexInArray", "()I", "setIndexInArray", "Lkotlinx/coroutines/scheduling/WorkQueue;", "localQueue", "Lkotlinx/coroutines/scheduling/WorkQueue;", "mayHaveLocalTasks", "Z", "", "minDelayUntilStealableTaskNs", "J", "", "nextParkedWorker", "Ljava/lang/Object;", "getNextParkedWorker", "()Ljava/lang/Object;", "setNextParkedWorker", "(Ljava/lang/Object;)V", "rngState", "Lkotlinx/coroutines/scheduling/CoroutineScheduler;", "getScheduler", "()Lkotlinx/coroutines/scheduling/CoroutineScheduler;", "scheduler", "state", "Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;", "terminationDeadline", "kotlinx-coroutines-core", "Ljava/lang/Thread;"}, k = 1, mv = {1, 4, 0})
    public final class Worker extends Thread {
        static final AtomicIntegerFieldUpdater workerCtl$FU = AtomicIntegerFieldUpdater.newUpdater(Worker.class, "workerCtl");
        private volatile int indexInArray;
        public final WorkQueue localQueue;
        public boolean mayHaveLocalTasks;
        private long minDelayUntilStealableTaskNs;
        private volatile Object nextParkedWorker;
        private int rngState;
        public WorkerState state;
        private long terminationDeadline;
        volatile int workerCtl;

        private Worker() {
            setDaemon(true);
            this.localQueue = new WorkQueue();
            this.state = WorkerState.DORMANT;
            this.workerCtl = 0;
            this.nextParkedWorker = CoroutineScheduler.NOT_IN_STACK;
            this.rngState = Random.INSTANCE.nextInt();
        }

        public final int getIndexInArray() {
            return this.indexInArray;
        }

        public final void setIndexInArray(int i) {
            StringBuilder sb = new StringBuilder();
            sb.append(CoroutineScheduler.this.schedulerName);
            sb.append("-worker-");
            sb.append(i == 0 ? "TERMINATED" : String.valueOf(i));
            setName(sb.toString());
            this.indexInArray = i;
        }

        public Worker(CoroutineScheduler coroutineScheduler, int i) {
            this();
            setIndexInArray(i);
        }

        /* renamed from: getScheduler, reason: from getter */
        public final CoroutineScheduler getThis$0() {
            return CoroutineScheduler.this;
        }

        public final Object getNextParkedWorker() {
            return this.nextParkedWorker;
        }

        public final void setNextParkedWorker(Object obj) {
            this.nextParkedWorker = obj;
        }

        private final boolean tryAcquireCpuPermit() {
            boolean z;
            if (this.state != WorkerState.CPU_ACQUIRED) {
                CoroutineScheduler coroutineScheduler = CoroutineScheduler.this;
                while (true) {
                    long j = coroutineScheduler.controlState;
                    if (((int) ((CoroutineScheduler.CPU_PERMITS_MASK & j) >> 42)) == 0) {
                        z = false;
                        break;
                    }
                    if (CoroutineScheduler.controlState$FU.compareAndSet(coroutineScheduler, j, j - 4398046511104L)) {
                        z = true;
                        break;
                    }
                }
                if (!z) {
                    return false;
                }
                this.state = WorkerState.CPU_ACQUIRED;
            }
            return true;
        }

        public final boolean tryReleaseCpu$kotlinx_coroutines_core(WorkerState newState) {
            WorkerState workerState = this.state;
            boolean z = workerState == WorkerState.CPU_ACQUIRED;
            if (z) {
                CoroutineScheduler.controlState$FU.addAndGet(CoroutineScheduler.this, 4398046511104L);
            }
            if (workerState != newState) {
                this.state = newState;
            }
            return z;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            runWorker();
        }

        private final void runWorker() {
            loop0: while (true) {
                boolean z = false;
                while (!CoroutineScheduler.this.isTerminated() && this.state != WorkerState.TERMINATED) {
                    Task findTask = findTask(this.mayHaveLocalTasks);
                    if (findTask != null) {
                        this.minDelayUntilStealableTaskNs = 0L;
                        executeTask(findTask);
                    } else {
                        this.mayHaveLocalTasks = false;
                        if (this.minDelayUntilStealableTaskNs == 0) {
                            tryPark();
                        } else if (z) {
                            tryReleaseCpu$kotlinx_coroutines_core(WorkerState.PARKING);
                            Thread.interrupted();
                            LockSupport.parkNanos(this.minDelayUntilStealableTaskNs);
                            this.minDelayUntilStealableTaskNs = 0L;
                        } else {
                            z = true;
                        }
                    }
                }
            }
            tryReleaseCpu$kotlinx_coroutines_core(WorkerState.TERMINATED);
        }

        private final void tryPark() {
            if (!inStack()) {
                CoroutineScheduler.this.parkedWorkersStackPush$kotlinx_coroutines_core(this);
                return;
            }
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (!(this.localQueue.getSize$kotlinx_coroutines_core() == 0)) {
                    throw new AssertionError();
                }
            }
            this.workerCtl = -1;
            while (inStack() && !CoroutineScheduler.this.isTerminated() && this.state != WorkerState.TERMINATED) {
                tryReleaseCpu$kotlinx_coroutines_core(WorkerState.PARKING);
                Thread.interrupted();
                park();
            }
        }

        private final boolean inStack() {
            return this.nextParkedWorker != CoroutineScheduler.NOT_IN_STACK;
        }

        private final void beforeTask(int taskMode) {
            if (taskMode != 0 && tryReleaseCpu$kotlinx_coroutines_core(WorkerState.BLOCKING)) {
                CoroutineScheduler.this.signalCpuWork$kotlinx_coroutines_core();
            }
        }

        private final void afterTask(int taskMode) {
            if (taskMode == 0) {
                return;
            }
            CoroutineScheduler.controlState$FU.addAndGet(CoroutineScheduler.this, CoroutineScheduler.PARKED_VERSION_MASK);
            WorkerState workerState = this.state;
            if (workerState != WorkerState.TERMINATED) {
                if (DebugKt.getASSERTIONS_ENABLED()) {
                    if (!(workerState == WorkerState.BLOCKING)) {
                        throw new AssertionError();
                    }
                }
                this.state = WorkerState.DORMANT;
            }
        }

        public final int nextInt$kotlinx_coroutines_core(int upperBound) {
            int i = this.rngState;
            int i2 = i ^ (i << 13);
            int i3 = i2 ^ (i2 >> 17);
            int i4 = i3 ^ (i3 << 5);
            this.rngState = i4;
            int i5 = upperBound - 1;
            return (i5 & upperBound) == 0 ? i4 & i5 : (i4 & Integer.MAX_VALUE) % upperBound;
        }

        private final void park() {
            if (this.terminationDeadline == 0) {
                this.terminationDeadline = System.nanoTime() + CoroutineScheduler.this.idleWorkerKeepAliveNs;
            }
            LockSupport.parkNanos(CoroutineScheduler.this.idleWorkerKeepAliveNs);
            if (System.nanoTime() - this.terminationDeadline >= 0) {
                this.terminationDeadline = 0L;
                tryTerminateWorker();
            }
        }

        private final void tryTerminateWorker() {
            synchronized (CoroutineScheduler.this.workers) {
                if (CoroutineScheduler.this.isTerminated()) {
                    return;
                }
                if (CoroutineScheduler.this.getCreatedWorkers() <= CoroutineScheduler.this.corePoolSize) {
                    return;
                }
                if (workerCtl$FU.compareAndSet(this, -1, 1)) {
                    int i = this.indexInArray;
                    setIndexInArray(0);
                    CoroutineScheduler.this.parkedWorkersStackTopUpdate$kotlinx_coroutines_core(this, i, 0);
                    int andDecrement = (int) (CoroutineScheduler.controlState$FU.getAndDecrement(CoroutineScheduler.this) & 2097151);
                    if (andDecrement != i) {
                        Worker worker = CoroutineScheduler.this.workers.get(andDecrement);
                        Intrinsics.checkNotNull(worker);
                        Worker worker2 = worker;
                        CoroutineScheduler.this.workers.set(i, worker2);
                        worker2.setIndexInArray(i);
                        CoroutineScheduler.this.parkedWorkersStackTopUpdate$kotlinx_coroutines_core(worker2, andDecrement, i);
                    }
                    CoroutineScheduler.this.workers.set(andDecrement, null);
                    Unit unit = Unit.INSTANCE;
                    this.state = WorkerState.TERMINATED;
                }
            }
        }

        private final void idleReset(int mode) {
            this.terminationDeadline = 0L;
            if (this.state == WorkerState.PARKING) {
                if (DebugKt.getASSERTIONS_ENABLED()) {
                    if (!(mode == 1)) {
                        throw new AssertionError();
                    }
                }
                this.state = WorkerState.BLOCKING;
            }
        }

        public final Task findTask(boolean scanLocalQueue) {
            Task removeFirstOrNull;
            if (tryAcquireCpuPermit()) {
                return findAnyTask(scanLocalQueue);
            }
            if (scanLocalQueue) {
                removeFirstOrNull = this.localQueue.poll();
                if (removeFirstOrNull == null) {
                    removeFirstOrNull = CoroutineScheduler.this.globalBlockingQueue.removeFirstOrNull();
                }
            } else {
                removeFirstOrNull = CoroutineScheduler.this.globalBlockingQueue.removeFirstOrNull();
            }
            return removeFirstOrNull != null ? removeFirstOrNull : trySteal(true);
        }

        private final Task findAnyTask(boolean scanLocalQueue) {
            Task pollGlobalQueues;
            Task pollGlobalQueues2;
            if (scanLocalQueue) {
                boolean z = nextInt$kotlinx_coroutines_core(CoroutineScheduler.this.corePoolSize * 2) == 0;
                if (z && (pollGlobalQueues2 = pollGlobalQueues()) != null) {
                    return pollGlobalQueues2;
                }
                Task poll = this.localQueue.poll();
                if (poll != null) {
                    return poll;
                }
                if (!z && (pollGlobalQueues = pollGlobalQueues()) != null) {
                    return pollGlobalQueues;
                }
            } else {
                Task pollGlobalQueues3 = pollGlobalQueues();
                if (pollGlobalQueues3 != null) {
                    return pollGlobalQueues3;
                }
            }
            return trySteal(false);
        }

        private final Task pollGlobalQueues() {
            if (nextInt$kotlinx_coroutines_core(2) == 0) {
                Task removeFirstOrNull = CoroutineScheduler.this.globalCpuQueue.removeFirstOrNull();
                return removeFirstOrNull != null ? removeFirstOrNull : CoroutineScheduler.this.globalBlockingQueue.removeFirstOrNull();
            }
            Task removeFirstOrNull2 = CoroutineScheduler.this.globalBlockingQueue.removeFirstOrNull();
            return removeFirstOrNull2 != null ? removeFirstOrNull2 : CoroutineScheduler.this.globalCpuQueue.removeFirstOrNull();
        }

        private final Task trySteal(boolean blockingOnly) {
            long tryStealFrom;
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (!(this.localQueue.getSize$kotlinx_coroutines_core() == 0)) {
                    throw new AssertionError();
                }
            }
            int createdWorkers = CoroutineScheduler.this.getCreatedWorkers();
            if (createdWorkers < 2) {
                return null;
            }
            int nextInt$kotlinx_coroutines_core = nextInt$kotlinx_coroutines_core(createdWorkers);
            long j = Long.MAX_VALUE;
            for (int i = 0; i < createdWorkers; i++) {
                nextInt$kotlinx_coroutines_core++;
                if (nextInt$kotlinx_coroutines_core > createdWorkers) {
                    nextInt$kotlinx_coroutines_core = 1;
                }
                Worker worker = CoroutineScheduler.this.workers.get(nextInt$kotlinx_coroutines_core);
                if (worker != null && worker != this) {
                    if (DebugKt.getASSERTIONS_ENABLED()) {
                        if (!(this.localQueue.getSize$kotlinx_coroutines_core() == 0)) {
                            throw new AssertionError();
                        }
                    }
                    if (blockingOnly) {
                        tryStealFrom = this.localQueue.tryStealBlockingFrom(worker.localQueue);
                    } else {
                        tryStealFrom = this.localQueue.tryStealFrom(worker.localQueue);
                    }
                    if (tryStealFrom == -1) {
                        return this.localQueue.poll();
                    }
                    if (tryStealFrom > 0) {
                        j = Math.min(j, tryStealFrom);
                    }
                }
            }
            if (j == LongCompanionObject.MAX_VALUE) {
                j = 0;
            }
            this.minDelayUntilStealableTaskNs = j;
            return null;
        }

        private final void executeTask(Task task) {
            int taskMode = task.taskContext.getTaskMode();
            idleReset(taskMode);
            beforeTask(taskMode);
            CoroutineScheduler.this.runSafely(task);
            afterTask(taskMode);
        }
    }

    private final boolean addToGlobalQueue(Task task) {
        if (task.taskContext.getTaskMode() == 1) {
            return this.globalBlockingQueue.addLast(task);
        }
        return this.globalCpuQueue.addLast(task);
    }

    public final void parkedWorkersStackTopUpdate$kotlinx_coroutines_core(Worker worker, int oldIndex, int newIndex) {
        while (true) {
            long j = this.parkedWorkersStack;
            int i = (int) (2097151 & j);
            long j2 = (2097152 + j) & PARKED_VERSION_MASK;
            if (i == oldIndex) {
                i = newIndex == 0 ? parkedWorkersStackNextIndex(worker) : newIndex;
            }
            if (i >= 0 && parkedWorkersStack$FU.compareAndSet(this, j, j2 | i)) {
                return;
            }
        }
    }

    private final Worker parkedWorkersStackPop() {
        while (true) {
            long j = this.parkedWorkersStack;
            Worker worker = this.workers.get((int) (2097151 & j));
            if (worker == null) {
                return null;
            }
            long j2 = (2097152 + j) & PARKED_VERSION_MASK;
            int parkedWorkersStackNextIndex = parkedWorkersStackNextIndex(worker);
            if (parkedWorkersStackNextIndex >= 0 && parkedWorkersStack$FU.compareAndSet(this, j, parkedWorkersStackNextIndex | j2)) {
                worker.setNextParkedWorker(NOT_IN_STACK);
                return worker;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean tryAcquireCpuPermit() {
        long j;
        do {
            j = this.controlState;
            if (((int) ((CPU_PERMITS_MASK & j) >> 42)) == 0) {
                return false;
            }
        } while (!controlState$FU.compareAndSet(this, j, j - 4398046511104L));
        return true;
    }

    private final void signalBlockingWork(boolean skipUnpark) {
        long addAndGet = controlState$FU.addAndGet(this, 2097152L);
        if (skipUnpark || tryUnpark() || tryCreateWorker(addAndGet)) {
            return;
        }
        tryUnpark();
    }
}
