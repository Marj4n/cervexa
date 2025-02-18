package com.github.mikephil.charting.animation;

import android.animation.TimeInterpolator;

/* loaded from: classes.dex */
public interface EasingFunction extends TimeInterpolator {
    @Override // android.animation.TimeInterpolator
    float getInterpolation(float f);
}
