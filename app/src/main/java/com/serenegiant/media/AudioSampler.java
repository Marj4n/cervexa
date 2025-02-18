package com.serenegiant.media;

import android.media.AudioRecord;
import android.os.Process;
import android.util.Log;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class AudioSampler extends IAudioSampler {
    private static final int AUDIO_FORMAT = 2;
    private static final String TAG = AudioSampler.class.getSimpleName();
    private final int AUDIO_SOURCE;
    private final int BUFFER_SIZE;
    private final int CHANNEL_COUNT;
    private final int SAMPLES_PER_FRAME;
    private final int SAMPLING_RATE;
    private AudioThread mAudioThread;

    @Override // com.serenegiant.media.IAudioSampler
    public int getBitResolution() {
        return 16;
    }

    public AudioSampler(int i, int i2, int i3, int i4, int i5) {
        this.AUDIO_SOURCE = i;
        this.CHANNEL_COUNT = i2;
        this.SAMPLING_RATE = i3;
        this.SAMPLES_PER_FRAME = i4 * i2;
        this.BUFFER_SIZE = getAudioBufferSize(i2, i3, i4, i5);
    }

    @Override // com.serenegiant.media.IAudioSampler
    public int getBufferSize() {
        return this.SAMPLES_PER_FRAME;
    }

    @Override // com.serenegiant.media.IAudioSampler
    public synchronized void start() {
        super.start();
        if (this.mAudioThread == null) {
            init_pool(this.SAMPLES_PER_FRAME);
            AudioThread audioThread = new AudioThread();
            this.mAudioThread = audioThread;
            audioThread.start();
        }
    }

    @Override // com.serenegiant.media.IAudioSampler
    public synchronized void stop() {
        this.mIsCapturing = false;
        this.mAudioThread = null;
        super.stop();
    }

    @Override // com.serenegiant.media.IAudioSampler
    public int getAudioSource() {
        return this.AUDIO_SOURCE;
    }

    protected static final class AudioRecordRec {
        AudioRecord audioRecord;
        int bufferSize;

        protected AudioRecordRec() {
        }
    }

    public static int getAudioBufferSize(int i, int i2, int i3, int i4) {
        int minBufferSize = AudioRecord.getMinBufferSize(i2, i == 1 ? 16 : 12, 2);
        int i5 = i4 * i3;
        return i5 < minBufferSize ? ((minBufferSize / i3) + 1) * i3 * 2 * i : i5;
    }

    private final class AudioThread extends Thread {
        public AudioThread() {
            super("AudioThread");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public final void run() {
            int i;
            Process.setThreadPriority(-16);
            int i2 = 3;
            while (AudioSampler.this.mIsCapturing && i2 > 0) {
                AudioRecord createAudioRecord = IAudioSampler.createAudioRecord(AudioSampler.this.AUDIO_SOURCE, AudioSampler.this.SAMPLING_RATE, AudioSampler.this.CHANNEL_COUNT, 2, AudioSampler.this.BUFFER_SIZE);
                if (createAudioRecord != null) {
                    try {
                        try {
                            if (AudioSampler.this.mIsCapturing) {
                                createAudioRecord.startRecording();
                                while (true) {
                                    i = 0;
                                    while (AudioSampler.this.mIsCapturing) {
                                        try {
                                            MediaData obtain = AudioSampler.this.obtain();
                                            if (obtain != null) {
                                                int recordingState = createAudioRecord.getRecordingState();
                                                if (recordingState != 3) {
                                                    if (i == 0) {
                                                        Log.e(AudioSampler.TAG, "not a recording state," + recordingState);
                                                    }
                                                    i++;
                                                    AudioSampler.this.recycle(obtain);
                                                    if (i > 20) {
                                                        break;
                                                    } else {
                                                        Thread.sleep(100L);
                                                    }
                                                } else {
                                                    ByteBuffer byteBuffer = obtain.mBuffer;
                                                    byteBuffer.clear();
                                                    try {
                                                        int read = createAudioRecord.read(byteBuffer, AudioSampler.this.SAMPLES_PER_FRAME);
                                                        if (read > 0) {
                                                            try {
                                                                obtain.presentationTimeUs = AudioSampler.this.getInputPTSUs();
                                                                obtain.size = read;
                                                                byteBuffer.position(read);
                                                                byteBuffer.flip();
                                                                AudioSampler.this.addMediaData(obtain);
                                                            } catch (Throwable th) {
                                                                th = th;
                                                                i = 0;
                                                                createAudioRecord.stop();
                                                                throw th;
                                                            }
                                                        } else if (read == 0) {
                                                            AudioSampler.this.recycle(obtain);
                                                        } else {
                                                            if (read == -1) {
                                                                if (i == 0) {
                                                                    Log.e(AudioSampler.TAG, "Read error ERROR");
                                                                }
                                                            } else if (read == -2) {
                                                                if (i == 0) {
                                                                    Log.e(AudioSampler.TAG, "Read error ERROR_BAD_VALUE");
                                                                }
                                                            } else if (read != -3) {
                                                                if (read == -6) {
                                                                    Log.e(AudioSampler.TAG, "Read error ERROR_DEAD_OBJECT");
                                                                    i++;
                                                                    i2--;
                                                                    AudioSampler.this.recycle(obtain);
                                                                    break;
                                                                }
                                                                if (read < 0 && i == 0) {
                                                                    Log.e(AudioSampler.TAG, "Read returned unknown err " + read);
                                                                }
                                                            } else if (i == 0) {
                                                                Log.e(AudioSampler.TAG, "Read error ERROR_INVALID_OPERATION");
                                                            }
                                                            i++;
                                                            AudioSampler.this.recycle(obtain);
                                                        }
                                                    } catch (Exception e) {
                                                        Log.e(AudioSampler.TAG, "AudioRecord#read failed:" + e);
                                                        i++;
                                                        i2 += -1;
                                                        AudioSampler.this.recycle(obtain);
                                                        AudioSampler.this.callOnError(e);
                                                    }
                                                }
                                            }
                                            if (i > 10) {
                                                break;
                                            }
                                        } catch (Throwable th2) {
                                            th = th2;
                                        }
                                    }
                                }
                                i2--;
                                try {
                                    createAudioRecord.stop();
                                } catch (Exception e2) {
                                    e = e2;
                                    i2--;
                                    AudioSampler.this.callOnError(e);
                                    if (AudioSampler.this.mIsCapturing) {
                                        while (AudioSampler.this.mIsCapturing) {
                                            try {
                                                Thread.sleep(100L);
                                            } catch (InterruptedException unused) {
                                            }
                                        }
                                    } else {
                                        continue;
                                    }
                                }
                            } else {
                                i = 0;
                            }
                        } finally {
                            createAudioRecord.release();
                        }
                    } catch (Exception e3) {
                        e = e3;
                        i = 0;
                    }
                    if (AudioSampler.this.mIsCapturing && i > 0 && i2 > 0) {
                        for (int i3 = 0; AudioSampler.this.mIsCapturing && i3 < 5; i3++) {
                            Thread.sleep(100L);
                        }
                    }
                } else {
                    AudioSampler.this.callOnError(new RuntimeException("AudioRecord failed to initialize"));
                    i2 = 0;
                }
            }
            AudioSampler.this.stop();
        }
    }

    @Override // com.serenegiant.media.IAudioSampler
    public int getChannels() {
        return this.CHANNEL_COUNT;
    }

    @Override // com.serenegiant.media.IAudioSampler
    public int getSamplingFrequency() {
        return this.SAMPLING_RATE;
    }
}
