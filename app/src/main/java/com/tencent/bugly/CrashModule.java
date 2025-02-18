package com.tencent.bugly;

import android.content.Context;
import android.text.TextUtils;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.crash.c;
import com.tencent.bugly.proguard.x;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public class CrashModule extends a {
    public static final int MODULE_ID = 1004;
    private static int c;
    private static CrashModule e = new CrashModule();
    private long a;
    private BuglyStrategy.a b;
    private boolean d = false;

    public static CrashModule getInstance() {
        e.id = 1004;
        return e;
    }

    public synchronized boolean hasInitialized() {
        return this.d;
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0085 A[Catch: all -> 0x00af, TryCatch #0 {, blocks: (B:7:0x0003, B:10:0x0009, B:12:0x0039, B:14:0x0049, B:16:0x004f, B:17:0x0052, B:19:0x0057, B:22:0x005e, B:24:0x006e, B:27:0x0075, B:29:0x0085, B:30:0x008c, B:35:0x0080, B:36:0x0069), top: B:6:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x008a  */
    @Override // com.tencent.bugly.a
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void init(android.content.Context r12, boolean r13, com.tencent.bugly.BuglyStrategy r14) {
        /*
            r11 = this;
            monitor-enter(r11)
            if (r12 == 0) goto Lb2
            boolean r0 = r11.d     // Catch: java.lang.Throwable -> Laf
            if (r0 == 0) goto L9
            goto Lb2
        L9:
            java.lang.String r0 = "Initializing crash module."
            r1 = 0
            java.lang.Object[] r2 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> Laf
            com.tencent.bugly.proguard.x.a(r0, r2)     // Catch: java.lang.Throwable -> Laf
            com.tencent.bugly.proguard.n r0 = com.tencent.bugly.proguard.n.a()     // Catch: java.lang.Throwable -> Laf
            int r2 = com.tencent.bugly.CrashModule.c     // Catch: java.lang.Throwable -> Laf
            r3 = 1
            int r2 = r2 + r3
            com.tencent.bugly.CrashModule.c = r2     // Catch: java.lang.Throwable -> Laf
            r4 = 1004(0x3ec, float:1.407E-42)
            r0.a(r4, r2)     // Catch: java.lang.Throwable -> Laf
            r11.d = r3     // Catch: java.lang.Throwable -> Laf
            com.tencent.bugly.crashreport.CrashReport.setContext(r12)     // Catch: java.lang.Throwable -> Laf
            r11.a(r12, r14)     // Catch: java.lang.Throwable -> Laf
            r5 = 1004(0x3ec, float:1.407E-42)
            com.tencent.bugly.BuglyStrategy$a r8 = r11.b     // Catch: java.lang.Throwable -> Laf
            r9 = 0
            r10 = 0
            r6 = r12
            r7 = r13
            com.tencent.bugly.crashreport.crash.c r13 = com.tencent.bugly.crashreport.crash.c.a(r5, r6, r7, r8, r9, r10)     // Catch: java.lang.Throwable -> Laf
            r13.f()     // Catch: java.lang.Throwable -> Laf
            if (r14 == 0) goto L47
            int r0 = r14.getCallBackType()     // Catch: java.lang.Throwable -> Laf
            r13.a(r0)     // Catch: java.lang.Throwable -> Laf
            boolean r0 = r14.getCloseErrorCallback()     // Catch: java.lang.Throwable -> Laf
            r13.a(r0)     // Catch: java.lang.Throwable -> Laf
        L47:
            if (r14 == 0) goto L52
            boolean r0 = r14.isEnableCatchAnrTrace()     // Catch: java.lang.Throwable -> Laf
            if (r0 == 0) goto L52
            r13.k()     // Catch: java.lang.Throwable -> Laf
        L52:
            r13.o()     // Catch: java.lang.Throwable -> Laf
            if (r14 == 0) goto L69
            boolean r0 = r14.isEnableNativeCrashMonitor()     // Catch: java.lang.Throwable -> Laf
            if (r0 == 0) goto L5e
            goto L69
        L5e:
            java.lang.String r0 = "[crash] Closed native crash monitor!"
            java.lang.Object[] r2 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> Laf
            com.tencent.bugly.proguard.x.a(r0, r2)     // Catch: java.lang.Throwable -> Laf
            r13.g()     // Catch: java.lang.Throwable -> Laf
            goto L6c
        L69:
            r13.h()     // Catch: java.lang.Throwable -> Laf
        L6c:
            if (r14 == 0) goto L80
            boolean r0 = r14.isEnableANRCrashMonitor()     // Catch: java.lang.Throwable -> Laf
            if (r0 == 0) goto L75
            goto L80
        L75:
            java.lang.String r0 = "[crash] Closed ANR monitor!"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> Laf
            com.tencent.bugly.proguard.x.a(r0, r1)     // Catch: java.lang.Throwable -> Laf
            r13.j()     // Catch: java.lang.Throwable -> Laf
            goto L83
        L80:
            r13.i()     // Catch: java.lang.Throwable -> Laf
        L83:
            if (r14 == 0) goto L8a
            long r0 = r14.getAppReportDelay()     // Catch: java.lang.Throwable -> Laf
            goto L8c
        L8a:
            r0 = 0
        L8c:
            r13.a(r0)     // Catch: java.lang.Throwable -> Laf
            r13.n()     // Catch: java.lang.Throwable -> Laf
            com.tencent.bugly.crashreport.crash.d.a(r12)     // Catch: java.lang.Throwable -> Laf
            com.tencent.bugly.crashreport.crash.BuglyBroadcastReceiver r13 = com.tencent.bugly.crashreport.crash.BuglyBroadcastReceiver.getInstance()     // Catch: java.lang.Throwable -> Laf
            java.lang.String r14 = "android.net.conn.CONNECTIVITY_CHANGE"
            r13.addFilter(r14)     // Catch: java.lang.Throwable -> Laf
            r13.register(r12)     // Catch: java.lang.Throwable -> Laf
            com.tencent.bugly.proguard.n r12 = com.tencent.bugly.proguard.n.a()     // Catch: java.lang.Throwable -> Laf
            int r13 = com.tencent.bugly.CrashModule.c     // Catch: java.lang.Throwable -> Laf
            int r13 = r13 - r3
            com.tencent.bugly.CrashModule.c = r13     // Catch: java.lang.Throwable -> Laf
            r12.a(r4, r13)     // Catch: java.lang.Throwable -> Laf
            monitor-exit(r11)
            return
        Laf:
            r12 = move-exception
            monitor-exit(r11)
            throw r12
        Lb2:
            monitor-exit(r11)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.CrashModule.init(android.content.Context, boolean, com.tencent.bugly.BuglyStrategy):void");
    }

    private synchronized void a(Context context, BuglyStrategy buglyStrategy) {
        if (buglyStrategy == null) {
            return;
        }
        String libBuglySOFilePath = buglyStrategy.getLibBuglySOFilePath();
        if (!TextUtils.isEmpty(libBuglySOFilePath)) {
            com.tencent.bugly.crashreport.common.info.a.a(context).n = libBuglySOFilePath;
            x.a("setted libBugly.so file path :%s", libBuglySOFilePath);
        }
        if (buglyStrategy.getCrashHandleCallback() != null) {
            this.b = buglyStrategy.getCrashHandleCallback();
            x.a("setted CrashHanldeCallback", new Object[0]);
        }
        if (buglyStrategy.getAppReportDelay() > 0) {
            long appReportDelay = buglyStrategy.getAppReportDelay();
            this.a = appReportDelay;
            x.a("setted delay: %d", Long.valueOf(appReportDelay));
        }
    }

    @Override // com.tencent.bugly.a
    public void onServerStrategyChanged(StrategyBean strategyBean) {
        c a;
        if (strategyBean == null || (a = c.a()) == null) {
            return;
        }
        a.a(strategyBean);
    }

    @Override // com.tencent.bugly.a
    public String[] getTables() {
        return new String[]{"t_cr"};
    }
}
