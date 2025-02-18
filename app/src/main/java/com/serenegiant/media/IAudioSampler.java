package com.serenegiant.media;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Process;
import android.util.Log;
import com.serenegiant.utils.BuildCheck;
import com.serenegiant.utils.Time;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public abstract class IAudioSampler {
    public static final int AUDIO_SOURCE_UAC = 100;
    private CallbackThread mCallbackThread;
    protected volatile boolean mIsCapturing;
    private final String TAG = getClass().getSimpleName();
    private final int MAX_POOL_SIZE = 200;
    private final int MAX_QUEUE_SIZE = 200;
    private final LinkedBlockingQueue<MediaData> mPool = new LinkedBlockingQueue<>(200);
    private final LinkedBlockingQueue<MediaData> mAudioQueue = new LinkedBlockingQueue<>(200);
    private final Object mCallbackSync = new Object();
    private final Set<SoundSamplerCallback> mCallbacks = new CopyOnWriteArraySet();
    protected int mDefaultBufferSize = 1024;
    private int mBufferNum = 0;
    private long prevInputPTSUs = -1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AudioSource {
    }

    public interface SoundSamplerCallback {
        void onData(ByteBuffer byteBuffer, int i, long j);

        void onError(Exception exc);
    }

    public abstract int getAudioSource();

    public abstract int getBitResolution();

    public abstract int getChannels();

    public abstract int getSamplingFrequency();

    public static AudioRecord createAudioRecord(int i, int i2, int i3, int i4, int i5) {
        int[] iArr = {0, 5, 1, 0, 7, 6};
        if (i == 1) {
            iArr[0] = 1;
        } else if (i == 2) {
            iArr[0] = 5;
        } else if (i != 3) {
            iArr[0] = 1;
        } else {
            iArr[0] = 7;
        }
        AudioRecord audioRecord = null;
        for (int i6 = 0; i6 < 6; i6++) {
            int i7 = iArr[i6];
            try {
                int i8 = 16;
                if (BuildCheck.isAndroid6()) {
                    AudioRecord.Builder audioSource = new AudioRecord.Builder().setAudioSource(i7);
                    AudioFormat.Builder sampleRate = new AudioFormat.Builder().setEncoding(i4).setSampleRate(i2);
                    if (i3 != 1) {
                        i8 = 12;
                    }
                    audioRecord = audioSource.setAudioFormat(sampleRate.setChannelMask(i8).build()).setBufferSizeInBytes(i5).build();
                } else {
                    audioRecord = new AudioRecord(i7, i2, i3 == 1 ? 16 : 12, i4, i5);
                }
            } catch (Exception unused) {
            }
            if (audioRecord.getState() != 1) {
                audioRecord.release();
                audioRecord = null;
            }
            if (audioRecord != null) {
                break;
            }
        }
        return audioRecord;
    }

    public void release() {
        if (isStarted()) {
            stop();
        }
        this.mCallbacks.clear();
    }

    public synchronized void start() {
        synchronized (this.mCallbackSync) {
            if (this.mCallbackThread == null) {
                this.mIsCapturing = true;
                CallbackThread callbackThread = new CallbackThread();
                this.mCallbackThread = callbackThread;
                callbackThread.start();
            }
        }
    }

    public synchronized void stop() {
        synchronized (this.mCallbackSync) {
            boolean z = this.mIsCapturing;
            this.mIsCapturing = false;
            this.mCallbackThread = null;
            if (z) {
                try {
                    this.mCallbackSync.wait();
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    public void addCallback(SoundSamplerCallback soundSamplerCallback) {
        if (soundSamplerCallback != null) {
            this.mCallbacks.add(soundSamplerCallback);
        }
    }

    public void removeCallback(SoundSamplerCallback soundSamplerCallback) {
        if (soundSamplerCallback != null) {
            while (this.mCallbacks.remove(soundSamplerCallback)) {
            }
        }
    }

    public boolean isStarted() {
        return this.mIsCapturing;
    }

    public int getBufferSize() {
        return this.mDefaultBufferSize;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callOnData(MediaData mediaData) {
        ByteBuffer byteBuffer = mediaData.mBuffer;
        int i = mediaData.size;
        long j = mediaData.presentationTimeUs;
        for (SoundSamplerCallback soundSamplerCallback : this.mCallbacks) {
            try {
                byteBuffer.clear();
                byteBuffer.position(i);
                byteBuffer.flip();
                soundSamplerCallback.onData(byteBuffer, i, j);
            } catch (Exception e) {
                this.mCallbacks.remove(soundSamplerCallback);
                Log.w(this.TAG, "callOnData:", e);
            }
        }
    }

    protected void callOnError(Exception exc) {
        for (SoundSamplerCallback soundSamplerCallback : this.mCallbacks) {
            try {
                soundSamplerCallback.onError(exc);
            } catch (Exception e) {
                this.mCallbacks.remove(soundSamplerCallback);
                Log.w(this.TAG, "callOnError:", e);
            }
        }
    }

    protected void init_pool(int i) {
        this.mDefaultBufferSize = i;
        this.mAudioQueue.clear();
        this.mPool.clear();
        for (int i2 = 0; i2 < 8; i2++) {
            this.mPool.add(new MediaData(i));
        }
    }

    protected MediaData obtain() {
        MediaData mediaData;
        if (!this.mPool.isEmpty()) {
            mediaData = this.mPool.poll();
        } else if (this.mBufferNum < 200) {
            mediaData = new MediaData(this.mDefaultBufferSize);
            this.mBufferNum++;
        } else {
            mediaData = null;
        }
        if (mediaData != null) {
            mediaData.size = 0;
        }
        return mediaData;
    }

    protected void recycle(MediaData mediaData) {
        if (this.mPool.offer(mediaData)) {
            return;
        }
        this.mBufferNum--;
    }

    protected boolean addMediaData(MediaData mediaData) {
        return this.mAudioQueue.offer(mediaData);
    }

    protected MediaData pollMediaData(long j) throws InterruptedException {
        return this.mAudioQueue.poll(j, TimeUnit.MILLISECONDS);
    }

    protected long getInputPTSUs() {
        long nanoTime = Time.nanoTime() / 1000;
        long j = this.prevInputPTSUs;
        if (nanoTime <= j) {
            nanoTime = 9643 + j;
        }
        this.prevInputPTSUs = nanoTime;
        return nanoTime;
    }

    private final class CallbackThread extends Thread {
        public CallbackThread() {
            super("AudioSampler");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public final void run() {
            Process.setThreadPriority(-16);
            while (IAudioSampler.this.mIsCapturing) {
                try {
                    MediaData pollMediaData = IAudioSampler.this.pollMediaData(100L);
                    if (pollMediaData != null) {
                        IAudioSampler.this.callOnData(pollMediaData);
                        IAudioSampler.this.recycle(pollMediaData);
                    }
                } catch (InterruptedException unused) {
                }
            }
            synchronized (IAudioSampler.this.mCallbackSync) {
                IAudioSampler.this.mCallbackSync.notifyAll();
            }
        }
    }
}
