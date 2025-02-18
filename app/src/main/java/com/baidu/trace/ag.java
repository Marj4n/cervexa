package com.baidu.trace;

import android.content.Context;
import android.os.Handler;
import com.baidu.trace.api.fence.MonitoredStatusByLocationRequest;
import com.baidu.trace.api.fence.OnFenceListener;

/* loaded from: classes.dex */
final class ag implements Runnable {
    private /* synthetic */ Context a;
    private /* synthetic */ Handler b;
    private /* synthetic */ MonitoredStatusByLocationRequest c;
    private /* synthetic */ OnFenceListener d;

    ag(Context context, Handler handler, MonitoredStatusByLocationRequest monitoredStatusByLocationRequest, OnFenceListener onFenceListener) {
        this.a = context;
        this.b = handler;
        this.c = monitoredStatusByLocationRequest;
        this.d = onFenceListener;
    }

    @Override // java.lang.Runnable
    public final void run() {
        a.b(this.a, this.b, this.c, this.d);
    }
}
