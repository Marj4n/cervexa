package com.serenegiant.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public class ThreadPool {
    private static final int CORE_POOL_SIZE = 4;
    private static final ThreadPoolExecutor EXECUTOR;
    private static final int KEEP_ALIVE_TIME = 10;
    private static final int MAX_POOL_SIZE = 32;

    static {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 32, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        EXECUTOR = threadPoolExecutor;
        threadPoolExecutor.allowCoreThreadTimeOut(true);
    }

    public static void preStartAllCoreThreads() {
        EXECUTOR.prestartAllCoreThreads();
    }

    public static void queueEvent(Runnable runnable) {
        EXECUTOR.execute(runnable);
    }

    public static boolean removeEvent(Runnable runnable) {
        return EXECUTOR.remove(runnable);
    }
}
