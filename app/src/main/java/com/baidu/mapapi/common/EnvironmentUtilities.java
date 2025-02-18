package com.baidu.mapapi.common;

import android.content.Context;
import com.baidu.platform.comapi.util.d;
import java.io.File;

/* loaded from: classes.dex */
public class EnvironmentUtilities {
    static String a;
    static String b;
    static String c;
    static int d;
    static int e;
    static int f;
    private static d g;

    public static String getAppCachePath() {
        return b;
    }

    public static String getAppSDCardPath() {
        String str = a + "/BaiduMapSDKNew";
        if (str.length() != 0) {
            File file = new File(str);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return str;
    }

    public static String getAppSecondCachePath() {
        return c;
    }

    public static int getDomTmpStgMax() {
        return e;
    }

    public static int getItsTmpStgMax() {
        return f;
    }

    public static int getMapTmpStgMax() {
        return d;
    }

    public static String getSDCardPath() {
        return a;
    }

    public static void initAppDirectory(Context context) {
        String c2;
        if (g == null) {
            d a2 = d.a();
            g = a2;
            a2.a(context);
        }
        String str = a;
        if (str == null || str.length() <= 0) {
            a = g.b().a();
            c2 = g.b().c();
        } else {
            c2 = a + File.separator + "BaiduMapSDKNew" + File.separator + "cache";
        }
        b = c2;
        c = g.b().d();
        d = 20971520;
        e = 52428800;
        f = 5242880;
    }

    public static void setSDCardPath(String str) {
        a = str;
    }
}
