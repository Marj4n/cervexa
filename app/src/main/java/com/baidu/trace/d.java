package com.baidu.trace;

import com.baidu.lbsapi.auth.tracesdk.LBSAuthManager;
import com.generalplus.GoPlusDrone.Fragment.BaseFragment;
import com.jieli.lib.dv.control.utils.TopicKey;
import com.jieli.stream.dv.running2.util.IConstant;
import java.util.Hashtable;

/* loaded from: classes.dex */
public final class d {
    protected static String a;
    protected static LBSAuthManager b;

    protected static void a() {
        if (b == null) {
            return;
        }
        Hashtable hashtable = new Hashtable();
        hashtable.put("from", "trace");
        hashtable.put("mb", com.baidu.trace.c.f.a);
        hashtable.put("os", IConstant.ANDROID_DIR);
        hashtable.put("sv", "3.0.7");
        hashtable.put("imt", b.getIMEI());
        hashtable.put("net", com.baidu.trace.c.e.a);
        hashtable.put("cpu", com.baidu.trace.c.f.b);
        hashtable.put("resid", "02");
        hashtable.put("appid", "-1");
        hashtable.put(TopicKey.VERSION, "1");
        hashtable.put("pcn", com.baidu.trace.c.e.c);
        hashtable.put("cuid", b.getCUID());
        hashtable.put(BaseFragment.DATA_NAME, com.baidu.trace.c.e.b);
        b.authenticate(true, "lbs_tracesdk", hashtable, new e());
    }

    protected static void b() {
        b = null;
    }
}
