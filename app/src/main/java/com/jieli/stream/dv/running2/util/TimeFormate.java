package com.jieli.stream.dv.running2.util;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/* loaded from: classes.dex */
public class TimeFormate {
    public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static final SimpleDateFormat yyyyMMdd_format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    public static final SimpleDateFormat yyyyMMddHHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    public static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat yyyyMMdd_HHmmss = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());

    public enum DateType {
        YEAR,
        MONTH,
        DAY,
        HOUR,
        MIN,
        SEC,
        TIME
    }

    public static String formatYMD(long j) {
        return formatYMD(new Date(j));
    }

    public static String formatYMD(Date date) {
        return yyyyMMdd.format(date);
    }

    public static String formatYMD(Calendar calendar) {
        return formatYMD(calendar.getTime());
    }

    public static String formatToYMD(long j) {
        return formatToYMD(new Date(j));
    }

    public static String formatToYMD(Date date) {
        return yyyyMMdd_format.format(date);
    }

    public static String formatToYMD(Calendar calendar) {
        return formatToYMD(calendar.getTime());
    }

    public static String formatYMDHM(long j) {
        return formatYMDHM(new Date(j));
    }

    public static String formatYMDHM(Date date) {
        return yyyyMMddHHmm.format(date);
    }

    public static String formatYMDHM(Calendar calendar) {
        return formatYMDHM(calendar.getTime());
    }

    public static String formatYMDHMS(long j) {
        return formatYMDHMS(new Date(j));
    }

    public static String formatYMDHMS(Date date) {
        return yyyyMMddHHmmss.format(date);
    }

    public static String formatYMD_HMS(long j) {
        return formatYMD_HMS(new Date(j));
    }

    public static String formatYMD_HMS(Date date) {
        return yyyyMMdd_HHmmss.format(date);
    }

    public static String formatYMDHMS(Calendar calendar) {
        return formatYMDHMS(calendar.getTime());
    }

    public static String getTimeFormatValue(int i) {
        int i2 = i / 60;
        int i3 = (i2 / 60) % 24;
        int i4 = i2 % 60;
        int i5 = i % 60;
        return i3 == 0 ? MessageFormat.format("{0,number,00}:{1,number,00}", Integer.valueOf(i4), Integer.valueOf(i5)) : MessageFormat.format("{0,number,00}:{1,number,00}:{2,number,00}", Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5));
    }

    public static String getHHMMSSFormatValue(int i) {
        long j = i / 1000;
        long j2 = j / 60;
        return MessageFormat.format("{0,number,00}:{1,number,00}:{2,number,00}", Long.valueOf(j2 / 60), Long.valueOf(j2 % 60), Long.valueOf(j % 60));
    }

    public static String getFormatedDateString(int i) {
        TimeZone simpleTimeZone;
        if (i > 13 || i < -12) {
            i = 0;
        }
        int i2 = i * 60 * 60 * 1000;
        String[] availableIDs = TimeZone.getAvailableIDs(i2);
        if (availableIDs.length == 0) {
            simpleTimeZone = TimeZone.getDefault();
        } else {
            simpleTimeZone = new SimpleTimeZone(i2, availableIDs[0]);
        }
        SimpleDateFormat simpleDateFormat = yyyyMMddHHmmss;
        simpleDateFormat.setTimeZone(simpleTimeZone);
        return simpleDateFormat.format(new Date());
    }

    public static long getDateMillTime(String str, SimpleDateFormat simpleDateFormat) {
        if (str == null || str.isEmpty() || simpleDateFormat == null) {
            return 0L;
        }
        Date date = null;
        try {
            date = simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            return date.getTime();
        }
        return 0L;
    }

    public static String getDateTime(String str, SimpleDateFormat simpleDateFormat, DateType dateType) {
        Date date;
        if (str == null || str.isEmpty() || simpleDateFormat == null) {
            return null;
        }
        try {
            date = simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            date = null;
        }
        if (date == null) {
            return null;
        }
        switch (AnonymousClass1.$SwitchMap$com$jieli$stream$dv$running2$util$TimeFormate$DateType[dateType.ordinal()]) {
            case 1:
                return String.valueOf(date.getYear() + 1900);
            case 2:
                return String.valueOf(date.getMonth() + 1);
            case 3:
                return String.valueOf(date.getDate());
            case 4:
                return String.valueOf(date.getHours());
            case 5:
                return String.valueOf(date.getMinutes());
            case 6:
                return String.valueOf(date.getSeconds());
            case 7:
                return getInt2TwoByte(date.getHours()) + ":" + getInt2TwoByte(date.getMinutes()) + ":" + getInt2TwoByte(date.getSeconds());
            default:
                return null;
        }
    }

    /* renamed from: com.jieli.stream.dv.running2.util.TimeFormate$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$jieli$stream$dv$running2$util$TimeFormate$DateType;

        static {
            int[] iArr = new int[DateType.values().length];
            $SwitchMap$com$jieli$stream$dv$running2$util$TimeFormate$DateType = iArr;
            try {
                iArr[DateType.YEAR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$jieli$stream$dv$running2$util$TimeFormate$DateType[DateType.MONTH.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$jieli$stream$dv$running2$util$TimeFormate$DateType[DateType.DAY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$jieli$stream$dv$running2$util$TimeFormate$DateType[DateType.HOUR.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$jieli$stream$dv$running2$util$TimeFormate$DateType[DateType.MIN.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$jieli$stream$dv$running2$util$TimeFormate$DateType[DateType.SEC.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$jieli$stream$dv$running2$util$TimeFormate$DateType[DateType.TIME.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public static String getFormatedDateTime(SimpleDateFormat simpleDateFormat, long j) {
        if (simpleDateFormat == null || j < 0) {
            return null;
        }
        return simpleDateFormat.format(new Date(j));
    }

    private static String getInt2TwoByte(int i) {
        String valueOf = String.valueOf(i);
        if (i >= 10) {
            return valueOf;
        }
        return "0" + i;
    }
}
