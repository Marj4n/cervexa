package com.baidu.trace.api.track;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import com.baidu.trace.model.Point;
import java.util.Map;

/* loaded from: classes.dex */
public class LatestPoint extends Point {
    private String h;
    private String i;
    private Map<String, String> j;

    public LatestPoint() {
    }

    public LatestPoint(LatLng latLng, CoordType coordType) {
        super(latLng, coordType);
    }

    public LatestPoint(LatLng latLng, CoordType coordType, String str, String str2, Map<String, String> map) {
        super(latLng, coordType);
        this.h = str;
        this.i = str2;
        this.j = map;
    }

    public Map<String, String> getColumns() {
        return this.j;
    }

    public String getFloor() {
        return this.h;
    }

    public String getObjectName() {
        return this.i;
    }

    public void setColumns(Map<String, String> map) {
        this.j = map;
    }

    public void setFloor(String str) {
        this.h = str;
    }

    public void setObjectName(String str) {
        this.i = str;
    }

    @Override // com.baidu.trace.model.Point
    public String toString() {
        return "LatestPoint [location=" + this.a + ", coordType=" + this.b + ", radius=" + this.c + ", locTime=" + this.d + ", direction=" + this.e + ", speed=" + this.f + ", height=" + this.g + ", floor=" + this.h + ", objectName=" + this.i + ", columns=" + this.j + "]";
    }
}
