package com.baidu.trace.api.analysis;

import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.ProcessOption;

/* loaded from: classes.dex */
public final class DrivingBehaviorRequest extends BaseRequest {
    private String a;
    private long b;
    private long c;
    private int d;
    private ThresholdOption e;
    private ProcessOption f;
    private CoordType g;

    public DrivingBehaviorRequest() {
        this.d = 0;
        this.e = null;
        this.g = CoordType.bd09ll;
    }

    public DrivingBehaviorRequest(int i, long j) {
        super(i, j);
        this.d = 0;
        this.e = null;
        this.g = CoordType.bd09ll;
    }

    public DrivingBehaviorRequest(int i, long j, String str) {
        super(i, j);
        this.d = 0;
        this.e = null;
        this.g = CoordType.bd09ll;
        this.a = str;
    }

    public DrivingBehaviorRequest(int i, long j, String str, long j2, long j3, int i2, ProcessOption processOption, CoordType coordType) {
        super(i, j);
        this.d = 0;
        this.e = null;
        this.g = CoordType.bd09ll;
        this.a = str;
        this.b = j2;
        this.c = j3;
        this.d = i2;
        this.f = processOption;
        this.g = coordType;
    }

    public DrivingBehaviorRequest(int i, long j, String str, long j2, long j3, ThresholdOption thresholdOption, ProcessOption processOption, CoordType coordType) {
        super(i, j);
        this.d = 0;
        this.e = null;
        this.g = CoordType.bd09ll;
        this.a = str;
        this.b = j2;
        this.c = j3;
        this.e = thresholdOption;
        this.f = processOption;
        this.g = coordType;
    }

    public final CoordType getCoordTypeOutput() {
        return this.g;
    }

    public final long getEndTime() {
        return this.c;
    }

    public final String getEntityName() {
        return this.a;
    }

    public final ProcessOption getProcessOption() {
        return this.f;
    }

    public final int getSpeedingThreshold() {
        return this.d;
    }

    public final long getStartTime() {
        return this.b;
    }

    public final ThresholdOption getThresholdOption() {
        return this.e;
    }

    public final void setCoordTypeOutput(CoordType coordType) {
        this.g = coordType;
    }

    public final void setEndTime(long j) {
        this.c = j;
    }

    public final void setEntityName(String str) {
        this.a = str;
    }

    public final void setProcessOption(ProcessOption processOption) {
        this.f = processOption;
    }

    public final void setSpeedingThreshold(int i) {
        this.d = i;
    }

    public final void setStartTime(long j) {
        this.b = j;
    }

    public final void setThresholdOption(ThresholdOption thresholdOption) {
        this.e = thresholdOption;
    }

    public final String toString() {
        return "DrivingBehaviorRequest [tag=" + this.tag + ", serviceId=" + this.serviceId + ", entityName=" + this.a + ", startTime=" + this.b + ", endTime=" + this.c + ", thresholdOption=" + this.e + ", processOption=" + this.f + ", coordTypeOutput=" + this.g + "]";
    }
}
