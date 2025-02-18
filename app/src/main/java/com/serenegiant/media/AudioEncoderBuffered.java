package com.serenegiant.media;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public class AudioEncoderBuffered extends AbstractAudioEncoder {
    private static final String TAG = AudioEncoderBuffered.class.getSimpleName();
    private final int MAX_POOL_SIZE;
    private final int MAX_QUEUE_SIZE;
    protected final LinkedBlockingQueue<MediaData> mAudioQueue;
    private AudioThread mAudioThread;
    private int mBufferNum;
    protected int mBufferSize;
    private DequeueThread mDequeueThread;
    protected final LinkedBlockingQueue<MediaData> mPool;

    public AudioEncoderBuffered(IRecorder iRecorder, EncoderListener encoderListener, int i, int i2) {
        super(iRecorder, encoderListener, i, i2);
        this.MAX_POOL_SIZE = 100;
        this.MAX_QUEUE_SIZE = 100;
        this.mAudioThread = null;
        this.mDequeueThread = null;
        this.mBufferSize = 1024;
        this.mPool = new LinkedBlockingQueue<>(100);
        this.mAudioQueue = new LinkedBlockingQueue<>(100);
        this.mBufferNum = 0;
        if (i < 0 || i > 7) {
            throw new IllegalArgumentException("invalid audio source:" + i);
        }
    }

    @Override // com.serenegiant.media.AbstractEncoder, com.serenegiant.media.Encoder
    public void start() {
        super.start();
        if (this.mAudioThread == null) {
            AudioThread audioThread = new AudioThread();
            this.mAudioThread = audioThread;
            audioThread.start();
            DequeueThread dequeueThread = new DequeueThread();
            this.mDequeueThread = dequeueThread;
            dequeueThread.start();
        }
    }

    @Override // com.serenegiant.media.AbstractEncoder, com.serenegiant.media.Encoder
    public void stop() {
        this.mAudioThread = null;
        this.mDequeueThread = null;
        super.stop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MediaData obtain() {
        MediaData mediaData;
        try {
            mediaData = this.mPool.poll(20L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException unused) {
            mediaData = null;
        }
        if (mediaData == null && this.mBufferNum < 100) {
            mediaData = new MediaData(this.mBufferSize);
            this.mBufferNum++;
        }
        if (mediaData != null) {
            mediaData.size = 0;
        }
        return mediaData;
    }

    protected void recycle(MediaData mediaData) {
        this.mPool.offer(mediaData);
    }

    private final class AudioThread extends Thread {
        public AudioThread() {
            super("AudioThread");
        }

        /* JADX WARN: Code restructure failed: missing block: B:45:0x00a8, code lost:
        
            android.util.Log.e(com.serenegiant.media.AudioEncoderBuffered.TAG, "Read error ERROR_DEAD_OBJECT");
            r9.this$0.recycle(r4);
         */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void run() {
            /*
                Method dump skipped, instructions count: 240
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.media.AudioEncoderBuffered.AudioThread.run():void");
        }
    }

    private final class DequeueThread extends Thread {
        public DequeueThread() {
            super("DequeueThread");
        }

        /* JADX WARN: Code restructure failed: missing block: B:13:0x001b, code lost:
        
            r2 = r8.this$0.mAudioQueue.poll(30, java.util.concurrent.TimeUnit.MILLISECONDS);
         */
        /* JADX WARN: Code restructure failed: missing block: B:14:0x0029, code lost:
        
            if (r2 == null) goto L55;
         */
        /* JADX WARN: Code restructure failed: missing block: B:17:0x002d, code lost:
        
            if (r2.size <= 0) goto L18;
         */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x002f, code lost:
        
            r8.this$0.encode(r2.mBuffer, r2.size, r2.presentationTimeUs);
            r8.this$0.frameAvailableSoon();
            r1 = r1 + 1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x0041, code lost:
        
            r8.this$0.recycle(r2);
         */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void run() {
            /*
                r8 = this;
                r0 = 0
                r1 = 0
            L2:
                com.serenegiant.media.AudioEncoderBuffered r2 = com.serenegiant.media.AudioEncoderBuffered.this
                java.lang.Object r2 = r2.mSync
                monitor-enter(r2)
                com.serenegiant.media.AudioEncoderBuffered r3 = com.serenegiant.media.AudioEncoderBuffered.this     // Catch: java.lang.Throwable -> L8b
                boolean r3 = r3.mIsCapturing     // Catch: java.lang.Throwable -> L8b
                if (r3 == 0) goto L49
                com.serenegiant.media.AudioEncoderBuffered r3 = com.serenegiant.media.AudioEncoderBuffered.this     // Catch: java.lang.Throwable -> L8b
                boolean r3 = r3.mRequestStop     // Catch: java.lang.Throwable -> L8b
                if (r3 != 0) goto L49
                com.serenegiant.media.AudioEncoderBuffered r3 = com.serenegiant.media.AudioEncoderBuffered.this     // Catch: java.lang.Throwable -> L8b
                boolean r3 = r3.mIsEOS     // Catch: java.lang.Throwable -> L8b
                if (r3 == 0) goto L1a
                goto L49
            L1a:
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L8b
                com.serenegiant.media.AudioEncoderBuffered r2 = com.serenegiant.media.AudioEncoderBuffered.this     // Catch: java.lang.InterruptedException -> L47
                java.util.concurrent.LinkedBlockingQueue<com.serenegiant.media.MediaData> r2 = r2.mAudioQueue     // Catch: java.lang.InterruptedException -> L47
                r3 = 30
                java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch: java.lang.InterruptedException -> L47
                java.lang.Object r2 = r2.poll(r3, r5)     // Catch: java.lang.InterruptedException -> L47
                com.serenegiant.media.MediaData r2 = (com.serenegiant.media.MediaData) r2     // Catch: java.lang.InterruptedException -> L47
                if (r2 == 0) goto L2
                int r3 = r2.size
                if (r3 <= 0) goto L41
                com.serenegiant.media.AudioEncoderBuffered r3 = com.serenegiant.media.AudioEncoderBuffered.this
                java.nio.ByteBuffer r4 = r2.mBuffer
                int r5 = r2.size
                long r6 = r2.presentationTimeUs
                r3.encode(r4, r5, r6)
                com.serenegiant.media.AudioEncoderBuffered r3 = com.serenegiant.media.AudioEncoderBuffered.this
                r3.frameAvailableSoon()
                int r1 = r1 + 1
            L41:
                com.serenegiant.media.AudioEncoderBuffered r3 = com.serenegiant.media.AudioEncoderBuffered.this
                r3.recycle(r2)
                goto L2
            L47:
                goto L4a
            L49:
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L8b
            L4a:
                if (r1 != 0) goto L8a
                com.serenegiant.media.AudioEncoderBuffered r1 = com.serenegiant.media.AudioEncoderBuffered.this
                int r1 = r1.mBufferSize
                java.nio.ByteBuffer r1 = java.nio.ByteBuffer.allocateDirect(r1)
                java.nio.ByteOrder r2 = java.nio.ByteOrder.nativeOrder()
                java.nio.ByteBuffer r1 = r1.order(r2)
            L5c:
                com.serenegiant.media.AudioEncoderBuffered r2 = com.serenegiant.media.AudioEncoderBuffered.this
                boolean r2 = r2.mIsCapturing
                if (r2 == 0) goto L8a
                r2 = 5
                if (r0 >= r2) goto L8a
                r2 = 1024(0x400, float:1.435E-42)
                r1.position(r2)
                r1.flip()
                com.serenegiant.media.AudioEncoderBuffered r3 = com.serenegiant.media.AudioEncoderBuffered.this
                long r4 = r3.getInputPTSUs()
                r3.encode(r1, r2, r4)
                com.serenegiant.media.AudioEncoderBuffered r2 = com.serenegiant.media.AudioEncoderBuffered.this
                r2.frameAvailableSoon()
                monitor-enter(r8)
                r2 = 50
                r8.wait(r2)     // Catch: java.lang.Throwable -> L82 java.lang.InterruptedException -> L84
                goto L84
            L82:
                r0 = move-exception
                goto L88
            L84:
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L82
                int r0 = r0 + 1
                goto L5c
            L88:
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L82
                throw r0
            L8a:
                return
            L8b:
                r0 = move-exception
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L8b
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.media.AudioEncoderBuffered.DequeueThread.run():void");
        }
    }
}
