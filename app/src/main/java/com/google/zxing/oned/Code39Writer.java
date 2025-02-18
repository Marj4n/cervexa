package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Hashtable;

/* loaded from: classes.dex */
public final class Code39Writer extends UPCEANWriter {
    private static void toIntArray(int i, int[] iArr) {
        for (int i2 = 0; i2 < 9; i2++) {
            int i3 = 1;
            if (((1 << i2) & i) != 0) {
                i3 = 2;
            }
            iArr[i2] = i3;
        }
    }

    @Override // com.google.zxing.oned.UPCEANWriter, com.google.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Hashtable hashtable) throws WriterException {
        if (barcodeFormat == BarcodeFormat.CODE_39) {
            return super.encode(str, barcodeFormat, i, i2, hashtable);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Can only encode CODE_39, but got ");
        stringBuffer.append(barcodeFormat);
        throw new IllegalArgumentException(stringBuffer.toString());
    }

    @Override // com.google.zxing.oned.UPCEANWriter
    public byte[] encode(String str) {
        int length = str.length();
        if (length > 80) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Requested contents should be less than 80 digits long, but got ");
            stringBuffer.append(length);
            throw new IllegalArgumentException(stringBuffer.toString());
        }
        int[] iArr = new int[9];
        int i = length + 25;
        for (int i2 = 0; i2 < length; i2++) {
            toIntArray(Code39Reader.CHARACTER_ENCODINGS["0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".indexOf(str.charAt(i2))], iArr);
            for (int i3 = 0; i3 < 9; i3++) {
                i += iArr[i3];
            }
        }
        byte[] bArr = new byte[i];
        toIntArray(Code39Reader.CHARACTER_ENCODINGS[39], iArr);
        int appendPattern = appendPattern(bArr, 0, iArr, 1);
        int[] iArr2 = {1};
        int appendPattern2 = appendPattern + appendPattern(bArr, appendPattern, iArr2, 0);
        for (int i4 = length - 1; i4 >= 0; i4--) {
            toIntArray(Code39Reader.CHARACTER_ENCODINGS["0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".indexOf(str.charAt(i4))], iArr);
            int appendPattern3 = appendPattern2 + appendPattern(bArr, appendPattern2, iArr, 1);
            appendPattern2 = appendPattern3 + appendPattern(bArr, appendPattern3, iArr2, 0);
        }
        toIntArray(Code39Reader.CHARACTER_ENCODINGS[39], iArr);
        appendPattern(bArr, appendPattern2, iArr, 1);
        return bArr;
    }
}
