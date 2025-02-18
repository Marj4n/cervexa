package com.serenegiant.media;

import android.media.MediaCodec;
import android.media.MediaFormat;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public interface IMuxer {
    int addTrack(MediaFormat mediaFormat);

    boolean isStarted();

    void release();

    void start();

    void stop();

    void writeSampleData(int i, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo);
}
