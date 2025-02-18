package com.baidu.trace.b;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.serenegiant.net.NetworkChangedReceiver;

/* loaded from: classes.dex */
public final class c extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) {
            return;
        }
        String action = intent.getAction();
        if (NetworkChangedReceiver.ACTION_GLOBAL_CONNECTIVITY_CHANGE.equals(action) || "com.baidu.trace.action.SOCKET_RECONNECT".equals(action)) {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected() || !activeNetworkInfo.isAvailable()) {
                d.b(Integer.MIN_VALUE);
                return;
            }
            int type = activeNetworkInfo.getType();
            if (d.h() && com.baidu.trace.c.e.b() - d.i() > 60) {
                d.a(false);
            }
            if ((d.a(type) || d.a(context)) && !d.h()) {
                d.b(type);
                d.a(true);
                d.a(com.baidu.trace.c.e.b());
                d.a().d();
            }
        }
    }
}
