package com.serenegiant.media;

/* loaded from: classes2.dex */
public interface IMediaCodecCallback {
    boolean onError(IMediaCodec iMediaCodec, Exception exc);

    boolean onFrameAvailable(IMediaCodec iMediaCodec, long j);

    void onPrepared(IMediaCodec iMediaCodec);

    void onRelease(IMediaCodec iMediaCodec);

    void onStart(IMediaCodec iMediaCodec);

    void onStop(IMediaCodec iMediaCodec);
}
