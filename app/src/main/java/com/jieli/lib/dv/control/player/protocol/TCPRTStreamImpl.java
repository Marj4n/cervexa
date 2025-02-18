package com.jieli.lib.dv.control.player.protocol;

import com.jieli.lib.dv.control.player.IPlayerListener;
import com.jieli.lib.dv.control.player.PlayerConstants;
import com.jieli.lib.dv.control.player.Stream;
import com.jieli.lib.dv.control.utils.ClientContext;

/* loaded from: classes.dex */
public final class TCPRTStreamImpl extends Stream {
    private long mNativeContext;
    private IPlayerListener mPlayerListener;
    private final String tag = getClass().getSimpleName();

    private native boolean nativeCloseClient();

    private native boolean nativeConfigureRtp(int[] iArr, int[] iArr2, String str);

    private native boolean nativeCreateClient(String str, int i);

    private native boolean nativeInit();

    private native boolean nativeIsReceiving();

    private native void nativeRelease();

    private native boolean nativeSetFrameRate(int i);

    private native void nativeSetMute(boolean z);

    private native boolean nativeSetResolution(int i, int i2);

    private native boolean nativeSetSampleRate(int i);

    private native boolean nativeSetTimeout(int i);

    private native void nativeUseDeviceTimestamp(boolean z);

    public TCPRTStreamImpl() {
        Stream.loadLibrariesOnce(Stream.sLocalLibLoader);
        nativeInit();
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    public boolean create(int i) {
        return create(i, null);
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    public boolean create(int i, String str) {
        return create(i, str, true);
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean create(int i, String str, boolean z) {
        return nativeCreateClient(str, i);
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient, com.jieli.lib.dv.control.base.AbstractClient
    protected boolean close() {
        synchronized (this) {
            if (this.mNativeContext == 0) {
                return false;
            }
            return nativeCloseClient();
        }
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected void onError(final int i, final String str) {
        if (this.mPlayerListener == null) {
            return;
        }
        ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.player.protocol.TCPRTStreamImpl.1
            @Override // java.lang.Runnable
            public void run() {
                if (TCPRTStreamImpl.this.mPlayerListener != null) {
                    TCPRTStreamImpl.this.mPlayerListener.onError(i, str);
                }
            }
        });
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected void onStateChanged(final int i) {
        if (this.mPlayerListener == null) {
            return;
        }
        ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.player.protocol.TCPRTStreamImpl.2
            @Override // java.lang.Runnable
            public void run() {
                if (TCPRTStreamImpl.this.mPlayerListener != null) {
                    TCPRTStreamImpl.this.mPlayerListener.onStateChanged(i);
                }
            }
        });
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean isReceiving() {
        return nativeIsReceiving();
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean setResolution(int i, int i2) {
        return nativeSetResolution(i, i2);
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean setFrameRate(int i) {
        return nativeSetFrameRate(i);
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected boolean setSampleRate(int i) {
        return nativeSetSampleRate(i);
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected void registerStreamListener(IPlayerListener iPlayerListener) {
        this.mPlayerListener = iPlayerListener;
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected void unregisterStreamListener(IPlayerListener iPlayerListener) {
        this.mPlayerListener = null;
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    protected void onFrameReceived(int i, byte[] bArr, long j, long j2) {
        IPlayerListener iPlayerListener;
        if (i == 1) {
            IPlayerListener iPlayerListener2 = this.mPlayerListener;
            if (iPlayerListener2 != null) {
                iPlayerListener2.onAudio(i, DEFAULT_CHANNEL, bArr, j, j2);
                return;
            }
            return;
        }
        if ((i == 2 || i == 3) && (iPlayerListener = this.mPlayerListener) != null) {
            iPlayerListener.onVideo(i, DEFAULT_CHANNEL, bArr, j, j2);
        }
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    protected boolean setSoTimeout(int i) {
        return nativeSetTimeout(i);
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    protected boolean release() {
        synchronized (this) {
            this.mPlayerListener = null;
            nativeRelease();
            this.mNativeContext = 0L;
        }
        return true;
    }

    protected void finalize() {
        nativeRelease();
        this.mNativeContext = 0L;
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    protected void useDeviceTimestamp(boolean z) {
        nativeUseDeviceTimestamp(z);
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    protected boolean configure(int[] iArr, int[] iArr2) {
        return nativeConfigureRtp(iArr, iArr2, PlayerConstants.RTP_ADDRESS);
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    protected void setMute(boolean z) {
        nativeSetMute(z);
    }
}
