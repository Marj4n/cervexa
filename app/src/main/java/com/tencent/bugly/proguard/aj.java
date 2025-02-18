package com.tencent.bugly.proguard;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class aj extends k implements Cloneable {
    private static byte[] d;
    private byte a;
    private String b;
    private byte[] c;

    @Override // com.tencent.bugly.proguard.k
    public final void a(StringBuilder sb, int i) {
    }

    public aj() {
        this.a = (byte) 0;
        this.b = "";
        this.c = null;
    }

    public aj(byte b, String str, byte[] bArr) {
        this.a = (byte) 0;
        this.b = "";
        this.c = null;
        this.a = b;
        this.b = str;
        this.c = bArr;
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(j jVar) {
        jVar.a(this.a, 0);
        jVar.a(this.b, 1);
        byte[] bArr = this.c;
        if (bArr != null) {
            jVar.a(bArr, 2);
        }
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(i iVar) {
        this.a = iVar.a(this.a, 0, true);
        this.b = iVar.b(1, true);
        if (d == null) {
            d = new byte[]{0};
        }
        this.c = iVar.c(2, false);
    }
}
