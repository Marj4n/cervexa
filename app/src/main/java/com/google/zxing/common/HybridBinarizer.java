package com.google.zxing.common;

import com.google.zxing.Binarizer;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import java.lang.reflect.Array;
import kotlin.UByte;

/* loaded from: classes.dex */
public final class HybridBinarizer extends GlobalHistogramBinarizer {
    private static final int MINIMUM_DIMENSION = 40;
    private BitMatrix matrix;

    public HybridBinarizer(LuminanceSource luminanceSource) {
        super(luminanceSource);
        this.matrix = null;
    }

    private void binarizeEntireImage() throws NotFoundException {
        if (this.matrix == null) {
            LuminanceSource luminanceSource = getLuminanceSource();
            if (luminanceSource.getWidth() < 40 || luminanceSource.getHeight() < 40) {
                this.matrix = super.getBlackMatrix();
                return;
            }
            byte[] matrix = luminanceSource.getMatrix();
            int width = luminanceSource.getWidth();
            int height = luminanceSource.getHeight();
            int i = width >> 3;
            if ((width & 7) != 0) {
                i++;
            }
            int i2 = i;
            int i3 = height >> 3;
            if ((height & 7) != 0) {
                i3++;
            }
            int i4 = i3;
            int[][] calculateBlackPoints = calculateBlackPoints(matrix, i2, i4, width, height);
            BitMatrix bitMatrix = new BitMatrix(width, height);
            this.matrix = bitMatrix;
            calculateThresholdForBlock(matrix, i2, i4, width, height, calculateBlackPoints, bitMatrix);
        }
    }

    private static int[][] calculateBlackPoints(byte[] bArr, int i, int i2, int i3, int i4) {
        int i5 = 0;
        int[][] iArr = (int[][]) Array.newInstance((Class<?>) int.class, i2, i);
        int i6 = 0;
        while (i6 < i2) {
            int i7 = i6 << 3;
            if (i7 + 8 >= i4) {
                i7 = i4 - 8;
            }
            int i8 = 0;
            while (i8 < i) {
                int i9 = i8 << 3;
                if (i9 + 8 >= i3) {
                    i9 = i3 - 8;
                }
                int i10 = 0;
                int i11 = 0;
                int i12 = 255;
                int i13 = 0;
                while (true) {
                    if (i10 >= 8) {
                        break;
                    }
                    int i14 = ((i7 + i10) * i3) + i9;
                    for (int i15 = 8; i5 < i15; i15 = 8) {
                        int i16 = bArr[i14 + i5] & UByte.MAX_VALUE;
                        i13 += i16;
                        if (i16 < i12) {
                            i12 = i16;
                        }
                        if (i16 > i11) {
                            i11 = i16;
                        }
                        i5++;
                    }
                    i10++;
                    i5 = 0;
                }
                iArr[i6][i8] = i11 - i12 > 24 ? i13 >> 6 : i11 == 0 ? 1 : i12 >> 1;
                i8++;
                i5 = 0;
            }
            i6++;
            i5 = 0;
        }
        return iArr;
    }

    private static void calculateThresholdForBlock(byte[] bArr, int i, int i2, int i3, int i4, int[][] iArr, BitMatrix bitMatrix) {
        int i5 = 0;
        while (i5 < i2) {
            int i6 = i5 << 3;
            if (i6 + 8 >= i4) {
                i6 = i4 - 8;
            }
            int i7 = i6;
            int i8 = 0;
            while (i8 < i) {
                int i9 = i8 << 3;
                if (i9 + 8 >= i3) {
                    i9 = i3 - 8;
                }
                int i10 = i9;
                int i11 = i8 > 1 ? i8 : 2;
                if (i11 >= i - 2) {
                    i11 = i - 3;
                }
                int i12 = i5 > 1 ? i5 : 2;
                if (i12 >= i2 - 2) {
                    i12 = i2 - 3;
                }
                int i13 = 0;
                for (int i14 = -2; i14 <= 2; i14++) {
                    int[] iArr2 = iArr[i12 + i14];
                    i13 = i13 + iArr2[i11 - 2] + iArr2[i11 - 1] + iArr2[i11] + iArr2[i11 + 1] + iArr2[i11 + 2];
                }
                threshold8x8Block(bArr, i10, i7, i13 / 25, i3, bitMatrix);
                i8++;
            }
            i5++;
        }
    }

    private static void threshold8x8Block(byte[] bArr, int i, int i2, int i3, int i4, BitMatrix bitMatrix) {
        for (int i5 = 0; i5 < 8; i5++) {
            int i6 = i2 + i5;
            int i7 = (i6 * i4) + i;
            for (int i8 = 0; i8 < 8; i8++) {
                if ((bArr[i7 + i8] & 255) < i3) {
                    bitMatrix.set(i + i8, i6);
                }
            }
        }
    }

    @Override // com.google.zxing.common.GlobalHistogramBinarizer, com.google.zxing.Binarizer
    public Binarizer createBinarizer(LuminanceSource luminanceSource) {
        return new HybridBinarizer(luminanceSource);
    }

    @Override // com.google.zxing.common.GlobalHistogramBinarizer, com.google.zxing.Binarizer
    public BitMatrix getBlackMatrix() throws NotFoundException {
        binarizeEntireImage();
        return this.matrix;
    }
}
