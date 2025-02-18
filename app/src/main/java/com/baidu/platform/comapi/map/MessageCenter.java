package com.baidu.platform.comapi.map;

import android.os.Handler;

/* loaded from: classes.dex */
public class MessageCenter {
    public static void registMessage(int i, Handler handler) {
        com.baidu.platform.comjni.engine.a.a(i, handler);
    }

    public static void unregistMessage(int i, Handler handler) {
        com.baidu.platform.comjni.engine.a.b(i, handler);
    }
}
