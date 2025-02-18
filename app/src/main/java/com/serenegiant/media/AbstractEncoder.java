package com.serenegiant.media;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import com.serenegiant.utils.BufferHelper;
import com.serenegiant.utils.Time;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public abstract class AbstractEncoder implements Encoder {
    private static final String TAG = AbstractEncoder.class.getSimpleName();
    public static final int TIMEOUT_USEC = 10000;
    protected final String MIME_TYPE;
    private MediaCodec.BufferInfo mBufferInfo;
    protected volatile boolean mIsCapturing;
    protected volatile boolean mIsEOS;
    private final EncoderListener mListener;
    protected MediaCodec mMediaCodec;
    private IRecorder mRecorder;
    protected volatile boolean mRecorderStarted;
    private volatile int mRequestDrain;
    protected volatile boolean mRequestStop;
    protected int mTrackIndex;
    protected final Object mSync = new Object();
    private final Runnable mDrainTask = new Runnable() { // from class: com.serenegiant.media.AbstractEncoder.1
        /* JADX WARN: Can't wrap try/catch for region: R(16:0|1|8|6|(4:10|28|16|7)|20|(6:21|48|(1:69)(2:(3:32|33|35)(5:43|44|95|49|50)|36)|58|a8|63)|70|71|72|74|75|76|58|a8|(2:(0)|(1:68))) */
        /* JADX WARN: Removed duplicated region for block: B:60:0x00a9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() {
            /*
                Method dump skipped, instructions count: 197
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.media.AbstractEncoder.AnonymousClass1.run():void");
        }
    };
    private long prevOutputPTSUs = -1;
    private long prevInputPTSUs = -1;

    protected abstract MediaFormat createOutputFormat(byte[] bArr, int i, int i2, int i3, int i4);

    @Override // com.serenegiant.media.Encoder
    public void encode(ByteBuffer byteBuffer) {
    }

    public int getCaptureFormat() {
        return -1;
    }

    protected abstract boolean internalPrepare() throws Exception;

    static /* synthetic */ int access$010(AbstractEncoder abstractEncoder) {
        int i = abstractEncoder.mRequestDrain;
        abstractEncoder.mRequestDrain = i - 1;
        return i;
    }

    public AbstractEncoder(String str, IRecorder iRecorder, EncoderListener encoderListener) {
        if (encoderListener == null) {
            throw new NullPointerException("EncodeListener is null");
        }
        if (iRecorder == null) {
            throw new NullPointerException("IMuxer is null");
        }
        this.MIME_TYPE = str;
        this.mRecorder = iRecorder;
        this.mListener = encoderListener;
        iRecorder.addEncoder(this);
        this.mBufferInfo = new MediaCodec.BufferInfo();
    }

    public IRecorder getRecorder() {
        return this.mRecorder;
    }

    @Override // com.serenegiant.media.Encoder
    public String getOutputPath() {
        IRecorder iRecorder = this.mRecorder;
        if (iRecorder != null) {
            return iRecorder.getOutputPath();
        }
        return null;
    }

    protected void finalize() throws Throwable {
        this.mRecorder = null;
        release();
        super.finalize();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.serenegiant.media.Encoder
    public final void prepare() throws Exception {
        try {
            this.mListener.onStartEncode(this, this instanceof ISurfaceEncoder ? ((ISurfaceEncoder) this).getInputSurface() : null, getCaptureFormat(), internalPrepare());
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        synchronized (this.mSync) {
            new Thread(this.mDrainTask, getClass().getSimpleName()).start();
            try {
                this.mSync.wait(1000L);
            } catch (InterruptedException unused) {
            }
        }
    }

    protected void callOnError(Exception exc) {
        try {
            this.mListener.onError(exc);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    @Override // com.serenegiant.media.Encoder
    public void start() {
        synchronized (this.mSync) {
            this.mIsCapturing = true;
            this.mRequestStop = false;
            this.mRequestDrain = 0;
        }
    }

    @Override // com.serenegiant.media.Encoder
    public void stop() {
        synchronized (this.mSync) {
            if (this.mRequestStop) {
                return;
            }
            this.mRequestStop = true;
            this.mSync.notifyAll();
        }
    }

    @Override // com.serenegiant.media.Encoder
    public void frameAvailableSoon() {
        synchronized (this.mSync) {
            if (this.mIsCapturing && !this.mRequestStop) {
                this.mRequestDrain++;
                this.mSync.notifyAll();
            }
        }
    }

    @Override // com.serenegiant.media.Encoder
    public void release() {
        if (this.mIsCapturing) {
            try {
                this.mListener.onStopEncode(this);
            } catch (Exception unused) {
            }
        }
        this.mIsCapturing = false;
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec != null) {
            try {
                mediaCodec.stop();
                this.mMediaCodec.release();
                this.mMediaCodec = null;
            } catch (Exception unused2) {
            }
        }
        if (this.mRecorderStarted) {
            this.mRecorderStarted = false;
            IRecorder iRecorder = this.mRecorder;
            if (iRecorder != null) {
                try {
                    iRecorder.stop(this);
                } catch (Exception unused3) {
                }
            }
        }
        try {
            this.mListener.onDestroy(this);
        } catch (Exception unused4) {
        }
        this.mBufferInfo = null;
        this.mRecorder = null;
    }

    @Override // com.serenegiant.media.Encoder
    public void signalEndOfInputStream() {
        encode(null, 0, getInputPTSUs());
    }

    @Override // com.serenegiant.media.Encoder
    public boolean isCapturing() {
        boolean z;
        synchronized (this.mSync) {
            z = this.mIsCapturing;
        }
        return z;
    }

    @Override // com.serenegiant.media.Encoder
    public void encode(ByteBuffer byteBuffer, int i, long j) {
        synchronized (this.mSync) {
            if (this.mIsCapturing && !this.mRequestStop) {
                if (this.mMediaCodec == null) {
                    return;
                }
                ByteBuffer[] inputBuffers = this.mMediaCodec.getInputBuffers();
                while (this.mIsCapturing) {
                    int dequeueInputBuffer = this.mMediaCodec.dequeueInputBuffer(10000L);
                    if (dequeueInputBuffer >= 0) {
                        ByteBuffer byteBuffer2 = inputBuffers[dequeueInputBuffer];
                        byteBuffer2.clear();
                        if (byteBuffer != null && i > 0) {
                            byteBuffer.clear();
                            byteBuffer.position(i);
                            byteBuffer.flip();
                            byteBuffer2.put(byteBuffer);
                        }
                        if (i <= 0) {
                            this.mIsEOS = true;
                            this.mMediaCodec.queueInputBuffer(dequeueInputBuffer, 0, 0, j, 4);
                            return;
                        } else {
                            this.mMediaCodec.queueInputBuffer(dequeueInputBuffer, 0, i, j, 0);
                            return;
                        }
                    }
                    if (dequeueInputBuffer == -1) {
                        frameAvailableSoon();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void drain() {
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec == null) {
            return;
        }
        try {
            ByteBuffer[] outputBuffers = mediaCodec.getOutputBuffers();
            IRecorder iRecorder = this.mRecorder;
            if (iRecorder == null) {
                return;
            }
            int i = 0;
            while (this.mIsCapturing) {
                int dequeueOutputBuffer = this.mMediaCodec.dequeueOutputBuffer(this.mBufferInfo, 10000L);
                if (dequeueOutputBuffer == -1) {
                    if (!this.mIsEOS && (i = i + 1) > 5) {
                        return;
                    }
                } else if (dequeueOutputBuffer == -3) {
                    outputBuffers = this.mMediaCodec.getOutputBuffers();
                } else if (dequeueOutputBuffer == -2) {
                    if (this.mRecorderStarted) {
                        throw new RuntimeException("format changed twice");
                    }
                    if (!startRecorder(iRecorder, this.mMediaCodec.getOutputFormat())) {
                        return;
                    }
                } else if (dequeueOutputBuffer >= 0) {
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
                            int byteComp = BufferHelper.byteComp(bArr, 0, BufferHelper.ANNEXB_START_MARK, BufferHelper.ANNEXB_START_MARK.length);
                            int byteComp2 = BufferHelper.byteComp(bArr, byteComp + 2, BufferHelper.ANNEXB_START_MARK, BufferHelper.ANNEXB_START_MARK.length);
                            if (!startRecorder(iRecorder, createOutputFormat(bArr, this.mBufferInfo.size, byteComp, byteComp2, BufferHelper.byteComp(bArr, byteComp2 + 2, BufferHelper.ANNEXB_START_MARK, BufferHelper.ANNEXB_START_MARK.length)))) {
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
                            iRecorder.writeSampleData(this.mTrackIndex, byteBuffer, this.mBufferInfo);
                        } catch (TimeoutException unused) {
                            iRecorder.stopRecording();
                        } catch (Exception unused2) {
                            iRecorder.stopRecording();
                        }
                        i = 0;
                    }
                    this.mMediaCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
                    if ((this.mBufferInfo.flags & 4) != 0) {
                        stopRecorder(iRecorder);
                        return;
                    }
                } else {
                    continue;
                }
            }
        } catch (IllegalStateException unused3) {
        }
    }

    protected boolean startRecorder(IRecorder iRecorder, MediaFormat mediaFormat) {
        if (iRecorder.getState() != 3) {
            for (int i = 0; i < 10 && iRecorder.getState() != 3; i++) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException unused) {
                }
            }
        }
        if (iRecorder.getState() == 3) {
            int addTrack = iRecorder.addTrack(this, mediaFormat);
            this.mTrackIndex = addTrack;
            if (addTrack >= 0) {
                this.mRecorderStarted = true;
                iRecorder.start(this);
            } else {
                iRecorder.removeEncoder(this);
            }
        }
        return iRecorder.isStarted();
    }

    protected void stopRecorder(IRecorder iRecorder) {
        this.mIsCapturing = false;
        this.mRecorderStarted = false;
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
