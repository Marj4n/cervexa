package com.serenegiant.widget;

/* loaded from: classes2.dex */
public interface IScaledView {
    public static final int SCALE_MODE_CROP = 2;
    public static final int SCALE_MODE_KEEP_ASPECT = 0;
    public static final int SCALE_MODE_STRETCH_TO_FIT = 1;

    int getScaleMode();

    void setScaleMode(int i);
}
