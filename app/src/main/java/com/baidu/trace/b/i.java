package com.baidu.trace.b;

import android.content.Context;
import android.os.Handler;
import com.baidu.trace.az;
import com.baidu.trace.c.f;

/* loaded from: classes.dex */
public final class i extends Thread {
    private Context a;
    private Handler b;
    private byte[] c;
    private f.a d;

    public i(Context context, Handler handler, byte[] bArr, f.a aVar) {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.a = context;
        this.b = handler;
        this.c = bArr;
        this.d = aVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        if (!d.a(this.a)) {
            Handler handler = this.b;
            if (handler != null) {
                handler.obtainMessage(19).sendToTarget();
                return;
            }
            return;
        }
        byte[] bArr = this.c;
        if (bArr != null && bArr.length > 0) {
            if (this.d != null) {
                if (f.a.a == this.d) {
                    az.a = com.baidu.trace.c.e.a();
                } else if (f.a.b == this.d) {
                    az.b = com.baidu.trace.c.e.a();
                }
                az.a(this.d);
            }
            a.a(this.c, this.b);
        }
        this.c = null;
    }
}
