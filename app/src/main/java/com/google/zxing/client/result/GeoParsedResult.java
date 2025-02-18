package com.google.zxing.client.result;

import com.github.mikephil.charting.utils.Utils;

/* loaded from: classes.dex */
public final class GeoParsedResult extends ParsedResult {
    private final double altitude;
    private final double latitude;
    private final double longitude;
    private final String query;

    GeoParsedResult(double d, double d2, double d3, String str) {
        super(ParsedResultType.GEO);
        this.latitude = d;
        this.longitude = d2;
        this.altitude = d3;
        this.query = str;
    }

    public double getAltitude() {
        return this.altitude;
    }

    @Override // com.google.zxing.client.result.ParsedResult
    public String getDisplayResult() {
        StringBuffer stringBuffer = new StringBuffer(20);
        stringBuffer.append(this.latitude);
        stringBuffer.append(", ");
        stringBuffer.append(this.longitude);
        if (this.altitude > Utils.DOUBLE_EPSILON) {
            stringBuffer.append(", ");
            stringBuffer.append(this.altitude);
            stringBuffer.append('m');
        }
        if (this.query != null) {
            stringBuffer.append(" (");
            stringBuffer.append(this.query);
            stringBuffer.append(')');
        }
        return stringBuffer.toString();
    }

    public String getGeoURI() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("geo:");
        stringBuffer.append(this.latitude);
        stringBuffer.append(',');
        stringBuffer.append(this.longitude);
        if (this.altitude > Utils.DOUBLE_EPSILON) {
            stringBuffer.append(',');
            stringBuffer.append(this.altitude);
        }
        if (this.query != null) {
            stringBuffer.append('?');
            stringBuffer.append(this.query);
        }
        return stringBuffer.toString();
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String getQuery() {
        return this.query;
    }
}
