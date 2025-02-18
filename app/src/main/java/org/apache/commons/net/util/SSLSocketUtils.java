package org.apache.commons.net.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.net.ssl.SSLSocket;

/* loaded from: classes2.dex */
public class SSLSocketUtils {
    private SSLSocketUtils() {
    }

    public static boolean enableEndpointNameVerification(SSLSocket sSLSocket) {
        Object invoke;
        try {
            Class<?> cls = Class.forName("javax.net.ssl.SSLParameters");
            Method declaredMethod = cls.getDeclaredMethod("setEndpointIdentificationAlgorithm", String.class);
            Method declaredMethod2 = SSLSocket.class.getDeclaredMethod("getSSLParameters", new Class[0]);
            Method declaredMethod3 = SSLSocket.class.getDeclaredMethod("setSSLParameters", cls);
            if (declaredMethod != null && declaredMethod2 != null && declaredMethod3 != null && (invoke = declaredMethod2.invoke(sSLSocket, new Object[0])) != null) {
                declaredMethod.invoke(invoke, "HTTPS");
                declaredMethod3.invoke(sSLSocket, invoke);
                return true;
            }
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException unused) {
        }
        return false;
    }
}
