package com.google.zxing.client.result;

import com.google.zxing.Result;

/* loaded from: classes.dex */
final class BookmarkDoCoMoResultParser extends AbstractDoCoMoResultParser {
    private BookmarkDoCoMoResultParser() {
    }

    public static URIParsedResult parse(Result result) {
        String text = result.getText();
        if (text == null || !text.startsWith("MEBKM:")) {
            return null;
        }
        String matchSingleDoCoMoPrefixedField = matchSingleDoCoMoPrefixedField("TITLE:", text, true);
        String[] matchDoCoMoPrefixedField = matchDoCoMoPrefixedField("URL:", text, true);
        if (matchDoCoMoPrefixedField == null) {
            return null;
        }
        String str = matchDoCoMoPrefixedField[0];
        if (URIResultParser.isBasicallyValidURI(str)) {
            return new URIParsedResult(str, matchSingleDoCoMoPrefixedField);
        }
        return null;
    }
}
