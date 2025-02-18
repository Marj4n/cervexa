package com.serenegiant.media;

import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public interface Encoder {
    void encode(ByteBuffer byteBuffer);

    void encode(ByteBuffer byteBuffer, int i, long j);

    void frameAvailableSoon();

    String getOutputPath();

    @Deprecated
    boolean isAudio();

    boolean isCapturing();

    void prepare() throws Exception;

    void release();

    void signalEndOfInputStream();

    void start();

    void stop();
}
