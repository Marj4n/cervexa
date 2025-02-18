package com.baidu.trace.api.entity;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import java.util.List;

/* loaded from: classes.dex */
public final class PolygonSearchRequest extends CommonRequest {
    private List<LatLng> f;
    private CoordType g;

    public PolygonSearchRequest() {
        this.g = CoordType.bd09ll;
    }

    public PolygonSearchRequest(int i, long j, FilterCondition filterCondition, SortBy sortBy, CoordType coordType, List<LatLng> list, CoordType coordType2, int i2, int i3) {
        super(i, j, filterCondition, sortBy, coordType, i2, i3);
        this.g = CoordType.bd09ll;
        this.f = list;
        this.g = coordType2;
    }

    public PolygonSearchRequest(int i, long j, FilterCondition filterCondition, CoordType coordType, List<LatLng> list, CoordType coordType2, int i2, int i3) {
        super(i, j, filterCondition, coordType, i2, i3);
        this.g = CoordType.bd09ll;
        this.f = list;
        this.g = coordType2;
    }

    public PolygonSearchRequest(int i, long j, List<LatLng> list, CoordType coordType) {
        super(i, j);
        this.g = CoordType.bd09ll;
        this.f = list;
        this.g = coordType;
    }

    public final CoordType getCoordTypeInput() {
        return this.g;
    }

    public final List<LatLng> getVertexes() {
        return this.f;
    }

    public final void setCoordTypeInput(CoordType coordType) {
        this.g = coordType;
    }

    public final void setVertexes(List<LatLng> list) {
        this.f = list;
    }

    @Override // com.baidu.trace.api.entity.CommonRequest
    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("PolygonSearchRequest{");
        stringBuffer.append("tag=");
        stringBuffer.append(this.tag);
        stringBuffer.append(", serviceId=");
        stringBuffer.append(this.serviceId);
        stringBuffer.append(", vertexes=");
        stringBuffer.append(this.f);
        stringBuffer.append(", coordTypeInput=");
        stringBuffer.append(this.g);
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
