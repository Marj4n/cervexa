package com.baidu.trace.api.track;

import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.ProcessOption;

/* loaded from: classes.dex */
public final class LatestPointRequest extends BaseRequest {
    private String a;
    private ProcessOption b;
    private CoordType c;

    public LatestPointRequest() {
        this.c = CoordType.bd09ll;
    }

    public LatestPointRequest(int i, long j) {
        super(i, j);
        this.c = CoordType.bd09ll;
    }

    public LatestPointRequest(int i, long j, String str) {
        this(i, j);
        this.a = str;
    }

    public LatestPointRequest(int i, long j, String str, ProcessOption processOption, CoordType coordType) {
        this(i, j, str);
        this.b = processOption;
        this.c = coordType;
    }

    public final CoordType getCoordTypeOutput() {
        return this.c;
    }

    public final String getEntityName() {
        return this.a;
    }

    public final ProcessOption getProcessOption() {
        return this.b;
    }

    public final void setCoordTypeOutput(CoordType coordType) {
        this.c = coordType;
    }

    public final void setEntityName(String str) {
        this.a = str;
    }

    public final void setProcessOption(ProcessOption processOption) {
        this.b = processOption;
    }

    public final String toString() {
        return "LatestPointRequest [tag=" + this.tag + ", serviceId=" + this.serviceId + ", entityName=" + this.a + ", processOption=" + this.b + ", coordTypeOutput=" + this.c + "]";
    }
}
