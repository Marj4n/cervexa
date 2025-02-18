package com.baidu.trace.api.fence;

import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.CoordType;
import java.util.List;

/* loaded from: classes.dex */
public final class FenceListRequest extends BaseRequest {
    private String a;
    private List<Long> b;
    private CoordType c;
    private FenceType d;

    private FenceListRequest(int i, long j, String str, List<Long> list, CoordType coordType, FenceType fenceType) {
        super(i, j);
        this.c = CoordType.bd09ll;
        this.a = str;
        this.b = list;
        this.c = coordType;
        this.d = fenceType;
    }

    public static FenceListRequest buildLocalRequest(int i, long j, String str, List<Long> list) {
        return new FenceListRequest(i, j, str, list, CoordType.bd09ll, FenceType.local);
    }

    public static FenceListRequest buildServerRequest(int i, long j, String str, List<Long> list, CoordType coordType) {
        return new FenceListRequest(i, j, str, list, coordType, FenceType.server);
    }

    public final CoordType getCoordTypeOutput() {
        return this.c;
    }

    public final List<Long> getFenceIds() {
        return this.b;
    }

    public final FenceType getFenceType() {
        return this.d;
    }

    public final String getMonitoredPerson() {
        return this.a;
    }

    public final void setCoordTypeOutput(CoordType coordType) {
        if (FenceType.server == this.d) {
            this.c = coordType;
        }
    }

    public final void setFenceIds(List<Long> list) {
        this.b = list;
    }

    public final void setMonitoredPerson(String str) {
        this.a = str;
    }

    public final String toString() {
        return "FenceListRequest [tag=" + this.tag + ", serviceId=" + this.serviceId + ", monitoredPerson=" + this.a + ", fenceIds=" + this.b + ", coordTypeOutput=" + this.c + ", fenceType=" + this.d + "]";
    }
}
