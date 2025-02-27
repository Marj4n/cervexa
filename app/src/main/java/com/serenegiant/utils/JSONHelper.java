package com.serenegiant.utils;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class JSONHelper {
    private static final boolean DEBUG = false;
    private static final String TAG = JSONHelper.class.getSimpleName();

    public static long getLong(JSONObject jSONObject, String str, long j) throws JSONException {
        if (!jSONObject.has(str)) {
            return j;
        }
        try {
            try {
                return jSONObject.getLong(str);
            } catch (JSONException unused) {
                return Long.parseLong(jSONObject.getString(str));
            }
        } catch (Exception unused2) {
            return jSONObject.getBoolean(str) ? 1L : 0L;
        }
    }

    public static long optLong(JSONObject jSONObject, String str, long j) {
        if (!jSONObject.has(str)) {
            return j;
        }
        try {
            try {
                try {
                    return jSONObject.getLong(str);
                } catch (JSONException unused) {
                    return Long.parseLong(jSONObject.getString(str));
                }
            } catch (Exception unused2) {
                return jSONObject.getBoolean(str) ? 1L : 0L;
            }
        } catch (Exception e) {
            Log.w(TAG, e);
            return j;
        }
    }

    public static int getInt(JSONObject jSONObject, String str, int i) throws JSONException {
        if (!jSONObject.has(str)) {
            return i;
        }
        try {
            try {
                return jSONObject.getInt(str);
            } catch (JSONException unused) {
                return Integer.parseInt(jSONObject.getString(str));
            }
        } catch (Exception unused2) {
            return jSONObject.getBoolean(str) ? 1 : 0;
        }
    }

    public static int optInt(JSONObject jSONObject, String str, int i) throws JSONException {
        if (!jSONObject.has(str)) {
            return i;
        }
        try {
            try {
                return jSONObject.getInt(str);
            } catch (Exception e) {
                Log.w(TAG, e);
                return i;
            }
        } catch (Exception unused2) {
            return jSONObject.getBoolean(str) ? 1 : 0;
        }
    }

    public static boolean getBoolean(JSONObject jSONObject, String str, boolean z) throws JSONException {
        if (!jSONObject.has(str)) {
            return z;
        }
        try {
            return jSONObject.getBoolean(str);
        } catch (Exception unused) {
            return jSONObject.getInt(str) != 0;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0013 A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0015 A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean optBoolean(JSONObject r4, String r5, boolean r6) {
        /*
            boolean r0 = r4.has(r5)
            if (r0 == 0) goto L28
            boolean r6 = r4.getBoolean(r5)     // Catch: java.lang.Exception -> Lb
            goto L28
        Lb:
            r0 = 1
            r1 = 0
            int r4 = r4.getInt(r5)     // Catch: org.json.JSONException -> L17
            if (r4 == 0) goto L15
        L13:
            r6 = 1
            goto L28
        L15:
            r6 = 0
            goto L28
        L17:
            double r4 = r4.getDouble(r5)     // Catch: org.json.JSONException -> L22
            r2 = 0
            int r6 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r6 == 0) goto L15
            goto L13
        L22:
            r4 = move-exception
            java.lang.String r5 = com.serenegiant.utils.JSONHelper.TAG
            android.util.Log.w(r5, r4)
        L28:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.utils.JSONHelper.optBoolean(org.json.JSONObject, java.lang.String, boolean):boolean");
    }
}
