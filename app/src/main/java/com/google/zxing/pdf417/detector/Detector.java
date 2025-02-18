package com.google.zxing.pdf417.detector;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.GridSampler;
import java.util.Hashtable;

/* loaded from: classes.dex */
public final class Detector {
    private static final int MAX_AVG_VARIANCE = 107;
    private static final int MAX_INDIVIDUAL_VARIANCE = 204;
    private static final int SKEW_THRESHOLD = 2;
    private static final int[] START_PATTERN = {8, 1, 1, 1, 1, 1, 1, 3};
    private static final int[] START_PATTERN_REVERSE = {3, 1, 1, 1, 1, 1, 1, 8};
    private static final int[] STOP_PATTERN = {7, 1, 1, 3, 1, 1, 1, 2, 1};
    private static final int[] STOP_PATTERN_REVERSE = {1, 2, 1, 1, 1, 3, 1, 1, 7};
    private final BinaryBitmap image;

    public Detector(BinaryBitmap binaryBitmap) {
        this.image = binaryBitmap;
    }

    private static int computeDimension(ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, ResultPoint resultPoint4, float f) {
        return ((((round(ResultPoint.distance(resultPoint, resultPoint2) / f) + round(ResultPoint.distance(resultPoint3, resultPoint4) / f)) >> 1) + 8) / 17) * 17;
    }

    private static float computeModuleWidth(ResultPoint[] resultPointArr) {
        return (((ResultPoint.distance(resultPointArr[0], resultPointArr[4]) + ResultPoint.distance(resultPointArr[1], resultPointArr[5])) / 34.0f) + ((ResultPoint.distance(resultPointArr[6], resultPointArr[2]) + ResultPoint.distance(resultPointArr[7], resultPointArr[3])) / 36.0f)) / 2.0f;
    }

    private static void correctCodeWordVertices(ResultPoint[] resultPointArr, boolean z) {
        float y = resultPointArr[4].getY() - resultPointArr[6].getY();
        if (z) {
            y = -y;
        }
        if (y > 2.0f) {
            float x = resultPointArr[4].getX() - resultPointArr[0].getX();
            resultPointArr[4] = new ResultPoint(resultPointArr[4].getX(), resultPointArr[4].getY() + ((x * (resultPointArr[6].getY() - resultPointArr[0].getY())) / (resultPointArr[6].getX() - resultPointArr[0].getX())));
        } else if ((-y) > 2.0f) {
            float x2 = resultPointArr[2].getX() - resultPointArr[6].getX();
            resultPointArr[6] = new ResultPoint(resultPointArr[6].getX(), resultPointArr[6].getY() - ((x2 * (resultPointArr[2].getY() - resultPointArr[4].getY())) / (resultPointArr[2].getX() - resultPointArr[4].getX())));
        }
        float y2 = resultPointArr[7].getY() - resultPointArr[5].getY();
        if (z) {
            y2 = -y2;
        }
        if (y2 > 2.0f) {
            float x3 = resultPointArr[5].getX() - resultPointArr[1].getX();
            resultPointArr[5] = new ResultPoint(resultPointArr[5].getX(), resultPointArr[5].getY() + ((x3 * (resultPointArr[7].getY() - resultPointArr[1].getY())) / (resultPointArr[7].getX() - resultPointArr[1].getX())));
            return;
        }
        if ((-y2) > 2.0f) {
            float x4 = resultPointArr[3].getX() - resultPointArr[7].getX();
            resultPointArr[7] = new ResultPoint(resultPointArr[7].getX(), resultPointArr[7].getY() - ((x4 * (resultPointArr[3].getY() - resultPointArr[5].getY())) / (resultPointArr[3].getX() - resultPointArr[5].getX())));
        }
    }

    private static int[] findGuardPattern(BitMatrix bitMatrix, int i, int i2, int i3, boolean z, int[] iArr) {
        int length = iArr.length;
        int[] iArr2 = new int[length];
        int i4 = i;
        int i5 = i4;
        boolean z2 = z;
        int i6 = 0;
        while (i4 < i + i3) {
            if (bitMatrix.get(i4, i2) ^ z2) {
                iArr2[i6] = iArr2[i6] + 1;
            } else {
                int i7 = length - 1;
                if (i6 == i7) {
                    if (patternMatchVariance(iArr2, iArr, MAX_INDIVIDUAL_VARIANCE) < 107) {
                        return new int[]{i5, i4};
                    }
                    i5 += iArr2[0] + iArr2[1];
                    for (int i8 = 2; i8 < length; i8++) {
                        iArr2[i8 - 2] = iArr2[i8];
                    }
                    iArr2[length - 2] = 0;
                    iArr2[i7] = 0;
                    i6--;
                } else {
                    i6++;
                }
                iArr2[i6] = 1;
                z2 = !z2;
            }
            i4++;
        }
        return null;
    }

