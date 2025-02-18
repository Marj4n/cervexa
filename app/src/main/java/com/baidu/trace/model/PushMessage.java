package com.baidu.trace.model;

import com.baidu.trace.api.fence.FenceAlarmPushInfo;

/* loaded from: classes.dex */
public final class PushMessage {
    private String a;
    private FenceAlarmPushInfo b;

    public final FenceAlarmPushInfo getFenceAlarmPushInfo() {
        return this.b;
    }

    public final String getMessage() {
        return this.a;
    }

    public final void setFenceAlarmPushInfo(FenceAlarmPushInfo fenceAlarmPushInfo) {
        this.b = fenceAlarmPushInfo;
    }

    public final void setMessage(String str) {
        this.a = str;
    }

    public final String toString() {
        return "PushMessage [fenceAlarmPushInfo=" + this.b + "]";
    }
}
