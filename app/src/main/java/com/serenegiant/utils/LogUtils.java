package com.serenegiant.utils;

import android.util.Log;

/* loaded from: classes2.dex */
public class LogUtils {
    public static final int DEBUG_LEVEL_DEBUG = 4;
    public static final int DEBUG_LEVEL_ERROR = 1;
    public static final int DEBUG_LEVEL_INFO = 3;
    public static final int DEBUG_LEVEL_OFF = 0;
    public static final int DEBUG_LEVEL_VERBOSE = 5;
    public static final int DEBUG_LEVEL_WARNING = 2;
    private static String TAG = LogUtils.class.getSimpleName();
    private static int LOG_LEVEL = 0;

    private static String null2str(String str) {
        return str == null ? "(null)" : str;
    }

    public void tag(String str) {
        if (str != null) {
            TAG = str;
        } else {
            TAG = LogUtils.class.getSimpleName();
        }
    }

    public static void logLevel(int i) {
        LOG_LEVEL = i;
    }

    public static int logLevel() {
        return LOG_LEVEL;
    }

    public static void v() {
        if (LOG_LEVEL >= 5) {
            Log.v(TAG, getMetaInfo());
        }
    }

    public static void v(String str) {
        if (LOG_LEVEL >= 5) {
            Log.v(TAG, getMetaInfo() + null2str(str));
        }
    }

    public static void d() {
        if (LOG_LEVEL >= 4) {
            Log.d(TAG, getMetaInfo());
        }
    }

    public static void d(String str) {
        if (LOG_LEVEL >= 4) {
            Log.d(TAG, getMetaInfo() + null2str(str));
        }
    }

    public static void i() {
        if (LOG_LEVEL >= 3) {
            Log.i(TAG, getMetaInfo());
        }
    }

    public static void i(String str) {
        if (LOG_LEVEL >= 3) {
            Log.i(TAG, getMetaInfo() + null2str(str));
        }
    }

    public static void w(String str) {
        if (LOG_LEVEL >= 2) {
            Log.w(TAG, getMetaInfo() + null2str(str));
        }
    }

    public static void w(String str, Throwable th) {
        if (LOG_LEVEL >= 2) {
            Log.w(TAG, getMetaInfo() + null2str(str), th);
            printThrowable(th);
            if (th.getCause() != null) {
                printThrowable(th.getCause());
            }
        }
    }

    public static void e(String str) {
        if (LOG_LEVEL >= 1) {
            Log.e(TAG, getMetaInfo() + null2str(str));
        }
    }

    public static void e(String str, Throwable th) {
        if (LOG_LEVEL >= 1) {
            Log.e(TAG, getMetaInfo() + null2str(str), th);
            printThrowable(th);
            if (th.getCause() != null) {
                printThrowable(th.getCause());
            }
        }
    }

    public static void e(Throwable th) {
        if (LOG_LEVEL >= 1) {
            printThrowable(th);
            if (th.getCause() != null) {
                printThrowable(th.getCause());
            }
        }
    }

    private static void printThrowable(Throwable th) {
        Log.e(TAG, th.getClass().getName() + ": " + th.getMessage());
        for (StackTraceElement stackTraceElement : th.getStackTrace()) {
            Log.e(TAG, "  at " + getMetaInfo(stackTraceElement));
        }
    }

    private static String getMetaInfo() {
        return getMetaInfo(Thread.currentThread().getStackTrace()[4]);
    }

    public static String getMetaInfo(StackTraceElement stackTraceElement) {
        String className = stackTraceElement.getClassName();
        return "[" + className.substring(className.lastIndexOf(".") + 1) + "#" + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber() + "]";
    }
}
