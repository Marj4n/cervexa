package com.baidu.trace.api.fence;

import com.baidu.trace.model.BaseResponse;
import java.util.List;

/* loaded from: classes.dex */
public final class HistoryAlarmResponse extends BaseResponse {
    private int a;
    private int b;
    private FenceType c;
    private List<FenceAlarmInfo> d;

    public HistoryAlarmResponse(int i, int i2, String str, int i3, int i4, FenceType fenceType, List<FenceAlarmInfo> list) {
        this(i, i2, str, fenceType);
        this.a = i3;
        this.b = i4;
        this.d = list;
    }

    public HistoryAlarmResponse(int i, int i2, String str, FenceType fenceType) {
        super(i, i2, str);
        this.c = fenceType;
    }

    public final List<FenceAlarmInfo> getFenceAlarmInfos() {
        return this.d;
    }

    public final FenceType getFenceType() {
        return this.c;
    }

    public final int getSize() {
        return this.b;
    }

    public final int getTotal() {
        return this.a;
    }

    public final void setFenceAlarmInfos(List<FenceAlarmInfo> list) {
        this.d = list;
    }

    public final void setFenceType(FenceType fenceType) {
        this.c = fenceType;
    }

    public final void setSize(int i) {
        this.b = i;
    }

    public final void setTotal(int i) {
        this.a = i;
    }

    public final String toString() {
        return "HistoryAlarmResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + ", total=" + this.a + ", size=" + this.b + ", fenceType=" + this.c + ", fenceAlarmInfos=" + this.d + "]";
    }
}
