package com.google.zxing.client.result;

import com.google.zxing.Result;

/* loaded from: classes.dex */
final class TelResultParser extends ResultParser {
    private TelResultParser() {
    }

    public static TelParsedResult parse(Result result) {
        String str;
        String text = result.getText();
        if (text == null || !(text.startsWith("tel:") || text.startsWith("TEL:"))) {
            return null;
        }
        if (text.startsWith("TEL:")) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("tel:");
            stringBuffer.append(text.substring(4));
            str = stringBuffer.toString();
        } else {
            str = text;
        }
        int indexOf = text.indexOf(63, 4);
        return new TelParsedResult(indexOf < 0 ? text.substring(4) : text.substring(4, indexOf), str, null);
    }
}
