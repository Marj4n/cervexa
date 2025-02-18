package com.baidu.trace.api.fence;

import com.baidu.trace.model.BaseResponse;
import java.util.List;

/* loaded from: classes.dex */
public final class MonitoredStatusByLocationResponse extends BaseResponse {
    private int a;
    private FenceType b;
    private List<MonitoredStatusInfo> c;

    public MonitoredStatusByLocationResponse(int i, int i2, String str, int i3, FenceType fenceType, List<MonitoredStatusInfo> list) {
        this(i, i2, str, fenceType);
        this.a = i3;
        this.c = list;
    }

    public MonitoredStatusByLocationResponse(int i, int i2, String str, FenceType fenceType) {
        super(i, i2, str);
        this.b = fenceType;
    }

    public final FenceType getFenceType() {
        return this.b;
    }

    public final List<MonitoredStatusInfo> getMonitoredStatusInfos() {
        return this.c;
    }

    public final int getSize() {
        return this.a;
    }

    public final void setFenceType(FenceType fenceType) {
        this.b = fenceType;
    }

    public final void setMonitoredStatusInfos(List<MonitoredStatusInfo> list) {
        this.c = list;
    }

    public final void setSize(int i) {
        this.a = i;
    }

    public final String toString() {
        return "MonitoredStatusByLocationResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + ", size=" + this.a + ", fenceType=" + this.b + ", monitoredStatusInfos=" + this.c + "]";
    }
}
