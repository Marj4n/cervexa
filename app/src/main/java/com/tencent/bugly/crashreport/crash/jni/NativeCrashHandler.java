package com.tencent.bugly.crashreport.crash.jni;

import android.content.Context;
import android.os.Build;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.crashreport.crash.c;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.io.File;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public class NativeCrashHandler implements com.tencent.bugly.crashreport.a {
    private static NativeCrashHandler a = null;
    private static int b = 1;
    private static boolean m = false;
    private static boolean n = false;
    private static boolean p = true;
    private final Context c;
    private final com.tencent.bugly.crashreport.common.info.a d;
    private final w e;
    private NativeExceptionHandler f;
    private String g;
    private final boolean h;
    private boolean i = false;
    private boolean j = false;
    private boolean k = false;
    private boolean l = false;
    private com.tencent.bugly.crashreport.crash.b o;

    protected native boolean appendNativeLog(String str, String str2, String str3);

    protected native boolean appendWholeNativeLog(String str);

    protected native String getNativeKeyValueList();

    protected native String getNativeLog();

    protected native boolean putNativeKeyValue(String str, String str2);

    protected native String regist(String str, boolean z, int i);

    protected native String removeNativeKeyValue(String str);

    protected native void setNativeInfo(int i, String str);

    protected native void testCrash();

    protected native String unregist();

    static /* synthetic */ boolean a(NativeCrashHandler nativeCrashHandler, int i, String str) {
        return nativeCrashHandler.a(999, str);
    }

    private NativeCrashHandler(Context context, com.tencent.bugly.crashreport.common.info.a aVar, com.tencent.bugly.crashreport.crash.b bVar, w wVar, boolean z, String str) {
        this.c = z.a(context);
        try {
            if (z.a(str)) {
                str = context.getDir("bugly", 0).getAbsolutePath();
            }
        } catch (Throwable unused) {
            str = "/data/data/" + com.tencent.bugly.crashreport.common.info.a.a(context).c + "/app_bugly";
        }
        this.o = bVar;
        this.g = str;
        this.d = aVar;
        this.e = wVar;
        this.h = z;
        this.f = new a(context, aVar, bVar, com.tencent.bugly.crashreport.common.strategy.a.a());
    }

    public static synchronized NativeCrashHandler getInstance(Context context, com.tencent.bugly.crashreport.common.info.a aVar, com.tencent.bugly.crashreport.crash.b bVar, com.tencent.bugly.crashreport.common.strategy.a aVar2, w wVar, boolean z, String str) {
        NativeCrashHandler nativeCrashHandler;
        synchronized (NativeCrashHandler.class) {
            if (a == null) {
                a = new NativeCrashHandler(context, aVar, bVar, wVar, z, str);
            }
            nativeCrashHandler = a;
        }
        return nativeCrashHandler;
    }

    public static synchronized NativeCrashHandler getInstance() {
        NativeCrashHandler nativeCrashHandler;
        synchronized (NativeCrashHandler.class) {
            nativeCrashHandler = a;
        }
        return nativeCrashHandler;
    }

    public synchronized String getDumpFilePath() {
        return this.g;
    }

    public synchronized void setDumpFilePath(String str) {
        this.g = str;
    }

    public static void setShouldHandleInJava(boolean z) {
        p = z;
        NativeCrashHandler nativeCrashHandler = a;
        if (nativeCrashHandler != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(z);
            nativeCrashHandler.a(999, sb.toString());
        }
    }

    public static boolean isShouldHandleInJava() {
        return p;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(17:40|(1:42)(16:68|(1:70)|44|45|(1:47)|48|(1:50)|52|(1:54)(1:66)|55|(1:57)(1:65)|58|(1:60)|61|62|63)|43|44|45|(0)|48|(0)|52|(0)(0)|55|(0)(0)|58|(0)|61|62|63) */
    /* JADX WARN: Removed duplicated region for block: B:47:0x007e A[Catch: all -> 0x008c, TryCatch #3 {all -> 0x008c, blocks: (B:45:0x0074, B:47:0x007e, B:48:0x0080, B:50:0x008a), top: B:44:0x0074 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x008a A[Catch: all -> 0x008c, TRY_LEAVE, TryCatch #3 {all -> 0x008c, blocks: (B:45:0x0074, B:47:0x007e, B:48:0x0080, B:50:0x008a), top: B:44:0x0074 }] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0090 A[Catch: all -> 0x00f1, TryCatch #0 {all -> 0x00f1, blocks: (B:38:0x0015, B:40:0x001f, B:42:0x0051, B:43:0x005b, B:52:0x008c, B:54:0x0090, B:55:0x009f, B:57:0x00a3, B:58:0x00b2, B:60:0x00ca, B:61:0x00e0, B:65:0x00ab, B:66:0x0098, B:68:0x0063, B:70:0x0069), top: B:37:0x0015, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00a3 A[Catch: all -> 0x00f1, TryCatch #0 {all -> 0x00f1, blocks: (B:38:0x0015, B:40:0x001f, B:42:0x0051, B:43:0x005b, B:52:0x008c, B:54:0x0090, B:55:0x009f, B:57:0x00a3, B:58:0x00b2, B:60:0x00ca, B:61:0x00e0, B:65:0x00ab, B:66:0x0098, B:68:0x0063, B:70:0x0069), top: B:37:0x0015, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00ca A[Catch: all -> 0x00f1, TryCatch #0 {all -> 0x00f1, blocks: (B:38:0x0015, B:40:0x001f, B:42:0x0051, B:43:0x005b, B:52:0x008c, B:54:0x0090, B:55:0x009f, B:57:0x00a3, B:58:0x00b2, B:60:0x00ca, B:61:0x00e0, B:65:0x00ab, B:66:0x0098, B:68:0x0063, B:70:0x0069), top: B:37:0x0015, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00ab A[Catch: all -> 0x00f1, TryCatch #0 {all -> 0x00f1, blocks: (B:38:0x0015, B:40:0x001f, B:42:0x0051, B:43:0x005b, B:52:0x008c, B:54:0x0090, B:55:0x009f, B:57:0x00a3, B:58:0x00b2, B:60:0x00ca, B:61:0x00e0, B:65:0x00ab, B:66:0x0098, B:68:0x0063, B:70:0x0069), top: B:37:0x0015, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0098 A[Catch: all -> 0x00f1, TryCatch #0 {all -> 0x00f1, blocks: (B:38:0x0015, B:40:0x001f, B:42:0x0051, B:43:0x005b, B:52:0x008c, B:54:0x0090, B:55:0x009f, B:57:0x00a3, B:58:0x00b2, B:60:0x00ca, B:61:0x00e0, B:65:0x00ab, B:66:0x0098, B:68:0x0063, B:70:0x0069), top: B:37:0x0015, outer: #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized void a(boolean r11) {
        /*
            Method dump skipped, instructions count: 462
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler.a(boolean):void");
    }

    public synchronized void startNativeMonitor() {
        if (!this.j && !this.i) {
            String str = "Bugly";
            boolean z = !z.a(this.d.n);
            String str2 = this.d.n;
            if (z) {
                str = str2;
            } else {
                this.d.getClass();
            }
            boolean a2 = a(str, z);
            this.j = a2;
            if (a2 || this.i) {
                a(this.h);
                if (m) {
                    setNativeAppVersion(this.d.k);
                    setNativeAppChannel(this.d.m);
                    setNativeAppPackage(this.d.c);
                    setNativeUserId(this.d.g());
                    setNativeIsAppForeground(this.d.a());
                    setNativeLaunchTime(this.d.a);
                }
                return;
            }
            return;
        }
        a(this.h);
    }

    public void checkUploadRecordCrash() {
        this.e.a(new Runnable() { // from class: com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler.1
            @Override // java.lang.Runnable
            public final void run() {
                if (z.a(NativeCrashHandler.this.c, "native_record_lock", 10000L)) {
                    if (!NativeCrashHandler.p) {
                        NativeCrashHandler.a(NativeCrashHandler.this, 999, Bugly.SDK_IS_DEV);
                    }
                    CrashDetailBean a2 = b.a(NativeCrashHandler.this.c, NativeCrashHandler.this.g, NativeCrashHandler.this.f);
                    if (a2 != null) {
                        x.a("[Native] Get crash from native record.", new Object[0]);
                        if (!NativeCrashHandler.this.o.a(a2)) {
                            NativeCrashHandler.this.o.a(a2, 3000L, false);
                        }
                        b.a(false, NativeCrashHandler.this.g);
                    }
                    NativeCrashHandler.this.a();
                    z.b(NativeCrashHandler.this.c, "native_record_lock");
                    return;
                }
                x.a("[Native] Failed to lock file for handling native crash record.", new Object[0]);
            }
        });
    }

    private static boolean a(String str, boolean z) {
        boolean z2;
        try {
            x.a("[Native] Trying to load so: %s", str);
            if (z) {
                System.load(str);
            } else {
                System.loadLibrary(str);
            }
        } catch (Throwable th) {
            th = th;
            z2 = false;
        }
        try {
            x.a("[Native] Successfully loaded SO: %s", str);
            return true;
        } catch (Throwable th2) {
            th = th2;
            z2 = true;
            x.d(th.getMessage(), new Object[0]);
            x.d("[Native] Failed to load so: %s", str);
            return z2;
        }
    }

    private synchronized void c() {
        if (!this.k) {
            x.d("[Native] Native crash report has already unregistered.", new Object[0]);
            return;
        }
        try {
            if (unregist() != null) {
                x.a("[Native] Successfully closed native crash report.", new Object[0]);
                this.k = false;
                return;
            }
        } catch (Throwable unused) {
            x.c("[Native] Failed to close native crash report.", new Object[0]);
        }
        try {
            z.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "enableHandler", null, new Class[]{Boolean.TYPE}, new Object[]{false});
            this.k = false;
            x.a("[Native] Successfully closed native crash report.", new Object[0]);
        } catch (Throwable unused2) {
            x.c("[Native] Failed to close native crash report.", new Object[0]);
            this.j = false;
            this.i = false;
        }
    }

    public void testNativeCrash() {
        if (!this.j) {
            x.d("[Native] Bugly SO file has not been load.", new Object[0]);
        } else {
            testCrash();
        }
    }

    public void testNativeCrash(boolean z, boolean z2, boolean z3) {
        StringBuilder sb = new StringBuilder();
        sb.append(z);
        a(16, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(z2);
        a(17, sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(z3);
        a(18, sb3.toString());
        testNativeCrash();
    }

    public void dumpAnrNativeStack() {
        a(19, "1");
    }

    public NativeExceptionHandler getNativeExceptionHandler() {
        return this.f;
    }

    protected final void a() {
        long b2 = z.b() - c.g;
        long b3 = z.b() + 86400000;
        File file = new File(this.g);
        if (file.exists() && file.isDirectory()) {
            try {
                File[] listFiles = file.listFiles();
                if (listFiles != null && listFiles.length != 0) {
                    int i = 0;
                    int i2 = 0;
                    for (File file2 : listFiles) {
                        long lastModified = file2.lastModified();
                        if (lastModified < b2 || lastModified >= b3) {
                            x.a("[Native] Delete record file: %s", file2.getAbsolutePath());
                            i++;
                            if (file2.delete()) {
                                i2++;
                            }
                        }
                    }
                    x.c("[Native] Number of record files overdue: %d, has deleted: %d", Integer.valueOf(i), Integer.valueOf(i2));
                }
            } catch (Throwable th) {
                x.a(th);
            }
        }
    }

    public void removeEmptyNativeRecordFiles() {
        b.c(this.g);
    }

    private synchronized void b(boolean z) {
        if (z) {
            startNativeMonitor();
        } else {
            c();
        }
    }

    public synchronized boolean isUserOpened() {
        return this.l;
    }

    private synchronized void c(boolean z) {
        if (this.l != z) {
            x.a("user change native %b", Boolean.valueOf(z));
            this.l = z;
        }
    }

    public synchronized void setUserOpened(boolean z) {
        c(z);
        boolean isUserOpened = isUserOpened();
        com.tencent.bugly.crashreport.common.strategy.a a2 = com.tencent.bugly.crashreport.common.strategy.a.a();
        if (a2 != null) {
            isUserOpened = isUserOpened && a2.c().e;
        }
        if (isUserOpened != this.k) {
            x.a("native changed to %b", Boolean.valueOf(isUserOpened));
            b(isUserOpened);
        }
    }

    public synchronized void onStrategyChanged(StrategyBean strategyBean) {
        if (strategyBean != null) {
            if (strategyBean.e != this.k) {
                x.d("server native changed to %b", Boolean.valueOf(strategyBean.e));
            }
        }
        boolean z = com.tencent.bugly.crashreport.common.strategy.a.a().c().e && this.l;
        if (z != this.k) {
            x.a("native changed to %b", Boolean.valueOf(z));
            b(z);
        }
    }

    public boolean appendLogToNative(String str, String str2, String str3) {
        if ((this.i || this.j) && m && str != null && str2 != null && str3 != null) {
            try {
                if (this.j) {
                    return appendNativeLog(str, str2, str3);
                }
                Boolean bool = (Boolean) z.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "appendNativeLog", null, new Class[]{String.class, String.class, String.class}, new Object[]{str, str2, str3});
                if (bool != null) {
                    return bool.booleanValue();
                }
                return false;
            } catch (UnsatisfiedLinkError unused) {
                m = false;
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
                return false;
            }
        }
        return false;
    }

    public String getLogFromNative() {
        if ((!this.i && !this.j) || !m) {
            return null;
        }
        try {
            if (this.j) {
                return getNativeLog();
            }
            return (String) z.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "getNativeLog", null, null, null);
        } catch (UnsatisfiedLinkError unused) {
            m = false;
            return null;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public boolean putKeyValueToNative(String str, String str2) {
        if ((this.i || this.j) && m && str != null && str2 != null) {
            try {
                if (this.j) {
                    return putNativeKeyValue(str, str2);
                }
                Boolean bool = (Boolean) z.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "putNativeKeyValue", null, new Class[]{String.class, String.class}, new Object[]{str, str2});
                if (bool != null) {
                    return bool.booleanValue();
                }
                return false;
            } catch (UnsatisfiedLinkError unused) {
                m = false;
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
                return false;
            }
        }
        return false;
    }

    private boolean a(int i, String str) {
        if (this.j && n) {
            try {
                setNativeInfo(i, str);
                return true;
            } catch (UnsatisfiedLinkError unused) {
                n = false;
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
                return false;
            }
        }
        return false;
    }

    public boolean filterSigabrtSysLog() {
        return a(998, "true");
    }

    public boolean setNativeAppVersion(String str) {
        return a(10, str);
    }

    public boolean setNativeAppChannel(String str) {
        return a(12, str);
    }

    public boolean setNativeAppPackage(String str) {
        return a(13, str);
    }

    public boolean setNativeUserId(String str) {
        return a(11, str);
    }

    @Override // com.tencent.bugly.crashreport.a
    public boolean setNativeIsAppForeground(boolean z) {
        return a(14, z ? "true" : Bugly.SDK_IS_DEV);
    }

    public boolean setNativeLaunchTime(long j) {
        try {
            return a(15, String.valueOf(j));
        } catch (NumberFormatException e) {
            if (x.a(e)) {
                return false;
            }
            e.printStackTrace();
            return false;
        }
    }

    public void enableCatchAnrTrace() {
        if (Build.VERSION.SDK_INT > 30 || Build.VERSION.SDK_INT < 23) {
            return;
        }
        b |= 2;
    }

    public boolean isEnableCatchAnrTrace() {
        return (b & 2) == 2;
    }
}
