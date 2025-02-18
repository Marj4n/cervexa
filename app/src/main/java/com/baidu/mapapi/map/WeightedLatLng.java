package com.baidu.mapapi.map;

import android.graphics.Point;
import com.baidu.mapapi.map.l;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.github.mikephil.charting.utils.Utils;

/* loaded from: classes.dex */
public class WeightedLatLng extends l.a {
    public static final double DEFAULT_INTENSITY = 1.0d;
    private Point a;
    public final double intensity;
    public final LatLng latLng;

    public WeightedLatLng(LatLng latLng) {
        this(latLng, 1.0d);
    }

    public WeightedLatLng(LatLng latLng, double d) {
        if (latLng == null) {
            throw new IllegalArgumentException("latLng can not be null");
        }
        this.latLng = latLng;
        GeoPoint ll2mc = CoordUtil.ll2mc(latLng);
        this.a = new Point((int) ll2mc.getLongitudeE6(), (int) ll2mc.getLatitudeE6());
        if (d > Utils.DOUBLE_EPSILON) {
            this.intensity = d;
        } else {
            this.intensity = 1.0d;
        }
    }

    @Override // com.baidu.mapapi.map.l.a
    Point a() {
        return this.a;
    }
}
