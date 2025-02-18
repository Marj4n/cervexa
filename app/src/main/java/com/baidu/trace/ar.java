package com.baidu.trace;

import android.content.Context;
import android.os.Handler;
import android.preference.PreferenceManager;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import com.baidu.trace.model.LocationMode;
import com.baidu.trace.model.TraceLocation;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

/* loaded from: classes.dex */
public class ar {
    protected static aj a = null;
    public static int b = 5000;
    public static int c = 1;
    public static int d = 600000;
    private Context e;
    private WeakReference<LBSTraceService> f;
    private LocRequest g;
    private y h;
    private p i;
    private ak j;
    private com.baidu.trace.a.g k;
    private ArrayList<com.baidu.trace.a.j> l;
    private com.baidu.trace.a.c m;
    private boolean n;
    private boolean o;
    private boolean p;
    private boolean q;
    private int r;
    private Handler s;
    private OnEntityListener t;

    static class a {
        String a;
        byte[] b;
        long c;

        public a(String str, byte[] bArr, long j) {
            this.a = str;
            this.b = bArr;
            this.c = j;
        }
    }

    public ar(Context context, Handler handler) {
        this.e = null;
        this.f = null;
        this.g = null;
        this.k = null;
        this.l = null;
        this.m = null;
        this.n = true;
        this.o = false;
        this.p = true;
        this.q = true;
        this.r = LocationMode.High_Accuracy.ordinal();
        this.s = null;
        this.t = null;
        this.e = context;
        this.h = new y(context);
        this.i = new p(this.e);
        this.j = new ak(this.e);
        this.s = handler;
    }

    public ar(WeakReference<LBSTraceService> weakReference, Handler handler) {
        Context context;
        Class cls;
        this.e = null;
        this.f = null;
        this.g = null;
        this.k = null;
        this.l = null;
        this.m = null;
        this.n = true;
        this.o = false;
        this.p = true;
        this.q = true;
        this.r = LocationMode.High_Accuracy.ordinal();
        this.s = null;
        this.t = null;
        this.f = weakReference;
        if (weakReference != null && weakReference.get() != null) {
            this.e = this.f.get().getServiceContext();
            this.r = this.f.get().getLocationMode();
        }
        this.h = new y(this.e);
        this.i = new p(this.e);
        this.j = new ak(this.e);
        this.s = handler;
        if (PreferenceManager.getDefaultSharedPreferences(this.e).getBoolean("same_process", false)) {
            context = this.e;
            cls = LBSTraceClient.class;
        } else {
            context = this.e;
            cls = LBSTraceService.class;
        }
        com.baidu.trace.c.a.a(context, cls);
        this.t = new as(this);
    }

    private ArrayList<com.baidu.trace.a.e> a(long j) {
        WeakReference<LBSTraceService> weakReference = this.f;
        Map map = null;
        if (weakReference == null || weakReference.get() == null || this.f.get().getClientListener() == null) {
            return null;
        }
        ArrayList<com.baidu.trace.a.e> arrayList = new ArrayList<>();
        try {
            if (this.f != null && this.f.get() != null) {
                map = this.f.get().getClientListener().gatherAttribute(j);
            }
        } catch (Exception unused) {
        }
        if (map != null) {
            for (Map.Entry entry : map.entrySet()) {
                com.baidu.trace.a.e eVar = new com.baidu.trace.a.e();
                if (!"_entity_name".equals(entry.getKey())) {
                    eVar.a = com.baidu.trace.c.d.a((String) entry.getKey());
                    eVar.b = com.baidu.trace.c.d.a((String) entry.getValue());
                    arrayList.add(eVar);
                }
            }
        }
        return arrayList;
    }

