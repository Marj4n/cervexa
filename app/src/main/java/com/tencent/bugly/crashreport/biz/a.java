package com.tencent.bugly.crashreport.biz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.p;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.util.ArrayList;
import java.util.List;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class a {
    private Context a;
    private long b;
    private int c;
    private boolean d;

    static /* synthetic */ void a(a aVar, UserInfoBean userInfoBean, boolean z) {
        List<UserInfoBean> a;
        if (userInfoBean != null) {
            if (!z && userInfoBean.b != 1 && (a = aVar.a(com.tencent.bugly.crashreport.common.info.a.a(aVar.a).d)) != null && a.size() >= 20) {
                x.a("[UserInfo] There are too many user info in local: %d", Integer.valueOf(a.size()));
                return;
            }
            long a2 = p.a().a("t_ui", a(userInfoBean), (o) null, true);
            if (a2 >= 0) {
                x.c("[Database] insert %s success with ID: %d", "t_ui", Long.valueOf(a2));
                userInfoBean.a = a2;
            }
        }
    }

    public a(Context context, boolean z) {
        this.d = true;
        this.a = context;
        this.d = z;
    }

    public final void a(int i, boolean z, long j) {
        com.tencent.bugly.crashreport.common.strategy.a a = com.tencent.bugly.crashreport.common.strategy.a.a();
        if (a != null && !a.c().f && i != 1 && i != 3) {
            x.e("UserInfo is disable", new Object[0]);
            return;
        }
        if (i == 1 || i == 3) {
            this.c++;
        }
        com.tencent.bugly.crashreport.common.info.a a2 = com.tencent.bugly.crashreport.common.info.a.a(this.a);
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.b = i;
        userInfoBean.c = a2.d;
        userInfoBean.d = a2.g();
        userInfoBean.e = System.currentTimeMillis();
        userInfoBean.f = -1L;
        userInfoBean.n = a2.k;
        userInfoBean.o = i == 1 ? 1 : 0;
        userInfoBean.l = a2.a();
        userInfoBean.m = a2.q;
        userInfoBean.g = a2.r;
        userInfoBean.h = a2.s;
        userInfoBean.i = a2.t;
        userInfoBean.k = a2.u;
        userInfoBean.r = a2.t();
        userInfoBean.s = a2.y();
        userInfoBean.p = a2.z();
        userInfoBean.q = a2.A();
        w.a().a(new RunnableC0037a(userInfoBean, z), 0L);
    }

    public final void a() {
        this.b = z.b() + 86400000;
        w.a().a(new b(), (this.b - System.currentTimeMillis()) + 5000);
    }

    /* compiled from: BUGLY */
    /* renamed from: com.tencent.bugly.crashreport.biz.a$a, reason: collision with other inner class name */
    class RunnableC0037a implements Runnable {
        private boolean a;
        private UserInfoBean b;

        public RunnableC0037a(UserInfoBean userInfoBean, boolean z) {
            this.b = userInfoBean;
            this.a = z;
        }

        @Override // java.lang.Runnable
        public final void run() {
            com.tencent.bugly.crashreport.common.info.a b;
            try {
                if (this.b != null) {
                    UserInfoBean userInfoBean = this.b;
                    if (userInfoBean != null && (b = com.tencent.bugly.crashreport.common.info.a.b()) != null) {
                        userInfoBean.j = b.e();
                    }
                    x.c("[UserInfo] Record user info.", new Object[0]);
                    a.a(a.this, this.b, false);
                }
                if (this.a) {
                    a aVar = a.this;
                    w a = w.a();
                    if (a != null) {
                        a.a(aVar.new AnonymousClass2());
                    }
                }
            } catch (Throwable th) {
                if (x.a(th)) {
                    return;
                }
                th.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0115  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x00f2 A[Catch: all -> 0x0173, TryCatch #0 {, blocks: (B:3:0x0001, B:8:0x0007, B:12:0x000f, B:16:0x0017, B:18:0x001d, B:22:0x0027, B:24:0x003c, B:27:0x0045, B:29:0x004c, B:30:0x004f, B:32:0x0055, B:34:0x0069, B:36:0x0079, B:43:0x0081, B:45:0x008b, B:46:0x0090, B:48:0x0096, B:50:0x00a4, B:52:0x00b1, B:53:0x00b4, B:56:0x00c2, B:58:0x00c6, B:60:0x00cb, B:63:0x00d0, B:73:0x00d7, B:74:0x00ec, B:76:0x00f2, B:78:0x00f7, B:81:0x00fe, B:84:0x0116, B:86:0x011c, B:89:0x0125, B:91:0x012b, B:94:0x0134, B:96:0x013e, B:99:0x0147, B:102:0x0165, B:107:0x016a, B:111:0x00e6), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0113  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x011c A[Catch: all -> 0x0173, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:8:0x0007, B:12:0x000f, B:16:0x0017, B:18:0x001d, B:22:0x0027, B:24:0x003c, B:27:0x0045, B:29:0x004c, B:30:0x004f, B:32:0x0055, B:34:0x0069, B:36:0x0079, B:43:0x0081, B:45:0x008b, B:46:0x0090, B:48:0x0096, B:50:0x00a4, B:52:0x00b1, B:53:0x00b4, B:56:0x00c2, B:58:0x00c6, B:60:0x00cb, B:63:0x00d0, B:73:0x00d7, B:74:0x00ec, B:76:0x00f2, B:78:0x00f7, B:81:0x00fe, B:84:0x0116, B:86:0x011c, B:89:0x0125, B:91:0x012b, B:94:0x0134, B:96:0x013e, B:99:0x0147, B:102:0x0165, B:107:0x016a, B:111:0x00e6), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0125 A[Catch: all -> 0x0173, TRY_ENTER, TryCatch #0 {, blocks: (B:3:0x0001, B:8:0x0007, B:12:0x000f, B:16:0x0017, B:18:0x001d, B:22:0x0027, B:24:0x003c, B:27:0x0045, B:29:0x004c, B:30:0x004f, B:32:0x0055, B:34:0x0069, B:36:0x0079, B:43:0x0081, B:45:0x008b, B:46:0x0090, B:48:0x0096, B:50:0x00a4, B:52:0x00b1, B:53:0x00b4, B:56:0x00c2, B:58:0x00c6, B:60:0x00cb, B:63:0x00d0, B:73:0x00d7, B:74:0x00ec, B:76:0x00f2, B:78:0x00f7, B:81:0x00fe, B:84:0x0116, B:86:0x011c, B:89:0x0125, B:91:0x012b, B:94:0x0134, B:96:0x013e, B:99:0x0147, B:102:0x0165, B:107:0x016a, B:111:0x00e6), top: B:2:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void c() {
        /*
            Method dump skipped, instructions count: 374
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.biz.a.c():void");
    }

    public final void b() {
        w a = w.a();
        if (a != null) {
            a.a(new AnonymousClass2());
        }
    }

    /* compiled from: BUGLY */
    /* renamed from: com.tencent.bugly.crashreport.biz.a$2, reason: invalid class name */
    final class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            try {
                a.this.c();
            } catch (Throwable th) {
                x.a(th);
            }
        }
    }

    /* compiled from: BUGLY */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis < a.this.b) {
                w.a().a(a.this.new b(), (a.this.b - currentTimeMillis) + 5000);
            } else {
                a.this.a(3, false, 0L);
                a.this.a();
            }
        }
    }

    /* compiled from: BUGLY */
    class c implements Runnable {
        private long a;

        public c(long j) {
            this.a = 21600000L;
            this.a = j;
        }

        @Override // java.lang.Runnable
        public final void run() {
            a aVar = a.this;
            w a = w.a();
            if (a != null) {
                a.a(aVar.new AnonymousClass2());
            }
            a aVar2 = a.this;
            long j = this.a;
            w.a().a(aVar2.new c(j), j);
        }
    }

    public final List<UserInfoBean> a(String str) {
        Cursor cursor;
        String str2;
        try {
            if (z.a(str)) {
                str2 = null;
            } else {
                str2 = "_pc = '" + str + "'";
            }
            cursor = p.a().a("t_ui", null, str2, null, null, true);
            if (cursor == null) {
                return null;
            }
            try {
                StringBuilder sb = new StringBuilder();
                ArrayList arrayList = new ArrayList();
                while (cursor.moveToNext()) {
                    UserInfoBean a = a(cursor);
                    if (a != null) {
                        arrayList.add(a);
                    } else {
                        try {
                            long j = cursor.getLong(cursor.getColumnIndex("_id"));
                            sb.append(" or _id");
                            sb.append(" = ");
                            sb.append(j);
                        } catch (Throwable unused) {
                            x.d("[Database] unknown id.", new Object[0]);
                        }
                    }
                }
                String sb2 = sb.toString();
                if (sb2.length() > 0) {
                    x.d("[Database] deleted %s error data %d", "t_ui", Integer.valueOf(p.a().a("t_ui", sb2.substring(4), (String[]) null, (o) null, true)));
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

    private static void a(List<UserInfoBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size() && i < 50; i++) {
            UserInfoBean userInfoBean = list.get(i);
            sb.append(" or _id");
            sb.append(" = ");
            sb.append(userInfoBean.a);
        }
        String sb2 = sb.toString();
        if (sb2.length() > 0) {
            sb2 = sb2.substring(4);
        }
        String str = sb2;
        sb.setLength(0);
        try {
            x.c("[Database] deleted %s data %d", "t_ui", Integer.valueOf(p.a().a("t_ui", str, (String[]) null, (o) null, true)));
        } catch (Throwable th) {
            if (x.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    private static ContentValues a(UserInfoBean userInfoBean) {
        if (userInfoBean == null) {
            return null;
        }
        try {
            ContentValues contentValues = new ContentValues();
            if (userInfoBean.a > 0) {
                contentValues.put("_id", Long.valueOf(userInfoBean.a));
            }
            contentValues.put("_tm", Long.valueOf(userInfoBean.e));
            contentValues.put("_ut", Long.valueOf(userInfoBean.f));
            contentValues.put("_tp", Integer.valueOf(userInfoBean.b));
            contentValues.put("_pc", userInfoBean.c);
            contentValues.put("_dt", z.a(userInfoBean));
            return contentValues;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private static UserInfoBean a(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            byte[] blob = cursor.getBlob(cursor.getColumnIndex("_dt"));
            if (blob == null) {
                return null;
            }
            long j = cursor.getLong(cursor.getColumnIndex("_id"));
            UserInfoBean userInfoBean = (UserInfoBean) z.a(blob, UserInfoBean.CREATOR);
            if (userInfoBean != null) {
                userInfoBean.a = j;
            }
            return userInfoBean;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }
}
