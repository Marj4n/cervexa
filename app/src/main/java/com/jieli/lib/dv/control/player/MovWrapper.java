package com.jieli.lib.dv.control.player;

/* loaded from: classes.dex */
public class MovWrapper extends AbsRecording {
    public static final int TIME_MASTER_SEQUENCE = 2;
    public static final int TIME_MASTER_SYSTEM = 1;
    private OnRecordListener mOnRecordListener = null;
    private long nativeCustomData;

    private native boolean nativeClose(long j);

    private native boolean nativeCreate(String str, long j);

    private native int nativeGetDuration(long j);

    private native long nativeInit();

    private native boolean nativeSetAudioTrack(int i, int i2, int i3, long j);

    private native boolean nativeSetDuration(int i, long j);

    private native boolean nativeSetFrameRate(int i, long j);

    private native boolean nativeSetResolution(int i, int i2, long j);

    private native boolean nativeSetTimeMaster(int i, long j);

    private native boolean nativeWrite(int i, byte[] bArr, int i2, long j);

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean create(int i) {
        return false;
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean create(int i, String str, boolean z) {
        return false;
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean isReceiving() {
        return false;
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected void registerStreamListener(IPlayerListener iPlayerListener) {
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean setSampleRate(int i) {
        return false;
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected void unregisterStreamListener(IPlayerListener iPlayerListener) {
    }

    public MovWrapper() {
        Stream.loadLibrariesOnce(Stream.sLocalLibLoader);
        this.nativeCustomData = nativeInit();
    }

    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.mOnRecordListener = onRecordListener;
    }

    @Override // com.jieli.lib.dv.control.player.AbsRecording
    public boolean create(String str) {
        return nativeCreate(str, this.nativeCustomData);
    }

    @Override // com.jieli.lib.dv.control.player.AbsRecording, com.jieli.lib.dv.control.player.IStreamClient, com.jieli.lib.dv.control.base.AbstractClient
    public boolean close() {
        boolean nativeClose = nativeClose(this.nativeCustomData);
        if (nativeClose) {
            this.nativeCustomData = 0L;
        }
        return nativeClose;
    }

    @Override // com.jieli.lib.dv.control.player.AbsRecording, com.jieli.lib.dv.control.player.IStreamClient
    protected void onError(int i, String str) {
        OnRecordListener onRecordListener = this.mOnRecordListener;
        if (onRecordListener != null) {
            onRecordListener.onError(i, str);
        }
    }

    @Override // com.jieli.lib.dv.control.player.AbsRecording
    protected void onStateChanged(int i, String str) {
        OnRecordListener onRecordListener = this.mOnRecordListener;
        if (onRecordListener != null) {
            onRecordListener.onStateChanged(i, str);
        }
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    public boolean setFrameRate(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("FPS cannot <= 0");
        }
        return nativeSetFrameRate(i, this.nativeCustomData);
    }

    public boolean write(int i, byte[] bArr) {
        return nativeWrite(i, bArr, bArr.length, this.nativeCustomData);
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    public boolean setResolution(int i, int i2) {
        return nativeSetResolution(i, i2, this.nativeCustomData);
    }

    public boolean setAudioTrack(int i, int i2, int i3) {
        return nativeSetAudioTrack(i, i2, i3, this.nativeCustomData);
    }

    public boolean setVideoDuration(int i) {
        return nativeSetDuration(i, this.nativeCustomData);
    }

    public int getVideoDuration() {
        return nativeGetDuration(this.nativeCustomData);
    }

    public boolean setTimeMaster(int i) {
        return nativeSetTimeMaster(i, this.nativeCustomData);
    }
}
