package com.serenegiant.media;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Process;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: classes2.dex */
public abstract class MediaReaper implements Runnable {
    private static final boolean DEBUG = false;
    public static final int REAPER_AUDIO = 1;
    public static final int REAPER_VIDEO = 0;
    private static final String TAG = MediaReaper.class.getSimpleName();
    public static final int TIMEOUT_USEC = 10000;
    private volatile boolean mIsEOS;
    private volatile boolean mIsRunning;
    private final ReaperListener mListener;
    private final int mReaperType;
    private volatile boolean mRecorderStarted;
    private int mRequestDrain;
    private boolean mRequestStop;
    private final WeakReference<MediaCodec> mWeakEncoder;
    private final Object mSync = new Object();
    private long prevOutputPTSUs = -1;
    private MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();

    public interface ReaperListener {
        void onError(MediaReaper mediaReaper, Exception exc);

        void onOutputFormatChanged(MediaReaper mediaReaper, MediaFormat mediaFormat);

        void onStop(MediaReaper mediaReaper);

        void writeSampleData(MediaReaper mediaReaper, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo);
    }

    protected abstract MediaFormat createOutputFormat(byte[] bArr, int i, int i2, int i3, int i4);

    public static class VideoReaper extends MediaReaper {
        public static final String MIME_AVC = "video/avc";
        private final int mHeight;
        private final int mWidth;

        public VideoReaper(MediaCodec mediaCodec, ReaperListener reaperListener, int i, int i2) {
            super(0, mediaCodec, reaperListener);
            this.mWidth = i;
            this.mHeight = i2;
        }

