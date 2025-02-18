package com.baidu.trace.api.analysis;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.Point;

/* loaded from: classes.dex */
public class StartPoint extends Point {
    private String h;

    public StartPoint() {
    }

    public StartPoint(CoordType coordType) {
        this.b = coordType;
    }

    public StartPoint(String str) {
        this.h = str;
    }

    public String getAddress() {
        return this.h;
    }

    public void setAddress(String str) {
        this.h = str;
    }

    @Override // com.baidu.trace.model.Point
    public String toString() {
        return "StartPoint [location=" + this.a + ", coordType=" + this.b + ", locTime=" + this.d + ", address=" + this.h + "]";
    }
}
