package org.easydarwin.sw;

/* loaded from: classes2.dex */
public class JNIUtil {
    private static native void callMethod(String str, Object[] objArr, Object... objArr2);

    static {
        System.loadLibrary("Utils");
    }

    public static void yV12ToYUV420P(byte[] bArr, int i, int i2) {
        callMethod("YV12ToYUV420P", null, bArr, Integer.valueOf(i), Integer.valueOf(i2));
    }

    public static void nV21To420SP(byte[] bArr, int i, int i2) {
        callMethod("NV21To420SP", null, bArr, Integer.valueOf(i), Integer.valueOf(i2));
    }

    public static void rotateMatrix(byte[] bArr, int i, int i2, int i3, int i4) {
        callMethod("RotateByteMatrix", null, bArr, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4));
    }

    public static void rotateShortMatrix(byte[] bArr, int i, int i2, int i3, int i4) {
        callMethod("RotateShortMatrix", null, bArr, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4));
    }
}
