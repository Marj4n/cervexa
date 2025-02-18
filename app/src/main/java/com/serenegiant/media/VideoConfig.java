package com.serenegiant.media;

import android.os.Build;

/* loaded from: classes2.dex */
public class VideoConfig {
    private static float BPP = 0.25f;
    public static float BPP_MAX = 0.3f;
    public static final float BPP_MIN = 0.01f;
    public static final int FPS_MAX = 121;
    public static final int FPS_MIN = 2;
    private static float IFI = 10.0f * 30.0f;
    private static float IFRAME_INTERVAL = 10.0f;
    private static final int IFRAME_MAX = 30;
    private static final int IFRAME_MIN = 1;
    private static int captureFps = 15;
    public static long maxDuration = 30000;
    public static int maxRepeats = 1;
    public static long repeatInterval = 60000;
    public static boolean sIsSurfaceCapture;
    public static boolean sUseMediaMuxer;

    public static float calcBPP(int i, int i2, int i3, int i4) {
        return i4 / ((i3 * i) * i2);
    }

    static {
        int i = Build.VERSION.SDK_INT;
        sUseMediaMuxer = false;
        int i2 = Build.VERSION.SDK_INT;
        sIsSurfaceCapture = false;
    }

    public static void setCaptureFps(int i) {
        if (i > 121) {
            i = 121;
        } else if (i < 2) {
            i = 2;
        }
        captureFps = i;
    }

    public static int getCaptureFps() {
        int i = captureFps;
        if (i > 121) {
            return 121;
        }
        if (i < 2) {
            return 2;
        }
        return i;
    }

    public static void setIFrame(float f) {
        IFRAME_INTERVAL = f;
        IFI = f * 30.0f;
    }

    public static final int getIFrame() {
        float f;
        if (getCaptureFps() < 2) {
            f = 1.0f;
        } else {
            try {
                f = (float) Math.ceil(IFI / r0);
            } catch (Exception unused) {
                f = IFRAME_INTERVAL;
            }
        }
        int i = (int) f;
        return (int) (i >= 1 ? i > 30 ? 30.0f : f : 1.0f);
    }

    public static int calcBitrate(int i, int i2, int i3, float f) {
        int floor = ((int) (Math.floor(((((f * i3) * i) * i2) / 1000.0f) / 100.0f) * 100.0d)) * 1000;
        if (floor < 200000) {
            return 200000;
        }
        if (floor > 20000000) {
            return 20000000;
        }
        return floor;
    }

    public static int getBitrate(int i, int i2) {
        return calcBitrate(i, i2, getCaptureFps(), BPP);
    }

    public static int getBitrate(int i, int i2, int i3) {
        return calcBitrate(i, i2, i3, BPP);
    }

    public static int getBitrate(int i, int i2, int i3, float f) {
        return calcBitrate(i, i2, i3, f);
    }

    public static float calcBPP(int i, int i2, int i3) {
        return calcBPP(i, i2, getCaptureFps(), i3);
    }

    public static void setBPP(int i, int i2, int i3) throws IllegalArgumentException {
        setBPP(calcBPP(i, i2, i3));
    }

    public static void setBPP(float f) throws IllegalArgumentException {
        if (f < 0.01f || f > BPP_MAX) {
            throw new IllegalArgumentException("bpp should be within [BPP_MIN, BPP_MAX]");
        }
        BPP = f;
    }

    public static float bpp() {
        return BPP;
    }

    public static int getSizeRate(int i, int i2) {
        return (getBitrate(i, i2) * 60) / 8;
    }
}
