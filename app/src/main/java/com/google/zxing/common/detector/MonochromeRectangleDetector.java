package com.google.zxing.common.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

/* loaded from: classes.dex */
public final class MonochromeRectangleDetector {
    private static final int MAX_MODULES = 32;
    private final BitMatrix image;

    public MonochromeRectangleDetector(BitMatrix bitMatrix) {
        this.image = bitMatrix;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0020  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0066 A[EDGE_INSN: B:67:0x0066->B:47:0x0066 BREAK  A[LOOP:3: B:39:0x0052->B:61:0x0052], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0031 A[EDGE_INSN: B:81:0x0031->B:21:0x0031 BREAK  A[LOOP:1: B:13:0x001c->B:75:0x001c], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int[] blackWhiteRange(int r6, int r7, int r8, int r9, boolean r10) {
        /*
            r5 = this;
            int r0 = r8 + r9
            r1 = 1
            int r0 = r0 >> r1
            r2 = r0
        L5:
            if (r2 < r8) goto L3a
            com.google.zxing.common.BitMatrix r3 = r5.image
            if (r10 == 0) goto L12
            boolean r3 = r3.get(r2, r6)
            if (r3 == 0) goto L1b
            goto L18
        L12:
            boolean r3 = r3.get(r6, r2)
            if (r3 == 0) goto L1b
        L18:
            int r2 = r2 + (-1)
            goto L5
        L1b:
            r3 = r2
        L1c:
            int r3 = r3 + (-1)
            if (r3 < r8) goto L31
            com.google.zxing.common.BitMatrix r4 = r5.image
            if (r10 == 0) goto L2b
            boolean r4 = r4.get(r3, r6)
            if (r4 == 0) goto L1c
            goto L31
        L2b:
            boolean r4 = r4.get(r6, r3)
            if (r4 == 0) goto L1c
        L31:
            int r4 = r2 - r3
            if (r3 < r8) goto L3a
            if (r4 <= r7) goto L38
            goto L3a
        L38:
            r2 = r3
            goto L5
        L3a:
            int r2 = r2 + r1
        L3b:
            if (r0 >= r9) goto L6f
            com.google.zxing.common.BitMatrix r8 = r5.image
            if (r10 == 0) goto L48
            boolean r8 = r8.get(r0, r6)
            if (r8 == 0) goto L51
            goto L4e
        L48:
            boolean r8 = r8.get(r6, r0)
            if (r8 == 0) goto L51
        L4e:
            int r0 = r0 + 1
            goto L3b
        L51:
            r8 = r0
        L52:
            int r8 = r8 + r1
            if (r8 >= r9) goto L66
            com.google.zxing.common.BitMatrix r3 = r5.image
            if (r10 == 0) goto L60
            boolean r3 = r3.get(r8, r6)
            if (r3 == 0) goto L52
            goto L66
        L60:
            boolean r3 = r3.get(r6, r8)
            if (r3 == 0) goto L52
        L66:
            int r3 = r8 - r0
            if (r8 >= r9) goto L6f
            if (r3 <= r7) goto L6d
            goto L6f
        L6d:
            r0 = r8
            goto L3b
        L6f:
            int r0 = r0 + (-1)
            if (r0 <= r2) goto L7c
            r6 = 2
            int[] r6 = new int[r6]
            r7 = 0
            r6[r7] = r2
            r6[r1] = r0
            goto L7d
        L7c:
            r6 = 0
        L7d:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.common.detector.MonochromeRectangleDetector.blackWhiteRange(int, int, int, int, boolean):int[]");
    }

    private ResultPoint findCornerFromCenter(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) throws NotFoundException {
        boolean z;
        MonochromeRectangleDetector monochromeRectangleDetector;
        int i10;
        int i11;
        int i12;
        int i13;
        int[] iArr = null;
        int i14 = i;
        int i15 = i5;
        while (i15 < i8 && i15 >= i7 && i14 < i4 && i14 >= i3) {
            if (i2 == 0) {
                z = true;
                monochromeRectangleDetector = this;
                i10 = i15;
                i11 = i9;
                i12 = i3;
                i13 = i4;
            } else {
                z = false;
                monochromeRectangleDetector = this;
                i10 = i14;
                i11 = i9;
                i12 = i7;
                i13 = i8;
            }
            int[] blackWhiteRange = monochromeRectangleDetector.blackWhiteRange(i10, i11, i12, i13, z);
            if (blackWhiteRange == null) {
                if (iArr == null) {
                    throw NotFoundException.getNotFoundInstance();
                }
                if (i2 == 0) {
                    int i16 = i15 - i6;
                    if (iArr[0] >= i) {
                        return new ResultPoint(iArr[1], i16);
                    }
                    if (iArr[1] > i) {
                        return new ResultPoint(i6 > 0 ? iArr[0] : iArr[1], i16);
                    }
                    return new ResultPoint(iArr[0], i16);
                }
                int i17 = i14 - i2;
                if (iArr[0] >= i5) {
                    return new ResultPoint(i17, iArr[1]);
                }
                if (iArr[1] > i5) {
                    return new ResultPoint(i17, i2 < 0 ? iArr[0] : iArr[1]);
                }
                return new ResultPoint(i17, iArr[0]);
            }
            i15 += i6;
            i14 += i2;
            iArr = blackWhiteRange;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    public ResultPoint[] detect() throws NotFoundException {
        int height = this.image.getHeight();
        int width = this.image.getWidth();
        int i = height >> 1;
        int i2 = width >> 1;
        int max = Math.max(1, height / 256);
        int max2 = Math.max(1, width / 256);
        int i3 = -max;
        int i4 = i2 >> 1;
        int y = ((int) findCornerFromCenter(i2, 0, 0, width, i, i3, 0, height, i4).getY()) - 1;
        int i5 = i >> 1;
        ResultPoint findCornerFromCenter = findCornerFromCenter(i2, -max2, 0, width, i, 0, y, height, i5);
        int x = ((int) findCornerFromCenter.getX()) - 1;
        ResultPoint findCornerFromCenter2 = findCornerFromCenter(i2, max2, x, width, i, 0, y, height, i5);
        int x2 = ((int) findCornerFromCenter2.getX()) + 1;
        ResultPoint findCornerFromCenter3 = findCornerFromCenter(i2, 0, x, x2, i, max, y, height, i4);
        return new ResultPoint[]{findCornerFromCenter(i2, 0, x, x2, i, i3, y, ((int) findCornerFromCenter3.getY()) + 1, i2 >> 2), findCornerFromCenter, findCornerFromCenter2, findCornerFromCenter3};
    }
}
