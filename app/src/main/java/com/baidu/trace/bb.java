package com.baidu.trace;

import android.os.Handler;
import android.os.Message;

/* loaded from: classes.dex */
final class bb extends Thread {
    private /* synthetic */ Handler a;

    bb(Handler handler) {
        this.a = handler;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        Handler handler;
        synchronized (this) {
            try {
                wait(10000L);
            } catch (InterruptedException unused) {
            }
        }
        if (bd.f > 0 && (handler = this.a) != null) {
            Message obtainMessage = handler.obtainMessage(141);
            obtainMessage.arg1 = 141;
            obtainMessage.sendToTarget();
        }
        bd.f = 0;
    }
}
