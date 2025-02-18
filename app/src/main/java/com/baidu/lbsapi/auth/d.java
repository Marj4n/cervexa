package com.baidu.lbsapi.auth;

import android.content.Context;
import java.util.HashMap;

/* loaded from: classes.dex */
class d implements Runnable {
    final /* synthetic */ c a;

    d(c cVar) {
        this.a = cVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        Context context;
        HashMap<String, String> hashMap;
        a.a("postWithHttps start Thread id = " + String.valueOf(Thread.currentThread().getId()));
        context = this.a.a;
        g gVar = new g(context);
        hashMap = this.a.b;
        this.a.a(gVar.a(hashMap));
    }
}
