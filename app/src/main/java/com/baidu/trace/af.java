package com.baidu.trace;

import android.content.Context;
import android.os.Handler;
import com.baidu.trace.api.fence.MonitoredStatusRequest;
import com.baidu.trace.api.fence.OnFenceListener;

/* loaded from: classes.dex */
final class af implements Runnable {
    private /* synthetic */ Context a;
    private /* synthetic */ Handler b;
    private /* synthetic */ MonitoredStatusRequest c;
    private /* synthetic */ OnFenceListener d;

    af(Context context, Handler handler, MonitoredStatusRequest monitoredStatusRequest, OnFenceListener onFenceListener) {
        this.a = context;
        this.b = handler;
        this.c = monitoredStatusRequest;
        this.d = onFenceListener;
    }

    @Override // java.lang.Runnable
    public final void run() {
        a.b(this.a, this.b, this.c, this.d);
    }
}
