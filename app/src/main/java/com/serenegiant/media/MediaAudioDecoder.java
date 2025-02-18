package com.serenegiant.media;

import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class MediaAudioDecoder extends MediaDecoder {
    private static final boolean DEBUG = false;
    private int mAudioChannels;
    private int mAudioInputBufSize;
    private byte[] mAudioOutTempBuf;
    private int mAudioSampleRate;
    private AudioTrack mAudioTrack;

    @Override // com.serenegiant.media.MediaDecoder
    protected Surface getOutputSurface() {
        return null;
    }

    @Override // com.serenegiant.media.MediaDecoder
    protected int handlePrepare(MediaExtractor mediaExtractor) {
        int selectTrack = selectTrack(mediaExtractor, "audio/");
        if (selectTrack >= 0) {
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(selectTrack);
            this.mAudioChannels = trackFormat.getInteger("channel-count");
            int integer = trackFormat.getInteger("sample-rate");
            this.mAudioSampleRate = integer;
            int minBufferSize = AudioTrack.getMinBufferSize(integer, this.mAudioChannels == 1 ? 4 : 12, 2);
            int integer2 = trackFormat.getInteger("max-input-size");
            int i = minBufferSize > 0 ? minBufferSize * this.mAudioChannels * 2 : integer2;
            this.mAudioInputBufSize = i;
            if (i > integer2) {
                this.mAudioInputBufSize = integer2;
            }
            int i2 = this.mAudioChannels * 2;
            this.mAudioInputBufSize = (this.mAudioInputBufSize / i2) * i2;
        }
        return selectTrack;
    }

    @Override // com.serenegiant.media.MediaDecoder
    protected MediaCodec createCodec(MediaExtractor mediaExtractor, int i, MediaFormat mediaFormat) throws IOException, IllegalArgumentException {
        MediaCodec createCodec = super.createCodec(mediaExtractor, i, mediaFormat);
        if (createCodec != null) {
            int capacity = createCodec.getOutputBuffers()[0].capacity();
            if (capacity <= 0) {
                capacity = this.mAudioInputBufSize;
            }
            this.mAudioOutTempBuf = new byte[capacity];
            try {
                AudioTrack audioTrack = new AudioTrack(3, this.mAudioSampleRate, this.mAudioChannels == 1 ? 4 : 12, 2, this.mAudioInputBufSize, 1);
                this.mAudioTrack = audioTrack;
                audioTrack.play();
            } catch (Exception e) {
                Log.e(this.TAG, "failed to start audio track playing", e);
                AudioTrack audioTrack2 = this.mAudioTrack;
                if (audioTrack2 != null) {
                    audioTrack2.release();
                    this.mAudioTrack = null;
                }
                throw e;
            }
        }
        return createCodec;
    }

    @Override // com.serenegiant.media.MediaDecoder
    protected boolean handleOutput(ByteBuffer byteBuffer, int i, int i2, long j) {
        if (this.mAudioOutTempBuf.length < i2) {
            this.mAudioOutTempBuf = new byte[i2];
        }
        byteBuffer.position(i);
        byteBuffer.get(this.mAudioOutTempBuf, 0, i2);
        byteBuffer.clear();
        AudioTrack audioTrack = this.mAudioTrack;
        if (audioTrack == null) {
            return true;
        }
        audioTrack.write(this.mAudioOutTempBuf, 0, i2);
        return true;
    }
}
