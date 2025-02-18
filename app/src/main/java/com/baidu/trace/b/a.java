package com.baidu.trace.b;

import android.os.Handler;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public final class a {
    private static EnumC0016a a = EnumC0016a.a;
    private static b b = null;

    /* JADX WARN: $VALUES field not found */
    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* renamed from: com.baidu.trace.b.a$a, reason: collision with other inner class name */
    public static final class EnumC0016a {
        public static final EnumC0016a a = new EnumC0016a("TCP", 0);
        public static final EnumC0016a b = new EnumC0016a("UDP", 1);

        private EnumC0016a(String str, int i) {
        }
    }

    public static EnumC0016a a() {
        return a;
    }

    public static void a(EnumC0016a enumC0016a, e eVar) {
        int i;
        a = enumC0016a;
        if (EnumC0016a.a == enumC0016a) {
            b bVar = b;
            if (bVar != null) {
                bVar.a();
                if (b instanceof k) {
                    b = null;
                }
                b.a(eVar);
                return;
            }
            b = j.e();
            b.a(eVar);
            return;
        }
        if (EnumC0016a.b == enumC0016a) {
            b bVar2 = b;
            if (bVar2 != null) {
                bVar2.a();
                b bVar3 = b;
                if (bVar3 instanceof j) {
                    i = bVar3.c();
                    b = null;
                }
                b.a(eVar);
            }
            i = 8300;
            b = k.a(i);
            b.a(eVar);
        }
    }

    public static synchronized void a(byte[] bArr, Handler handler) {
        synchronized (a.class) {
            if (d()) {
                try {
                    b.a(bArr);
                } catch (Exception unused) {
                    if (handler != null) {
                        handler.obtainMessage(4).sendToTarget();
                    }
                }
            } else if (handler != null) {
                handler.obtainMessage(4).sendToTarget();
            }
        }
    }

    public static b b() {
        return b;
    }

    protected static synchronized DataInputStream c() throws IOException {
        synchronized (a.class) {
            if (!d()) {
                throw new IOException();
            }
            InputStream d = b.d();
            if (d == null) {
                return null;
            }
            return new DataInputStream(d);
        }
    }

    public static boolean d() {
        b bVar = b;
        if (bVar == null) {
            return false;
        }
        try {
            return bVar.b();
        } catch (Exception unused) {
            return false;
        }
    }

    public static void e() {
        b bVar = b;
        if (bVar != null) {
            bVar.a();
        }
    }
}
