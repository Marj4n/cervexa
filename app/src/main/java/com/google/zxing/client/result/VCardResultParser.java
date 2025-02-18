package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

/* loaded from: classes.dex */
final class VCardResultParser extends ResultParser {
    private VCardResultParser() {
    }

    private static String decodeQuotedPrintable(String str, String str2) {
        char charAt;
        int length = str.length();
        StringBuffer stringBuffer = new StringBuffer(length);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        while (i < length) {
            char charAt2 = str.charAt(i);
            if (charAt2 != '\n' && charAt2 != '\r') {
                if (charAt2 != '=') {
                    maybeAppendFragment(byteArrayOutputStream, str2, stringBuffer);
                    stringBuffer.append(charAt2);
                } else if (i < length - 2 && (charAt = str.charAt(i + 1)) != '\r' && charAt != '\n') {
                    i += 2;
                    try {
                        byteArrayOutputStream.write((toHexValue(charAt) * 16) + toHexValue(str.charAt(i)));
                    } catch (IllegalArgumentException unused) {
                    }
                }
            }
            i++;
        }
        maybeAppendFragment(byteArrayOutputStream, str2, stringBuffer);
        return stringBuffer.toString();
    }

    private static String formatAddress(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        StringBuffer stringBuffer = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (charAt == ';') {
                charAt = ' ';
            }
            stringBuffer.append(charAt);
        }
        return stringBuffer.toString().trim();
    }

    private static void formatNames(String[] strArr) {
        if (strArr != null) {
            for (int i = 0; i < strArr.length; i++) {
                String str = strArr[i];
                String[] strArr2 = new String[5];
                int i2 = 0;
                int i3 = 0;
                while (true) {
                    int indexOf = str.indexOf(59, i2);
                    if (indexOf > 0) {
                        strArr2[i3] = str.substring(i2, indexOf);
                        i3++;
                        i2 = indexOf + 1;
                    }
                }
                strArr2[i3] = str.substring(i2);
                StringBuffer stringBuffer = new StringBuffer(100);
                maybeAppendComponent(strArr2, 3, stringBuffer);
                maybeAppendComponent(strArr2, 1, stringBuffer);
                maybeAppendComponent(strArr2, 2, stringBuffer);
                maybeAppendComponent(strArr2, 0, stringBuffer);
                maybeAppendComponent(strArr2, 4, stringBuffer);
                strArr[i] = stringBuffer.toString().trim();
            }
        }
    }

    private static boolean isLikeVCardDate(String str) {
        if (str == null || isStringOfDigits(str, 8)) {
            return true;
        }
        return str.length() == 10 && str.charAt(4) == '-' && str.charAt(7) == '-' && isSubstringOfDigits(str, 0, 4) && isSubstringOfDigits(str, 5, 2) && isSubstringOfDigits(str, 8, 2);
    }

    static String matchSingleVCardPrefixedField(String str, String str2, boolean z) {
        String[] matchVCardPrefixedField = matchVCardPrefixedField(str, str2, z);
        if (matchVCardPrefixedField == null) {
            return null;
        }
        return matchVCardPrefixedField[0];
    }

    private static String[] matchVCardPrefixedField(String str, String str2, boolean z) {
        boolean z2;
        String str3;
        int indexOf;
        int length = str2.length();
        int i = 0;
        Vector vector = null;
        while (i < length) {
            int indexOf2 = str2.indexOf(str, i);
            if (indexOf2 < 0) {
                break;
            }
            if (indexOf2 <= 0 || str2.charAt(indexOf2 - 1) == '\n') {
                i = indexOf2 + str.length();
                char c = ';';
                if (str2.charAt(i) == ':' || str2.charAt(i) == ';') {
                    int i2 = i;
                    while (str2.charAt(i2) != ':') {
                        i2++;
                    }
                    if (i2 > i) {
                        int i3 = i + 1;
                        z2 = false;
                        str3 = null;
                        while (i3 <= i2) {
                            if (str2.charAt(i3) == c || str2.charAt(i3) == ':') {
                                String substring = str2.substring(i + 1, i3);
                                int indexOf3 = substring.indexOf(61);
                                if (indexOf3 >= 0) {
                                    String substring2 = substring.substring(0, indexOf3);
                                    String substring3 = substring.substring(indexOf3 + 1);
                                    if ("ENCODING".equalsIgnoreCase(substring2)) {
                                        if ("QUOTED-PRINTABLE".equalsIgnoreCase(substring3)) {
                                            z2 = true;
                                        }
                                    } else if ("CHARSET".equalsIgnoreCase(substring2)) {
                                        str3 = substring3;
                                    }
                                }
                                i = i3;
                            }
                            i3++;
                            c = ';';
                        }
                    } else {
                        z2 = false;
                        str3 = null;
                    }
                    int i4 = i2 + 1;
                    int i5 = i4;
                    while (true) {
                        indexOf = str2.indexOf(10, i5);
                        if (indexOf >= 0) {
                            if (indexOf < str2.length() - 1) {
                                int i6 = indexOf + 1;
                                if (str2.charAt(i6) == ' ' || str2.charAt(i6) == '\t') {
                                    i5 = indexOf + 2;
                                }
                            }
                            if (!z2 || (str2.charAt(indexOf - 1) != '=' && str2.charAt(indexOf - 2) != '=')) {
                                break;
                            }
                            i5 = indexOf + 1;
                        } else {
                            break;
                        }
                    }
                    if (indexOf < 0) {
                        i = length;
                    } else {
                        if (indexOf > i4) {
                            if (vector == null) {
                                vector = new Vector(1);
                            }
                            if (str2.charAt(indexOf - 1) == '\r') {
                                indexOf--;
                            }
                            String substring4 = str2.substring(i4, indexOf);
                            if (z) {
                                substring4 = substring4.trim();
                            }
                            vector.addElement(z2 ? decodeQuotedPrintable(substring4, str3) : stripContinuationCRLF(substring4));
                        }
                        i = indexOf + 1;
                    }
                }
            } else {
                i = indexOf2 + 1;
            }
        }
        if (vector == null || vector.isEmpty()) {
            return null;
        }
        return toStringArray(vector);
    }

    private static void maybeAppendComponent(String[] strArr, int i, StringBuffer stringBuffer) {
        if (strArr[i] != null) {
            stringBuffer.append(' ');
            stringBuffer.append(strArr[i]);
        }
    }

    private static void maybeAppendFragment(ByteArrayOutputStream byteArrayOutputStream, String str, StringBuffer stringBuffer) {
        String str2;
        if (byteArrayOutputStream.size() > 0) {
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            if (str == null) {
                str2 = new String(byteArray);
            } else {
                try {
                    str2 = new String(byteArray, str);
                } catch (UnsupportedEncodingException unused) {
                    str2 = new String(byteArray);
                }
            }
            byteArrayOutputStream.reset();
            stringBuffer.append(str2);
        }
    }

    public static AddressBookParsedResult parse(Result result) {
        String text = result.getText();
        if (text == null || !text.startsWith("BEGIN:VCARD")) {
            return null;
        }
        String[] matchVCardPrefixedField = matchVCardPrefixedField("FN", text, true);
        if (matchVCardPrefixedField == null) {
            matchVCardPrefixedField = matchVCardPrefixedField("N", text, true);
            formatNames(matchVCardPrefixedField);
        }
        String[] strArr = matchVCardPrefixedField;
        String[] matchVCardPrefixedField2 = matchVCardPrefixedField("TEL", text, true);
        String[] matchVCardPrefixedField3 = matchVCardPrefixedField("EMAIL", text, true);
        String matchSingleVCardPrefixedField = matchSingleVCardPrefixedField("NOTE", text, false);
        String[] matchVCardPrefixedField4 = matchVCardPrefixedField("ADR", text, true);
        if (matchVCardPrefixedField4 != null) {
            for (int i = 0; i < matchVCardPrefixedField4.length; i++) {
                matchVCardPrefixedField4[i] = formatAddress(matchVCardPrefixedField4[i]);
            }
        }
        String matchSingleVCardPrefixedField2 = matchSingleVCardPrefixedField("ORG", text, true);
        String matchSingleVCardPrefixedField3 = matchSingleVCardPrefixedField("BDAY", text, true);
        return new AddressBookParsedResult(strArr, null, matchVCardPrefixedField2, matchVCardPrefixedField3, matchSingleVCardPrefixedField, matchVCardPrefixedField4, matchSingleVCardPrefixedField2, !isLikeVCardDate(matchSingleVCardPrefixedField3) ? null : matchSingleVCardPrefixedField3, matchSingleVCardPrefixedField("TITLE", text, true), matchSingleVCardPrefixedField("URL", text, true));
    }

    private static String stripContinuationCRLF(String str) {
        int length = str.length();
        StringBuffer stringBuffer = new StringBuffer(length);
        boolean z = false;
        for (int i = 0; i < length; i++) {
            if (!z) {
                char charAt = str.charAt(i);
                if (charAt == '\n') {
                    z = true;
                } else if (charAt != '\r') {
                    stringBuffer.append(charAt);
                }
            }
            z = false;
        }
        return stringBuffer.toString();
    }

    private static int toHexValue(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        char c2 = 'A';
        if (c < 'A' || c > 'F') {
            c2 = 'a';
            if (c < 'a' || c > 'f') {
                throw new IllegalArgumentException();
            }
        }
        return (c - c2) + 10;
    }
}
