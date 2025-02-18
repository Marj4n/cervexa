package com.baidu.trace;

import android.content.Context;
import com.baidu.lbsapi.auth.tracesdk.LBSAuthManager;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class z implements com.baidu.trace.c.g {
    protected static String a = "";
    protected static long b = 0;
    protected static String c = "";
    protected static String d = "";
    protected static String e = "";
    protected static String f = "";
    protected static String g = "";
    protected static String h = "";
    protected static String i = "";
    protected static String j = "";
    protected static String k = "";
    protected static String l = "";
    protected static String m = "";
    protected static byte n = 0;
    protected static byte o = 0;
    protected static byte p = 15;
    protected static byte q = 30;
    protected static byte r = 0;
    protected static byte s = 1;
    protected static byte t = 0;
    protected static byte u = 0;
    protected static byte v = 0;
    protected static byte w = 5;

    protected static void a(Context context) {
        LBSAuthManager lBSAuthManager = LBSAuthManager.getInstance(context);
        k = lBSAuthManager.getCUID();
        h = lBSAuthManager.getDeviceID();
        i = lBSAuthManager.getIMEI();
        j = com.baidu.trace.c.e.b(context);
        f = com.baidu.trace.c.f.c;
        g = "3.0.7";
        l = com.baidu.trace.c.f.a;
        m = com.baidu.trace.c.f.b;
    }

    @Override // com.baidu.trace.c.g
    public final void a() {
    }

    @Override // com.baidu.trace.c.g
    public final void a(JSONObject jSONObject) {
        az.b = 0;
        com.baidu.trace.a.f fVar = new com.baidu.trace.a.f();
        try {
            try {
                fVar.a = (byte) jSONObject.getInt("operat_result");
                fVar.i = (byte) jSONObject.getInt("is_activated");
                fVar.b = (byte) jSONObject.getInt("heartbeat_period");
                fVar.c = (byte) jSONObject.getInt("packed_data_transmit_period");
                fVar.d = (byte) jSONObject.getInt("packed_data_capacity");
                fVar.e = (byte) jSONObject.getInt("packed_data_need_response");
                fVar.f = (byte) jSONObject.getInt("module_switch");
                fVar.g = (byte) jSONObject.getInt("sensor_wakeup_threshold");
                fVar.h = (byte) jSONObject.getInt("volume_size");
                fVar.j = (byte) jSONObject.getInt("location_info_sample_period");
            } catch (Exception unused) {
                fVar.a = (byte) 0;
                fVar.b = (byte) 15;
                fVar.f = (byte) 31;
                fVar.d = (byte) 96;
                fVar.e = (byte) 1;
                fVar.c = (byte) 30;
                fVar.j = (byte) 5;
                fVar.g = (byte) 5;
                fVar.h = (byte) 50;
            }
        } finally {
            bc.a().a(fVar);
        }
    }
}
