package com.tencent.open.a;

import android.os.Environment;
import android.text.TextUtils;
import com.tencent.connect.common.Constants;
import com.tencent.open.a.d;
import java.io.File;

/* compiled from: ProGuard */
/* loaded from: classes2.dex */
public class f {
    public static f a = null;
    protected static final b c = new b(c(), c.m, c.g, c.h, c.c, c.i, 10, c.e, c.n);
    private static boolean d = false;
    protected a b = new a(c);

    public static f a() {
        if (a == null) {
            synchronized (f.class) {
                if (a == null) {
                    a = new f();
                    d = true;
                }
            }
        }
        return a;
    }

    private f() {
    }

    protected void a(int i, String str, String str2, Throwable th) {
        a aVar;
        if (d) {
            String b = com.tencent.open.utils.d.b();
            if (!TextUtils.isEmpty(b)) {
                String str3 = b + " SDK_VERSION:" + Constants.SDK_VERSION;
                if (this.b == null) {
                    return;
                }
                e.a.b(32, Thread.currentThread(), System.currentTimeMillis(), "openSDK_LOG", str3, null);
                this.b.b(32, Thread.currentThread(), System.currentTimeMillis(), "openSDK_LOG", str3, null);
                d = false;
            }
        }
        e.a.b(i, Thread.currentThread(), System.currentTimeMillis(), str, str2, th);
        if (!d.a.a(c.b, i) || (aVar = this.b) == null) {
            return;
        }
        aVar.b(i, Thread.currentThread(), System.currentTimeMillis(), str, str2, th);
    }

    public static final void a(String str, String str2) {
        a().a(1, str, str2, null);
    }

    public static final void b(String str, String str2) {
        a().a(2, str, str2, null);
    }

    public static final void a(String str, String str2, Throwable th) {
        a().a(2, str, str2, th);
    }

    public static final void c(String str, String str2) {
        a().a(4, str, str2, null);
    }

    public static final void d(String str, String str2) {
        a().a(8, str, str2, null);
    }

    public static final void e(String str, String str2) {
        a().a(16, str, str2, null);
    }

    public static final void b(String str, String str2, Throwable th) {
        a().a(16, str, str2, th);
    }

    public static void b() {
        synchronized (f.class) {
            a().d();
            if (a != null) {
                a = null;
            }
        }
    }

    protected static File c() {
        String str = c.d;
        d.c b = d.b.b();
        if (b != null && b.c() > c.f) {
            return new File(Environment.getExternalStorageDirectory(), str);
        }
        return new File(com.tencent.open.utils.d.c(), str);
    }

    protected void d() {
        a aVar = this.b;
        if (aVar != null) {
            aVar.a();
            this.b.b();
            this.b = null;
        }
    }
}
