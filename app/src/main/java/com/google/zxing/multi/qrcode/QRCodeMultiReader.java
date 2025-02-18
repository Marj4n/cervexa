package com.google.zxing.multi.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.multi.qrcode.detector.MultiDetector;
import com.google.zxing.qrcode.QRCodeReader;
import java.util.Hashtable;
import java.util.Vector;

/* loaded from: classes.dex */
public final class QRCodeMultiReader extends QRCodeReader implements MultipleBarcodeReader {
    private static final Result[] EMPTY_RESULT_ARRAY = new Result[0];

    @Override // com.google.zxing.multi.MultipleBarcodeReader
    public Result[] decodeMultiple(BinaryBitmap binaryBitmap) throws NotFoundException {
        return decodeMultiple(binaryBitmap, null);
    }

    @Override // com.google.zxing.multi.MultipleBarcodeReader
    public Result[] decodeMultiple(BinaryBitmap binaryBitmap, Hashtable hashtable) throws NotFoundException {
        Vector vector = new Vector();
        DetectorResult[] detectMulti = new MultiDetector(binaryBitmap.getBlackMatrix()).detectMulti(hashtable);
        for (int i = 0; i < detectMulti.length; i++) {
            try {
                DecoderResult decode = getDecoder().decode(detectMulti[i].getBits());
                Result result = new Result(decode.getText(), decode.getRawBytes(), detectMulti[i].getPoints(), BarcodeFormat.QR_CODE);
                if (decode.getByteSegments() != null) {
                    result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, decode.getByteSegments());
                }
                if (decode.getECLevel() != null) {
                    result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, decode.getECLevel().toString());
                }
                vector.addElement(result);
            } catch (ReaderException unused) {
            }
        }
        if (vector.isEmpty()) {
            return EMPTY_RESULT_ARRAY;
        }
        Result[] resultArr = new Result[vector.size()];
        for (int i2 = 0; i2 < vector.size(); i2++) {
            resultArr[i2] = (Result) vector.elementAt(i2);
        }
        return resultArr;
    }
}
