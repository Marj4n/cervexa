package com.baidu.trace.api.fence;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;

/* loaded from: classes.dex */
public class AlarmPoint {
    protected LatLng a;
    protected CoordType b;
    protected long c;
    protected double d;
    private long e;

    public AlarmPoint() {
    }

    public AlarmPoint(LatLng latLng, CoordType coordType, long j, double d) {
        this.a = latLng;
        this.b = coordType;
        this.c = j;
        this.d = d;
    }

    public AlarmPoint(LatLng latLng, CoordType coordType, long j, long j2, double d) {
        this.a = latLng;
        this.b = coordType;
        this.c = j;
        this.e = j2;
        this.d = d;
    }

    public CoordType getCoordType() {
        return this.b;
    }

    public long getCreateTime() {
        return this.e;
    }

    public long getLocTime() {
        return this.c;
    }

    public LatLng getLocation() {
        return this.a;
    }

    public double getRadius() {
        return this.d;
    }

    public void setCoordType(CoordType coordType) {
        this.b = coordType;
    }

    public void setCreateTime(long j) {
        this.e = j;
    }

    public void setLocTime(long j) {
        this.c = j;
    }

    public void setLocation(LatLng latLng) {
        this.a = latLng;
    }

    public void setRadius(double d) {
        this.d = d;
    }

    public String toString() {
        return "AlarmPoint [location=" + this.a + ", coordType=" + this.b + ", locTime=" + this.c + ", createTime=" + this.e + ", radius = " + this.d + "]";
    }
}
