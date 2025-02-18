package com.serenegiant.usb.encoder.biz;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import com.serenegiant.media.MediaCodecHelper;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* loaded from: classes2.dex */
public class H264EncodeConsumer extends Thread {
    private static final float BPP = 0.5f;
    private static final boolean DEBUG = false;
    private static final int FRAME_INTERVAL = 1;
    private static final int FRAME_RATE = 15;
    private static final String MIME_TYPE = "video/avc";
    private static final String TAG = "H264EncodeConsumer";
    private static final int TIMES_OUT = 10000;
    private static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test2.h264";
    protected static int[] recognizedFormats = {21, MediaCodecHelper.OMX_QCOM_COLOR_FormatYVU420SemiPlanar};
    private ByteBuffer[] inputBuffers;
    private OnH264EncodeResultListener listener;
    private int mColorFormat;
    private MediaFormat mFormat;
    private int mHeight;
    private MediaCodec mMediaCodec;
    private WeakReference<Mp4MediaMuxer> mMuxerRef;
    private int mWidth;
    private MediaFormat newFormat;
    private ByteBuffer[] outputBuffers;
    private BufferedOutputStream outputStream;
    private boolean isExit = false;
    private boolean isEncoderStart = false;
    final int millisPerframe = 50;
    long lastPush = 0;
    private boolean isAddKeyFrame = false;

    public interface OnH264EncodeResultListener {
        void onEncodeResult(byte[] bArr, int i, int i2, long j);
    }

    public void setOnH264EncodeResultListener(OnH264EncodeResultListener onH264EncodeResultListener) {
        this.listener = onH264EncodeResultListener;
    }

    public H264EncodeConsumer(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
    }

    public synchronized void setTmpuMuxer(Mp4MediaMuxer mp4MediaMuxer) {
        WeakReference<Mp4MediaMuxer> weakReference = new WeakReference<>(mp4MediaMuxer);
        this.mMuxerRef = weakReference;
        Mp4MediaMuxer mp4MediaMuxer2 = weakReference.get();
        if (mp4MediaMuxer2 != null && this.newFormat != null) {
            mp4MediaMuxer2.addTrack(this.newFormat, true);
        }
    }

