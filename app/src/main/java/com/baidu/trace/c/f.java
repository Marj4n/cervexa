package com.baidu.trace.c;

import android.os.Build;
import com.jieli.stream.dv.running2.util.IConstant;

/* loaded from: classes.dex */
public final class f {
    public static final String a = Build.BRAND + "-" + Build.MODEL;
    public static final String b = Build.CPU_ABI;
    public static final String c;

    /* JADX WARN: $VALUES field not found */
    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    public static final class a {
        public static final a a = new a("CONNECT", 0);
        public static final a b = new a("ENTER", 1);

        private a(String str, int i) {
        }
    }

    public enum b {
        NOT_START,
        LOGGING,
        STARTED,
        LOGOUTING
    }

    static {
        StringBuilder sb = new StringBuilder(IConstant.ANDROID_DIR);
        sb.append(Build.VERSION.RELEASE);
        c = sb.toString();
    }
}
