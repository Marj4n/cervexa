package com.google.zxing.oned.rss;

/* loaded from: classes.dex */
public final class RSSUtils {
    private RSSUtils() {
    }

    static int combins(int i, int i2) {
        int i3 = i - i2;
        if (i3 > i2) {
            i3 = i2;
            i2 = i3;
        }
        int i4 = 1;
        int i5 = 1;
        while (i > i2) {
            i4 *= i;
            if (i5 <= i3) {
                i4 /= i5;
                i5++;
            }
            i--;
        }
        while (i5 <= i3) {
            i4 /= i5;
            i5++;
        }
        return i4;
    }

    static int[] elements(int[] iArr, int i, int i2) {
        int[] iArr2 = new int[iArr.length + 2];
        int i3 = i2 << 1;
        iArr2[0] = 1;
        int i4 = 10;
        int i5 = 1;
        for (int i6 = 1; i6 < i3 - 2; i6 += 2) {
            int i7 = i6 - 1;
            iArr2[i6] = iArr[i7] - iArr2[i7];
            int i8 = i6 + 1;
            iArr2[i8] = iArr[i6] - iArr2[i6];
            i5 += iArr2[i6] + iArr2[i8];
            if (iArr2[i6] < i4) {
                i4 = iArr2[i6];
            }
        }
        int i9 = i3 - 1;
        iArr2[i9] = i - i5;
        if (iArr2[i9] < i4) {
            i4 = iArr2[i9];
        }
        if (i4 > 1) {
            for (int i10 = 0; i10 < i3; i10 += 2) {
                int i11 = i4 - 1;
                iArr2[i10] = iArr2[i10] + i11;
                int i12 = i10 + 1;
                iArr2[i12] = iArr2[i12] - i11;
            }
        }
        return iArr2;
    }

    public static int getRSSvalue(int[] iArr, int i, boolean z) {
        int[] iArr2 = iArr;
        int length = iArr2.length;
        int i2 = 0;
        for (int i3 : iArr2) {
            i2 += i3;
        }
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (true) {
            int i7 = length - 1;
            if (i4 >= i7) {
                return i5;
            }
            int i8 = 1 << i4;
            i6 |= i8;
            int i9 = 1;
            while (i9 < iArr2[i4]) {
                int i10 = i2 - i9;
                int i11 = length - i4;
                int i12 = i11 - 2;
                int combins = combins(i10 - 1, i12);
                if (z && i6 == 0) {
                    int i13 = i11 - 1;
                    if (i10 - i13 >= i13) {
                        combins -= combins(i10 - i11, i12);
                    }
                }
                if (i11 - 1 > 1) {
                    int i14 = 0;
                    for (int i15 = i10 - i12; i15 > i; i15--) {
                        i14 += combins((i10 - i15) - 1, i11 - 3);
                    }
                    combins -= i14 * (i7 - i4);
                } else if (i10 > i) {
                    combins--;
                }
                i5 += combins;
                i9++;
                i6 &= ~i8;
                iArr2 = iArr;
            }
            i2 -= i9;
            i4++;
            iArr2 = iArr;
        }
    }

    static int[] getRSSwidths(int i, int i2, int i3, int i4, boolean z) {
        int i5;
        int combins;
        int i6 = i3;
        int[] iArr = new int[i6];
        int i7 = i;
        int i8 = i2;
        int i9 = 0;
        int i10 = 0;
        while (true) {
            int i11 = i6 - 1;
            if (i9 >= i11) {
                iArr[i9] = i8;
                return iArr;
            }
            int i12 = 1 << i9;
            i10 |= i12;
            int i13 = 1;
            while (true) {
                i5 = i8 - i13;
                int i14 = i6 - i9;
                int i15 = i14 - 2;
                combins = combins(i5 - 1, i15);
                if (z && i10 == 0) {
                    int i16 = i14 - 1;
                    if (i5 - i16 >= i16) {
                        combins -= combins(i5 - i14, i15);
                    }
                }
                if (i14 - 1 > 1) {
                    int i17 = 0;
                    for (int i18 = i5 - i15; i18 > i4; i18--) {
                        i17 += combins((i5 - i18) - 1, i14 - 3);
                    }
                    combins -= i17 * (i11 - i9);
                } else if (i5 > i4) {
                    combins--;
                }
                i7 -= combins;
                if (i7 < 0) {
                    break;
                }
                i13++;
                i10 &= ~i12;
                i6 = i3;
            }
            i7 += combins;
            iArr[i9] = i13;
            i9++;
            i6 = i3;
            i8 = i5;
        }
    }
}
