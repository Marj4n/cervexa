package com.baidu.lbsapi.auth;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import com.baidu.android.bbalbs.common.util.CommonParam;
import com.jieli.stream.dv.running2.util.IConstant;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes.dex */
public class LBSAuthManager {
    public static final int CODE_AUTHENTICATE_SUCC = 0;
    public static final int CODE_AUTHENTICATING = 602;
    public static final int CODE_INNER_ERROR = -1;
    public static final int CODE_KEY_NOT_EXIST = 101;
    public static final int CODE_NETWORK_FAILED = -11;
    public static final int CODE_NETWORK_INVALID = -10;
    public static final int CODE_UNAUTHENTICATE = 601;
    public static final String VERSION = "1.0.20";
    private static Context a;
    private static l d;
    private static int e;
    private static Hashtable<String, LBSAuthManagerListener> f = new Hashtable<>();
    private static LBSAuthManager g;
    private c b = null;
    private e c = null;
    private final Handler h = new h(this, Looper.getMainLooper());

    private LBSAuthManager(Context context) {
        a = context;
        l lVar = d;
        if (lVar != null && !lVar.isAlive()) {
            d = null;
        }
        a.b("BaiduApiAuth SDK Version:1.0.20");
        d();
    }

    private int a(String str) {
        int i = -1;
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.has("status")) {
                jSONObject.put("status", -1);
            }
            i = jSONObject.getInt("status");
            if (jSONObject.has("current") && i == 0) {
                long j = jSONObject.getLong("current");
                long currentTimeMillis = System.currentTimeMillis();
                if ((currentTimeMillis - j) / 3600000.0d < 24.0d) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    if (!simpleDateFormat.format(Long.valueOf(currentTimeMillis)).equals(simpleDateFormat.format(Long.valueOf(j)))) {
                    }
                }
                i = 601;
            }
            if (jSONObject.has("current") && i == 602) {
                if ((System.currentTimeMillis() - jSONObject.getLong("current")) / 1000 > 180.0d) {
                    return 601;
                }
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return i;
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0084, code lost:
    
        if (r6 == null) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0074, code lost:
    
        if (r6 == null) goto L46;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:19:0x007c  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0071  */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v13 */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r2v5, types: [java.io.BufferedReader] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String a(int r6) throws java.io.IOException {
        /*
            r5 = this;
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch: java.lang.Throwable -> L52 java.io.IOException -> L67 java.io.FileNotFoundException -> L77
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L52 java.io.IOException -> L67 java.io.FileNotFoundException -> L77
            r2.<init>()     // Catch: java.lang.Throwable -> L52 java.io.IOException -> L67 java.io.FileNotFoundException -> L77
            java.lang.String r3 = "/proc/"
            r2.append(r3)     // Catch: java.lang.Throwable -> L52 java.io.IOException -> L67 java.io.FileNotFoundException -> L77
            r2.append(r6)     // Catch: java.lang.Throwable -> L52 java.io.IOException -> L67 java.io.FileNotFoundException -> L77
            java.lang.String r6 = "/cmdline"
            r2.append(r6)     // Catch: java.lang.Throwable -> L52 java.io.IOException -> L67 java.io.FileNotFoundException -> L77
            java.lang.String r6 = r2.toString()     // Catch: java.lang.Throwable -> L52 java.io.IOException -> L67 java.io.FileNotFoundException -> L77
            r1.<init>(r6)     // Catch: java.lang.Throwable -> L52 java.io.IOException -> L67 java.io.FileNotFoundException -> L77
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L52 java.io.IOException -> L67 java.io.FileNotFoundException -> L77
            r6.<init>(r1)     // Catch: java.lang.Throwable -> L52 java.io.IOException -> L67 java.io.FileNotFoundException -> L77
            java.io.InputStreamReader r1 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L4e java.io.FileNotFoundException -> L50
            r1.<init>(r6)     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L4e java.io.FileNotFoundException -> L50
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L40 java.io.IOException -> L45 java.io.FileNotFoundException -> L47
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L40 java.io.IOException -> L45 java.io.FileNotFoundException -> L47
            java.lang.String r0 = r2.readLine()     // Catch: java.lang.Throwable -> L3a java.io.IOException -> L3c java.io.FileNotFoundException -> L3e
            r2.close()
            r1.close()
        L35:
            r6.close()
            goto L87
        L3a:
            r0 = move-exception
            goto L57
        L3c:
            goto L6a
        L3e:
            goto L7a
        L40:
            r2 = move-exception
            r4 = r2
            r2 = r0
            r0 = r4
            goto L57
        L45:
            r2 = r0
            goto L6a
        L47:
            r2 = r0
            goto L7a
        L49:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
            goto L57
        L4e:
            r1 = r0
            goto L69
        L50:
            r1 = r0
            goto L79
        L52:
            r6 = move-exception
            r1 = r0
            r2 = r1
            r0 = r6
            r6 = r2
        L57:
            if (r2 == 0) goto L5c
            r2.close()
        L5c:
            if (r1 == 0) goto L61
            r1.close()
        L61:
            if (r6 == 0) goto L66
            r6.close()
        L66:
            throw r0
        L67:
            r6 = r0
            r1 = r6
        L69:
            r2 = r1
        L6a:
            if (r2 == 0) goto L6f
            r2.close()
        L6f:
            if (r1 == 0) goto L74
            r1.close()
        L74:
            if (r6 == 0) goto L87
            goto L35
        L77:
            r6 = r0
            r1 = r6
        L79:
            r2 = r1
        L7a:
            if (r2 == 0) goto L7f
            r2.close()
        L7f:
            if (r1 == 0) goto L84
            r1.close()
        L84:
            if (r6 == 0) goto L87
            goto L35
        L87:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.lbsapi.auth.LBSAuthManager.a(int):java.lang.String");
    }

    private String a(Context context) {
        int myPid = Process.myPid();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.pid == myPid) {
                    return runningAppProcessInfo.processName;
                }
            }
        }
        String str = null;
        try {
            str = a(myPid);
        } catch (IOException unused) {
        }
        return str != null ? str : a.getPackageName();
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x003a, code lost:
    
        if (r6.equals("") != false) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String a(android.content.Context r6, java.lang.String r7) {
        /*
            r5 = this;
            java.lang.String r0 = "无法在AndroidManifest.xml中获取com.baidu.android.lbs.API_KEY的值"
            java.lang.String r1 = ""
            java.lang.String r2 = r6.getPackageName()
            r3 = 101(0x65, float:1.42E-43)
            android.content.pm.PackageManager r6 = r6.getPackageManager()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L51
            r4 = 128(0x80, float:1.8E-43)
            android.content.pm.ApplicationInfo r6 = r6.getApplicationInfo(r2, r4)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L51
            android.os.Bundle r2 = r6.metaData     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L51
            if (r2 != 0) goto L2c
            java.util.Hashtable<java.lang.String, com.baidu.lbsapi.auth.LBSAuthManagerListener> r6 = com.baidu.lbsapi.auth.LBSAuthManager.f     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L51
            java.lang.Object r6 = r6.get(r7)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L51
            com.baidu.lbsapi.auth.LBSAuthManagerListener r6 = (com.baidu.lbsapi.auth.LBSAuthManagerListener) r6     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L51
            if (r6 == 0) goto L63
            java.lang.String r2 = "AndroidManifest.xml的application中没有meta-data标签"
            java.lang.String r2 = com.baidu.lbsapi.auth.ErrorMessage.a(r3, r2)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L51
            r6.onAuthResult(r3, r2)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L51
            goto L63
        L2c:
            android.os.Bundle r6 = r6.metaData     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L51
            java.lang.String r2 = "com.baidu.lbsapi.API_KEY"
            java.lang.String r6 = r6.getString(r2)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L51
            if (r6 == 0) goto L3c
            boolean r1 = r6.equals(r1)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L4f
            if (r1 == 0) goto L4d
        L3c:
            java.util.Hashtable<java.lang.String, com.baidu.lbsapi.auth.LBSAuthManagerListener> r1 = com.baidu.lbsapi.auth.LBSAuthManager.f     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L4f
            java.lang.Object r1 = r1.get(r7)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L4f
            com.baidu.lbsapi.auth.LBSAuthManagerListener r1 = (com.baidu.lbsapi.auth.LBSAuthManagerListener) r1     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L4f
            if (r1 == 0) goto L4d
            java.lang.String r2 = com.baidu.lbsapi.auth.ErrorMessage.a(r3, r0)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L4f
            r1.onAuthResult(r3, r2)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L4f
        L4d:
            r1 = r6
            goto L63
        L4f:
            r1 = r6
            goto L52
        L51:
        L52:
            java.util.Hashtable<java.lang.String, com.baidu.lbsapi.auth.LBSAuthManagerListener> r6 = com.baidu.lbsapi.auth.LBSAuthManager.f
            java.lang.Object r6 = r6.get(r7)
            com.baidu.lbsapi.auth.LBSAuthManagerListener r6 = (com.baidu.lbsapi.auth.LBSAuthManagerListener) r6
            if (r6 == 0) goto L63
            java.lang.String r7 = com.baidu.lbsapi.auth.ErrorMessage.a(r3, r0)
            r6.onAuthResult(r3, r7)
        L63:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.lbsapi.auth.LBSAuthManager.a(android.content.Context, java.lang.String):java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a(String str, String str2) {
        if (str == null) {
            str = e();
        }
        Message obtainMessage = this.h.obtainMessage();
        int i = -1;
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.has("status")) {
                jSONObject.put("status", -1);
            }
            if (!jSONObject.has("current")) {
                jSONObject.put("current", System.currentTimeMillis());
            }
            c(jSONObject.toString());
            if (jSONObject.has("current")) {
                jSONObject.remove("current");
            }
            i = jSONObject.getInt("status");
            obtainMessage.what = i;
            obtainMessage.obj = jSONObject.toString();
            Bundle bundle = new Bundle();
            bundle.putString("listenerKey", str2);
            obtainMessage.setData(bundle);
            this.h.sendMessage(obtainMessage);
        } catch (JSONException e2) {
            e2.printStackTrace();
            obtainMessage.what = i;
            obtainMessage.obj = new JSONObject();
            Bundle bundle2 = new Bundle();
            bundle2.putString("listenerKey", str2);
            obtainMessage.setData(bundle2);
            this.h.sendMessage(obtainMessage);
        }
        d.c();
        e--;
        if (a.a) {
            a.a("httpRequest called mAuthCounter-- = " + e);
        }
        if (e == 0) {
            d.a();
            if (d != null) {
                d = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(boolean z, String str, Hashtable<String, String> hashtable, String str2) {
        String str3;
        String str4;
        String str5;
        String a2 = a(a, str2);
        if (a2 == null || a2.equals("")) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("url", "https://api.map.baidu.com/sdkcs/verify");
        a.a("url:https://api.map.baidu.com/sdkcs/verify");
        hashMap.put("output", "json");
        hashMap.put("ak", a2);
        a.a("ak:" + a2);
        hashMap.put("mcode", b.a(a));
        hashMap.put("from", "lbs_yunsdk");
        if (hashtable != null && hashtable.size() > 0) {
            for (Map.Entry<String, String> entry : hashtable.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    hashMap.put(key, value);
                }
            }
        }
        try {
            str3 = CommonParam.a(a);
        } catch (Exception unused) {
            str3 = "";
        }
        a.a("cuid:" + str3);
        if (TextUtils.isEmpty(str3)) {
            hashMap.put("cuid", "");
        } else {
            hashMap.put("cuid", str3);
        }
        hashMap.put("pcn", a.getPackageName());
        hashMap.put(IConstant.VERSION, VERSION);
        try {
            str4 = b.c(a);
        } catch (Exception unused2) {
            str4 = "";
        }
        if (TextUtils.isEmpty(str4)) {
            hashMap.put("macaddr", "");
        } else {
            hashMap.put("macaddr", str4);
        }
        try {
            str5 = b.a();
        } catch (Exception unused3) {
            str5 = "";
        }
        if (TextUtils.isEmpty(str5)) {
            hashMap.put(IjkMediaMeta.IJKM_KEY_LANGUAGE, "");
        } else {
            hashMap.put(IjkMediaMeta.IJKM_KEY_LANGUAGE, str5);
        }
        if (z) {
            hashMap.put("force", z ? "1" : "0");
        }
        if (str == null) {
            hashMap.put("from_service", "");
        } else {
            hashMap.put("from_service", str);
        }
        c cVar = new c(a);
        this.b = cVar;
        cVar.a(hashMap, new j(this, str2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(boolean z, String str, Hashtable<String, String> hashtable, String[] strArr, String str2) {
        String str3;
        String str4;
        String str5;
        String a2 = a(a, str2);
        if (a2 == null || a2.equals("")) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("url", "https://api.map.baidu.com/sdkcs/verify");
        hashMap.put("output", "json");
        hashMap.put("ak", a2);
        hashMap.put("from", "lbs_yunsdk");
        if (hashtable != null && hashtable.size() > 0) {
            for (Map.Entry<String, String> entry : hashtable.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    hashMap.put(key, value);
                }
            }
        }
        try {
            str3 = CommonParam.a(a);
        } catch (Exception unused) {
            str3 = "";
        }
        if (TextUtils.isEmpty(str3)) {
            hashMap.put("cuid", "");
        } else {
            hashMap.put("cuid", str3);
        }
        hashMap.put("pcn", a.getPackageName());
        hashMap.put(IConstant.VERSION, VERSION);
        try {
            str4 = b.c(a);
        } catch (Exception unused2) {
            str4 = "";
        }
        if (TextUtils.isEmpty(str4)) {
            hashMap.put("macaddr", "");
        } else {
            hashMap.put("macaddr", str4);
        }
        try {
            str5 = b.a();
        } catch (Exception unused3) {
            str5 = "";
        }
        if (TextUtils.isEmpty(str5)) {
            hashMap.put(IjkMediaMeta.IJKM_KEY_LANGUAGE, "");
        } else {
            hashMap.put(IjkMediaMeta.IJKM_KEY_LANGUAGE, str5);
        }
        if (z) {
            hashMap.put("force", z ? "1" : "0");
        }
        if (str == null) {
            hashMap.put("from_service", "");
        } else {
            hashMap.put("from_service", str);
        }
        e eVar = new e(a);
        this.c = eVar;
        eVar.a(hashMap, strArr, new k(this, str2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean b(String str) {
        String str2;
        JSONObject jSONObject;
        String a2 = a(a, str);
        try {
            jSONObject = new JSONObject(e());
        } catch (JSONException e2) {
            e2.printStackTrace();
            str2 = "";
        }
        if (!jSONObject.has("ak")) {
            return true;
        }
        str2 = jSONObject.getString("ak");
        return (a2 == null || str2 == null || a2.equals(str2)) ? false : true;
    }

    private void c(String str) {
        a.getSharedPreferences("authStatus_" + a(a), 0).edit().putString("status", str).commit();
    }

    private void d() {
        synchronized (LBSAuthManager.class) {
            if (d == null) {
                l lVar = new l("auth");
                d = lVar;
                lVar.start();
                while (d.a == null) {
                    try {
                        if (a.a) {
                            a.a("wait for create auth thread.");
                        }
                        Thread.sleep(3L);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    private String e() {
        return a.getSharedPreferences("authStatus_" + a(a), 0).getString("status", "{\"status\":601}");
    }

    public static LBSAuthManager getInstance(Context context) {
        if (g == null) {
            synchronized (LBSAuthManager.class) {
                if (g == null) {
                    g = new LBSAuthManager(context);
                }
            }
        } else if (context != null) {
            a = context;
        } else if (a.a) {
            a.c("input context is null");
            new RuntimeException("here").printStackTrace();
        }
        return g;
    }

    public int authenticate(boolean z, String str, Hashtable<String, String> hashtable, LBSAuthManagerListener lBSAuthManagerListener) {
        synchronized (LBSAuthManager.class) {
            String str2 = System.currentTimeMillis() + "";
            if (lBSAuthManagerListener != null) {
                f.put(str2, lBSAuthManagerListener);
            }
            String a2 = a(a, str2);
            if (a2 != null && !a2.equals("")) {
                e++;
                if (a.a) {
                    a.a(" mAuthCounter  ++ = " + e);
                }
                String e2 = e();
                if (a.a) {
                    a.a("getAuthMessage from cache:" + e2);
                }
                int a3 = a(e2);
                if (a3 == 601) {
                    try {
                        c(new JSONObject().put("status", 602).toString());
                    } catch (JSONException e3) {
                        e3.printStackTrace();
                    }
                }
                d();
                if (a.a) {
                    a.a("mThreadLooper.mHandler = " + d.a);
                }
                if (d != null && d.a != null) {
                    d.a.post(new i(this, a3, z, str2, str, hashtable));
                    return a3;
                }
                return -1;
            }
            return 101;
        }
    }

    public String getCUID() {
        Context context = a;
        if (context == null) {
            return "";
        }
        try {
            return CommonParam.a(context);
        } catch (Exception e2) {
            if (!a.a) {
                return "";
            }
            e2.printStackTrace();
            return "";
        }
    }

    public String getKey() {
        Context context = a;
        if (context == null) {
            return "";
        }
        try {
            return getPublicKey(context);
        } catch (PackageManager.NameNotFoundException e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public String getMCode() {
        Context context = a;
        return context == null ? "" : b.a(context);
    }

    public String getPublicKey(Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getString("com.baidu.lbsapi.API_KEY");
    }
}
