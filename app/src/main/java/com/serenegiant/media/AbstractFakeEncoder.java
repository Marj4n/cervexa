package com.serenegiant.media;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import com.serenegiant.utils.BufferHelper;
import com.serenegiant.utils.BuildCheck;
import com.serenegiant.utils.Time;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public abstract class AbstractFakeEncoder implements Encoder {
    public static final int BUFFER_FLAG_KEY_FRAME;
    private static final int DEFAULT_FRAME_SZ = 1024;
    private static final int DEFAULT_MAX_POOL_SZ = 8;
    private static final int DEFAULT_MAX_QUEUE_SZ = 6;
    private static final long MAX_WAIT_FRAME_MS = 100;
    private static final String TAG = AbstractFakeEncoder.class.getSimpleName();
    private final int FRAME_SZ;
    private final int MAX_POOL_SZ;
    private final String MIME_TYPE;
    private int cnt;
    private final MediaCodec.BufferInfo mBufferInfo;
    private final Runnable mDrainTask;
    private Thread mDrainThread;
    private final LinkedBlockingQueue<MediaData> mFrameQueue;
    private volatile boolean mIsCapturing;
    private boolean mIsEOS;
    private final EncoderListener mListener;
    private final List<MediaData> mPool;
    private IRecorder mRecorder;
    private volatile boolean mRecorderStarted;
    private volatile boolean mRequestStop;
    private final Object mSync;
    private int mTrackIndex;
    private volatile boolean mWaitingKeyFrame;
    private long prevInputPTSUs;
    private long prevOutputPTSUs;

    protected abstract MediaFormat createOutputFormat(String str, byte[] bArr, int i, int i2, int i3, int i4);

    static {
        BuildCheck.isLollipop();
        BUFFER_FLAG_KEY_FRAME = 1;
    }

    public AbstractFakeEncoder(String str, IRecorder iRecorder, EncoderListener encoderListener) {
        this(str, iRecorder, encoderListener, 1024, 8, 6);
    }

    public AbstractFakeEncoder(String str, IRecorder iRecorder, EncoderListener encoderListener, int i) {
        this(str, iRecorder, encoderListener, i, 8, 6);
    }

    public AbstractFakeEncoder(String str, IRecorder iRecorder, EncoderListener encoderListener, int i, int i2, int i3) {
        this.mSync = new Object();
        this.mPool = new ArrayList();
        this.mBufferInfo = new MediaCodec.BufferInfo();
        this.cnt = 0;
        this.mDrainTask = new Runnable() { // from class: com.serenegiant.media.AbstractFakeEncoder.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (AbstractFakeEncoder.this.mSync) {
                    AbstractFakeEncoder.this.mRequestStop = false;
                    AbstractFakeEncoder.this.mSync.notify();
                }
                while (AbstractFakeEncoder.this.mIsCapturing) {
                    MediaData waitFrame = AbstractFakeEncoder.this.waitFrame(100L);
                    if (waitFrame != null) {
                        try {
                            if (AbstractFakeEncoder.this.mIsCapturing) {
                                AbstractFakeEncoder.this.handleFrame(waitFrame);
                            }
                        } finally {
                            AbstractFakeEncoder.this.recycle(waitFrame);
                        }
                    }
                }
                synchronized (AbstractFakeEncoder.this.mSync) {
                    AbstractFakeEncoder.this.mRequestStop = true;
                    AbstractFakeEncoder.this.mIsCapturing = false;
                }
                AbstractFakeEncoder.this.mDrainThread = null;
            }
        };
        this.prevInputPTSUs = -1L;
        this.prevOutputPTSUs = -1L;
        this.MIME_TYPE = str;
        this.FRAME_SZ = i;
        this.MAX_POOL_SZ = i2;
        this.mRecorder = iRecorder;
        this.mListener = encoderListener;
        this.mFrameQueue = new LinkedBlockingQueue<>(i3);
        iRecorder.addEncoder(this);
    }

    protected void finalize() throws Throwable {
        release();
        super.finalize();
    }

    public IRecorder getRecorder() {
        return this.mRecorder;
    }

    public boolean isRecorderStarted() {
        return this.mRecorderStarted;
    }

    @Override // com.serenegiant.media.Encoder
    public String getOutputPath() {
        IRecorder iRecorder = this.mRecorder;
        if (iRecorder != null) {
            return iRecorder.getOutputPath();
        }
        return null;
    }

    @Override // com.serenegiant.media.Encoder
    public synchronized void release() {
        try {
            if (this.mDrainThread != null) {
                this.mDrainThread.interrupt();
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        this.mDrainThread = null;
        if (this.mRecorder != null) {
            internalRelease();
        }
    }

    @Override // com.serenegiant.media.Encoder
    public void signalEndOfInputStream() {
        MediaData obtain = obtain(0);
        obtain.set(null, 0, 0, getInputPTSUs(), 4);
        offer(obtain);
    }

    @Override // com.serenegiant.media.Encoder
    public void encode(ByteBuffer byteBuffer) {
        throw new UnsupportedOperationException("can not call encode");
    }

    @Override // com.serenegiant.media.Encoder
    public void encode(ByteBuffer byteBuffer, int i, long j) {
        throw new UnsupportedOperationException("can not call encode");
    }

    public boolean queueFrame(ByteBuffer byteBuffer, int i, int i2, long j, int i3) throws IllegalStateException {
        if (!this.mIsCapturing) {
            throw new IllegalStateException();
        }
        if (this.mRequestStop) {
            return false;
        }
        MediaData obtain = obtain(i2);
        obtain.set(byteBuffer, i, i2, j, i3);
        return offer(obtain);
    }

    @Override // com.serenegiant.media.Encoder
    public boolean isCapturing() {
        return this.mIsCapturing;
    }

    @Override // com.serenegiant.media.Encoder
    public void prepare() throws Exception {
        this.mTrackIndex = -1;
        this.mRecorderStarted = false;
        this.mWaitingKeyFrame = true;
        this.mIsCapturing = true;
        this.mIsEOS = false;
        this.mRequestStop = false;
        callOnStartEncode(null, -1, false);
    }

    @Override // com.serenegiant.media.Encoder
    public void start() {
        synchronized (this.mSync) {
            if (this.mIsCapturing && !this.mRequestStop) {
                initPool();
                Thread thread = new Thread(this.mDrainTask, getClass().getSimpleName());
                this.mDrainThread = thread;
                thread.start();
                try {
                    this.mSync.wait();
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    @Override // com.serenegiant.media.Encoder
    public void stop() {
        synchronized (this.mSync) {
            if (this.mRequestStop) {
                return;
            }
            this.mRequestStop = true;
            signalEndOfInputStream();
            this.mSync.notifyAll();
        }
    }

    @Override // com.serenegiant.media.Encoder
    public void frameAvailableSoon() {
        synchronized (this.mSync) {
            if (this.mIsCapturing && !this.mRequestStop) {
                this.mSync.notifyAll();
            }
        }
    }

    protected void callOnStartEncode(Surface surface, int i, boolean z) {
        try {
            this.mListener.onStartEncode(this, surface, i, z);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    protected void callOnError(Exception exc) {
        try {
            this.mListener.onError(exc);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    protected void initPool() {
        this.mFrameQueue.clear();
        synchronized (this.mPool) {
            this.mPool.clear();
            for (int i = 0; i < this.MAX_POOL_SZ; i++) {
                this.mPool.add(new MediaData(this.FRAME_SZ));
            }
        }
    }

    protected void clearFrames() {
        synchronized (this.mPool) {
            this.mPool.clear();
        }
        this.mFrameQueue.clear();
        this.cnt = 0;
    }

    protected MediaData obtain(int i) {
        MediaData mediaData;
        synchronized (this.mPool) {
            if (this.mPool.isEmpty()) {
                this.cnt++;
                mediaData = new MediaData(this.FRAME_SZ);
            } else {
                MediaData remove = this.mPool.remove(this.mPool.size() - 1);
                remove.resize(i);
                mediaData = remove;
            }
        }
        return mediaData;
    }

    protected boolean offer(MediaData mediaData) {
        boolean offer = this.mFrameQueue.offer(mediaData);
        if (offer) {
            return offer;
        }
        MediaData poll = this.mFrameQueue.poll();
        boolean offer2 = this.mFrameQueue.offer(mediaData);
        if (poll != null) {
            recycle(poll);
        }
        return offer2;
    }

    protected MediaData waitFrame(long j) {
        try {
            return this.mFrameQueue.poll(j, TimeUnit.MILLISECONDS);
        } catch (InterruptedException unused) {
            return null;
        }
    }

    protected void recycle(MediaData mediaData) {
        synchronized (this.mPool) {
            if (this.mPool.size() < this.MAX_POOL_SZ) {
                this.mPool.add(mediaData);
            } else {
                this.cnt--;
            }
        }
    }

    protected void handleFrame(MediaData mediaData) {
        IRecorder iRecorder = this.mRecorder;
        if (iRecorder == null) {
            Log.w(TAG, "muxer is unexpectedly null");
            return;
        }
        this.mBufferInfo.set(0, mediaData.size, mediaData.presentationTimeUs, mediaData.flags);
        int i = this.mBufferInfo.flags;
        int i2 = BUFFER_FLAG_KEY_FRAME;
        boolean z = (i & i2) == i2;
        if (!this.mRecorderStarted && (z || (this.mBufferInfo.flags & 2) != 0)) {
            byte[] bArr = new byte[this.mBufferInfo.size];
            ByteBuffer duplicate = mediaData.mBuffer.duplicate();
            duplicate.clear();
            duplicate.get(bArr, 0, this.mBufferInfo.size);
            int findAnnexB = BufferHelper.findAnnexB(bArr, 0);
            int findAnnexB2 = BufferHelper.findAnnexB(bArr, findAnnexB + 2);
            try {
                if (!startRecorder(iRecorder, createOutputFormat(this.MIME_TYPE, bArr, this.mBufferInfo.size, findAnnexB, findAnnexB2, BufferHelper.findAnnexB(bArr, findAnnexB2 + 2)))) {
                    Log.w(TAG, "handleFrame:failed to start recorder");
                    return;
                }
            } catch (Exception unused) {
                return;
            }
        }
        if ((this.mBufferInfo.flags & 2) != 0) {
            this.mBufferInfo.size = 0;
        }
        if (this.mRecorderStarted && this.mBufferInfo.size != 0 && (z || !this.mWaitingKeyFrame)) {
            this.mWaitingKeyFrame = false;
            try {
                this.mBufferInfo.presentationTimeUs = getNextOutputPTSUs(this.mBufferInfo.presentationTimeUs);
                iRecorder.writeSampleData(this.mTrackIndex, mediaData.mBuffer, this.mBufferInfo);
            } catch (TimeoutException unused2) {
                iRecorder.stopRecording();
            } catch (Exception unused3) {
                iRecorder.stopRecording();
            }
        }
        if ((this.mBufferInfo.flags & 4) != 0) {
            stopRecorder(iRecorder);
        }
    }

    protected boolean startRecorder(IRecorder iRecorder, MediaFormat mediaFormat) {
        int addTrack = iRecorder.addTrack(this, mediaFormat);
        this.mTrackIndex = addTrack;
        if (addTrack >= 0) {
            this.mRecorderStarted = true;
            if (!iRecorder.start(this)) {
                Log.e(TAG, "failed to start muxer mTrackIndex=" + this.mTrackIndex);
            }
        } else {
            Log.e(TAG, "failed to addTrack: mTrackIndex=" + this.mTrackIndex);
            iRecorder.removeEncoder(this);
        }
        return iRecorder.isStarted();
    }

    protected void stopRecorder(IRecorder iRecorder) {
        if (this.mRecorder != null) {
            internalRelease();
        }
    }

    private void internalRelease() {
        this.mIsEOS = true;
        if (this.mIsCapturing) {
            this.mIsCapturing = false;
            try {
                this.mListener.onStopEncode(this);
            } catch (Exception e) {
                Log.e(TAG, "failed onStopped", e);
            }
        }
        if (this.mRecorderStarted) {
            this.mRecorderStarted = false;
            IRecorder iRecorder = this.mRecorder;
            if (iRecorder != null) {
                try {
                    iRecorder.stop(this);
                } catch (Exception e2) {
                    Log.e(TAG, "failed stopping muxer", e2);
                }
            }
        }
        try {
            this.mListener.onDestroy(this);
        } catch (Exception e3) {
            Log.e(TAG, "destroy:", e3);
        }
        this.mRecorder = null;
        clearFrames();
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

    protected long getNextOutputPTSUs(long j) {
        long j2 = this.prevOutputPTSUs;
        if (j <= j2) {
            j = 9643 + j2;
        }
        this.prevOutputPTSUs = j;
        return j;
    }
}
