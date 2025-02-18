package com.baidu.trace;

import com.baidu.trace.api.fence.CreateFenceResponse;
import com.baidu.trace.api.fence.DeleteFenceResponse;
import com.baidu.trace.api.fence.FenceListResponse;
import com.baidu.trace.api.fence.HistoryAlarmResponse;
import com.baidu.trace.api.fence.MonitoredStatusByLocationResponse;
import com.baidu.trace.api.fence.MonitoredStatusResponse;
import com.baidu.trace.api.fence.OnFenceListener;
import com.baidu.trace.api.fence.UpdateFenceResponse;
import com.baidu.trace.model.BaseResponse;

/* loaded from: classes.dex */
final class ai implements Runnable {
    private /* synthetic */ int a;
    private /* synthetic */ OnFenceListener b;
    private /* synthetic */ BaseResponse c;

    ai(int i, OnFenceListener onFenceListener, BaseResponse baseResponse) {
        this.a = i;
        this.b = onFenceListener;
        this.c = baseResponse;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.a) {
            case 1:
                this.b.onCreateFenceCallback((CreateFenceResponse) this.c);
                break;
            case 2:
                this.b.onUpdateFenceCallback((UpdateFenceResponse) this.c);
                break;
            case 3:
                this.b.onDeleteFenceCallback((DeleteFenceResponse) this.c);
                break;
            case 4:
                this.b.onFenceListCallback((FenceListResponse) this.c);
                break;
            case 5:
                this.b.onMonitoredStatusCallback((MonitoredStatusResponse) this.c);
                break;
            case 6:
                this.b.onMonitoredStatusByLocationCallback((MonitoredStatusByLocationResponse) this.c);
                break;
            case 7:
                this.b.onHistoryAlarmCallback((HistoryAlarmResponse) this.c);
                break;
        }
    }
}
