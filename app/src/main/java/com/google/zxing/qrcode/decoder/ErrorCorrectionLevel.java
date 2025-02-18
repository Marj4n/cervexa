package com.google.zxing.qrcode.decoder;

/* loaded from: classes.dex */
public final class ErrorCorrectionLevel {
    private static final ErrorCorrectionLevel[] FOR_BITS;
    public static final ErrorCorrectionLevel H;
    public static final ErrorCorrectionLevel L = new ErrorCorrectionLevel(0, 1, "L");
    public static final ErrorCorrectionLevel M = new ErrorCorrectionLevel(1, 0, "M");
    public static final ErrorCorrectionLevel Q = new ErrorCorrectionLevel(2, 3, "Q");
    private final int bits;
    private final String name;
    private final int ordinal;

    static {
        ErrorCorrectionLevel errorCorrectionLevel = new ErrorCorrectionLevel(3, 2, "H");
        H = errorCorrectionLevel;
        FOR_BITS = new ErrorCorrectionLevel[]{M, L, errorCorrectionLevel, Q};
    }

    private ErrorCorrectionLevel(int i, int i2, String str) {
        this.ordinal = i;
        this.bits = i2;
        this.name = str;
    }

    public static ErrorCorrectionLevel forBits(int i) {
        if (i >= 0) {
            ErrorCorrectionLevel[] errorCorrectionLevelArr = FOR_BITS;
            if (i < errorCorrectionLevelArr.length) {
                return errorCorrectionLevelArr[i];
            }
        }
        throw new IllegalArgumentException();
    }

    public int getBits() {
        return this.bits;
    }

    public String getName() {
        return this.name;
    }

    public int ordinal() {
        return this.ordinal;
    }

    public String toString() {
        return this.name;
    }
}
