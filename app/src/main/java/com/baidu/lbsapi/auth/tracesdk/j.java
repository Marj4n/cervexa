package com.baidu.lbsapi.auth.tracesdk;

import com.baidu.lbsapi.auth.tracesdk.c;

/* loaded from: classes.dex */
class j implements c.a {
    final /* synthetic */ String a;
    final /* synthetic */ LBSAuthManager b;

    j(LBSAuthManager lBSAuthManager, String str) {
        this.b = lBSAuthManager;
        this.a = str;
    }

    @Override // com.baidu.lbsapi.auth.tracesdk.c.a
    public void a(String str) {
        this.b.a(str, this.a);
    }
}
