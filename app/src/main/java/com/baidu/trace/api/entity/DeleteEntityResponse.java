package com.baidu.trace.api.entity;

import com.baidu.trace.model.BaseResponse;

/* loaded from: classes.dex */
public final class DeleteEntityResponse extends BaseResponse {
    public DeleteEntityResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public final String toString() {
        return "DeleteEntityResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + "]";
    }
}
