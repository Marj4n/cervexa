package com.baidu.trace.api.entity;

import com.baidu.trace.model.CoordType;

/* loaded from: classes.dex */
public final class SearchRequest extends CommonRequest {
    private String f;

    public SearchRequest() {
    }

    public SearchRequest(int i, long j) {
        super(i, j);
    }

    public SearchRequest(int i, long j, String str, FilterCondition filterCondition, SortBy sortBy, CoordType coordType, int i2, int i3) {
        super(i, j, filterCondition, sortBy, coordType, i2, i3);
        this.f = str;
    }

    public SearchRequest(int i, long j, String str, FilterCondition filterCondition, CoordType coordType, int i2, int i3) {
        super(i, j, filterCondition, coordType, i2, i3);
        this.f = str;
    }

    public final String getKeyword() {
        return this.f;
    }

    public final void setKeyword(String str) {
        this.f = str;
    }

    @Override // com.baidu.trace.api.entity.CommonRequest
    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("SearchRequest{");
        stringBuffer.append("tag='");
        stringBuffer.append(this.tag);
        stringBuffer.append(", serviceId='");
        stringBuffer.append(this.serviceId);
        stringBuffer.append(", keyword='");
        stringBuffer.append(this.f);
        stringBuffer.append('\'');
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
