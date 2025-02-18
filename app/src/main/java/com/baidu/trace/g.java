package com.baidu.trace;

import org.json.JSONObject;

/* loaded from: classes.dex */
public final class g implements com.baidu.trace.c.g {
    protected static String a;
    protected static long b;
    protected static String c;
    protected static String d;

    protected static void b() {
        a = null;
        c = null;
        d = null;
    }

    @Override // com.baidu.trace.c.g
    public final void a() {
    }

    @Override // com.baidu.trace.c.g
    public final void a(JSONObject jSONObject) {
        try {
            if (jSONObject.has("access_key") && jSONObject.has("expire_time") && jSONObject.has("secret_key") && jSONObject.has("token")) {
                a = jSONObject.getString("access_key");
                b = jSONObject.getLong("expire_time");
                c = jSONObject.getString("secret_key");
                d = jSONObject.getString("token");
                h.a();
                bc.a().a(0);
                return;
            }
            b();
            bc.a().a(1);
        } catch (Exception unused) {
            b();
            bc.a().a(1);
        }
    }
}
