package com.serenegiant.media;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public abstract class AbstractRecorder {
    private static final boolean DEBUG = false;
    private static final String TAG = "AbstractRecorder";
    protected MediaEncoder mAudioEncoder;
    protected final String mOutputPath;
    protected MediaEncoder mVideoEncoder;
    protected int mStartedCount = 0;
    protected int mEncoderCount = 0;
    protected volatile boolean mIsStarted = false;

    abstract int addTrack(MediaFormat mediaFormat);

    public abstract int getHeight();

    public abstract Surface getInputSurface() throws IllegalStateException;

    public abstract int getWidth();

    protected abstract void internal_start();

    protected abstract void internal_stop();

    abstract void writeSampleData(int i, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo);

    public AbstractRecorder(String str) {
        this.mOutputPath = str;
    }

    public void prepare() throws IOException {
        MediaEncoder mediaEncoder = this.mVideoEncoder;
        if (mediaEncoder != null) {
            mediaEncoder.prepare();
        }
        MediaEncoder mediaEncoder2 = this.mAudioEncoder;
        if (mediaEncoder2 != null) {
            mediaEncoder2.prepare();
        }
    }

    public void startRecording() {
        MediaEncoder mediaEncoder = this.mVideoEncoder;
        if (mediaEncoder != null) {
            mediaEncoder.start();
        }
        MediaEncoder mediaEncoder2 = this.mAudioEncoder;
        if (mediaEncoder2 != null) {
            mediaEncoder2.start();
        }
    }

    public void stopRecording() {
        MediaEncoder mediaEncoder = this.mVideoEncoder;
        if (mediaEncoder != null) {
            mediaEncoder.stop();
        }
        this.mVideoEncoder = null;
        MediaEncoder mediaEncoder2 = this.mAudioEncoder;
        if (mediaEncoder2 != null) {
            mediaEncoder2.stop();
        }
        this.mAudioEncoder = null;
    }

    public void release() {
        MediaEncoder mediaEncoder = this.mVideoEncoder;
        if (mediaEncoder != null) {
            mediaEncoder.release();
            this.mVideoEncoder = null;
        }
        MediaEncoder mediaEncoder2 = this.mAudioEncoder;
        if (mediaEncoder2 != null) {
            mediaEncoder2.release();
            this.mAudioEncoder = null;
        }
    }

    public boolean isStarted() {
        return this.mIsStarted;
    }

    void addEncoder(MediaEncoder mediaEncoder) {
        if (mediaEncoder.isAudio()) {
            if (this.mAudioEncoder != null) {
                throw new IllegalArgumentException("Video encoder already added.");
            }
            this.mAudioEncoder = mediaEncoder;
        } else {
            if (this.mVideoEncoder != null) {
                throw new IllegalArgumentException("Video encoder already added.");
            }
            this.mVideoEncoder = mediaEncoder;
        }
        this.mEncoderCount = (this.mVideoEncoder != null ? 1 : 0) + (this.mAudioEncoder == null ? 0 : 1);
    }

    synchronized boolean start() {
        int i = this.mStartedCount + 1;
        this.mStartedCount = i;
        if (this.mEncoderCount > 0 && i == this.mEncoderCount) {
            internal_start();
            this.mIsStarted = true;
            notifyAll();
        }
        return this.mIsStarted;
    }

    synchronized void stop() {
        int i = this.mStartedCount - 1;
        this.mStartedCount = i;
        if (this.mEncoderCount > 0 && i <= 0) {
            this.mIsStarted = false;
            internal_stop();
        }
    }

    public void frameAvailableSoon() {
        MediaEncoder mediaEncoder = this.mVideoEncoder;
        if (mediaEncoder != null) {
            mediaEncoder.frameAvailableSoon();
        }
    }

    public String getOutputPath() {
        return this.mOutputPath;
    }
}
