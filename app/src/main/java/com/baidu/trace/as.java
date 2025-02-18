package com.baidu.trace;

import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import com.baidu.trace.model.TraceLocation;

/* loaded from: classes.dex */
final class as extends OnEntityListener {
    private /* synthetic */ ar a;

    as(ar arVar) {
        this.a = arVar;
    }

    @Override // com.baidu.trace.api.entity.OnEntityListener
    public final void onReceiveLocation(TraceLocation traceLocation) {
        if (traceLocation.getStatus() == 0 && Math.abs(traceLocation.getLongitude()) >= 0.1d && Math.abs(traceLocation.getLatitude()) >= 0.1d && Math.abs(traceLocation.getRadius()) >= 0.1d) {
            LatLng latLng = new LatLng(traceLocation.getLatitude(), traceLocation.getLongitude());
            long c = com.baidu.trace.c.e.c(traceLocation.getTime());
            if (c <= 0) {
                c = com.baidu.trace.c.e.b();
            }
            ar.a(this.a, latLng, c, CoordType.bd09ll, traceLocation.getRadius());
        }
    }
}
