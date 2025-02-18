package com.baidu.lbsapi.auth.tracesdk;

import com.baidu.lbsapi.auth.tracesdk.e;

/* loaded from: classes.dex */
class k implements e.a {
    final /* synthetic */ String a;
    final /* synthetic */ LBSAuthManager b;

    k(LBSAuthManager lBSAuthManager, String str) {
        this.b = lBSAuthManager;
        this.a = str;
    }

    @Override // com.baidu.lbsapi.auth.tracesdk.e.a
    public void a(String str) {
        this.b.a(str, this.a);
    }
}
