package com.baidu.platform.comapi.util;

import android.content.Context;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

/* loaded from: classes.dex */
public class a {

    /* renamed from: com.baidu.platform.comapi.util.a$a, reason: collision with other inner class name */
    static class C0014a {
        public static String a(byte[] bArr) {
            char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            StringBuilder sb = new StringBuilder(bArr.length * 2);
            for (int i = 0; i < bArr.length; i++) {
                sb.append(cArr[(bArr[i] & 240) >> 4]);
                sb.append(cArr[bArr[i] & 15]);
            }
            return sb.toString();
        }
    }

    public static String a(Context context) {
        String packageName = context.getPackageName();
        return a(context, packageName) + ";" + packageName;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x003f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String a(android.content.Context r3, java.lang.String r4) {
        /*
            r0 = 0
            android.content.pm.PackageManager r3 = r3.getPackageManager()     // Catch: java.security.cert.CertificateException -> L29 android.content.pm.PackageManager.NameNotFoundException -> L2e
            r1 = 64
            android.content.pm.PackageInfo r3 = r3.getPackageInfo(r4, r1)     // Catch: java.security.cert.CertificateException -> L29 android.content.pm.PackageManager.NameNotFoundException -> L2e
            android.content.pm.Signature[] r3 = r3.signatures     // Catch: java.security.cert.CertificateException -> L29 android.content.pm.PackageManager.NameNotFoundException -> L2e
            java.lang.String r4 = "X.509"
            java.security.cert.CertificateFactory r4 = java.security.cert.CertificateFactory.getInstance(r4)     // Catch: java.security.cert.CertificateException -> L29 android.content.pm.PackageManager.NameNotFoundException -> L2e
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream     // Catch: java.security.cert.CertificateException -> L29 android.content.pm.PackageManager.NameNotFoundException -> L2e
            r3 = r3[r0]     // Catch: java.security.cert.CertificateException -> L29 android.content.pm.PackageManager.NameNotFoundException -> L2e
            byte[] r3 = r3.toByteArray()     // Catch: java.security.cert.CertificateException -> L29 android.content.pm.PackageManager.NameNotFoundException -> L2e
            r1.<init>(r3)     // Catch: java.security.cert.CertificateException -> L29 android.content.pm.PackageManager.NameNotFoundException -> L2e
            java.security.cert.Certificate r3 = r4.generateCertificate(r1)     // Catch: java.security.cert.CertificateException -> L29 android.content.pm.PackageManager.NameNotFoundException -> L2e
            java.security.cert.X509Certificate r3 = (java.security.cert.X509Certificate) r3     // Catch: java.security.cert.CertificateException -> L29 android.content.pm.PackageManager.NameNotFoundException -> L2e
            java.lang.String r3 = a(r3)     // Catch: java.security.cert.CertificateException -> L29 android.content.pm.PackageManager.NameNotFoundException -> L2e
            goto L34
        L29:
            r3 = move-exception
            r3.printStackTrace()
            goto L32
        L2e:
            r3 = move-exception
            r3.printStackTrace()
        L32:
            java.lang.String r3 = ""
        L34:
            java.lang.StringBuffer r4 = new java.lang.StringBuffer
            r4.<init>()
        L39:
            int r1 = r3.length()
            if (r0 >= r1) goto L5c
            char r1 = r3.charAt(r0)
            r4.append(r1)
            if (r0 <= 0) goto L59
            int r1 = r0 % 2
            r2 = 1
            if (r1 != r2) goto L59
            int r1 = r3.length()
            int r1 = r1 - r2
            if (r0 >= r1) goto L59
            java.lang.String r1 = ":"
            r4.append(r1)
        L59:
            int r0 = r0 + 1
            goto L39
        L5c:
            java.lang.String r3 = r4.toString()
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.util.a.a(android.content.Context, java.lang.String):java.lang.String");
    }

    static String a(X509Certificate x509Certificate) {
        try {
            return C0014a.a(a(x509Certificate.getEncoded()));
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
}
