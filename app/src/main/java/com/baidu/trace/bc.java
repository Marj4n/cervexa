package com.baidu.trace;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.jieli.stream.dv.running2.util.IConstant;

/* loaded from: classes.dex */
public final class bc {
    private static final bc b = new bc();
    private Handler a;

    private bc() {
    }

    protected static bc a() {
        return b;
    }

    protected static void a(com.baidu.trace.a.h hVar) {
        com.baidu.trace.c.e.d = com.baidu.trace.c.e.a();
        aw.b(hVar.a);
    }

    protected static void b() {
    }

    protected final void a(int i) {
        Handler handler = this.a;
        if (handler == null) {
            return;
        }
        Message obtainMessage = handler.obtainMessage(32);
        obtainMessage.arg1 = i;
        obtainMessage.sendToTarget();
    }

    protected final void a(Handler handler) {
        this.a = handler;
    }

    protected final void a(com.baidu.trace.a.a aVar) {
        com.baidu.trace.c.e.d = com.baidu.trace.c.e.a();
        b.a = aVar.a;
        Handler handler = this.a;
        if (handler != null) {
            Message obtainMessage = handler.obtainMessage(141);
            obtainMessage.arg1 = 141;
            obtainMessage.sendToTarget();
        }
    }

    protected final void a(com.baidu.trace.a.d dVar) {
        com.baidu.trace.c.e.d = com.baidu.trace.c.e.a();
        if (this.a != null) {
            if (1 == dVar.a) {
                this.a.obtainMessage(28).sendToTarget();
                return;
            }
            Message obtainMessage = this.a.obtainMessage(30);
            obtainMessage.arg1 = 1241;
            obtainMessage.sendToTarget();
        }
    }

    protected final void a(com.baidu.trace.a.f fVar) {
        z.n = fVar.a;
        z.o = fVar.i;
        z.w = fVar.j;
        z.q = fVar.c;
        if (Trace.a == 5 && z.w > ar.c) {
            ar.b = z.w * 1000;
        }
        if (Trace.b == 30 && z.q * 1000 >= ar.b) {
            bd.i = z.q * 1000;
        }
        if (fVar.b >= 5) {
            z.p = fVar.b;
        }
        z.r = fVar.d;
        z.s = fVar.e;
        z.t = fVar.f;
        z.u = fVar.g;
        z.v = fVar.h;
        com.baidu.trace.c.e.d = com.baidu.trace.c.e.a();
        if (1 == z.n) {
            Handler handler = this.a;
            if (handler != null) {
                handler.obtainMessage(29).sendToTarget();
                return;
            }
            return;
        }
        Handler handler2 = this.a;
        if (handler2 != null) {
            Message obtainMessage = handler2.obtainMessage(30);
            obtainMessage.arg1 = 1241;
            obtainMessage.sendToTarget();
        }
    }

    protected final void a(com.baidu.trace.a.i iVar, long j) {
        com.baidu.trace.c.e.d = com.baidu.trace.c.e.a();
        byte[] a = com.baidu.trace.c.d.a(iVar.a);
        int i = 0;
        for (int i2 = 0; i2 < ay.c.length && i2 < a.length; i2++) {
            ay.c[i2] = a[i2];
            i++;
        }
        while (i < ay.c.length) {
            ay.c[i] = 0;
            i++;
        }
        ay.d = iVar.c;
        ay.e = iVar.d;
        ay.b = iVar.b;
        ay.a = (byte) 1;
        if (ay.d == 1) {
            r.a(ay.e);
            ay.e = null;
        }
        Handler handler = this.a;
        if (handler != null) {
            Message obtainMessage = handler.obtainMessage(IConstant.OP_ENTER_EDIT_MODE);
            Bundle bundle = new Bundle();
            bundle.putByteArray("ak", ay.c);
            bundle.putInt("pushId", ay.b);
            bundle.putLong("timeStamp", j);
            bundle.putByte("infoType", ay.d);
            bundle.putString("infoContent", ay.e);
            obtainMessage.setData(bundle);
            obtainMessage.sendToTarget();
        }
    }

    protected final boolean c() {
        if (this.a == null) {
            return false;
        }
        g.b();
        this.a.obtainMessage(31).sendToTarget();
        return true;
    }
}
