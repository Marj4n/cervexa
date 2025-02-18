package rx.android.schedulers;

import android.os.Handler;
import rx.Scheduler;

@Deprecated
/* loaded from: classes2.dex */
public final class HandlerScheduler extends LooperScheduler {
    @Override // rx.android.schedulers.LooperScheduler, rx.Scheduler
    public /* bridge */ /* synthetic */ Scheduler.Worker createWorker() {
        return super.createWorker();
    }

    @Deprecated
    public static HandlerScheduler from(Handler handler) {
        if (handler == null) {
            throw new NullPointerException("handler == null");
        }
        return new HandlerScheduler(handler);
    }

    private HandlerScheduler(Handler handler) {
        super(handler);
    }
}
