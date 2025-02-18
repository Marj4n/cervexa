package com.baidu.mapapi.utils;

import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.platform.comapi.location.CoordinateType;

/* loaded from: classes.dex */
public class CoordinateConverter {
    private LatLng a;
    private CoordType b;

    /* renamed from: com.baidu.mapapi.utils.CoordinateConverter$1, reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[CoordType.values().length];
            a = iArr;
            try {
                iArr[CoordType.COMMON.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[CoordType.GPS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public enum CoordType {
        GPS,
        COMMON
    }

    private static LatLng a(LatLng latLng) {
        return a(latLng, CoordinateType.WGS84);
    }

    private static LatLng a(LatLng latLng, String str) {
        if (latLng == null) {
            return null;
        }
        return CoordUtil.Coordinate_encryptEx((float) latLng.longitude, (float) latLng.latitude, str);
    }

    private static LatLng b(LatLng latLng) {
        return a(latLng, CoordinateType.GCJ02);
    }

    public LatLng convert() {
        if (this.a == null) {
            return null;
        }
        if (this.b == null) {
            this.b = CoordType.GPS;
        }
        int i = AnonymousClass1.a[this.b.ordinal()];
        if (i == 1) {
            return b(this.a);
        }
        if (i != 2) {
            return null;
        }
        return a(this.a);
    }

    public CoordinateConverter coord(LatLng latLng) {
        this.a = latLng;
        return this;
    }

    public CoordinateConverter from(CoordType coordType) {
        this.b = coordType;
        return this;
    }
}
