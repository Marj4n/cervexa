package com.baidu.trace.b;

import android.content.Context;
import android.os.Handler;
import com.baidu.trace.b.a;

/* loaded from: classes.dex */
public final class f extends Thread {
    private Context a;
    private Handler b;
    private boolean c = true;
    private int d = 10;

    public f(Context context, Handler handler) {
        this.a = null;
        this.a = context;
        this.b = handler;
    }

    static /* synthetic */ boolean a(f fVar, boolean z) {
        fVar.c = false;
        return false;
    }

    public final void a() {
        this.c = false;
    }

    public final void b() {
        a.e();
        this.d--;
        try {
            sleep(6000L);
        } catch (InterruptedException unused) {
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        while (this.c) {
            if (d.a(this.a)) {
                a.a(a.EnumC0016a.a, new e(this));
            } else {
                this.c = false;
                Handler handler = this.b;
                if (handler != null) {
                    handler.obtainMessage(19).sendToTarget();
                }
            }
        }
    }
}
