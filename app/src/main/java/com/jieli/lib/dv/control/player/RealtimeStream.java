package com.jieli.lib.dv.control.player;

import com.jieli.lib.dv.control.player.protocol.TCPRTStreamImpl;
import com.jieli.lib.dv.control.player.protocol.UDPRTStreamImpl;

/* loaded from: classes.dex */
public final class RealtimeStream extends Stream {
    private static String tag = RealtimeStream.class.getSimpleName();
    private Stream mStream;

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected void onError(int i, String str) {
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    protected void onFrameReceived(int i, byte[] bArr, long j, long j2) {
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    protected void onStateChanged(int i) {
    }

    public RealtimeStream() {
        this(1);
    }

    public RealtimeStream(int i) {
        if (i == 0) {
            this.mStream = new TCPRTStreamImpl();
        } else {
            this.mStream = new UDPRTStreamImpl();
        }
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    public boolean setSoTimeout(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("timeout can't be negative: " + i);
        }
        return this.mStream.setSoTimeout(i);
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
        return this.mStream.create(i, str, z);
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient, com.jieli.lib.dv.control.base.AbstractClient
    public boolean close() {
        return this.mStream.close();
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    public boolean setResolution(int i, int i2) {
        return this.mStream.setResolution(i, i2);
    }

    public void registerPlayerListener(OnRealTimeListener onRealTimeListener) {
        registerStreamListener(onRealTimeListener);
    }

    public void unregisterPlayerListener(OnRealTimeListener onRealTimeListener) {
        unregisterStreamListener(onRealTimeListener);
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    public boolean release() {
        return this.mStream.release();
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    public boolean setFrameRate(int i) {
        return this.mStream.setFrameRate(i);
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    public boolean setSampleRate(int i) {
        return this.mStream.setSampleRate(i);
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    public void registerStreamListener(IPlayerListener iPlayerListener) {
        this.mStream.registerStreamListener(iPlayerListener);
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    public void unregisterStreamListener(IPlayerListener iPlayerListener) {
        this.mStream.unregisterStreamListener(iPlayerListener);
    }

    @Override // com.jieli.lib.dv.control.player.IStreamClient
    public boolean isReceiving() {
        return this.mStream.isReceiving();
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    public void useDeviceTimestamp(boolean z) {
        this.mStream.useDeviceTimestamp(z);
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    public boolean configure(int[] iArr, int[] iArr2) {
        return this.mStream.configure(iArr, iArr2);
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    public boolean configure(int i, int i2) {
        return this.mStream.configure(i, i2);
    }

    @Override // com.jieli.lib.dv.control.player.Stream
    public void setMute(boolean z) {
        this.mStream.setMute(z);
    }
}
