package com.serenegiant.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.util.Log;
import android.view.View;
import com.serenegiant.common.R;

/* loaded from: classes2.dex */
public class ViewAnimationHelper {
    public static final int ANIMATION_FADE_IN = 1;
    public static final int ANIMATION_FADE_OUT = 0;
    public static final int ANIMATION_UNKNOWN = -1;
    public static final int ANIMATION_ZOOM_IN = 2;
    public static final int ANIMATION_ZOOM_OUT = 3;
    private static final long DEFAULT_DURATION_MS = 500;
    private static final String TAG = ViewAnimationHelper.class.getSimpleName();
    private static final Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() { // from class: com.serenegiant.utils.ViewAnimationHelper.5
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            ViewAnimationHelper.onAnimation(animator, 0);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ViewAnimationHelper.onAnimation(animator, 1);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            ViewAnimationHelper.onAnimation(animator, 2);
        }
    };

    public interface ViewAnimationListener {
        void onAnimationCancel(Animator animator, View view, int i);

        void onAnimationEnd(Animator animator, View view, int i);

        void onAnimationStart(Animator animator, View view, int i);
    }

    public static void fadeIn(final View view, final long j, final long j2, final ViewAnimationListener viewAnimationListener) {
        if (view == null) {
            return;
        }
        view.postDelayed(new Runnable() { // from class: com.serenegiant.utils.ViewAnimationHelper.1
            @Override // java.lang.Runnable
            public void run() {
                view.setVisibility(0);
                view.setTag(R.id.anim_type, 1);
                view.setTag(R.id.anim_listener, viewAnimationListener);
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
                view.setAlpha(0.0f);
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f);
                ofFloat.addListener(ViewAnimationHelper.mAnimatorListener);
                if (BuildCheck.isJellyBeanMR2()) {
                    ofFloat.setAutoCancel(true);
                }
                long j3 = j;
                if (j3 <= 0) {
                    j3 = ViewAnimationHelper.DEFAULT_DURATION_MS;
                }
                ofFloat.setDuration(j3);
                long j4 = j2;
                ofFloat.setStartDelay(j4 > 0 ? j4 : 0L);
                ofFloat.start();
            }
        }, 100L);
    }

    public static void fadeOut(final View view, final long j, final long j2, final ViewAnimationListener viewAnimationListener) {
        if (view == null || view.getVisibility() != 0) {
            return;
        }
        view.postDelayed(new Runnable() { // from class: com.serenegiant.utils.ViewAnimationHelper.2
            @Override // java.lang.Runnable
            public void run() {
                view.setTag(R.id.anim_type, 0);
                view.setTag(R.id.anim_listener, viewAnimationListener);
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
                view.setAlpha(1.0f);
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f);
                ofFloat.addListener(ViewAnimationHelper.mAnimatorListener);
                if (BuildCheck.isAndroid4_3()) {
                    ofFloat.setAutoCancel(true);
                }
                long j3 = j;
                if (j3 <= 0) {
                    j3 = ViewAnimationHelper.DEFAULT_DURATION_MS;
                }
                ofFloat.setDuration(j3);
                long j4 = j2;
                ofFloat.setStartDelay(j4 > 0 ? j4 : 0L);
                ofFloat.start();
            }
        }, 100L);
    }

    public static void zoomIn(final View view, final long j, final long j2, final ViewAnimationListener viewAnimationListener) {
        if (view == null) {
            return;
        }
        view.postDelayed(new Runnable() { // from class: com.serenegiant.utils.ViewAnimationHelper.3
            @Override // java.lang.Runnable
            public void run() {
                view.setVisibility(0);
                view.setTag(R.id.anim_type, 2);
                view.setTag(R.id.anim_listener, viewAnimationListener);
                view.setScaleX(0.0f);
                view.setScaleY(0.0f);
                view.setAlpha(1.0f);
                ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat("scaleX", 0.01f, 1.0f), PropertyValuesHolder.ofFloat("scaleY", 0.01f, 1.0f));
                ofPropertyValuesHolder.addListener(ViewAnimationHelper.mAnimatorListener);
                if (BuildCheck.isJellyBeanMR2()) {
                    ofPropertyValuesHolder.setAutoCancel(true);
                }
                long j3 = j;
                if (j3 <= 0) {
                    j3 = ViewAnimationHelper.DEFAULT_DURATION_MS;
                }
                ofPropertyValuesHolder.setDuration(j3);
                long j4 = j2;
                ofPropertyValuesHolder.setStartDelay(j4 > 0 ? j4 : 0L);
                ofPropertyValuesHolder.start();
            }
        }, 100L);
    }

    public static void zoomOut(final View view, final long j, final long j2, final ViewAnimationListener viewAnimationListener) {
        if (view == null) {
            return;
        }
        view.postDelayed(new Runnable() { // from class: com.serenegiant.utils.ViewAnimationHelper.4
            @Override // java.lang.Runnable
            public void run() {
                view.setVisibility(0);
                view.setTag(R.id.anim_type, 3);
                view.setTag(R.id.anim_listener, viewAnimationListener);
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
                view.setAlpha(1.0f);
                ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.0f), PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.0f));
                ofPropertyValuesHolder.addListener(ViewAnimationHelper.mAnimatorListener);
                if (BuildCheck.isJellyBeanMR2()) {
                    ofPropertyValuesHolder.setAutoCancel(true);
                }
                long j3 = j;
                if (j3 <= 0) {
                    j3 = ViewAnimationHelper.DEFAULT_DURATION_MS;
                }
                ofPropertyValuesHolder.setDuration(j3);
                long j4 = j2;
                ofPropertyValuesHolder.setStartDelay(j4 > 0 ? j4 : 0L);
                ofPropertyValuesHolder.start();
            }
        }, 100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onAnimation(Animator animator, final int i) {
        if (animator instanceof ObjectAnimator) {
            final ObjectAnimator objectAnimator = (ObjectAnimator) animator;
            final View view = (View) objectAnimator.getTarget();
            if (view == null) {
                return;
            }
            final ViewAnimationListener viewAnimationListener = (ViewAnimationListener) view.getTag(R.id.anim_listener);
            try {
                final int intValue = ((Integer) view.getTag(R.id.anim_type)).intValue();
                if (viewAnimationListener != null) {
                    view.postDelayed(new Runnable() { // from class: com.serenegiant.utils.ViewAnimationHelper.6
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                int i2 = i;
                                if (i2 == 0) {
                                    viewAnimationListener.onAnimationStart(objectAnimator, view, intValue);
                                } else if (i2 == 1) {
                                    viewAnimationListener.onAnimationEnd(objectAnimator, view, intValue);
                                } else if (i2 == 2) {
                                    viewAnimationListener.onAnimationCancel(objectAnimator, view, intValue);
                                }
                            } catch (Exception e) {
                                Log.w(ViewAnimationHelper.TAG, e);
                            }
                        }
                    }, 100L);
                }
            } catch (Exception unused) {
            }
        }
    }
}
