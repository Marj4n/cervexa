package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

/* loaded from: classes.dex */
public class ISBNResultParser extends ResultParser {
    private ISBNResultParser() {
    }

    public static ISBNParsedResult parse(Result result) {
        String text;
        if (!BarcodeFormat.EAN_13.equals(result.getBarcodeFormat()) || (text = result.getText()) == null || text.length() != 13) {
            return null;
        }
        if (text.startsWith("978") || text.startsWith("979")) {
            return new ISBNParsedResult(text);
        }
        return null;
    }
}
