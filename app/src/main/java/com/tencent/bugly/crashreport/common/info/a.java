package com.tencent.bugly.crashreport.common.info;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Process;
import android.text.TextUtils;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class a {
    private static a Z;
    public boolean B;
    public SharedPreferences F;
    private final Context G;
    private String H;
    private String I;
    private String U;
    public String c;
    public final String d;
    public final String g;
    public final String h;
    public final String i;
    public long j;
    public String k;
    public String l;
    public String m;
    public List<String> p;
    public boolean v;
    public String w;
    public String x;
    public String y;
    public String z;
    public boolean e = true;
    public String f = "3.3.92";
    private String J = "unknown";
    private String K = "";
    private String L = null;
    private long M = -1;
    private long N = -1;
    private long O = -1;
    private String P = null;
    private String Q = null;
    private Map<String, PlugInBean> R = null;
    private boolean S = true;
    private String T = null;
    private Boolean V = null;
    private String W = null;
    public String n = null;
    public String o = null;
    private Map<String, PlugInBean> X = null;
    private Map<String, PlugInBean> Y = null;
    private int aa = -1;
    private int ab = -1;
    private Map<String, String> ac = new HashMap();
    private Map<String, String> ad = new HashMap();
    private Map<String, String> ae = new HashMap();
    private boolean af = true;
    public String q = "unknown";
    public long r = 0;
    public long s = 0;
    public long t = 0;
    public long u = 0;
    public boolean A = false;
    private Boolean ag = null;
    private Boolean ah = null;
    public HashMap<String, String> C = new HashMap<>();
    public List<String> D = new ArrayList();
    public com.tencent.bugly.crashreport.a E = null;
    private final Object ai = new Object();
    private final Object aj = new Object();
    private final Object ak = new Object();
    private final Object al = new Object();
    private final Object am = new Object();
    private final Object an = new Object();
    private final Object ao = new Object();
    public final long a = System.currentTimeMillis();
    public final byte b = 1;

    private a(Context context) {
        this.k = null;
        this.l = null;
        this.U = null;
        this.m = null;
        this.p = null;
        this.v = false;
        this.w = null;
        this.x = null;
        this.y = null;
        this.z = "";
        this.B = false;
        this.G = z.a(context);
        PackageInfo b = AppInfo.b(context);
        if (b != null) {
            try {
                String str = b.versionName;
                this.k = str;
                this.w = str;
                this.x = Integer.toString(b.versionCode);
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
            }
        }
        this.c = AppInfo.a(context);
        this.d = AppInfo.a(Process.myPid());
        this.g = b.k();
        this.h = b.a();
        this.l = AppInfo.c(context);
        this.i = "Android " + b.b() + ",level " + b.c();
        Map<String, String> d = AppInfo.d(context);
        if (d != null) {
            try {
                this.p = AppInfo.a(d);
                String str2 = d.get("BUGLY_APPID");
                if (str2 != null) {
                    this.U = str2;
                    c("APP_ID", str2);
                }
                String str3 = d.get("BUGLY_APP_VERSION");
                if (str3 != null) {
                    this.k = str3;
                }
                String str4 = d.get("BUGLY_APP_CHANNEL");
                if (str4 != null) {
                    this.m = str4;
                }
                String str5 = d.get("BUGLY_ENABLE_DEBUG");
                if (str5 != null) {
                    this.v = str5.equalsIgnoreCase("true");
                }
                String str6 = d.get("com.tencent.rdm.uuid");
                if (str6 != null) {
                    this.y = str6;
                }
                String str7 = d.get("BUGLY_APP_BUILD_NO");
                if (!TextUtils.isEmpty(str7)) {
                    Integer.parseInt(str7);
                }
                String str8 = d.get("BUGLY_AREA");
                if (str8 != null) {
                    this.z = str8;
                }
            } catch (Throwable th2) {
                if (!x.a(th2)) {
                    th2.printStackTrace();
                }
            }
        }
        try {
            if (!context.getDatabasePath("bugly_db_").exists()) {
                this.B = true;
                x.c("App is first time to be installed on the device.", new Object[0]);
            }
        } catch (Throwable th3) {
            if (com.tencent.bugly.b.c) {
                th3.printStackTrace();
            }
        }
        this.F = z.a("BUGLY_COMMON_VALUES", context);
        x.c("com info create end", new Object[0]);
    }

    public final boolean a() {
        return this.af;
    }

    public final void a(boolean z) {
        this.af = z;
        com.tencent.bugly.crashreport.a aVar = this.E;
        if (aVar != null) {
            aVar.setNativeIsAppForeground(z);
        }
    }

    public static synchronized a a(Context context) {
        a aVar;
        synchronized (a.class) {
            if (Z == null) {
                Z = new a(context);
            }
            aVar = Z;
        }
        return aVar;
    }

    public static synchronized a b() {
        a aVar;
        synchronized (a.class) {
            aVar = Z;
        }
        return aVar;
    }

    public final String c() {
        return this.f;
    }

    public final void d() {
        synchronized (this.ai) {
            this.H = UUID.randomUUID().toString();
        }
    }

    public final String e() {
        String str;
        synchronized (this.ai) {
            if (this.H == null) {
                synchronized (this.ai) {
                    this.H = UUID.randomUUID().toString();
                }
            }
            str = this.H;
        }
        return str;
    }

    public final String f() {
        if (z.a((String) null)) {
            return this.U;
        }
        return null;
    }

    public final void a(String str) {
        this.U = str;
        c("APP_ID", str);
    }

    public final String g() {
        String str;
        synchronized (this.an) {
            str = this.J;
        }
        return str;
    }

    public final void b(String str) {
        synchronized (this.an) {
            if (str == null) {
                str = "10000";
            }
            this.J = str;
        }
    }

    public final void b(boolean z) {
        this.S = z;
    }

    public final String h() {
        String str = this.I;
        if (str != null) {
            return str;
        }
        String str2 = "";
        if (this.S) {
            if (this.L == null) {
                this.L = z.c("androidid", "");
                String a = b.a(this.G);
                this.L = a;
                if (!TextUtils.isEmpty(a)) {
                    z.b("androidid", this.L);
                }
            }
            str2 = this.L;
        }
        this.I = str2;
        return str2;
    }

    public final void c(String str) {
        this.I = str;
        synchronized (this.ao) {
            this.ad.put("E8", str);
        }
    }

    public final synchronized void d(String str) {
    }

    public final synchronized String i() {
        return this.K;
    }

    public final synchronized void e(String str) {
        this.K = str;
    }

    public final long j() {
        if (this.M <= 0) {
            this.M = b.d();
        }
        return this.M;
    }

    public final long k() {
        if (this.N <= 0) {
            this.N = b.f();
        }
        return this.N;
    }

    public final long l() {
        if (this.O <= 0) {
            this.O = b.h();
        }
        return this.O;
    }

    public final String m() {
        if (this.P == null) {
            this.P = b.a(this.G, true);
        }
        return this.P;
    }

    public final String n() {
        if (this.Q == null) {
            this.Q = b.d(this.G);
        }
        return this.Q;
    }

    public final void a(String str, String str2) {
        if (str == null || str2 == null) {
            return;
        }
        synchronized (this.aj) {
            this.C.put(str, str2);
        }
    }

    public final String o() {
        try {
            Map<String, ?> all = this.G.getSharedPreferences("BuglySdkInfos", 0).getAll();
            if (!all.isEmpty()) {
                synchronized (this.aj) {
                    for (Map.Entry<String, ?> entry : all.entrySet()) {
                        try {
                            this.C.put(entry.getKey(), entry.getValue().toString());
                        } catch (Throwable th) {
                            x.a(th);
                        }
                    }
                }
            }
        } catch (Throwable th2) {
            x.a(th2);
        }
        if (!this.C.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry2 : this.C.entrySet()) {
                sb.append("[");
                sb.append(entry2.getKey());
                sb.append(",");
                sb.append(entry2.getValue());
                sb.append("] ");
            }
            x.c("SDK_INFO = %s", sb.toString());
            c("SDK_INFO", sb.toString());
            return sb.toString();
        }
        x.c("SDK_INFO is empty", new Object[0]);
        return null;
    }

    public final synchronized Map<String, PlugInBean> p() {
        return null;
    }

    public final String q() {
        if (this.T == null) {
            this.T = b.j();
        }
        return this.T;
    }

    public final Boolean r() {
        if (this.V == null) {
            this.V = Boolean.valueOf(b.l());
        }
        return this.V;
    }

    public final String s() {
        if (this.W == null) {
            String str = b.c(this.G);
            this.W = str;
            x.a("ROM ID: %s", str);
        }
        return this.W;
    }

    public final Map<String, String> t() {
        synchronized (this.ak) {
            if (this.ac.size() <= 0) {
                return null;
            }
            return new HashMap(this.ac);
        }
    }

    public final String f(String str) {
        String remove;
        if (z.a(str)) {
            x.d("key should not be empty %s", str);
            return null;
        }
        synchronized (this.ak) {
            remove = this.ac.remove(str);
        }
        return remove;
    }

    public final void u() {
        synchronized (this.ak) {
            this.ac.clear();
        }
    }

    public final String g(String str) {
        String str2;
        if (z.a(str)) {
            x.d("key should not be empty %s", str);
            return null;
        }
        synchronized (this.ak) {
            str2 = this.ac.get(str);
        }
        return str2;
    }

    public final void b(String str, String str2) {
        if (z.a(str) || z.a(str2)) {
            x.d("key&value should not be empty %s %s", str, str2);
            return;
        }
        synchronized (this.ak) {
            this.ac.put(str, str2);
        }
    }

    public final int v() {
        int size;
        synchronized (this.ak) {
            size = this.ac.size();
        }
        return size;
    }

    public final Set<String> w() {
        Set<String> keySet;
        synchronized (this.ak) {
            keySet = this.ac.keySet();
        }
        return keySet;
    }

    public final Map<String, String> x() {
        synchronized (this.ao) {
            if (this.ad.size() <= 0) {
                return null;
            }
            return new HashMap(this.ad);
        }
    }

    public final void c(String str, String str2) {
        if (z.a(str) || z.a(str2)) {
            x.d("server key&value should not be empty %s %s", str, str2);
            return;
        }
        synchronized (this.al) {
            this.ae.put(str, str2);
        }
    }

    public final Map<String, String> y() {
        synchronized (this.al) {
            if (this.ae.size() <= 0) {
                return null;
            }
            return new HashMap(this.ae);
        }
    }

    public final void a(int i) {
        synchronized (this.am) {
            int i2 = this.aa;
            if (i2 != i) {
                this.aa = i;
                x.a("user scene tag %d changed to tag %d", Integer.valueOf(i2), Integer.valueOf(this.aa));
            }
        }
    }

    public final int z() {
        int i;
        synchronized (this.am) {
            i = this.aa;
        }
        return i;
    }

    public final void b(int i) {
        int i2 = this.ab;
        if (i2 != 24096) {
            this.ab = 24096;
            x.a("server scene tag %d changed to tag %d", Integer.valueOf(i2), Integer.valueOf(this.ab));
        }
    }

    public final int A() {
        return this.ab;
    }

    public final synchronized Map<String, PlugInBean> B() {
        return null;
    }

    public static int C() {
        return b.c();
    }

    public final boolean D() {
        if (this.ag == null) {
            this.ag = Boolean.valueOf(b.e(this.G));
            x.a("Is it a virtual machine? " + this.ag, new Object[0]);
        }
        return this.ag.booleanValue();
    }

    public final boolean E() {
        if (this.ah == null) {
            this.ah = Boolean.valueOf(b.f(this.G));
            x.a("Does it has hook frame? " + this.ah, new Object[0]);
        }
        return this.ah.booleanValue();
    }
}
