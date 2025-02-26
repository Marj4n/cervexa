package com.serenegiant.media;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import com.serenegiant.utils.BuildCheck;
import com.serenegiant.utils.Time;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import tv.danmaku.ijk.media.player.misc.IMediaFormat;

/* loaded from: classes2.dex */
public abstract class MediaDecoder implements IMediaCodec {
    private static final boolean DEBUG = false;
    protected static final int STATE_INITIALIZED = 1;
    protected static final int STATE_PAUSED = 4;
    protected static final int STATE_PLAYING = 3;
    protected static final int STATE_PREPARED = 2;
    protected static final int STATE_UNINITIALIZED = 0;
    protected static final int STATE_WAIT = 5;
    private static final String TAG_STATIC = MediaDecoder.class.getSimpleName();
    private static final int TIMEOUT_USEC = 10000;
    private int mBitRate;
    private MediaCodec.BufferInfo mBufferInfo;
    private IMediaCodecCallback mCallback;
    private long mDuration;
    private ByteBuffer[] mInputBuffers;
    private volatile boolean mInputDone;
    private volatile boolean mIsRunning;
    private MediaCodec mMediaCodec;
    private MediaExtractor mMediaExtractor;
    private MediaMetadataRetriever mMediaMetadataRetriever;
    private ByteBuffer[] mOutputBuffers;
    private volatile boolean mOutputDone;
    private long mStartTime;
    private int mTrackIndex;
    private long presentationTimeUs;
    protected final String TAG = getClass().getSimpleName();
    private final Object mSync = new Object();
    private long mRequestTime = -1;
    protected int mState = 0;
    private final Runnable mPlaybackTask = new Runnable() { // from class: com.serenegiant.media.MediaDecoder.1
        @Override // java.lang.Runnable
        public final void run() {
            MediaDecoder mediaDecoder = MediaDecoder.this;
            mediaDecoder.mInputDone = mediaDecoder.mOutputDone = false;
            MediaDecoder.this.mIsRunning = true;
            MediaDecoder.this.callOnStart();
            while (true) {
                if (MediaDecoder.this.mInputDone && MediaDecoder.this.mOutputDone) {
                    break;
                }
                try {
                    if (MediaDecoder.this.mRequestTime >= 0) {
                        MediaDecoder.this.handleSeek(MediaDecoder.this.mRequestTime);
                    }
                    if (!MediaDecoder.this.mInputDone) {
                        MediaDecoder.this.internal_HandleInput();
                    }
                    if (!MediaDecoder.this.mOutputDone) {
                        MediaDecoder.this.internal_handleOutput();
                    }
                    if (!MediaDecoder.this.mIsRunning || (MediaDecoder.this.mInputDone && MediaDecoder.this.mOutputDone)) {
                        MediaDecoder.this.mState = 5;
                        MediaDecoder.this.callOnStop();
                        if (!MediaDecoder.this.mIsRunning) {
                            break;
                        }
                        MediaDecoder.this.mMediaCodec.flush();
                        synchronized (MediaDecoder.this.mSync) {
                            if (MediaDecoder.this.mState == 5) {
                                try {
                                    MediaDecoder.this.mSync.wait();
                                } catch (InterruptedException unused) {
                                }
                            }
                        }
                        if (!MediaDecoder.this.mIsRunning) {
                            break;
                        }
                        MediaDecoder.this.callOnStart();
                        MediaDecoder.this.mStartTime = MediaDecoder.this.presentationTimeUs = -1L;
                        MediaDecoder.this.mInputDone = MediaDecoder.this.mOutputDone = false;
                        MediaDecoder.this.mState = 3;
                    }
                } catch (Exception e) {
                    Log.e(MediaDecoder.this.TAG, "PlaybackTask:", e);
                    MediaDecoder.this.callErrorHandler(e);
                }
            }
            MediaDecoder.this.internal_stop();
            synchronized (MediaDecoder.this.mSync) {
                MediaDecoder.this.mSync.notifyAll();
            }
        }
    };

    protected abstract Surface getOutputSurface();

    protected abstract boolean handleOutput(ByteBuffer byteBuffer, int i, int i2, long j);

    protected abstract int handlePrepare(MediaExtractor mediaExtractor);

    public void setCallback(IMediaCodecCallback iMediaCodecCallback) {
        this.mCallback = iMediaCodecCallback;
    }

    public IMediaCodecCallback getCallback() {
        return this.mCallback;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public int getBitRate() {
        return this.mBitRate;
    }

    public void setDataSource(String str) throws IOException {
        release();
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            this.mMediaMetadataRetriever = mediaMetadataRetriever;
            mediaMetadataRetriever.setDataSource(str);
            updateMovieInfo(this.mMediaMetadataRetriever);
            MediaExtractor mediaExtractor = new MediaExtractor();
            this.mMediaExtractor = mediaExtractor;
            mediaExtractor.setDataSource(str);
            this.mState = 1;
        } catch (IOException e) {
            internal_release();
            if (!callErrorHandler(e)) {
                throw e;
            }
        }
    }

