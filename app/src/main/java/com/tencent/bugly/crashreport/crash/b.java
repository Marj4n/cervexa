package com.tencent.bugly.crashreport.crash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import com.jieli.stream.dv.running2.util.IConstant;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.common.info.PlugInBean;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler;
import com.tencent.bugly.proguard.ah;
import com.tencent.bugly.proguard.aj;
import com.tencent.bugly.proguard.ak;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.p;
import com.tencent.bugly.proguard.r;
import com.tencent.bugly.proguard.u;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class b {
    private static int a;
    private Context b;
    private u c;
    private p d;
    private com.tencent.bugly.crashreport.common.strategy.a e;
    private o f;
    private BuglyStrategy.a g;

    public b(int i, Context context, u uVar, p pVar, com.tencent.bugly.crashreport.common.strategy.a aVar, BuglyStrategy.a aVar2, o oVar) {
        a = i;
        this.b = context;
        this.c = uVar;
        this.d = pVar;
        this.e = aVar;
        this.g = aVar2;
        this.f = oVar;
    }

    private static List<a> a(List<a> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        long currentTimeMillis = System.currentTimeMillis();
        ArrayList arrayList = new ArrayList();
        for (a aVar : list) {
            if (aVar.d && aVar.b <= currentTimeMillis - 86400000) {
                arrayList.add(aVar);
            }
        }
        return arrayList;
    }

    private CrashDetailBean a(List<a> list, CrashDetailBean crashDetailBean) {
        List<CrashDetailBean> b;
        String[] split;
        if (list == null || list.size() == 0) {
            return crashDetailBean;
        }
        CrashDetailBean crashDetailBean2 = null;
        ArrayList arrayList = new ArrayList(10);
        for (a aVar : list) {
            if (aVar.e) {
                arrayList.add(aVar);
            }
        }
        if (arrayList.size() > 0 && (b = b(arrayList)) != null && b.size() > 0) {
            Collections.sort(b);
            for (int i = 0; i < b.size(); i++) {
                CrashDetailBean crashDetailBean3 = b.get(i);
                if (i == 0) {
                    crashDetailBean2 = crashDetailBean3;
                } else if (crashDetailBean3.s != null && (split = crashDetailBean3.s.split("\n")) != null) {
                    for (String str : split) {
                        if (!crashDetailBean2.s.contains(str)) {
                            crashDetailBean2.t++;
                            crashDetailBean2.s += str + "\n";
                        }
                    }
                }
            }
        }
        if (crashDetailBean2 == null) {
            crashDetailBean.j = true;
            crashDetailBean.t = 0;
            crashDetailBean.s = "";
            crashDetailBean2 = crashDetailBean;
        }
        for (a aVar2 : list) {
            if (!aVar2.e && !aVar2.d) {
                String str2 = crashDetailBean2.s;
                StringBuilder sb = new StringBuilder();
                sb.append(aVar2.b);
                if (!str2.contains(sb.toString())) {
                    crashDetailBean2.t++;
                    crashDetailBean2.s += aVar2.b + "\n";
                }
            }
        }
        if (crashDetailBean2.r != crashDetailBean.r) {
            String str3 = crashDetailBean2.s;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(crashDetailBean.r);
            if (!str3.contains(sb2.toString())) {
                crashDetailBean2.t++;
                crashDetailBean2.s += crashDetailBean.r + "\n";
            }
        }
        return crashDetailBean2;
    }

    public final boolean a(CrashDetailBean crashDetailBean) {
        return b(crashDetailBean);
    }

    public final boolean b(CrashDetailBean crashDetailBean) {
        if (crashDetailBean == null) {
            return true;
        }
        if (c.n != null && !c.n.isEmpty()) {
            x.c("Crash filter for crash stack is: %s", c.n);
            if (crashDetailBean.q.contains(c.n)) {
                x.d("This crash contains the filter string set. It will not be record and upload.", new Object[0]);
                return true;
            }
        }
        if (c.o != null && !c.o.isEmpty()) {
            x.c("Crash regular filter for crash stack is: %s", c.o);
            if (Pattern.compile(c.o).matcher(crashDetailBean.q).find()) {
                x.d("This crash matches the regular filter string set. It will not be record and upload.", new Object[0]);
                return true;
            }
        }
        if (crashDetailBean.b != 2) {
            r rVar = new r();
            rVar.b = 1;
            rVar.c = crashDetailBean.A;
            rVar.d = crashDetailBean.B;
            rVar.e = crashDetailBean.r;
            this.d.b(1);
            this.d.a(rVar);
            x.b("[crash] a crash occur, handling...", new Object[0]);
        } else {
            x.b("[crash] a caught exception occur, handling...", new Object[0]);
        }
        List<a> b = b();
        ArrayList arrayList = null;
        if (b != null && b.size() > 0) {
            arrayList = new ArrayList(10);
            ArrayList arrayList2 = new ArrayList(10);
            arrayList.addAll(a(b));
            b.removeAll(arrayList);
            if (b.size() > 20) {
                StringBuilder sb = new StringBuilder();
                sb.append("_id in ");
                sb.append("(");
                sb.append("SELECT _id");
                sb.append(" FROM t_cr");
                sb.append(" order by _id");
                sb.append(" limit 5");
                sb.append(")");
                String sb2 = sb.toString();
                sb.setLength(0);
                try {
                    x.c("deleted first record %s data %d", "t_cr", Integer.valueOf(p.a().a("t_cr", sb2, (String[]) null, (o) null, true)));
                } catch (Throwable th) {
                    if (!x.a(th)) {
                        th.printStackTrace();
                    }
                }
            }
            if (!com.tencent.bugly.b.c && c.d) {
                boolean z = false;
                for (a aVar : b) {
                    if (crashDetailBean.u.equals(aVar.c)) {
                        if (aVar.e) {
                            z = true;
                        }
                        arrayList2.add(aVar);
                    }
                }
                if (z || arrayList2.size() >= c.c) {
                    x.a("same crash occur too much do merged!", new Object[0]);
                    CrashDetailBean a2 = a(arrayList2, crashDetailBean);
                    for (a aVar2 : arrayList2) {
                        if (aVar2.a != a2.a) {
                            arrayList.add(aVar2);
                        }
                    }
                    e(a2);
                    c(arrayList);
                    x.b("[crash] save crash success. For this device crash many times, it will not upload crashes immediately", new Object[0]);
                    return true;
                }
            }
        }
        e(crashDetailBean);
        if (arrayList != null && !arrayList.isEmpty()) {
            c(arrayList);
        }
        x.b("[crash] save crash success", new Object[0]);
        return false;
    }

    public final List<CrashDetailBean> a() {
        StrategyBean c = com.tencent.bugly.crashreport.common.strategy.a.a().c();
        if (c == null) {
            x.d("have not synced remote!", new Object[0]);
            return null;
        }
        if (!c.e) {
            x.d("Crashreport remote closed, please check your APP ID correct and Version available, then uninstall and reinstall your app.", new Object[0]);
            x.b("[init] WARNING! Crashreport closed by server, please check your APP ID correct and Version available, then uninstall and reinstall your app.", new Object[0]);
            return null;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long b = z.b();
        List<a> b2 = b();
        x.c("Size of crash list loaded from DB: %s", Integer.valueOf(b2.size()));
        if (b2 == null || b2.size() <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(a(b2));
        b2.removeAll(arrayList);
        Iterator<a> it = b2.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (next.b < b - c.g) {
                it.remove();
                arrayList.add(next);
            } else if (next.d) {
                if (next.b >= currentTimeMillis - 86400000) {
                    it.remove();
                } else if (!next.e) {
                    it.remove();
                    arrayList.add(next);
                }
            } else if (next.f >= 3 && next.b < currentTimeMillis - 86400000) {
                it.remove();
                arrayList.add(next);
            }
        }
        if (arrayList.size() > 0) {
            c(arrayList);
        }
        ArrayList arrayList2 = new ArrayList();
        List<CrashDetailBean> b3 = b(b2);
        if (b3 != null && b3.size() > 0) {
            String str = com.tencent.bugly.crashreport.common.info.a.b().k;
            Iterator<CrashDetailBean> it2 = b3.iterator();
            while (it2.hasNext()) {
                CrashDetailBean next2 = it2.next();
                if (!str.equals(next2.f)) {
                    it2.remove();
                    arrayList2.add(next2);
                }
            }
        }
        if (arrayList2.size() > 0) {
            d(arrayList2);
        }
        return b3;
    }

    public final void c(CrashDetailBean crashDetailBean) {
        int i = crashDetailBean.b;
        if (i != 0) {
            if (i != 1) {
                if (i == 3 && !c.a().r()) {
                    return;
                }
            } else if (!c.a().q()) {
                return;
            }
        } else if (!c.a().q()) {
            return;
        }
        if (this.f != null) {
            x.c("Calling 'onCrashHandleEnd' of RQD crash listener.", new Object[0]);
            int i2 = crashDetailBean.b;
        }
    }

    public final void a(CrashDetailBean crashDetailBean, long j, boolean z) {
        if (c.l) {
            x.a("try to upload right now", new Object[0]);
            ArrayList arrayList = new ArrayList();
            arrayList.add(crashDetailBean);
            a(arrayList, 3000L, z, crashDetailBean.b == 7, z);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0088 A[Catch: all -> 0x00cc, TryCatch #0 {all -> 0x00cc, blocks: (B:20:0x0041, B:23:0x004f, B:27:0x0058, B:28:0x0068, B:30:0x006e, B:33:0x0088, B:35:0x0090, B:37:0x0096, B:39:0x009e, B:41:0x00a8, B:43:0x00b0, B:45:0x00b7, B:47:0x00c3, B:49:0x007e), top: B:19:0x0041 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0090 A[Catch: all -> 0x00cc, TryCatch #0 {all -> 0x00cc, blocks: (B:20:0x0041, B:23:0x004f, B:27:0x0058, B:28:0x0068, B:30:0x006e, B:33:0x0088, B:35:0x0090, B:37:0x0096, B:39:0x009e, B:41:0x00a8, B:43:0x00b0, B:45:0x00b7, B:47:0x00c3, B:49:0x007e), top: B:19:0x0041 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void a(final java.util.List<com.tencent.bugly.crashreport.crash.CrashDetailBean> r15, long r16, boolean r18, boolean r19, boolean r20) {
        /*
            Method dump skipped, instructions count: 229
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.b.a(java.util.List, long, boolean, boolean, boolean):void");
    }

    public static void a(boolean z, List<CrashDetailBean> list) {
        if (list != null && list.size() > 0) {
            x.c("up finish update state %b", Boolean.valueOf(z));
            for (CrashDetailBean crashDetailBean : list) {
                x.c("pre uid:%s uc:%d re:%b me:%b", crashDetailBean.c, Integer.valueOf(crashDetailBean.l), Boolean.valueOf(crashDetailBean.d), Boolean.valueOf(crashDetailBean.j));
                crashDetailBean.l++;
                crashDetailBean.d = z;
                x.c("set uid:%s uc:%d re:%b me:%b", crashDetailBean.c, Integer.valueOf(crashDetailBean.l), Boolean.valueOf(crashDetailBean.d), Boolean.valueOf(crashDetailBean.j));
            }
            Iterator<CrashDetailBean> it = list.iterator();
            while (it.hasNext()) {
                c.a().a(it.next());
            }
            x.c("update state size %d", Integer.valueOf(list.size()));
        }
        if (z) {
            return;
        }
        x.b("[crash] upload fail.", new Object[0]);
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00af A[Catch: all -> 0x01f0, TryCatch #0 {all -> 0x01f0, blocks: (B:11:0x000e, B:12:0x0011, B:15:0x0062, B:17:0x0071, B:19:0x0087, B:22:0x00af, B:24:0x00b5, B:25:0x00c8, B:27:0x00ce, B:30:0x00e0, B:32:0x00ee, B:33:0x0101, B:35:0x010d, B:37:0x0119, B:38:0x0150, B:41:0x013e, B:44:0x016a, B:46:0x0175, B:47:0x019a, B:49:0x019e, B:51:0x01a1, B:52:0x01bb, B:53:0x01c9, B:55:0x01cd, B:57:0x01e8, B:62:0x0183, B:64:0x0187, B:66:0x0092, B:68:0x0096, B:69:0x0017, B:72:0x0023, B:75:0x002f, B:78:0x003b, B:82:0x0049, B:86:0x0056), top: B:10:0x000e }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00ce A[Catch: all -> 0x01f0, TryCatch #0 {all -> 0x01f0, blocks: (B:11:0x000e, B:12:0x0011, B:15:0x0062, B:17:0x0071, B:19:0x0087, B:22:0x00af, B:24:0x00b5, B:25:0x00c8, B:27:0x00ce, B:30:0x00e0, B:32:0x00ee, B:33:0x0101, B:35:0x010d, B:37:0x0119, B:38:0x0150, B:41:0x013e, B:44:0x016a, B:46:0x0175, B:47:0x019a, B:49:0x019e, B:51:0x01a1, B:52:0x01bb, B:53:0x01c9, B:55:0x01cd, B:57:0x01e8, B:62:0x0183, B:64:0x0187, B:66:0x0092, B:68:0x0096, B:69:0x0017, B:72:0x0023, B:75:0x002f, B:78:0x003b, B:82:0x0049, B:86:0x0056), top: B:10:0x000e }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0175 A[Catch: all -> 0x01f0, TryCatch #0 {all -> 0x01f0, blocks: (B:11:0x000e, B:12:0x0011, B:15:0x0062, B:17:0x0071, B:19:0x0087, B:22:0x00af, B:24:0x00b5, B:25:0x00c8, B:27:0x00ce, B:30:0x00e0, B:32:0x00ee, B:33:0x0101, B:35:0x010d, B:37:0x0119, B:38:0x0150, B:41:0x013e, B:44:0x016a, B:46:0x0175, B:47:0x019a, B:49:0x019e, B:51:0x01a1, B:52:0x01bb, B:53:0x01c9, B:55:0x01cd, B:57:0x01e8, B:62:0x0183, B:64:0x0187, B:66:0x0092, B:68:0x0096, B:69:0x0017, B:72:0x0023, B:75:0x002f, B:78:0x003b, B:82:0x0049, B:86:0x0056), top: B:10:0x000e }] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x019e A[Catch: all -> 0x01f0, TryCatch #0 {all -> 0x01f0, blocks: (B:11:0x000e, B:12:0x0011, B:15:0x0062, B:17:0x0071, B:19:0x0087, B:22:0x00af, B:24:0x00b5, B:25:0x00c8, B:27:0x00ce, B:30:0x00e0, B:32:0x00ee, B:33:0x0101, B:35:0x010d, B:37:0x0119, B:38:0x0150, B:41:0x013e, B:44:0x016a, B:46:0x0175, B:47:0x019a, B:49:0x019e, B:51:0x01a1, B:52:0x01bb, B:53:0x01c9, B:55:0x01cd, B:57:0x01e8, B:62:0x0183, B:64:0x0187, B:66:0x0092, B:68:0x0096, B:69:0x0017, B:72:0x0023, B:75:0x002f, B:78:0x003b, B:82:0x0049, B:86:0x0056), top: B:10:0x000e }] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x01cd A[Catch: all -> 0x01f0, TryCatch #0 {all -> 0x01f0, blocks: (B:11:0x000e, B:12:0x0011, B:15:0x0062, B:17:0x0071, B:19:0x0087, B:22:0x00af, B:24:0x00b5, B:25:0x00c8, B:27:0x00ce, B:30:0x00e0, B:32:0x00ee, B:33:0x0101, B:35:0x010d, B:37:0x0119, B:38:0x0150, B:41:0x013e, B:44:0x016a, B:46:0x0175, B:47:0x019a, B:49:0x019e, B:51:0x01a1, B:52:0x01bb, B:53:0x01c9, B:55:0x01cd, B:57:0x01e8, B:62:0x0183, B:64:0x0187, B:66:0x0092, B:68:0x0096, B:69:0x0017, B:72:0x0023, B:75:0x002f, B:78:0x003b, B:82:0x0049, B:86:0x0056), top: B:10:0x000e }] */
    /* JADX WARN: Removed duplicated region for block: B:61:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0183 A[Catch: all -> 0x01f0, TryCatch #0 {all -> 0x01f0, blocks: (B:11:0x000e, B:12:0x0011, B:15:0x0062, B:17:0x0071, B:19:0x0087, B:22:0x00af, B:24:0x00b5, B:25:0x00c8, B:27:0x00ce, B:30:0x00e0, B:32:0x00ee, B:33:0x0101, B:35:0x010d, B:37:0x0119, B:38:0x0150, B:41:0x013e, B:44:0x016a, B:46:0x0175, B:47:0x019a, B:49:0x019e, B:51:0x01a1, B:52:0x01bb, B:53:0x01c9, B:55:0x01cd, B:57:0x01e8, B:62:0x0183, B:64:0x0187, B:66:0x0092, B:68:0x0096, B:69:0x0017, B:72:0x0023, B:75:0x002f, B:78:0x003b, B:82:0x0049, B:86:0x0056), top: B:10:0x000e }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void d(com.tencent.bugly.crashreport.crash.CrashDetailBean r13) {
        /*
            Method dump skipped, instructions count: 544
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.b.d(com.tencent.bugly.crashreport.crash.CrashDetailBean):void");
    }

    private static ContentValues f(CrashDetailBean crashDetailBean) {
        if (crashDetailBean == null) {
            return null;
        }
        try {
            ContentValues contentValues = new ContentValues();
            if (crashDetailBean.a > 0) {
                contentValues.put("_id", Long.valueOf(crashDetailBean.a));
            }
            contentValues.put("_tm", Long.valueOf(crashDetailBean.r));
            contentValues.put("_s1", crashDetailBean.u);
            int i = 1;
            contentValues.put("_up", Integer.valueOf(crashDetailBean.d ? 1 : 0));
            if (!crashDetailBean.j) {
                i = 0;
            }
            contentValues.put("_me", Integer.valueOf(i));
            contentValues.put("_uc", Integer.valueOf(crashDetailBean.l));
            contentValues.put("_dt", z.a(crashDetailBean));
            return contentValues;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private static CrashDetailBean a(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            byte[] blob = cursor.getBlob(cursor.getColumnIndex("_dt"));
            if (blob == null) {
                return null;
            }
            long j = cursor.getLong(cursor.getColumnIndex("_id"));
            CrashDetailBean crashDetailBean = (CrashDetailBean) z.a(blob, CrashDetailBean.CREATOR);
            if (crashDetailBean != null) {
                crashDetailBean.a = j;
            }
            return crashDetailBean;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public final void e(CrashDetailBean crashDetailBean) {
        ContentValues f;
        if (crashDetailBean == null || (f = f(crashDetailBean)) == null) {
            return;
        }
        long a2 = p.a().a("t_cr", f, (o) null, true);
        if (a2 >= 0) {
            x.c("insert %s success!", "t_cr");
            crashDetailBean.a = a2;
        }
    }

    private List<CrashDetailBean> b(List<a> list) {
        Cursor cursor;
        if (list == null || list.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("_id in ");
        sb.append("(");
        Iterator<a> it = list.iterator();
        while (it.hasNext()) {
            sb.append(it.next().a);
            sb.append(",");
        }
        if (sb.toString().contains(",")) {
            sb = new StringBuilder(sb.substring(0, sb.lastIndexOf(",")));
        }
        sb.append(")");
        String sb2 = sb.toString();
        sb.setLength(0);
        try {
            cursor = p.a().a("t_cr", null, sb2, null, null, true);
            if (cursor == null) {
                return null;
            }
            try {
                ArrayList arrayList = new ArrayList();
                sb.append("_id in ");
                sb.append("(");
                int i = 0;
                while (cursor.moveToNext()) {
                    CrashDetailBean a2 = a(cursor);
                    if (a2 != null) {
                        arrayList.add(a2);
                    } else {
                        try {
                            sb.append(cursor.getLong(cursor.getColumnIndex("_id")));
                            sb.append(",");
                            i++;
                        } catch (Throwable unused) {
                            x.d("unknown id!", new Object[0]);
                        }
                    }
                }
                if (sb.toString().contains(",")) {
                    sb = new StringBuilder(sb.substring(0, sb.lastIndexOf(",")));
                }
                sb.append(")");
                String sb3 = sb.toString();
                if (i > 0) {
                    x.d("deleted %s illegal data %d", "t_cr", Integer.valueOf(p.a().a("t_cr", sb3, (String[]) null, (o) null, true)));
                }
                if (cursor != null) {
                    cursor.close();
                }
                return arrayList;
            } catch (Throwable th) {
                th = th;
                try {
                    if (!x.a(th)) {
                        th.printStackTrace();
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                    return null;
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        } catch (Throwable th2) {
            th = th2;
            cursor = null;
        }
    }

    private static a b(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            a aVar = new a();
            aVar.a = cursor.getLong(cursor.getColumnIndex("_id"));
            aVar.b = cursor.getLong(cursor.getColumnIndex("_tm"));
            aVar.c = cursor.getString(cursor.getColumnIndex("_s1"));
            aVar.d = cursor.getInt(cursor.getColumnIndex("_up")) == 1;
            aVar.e = cursor.getInt(cursor.getColumnIndex("_me")) == 1;
            aVar.f = cursor.getInt(cursor.getColumnIndex("_uc"));
            return aVar;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private List<a> b() {
        ArrayList arrayList = new ArrayList();
        Cursor cursor = null;
        try {
            Cursor a2 = p.a().a("t_cr", new String[]{"_id", "_tm", "_s1", "_up", "_me", "_uc"}, null, null, null, true);
            if (a2 == null) {
                if (a2 != null) {
                    a2.close();
                }
                return null;
            }
            try {
                if (a2.getCount() <= 0) {
                    if (a2 != null) {
                        a2.close();
                    }
                    return arrayList;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("_id in ");
                sb.append("(");
                int i = 0;
                while (a2.moveToNext()) {
                    a b = b(a2);
                    if (b != null) {
                        arrayList.add(b);
                    } else {
                        try {
                            sb.append(a2.getLong(a2.getColumnIndex("_id")));
                            sb.append(",");
                            i++;
                        } catch (Throwable unused) {
                            x.d("unknown id!", new Object[0]);
                        }
                    }
                }
                if (sb.toString().contains(",")) {
                    sb = new StringBuilder(sb.substring(0, sb.lastIndexOf(",")));
                }
                sb.append(")");
                String sb2 = sb.toString();
                sb.setLength(0);
                if (i > 0) {
                    x.d("deleted %s illegal data %d", "t_cr", Integer.valueOf(p.a().a("t_cr", sb2, (String[]) null, (o) null, true)));
                }
                if (a2 != null) {
                    a2.close();
                }
                return arrayList;
            } catch (Throwable th) {
                th = th;
                cursor = a2;
                try {
                    if (!x.a(th)) {
                        th.printStackTrace();
                    }
                    return arrayList;
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static void c(List<a> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("_id in ");
        sb.append("(");
        Iterator<a> it = list.iterator();
        while (it.hasNext()) {
            sb.append(it.next().a);
            sb.append(",");
        }
        StringBuilder sb2 = new StringBuilder(sb.substring(0, sb.lastIndexOf(",")));
        sb2.append(")");
        String sb3 = sb2.toString();
        sb2.setLength(0);
        try {
            x.c("deleted %s data %d", "t_cr", Integer.valueOf(p.a().a("t_cr", sb3, (String[]) null, (o) null, true)));
        } catch (Throwable th) {
            if (x.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    private static void d(List<CrashDetailBean> list) {
        if (list != null) {
            try {
                if (list.size() == 0) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (CrashDetailBean crashDetailBean : list) {
                    sb.append(" or _id");
                    sb.append(" = ");
                    sb.append(crashDetailBean.a);
                }
                String sb2 = sb.toString();
                if (sb2.length() > 0) {
                    sb2 = sb2.substring(4);
                }
                sb.setLength(0);
                x.c("deleted %s data %d", "t_cr", Integer.valueOf(p.a().a("t_cr", sb2, (String[]) null, (o) null, true)));
            } catch (Throwable th) {
                if (x.a(th)) {
                    return;
                }
                th.printStackTrace();
            }
        }
    }

    private static ak a(Context context, CrashDetailBean crashDetailBean, com.tencent.bugly.crashreport.common.info.a aVar) {
        aj a2;
        aj a3;
        aj ajVar;
        if (context == null || crashDetailBean == null || aVar == null) {
            x.d("enExp args == null", new Object[0]);
            return null;
        }
        ak akVar = new ak();
        switch (crashDetailBean.b) {
            case 0:
                akVar.a = crashDetailBean.j ? "200" : "100";
                break;
            case 1:
                akVar.a = crashDetailBean.j ? "201" : "101";
                break;
            case 2:
                akVar.a = crashDetailBean.j ? "202" : "102";
                break;
            case 3:
                akVar.a = crashDetailBean.j ? "203" : "103";
                break;
            case 4:
                akVar.a = crashDetailBean.j ? "204" : "104";
                break;
            case 5:
                akVar.a = crashDetailBean.j ? "207" : "107";
                break;
            case 6:
                akVar.a = crashDetailBean.j ? "206" : "106";
                break;
            case 7:
                akVar.a = crashDetailBean.j ? "208" : "108";
                break;
            default:
                x.e("crash type error! %d", Integer.valueOf(crashDetailBean.b));
                break;
        }
        akVar.b = crashDetailBean.r;
        akVar.c = crashDetailBean.n;
        akVar.d = crashDetailBean.o;
        akVar.e = crashDetailBean.p;
        akVar.g = crashDetailBean.q;
        akVar.h = crashDetailBean.z;
        akVar.i = crashDetailBean.c;
        akVar.j = null;
        akVar.l = crashDetailBean.m;
        akVar.m = crashDetailBean.e;
        akVar.f = crashDetailBean.B;
        akVar.n = null;
        x.c("libInfo %s", akVar.o);
        if (crashDetailBean.h != null && crashDetailBean.h.size() > 0) {
            akVar.p = new ArrayList<>();
            for (Map.Entry<String, PlugInBean> entry : crashDetailBean.h.entrySet()) {
                ah ahVar = new ah();
                ahVar.a = entry.getValue().a;
                ahVar.b = entry.getValue().c;
                ahVar.c = entry.getValue().b;
                akVar.p.add(ahVar);
            }
        }
        if (crashDetailBean.j) {
            akVar.k = crashDetailBean.t;
            if (crashDetailBean.s != null && crashDetailBean.s.length() > 0) {
                if (akVar.q == null) {
                    akVar.q = new ArrayList<>();
                }
                try {
                    akVar.q.add(new aj((byte) 1, "alltimes.txt", crashDetailBean.s.getBytes("utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    akVar.q = null;
                }
            }
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(akVar.k);
            objArr[1] = Integer.valueOf(akVar.q != null ? akVar.q.size() : 0);
            x.c("crashcount:%d sz:%d", objArr);
        }
        if (crashDetailBean.w != null) {
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            try {
                akVar.q.add(new aj((byte) 1, "log.txt", crashDetailBean.w.getBytes("utf-8")));
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
                akVar.q = null;
            }
        }
        if (crashDetailBean.x != null) {
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            try {
                akVar.q.add(new aj((byte) 1, "jniLog.txt", crashDetailBean.x.getBytes("utf-8")));
            } catch (UnsupportedEncodingException e3) {
                e3.printStackTrace();
                akVar.q = null;
            }
        }
        if (!z.a(crashDetailBean.V)) {
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            try {
                ajVar = new aj((byte) 1, "crashInfos.txt", crashDetailBean.V.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e4) {
                e4.printStackTrace();
                ajVar = null;
            }
            if (ajVar != null) {
                x.c("attach crash infos", new Object[0]);
                akVar.q.add(ajVar);
            }
        }
        if (crashDetailBean.W != null) {
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            aj a4 = a("backupRecord.zip", context, crashDetailBean.W);
            if (a4 != null) {
                x.c("attach backup record", new Object[0]);
                akVar.q.add(a4);
            }
        }
        if (crashDetailBean.y != null && crashDetailBean.y.length > 0) {
            aj ajVar2 = new aj((byte) 2, "buglylog.zip", crashDetailBean.y);
            x.c("attach user log", new Object[0]);
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            akVar.q.add(ajVar2);
        }
        if (crashDetailBean.b == 3) {
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            x.c("crashBean.anrMessages:%s", crashDetailBean.P);
            if (crashDetailBean.P != null && crashDetailBean.P.containsKey("BUGLY_CR_01")) {
                try {
                    if (!TextUtils.isEmpty(crashDetailBean.P.get("BUGLY_CR_01"))) {
                        akVar.q.add(new aj((byte) 1, "anrMessage.txt", crashDetailBean.P.get("BUGLY_CR_01").getBytes("utf-8")));
                        x.c("attach anr message", new Object[0]);
                    }
                } catch (UnsupportedEncodingException e5) {
                    e5.printStackTrace();
                    akVar.q = null;
                }
                crashDetailBean.P.remove("BUGLY_CR_01");
            }
            if (crashDetailBean.v != null && NativeCrashHandler.getInstance().isEnableCatchAnrTrace() && (a3 = a("trace.zip", context, crashDetailBean.v)) != null) {
                x.c("attach traces", new Object[0]);
                akVar.q.add(a3);
            }
        }
        if (crashDetailBean.b == 1) {
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            if (crashDetailBean.v != null && (a2 = a("tomb.zip", context, crashDetailBean.v)) != null) {
                x.c("attach tombs", new Object[0]);
                akVar.q.add(a2);
            }
        }
        if (aVar.D != null && !aVar.D.isEmpty()) {
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            StringBuilder sb = new StringBuilder();
            Iterator<String> it = aVar.D.iterator();
            while (it.hasNext()) {
                sb.append(it.next());
            }
            try {
                akVar.q.add(new aj((byte) 1, "martianlog.txt", sb.toString().getBytes("utf-8")));
                x.c("attach pageTracingList", new Object[0]);
            } catch (UnsupportedEncodingException e6) {
                e6.printStackTrace();
            }
        }
        if (crashDetailBean.U != null && crashDetailBean.U.length > 0) {
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            akVar.q.add(new aj((byte) 1, "userExtraByteData", crashDetailBean.U));
            x.c("attach extraData", new Object[0]);
        }
        akVar.r = new HashMap();
        Map<String, String> map = akVar.r;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(crashDetailBean.C);
        map.put("A9", sb2.toString());
        Map<String, String> map2 = akVar.r;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(crashDetailBean.D);
        map2.put("A11", sb3.toString());
        Map<String, String> map3 = akVar.r;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(crashDetailBean.E);
        map3.put("A10", sb4.toString());
        akVar.r.put("A23", crashDetailBean.f);
        akVar.r.put("A7", aVar.g);
        akVar.r.put("A6", aVar.n());
        akVar.r.put("A5", aVar.m());
        akVar.r.put("A22", aVar.h());
        Map<String, String> map4 = akVar.r;
        StringBuilder sb5 = new StringBuilder();
        sb5.append(crashDetailBean.G);
        map4.put("A2", sb5.toString());
        Map<String, String> map5 = akVar.r;
        StringBuilder sb6 = new StringBuilder();
        sb6.append(crashDetailBean.F);
        map5.put("A1", sb6.toString());
        akVar.r.put("A24", aVar.i);
        Map<String, String> map6 = akVar.r;
        StringBuilder sb7 = new StringBuilder();
        sb7.append(crashDetailBean.H);
        map6.put("A17", sb7.toString());
        akVar.r.put("A25", aVar.h());
        akVar.r.put("A15", aVar.q());
        Map<String, String> map7 = akVar.r;
        StringBuilder sb8 = new StringBuilder();
        sb8.append(aVar.r());
        map7.put("A13", sb8.toString());
        akVar.r.put("A34", crashDetailBean.A);
        if (aVar.y != null) {
            akVar.r.put("productIdentify", aVar.y);
        }
        try {
            akVar.r.put("A26", URLEncoder.encode(crashDetailBean.I, "utf-8"));
        } catch (UnsupportedEncodingException e7) {
            e7.printStackTrace();
        }
        if (crashDetailBean.b == 1) {
            akVar.r.put("A27", crashDetailBean.K);
            akVar.r.put("A28", crashDetailBean.J);
            Map<String, String> map8 = akVar.r;
            StringBuilder sb9 = new StringBuilder();
            sb9.append(crashDetailBean.k);
            map8.put("A29", sb9.toString());
        }
        akVar.r.put("A30", crashDetailBean.L);
        Map<String, String> map9 = akVar.r;
        StringBuilder sb10 = new StringBuilder();
        sb10.append(crashDetailBean.M);
        map9.put("A18", sb10.toString());
        Map<String, String> map10 = akVar.r;
        StringBuilder sb11 = new StringBuilder();
        sb11.append(!crashDetailBean.N);
        map10.put("A36", sb11.toString());
        Map<String, String> map11 = akVar.r;
        StringBuilder sb12 = new StringBuilder();
        sb12.append(aVar.r);
        map11.put("F02", sb12.toString());
        Map<String, String> map12 = akVar.r;
        StringBuilder sb13 = new StringBuilder();
        sb13.append(aVar.s);
        map12.put("F03", sb13.toString());
        akVar.r.put("F04", aVar.e());
        Map<String, String> map13 = akVar.r;
        StringBuilder sb14 = new StringBuilder();
        sb14.append(aVar.t);
        map13.put("F05", sb14.toString());
        akVar.r.put("F06", aVar.q);
        akVar.r.put("F08", aVar.w);
        akVar.r.put("F09", aVar.x);
        Map<String, String> map14 = akVar.r;
        StringBuilder sb15 = new StringBuilder();
        sb15.append(aVar.u);
        map14.put("F10", sb15.toString());
        if (crashDetailBean.Q >= 0) {
            Map<String, String> map15 = akVar.r;
            StringBuilder sb16 = new StringBuilder();
            sb16.append(crashDetailBean.Q);
            map15.put("C01", sb16.toString());
        }
        if (crashDetailBean.R >= 0) {
            Map<String, String> map16 = akVar.r;
            StringBuilder sb17 = new StringBuilder();
            sb17.append(crashDetailBean.R);
            map16.put("C02", sb17.toString());
        }
        if (crashDetailBean.S != null && crashDetailBean.S.size() > 0) {
            for (Map.Entry<String, String> entry2 : crashDetailBean.S.entrySet()) {
                akVar.r.put("C03_" + entry2.getKey(), entry2.getValue());
            }
        }
        if (crashDetailBean.T != null && crashDetailBean.T.size() > 0) {
            for (Map.Entry<String, String> entry3 : crashDetailBean.T.entrySet()) {
                akVar.r.put("C04_" + entry3.getKey(), entry3.getValue());
            }
        }
        akVar.s = null;
        if (crashDetailBean.O != null && crashDetailBean.O.size() > 0) {
            akVar.s = crashDetailBean.O;
            x.a("setted message size %d", Integer.valueOf(akVar.s.size()));
        }
        Object[] objArr2 = new Object[12];
        objArr2[0] = crashDetailBean.n;
        objArr2[1] = crashDetailBean.c;
        objArr2[2] = aVar.e();
        objArr2[3] = Long.valueOf((crashDetailBean.r - crashDetailBean.M) / 1000);
        objArr2[4] = Boolean.valueOf(crashDetailBean.k);
        objArr2[5] = Boolean.valueOf(crashDetailBean.N);
        objArr2[6] = Boolean.valueOf(crashDetailBean.j);
        objArr2[7] = Boolean.valueOf(crashDetailBean.b == 1);
        objArr2[8] = Integer.valueOf(crashDetailBean.t);
        objArr2[9] = crashDetailBean.s;
        objArr2[10] = Boolean.valueOf(crashDetailBean.d);
        objArr2[11] = Integer.valueOf(akVar.r.size());
        x.c("%s rid:%s sess:%s ls:%ds isR:%b isF:%b isM:%b isN:%b mc:%d ,%s ,isUp:%b ,vm:%d", objArr2);
        return akVar;
    }

    private static aj a(String str, Context context, String str2) {
        FileInputStream fileInputStream;
        if (str2 == null || context == null) {
            x.d("rqdp{  createZipAttachment sourcePath == null || context == null ,pls check}", new Object[0]);
            return null;
        }
        x.c("zip %s", str2);
        File file = new File(str2);
        File file2 = new File(context.getCacheDir(), str);
        if (!z.a(file, file2, 5000)) {
            x.d("zip fail!", new Object[0]);
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            fileInputStream = new FileInputStream(file2);
            try {
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read <= 0) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                    byteArrayOutputStream.flush();
                }
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                x.c("read bytes :%d", Integer.valueOf(byteArray.length));
                aj ajVar = new aj((byte) 2, file2.getName(), byteArray);
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    if (!x.a(e)) {
                        e.printStackTrace();
                    }
                }
                if (file2.exists()) {
                    x.c("del tmp", new Object[0]);
                    file2.delete();
                }
                return ajVar;
            } catch (Throwable th) {
                th = th;
                try {
                    if (!x.a(th)) {
                        th.printStackTrace();
                    }
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e2) {
                            if (!x.a(e2)) {
                                e2.printStackTrace();
                            }
                        }
                    }
                    if (file2.exists()) {
                        x.c("del tmp", new Object[0]);
                        file2.delete();
                    }
                    return null;
                } catch (Throwable th2) {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e3) {
                            if (!x.a(e3)) {
                                e3.printStackTrace();
                            }
                        }
                    }
                    if (file2.exists()) {
                        x.c("del tmp", new Object[0]);
                        file2.delete();
                    }
                    throw th2;
                }
            }
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
        }
    }

    public static void a(String str, String str2, String str3, String str4, String str5, CrashDetailBean crashDetailBean) {
        String str6;
        com.tencent.bugly.crashreport.common.info.a b = com.tencent.bugly.crashreport.common.info.a.b();
        if (b == null) {
            return;
        }
        x.e("#++++++++++Record By Bugly++++++++++#", new Object[0]);
        x.e("# You can use Bugly(http:\\\\bugly.qq.com) to get more Crash Detail!", new Object[0]);
        x.e("# PKG NAME: %s", b.c);
        x.e("# APP VER: %s", b.k);
        x.e("# SDK VER: %s", b.f);
        x.e("# LAUNCH TIME: %s", z.a(new Date(com.tencent.bugly.crashreport.common.info.a.b().a)));
        x.e("# CRASH TYPE: %s", str);
        x.e("# CRASH TIME: %s", str2);
        x.e("# CRASH PROCESS: %s", str3);
        x.e("# CRASH THREAD: %s", str4);
        if (crashDetailBean != null) {
            x.e("# REPORT ID: %s", crashDetailBean.c);
            Object[] objArr = new Object[2];
            objArr[0] = b.h;
            objArr[1] = b.r().booleanValue() ? "ROOTED" : "UNROOT";
            x.e("# CRASH DEVICE: %s %s", objArr);
            x.e("# RUNTIME AVAIL RAM:%d ROM:%d SD:%d", Long.valueOf(crashDetailBean.C), Long.valueOf(crashDetailBean.D), Long.valueOf(crashDetailBean.E));
            x.e("# RUNTIME TOTAL RAM:%d ROM:%d SD:%d", Long.valueOf(crashDetailBean.F), Long.valueOf(crashDetailBean.G), Long.valueOf(crashDetailBean.H));
            if (!z.a(crashDetailBean.K)) {
                x.e("# EXCEPTION FIRED BY %s %s", crashDetailBean.K, crashDetailBean.J);
            } else if (crashDetailBean.b == 3) {
                Object[] objArr2 = new Object[1];
                if (crashDetailBean.P == null) {
                    str6 = IConstant.DEFAULT_PATH;
                } else {
                    str6 = crashDetailBean.P.get("BUGLY_CR_01");
                }
                objArr2[0] = str6;
                x.e("# EXCEPTION ANR MESSAGE:\n %s", objArr2);
            }
        }
        if (!z.a(str5)) {
            x.e("# CRASH STACK: ", new Object[0]);
            x.e(str5, new Object[0]);
        }
        x.e("#++++++++++++++++++++++++++++++++++++++++++#", new Object[0]);
    }
}
