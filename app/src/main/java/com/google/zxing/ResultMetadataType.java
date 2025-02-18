package com.google.zxing;

import java.util.Hashtable;

/* loaded from: classes.dex */
public final class ResultMetadataType {
    private final String name;
    private static final Hashtable VALUES = new Hashtable();
    public static final ResultMetadataType OTHER = new ResultMetadataType("OTHER");
    public static final ResultMetadataType ORIENTATION = new ResultMetadataType("ORIENTATION");
    public static final ResultMetadataType BYTE_SEGMENTS = new ResultMetadataType("BYTE_SEGMENTS");
    public static final ResultMetadataType ERROR_CORRECTION_LEVEL = new ResultMetadataType("ERROR_CORRECTION_LEVEL");
    public static final ResultMetadataType ISSUE_NUMBER = new ResultMetadataType("ISSUE_NUMBER");
    public static final ResultMetadataType SUGGESTED_PRICE = new ResultMetadataType("SUGGESTED_PRICE");
    public static final ResultMetadataType POSSIBLE_COUNTRY = new ResultMetadataType("POSSIBLE_COUNTRY");

    private ResultMetadataType(String str) {
        this.name = str;
        VALUES.put(str, this);
    }

    public static ResultMetadataType valueOf(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException();
        }
        ResultMetadataType resultMetadataType = (ResultMetadataType) VALUES.get(str);
        if (resultMetadataType != null) {
            return resultMetadataType;
        }
        throw new IllegalArgumentException();
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}
