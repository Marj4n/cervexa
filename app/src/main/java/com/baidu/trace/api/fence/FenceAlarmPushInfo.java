package com.baidu.trace.api.fence;

/* loaded from: classes.dex */
public class FenceAlarmPushInfo {
    protected long a;
    protected String b;
    protected String c;
    protected MonitoredAction d;
    protected AlarmPoint e;
    protected AlarmPoint f;

    public FenceAlarmPushInfo() {
    }

    public FenceAlarmPushInfo(long j, String str, String str2, MonitoredAction monitoredAction, AlarmPoint alarmPoint, AlarmPoint alarmPoint2) {
        this.a = j;
        this.b = str;
        this.c = str2;
        this.d = monitoredAction;
        this.e = alarmPoint;
        this.f = alarmPoint2;
    }

    public AlarmPoint getCurrentPoint() {
        return this.e;
    }

    public long getFenceId() {
        return this.a;
    }

    public String getFenceName() {
        return this.b;
    }

    public MonitoredAction getMonitoredAction() {
        return this.d;
    }

    public String getMonitoredPerson() {
        return this.c;
    }

    public AlarmPoint getPrePoint() {
        return this.f;
    }

    public void setCurrentPoint(AlarmPoint alarmPoint) {
        this.e = alarmPoint;
    }

    public void setFenceId(long j) {
        this.a = j;
    }

    public void setFenceName(String str) {
        this.b = str;
    }

    public void setMonitoredAction(MonitoredAction monitoredAction) {
        this.d = monitoredAction;
    }

    public void setMonitoredPerson(String str) {
        this.c = str;
    }

    public void setPrePoint(AlarmPoint alarmPoint) {
        this.f = alarmPoint;
    }

    public String toString() {
        return "FenceAlarmPushInfo [fenceId=" + this.a + ", fenceName=" + this.b + ", monitoredPerson=" + this.c + ", monitoredAction=" + this.d + ", currentPoint=" + this.e + ", prePoint=" + this.f + "]";
    }
}
