package com.serenegiant.media;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes2.dex */
public class AudioEncoder extends AbstractEncoder implements IAudioEncoder {
    private static final String TAG = AudioEncoder.class.getSimpleName();
    protected final int mAudioSource;
    private AudioThread mAudioThread;
    protected final int mChannelCount;
    protected final int mSampleRate;

    @Override // com.serenegiant.media.Encoder
    public boolean isAudio() {
        return true;
    }

    public AudioEncoder(IRecorder iRecorder, EncoderListener encoderListener, int i, int i2) {
        super(MediaCodecHelper.MIME_AUDIO_AAC, iRecorder, encoderListener);
        this.mAudioThread = null;
        this.mAudioSource = i;
        this.mSampleRate = 44100;
        this.mChannelCount = i2;
        if (i < 0 || i > 7) {
            throw new IllegalArgumentException("invalid audio source:" + i);
        }
    }

    @Override // com.serenegiant.media.AbstractEncoder
    protected boolean internalPrepare() throws Exception {
        this.mTrackIndex = -1;
        this.mIsEOS = false;
        this.mRecorderStarted = false;
        if (MediaCodecHelper.selectAudioEncoder(this.MIME_TYPE) == null) {
            return true;
        }
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat(this.MIME_TYPE, this.mSampleRate, this.mChannelCount);
        createAudioFormat.setInteger("aac-profile", 2);
        createAudioFormat.setInteger("channel-mask", this.mChannelCount == 1 ? 16 : 12);
        createAudioFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, AbstractAudioEncoder.DEFAULT_BIT_RATE);
        createAudioFormat.setInteger("channel-count", 1);
        this.mMediaCodec = MediaCodec.createEncoderByType(this.MIME_TYPE);
        this.mMediaCodec.configure(createAudioFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mMediaCodec.start();
        return false;
    }

    @Override // com.serenegiant.media.AbstractEncoder, com.serenegiant.media.Encoder
    public void start() {
        super.start();
        if (this.mAudioThread == null) {
            AudioThread audioThread = new AudioThread();
            this.mAudioThread = audioThread;
            audioThread.start();
        }
    }

    @Override // com.serenegiant.media.AbstractEncoder, com.serenegiant.media.Encoder
    public void release() {
        this.mAudioThread = null;
        super.release();
    }

    private final class AudioThread extends Thread {
        public AudioThread() {
            super("AudioThread");
        }

        /* JADX WARN: Code restructure failed: missing block: B:59:0x00e3, code lost:
        
            r11.this$0.frameAvailableSoon();
         */
        /* JADX WARN: Removed duplicated region for block: B:28:0x012d A[ORIG_RETURN, RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:5:0x0101 A[LOOP:0: B:5:0x0101->B:17:?, LOOP_START, PHI: r3
  0x0101: PHI (r3v5 int) = (r3v4 int), (r3v6 int) binds: [B:4:0x00ff, B:17:?] A[DONT_GENERATE, DONT_INLINE]] */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void run() {
            /*
                Method dump skipped, instructions count: 302
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.media.AudioEncoder.AudioThread.run():void");
        }
    }

    @Override // com.serenegiant.media.AbstractEncoder
    protected MediaFormat createOutputFormat(byte[] bArr, int i, int i2, int i3, int i4) {
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat(this.MIME_TYPE, this.mSampleRate, this.mChannelCount);
        ByteBuffer order = ByteBuffer.allocateDirect(i).order(ByteOrder.nativeOrder());
        order.put(bArr, 0, i);
        order.flip();
        createAudioFormat.setByteBuffer("csd-0", order);
        return createAudioFormat;
    }
}
