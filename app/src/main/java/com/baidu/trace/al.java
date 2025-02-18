package com.baidu.trace;

import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Build;

/* loaded from: classes.dex */
final class al implements GpsStatus.Listener {
    private /* synthetic */ ak a;

    al(ak akVar) {
        this.a = akVar;
    }

    @Override // android.location.GpsStatus.Listener
    public final void onGpsStatusChanged(int i) {
        LocationManager locationManager;
        LocationManager locationManager2;
        if (Build.VERSION.SDK_INT < 24) {
            locationManager2 = this.a.b;
            ak.a(this.a, i, locationManager2.getGpsStatus(null));
        } else {
            try {
                locationManager = this.a.b;
                ak.a(this.a, i, locationManager.getGpsStatus(null));
            } catch (Exception unused) {
                ak.a(this.a, 1);
            }
        }
    }
}
