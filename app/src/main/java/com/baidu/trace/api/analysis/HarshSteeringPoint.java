package com.baidu.trace.api.analysis;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import com.baidu.trace.model.Point;

/* loaded from: classes.dex */
public class HarshSteeringPoint extends Point {
    private double h;
    private TurnType i;
    private double j;

    public HarshSteeringPoint() {
    }

    public HarshSteeringPoint(CoordType coordType) {
        this.b = coordType;
    }

    public HarshSteeringPoint(LatLng latLng, CoordType coordType, long j, double d, TurnType turnType, double d2) {
        this.a = latLng;
        this.b = coordType;
        this.d = j;
        this.h = d;
        this.i = turnType;
        this.j = d2;
    }

    public double getCentripetalAcceleration() {
        return this.h;
    }

    public double getTurnSpeed() {
        return this.j;
    }

    public TurnType getTurnType() {
        return this.i;
    }

    public void setCentripetalAcceleration(double d) {
        this.h = d;
    }

    public void setTurnSpeed(double d) {
        this.j = d;
    }

    public void setTurnType(TurnType turnType) {
        this.i = turnType;
    }

    @Override // com.baidu.trace.model.Point
    public String toString() {
        return "HarshSteeringPoint [location=" + this.a + ", coordType=" + this.b + ", locTime=" + this.d + ", centripetalAcceleration=" + this.h + ", turnType=" + this.i + ", turnSpeed=" + this.j + "]";
    }
}
