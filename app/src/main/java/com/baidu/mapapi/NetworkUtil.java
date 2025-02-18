package com.baidu.mapapi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import com.baidu.platform.comapi.util.SysUpdateObservable;

/* loaded from: classes.dex */
public class NetworkUtil {
    public static NetworkInfo getActiveNetworkInfo(Context context) {
        try {
            return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (Exception unused) {
            return null;
        }
    }

    public static String getCurrentNetMode(Context context) {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        int i = 1;
        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() != 1) {
                switch (((TelephonyManager) context.getSystemService("phone")).getNetworkType()) {
                    case 1:
                    case 2:
                        i = 6;
                        break;
                    case 3:
                    case 9:
                    case 10:
                    case 15:
                        i = 9;
                        break;
                    case 4:
                        i = 5;
                        break;
                    case 5:
                    case 6:
                    case 7:
                    case 12:
                        i = 7;
                        break;
                    case 8:
                        i = 8;
                        break;
                    case 11:
                        i = 2;
                        break;
                    case 13:
                        i = 4;
                        break;
                    case 14:
                        i = 10;
                        break;
                }
            }
            return Integer.toString(i);
        }
        i = 0;
        return Integer.toString(i);
    }

    public static boolean initConnectState() {
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x000e, code lost:
    
        if (r3.isConnected() != false) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean isWifiConnected(android.net.NetworkInfo r3) {
        /*
            r0 = 1
            r1 = 0
            if (r3 == 0) goto L16
            int r2 = r3.getType()     // Catch: java.lang.Exception -> L11
            if (r0 != r2) goto L16
            boolean r3 = r3.isConnected()     // Catch: java.lang.Exception -> L11
            if (r3 == 0) goto L16
            goto L17
        L11:
            r3 = move-exception
            r3.printStackTrace()
            goto L18
        L16:
            r0 = 0
        L17:
            r1 = r0
        L18:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapapi.NetworkUtil.isWifiConnected(android.net.NetworkInfo):boolean");
    }

    public static void updateNetworkProxy(Context context) {
        SysUpdateObservable.getInstance().updateNetworkProxy(context);
    }
}
