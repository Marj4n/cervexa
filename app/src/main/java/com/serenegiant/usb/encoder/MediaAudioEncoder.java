package com.serenegiant.usb.encoder;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import com.serenegiant.usb.encoder.MediaEncoder;
import java.io.IOException;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes2.dex */
public class MediaAudioEncoder extends MediaEncoder implements IAudioEncoder {
    private static final int[] AUDIO_SOURCES = {0, 1, 5};
    private static final int BIT_RATE = 64000;
    private static final boolean DEBUG = true;
    public static final int FRAMES_PER_BUFFER = 25;
    private static final String MIME_TYPE = "audio/mp4a-latm";
    public static final int SAMPLES_PER_FRAME = 1024;
    public static final int SAMPLE_RATE = 44100;
    private static final String TAG = "MediaAudioEncoder";
    private AudioThread mAudioThread;

    public MediaAudioEncoder(MediaMuxerWrapper mediaMuxerWrapper, MediaEncoderListener mediaEncoderListener) {
        super(mediaMuxerWrapper, mediaEncoderListener);
        this.mAudioThread = null;
    }

    @Override // com.serenegiant.usb.encoder.MediaEncoder
    protected void prepare() throws IOException {
        Log.v(TAG, "prepare:");
        this.mTrackIndex = -1;
        this.mIsEOS = false;
        this.mMuxerStarted = false;
        MediaCodecInfo selectAudioCodec = selectAudioCodec("audio/mp4a-latm");
        if (selectAudioCodec == null) {
            Log.e(TAG, "Unable to find an appropriate codec for audio/mp4a-latm");
            return;
        }
        Log.i(TAG, "selected codec: " + selectAudioCodec.getName());
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", 44100, 1);
        createAudioFormat.setInteger("aac-profile", 2);
        createAudioFormat.setInteger("channel-mask", 16);
        createAudioFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, 64000);
        createAudioFormat.setInteger("channel-count", 1);
        Log.i(TAG, "format: " + createAudioFormat);
        this.mMediaCodec = MediaCodec.createEncoderByType("audio/mp4a-latm");
        this.mMediaCodec.configure(createAudioFormat, (Surface) null, (MediaCrypto) null, 1);
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
    protected void startRecording() {
        super.startRecording();
        if (this.mAudioThread == null) {
            AudioThread audioThread = new AudioThread();
            this.mAudioThread = audioThread;
            audioThread.start();
        }
    }

    @Override // com.serenegiant.usb.encoder.MediaEncoder
    protected void release() {
        this.mAudioThread = null;
        super.release();
    }

    private class AudioThread extends Thread {
        private AudioThread() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:70:0x00a4, code lost:
        
            r17.this$0.frameAvailableSoon();
         */
        /* JADX WARN: Removed duplicated region for block: B:19:0x00cc A[LOOP:1: B:19:0x00cc->B:33:?, LOOP_START, PHI: r14
  0x00cc: PHI (r14v1 int) = (r14v0 int), (r14v2 int) binds: [B:18:0x00ca, B:33:?] A[DONT_GENERATE, DONT_INLINE]] */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() {
            /*
                Method dump skipped, instructions count: 258
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.usb.encoder.MediaAudioEncoder.AudioThread.run():void");
        }
    }

    private static final MediaCodecInfo selectAudioCodec(String str) {
        Log.v(TAG, "selectAudioCodec:");
        int codecCount = MediaCodecList.getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                String[] supportedTypes = codecInfoAt.getSupportedTypes();
                for (int i2 = 0; i2 < supportedTypes.length; i2++) {
                    Log.i(TAG, "supportedType:" + codecInfoAt.getName() + ",MIME=" + supportedTypes[i2]);
                    if (supportedTypes[i2].equalsIgnoreCase(str)) {
                        return codecInfoAt;
                    }
                }
            }
        }
        return null;
    }
}
