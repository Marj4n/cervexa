package com.baidu.trace.model;

/* loaded from: classes.dex */
public class BaseRequest {
    private boolean a = false;
    public long serviceId;
    public int tag;

    public BaseRequest() {
    }

    public BaseRequest(int i, long j) {
        this.tag = i;
        this.serviceId = j;
    }

    public void cancel() {
        this.a = true;
    }

    public long getServiceId() {
        return this.serviceId;
    }

    public int getTag() {
        return this.tag;
    }

    public boolean isCanceled() {
        return this.a;
    }

    public void setServiceId(long j) {
        this.serviceId = j;
    }

    public void setTag(int i) {
        this.tag = i;
    }
}
