package com.generalplus.GoPlusDrone.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes.dex */
public class ThreadUtils {
    private static final byte TYPE_CACHED = -2;
    private static final byte TYPE_CPU = -8;
    private static final byte TYPE_IO = -4;
    private static final byte TYPE_SINGLE = -1;
    private static final Map<Integer, Map<Integer, ExecutorService>> TYPE_PRIORITY_POOLS = new ConcurrentHashMap();
    private static final Map<Task, ScheduledExecutorService> TASK_SCHEDULED = new ConcurrentHashMap();
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static ExecutorService getFixedPool(int i) {
        return getPoolByTypeAndPriority(i);
    }

    public static ExecutorService getFixedPool(int i, int i2) {
        return getPoolByTypeAndPriority(i, i2);
    }

    public static ExecutorService getSinglePool() {
        return getPoolByTypeAndPriority(-1);
    }

    public static ExecutorService getSinglePool(int i) {
        return getPoolByTypeAndPriority(-1, i);
    }

    public static ExecutorService getCachedPool() {
        return getPoolByTypeAndPriority(-2);
    }

    public static ExecutorService getCachedPool(int i) {
        return getPoolByTypeAndPriority(-2, i);
    }

    public static ExecutorService getIoPool() {
        return getPoolByTypeAndPriority(-2);
    }

    public static ExecutorService getIoPool(int i) {
        return getPoolByTypeAndPriority(-2, i);
    }

    public static ExecutorService getCpuPool() {
        return getPoolByTypeAndPriority(-8);
    }

    public static ExecutorService getCpuPool(int i) {
        return getPoolByTypeAndPriority(-8, i);
    }

    public static <T> void executeByFixed(int i, Task<T> task) {
        execute(getPoolByTypeAndPriority(i), task);
    }

    public static <T> void executeByFixed(int i, Task<T> task, int i2) {
        execute(getPoolByTypeAndPriority(i, i2), task);
    }

    public static <T> void executeByFixedWithDelay(int i, Task<T> task, long j, TimeUnit timeUnit) {
        executeWithDelay(getPoolByTypeAndPriority(i), task, j, timeUnit);
    }

    public static <T> void executeByFixedWithDelay(int i, Task<T> task, long j, TimeUnit timeUnit, int i2) {
        executeWithDelay(getPoolByTypeAndPriority(i, i2), task, j, timeUnit);
    }

    public static <T> void executeByFixedAtFixRate(int i, Task<T> task, long j, TimeUnit timeUnit) {
        executeAtFixedRate(getPoolByTypeAndPriority(i), task, 0L, j, timeUnit);
    }

    public static <T> void executeByFixedAtFixRate(int i, Task<T> task, long j, TimeUnit timeUnit, int i2) {
        executeAtFixedRate(getPoolByTypeAndPriority(i, i2), task, 0L, j, timeUnit);
    }

    public static <T> void executeByFixedAtFixRate(int i, Task<T> task, long j, long j2, TimeUnit timeUnit) {
        executeAtFixedRate(getPoolByTypeAndPriority(i), task, j, j2, timeUnit);
    }

    public static <T> void executeByFixedAtFixRate(int i, Task<T> task, long j, long j2, TimeUnit timeUnit, int i2) {
        executeAtFixedRate(getPoolByTypeAndPriority(i, i2), task, j, j2, timeUnit);
    }

    public static <T> void executeBySingle(Task<T> task) {
        execute(getPoolByTypeAndPriority(-1), task);
    }

    public static <T> void executeBySingle(Task<T> task, int i) {
        execute(getPoolByTypeAndPriority(-1, i), task);
    }

    public static <T> void executeBySingleWithDelay(Task<T> task, long j, TimeUnit timeUnit) {
        executeWithDelay(getPoolByTypeAndPriority(-1), task, j, timeUnit);
    }

    public static <T> void executeBySingleWithDelay(Task<T> task, long j, TimeUnit timeUnit, int i) {
        executeWithDelay(getPoolByTypeAndPriority(-1, i), task, j, timeUnit);
    }

    public static <T> void executeBySingleAtFixRate(Task<T> task, long j, TimeUnit timeUnit) {
        executeAtFixedRate(getPoolByTypeAndPriority(-1), task, 0L, j, timeUnit);
    }

    public static <T> void executeBySingleAtFixRate(Task<T> task, long j, TimeUnit timeUnit, int i) {
        executeAtFixedRate(getPoolByTypeAndPriority(-1, i), task, 0L, j, timeUnit);
    }

    public static <T> void executeBySingleAtFixRate(Task<T> task, long j, long j2, TimeUnit timeUnit) {
        executeAtFixedRate(getPoolByTypeAndPriority(-1), task, j, j2, timeUnit);
    }

