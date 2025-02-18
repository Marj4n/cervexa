package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Hashtable;

/* loaded from: classes.dex */
public class UPCAWriter implements Writer {
    private final EAN13Writer subWriter = new EAN13Writer();

    private static String preencode(String str) {
        int length = str.length();
        if (length == 11) {
            int i = 0;
            for (int i2 = 0; i2 < 11; i2++) {
                i += (str.charAt(i2) - '0') * (i2 % 2 == 0 ? 3 : 1);
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(str);
            stringBuffer.append((1000 - i) % 10);
            str = stringBuffer.toString();
        } else if (length != 12) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Requested contents should be 11 or 12 digits long, but got ");
            stringBuffer2.append(str.length());
            throw new IllegalArgumentException(stringBuffer2.toString());
        }
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append('0');
        stringBuffer3.append(str);
        return stringBuffer3.toString();
    }

    @Override // com.google.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2) throws WriterException {
        return encode(str, barcodeFormat, i, i2, null);
    }

    @Override // com.google.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Hashtable hashtable) throws WriterException {
        if (barcodeFormat == BarcodeFormat.UPC_A) {
            return this.subWriter.encode(preencode(str), BarcodeFormat.EAN_13, i, i2, hashtable);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Can only encode UPC-A, but got ");
        stringBuffer.append(barcodeFormat);
        throw new IllegalArgumentException(stringBuffer.toString());
    }
}
