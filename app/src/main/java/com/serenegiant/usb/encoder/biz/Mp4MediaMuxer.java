package com.serenegiant.usb.encoder.biz;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import com.jiangdg.usbcamera.UVCCameraHelper;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class Mp4MediaMuxer {
    private static final String TAG = Mp4MediaMuxer.class.getSimpleName();
    private static final boolean VERBOSE = false;
    private long durationMillis;
    private int index;
    private boolean isVoiceClose;
    private MediaFormat mAudioFormat;
    private long mBeginMillis;
    private String mFilePath;
    private MediaMuxer mMuxer;
    private MediaFormat mVideoFormat;
    private int mVideoTrackIndex = -1;
    private int mAudioTrackIndex = -1;

    public Mp4MediaMuxer(String str, long j, boolean z) {
        String str2;
        this.index = 0;
        this.isVoiceClose = z;
        this.durationMillis = j;
        if (j != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("-");
            int i = this.index;
            this.index = i + 1;
            sb.append(i);
            sb.append(UVCCameraHelper.SUFFIX_MP4);
            str2 = sb.toString();
        } else {
            str2 = str + UVCCameraHelper.SUFFIX_MP4;
        }
        MediaMuxer mediaMuxer = null;
        try {
            try {
                if (Build.VERSION.SDK_INT >= 18) {
                    mediaMuxer = new MediaMuxer(str2, 0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            this.mMuxer = (MediaMuxer) null;
        }
    }

    public synchronized void addTrack(MediaFormat mediaFormat, boolean z) {
        if (!this.isVoiceClose && this.mAudioTrackIndex != -1 && this.mVideoTrackIndex != -1) {
            throw new RuntimeException("already add all tracks");
        }
        if (Build.VERSION.SDK_INT >= 18) {
            int addTrack = this.mMuxer.addTrack(mediaFormat);
            if (z) {
                this.mVideoFormat = mediaFormat;
                this.mVideoTrackIndex = addTrack;
                if (this.isVoiceClose || this.mAudioTrackIndex != -1) {
                    this.mMuxer.start();
                    this.mBeginMillis = System.currentTimeMillis();
                }
            } else {
                this.mAudioFormat = mediaFormat;
                this.mAudioTrackIndex = addTrack;
                if (this.mVideoTrackIndex != -1) {
                    this.mMuxer.start();
                    this.mBeginMillis = System.currentTimeMillis();
                }
            }
        }
    }

    public synchronized void pumpStream(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo, boolean z) {
        if ((this.isVoiceClose || this.mAudioTrackIndex != -1) && this.mVideoTrackIndex != -1) {
            if ((bufferInfo.flags & 2) == 0 && bufferInfo.size != 0) {
                if (z && this.mVideoTrackIndex == -1) {
                    throw new RuntimeException("muxer hasn't started");
                }
                byteBuffer.position(bufferInfo.offset);
                byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
                if (Build.VERSION.SDK_INT >= 18) {
                    this.mMuxer.writeSampleData(z ? this.mVideoTrackIndex : this.mAudioTrackIndex, byteBuffer, bufferInfo);
                }
            }
            int i = bufferInfo.flags;
            if (this.durationMillis != 0 && System.currentTimeMillis() - this.mBeginMillis >= this.durationMillis && Build.VERSION.SDK_INT >= 18) {
                this.mMuxer.stop();
                this.mMuxer.release();
                this.mMuxer = null;
                this.mAudioTrackIndex = -1;
                this.mVideoTrackIndex = -1;
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.mFilePath);
                    sb.append("-");
                    int i2 = this.index + 1;
                    this.index = i2;
                    sb.append(i2);
                    sb.append(UVCCameraHelper.SUFFIX_MP4);
                    this.mMuxer = new MediaMuxer(sb.toString(), 0);
                    addTrack(this.mVideoFormat, true);
                    addTrack(this.mAudioFormat, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized void release() {
        if (Build.VERSION.SDK_INT >= 18 && this.mMuxer != null && this.mVideoTrackIndex != -1) {
            try {
                this.mMuxer.stop();
                this.mMuxer.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - this.mBeginMillis <= 1500) {
                new File(this.mFilePath + "-" + this.index + UVCCameraHelper.SUFFIX_MP4).delete();
            }
            this.mVideoTrackIndex = -1;
            this.mAudioTrackIndex = -1;
        }
    }
}
