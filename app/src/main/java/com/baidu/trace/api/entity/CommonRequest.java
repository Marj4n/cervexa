package com.baidu.trace.api.entity;

import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.CoordType;

/* loaded from: classes.dex */
public class CommonRequest extends BaseRequest {
    protected FilterCondition a;
    protected SortBy b;
    protected CoordType c;
    protected int d;
    protected int e;

    public CommonRequest() {
        this.c = CoordType.bd09ll;
        this.d = 1;
        this.e = 100;
    }

    public CommonRequest(int i, long j) {
        super(i, j);
        this.c = CoordType.bd09ll;
        this.d = 1;
        this.e = 100;
    }

    public CommonRequest(int i, long j, FilterCondition filterCondition, SortBy sortBy, CoordType coordType, int i2, int i3) {
        super(i, j);
        this.c = CoordType.bd09ll;
        this.d = 1;
        this.e = 100;
        this.a = filterCondition;
        this.b = sortBy;
        this.c = coordType;
        this.d = i2;
        this.e = i3;
    }

    public CommonRequest(int i, long j, FilterCondition filterCondition, CoordType coordType, int i2, int i3) {
        super(i, j);
        this.c = CoordType.bd09ll;
        this.d = 1;
        this.e = 100;
        this.a = filterCondition;
        this.c = coordType;
        this.d = i2;
        this.e = i3;
    }

    public CoordType getCoordTypeOutput() {
        return this.c;
    }

    public FilterCondition getFilterCondition() {
        return this.a;
    }

    public int getPageIndex() {
        return this.d;
    }

    public int getPageSize() {
        return this.e;
    }

    public SortBy getSortBy() {
        return this.b;
    }

    public void setCoordTypeOutput(CoordType coordType) {
        this.c = coordType;
    }

    public void setFilterCondition(FilterCondition filterCondition) {
        this.a = filterCondition;
    }

    public void setPageIndex(int i) {
        this.d = i;
    }

    public void setPageSize(int i) {
        this.e = i;
    }

    public void setSortBy(SortBy sortBy) {
        this.b = sortBy;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("CommonRequest{");
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
        stringBuffer.append(", pageIndex=");
        stringBuffer.append(this.d);
        stringBuffer.append(", pageSize=");
        stringBuffer.append(this.e);
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
