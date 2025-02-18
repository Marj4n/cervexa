package com.google.zxing.client.result;

/* loaded from: classes.dex */
public final class CalendarParsedResult extends ParsedResult {
    private final String attendee;
    private final String description;
    private final String end;
    private final double latitude;
    private final String location;
    private final double longitude;
    private final String start;
    private final String summary;

    public CalendarParsedResult(String str, String str2, String str3, String str4, String str5, String str6) {
        this(str, str2, str3, str4, str5, str6, Double.NaN, Double.NaN);
    }

    public CalendarParsedResult(String str, String str2, String str3, String str4, String str5, String str6, double d, double d2) {
        super(ParsedResultType.CALENDAR);
        if (str2 == null) {
            throw new IllegalArgumentException();
        }
        validateDate(str2);
        if (str3 == null) {
            str3 = str2;
        } else {
            validateDate(str3);
        }
        this.summary = str;
        this.start = str2;
        this.end = str3;
        this.location = str4;
        this.attendee = str5;
        this.description = str6;
        this.latitude = d;
        this.longitude = d2;
    }

    private static void validateDate(String str) {
        if (str != null) {
            int length = str.length();
            if (length != 8 && length != 15 && length != 16) {
                throw new IllegalArgumentException();
            }
            for (int i = 0; i < 8; i++) {
                if (!Character.isDigit(str.charAt(i))) {
                    throw new IllegalArgumentException();
                }
            }
            if (length > 8) {
                if (str.charAt(8) != 'T') {
                    throw new IllegalArgumentException();
                }
                for (int i2 = 9; i2 < 15; i2++) {
                    if (!Character.isDigit(str.charAt(i2))) {
                        throw new IllegalArgumentException();
                    }
                }
                if (length == 16 && str.charAt(15) != 'Z') {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    public String getAttendee() {
        return this.attendee;
    }

    public String getDescription() {
        return this.description;
    }

    @Override // com.google.zxing.client.result.ParsedResult
    public String getDisplayResult() {
        StringBuffer stringBuffer = new StringBuffer(100);
        maybeAppend(this.summary, stringBuffer);
        maybeAppend(this.start, stringBuffer);
        maybeAppend(this.end, stringBuffer);
        maybeAppend(this.location, stringBuffer);
        maybeAppend(this.attendee, stringBuffer);
        maybeAppend(this.description, stringBuffer);
        return stringBuffer.toString();
    }

    public String getEnd() {
        return this.end;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public String getLocation() {
        return this.location;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String getStart() {
        return this.start;
    }

    public String getSummary() {
        return this.summary;
    }
}
