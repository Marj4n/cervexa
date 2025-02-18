package com.baidu.trace.api.entity;

import com.baidu.trace.model.BaseRequest;

/* loaded from: classes.dex */
public class LocRequest extends BaseRequest {
    public LocRequest() {
    }

    public LocRequest(int i, long j) {
        super(i, j);
    }

    public LocRequest(long j) {
        this.serviceId = j;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("LocRequest{");
        stringBuffer.append("tag=");
        stringBuffer.append(this.tag);
        stringBuffer.append("serviceId=");
        stringBuffer.append(this.serviceId);
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
