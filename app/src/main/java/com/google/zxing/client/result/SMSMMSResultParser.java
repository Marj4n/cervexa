package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.Hashtable;
import java.util.Vector;

/* loaded from: classes.dex */
final class SMSMMSResultParser extends ResultParser {
    private SMSMMSResultParser() {
    }

    private static void addNumberVia(Vector vector, Vector vector2, String str) {
        int indexOf = str.indexOf(59);
        String str2 = null;
        if (indexOf < 0) {
            vector.addElement(str);
        } else {
            vector.addElement(str.substring(0, indexOf));
            String substring = str.substring(indexOf + 1);
            if (substring.startsWith("via=")) {
                str2 = substring.substring(4);
            }
        }
        vector2.addElement(str2);
    }

    public static SMSParsedResult parse(Result result) {
        String str;
        String text = result.getText();
        String str2 = null;
        if (text == null) {
            return null;
        }
        if (!text.startsWith("sms:") && !text.startsWith("SMS:") && !text.startsWith("mms:") && !text.startsWith("MMS:")) {
            return null;
        }
        Hashtable parseNameValuePairs = parseNameValuePairs(text);
        boolean z = false;
        if (parseNameValuePairs == null || parseNameValuePairs.isEmpty()) {
            str = null;
        } else {
            str2 = (String) parseNameValuePairs.get("subject");
            str = (String) parseNameValuePairs.get("body");
            z = true;
        }
        int indexOf = text.indexOf(63, 4);
        String substring = (indexOf < 0 || !z) ? text.substring(4) : text.substring(4, indexOf);
        int i = -1;
        Vector vector = new Vector(1);
        Vector vector2 = new Vector(1);
        while (true) {
            int i2 = i + 1;
            int indexOf2 = substring.indexOf(44, i2);
            if (indexOf2 <= i) {
                addNumberVia(vector, vector2, substring.substring(i2));
                return new SMSParsedResult(toStringArray(vector), toStringArray(vector2), str2, str);
            }
            addNumberVia(vector, vector2, substring.substring(i2, indexOf2));
            i = indexOf2;
        }
    }
}