        @Override // com.serenegiant.media.MediaReaper
        protected MediaFormat createOutputFormat(byte[] bArr, int i, int i2, int i3, int i4) {
            if (i2 >= 0) {
                MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", this.mWidth, this.mHeight);
                int i5 = i3 - i2;
                ByteBuffer order = ByteBuffer.allocateDirect(i5).order(ByteOrder.nativeOrder());
                order.put(bArr, i2, i5);
                order.flip();
                createVideoFormat.setByteBuffer("csd-0", order);
                if (i3 > i2) {
                    int i6 = i4 > i3 ? i4 - i3 : i - i3;
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

    public static class AudioReaper extends MediaReaper {
        private static final String MIME_TYPE = "audio/mp4a-latm";
        private final int mChannelCount;
        private final int mSampleRate;

        public AudioReaper(MediaCodec mediaCodec, ReaperListener reaperListener, int i, int i2) {
            super(1, mediaCodec, reaperListener);
            this.mSampleRate = i;
            this.mChannelCount = i2;
        }

        @Override // com.serenegiant.media.MediaReaper
        protected MediaFormat createOutputFormat(byte[] bArr, int i, int i2, int i3, int i4) {
            MediaFormat createAudioFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", this.mSampleRate, this.mChannelCount);
            ByteBuffer order = ByteBuffer.allocateDirect(i).order(ByteOrder.nativeOrder());
            order.put(bArr, 0, i);
            order.flip();
            createAudioFormat.setByteBuffer("csd-0", order);
            return createAudioFormat;
        }
    }

    public MediaReaper(int i, MediaCodec mediaCodec, ReaperListener reaperListener) {
        this.mWeakEncoder = new WeakReference<>(mediaCodec);
        this.mListener = reaperListener;
        this.mReaperType = i;
        synchronized (this.mSync) {
            new Thread(this, getClass().getSimpleName()).start();
            try {
                this.mSync.wait();
            } catch (InterruptedException unused) {
            }
        }
    }

    public void release() {
        if (this.mIsRunning && !this.mRequestStop) {
            this.mRequestStop = true;
        }
        synchronized (this.mSync) {
            this.mSync.notifyAll();
        }
    }

    public void frameAvailableSoon() {
        synchronized (this.mSync) {
            if (this.mIsRunning && !this.mRequestStop) {
                this.mRequestDrain++;
                this.mSync.notifyAll();
            }
        }
    }

    public int reaperType() {
        return this.mReaperType;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean z;
        boolean z2;
        Process.setThreadPriority(-4);
        synchronized (this.mSync) {
            this.mIsRunning = true;
            this.mRequestStop = false;
            this.mRequestDrain = 0;
            this.mSync.notify();
        }
        while (true) {
            if (!this.mIsRunning) {
                break;
            }
            synchronized (this.mSync) {
                z = this.mRequestStop;
                z2 = this.mRequestDrain > 0;
                if (z2) {
                    this.mRequestDrain--;
                }
            }
            if (z) {
                drain();
                this.mIsEOS = true;
                release();
                break;
            } else if (z2) {
                try {
                    drain();
                } catch (IllegalStateException unused) {
                } catch (Exception e) {
                    Log.w(TAG, e);
                }
            } else {
                synchronized (this.mSync) {
                    try {
                        this.mSync.wait(50L);
                    } catch (InterruptedException unused2) {
                    } finally {
                    }
                }
            }
        }
        synchronized (this.mSync) {
            this.mRequestStop = true;
            this.mIsRunning = false;
        }
    }

    private final void drain() {
        MediaCodec mediaCodec = this.mWeakEncoder.get();
        if (mediaCodec == null) {
            return;
        }
        try {
            ByteBuffer[] outputBuffers = mediaCodec.getOutputBuffers();
            int i = 0;
            while (this.mIsRunning) {
                int dequeueOutputBuffer = mediaCodec.dequeueOutputBuffer(this.mBufferInfo, 10000L);
                if (dequeueOutputBuffer == -1) {
                    if (!this.mIsEOS && (i = i + 1) > 5) {
                        return;
                    }
                } else if (dequeueOutputBuffer == -3) {
                    outputBuffers = mediaCodec.getOutputBuffers();
                } else if (dequeueOutputBuffer == -2) {
                    if (this.mRecorderStarted) {
                        throw new RuntimeException("format changed twice");
                    }
                    if (!callOnFormatChanged(mediaCodec.getOutputFormat())) {
                        return;
                    }
                } else if (dequeueOutputBuffer < 0) {
                    continue;
                } else {
                    ByteBuffer byteBuffer = outputBuffers[dequeueOutputBuffer];
                    if (byteBuffer == null) {
                        throw new RuntimeException("encoderOutputBuffer " + dequeueOutputBuffer + " was null");
                    }
                    if ((this.mBufferInfo.flags & 2) != 0) {
                        if (!this.mRecorderStarted) {
                            byte[] bArr = new byte[this.mBufferInfo.size];
                            byteBuffer.position(0);
                            byteBuffer.get(bArr, this.mBufferInfo.offset, this.mBufferInfo.size);
                            byteBuffer.position(0);
                            int findStartMarker = MediaCodecHelper.findStartMarker(bArr, 0);
                            int findStartMarker2 = MediaCodecHelper.findStartMarker(bArr, findStartMarker + 2);
                            if (!callOnFormatChanged(createOutputFormat(bArr, this.mBufferInfo.size, findStartMarker, findStartMarker2, MediaCodecHelper.findStartMarker(bArr, findStartMarker2 + 2)))) {
                                return;
                            }
                        }
                        this.mBufferInfo.size = 0;
                    }
                    if (this.mBufferInfo.size != 0) {
                        if (!this.mRecorderStarted) {
                            throw new RuntimeException("drain:muxer hasn't started");
                        }
                        try {
                            this.mBufferInfo.presentationTimeUs = getNextOutputPTSUs(this.mBufferInfo.presentationTimeUs);
                            this.mListener.writeSampleData(this, byteBuffer, this.mBufferInfo);
                        } catch (TimeoutException e) {
                            callOnError(e);
                        } catch (Exception e2) {
                            callOnError(e2);
                        }
                        i = 0;
                    }
                    mediaCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
                    if ((this.mBufferInfo.flags & 4) != 0) {
                        callOnStop();
                        return;
                    }
                }
            }
        } catch (IllegalStateException unused) {
        }
    }

    private boolean callOnFormatChanged(MediaFormat mediaFormat) {
        try {
            this.mListener.onOutputFormatChanged(this, mediaFormat);
            this.mRecorderStarted = true;
            return true;
        } catch (Exception e) {
            callOnError(e);
            return false;
        }
    }

    private void callOnStop() {
        try {
            this.mListener.onStop(this);
        } catch (Exception e) {
            callOnError(e);
        }
    }

    private void callOnError(Exception exc) {
        try {
            this.mListener.onError(this, exc);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    protected long getNextOutputPTSUs(long j) {
        long j2 = this.prevOutputPTSUs;
        if (j <= j2) {
            j = 9643 + j2;
        }
        this.prevOutputPTSUs = j;
        return j;
    }
}
