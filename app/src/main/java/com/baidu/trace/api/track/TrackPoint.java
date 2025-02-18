package com.baidu.trace.api.track;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.Point;
import java.util.Map;

/* loaded from: classes.dex */
public class TrackPoint extends Point {
    private String h;
    private String i;
    private Map<String, String> j;

    public TrackPoint() {
    }

    public TrackPoint(CoordType coordType) {
        this.b = coordType;
    }

    public TrackPoint(String str, String str2, Map<String, String> map) {
        this.h = str;
        this.i = str2;
        this.j = map;
    }

    public Map<String, String> getColumns() {
        return this.j;
    }

    public String getCreateTime() {
        return this.h;
    }

    public String getObjectName() {
        return this.i;
    }

    public void setColumns(Map<String, String> map) {
        this.j = map;
    }

    public void setCreateTime(String str) {
        this.h = str;
    }

    public void setObjectName(String str) {
        this.i = str;
    }

    @Override // com.baidu.trace.model.Point
    public String toString() {
        return "TrackPoint [location=" + this.a + ", coordType=" + this.b + ", radius=" + this.c + ", locTime=" + this.d + ", direction=" + this.e + ", speed=" + this.f + ", height=" + this.g + ", createTime=" + this.h + ", objectName=" + this.i + ", columns=" + this.j + "]";
    }
}
