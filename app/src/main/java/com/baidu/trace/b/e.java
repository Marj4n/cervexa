package com.baidu.trace.b;

import android.os.Handler;

/* loaded from: classes.dex */
public class e {
    final /* synthetic */ f a;

    e(f fVar) {
        this.a = fVar;
    }

    public void a() {
        Handler handler;
        f.a(this.a, false);
        handler = this.a.b;
        handler.obtainMessage(1).sendToTarget();
    }

    public void b() {
        int i;
        Handler handler;
        i = this.a.d;
        if (i > 0) {
            this.a.b();
            return;
        }
        f.a(this.a, false);
        handler = this.a.b;
        handler.obtainMessage(19).sendToTarget();
    }
}
