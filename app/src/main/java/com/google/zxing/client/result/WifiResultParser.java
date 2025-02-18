package com.google.zxing.client.result;

import com.google.zxing.Result;

/* loaded from: classes.dex */
final class WifiResultParser extends ResultParser {
    private WifiResultParser() {
    }

    public static WifiParsedResult parse(Result result) {
        String text = result.getText();
        if (text == null || !text.startsWith("WIFI:")) {
            return null;
        }
        return new WifiParsedResult(matchSinglePrefixedField("T:", text, ';', false), matchSinglePrefixedField("S:", text, ';', false), matchSinglePrefixedField("P:", text, ';', false));
    }
}
