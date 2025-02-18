package com.baidu.trace.api.entity;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.Point;
import java.util.Map;

/* loaded from: classes.dex */
public class LatestLocation extends Point {
    private String h;
    private double i;
    private String j;
    private Map<String, String> k;

    public LatestLocation() {
    }

    public LatestLocation(CoordType coordType) {
        this.b = coordType;
    }

    public LatestLocation(String str, double d) {
        this.h = str;
        this.i = d;
    }

    public Map<String, String> getColumns() {
        return this.k;
    }

    public double getDistance() {
        return this.i;
    }

    public String getFloor() {
        return this.h;
    }

    public String getObjectName() {
        return this.j;
    }

    public void setColumns(Map<String, String> map) {
        this.k = map;
    }

    public void setDistance(double d) {
        this.i = d;
    }

    public void setFloor(String str) {
        this.h = str;
    }

    public void setObjectName(String str) {
        this.j = str;
    }

    @Override // com.baidu.trace.model.Point
    public String toString() {
        return "LatestLocation [location=" + this.a + ", coordType=" + this.b + ", radius=" + this.c + ", locTime=" + this.d + ", direction=" + this.e + ", speed=" + this.f + ", height=" + this.g + ", floor=" + this.h + ", distance=" + this.i + ", objectName=" + this.j + ", columns=" + this.k + "]";
    }
}
