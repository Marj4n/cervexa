package com.tencent.bugly.crashreport.crash.anr;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.FileObserver;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.common.info.AppInfo;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.crashreport.crash.anr.TraceFileHelper;
import com.tencent.bugly.crashreport.crash.c;
import com.tencent.bugly.proguard.aa;
import com.tencent.bugly.proguard.ab;
import com.tencent.bugly.proguard.ac;
import com.tencent.bugly.proguard.p;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class b implements ac {
    private static b m;
    private final Context c;
    private final com.tencent.bugly.crashreport.common.info.a d;
    private final w e;
    private String f;
    private final com.tencent.bugly.crashreport.crash.b g;
    private FileObserver h;
    private ab j;
    private int k;
    private AtomicInteger a = new AtomicInteger(0);
    private long b = -1;
    private boolean i = true;
    private ActivityManager.ProcessErrorStateInfo l = new ActivityManager.ProcessErrorStateInfo();

    static /* synthetic */ boolean a(b bVar, String str) {
        return str.startsWith("bugly_trace_");
    }

    public static b a(Context context, com.tencent.bugly.crashreport.common.strategy.a aVar, com.tencent.bugly.crashreport.common.info.a aVar2, w wVar, p pVar, com.tencent.bugly.crashreport.crash.b bVar, BuglyStrategy.a aVar3) {
        if (m == null) {
            m = new b(context, aVar, aVar2, wVar, bVar);
        }
        return m;
    }

    private b(Context context, com.tencent.bugly.crashreport.common.strategy.a aVar, com.tencent.bugly.crashreport.common.info.a aVar2, w wVar, com.tencent.bugly.crashreport.crash.b bVar) {
        this.c = z.a(context);
        this.f = context.getDir("bugly", 0).getAbsolutePath();
        this.d = aVar2;
        this.e = wVar;
        this.g = bVar;
    }

    private ActivityManager.ProcessErrorStateInfo a(Context context, long j) {
        try {
            x.c("to find!", new Object[0]);
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            int i = 0;
            while (true) {
                x.c("waiting!", new Object[0]);
                List<ActivityManager.ProcessErrorStateInfo> processesInErrorState = activityManager.getProcessesInErrorState();
                if (processesInErrorState != null) {
                    for (ActivityManager.ProcessErrorStateInfo processErrorStateInfo : processesInErrorState) {
                        if (processErrorStateInfo.condition == 2) {
                            x.c("found!", new Object[0]);
                            return processErrorStateInfo;
                        }
                    }
                }
                z.b(500L);
                int i2 = i + 1;
                if (i >= 40) {
                    x.c("end!", new Object[0]);
                    return null;
                }
                i = i2;
            }
        } catch (Exception e) {
            x.b(e);
            return null;
        } catch (OutOfMemoryError e2) {
            this.l.pid = Process.myPid();
            this.l.shortMsg = "bugly sdk waitForAnrProcessStateChanged encount error:" + e2.getMessage();
            return this.l;
        }
    }

    private CrashDetailBean a(a aVar) {
        CrashDetailBean crashDetailBean = new CrashDetailBean();
        try {
            crashDetailBean.C = com.tencent.bugly.crashreport.common.info.b.g();
            crashDetailBean.D = com.tencent.bugly.crashreport.common.info.b.e();
            crashDetailBean.E = com.tencent.bugly.crashreport.common.info.b.i();
            crashDetailBean.F = this.d.k();
            crashDetailBean.G = this.d.j();
            crashDetailBean.H = this.d.l();
            if (!com.tencent.bugly.crashreport.common.info.b.m()) {
                crashDetailBean.w = z.a(this.c, c.e, (String) null);
            }
            crashDetailBean.b = 3;
            crashDetailBean.e = this.d.h();
            crashDetailBean.f = this.d.k;
            crashDetailBean.g = this.d.q();
            crashDetailBean.m = this.d.g();
            crashDetailBean.n = "ANR_EXCEPTION";
            crashDetailBean.o = aVar.f;
            crashDetailBean.q = aVar.g;
            crashDetailBean.P = new HashMap();
            crashDetailBean.P.put("BUGLY_CR_01", aVar.e);
            int indexOf = crashDetailBean.q != null ? crashDetailBean.q.indexOf("\n") : -1;
            crashDetailBean.p = indexOf > 0 ? crashDetailBean.q.substring(0, indexOf) : "GET_FAIL";
            crashDetailBean.r = aVar.c;
            if (crashDetailBean.q != null) {
                crashDetailBean.u = z.a(crashDetailBean.q.getBytes());
            }
            crashDetailBean.z = aVar.b;
            crashDetailBean.A = aVar.a;
            crashDetailBean.B = "main(1)";
            crashDetailBean.I = this.d.s();
            crashDetailBean.h = this.d.p();
            crashDetailBean.i = this.d.B();
            crashDetailBean.v = aVar.d;
            crashDetailBean.L = this.d.o;
            crashDetailBean.M = this.d.a;
            crashDetailBean.N = this.d.a();
            if (!com.tencent.bugly.crashreport.common.info.b.m()) {
                this.g.d(crashDetailBean);
            }
            crashDetailBean.Q = this.d.z();
            crashDetailBean.R = this.d.A();
            crashDetailBean.S = this.d.t();
            crashDetailBean.T = this.d.y();
            crashDetailBean.y = y.a();
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
        }
        return crashDetailBean;
    }

    private static boolean a(String str, String str2, String str3) {
        Throwable th;
        BufferedWriter bufferedWriter;
        TraceFileHelper.a readTargetDumpInfo = TraceFileHelper.readTargetDumpInfo(str3, str, true);
        if (readTargetDumpInfo == null || readTargetDumpInfo.d == null || readTargetDumpInfo.d.size() <= 0) {
            x.e("not found trace dump for %s", str3);
            return false;
        }
        File file = new File(str2);
        try {
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            if (!file.exists() || !file.canWrite()) {
                x.e("backup file create fail %s", str2);
                return false;
            }
            BufferedWriter bufferedWriter2 = null;
            try {
                try {
                    bufferedWriter = new BufferedWriter(new FileWriter(file, false));
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (IOException e) {
                e = e;
            }
            try {
                String[] strArr = readTargetDumpInfo.d.get("main");
                int i = 3;
                if (strArr != null && strArr.length >= 3) {
                    String str4 = strArr[0];
                    String str5 = strArr[1];
                    bufferedWriter.write("\"main\" tid=" + strArr[2] + " :\n" + str4 + "\n" + str5 + "\n\n");
                    bufferedWriter.flush();
                }
                for (Map.Entry<String, String[]> entry : readTargetDumpInfo.d.entrySet()) {
                    if (!entry.getKey().equals("main")) {
                        if (entry.getValue() != null && entry.getValue().length >= i) {
                            String str6 = entry.getValue()[0];
                            String str7 = entry.getValue()[1];
                            bufferedWriter.write("\"" + entry.getKey() + "\" tid=" + entry.getValue()[2] + " :\n" + str6 + "\n" + str7 + "\n\n");
                            bufferedWriter.flush();
                        }
                        i = 3;
                    }
                }
                try {
                    bufferedWriter.close();
                } catch (IOException e2) {
                    if (!x.a(e2)) {
                        e2.printStackTrace();
                    }
                }
                return true;
            } catch (IOException e3) {
                e = e3;
                bufferedWriter2 = bufferedWriter;
                if (!x.a(e)) {
                    e.printStackTrace();
                }
                x.e("dump trace fail %s", e.getClass().getName() + ":" + e.getMessage());
                if (bufferedWriter2 != null) {
                    try {
                        bufferedWriter2.close();
                    } catch (IOException e4) {
                        if (!x.a(e4)) {
                            e4.printStackTrace();
                        }
                    }
                }
                return false;
            } catch (Throwable th3) {
                th = th3;
                bufferedWriter2 = bufferedWriter;
                if (bufferedWriter2 != null) {
                    try {
                        bufferedWriter2.close();
                        throw th;
                    } catch (IOException e5) {
                        if (!x.a(e5)) {
                            e5.printStackTrace();
                            throw th;
                        }
                        throw th;
                    }
                }
                throw th;
            }
        } catch (Exception e6) {
            if (!x.a(e6)) {
                e6.printStackTrace();
            }
            x.e("backup file create error! %s  %s", e6.getClass().getName() + ":" + e6.getMessage(), str2);
            return false;
        }
    }

    public final boolean a() {
        return this.a.get() != 0;
    }

    private boolean a(Context context, String str, ActivityManager.ProcessErrorStateInfo processErrorStateInfo, long j, Map<String, String> map) {
        a aVar = new a();
        aVar.c = j;
        aVar.a = processErrorStateInfo != null ? processErrorStateInfo.processName : AppInfo.a(Process.myPid());
        aVar.f = processErrorStateInfo != null ? processErrorStateInfo.shortMsg : "";
        aVar.e = processErrorStateInfo != null ? processErrorStateInfo.longMsg : "";
        aVar.b = map;
        Thread thread = Looper.getMainLooper().getThread();
        if (map != null) {
            Iterator<String> it = map.keySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String next = it.next();
                if (next.startsWith(thread.getName())) {
                    aVar.g = map.get(next);
                    break;
                }
            }
        }
        if (TextUtils.isEmpty(aVar.g)) {
            aVar.g = "main stack is null , some error may be encountered.";
        }
        Object[] objArr = new Object[7];
        objArr[0] = Long.valueOf(aVar.c);
        objArr[1] = aVar.d;
        objArr[2] = aVar.a;
        objArr[3] = aVar.g;
        objArr[4] = aVar.f;
        objArr[5] = aVar.e;
        objArr[6] = Integer.valueOf(aVar.b == null ? 0 : aVar.b.size());
        x.c("anr tm:%d\ntr:%s\nproc:%s\nmain stack:%s\nsMsg:%s\n lMsg:%s\n threads:%d", objArr);
        x.a("found visiable anr , start to upload!", new Object[0]);
        CrashDetailBean a = a(aVar);
        if (a == null) {
            x.e("pack anr fail!", new Object[0]);
            return false;
        }
        c.a().a(a);
        if (a.a >= 0) {
            x.a("backup anr record success!", new Object[0]);
        } else {
            x.d("backup anr record fail!", new Object[0]);
        }
        if (str != null && new File(str).exists()) {
            aVar.d = new File(this.f, "bugly_trace_" + j + ".txt").getAbsolutePath();
            this.a.set(3);
            if (a(str, aVar.d, aVar.a)) {
                x.a("backup trace success", new Object[0]);
            }
        } else {
            File h = h();
            x.a("traceFile is %s", h);
            if (h != null) {
                a.v = h.getAbsolutePath();
            }
        }
        com.tencent.bugly.crashreport.crash.b.a("ANR", z.a(), aVar.a, "main", aVar.g, a);
        if (!this.g.a(a)) {
            this.g.a(a, 3000L, true);
        }
        this.g.c(a);
        return true;
    }

    public final void a(String str) {
        synchronized (this) {
            if (this.a.get() != 0) {
                x.c("trace started return ", new Object[0]);
                return;
            }
            this.a.set(1);
            try {
                x.c("read trace first dump for create time!", new Object[0]);
                TraceFileHelper.a readFirstDumpInfo = TraceFileHelper.readFirstDumpInfo(str, false);
                long j = readFirstDumpInfo != null ? readFirstDumpInfo.c : -1L;
                if (j == -1) {
                    x.d("trace dump fail could not get time!", new Object[0]);
                    j = System.currentTimeMillis();
                }
                long j2 = j;
                if (Math.abs(j2 - this.b) < 10000) {
                    x.d("should not process ANR too Fre in %d", 10000);
                } else {
                    this.b = j2;
                    this.a.set(1);
                    try {
                        Map<String, String> a = z.a(c.f, false);
                        if (a != null && a.size() > 0) {
                            ActivityManager.ProcessErrorStateInfo a2 = a(this.c, 20000L);
                            this.l = a2;
                            if (a2 == null) {
                                x.c("proc state is unvisiable!", new Object[0]);
                            } else if (a2.pid != Process.myPid()) {
                                x.c("not mind proc!", this.l.processName);
                            } else {
                                x.a("found visiable anr , start to process!", new Object[0]);
                                a(this.c, str, this.l, j2, a);
                            }
                        }
                        x.d("can't get all thread skip this anr", new Object[0]);
                    } catch (Throwable th) {
                        x.a(th);
                        x.e("get all thread stack fail!", new Object[0]);
                    }
                }
            } finally {
                try {
                } finally {
                }
            }
        }
    }

    private synchronized void d() {
        if (f()) {
            x.d("start when started!", new Object[0]);
            return;
        }
        FileObserver fileObserver = new FileObserver("/data/anr/", 8) { // from class: com.tencent.bugly.crashreport.crash.anr.b.1
            @Override // android.os.FileObserver
            public final void onEvent(int i, String str) {
                if (str == null) {
                    return;
                }
                final String str2 = "/data/anr/" + str;
                x.d("watching file %s", str2);
                if (str2.contains("trace")) {
                    b.this.e.a(new Runnable() { // from class: com.tencent.bugly.crashreport.crash.anr.b.1.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            b.this.a(str2);
                        }
                    });
                } else {
                    x.d("not anr file %s", str2);
                }
            }
        };
        this.h = fileObserver;
        try {
            fileObserver.startWatching();
            x.a("start anr monitor!", new Object[0]);
            this.e.a(new Runnable() { // from class: com.tencent.bugly.crashreport.crash.anr.b.2
                @Override // java.lang.Runnable
                public final void run() {
                    b.this.b();
                }
            });
        } catch (Throwable th) {
            this.h = null;
            x.d("start anr monitor failed!", new Object[0]);
            if (x.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    private synchronized void e() {
        if (!f()) {
            x.d("close when closed!", new Object[0]);
            return;
        }
        try {
            this.h.stopWatching();
            this.h = null;
            x.d("close anr monitor!", new Object[0]);
        } catch (Throwable th) {
            x.d("stop anr monitor failed!", new Object[0]);
            if (x.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    private synchronized boolean f() {
        return this.h != null;
    }

    private synchronized void b(boolean z) {
        if (Build.VERSION.SDK_INT <= 19) {
            if (z) {
                d();
                return;
            } else {
                e();
                return;
            }
        }
        if (z) {
            i();
        } else {
            j();
        }
    }

    private synchronized boolean g() {
        return this.i;
    }

    private synchronized void c(boolean z) {
        if (this.i != z) {
            x.a("user change anr %b", Boolean.valueOf(z));
            this.i = z;
        }
    }

    public final void a(boolean z) {
        c(z);
        boolean g = g();
        com.tencent.bugly.crashreport.common.strategy.a a = com.tencent.bugly.crashreport.common.strategy.a.a();
        if (a != null) {
            g = g && a.c().e;
        }
        if (g != f()) {
            x.a("anr changed to %b", Boolean.valueOf(g));
            b(g);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x005f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x008d A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected final void b() {
        /*
            r15 = this;
            java.lang.String r0 = "bugly_trace_"
            long r1 = com.tencent.bugly.proguard.z.b()
            long r3 = com.tencent.bugly.crashreport.crash.c.g
            long r1 = r1 - r3
            java.io.File r3 = new java.io.File
            java.lang.String r4 = r15.f
            r3.<init>(r4)
            boolean r4 = r3.exists()
            if (r4 == 0) goto La8
            boolean r4 = r3.isDirectory()
            if (r4 == 0) goto La8
            java.io.File[] r3 = r3.listFiles()     // Catch: java.lang.Throwable -> La4
            if (r3 == 0) goto La3
            int r4 = r3.length     // Catch: java.lang.Throwable -> La4
            if (r4 != 0) goto L27
            goto La3
        L27:
            java.lang.String r4 = ".txt"
            int r5 = r3.length     // Catch: java.lang.Throwable -> La4
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
        L2e:
            if (r7 >= r5) goto L90
            r10 = r3[r7]     // Catch: java.lang.Throwable -> La4
            java.lang.String r11 = r10.getName()     // Catch: java.lang.Throwable -> La4
            boolean r12 = r11.startsWith(r0)     // Catch: java.lang.Throwable -> La4
            r13 = 12
            r14 = 1
            if (r12 == 0) goto L42
        L3f:
            r9 = 12
            goto L4a
        L42:
            boolean r12 = r11.startsWith(r0)     // Catch: java.lang.Throwable -> La4
            if (r12 == 0) goto L49
            goto L3f
        L49:
            r14 = 0
        L4a:
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La4
            java.lang.String r13 = "Number Trace file : "
            r12.<init>(r13)     // Catch: java.lang.Throwable -> La4
            r12.append(r11)     // Catch: java.lang.Throwable -> La4
            java.lang.String r12 = r12.toString()     // Catch: java.lang.Throwable -> La4
            java.lang.Object[] r13 = new java.lang.Object[r6]     // Catch: java.lang.Throwable -> La4
            com.tencent.bugly.proguard.x.c(r12, r13)     // Catch: java.lang.Throwable -> La4
            if (r14 == 0) goto L8d
            int r12 = r11.indexOf(r4)     // Catch: java.lang.Throwable -> L72
            if (r12 <= 0) goto L85
            java.lang.String r12 = r11.substring(r9, r12)     // Catch: java.lang.Throwable -> L72
            long r11 = java.lang.Long.parseLong(r12)     // Catch: java.lang.Throwable -> L72
            int r13 = (r11 > r1 ? 1 : (r11 == r1 ? 0 : -1))
            if (r13 < 0) goto L85
            goto L8d
        L72:
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La4
            java.lang.String r13 = "Trace file that has invalid format: "
            r12.<init>(r13)     // Catch: java.lang.Throwable -> La4
            r12.append(r11)     // Catch: java.lang.Throwable -> La4
            java.lang.String r11 = r12.toString()     // Catch: java.lang.Throwable -> La4
            java.lang.Object[] r12 = new java.lang.Object[r6]     // Catch: java.lang.Throwable -> La4
            com.tencent.bugly.proguard.x.c(r11, r12)     // Catch: java.lang.Throwable -> La4
        L85:
            boolean r10 = r10.delete()     // Catch: java.lang.Throwable -> La4
            if (r10 == 0) goto L8d
            int r8 = r8 + 1
        L8d:
            int r7 = r7 + 1
            goto L2e
        L90:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La4
            java.lang.String r1 = "Number of overdue trace files that has deleted: "
            r0.<init>(r1)     // Catch: java.lang.Throwable -> La4
            r0.append(r8)     // Catch: java.lang.Throwable -> La4
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> La4
            java.lang.Object[] r1 = new java.lang.Object[r6]     // Catch: java.lang.Throwable -> La4
            com.tencent.bugly.proguard.x.c(r0, r1)     // Catch: java.lang.Throwable -> La4
        La3:
            return
        La4:
            r0 = move-exception
            com.tencent.bugly.proguard.x.a(r0)
        La8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.anr.b.b():void");
    }

    public final synchronized void c() {
        x.d("customer decides whether to open or close.", new Object[0]);
    }

    @Override // com.tencent.bugly.proguard.ac
    public final boolean a(aa aaVar) {
        Map<String, String> hashMap = new HashMap<>();
        if (aaVar.e().equals(Looper.getMainLooper())) {
            try {
                hashMap = z.a(200000, false);
            } catch (Throwable th) {
                x.b(th);
                hashMap.put("main", th.getMessage());
            }
            Map<String, String> map = hashMap;
            x.c("onThreadBlock found visiable anr , start to process!", new Object[0]);
            String c = com.tencent.bugly.crashreport.common.info.b.c(this.c);
            if (!TextUtils.isEmpty(c) && (c.contains("XiaoMi") || c.contains("samsung"))) {
                this.l = a(this.c, 20000L);
            }
            a(this.c, "", this.l, System.currentTimeMillis(), map);
        } else {
            x.c("anr handler onThreadBlock only care main thread ,current thread is: %s", aaVar.d());
        }
        return true;
    }

    private File h() {
        long currentTimeMillis = System.currentTimeMillis();
        File file = new File(this.f);
        if (file.exists() && file.isDirectory()) {
            try {
                File[] listFiles = file.listFiles();
                if (listFiles != null && listFiles.length != 0) {
                    int i = 24;
                    int length = listFiles.length;
                    int i2 = 0;
                    while (i2 < length) {
                        File file2 = listFiles[i2];
                        String name = file2.getName();
                        if (name.startsWith("jni_mannual_bugly_trace_")) {
                            try {
                                int indexOf = name.indexOf(".txt");
                                if (indexOf > 0) {
                                    long parseLong = Long.parseLong(name.substring(i, indexOf));
                                    long j = (currentTimeMillis - parseLong) / 1000;
                                    x.c("current time %d trace time is %d s", Long.valueOf(currentTimeMillis), Long.valueOf(parseLong));
                                    x.c("current time minus trace time is %d s", Long.valueOf(j));
                                    if (j < 30) {
                                        return file2;
                                    }
                                } else {
                                    continue;
                                }
                            } catch (Throwable unused) {
                                x.c("Trace file that has invalid format: " + name, new Object[0]);
                            }
                        }
                        i2++;
                        i = 24;
                    }
                }
                return null;
            } catch (Throwable th) {
                x.a(th);
                return null;
            }
        }
        return null;
    }

    private synchronized void i() {
        if (f()) {
            x.d("start when started!", new Object[0]);
            return;
        }
        if (TextUtils.isEmpty(this.f)) {
            return;
        }
        if (this.j == null || !this.j.isAlive()) {
            ab abVar = new ab();
            this.j = abVar;
            StringBuilder sb = new StringBuilder("Bugly-ThreadMonitor");
            int i = this.k;
            this.k = i + 1;
            sb.append(i);
            abVar.setName(sb.toString());
            this.j.a();
            this.j.a(this);
            this.j.d();
            this.e.a(new Runnable() { // from class: com.tencent.bugly.crashreport.crash.anr.b.3
                @Override // java.lang.Runnable
                public final void run() {
                    b.this.b();
                }
            });
        }
        FileObserver fileObserver = new FileObserver(this.f, 256) { // from class: com.tencent.bugly.crashreport.crash.anr.b.4
            @Override // android.os.FileObserver
            public final void onEvent(int i2, String str) {
                if (str == null) {
                    return;
                }
                x.d("startWatchingPrivateAnrDir %s", str);
                if (b.a(b.this, str)) {
                    if (b.this.j != null) {
                        b.this.j.a(true);
                        return;
                    }
                    return;
                }
                x.c("trace file not caused by sigquit , ignore ", new Object[0]);
            }
        };
        this.h = fileObserver;
        try {
            fileObserver.startWatching();
            x.a("startWatchingPrivateAnrDir! dumFilePath is %s", this.f);
            this.e.a(new Runnable() { // from class: com.tencent.bugly.crashreport.crash.anr.b.5
                @Override // java.lang.Runnable
                public final void run() {
                    b.this.b();
                }
            });
        } catch (Throwable th) {
            this.h = null;
            x.d("startWatchingPrivateAnrDir failed!", new Object[0]);
            if (x.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    private synchronized void j() {
        if (!f()) {
            x.d("close when closed!", new Object[0]);
            return;
        }
        if (this.j != null) {
            this.j.c();
            this.j.b();
            this.j.b(this);
            this.j = null;
        }
        x.a("stopWatchingPrivateAnrDir", new Object[0]);
        try {
            this.h.stopWatching();
            this.h = null;
            x.d("close anr monitor!", new Object[0]);
        } catch (Throwable th) {
            x.d("stop anr monitor failed!", new Object[0]);
            if (x.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }
}
