package com.jieli.lib.dv.control.utils;

import android.util.Log;

/* loaded from: classes.dex */
public class Dlog {
    private static boolean a = true;

    public static void v(String str, String str2) {
        if (a) {
            Log.v(str, str2);
        }
    }

    public static void d(String str, String str2) {
        if (a) {
            Log.d(str, str2);
        }
    }

    public static void i(String str, String str2) {
        if (a) {
            Log.i(str, str2);
        }
    }

    public static void w(String str, String str2) {
        if (a) {
            Log.w(str, str2);
        }
    }

    public static void e(String str, String str2) {
        if (a) {
            Log.e(str, str2);
        }
    }
}
