package com.baidu.trace.api.entity;

import com.baidu.trace.model.BaseResponse;

/* loaded from: classes.dex */
public final class UpdateEntityResponse extends BaseResponse {
    public UpdateEntityResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public final String toString() {
        return "UpdateEntityResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + "]";
    }
}
