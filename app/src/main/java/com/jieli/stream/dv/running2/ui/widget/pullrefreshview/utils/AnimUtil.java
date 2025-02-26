package com.jieli.stream.dv.running2.ui.widget.pullrefreshview.utils;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import androidx.core.view.ViewCompat;

/* loaded from: classes.dex */
public class AnimUtil {
    public static void startRotation(View view, float f) {
        ObjectAnimator.ofFloat(view, "rotation", ViewCompat.getRotation(view), f).start();
    }

    public static void startRotation(View view, float f, long j, long j2) {
        ObjectAnimator duration = ObjectAnimator.ofFloat(view, "rotation", ViewCompat.getRotation(view), f).setDuration(j);
        duration.setStartDelay(j2);
        duration.start();
    }

    public static void startRotation(View view, float f, long j, long j2, int i) {
        ObjectAnimator duration = ObjectAnimator.ofFloat(view, "rotation", ViewCompat.getRotation(view), f).setDuration(j);
        duration.setStartDelay(j2);
        duration.setRepeatCount(i);
        duration.setInterpolator(new LinearInterpolator());
        duration.start();
    }

    public static void startShow(View view, float f, long j, long j2) {
        ViewCompat.setAlpha(view, f);
        view.setVisibility(0);
        ObjectAnimator duration = ObjectAnimator.ofFloat(view, "alpha", f, 1.0f).setDuration(j);
        duration.setStartDelay(j2);
        duration.start();
    }

    public static void startHide(View view, long j, long j2) {
        view.setVisibility(0);
        ObjectAnimator duration = ObjectAnimator.ofFloat(view, "alpha", ViewCompat.getAlpha(view), 0.0f).setDuration(j);
        duration.setStartDelay(j2);
        duration.start();
    }

    public static void startShow(View view, float f) {
        ViewCompat.setAlpha(view, f);
        view.setVisibility(0);
        ObjectAnimator.ofFloat(view, "alpha", f, 1.0f).start();
    }

    public static void startHide(View view) {
        view.setVisibility(0);
        ObjectAnimator.ofFloat(view, "alpha", ViewCompat.getAlpha(view), 0.0f).start();
    }

    public static void startScale(View view, float f) {
        ObjectAnimator.ofFloat(view, "scaleX", ViewCompat.getScaleX(view), f).start();
        ObjectAnimator.ofFloat(view, "scaleY", ViewCompat.getScaleY(view), f).start();
    }

    public static void startScale(View view, float f, long j, long j2, Interpolator interpolator) {
        ObjectAnimator duration = ObjectAnimator.ofFloat(view, "scaleX", ViewCompat.getScaleX(view), f).setDuration(j);
        duration.setStartDelay(j2);
        duration.setInterpolator(interpolator);
        duration.start();
        ObjectAnimator duration2 = ObjectAnimator.ofFloat(view, "scaleY", ViewCompat.getScaleY(view), f).setDuration(j);
        duration2.setStartDelay(j2);
        duration2.setInterpolator(interpolator);
        duration2.start();
    }

    public static void startScale(View view, float f, float f2) {
        ObjectAnimator.ofFloat(view, "scaleX", f, f2).start();
        ObjectAnimator.ofFloat(view, "scaleY", f, f2).start();
    }

    public static void startScale(View view, float f, float f2, long j, long j2, Interpolator interpolator) {
        ObjectAnimator duration = ObjectAnimator.ofFloat(view, "scaleX", f, f2).setDuration(j);
        duration.setStartDelay(j2);
        duration.setInterpolator(interpolator);
        duration.start();
        ObjectAnimator duration2 = ObjectAnimator.ofFloat(view, "scaleY", f, f2).setDuration(j);
        duration2.setStartDelay(j2);
        duration2.setInterpolator(interpolator);
        duration2.start();
    }

    public static void startFromY(View view, float f) {
        ObjectAnimator.ofFloat(view, "translationY", f, 0.0f).start();
    }

    public static void startToY(View view, float f) {
        ObjectAnimator.ofFloat(view, "translationY", 0.0f, f).start();
    }
}