    public void setRawYuv(byte[] bArr, int i, int i2) {
        if (this.isEncoderStart) {
            if (this.mWidth != i || this.mHeight != i2) {
                this.mWidth = i;
                this.mHeight = i2;
                return;
            }
            try {
                if (this.lastPush == 0) {
                    this.lastPush = System.currentTimeMillis();
                }
                long currentTimeMillis = System.currentTimeMillis() - this.lastPush;
                if (currentTimeMillis >= 0) {
                    currentTimeMillis = 50 - currentTimeMillis;
                    if (currentTimeMillis > 0) {
                        Thread.sleep(currentTimeMillis / 2);
                    }
                }
                feedMediaCodecData(nv12ToNV21(bArr, this.mWidth, this.mHeight));
                if (currentTimeMillis > 0) {
                    Thread.sleep(currentTimeMillis / 2);
                }
                this.lastPush = System.currentTimeMillis();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void feedMediaCodecData(byte[] bArr) {
        int i;
        ByteBuffer byteBuffer;
        if (this.isEncoderStart) {
            try {
                i = this.mMediaCodec.dequeueInputBuffer(0L);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                i = -1;
            }
            if (i >= 0) {
                if (Build.VERSION.SDK_INT >= 21) {
                    byteBuffer = this.mMediaCodec.getInputBuffer(i);
                } else {
                    byteBuffer = this.inputBuffers[i];
                }
                byteBuffer.clear();
                byteBuffer.put(bArr);
                byteBuffer.clear();
                this.mMediaCodec.queueInputBuffer(i, 0, bArr.length, System.nanoTime() / 1000, 1);
            }
        }
    }

    public void exit() {
        this.isExit = true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        ByteBuffer byteBuffer;
        byte[] bArr;
        boolean z;
        WeakReference<Mp4MediaMuxer> weakReference;
        Mp4MediaMuxer mp4MediaMuxer;
        Mp4MediaMuxer mp4MediaMuxer2;
        if (!this.isEncoderStart) {
            startMediaCodec();
        }
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (!this.isExit) {
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            byte[] bArr2 = new byte[0];
            byte[] bArr3 = new byte[this.mWidth * this.mHeight];
            do {
                int dequeueOutputBuffer = this.mMediaCodec.dequeueOutputBuffer(bufferInfo, 10000L);
                if (dequeueOutputBuffer != -1) {
                    if (dequeueOutputBuffer == -3) {
                        this.outputBuffers = this.mMediaCodec.getOutputBuffers();
                    } else if (dequeueOutputBuffer == -2) {
                        synchronized (this) {
                            this.newFormat = this.mMediaCodec.getOutputFormat();
                            if (this.mMuxerRef != null && (mp4MediaMuxer2 = this.mMuxerRef.get()) != null) {
                                mp4MediaMuxer2.addTrack(this.newFormat, true);
                            }
                        }
                    } else if (dequeueOutputBuffer >= 0) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            byteBuffer = this.mMediaCodec.getOutputBuffer(dequeueOutputBuffer);
                        } else {
                            byteBuffer = this.outputBuffers[dequeueOutputBuffer];
                        }
                        byteBuffer.position(bufferInfo.offset);
                        byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
                        if ((bufferInfo.flags & 2) != 0) {
                            z = (bufferInfo.flags & 1) != 0;
                            if (z) {
                                bArr = new byte[0];
                            } else {
                                bArr2 = new byte[bufferInfo.size];
                                byteBuffer.get(bArr2);
                                this.mMediaCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
                            }
                        } else {
                            bArr = bArr2;
                            z = false;
                        }
                        boolean z2 = z | ((bufferInfo.flags & 1) != 0);
                        int length = bArr.length + bufferInfo.size;
                        if (length > bArr3.length) {
                            bArr3 = new byte[length];
                        }
                        if (z2) {
                            System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
                            byteBuffer.get(bArr3, bArr.length, bufferInfo.size);
                            OnH264EncodeResultListener onH264EncodeResultListener = this.listener;
                            if (onH264EncodeResultListener != null) {
                                onH264EncodeResultListener.onEncodeResult(bArr3, 0, bArr.length + bufferInfo.size, bufferInfo.presentationTimeUs / 1000);
                            }
                            WeakReference<Mp4MediaMuxer> weakReference2 = this.mMuxerRef;
                            if (weakReference2 != null) {
                                Mp4MediaMuxer mp4MediaMuxer3 = weakReference2.get();
                                if (mp4MediaMuxer3 != null) {
                                    mp4MediaMuxer3.pumpStream(byteBuffer, bufferInfo, true);
                                }
                                this.isAddKeyFrame = true;
                            }
                        } else {
                            byteBuffer.get(bArr3, 0, bufferInfo.size);
                            OnH264EncodeResultListener onH264EncodeResultListener2 = this.listener;
                            if (onH264EncodeResultListener2 != null) {
                                onH264EncodeResultListener2.onEncodeResult(bArr3, 0, bufferInfo.size, bufferInfo.presentationTimeUs / 1000);
                            }
                            if (this.isAddKeyFrame && (weakReference = this.mMuxerRef) != null && (mp4MediaMuxer = weakReference.get()) != null) {
                                mp4MediaMuxer.pumpStream(byteBuffer, bufferInfo, true);
                            }
                        }
                        this.mMediaCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
                        bArr2 = bArr;
                    }
                }
                if (!this.isExit) {
                }
            } while (this.isEncoderStart);
        }
        stopMediaCodec();
    }

    private void startMediaCodec() {
        if (selectVideoCodec("video/avc") == null) {
            Log.e(TAG, "Unable to find an appropriate codec for video/avc");
            return;
        }
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", this.mWidth, this.mHeight);
        createVideoFormat.setInteger("color-format", this.mColorFormat);
        createVideoFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, calcBitRate());
        createVideoFormat.setInteger("frame-rate", 30);
        createVideoFormat.setInteger("i-frame-interval", 1);
        try {
            this.mMediaCodec = MediaCodec.createEncoderByType("video/avc");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mMediaCodec.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mMediaCodec.start();
        this.isEncoderStart = true;
        if (Build.VERSION.SDK_INT >= 22) {
            this.outputBuffers = null;
            this.inputBuffers = null;
        } else {
            this.inputBuffers = this.mMediaCodec.getInputBuffers();
            this.outputBuffers = this.mMediaCodec.getOutputBuffers();
        }
        Bundle bundle = new Bundle();
        bundle.putInt("request-sync", 0);
        if (Build.VERSION.SDK_INT >= 19) {
            this.mMediaCodec.setParameters(bundle);
        }
    }

    private void stopMediaCodec() {
        this.isEncoderStart = false;
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec != null) {
            mediaCodec.stop();
            this.mMediaCodec.release();
            Log.d(TAG, "关闭视频编码器");
        }
    }

    private int calcBitRate() {
        int i = (int) (this.mWidth * 7.5f * this.mHeight);
        Log.i(TAG, String.format("bitrate=%5.2f[Mbps]", Float.valueOf((i / 1024.0f) / 1024.0f)));
        return i;
    }

