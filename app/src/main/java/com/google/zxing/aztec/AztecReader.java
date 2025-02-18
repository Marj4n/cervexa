package com.google.zxing.aztec;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.aztec.decoder.Decoder;
import com.google.zxing.aztec.detector.Detector;
import com.google.zxing.common.DecoderResult;
import java.util.Hashtable;

/* loaded from: classes.dex */
public final class AztecReader implements Reader {
    @Override // com.google.zxing.Reader
    public Result decode(BinaryBitmap binaryBitmap) throws NotFoundException, FormatException {
        return decode(binaryBitmap, null);
    }

    @Override // com.google.zxing.Reader
    public Result decode(BinaryBitmap binaryBitmap, Hashtable hashtable) throws NotFoundException, FormatException {
        ResultPointCallback resultPointCallback;
        AztecDetectorResult detect = new Detector(binaryBitmap.getBlackMatrix()).detect();
        ResultPoint[] points = detect.getPoints();
        if (hashtable != null && detect.getPoints() != null && (resultPointCallback = (ResultPointCallback) hashtable.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK)) != null) {
            for (int i = 0; i < detect.getPoints().length; i++) {
                resultPointCallback.foundPossibleResultPoint(detect.getPoints()[i]);
            }
        }
        DecoderResult decode = new Decoder().decode(detect);
        Result result = new Result(decode.getText(), decode.getRawBytes(), points, BarcodeFormat.AZTEC);
        if (decode.getByteSegments() != null) {
            result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, decode.getByteSegments());
        }
        if (decode.getECLevel() != null) {
            result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, decode.getECLevel().toString());
        }
        return result;
    }

    @Override // com.google.zxing.Reader
    public void reset() {
    }
}
