package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/* loaded from: classes.dex */
public final class Code128Writer extends UPCEANWriter {
    private static final int CODE_CODE_B = 100;
    private static final int CODE_CODE_C = 99;
    private static final int CODE_START_B = 104;
    private static final int CODE_START_C = 105;
    private static final int CODE_STOP = 106;

    private static boolean isDigits(String str, int i, int i2) {
        int i3 = i2 + i;
        while (i < i3) {
            char charAt = str.charAt(i);
            if (charAt < '0' || charAt > '9') {
                return false;
            }
            i++;
        }
        return true;
    }

    @Override // com.google.zxing.oned.UPCEANWriter, com.google.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Hashtable hashtable) throws WriterException {
        if (barcodeFormat == BarcodeFormat.CODE_128) {
            return super.encode(str, barcodeFormat, i, i2, hashtable);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Can only encode CODE_128, but got ");
        stringBuffer.append(barcodeFormat);
        throw new IllegalArgumentException(stringBuffer.toString());
    }

    @Override // com.google.zxing.oned.UPCEANWriter
    public byte[] encode(String str) {
        int i;
        int length = str.length();
        if (length < 1 || length > 80) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Contents length should be between 1 and 80 characters, but got ");
            stringBuffer.append(length);
            throw new IllegalArgumentException(stringBuffer.toString());
        }
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            char charAt = str.charAt(i3);
            if (charAt < ' ' || charAt > '~') {
                throw new IllegalArgumentException("Contents should only contain characters between ' ' and '~'");
            }
        }
        Vector vector = new Vector();
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 1;
        while (i4 < length) {
            int i8 = i6 == 99 ? 2 : 4;
            int i9 = (length - i4 < i8 || !isDigits(str, i4, i8)) ? 100 : 99;
            if (i9 != i6) {
                int i10 = i9;
                i = i6 == 0 ? i9 == 100 ? 104 : 105 : i9;
                i6 = i10;
            } else if (i6 == 100) {
                i = str.charAt(i4) - ' ';
                i4++;
            } else {
                int i11 = i4 + 2;
                i = Integer.parseInt(str.substring(i4, i11));
                i4 = i11;
            }
            vector.addElement(Code128Reader.CODE_PATTERNS[i]);
            i5 += i * i7;
            if (i4 != 0) {
                i7++;
            }
        }
        vector.addElement(Code128Reader.CODE_PATTERNS[i5 % 103]);
        vector.addElement(Code128Reader.CODE_PATTERNS[106]);
        Enumeration elements = vector.elements();
        int i12 = 0;
        while (elements.hasMoreElements()) {
            for (int i13 : (int[]) elements.nextElement()) {
                i12 += i13;
            }
        }
        byte[] bArr = new byte[i12];
        Enumeration elements2 = vector.elements();
        while (elements2.hasMoreElements()) {
            i2 += appendPattern(bArr, i2, (int[]) elements2.nextElement(), 1);
        }
        return bArr;
    }
}
