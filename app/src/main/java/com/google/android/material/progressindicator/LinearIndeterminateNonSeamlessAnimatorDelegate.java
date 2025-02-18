package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Property;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat;
import com.google.android.material.R;
import java.util.Arrays;

/* loaded from: classes.dex */
final class LinearIndeterminateNonSeamlessAnimatorDelegate extends IndeterminateAnimatorDelegate<AnimatorSet> {
    private static final Property<LinearIndeterminateNonSeamlessAnimatorDelegate, Float> LINE_1_HEAD_FRACTION = new Property<LinearIndeterminateNonSeamlessAnimatorDelegate, Float>(Float.class, "line1HeadFraction") { // from class: com.google.android.material.progressindicator.LinearIndeterminateNonSeamlessAnimatorDelegate.2
        @Override // android.util.Property
        public Float get(LinearIndeterminateNonSeamlessAnimatorDelegate linearIndeterminateNonSeamlessAnimatorDelegate) {
            return Float.valueOf(linearIndeterminateNonSeamlessAnimatorDelegate.getLine1HeadFraction());
        }

        @Override // android.util.Property
        public void set(LinearIndeterminateNonSeamlessAnimatorDelegate linearIndeterminateNonSeamlessAnimatorDelegate, Float f) {
            linearIndeterminateNonSeamlessAnimatorDelegate.setLine1HeadFraction(f.floatValue());
        }
    };
    private static final Property<LinearIndeterminateNonSeamlessAnimatorDelegate, Float> LINE_1_TAIL_FRACTION = new Property<LinearIndeterminateNonSeamlessAnimatorDelegate, Float>(Float.class, "line1TailFraction") { // from class: com.google.android.material.progressindicator.LinearIndeterminateNonSeamlessAnimatorDelegate.3
        @Override // android.util.Property
        public Float get(LinearIndeterminateNonSeamlessAnimatorDelegate linearIndeterminateNonSeamlessAnimatorDelegate) {
            return Float.valueOf(linearIndeterminateNonSeamlessAnimatorDelegate.getLine1TailFraction());
        }

        @Override // android.util.Property
        public void set(LinearIndeterminateNonSeamlessAnimatorDelegate linearIndeterminateNonSeamlessAnimatorDelegate, Float f) {
            linearIndeterminateNonSeamlessAnimatorDelegate.setLine1TailFraction(f.floatValue());
        }
    };
    private static final Property<LinearIndeterminateNonSeamlessAnimatorDelegate, Float> LINE_2_HEAD_FRACTION = new Property<LinearIndeterminateNonSeamlessAnimatorDelegate, Float>(Float.class, "line2HeadFraction") { // from class: com.google.android.material.progressindicator.LinearIndeterminateNonSeamlessAnimatorDelegate.4
        @Override // android.util.Property
        public Float get(LinearIndeterminateNonSeamlessAnimatorDelegate linearIndeterminateNonSeamlessAnimatorDelegate) {
            return Float.valueOf(linearIndeterminateNonSeamlessAnimatorDelegate.getLine2HeadFraction());
        }

        @Override // android.util.Property
        public void set(LinearIndeterminateNonSeamlessAnimatorDelegate linearIndeterminateNonSeamlessAnimatorDelegate, Float f) {
            linearIndeterminateNonSeamlessAnimatorDelegate.setLine2HeadFraction(f.floatValue());
        }
    };
    private static final Property<LinearIndeterminateNonSeamlessAnimatorDelegate, Float> LINE_2_TAIL_FRACTION = new Property<LinearIndeterminateNonSeamlessAnimatorDelegate, Float>(Float.class, "line2TailFraction") { // from class: com.google.android.material.progressindicator.LinearIndeterminateNonSeamlessAnimatorDelegate.5
        @Override // android.util.Property
        public Float get(LinearIndeterminateNonSeamlessAnimatorDelegate linearIndeterminateNonSeamlessAnimatorDelegate) {
            return Float.valueOf(linearIndeterminateNonSeamlessAnimatorDelegate.getLine2TailFraction());
        }

        @Override // android.util.Property
        public void set(LinearIndeterminateNonSeamlessAnimatorDelegate linearIndeterminateNonSeamlessAnimatorDelegate, Float f) {
            linearIndeterminateNonSeamlessAnimatorDelegate.setLine2TailFraction(f.floatValue());
        }
    };
    private static final int MAIN_LINE_1_HEAD_DURATION = 750;
    private static final int MAIN_LINE_1_TAIL_DELAY = 333;
    private static final int MAIN_LINE_1_TAIL_DURATION = 850;
    private static final int MAIN_LINE_2_HEAD_DELAY = 1000;
    private static final int MAIN_LINE_2_HEAD_DURATION = 567;
    private static final int MAIN_LINE_2_TAIL_DELAY = 1267;
    private static final int MAIN_LINE_2_TAIL_DURATION = 533;
    Animatable2Compat.AnimationCallback animatorCompleteCallback;
    boolean animatorCompleteEndRequested;
    private AnimatorSet animatorSet;
    private final Context context;
    private int displayedSegmentColorIndex;
    private float line1HeadFraction;
    private float line1TailFraction;
    private float line2HeadFraction;
    private float line2TailFraction;

