package com.baidu.trace;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class r {
    public static void a(String str) {
        try {
            JSONArray jSONArray = new JSONArray(str);
            byte b = 15;
            byte b2 = 30;
            byte b3 = 5;
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                int i2 = jSONObject.getInt("id");
                byte b4 = (byte) jSONObject.getInt("value");
                if (i2 != 1) {
                    if (i2 != 2) {
                        if (i2 != 3) {
                            if (i2 == 4) {
                                z.s = b4;
                            } else if (i2 == 12 && b4 >= ar.c) {
                                b3 = b4;
                            }
                        } else if (b4 >= 5 && b4 <= 128) {
                            z.r = b4;
                        }
                    } else if (b4 >= 2) {
                        b2 = b4;
                    }
                } else if (b4 >= 5) {
                    b = b4;
                }
            }
            z.p = b;
            if (b3 == 0 || b2 == 0 || b2 % b3 != 0) {
                return;
            }
            ar.b = b3 * 1000;
            z.w = b3;
            bd.i = b2 * 1000;
            z.q = b2;
            bd.d();
        } catch (JSONException unused) {
        }
    }
}
