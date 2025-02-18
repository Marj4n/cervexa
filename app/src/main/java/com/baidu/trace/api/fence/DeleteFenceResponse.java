package com.baidu.trace.api.fence;

import com.baidu.trace.model.BaseResponse;
import java.util.List;

/* loaded from: classes.dex */
public final class DeleteFenceResponse extends BaseResponse {
    private List<Long> a;
    private FenceType b;

    public DeleteFenceResponse() {
    }

    public DeleteFenceResponse(int i, int i2, String str, FenceType fenceType) {
        super(i, i2, str);
        this.b = fenceType;
    }

    public DeleteFenceResponse(int i, int i2, String str, List<Long> list, FenceType fenceType) {
        this(i, i2, str, fenceType);
        this.a = list;
    }

    public final List<Long> getFenceIds() {
        return this.a;
    }

    public final FenceType getFenceType() {
        return this.b;
    }

    public final void setFenceIds(List<Long> list) {
        this.a = list;
    }

    public final void setFenceType(FenceType fenceType) {
        this.b = fenceType;
    }

    public final String toString() {
        return "DeleteFenceResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + ", fenceType=" + this.b + ", fenceIds=" + this.a + "]";
    }
}