    public static <T> void executeBySingleAtFixRate(Task<T> task, long j, long j2, TimeUnit timeUnit, int i) {
        executeAtFixedRate(getPoolByTypeAndPriority(-1, i), task, j, j2, timeUnit);
    }

    public static <T> void executeByCached(Task<T> task) {
        execute(getPoolByTypeAndPriority(-2), task);
    }

    public static <T> void executeByCached(Task<T> task, int i) {
        execute(getPoolByTypeAndPriority(-2, i), task);
    }

    public static <T> void executeByCachedWithDelay(Task<T> task, long j, TimeUnit timeUnit) {
        executeWithDelay(getPoolByTypeAndPriority(-2), task, j, timeUnit);
    }

    public static <T> void executeByCachedWithDelay(Task<T> task, long j, TimeUnit timeUnit, int i) {
        executeWithDelay(getPoolByTypeAndPriority(-2, i), task, j, timeUnit);
    }

    public static <T> void executeByCachedAtFixRate(Task<T> task, long j, TimeUnit timeUnit) {
        executeAtFixedRate(getPoolByTypeAndPriority(-2), task, 0L, j, timeUnit);
    }

    public static <T> void executeByCachedAtFixRate(Task<T> task, long j, TimeUnit timeUnit, int i) {
        executeAtFixedRate(getPoolByTypeAndPriority(-2, i), task, 0L, j, timeUnit);
    }

    public static <T> void executeByCachedAtFixRate(Task<T> task, long j, long j2, TimeUnit timeUnit) {
        executeAtFixedRate(getPoolByTypeAndPriority(-2), task, j, j2, timeUnit);
    }

    public static <T> void executeByCachedAtFixRate(Task<T> task, long j, long j2, TimeUnit timeUnit, int i) {
        executeAtFixedRate(getPoolByTypeAndPriority(-2, i), task, j, j2, timeUnit);
    }

    public static <T> void executeByIo(Task<T> task) {
        execute(getPoolByTypeAndPriority(-4), task);
    }

    public static <T> void executeByIo(Task<T> task, int i) {
        execute(getPoolByTypeAndPriority(-4, i), task);
    }

    public static <T> void executeByIoWithDelay(Task<T> task, long j, TimeUnit timeUnit) {
        executeWithDelay(getPoolByTypeAndPriority(-4), task, j, timeUnit);
    }

    public static <T> void executeByIoWithDelay(Task<T> task, long j, TimeUnit timeUnit, int i) {
        executeWithDelay(getPoolByTypeAndPriority(-4, i), task, j, timeUnit);
    }

    public static <T> void executeByIoAtFixRate(Task<T> task, long j, TimeUnit timeUnit) {
        executeAtFixedRate(getPoolByTypeAndPriority(-4), task, 0L, j, timeUnit);
    }

    public static <T> void executeByIoAtFixRate(Task<T> task, long j, TimeUnit timeUnit, int i) {
        executeAtFixedRate(getPoolByTypeAndPriority(-4, i), task, 0L, j, timeUnit);
    }

    public static <T> void executeByIoAtFixRate(Task<T> task, long j, long j2, TimeUnit timeUnit) {
        executeAtFixedRate(getPoolByTypeAndPriority(-4), task, j, j2, timeUnit);
    }

    public static <T> void executeByIoAtFixRate(Task<T> task, long j, long j2, TimeUnit timeUnit, int i) {
        executeAtFixedRate(getPoolByTypeAndPriority(-4, i), task, j, j2, timeUnit);
    }

    public static <T> void executeByCpu(Task<T> task) {
        execute(getPoolByTypeAndPriority(-8), task);
    }

    public static <T> void executeByCpu(Task<T> task, int i) {
        execute(getPoolByTypeAndPriority(-8, i), task);
    }

    public static <T> void executeByCpuWithDelay(Task<T> task, long j, TimeUnit timeUnit) {
        executeWithDelay(getPoolByTypeAndPriority(-8), task, j, timeUnit);
    }

    public static <T> void executeByCpuWithDelay(Task<T> task, long j, TimeUnit timeUnit, int i) {
        executeWithDelay(getPoolByTypeAndPriority(-8, i), task, j, timeUnit);
    }

    public static <T> void executeByCpuAtFixRate(Task<T> task, long j, TimeUnit timeUnit) {
        executeAtFixedRate(getPoolByTypeAndPriority(-8), task, 0L, j, timeUnit);
    }

    public static <T> void executeByCpuAtFixRate(Task<T> task, long j, TimeUnit timeUnit, int i) {
        executeAtFixedRate(getPoolByTypeAndPriority(-8, i), task, 0L, j, timeUnit);
    }

    public static <T> void executeByCpuAtFixRate(Task<T> task, long j, long j2, TimeUnit timeUnit) {
        executeAtFixedRate(getPoolByTypeAndPriority(-8), task, j, j2, timeUnit);
    }

