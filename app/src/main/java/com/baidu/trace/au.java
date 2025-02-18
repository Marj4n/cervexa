package com.baidu.trace;

import android.content.Context;
import com.baidu.mapapi.UIMsg;
import com.baidu.trace.model.StatusCodes;

/* loaded from: classes.dex */
final class au implements Runnable {
    private /* synthetic */ Context a;
    private /* synthetic */ ar b;

    au(ar arVar, Context context) {
        this.b = arVar;
        this.a = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ak akVar;
        ak akVar2;
        ak akVar3;
        akVar = this.b.j;
        if (akVar == null) {
            this.b.j = new ak(this.a);
        }
        if (com.baidu.trace.c.e.e(this.a)) {
            akVar3 = this.b.j;
            akVar3.a(UIMsg.m_AppUI.MSG_APP_DATA_OK);
            return;
        }
        a.a("BaiduTraceSDK", "Need ACCESS_FINE_LOCATION permission to get gps information");
        this.b.d();
        if (3 != ak.a) {
            akVar2 = this.b.j;
            akVar2.a(3, StatusCodes.MSG_NOT_GRANTED_PERMISSIONS);
        }
    }
}
