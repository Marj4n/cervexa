package com.tencent.bugly.proguard;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class ah extends k implements Cloneable {
    public String a = "";
    private String d = "";
    public String b = "";
    private String e = "";
    public String c = "";

    @Override // com.tencent.bugly.proguard.k
    public final void a(StringBuilder sb, int i) {
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(j jVar) {
        jVar.a(this.a, 0);
        String str = this.d;
        if (str != null) {
            jVar.a(str, 1);
        }
        String str2 = this.b;
        if (str2 != null) {
            jVar.a(str2, 2);
        }
        String str3 = this.e;
        if (str3 != null) {
            jVar.a(str3, 3);
        }
        String str4 = this.c;
        if (str4 != null) {
            jVar.a(str4, 4);
        }
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(i iVar) {
        this.a = iVar.b(0, true);
        this.d = iVar.b(1, false);
        this.b = iVar.b(2, false);
        this.e = iVar.b(3, false);
        this.c = iVar.b(4, false);
    }
}
