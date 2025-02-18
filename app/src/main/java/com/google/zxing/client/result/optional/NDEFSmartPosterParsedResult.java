package com.google.zxing.client.result.optional;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

/* loaded from: classes.dex */
public final class NDEFSmartPosterParsedResult extends ParsedResult {
    public static final int ACTION_DO = 0;
    public static final int ACTION_OPEN = 2;
    public static final int ACTION_SAVE = 1;
    public static final int ACTION_UNSPECIFIED = -1;
    private final int action;
    private final String title;
    private final String uri;

    NDEFSmartPosterParsedResult(int i, String str, String str2) {
        super(ParsedResultType.NDEF_SMART_POSTER);
        this.action = i;
        this.uri = str;
        this.title = str2;
    }

    public int getAction() {
        return this.action;
    }

    @Override // com.google.zxing.client.result.ParsedResult
    public String getDisplayResult() {
        if (this.title == null) {
            return this.uri;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.title);
        stringBuffer.append('\n');
        stringBuffer.append(this.uri);
        return stringBuffer.toString();
    }

    public String getTitle() {
        return this.title;
    }

    public String getURI() {
        return this.uri;
    }
}
