package com.baidu.trace.api.fence;

/* loaded from: classes.dex */
public final class FenceAlarmInfo extends FenceAlarmPushInfo {
    public FenceAlarmInfo() {
    }

    public FenceAlarmInfo(long j, String str, String str2, MonitoredAction monitoredAction, AlarmPoint alarmPoint, AlarmPoint alarmPoint2) {
        super(j, str, str2, monitoredAction, alarmPoint, alarmPoint2);
    }

    @Override // com.baidu.trace.api.fence.FenceAlarmPushInfo
    public final String toString() {
        return "FenceAlarmInfo [fenceId=" + this.a + ", fenceName=" + this.b + ", monitoredPerson=" + this.c + ", monitoredAction=" + this.d + ", currentPoint=" + this.e + ", prePoint=" + this.f + "]";
    }
}
