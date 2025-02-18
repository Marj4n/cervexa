package com.baidu.trace;

import android.content.Context;
import android.os.Handler;
import com.baidu.trace.api.fence.FenceListRequest;
import com.baidu.trace.api.fence.OnFenceListener;

/* loaded from: classes.dex */
final class ae implements Runnable {
    private /* synthetic */ Context a;
    private /* synthetic */ Handler b;
    private /* synthetic */ FenceListRequest c;
    private /* synthetic */ OnFenceListener d;

    ae(Context context, Handler handler, FenceListRequest fenceListRequest, OnFenceListener onFenceListener) {
        this.a = context;
        this.b = handler;
        this.c = fenceListRequest;
        this.d = onFenceListener;
    }

    @Override // java.lang.Runnable
    public final void run() {
        a.b(this.a, this.b, this.c, this.d);
    }
}
