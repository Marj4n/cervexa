package com.google.zxing.oned.rss;

/* loaded from: classes.dex */
public class DataCharacter {
    private final int checksumPortion;
    private final int value;

    public DataCharacter(int i, int i2) {
        this.value = i;
        this.checksumPortion = i2;
    }

    public int getChecksumPortion() {
        return this.checksumPortion;
    }

    public int getValue() {
        return this.value;
    }
}
