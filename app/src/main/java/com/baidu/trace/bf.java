package com.baidu.trace;

/* loaded from: classes.dex */
final class bf implements Runnable {
    private /* synthetic */ be a;

    bf(be beVar) {
        this.a = beVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.a.b.onQueryCacheTrackCallback(this.a.a);
    }
}
