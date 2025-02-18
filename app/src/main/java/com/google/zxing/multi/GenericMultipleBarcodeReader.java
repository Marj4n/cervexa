package com.google.zxing.multi;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import java.util.Hashtable;
import java.util.Vector;

/* loaded from: classes.dex */
public final class GenericMultipleBarcodeReader implements MultipleBarcodeReader {
    private static final int MIN_DIMENSION_TO_RECUR = 100;
    private final Reader delegate;

    public GenericMultipleBarcodeReader(Reader reader) {
        this.delegate = reader;
    }

    private void doDecodeMultiple(BinaryBitmap binaryBitmap, Hashtable hashtable, Vector vector, int i, int i2) {
        boolean z;
        float f;
        float f2;
        try {
            Result decode = this.delegate.decode(binaryBitmap, hashtable);
            int i3 = 0;
            while (true) {
                if (i3 >= vector.size()) {
                    z = false;
                    break;
                } else {
                    if (((Result) vector.elementAt(i3)).getText().equals(decode.getText())) {
                        z = true;
                        break;
                    }
                    i3++;
                }
            }
            if (z) {
                return;
            }
            vector.addElement(translateResultPoints(decode, i, i2));
            ResultPoint[] resultPoints = decode.getResultPoints();
            if (resultPoints == null || resultPoints.length == 0) {
                return;
            }
            int width = binaryBitmap.getWidth();
            int height = binaryBitmap.getHeight();
            float f3 = width;
            float f4 = height;
            float f5 = 0.0f;
            float f6 = 0.0f;
            for (ResultPoint resultPoint : resultPoints) {
                float x = resultPoint.getX();
                float y = resultPoint.getY();
                if (x < f3) {
                    f3 = x;
                }
                if (y < f4) {
                    f4 = y;
                }
                if (x > f6) {
                    f6 = x;
                }
                if (y > f5) {
                    f5 = y;
                }
            }
            if (f3 > 100.0f) {
                f = f5;
                f2 = f6;
                doDecodeMultiple(binaryBitmap.crop(0, 0, (int) f3, height), hashtable, vector, i, i2);
            } else {
                f = f5;
                f2 = f6;
            }
            if (f4 > 100.0f) {
                doDecodeMultiple(binaryBitmap.crop(0, 0, width, (int) f4), hashtable, vector, i, i2);
            }
            float f7 = f2;
            if (f7 < width - 100) {
                int i4 = (int) f7;
                doDecodeMultiple(binaryBitmap.crop(i4, 0, width - i4, height), hashtable, vector, i + i4, i2);
            }
            float f8 = f;
            if (f8 < height - 100) {
                int i5 = (int) f8;
                doDecodeMultiple(binaryBitmap.crop(0, i5, width, height - i5), hashtable, vector, i, i2 + i5);
            }
        } catch (ReaderException unused) {
        }
    }

    private static Result translateResultPoints(Result result, int i, int i2) {
        ResultPoint[] resultPoints = result.getResultPoints();
        ResultPoint[] resultPointArr = new ResultPoint[resultPoints.length];
        for (int i3 = 0; i3 < resultPoints.length; i3++) {
            ResultPoint resultPoint = resultPoints[i3];
            resultPointArr[i3] = new ResultPoint(resultPoint.getX() + i, resultPoint.getY() + i2);
        }
        return new Result(result.getText(), result.getRawBytes(), resultPointArr, result.getBarcodeFormat());
    }

    @Override // com.google.zxing.multi.MultipleBarcodeReader
    public Result[] decodeMultiple(BinaryBitmap binaryBitmap) throws NotFoundException {
        return decodeMultiple(binaryBitmap, null);
    }

    @Override // com.google.zxing.multi.MultipleBarcodeReader
    public Result[] decodeMultiple(BinaryBitmap binaryBitmap, Hashtable hashtable) throws NotFoundException {
        Vector vector = new Vector();
        doDecodeMultiple(binaryBitmap, hashtable, vector, 0, 0);
        if (vector.isEmpty()) {
            throw NotFoundException.getNotFoundInstance();
        }
        int size = vector.size();
        Result[] resultArr = new Result[size];
        for (int i = 0; i < size; i++) {
            resultArr[i] = (Result) vector.elementAt(i);
        }
        return resultArr;
    }
}
