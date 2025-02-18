package com.google.zxing.client.result;

/* loaded from: classes.dex */
public abstract class ParsedResult {
    private final ParsedResultType type;

    protected ParsedResult(ParsedResultType parsedResultType) {
        this.type = parsedResultType;
    }

    public static void maybeAppend(String str, StringBuffer stringBuffer) {
        if (str == null || str.length() <= 0) {
            return;
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.append('\n');
        }
        stringBuffer.append(str);
    }

    public static void maybeAppend(String[] strArr, StringBuffer stringBuffer) {
        if (strArr != null) {
            for (int i = 0; i < strArr.length; i++) {
                if (strArr[i] != null && strArr[i].length() > 0) {
                    if (stringBuffer.length() > 0) {
                        stringBuffer.append('\n');
                    }
                    stringBuffer.append(strArr[i]);
                }
            }
        }
    }

    public abstract String getDisplayResult();

    public ParsedResultType getType() {
        return this.type;
    }

    public String toString() {
        return getDisplayResult();
    }
}
