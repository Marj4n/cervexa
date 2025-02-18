package com.serenegiant.utils;

/* loaded from: classes2.dex */
public final class BitsHelper {
    public static int countBits(byte b) {
        int i = (b & 85) + ((b >>> 1) & 85);
        int i2 = (i & 51) + ((i >>> 2) & 51);
        return (i2 & 15) + ((i2 >>> 4) & 15);
    }

    public static int countBits(int i) {
        int i2 = (i & 1431655765) + ((i >>> 1) & 1431655765);
        int i3 = (i2 & 858993459) + (858993459 & (i2 >>> 2));
        int i4 = (i3 & 252645135) + (252645135 & (i3 >>> 4));
        int i5 = (i4 & 16711935) + (16711935 & (i4 >>> 8));
        return (i5 & 65535) + (65535 & (i5 >>> 16));
    }

    public static int countBits(long j) {
        long j2 = (j & 6148914691236517205L) + ((j >>> 1) & 6148914691236517205L);
        long j3 = (j2 & 3689348814741910323L) + (3689348814741910323L & (j2 >>> 2));
        long j4 = (j3 & 1085102592571150095L) + (1085102592571150095L & (j3 >>> 4));
        long j5 = (j4 & 71777214294589695L) + (71777214294589695L & (j4 >>> 8));
        long j6 = (j5 & 281470681808895L) + (281470681808895L & (j5 >>> 16));
        return (int) ((j6 & 4294967295L) + (4294967295L & (j6 >>> 32)));
    }

    public static int countBits(short s) {
        int i = (s & 21845) + ((s >>> 1) & 21845);
        int i2 = (i & 13107) + ((i >>> 2) & 13107);
        int i3 = (i2 & 3855) + ((i2 >>> 4) & 3855);
        return (i3 & 255) + ((i3 >>> 8) & 255);
    }

    public static int MSB(byte b) {
        if (b == 0) {
            return 0;
        }
        byte b2 = (byte) (b | (b >>> 1));
        byte b3 = (byte) (b2 | (b2 >>> 2));
        return countBits((byte) (b3 | (b3 >>> 4))) - 1;
    }

    public static int MSB(short s) {
        if (s == 0) {
            return 0;
        }
        short s2 = (short) (s | (s >>> 1));
        short s3 = (short) (s2 | (s2 >>> 2));
        short s4 = (short) (s3 | (s3 >>> 4));
        return countBits((short) (s4 | (s4 >>> 8))) - 1;
    }

    public static int MSB(int i) {
        if (i == 0) {
            return 0;
        }
        int i2 = i | (i >>> 1);
        int i3 = i2 | (i2 >>> 2);
        int i4 = i3 | (i3 >>> 4);
        int i5 = i4 | (i4 >>> 8);
        return countBits(i5 | (i5 >>> 16)) - 1;
    }

    public static int MSB(long j) {
        if (j == 0) {
            return 0;
        }
        long j2 = j | (j >>> 1);
        long j3 = j2 | (j2 >>> 2);
        long j4 = j3 | (j3 >>> 4);
        long j5 = j4 | (j4 >>> 8);
        long j6 = j5 | (j5 >>> 16);
        return countBits(j6 | (j6 >>> 32)) - 1;
    }

    public static int LSB(byte b) {
        if (b == 0) {
            return 0;
        }
        byte b2 = (byte) (b | (b << 1));
        byte b3 = (byte) (b2 | (b2 << 2));
        return 8 - countBits((byte) (b3 | (b3 << 4)));
    }

    public static int LSB(short s) {
        if (s == 0) {
            return 0;
        }
        short s2 = (short) (s | (s << 1));
        short s3 = (short) (s2 | (s2 << 2));
        short s4 = (short) (s3 | (s3 << 4));
        return 16 - countBits((short) (s4 | (s4 << 8)));
    }

    public static int LSB(int i) {
        if (i == 0) {
            return 0;
        }
        int i2 = i | (i << 1);
        int i3 = i2 | (i2 << 2);
        int i4 = i3 | (i3 << 4);
        int i5 = i4 | (i4 << 8);
        return 32 - countBits(i5 | (i5 << 16));
    }

    public static int LSB(long j) {
        if (j == 0) {
            return 0;
        }
        long j2 = j | (j << 1);
        long j3 = j2 | (j2 << 2);
        long j4 = j3 | (j3 << 4);
        long j5 = j4 | (j4 << 8);
        long j6 = j5 | (j5 << 16);
        return 64 - countBits(j6 | (j6 << 32));
    }

    public static int squareBits(byte b) {
        if (b == 0) {
            return 0;
        }
        return 1 << (MSB(b - 1) + 1);
    }

    public static int squareBits(short s) {
        if (s == 0) {
            return 0;
        }
        return 1 << (MSB(s - 1) + 1);
    }

    public static int squareBits(int i) {
        if (i == 0) {
            return 0;
        }
        return 1 << (MSB(i - 1) + 1);
    }

    public static int squareBits(long j) {
        if (j == 0) {
            return 0;
        }
        return 1 << (MSB(j - 1) + 1);
    }
}
