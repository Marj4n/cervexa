package com.baidu.trace;

import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.baidu.trace.model.StatusCodes;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class ak implements LocationListener {
    protected static int a = -1;
    private LocationManager b;
    private Location c;
    private boolean d;
    private Context h;
    private a i;
    private int e = 0;
    private int f = 10;
    private long g = 0;
    private final GpsStatus.Listener j = new al(this);

    public class a {
        long a;
        int b;
        int c;
        int d;
        int e;
        int f;
        int g;

        protected a(ak akVar) {
        }
    }

    public ak(Context context) {
        LocationManager locationManager;
        this.h = context;
        try {
            locationManager = (LocationManager) context.getSystemService("location");
            this.b = locationManager;
        } catch (SecurityException unused) {
        } catch (Exception unused2) {
            this.b = null;
        }
        if (locationManager != null && locationManager.getAllProviders() != null && this.b.getAllProviders().contains("gps")) {
            this.b.addGpsStatusListener(this.j);
            this.b.sendExtraCommand("gps", "force_xtra_injection", new Bundle());
            this.i = new a(this);
        }
        this.b = null;
        this.i = new a(this);
    }

    static /* synthetic */ int a(ak akVar, int i) {
        akVar.e = 1;
        return 1;
    }

    static /* synthetic */ void a(ak akVar, int i, GpsStatus gpsStatus) {
        if (gpsStatus == null || i != 4) {
            return;
        }
        int maxSatellites = gpsStatus.getMaxSatellites();
        Iterator<GpsSatellite> it = gpsStatus.getSatellites().iterator();
        akVar.e = 0;
        for (int i2 = 0; it.hasNext() && i2 <= maxSatellites; i2++) {
            if (it.next().usedInFix()) {
                akVar.e++;
            }
        }
    }

    protected final void a() {
        LocationManager locationManager = this.b;
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
        a = -1;
    }

    protected final void a(int i, String str) {
        a = i;
        Intent intent = new Intent(StatusCodes.GPS_STATUS_ACTION);
        intent.putExtra("statusCode", i);
        intent.putExtra("statusMessage", str);
        this.h.sendBroadcast(intent);
    }

    protected final void a(com.baidu.trace.a.g gVar, int i) {
        if (this.i == null || System.currentTimeMillis() - this.g >= i) {
            gVar.a = false;
            if (1 != a) {
                a(1, StatusCodes.MSG_GPS_SEARCHING);
                return;
            }
            return;
        }
        int i2 = this.e;
        if (i2 == 0) {
            i2 = 1;
        }
        gVar.c = Integer.valueOf(i2).byteValue();
        gVar.d = Integer.valueOf(this.i.b).shortValue();
        gVar.g = Integer.valueOf(this.i.e).shortValue();
        gVar.e = Integer.valueOf(this.i.c).byteValue();
        gVar.i = this.i.g;
        gVar.h = this.i.f;
        gVar.f = Integer.valueOf(this.i.d).shortValue();
        gVar.a = (gVar.c <= 0 || gVar.i == 0 || gVar.h == 0) ? false : true;
        gVar.b = this.i.a;
        com.baidu.trace.a.a("G-GPS local time is: " + this.i.a);
        if (a != 0) {
            a(0, StatusCodes.MSG_GPS_LOCATED);
        }
    }

    protected final boolean a(int i) {
        LocationManager locationManager = this.b;
        if (locationManager == null) {
            return false;
        }
        boolean isProviderEnabled = locationManager.isProviderEnabled("gps");
        this.d = isProviderEnabled;
        if (!isProviderEnabled) {
            if (2 != a) {
                a(2, StatusCodes.MSG_GPS_CLOSED);
            }
            return false;
        }
        try {
            if (this.c == null) {
                this.c = this.b.getLastKnownLocation("gps") != null ? this.b.getLastKnownLocation("gps") : new Location("gps");
            }
            this.b.addGpsStatusListener(this.j);
            this.b.requestLocationUpdates(this.c.getProvider(), i, this.f, this);
        } catch (Exception unused) {
        }
        if (this.c != null) {
            return true;
        }
        if (1 != a) {
            a(1, StatusCodes.MSG_GPS_SEARCHING);
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x0070, code lost:
    
        if (r5 == false) goto L6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0077, code lost:
    
        if (r0 != false) goto L6;
     */
    /* JADX WARN: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x007c  */
    @Override // android.location.LocationListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onLocationChanged(android.location.Location r11) {
        /*
            Method dump skipped, instructions count: 230
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.trace.ak.onLocationChanged(android.location.Location):void");
    }

    @Override // android.location.LocationListener
    public final void onProviderDisabled(String str) {
    }

    @Override // android.location.LocationListener
    public final void onProviderEnabled(String str) {
    }

    @Override // android.location.LocationListener
    public final void onStatusChanged(String str, int i, Bundle bundle) {
    }
}