    public static <T> void executeByCpuAtFixRate(Task<T> task, long j, long j2, TimeUnit timeUnit, int i) {
        executeAtFixedRate(getPoolByTypeAndPriority(-8, i), task, j, j2, timeUnit);
    }

    public static <T> void executeByCustom(ExecutorService executorService, Task<T> task) {
        execute(executorService, task);
    }

    public static <T> void executeByCustomWithDelay(ExecutorService executorService, Task<T> task, long j, TimeUnit timeUnit) {
        executeWithDelay(executorService, task, j, timeUnit);
    }

    public static <T> void executeByCustomAtFixRate(ExecutorService executorService, Task<T> task, long j, TimeUnit timeUnit) {
        executeAtFixedRate(executorService, task, 0L, j, timeUnit);
    }

    public static <T> void executeByCustomAtFixRate(ExecutorService executorService, Task<T> task, long j, long j2, TimeUnit timeUnit) {
        executeAtFixedRate(executorService, task, j, j2, timeUnit);
    }

    public static void cancel(Task task) {
        task.cancel();
    }

    private static <T> void execute(ExecutorService executorService, Task<T> task) {
        executeWithDelay(executorService, task, 0L, TimeUnit.MILLISECONDS);
    }

    private static <T> void executeWithDelay(final ExecutorService executorService, final Task<T> task, long j, TimeUnit timeUnit) {
        if (j <= 0) {
            getScheduledByTask(task).execute(new Runnable() { // from class: com.generalplus.GoPlusDrone.Fragment.ThreadUtils.1
                @Override // java.lang.Runnable
                public void run() {
                    executorService.execute(task);
                }
            });
        } else {
            getScheduledByTask(task).schedule(new Runnable() { // from class: com.generalplus.GoPlusDrone.Fragment.ThreadUtils.2
                @Override // java.lang.Runnable
                public void run() {
                    executorService.execute(task);
                }
            }, j, timeUnit);
        }
    }

    private static <T> void executeAtFixedRate(final ExecutorService executorService, final Task<T> task, long j, long j2, TimeUnit timeUnit) {
        ((Task) task).isSchedule = true;
        getScheduledByTask(task).scheduleAtFixedRate(new Runnable() { // from class: com.generalplus.GoPlusDrone.Fragment.ThreadUtils.3
            @Override // java.lang.Runnable
            public void run() {
                executorService.execute(task);
            }
        }, j, j2, timeUnit);
    }

    private static ScheduledExecutorService getScheduledByTask(Task task) {
        ScheduledExecutorService scheduledExecutorService = TASK_SCHEDULED.get(task);
        if (scheduledExecutorService != null) {
            return scheduledExecutorService;
        }
        ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1, new UtilsThreadFactory("scheduled", 10));
        TASK_SCHEDULED.put(task, newScheduledThreadPool);
        return newScheduledThreadPool;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeScheduleByTask(Task task) {
        ScheduledExecutorService scheduledExecutorService = TASK_SCHEDULED.get(task);
        if (scheduledExecutorService != null) {
            TASK_SCHEDULED.remove(task);
            shutdownAndAwaitTermination(scheduledExecutorService);
        }
    }

    private static ExecutorService getPoolByTypeAndPriority(int i) {
        return getPoolByTypeAndPriority(i, 5);
    }

    private static ExecutorService getPoolByTypeAndPriority(int i, int i2) {
        Map<Integer, ExecutorService> map = TYPE_PRIORITY_POOLS.get(Integer.valueOf(i));
        if (map == null) {
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            ExecutorService createPoolByTypeAndPriority = createPoolByTypeAndPriority(i, i2);
            concurrentHashMap.put(Integer.valueOf(i2), createPoolByTypeAndPriority);
            TYPE_PRIORITY_POOLS.put(Integer.valueOf(i), concurrentHashMap);
            return createPoolByTypeAndPriority;
        }
        ExecutorService executorService = map.get(Integer.valueOf(i2));
        if (executorService != null) {
            return executorService;
        }
        ExecutorService createPoolByTypeAndPriority2 = createPoolByTypeAndPriority(i, i2);
        map.put(Integer.valueOf(i2), createPoolByTypeAndPriority2);
        return createPoolByTypeAndPriority2;
    }

    private static ExecutorService createPoolByTypeAndPriority(int i, int i2) {
        if (i == -8) {
            int i3 = CPU_COUNT;
            return new ThreadPoolExecutor(i3 + 1, (i3 * 2) + 1, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue(128), new UtilsThreadFactory("cpu", i2));
        }
        if (i == -4) {
            int i4 = CPU_COUNT;
            return new ThreadPoolExecutor((i4 * 2) + 1, (i4 * 2) + 1, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue(128), new UtilsThreadFactory("io", i2));
        }
        if (i == -2) {
            return Executors.newCachedThreadPool(new UtilsThreadFactory("cached", i2));
        }
        if (i == -1) {
            return Executors.newSingleThreadExecutor(new UtilsThreadFactory("single", i2));
        }
        return Executors.newFixedThreadPool(i, new UtilsThreadFactory("fixed(" + i + ")", i2));
    }

