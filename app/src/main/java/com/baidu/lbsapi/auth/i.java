package com.baidu.lbsapi.auth;

import android.content.Context;
import java.util.Hashtable;

/* loaded from: classes.dex */
class i implements Runnable {
    final /* synthetic */ int a;
    final /* synthetic */ boolean b;
    final /* synthetic */ String c;
    final /* synthetic */ String d;
    final /* synthetic */ Hashtable e;
    final /* synthetic */ LBSAuthManager f;

    i(LBSAuthManager lBSAuthManager, int i, boolean z, String str, String str2, Hashtable hashtable) {
        this.f = lBSAuthManager;
        this.a = i;
        this.b = z;
        this.c = str;
        this.d = str2;
        this.e = hashtable;
    }

    @Override // java.lang.Runnable
    public void run() {
        Context context;
        boolean b;
        l lVar;
        boolean b2;
        if (a.a) {
            StringBuilder sb = new StringBuilder();
            sb.append("status = ");
            sb.append(this.a);
            sb.append("; forced = ");
            sb.append(this.b);
            sb.append("checkAK = ");
            b2 = this.f.b(this.c);
            sb.append(b2);
            a.a(sb.toString());
        }
        int i = this.a;
        if (i != 601 && !this.b && i != -1) {
            b = this.f.b(this.c);
            if (!b) {
                if (602 == this.a) {
                    if (a.a) {
                        a.a("authenticate wait  ");
                    }
                    lVar = LBSAuthManager.d;
                    lVar.b();
                } else if (a.a) {
                    a.a("authenticate else  ");
                }
                this.f.a((String) null, this.c);
                return;
            }
        }
        if (a.a) {
            a.a("authenticate sendAuthRequest");
        }
        context = LBSAuthManager.a;
        String[] b3 = b.b(context);
        if (a.a) {
            a.a("authStrings.length:" + b3.length);
        }
        if (b3 == null || b3.length <= 1) {
            this.f.a(this.b, this.d, this.e, this.c);
            return;
        }
        if (a.a) {
            a.a("more sha1 auth");
        }
        this.f.a(this.b, this.d, (Hashtable<String, String>) this.e, b3, this.c);
    }
}
