package com.baidu.trace.api.track;

import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.ProcessOption;

/* loaded from: classes.dex */
public final class DistanceRequest extends BaseRequest {
    private String a;
    private long b;
    private long c;
    private boolean d;
    private ProcessOption e;
    private SupplementMode f;

    public DistanceRequest() {
        this.d = false;
    }

    public DistanceRequest(int i, long j) {
        super(i, j);
        this.d = false;
    }

    public DistanceRequest(int i, long j, String str) {
        super(i, j);
        this.d = false;
        this.a = str;
    }

    public DistanceRequest(int i, long j, String str, long j2, long j3, boolean z, ProcessOption processOption, SupplementMode supplementMode) {
        super(i, j);
        this.d = false;
        this.a = str;
        this.b = j2;
        this.c = j3;
        this.d = z;
        this.e = processOption;
        this.f = supplementMode;
    }

    public final long getEndTime() {
        return this.c;
    }

    public final String getEntityName() {
        return this.a;
    }

    public final ProcessOption getProcessOption() {
        return this.e;
    }

    public final long getStartTime() {
        return this.b;
    }

    public final SupplementMode getSupplementMode() {
        return this.f;
    }

    public final boolean isProcessed() {
        return this.d;
    }

    public final void setEndTime(long j) {
        this.c = j;
    }

    public final void setEntityName(String str) {
        this.a = str;
    }

    public final void setProcessOption(ProcessOption processOption) {
        this.e = processOption;
    }

    public final void setProcessed(boolean z) {
        this.d = z;
    }

    public final void setStartTime(long j) {
        this.b = j;
    }

    public final void setSupplementMode(SupplementMode supplementMode) {
        this.f = supplementMode;
    }

    public final String toString() {
        return "DistanceRequest [tag=" + this.tag + ", serviceId=" + this.serviceId + ", entityName=" + this.a + ", startTime=" + this.b + ", endTime=" + this.c + ", isProcessed=" + this.d + ", processOption=" + this.e + ", supplementMode=" + this.f + "]";
    }
}