    public void setDataSource(String str, Map<String, String> map) throws IOException {
        release();
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            this.mMediaMetadataRetriever = mediaMetadataRetriever;
            mediaMetadataRetriever.setDataSource(str, map);
            updateMovieInfo(this.mMediaMetadataRetriever);
            MediaExtractor mediaExtractor = new MediaExtractor();
            this.mMediaExtractor = mediaExtractor;
            mediaExtractor.setDataSource(str, map);
            this.mState = 1;
        } catch (IOException e) {
            internal_release();
            if (!callErrorHandler(e)) {
                throw e;
            }
        }
    }

    public void setDataSource(FileDescriptor fileDescriptor) throws IOException {
        release();
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            this.mMediaMetadataRetriever = mediaMetadataRetriever;
            mediaMetadataRetriever.setDataSource(fileDescriptor);
            updateMovieInfo(this.mMediaMetadataRetriever);
            MediaExtractor mediaExtractor = new MediaExtractor();
            this.mMediaExtractor = mediaExtractor;
            mediaExtractor.setDataSource(fileDescriptor);
            this.mState = 1;
        } catch (IOException e) {
            internal_release();
            if (!callErrorHandler(e)) {
                throw e;
            }
        }
    }

    public void setDataSource(FileDescriptor fileDescriptor, long j, long j2) throws IOException {
        release();
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            this.mMediaMetadataRetriever = mediaMetadataRetriever;
            mediaMetadataRetriever.setDataSource(fileDescriptor, j, j2);
            updateMovieInfo(this.mMediaMetadataRetriever);
            MediaExtractor mediaExtractor = new MediaExtractor();
            this.mMediaExtractor = mediaExtractor;
            mediaExtractor.setDataSource(fileDescriptor, j, j2);
            this.mState = 1;
        } catch (IOException e) {
            internal_release();
            if (!callErrorHandler(e)) {
                throw e;
            }
        }
    }

    public void setDataSource(Context context, Uri uri, Map<String, String> map) throws IOException {
        release();
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            this.mMediaMetadataRetriever = mediaMetadataRetriever;
            mediaMetadataRetriever.setDataSource(context, uri);
            updateMovieInfo(this.mMediaMetadataRetriever);
            MediaExtractor mediaExtractor = new MediaExtractor();
            this.mMediaExtractor = mediaExtractor;
            mediaExtractor.setDataSource(context, uri, map);
            this.mState = 1;
        } catch (IOException e) {
            internal_release();
            if (!callErrorHandler(e)) {
                throw e;
            }
        }
    }

    public void setDataSource(Context context, Uri uri) throws IOException {
        release();
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        this.mMediaMetadataRetriever = mediaMetadataRetriever;
        try {
            mediaMetadataRetriever.setDataSource(context, uri);
            updateMovieInfo(this.mMediaMetadataRetriever);
            MediaExtractor mediaExtractor = new MediaExtractor();
            this.mMediaExtractor = mediaExtractor;
            mediaExtractor.setDataSource(context, uri, (Map<String, String>) null);
            this.mState = 1;
        } catch (IOException e) {
            release();
            if (!callErrorHandler(e)) {
                throw e;
            }
        }
    }

    @Override // com.serenegiant.media.IMediaCodec, com.serenegiant.media.Encoder
    public void prepare() throws IOException {
        int handlePrepare = 0;
        MediaExtractor mediaExtractor = this.mMediaExtractor;
        if (mediaExtractor == null) {
            IllegalStateException illegalStateException = new IllegalStateException("DataSource not set yet");
            if (!callErrorHandler(illegalStateException)) {
                throw illegalStateException;
            }
            return;
        }
        if (this.mState != 1) {
            IllegalStateException illegalStateException2 = new IllegalStateException("already prepared");
            if (!callErrorHandler(illegalStateException2)) {
                throw illegalStateException2;
            }
            return;
        }
        try {
            handlePrepare = handlePrepare(mediaExtractor);
            this.mTrackIndex = handlePrepare;
        } catch (Exception e) {
            MediaExtractor mediaExtractor2 = this.mMediaExtractor;
            if (mediaExtractor2 != null) {
                mediaExtractor2.release();
                this.mMediaExtractor = null;
            }
            if (!callErrorHandler(e)) {
                throw e;
            }
        }
        if (handlePrepare >= 0) {
            this.mMediaExtractor.selectTrack(handlePrepare);
            MediaFormat trackFormat = this.mMediaExtractor.getTrackFormat(this.mTrackIndex);
            this.mDuration = trackFormat.getLong("durationUs");
            this.mMediaCodec = createCodec(this.mMediaExtractor, this.mTrackIndex, trackFormat);
            if (this.mTrackIndex < 0 || this.mMediaCodec == null) {
                return;
            }
            this.mState = 2;
            callOnPrepared();
            return;
        }
        throw new IOException("track not found");
    }

    @Override // com.serenegiant.media.IMediaCodec
    public boolean isPrepared() {
        return this.mState >= 2;
    }

    @Override // com.serenegiant.media.IMediaCodec
    public boolean isRunning() {
        return this.mState == 3;
    }

    protected MediaCodec createCodec(MediaExtractor mediaExtractor, int i, MediaFormat mediaFormat) throws IOException {
        if (i < 0) {
            return null;
        }
        MediaCodec createDecoderByType = MediaCodec.createDecoderByType(mediaFormat.getString(IMediaFormat.KEY_MIME));
        createDecoderByType.configure(mediaFormat, getOutputSurface(), (MediaCrypto) null, 0);
        createDecoderByType.start();
        return createDecoderByType;
    }

    public void restart() {
        if (this.mState == 5) {
            synchronized (this.mSync) {
                this.mMediaExtractor.unselectTrack(this.mTrackIndex);
                this.mMediaExtractor.selectTrack(this.mTrackIndex);
                this.mState = 3;
                this.mSync.notifyAll();
            }
        }
    }

    @Override // com.serenegiant.media.IMediaCodec, com.serenegiant.media.Encoder
    public void start() {
        boolean z;
        int i = this.mState;
        if (i == 2) {
            z = true;
        } else {
            if (i == 3) {
                return;
            }
            if (i != 4) {
                throw new IllegalStateException("invalid state:" + this.mState);
            }
            z = false;
        }
        this.mState = 3;
        if (z) {
            this.presentationTimeUs = -1L;
            this.mBufferInfo = new MediaCodec.BufferInfo();
            this.mInputBuffers = this.mMediaCodec.getInputBuffers();
            this.mOutputBuffers = this.mMediaCodec.getOutputBuffers();
            new Thread(this.mPlaybackTask, this.TAG).start();
        }
    }

    @Override // com.serenegiant.media.IMediaCodec
    public void stop() {
        synchronized (this.mSync) {
            this.mIsRunning = false;
            if (this.mState >= 3) {
                this.mSync.notifyAll();
                try {
                    this.mSync.wait(50L);
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void internal_stop() {
        int i = this.mState;
        if (i == 3 || i == 4 || i == 5) {
            MediaCodec mediaCodec = this.mMediaCodec;
            if (mediaCodec != null) {
                mediaCodec.stop();
            }
            this.mState = 2;
        }
    }

    public void pause() {
        int i = this.mState;
        if (i == 3 || i == 4 || i == 5) {
            this.mState = 4;
            return;
        }
        IllegalStateException illegalStateException = new IllegalStateException();
        if (!callErrorHandler(illegalStateException)) {
            throw illegalStateException;
        }
    }

    @Override // com.serenegiant.media.IMediaCodec, com.serenegiant.media.Encoder
    public void release() throws IOException {
        if (this.mState != 0) {
            stop();
            this.mState = 0;
            callOnRelease();
        }
        internal_release();
    }

    private void internal_release() throws IOException {
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec != null) {
            mediaCodec.release();
            this.mMediaCodec = null;
        }
        MediaExtractor mediaExtractor = this.mMediaExtractor;
        if (mediaExtractor != null) {
            mediaExtractor.release();
            this.mMediaExtractor = null;
        }
        MediaMetadataRetriever mediaMetadataRetriever = this.mMediaMetadataRetriever;
        if (mediaMetadataRetriever != null) {
            mediaMetadataRetriever.release();
            this.mMediaMetadataRetriever = null;
        }
        this.mTrackIndex = -1;
        this.mDuration = 0L;
        this.mBitRate = 0;
    }

    public void seek(long j) {
        this.mRequestTime = j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void handleSeek(long j) {
        if (j < 0) {
            return;
        }
        MediaExtractor mediaExtractor = this.mMediaExtractor;
        if (mediaExtractor != null) {
            mediaExtractor.seekTo(j, 0);
            this.mMediaExtractor.advance();
        }
        this.mRequestTime = -1L;
    }

    protected void internal_HandleInput() {
        boolean z;
        int dequeueInputBuffer = 0;
        if (!this.mInputDone) {
            if (this.presentationTimeUs < 0) {
                this.presentationTimeUs = this.mMediaExtractor.getSampleTime();
            }
            long j = this.presentationTimeUs;
            if (j >= 0) {
                this.presentationTimeUs = handleInput(j);
                z = true;
                if (!z || (dequeueInputBuffer = this.mMediaCodec.dequeueInputBuffer(10000L)) < 0) {
                }
                this.mMediaCodec.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
                this.mInputDone = true;
                return;
            }
        }
        z = false;
        if (z) {
        }
    }

    protected long handleInput(long j) {
        int dequeueInputBuffer = this.mMediaCodec.dequeueInputBuffer(10000L);
        if (dequeueInputBuffer < 0) {
            return j;
        }
        int readSampleData = this.mMediaExtractor.readSampleData(this.mInputBuffers[dequeueInputBuffer], 0);
        if (readSampleData > 0) {
            this.mMediaCodec.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, j, 0);
        }
        this.mMediaExtractor.advance();
        return -1L;
    }

    protected void internal_handleOutput() {
        IMediaCodecCallback iMediaCodecCallback;
        int dequeueOutputBuffer = this.mMediaCodec.dequeueOutputBuffer(this.mBufferInfo, 10000L);
        if (dequeueOutputBuffer == -1) {
            return;
        }
        if (dequeueOutputBuffer == -3) {
            this.mOutputBuffers = this.mMediaCodec.getOutputBuffers();
            return;
        }
        if (dequeueOutputBuffer == -2) {
            this.mMediaCodec.getOutputFormat();
            return;
        }
        if (dequeueOutputBuffer < 0) {
            RuntimeException runtimeException = new RuntimeException("unexpected result from dequeueOutputBuffer: " + dequeueOutputBuffer);
            if (!callErrorHandler(runtimeException)) {
                throw runtimeException;
            }
            return;
        }
        boolean z = false;
        if (this.mBufferInfo.size > 0 && (!handleOutput(this.mOutputBuffers[dequeueOutputBuffer], 0, this.mBufferInfo.size, this.mBufferInfo.presentationTimeUs)) && ((iMediaCodecCallback = this.mCallback) == null || !iMediaCodecCallback.onFrameAvailable(this, this.mBufferInfo.presentationTimeUs))) {
            this.mStartTime = adjustPresentationTime(this.mStartTime, this.mBufferInfo.presentationTimeUs);
        }
        this.mMediaCodec.releaseOutputBuffer(dequeueOutputBuffer, z);
        if ((this.mBufferInfo.flags & 4) != 0) {
            this.mOutputDone = true;
        }
    }

    protected boolean callErrorHandler(Exception exc) {
        IMediaCodecCallback iMediaCodecCallback = this.mCallback;
        if (iMediaCodecCallback != null) {
            return iMediaCodecCallback.onError(this, exc);
        }
        return false;
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

    protected void updateMovieInfo(MediaMetadataRetriever mediaMetadataRetriever) {
        this.mBitRate = 0;
        String extractMetadata = mediaMetadataRetriever.extractMetadata(20);
        if (TextUtils.isEmpty(extractMetadata)) {
            return;
        }
        this.mBitRate = Integer.parseInt(extractMetadata);
    }

    protected long adjustPresentationTime(long j, long j2) {
        if (BuildCheck.isJellyBeanMr1()) {
            return adjustPresentationTimeAPI17(j, j2);
        }
        if (j > 0) {
            long nanoTime = Time.nanoTime();
            while (true) {
                long j3 = j2 - ((nanoTime / 1000) - j);
                if (j3 <= 0) {
                    break;
                }
                synchronized (this.mSync) {
                    try {
                        this.mSync.wait(j3 / 1000, (int) ((j3 % 1000) * 1000));
                    } catch (InterruptedException unused) {
                    }
                    if (!this.mIsRunning) {
                        break;
                    }
                }
                nanoTime = Time.nanoTime();
            }
            return j;
        }
        return Time.nanoTime() / 1000;
    }

    protected long adjustPresentationTimeAPI17(long j, long j2) {
        if (j > 0) {
            long nanoTime = Time.nanoTime();
            while (true) {
                long j3 = j2 - ((nanoTime / 1000) - j);
                if (j3 <= 0) {
                    break;
                }
                synchronized (this.mSync) {
                    try {
                        this.mSync.wait(j3 / 1000, (int) ((j3 % 1000) * 1000));
                    } catch (InterruptedException unused) {
                    }
                    if (!this.mIsRunning) {
                        break;
                    }
                }
                nanoTime = Time.nanoTime();
            }
            return j;
        }
        return Time.nanoTime() / 1000;
    }

    protected static final int selectTrack(MediaExtractor mediaExtractor, String str) {
        int trackCount = mediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            if (mediaExtractor.getTrackFormat(i).getString(IMediaFormat.KEY_MIME).startsWith(str)) {
                return i;
            }
        }
        return -1;
    }
}
