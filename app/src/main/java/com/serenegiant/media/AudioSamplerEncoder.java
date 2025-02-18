package com.serenegiant.media;

import com.serenegiant.media.IAudioSampler;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class AudioSamplerEncoder extends AbstractAudioEncoder {
    private int frame_count;
    private final Runnable mAudioTask;
    private final IAudioSampler mSampler;
    private final boolean mSamplerCreated;
    private final IAudioSampler.SoundSamplerCallback mSoundSamplerCallback;

    static /* synthetic */ int access$008(AudioSamplerEncoder audioSamplerEncoder) {
        int i = audioSamplerEncoder.frame_count;
        audioSamplerEncoder.frame_count = i + 1;
        return i;
    }

    public AudioSamplerEncoder(IRecorder iRecorder, EncoderListener encoderListener, int i, IAudioSampler iAudioSampler) {
        super(iRecorder, encoderListener, i, iAudioSampler != null ? iAudioSampler.getChannels() : 1, iAudioSampler != null ? iAudioSampler.getSamplingFrequency() : 44100, AbstractAudioEncoder.DEFAULT_BIT_RATE);
        this.frame_count = 0;
        this.mSoundSamplerCallback = new IAudioSampler.SoundSamplerCallback() { // from class: com.serenegiant.media.AudioSamplerEncoder.1
            @Override // com.serenegiant.media.IAudioSampler.SoundSamplerCallback
            public void onError(Exception exc) {
            }

            @Override // com.serenegiant.media.IAudioSampler.SoundSamplerCallback
            public void onData(ByteBuffer byteBuffer, int i2, long j) {
                synchronized (AudioSamplerEncoder.this.mSync) {
                    if (AudioSamplerEncoder.this.mIsCapturing && !AudioSamplerEncoder.this.mRequestStop && !AudioSamplerEncoder.this.mIsEOS) {
                        if (i2 > 0) {
                            AudioSamplerEncoder.this.frameAvailableSoon();
                            AudioSamplerEncoder.this.encode(byteBuffer, i2, j);
                            AudioSamplerEncoder.access$008(AudioSamplerEncoder.this);
                        }
                    }
                }
            }
        };
        this.mAudioTask = new Runnable() { // from class: com.serenegiant.media.AudioSamplerEncoder.2
            /* JADX WARN: Removed duplicated region for block: B:19:0x002c  */
            /* JADX WARN: Removed duplicated region for block: B:43:0x006a A[ORIG_RETURN, RETURN] */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void run() {
                /*
                    r6 = this;
                L0:
                    com.serenegiant.media.AudioSamplerEncoder r0 = com.serenegiant.media.AudioSamplerEncoder.this
                    java.lang.Object r0 = r0.mSync
                    monitor-enter(r0)
                    com.serenegiant.media.AudioSamplerEncoder r1 = com.serenegiant.media.AudioSamplerEncoder.this     // Catch: java.lang.Throwable -> L6b
                    boolean r1 = r1.mIsCapturing     // Catch: java.lang.Throwable -> L6b
                    if (r1 == 0) goto L23
                    com.serenegiant.media.AudioSamplerEncoder r1 = com.serenegiant.media.AudioSamplerEncoder.this     // Catch: java.lang.Throwable -> L6b
                    boolean r1 = r1.mRequestStop     // Catch: java.lang.Throwable -> L6b
                    if (r1 != 0) goto L23
                    com.serenegiant.media.AudioSamplerEncoder r1 = com.serenegiant.media.AudioSamplerEncoder.this     // Catch: java.lang.Throwable -> L6b
                    boolean r1 = r1.mIsEOS     // Catch: java.lang.Throwable -> L6b
                    if (r1 == 0) goto L18
                    goto L23
                L18:
                    com.serenegiant.media.AudioSamplerEncoder r1 = com.serenegiant.media.AudioSamplerEncoder.this     // Catch: java.lang.InterruptedException -> L21 java.lang.Throwable -> L6b
                    java.lang.Object r1 = r1.mSync     // Catch: java.lang.InterruptedException -> L21 java.lang.Throwable -> L6b
                    r1.wait()     // Catch: java.lang.InterruptedException -> L21 java.lang.Throwable -> L6b
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> L6b
                    goto L0
                L21:
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> L6b
                    goto L24
                L23:
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> L6b
                L24:
                    com.serenegiant.media.AudioSamplerEncoder r0 = com.serenegiant.media.AudioSamplerEncoder.this
                    int r0 = com.serenegiant.media.AudioSamplerEncoder.access$000(r0)
                    if (r0 != 0) goto L6a
                    r0 = 1024(0x400, float:1.435E-42)
                    java.nio.ByteBuffer r1 = java.nio.ByteBuffer.allocateDirect(r0)
                    java.nio.ByteOrder r2 = java.nio.ByteOrder.nativeOrder()
                    java.nio.ByteBuffer r1 = r1.order(r2)
                    r2 = 0
                L3b:
                    com.serenegiant.media.AudioSamplerEncoder r3 = com.serenegiant.media.AudioSamplerEncoder.this
                    boolean r3 = r3.mIsCapturing
                    if (r3 == 0) goto L6a
                    r3 = 5
                    if (r2 >= r3) goto L6a
                    r1.clear()
                    r1.position(r0)
                    r1.flip()
                    com.serenegiant.media.AudioSamplerEncoder r3 = com.serenegiant.media.AudioSamplerEncoder.this
                    long r4 = r3.getInputPTSUs()
                    r3.encode(r1, r0, r4)
                    com.serenegiant.media.AudioSamplerEncoder r3 = com.serenegiant.media.AudioSamplerEncoder.this
                    r3.frameAvailableSoon()
                    monitor-enter(r6)
                    r3 = 50
                    r6.wait(r3)     // Catch: java.lang.Throwable -> L62 java.lang.InterruptedException -> L64
                    goto L64
                L62:
                    r0 = move-exception
                    goto L68
                L64:
                    monitor-exit(r6)     // Catch: java.lang.Throwable -> L62
                    int r2 = r2 + 1
                    goto L3b
                L68:
                    monitor-exit(r6)     // Catch: java.lang.Throwable -> L62
                    throw r0
                L6a:
                    return
                L6b:
                    r1 = move-exception
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> L6b
                    throw r1
                */
                throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.media.AudioSamplerEncoder.AnonymousClass2.run():void");
            }
        };
        if (iAudioSampler != null) {
            this.mSamplerCreated = false;
        } else {
            if (i < 0 || i > 7) {
                throw new IllegalArgumentException("invalid audio source:" + i);
            }
            iAudioSampler = new AudioSampler(i, 1, 44100, 1024, 25);
            this.mSamplerCreated = true;
        }
        this.mSampler = iAudioSampler;
    }

    @Override // com.serenegiant.media.AbstractEncoder, com.serenegiant.media.Encoder
    public void start() {
        super.start();
        this.mSampler.addCallback(this.mSoundSamplerCallback);
        if (this.mSamplerCreated) {
            this.mSampler.start();
        }
        new Thread(this.mAudioTask, "AudioTask").start();
    }

    @Override // com.serenegiant.media.AbstractEncoder, com.serenegiant.media.Encoder
    public void stop() {
        this.mSampler.removeCallback(this.mSoundSamplerCallback);
        if (this.mSamplerCreated) {
            this.mSampler.stop();
        }
        super.stop();
    }

    @Override // com.serenegiant.media.AbstractEncoder, com.serenegiant.media.Encoder
    public void release() {
        if (this.mSamplerCreated) {
            this.mSampler.release();
        }
        super.release();
    }
}
