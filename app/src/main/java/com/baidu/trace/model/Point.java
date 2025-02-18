package com.baidu.trace.model;

/* loaded from: classes.dex */
public class Point {
    protected LatLng a;
    protected CoordType b;
    protected double c;
    protected long d;
    protected int e;
    protected double f;
    protected double g;

    public Point() {
        this.b = CoordType.bd09ll;
    }

    public Point(LatLng latLng, CoordType coordType) {
        this.b = CoordType.bd09ll;
        this.a = latLng;
        this.b = coordType;
    }

    public CoordType getCoordType() {
        return this.b;
    }

    public int getDirection() {
        return this.e;
    }

    public double getHeight() {
        return this.g;
    }

    public long getLocTime() {
        return this.d;
    }

    public LatLng getLocation() {
        return this.a;
    }

    public double getRadius() {
        return this.c;
    }

    public double getSpeed() {
        return this.f;
    }

    public void setCoordType(CoordType coordType) {
        this.b = coordType;
    }

    public void setDirection(int i) {
        this.e = i;
    }

    public void setHeight(double d) {
        this.g = d;
    }

    public void setLocTime(long j) {
        this.d = j;
    }

    public void setLocation(LatLng latLng) {
        this.a = latLng;
    }

    public void setRadius(double d) {
        this.c = d;
    }

    public void setSpeed(double d) {
        this.f = d;
    }

    public String toString() {
        return "Point [location=" + this.a + ", coordType=" + this.b + ", radius=" + this.c + ", locTime=" + this.d + ", direction=" + this.e + ", speed=" + this.f + ", height=" + this.g + "]";
    }
}
