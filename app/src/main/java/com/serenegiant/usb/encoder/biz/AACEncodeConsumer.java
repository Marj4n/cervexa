package com.serenegiant.usb.encoder.biz;

import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Process;
import android.util.Log;
import android.view.Surface;
import com.baidu.trace.model.StatusCodes;
import com.serenegiant.media.AbstractAudioEncoder;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import tv.danmaku.ijk.media.player.IjkMediaMeta;
import tv.danmaku.ijk.media.player.misc.IMediaFormat;

/* loaded from: classes2.dex */
public class AACEncodeConsumer extends Thread {
    private static final int BIT_RATE = 16000;
    private static final int BUFFER_SIZE = 1920;
    private static final boolean DEBUG = false;
    private static final String MIME_TYPE = "audio/mp4a-latm";
    private static final int SAMPLE_RATE = 8000;
    private static final String TAG = "TMPU";
    private static final long TIMES_OUT = 1000;
    private int bufferSizeInBytes;
    private FileOutputStream fops;
    private OnAACEncodeResultListener listener;
    private MediaCodec mAudioEncoder;
    private AudioRecord mAudioRecord;
    private WeakReference<Mp4MediaMuxer> mMuxerRef;
    private int mSamplingRateIndex;
    private MediaFormat newFormat;
    private static final int[] AUDIO_SOURCES = {0, 1, 5};
    public static final int[] AUDIO_SAMPLING_RATES = {96000, 88200, AbstractAudioEncoder.DEFAULT_BIT_RATE, 48000, 44100, 32000, 24000, 22050, 16000, StatusCodes.START_GATHER_REQUEST_FAILED, 11025, 8000, 7350, -1, -1, -1};
    private int outChannel = 1;
    private int bitRateForLame = 32;
    private int qaulityDegree = 7;
    private boolean isEncoderStart = false;
    private boolean isRecMp3 = false;
    private boolean isExit = false;
    private long prevPresentationTimes = 0;

    public interface OnAACEncodeResultListener {
        void onEncodeResult(byte[] bArr, int i, int i2, long j);
    }

    public AACEncodeConsumer() {
        int i = 0;
        this.mSamplingRateIndex = 0;
        while (true) {
            int[] iArr = AUDIO_SAMPLING_RATES;
            if (i >= iArr.length) {
                return;
            }
            if (iArr[i] == 8000) {
                this.mSamplingRateIndex = i;
                return;
            }
            i++;
        }
    }

    public void setOnAACEncodeResultListener(OnAACEncodeResultListener onAACEncodeResultListener) {
        this.listener = onAACEncodeResultListener;
    }

    public void exit() {
        this.isExit = true;
    }

