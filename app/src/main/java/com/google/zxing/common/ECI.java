package com.google.zxing.common;

/* loaded from: classes.dex */
public abstract class ECI {
    private final int value;

    ECI(int i) {
        this.value = i;
    }

    public static ECI getECIByValue(int i) {
        if (i >= 0 && i <= 999999) {
            if (i < 900) {
                return CharacterSetECI.getCharacterSetECIByValue(i);
            }
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Bad ECI value: ");
        stringBuffer.append(i);
        throw new IllegalArgumentException(stringBuffer.toString());
    }

    public int getValue() {
        return this.value;
    }
}
