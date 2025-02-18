package com.serenegiant.media;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.view.Surface;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes2.dex */
public class SurfaceEncoder extends AbstractVideoEncoder implements ISurfaceEncoder {
    protected Surface mInputSurface;

    @Override // com.serenegiant.media.AbstractEncoder
    public int getCaptureFormat() {
        return 0;
    }

    public SurfaceEncoder(IRecorder iRecorder, EncoderListener encoderListener) {
        super("video/avc", iRecorder, encoderListener);
    }

    @Override // com.serenegiant.media.ISurfaceEncoder
    public Surface getInputSurface() {
        return this.mInputSurface;
    }

    @Override // com.serenegiant.media.AbstractEncoder
    protected boolean internalPrepare() throws Exception {
        this.mTrackIndex = -1;
        this.mRecorderStarted = false;
        this.mIsCapturing = true;
        this.mIsEOS = false;
        if (MediaCodecHelper.selectVideoEncoder("video/avc") == null) {
            return true;
        }
        boolean z = this.mWidth >= 1000 || this.mHeight >= 1000;
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", this.mWidth, this.mHeight);
        createVideoFormat.setInteger("color-format", MediaCodecHelper.OMX_COLOR_FormatAndroidOpaque);
        createVideoFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, this.mBitRate > 0 ? this.mBitRate : VideoConfig.getBitrate(this.mWidth, this.mHeight));
        createVideoFormat.setInteger("frame-rate", this.mFramerate > 0 ? this.mFramerate : VideoConfig.getCaptureFps());
        createVideoFormat.setInteger("i-frame-interval", this.mIFrameIntervals > 0 ? this.mIFrameIntervals : VideoConfig.getIFrame());
        this.mMediaCodec = MediaCodec.createEncoderByType("video/avc");
        this.mMediaCodec.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mInputSurface = this.mMediaCodec.createInputSurface();
        this.mMediaCodec.start();
        return z;
    }

    @Override // com.serenegiant.media.AbstractEncoder, com.serenegiant.media.Encoder
    public void release() {
        super.release();
        this.mInputSurface = null;
    }

    @Override // com.serenegiant.media.AbstractEncoder, com.serenegiant.media.Encoder
    public void signalEndOfInputStream() {
        this.mIsEOS = true;
        if (this.mMediaCodec != null) {
            this.mMediaCodec.signalEndOfInputStream();
        }
    }
}
