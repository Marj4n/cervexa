package com.google.zxing.common;

import java.util.Vector;

/* loaded from: classes.dex */
public final class DecoderResult {
    private final Vector byteSegments;
    private final String ecLevel;
    private final byte[] rawBytes;
    private final String text;

    public DecoderResult(byte[] bArr, String str, Vector vector, String str2) {
        if (bArr == null && str == null) {
            throw new IllegalArgumentException();
        }
        this.rawBytes = bArr;
        this.text = str;
        this.byteSegments = vector;
        this.ecLevel = str2;
    }

    public Vector getByteSegments() {
        return this.byteSegments;
    }

    public String getECLevel() {
        return this.ecLevel;
    }

    public byte[] getRawBytes() {
        return this.rawBytes;
    }

    public String getText() {
        return this.text;
    }
}
