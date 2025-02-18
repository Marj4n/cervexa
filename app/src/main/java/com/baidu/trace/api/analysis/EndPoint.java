package com.baidu.trace.api.analysis;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.Point;

/* loaded from: classes.dex */
public class EndPoint extends Point {
    private String h;

    public EndPoint() {
    }

    public EndPoint(CoordType coordType) {
        this.b = coordType;
    }

    public EndPoint(String str) {
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
        return "EndPoint [location=" + this.a + ", coordType=" + this.b + ", locTime=" + this.d + ", address=" + this.h + "]";
    }
}
