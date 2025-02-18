package com.google.zxing.client.result;

import com.google.zxing.Result;

/* loaded from: classes.dex */
final class SMTPResultParser {
    private SMTPResultParser() {
    }

    public static EmailAddressParsedResult parse(Result result) {
        String str;
        String text = result.getText();
        String str2 = null;
        if (text == null) {
            return null;
        }
        if (!text.startsWith("smtp:") && !text.startsWith("SMTP:")) {
            return null;
        }
        String substring = text.substring(5);
        int indexOf = substring.indexOf(58);
        if (indexOf >= 0) {
            String substring2 = substring.substring(indexOf + 1);
            substring = substring.substring(0, indexOf);
            int indexOf2 = substring2.indexOf(58);
            if (indexOf2 >= 0) {
                String substring3 = substring2.substring(indexOf2 + 1);
                String substring4 = substring2.substring(0, indexOf2);
                str = substring3;
                str2 = substring4;
            } else {
                str = null;
                str2 = substring2;
            }
        } else {
            str = null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("mailto:");
        stringBuffer.append(substring);
        return new EmailAddressParsedResult(substring, str2, str, stringBuffer.toString());
    }
}
