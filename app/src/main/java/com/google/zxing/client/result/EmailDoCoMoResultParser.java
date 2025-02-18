package com.google.zxing.client.result;

import com.google.zxing.Result;
import kotlin.text.Typography;

/* loaded from: classes.dex */
final class EmailDoCoMoResultParser extends AbstractDoCoMoResultParser {
    private static final char[] ATEXT_SYMBOLS = {'@', '.', '!', '#', Typography.dollar, '%', Typography.amp, '\'', '*', '+', '-', '/', '=', '?', '^', '_', '`', '{', '|', '}', '~'};

    EmailDoCoMoResultParser() {
    }

    private static boolean isAtextSymbol(char c) {
        int i = 0;
        while (true) {
            char[] cArr = ATEXT_SYMBOLS;
            if (i >= cArr.length) {
                return false;
            }
            if (c == cArr[i]) {
                return true;
            }
            i++;
        }
    }

    static boolean isBasicallyValidEmailAddress(String str) {
        if (str == null) {
            return false;
        }
        boolean z = false;
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if ((charAt < 'a' || charAt > 'z') && ((charAt < 'A' || charAt > 'Z') && ((charAt < '0' || charAt > '9') && !isAtextSymbol(charAt)))) {
                return false;
            }
            if (charAt == '@') {
                if (z) {
                    return false;
                }
                z = true;
            }
        }
        return z;
    }

    public static EmailAddressParsedResult parse(Result result) {
        String[] matchDoCoMoPrefixedField;
        String text = result.getText();
        if (text == null || !text.startsWith("MATMSG:") || (matchDoCoMoPrefixedField = matchDoCoMoPrefixedField("TO:", text, true)) == null) {
            return null;
        }
        String str = matchDoCoMoPrefixedField[0];
        if (!isBasicallyValidEmailAddress(str)) {
            return null;
        }
        String matchSingleDoCoMoPrefixedField = matchSingleDoCoMoPrefixedField("SUB:", text, false);
        String matchSingleDoCoMoPrefixedField2 = matchSingleDoCoMoPrefixedField("BODY:", text, false);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("mailto:");
        stringBuffer.append(str);
        return new EmailAddressParsedResult(str, matchSingleDoCoMoPrefixedField, matchSingleDoCoMoPrefixedField2, stringBuffer.toString());
    }
}
