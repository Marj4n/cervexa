package com.baidu.trace.api.analysis;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import com.baidu.trace.model.Point;

/* loaded from: classes.dex */
public class HarshBreakingPoint extends Point {
    private double h;
    private double i;
    private double j;

    public HarshBreakingPoint() {
    }

    public HarshBreakingPoint(CoordType coordType) {
        this.b = coordType;
    }

    public HarshBreakingPoint(LatLng latLng, CoordType coordType, long j, double d, double d2, double d3) {
        this.a = latLng;
        this.b = coordType;
        this.d = j;
        this.h = d;
        this.i = d2;
        this.j = d3;
    }

    public double getAcceleration() {
        return this.h;
    }

    public double getEndSpeed() {
        return this.j;
    }

    public double getInitialSpeed() {
        return this.i;
    }

    public void setAcceleration(double d) {
        this.h = d;
    }

    public void setEndSpeed(double d) {
        this.j = d;
    }

    public void setInitialSpeed(double d) {
        this.i = d;
    }

    @Override // com.baidu.trace.model.Point
    public String toString() {
        return "HarshBreakingPoint [location=" + this.a + ", coordType=" + this.b + ", locTime=" + this.d + ", acceleration=" + this.h + ", initialSpeed=" + this.i + ", endSpeed=" + this.j + "]";
    }
}
