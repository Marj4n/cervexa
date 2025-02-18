package com.jieli.lib.dv.control.projection.tools;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import kotlin.UByte;

/* loaded from: classes.dex */
public class FormatHex {
    private static final char[] a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static byte[] contructIntTo1B(int i) {
        return new byte[]{(byte) (i & 255)};
    }

    public static byte[] contructIntTo2B(int i) {
        return new byte[]{(byte) (i & 255), (byte) ((i >> 8) & 255)};
    }

    public static char convertIntToAscii(int i) {
        if (i < 0 || i > 255) {
            return (char) 0;
        }
        return (char) i;
    }

    public static byte[] intToByteArray(int i) {
        return new byte[]{(byte) (i & 255), (byte) ((i >> 8) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 24) & 255)};
    }

    public static char[] encodeHex(byte[] bArr) {
        return encodeHex(bArr, true);
    }

    public static char[] encodeHex(byte[] bArr, boolean z) {
        return encodeHex(bArr, z ? a : b);
    }

    protected static char[] encodeHex(byte[] bArr, char[] cArr) {
        int length = bArr.length;
        char[] cArr2 = new char[(length << 1) + length];
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int i3 = i2 + 1;
            cArr2[i2] = cArr[(bArr[i] & 240) >>> 4];
            int i4 = i3 + 1;
            cArr2[i3] = cArr[bArr[i] & 15];
            cArr2[i4] = ' ';
            i++;
            i2 = i4 + 1;
        }
        return cArr2;
    }

    protected static char[] encodeHex2(byte[] bArr, char[] cArr) {
        int length = bArr.length;
        char[] cArr2 = new char[length << 1];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + 1;
            cArr2[i] = cArr[(bArr[i2] & 240) >>> 4];
            i = i3 + 1;
            cArr2[i3] = cArr[bArr[i2] & 15];
        }
        return cArr2;
    }

    public static String encodeHexStr(byte[] bArr) {
        return encodeHexStr(bArr, true);
    }

    public static String encodeHexStr(byte[] bArr, boolean z) {
        return encodeHexStr(bArr, z ? a : b);
    }

    protected static String encodeHexStr(byte[] bArr, char[] cArr) {
        return new String(encodeHex(bArr, cArr));
    }

    public static byte[] decodeHex(char[] cArr) {
        int length = cArr.length;
        if ((length & 1) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }
        byte[] bArr = new byte[length >> 1];
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int digit = toDigit(cArr[i], i) << 4;
            int i3 = i + 1;
            int digit2 = digit | toDigit(cArr[i3], i3);
            i = i3 + 1;
            bArr[i2] = (byte) (digit2 & 255);
            i2++;
        }
        return bArr;
    }

    public static int bytesToInt(byte[] bArr) {
        int i;
        int i2;
        if (bArr.length == 2) {
            i = bArr[1] & UByte.MAX_VALUE;
            i2 = (bArr[0] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK;
        } else {
            i = (bArr[3] & UByte.MAX_VALUE) | (65280 & (bArr[2] << 8)) | ((bArr[1] << 16) & 16711680);
            i2 = (bArr[0] << 24) & ViewCompat.MEASURED_STATE_MASK;
        }
        return i2 | i;
    }

    public static long getLong(byte[] bArr, boolean z) {
        if (bArr == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (bArr.length > 8) {
            throw new IllegalArgumentException("byte array size > 8 !");
        }
        long j = 0;
        if (z) {
            for (int length = bArr.length - 1; length >= 0; length--) {
                j = (j << 8) | (bArr[length] & UByte.MAX_VALUE);
            }
        } else {
            for (byte b2 : bArr) {
                j = (j << 8) | (b2 & UByte.MAX_VALUE);
            }
        }
        return j;
    }

    public static int byteArrayToInt(byte[] bArr) {
        byte[] bArr2 = new byte[4];
        int i = 0;
        int i2 = 0;
        while (i < 4) {
            if (i2 < bArr.length) {
                bArr2[i] = bArr[i2];
            } else {
                bArr2[i] = 0;
            }
            i++;
            i2++;
        }
        int i3 = (bArr2[3] & UByte.MAX_VALUE) << 24;
        int i4 = (bArr2[2] & UByte.MAX_VALUE) << 16;
        return i3 + i4 + ((bArr2[1] & UByte.MAX_VALUE) << 8) + (bArr2[0] & UByte.MAX_VALUE);
    }

    protected static int toDigit(char c, int i) {
        int digit = Character.digit(c, 16);
        if (digit != -1) {
            return digit;
        }
        throw new RuntimeException("Illegal hexadecimal character " + c + " at index " + i);
    }
}
