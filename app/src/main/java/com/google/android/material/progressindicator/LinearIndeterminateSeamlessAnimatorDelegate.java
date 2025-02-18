package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Property;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.math.MathUtils;

/* loaded from: classes.dex */
final class LinearIndeterminateSeamlessAnimatorDelegate extends IndeterminateAnimatorDelegate<AnimatorSet> {
    private static final int DURATION_PER_COLOR = 667;
    private static final Property<LinearIndeterminateSeamlessAnimatorDelegate, Float> LINE_CONNECT_POINT_1_FRACTION = new Property<LinearIndeterminateSeamlessAnimatorDelegate, Float>(Float.class, "lineConnectPoint1Fraction") { // from class: com.google.android.material.progressindicator.LinearIndeterminateSeamlessAnimatorDelegate.3
        @Override // android.util.Property
        public Float get(LinearIndeterminateSeamlessAnimatorDelegate linearIndeterminateSeamlessAnimatorDelegate) {
            return Float.valueOf(linearIndeterminateSeamlessAnimatorDelegate.getLineConnectPoint1Fraction());
        }

        @Override // android.util.Property
        public void set(LinearIndeterminateSeamlessAnimatorDelegate linearIndeterminateSeamlessAnimatorDelegate, Float f) {
            linearIndeterminateSeamlessAnimatorDelegate.setLineConnectPoint1Fraction(f.floatValue());
        }
    };
    private static final Property<LinearIndeterminateSeamlessAnimatorDelegate, Float> LINE_CONNECT_POINT_2_FRACTION = new Property<LinearIndeterminateSeamlessAnimatorDelegate, Float>(Float.class, "lineConnectPoint2Fraction") { // from class: com.google.android.material.progressindicator.LinearIndeterminateSeamlessAnimatorDelegate.4
        @Override // android.util.Property
        public Float get(LinearIndeterminateSeamlessAnimatorDelegate linearIndeterminateSeamlessAnimatorDelegate) {
            return Float.valueOf(linearIndeterminateSeamlessAnimatorDelegate.getLineConnectPoint2Fraction());
        }

        @Override // android.util.Property
        public void set(LinearIndeterminateSeamlessAnimatorDelegate linearIndeterminateSeamlessAnimatorDelegate, Float f) {
            linearIndeterminateSeamlessAnimatorDelegate.setLineConnectPoint2Fraction(f.floatValue());
        }
    };
    private static final int NEXT_COLOR_DELAY = 333;
    private AnimatorSet animatorSet;
    private float lineConnectPoint1Fraction;
    private float lineConnectPoint2Fraction;
    private int referenceSegmentColorIndex;

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void registerAnimatorsCompleteCallback(Animatable2Compat.AnimationCallback animationCallback) {
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void requestCancelAnimatorAfterCurrentCycle() {
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void resetPropertiesForNextCycle() {
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void unregisterAnimatorsCompleteCallback() {
    }

    public LinearIndeterminateSeamlessAnimatorDelegate() {
        super(3);
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void startAnimator() {
        maybeInitializeAnimators();
        this.animatorSet.start();
    }

    private void maybeInitializeAnimators() {
        if (this.animatorSet == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, LINE_CONNECT_POINT_1_FRACTION, 0.0f, 1.0f);
            ofFloat.setDuration(667L);
            ofFloat.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            ofFloat.setRepeatCount(-1);
            ofFloat.setRepeatMode(1);
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.progressindicator.LinearIndeterminateSeamlessAnimatorDelegate.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                    super.onAnimationRepeat(animator);
                    if (LinearIndeterminateSeamlessAnimatorDelegate.this.getLineConnectPoint2Fraction() <= 0.0f || LinearIndeterminateSeamlessAnimatorDelegate.this.getLineConnectPoint2Fraction() >= 1.0f) {
                        return;
                    }
                    LinearIndeterminateSeamlessAnimatorDelegate.this.shiftSegmentColors();
                }
            });
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, LINE_CONNECT_POINT_2_FRACTION, 0.0f, 0.0f);
            ofFloat2.setDuration(333L);
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this, LINE_CONNECT_POINT_2_FRACTION, 0.0f, 1.0f);
            ofFloat3.setDuration(667L);
            ofFloat3.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            ofFloat3.setRepeatCount(-1);
            ofFloat3.setRepeatMode(1);
            ofFloat3.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.progressindicator.LinearIndeterminateSeamlessAnimatorDelegate.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                    super.onAnimationRepeat(animator);
                    if (LinearIndeterminateSeamlessAnimatorDelegate.this.getLineConnectPoint1Fraction() <= 0.0f || LinearIndeterminateSeamlessAnimatorDelegate.this.getLineConnectPoint1Fraction() >= 1.0f) {
                        return;
                    }
                    LinearIndeterminateSeamlessAnimatorDelegate.this.shiftSegmentColors();
                }
            });
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playSequentially(ofFloat2, ofFloat3);
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.animatorSet = animatorSet2;
            animatorSet2.playTogether(ofFloat, animatorSet);
        }
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void resetPropertiesForNewStart() {
        setLineConnectPoint1Fraction(0.0f);
        setLineConnectPoint2Fraction(0.0f);
        resetSegmentColors();
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void cancelAnimatorImmediately() {
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void invalidateSpecValues() {
        resetSegmentColors();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shiftSegmentColors() {
        this.referenceSegmentColorIndex = (this.referenceSegmentColorIndex + 1) % this.drawable.combinedIndicatorColorArray.length;
        updateSegmentColors();
    }

    private void resetSegmentColors() {
        this.referenceSegmentColorIndex = 0;
        updateSegmentColors();
    }

    private void updateSegmentColors() {
        int floorMod = MathUtils.floorMod(this.referenceSegmentColorIndex + 2, this.drawable.combinedIndicatorColorArray.length);
        int floorMod2 = MathUtils.floorMod(this.referenceSegmentColorIndex + 1, this.drawable.combinedIndicatorColorArray.length);
        this.segmentColors[0] = this.drawable.combinedIndicatorColorArray[floorMod];
        this.segmentColors[1] = this.drawable.combinedIndicatorColorArray[floorMod2];
        this.segmentColors[2] = this.drawable.combinedIndicatorColorArray[this.referenceSegmentColorIndex];
    }

    private void updateSegmentPositions() {
        this.segmentPositions[0] = 0.0f;
        float[] fArr = this.segmentPositions;
        float[] fArr2 = this.segmentPositions;
        float min = Math.min(getLineConnectPoint1Fraction(), getLineConnectPoint2Fraction());
        fArr2[2] = min;
        fArr[1] = min;
        float[] fArr3 = this.segmentPositions;
        float[] fArr4 = this.segmentPositions;
        float max = Math.max(getLineConnectPoint1Fraction(), getLineConnectPoint2Fraction());
        fArr4[4] = max;
        fArr3[3] = max;
        this.segmentPositions[5] = 1.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getLineConnectPoint1Fraction() {
        return this.lineConnectPoint1Fraction;
    }

    void setLineConnectPoint1Fraction(float f) {
        this.lineConnectPoint1Fraction = f;
        updateSegmentPositions();
        this.drawable.invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getLineConnectPoint2Fraction() {
        return this.lineConnectPoint2Fraction;
    }

    void setLineConnectPoint2Fraction(float f) {
        this.lineConnectPoint2Fraction = f;
        updateSegmentPositions();
        this.drawable.invalidateSelf();
    }
}
