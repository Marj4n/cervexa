package com.generalplus.ffmpegLib;

/* loaded from: classes.dex */
public class ffDecodeFrame {
    public static final int FFDECODE_FORMAT_YUV420P = 0;
    public static final int FFDECODE_FORMAT_YUV422P = 1;
    public static final int FFDECODE_FORMAT_YUV444P = 2;
    public static final int FFDECODE_FORMAT_YUVJ420P = 3;
    public static final int FFDECODE_FORMAT_YUVJ422P = 4;
    public static final int FFDECODE_FORMAT_YUVJ444P = 5;
    public byte[][] data;
    public int format;
    public int height;
    public int[] linesize;
    public int width;

    public ffDecodeFrame(byte[][] bArr, int[] iArr, int i, int i2, int i3) {
        this.data = bArr;
        this.linesize = iArr;
        this.width = i;
        this.height = i2;
        this.format = i3;
    }
}
