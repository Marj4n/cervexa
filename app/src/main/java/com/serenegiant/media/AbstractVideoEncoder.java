package com.serenegiant.media;

import android.media.MediaFormat;
import android.os.Bundle;
import com.serenegiant.utils.BuildCheck;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: classes2.dex */
public abstract class AbstractVideoEncoder extends AbstractEncoder implements IVideoEncoder {
    public static boolean supportsAdaptiveStreaming = BuildCheck.isKitKat();
    protected int mBitRate;
    protected int mFramerate;
    protected int mHeight;
    protected int mIFrameIntervals;
    protected int mWidth;

    @Override // com.serenegiant.media.Encoder
    public final boolean isAudio() {
        return false;
    }

    public AbstractVideoEncoder(String str, IRecorder iRecorder, EncoderListener encoderListener) {
        super(str, iRecorder, encoderListener);
        this.mBitRate = -1;
        this.mFramerate = -1;
        this.mIFrameIntervals = -1;
    }

    @Override // com.serenegiant.media.IVideoEncoder
    public void setVideoSize(int i, int i2) throws IllegalArgumentException, IllegalStateException {
        this.mWidth = i;
        this.mHeight = i2;
        this.mBitRate = VideoConfig.getBitrate(i, i2);
    }

    public void setVideoConfig(int i, int i2, int i3) {
        this.mBitRate = i;
        this.mFramerate = i2;
        this.mIFrameIntervals = i3;
    }

    @Override // com.serenegiant.media.IVideoEncoder
    public int getWidth() {
        return this.mWidth;
    }

    @Override // com.serenegiant.media.IVideoEncoder
    public int getHeight() {
        return this.mHeight;
    }

    public void adjustBitrate(int i) {
        if (!supportsAdaptiveStreaming || this.mMediaCodec == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("video-bitrate", i);
        this.mMediaCodec.setParameters(bundle);
    }

    @Override // com.serenegiant.media.AbstractEncoder
    protected MediaFormat createOutputFormat(byte[] bArr, int i, int i2, int i3, int i4) {
        if (i2 >= 0) {
            MediaFormat createVideoFormat = MediaFormat.createVideoFormat(this.MIME_TYPE, this.mWidth, this.mHeight);
            int i5 = i3 - i2;
            ByteBuffer order = ByteBuffer.allocateDirect(i5).order(ByteOrder.nativeOrder());
            order.put(bArr, i2, i5);
            order.flip();
            createVideoFormat.setByteBuffer("csd-0", order);
            if (i3 > i2) {
                int i6 = (i - i3) + i2;
                ByteBuffer order2 = ByteBuffer.allocateDirect(i6).order(ByteOrder.nativeOrder());
                order2.put(bArr, i3, i6);
                order2.flip();
                createVideoFormat.setByteBuffer("csd-1", order2);
            }
            return createVideoFormat;
        }
        throw new RuntimeException("unexpected csd data came.");
    }
}
