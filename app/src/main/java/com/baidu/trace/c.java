package com.baidu.trace;

import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class c implements com.baidu.trace.c.g {
    private static ArrayList<com.baidu.trace.a.e> a;

    protected static void b() {
        ArrayList<com.baidu.trace.a.e> arrayList = a;
        if (arrayList != null) {
            arrayList.clear();
            a = null;
        }
    }

    @Override // com.baidu.trace.c.g
    public final void a() {
        if (a == null) {
            a = new ArrayList<>();
        }
        TraceJniInterface.a().clearAttributeData();
        Iterator<com.baidu.trace.a.e> it = a.iterator();
        while (it.hasNext()) {
            com.baidu.trace.a.e next = it.next();
            TraceJniInterface.a().addAttributeData(next.a, next.b);
        }
    }

    @Override // com.baidu.trace.c.g
    public final void a(JSONObject jSONObject) {
        new com.baidu.trace.a.b();
        bc.a();
        bc.b();
    }
}
