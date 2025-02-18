package com.baidu.trace.api.analysis;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import com.baidu.trace.model.Point;

/* loaded from: classes.dex */
public class SpeedingPoint extends Point {
    private double h;
    private double i;

    public SpeedingPoint() {
    }

    public SpeedingPoint(CoordType coordType) {
        this.b = coordType;
    }

    public SpeedingPoint(LatLng latLng, CoordType coordType, long j, double d, double d2) {
        this.a = latLng;
        this.b = coordType;
        this.d = j;
        this.h = d;
        this.i = d2;
    }

    public double getActualSpeed() {
        return this.h;
    }

    public double getLimitSpeed() {
        return this.i;
    }

    public void setActualSpeed(double d) {
        this.h = d;
    }

    public void setLimitSpeed(double d) {
        this.i = d;
    }

    @Override // com.baidu.trace.model.Point
    public String toString() {
        return "SpeedingPoint [location=" + this.a + ", coordType=" + this.b + ", locTime=" + this.d + ", actualSpeed=" + this.h + ", limitSpeed=" + this.i + "]";
    }
}
