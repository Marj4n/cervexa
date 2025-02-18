package com.serenegiant.usb.encoder;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import com.jiangdg.usbcamera.UVCCameraHelper;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

/* loaded from: classes2.dex */
public class MediaMuxerWrapper {
    private static final boolean DEBUG = true;
    private static final String DIR_NAME = "USBCameraTest";
    private static final String TAG = "MediaMuxerWrapper";
    private static final SimpleDateFormat mDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
    private MediaEncoder mAudioEncoder;
    private int mEncoderCount;
    private boolean mIsStarted;
    private final MediaMuxer mMediaMuxer;
    private String mOutputPath;
    private int mStatredCount;
    private MediaEncoder mVideoEncoder;

    public MediaMuxerWrapper(String str) throws IOException {
        try {
            if (TextUtils.isEmpty(str)) {
                this.mOutputPath = getCaptureFile(Environment.DIRECTORY_MOVIES, UVCCameraHelper.SUFFIX_MP4).toString();
            }
            this.mOutputPath = str;
            this.mMediaMuxer = new MediaMuxer(this.mOutputPath, 0);
            this.mStatredCount = 0;
            this.mEncoderCount = 0;
            this.mIsStarted = false;
        } catch (NullPointerException unused) {
            throw new RuntimeException("This app has no permission of writing external storage");
        }
    }

    public String getOutputPath() {
        return this.mOutputPath;
    }

    public void prepare() throws IOException {
        MediaEncoder mediaEncoder = this.mVideoEncoder;
        if (mediaEncoder != null) {
            mediaEncoder.prepare();
        }
        MediaEncoder mediaEncoder2 = this.mAudioEncoder;
        if (mediaEncoder2 != null) {
            mediaEncoder2.prepare();
        }
    }

    public void startRecording() {
        MediaEncoder mediaEncoder = this.mVideoEncoder;
        if (mediaEncoder != null) {
            mediaEncoder.startRecording();
        }
        MediaEncoder mediaEncoder2 = this.mAudioEncoder;
        if (mediaEncoder2 != null) {
            mediaEncoder2.startRecording();
        }
    }

    public void stopRecording() {
        MediaEncoder mediaEncoder = this.mVideoEncoder;
        if (mediaEncoder != null) {
            mediaEncoder.stopRecording();
        }
        this.mVideoEncoder = null;
        MediaEncoder mediaEncoder2 = this.mAudioEncoder;
        if (mediaEncoder2 != null) {
            mediaEncoder2.stopRecording();
        }
        this.mAudioEncoder = null;
    }

    public synchronized boolean isStarted() {
        return this.mIsStarted;
    }

    void addEncoder(MediaEncoder mediaEncoder) {
        if (mediaEncoder instanceof MediaVideoEncoder) {
            if (this.mVideoEncoder != null) {
                throw new IllegalArgumentException("Video encoder already added.");
            }
            this.mVideoEncoder = mediaEncoder;
        } else if (mediaEncoder instanceof MediaSurfaceEncoder) {
            if (this.mVideoEncoder != null) {
                throw new IllegalArgumentException("Video encoder already added.");
            }
            this.mVideoEncoder = mediaEncoder;
        } else if (mediaEncoder instanceof MediaVideoBufferEncoder) {
            if (this.mVideoEncoder != null) {
                throw new IllegalArgumentException("Video encoder already added.");
            }
            this.mVideoEncoder = mediaEncoder;
        } else if (mediaEncoder instanceof MediaAudioEncoder) {
            if (this.mAudioEncoder != null) {
                throw new IllegalArgumentException("Video encoder already added.");
            }
            this.mAudioEncoder = mediaEncoder;
        } else {
            throw new IllegalArgumentException("unsupported encoder");
        }
        this.mEncoderCount = (this.mVideoEncoder != null ? 1 : 0) + (this.mAudioEncoder == null ? 0 : 1);
    }

    synchronized boolean start() {
        Log.v(TAG, "start:");
        int i = this.mStatredCount + 1;
        this.mStatredCount = i;
        if (this.mEncoderCount > 0 && i == this.mEncoderCount) {
            this.mMediaMuxer.start();
            this.mIsStarted = true;
            notifyAll();
            Log.v(TAG, "MediaMuxer started:");
        }
        return this.mIsStarted;
    }

    synchronized void stop() {
        Log.v(TAG, "stop:mStatredCount=" + this.mStatredCount);
        int i = this.mStatredCount + (-1);
        this.mStatredCount = i;
        if (this.mEncoderCount > 0 && i <= 0) {
            try {
                this.mMediaMuxer.stop();
            } catch (Exception e) {
                Log.w(TAG, e);
            }
            this.mIsStarted = false;
            Log.v(TAG, "MediaMuxer stopped:");
        }
    }

    synchronized int addTrack(MediaFormat mediaFormat) {
        int addTrack;
        if (this.mIsStarted) {
            throw new IllegalStateException("muxer already started");
        }
        addTrack = this.mMediaMuxer.addTrack(mediaFormat);
        Log.i(TAG, "addTrack:trackNum=" + this.mEncoderCount + ",trackIx=" + addTrack + ",format=" + mediaFormat);
        return addTrack;
    }

    synchronized void writeSampleData(int i, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        if (this.mStatredCount > 0) {
            this.mMediaMuxer.writeSampleData(i, byteBuffer, bufferInfo);
        }
    }

    public static final File getCaptureFile(String str, String str2) {
        File file = new File(Environment.getExternalStoragePublicDirectory(str), DIR_NAME);
        Log.d(TAG, "path=" + file.toString());
        file.mkdirs();
        if (!file.canWrite()) {
            return null;
        }
        return new File(file, getDateTimeString() + str2);
    }

    private static final String getDateTimeString() {
        return mDateTimeFormat.format(new GregorianCalendar().getTime());
    }
}
