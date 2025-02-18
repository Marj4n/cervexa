package com.baidu.trace;

/* loaded from: classes.dex */
final class bh implements Runnable {
    private /* synthetic */ bg a;

    bh(bg bgVar) {
        this.a = bgVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.a.b.onClearCacheTrackCallback(this.a.a);
    }
}
