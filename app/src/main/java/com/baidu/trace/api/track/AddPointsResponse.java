package com.baidu.trace.api.track;

import com.baidu.trace.model.BaseResponse;

/* loaded from: classes.dex */
public class AddPointsResponse extends BaseResponse {
    private int a;
    private FailInfo b;

    public AddPointsResponse() {
    }

    public AddPointsResponse(int i) {
        this.tag = i;
    }

    public AddPointsResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public AddPointsResponse(int i, int i2, String str, int i3, FailInfo failInfo) {
        super(i, i2, str);
        this.a = i3;
        this.b = failInfo;
    }

    public FailInfo getFailInfo() {
        return this.b;
    }

    public int getSuccessNum() {
        return this.a;
    }

    public void setFailInfo(FailInfo failInfo) {
        this.b = failInfo;
    }

    public void setSuccessNum(int i) {
        this.a = i;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("AddPointsResponse{");
        stringBuffer.append("tag=");
        stringBuffer.append(this.tag);
        stringBuffer.append(", status=");
        stringBuffer.append(this.status);
        stringBuffer.append(", message=");
        stringBuffer.append(this.message);
        stringBuffer.append('\'');
        stringBuffer.append(", successNum=");
        stringBuffer.append(this.a);
        stringBuffer.append(", failInfo=");
        stringBuffer.append(this.b);
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
