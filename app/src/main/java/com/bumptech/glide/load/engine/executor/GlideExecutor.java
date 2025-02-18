package com.bumptech.glide.load.engine.executor;

import android.os.Process;
import android.os.StrictMode;
import android.util.Log;
import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public final class GlideExecutor extends ThreadPoolExecutor {
    private static final String CPU_LOCATION = "/sys/devices/system/cpu/";
    private static final String CPU_NAME_REGEX = "cpu[0-9]+";
    public static final String DEFAULT_DISK_CACHE_EXECUTOR_NAME = "disk-cache";
    public static final int DEFAULT_DISK_CACHE_EXECUTOR_THREADS = 1;
    public static final String DEFAULT_SOURCE_EXECUTOR_NAME = "source";
    private static final int MAXIMUM_AUTOMATIC_THREAD_COUNT = 4;
    private static final long SOURCE_UNLIMITED_EXECUTOR_KEEP_ALIVE_TIME_MS = TimeUnit.SECONDS.toMillis(10);
    private static final String SOURCE_UNLIMITED_EXECUTOR_NAME = "source-unlimited";
    private static final String TAG = "GlideExecutor";
    private final boolean executeSynchronously;

    public enum UncaughtThrowableStrategy {
        IGNORE,
        LOG { // from class: com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.1
            @Override // com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy
            protected void handle(Throwable th) {
                if (th == null || !Log.isLoggable(GlideExecutor.TAG, 6)) {
                    return;
                }
                Log.e(GlideExecutor.TAG, "Request threw uncaught throwable", th);
            }
        },
        THROW { // from class: com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.2
            @Override // com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy
            protected void handle(Throwable th) {
                super.handle(th);
                if (th != null) {
                    throw new RuntimeException("Request threw uncaught throwable", th);
                }
            }
        };

        public static final UncaughtThrowableStrategy DEFAULT = LOG;

        protected void handle(Throwable th) {
        }
    }

    public static GlideExecutor newDiskCacheExecutor() {
        return newDiskCacheExecutor(1, DEFAULT_DISK_CACHE_EXECUTOR_NAME, UncaughtThrowableStrategy.DEFAULT);
    }

    public static GlideExecutor newDiskCacheExecutor(int i, String str, UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        return new GlideExecutor(i, str, uncaughtThrowableStrategy, true, false);
    }

    public static GlideExecutor newSourceExecutor() {
        return newSourceExecutor(calculateBestThreadCount(), "source", UncaughtThrowableStrategy.DEFAULT);
    }

    public static GlideExecutor newSourceExecutor(int i, String str, UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        return new GlideExecutor(i, str, uncaughtThrowableStrategy, false, false);
    }

    public static GlideExecutor newUnlimitedSourceExecutor() {
        return new GlideExecutor(0, Integer.MAX_VALUE, SOURCE_UNLIMITED_EXECUTOR_KEEP_ALIVE_TIME_MS, SOURCE_UNLIMITED_EXECUTOR_NAME, UncaughtThrowableStrategy.DEFAULT, false, false, new SynchronousQueue());
    }

    GlideExecutor(int i, String str, UncaughtThrowableStrategy uncaughtThrowableStrategy, boolean z, boolean z2) {
        this(i, i, 0L, str, uncaughtThrowableStrategy, z, z2);
    }

    GlideExecutor(int i, int i2, long j, String str, UncaughtThrowableStrategy uncaughtThrowableStrategy, boolean z, boolean z2) {
        this(i, i2, j, str, uncaughtThrowableStrategy, z, z2, new PriorityBlockingQueue());
    }

    GlideExecutor(int i, int i2, long j, String str, UncaughtThrowableStrategy uncaughtThrowableStrategy, boolean z, boolean z2, BlockingQueue<Runnable> blockingQueue) {
        super(i, i2, j, TimeUnit.MILLISECONDS, blockingQueue, new DefaultThreadFactory(str, uncaughtThrowableStrategy, z));
        this.executeSynchronously = z2;
    }

    @Override // java.util.concurrent.ThreadPoolExecutor, java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        if (this.executeSynchronously) {
            runnable.run();
        } else {
            super.execute(runnable);
        }
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public Future<?> submit(Runnable runnable) {
        return maybeWait(super.submit(runnable));
    }

    private <T> Future<T> maybeWait(Future<T> future) {
        if (this.executeSynchronously) {
            boolean z = false;
            while (!future.isDone()) {
                try {
                    try {
                        future.get();
                    } catch (InterruptedException unused) {
                        z = true;
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                } finally {
                    if (z) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        return future;
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Runnable runnable, T t) {
        return maybeWait(super.submit(runnable, t));
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Callable<T> callable) {
        return maybeWait(super.submit(callable));
    }

    public static int calculateBestThreadCount() {
        File[] fileArr;
        StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            File file = new File(CPU_LOCATION);
            final Pattern compile = Pattern.compile(CPU_NAME_REGEX);
            fileArr = file.listFiles(new FilenameFilter() { // from class: com.bumptech.glide.load.engine.executor.GlideExecutor.1
                @Override // java.io.FilenameFilter
                public boolean accept(File file2, String str) {
                    return compile.matcher(str).matches();
                }
            });
        } catch (Throwable th) {
            try {
                if (Log.isLoggable(TAG, 6)) {
                    Log.e(TAG, "Failed to calculate accurate cpu count", th);
                }
                StrictMode.setThreadPolicy(allowThreadDiskReads);
                fileArr = null;
            } finally {
                StrictMode.setThreadPolicy(allowThreadDiskReads);
            }
        }
        return Math.min(4, Math.max(Math.max(1, Runtime.getRuntime().availableProcessors()), fileArr != null ? fileArr.length : 0));
    }

    private static final class DefaultThreadFactory implements ThreadFactory {
        private final String name;
        final boolean preventNetworkOperations;
        private int threadNum;
        final UncaughtThrowableStrategy uncaughtThrowableStrategy;

        DefaultThreadFactory(String str, UncaughtThrowableStrategy uncaughtThrowableStrategy, boolean z) {
            this.name = str;
            this.uncaughtThrowableStrategy = uncaughtThrowableStrategy;
            this.preventNetworkOperations = z;
        }

        @Override // java.util.concurrent.ThreadFactory
        public synchronized Thread newThread(Runnable runnable) {
            Thread thread;
            thread = new Thread(runnable, "glide-" + this.name + "-thread-" + this.threadNum) { // from class: com.bumptech.glide.load.engine.executor.GlideExecutor.DefaultThreadFactory.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    Process.setThreadPriority(9);
                    if (DefaultThreadFactory.this.preventNetworkOperations) {
                        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyDeath().build());
                    }
                    try {
                        super.run();
                    } catch (Throwable th) {
                        DefaultThreadFactory.this.uncaughtThrowableStrategy.handle(th);
                    }
                }
            };
            this.threadNum = this.threadNum + 1;
            return thread;
        }
    }
}
