package com.baidu.lbsapi.auth.tracesdk;

import android.content.Context;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
class c {
    private Context a;
    private HashMap b = null;
    private a c = null;

    interface a {
        void a(Object obj);
    }

    protected c(Context context) {
        this.a = context;
    }

    private HashMap a(HashMap hashMap) {
        HashMap hashMap2 = new HashMap();
        Iterator it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            String str = ((String) it.next()).toString();
            hashMap2.put(str, hashMap.get(str));
        }
        return hashMap2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        JSONObject jSONObject;
        if (str == null) {
            str = "";
        }
        try {
            jSONObject = new JSONObject(str);
            if (!jSONObject.has("status")) {
                jSONObject.put("status", -1);
            }
        } catch (JSONException unused) {
            jSONObject = new JSONObject();
            try {
                jSONObject.put("status", -1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        a aVar = this.c;
        if (aVar != null) {
            aVar.a(jSONObject.toString());
        }
    }

    protected void a(HashMap hashMap, a aVar) {
        this.b = a(hashMap);
        this.c = aVar;
        new Thread(new d(this)).start();
    }
}
