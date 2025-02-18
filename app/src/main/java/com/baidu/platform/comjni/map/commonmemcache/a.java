package com.baidu.platform.comjni.map.commonmemcache;

/* loaded from: classes.dex */
public class a {
    private long a = 0;
    private JNICommonMemCache b;

    public a() {
        this.b = null;
        this.b = new JNICommonMemCache();
    }

    public long a() {
        long Create = this.b.Create();
        this.a = Create;
        return Create;
    }

    public void b() {
        long j = this.a;
        if (j != 0) {
            this.b.Init(j);
        }
    }
}
