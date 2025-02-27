package com.jieli.stream.dv.running2.baidu.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import com.baidu.mapapi.model.LatLng;
import com.github.mikephil.charting.utils.Utils;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes.dex */
public class CommonUtil {
    public static final double DISTANCE = 1.0E-4d;
    private static DecimalFormat df = new DecimalFormat("######0.00");

    public static String getCurProcessName(Context context) {
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses()) {
            if (runningAppProcessInfo.pid == myPid) {
                return runningAppProcessInfo.processName;
            }
        }
        return "";
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static boolean isEqualToZero(double d) {
        return Math.abs(d - Utils.DOUBLE_EPSILON) < 0.01d;
    }

    public static boolean isZeroPoint(double d, double d2) {
        return isEqualToZero(d) && isEqualToZero(d2);
    }

    public static long toTimeStamp(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(str).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static String getHMS(long j) {
        try {
            return new SimpleDateFormat("HH:mm:ss").format((Date) new Timestamp(j));
        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(j);
        }
    }

    public static String formatTime(long j) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) new Timestamp(j));
        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(j);
        }
    }

    public static String formatSecond(int i) {
        Integer valueOf = Integer.valueOf(i / 3600);
        Integer valueOf2 = Integer.valueOf((i / 60) - (valueOf.intValue() * 60));
        return String.format("%1$,02d:%2$,02d:%3$,02d", valueOf, valueOf2, Integer.valueOf((i - (valueOf2.intValue() * 60)) - ((valueOf.intValue() * 60) * 60)));
    }

    public static final String formatDouble(double d) {
        return df.format(d);
    }

    public static double getXMoveDistance(double d) {
        if (d == Double.MAX_VALUE) {
            return 1.0E-4d;
        }
        return Math.abs((1.0E-4d * d) / Math.sqrt((d * d) + 1.0d));
    }

    public static double getInterception(double d, LatLng latLng) {
        return latLng.latitude - (d * latLng.longitude);
    }

    public static double getSlope(LatLng latLng, LatLng latLng2) {
        if (latLng2.longitude == latLng.longitude) {
            return Double.MAX_VALUE;
        }
        return (latLng2.latitude - latLng.latitude) / (latLng2.longitude - latLng.longitude);
    }

    public static double getAngle(LatLng latLng, LatLng latLng2) {
        double slope = getSlope(latLng, latLng2);
        if (slope != Double.MAX_VALUE) {
            return (((Math.atan(slope) / 3.141592653589793d) * 180.0d) + ((latLng2.latitude - latLng.latitude) * slope < Utils.DOUBLE_EPSILON ? 180.0f : 0.0f)) - 90.0d;
        }
        if (latLng2.latitude > latLng.latitude) {
            return Utils.DOUBLE_EPSILON;
        }
        return 180.0d;
    }
}
