package com.baidu.trace.api.analysis;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import com.baidu.trace.model.Point;

/* loaded from: classes.dex */
public class StayPoint extends Point {
    private long h;
    private long i;
    private int j;

    public StayPoint() {
    }

    public StayPoint(long j, long j2, int i, LatLng latLng, CoordType coordType) {
        this.h = j;
        this.i = j2;
        this.j = i;
        this.a = latLng;
        this.b = coordType;
    }

    public int getDuration() {
        return this.j;
    }

    public long getEndTime() {
        return this.i;
    }

    @Override // com.baidu.trace.model.Point
    public LatLng getLocation() {
        return this.a;
    }

    public long getStartTime() {
        return this.h;
    }

    public void setDuration(int i) {
        this.j = i;
    }

    public void setEndTime(long j) {
        this.i = j;
    }

    @Override // com.baidu.trace.model.Point
    public void setLocation(LatLng latLng) {
        this.a = latLng;
    }

    public void setStartTime(long j) {
        this.h = j;
    }

    @Override // com.baidu.trace.model.Point
    public String toString() {
        return "StayPoint [startTime=" + this.h + ", endTime=" + this.i + ", duration=" + this.j + ", location=" + this.a + ", coordType=" + this.b + "]";
    }
}
