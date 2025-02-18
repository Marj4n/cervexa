package com.baidu.trace;

/* loaded from: classes.dex */
public final class TraceJniInterface {
    private static final TraceJniInterface a = new TraceJniInterface();

    static {
        av.a().a("BaiduTraceSDK_v3_0_7");
    }

    private TraceJniInterface() {
    }

    protected static TraceJniInterface a() {
        return a;
    }

    public static native void clearTrackData();

    public static native double[] gcjToBaidu(double d, double d2);

    public static native double[] processTrackPoint(double d, double d2, double d3, long j, int i);

    public static native double[] wgsToBaidu(double d, double d2);

    protected final native void addAttributeData(byte[] bArr, byte[] bArr2);

    protected final native void addCustomData(byte[] bArr, byte[] bArr2);

    protected final native void addNearbyCell(int i, int i2);

    protected final native void addWifiData(byte[] bArr, byte b, byte[] bArr2, byte b2);

    protected final native byte[] buildHeartbeatData();

    protected final native byte[] buildLocationData(int i);

    protected final native byte[] buildRequestProtocolData(short s, int i);

    protected final native void clearAttributeData();

    protected final native void clearBluetoothData();

    protected final native void clearCustomData();

    protected final native void clearNearbyCells();

    protected final native void clearWifiData();

    protected final native byte[] parseResponseProtocolData(byte[] bArr);

    protected final native void setAppInfo(byte[] bArr, byte[] bArr2, long j, byte[] bArr3, byte[] bArr4);

    protected final native void setCellData(byte b, byte b2, short s, short s2, int i, int i2);

    protected final native void setDeviceInfo(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, byte[] bArr6, byte[] bArr7, byte[] bArr8);

    protected final native void setGPSData(byte b, short s, byte b2, short s2, short s3, int i, int i2);

    protected final native void setPackData(byte b, byte[] bArr);

    protected final native void setPushResult(int i, byte b);

    protected final native void socketDisconnection();
}
