package com.google.zxing.client.result;

import com.google.zxing.Result;

/* loaded from: classes.dex */
final class AddressBookDoCoMoResultParser extends AbstractDoCoMoResultParser {
    AddressBookDoCoMoResultParser() {
    }

    public static AddressBookParsedResult parse(Result result) {
        String[] matchDoCoMoPrefixedField;
        String text = result.getText();
        if (text == null || !text.startsWith("MECARD:") || (matchDoCoMoPrefixedField = matchDoCoMoPrefixedField("N:", text, true)) == null) {
            return null;
        }
        String parseName = parseName(matchDoCoMoPrefixedField[0]);
        String matchSingleDoCoMoPrefixedField = matchSingleDoCoMoPrefixedField("SOUND:", text, true);
        String[] matchDoCoMoPrefixedField2 = matchDoCoMoPrefixedField("TEL:", text, true);
        String[] matchDoCoMoPrefixedField3 = matchDoCoMoPrefixedField("EMAIL:", text, true);
        String matchSingleDoCoMoPrefixedField2 = matchSingleDoCoMoPrefixedField("NOTE:", text, false);
        String[] matchDoCoMoPrefixedField4 = matchDoCoMoPrefixedField("ADR:", text, true);
        String matchSingleDoCoMoPrefixedField3 = matchSingleDoCoMoPrefixedField("BDAY:", text, true);
        return new AddressBookParsedResult(maybeWrap(parseName), matchSingleDoCoMoPrefixedField, matchDoCoMoPrefixedField2, matchDoCoMoPrefixedField3, matchSingleDoCoMoPrefixedField2, matchDoCoMoPrefixedField4, matchSingleDoCoMoPrefixedField("ORG:", text, true), (matchSingleDoCoMoPrefixedField3 == null || isStringOfDigits(matchSingleDoCoMoPrefixedField3, 8)) ? matchSingleDoCoMoPrefixedField3 : null, null, matchSingleDoCoMoPrefixedField("URL:", text, true));
    }

    private static String parseName(String str) {
        int indexOf = str.indexOf(44);
        if (indexOf < 0) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str.substring(indexOf + 1));
        stringBuffer.append(' ');
        stringBuffer.append(str.substring(0, indexOf));
        return stringBuffer.toString();
    }
}
