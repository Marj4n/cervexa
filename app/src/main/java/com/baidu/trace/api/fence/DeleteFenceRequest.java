package com.baidu.trace.api.fence;

import com.baidu.trace.model.BaseRequest;
import java.util.List;

/* loaded from: classes.dex */
public final class DeleteFenceRequest extends BaseRequest {
    private String a;
    private List<Long> b;
    private FenceType c;

    private DeleteFenceRequest(int i, long j, String str, List<Long> list, FenceType fenceType) {
        super(i, j);
        this.a = str;
        this.b = list;
        this.c = fenceType;
    }

    public static DeleteFenceRequest buildLocalRequest(int i, long j, String str, List<Long> list) {
        return new DeleteFenceRequest(i, j, str, list, FenceType.local);
    }

    public static DeleteFenceRequest buildServerRequest(int i, long j, String str, List<Long> list) {
        return new DeleteFenceRequest(i, j, str, list, FenceType.server);
    }

    public final List<Long> getFenceIds() {
        return this.b;
    }

    public final FenceType getFenceType() {
        return this.c;
    }

    public final String getMonitoredPerson() {
        return this.a;
    }

    public final void setFenceIds(List<Long> list) {
        this.b = list;
    }

    public final void setMonitoredPerson(String str) {
        this.a = str;
    }

    public final String toString() {
        return "DeleteFenceRequest [tag=" + this.tag + ", serviceId=" + this.serviceId + ", monitoredPerson=" + this.a + ", fenceIds=" + this.b + ", fenceType=" + this.c + "]";
    }
}
