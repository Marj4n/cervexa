package com.baidu.trace.api.entity;

import com.baidu.trace.model.CoordType;

/* loaded from: classes.dex */
public final class EntityListRequest extends CommonRequest {
    private CoordType f;

    public EntityListRequest() {
        this.f = CoordType.bd09ll;
    }

    public EntityListRequest(int i, long j) {
        super(i, j);
        this.f = CoordType.bd09ll;
    }

    public EntityListRequest(int i, long j, FilterCondition filterCondition, CoordType coordType, int i2, int i3) {
        super(i, j, filterCondition, coordType, i2, i3);
        this.f = CoordType.bd09ll;
    }

    @Override // com.baidu.trace.api.entity.CommonRequest
    public final CoordType getCoordTypeOutput() {
        return this.f;
    }

    @Override // com.baidu.trace.api.entity.CommonRequest
    public final void setCoordTypeOutput(CoordType coordType) {
        this.f = coordType;
    }

    @Override // com.baidu.trace.api.entity.CommonRequest
    public final String toString() {
        return "EntityListRequest [tag=" + this.tag + ", serviceId=" + this.serviceId + ", coordTypeOutput=" + this.f + ", filterCondition=" + this.a + ", pageIndex=" + this.d + ", pageSize=" + this.e + "]";
    }
}
