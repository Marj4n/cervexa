package com.baidu.trace;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import com.baidu.trace.api.fence.DeleteFenceRequest;
import com.baidu.trace.api.fence.OnFenceListener;

/* loaded from: classes.dex */
final class ad implements Runnable {
    private /* synthetic */ Context a;
    private /* synthetic */ Handler b;
    private /* synthetic */ DeleteFenceRequest c;
    private /* synthetic */ OnFenceListener d;
    private /* synthetic */ IService e;

    ad(Context context, Handler handler, DeleteFenceRequest deleteFenceRequest, OnFenceListener onFenceListener, IService iService) {
        this.a = context;
        this.b = handler;
        this.c = deleteFenceRequest;
        this.d = onFenceListener;
        this.e = iService;
    }

    @Override // java.lang.Runnable
    public final void run() {
        StringBuffer stringBuffer = new StringBuffer();
        boolean a = a.a(this.a, this.b, this.c, stringBuffer, this.d);
        if (this.e != null) {
            if (this.c.getFenceIds() == null || !TextUtils.isEmpty(stringBuffer.toString())) {
                try {
                    if (this.c.getFenceIds() != null) {
                        this.e.handleLocalFence(2, 0L, stringBuffer.substring(0, stringBuffer.length() - 1));
                    } else if (a) {
                        this.e.handleLocalFence(3, 0L, null);
                    }
                } catch (Exception unused) {
                }
            }
        }
    }
}