    private static void shutdownAndAwaitTermination(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (executorService.awaitTermination(60L, TimeUnit.SECONDS)) {
                return;
            }
            executorService.shutdownNow();
            if (executorService.awaitTermination(60L, TimeUnit.SECONDS)) {
                return;
            }
            System.err.println("Pool did not terminate");
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public static abstract class SimpleTask<T> extends Task<T> {
        @Override // com.generalplus.GoPlusDrone.Fragment.ThreadUtils.Task
        public void onCancel() {
            Log.e("ThreadUtils", "onCancel: " + Thread.currentThread());
        }

        @Override // com.generalplus.GoPlusDrone.Fragment.ThreadUtils.Task
        public void onFail(Throwable th) {
            Log.e("ThreadUtils", "onFail: ", th);
        }
    }

    public static abstract class Task<T> implements Runnable {
        private static final int CANCELLED = 2;
        private static final int COMPLETING = 1;
        private static final int EXCEPTIONAL = 3;
        private static final int NEW = 0;
        private boolean isSchedule;
        private volatile int state = 0;

        public abstract T doInBackground() throws Throwable;

        public abstract void onCancel();

        public abstract void onFail(Throwable th);

        public abstract void onSuccess(T t);

        @Override // java.lang.Runnable
        public void run() {
            try {
                final T doInBackground = doInBackground();
                if (this.state != 0) {
                    return;
                }
                if (!this.isSchedule) {
                    this.state = 1;
                    Deliver.post(new Runnable() { // from class: com.generalplus.GoPlusDrone.Fragment.ThreadUtils.Task.2
                        /* JADX WARN: Multi-variable type inference failed */
                        @Override // java.lang.Runnable
                        public void run() {
                            Task.this.onSuccess(doInBackground);
                            ThreadUtils.removeScheduleByTask(Task.this);
                        }
                    });
                } else {
                    Deliver.post(new Runnable() { // from class: com.generalplus.GoPlusDrone.Fragment.ThreadUtils.Task.1
                        /* JADX WARN: Multi-variable type inference failed */
                        @Override // java.lang.Runnable
                        public void run() {
                            Task.this.onSuccess(doInBackground);
                        }
                    });
                }
            } catch (Throwable th) {
                if (this.state != 0) {
                    return;
                }
                this.state = 3;
                Deliver.post(new Runnable() { // from class: com.generalplus.GoPlusDrone.Fragment.ThreadUtils.Task.3
                    @Override // java.lang.Runnable
                    public void run() {
                        Task.this.onFail(th);
                        ThreadUtils.removeScheduleByTask(Task.this);
                    }
                });
            }
        }

        public void cancel() {
            if (this.state != 0) {
                return;
            }
            this.state = 2;
            Deliver.post(new Runnable() { // from class: com.generalplus.GoPlusDrone.Fragment.ThreadUtils.Task.4
                @Override // java.lang.Runnable
                public void run() {
                    Task.this.onCancel();
                    ThreadUtils.removeScheduleByTask(Task.this);
                }
            });
        }
    }

    private static final class UtilsThreadFactory extends AtomicLong implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final String namePrefix;
        private final int priority;

        UtilsThreadFactory(String str, int i) {
            SecurityManager securityManager = System.getSecurityManager();
            this.group = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = str + "-pool-" + POOL_NUMBER.getAndIncrement() + "-thread-";
            this.priority = i;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(this.group, runnable, this.namePrefix + getAndIncrement(), 0L) { // from class: com.generalplus.GoPlusDrone.Fragment.ThreadUtils.UtilsThreadFactory.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        super.run();
                    } catch (Throwable th) {
                        Log.e("ThreadUtils", "Request threw uncaught throwable", th);
                    }
                }
            };
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            thread.setPriority(this.priority);
            return thread;
        }
    }

    private static class Deliver {
        private static final Handler MAIN_HANDLER;

        private Deliver() {
        }

        static {
            Looper looper;
            try {
                looper = Looper.getMainLooper();
            } catch (Exception unused) {
                looper = null;
            }
            if (looper != null) {
                MAIN_HANDLER = new Handler(looper);
            } else {
                MAIN_HANDLER = null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void post(Runnable runnable) {
            Handler handler = MAIN_HANDLER;
            if (handler != null) {
                handler.post(runnable);
            } else {
                runnable.run();
            }
        }
    }
}
