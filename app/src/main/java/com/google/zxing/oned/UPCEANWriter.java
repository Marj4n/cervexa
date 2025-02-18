package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Hashtable;

/* loaded from: classes.dex */
public abstract class UPCEANWriter implements Writer {
    protected static int appendPattern(byte[] bArr, int i, int[] iArr, int i2) {
        if (i2 != 0 && i2 != 1) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("startColor must be either 0 or 1, but got: ");
            stringBuffer.append(i2);
            throw new IllegalArgumentException(stringBuffer.toString());
        }
        byte b = (byte) i2;
        int i3 = 0;
        for (int i4 : iArr) {
            for (int i5 = 0; i5 < i4; i5++) {
                bArr[i] = b;
                i++;
                i3++;
            }
            b = (byte) (b ^ 1);
        }
        return i3;
    }

    private static BitMatrix renderResult(byte[] bArr, int i, int i2) {
        int length = bArr.length;
        int length2 = (UPCEANReader.START_END_PATTERN.length << 1) + length;
        int max = Math.max(i, length2);
        int max2 = Math.max(1, i2);
        int i3 = max / length2;
        int i4 = (max - (length * i3)) / 2;
        BitMatrix bitMatrix = new BitMatrix(max, max2);
        int i5 = 0;
        while (i5 < length) {
            if (bArr[i5] == 1) {
                bitMatrix.setRegion(i4, 0, i3, max2);
            }
            i5++;
            i4 += i3;
        }
        return bitMatrix;
    }

    @Override // com.google.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2) throws WriterException {
        return encode(str, barcodeFormat, i, i2, null);
    }

    @Override // com.google.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Hashtable hashtable) throws WriterException {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("Found empty contents");
        }
        if (i >= 0 && i2 >= 0) {
            return renderResult(encode(str), i, i2);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Requested dimensions are too small: ");
        stringBuffer.append(i);
        stringBuffer.append('x');
        stringBuffer.append(i2);
        throw new IllegalArgumentException(stringBuffer.toString());
    }

    public abstract byte[] encode(String str);
}
