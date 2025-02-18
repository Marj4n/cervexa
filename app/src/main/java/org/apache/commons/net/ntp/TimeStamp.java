package org.apache.commons.net.ntp;

import com.jieli.stream.dv.running2.util.IConstant;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes2.dex */
public class TimeStamp implements Serializable, Comparable<TimeStamp> {
    public static final String NTP_DATE_FORMAT = "EEE, MMM dd yyyy HH:mm:ss.SSS";
    protected static final long msb0baseTime = 2085978496000L;
    protected static final long msb1baseTime = -2208988800000L;
    private static final long serialVersionUID = 8139806907588338737L;
    private final long ntpTime;
    private DateFormat simpleFormatter;
    private DateFormat utcFormatter;

    public TimeStamp(long j) {
        this.ntpTime = j;
    }

    public TimeStamp(String str) throws NumberFormatException {
        this.ntpTime = decodeNtpHexString(str);
    }

    public TimeStamp(Date date) {
        this.ntpTime = date == null ? 0L : toNtpTime(date.getTime());
    }

    public long ntpValue() {
        return this.ntpTime;
    }

    public long getSeconds() {
        return (this.ntpTime >>> 32) & 4294967295L;
    }

    public long getFraction() {
        return this.ntpTime & 4294967295L;
    }

    public long getTime() {
        return getTime(this.ntpTime);
    }

    public Date getDate() {
        return new Date(getTime(this.ntpTime));
    }

    public static long getTime(long j) {
        long j2 = (j >>> 32) & 4294967295L;
        return (j2 * 1000) + ((IjkMediaMeta.AV_CH_WIDE_LEFT & j2) == 0 ? msb0baseTime : msb1baseTime) + Math.round(((j & 4294967295L) * 1000.0d) / 4.294967296E9d);
    }

    public static TimeStamp getNtpTime(long j) {
        return new TimeStamp(toNtpTime(j));
    }

    public static TimeStamp getCurrentTime() {
        return getNtpTime(System.currentTimeMillis());
    }

    protected static long decodeNtpHexString(String str) throws NumberFormatException {
        if (str == null) {
            throw new NumberFormatException(IConstant.DEFAULT_PATH);
        }
        int indexOf = str.indexOf(46);
        if (indexOf == -1) {
            if (str.length() == 0) {
                return 0L;
            }
            return Long.parseLong(str, 16) << 32;
        }
        return (Long.parseLong(str.substring(0, indexOf), 16) << 32) | Long.parseLong(str.substring(indexOf + 1), 16);
    }

    public static TimeStamp parseNtpString(String str) throws NumberFormatException {
        return new TimeStamp(decodeNtpHexString(str));
    }

    protected static long toNtpTime(long j) {
        long j2 = msb0baseTime;
        boolean z = j < msb0baseTime;
        if (z) {
            j2 = msb1baseTime;
        }
        long j3 = j - j2;
        long j4 = j3 / 1000;
        long j5 = ((j3 % 1000) * IjkMediaMeta.AV_CH_WIDE_RIGHT) / 1000;
        if (z) {
            j4 |= IjkMediaMeta.AV_CH_WIDE_LEFT;
        }
        return j5 | (j4 << 32);
    }

    public int hashCode() {
        long j = this.ntpTime;
        return (int) (j ^ (j >>> 32));
    }

    public boolean equals(Object obj) {
        return (obj instanceof TimeStamp) && this.ntpTime == ((TimeStamp) obj).ntpValue();
    }

    public String toString() {
        return toString(this.ntpTime);
    }

    private static void appendHexString(StringBuilder sb, long j) {
        String hexString = Long.toHexString(j);
        for (int length = hexString.length(); length < 8; length++) {
            sb.append('0');
        }
        sb.append(hexString);
    }

    public static String toString(long j) {
        StringBuilder sb = new StringBuilder();
        appendHexString(sb, (j >>> 32) & 4294967295L);
        sb.append('.');
        appendHexString(sb, j & 4294967295L);
        return sb.toString();
    }

    public String toDateString() {
        if (this.simpleFormatter == null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(NTP_DATE_FORMAT, Locale.US);
            this.simpleFormatter = simpleDateFormat;
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
        }
        return this.simpleFormatter.format(getDate());
    }

    public String toUTCString() {
        if (this.utcFormatter == null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss.SSS 'UTC'", Locale.US);
            this.utcFormatter = simpleDateFormat;
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return this.utcFormatter.format(getDate());
    }

    @Override // java.lang.Comparable
    public int compareTo(TimeStamp timeStamp) {
        long j = this.ntpTime;
        long j2 = timeStamp.ntpTime;
        if (j < j2) {
            return -1;
        }
        return j == j2 ? 0 : 1;
    }
}
