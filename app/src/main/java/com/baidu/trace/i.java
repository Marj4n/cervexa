package com.baidu.trace;

import com.baidu.trace.api.bos.BosPutObjectResponse;
import com.baidu.trace.api.bos.OnBosListener;

/* loaded from: classes.dex */
final class i implements Runnable {
    private /* synthetic */ OnBosListener a;
    private /* synthetic */ BosPutObjectResponse b;

    i(OnBosListener onBosListener, BosPutObjectResponse bosPutObjectResponse) {
        this.a = onBosListener;
        this.b = bosPutObjectResponse;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.a.onPutObjectCallback(this.b);
    }
}
