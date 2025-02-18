package com.jieli.lib.dv.control.player;

/* loaded from: classes.dex */
public interface IPlayerListener {
    void onAudio(int i, int i2, byte[] bArr, long j, long j2);

    void onError(int i, String str);

    void onStateChanged(int i);

    void onVideo(int i, int i2, byte[] bArr, long j, long j2);
}
