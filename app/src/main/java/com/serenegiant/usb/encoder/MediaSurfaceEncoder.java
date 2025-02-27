package com.serenegiant.usb.encoder;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import com.serenegiant.media.MediaCodecHelper;
import com.serenegiant.usb.encoder.MediaEncoder;
import java.io.IOException;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes2.dex */
public class MediaSurfaceEncoder extends MediaEncoder implements IVideoEncoder {
    private static final float BPP = 0.5f;
    private static final boolean DEBUG = true;
    private static final int FRAME_RATE = 15;
    private static final String MIME_TYPE = "video/avc";
    private static final String TAG = "MediaSurfaceEncoder";
    protected static int[] recognizedFormats = {MediaCodecHelper.OMX_COLOR_FormatAndroidOpaque};
    private final int mHeight;
    private Surface mSurface;
    private final int mWidth;

    public MediaSurfaceEncoder(MediaMuxerWrapper mediaMuxerWrapper, int i, int i2, MediaEncoderListener mediaEncoderListener) {
        super(mediaMuxerWrapper, mediaEncoderListener);
        Log.i(TAG, "MediaVideoEncoder: ");
        this.mWidth = i;
        this.mHeight = i2;
    }

    public Surface getInputSurface() {
        return this.mSurface;
    }

    @Override // com.serenegiant.usb.encoder.MediaEncoder
    protected void prepare() throws IOException {
        Log.i(TAG, "prepare: ");
        this.mTrackIndex = -1;
        this.mIsEOS = false;
        this.mMuxerStarted = false;
        MediaCodecInfo selectVideoCodec = selectVideoCodec("video/avc");
        if (selectVideoCodec == null) {
            Log.e(TAG, "Unable to find an appropriate codec for video/avc");
            return;
        }
        Log.i(TAG, "selected codec: " + selectVideoCodec.getName());
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", this.mWidth, this.mHeight);
        createVideoFormat.setInteger("color-format", MediaCodecHelper.OMX_COLOR_FormatAndroidOpaque);
        createVideoFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, calcBitRate());
        createVideoFormat.setInteger("frame-rate", 15);
        createVideoFormat.setInteger("i-frame-interval", 10);
        Log.i(TAG, "format: " + createVideoFormat);
        this.mMediaCodec = MediaCodec.createEncoderByType("video/avc");
        this.mMediaCodec.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mSurface = this.mMediaCodec.createInputSurface();
        this.mMediaCodec.start();
        Log.i(TAG, "prepare finishing");
        if (this.mListener != null) {
            try {
                this.mListener.onPrepared(this);
            } catch (Exception e) {
                Log.e(TAG, "prepare:", e);
            }
        }
    }

    @Override // com.serenegiant.usb.encoder.MediaEncoder
    protected void release() {
        Log.i(TAG, "release:");
        Surface surface = this.mSurface;
        if (surface != null) {
            surface.release();
            this.mSurface = null;
        }
        super.release();
    }

    private int calcBitRate() {
        int i = (int) (this.mWidth * 7.5f * this.mHeight);
        Log.i(TAG, String.format("bitrate=%5.2f[Mbps]", Float.valueOf((i / 1024.0f) / 1024.0f)));
        return i;
    }

    protected static final MediaCodecInfo selectVideoCodec(String str) {
        Log.v(TAG, "selectVideoCodec:");
        int codecCount = MediaCodecList.getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                String[] supportedTypes = codecInfoAt.getSupportedTypes();
                for (int i2 = 0; i2 < supportedTypes.length; i2++) {
                    if (supportedTypes[i2].equalsIgnoreCase(str)) {
                        Log.i(TAG, "codec:" + codecInfoAt.getName() + ",MIME=" + supportedTypes[i2]);
                        if (selectColorFormat(codecInfoAt, str) > 0) {
                            return codecInfoAt;
                        }
                    }
                }
            }
        }
        return null;
    }

    protected static final int selectColorFormat(MediaCodecInfo mediaCodecInfo, String str) {
        Log.i(TAG, "selectColorFormat: ");
        try {
            Thread.currentThread().setPriority(10);
            MediaCodecInfo.CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(str);
            Thread.currentThread().setPriority(5);
            int i = 0;
            int i2 = 0;
            while (true) {
                if (i2 >= capabilitiesForType.colorFormats.length) {
                    break;
                }
                int i3 = capabilitiesForType.colorFormats[i2];
                if (isRecognizedVideoFormat(i3)) {
                    i = i3;
                    break;
                }
                i2++;
            }
            if (i == 0) {
                Log.e(TAG, "couldn't find a good color format for " + mediaCodecInfo.getName() + " / " + str);
            }
            return i;
        } catch (Throwable th) {
            Thread.currentThread().setPriority(5);
            throw th;
        }
    }

    private static final boolean isRecognizedVideoFormat(int i) {
        Log.i(TAG, "isRecognizedVideoFormat:colorFormat=" + i);
        int[] iArr = recognizedFormats;
        int length = iArr != null ? iArr.length : 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (recognizedFormats[i2] == i) {
                return true;
            }
        }
        return false;
    }
}
