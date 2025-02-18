package com.baidu.trace.api.fence;

/* loaded from: classes.dex */
public final class MonitoredStatusInfo {
    private long a;
    private MonitoredStatus b;

    public MonitoredStatusInfo() {
        this.b = MonitoredStatus.unknown;
    }

    public MonitoredStatusInfo(long j, MonitoredStatus monitoredStatus) {
        this.b = MonitoredStatus.unknown;
        this.a = j;
        this.b = monitoredStatus;
    }

    public final long getFenceId() {
        return this.a;
    }

    public final MonitoredStatus getMonitoredStatus() {
        return this.b;
    }

    public final void setFenceId(long j) {
        this.a = j;
    }

    public final void setMonitoredStatus(MonitoredStatus monitoredStatus) {
        this.b = monitoredStatus;
    }

    public final String toString() {
        return "MonitoredStatusInfo [fenceId=" + this.a + ", monitoredStatus=" + this.b + "]";
    }
}
