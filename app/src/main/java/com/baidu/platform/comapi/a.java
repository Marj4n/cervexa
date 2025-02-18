package com.baidu.platform.comapi;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.VersionInfo;
import com.baidu.platform.comapi.util.PermissionCheck;
import com.baidu.platform.comapi.util.SysUpdateObservable;
import com.baidu.platform.comapi.util.e;
import com.serenegiant.net.NetworkChangedReceiver;

/* loaded from: classes.dex */
public class a implements PermissionCheck.c {
    private static a f;
    private Context b;
    private Handler c;
    private d d;
    private int e;
    private static final String a = a.class.getSimpleName();
    private static int g = -100;

    static {
        NativeLoader.getInstance().loadLibrary(VersionInfo.getKitName());
        com.baidu.platform.comjni.tools.a.b();
    }

    private a() {
    }

    public static a a() {
        if (f == null) {
            f = new a();
        }
        return f;
    }

    private void f() {
        d dVar;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetworkChangedReceiver.ACTION_GLOBAL_CONNECTIVITY_CHANGE);
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        Context context = this.b;
        if (context == null || (dVar = this.d) == null) {
            return;
        }
        context.registerReceiver(dVar, intentFilter);
    }

    private void g() {
        Context context;
        d dVar = this.d;
        if (dVar == null || (context = this.b) == null) {
            return;
        }
        context.unregisterReceiver(dVar);
    }

    public void a(Context context) {
        this.b = context;
    }

    public void a(Message message) {
        Intent intent;
        if (message.what != 2012) {
            if (message.arg2 == 3) {
                this.b.sendBroadcast(new Intent(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR));
            }
            if (message.arg2 != 2 && message.arg2 != 404 && message.arg2 != 5 && message.arg2 != 8) {
                return;
            } else {
                intent = new Intent(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
            }
        } else if (message.arg1 == 0) {
            intent = new Intent(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        } else {
            Intent intent2 = new Intent(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
            intent2.putExtra(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, message.arg1);
            intent = intent2;
        }
        this.b.sendBroadcast(intent);
    }

    @Override // com.baidu.platform.comapi.util.PermissionCheck.c
    public void a(PermissionCheck.b bVar) {
        if (bVar == null) {
            return;
        }
        if (bVar.a == 0) {
            e.z = bVar.e;
            e.a(bVar.b, bVar.c);
        } else {
            Log.e("baidumapsdk", "Authentication Error\n" + bVar.toString());
        }
        if (this.c == null || bVar.a == g) {
            return;
        }
        g = bVar.a;
        Message.obtain(this.c, 2012, bVar.a, bVar.a, null).sendToTarget();
    }

    public void b() {
        if (this.e == 0) {
            if (this.b == null) {
                throw new IllegalStateException("you have not supplyed the global app context info from SDKInitializer.initialize(Context) function.");
            }
            this.d = new d();
            f();
            SysUpdateObservable.getInstance().updateNetworkInfo(this.b);
        }
        this.e++;
    }

    public boolean c() {
        if (this.b == null) {
            throw new IllegalStateException("you have not supplyed the global app context info from SDKInitializer.initialize(Context) function.");
        }
        this.c = new b(this);
        e.b(this.b);
        e.f();
        PermissionCheck.init(this.b);
        PermissionCheck.setPermissionCheckResultListener(this);
        PermissionCheck.permissionCheck();
        return true;
    }

    public void d() {
        int i = this.e - 1;
        this.e = i;
        if (i == 0) {
            g();
            e.a();
        }
    }

    public Context e() {
        Context context = this.b;
        if (context != null) {
            return context;
        }
        throw new IllegalStateException("you have not supplyed the global app context info from SDKInitializer.initialize(Context) function.");
    }
}
