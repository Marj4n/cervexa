package com.baidu.lbsapi.auth.tracesdk;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
class e {
    private Context a;
    private List b = null;
    private a c = null;

    interface a {
        void a(Object obj);
    }

    protected e(Context context) {
        this.a = context;
    }

    private List a(HashMap hashMap, String[] strArr) {
        ArrayList arrayList = new ArrayList();
        if (strArr == null || strArr.length <= 0) {
            HashMap hashMap2 = new HashMap();
            Iterator it = hashMap.keySet().iterator();
            while (it.hasNext()) {
                String str = ((String) it.next()).toString();
                hashMap2.put(str, hashMap.get(str));
            }
            arrayList.add(hashMap2);
        } else {
            for (String str2 : strArr) {
                HashMap hashMap3 = new HashMap();
                Iterator it2 = hashMap.keySet().iterator();
                while (it2.hasNext()) {
                    String str3 = ((String) it2.next()).toString();
                    hashMap3.put(str3, hashMap.get(str3));
                }
                hashMap3.put("mcode", str2);
                arrayList.add(hashMap3);
            }
        }
        return arrayList;
    }

    private void a(String str) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public void a(List list) {
        int i;
        com.baidu.lbsapi.auth.tracesdk.a.a("syncConnect start Thread id = " + String.valueOf(Thread.currentThread().getId()));
        if (list == null || list.size() == 0) {
            com.baidu.lbsapi.auth.tracesdk.a.c("syncConnect failed,params list is null or size is 0");
            return;
        }
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        while (i2 < list.size()) {
            com.baidu.lbsapi.auth.tracesdk.a.a("syncConnect resuest " + i2 + "  start!!!");
            HashMap hashMap = (HashMap) list.get(i2);
            g gVar = new g(this.a);
            if (gVar.a()) {
                String a2 = gVar.a(hashMap);
                if (a2 == null) {
                    a2 = "";
                }
                com.baidu.lbsapi.auth.tracesdk.a.a("syncConnect resuest " + i2 + "  result:" + a2);
                arrayList.add(a2);
                try {
                    JSONObject jSONObject = new JSONObject(a2);
                    if (jSONObject.has("status") && jSONObject.getInt("status") == 0) {
                        com.baidu.lbsapi.auth.tracesdk.a.a("auth end and break");
                        a(a2);
                        return;
                    }
                } catch (JSONException unused) {
                    com.baidu.lbsapi.auth.tracesdk.a.a("continue-------------------------------");
                }
            } else {
                com.baidu.lbsapi.auth.tracesdk.a.a("Current network is not available.");
                arrayList.add(ErrorMessage.a("Current network is not available."));
            }
            com.baidu.lbsapi.auth.tracesdk.a.a("syncConnect end");
            i2++;
        }
        com.baidu.lbsapi.auth.tracesdk.a.a("--iiiiii:" + i2 + "<><>paramList.size():" + list.size() + "<><>authResults.size():" + arrayList.size());
        if (list.size() <= 0 || i2 != list.size() || arrayList.size() <= 0 || i2 != arrayList.size() || i2 - 1 <= 0) {
            return;
        }
        try {
            JSONObject jSONObject2 = new JSONObject((String) arrayList.get(i));
            if (!jSONObject2.has("status") || jSONObject2.getInt("status") == 0) {
                return;
            }
            com.baidu.lbsapi.auth.tracesdk.a.a("i-1 result is not 0,return first result");
            a((String) arrayList.get(0));
        } catch (JSONException e) {
            a(ErrorMessage.a("JSONException:" + e.getMessage()));
        }
    }

    protected void a(HashMap hashMap, String[] strArr, a aVar) {
        this.b = a(hashMap, strArr);
        this.c = aVar;
        new Thread(new f(this)).start();
    }
}
