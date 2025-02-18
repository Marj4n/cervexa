package com.baidu.mapapi.map;

import android.graphics.Point;
import android.graphics.PointF;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.C0074e;
import com.baidu.platform.comapi.map.E;

/* loaded from: classes.dex */
public final class Projection {
    private C0074e a;

    Projection(C0074e c0074e) {
        this.a = c0074e;
    }

    public LatLng fromScreenLocation(Point point) {
        C0074e c0074e;
        if (point == null || (c0074e = this.a) == null) {
            return null;
        }
        return CoordUtil.mc2ll(c0074e.b(point.x, point.y));
    }

    public float metersToEquatorPixels(float f) {
        if (f <= 0.0f) {
            return 0.0f;
        }
        return (float) (f / this.a.I());
    }

    public PointF toOpenGLLocation(LatLng latLng, MapStatus mapStatus) {
        if (latLng == null || mapStatus == null) {
            return null;
        }
        GeoPoint ll2mc = CoordUtil.ll2mc(latLng);
        E e = mapStatus.a;
        return new PointF((float) ((ll2mc.getLongitudeE6() - e.d) / e.n), (float) ((ll2mc.getLatitudeE6() - e.e) / e.n));
    }

    public PointF toOpenGLNormalization(LatLng latLng, MapStatus mapStatus) {
        if (latLng == null || mapStatus == null) {
            return null;
        }
        GeoPoint ll2mc = CoordUtil.ll2mc(latLng);
        E.a aVar = mapStatus.a.k;
        return new PointF((float) ((((ll2mc.getLongitudeE6() - aVar.a) * 2.0d) / Math.abs(aVar.b - aVar.a)) - 1.0d), (float) ((((ll2mc.getLatitudeE6() - aVar.d) * 2.0d) / Math.abs(aVar.c - aVar.d)) - 1.0d));
    }

    public Point toScreenLocation(LatLng latLng) {
        if (latLng == null || this.a == null) {
            return null;
        }
        return this.a.a(CoordUtil.ll2mc(latLng));
    }
}
