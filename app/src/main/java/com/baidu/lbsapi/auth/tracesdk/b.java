package com.baidu.lbsapi.auth.tracesdk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Locale;

/* loaded from: classes.dex */
class b {

    static class a {
        public static String a(byte[] bArr) {
            char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            StringBuilder sb = new StringBuilder(bArr.length << 1);
            for (int i = 0; i < bArr.length; i++) {
                sb.append(cArr[(bArr[i] & 240) >> 4]);
                sb.append(cArr[bArr[i] & 15]);
            }
            return sb.toString();
        }
    }

    static String a() {
        return Locale.getDefault().getLanguage();
    }

    protected static String a(Context context) {
        String packageName = context.getPackageName();
        return a(context, packageName) + ";" + packageName;
    }

    private static String a(Context context, String str) {
        String str2;
        try {
            str2 = a((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(context.getPackageManager().getPackageInfo(str, 64).signatures[0].toByteArray())));
        } catch (PackageManager.NameNotFoundException | CertificateException unused) {
            str2 = "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str2.length(); i++) {
            stringBuffer.append(str2.charAt(i));
            if (i > 0 && i % 2 == 1 && i < str2.length() - 1) {
                stringBuffer.append(":");
            }
        }
        return stringBuffer.toString();
    }

    static String a(X509Certificate x509Certificate) {
        try {
            return a.a(a(x509Certificate.getEncoded()));
        } catch (CertificateEncodingException unused) {
            return null;
        }
    }

    static byte[] a(byte[] bArr) {
        try {
            return MessageDigest.getInstance("SHA1").digest(bArr);
        } catch (NoSuchAlgorithmException unused) {
            return null;
        }
    }

    protected static String[] b(Context context) {
        String packageName = context.getPackageName();
        String[] b = b(context, packageName);
        if (b == null || b.length <= 0) {
            return null;
        }
        int length = b.length;
        String[] strArr = new String[length];
        for (int i = 0; i < length; i++) {
            strArr[i] = b[i] + ";" + packageName;
            if (com.baidu.lbsapi.auth.tracesdk.a.a) {
                com.baidu.lbsapi.auth.tracesdk.a.a("mcode" + strArr[i]);
            }
        }
        return strArr;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0049  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String[] b(android.content.Context r6, java.lang.String r7) {
        /*
            r0 = 0
            r1 = 0
            android.content.pm.PackageManager r6 = r6.getPackageManager()     // Catch: java.lang.Throwable -> L3c
            r2 = 64
            android.content.pm.PackageInfo r6 = r6.getPackageInfo(r7, r2)     // Catch: java.lang.Throwable -> L3c
            android.content.pm.Signature[] r6 = r6.signatures     // Catch: java.lang.Throwable -> L3c
            if (r6 == 0) goto L3c
            int r7 = r6.length     // Catch: java.lang.Throwable -> L3c
            if (r7 <= 0) goto L3c
            int r7 = r6.length     // Catch: java.lang.Throwable -> L3c
            java.lang.String[] r7 = new java.lang.String[r7]     // Catch: java.lang.Throwable -> L3c
            r2 = 0
        L17:
            int r3 = r6.length     // Catch: java.lang.Throwable -> L3a
            if (r2 >= r3) goto L3d
            java.lang.String r3 = "X.509"
            java.security.cert.CertificateFactory r3 = java.security.cert.CertificateFactory.getInstance(r3)     // Catch: java.lang.Throwable -> L3a
            java.io.ByteArrayInputStream r4 = new java.io.ByteArrayInputStream     // Catch: java.lang.Throwable -> L3a
            r5 = r6[r2]     // Catch: java.lang.Throwable -> L3a
            byte[] r5 = r5.toByteArray()     // Catch: java.lang.Throwable -> L3a
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L3a
            java.security.cert.Certificate r3 = r3.generateCertificate(r4)     // Catch: java.lang.Throwable -> L3a
            java.security.cert.X509Certificate r3 = (java.security.cert.X509Certificate) r3     // Catch: java.lang.Throwable -> L3a
            java.lang.String r3 = a(r3)     // Catch: java.lang.Throwable -> L3a
            r7[r2] = r3     // Catch: java.lang.Throwable -> L3a
            int r2 = r2 + 1
            goto L17
        L3a:
            goto L3d
        L3c:
            r7 = r0
        L3d:
            if (r7 == 0) goto L81
            int r6 = r7.length
            if (r6 <= 0) goto L81
            int r6 = r7.length
            java.lang.String[] r0 = new java.lang.String[r6]
            r6 = 0
        L46:
            int r2 = r7.length
            if (r6 >= r2) goto L81
            java.lang.StringBuffer r2 = new java.lang.StringBuffer
            r2.<init>()
            r3 = 0
        L4f:
            r4 = r7[r6]
            int r4 = r4.length()
            if (r3 >= r4) goto L78
            r4 = r7[r6]
            char r4 = r4.charAt(r3)
            r2.append(r4)
            if (r3 <= 0) goto L75
            int r4 = r3 % 2
            r5 = 1
            if (r4 != r5) goto L75
            r4 = r7[r6]
            int r4 = r4.length()
            int r4 = r4 - r5
            if (r3 >= r4) goto L75
            java.lang.String r4 = ":"
            r2.append(r4)
        L75:
            int r3 = r3 + 1
            goto L4f
        L78:
            java.lang.String r2 = r2.toString()
            r0[r6] = r2
            int r6 = r6 + 1
            goto L46
        L81:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.lbsapi.auth.tracesdk.b.b(android.content.Context, java.lang.String):java.lang.String[]");
    }

    static String c(Context context) {
        String string = context.getSharedPreferences("mac", 0).getString("macaddr", null);
        if (string == null) {
            String d = d(context);
            if (d != null) {
                string = Base64.encodeToString(d.getBytes(), 0);
                if (!TextUtils.isEmpty(string)) {
                    context.getSharedPreferences("mac", 0).edit().putString("macaddr", string).commit();
                }
            } else {
                string = "";
            }
        }
        if (com.baidu.lbsapi.auth.tracesdk.a.a) {
            com.baidu.lbsapi.auth.tracesdk.a.a("getMacID mac_adress: " + string);
        }
        return string;
    }

    private static boolean c(Context context, String str) {
        boolean z = context.checkCallingOrSelfPermission(str) != -1;
        if (com.baidu.lbsapi.auth.tracesdk.a.a) {
            com.baidu.lbsapi.auth.tracesdk.a.a("hasPermission " + z + " | " + str);
        }
        return z;
    }

    static String d(Context context) {
        String format;
        String str = null;
        try {
        } catch (Exception e) {
            if (com.baidu.lbsapi.auth.tracesdk.a.a) {
                com.baidu.lbsapi.auth.tracesdk.a.a(e.toString());
            }
        }
        if (!c(context, "android.permission.ACCESS_WIFI_STATE")) {
            if (com.baidu.lbsapi.auth.tracesdk.a.a) {
            }
            return str;
        }
        WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
        str = connectionInfo.getMacAddress();
        if (!TextUtils.isEmpty(str)) {
            Base64.encode(str.getBytes(), 0);
        }
        format = com.baidu.lbsapi.auth.tracesdk.a.a ? String.format("ssid=%s mac=%s", connectionInfo.getSSID(), connectionInfo.getMacAddress()) : "You need the android.Manifest.permission.ACCESS_WIFI_STATE permission. Open AndroidManifest.xml and just before the final </manifest> tag add:android.permission.ACCESS_WIFI_STATE";
        return str;
        com.baidu.lbsapi.auth.tracesdk.a.a(format);
        return str;
    }
}