    protected final MediaCodecInfo selectVideoCodec(String str) {
        int selectColorFormat;
        int codecCount = MediaCodecList.getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                for (String str2 : codecInfoAt.getSupportedTypes()) {
                    if (str2.equalsIgnoreCase(str) && (selectColorFormat = selectColorFormat(codecInfoAt, str)) > 0) {
                        this.mColorFormat = selectColorFormat;
                        return codecInfoAt;
                    }
                }
            }
        }
        return null;
    }

    protected static final int selectColorFormat(MediaCodecInfo mediaCodecInfo, String str) {
        try {
            Thread.currentThread().setPriority(10);
            MediaCodecInfo.CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(str);
            Thread.currentThread().setPriority(5);
            int i = 0;
            int i2 = 0;
            while (true) {
                if (i2 >= capabilitiesForType.colorFormats.length) {
                    break;
                }
                int i3 = capabilitiesForType.colorFormats[i2];
                if (isRecognizedViewoFormat(i3)) {
                    i = i3;
                    break;
                }
                i2++;
            }
            if (i == 0) {
                Log.e(TAG, "couldn't find a good color format for " + mediaCodecInfo.getName() + " / " + str);
            }
            return i;
        } catch (Throwable th) {
            Thread.currentThread().setPriority(5);
            throw th;
        }
    }

    private static final boolean isRecognizedViewoFormat(int i) {
        int[] iArr = recognizedFormats;
        int length = iArr != null ? iArr.length : 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (recognizedFormats[i2] == i) {
                return true;
            }
        }
        return false;
    }

    private byte[] nv21ToI420(byte[] bArr, int i, int i2) {
        int i3 = i * i2;
        byte[] bArr2 = new byte[(i3 * 3) / 2];
        ByteBuffer wrap = ByteBuffer.wrap(bArr2, 0, i3);
        int i4 = i3 / 4;
        ByteBuffer wrap2 = ByteBuffer.wrap(bArr2, i3, i4);
        ByteBuffer wrap3 = ByteBuffer.wrap(bArr2, i3 + i4, i4);
        wrap.put(bArr, 0, i3);
        while (i3 < bArr.length) {
            wrap3.put(bArr[i3]);
            wrap2.put(bArr[i3 + 1]);
            i3 += 2;
        }
        return bArr2;
    }

    private byte[] nv12ToI420(byte[] bArr, int i, int i2) {
        int i3 = i * i2;
        byte[] bArr2 = new byte[(i3 * 3) / 2];
        ByteBuffer wrap = ByteBuffer.wrap(bArr2, 0, i3);
        int i4 = i3 / 4;
        ByteBuffer wrap2 = ByteBuffer.wrap(bArr2, i3, i4);
        ByteBuffer wrap3 = ByteBuffer.wrap(bArr2, i3 + i4, i4);
        wrap.put(bArr, 0, i3);
        while (i3 < bArr.length) {
            wrap2.put(bArr[i3]);
            wrap3.put(bArr[i3 + 1]);
            i3 += 2;
        }
        return bArr2;
    }

    private byte[] nv12ToNv21(byte[] bArr, int i, int i2) {
        int i3 = i * i2;
        byte[] bArr2 = new byte[(i3 * 3) / 2];
        ByteBuffer wrap = ByteBuffer.wrap(bArr2, 0, i3);
        int i4 = i3 / 4;
        ByteBuffer wrap2 = ByteBuffer.wrap(bArr2, i3, i4);
        ByteBuffer wrap3 = ByteBuffer.wrap(bArr2, i3 + i4, i4);
        wrap.put(bArr, 0, i3);
        while (i3 < bArr.length) {
            wrap2.put(bArr[i3]);
            wrap3.put(bArr[i3 + 1]);
            i3 += 2;
        }
        return bArr2;
    }

    private byte[] nv21ToNV12(byte[] bArr, int i, int i2) {
        int i3 = i * i2;
        byte[] bArr2 = new byte[(i3 * 3) / 2];
        System.arraycopy(bArr, 0, bArr2, 0, i3);
        while (i3 < bArr.length) {
            int i4 = i3 + 1;
            bArr2[i4] = bArr[i4];
            bArr2[i3] = bArr[i3];
            i3 += 2;
        }
        return bArr2;
    }

    private byte[] nv12ToNV21(byte[] bArr, int i, int i2) {
        int i3 = i * i2;
        byte[] bArr2 = new byte[(i3 * 3) / 2];
        System.arraycopy(bArr, 0, bArr2, 0, i3);
        while (i3 < bArr.length) {
            int i4 = i3 + 1;
            bArr2[i3] = bArr[i4];
            bArr2[i4] = bArr[i3];
            i3 += 2;
        }
        return bArr2;
    }
}
