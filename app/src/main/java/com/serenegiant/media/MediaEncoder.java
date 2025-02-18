package com.serenegiant.media;

import android.media.MediaCodec;
import android.util.Log;
import com.serenegiant.utils.Time;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public abstract class MediaEncoder implements IMediaCodec {
    private static final boolean DEBUG = false;
    private MediaCodec.BufferInfo mBufferInfo;
    private final IMediaCodecCallback mCallback;
    protected final boolean mIsAudio;
    protected volatile boolean mIsCapturing;
    protected boolean mIsEOS;
    protected boolean mIsPrepared;
    protected MediaCodec mMediaCodec;
    protected boolean mMuxerStarted;
    private int mRequestDrain;
    protected volatile boolean mRequestStop;
    protected int mTrackIndex;
    protected final WeakReference<AbstractRecorder> mWeakMuxer;
    private final String TAG = getClass().getSimpleName();
    protected final Object mSync = new Object();
    private final Runnable mEncodeTask = new Runnable() { // from class: com.serenegiant.media.MediaEncoder.1
        @Override // java.lang.Runnable
        public void run() {
            synchronized (MediaEncoder.this.mSync) {
                MediaEncoder.this.mRequestStop = false;
                MediaEncoder.this.mRequestDrain = 0;
                MediaEncoder.this.mSync.notify();
                try {
                    MediaEncoder.this.mSync.wait();
                    MediaEncoder.this.callOnStart();
                } catch (InterruptedException unused) {
                }
            }
            while (true) {
                synchronized (MediaEncoder.this.mSync) {
                    boolean z = MediaEncoder.this.mRequestStop;
                    boolean z2 = MediaEncoder.this.mRequestDrain > 0;
                    if (z2) {
                        MediaEncoder.access$010(MediaEncoder.this);
                    }
                    if (!z2 && !z) {
                        try {
                            MediaEncoder.this.mSync.wait();
                        } catch (InterruptedException unused2) {
                        }
                    } else if (z) {
                        break;
                    } else if (z2) {
                        MediaEncoder.this.drain();
                    }
                }
            }
            MediaEncoder.this.callOnStop();
            MediaEncoder.this.drain();
            MediaEncoder.this.signalEndOfInputStream();
            MediaEncoder.this.drain();
            MediaEncoder.this.release();
            synchronized (MediaEncoder.this.mSync) {
                MediaEncoder.this.mRequestStop = true;
                MediaEncoder.this.mIsCapturing = false;
            }
        }
    };
    private long prevOutputPTSUs = 0;

    static /* synthetic */ int access$010(MediaEncoder mediaEncoder) {
        int i = mediaEncoder.mRequestDrain;
        mediaEncoder.mRequestDrain = i - 1;
        return i;
    }

    public MediaEncoder(boolean z, AbstractRecorder abstractRecorder, IMediaCodecCallback iMediaCodecCallback) {
        if (iMediaCodecCallback == null) {
            throw new NullPointerException("MediaEncoderListener is null");
        }
        if (abstractRecorder == null) {
            throw new NullPointerException("MediaMuxerWrapper is null");
        }
        this.mIsAudio = z;
        this.mWeakMuxer = new WeakReference<>(abstractRecorder);
        abstractRecorder.addEncoder(this);
        this.mCallback = iMediaCodecCallback;
        synchronized (this.mSync) {
            this.mBufferInfo = new MediaCodec.BufferInfo();
            new Thread(this.mEncodeTask, getClass().getSimpleName()).start();
            try {
                this.mSync.wait();
            } catch (InterruptedException unused) {
            }
        }
    }

    @Override // com.serenegiant.media.IMediaCodec, com.serenegiant.media.Encoder
    public void start() {
        synchronized (this.mSync) {
            this.mIsCapturing = true;
            this.mRequestStop = false;
            this.mSync.notifyAll();
        }
    }

    @Override // com.serenegiant.media.IMediaCodec
    public void stop() {
        synchronized (this.mSync) {
            if (this.mIsCapturing && !this.mRequestStop) {
                this.mRequestStop = true;
                this.mSync.notifyAll();
            }
        }
    }

    public String getOutputPath() {
        AbstractRecorder abstractRecorder = this.mWeakMuxer.get();
        if (abstractRecorder != null) {
            return abstractRecorder.getOutputPath();
        }
        return null;
    }

    public void frameAvailableSoon() {
        synchronized (this.mSync) {
            if (this.mIsCapturing && !this.mRequestStop) {
                this.mRequestDrain++;
                this.mSync.notifyAll();
            }
        }
    }

    @Override // com.serenegiant.media.IMediaCodec
    public boolean isPrepared() {
        return this.mIsPrepared;
    }

    @Override // com.serenegiant.media.IMediaCodec
    public boolean isRunning() {
        return this.mIsCapturing;
    }

    public boolean isCapturing() {
        return this.mIsCapturing;
    }

    public boolean isAudio() {
        return this.mIsAudio;
    }

    protected boolean callErrorHandler(Exception exc) {
        IMediaCodecCallback iMediaCodecCallback = this.mCallback;
        return iMediaCodecCallback != null && iMediaCodecCallback.onError(this, exc);
    }

    protected void callOnPrepared() {
        IMediaCodecCallback iMediaCodecCallback = this.mCallback;
        if (iMediaCodecCallback != null) {
            try {
                iMediaCodecCallback.onPrepared(this);
            } catch (Exception e) {
                Log.w(this.TAG, "callOnPrepared", e);
            }
        }
    }

    protected void callOnStart() {
        IMediaCodecCallback iMediaCodecCallback = this.mCallback;
        if (iMediaCodecCallback != null) {
            try {
                iMediaCodecCallback.onStart(this);
            } catch (Exception e) {
                Log.w(this.TAG, "callOnStart", e);
            }
        }
    }

    protected void callOnStop() {
        IMediaCodecCallback iMediaCodecCallback = this.mCallback;
        if (iMediaCodecCallback != null) {
            try {
                iMediaCodecCallback.onStop(this);
            } catch (Exception e) {
                Log.w(this.TAG, "callOnStop", e);
            }
        }
    }

    protected void callOnRelease() {
        IMediaCodecCallback iMediaCodecCallback = this.mCallback;
        if (iMediaCodecCallback != null) {
            try {
                iMediaCodecCallback.onRelease(this);
            } catch (Exception e) {
                Log.w(this.TAG, "callOnRelease", e);
            }
        }
    }

    @Override // com.serenegiant.media.IMediaCodec, com.serenegiant.media.Encoder
    public void release() {
        AbstractRecorder abstractRecorder;
        this.mIsCapturing = false;
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec != null) {
            try {
                mediaCodec.stop();
                this.mMediaCodec.release();
                this.mMediaCodec = null;
            } catch (Exception e) {
                if (!callErrorHandler(e)) {
                    Log.e(this.TAG, "failed releasing MediaCodec", e);
                }
            }
        }
        if (this.mMuxerStarted && (abstractRecorder = this.mWeakMuxer.get()) != null) {
            try {
                abstractRecorder.stop();
            } catch (Exception e2) {
                if (!callErrorHandler(e2)) {
                    Log.e(this.TAG, "failed stopping muxer", e2);
                }
            }
        }
        this.mBufferInfo = null;
        callOnRelease();
    }

    public void signalEndOfInputStream() {
        encode(null, 0, getPTSUs());
    }

    public void encode(ByteBuffer byteBuffer) {
        encode(byteBuffer, byteBuffer.capacity(), getPTSUs());
    }

    public void encode(ByteBuffer byteBuffer, int i, long j) {
        MediaCodec mediaCodec;
        if (!this.mIsCapturing || this.mRequestStop || (mediaCodec = this.mMediaCodec) == null) {
            return;
        }
        ByteBuffer[] inputBuffers = mediaCodec.getInputBuffers();
        while (this.mIsCapturing) {
            int dequeueInputBuffer = this.mMediaCodec.dequeueInputBuffer(10000L);
            if (dequeueInputBuffer >= 0) {
                ByteBuffer byteBuffer2 = inputBuffers[dequeueInputBuffer];
                byteBuffer2.clear();
                if (byteBuffer != null) {
                    byteBuffer2.put(byteBuffer);
                }
                if (i > 0) {
                    this.mMediaCodec.queueInputBuffer(dequeueInputBuffer, 0, i, j, 0);
                    return;
                } else {
                    this.mIsEOS = true;
                    this.mMediaCodec.queueInputBuffer(dequeueInputBuffer, 0, 0, j, 4);
                    return;
                }
            }
        }
    }

    protected void drain() {
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec == null) {
            return;
        }
        ByteBuffer[] outputBuffers = mediaCodec.getOutputBuffers();
        AbstractRecorder abstractRecorder = this.mWeakMuxer.get();
        if (abstractRecorder == null) {
            Log.w(this.TAG, "muxer is unexpectedly null");
            return;
        }
        int i = 0;
        while (this.mIsCapturing) {
            try {
                int dequeueOutputBuffer = this.mMediaCodec.dequeueOutputBuffer(this.mBufferInfo, 10000L);
                if (dequeueOutputBuffer == -1) {
                    if (!this.mIsEOS && (i = i + 1) > 5) {
                        return;
                    }
                } else if (dequeueOutputBuffer == -3) {
                    outputBuffers = this.mMediaCodec.getOutputBuffers();
                } else if (dequeueOutputBuffer == -2) {
                    if (this.mMuxerStarted) {
                        RuntimeException runtimeException = new RuntimeException("format changed twice");
                        if (!callErrorHandler(runtimeException)) {
                            throw runtimeException;
                        }
                    }
                    this.mTrackIndex = abstractRecorder.addTrack(this.mMediaCodec.getOutputFormat());
                    this.mMuxerStarted = true;
                    if (abstractRecorder.start()) {
                        continue;
                    } else {
                        synchronized (abstractRecorder) {
                            while (!abstractRecorder.isStarted() && this.mIsCapturing) {
                                try {
                                    abstractRecorder.wait(100L);
                                } catch (InterruptedException unused) {
                                    return;
                                }
                            }
                        }
                    }
                } else if (dequeueOutputBuffer >= 0) {
                    ByteBuffer byteBuffer = outputBuffers[dequeueOutputBuffer];
                    if (byteBuffer == null) {
                        RuntimeException runtimeException2 = new RuntimeException("encoderOutputBuffer " + dequeueOutputBuffer + " was null");
                        if (!callErrorHandler(runtimeException2)) {
                            throw runtimeException2;
                        }
                    }
                    if ((this.mBufferInfo.flags & 2) != 0) {
                        this.mBufferInfo.size = 0;
                    }
                    if (this.mBufferInfo.size != 0) {
                        if (!this.mMuxerStarted) {
                            throw new RuntimeException("drain:muxer hasn't started");
                        }
                        this.mBufferInfo.presentationTimeUs = getPTSUs();
                        abstractRecorder.writeSampleData(this.mTrackIndex, byteBuffer, this.mBufferInfo);
                        this.prevOutputPTSUs = this.mBufferInfo.presentationTimeUs;
                        i = 0;
                    }
                    this.mMediaCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
                    if ((this.mBufferInfo.flags & 4) != 0) {
                        this.mIsCapturing = false;
                        return;
                    }
                }
            } catch (Exception e) {
                callErrorHandler(e);
                return;
            }
        }
    }

    protected long getPTSUs() {
        long nanoTime = Time.nanoTime() / 1000;
        long j = this.prevOutputPTSUs;
        return nanoTime < j ? nanoTime + (j - nanoTime) : nanoTime;
    }
}
