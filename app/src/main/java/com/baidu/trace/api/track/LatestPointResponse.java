package com.baidu.trace.api.track;

import com.baidu.trace.model.BaseResponse;

/* loaded from: classes.dex */
public final class LatestPointResponse extends BaseResponse {
    private String a;
    private LatestPoint b;
    private double c;

    public LatestPointResponse() {
    }

    public LatestPointResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public LatestPointResponse(int i, int i2, String str, String str2, LatestPoint latestPoint, double d) {
        super(i, i2, str);
        this.a = str2;
        this.b = latestPoint;
        this.c = d;
    }

    public final String getEntityName() {
        return this.a;
    }

    public final LatestPoint getLatestPoint() {
        return this.b;
    }

    public final double getLimitSpeed() {
        return this.c;
    }

    public final void setEntityName(String str) {
        this.a = str;
    }

    public final void setLatestPoint(LatestPoint latestPoint) {
        this.b = latestPoint;
    }

    public final void setLimitSpeed(double d) {
        this.c = d;
    }

    public final String toString() {
        return "LatestPointResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + ", entityName=" + this.a + ", latestPoint=" + this.b + ", limitSpeed=" + this.c + "]";
    }
}
