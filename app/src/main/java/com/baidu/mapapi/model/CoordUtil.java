package com.baidu.mapapi.model;

import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.model.inner.Point;
import java.util.List;

/* loaded from: classes.dex */
public class CoordUtil {
    public static LatLng Coordinate_encryptEx(float f, float f2, String str) {
        return com.baidu.platform.comapi.util.b.a(f, f2, str);
    }

    public static LatLng decodeLocation(String str) {
        return com.baidu.platform.comapi.util.b.a(str);
    }

    public static List<LatLng> decodeLocationList(String str) {
        return com.baidu.platform.comapi.util.b.c(str);
    }

    public static List<List<LatLng>> decodeLocationList2D(String str) {
        return com.baidu.platform.comapi.util.b.d(str);
    }

    public static LatLng decodeNodeLocation(String str) {
        return com.baidu.platform.comapi.util.b.b(str);
    }

    public static double getDistance(Point point, Point point2) {
        return com.baidu.platform.comjni.tools.a.a(point, point2);
    }

    public static int getMCDistanceByOneLatLngAndRadius(LatLng latLng, int i) {
        return com.baidu.platform.comapi.util.b.a(latLng, i);
    }

    public static GeoPoint ll2mc(LatLng latLng) {
        return com.baidu.platform.comapi.util.b.a(latLng);
    }

    public static Point ll2point(LatLng latLng) {
        return com.baidu.platform.comapi.util.b.b(latLng);
    }

    public static LatLng mc2ll(GeoPoint geoPoint) {
        return com.baidu.platform.comapi.util.b.a(geoPoint);
    }
}
