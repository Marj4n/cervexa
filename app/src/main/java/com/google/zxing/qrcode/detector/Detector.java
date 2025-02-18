package com.google.zxing.qrcode.detector;

import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.GridSampler;
import com.google.zxing.common.PerspectiveTransform;
import com.google.zxing.qrcode.decoder.Version;
import java.util.Hashtable;

/* loaded from: classes.dex */
public class Detector {
    private final BitMatrix image;
    private ResultPointCallback resultPointCallback;

    public Detector(BitMatrix bitMatrix) {
        this.image = bitMatrix;
    }

    private float calculateModuleSizeOneWay(ResultPoint resultPoint, ResultPoint resultPoint2) {
        float sizeOfBlackWhiteBlackRunBothWays = sizeOfBlackWhiteBlackRunBothWays((int) resultPoint.getX(), (int) resultPoint.getY(), (int) resultPoint2.getX(), (int) resultPoint2.getY());
        float sizeOfBlackWhiteBlackRunBothWays2 = sizeOfBlackWhiteBlackRunBothWays((int) resultPoint2.getX(), (int) resultPoint2.getY(), (int) resultPoint.getX(), (int) resultPoint.getY());
        return Float.isNaN(sizeOfBlackWhiteBlackRunBothWays) ? sizeOfBlackWhiteBlackRunBothWays2 / 7.0f : Float.isNaN(sizeOfBlackWhiteBlackRunBothWays2) ? sizeOfBlackWhiteBlackRunBothWays / 7.0f : (sizeOfBlackWhiteBlackRunBothWays + sizeOfBlackWhiteBlackRunBothWays2) / 14.0f;
    }

