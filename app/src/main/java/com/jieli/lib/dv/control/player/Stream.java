package com.jieli.lib.dv.control.player;

/* loaded from: classes.dex */
public abstract class Stream extends IStreamClient {
    protected static int DEFAULT_CHANNEL = 0;
    public static final int TYPE_DEV_GPS = 8;
    public static final int TYPE_RTS_H264 = 3;
    public static final int TYPE_RTS_JPG = 2;
    public static final int TYPE_RTS_PCM = 1;

    public static final class Protocol {
        public static final int TCP_MODE = 0;
        public static final int UDP_MODE = 1;
    }

    public static final class Status {
        public static final int END = 6;
        protected static final int IDLE = 0;
        public static final int PAUSE = 4;
        public static final int PLAYING = 2;
        public static final int PREPARE = 1;
        public static final int STOP = 5;
    }

    protected boolean configure(int[] iArr, int[] iArr2) {
        return false;
    }

    protected boolean isMute() {
        return false;
    }

    protected abstract void onFrameReceived(int i, byte[] bArr, long j, long j2);

    protected abstract boolean release();

    protected void setMute(boolean z) {
    }

    protected abstract boolean setSoTimeout(int i);

    protected void useDeviceTimestamp(boolean z) {
    }

    protected boolean configure(int i, int i2) {
        return configure(new int[]{i}, new int[]{i2});
    }
}
