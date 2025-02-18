package com.baidu.trace;

import android.content.Context;
import android.os.Build;
import com.baidu.lbsapi.auth.tracesdk.LBSAuthManager;
import com.jieli.stream.dv.running2.util.IConstant;

/* loaded from: classes.dex */
public final class q {
    protected static String a = null;
    protected static String b = null;
    protected static String c = null;
    protected static String d = null;
    private static boolean e = false;

    protected static void a(Context context, LBSAuthManager lBSAuthManager) {
        if (e) {
            return;
        }
        a = IConstant.ANDROID_DIR + Build.VERSION.RELEASE;
        b = "3.0.7";
        c = lBSAuthManager.getCUID();
        d = com.baidu.trace.c.e.c(context);
        lBSAuthManager.getKey();
        lBSAuthManager.getMCode();
        e = true;
    }
}
