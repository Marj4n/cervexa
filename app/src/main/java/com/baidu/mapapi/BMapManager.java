package com.baidu.mapapi;

import android.content.Context;
import com.baidu.platform.comapi.a;

/* loaded from: classes.dex */
public class BMapManager {
    public static void destroy() {
        a.a().d();
    }

    public static Context getContext() {
        return a.a().e();
    }

    public static void init() {
        a.a().b();
    }
}
