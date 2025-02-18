package com.baidu.trace;

import com.baidu.trace.api.bos.BosGetObjectResponse;
import com.baidu.trace.api.bos.BosObjectResponse;
import com.baidu.trace.api.bos.BosPutObjectResponse;
import com.baidu.trace.api.bos.OnBosListener;

/* loaded from: classes.dex */
final class k implements Runnable {
    private /* synthetic */ int a;
    private /* synthetic */ OnBosListener b;
    private /* synthetic */ BosObjectResponse c;

    k(int i, OnBosListener onBosListener, BosObjectResponse bosObjectResponse) {
        this.a = i;
        this.b = onBosListener;
        this.c = bosObjectResponse;
    }

    @Override // java.lang.Runnable
    public final void run() {
        int i = this.a;
        if (i == 1) {
            this.b.onPutObjectCallback((BosPutObjectResponse) this.c);
        } else {
            if (i != 2) {
                return;
            }
            this.b.onGetObjectCallback((BosGetObjectResponse) this.c);
        }
    }
}
