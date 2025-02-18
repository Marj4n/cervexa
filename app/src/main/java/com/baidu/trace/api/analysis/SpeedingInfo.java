package com.baidu.trace.api.analysis;

import java.util.List;

/* loaded from: classes.dex */
public class SpeedingInfo {
    private double a;
    private List<SpeedingPoint> b;

    public SpeedingInfo() {
    }

    public SpeedingInfo(double d, List<SpeedingPoint> list) {
        this.a = d;
        this.b = list;
    }

    public double getDistance() {
        return this.a;
    }

    public List<SpeedingPoint> getPoints() {
        return this.b;
    }

    public void setDistance(double d) {
        this.a = d;
    }

    public void setPoints(List<SpeedingPoint> list) {
        this.b = list;
    }

    public String toString() {
        return "SpeedingInfo [distance=" + this.a + ", points=" + this.b + "]";
    }
}
