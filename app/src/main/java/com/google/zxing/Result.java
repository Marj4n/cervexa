package com.google.zxing;

import java.util.Enumeration;
import java.util.Hashtable;

/* loaded from: classes.dex */
public final class Result {
    private final BarcodeFormat format;
    private final byte[] rawBytes;
    private Hashtable resultMetadata;
    private ResultPoint[] resultPoints;
    private final String text;
    private final long timestamp;

    public Result(String str, byte[] bArr, ResultPoint[] resultPointArr, BarcodeFormat barcodeFormat) {
        this(str, bArr, resultPointArr, barcodeFormat, System.currentTimeMillis());
    }

    public Result(String str, byte[] bArr, ResultPoint[] resultPointArr, BarcodeFormat barcodeFormat, long j) {
        if (str == null && bArr == null) {
            throw new IllegalArgumentException("Text and bytes are null");
        }
        this.text = str;
        this.rawBytes = bArr;
        this.resultPoints = resultPointArr;
        this.format = barcodeFormat;
        this.resultMetadata = null;
        this.timestamp = j;
    }

    public void addResultPoints(ResultPoint[] resultPointArr) {
        ResultPoint[] resultPointArr2 = this.resultPoints;
        if (resultPointArr2 == null) {
            this.resultPoints = resultPointArr;
            return;
        }
        if (resultPointArr == null || resultPointArr.length <= 0) {
            return;
        }
        ResultPoint[] resultPointArr3 = new ResultPoint[resultPointArr2.length + resultPointArr.length];
        System.arraycopy(resultPointArr2, 0, resultPointArr3, 0, resultPointArr2.length);
        System.arraycopy(resultPointArr, 0, resultPointArr3, this.resultPoints.length, resultPointArr.length);
        this.resultPoints = resultPointArr3;
    }

    public BarcodeFormat getBarcodeFormat() {
        return this.format;
    }

    public byte[] getRawBytes() {
        return this.rawBytes;
    }

    public Hashtable getResultMetadata() {
        return this.resultMetadata;
    }

    public ResultPoint[] getResultPoints() {
        return this.resultPoints;
    }

    public String getText() {
        return this.text;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void putAllMetadata(Hashtable hashtable) {
        if (hashtable != null) {
            if (this.resultMetadata == null) {
                this.resultMetadata = hashtable;
                return;
            }
            Enumeration keys = hashtable.keys();
            while (keys.hasMoreElements()) {
                ResultMetadataType resultMetadataType = (ResultMetadataType) keys.nextElement();
                this.resultMetadata.put(resultMetadataType, hashtable.get(resultMetadataType));
            }
        }
    }

    public void putMetadata(ResultMetadataType resultMetadataType, Object obj) {
        if (this.resultMetadata == null) {
            this.resultMetadata = new Hashtable(3);
        }
        this.resultMetadata.put(resultMetadataType, obj);
    }

    public String toString() {
        String str = this.text;
        if (str != null) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        stringBuffer.append(this.rawBytes.length);
        stringBuffer.append(" bytes]");
        return stringBuffer.toString();
    }
}
