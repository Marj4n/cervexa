package com.baidu.trace.api.entity;

import com.baidu.trace.model.BaseResponse;

/* loaded from: classes.dex */
public final class AddEntityResponse extends BaseResponse {
    public AddEntityResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public final String toString() {
        return "AddEntityResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + "]";
    }
}
