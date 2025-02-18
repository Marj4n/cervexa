package com.jieli.stream.dv.running2.util;

import android.util.Log;

/* loaded from: classes.dex */
public class Dbug {
    private static boolean IS_DEBUG = false;

    public static void openOrCloseDebug(boolean z) {
        IS_DEBUG = z;
    }

    public static void v(String str, String str2) {
        if (IS_DEBUG) {
            Log.v(str, str2);
        }
    }

    public static void d(String str, String str2) {
        if (IS_DEBUG) {
            Log.d(str, str2);
        }
    }

    public static void i(String str, String str2) {
        if (IS_DEBUG) {
            Log.i(str, str2);
        }
    }

    public static void w(String str, String str2) {
        if (IS_DEBUG) {
            Log.w(str, str2);
        }
    }

    public static void e(String str, String str2) {
        if (IS_DEBUG) {
            Log.e(str, str2);
        }
    }
}
