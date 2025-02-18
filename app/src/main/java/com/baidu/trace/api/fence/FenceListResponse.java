package com.baidu.trace.api.fence;

import com.baidu.trace.model.BaseResponse;
import java.util.List;

/* loaded from: classes.dex */
public final class FenceListResponse extends BaseResponse {
    private int a;
    private FenceType b;
    private List<FenceInfo> c;

    public FenceListResponse(int i, int i2, String str, int i3, FenceType fenceType, List<FenceInfo> list) {
        this(i, i2, str, fenceType);
        this.a = i3;
        this.c = list;
    }

    public FenceListResponse(int i, int i2, String str, FenceType fenceType) {
        super(i, i2, str);
        this.b = fenceType;
    }

    public final List<FenceInfo> getFenceInfos() {
        return this.c;
    }

    public final FenceType getFenceType() {
        return this.b;
    }

    public final int getSize() {
        return this.a;
    }

    public final void setFenceInfos(List<FenceInfo> list) {
        this.c = list;
    }

    public final void setFenceType(FenceType fenceType) {
        this.b = fenceType;
    }

    public final void setSize(int i) {
        this.a = i;
    }

    public final String toString() {
        return "FenceListResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + ", size=" + this.a + ", fenceType=" + this.b + ", fenceInfos=" + this.c + "]";
    }
}
