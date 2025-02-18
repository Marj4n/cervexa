package com.baidu.a.a.a.a.a;

import java.io.UnsupportedEncodingException;
import kotlin.UByte;

/* loaded from: classes.dex */
public final class b {
    private static final byte[] a = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};

    public static String a(byte[] bArr, String str) throws UnsupportedEncodingException {
        int length = (bArr.length << 2) / 3;
        byte[] bArr2 = new byte[length + (length / 76) + 3];
        int length2 = bArr.length - (bArr.length % 3);
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < length2; i3 += 3) {
            int i4 = i + 1;
            byte[] bArr3 = a;
            bArr2[i] = bArr3[(bArr[i3] & UByte.MAX_VALUE) >> 2];
            int i5 = i4 + 1;
            int i6 = i3 + 1;
            bArr2[i4] = bArr3[((bArr[i3] & 3) << 4) | ((bArr[i6] & UByte.MAX_VALUE) >> 4)];
            int i7 = i5 + 1;
            int i8 = (bArr[i6] & 15) << 2;
            int i9 = i3 + 2;
            bArr2[i5] = bArr3[i8 | ((bArr[i9] & UByte.MAX_VALUE) >> 6)];
            i = i7 + 1;
            bArr2[i7] = bArr3[bArr[i9] & 63];
            if ((i - i2) % 76 == 0 && i != 0) {
                bArr2[i] = 10;
                i2++;
                i++;
            }
        }
        int length3 = bArr.length % 3;
        if (length3 == 1) {
            int i10 = i + 1;
            byte[] bArr4 = a;
            bArr2[i] = bArr4[(bArr[length2] & UByte.MAX_VALUE) >> 2];
            int i11 = i10 + 1;
            bArr2[i10] = bArr4[(bArr[length2] & 3) << 4];
            int i12 = i11 + 1;
            bArr2[i11] = 61;
            i = i12 + 1;
            bArr2[i12] = 61;
        } else if (length3 == 2) {
            int i13 = i + 1;
            byte[] bArr5 = a;
            bArr2[i] = bArr5[(bArr[length2] & UByte.MAX_VALUE) >> 2];
            int i14 = i13 + 1;
            int i15 = (bArr[length2] & 3) << 4;
            int i16 = length2 + 1;
            bArr2[i13] = bArr5[((bArr[i16] & UByte.MAX_VALUE) >> 4) | i15];
            int i17 = i14 + 1;
            bArr2[i14] = bArr5[(bArr[i16] & 15) << 2];
            i = i17 + 1;
            bArr2[i17] = 61;
        }
        return new String(bArr2, 0, i, str);
    }

    public static byte[] a(byte[] bArr) {
        return a(bArr, bArr.length);
    }

    public static byte[] a(byte[] bArr, int i) {
        int i2;
        int i3 = (i / 4) * 3;
        if (i3 == 0) {
            return new byte[0];
        }
        byte[] bArr2 = new byte[i3];
        int i4 = 0;
        while (true) {
            byte b = bArr[i - 1];
            if (b != 10 && b != 13 && b != 32 && b != 9) {
                if (b != 61) {
                    break;
                }
                i4++;
            }
            i--;
        }
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        for (int i8 = 0; i8 < i; i8++) {
            byte b2 = bArr[i8];
            if (b2 != 10 && b2 != 13 && b2 != 32 && b2 != 9) {
                if (b2 >= 65 && b2 <= 90) {
                    i2 = b2 - 65;
                } else if (b2 >= 97 && b2 <= 122) {
                    i2 = b2 - 71;
                } else if (b2 >= 48 && b2 <= 57) {
                    i2 = b2 + 4;
                } else if (b2 == 43) {
                    i2 = 62;
                } else {
                    if (b2 != 47) {
                        return null;
                    }
                    i2 = 63;
                }
                i5 = (i5 << 6) | ((byte) i2);
                if (i7 % 4 == 3) {
                    int i9 = i6 + 1;
                    bArr2[i6] = (byte) ((i5 >> 16) & 255);
                    int i10 = i9 + 1;
                    bArr2[i9] = (byte) ((i5 >> 8) & 255);
                    bArr2[i10] = (byte) i5;
                    i6 = i10 + 1;
                }
                i7++;
            }
        }
        if (i4 > 0) {
            int i11 = i5 << (i4 * 6);
            int i12 = i6 + 1;
            bArr2[i6] = (byte) ((i11 >> 16) & 255);
            if (i4 == 1) {
                i6 = i12 + 1;
                bArr2[i12] = (byte) ((i11 >> 8) & 255);
            } else {
                i6 = i12;
            }
        }
        byte[] bArr3 = new byte[i6];
        System.arraycopy(bArr2, 0, bArr3, 0, i6);
        return bArr3;
    }
}
