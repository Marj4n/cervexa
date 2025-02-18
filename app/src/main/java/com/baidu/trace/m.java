package com.baidu.trace;

import com.baidu.trace.api.bos.BosGetObjectResponse;
import com.baidu.trace.api.bos.OnBosListener;

/* loaded from: classes.dex */
final class m implements Runnable {
    private /* synthetic */ OnBosListener a;
    private /* synthetic */ BosGetObjectResponse b;

    m(OnBosListener onBosListener, BosGetObjectResponse bosGetObjectResponse) {
        this.a = onBosListener;
        this.b = bosGetObjectResponse;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.a.onGetObjectCallback(this.b);
    }
}
