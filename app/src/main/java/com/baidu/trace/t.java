package com.baidu.trace;

import org.json.JSONObject;

/* loaded from: classes.dex */
public class t implements com.baidu.trace.c.g {
    @Override // com.baidu.trace.c.g
    public final void a() {
    }

    @Override // com.baidu.trace.c.g
    public final void a(JSONObject jSONObject) {
        az.a = 0;
        com.baidu.trace.a.d dVar = new com.baidu.trace.a.d();
        try {
            try {
                dVar.a = (byte) jSONObject.getInt("operat_result");
            } catch (Exception unused) {
                dVar.a = (byte) 0;
            }
        } finally {
            bc.a().a(dVar);
        }
    }
}
