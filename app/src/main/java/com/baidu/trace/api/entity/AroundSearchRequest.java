package com.baidu.trace.api.entity;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;

/* loaded from: classes.dex */
public final class AroundSearchRequest extends CommonRequest {
    private LatLng f;
    private double g;
    private CoordType h;

    public AroundSearchRequest() {
        this.h = CoordType.bd09ll;
    }

    public AroundSearchRequest(int i, long j) {
        super(i, j);
        this.h = CoordType.bd09ll;
    }

    public AroundSearchRequest(int i, long j, FilterCondition filterCondition, CoordType coordType, int i2, int i3) {
        super(i, j, filterCondition, coordType, i2, i3);
        this.h = CoordType.bd09ll;
    }

    public AroundSearchRequest(int i, long j, LatLng latLng, double d, CoordType coordType, FilterCondition filterCondition, SortBy sortBy, CoordType coordType2, int i2, int i3) {
        super(i, j, filterCondition, sortBy, coordType2, i2, i3);
        this.h = CoordType.bd09ll;
        this.f = latLng;
        this.g = d;
        this.h = coordType;
    }

    public AroundSearchRequest(int i, long j, LatLng latLng, double d, CoordType coordType, FilterCondition filterCondition, CoordType coordType2, int i2, int i3) {
        super(i, j, filterCondition, coordType2, i2, i3);
        this.h = CoordType.bd09ll;
        this.f = latLng;
        this.g = d;
        this.h = coordType;
    }

    public final LatLng getCenter() {
        return this.f;
    }

    public final CoordType getCoordTypeInput() {
        return this.h;
    }

    public final double getRadius() {
        return this.g;
    }

    public final void setCenter(LatLng latLng) {
        this.f = latLng;
    }

    public final void setCoordTypeInput(CoordType coordType) {
        this.h = coordType;
    }

    public final void setRadius(double d) {
        this.g = d;
    }

    @Override // com.baidu.trace.api.entity.CommonRequest
    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("AroundSearchRequest{");
        stringBuffer.append("tag=");
        stringBuffer.append(this.tag);
        stringBuffer.append(", serviceId=");
        stringBuffer.append(this.serviceId);
        stringBuffer.append(", center=");
        stringBuffer.append(this.f);
        stringBuffer.append(", radius=");
        stringBuffer.append(this.g);
        stringBuffer.append(", coordTypeInput=");
        stringBuffer.append(this.h);
        stringBuffer.append(", filterCondition=");
        stringBuffer.append(this.a);
        stringBuffer.append(", sortBy=");
        stringBuffer.append(this.b);
        stringBuffer.append(", coordTypeOutput=");
        stringBuffer.append(this.c);
        stringBuffer.append(", pageIndex=");
        stringBuffer.append(this.d);
        stringBuffer.append(", pageSize=");
        stringBuffer.append(this.e);
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
