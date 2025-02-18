package com.baidu.trace.c;

import android.util.Log;
import com.baidu.trace.c.a;
import java.text.SimpleDateFormat;

/* loaded from: classes.dex */
public final class h {
    private static final SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
    private static a.AnonymousClass1 b;
    private static a.AnonymousClass1 c;

    /* renamed from: com.baidu.trace.c.h$1, reason: invalid class name */
    final /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[a.values().length];
            a = iArr;
            try {
                iArr[a.DEBUG.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[a.INFO.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[a.WARNING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                a[a.ERROR.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public enum a {
        DEBUG(0),
        INFO(1),
        WARNING(2),
        ERROR(3);

        a(int i) {
        }
    }

    static {
        new SimpleDateFormat("yyyy-MM-dd");
        b = null;
        c = null;
    }

    public static void a() {
    }

    public static void a(String str, String str2) {
        int i = AnonymousClass1.a[a.INFO.ordinal()];
        if (i == 1) {
            Log.d(str, str2);
        } else if (i == 2) {
            Log.i(str, str2);
        } else if (i == 3) {
            Log.w(str, str2);
        } else if (i == 4) {
            Log.e(str, str2);
        }
        "Error".equals(str);
    }

    public static void b() {
    }
}
