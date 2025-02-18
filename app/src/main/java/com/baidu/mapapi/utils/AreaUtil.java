package com.baidu.mapapi.utils;

import com.baidu.mapapi.model.LatLng;
import com.github.mikephil.charting.utils.Utils;

/* loaded from: classes.dex */
public class AreaUtil {
    public static double calculateArea(LatLng latLng, LatLng latLng2) {
        if (latLng != null && latLng2 != null) {
            LatLng latLng3 = new LatLng(latLng.latitude, latLng2.longitude);
            double distance = DistanceUtil.getDistance(latLng3, latLng2);
            double distance2 = DistanceUtil.getDistance(latLng, latLng3);
            if (distance != Utils.DOUBLE_EPSILON && distance2 != Utils.DOUBLE_EPSILON) {
                return distance * distance2;
            }
        }
        return Utils.DOUBLE_EPSILON;
    }
}
