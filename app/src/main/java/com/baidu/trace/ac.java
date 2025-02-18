package com.baidu.trace;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import com.baidu.trace.api.fence.OnFenceListener;
import com.baidu.trace.api.fence.UpdateFenceRequest;

/* loaded from: classes.dex */
final class ac implements Runnable {
    private /* synthetic */ Context a;
    private /* synthetic */ Handler b;
    private /* synthetic */ UpdateFenceRequest c;
    private /* synthetic */ OnFenceListener d;
    private /* synthetic */ IService e;

    ac(Context context, Handler handler, UpdateFenceRequest updateFenceRequest, OnFenceListener onFenceListener, IService iService) {
        this.a = context;
        this.b = handler;
        this.c = updateFenceRequest;
        this.d = onFenceListener;
        this.e = iService;
    }

    @Override // java.lang.Runnable
    public final void run() {
        String b;
        IService iService;
        b = a.b(this.a, this.b, this.c, this.d);
        if (TextUtils.isEmpty(b) || (iService = this.e) == null) {
            return;
        }
        try {
            iService.handleLocalFence(1, this.c.getFence().getFenceId(), b);
        } catch (Exception unused) {
        }
    }
}
