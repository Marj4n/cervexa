package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Hashtable;

/* loaded from: classes.dex */
public final class EAN8Writer extends UPCEANWriter {
    private static final int codeWidth = 67;

    @Override // com.google.zxing.oned.UPCEANWriter, com.google.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Hashtable hashtable) throws WriterException {
        if (barcodeFormat == BarcodeFormat.EAN_8) {
            return super.encode(str, barcodeFormat, i, i2, hashtable);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Can only encode EAN_8, but got ");
        stringBuffer.append(barcodeFormat);
        throw new IllegalArgumentException(stringBuffer.toString());
    }

    @Override // com.google.zxing.oned.UPCEANWriter
    public byte[] encode(String str) {
        if (str.length() != 8) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Requested contents should be 8 digits long, but got ");
            stringBuffer.append(str.length());
            throw new IllegalArgumentException(stringBuffer.toString());
        }
        byte[] bArr = new byte[67];
        int appendPattern = appendPattern(bArr, 0, UPCEANReader.START_END_PATTERN, 1) + 0;
        int i = 0;
        while (i <= 3) {
            int i2 = i + 1;
            appendPattern += appendPattern(bArr, appendPattern, UPCEANReader.L_PATTERNS[Integer.parseInt(str.substring(i, i2))], 0);
            i = i2;
        }
        int appendPattern2 = appendPattern + appendPattern(bArr, appendPattern, UPCEANReader.MIDDLE_PATTERN, 0);
        int i3 = 4;
        while (i3 <= 7) {
            int i4 = i3 + 1;
            appendPattern2 += appendPattern(bArr, appendPattern2, UPCEANReader.L_PATTERNS[Integer.parseInt(str.substring(i3, i4))], 1);
            i3 = i4;
        }
        appendPattern(bArr, appendPattern2, UPCEANReader.START_END_PATTERN, 1);
        return bArr;
    }
}
