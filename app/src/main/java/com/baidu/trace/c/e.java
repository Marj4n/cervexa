package com.baidu.trace.c;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.baidu.trace.TraceJniInterface;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import com.baidu.trace.model.TraceLocation;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public final class e {
    public static String a;
    public static String b;
    public static String c;
    public static int d;
    private static final Pattern e = Pattern.compile("^(?!_)[a-zA-Z0-9_\\-]{1,128}");
    private static final Pattern f = Pattern.compile("[a-zA-Z0-9_\\-一-龥]{1,128}");
    private static final SimpleDateFormat g;
    private static List<ScanResult> h;
    private static List<ScanResult> i;
    private static WifiManager j;
    private static boolean k;

    static {
        new DecimalFormat("#0.##");
        g = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        j = null;
        a = "4G";
        b = "yingyan";
        c = "com.baidu.trace";
        d = 0;
        k = false;
    }

    private static double a(double d2) {
        return (d2 * 3.141592653589793d) / 180.0d;
    }

    public static double a(LatLng latLng, LatLng latLng2) {
        double a2 = a(latLng.latitude);
        double a3 = a(latLng2.latitude);
        return Math.round(((Math.asin(Math.sqrt(Math.pow(Math.sin((a2 - a3) / 2.0d), 2.0d) + ((Math.cos(a2) * Math.cos(a3)) * Math.pow(Math.sin((a(latLng.longitude) - a(latLng2.longitude)) / 2.0d), 2.0d)))) * 2.0d) * 6378.137d) * 10000.0d) / 10;
    }

    public static int a() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static void a(int i2, int i3, TreeMap<String, String> treeMap) {
        if (i2 > 0) {
            treeMap.put("page_index", String.valueOf(i2));
        }
        if (i3 > 0) {
            treeMap.put("page_size", String.valueOf(i3));
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static void a(Context context) {
        NetworkInfo activeNetworkInfo;
        String str;
        if (k) {
            return;
        }
        b = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager != null && (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) != null && activeNetworkInfo.isAvailable()) {
            if (1 != activeNetworkInfo.getType()) {
                if (activeNetworkInfo.getType() == 0) {
                    String subtypeName = activeNetworkInfo.getSubtypeName();
                    switch (activeNetworkInfo.getSubtype()) {
                        case 1:
                        case 2:
                        case 4:
                        case 7:
                        case 11:
                            str = "2G";
                            break;
                        case 3:
                        case 5:
                        case 6:
                        case 8:
                        case 9:
                        case 10:
                        case 12:
                        case 14:
                        case 15:
                            a = "3G";
                            break;
                        case 13:
                            str = "4G";
                            break;
                        default:
                            if (!subtypeName.equalsIgnoreCase("TD-SCDMA") && !subtypeName.equalsIgnoreCase("WCDMA") && !subtypeName.equalsIgnoreCase("CDMA2000")) {
                                a = subtypeName;
                                break;
                            }
                            a = "3G";
                            break;
                    }
                }
            } else {
                str = "WIFI";
            }
            a = str;
        }
        c(context);
        k = true;
    }

    public static void a(TraceLocation traceLocation) {
        CoordType coordType;
        double[] wgsToBaidu = TraceJniInterface.wgsToBaidu(traceLocation.getLatitude(), traceLocation.getLongitude());
        if (wgsToBaidu == null || 2 != wgsToBaidu.length) {
            coordType = CoordType.wgs84;
        } else {
            traceLocation.setLatitude(wgsToBaidu[0]);
            traceLocation.setLongitude(wgsToBaidu[1]);
            coordType = CoordType.bd09ll;
        }
        traceLocation.setCoordType(coordType);
    }

    public static boolean a(int i2, int i3, int i4) {
        return i2 >= i3 && i2 <= i4;
    }

    public static boolean a(Context context, String str) {
        List<ActivityManager.RunningServiceInfo> runningServices;
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager == null || (runningServices = activityManager.getRunningServices(128)) == null) {
            return false;
        }
        Iterator<ActivityManager.RunningServiceInfo> it = runningServices.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next().service.getClassName().toString())) {
                return true;
            }
        }
        return false;
    }

    public static boolean a(LatLng latLng) {
        return latLng != null && 180.0d > Math.abs(latLng.longitude) && 90.0d > Math.abs(latLng.latitude);
    }

    public static boolean a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return f.matcher(str).matches();
    }

    public static boolean a(List<ScanResult> list, List<ScanResult> list2) {
        if (list != null && list2 != null) {
            int size = list.size();
            int size2 = list2.size();
            int i2 = size + size2;
            if (size != 0 && size2 != 0) {
                int i3 = 0;
                for (int i4 = 0; i4 < size; i4++) {
                    String str = list.get(i4).BSSID;
                    if (str != null) {
                        int i5 = 0;
                        while (true) {
                            if (i5 >= size2) {
                                break;
                            }
                            if (str.equals(list2.get(i5).BSSID)) {
                                i3++;
                                break;
                            }
                            i5++;
                        }
                    }
                }
                int i6 = i3 << 1;
                int i7 = (int) (i2 * 0.5f);
                if (size > 5 && size2 > 5) {
                    return i6 > i7;
                }
                if (i6 == i2) {
                    return true;
                }
            }
        }
        return false;
    }

    public static long b() {
        return System.currentTimeMillis() / 1000;
    }

    public static String b(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            return (telephonyManager == null || telephonyManager.getSimSerialNumber() == null) ? "" : telephonyManager.getSimSerialNumber();
        } catch (Exception unused) {
        }
        return "";
    }

    public static boolean b(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return e.matcher(str).matches();
    }

    public static long c(String str) {
        try {
            return g.parse(str).getTime() / 1000;
        } catch (ParseException unused) {
            return 0L;
        }
    }

    public static String c() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public static String c(Context context) {
        try {
            c = context.getPackageName();
        } catch (Exception unused) {
        }
        return c;
    }

    public static boolean d(Context context) {
        if (j == null) {
            j = (WifiManager) context.getSystemService("wifi");
        }
        j.startScan();
        h = j.getScanResults();
        List<ScanResult> list = i;
        if (list != null && list.size() != 0) {
            if (a(h, i)) {
                return true;
            }
            i = null;
        }
        i = h;
        return false;
    }

    public static boolean e(Context context) {
        return Build.VERSION.SDK_INT < 23 || context.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0;
    }

    public static boolean f(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return (context.checkSelfPermission("android.permission.ACCESS_WIFI_STATE") == 0) && (context.checkSelfPermission("android.permission.CHANGE_WIFI_STATE") == 0);
    }

    public static boolean g(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return (context.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0) && (context.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0);
    }
}
