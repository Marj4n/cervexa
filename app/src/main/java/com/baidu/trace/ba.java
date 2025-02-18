package com.baidu.trace;

import com.baidu.trace.c.f;

/* loaded from: classes.dex */
final class ba extends Thread {
    private /* synthetic */ f.a a;

    ba(f.a aVar) {
        this.a = aVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        synchronized (this) {
            try {
                wait(10000L);
            } catch (InterruptedException unused) {
            }
        }
        if (f.a.a == this.a) {
            if (az.a > 0) {
                com.baidu.trace.b.a.e();
            }
        } else {
            if (f.a.b != this.a || az.b <= 0) {
                return;
            }
            com.baidu.trace.b.a.e();
        }
    }
}