    protected static int computeDimension(ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, float f) throws NotFoundException {
        int round = ((round(ResultPoint.distance(resultPoint, resultPoint2) / f) + round(ResultPoint.distance(resultPoint, resultPoint3) / f)) >> 1) + 7;
        int i = round & 3;
        if (i == 0) {
            return round + 1;
        }
        if (i == 2) {
            return round - 1;
        }
        if (i != 3) {
            return round;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    public static PerspectiveTransform createTransform(ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, ResultPoint resultPoint4, int i) {
        float x;
        float y;
        float f;
        float f2 = i - 3.5f;
        if (resultPoint4 != null) {
            x = resultPoint4.getX();
            y = resultPoint4.getY();
            f = f2 - 3.0f;
        } else {
            x = (resultPoint2.getX() - resultPoint.getX()) + resultPoint3.getX();
            y = (resultPoint2.getY() - resultPoint.getY()) + resultPoint3.getY();
            f = f2;
        }
        return PerspectiveTransform.quadrilateralToQuadrilateral(3.5f, 3.5f, f2, 3.5f, f, f, 3.5f, f2, resultPoint.getX(), resultPoint.getY(), resultPoint2.getX(), resultPoint2.getY(), x, y, resultPoint3.getX(), resultPoint3.getY());
    }

    private static int round(float f) {
        return (int) (f + 0.5f);
    }

    private static BitMatrix sampleGrid(BitMatrix bitMatrix, PerspectiveTransform perspectiveTransform, int i) throws NotFoundException {
        return GridSampler.getInstance().sampleGrid(bitMatrix, i, i, perspectiveTransform);
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x005f, code lost:
    
        if (r19.image.get(r17, r1) != false) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0071, code lost:
    
        if (r3 != 3) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0089, code lost:
    
        r1 = r16;
        r12 = r12 + r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x008c, code lost:
    
        if (r12 <= 0) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x008e, code lost:
    
        if (r1 != r7) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0091, code lost:
    
        r16 = r1 + r13;
        r12 = r12 - r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0097, code lost:
    
        r15 = r15 + r14;
        r6 = r20;
        r1 = r18;
        r4 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0095, code lost:
    
        r16 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0073, code lost:
    
        r15 = r15 - r2;
        r16 = r16 - r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0078, code lost:
    
        if (r14 >= 0) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x007a, code lost:
    
        r15 = r15 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x007c, code lost:
    
        r1 = (r15 * r15) + (r16 * r16);
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x006e, code lost:
    
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x006c, code lost:
    
        if (r19.image.get(r17, r1) == false) goto L30;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private float sizeOfBlackWhiteBlackRun(int r20, int r21, int r22, int r23) {
        /*
            r19 = this;
            r0 = r19
            int r1 = r23 - r21
            int r1 = java.lang.Math.abs(r1)
            int r2 = r22 - r20
            int r2 = java.lang.Math.abs(r2)
            r3 = 0
            r4 = 1
            if (r1 <= r2) goto L14
            r1 = 1
            goto L15
        L14:
            r1 = 0
        L15:
            if (r1 == 0) goto L20
            r5 = r20
            r2 = r21
            r7 = r22
            r6 = r23
            goto L28
        L20:
            r2 = r20
            r5 = r21
            r6 = r22
            r7 = r23
        L28:
            int r8 = r6 - r2
            int r9 = java.lang.Math.abs(r8)
            int r10 = r7 - r5
            int r11 = java.lang.Math.abs(r10)
            int r12 = -r9
            int r12 = r12 >> r4
            r13 = -1
            if (r2 >= r6) goto L3b
            r14 = 1
            goto L3c
        L3b:
            r14 = -1
        L3c:
            if (r5 >= r7) goto L3f
            r13 = 1
        L3f:
            r15 = r2
            r16 = r5
        L42:
            if (r15 == r6) goto L9e
            if (r1 == 0) goto L49
            r17 = r16
            goto L4b
        L49:
            r17 = r15
        L4b:
            r18 = r1
            if (r1 == 0) goto L51
            r1 = r15
            goto L53
        L51:
            r1 = r16
        L53:
            if (r3 != r4) goto L62
            com.google.zxing.common.BitMatrix r4 = r0.image
            r20 = r6
            r6 = r17
            boolean r1 = r4.get(r6, r1)
            if (r1 == 0) goto L70
            goto L6e
        L62:
            r20 = r6
            r6 = r17
            com.google.zxing.common.BitMatrix r4 = r0.image
            boolean r1 = r4.get(r6, r1)
            if (r1 != 0) goto L70
        L6e:
            int r3 = r3 + 1
        L70:
            r1 = 3
            if (r3 != r1) goto L89
            int r15 = r15 - r2
            r1 = r16
            int r16 = r1 - r5
            if (r14 >= 0) goto L7c
            int r15 = r15 + 1
        L7c:
            int r15 = r15 * r15
            int r16 = r16 * r16
            int r15 = r15 + r16
            double r1 = (double) r15
        L83:
            double r1 = java.lang.Math.sqrt(r1)
            float r1 = (float) r1
            return r1
        L89:
            r1 = r16
            int r12 = r12 + r11
            if (r12 <= 0) goto L95
            if (r1 != r7) goto L91
            goto L9e
        L91:
            int r16 = r1 + r13
            int r12 = r12 - r9
            goto L97
        L95:
            r16 = r1
        L97:
            int r15 = r15 + r14
            r6 = r20
            r1 = r18
            r4 = 1
            goto L42
        L9e:
            int r8 = r8 * r8
            int r10 = r10 * r10
            int r8 = r8 + r10
            double r1 = (double) r8
            goto L83
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.qrcode.detector.Detector.sizeOfBlackWhiteBlackRun(int, int, int, int):float");
    }

    private float sizeOfBlackWhiteBlackRunBothWays(int i, int i2, int i3, int i4) {
        float f;
        float sizeOfBlackWhiteBlackRun = sizeOfBlackWhiteBlackRun(i, i2, i3, i4);
        int i5 = i - (i3 - i);
        int i6 = 0;
        float f2 = 1.0f;
        if (i5 < 0) {
            f = i / (i - i5);
            i5 = 0;
        } else if (i5 > this.image.getWidth()) {
            f = (this.image.getWidth() - i) / (i5 - i);
            i5 = this.image.getWidth();
        } else {
            f = 1.0f;
        }
        float f3 = i2;
        int i7 = (int) (f3 - ((i4 - i2) * f));
        if (i7 < 0) {
            f2 = f3 / (i2 - i7);
        } else if (i7 > this.image.getHeight()) {
            f2 = (this.image.getHeight() - i2) / (i7 - i2);
            i6 = this.image.getHeight();
        } else {
            i6 = i7;
        }
        return sizeOfBlackWhiteBlackRun + sizeOfBlackWhiteBlackRun(i, i2, (int) (i + ((i5 - i) * f2)), i6);
    }

    protected float calculateModuleSize(ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3) {
        return (calculateModuleSizeOneWay(resultPoint, resultPoint2) + calculateModuleSizeOneWay(resultPoint, resultPoint3)) / 2.0f;
    }

    public DetectorResult detect() throws NotFoundException, FormatException {
        return detect(null);
    }

    public DetectorResult detect(Hashtable hashtable) throws NotFoundException, FormatException {
        ResultPointCallback resultPointCallback = hashtable == null ? null : (ResultPointCallback) hashtable.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
        this.resultPointCallback = resultPointCallback;
        return processFinderPatternInfo(new FinderPatternFinder(this.image, resultPointCallback).find(hashtable));
    }

    protected AlignmentPattern findAlignmentInRegion(float f, int i, int i2, float f2) throws NotFoundException {
        int i3 = (int) (f2 * f);
        int max = Math.max(0, i - i3);
        int min = Math.min(this.image.getWidth() - 1, i + i3) - max;
        float f3 = 3.0f * f;
        if (min < f3) {
            throw NotFoundException.getNotFoundInstance();
        }
        int max2 = Math.max(0, i2 - i3);
        int min2 = Math.min(this.image.getHeight() - 1, i2 + i3) - max2;
        if (min2 >= f3) {
            return new AlignmentPatternFinder(this.image, max, max2, min, min2, f, this.resultPointCallback).find();
        }
        throw NotFoundException.getNotFoundInstance();
    }

    protected BitMatrix getImage() {
        return this.image;
    }

    protected ResultPointCallback getResultPointCallback() {
        return this.resultPointCallback;
    }

    protected DetectorResult processFinderPatternInfo(FinderPatternInfo finderPatternInfo) throws NotFoundException, FormatException {
        FinderPattern topLeft = finderPatternInfo.getTopLeft();
        FinderPattern topRight = finderPatternInfo.getTopRight();
        FinderPattern bottomLeft = finderPatternInfo.getBottomLeft();
        float calculateModuleSize = calculateModuleSize(topLeft, topRight, bottomLeft);
        if (calculateModuleSize < 1.0f) {
            throw NotFoundException.getNotFoundInstance();
        }
        int computeDimension = computeDimension(topLeft, topRight, bottomLeft, calculateModuleSize);
        Version provisionalVersionForDimension = Version.getProvisionalVersionForDimension(computeDimension);
        int dimensionForVersion = provisionalVersionForDimension.getDimensionForVersion() - 7;
        AlignmentPattern alignmentPattern = null;
        if (provisionalVersionForDimension.getAlignmentPatternCenters().length > 0) {
            float x = (topRight.getX() - topLeft.getX()) + bottomLeft.getX();
            float y = (topRight.getY() - topLeft.getY()) + bottomLeft.getY();
            float f = 1.0f - (3.0f / dimensionForVersion);
            int x2 = (int) (topLeft.getX() + ((x - topLeft.getX()) * f));
            int y2 = (int) (topLeft.getY() + (f * (y - topLeft.getY())));
            for (int i = 4; i <= 16; i <<= 1) {
                try {
                    alignmentPattern = findAlignmentInRegion(calculateModuleSize, x2, y2, i);
                    break;
                } catch (NotFoundException unused) {
                }
            }
        }
        return new DetectorResult(sampleGrid(this.image, createTransform(topLeft, topRight, bottomLeft, alignmentPattern, computeDimension), computeDimension), alignmentPattern == null ? new ResultPoint[]{bottomLeft, topLeft, topRight} : new ResultPoint[]{bottomLeft, topLeft, topRight, alignmentPattern});
    }
}
