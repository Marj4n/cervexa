package com.serenegiant.media;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.util.Locale;

import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes2.dex */
public class MediaVideoEncoder extends MediaEncoder implements ISurfaceEncoder {
    private static final float BPP = 0.25f;
    private static final boolean DEBUG = false;
    private static final int FRAME_RATE = 25;
    private static final String MIME_TYPE = "video/avc";
    private static final String TAG = "MediaEncoder";
    private Surface mSurface;
    private int mVideoHeight;
    private int mVideoWidth;

    public MediaVideoEncoder(MediaMovieRecorder mediaMovieRecorder, IMediaCodecCallback iMediaCodecCallback) {
        this(640, 480, mediaMovieRecorder, iMediaCodecCallback);
    }

    public MediaVideoEncoder(int i, int i2, MediaMovieRecorder mediaMovieRecorder, IMediaCodecCallback iMediaCodecCallback) {
        super(false, mediaMovieRecorder, iMediaCodecCallback);
        this.mVideoWidth = i;
        this.mVideoHeight = i2;
    }

    @Override // com.serenegiant.media.IMediaCodec, com.serenegiant.media.Encoder
    public void prepare() throws IOException {
        this.mTrackIndex = -1;
        this.mIsEOS = false;
        this.mMuxerStarted = false;
        if (MediaCodecHelper.selectVideoEncoder("video/avc") == null) {
            Log.e(TAG, "Unable to find an appropriate codec for video/avc");
            return;
        }
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", this.mVideoWidth, this.mVideoHeight);
        createVideoFormat.setInteger("color-format", MediaCodecHelper.OMX_COLOR_FormatAndroidOpaque);
        createVideoFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, calcBitRate());
        createVideoFormat.setInteger("frame-rate", 25);
        createVideoFormat.setInteger("i-frame-interval", 10);
        this.mMediaCodec = MediaCodec.createEncoderByType("video/avc");
        this.mMediaCodec.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mSurface = this.mMediaCodec.createInputSurface();
        this.mMediaCodec.start();
        this.mIsPrepared = true;
        callOnPrepared();
    }

    @Override
    // com.serenegiant.media.MediaEncoder, com.serenegiant.media.IMediaCodec, com.serenegiant.media.Encoder
    public void release() {
        Surface surface = this.mSurface;
        if (surface != null) {
            surface.release();
            this.mSurface = null;
        }
        super.release();
    }

    @Override // com.serenegiant.media.IVideoEncoder
    public void setVideoSize(int i, int i2) throws IllegalArgumentException, IllegalStateException {
        if (i <= 0 || i2 <= 0) {
            throw new IllegalArgumentException(String.format(Locale.US, "size(%d,%d)", Integer.valueOf(i), Integer.valueOf(i2)));
        }
        if (isRunning()) {
            throw new IllegalStateException("already start capturing");
        }
        this.mVideoWidth = i;
        this.mVideoHeight = i2;
    }

    @Override // com.serenegiant.media.ISurfaceEncoder
    public Surface getInputSurface() throws IllegalStateException {
        Surface surface = this.mSurface;
        if (surface != null) {
            return surface;
        }
        throw new IllegalStateException("not prepared yet");
    }

    @Override // com.serenegiant.media.IVideoEncoder
    public int getWidth() {
        return this.mVideoWidth;
    }

    @Override // com.serenegiant.media.IVideoEncoder
    public int getHeight() {
        return this.mVideoHeight;
    }

    private int calcBitRate() {
        int i = (int) (this.mVideoWidth * 6.25f * this.mVideoHeight);
        Log.i(TAG, String.format("bitrate=%5.2f[Mbps]", Float.valueOf((i / 1024.0f) / 1024.0f)));
        return i;
    }
}
