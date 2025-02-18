package com.baidu.lbsapi.auth;

import android.content.Context;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
class c {
    private Context a;
    private HashMap<String, String> b = null;
    private a<String> c = null;

    interface a<Result> {
        void a(Result result);
    }

    protected c(Context context) {
        this.a = context;
    }

    private HashMap<String, String> a(HashMap<String, String> hashMap) {
        HashMap<String, String> hashMap2 = new HashMap<>();
        Iterator<String> it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            String str = it.next().toString();
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
        a<String> aVar = this.c;
        if (aVar != null) {
            aVar.a(jSONObject.toString());
        }
    }

    protected void a(HashMap<String, String> hashMap, a<String> aVar) {
        this.b = a(hashMap);
        this.c = aVar;
        new Thread(new d(this)).start();
    }
}
