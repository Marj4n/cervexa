package com.serenegiant.media;

import android.media.MediaFormat;
import com.serenegiant.common.BuildConfig;
import java.nio.ByteBuffer;
import java.util.HashMap;
import tv.danmaku.ijk.media.player.IjkMediaMeta;
import tv.danmaku.ijk.media.player.misc.IMediaFormat;

/* loaded from: classes2.dex */
public final class VideoMuxer implements IMuxer {
    private static boolean isLoaded = false;
    private volatile boolean mIsStarted;
    private int mLastTrackIndex = -1;
    protected long mNativePtr;
    private boolean mReleased;

    private static final native int nativeAddTrack(long j, String[] strArr, Object[] objArr);

    private final native long nativeCreate(String str);

    private final native long nativeCreateFromFD(int i);

    private final native void nativeDestroy(long j);

    private static final native int nativeStart(long j);

    private static final native int nativeStop(long j);

    private static final native int nativeWriteSampleData(long j, int i, ByteBuffer byteBuffer, int i2, int i3, long j2, int i4);

    static {
        if (0 == 0) {
            System.loadLibrary(BuildConfig.STL_NAME);
            System.loadLibrary("jpeg-turbo2000");
            System.loadLibrary("png16");
            System.loadLibrary("common");
            System.loadLibrary("mediaencoder");
            isLoaded = true;
        }
    }

    public VideoMuxer(String str) {
        this.mNativePtr = nativeCreate(str);
    }

    public VideoMuxer(int i) {
        this.mNativePtr = nativeCreateFromFD(i);
    }

    @Override // com.serenegiant.media.IMuxer
    public void release() {
        long j = this.mNativePtr;
        if (j != 0) {
            this.mReleased = true;
            nativeDestroy(j);
            this.mNativePtr = 0L;
        }
    }

    protected void finalize() throws Throwable {
        release();
        super.finalize();
    }

    @Override // com.serenegiant.media.IMuxer
    public int addTrack(MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            throw new IllegalArgumentException("format must not be null.");
        }
        HashMap hashMap = new HashMap();
        if (mediaFormat.containsKey(IMediaFormat.KEY_MIME)) {
            hashMap.put(IMediaFormat.KEY_MIME, mediaFormat.getString(IMediaFormat.KEY_MIME));
        }
        if (mediaFormat.containsKey("width")) {
            hashMap.put("width", Integer.valueOf(mediaFormat.getInteger("width")));
        }
        if (mediaFormat.containsKey("height")) {
            hashMap.put("height", Integer.valueOf(mediaFormat.getInteger("height")));
        }
        if (mediaFormat.containsKey(IjkMediaMeta.IJKM_KEY_BITRATE)) {
            hashMap.put(IjkMediaMeta.IJKM_KEY_BITRATE, Integer.valueOf(mediaFormat.getInteger(IjkMediaMeta.IJKM_KEY_BITRATE)));
        }
        if (mediaFormat.containsKey("color-format")) {
            hashMap.put("color-format", Integer.valueOf(mediaFormat.getInteger("color-format")));
        }
        if (mediaFormat.containsKey("frame-rate")) {
            hashMap.put("frame-rate", Integer.valueOf(mediaFormat.getInteger("frame-rate")));
        }
        if (mediaFormat.containsKey("i-frame-interval")) {
            hashMap.put("i-frame-interval", Integer.valueOf(mediaFormat.getInteger("i-frame-interval")));
        }
        if (mediaFormat.containsKey("max-input-size")) {
            hashMap.put("max-input-size", Integer.valueOf(mediaFormat.getInteger("max-input-size")));
        }
        if (mediaFormat.containsKey("durationUs")) {
            hashMap.put("durationUs", Integer.valueOf(mediaFormat.getInteger("durationUs")));
        }
        if (mediaFormat.containsKey("what")) {
            hashMap.put("what", Integer.valueOf(mediaFormat.getInteger("what")));
        }
        if (mediaFormat.containsKey("csd-0")) {
            hashMap.put("csd-0", mediaFormat.getByteBuffer("csd-0"));
        }
        if (mediaFormat.containsKey("csd-1")) {
            hashMap.put("csd-1", mediaFormat.getByteBuffer("csd-1"));
        }
        Object[] array = hashMap.keySet().toArray();
        int length = array.length;
        String[] strArr = new String[length];
        for (int i = 0; i < length; i++) {
            strArr[i] = (String) array[i];
        }
        int nativeAddTrack = nativeAddTrack(this.mNativePtr, strArr, hashMap.values().toArray());
        if (this.mLastTrackIndex >= nativeAddTrack) {
            throw new IllegalArgumentException("Invalid format.");
        }
        this.mLastTrackIndex = nativeAddTrack;
        return nativeAddTrack;
    }

    @Override // com.serenegiant.media.IMuxer
    public void start() {
        long j = this.mNativePtr;
        if ((j != 0 ? nativeStart(j) : -1) != 0) {
            throw new IllegalStateException("failed to start muxer");
        }
        this.mIsStarted = true;
    }

    @Override // com.serenegiant.media.IMuxer
    public void stop() {
        this.mIsStarted = false;
        long j = this.mNativePtr;
        if (j != 0 && nativeStop(j) != 0) {
            throw new RuntimeException("failed to stop muxer");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x004a A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x001f  */
    @Override // com.serenegiant.media.IMuxer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void writeSampleData(int r12, ByteBuffer r13, android.media.MediaCodec.BufferInfo r14) {
        /*
            r11 = this;
            boolean r0 = r11.mReleased
            r1 = 1
            if (r0 != 0) goto L1c
            long r2 = r11.mNativePtr
            r4 = 0
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 == 0) goto L1c
            int r6 = r14.offset
            int r7 = r14.size
            long r8 = r14.presentationTimeUs
            int r10 = r14.flags
            r4 = r12
            r5 = r13
            int r12 = nativeWriteSampleData(r2, r4, r5, r6, r7, r8, r10)
            goto L1d
        L1c:
            r12 = 1
        L1d:
            if (r12 == 0) goto L4a
            r13 = -2000(0xfffffffffffff830, float:NaN)
            if (r12 == r13) goto L44
            if (r12 == r1) goto L3c
            java.lang.IllegalArgumentException r13 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r0 = "writeSampleData:err="
            r14.append(r0)
            r14.append(r12)
            java.lang.String r12 = r14.toString()
            r13.<init>(r12)
            throw r13
        L3c:
            java.lang.IllegalStateException r12 = new java.lang.IllegalStateException
            java.lang.String r13 = "muxer already released."
            r12.<init>(r13)
            throw r12
        L44:
            com.serenegiant.media.TimeoutException r12 = new com.serenegiant.media.TimeoutException
            r12.<init>()
            throw r12
        L4a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.media.VideoMuxer.writeSampleData(int, java.nio.ByteBuffer, android.media.MediaCodec$BufferInfo):void");
    }

    @Override // com.serenegiant.media.IMuxer
    public boolean isStarted() {
        return this.mIsStarted && !this.mReleased;
    }
}
