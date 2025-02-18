package com.baidu.trace.c;

import com.baidu.trace.c.a;
import com.baidu.trace.model.StatusCodes;

/* loaded from: classes.dex */
final class b implements Runnable {
    private /* synthetic */ a.d a;

    b(a.d dVar) {
        this.a = dVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        a.d.a(this.a.a, this.a.f, StatusCodes.NETWORK_NOT_AVAILABLE, StatusCodes.MSG_NETWORK_NOT_AVAILABLE, this.a.b, this.a.g);
    }
}
