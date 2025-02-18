package com.shizhefei.view.indicator.slidebar;

import android.view.View;

/* loaded from: classes2.dex */
public interface ScrollBar {

    public enum Gravity {
        TOP,
        TOP_FLOAT,
        BOTTOM,
        BOTTOM_FLOAT,
        CENTENT,
        CENTENT_BACKGROUND
    }

    Gravity getGravity();

    int getHeight(int i);

    View getSlideView();

    int getWidth(int i);

    void onPageScrolled(int i, float f, int i2);
}
