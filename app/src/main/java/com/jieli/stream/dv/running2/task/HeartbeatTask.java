package com.jieli.stream.dv.running2.task;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.utils.Dlog;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IActions;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class HeartbeatTask extends Thread {
    private static final int DEFAULT_HEARTBEAT_PERIOD = 5000;
    public static final int DEFAULT_HEARTBEAT_TIMEOUT = 6;
    private WeakReference<Context> mContextWeakRef;
    private String tag = getClass().getSimpleName();
    private int mTimeoutCount = 0;
    private int mTimeout = 6;
    private long mPeriod = 5000;
    private boolean isHeartbeatTaskRunning = false;

    public boolean isHeartbeatTaskRunning() {
        return this.isHeartbeatTaskRunning;
    }

    public void stopRunning() {
        this.isHeartbeatTaskRunning = false;
        this.mTimeoutCount = 0;
    }

    public void setPeriodAndTimeout(long j, int i) {
        if (j <= 0) {
            j = 5000;
        }
        if (i <= 0) {
            i = 6;
        }
        this.mPeriod = j;
        this.mTimeout = i;
    }

    public long getPeriod() {
        return this.mPeriod;
    }

    public long getTimeout() {
        return this.mTimeout;
    }

    public HeartbeatTask(Context context) {
        this.mContextWeakRef = new WeakReference<>(context);
    }

    public void resetTimeoutCount() {
        this.mTimeoutCount = 0;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        super.run();
        this.isHeartbeatTaskRunning = true;
        this.mTimeoutCount = 0;
        Dlog.w(this.tag, "HeartbeatTask: start");
        while (true) {
            if (!this.isHeartbeatTaskRunning) {
                break;
            }
            ClientManager.getClient().tryToKeepAlive(new SendResponse() { // from class: com.jieli.stream.dv.running2.task.HeartbeatTask.1
                @Override // com.jieli.lib.dv.control.connect.response.Response
                public void onResponse(Integer num) {
                    if (num.intValue() != 1) {
                        Dbug.e(HeartbeatTask.this.tag, "Send failed!!!");
                    }
                }
            });
            SystemClock.sleep(this.mPeriod);
            int i = this.mTimeoutCount + 1;
            this.mTimeoutCount = i;
            if (i > this.mTimeout) {
                this.isHeartbeatTaskRunning = false;
                if (this.mContextWeakRef.get() != null) {
                    this.mContextWeakRef.get().sendBroadcast(new Intent(IActions.ACTION_CONNECTION_TIMEOUT));
                }
                Dlog.e(this.tag, "HeartbeatTask: over time");
            }
        }
        Dlog.i(this.tag, "HeartbeatTask ending..." + this.mTimeoutCount);
    }
}
