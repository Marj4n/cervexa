package com.baidu.platform.comapi;

import android.os.Handler;
import android.os.Message;

/* loaded from: classes.dex */
class b extends Handler {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        this.a.a(message);
    }
}
