package com.baidu.platform.comapi.map;

import android.os.Handler;
import android.os.Message;

/* loaded from: classes.dex */
class v extends Handler {
    final /* synthetic */ u a;

    v(u uVar) {
        this.a = uVar;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        u uVar;
        z zVar;
        super.handleMessage(message);
        uVar = u.c;
        if (uVar != null) {
            zVar = this.a.d;
            zVar.a(message);
        }
    }
}
