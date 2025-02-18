package org.apache.commons.net.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/* loaded from: classes2.dex */
public class Base64 {
    static final int CHUNK_SIZE = 76;
    private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final int MASK_6BITS = 63;
    private static final int MASK_8BITS = 255;
    private byte[] buffer;
    private int currentLinePos;
    private final int decodeSize;
    private final int encodeSize;
    private final byte[] encodeTable;
    private boolean eof;
    private final int lineLength;
    private final byte[] lineSeparator;
    private int modulus;
    private int pos;
    private int readPos;
    private int x;
    private static final byte[] CHUNK_SEPARATOR = {13, 10};
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final byte[] STANDARD_ENCODE_TABLE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] URL_SAFE_ENCODE_TABLE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
    private static final byte PAD = 61;
    private static final byte[] DECODE_TABLE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, PAD, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};

    private static boolean isWhiteSpace(byte b) {
        return b == 9 || b == 10 || b == 13 || b == 32;
    }

    public Base64() {
        this(false);
    }

    public Base64(boolean z) {
        this(76, CHUNK_SEPARATOR, z);
    }

    public Base64(int i) {
        this(i, CHUNK_SEPARATOR);
    }

    public Base64(int i, byte[] bArr) {
        this(i, bArr, false);
    }

    public Base64(int i, byte[] bArr, boolean z) {
        if (bArr == null) {
            bArr = EMPTY_BYTE_ARRAY;
            i = 0;
        }
        this.lineLength = i > 0 ? (i / 4) * 4 : 0;
        byte[] bArr2 = new byte[bArr.length];
        this.lineSeparator = bArr2;
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        if (i > 0) {
            this.encodeSize = bArr.length + 4;
        } else {
            this.encodeSize = 4;
        }
        this.decodeSize = this.encodeSize - 1;
        if (containsBase64Byte(bArr)) {
            throw new IllegalArgumentException("lineSeperator must not contain base64 characters: [" + newStringUtf8(bArr) + "]");
        }
        this.encodeTable = z ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE;
    }

    public boolean isUrlSafe() {
        return this.encodeTable == URL_SAFE_ENCODE_TABLE;
    }

    boolean hasData() {
        return this.buffer != null;
    }

    int avail() {
        if (this.buffer != null) {
            return this.pos - this.readPos;
        }
        return 0;
    }

    private void resizeBuffer() {
        byte[] bArr = this.buffer;
        if (bArr == null) {
            this.buffer = new byte[8192];
            this.pos = 0;
            this.readPos = 0;
        } else {
            byte[] bArr2 = new byte[bArr.length * 2];
            System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
            this.buffer = bArr2;
        }
    }

    int readResults(byte[] bArr, int i, int i2) {
        if (this.buffer == null) {
            return this.eof ? -1 : 0;
        }
        int min = Math.min(avail(), i2);
        byte[] bArr2 = this.buffer;
        if (bArr2 != bArr) {
            System.arraycopy(bArr2, this.readPos, bArr, i, min);
            int i3 = this.readPos + min;
            this.readPos = i3;
            if (i3 >= this.pos) {
                this.buffer = null;
            }
        } else {
            this.buffer = null;
        }
        return min;
    }

    void setInitialBuffer(byte[] bArr, int i, int i2) {
        if (bArr == null || bArr.length != i2) {
            return;
        }
        this.buffer = bArr;
        this.pos = i;
        this.readPos = i;
    }

    void encode(byte[] bArr, int i, int i2) {
        int i3;
        if (this.eof) {
            return;
        }
        if (i2 >= 0) {
            int i4 = 0;
            while (i4 < i2) {
                byte[] bArr2 = this.buffer;
                if (bArr2 == null || bArr2.length - this.pos < this.encodeSize) {
                    resizeBuffer();
                }
                int i5 = this.modulus + 1;
                this.modulus = i5;
                this.modulus = i5 % 3;
                int i6 = i + 1;
                int i7 = bArr[i];
                if (i7 < 0) {
                    i7 += 256;
                }
                int i8 = (this.x << 8) + i7;
                this.x = i8;
                if (this.modulus == 0) {
                    byte[] bArr3 = this.buffer;
                    int i9 = this.pos;
                    int i10 = i9 + 1;
                    this.pos = i10;
                    byte[] bArr4 = this.encodeTable;
                    bArr3[i9] = bArr4[(i8 >> 18) & 63];
                    int i11 = i10 + 1;
                    this.pos = i11;
                    bArr3[i10] = bArr4[(i8 >> 12) & 63];
                    int i12 = i11 + 1;
                    this.pos = i12;
                    bArr3[i11] = bArr4[(i8 >> 6) & 63];
                    int i13 = i12 + 1;
                    this.pos = i13;
                    bArr3[i12] = bArr4[i8 & 63];
                    int i14 = this.currentLinePos + 4;
                    this.currentLinePos = i14;
                    int i15 = this.lineLength;
                    if (i15 > 0 && i15 <= i14) {
                        byte[] bArr5 = this.lineSeparator;
                        System.arraycopy(bArr5, 0, bArr3, i13, bArr5.length);
                        this.pos += this.lineSeparator.length;
                        this.currentLinePos = 0;
                    }
                }
                i4++;
                i = i6;
            }
            return;
        }
        this.eof = true;
        byte[] bArr6 = this.buffer;
        if (bArr6 == null || bArr6.length - this.pos < this.encodeSize) {
            resizeBuffer();
        }
        int i16 = this.modulus;
        if (i16 == 1) {
            byte[] bArr7 = this.buffer;
            int i17 = this.pos;
            int i18 = i17 + 1;
            this.pos = i18;
            byte[] bArr8 = this.encodeTable;
            int i19 = this.x;
            bArr7[i17] = bArr8[(i19 >> 2) & 63];
            int i20 = i18 + 1;
            this.pos = i20;
            bArr7[i18] = bArr8[(i19 << 4) & 63];
            if (bArr8 == STANDARD_ENCODE_TABLE) {
                int i21 = i20 + 1;
                this.pos = i21;
                bArr7[i20] = PAD;
                this.pos = i21 + 1;
                bArr7[i21] = PAD;
            }
        } else if (i16 == 2) {
            byte[] bArr9 = this.buffer;
            int i22 = this.pos;
            int i23 = i22 + 1;
            this.pos = i23;
            byte[] bArr10 = this.encodeTable;
            int i24 = this.x;
            bArr9[i22] = bArr10[(i24 >> 10) & 63];
            int i25 = i23 + 1;
            this.pos = i25;
            bArr9[i23] = bArr10[(i24 >> 4) & 63];
            int i26 = i25 + 1;
            this.pos = i26;
            bArr9[i25] = bArr10[(i24 << 2) & 63];
            if (bArr10 == STANDARD_ENCODE_TABLE) {
                this.pos = i26 + 1;
                bArr9[i26] = PAD;
            }
        }
        if (this.lineLength <= 0 || (i3 = this.pos) <= 0) {
            return;
        }
        byte[] bArr11 = this.lineSeparator;
        System.arraycopy(bArr11, 0, this.buffer, i3, bArr11.length);
        this.pos += this.lineSeparator.length;
    }

    void decode(byte[] bArr, int i, int i2) {
        int i3;
        byte b;
        if (this.eof) {
            return;
        }
        if (i2 < 0) {
            this.eof = true;
        }
        int i4 = 0;
        while (true) {
            if (i4 >= i2) {
                break;
            }
            byte[] bArr2 = this.buffer;
            if (bArr2 == null || bArr2.length - this.pos < this.decodeSize) {
                resizeBuffer();
            }
            int i5 = i + 1;
            byte b2 = bArr[i];
            if (b2 == 61) {
                this.eof = true;
                break;
            }
            if (b2 >= 0) {
                byte[] bArr3 = DECODE_TABLE;
                if (b2 < bArr3.length && (b = bArr3[b2]) >= 0) {
                    int i6 = this.modulus + 1;
                    this.modulus = i6;
                    int i7 = i6 % 4;
                    this.modulus = i7;
                    int i8 = (this.x << 6) + b;
                    this.x = i8;
                    if (i7 == 0) {
                        byte[] bArr4 = this.buffer;
                        int i9 = this.pos;
                        int i10 = i9 + 1;
                        this.pos = i10;
                        bArr4[i9] = (byte) ((i8 >> 16) & 255);
                        int i11 = i10 + 1;
                        this.pos = i11;
                        bArr4[i10] = (byte) ((i8 >> 8) & 255);
                        this.pos = i11 + 1;
                        bArr4[i11] = (byte) (i8 & 255);
                    }
                }
            }
            i4++;
            i = i5;
        }
        if (!this.eof || (i3 = this.modulus) == 0) {
            return;
        }
        int i12 = this.x << 6;
        this.x = i12;
        if (i3 == 2) {
            int i13 = i12 << 6;
            this.x = i13;
            byte[] bArr5 = this.buffer;
            int i14 = this.pos;
            this.pos = i14 + 1;
            bArr5[i14] = (byte) ((i13 >> 16) & 255);
            return;
        }
        if (i3 != 3) {
            return;
        }
        byte[] bArr6 = this.buffer;
        int i15 = this.pos;
        int i16 = i15 + 1;
        this.pos = i16;
        bArr6[i15] = (byte) ((i12 >> 16) & 255);
        this.pos = i16 + 1;
        bArr6[i16] = (byte) ((i12 >> 8) & 255);
    }

    public static boolean isBase64(byte b) {
        if (b != 61) {
            if (b >= 0) {
                byte[] bArr = DECODE_TABLE;
                if (b >= bArr.length || bArr[b] == -1) {
                }
            }
            return false;
        }
        return true;
    }

    public static boolean isArrayByteBase64(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            if (!isBase64(bArr[i]) && !isWhiteSpace(bArr[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean containsBase64Byte(byte[] bArr) {
        for (byte b : bArr) {
            if (isBase64(b)) {
                return true;
            }
        }
        return false;
    }

    public static byte[] encodeBase64(byte[] bArr) {
        return encodeBase64(bArr, false);
    }

    public static String encodeBase64String(byte[] bArr) {
        return newStringUtf8(encodeBase64(bArr, true));
    }

    public static String encodeBase64StringUnChunked(byte[] bArr) {
        return newStringUtf8(encodeBase64(bArr, false));
    }

    public static String encodeBase64String(byte[] bArr, boolean z) {
        return newStringUtf8(encodeBase64(bArr, z));
    }

    public static byte[] encodeBase64URLSafe(byte[] bArr) {
        return encodeBase64(bArr, false, true);
    }

    public static String encodeBase64URLSafeString(byte[] bArr) {
        return newStringUtf8(encodeBase64(bArr, false, true));
    }

    public static byte[] encodeBase64Chunked(byte[] bArr) {
        return encodeBase64(bArr, true);
    }

    public byte[] decode(String str) {
        return decode(getBytesUtf8(str));
    }

    private byte[] getBytesUtf8(String str) {
        try {
            return str.getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] decode(byte[] bArr) {
        reset();
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        setInitialBuffer(new byte[r1], 0, r1);
        decode(bArr, 0, bArr.length);
        decode(bArr, 0, -1);
        int i = this.pos;
        byte[] bArr2 = new byte[i];
        readResults(bArr2, 0, i);
        return bArr2;
    }

    public static byte[] encodeBase64(byte[] bArr, boolean z) {
        return encodeBase64(bArr, z, false);
    }

    public static byte[] encodeBase64(byte[] bArr, boolean z, boolean z2) {
        return encodeBase64(bArr, z, z2, Integer.MAX_VALUE);
    }

    public static byte[] encodeBase64(byte[] bArr, boolean z, boolean z2, int i) {
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        long encodeLength = getEncodeLength(bArr, z ? 76 : 0, z ? CHUNK_SEPARATOR : EMPTY_BYTE_ARRAY);
        if (encodeLength > i) {
            throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + encodeLength + ") than the specified maxium size of " + i);
        }
        return (z ? new Base64(z2) : new Base64(0, CHUNK_SEPARATOR, z2)).encode(bArr);
    }

    public static byte[] decodeBase64(String str) {
        return new Base64().decode(str);
    }

    public static byte[] decodeBase64(byte[] bArr) {
        return new Base64().decode(bArr);
    }

    public String encodeToString(byte[] bArr) {
        return newStringUtf8(encode(bArr));
    }

    private static String newStringUtf8(byte[] bArr) {
        try {
            return new String(bArr, "UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] encode(byte[] bArr) {
        int i;
        reset();
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        int encodeLength = (int) getEncodeLength(bArr, this.lineLength, this.lineSeparator);
        byte[] bArr2 = new byte[encodeLength];
        setInitialBuffer(bArr2, 0, encodeLength);
        encode(bArr, 0, bArr.length);
        encode(bArr, 0, -1);
        if (this.buffer != bArr2) {
            readResults(bArr2, 0, encodeLength);
        }
        if (!isUrlSafe() || (i = this.pos) >= encodeLength) {
            return bArr2;
        }
        byte[] bArr3 = new byte[i];
        System.arraycopy(bArr2, 0, bArr3, 0, i);
        return bArr3;
    }

    private static long getEncodeLength(byte[] bArr, int i, byte[] bArr2) {
        int i2 = (i / 4) * 4;
        long length = (bArr.length * 4) / 3;
        long j = length % 4;
        if (j != 0) {
            length += 4 - j;
        }
        if (i2 <= 0) {
            return length;
        }
        long j2 = i2;
        boolean z = length % j2 == 0;
        long length2 = length + ((length / j2) * bArr2.length);
        return !z ? length2 + bArr2.length : length2;
    }

    public static BigInteger decodeInteger(byte[] bArr) {
        return new BigInteger(1, decodeBase64(bArr));
    }

    public static byte[] encodeInteger(BigInteger bigInteger) {
        if (bigInteger == null) {
            throw new NullPointerException("encodeInteger called with null parameter");
        }
        return encodeBase64(toIntegerBytes(bigInteger), false);
    }

    static byte[] toIntegerBytes(BigInteger bigInteger) {
        int bitLength = ((bigInteger.bitLength() + 7) >> 3) << 3;
        byte[] byteArray = bigInteger.toByteArray();
        int i = 1;
        if (bigInteger.bitLength() % 8 != 0 && (bigInteger.bitLength() / 8) + 1 == bitLength / 8) {
            return byteArray;
        }
        int length = byteArray.length;
        if (bigInteger.bitLength() % 8 == 0) {
            length--;
        } else {
            i = 0;
        }
        int i2 = bitLength / 8;
        int i3 = i2 - length;
        byte[] bArr = new byte[i2];
        System.arraycopy(byteArray, i, bArr, i3, length);
        return bArr;
    }

    private void reset() {
        this.buffer = null;
        this.pos = 0;
        this.readPos = 0;
        this.currentLinePos = 0;
        this.modulus = 0;
        this.eof = false;
    }

    int getLineLength() {
        return this.lineLength;
    }

    byte[] getLineSeparator() {
        return (byte[]) this.lineSeparator.clone();
    }
}
