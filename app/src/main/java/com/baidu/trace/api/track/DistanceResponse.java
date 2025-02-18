package com.baidu.trace.api.track;

import com.baidu.trace.model.BaseResponse;

/* loaded from: classes.dex */
public final class DistanceResponse extends BaseResponse {
    private String a;
    private double b;

    public DistanceResponse() {
    }

    public DistanceResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public DistanceResponse(int i, int i2, String str, String str2, double d) {
        super(i, i2, str);
        this.a = str2;
        this.b = d;
    }

    public final double getDistance() {
        return this.b;
    }

    public final String getEntityName() {
        return this.a;
    }

    public final void setDistance(double d) {
        this.b = d;
    }

    public final void setEntityName(String str) {
        this.a = str;
    }

    public final String toString() {
        return "DistanceResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + ", entityName=" + this.a + ", distance=" + this.b + "]";
    }
}
