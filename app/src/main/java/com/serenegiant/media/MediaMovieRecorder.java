package com.serenegiant.media;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class MediaMovieRecorder extends AbstractRecorder {
    private static final boolean DEBUG = false;
    private static final String TAG = "MediaMovieRecorder";
    private final boolean hasAudioEncoder;
    private final IMediaCodecCallback mMediaCodecCallback;
    private final IMuxer mMuxer;
    private IRecorderCallback mRecorderCallback;

    public interface IRecorderCallback {
        void onPrepared(MediaMovieRecorder mediaMovieRecorder);

        void onStart(MediaMovieRecorder mediaMovieRecorder);

        void onStop(MediaMovieRecorder mediaMovieRecorder);
    }

    public MediaMovieRecorder(String str, boolean z) throws IOException {
        this(str, z, false);
    }

    public MediaMovieRecorder(String str, boolean z, boolean z2) throws IOException {
        super(str);
        this.mMediaCodecCallback = new IMediaCodecCallback() { // from class: com.serenegiant.media.MediaMovieRecorder.1
            @Override // com.serenegiant.media.IMediaCodecCallback
            public boolean onError(IMediaCodec iMediaCodec, Exception exc) {
                return false;
            }

            @Override // com.serenegiant.media.IMediaCodecCallback
            public boolean onFrameAvailable(IMediaCodec iMediaCodec, long j) {
                return false;
            }

            @Override // com.serenegiant.media.IMediaCodecCallback
            public void onRelease(IMediaCodec iMediaCodec) {
            }

            @Override // com.serenegiant.media.IMediaCodecCallback
            public void onPrepared(IMediaCodec iMediaCodec) {
                if (!(MediaMovieRecorder.this.mVideoEncoder.isPrepared() && (!MediaMovieRecorder.this.hasAudioEncoder || MediaMovieRecorder.this.mAudioEncoder.isPrepared())) || MediaMovieRecorder.this.mRecorderCallback == null) {
                    return;
                }
                try {
                    MediaMovieRecorder.this.mRecorderCallback.onPrepared(MediaMovieRecorder.this);
                } catch (Exception e) {
                    Log.w(MediaMovieRecorder.TAG, e);
                }
            }

            @Override // com.serenegiant.media.IMediaCodecCallback
            public void onStart(IMediaCodec iMediaCodec) {
                if (!(MediaMovieRecorder.this.mVideoEncoder.isRunning() && (!MediaMovieRecorder.this.hasAudioEncoder || MediaMovieRecorder.this.mAudioEncoder.isRunning())) || MediaMovieRecorder.this.mRecorderCallback == null) {
                    return;
                }
                try {
                    MediaMovieRecorder.this.mRecorderCallback.onStart(MediaMovieRecorder.this);
                } catch (Exception e) {
                    Log.w(MediaMovieRecorder.TAG, e);
                }
            }

            @Override // com.serenegiant.media.IMediaCodecCallback
            public void onStop(IMediaCodec iMediaCodec) {
                if (MediaMovieRecorder.this.mRecorderCallback != null) {
                    try {
                        MediaMovieRecorder.this.mRecorderCallback.onStop(MediaMovieRecorder.this);
                        MediaMovieRecorder.this.release();
                    } catch (Exception e) {
                        Log.w(MediaMovieRecorder.TAG, e);
                    }
                }
            }
        };
        this.mMuxer = z2 ? new VideoMuxer(str) : new MediaMuxerWrapper(str, 0);
        new MediaVideoEncoder(this, this.mMediaCodecCallback);
        if (z) {
            new MediaAudioEncoder(this, this.mMediaCodecCallback);
        }
        this.hasAudioEncoder = z;
    }

    public void setCallback(IRecorderCallback iRecorderCallback) {
        this.mRecorderCallback = iRecorderCallback;
    }

    public IRecorderCallback getCallback() {
        return this.mRecorderCallback;
    }

    public void setVideoSize(int i, int i2) {
        ((MediaVideoEncoder) this.mVideoEncoder).setVideoSize(i, i2);
    }

    @Override // com.serenegiant.media.AbstractRecorder
    public int getWidth() {
        if (this.mVideoEncoder != null) {
            return ((MediaVideoEncoder) this.mVideoEncoder).getWidth();
        }
        return 0;
    }

    @Override // com.serenegiant.media.AbstractRecorder
    public int getHeight() {
        if (this.mVideoEncoder != null) {
            return ((MediaVideoEncoder) this.mVideoEncoder).getHeight();
        }
        return 0;
    }

    @Override // com.serenegiant.media.AbstractRecorder
    public Surface getInputSurface() throws IllegalStateException {
        return ((MediaVideoEncoder) this.mVideoEncoder).getInputSurface();
    }

    @Override // com.serenegiant.media.AbstractRecorder
    protected void internal_start() {
        this.mMuxer.start();
    }

    @Override // com.serenegiant.media.AbstractRecorder
    protected void internal_stop() {
        this.mMuxer.stop();
        this.mMuxer.release();
    }

    @Override // com.serenegiant.media.AbstractRecorder
    int addTrack(MediaFormat mediaFormat) {
        if (this.mIsStarted) {
            throw new IllegalStateException("muxer already started");
        }
        return this.mMuxer.addTrack(mediaFormat);
    }

    @Override // com.serenegiant.media.AbstractRecorder
    void writeSampleData(int i, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        if (this.mIsStarted) {
            this.mMuxer.writeSampleData(i, byteBuffer, bufferInfo);
        }
    }
}
