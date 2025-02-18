package com.serenegiant.media;

import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Process;
import android.util.Log;
import android.view.Surface;
import java.io.IOException;
import java.nio.ByteBuffer;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes2.dex */
public class MediaAudioEncoder extends MediaEncoder implements IAudioEncoder {
    private static final int[] AUDIO_SOURCES = {5, 1, 0, 7, 6};
    private static final int BIT_RATE = 64000;
    private static final boolean DEBUG = false;
    public static final int FRAMES_PER_BUFFER = 25;
    private static final String MIME_TYPE = "audio/mp4a-latm";
    public static final int SAMPLES_PER_FRAME = 1024;
    private static final int SAMPLE_RATE = 44100;
    private static final String TAG = "MediaAudioEncoder";
    private AudioThread mAudioThread;

    public MediaAudioEncoder(MediaMovieRecorder mediaMovieRecorder, IMediaCodecCallback iMediaCodecCallback) {
        super(true, mediaMovieRecorder, iMediaCodecCallback);
        this.mAudioThread = null;
    }

    @Override // com.serenegiant.media.IMediaCodec, com.serenegiant.media.Encoder
    public void prepare() throws IOException {
        this.mTrackIndex = -1;
        this.mIsEOS = false;
        this.mMuxerStarted = false;
        if (MediaCodecHelper.selectAudioEncoder("audio/mp4a-latm") == null) {
            Log.e(TAG, "Unable to find an appropriate codec for audio/mp4a-latm");
            return;
        }
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", 44100, 1);
        createAudioFormat.setInteger("aac-profile", 2);
        createAudioFormat.setInteger("channel-mask", 16);
        createAudioFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, 64000);
        createAudioFormat.setInteger("channel-count", 1);
        this.mMediaCodec = MediaCodec.createEncoderByType("audio/mp4a-latm");
        this.mMediaCodec.configure(createAudioFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mMediaCodec.start();
        this.mIsPrepared = true;
        callOnPrepared();
    }

    @Override // com.serenegiant.media.MediaEncoder, com.serenegiant.media.IMediaCodec, com.serenegiant.media.Encoder
    public void start() {
        super.start();
        if (this.mAudioThread == null) {
            AudioThread audioThread = new AudioThread();
            this.mAudioThread = audioThread;
            audioThread.start();
        }
    }

    @Override // com.serenegiant.media.MediaEncoder, com.serenegiant.media.IMediaCodec, com.serenegiant.media.Encoder
    public void release() {
        this.mAudioThread = null;
        super.release();
    }

    private class AudioThread extends Thread {
        private AudioThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Process.setThreadPriority(-19);
            try {
                int minBufferSize = AudioRecord.getMinBufferSize(44100, 16, 2);
                int i = 25600 < minBufferSize ? ((minBufferSize / 1024) + 1) * 1024 * 2 : 25600;
                AudioRecord audioRecord = null;
                for (int i2 : MediaAudioEncoder.AUDIO_SOURCES) {
                    try {
                        AudioRecord audioRecord2 = new AudioRecord(i2, 44100, 16, 2, i);
                        if (audioRecord2.getState() != 1) {
                            audioRecord2.release();
                            audioRecord2 = null;
                        }
                        audioRecord = audioRecord2;
                    } catch (Exception unused) {
                        audioRecord = null;
                    }
                    if (audioRecord != null) {
                        break;
                    }
                }
                if (audioRecord != null) {
                    try {
                        if (MediaAudioEncoder.this.mIsCapturing && audioRecord.getState() == 1) {
                            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(1024);
                            audioRecord.startRecording();
                            while (MediaAudioEncoder.this.mIsCapturing && !MediaAudioEncoder.this.mRequestStop && !MediaAudioEncoder.this.mIsEOS) {
                                try {
                                    allocateDirect.clear();
                                    int read = audioRecord.read(allocateDirect, 1024);
                                    if (read > 0) {
                                        allocateDirect.position(read);
                                        allocateDirect.flip();
                                        MediaAudioEncoder.this.encode(allocateDirect, read, MediaAudioEncoder.this.getPTSUs());
                                        MediaAudioEncoder.this.frameAvailableSoon();
                                    }
                                } catch (Throwable th) {
                                    audioRecord.stop();
                                    throw th;
                                }
                            }
                            MediaAudioEncoder.this.frameAvailableSoon();
                            audioRecord.stop();
                        }
                        audioRecord.release();
                    } catch (Throwable th2) {
                        audioRecord.release();
                        throw th2;
                    }
                }
            } catch (Exception e) {
                Log.e(MediaAudioEncoder.TAG, "AudioThread#run", e);
            }
        }
    }
}