    static /* synthetic */ void a(ar arVar, LatLng latLng, long j, CoordType coordType, double d2) {
        if (a == null) {
            return;
        }
        bi biVar = new bi(latLng, coordType, j, d2);
        System.currentTimeMillis();
        double[] processTrackPoint = TraceJniInterface.processTrackPoint(biVar.getLocation().latitude, biVar.getLocation().longitude, biVar.getRadius(), biVar.getLocTime(), biVar.getCoordType().ordinal());
        boolean z = true;
        if (processTrackPoint == null || processTrackPoint.length <= 3) {
            z = false;
        } else {
            biVar.setLocation(new LatLng(processTrackPoint[0], processTrackPoint[1]));
            biVar.setLocTime((long) processTrackPoint[2]);
            biVar.setRadius(processTrackPoint[3]);
            biVar.setCoordType(CoordType.bd09ll);
        }
        if (z) {
            a.a(biVar, arVar.s);
        }
        System.currentTimeMillis();
    }

    protected final void a() {
        com.baidu.trace.a.g gVar;
        ArrayList<com.baidu.trace.a.j> arrayList;
        TraceJniInterface.a().clearCustomData();
        TraceJniInterface.a().clearWifiData();
        TraceJniInterface.a().clearBluetoothData();
        TraceJniInterface.a().clearNearbyCells();
        com.baidu.trace.a.c cVar = null;
        if (LocationMode.Battery_Saving.ordinal() != this.r) {
            int i = b;
            int i2 = i / 2;
            int i3 = i * 2;
            gVar = new com.baidu.trace.a.g();
            if (this.j != null && com.baidu.trace.c.e.e(this.e) && this.j.a(i2)) {
                this.j.a(gVar, i3);
                if (gVar.a) {
                    this.p = false;
                    this.q = false;
                    this.o = true;
                    this.k = gVar;
                }
            } else {
                this.o = false;
                gVar.a = false;
            }
            d();
            this.k = gVar;
        } else {
            gVar = null;
        }
        if (gVar == null || !gVar.a) {
            TraceJniInterface.a().setGPSData((byte) 0, (short) 0, (byte) 0, (short) 0, (short) 0, 0, 0);
        } else {
            TraceJniInterface.a().setGPSData(gVar.c, gVar.d, gVar.e, gVar.f, gVar.g, gVar.h, gVar.i);
        }
        if (LocationMode.Device_Sensors.ordinal() != this.r) {
            if (this.p) {
                arrayList = new ArrayList<>();
                if (com.baidu.trace.c.e.f(this.e) && this.h.a()) {
                    this.h.a(arrayList);
                }
            } else {
                arrayList = null;
            }
            this.l = arrayList;
        } else {
            arrayList = null;
        }
        if (arrayList != null && arrayList.size() > 0) {
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                com.baidu.trace.a.j jVar = arrayList.get(i4);
                TraceJniInterface.a().addWifiData(jVar.a, jVar.b, jVar.c, jVar.d);
            }
        }
        if (LocationMode.Device_Sensors.ordinal() != this.r) {
            if (this.q) {
                com.baidu.trace.a.c cVar2 = new com.baidu.trace.a.c();
                if (com.baidu.trace.c.e.g(this.e) && this.i.a()) {
                    this.i.a(cVar2);
                } else {
                    cVar2.a = false;
                }
                cVar = cVar2;
            }
            this.m = cVar;
        }
        if (cVar == null || !cVar.a) {
            TraceJniInterface.a().setCellData((byte) 0, (byte) 0, (short) 0, (short) 0, 0, 0);
        } else {
            TraceJniInterface.a().setCellData(cVar.b, cVar.c, cVar.d, cVar.e, cVar.f, cVar.g);
            for (int i5 = 0; i5 < cVar.h.length && i5 < cVar.i.length; i5++) {
                TraceJniInterface.a().addNearbyCell(cVar.h[i5], cVar.i[i5]);
            }
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (gVar != null && gVar.a && gVar.b > 0) {
            com.baidu.trace.a.a("L-GPS local time is: " + gVar.b);
            if (Math.abs(System.currentTimeMillis() - gVar.b) > d) {
                currentTimeMillis = gVar.b;
            }
        }
        com.baidu.trace.a.a("L-GPS locTime is: " + currentTimeMillis);
        System.out.println("locTimeOffset : " + d);
        TraceJniInterface.a().addCustomData("_entity_name".getBytes(), z.c.getBytes());
        ArrayList<com.baidu.trace.a.e> a2 = a(currentTimeMillis);
        if (a2 != null && a2.size() > 0) {
            for (int i6 = 0; i6 < a2.size(); i6++) {
                com.baidu.trace.a.e eVar = a2.get(i6);
                TraceJniInterface.a().addCustomData(eVar.a, eVar.b);
            }
        }
        long j = currentTimeMillis / 1000;
        byte[] buildLocationData = TraceJniInterface.a().buildLocationData((int) j);
        if (buildLocationData == null) {
            return;
        }
        if (b <= 5000 && this.n && gVar != null && gVar.a) {
            this.n = false;
            return;
        }
        a aVar = new a(z.c, buildLocationData, j);
        if (bd.c != null) {
            bd.c.offer(aVar);
        }
        aj ajVar = a;
        if (ajVar != null && !ajVar.a()) {
            long j2 = aVar.c;
            if (gVar == null || !gVar.a) {
                if (this.g == null) {
                    this.g = new LocRequest(z.b);
                }
                aa.a(this.e, this.g, this.t);
            } else if (com.baidu.trace.c.a.a != null) {
                com.baidu.trace.c.a.a.execute(new at(this, gVar, j2));
            }
        }
        if (arrayList != null) {
            arrayList.clear();
        }
        if (a2 != null) {
            a2.clear();
        }
    }

