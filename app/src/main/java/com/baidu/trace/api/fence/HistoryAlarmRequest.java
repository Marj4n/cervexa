package com.baidu.trace.api.fence;

import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.CoordType;
import java.util.List;

/* loaded from: classes.dex */
public final class HistoryAlarmRequest extends BaseRequest {
    private long a;
    private long b;
    private String c;
    private List<Long> d;
    private CoordType e;
    private FenceType f;

    private HistoryAlarmRequest(int i, long j, long j2, long j3, String str, List<Long> list, CoordType coordType, FenceType fenceType) {
        super(i, j);
        this.e = CoordType.bd09ll;
        this.a = j2;
        this.b = j3;
        this.c = str;
        this.d = list;
        this.e = coordType;
        this.f = fenceType;
    }

    public static HistoryAlarmRequest buildLocalRequest(int i, long j, long j2, long j3, String str, List<Long> list) {
        return new HistoryAlarmRequest(i, j, j2, j3, str, list, CoordType.bd09ll, FenceType.local);
    }

    public static HistoryAlarmRequest buildServerRequest(int i, long j, long j2, long j3, String str, List<Long> list, CoordType coordType) {
        return new HistoryAlarmRequest(i, j, j2, j3, str, list, coordType, FenceType.server);
    }

    public final CoordType getCoordTypeOutput() {
        return this.e;
    }

    public final long getEndTime() {
        return this.b;
    }

    public final List<Long> getFenceIds() {
        return this.d;
    }

    public final FenceType getFenceType() {
        return this.f;
    }

    public final String getMonitoredPerson() {
        return this.c;
    }

    public final long getStartTime() {
        return this.a;
    }

    public final void setCoordTypeOutput(CoordType coordType) {
        if (FenceType.server == this.f) {
            this.e = coordType;
        }
    }

    public final void setEndTime(long j) {
        this.b = j;
    }

    public final void setFenceIds(List<Long> list) {
        this.d = list;
    }

    public final void setMonitoredPerson(String str) {
        this.c = str;
    }

    public final void setStartTime(long j) {
        this.a = j;
    }

    public final String toString() {
        return "HistoryAlarmRequest [tag=" + this.tag + ", serviceId=" + this.serviceId + ", startTime=" + this.a + ", endTime=" + this.b + ", monitoredPerson=" + this.c + ", fenceIds=" + this.d + ", coordTypeOutput=" + this.e + ", fenceType=" + this.f + "]";
    }
}
