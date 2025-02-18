package com.baidu.lbsapi.auth;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.bumptech.glide.load.Key;
import com.tencent.connect.common.Constants;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

/* loaded from: classes.dex */
public class g {
    private Context a;
    private String b = null;
    private HashMap<String, String> c = null;
    private String d = null;

    public g(Context context) {
        this.a = context;
    }

    private String a(Context context) {
        NetworkInfo activeNetworkInfo;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager != null && (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) != null && activeNetworkInfo.isAvailable()) {
                String extraInfo = activeNetworkInfo.getExtraInfo();
                return (extraInfo == null || !(extraInfo.trim().toLowerCase().equals("cmwap") || extraInfo.trim().toLowerCase().equals("uniwap") || extraInfo.trim().toLowerCase().equals("3gwap") || extraInfo.trim().toLowerCase().equals("ctwap"))) ? "wifi" : extraInfo.trim().toLowerCase().equals("ctwap") ? "ctwap" : "cmwap";
            }
            return null;
        } catch (Exception e) {
            if (a.a) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:115:0x01c2, code lost:
    
        if (com.baidu.lbsapi.auth.a.a == false) goto L125;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x0163, code lost:
    
        r14.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x0192, code lost:
    
        if (com.baidu.lbsapi.auth.a.a == false) goto L125;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x0161, code lost:
    
        if (com.baidu.lbsapi.auth.a.a == false) goto L125;
     */
    /* JADX WARN: Removed duplicated region for block: B:107:0x019b A[Catch: all -> 0x0131, TryCatch #12 {all -> 0x0131, blocks: (B:8:0x002f, B:132:0x0136, B:134:0x013a, B:135:0x013d, B:119:0x0169, B:121:0x016d, B:122:0x0170, B:105:0x0197, B:107:0x019b, B:108:0x019e), top: B:7:0x002f }] */
    /* JADX WARN: Removed duplicated region for block: B:110:0x01bb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:121:0x016d A[Catch: all -> 0x0131, TryCatch #12 {all -> 0x0131, blocks: (B:8:0x002f, B:132:0x0136, B:134:0x013a, B:135:0x013d, B:119:0x0169, B:121:0x016d, B:122:0x0170, B:105:0x0197, B:107:0x019b, B:108:0x019e), top: B:7:0x002f }] */
    /* JADX WARN: Removed duplicated region for block: B:124:0x018b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:134:0x013a A[Catch: all -> 0x0131, TryCatch #12 {all -> 0x0131, blocks: (B:8:0x002f, B:132:0x0136, B:134:0x013a, B:135:0x013d, B:119:0x0169, B:121:0x016d, B:122:0x0170, B:105:0x0197, B:107:0x019b, B:108:0x019e), top: B:7:0x002f }] */
    /* JADX WARN: Removed duplicated region for block: B:137:0x015a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x01c7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x01f7  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0205  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00fa A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00b4 A[Catch: all -> 0x0109, TryCatch #5 {all -> 0x0109, blocks: (B:54:0x00b0, B:56:0x00b4, B:57:0x00cf), top: B:53:0x00b0 }] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00f4 A[Catch: Exception -> 0x011a, IOException -> 0x011d, MalformedURLException -> 0x0120, all -> 0x0124, TRY_LEAVE, TryCatch #2 {all -> 0x0124, blocks: (B:10:0x0033, B:68:0x010e, B:70:0x0116, B:71:0x0119, B:60:0x00ec, B:62:0x00f4, B:18:0x0095, B:20:0x009d), top: B:9:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x010c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0116 A[Catch: Exception -> 0x011a, IOException -> 0x011d, MalformedURLException -> 0x0120, all -> 0x0124, TryCatch #2 {all -> 0x0124, blocks: (B:10:0x0033, B:68:0x010e, B:70:0x0116, B:71:0x0119, B:60:0x00ec, B:62:0x00f4, B:18:0x0095, B:20:0x009d), top: B:9:0x0033 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void a(javax.net.ssl.HttpsURLConnection r14) {
        /*
            Method dump skipped, instructions count: 555
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.lbsapi.auth.g.a(javax.net.ssl.HttpsURLConnection):void");
    }

    private static String b(HashMap<String, String> hashMap) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            if (z) {
                z = false;
            } else {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(entry.getKey(), Key.STRING_CHARSET_NAME));
            sb.append("=");
            sb.append(URLEncoder.encode(entry.getValue(), Key.STRING_CHARSET_NAME));
        }
        return sb.toString();
    }

    private HttpsURLConnection b() {
        String str;
        try {
            URL url = new URL(this.b);
            a.a("https URL: " + this.b);
            String a = a(this.a);
            if (a != null && !a.equals("")) {
                a.a("checkNetwork = " + a);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) (a.equals("cmwap") ? url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80))) : a.equals("ctwap") ? url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.200", 80))) : url.openConnection());
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setRequestMethod(Constants.HTTP_POST);
                httpsURLConnection.setConnectTimeout(50000);
                httpsURLConnection.setReadTimeout(50000);
                return httpsURLConnection;
            }
            a.c("Current network is not available.");
            this.d = ErrorMessage.a(-10, "Current network is not available.");
            return null;
        } catch (MalformedURLException e) {
            if (a.a) {
                e.printStackTrace();
                a.a(e.getMessage());
            }
            str = "Auth server could not be parsed as a URL.";
            this.d = ErrorMessage.a(-11, str);
            return null;
        } catch (Exception e2) {
            if (a.a) {
                e2.printStackTrace();
                a.a(e2.getMessage());
            }
            str = "Init httpsurlconnection failed.";
            this.d = ErrorMessage.a(-11, str);
            return null;
        }
    }

    private HashMap<String, String> c(HashMap<String, String> hashMap) {
        HashMap<String, String> hashMap2 = new HashMap<>();
        Iterator<String> it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            String str = it.next().toString();
            hashMap2.put(str, hashMap.get(str));
        }
        return hashMap2;
    }

    protected String a(HashMap<String, String> hashMap) {
        HashMap<String, String> c = c(hashMap);
        this.c = c;
        this.b = c.get("url");
        HttpsURLConnection b = b();
        if (b == null) {
            a.c("syncConnect failed,httpsURLConnection is null");
        } else {
            a(b);
        }
        return this.d;
    }

    protected boolean a() {
        NetworkInfo activeNetworkInfo;
        a.a("checkNetwork start");
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.a.getSystemService("connectivity");
            if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null) {
                return false;
            }
            if (!activeNetworkInfo.isAvailable()) {
                return false;
            }
            a.a("checkNetwork end");
            return true;
        } catch (Exception e) {
            if (a.a) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
