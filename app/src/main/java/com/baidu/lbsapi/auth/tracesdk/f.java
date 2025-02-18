package com.baidu.lbsapi.auth.tracesdk;

import java.util.List;

/* loaded from: classes.dex */
class f implements Runnable {
    final /* synthetic */ e a;

    f(e eVar) {
        this.a = eVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        List list;
        e eVar = this.a;
        list = eVar.b;
        eVar.a(list);
    }
}
