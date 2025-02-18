package com.jieli.lib.dv.control.model;

/* loaded from: classes.dex */
public class CtpInfo {
    private byte[] a;
    private byte[] b;

    public byte[] getTopic() {
        return this.a;
    }

    public void setTopic(byte[] bArr) {
        this.a = bArr;
    }

    public byte[] getPayload() {
        return this.b;
    }

    public void setPayload(byte[] bArr) {
        this.b = bArr;
    }

    public String toString() {
        return "[topic:" + new String(this.a) + ", payload:" + new String(this.b) + "]";
    }
}
