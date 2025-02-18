package com.baidu.lbsapi.auth.tracesdk;

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
    private HashMap c = null;
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
                String str = "cmwap";
                if (extraInfo == null || !(extraInfo.trim().toLowerCase().equals("cmwap") || extraInfo.trim().toLowerCase().equals("uniwap") || extraInfo.trim().toLowerCase().equals("3gwap") || extraInfo.trim().toLowerCase().equals("ctwap"))) {
                    str = "wifi";
                } else if (extraInfo.trim().toLowerCase().equals("ctwap")) {
                    return "ctwap";
                }
                return str;
            }
            return null;
        } catch (Exception e) {
            if (a.a) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:115:0x01b0, code lost:
    
        if (com.baidu.lbsapi.auth.tracesdk.a.a == false) goto L125;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x0157, code lost:
    
        r14.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x0183, code lost:
    
        if (com.baidu.lbsapi.auth.tracesdk.a.a == false) goto L125;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x0155, code lost:
    
        if (com.baidu.lbsapi.auth.tracesdk.a.a == false) goto L125;
     */
    /* JADX WARN: Removed duplicated region for block: B:107:0x018c A[Catch: all -> 0x0128, TryCatch #9 {all -> 0x0128, blocks: (B:8:0x002c, B:132:0x012d, B:134:0x0131, B:135:0x0134, B:119:0x015d, B:121:0x0161, B:122:0x0164, B:105:0x0188, B:107:0x018c, B:108:0x018f), top: B:7:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:110:0x01a9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0161 A[Catch: all -> 0x0128, TryCatch #9 {all -> 0x0128, blocks: (B:8:0x002c, B:132:0x012d, B:134:0x0131, B:135:0x0134, B:119:0x015d, B:121:0x0161, B:122:0x0164, B:105:0x0188, B:107:0x018c, B:108:0x018f), top: B:7:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:124:0x017c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0131 A[Catch: all -> 0x0128, TryCatch #9 {all -> 0x0128, blocks: (B:8:0x002c, B:132:0x012d, B:134:0x0131, B:135:0x0134, B:119:0x015d, B:121:0x0161, B:122:0x0164, B:105:0x0188, B:107:0x018c, B:108:0x018f), top: B:7:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:137:0x014e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01b5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x01df  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01ed  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00f1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00b1 A[Catch: all -> 0x0100, TryCatch #0 {all -> 0x0100, blocks: (B:55:0x00ad, B:57:0x00b1, B:58:0x00c9), top: B:54:0x00ad }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00eb A[Catch: Exception -> 0x0111, IOException -> 0x0114, MalformedURLException -> 0x0117, all -> 0x011b, TRY_LEAVE, TryCatch #22 {all -> 0x011b, blocks: (B:10:0x0030, B:69:0x0105, B:71:0x010d, B:72:0x0110, B:61:0x00e3, B:63:0x00eb, B:19:0x0092, B:21:0x009a), top: B:9:0x0030 }] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0103 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x010d A[Catch: Exception -> 0x0111, IOException -> 0x0114, MalformedURLException -> 0x0117, all -> 0x011b, TryCatch #22 {all -> 0x011b, blocks: (B:10:0x0030, B:69:0x0105, B:71:0x010d, B:72:0x0110, B:61:0x00e3, B:63:0x00eb, B:19:0x0092, B:21:0x009a), top: B:9:0x0030 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void a(javax.net.ssl.HttpsURLConnection r14) {
        /*
            Method dump skipped, instructions count: 528
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.lbsapi.auth.tracesdk.g.a(javax.net.ssl.HttpsURLConnection):void");
    }

    private static String b(HashMap hashMap) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (Map.Entry entry : hashMap.entrySet()) {
            if (z) {
                z = false;
            } else {
                sb.append("&");
            }
            sb.append(URLEncoder.encode((String) entry.getKey(), Key.STRING_CHARSET_NAME));
            sb.append("=");
            sb.append(URLEncoder.encode((String) entry.getValue(), Key.STRING_CHARSET_NAME));
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

    private HashMap c(HashMap hashMap) {
        HashMap hashMap2 = new HashMap();
        Iterator it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            String str = ((String) it.next()).toString();
            hashMap2.put(str, hashMap.get(str));
        }
        return hashMap2;
    }

    protected String a(HashMap hashMap) {
        HashMap c = c(hashMap);
        this.c = c;
        this.b = (String) c.get("url");
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
