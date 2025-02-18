package com.google.zxing.client.result;

import com.github.mikephil.charting.utils.Utils;
import com.google.zxing.Result;

/* loaded from: classes.dex */
final class GeoResultParser extends ResultParser {
    private GeoResultParser() {
    }

    public static GeoParsedResult parse(Result result) {
        String substring;
        String str;
        double parseDouble;
        double d;
        String text = result.getText();
        if (text != null && (text.startsWith("geo:") || text.startsWith("GEO:"))) {
            int indexOf = text.indexOf(63, 4);
            if (indexOf < 0) {
                substring = text.substring(4);
                str = null;
            } else {
                String substring2 = text.substring(indexOf + 1);
                substring = text.substring(4, indexOf);
                str = substring2;
            }
            int indexOf2 = substring.indexOf(44);
            if (indexOf2 < 0) {
                return null;
            }
            int i = indexOf2 + 1;
            int indexOf3 = substring.indexOf(44, i);
            try {
                double parseDouble2 = Double.parseDouble(substring.substring(0, indexOf2));
                if (parseDouble2 <= 90.0d && parseDouble2 >= -90.0d) {
                    if (indexOf3 < 0) {
                        d = Double.parseDouble(substring.substring(i));
                        parseDouble = 0.0d;
                    } else {
                        double parseDouble3 = Double.parseDouble(substring.substring(i, indexOf3));
                        parseDouble = Double.parseDouble(substring.substring(indexOf3 + 1));
                        d = parseDouble3;
                    }
                    if (d <= 180.0d && d >= -180.0d && parseDouble >= Utils.DOUBLE_EPSILON) {
                        return new GeoParsedResult(parseDouble2, d, parseDouble, str);
                    }
                }
            } catch (NumberFormatException unused) {
            }
        }
        return null;
    }
}
