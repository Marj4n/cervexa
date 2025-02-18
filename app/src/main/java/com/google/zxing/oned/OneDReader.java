package com.google.zxing.oned;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import com.jieli.stream.dv.running2.ui.widget.verticalseekbar.VerticalSeekBar;
import java.util.Hashtable;

/* loaded from: classes.dex */
public abstract class OneDReader implements Reader {
    protected static final int INTEGER_MATH_SHIFT = 8;
    protected static final int PATTERN_MATCH_RESULT_SCALE_FACTOR = 256;

    /* JADX WARN: Removed duplicated region for block: B:47:0x0092 A[Catch: ReaderException -> 0x00e1, TRY_LEAVE, TryCatch #0 {ReaderException -> 0x00e1, blocks: (B:45:0x008c, B:47:0x0092), top: B:44:0x008c }] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x00e0 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.google.zxing.Result doDecode(com.google.zxing.BinaryBitmap r21, java.util.Hashtable r22) throws com.google.zxing.NotFoundException {
        /*
            Method dump skipped, instructions count: 261
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.oned.OneDReader.doDecode(com.google.zxing.BinaryBitmap, java.util.Hashtable):com.google.zxing.Result");
    }

    protected static int patternMatchVariance(int[] iArr, int[] iArr2, int i) {
        int length = iArr.length;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4++) {
            i2 += iArr[i4];
            i3 += iArr2[i4];
        }
        if (i2 < i3) {
            return Integer.MAX_VALUE;
        }
        int i5 = (i2 << 8) / i3;
        int i6 = (i * i5) >> 8;
        int i7 = 0;
        for (int i8 = 0; i8 < length; i8++) {
            int i9 = iArr[i8] << 8;
            int i10 = iArr2[i8] * i5;
            int i11 = i9 > i10 ? i9 - i10 : i10 - i9;
            if (i11 > i6) {
                return Integer.MAX_VALUE;
            }
            i7 += i11;
        }
        return i7 / i2;
    }

    protected static void recordPattern(BitArray bitArray, int i, int[] iArr) throws NotFoundException {
        int length = iArr.length;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            iArr[i3] = 0;
        }
        int size = bitArray.getSize();
        if (i >= size) {
            throw NotFoundException.getNotFoundInstance();
        }
        boolean z = !bitArray.get(i);
        while (i < size) {
            if (bitArray.get(i) ^ z) {
                iArr[i2] = iArr[i2] + 1;
            } else {
                i2++;
                if (i2 == length) {
                    break;
                }
                iArr[i2] = 1;
                z = !z;
            }
            i++;
        }
        if (i2 != length) {
            if (i2 != length - 1 || i != size) {
                throw NotFoundException.getNotFoundInstance();
            }
        }
    }

    protected static void recordPatternInReverse(BitArray bitArray, int i, int[] iArr) throws NotFoundException {
        int length = iArr.length;
        boolean z = bitArray.get(i);
        while (i > 0 && length >= 0) {
            i--;
            if (bitArray.get(i) != z) {
                length--;
                z = !z;
            }
        }
        if (length >= 0) {
            throw NotFoundException.getNotFoundInstance();
        }
        recordPattern(bitArray, i + 1, iArr);
    }

    @Override // com.google.zxing.Reader
    public Result decode(BinaryBitmap binaryBitmap) throws NotFoundException, FormatException {
        return decode(binaryBitmap, null);
    }

    @Override // com.google.zxing.Reader
    public Result decode(BinaryBitmap binaryBitmap, Hashtable hashtable) throws NotFoundException, FormatException {
        try {
            return doDecode(binaryBitmap, hashtable);
        } catch (NotFoundException e) {
            if (!(hashtable != null && hashtable.containsKey(DecodeHintType.TRY_HARDER)) || !binaryBitmap.isRotateSupported()) {
                throw e;
            }
            BinaryBitmap rotateCounterClockwise = binaryBitmap.rotateCounterClockwise();
            Result doDecode = doDecode(rotateCounterClockwise, hashtable);
            Hashtable resultMetadata = doDecode.getResultMetadata();
            int i = VerticalSeekBar.ROTATION_ANGLE_CW_270;
            if (resultMetadata != null && resultMetadata.containsKey(ResultMetadataType.ORIENTATION)) {
                i = (((Integer) resultMetadata.get(ResultMetadataType.ORIENTATION)).intValue() + VerticalSeekBar.ROTATION_ANGLE_CW_270) % 360;
            }
            doDecode.putMetadata(ResultMetadataType.ORIENTATION, new Integer(i));
            ResultPoint[] resultPoints = doDecode.getResultPoints();
            int height = rotateCounterClockwise.getHeight();
            for (int i2 = 0; i2 < resultPoints.length; i2++) {
                resultPoints[i2] = new ResultPoint((height - resultPoints[i2].getY()) - 1.0f, resultPoints[i2].getX());
            }
            return doDecode;
        }
    }

    public abstract Result decodeRow(int i, BitArray bitArray, Hashtable hashtable) throws NotFoundException, ChecksumException, FormatException;

    @Override // com.google.zxing.Reader
    public void reset() {
    }
}
