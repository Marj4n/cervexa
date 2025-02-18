package org.apache.commons.net.ftp;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

@Deprecated
/* loaded from: classes2.dex */
public class FTPSTrustManager implements X509TrustManager {
    private static final X509Certificate[] EMPTY_X509CERTIFICATE_ARRAY = new X509Certificate[0];

    @Override // javax.net.ssl.X509TrustManager
    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        for (X509Certificate x509Certificate : x509CertificateArr) {
            x509Certificate.checkValidity();
        }
    }

    @Override // javax.net.ssl.X509TrustManager
    public X509Certificate[] getAcceptedIssuers() {
        return EMPTY_X509CERTIFICATE_ARRAY;
    }
}
