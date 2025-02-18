package com.baidu.trace.b;

import android.content.Context;
import android.os.Handler;
import com.baidu.trace.ax;
import java.io.DataInputStream;

/* loaded from: classes.dex */
public final class g extends Thread {
    private Context a;
    private Handler b;
    private boolean c = true;
    private DataInputStream d = null;

    public g(Context context, Handler handler) {
        this.a = null;
        this.b = null;
        this.a = context;
        this.b = handler;
    }

    public final void a() {
        this.c = false;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        Handler handler;
        int i;
        while (this.c) {
            if (d.a(this.a)) {
                try {
                    DataInputStream c = a.c();
                    this.d = c;
                    if (c != null && !ax.a(c)) {
                        throw new Exception();
                    }
                    this.d = null;
                } catch (Exception unused) {
                    if (!this.c) {
                        return;
                    }
                    this.c = false;
                    handler = this.b;
                    if (handler != null) {
                        i = 4;
                    }
                }
            } else {
                this.c = false;
                handler = this.b;
                if (handler != null) {
                    i = 19;
                    handler.obtainMessage(i).sendToTarget();
                }
            }
        }
    }
}
