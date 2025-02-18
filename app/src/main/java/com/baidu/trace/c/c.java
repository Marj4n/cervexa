package com.baidu.trace.c;

import android.text.TextUtils;
import com.baidu.trace.c.a;
import com.baidu.trace.model.StatusCodes;

/* loaded from: classes.dex */
final class c implements Runnable {
    private /* synthetic */ StringBuffer a;
    private /* synthetic */ a.d b;

    c(a.d dVar, StringBuffer stringBuffer) {
        this.b = dVar;
        this.a = stringBuffer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (TextUtils.isEmpty(this.a.toString())) {
            a.d.a(this.b.a, this.b.f, StatusCodes.REQUEST_FAILED, StatusCodes.MSG_REQUEST_FAILED, this.b.b, this.b.g);
        } else {
            a.d.a(this.b.a, this.b.f, this.a.toString(), this.b.b, this.b.g);
        }
    }
}
