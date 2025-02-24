package com.google.zxing.client.result.optional;

import kotlin.UByte;

/* loaded from: classes.dex */
public final class NDEFRecord {
    public static final String ACTION_WELL_KNOWN_TYPE = "act";
    public static final String SMART_POSTER_WELL_KNOWN_TYPE = "Sp";
    private static final int SUPPORTED_HEADER = 17;
    private static final int SUPPORTED_HEADER_MASK = 63;
    public static final String TEXT_WELL_KNOWN_TYPE = "T";
    public static final String URI_WELL_KNOWN_TYPE = "U";
    private final int header;
    private final byte[] payload;
    private final int totalRecordLength;
    private final String type;

    private NDEFRecord(int i, String str, byte[] bArr, int i2) {
        this.header = i;
        this.type = str;
        this.payload = bArr;
        this.totalRecordLength = i2;
    }

    static NDEFRecord readRecord(byte[] bArr, int i) {
        int i2 = bArr[i] & UByte.MAX_VALUE;
        if (((i2 ^ 17) & 63) != 0) {
            return null;
        }
        int i3 = bArr[i + 1] & 255;
        int i4 = bArr[i + 2] & 255;
        int i5 = i + 3;
        String bytesToString = AbstractNDEFResultParser.bytesToString(bArr, i5, i3, "US-ASCII");
        byte[] bArr2 = new byte[i4];
        System.arraycopy(bArr, i5 + i3, bArr2, 0, i4);
        return new NDEFRecord(i2, bytesToString, bArr2, i3 + 3 + i4);
    }

    byte[] getPayload() {
        return this.payload;
    }

    int getTotalRecordLength() {
        return this.totalRecordLength;
    }

    String getType() {
        return this.type;
    }

    boolean isMessageBegin() {
        return (this.header & 128) != 0;
    }

    boolean isMessageEnd() {
        return (this.header & 64) != 0;
    }
}