    public synchronized void setTmpuMuxer(Mp4MediaMuxer mp4MediaMuxer) {
        WeakReference<Mp4MediaMuxer> weakReference = new WeakReference<>(mp4MediaMuxer);
        this.mMuxerRef = weakReference;
        Mp4MediaMuxer mp4MediaMuxer2 = weakReference.get();
        if (mp4MediaMuxer2 != null && this.newFormat != null) {
            mp4MediaMuxer2.addTrack(this.newFormat, false);
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (!this.isEncoderStart) {
            initAudioRecord();
            initMediaCodec();
        }
        while (!this.isExit) {
            byte[] bArr = new byte[2048];
            int read = this.mAudioRecord.read(bArr, 0, BUFFER_SIZE);
            if (read > 0) {
                encodeBytes(bArr, read);
            }
        }
        stopMediaCodec();
        stopAudioRecord();
    }

    private void encodeBytes(byte[] bArr, int i) {
        int dequeueOutputBuffer;
        ByteBuffer outputBuffer;
        Mp4MediaMuxer mp4MediaMuxer;
        Mp4MediaMuxer mp4MediaMuxer2;
        ByteBuffer inputBuffer;
        ByteBuffer[] inputBuffers = this.mAudioEncoder.getInputBuffers();
        ByteBuffer[] outputBuffers = this.mAudioEncoder.getOutputBuffers();
        int dequeueInputBuffer = this.mAudioEncoder.dequeueInputBuffer(TIMES_OUT);
        if (dequeueInputBuffer >= 0) {
            if (!isLollipop()) {
                inputBuffer = inputBuffers[dequeueInputBuffer];
            } else {
                inputBuffer = this.mAudioEncoder.getInputBuffer(dequeueInputBuffer);
            }
            if (bArr == null || i <= 0) {
                this.mAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, 0, getPTSUs(), 4);
            } else {
                inputBuffer.clear();
                inputBuffer.put(bArr);
                this.mAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i, getPTSUs(), 0);
            }
        }
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        do {
            dequeueOutputBuffer = this.mAudioEncoder.dequeueOutputBuffer(bufferInfo, TIMES_OUT);
            if (dequeueOutputBuffer != -1) {
                if (dequeueOutputBuffer == -3) {
                    if (!isLollipop()) {
                        outputBuffers = this.mAudioEncoder.getOutputBuffers();
                    }
                } else if (dequeueOutputBuffer == -2) {
                    synchronized (this) {
                        this.newFormat = this.mAudioEncoder.getOutputFormat();
                        if (this.mMuxerRef != null && (mp4MediaMuxer2 = this.mMuxerRef.get()) != null) {
                            mp4MediaMuxer2.addTrack(this.newFormat, false);
                        }
                    }
                } else {
                    if ((bufferInfo.flags & 2) != 0) {
                        bufferInfo.size = 0;
                    }
                    if ((bufferInfo.flags & 4) != 0) {
                        return;
                    }
                    ByteBuffer allocate = ByteBuffer.allocate(10240);
                    if (!isLollipop()) {
                        outputBuffer = outputBuffers[dequeueOutputBuffer];
                    } else {
                        outputBuffer = this.mAudioEncoder.getOutputBuffer(dequeueOutputBuffer);
                    }
                    if (bufferInfo.size != 0) {
                        if (outputBuffer == null) {
                            throw new RuntimeException("encodecOutputBuffer" + dequeueOutputBuffer + "was null");
                        }
                        WeakReference<Mp4MediaMuxer> weakReference = this.mMuxerRef;
                        if (weakReference != null && (mp4MediaMuxer = weakReference.get()) != null) {
                            mp4MediaMuxer.pumpStream(outputBuffer, bufferInfo, false);
                        }
                        allocate.clear();
                        outputBuffer.get(allocate.array(), 7, bufferInfo.size);
                        outputBuffer.clear();
                        allocate.position(bufferInfo.size + 7);
                        addADTStoPacket(allocate.array(), bufferInfo.size + 7);
                        allocate.flip();
                        if (this.listener != null) {
                            Log.i(TAG, "----->得到aac数据流<-----");
                            this.listener.onEncodeResult(allocate.array(), 0, bufferInfo.size + 7, bufferInfo.presentationTimeUs / TIMES_OUT);
                        }
                    }
                    this.mAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                }
            }
        } while (dequeueOutputBuffer >= 0);
    }

    private void initAudioRecord() {
        Process.setThreadPriority(-16);
        int minBufferSize = AudioRecord.getMinBufferSize(8000, 16, 2);
        for (int i : AUDIO_SOURCES) {
            try {
                AudioRecord audioRecord = new AudioRecord(i, 8000, 16, 2, minBufferSize);
                this.mAudioRecord = audioRecord;
                if (audioRecord != null && audioRecord.getState() != 1) {
                    this.mAudioRecord.release();
                    this.mAudioRecord = null;
                }
            } catch (Exception unused) {
                this.mAudioRecord = null;
            }
            if (this.mAudioRecord != null) {
                break;
            }
        }
        this.mAudioRecord.startRecording();
    }

    private void initMediaCodec() {
        MediaCodecInfo selectSupportCodec = selectSupportCodec("audio/mp4a-latm");
        if (selectSupportCodec == null) {
            Log.e(TAG, "编码器不支持audio/mp4a-latm类型");
            return;
        }
        try {
            this.mAudioEncoder = MediaCodec.createByCodecName(selectSupportCodec.getName());
        } catch (IOException e) {
            Log.e(TAG, "创建编码器失败" + e.getMessage());
            e.printStackTrace();
        }
        MediaFormat mediaFormat = new MediaFormat();
        mediaFormat.setString(IMediaFormat.KEY_MIME, "audio/mp4a-latm");
        mediaFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, 16000);
        mediaFormat.setInteger("channel-count", 1);
        mediaFormat.setInteger("sample-rate", 8000);
        mediaFormat.setInteger("aac-profile", 2);
        mediaFormat.setInteger("max-input-size", BUFFER_SIZE);
        this.mAudioEncoder.configure(mediaFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mAudioEncoder.start();
        this.isEncoderStart = true;
    }

    private void stopAudioRecord() {
        AudioRecord audioRecord = this.mAudioRecord;
        if (audioRecord != null) {
            audioRecord.stop();
            this.mAudioRecord.release();
            this.mAudioRecord = null;
        }
    }

    private void stopMediaCodec() {
        MediaCodec mediaCodec = this.mAudioEncoder;
        if (mediaCodec != null) {
            mediaCodec.stop();
            this.mAudioEncoder.release();
            this.mAudioEncoder = null;
        }
        this.isEncoderStart = false;
    }

    private boolean isLollipop() {
        return Build.VERSION.SDK_INT >= 21;
    }

    private boolean isKITKAT() {
        return Build.VERSION.SDK_INT <= 19;
    }

    private long getPTSUs() {
        long nanoTime = System.nanoTime() / TIMES_OUT;
        long j = this.prevPresentationTimes;
        return nanoTime < j ? nanoTime + (j - nanoTime) : nanoTime;
    }

    private MediaCodecInfo selectSupportCodec(String str) {
        int codecCount = MediaCodecList.getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                for (String str2 : codecInfoAt.getSupportedTypes()) {
                    if (str2.equalsIgnoreCase(str)) {
                        return codecInfoAt;
                    }
                }
            }
        }
        return null;
    }

    private void addADTStoPacket(byte[] bArr, int i) {
        bArr[0] = -1;
        bArr[1] = -15;
        bArr[2] = (byte) ((this.mSamplingRateIndex << 2) + 64 + 0);
        bArr[3] = (byte) ((i >> 11) + 64);
        bArr[4] = (byte) ((i & 2047) >> 3);
        bArr[5] = (byte) (((i & 7) << 5) + 31);
        bArr[6] = -4;
    }

    private short[] transferByte2Short(byte[] bArr, int i) {
        int i2 = i / 2;
        ShortBuffer asShortBuffer = ByteBuffer.wrap(bArr, 0, i).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        short[] sArr = new short[i2];
        asShortBuffer.get(sArr, 0, i2);
        return sArr;
    }
}
