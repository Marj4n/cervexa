package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.Vector;

/* loaded from: classes.dex */
final class BizcardResultParser extends AbstractDoCoMoResultParser {
    BizcardResultParser() {
    }

    private static String buildName(String str, String str2) {
        if (str == null) {
            return str2;
        }
        if (str2 == null) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(' ');
        stringBuffer.append(str2);
        return stringBuffer.toString();
    }

    private static String[] buildPhoneNumbers(String str, String str2, String str3) {
        Vector vector = new Vector(3);
        if (str != null) {
            vector.addElement(str);
        }
        if (str2 != null) {
            vector.addElement(str2);
        }
        if (str3 != null) {
            vector.addElement(str3);
        }
        int size = vector.size();
        if (size == 0) {
            return null;
        }
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            strArr[i] = (String) vector.elementAt(i);
        }
        return strArr;
    }

    public static AddressBookParsedResult parse(Result result) {
        String text = result.getText();
        if (text == null || !text.startsWith("BIZCARD:")) {
            return null;
        }
        String buildName = buildName(matchSingleDoCoMoPrefixedField("N:", text, true), matchSingleDoCoMoPrefixedField("X:", text, true));
        String matchSingleDoCoMoPrefixedField = matchSingleDoCoMoPrefixedField("T:", text, true);
        String matchSingleDoCoMoPrefixedField2 = matchSingleDoCoMoPrefixedField("C:", text, true);
        return new AddressBookParsedResult(maybeWrap(buildName), null, buildPhoneNumbers(matchSingleDoCoMoPrefixedField("B:", text, true), matchSingleDoCoMoPrefixedField("M:", text, true), matchSingleDoCoMoPrefixedField("F:", text, true)), maybeWrap(matchSingleDoCoMoPrefixedField("E:", text, true)), null, matchDoCoMoPrefixedField("A:", text, true), matchSingleDoCoMoPrefixedField2, null, matchSingleDoCoMoPrefixedField, null);
    }
}
