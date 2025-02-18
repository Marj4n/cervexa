package com.baidu.platform.comapi.map;

import android.content.Context;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.NetworkUtil;
import com.baidu.mapapi.common.BaiduMapSDKException;
import com.baidu.platform.comapi.NativeLoader;
import com.baidu.platform.comapi.commonutils.SysUpdateUtil;
import com.baidu.platform.comapi.util.SysUpdateObservable;
import com.baidu.platform.comjni.engine.AppEngine;
import com.baidu.vi.VMsg;

/* renamed from: com.baidu.platform.comapi.map.i, reason: case insensitive filesystem */
/* loaded from: classes.dex */
public class C0078i {
    private static int a;
    private static Context b = BMapManager.getContext();

    static {
        if (!com.baidu.mapapi.VersionInfo.getApiVersion().equals(VersionInfo.getApiVersion())) {
            throw new BaiduMapSDKException("the version of map is not match with base");
        }
        NativeLoader.getInstance().loadLibrary(VersionInfo.getKitName());
        AppEngine.InitClass();
        a(BMapManager.getContext());
        SysUpdateObservable.getInstance().addObserver(new SysUpdateUtil());
        SysUpdateObservable.getInstance().init();
    }

    public static void a() {
        if (a == 0) {
            if (b == null) {
                throw new IllegalStateException("you have not supplyed the global app context info from SDKInitializer.initialize(Context) function.");
            }
            VMsg.init();
            AppEngine.InitEngine(b);
            AppEngine.StartSocketProc();
            NetworkUtil.updateNetworkProxy(b);
        }
        a++;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x00e6 A[Catch: Exception -> 0x01a5, TryCatch #1 {Exception -> 0x01a5, blocks: (B:9:0x006b, B:11:0x00a0, B:13:0x00b7, B:15:0x00d7, B:19:0x00e6, B:21:0x00ec, B:22:0x00ef, B:24:0x011d, B:25:0x0120, B:27:0x0140, B:28:0x0143, B:30:0x0163, B:31:0x0166, B:33:0x0186), top: B:8:0x006b }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x018c A[LOOP:0: B:36:0x018a->B:37:0x018c, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0198 A[ADDED_TO_REGION, LOOP:1: B:40:0x0198->B:41:0x019a, LOOP_START, PHI: r9
  0x0198: PHI (r9v3 int) = (r9v2 int), (r9v4 int) binds: [B:39:0x0196, B:41:0x019a] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x01a4 A[ORIG_RETURN, RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void a(android.content.Context r21) {
        /*
            Method dump skipped, instructions count: 431
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.map.C0078i.a(android.content.Context):void");
    }

    public static void a(boolean z) {
        C0074e.j(z);
    }

    public static void b() {
        int i = a - 1;
        a = i;
        if (i == 0) {
            AppEngine.UnInitEngine();
            VMsg.destroy();
        }
    }
}
