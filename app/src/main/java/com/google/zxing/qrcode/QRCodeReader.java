package com.google.zxing.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.qrcode.decoder.Decoder;
import com.google.zxing.qrcode.detector.Detector;
import java.util.Hashtable;

/* loaded from: classes.dex */
public class QRCodeReader implements Reader {
    private static final ResultPoint[] NO_POINTS = new ResultPoint[0];
    private final Decoder decoder = new Decoder();

    private static BitMatrix extractPureBits(BitMatrix bitMatrix) throws NotFoundException {
        int[] topLeftOnBit = bitMatrix.getTopLeftOnBit();
        int[] bottomRightOnBit = bitMatrix.getBottomRightOnBit();
        if (topLeftOnBit == null || bottomRightOnBit == null) {
            throw NotFoundException.getNotFoundInstance();
        }
        int moduleSize = moduleSize(topLeftOnBit, bitMatrix);
        int i = topLeftOnBit[1];
        int i2 = bottomRightOnBit[1];
        int i3 = topLeftOnBit[0];
        int i4 = ((bottomRightOnBit[0] - i3) + 1) / moduleSize;
        int i5 = ((i2 - i) + 1) / moduleSize;
        if (i4 == 0 || i5 == 0) {
            throw NotFoundException.getNotFoundInstance();
        }
        if (i5 != i4) {
            throw NotFoundException.getNotFoundInstance();
        }
        int i6 = moduleSize >> 1;
        int i7 = i + i6;
        int i8 = i3 + i6;
        BitMatrix bitMatrix2 = new BitMatrix(i4, i5);
        for (int i9 = 0; i9 < i5; i9++) {
            int i10 = (i9 * moduleSize) + i7;
            for (int i11 = 0; i11 < i4; i11++) {
                if (bitMatrix.get((i11 * moduleSize) + i8, i10)) {
                    bitMatrix2.set(i11, i9);
                }
            }
        }
        return bitMatrix2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0021, code lost:
    
        r3 = r3 - r6[0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0024, code lost:
    
        if (r3 == 0) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0026, code lost:
    
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x002b, code lost:
    
        throw com.google.zxing.NotFoundException.getNotFoundInstance();
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x001f, code lost:
    
        if (r4 == r0) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static int moduleSize(int[] r6, com.google.zxing.common.BitMatrix r7) throws com.google.zxing.NotFoundException {
        /*
            int r0 = r7.getHeight()
            int r1 = r7.getWidth()
            r2 = 0
            r3 = r6[r2]
            r4 = 1
            r4 = r6[r4]
        Le:
            if (r3 >= r1) goto L1d
            if (r4 >= r0) goto L1d
            boolean r5 = r7.get(r3, r4)
            if (r5 == 0) goto L1d
            int r3 = r3 + 1
            int r4 = r4 + 1
            goto Le
        L1d:
            if (r3 == r1) goto L2c
            if (r4 == r0) goto L2c
            r6 = r6[r2]
            int r3 = r3 - r6
            if (r3 == 0) goto L27
            return r3
        L27:
            com.google.zxing.NotFoundException r6 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r6
        L2c:
            com.google.zxing.NotFoundException r6 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.qrcode.QRCodeReader.moduleSize(int[], com.google.zxing.common.BitMatrix):int");
    }

    @Override // com.google.zxing.Reader
    public Result decode(BinaryBitmap binaryBitmap) throws NotFoundException, ChecksumException, FormatException {
        return decode(binaryBitmap, null);
    }

    @Override // com.google.zxing.Reader
    public Result decode(BinaryBitmap binaryBitmap, Hashtable hashtable) throws NotFoundException, ChecksumException, FormatException {
        ResultPoint[] points;
        DecoderResult decoderResult;
        if (hashtable == null || !hashtable.containsKey(DecodeHintType.PURE_BARCODE)) {
            DetectorResult detect = new Detector(binaryBitmap.getBlackMatrix()).detect(hashtable);
            DecoderResult decode = this.decoder.decode(detect.getBits(), hashtable);
            points = detect.getPoints();
            decoderResult = decode;
        } else {
            decoderResult = this.decoder.decode(extractPureBits(binaryBitmap.getBlackMatrix()), hashtable);
            points = NO_POINTS;
        }
        Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), points, BarcodeFormat.QR_CODE);
        if (decoderResult.getByteSegments() != null) {
            result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, decoderResult.getByteSegments());
        }
        if (decoderResult.getECLevel() != null) {
            result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, decoderResult.getECLevel().toString());
        }
        return result;
    }

    protected Decoder getDecoder() {
        return this.decoder;
    }

    @Override // com.google.zxing.Reader
    public void reset() {
    }
}
