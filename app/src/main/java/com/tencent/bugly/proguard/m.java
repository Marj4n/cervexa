package com.tencent.bugly.proguard;

import java.io.Serializable;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class m implements Serializable, Comparable<m> {
    public long a;
    public String b;
    public long c;
    public int d;
    public String e;
    public String f;
    public long g;

    @Override // java.lang.Comparable
    public final /* bridge */ /* synthetic */ int compareTo(m mVar) {
        return (int) (this.c - mVar.c);
    }
}
