package com.google.zxing.client.result;

/* loaded from: classes.dex */
public final class URIParsedResult extends ParsedResult {
    private final String title;
    private final String uri;

    public URIParsedResult(String str, String str2) {
        super(ParsedResultType.URI);
        this.uri = massageURI(str);
        this.title = str2;
    }

    private boolean containsUser() {
        int indexOf = this.uri.indexOf(58) + 1;
        int length = this.uri.length();
        while (indexOf < length && this.uri.charAt(indexOf) == '/') {
            indexOf++;
        }
        int indexOf2 = this.uri.indexOf(47, indexOf);
        if (indexOf2 >= 0) {
            length = indexOf2;
        }
        int indexOf3 = this.uri.indexOf(64, indexOf);
        return indexOf3 >= indexOf && indexOf3 < length;
    }

    private static boolean isColonFollowedByPortNumber(String str, int i) {
        int i2 = i + 1;
        int indexOf = str.indexOf(47, i2);
        if (indexOf < 0) {
            indexOf = str.length();
        }
        if (indexOf <= i2) {
            return false;
        }
        while (i2 < indexOf) {
            if (str.charAt(i2) < '0' || str.charAt(i2) > '9') {
                return false;
            }
            i2++;
        }
        return true;
    }

    private static String massageURI(String str) {
        StringBuffer stringBuffer;
        String trim = str.trim();
        int indexOf = trim.indexOf(58);
        if (indexOf < 0) {
            stringBuffer = new StringBuffer();
        } else {
            if (!isColonFollowedByPortNumber(trim, indexOf)) {
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append(trim.substring(0, indexOf).toLowerCase());
                stringBuffer2.append(trim.substring(indexOf));
                return stringBuffer2.toString();
            }
            stringBuffer = new StringBuffer();
        }
        stringBuffer.append("http://");
        stringBuffer.append(trim);
        return stringBuffer.toString();
    }

    @Override // com.google.zxing.client.result.ParsedResult
    public String getDisplayResult() {
        StringBuffer stringBuffer = new StringBuffer(30);
        maybeAppend(this.title, stringBuffer);
        maybeAppend(this.uri, stringBuffer);
        return stringBuffer.toString();
    }

    public String getTitle() {
        return this.title;
    }

    public String getURI() {
        return this.uri;
    }

    public boolean isPossiblyMaliciousURI() {
        return containsUser();
    }
}
