package com.google.zxing;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.EAN8Writer;
import com.google.zxing.oned.ITFWriter;
import com.google.zxing.oned.UPCAWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import java.util.Hashtable;

/* loaded from: classes.dex */
public final class MultiFormatWriter implements Writer {
    @Override // com.google.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2) throws WriterException {
        return encode(str, barcodeFormat, i, i2, null);
    }

    @Override // com.google.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Hashtable hashtable) throws WriterException {
        Writer iTFWriter;
        if (barcodeFormat == BarcodeFormat.EAN_8) {
            iTFWriter = new EAN8Writer();
        } else if (barcodeFormat == BarcodeFormat.EAN_13) {
            iTFWriter = new EAN13Writer();
        } else if (barcodeFormat == BarcodeFormat.UPC_A) {
            iTFWriter = new UPCAWriter();
        } else if (barcodeFormat == BarcodeFormat.QR_CODE) {
            iTFWriter = new QRCodeWriter();
        } else if (barcodeFormat == BarcodeFormat.CODE_39) {
            iTFWriter = new Code39Writer();
        } else if (barcodeFormat == BarcodeFormat.CODE_128) {
            iTFWriter = new Code128Writer();
        } else {
            if (barcodeFormat != BarcodeFormat.ITF) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("No encoder available for format ");
                stringBuffer.append(barcodeFormat);
                throw new IllegalArgumentException(stringBuffer.toString());
            }
            iTFWriter = new ITFWriter();
        }
        return iTFWriter.encode(str, barcodeFormat, i, i2, hashtable);
    }
}
