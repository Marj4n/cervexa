package com.baidu.trace.api.entity;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;

/* loaded from: classes.dex */
public final class BoundSearchRequest extends CommonRequest {
    private LatLng f;
    private LatLng g;
    private CoordType h;

    public BoundSearchRequest() {
        this.h = CoordType.bd09ll;
    }

    public BoundSearchRequest(int i, long j) {
        super(i, j);
        this.h = CoordType.bd09ll;
    }

    public BoundSearchRequest(int i, long j, FilterCondition filterCondition, CoordType coordType, int i2, int i3) {
        super(i, j, filterCondition, coordType, i2, i3);
        this.h = CoordType.bd09ll;
    }

    public BoundSearchRequest(int i, long j, LatLng latLng, LatLng latLng2, CoordType coordType, FilterCondition filterCondition, SortBy sortBy, CoordType coordType2, int i2, int i3) {
        super(i, j, filterCondition, sortBy, coordType2, i2, i3);
        this.h = CoordType.bd09ll;
        this.f = latLng;
        this.g = latLng2;
        this.h = coordType;
    }

    public BoundSearchRequest(int i, long j, LatLng latLng, LatLng latLng2, CoordType coordType, FilterCondition filterCondition, CoordType coordType2, int i2, int i3) {
        super(i, j, filterCondition, coordType2, i2, i3);
        this.h = CoordType.bd09ll;
        this.f = latLng;
        this.g = latLng2;
        this.h = coordType;
    }

    public final CoordType getCoordTypeInput() {
        return this.h;
    }

    @Override // com.baidu.trace.api.entity.CommonRequest
    public final FilterCondition getFilterCondition() {
        return this.a;
    }

    public final LatLng getLowerLeft() {
        return this.f;
    }

    public final LatLng getUpperRight() {
        return this.g;
    }

    public final void setCoordTypeInput(CoordType coordType) {
        this.h = coordType;
    }

    public final void setLowerLeft(LatLng latLng) {
        this.f = latLng;
    }

    public final void setUpperRight(LatLng latLng) {
        this.g = latLng;
    }

    @Override // com.baidu.trace.api.entity.CommonRequest
    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("BoundSearchRequest{");
        stringBuffer.append("tag=");
        stringBuffer.append(this.tag);
        stringBuffer.append(", serviceId=");
        stringBuffer.append(this.serviceId);
        stringBuffer.append(", lowerLeft=");
        stringBuffer.append(this.f);
        stringBuffer.append(", upperRight=");
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
