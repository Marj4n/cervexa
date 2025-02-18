package com.serenegiant.media;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;
import androidx.documentfile.provider.DocumentFile;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public interface IRecorder {
    public static final int STATE_INITIALIZED = 1;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_STARTED = 4;
    public static final int STATE_STARTING = 3;
    public static final int STATE_STOPPING = 5;
    public static final int STATE_UNINITIALIZED = 0;

    public interface RecorderCallback {
        void onError(Exception exc);

        void onPrepared(IRecorder iRecorder);

        void onStarted(IRecorder iRecorder);

        void onStopped(IRecorder iRecorder);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RecorderState {
    }

    void addEncoder(Encoder encoder);

    int addTrack(Encoder encoder, MediaFormat mediaFormat);

    void frameAvailableSoon();

    Encoder getAudioEncoder();

    Surface getInputSurface();

    IMuxer getMuxer();

    DocumentFile getOutputFile();

    String getOutputPath();

    int getState();

    Encoder getVideoEncoder();

    boolean isReady();

    boolean isStarted();

    boolean isStopped();

    boolean isStopping();

    void prepare();

    void release();

    void removeEncoder(Encoder encoder);

    void setMuxer(IMuxer iMuxer);

    boolean start(Encoder encoder);

    void startRecording() throws IllegalStateException;

    void stop(Encoder encoder);

    void stopRecording();

    void writeSampleData(int i, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo);
}
