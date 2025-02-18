package com.jieli.lib.dv.control.model;

/* loaded from: classes.dex */
public class GpsInfo {
    private double a;
    private double b;
    private double c;

    public double getLatitude() {
        return this.a;
    }

    public void setLatitude(double d) {
        this.a = d;
    }

    public double getLongitude() {
        return this.b;
    }

    public void setLongitude(double d) {
        this.b = d;
    }

    public double getSpeed() {
        return this.c;
    }

    public void setSpeed(double d) {
        this.c = d;
    }

    public String toString() {
        return "[Latitude:" + this.a + ",Longitude:" + this.b + ",Speed:" + this.c + "]";
    }
}
