package com.jieli.lib.dv.control.gps;

import com.jieli.lib.dv.control.base.AbstractClient;
import com.jieli.lib.dv.control.model.GpsInfo;
import com.jieli.lib.dv.control.player.Stream;

/* loaded from: classes.dex */
public class GpsParser extends AbstractClient {
    private long a = 0;
    private OnGpsListener b;

    private native boolean nativeClose();

    private native boolean nativeCreate();

    private native boolean nativeInit();

    private native boolean nativeParse(byte[] bArr);

    protected void onError(int i, String str) {
    }

    public GpsParser() {
        Stream.loadLibrariesOnce(Stream.sLocalLibLoader);
        nativeInit();
    }

    public boolean parse(byte[] bArr) {
        return nativeParse(bArr);
    }

    @Override // com.jieli.lib.dv.control.base.AbstractClient
    public boolean create() {
        return nativeCreate();
    }

    @Override // com.jieli.lib.dv.control.base.AbstractClient
    public boolean close() {
        this.b = null;
        return nativeClose();
    }

    public void setOnGpsListener(OnGpsListener onGpsListener) {
        this.b = onGpsListener;
    }

    protected void onParsed(double d, double d2, double d3) {
        if (this.b != null) {
            GpsInfo gpsInfo = new GpsInfo();
            gpsInfo.setLongitude(d);
            gpsInfo.setLatitude(d2);
            gpsInfo.setSpeed(d3);
            this.b.onGps(gpsInfo);
        }
    }
}
