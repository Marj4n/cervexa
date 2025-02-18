package com.baidu.trace;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import java.util.List;

/* loaded from: classes.dex */
public final class y {
    private WifiManager a;
    private List<ScanResult> b;
    private List<ScanResult> c;
    private long d;

    protected y(Context context) {
        this.a = (WifiManager) context.getSystemService("wifi");
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x00aa A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00ab  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected final void a(java.util.ArrayList<com.baidu.trace.a.j> r9) {
        /*
            Method dump skipped, instructions count: 332
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.trace.y.a(java.util.ArrayList):void");
    }

    protected final boolean a() {
        WifiManager wifiManager = this.a;
        if (wifiManager == null) {
            return false;
        }
        boolean isWifiEnabled = wifiManager.isWifiEnabled();
        return (isWifiEnabled || Build.VERSION.SDK_INT < 18) ? isWifiEnabled : this.a.isScanAlwaysAvailable();
    }

    protected final void b() {
        List<ScanResult> list = this.b;
        if (list != null) {
            list.clear();
            this.b = null;
        }
        List<ScanResult> list2 = this.c;
        if (list2 != null) {
            list2.clear();
            this.c = null;
        }
    }
}
