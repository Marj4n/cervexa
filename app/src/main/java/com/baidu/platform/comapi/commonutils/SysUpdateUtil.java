package com.baidu.platform.comapi.commonutils;

import android.content.Context;
import com.baidu.mapapi.NetworkUtil;
import com.baidu.platform.comapi.util.SysUpdateObserver;

/* loaded from: classes.dex */
public class SysUpdateUtil implements SysUpdateObserver {
    static com.baidu.platform.comjni.map.commonmemcache.a a = new com.baidu.platform.comjni.map.commonmemcache.a();
    public static boolean b = false;
    public static String c = "";
    public static int d = 0;

    @Override // com.baidu.platform.comapi.util.SysUpdateObserver
    public void init() {
        com.baidu.platform.comjni.map.commonmemcache.a aVar = a;
        if (aVar != null) {
            aVar.a();
            a.b();
        }
    }

    @Override // com.baidu.platform.comapi.util.SysUpdateObserver
    public void updateNetworkInfo(Context context) {
        NetworkUtil.updateNetworkProxy(context);
    }

    /* JADX WARN: Code restructure failed: missing block: B:57:0x00c2, code lost:
    
        if ("10.0.0.200".equals(r9.trim()) != false) goto L50;
     */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00d5  */
    @Override // com.baidu.platform.comapi.util.SysUpdateObserver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void updateNetworkProxy(android.content.Context r9) {
        /*
            r8 = this;
            android.net.NetworkInfo r9 = com.baidu.mapapi.NetworkUtil.getActiveNetworkInfo(r9)
            if (r9 == 0) goto Ld8
            boolean r0 = r9.isAvailable()
            if (r0 == 0) goto Ld8
            java.lang.String r0 = r9.getTypeName()
            java.lang.String r0 = r0.toLowerCase()
            java.lang.String r1 = "wifi"
            boolean r2 = r0.equals(r1)
            r3 = 0
            r4 = 0
            if (r2 == 0) goto L2a
            boolean r2 = r9.isConnected()
            if (r2 == 0) goto L2a
            com.baidu.platform.comjni.engine.AppEngine.SetProxyInfo(r3, r4)
            com.baidu.platform.comapi.commonutils.SysUpdateUtil.b = r4
            return
        L2a:
            java.lang.String r2 = "mobile"
            boolean r2 = r0.equals(r2)
            if (r2 != 0) goto L3e
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto Ld8
            boolean r0 = com.baidu.mapapi.NetworkUtil.isWifiConnected(r9)
            if (r0 != 0) goto Ld8
        L3e:
            java.lang.String r9 = r9.getExtraInfo()
            com.baidu.platform.comapi.commonutils.SysUpdateUtil.b = r4
            java.lang.String r0 = "10.0.0.200"
            r1 = 80
            java.lang.String r2 = "10.0.0.172"
            r5 = 1
            if (r9 == 0) goto L99
            java.lang.String r9 = r9.toLowerCase()
            java.lang.String r6 = "cmwap"
            boolean r6 = r9.startsWith(r6)
            if (r6 != 0) goto L96
            java.lang.String r6 = "uniwap"
            boolean r6 = r9.startsWith(r6)
            if (r6 != 0) goto L96
            java.lang.String r6 = "3gwap"
            boolean r6 = r9.startsWith(r6)
            if (r6 == 0) goto L6a
            goto L96
        L6a:
            java.lang.String r2 = "ctwap"
            boolean r2 = r9.startsWith(r2)
            if (r2 == 0) goto L73
            goto Lc4
        L73:
            java.lang.String r0 = "cmnet"
            boolean r0 = r9.startsWith(r0)
            if (r0 != 0) goto L93
            java.lang.String r0 = "uninet"
            boolean r0 = r9.startsWith(r0)
            if (r0 != 0) goto L93
            java.lang.String r0 = "ctnet"
            boolean r0 = r9.startsWith(r0)
            if (r0 != 0) goto L93
            java.lang.String r0 = "3gnet"
            boolean r9 = r9.startsWith(r0)
            if (r9 == 0) goto Lc9
        L93:
            com.baidu.platform.comapi.commonutils.SysUpdateUtil.b = r4
            goto Lc9
        L96:
            com.baidu.platform.comapi.commonutils.SysUpdateUtil.c = r2
            goto Lc6
        L99:
            java.lang.String r9 = android.net.Proxy.getDefaultHost()
            int r6 = android.net.Proxy.getDefaultPort()
            if (r9 == 0) goto Lc9
            int r7 = r9.length()
            if (r7 <= 0) goto Lc9
            java.lang.String r7 = r9.trim()
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto Lba
            com.baidu.platform.comapi.commonutils.SysUpdateUtil.c = r2
            com.baidu.platform.comapi.commonutils.SysUpdateUtil.d = r6
        Lb7:
            com.baidu.platform.comapi.commonutils.SysUpdateUtil.b = r5
            goto Lc9
        Lba:
            java.lang.String r9 = r9.trim()
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto Lc9
        Lc4:
            com.baidu.platform.comapi.commonutils.SysUpdateUtil.c = r0
        Lc6:
            com.baidu.platform.comapi.commonutils.SysUpdateUtil.d = r1
            goto Lb7
        Lc9:
            boolean r9 = com.baidu.platform.comapi.commonutils.SysUpdateUtil.b
            if (r9 != r5) goto Ld5
            java.lang.String r9 = com.baidu.platform.comapi.commonutils.SysUpdateUtil.c
            int r0 = com.baidu.platform.comapi.commonutils.SysUpdateUtil.d
            com.baidu.platform.comjni.engine.AppEngine.SetProxyInfo(r9, r0)
            goto Ld8
        Ld5:
            com.baidu.platform.comjni.engine.AppEngine.SetProxyInfo(r3, r4)
        Ld8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.commonutils.SysUpdateUtil.updateNetworkProxy(android.content.Context):void");
    }

    @Override // com.baidu.platform.comapi.util.SysUpdateObserver
    public void updatePhoneInfo() {
        com.baidu.platform.comjni.map.commonmemcache.a aVar = a;
        if (aVar != null) {
            aVar.b();
        }
    }
}
