package com.baidu.vi;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/* loaded from: classes.dex */
public class VMsg {
    private static final String a = VMsg.class.getSimpleName();
    private static Handler b;
    private static HandlerThread c;

    static class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            VMsg.OnUserCommand1(message.what, message.arg1, message.arg2, message.obj == null ? 0L : ((Long) message.obj).longValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native void OnUserCommand1(int i, int i2, int i3, long j);

    public static void destroy() {
        c.quit();
        c = null;
        b.removeCallbacksAndMessages(null);
        b = null;
    }

    public static void init() {
        HandlerThread handlerThread = new HandlerThread("VIMsgThread");
        c = handlerThread;
        handlerThread.start();
        b = new a(c.getLooper());
    }

    private static void postMessage(int i, int i2, int i3, long j) {
        Handler handler = b;
        if (handler == null) {
            return;
        }
        Message.obtain(handler, i, i2, i3, Long.valueOf(j)).sendToTarget();
    }
}