    public final void a(int i) {
        this.r = i;
    }

    protected final boolean a(Context context, Handler handler, TraceLocation traceLocation) {
        ak akVar = this.j;
        if (akVar == null || !this.o) {
            synchronized (ar.class) {
                handler.post(new au(this, context));
            }
            return false;
        }
        if (akVar == null) {
            return false;
        }
        com.baidu.trace.a.g gVar = new com.baidu.trace.a.g();
        this.j.a(gVar, 10000);
        if (gVar.a) {
            traceLocation.setLatitude(gVar.i / 600000.0d);
            traceLocation.setLongitude(gVar.h / 600000.0d);
            traceLocation.setRadius(gVar.d);
            traceLocation.setAltitude(gVar.g);
            traceLocation.setDirection(gVar.e << 1);
            traceLocation.setSpeed(gVar.f / 100.0f);
            traceLocation.setTime(com.baidu.trace.c.e.c());
            com.baidu.trace.c.e.a(traceLocation);
        }
        return gVar.a;
    }

    protected final void b() {
        ak akVar = this.j;
        if (akVar != null) {
            akVar.a();
            this.j = null;
        }
    }

    protected final void c() {
        b();
        if (com.baidu.trace.c.a.a != null && !PreferenceManager.getDefaultSharedPreferences(this.e).getBoolean("same_process", true) && "FinalizableDelegatedExecutorService".equals(com.baidu.trace.c.a.a.getClass().getSimpleName())) {
            com.baidu.trace.c.a.a();
        }
        y yVar = this.h;
        if (yVar != null) {
            yVar.b();
            this.h = null;
        }
        p pVar = this.i;
        if (pVar != null) {
            pVar.b();
            this.i = null;
        }
        ArrayList<com.baidu.trace.a.j> arrayList = this.l;
        if (arrayList != null) {
            arrayList.clear();
            this.l = null;
        }
        if (this.m != null) {
            this.m = null;
        }
        if (this.k != null) {
            this.k = null;
        }
        if (this.e != null) {
            this.e = null;
        }
        WeakReference<LBSTraceService> weakReference = this.f;
        if (weakReference != null) {
            weakReference.clear();
            this.f = null;
        }
        if (this.s != null) {
            this.s = null;
        }
        aj ajVar = a;
        if (ajVar != null) {
            ajVar.b();
            a = null;
        }
        TraceJniInterface.clearTrackData();
    }

    protected final void d() {
        this.q = true;
        this.p = true;
        this.o = false;
    }
}
