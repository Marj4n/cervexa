package com.google.zxing.oned.rss.expanded.decoders;

/* loaded from: classes.dex */
final class DecodedNumeric extends DecodedObject {
    static final int FNC1 = 10;
    private final int firstDigit;
    private final int secondDigit;

    DecodedNumeric(int i, int i2, int i3) {
        super(i);
        this.firstDigit = i2;
        this.secondDigit = i3;
        if (i2 < 0 || i2 > 10) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Invalid firstDigit: ");
            stringBuffer.append(i2);
            throw new IllegalArgumentException(stringBuffer.toString());
        }
        if (i3 < 0 || i3 > 10) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Invalid secondDigit: ");
            stringBuffer2.append(i3);
            throw new IllegalArgumentException(stringBuffer2.toString());
        }
    }

    int getFirstDigit() {
        return this.firstDigit;
    }

    int getSecondDigit() {
        return this.secondDigit;
    }

    int getValue() {
        return (this.firstDigit * 10) + this.secondDigit;
    }

    boolean isAnyFNC1() {
        return this.firstDigit == 10 || this.secondDigit == 10;
    }

    boolean isFirstDigitFNC1() {
        return this.firstDigit == 10;
    }

    boolean isSecondDigitFNC1() {
        return this.secondDigit == 10;
    }
}
