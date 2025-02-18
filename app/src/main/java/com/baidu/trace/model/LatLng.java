package com.baidu.trace.model;

/* loaded from: classes.dex */
public final class LatLng {
    public double latitude;
    public double longitude;

    public LatLng() {
    }

    public LatLng(double d, double d2) {
        this.latitude = d;
        this.longitude = d2;
    }

    public final double getLatitude() {
        return this.latitude;
    }

    public final double getLongitude() {
        return this.longitude;
    }

    public final void setLatitude(double d) {
        this.latitude = d;
    }

    public final void setLongitude(double d) {
        this.longitude = d;
    }

    public final String toString() {
        return "LatLng [latitude=" + this.latitude + ", longitude=" + this.longitude + "]";
    }
}
