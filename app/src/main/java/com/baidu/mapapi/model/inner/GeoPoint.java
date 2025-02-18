package com.baidu.mapapi.model.inner;

/* loaded from: classes.dex */
public class GeoPoint {
    private double a;
    private double b;

    public GeoPoint(double d, double d2) {
        this.a = d;
        this.b = d2;
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        GeoPoint geoPoint = (GeoPoint) obj;
        return this.a == geoPoint.a && this.b == geoPoint.b;
    }

    public double getLatitudeE6() {
        return this.a;
    }

    public double getLongitudeE6() {
        return this.b;
    }

    public void setLatitudeE6(double d) {
        this.a = d;
    }

    public void setLongitudeE6(double d) {
        this.b = d;
    }

    public String toString() {
        return "GeoPoint: Latitude: " + this.a + ", Longitude: " + this.b;
    }
}
