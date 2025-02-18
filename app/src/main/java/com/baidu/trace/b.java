package com.baidu.trace;

import org.json.JSONObject;

/* loaded from: classes.dex */
public class b implements com.baidu.trace.c.g {
    protected static byte a;

    @Override // com.baidu.trace.c.g
    public final void a() {
    }

    @Override // com.baidu.trace.c.g
    public final void a(JSONObject jSONObject) {
        com.baidu.trace.a.a aVar = new com.baidu.trace.a.a();
        try {
            try {
                aVar.a = (byte) jSONObject.getInt("operat_result");
            } catch (Exception unused) {
                aVar.a = (byte) 0;
            }
        } finally {
            bc.a().a(aVar);
        }
    }
}
