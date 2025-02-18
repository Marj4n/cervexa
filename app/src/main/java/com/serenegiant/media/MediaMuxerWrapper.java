package com.serenegiant.media;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class MediaMuxerWrapper implements IMuxer {
    private static final String TAG = MediaMuxerWrapper.class.getSimpleName();
    private volatile boolean mIsStarted;
    private final MediaMuxer mMuxer;
    private boolean mReleased;

    public MediaMuxerWrapper(String str, int i) throws IOException {
        this.mMuxer = new MediaMuxer(str, i);
    }

    public MediaMuxerWrapper(FileDescriptor fileDescriptor, int i) throws IOException {
        this.mMuxer = new MediaMuxer(fileDescriptor, i);
    }

    @Override // com.serenegiant.media.IMuxer
    public int addTrack(MediaFormat mediaFormat) {
        return this.mMuxer.addTrack(mediaFormat);
    }

    @Override // com.serenegiant.media.IMuxer
    public void writeSampleData(int i, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        if (this.mReleased) {
            return;
        }
        this.mMuxer.writeSampleData(i, byteBuffer, bufferInfo);
    }

    @Override // com.serenegiant.media.IMuxer
    public void start() {
        this.mMuxer.start();
        this.mIsStarted = true;
    }

    @Override // com.serenegiant.media.IMuxer
    public void stop() {
        if (this.mIsStarted) {
            this.mIsStarted = false;
            this.mMuxer.stop();
        }
    }

    @Override // com.serenegiant.media.IMuxer
    public void release() {
        this.mIsStarted = false;
        if (this.mReleased) {
            return;
        }
        this.mReleased = true;
        try {
            this.mMuxer.release();
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    @Override // com.serenegiant.media.IMuxer
    public boolean isStarted() {
        return this.mIsStarted && !this.mReleased;
    }
}
