package com.baidu.trace.b;

import android.os.Handler;

/* loaded from: classes.dex */
public final class h extends Thread {
    private Handler a;
    private boolean b = true;

    public h(Handler handler) {
        this.a = handler;
    }

    public final void a() {
        this.b = false;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        synchronized (this) {
            try {
                wait(15000L);
            } catch (InterruptedException unused) {
            }
        }
        Handler handler = this.a;
        if (handler == null || !this.b) {
            return;
        }
        handler.obtainMessage(24).sendToTarget();
    }
}
