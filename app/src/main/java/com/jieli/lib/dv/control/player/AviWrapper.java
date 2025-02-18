package com.jieli.lib.dv.control.player;

import com.jieli.lib.dv.control.exception.NativeException;
import com.jieli.lib.dv.control.utils.Dlog;

/* loaded from: classes.dex */
public class AviWrapper extends AbsRecording {
    private OnAviWrapperListener mOnRecordListener;
    private long nativeCustomData;
    private final String tag = getClass().getSimpleName();
    private boolean isRecording = false;

    private native boolean nativeConfigureAudio(long j, int i, int i2, int i3);

    private native boolean nativeConfigureVideo(long j, int i, int i2, int i3);

    private native long nativeCreate(String str) throws NativeException;

    private native boolean nativeInit() throws NativeException;

    private native boolean nativeRelease(long j);

    private native boolean nativeSetDuration(long j, long j2);

    private native boolean nativeSetPath(long j, String str);

    private native boolean nativeStartRecording(long j);

    private native boolean nativeStopRecording(long j);

    private native boolean nativeWriteData(long j, int i, byte[] bArr, int i2);

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
    protected boolean setFrameRate(int i) {
        return false;
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean setResolution(int i, int i2) {
        return false;
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean setSampleRate(int i) {
        return false;
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected void unregisterStreamListener(IPlayerListener iPlayerListener) {
    }

    public AviWrapper() {
        Stream.loadLibrariesOnce(Stream.sLocalLibLoader);
        try {
            if (nativeInit()) {
                return;
            }
            Dlog.e(this.tag, "Initialize native FieldID and MethodID failed.");
        } catch (NativeException e) {
            e.printStackTrace();
        }
    }

    public boolean configureVideo(int i, int i2, int i3) {
        return nativeConfigureVideo(this.nativeCustomData, i, i2, i3);
    }

    public boolean setAudioTrack(int i, int i2, int i3) {
        return nativeConfigureAudio(this.nativeCustomData, i, i3, i2);
    }

    public boolean setVideoDuration(long j) {
        return nativeSetDuration(this.nativeCustomData, j);
    }

    public boolean write(int i, byte[] bArr) {
        return nativeWriteData(this.nativeCustomData, i, bArr, bArr.length);
    }

    public boolean startRecording() {
        return nativeStartRecording(this.nativeCustomData);
    }

    public boolean stopRecording() {
        this.isRecording = false;
        return nativeStopRecording(this.nativeCustomData);
    }

    public boolean isRecording() {
        return this.isRecording;
    }

    public void setOnRecordListener(OnAviWrapperListener onAviWrapperListener) {
        this.mOnRecordListener = onAviWrapperListener;
    }

    public boolean destroy() {
        this.mOnRecordListener = null;
        this.isRecording = false;
        return nativeRelease(this.nativeCustomData);
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient, com.jieli.lib.dv.control.base.AbstractClient
    public boolean create() {
        try {
            this.nativeCustomData = nativeCreate(null);
            return true;
        } catch (NativeException e) {
            e.printStackTrace();
            this.nativeCustomData = 0L;
            return false;
        }
    }

    public boolean setPath(String str) {
        return nativeSetPath(this.nativeCustomData, str);
    }

    @Override // com.jieli.lib.dv.control.player.AbsRecording
    public boolean create(String str) {
        try {
            this.nativeCustomData = nativeCreate(str);
            return true;
        } catch (NativeException e) {
            e.printStackTrace();
            this.nativeCustomData = 0L;
            return false;
        }
    }

    @Override // com.jieli.lib.dv.control.player.AbsRecording, com.jieli.lib.dv.control.player.IStreamClient
    protected void onError(int i, String str) {
        OnAviWrapperListener onAviWrapperListener = this.mOnRecordListener;
        if (onAviWrapperListener != null) {
            onAviWrapperListener.onError(i, str);
        }
    }

    @Override // com.jieli.lib.dv.control.player.AbsRecording
    protected void onStateChanged(int i, String str) {
        if (i == 1) {
            this.isRecording = true;
        } else if (i == 2) {
            this.isRecording = false;
        }
        OnAviWrapperListener onAviWrapperListener = this.mOnRecordListener;
        if (onAviWrapperListener != null) {
            onAviWrapperListener.onStateChanged(i, str);
        }
    }
}
