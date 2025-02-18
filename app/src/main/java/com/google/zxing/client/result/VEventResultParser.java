package com.google.zxing.client.result;

import com.google.zxing.Result;

/* loaded from: classes.dex */
final class VEventResultParser extends ResultParser {
    private VEventResultParser() {
    }

    public static CalendarParsedResult parse(Result result) {
        double parseDouble;
        String text = result.getText();
        if (text == null || text.indexOf("BEGIN:VEVENT") < 0) {
            return null;
        }
        String matchSingleVCardPrefixedField = VCardResultParser.matchSingleVCardPrefixedField("SUMMARY", text, true);
        String matchSingleVCardPrefixedField2 = VCardResultParser.matchSingleVCardPrefixedField("DTSTART", text, true);
        String matchSingleVCardPrefixedField3 = VCardResultParser.matchSingleVCardPrefixedField("DTEND", text, true);
        String matchSingleVCardPrefixedField4 = VCardResultParser.matchSingleVCardPrefixedField("LOCATION", text, true);
        String matchSingleVCardPrefixedField5 = VCardResultParser.matchSingleVCardPrefixedField("DESCRIPTION", text, true);
        String matchSingleVCardPrefixedField6 = VCardResultParser.matchSingleVCardPrefixedField("GEO", text, true);
        double d = Double.NaN;
        if (matchSingleVCardPrefixedField6 == null) {
            parseDouble = Double.NaN;
        } else {
            int indexOf = matchSingleVCardPrefixedField6.indexOf(59);
            try {
                d = Double.parseDouble(matchSingleVCardPrefixedField6.substring(0, indexOf));
                parseDouble = Double.parseDouble(matchSingleVCardPrefixedField6.substring(indexOf + 1));
            } catch (NumberFormatException | IllegalArgumentException unused) {
                return null;
            }
        }
        return new CalendarParsedResult(matchSingleVCardPrefixedField, matchSingleVCardPrefixedField2, matchSingleVCardPrefixedField3, matchSingleVCardPrefixedField4, null, matchSingleVCardPrefixedField5, d, parseDouble);
    }
}
