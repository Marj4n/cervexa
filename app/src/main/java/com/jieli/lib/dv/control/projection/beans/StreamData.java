package com.jieli.lib.dv.control.projection.beans;

import android.text.TextUtils;

/* loaded from: classes.dex */
public class StreamData {
    private int a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private byte[] h;

    public int getType() {
        return this.a;
    }

    public void setType(int i) {
        this.a = i;
    }

    public int getSave() {
        return this.b;
    }

    public void setSave(int i) {
        this.b = i;
    }

    public int getPayloadLen() {
        return this.c;
    }

    public void setPayloadLen(int i) {
        this.c = i;
    }

    public int getSeq() {
        return this.d;
    }

    public void setSeq(int i) {
        this.d = i;
    }

    public int getFrameSize() {
        return this.e;
    }

    public void setFrameSize(int i) {
        this.e = i;
    }

    public int getOffset() {
        return this.f;
    }

    public void setOffset(int i) {
        this.f = i;
    }

    public int getDateFlag() {
        return this.g;
    }

    public void setDateFlag(int i) {
        this.g = i;
    }

    public byte[] getPayload() {
        return this.h;
    }

    public void setPayload(byte[] bArr) {
        this.h = bArr;
    }

    public String toString() {
        String str = "{ \"type\": " + this.a + ", \n\"save\": " + this.b + ", \n\"payloadLen\": " + this.c + ", \n\"frameSize\": " + this.e + ", \n\"offset\": " + this.f + ", \n\"dateFlag\": " + this.g;
        if (this.h != null) {
            String str2 = new String(this.h);
            if (!TextUtils.isEmpty(str2)) {
                str = str + ", \n\"payload\": \"" + str2 + "\"";
            }
        }
        return str + "}";
    }
}
