package com.jieli.lib.dv.control.utils;

import kotlin.UByte;

/* loaded from: classes.dex */
public class BufChangeHex {
    private static final char[] a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

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

    public static String str2HexStr(String str) {
        char[] charArray = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (byte b2 : str.getBytes()) {
            sb.append(charArray[(b2 & 240) >> 4]);
            sb.append(charArray[b2 & 15]);
        }
        return sb.toString().trim();
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
            int a2 = a(cArr[i], i) << 4;
            int i3 = i + 1;
            int a3 = a2 | a(cArr[i3], i3);
            i = i3 + 1;
            bArr[i2] = (byte) (a3 & 255);
            i2++;
        }
        return bArr;
    }

    private static int a(char c, int i) {
        int digit = Character.digit(c, 16);
        if (digit != -1) {
            return digit;
        }
        throw new RuntimeException("Illegal hexadecimal character " + c + " at index " + i);
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
}
