package com.baidu.trace;

import org.json.JSONObject;

/* loaded from: classes.dex */
public class ay implements com.baidu.trace.c.g {
    protected static byte a;
    protected static int b;
    protected static byte[] c = new byte[32];
    protected static byte d;
    protected static String e;

    @Override // com.baidu.trace.c.g
    public final void a() {
        TraceJniInterface.a().setPushResult(b, a);
    }

    @Override // com.baidu.trace.c.g
    public final void a(JSONObject jSONObject) {
        com.baidu.trace.a.i iVar = new com.baidu.trace.a.i();
        try {
            iVar.a = jSONObject.getString("ak");
            iVar.b = jSONObject.getInt("msg_flag");
            iVar.c = (byte) jSONObject.getInt("info_type");
            iVar.d = jSONObject.getString("info_content");
            bc.a().a(iVar, jSONObject.getLong("time_flag"));
        } catch (Exception unused) {
        }
    }
}
