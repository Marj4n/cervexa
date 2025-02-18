package com.baidu.trace.api.entity;

import com.baidu.trace.model.BaseRequest;

/* loaded from: classes.dex */
public final class DeleteEntityRequest extends BaseRequest {
    private String a;

    public DeleteEntityRequest() {
    }

    public DeleteEntityRequest(int i, long j) {
        super(i, j);
    }

    public DeleteEntityRequest(int i, long j, String str) {
        super(i, j);
        this.a = str;
    }

    public final String getEntityName() {
        return this.a;
    }

    public final void setEntityName(String str) {
        this.a = str;
    }

    public final String toString() {
        return "DeleteEntityRequest [tag=" + this.tag + ", serviceId=" + this.serviceId + ", entityName=" + this.a + "]";
    }
}
