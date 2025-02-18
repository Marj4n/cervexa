package com.baidu.trace;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import com.baidu.trace.api.fence.CreateFenceRequest;
import com.baidu.trace.api.fence.OnFenceListener;

/* loaded from: classes.dex */
final class ab implements Runnable {
    private /* synthetic */ Context a;
    private /* synthetic */ Handler b;
    private /* synthetic */ CreateFenceRequest c;
    private /* synthetic */ OnFenceListener d;
    private /* synthetic */ IService e;

    ab(Context context, Handler handler, CreateFenceRequest createFenceRequest, OnFenceListener onFenceListener, IService iService) {
        this.a = context;
        this.b = handler;
        this.c = createFenceRequest;
        this.d = onFenceListener;
        this.e = iService;
    }

    @Override // java.lang.Runnable
    public final void run() {
        String b;
        IService iService;
        b = a.b(this.a, this.b, this.c.getTag(), this.c.getFence(), this.d);
        if (TextUtils.isEmpty(b) || (iService = this.e) == null) {
            return;
        }
        try {
            iService.handleLocalFence(0, this.c.getFence().getFenceId(), b);
        } catch (Exception unused) {
        }
    }
}
