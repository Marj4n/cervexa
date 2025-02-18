package com.jieli.lib.dv.control.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/* loaded from: classes.dex */
public class ClientContext {
    private static Context a;
    private static Handler b;

    public static void set(Context context) {
        a = context;
    }

    public static Context get() {
        return a;
    }

    public static void post(Runnable runnable) {
        postDelayed(runnable, 0L);
    }

    public static void postDelayed(Runnable runnable, long j) {
        if (b == null) {
            b = new Handler(Looper.getMainLooper());
        }
        b.postDelayed(runnable, j);
    }
}
