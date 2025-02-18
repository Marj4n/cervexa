package com.baidu.trace;

import android.content.Context;
import android.os.Handler;
import com.baidu.trace.api.fence.HistoryAlarmRequest;
import com.baidu.trace.api.fence.OnFenceListener;

/* loaded from: classes.dex */
final class ah implements Runnable {
    private /* synthetic */ Context a;
    private /* synthetic */ Handler b;
    private /* synthetic */ HistoryAlarmRequest c;
    private /* synthetic */ OnFenceListener d;

    ah(Context context, Handler handler, HistoryAlarmRequest historyAlarmRequest, OnFenceListener onFenceListener) {
        this.a = context;
        this.b = handler;
        this.c = historyAlarmRequest;
        this.d = onFenceListener;
    }

    @Override // java.lang.Runnable
    public final void run() {
        a.b(this.a, this.b, this.c, this.d);
    }
}
