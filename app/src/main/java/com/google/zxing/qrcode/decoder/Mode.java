package com.google.zxing.qrcode.decoder;

/* loaded from: classes.dex */
public final class Mode {
    private final int bits;
    private final int[] characterCountBitsForVersions;
    private final String name;
    public static final Mode TERMINATOR = new Mode(new int[]{0, 0, 0}, 0, "TERMINATOR");
    public static final Mode NUMERIC = new Mode(new int[]{10, 12, 14}, 1, "NUMERIC");
    public static final Mode ALPHANUMERIC = new Mode(new int[]{9, 11, 13}, 2, "ALPHANUMERIC");
    public static final Mode STRUCTURED_APPEND = new Mode(new int[]{0, 0, 0}, 3, "STRUCTURED_APPEND");
    public static final Mode BYTE = new Mode(new int[]{8, 16, 16}, 4, "BYTE");
    public static final Mode ECI = new Mode(null, 7, "ECI");
    public static final Mode KANJI = new Mode(new int[]{8, 10, 12}, 8, "KANJI");
    public static final Mode FNC1_FIRST_POSITION = new Mode(null, 5, "FNC1_FIRST_POSITION");
    public static final Mode FNC1_SECOND_POSITION = new Mode(null, 9, "FNC1_SECOND_POSITION");
    public static final Mode HANZI = new Mode(new int[]{8, 10, 12}, 13, "HANZI");

    private Mode(int[] iArr, int i, String str) {
        this.characterCountBitsForVersions = iArr;
        this.bits = i;
        this.name = str;
    }

    public static Mode forBits(int i) {
        if (i == 0) {
            return TERMINATOR;
        }
        if (i == 1) {
            return NUMERIC;
        }
        if (i == 2) {
            return ALPHANUMERIC;
        }
        if (i == 3) {
            return STRUCTURED_APPEND;
        }
        if (i == 4) {
            return BYTE;
        }
        if (i == 5) {
            return FNC1_FIRST_POSITION;
        }
        if (i == 7) {
            return ECI;
        }
        if (i == 8) {
            return KANJI;
        }
        if (i == 9) {
            return FNC1_SECOND_POSITION;
        }
        if (i == 13) {
            return HANZI;
        }
        throw new IllegalArgumentException();
    }

    public int getBits() {
        return this.bits;
    }

    public int getCharacterCountBits(Version version) {
        if (this.characterCountBitsForVersions == null) {
            throw new IllegalArgumentException("Character count doesn't apply to this mode");
        }
        int versionNumber = version.getVersionNumber();
        return this.characterCountBitsForVersions[versionNumber <= 9 ? (char) 0 : versionNumber <= 26 ? (char) 1 : (char) 2];
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}