    public LinearIndeterminateNonSeamlessAnimatorDelegate(Context context) {
        super(2);
        this.animatorCompleteEndRequested = false;
        this.animatorCompleteCallback = null;
        this.context = context;
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void startAnimator() {
        maybeInitializeAnimators();
        this.animatorSet.start();
    }

    private void maybeInitializeAnimators() {
        if (this.animatorSet == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, LINE_1_HEAD_FRACTION, 0.0f, 1.0f);
            ofFloat.setDuration(750L);
            ofFloat.setInterpolator(AnimationUtilsCompat.loadInterpolator(this.context, R.animator.linear_indeterminate_line1_head_interpolator));
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, LINE_1_TAIL_FRACTION, 0.0f, 1.0f);
            ofFloat2.setStartDelay(333L);
            ofFloat2.setDuration(850L);
            ofFloat2.setInterpolator(AnimationUtilsCompat.loadInterpolator(this.context, R.animator.linear_indeterminate_line1_tail_interpolator));
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this, LINE_2_HEAD_FRACTION, 0.0f, 1.0f);
            ofFloat3.setStartDelay(1000L);
            ofFloat3.setDuration(567L);
            ofFloat3.setInterpolator(AnimationUtilsCompat.loadInterpolator(this.context, R.animator.linear_indeterminate_line2_head_interpolator));
            ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this, LINE_2_TAIL_FRACTION, 0.0f, 1.0f);
            ofFloat4.setStartDelay(1267L);
            ofFloat4.setDuration(533L);
            ofFloat4.setInterpolator(AnimationUtilsCompat.loadInterpolator(this.context, R.animator.linear_indeterminate_line2_tail_interpolator));
            AnimatorSet animatorSet = new AnimatorSet();
            this.animatorSet = animatorSet;
            animatorSet.playTogether(ofFloat, ofFloat2, ofFloat3, ofFloat4);
            this.animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.progressindicator.LinearIndeterminateNonSeamlessAnimatorDelegate.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (LinearIndeterminateNonSeamlessAnimatorDelegate.this.animatorCompleteEndRequested) {
                        LinearIndeterminateNonSeamlessAnimatorDelegate.this.animatorCompleteEndRequested = false;
                        LinearIndeterminateNonSeamlessAnimatorDelegate.this.animatorCompleteCallback.onAnimationEnd(LinearIndeterminateNonSeamlessAnimatorDelegate.this.drawable);
                        LinearIndeterminateNonSeamlessAnimatorDelegate.this.resetPropertiesForNewStart();
                    } else if (LinearIndeterminateNonSeamlessAnimatorDelegate.this.drawable.isVisible()) {
                        LinearIndeterminateNonSeamlessAnimatorDelegate.this.resetPropertiesForNextCycle();
                        LinearIndeterminateNonSeamlessAnimatorDelegate.this.startAnimator();
                    } else {
                        LinearIndeterminateNonSeamlessAnimatorDelegate.this.resetPropertiesForNewStart();
                    }
                }
            });
        }
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void resetPropertiesForNewStart() {
        resetPropertiesForNextCycle();
        resetSegmentColors();
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void resetPropertiesForNextCycle() {
        setLine1HeadFraction(0.0f);
        setLine1TailFraction(0.0f);
        setLine2HeadFraction(0.0f);
        setLine2TailFraction(0.0f);
        rotateSegmentColors();
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void cancelAnimatorImmediately() {
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void requestCancelAnimatorAfterCurrentCycle() {
        if (this.animatorCompleteEndRequested) {
            return;
        }
        if (!this.drawable.isVisible()) {
            cancelAnimatorImmediately();
        } else {
            this.animatorCompleteEndRequested = true;
        }
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void invalidateSpecValues() {
        resetSegmentColors();
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void registerAnimatorsCompleteCallback(Animatable2Compat.AnimationCallback animationCallback) {
        this.animatorCompleteCallback = animationCallback;
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void unregisterAnimatorsCompleteCallback() {
        this.animatorCompleteCallback = null;
    }

    private void rotateSegmentColors() {
        this.displayedSegmentColorIndex = (this.displayedSegmentColorIndex + 1) % this.drawable.combinedIndicatorColorArray.length;
        Arrays.fill(this.segmentColors, this.drawable.combinedIndicatorColorArray[this.displayedSegmentColorIndex]);
    }

    private void resetSegmentColors() {
        this.displayedSegmentColorIndex = 0;
        Arrays.fill(this.segmentColors, this.drawable.combinedIndicatorColorArray[this.displayedSegmentColorIndex]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getLine1HeadFraction() {
        return this.line1HeadFraction;
    }

    void setLine1HeadFraction(float f) {
        this.line1HeadFraction = f;
        this.segmentPositions[3] = f;
        this.drawable.invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getLine1TailFraction() {
        return this.line1TailFraction;
    }

    void setLine1TailFraction(float f) {
        this.line1TailFraction = f;
        this.segmentPositions[2] = f;
        this.drawable.invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getLine2HeadFraction() {
        return this.line2HeadFraction;
    }

    void setLine2HeadFraction(float f) {
        this.line2HeadFraction = f;
        this.segmentPositions[1] = f;
        this.drawable.invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getLine2TailFraction() {
        return this.line2TailFraction;
    }

    void setLine2TailFraction(float f) {
        this.line2TailFraction = f;
        this.segmentPositions[0] = f;
        this.drawable.invalidateSelf();
    }
}