    private static ResultPoint[] findVertices(BitMatrix bitMatrix) {
        boolean z;
        int height = bitMatrix.getHeight();
        int width = bitMatrix.getWidth();
        ResultPoint[] resultPointArr = new ResultPoint[8];
        boolean z2 = false;
        int i = 0;
        while (true) {
            if (i >= height) {
                z = false;
                break;
            }
            if (findGuardPattern(bitMatrix, 0, i, width, false, START_PATTERN) != null) {
                float f = i;
                resultPointArr[0] = new ResultPoint(r1[0], f);
                resultPointArr[4] = new ResultPoint(r1[1], f);
                z = true;
                break;
            }
            i++;
        }
        if (z) {
            int i2 = height - 1;
            while (true) {
                if (i2 <= 0) {
                    z = false;
                    break;
                }
                if (findGuardPattern(bitMatrix, 0, i2, width, false, START_PATTERN) != null) {
                    float f2 = i2;
                    resultPointArr[1] = new ResultPoint(r1[0], f2);
                    resultPointArr[5] = new ResultPoint(r1[1], f2);
                    z = true;
                    break;
                }
                i2--;
            }
        }
        if (z) {
            int i3 = 0;
            while (true) {
                if (i3 >= height) {
                    z = false;
                    break;
                }
                if (findGuardPattern(bitMatrix, 0, i3, width, false, STOP_PATTERN) != null) {
                    float f3 = i3;
                    resultPointArr[2] = new ResultPoint(r1[1], f3);
                    resultPointArr[6] = new ResultPoint(r1[0], f3);
                    z = true;
                    break;
                }
                i3++;
            }
        }
        if (z) {
            int i4 = height - 1;
            while (true) {
                if (i4 <= 0) {
                    break;
                }
                if (findGuardPattern(bitMatrix, 0, i4, width, false, STOP_PATTERN) != null) {
                    float f4 = i4;
                    resultPointArr[3] = new ResultPoint(r1[1], f4);
                    resultPointArr[7] = new ResultPoint(r1[0], f4);
                    z2 = true;
                    break;
                }
                i4--;
            }
        } else {
            z2 = z;
        }
        if (z2) {
            return resultPointArr;
        }
        return null;
    }

    private static ResultPoint[] findVertices180(BitMatrix bitMatrix) {
        boolean z;
        int height = bitMatrix.getHeight();
        boolean z2 = true;
        int width = bitMatrix.getWidth() >> 1;
        ResultPoint[] resultPointArr = new ResultPoint[8];
        int i = height - 1;
        int i2 = i;
        while (true) {
            if (i2 <= 0) {
                z = false;
                break;
            }
            if (findGuardPattern(bitMatrix, width, i2, width, true, START_PATTERN_REVERSE) != null) {
                float f = i2;
                resultPointArr[0] = new ResultPoint(r3[1], f);
                resultPointArr[4] = new ResultPoint(r3[0], f);
                z = true;
                break;
            }
            i2--;
        }
        if (z) {
            int i3 = 0;
            while (true) {
                if (i3 >= height) {
                    z = false;
                    break;
                }
                if (findGuardPattern(bitMatrix, width, i3, width, true, START_PATTERN_REVERSE) != null) {
                    float f2 = i3;
                    resultPointArr[1] = new ResultPoint(r3[1], f2);
                    resultPointArr[5] = new ResultPoint(r3[0], f2);
                    z = true;
                    break;
                }
                i3++;
            }
        }
        if (z) {
            while (true) {
                if (i <= 0) {
                    z = false;
                    break;
                }
                if (findGuardPattern(bitMatrix, 0, i, width, false, STOP_PATTERN_REVERSE) != null) {
                    float f3 = i;
                    resultPointArr[2] = new ResultPoint(r3[0], f3);
                    resultPointArr[6] = new ResultPoint(r3[1], f3);
                    z = true;
                    break;
                }
                i--;
            }
        }
        if (z) {
            int i4 = 0;
            while (true) {
                if (i4 >= height) {
                    z2 = false;
                    break;
                }
                if (findGuardPattern(bitMatrix, 0, i4, width, false, STOP_PATTERN_REVERSE) != null) {
                    float f4 = i4;
                    resultPointArr[3] = new ResultPoint(r3[0], f4);
                    resultPointArr[7] = new ResultPoint(r3[1], f4);
                    break;
                }
                i4++;
            }
        } else {
            z2 = z;
        }
        if (z2) {
            return resultPointArr;
        }
        return null;
    }

    private static int patternMatchVariance(int[] iArr, int[] iArr2, int i) {
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

    private static int round(float f) {
        return (int) (f + 0.5f);
    }

    private static BitMatrix sampleGrid(BitMatrix bitMatrix, ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, ResultPoint resultPoint4, int i) throws NotFoundException {
        float f = i;
        return GridSampler.getInstance().sampleGrid(bitMatrix, i, i, 0.0f, 0.0f, f, 0.0f, f, f, 0.0f, f, resultPoint.getX(), resultPoint.getY(), resultPoint3.getX(), resultPoint3.getY(), resultPoint4.getX(), resultPoint4.getY(), resultPoint2.getX(), resultPoint2.getY());
    }

    public DetectorResult detect() throws NotFoundException {
        return detect(null);
    }

    public DetectorResult detect(Hashtable hashtable) throws NotFoundException {
        BitMatrix blackMatrix = this.image.getBlackMatrix();
        ResultPoint[] findVertices = findVertices(blackMatrix);
        if (findVertices == null) {
            findVertices = findVertices180(blackMatrix);
            if (findVertices != null) {
                correctCodeWordVertices(findVertices, true);
            }
        } else {
            correctCodeWordVertices(findVertices, false);
        }
        if (findVertices == null) {
            throw NotFoundException.getNotFoundInstance();
        }
        float computeModuleWidth = computeModuleWidth(findVertices);
        if (computeModuleWidth < 1.0f) {
            throw NotFoundException.getNotFoundInstance();
        }
        int computeDimension = computeDimension(findVertices[4], findVertices[6], findVertices[5], findVertices[7], computeModuleWidth);
        if (computeDimension >= 1) {
            return new DetectorResult(sampleGrid(blackMatrix, findVertices[4], findVertices[5], findVertices[6], findVertices[7], computeDimension), new ResultPoint[]{findVertices[4], findVertices[5], findVertices[6], findVertices[7]});
        }
        throw NotFoundException.getNotFoundInstance();
    }
}
