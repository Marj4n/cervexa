package com.baidu.trace.b;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import com.baidu.trace.am;
import com.baidu.trace.c.f;

/* loaded from: classes.dex */
public final class d {
    private static int k = Integer.MIN_VALUE;
    private static boolean l = false;
    private static long m;
    private boolean a = false;
    private boolean b = false;
    private boolean c = false;
    private Handler d = null;
    private Context e = null;
    private f f = null;
    private h g = null;
    private g h = null;
    private am i = null;
    private i j = null;

    static class a {
        private static d a = new d();
    }

    public static d a() {
        return a.a;
    }

    public static void a(long j) {
        m = j;
    }

    public static void a(boolean z) {
        l = z;
    }

    protected static boolean a(int i) {
        return i != k;
    }

    public static boolean a(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                com.baidu.trace.a.a("BaiduTraceSDK", "current network is not available");
                return false;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable();
        } catch (Exception unused) {
            com.baidu.trace.a.a("BaiduTraceSDK", "current network is not available");
            return false;
        }
    }

    public static void b(int i) {
        k = i;
    }

    public static boolean h() {
        return l;
    }

    public static long i() {
        return m;
    }

    public final void a(Handler handler, Context context) {
        this.d = handler;
        this.e = context;
    }

    public final void a(String str) {
        if (this.c) {
            return;
        }
        this.c = true;
        if (this.i != null) {
            this.i = null;
        }
        am amVar = new am(0, str);
        this.i = amVar;
        amVar.start();
    }

    public final void a(byte[] bArr, f.a aVar) {
        if (this.j != null) {
            this.j = null;
        }
        i iVar = new i(this.e, this.d, bArr, aVar);
        this.j = iVar;
        iVar.start();
    }

    public final void b() {
        this.j = null;
        if (this.a) {
            this.a = false;
            f fVar = this.f;
            if (fVar != null) {
                fVar.a();
                this.f = null;
            }
        }
        if (this.b) {
            this.b = false;
            g gVar = this.h;
            if (gVar != null) {
                gVar.a();
            }
        }
        com.baidu.trace.b.a.e();
    }

    public final void c() {
        if (this.a) {
            return;
        }
        this.a = true;
        if (this.f != null) {
            this.f = null;
        }
        f fVar = new f(this.e, this.d);
        this.f = fVar;
        fVar.start();
    }

    protected final void d() {
        if (this.g != null) {
            this.g = null;
        }
        h hVar = new h(this.d);
        this.g = hVar;
        hVar.start();
    }

    public final void e() {
        h hVar = this.g;
        if (hVar != null) {
            hVar.a();
            this.g = null;
        }
    }

    public final void f() {
        this.c = false;
    }

    public final boolean g() {
        if (this.b) {
            return true;
        }
        if (com.baidu.trace.b.a.b() == null) {
            Handler handler = this.d;
            if (handler == null) {
                return false;
            }
            handler.obtainMessage(4).sendToTarget();
            return false;
        }
        this.b = true;
        g gVar = new g(this.e, this.d);
        this.h = gVar;
        gVar.start();
        return true;
    }
}
