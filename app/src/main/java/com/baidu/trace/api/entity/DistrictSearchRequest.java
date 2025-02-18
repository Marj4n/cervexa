package com.baidu.trace.api.entity;

import com.baidu.trace.model.CoordType;

/* loaded from: classes.dex */
public final class DistrictSearchRequest extends CommonRequest {
    private String f;
    private ReturnType g;

    public DistrictSearchRequest() {
        this.g = ReturnType.all;
    }

    public DistrictSearchRequest(int i, long j, FilterCondition filterCondition, SortBy sortBy, CoordType coordType, String str, ReturnType returnType, int i2, int i3) {
        super(i, j, filterCondition, sortBy, coordType, i2, i3);
        this.g = ReturnType.all;
        this.f = str;
        this.g = returnType;
    }

    public DistrictSearchRequest(int i, long j, FilterCondition filterCondition, CoordType coordType, String str, ReturnType returnType, int i2, int i3) {
        super(i, j, filterCondition, coordType, i2, i3);
        this.g = ReturnType.all;
        this.f = str;
        this.g = returnType;
    }

    public DistrictSearchRequest(int i, long j, String str, ReturnType returnType) {
        super(i, j);
        this.g = ReturnType.all;
        this.f = str;
        this.g = returnType;
    }

    public final String getKeyword() {
        return this.f;
    }

    public final ReturnType getReturnType() {
        return this.g;
    }

    public final void setKeyword(String str) {
        this.f = str;
    }

    public final void setReturnType(ReturnType returnType) {
        this.g = returnType;
    }

    @Override // com.baidu.trace.api.entity.CommonRequest
    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("DistrictSearchRequest{");
        stringBuffer.append("tag=");
        stringBuffer.append(this.tag);
        stringBuffer.append(", serviceId=");
        stringBuffer.append(this.serviceId);
        stringBuffer.append(", filterCondition=");
        stringBuffer.append(this.a);
        stringBuffer.append(", sortBy=");
        stringBuffer.append(this.b);
        stringBuffer.append(", coordTypeOutput=");
        stringBuffer.append(this.c);
        stringBuffer.append(", keyword='");
        stringBuffer.append(this.f);
        stringBuffer.append("'");
        stringBuffer.append(", returnType=");
        stringBuffer.append(this.g);
        stringBuffer.append(", pageIndex=");
        stringBuffer.append(this.d);
        stringBuffer.append(", pageSize=");
        stringBuffer.append(this.e);
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
