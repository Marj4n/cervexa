package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.util.Property;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.ArgbEvaluatorCompat;
import com.google.android.material.math.MathUtils;

/* loaded from: classes.dex */
final class CircularIndeterminateAnimatorDelegate extends IndeterminateAnimatorDelegate<AnimatorSet> {
    private static final int COLOR_FADING_DELAY = 1000;
    private static final int COLOR_FADING_DURATION = 333;
    private static final int DURATION_PER_COLOR_IN_MS = 1333;
    private static final float INDICATOR_DELTA_DEGREES = 250.0f;
    private static final float INDICATOR_MAX_DEGREES = 270.0f;
    private static final float INDICATOR_MIN_DEGREES = 20.0f;
    private static final float INDICATOR_OFFSET_PER_COLOR_DEGREES = 360.0f;
    Animatable2Compat.AnimationCallback animatorCompleteCallback;
    boolean animatorCompleteEndRequested;
    private AnimatorSet animatorSet;
    private ObjectAnimator colorFadingAnimator;
    private int displayedIndicatorColor;
    private ObjectAnimator indicatorCollapsingAnimator;
    private int indicatorColorIndex;
    private float indicatorHeadChangeFraction;
    private float indicatorInCycleOffset;
    private float indicatorStartOffset;
    private float indicatorTailChangeFraction;
    private static final Property<CircularIndeterminateAnimatorDelegate, Integer> DISPLAYED_INDICATOR_COLOR = new Property<CircularIndeterminateAnimatorDelegate, Integer>(Integer.class, "displayedIndicatorColor") { // from class: com.google.android.material.progressindicator.CircularIndeterminateAnimatorDelegate.3
        @Override // android.util.Property
        public Integer get(CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate) {
            return Integer.valueOf(circularIndeterminateAnimatorDelegate.getDisplayedIndicatorColor());
        }

        @Override // android.util.Property
        public void set(CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate, Integer num) {
            circularIndeterminateAnimatorDelegate.setDisplayedIndicatorColor(num.intValue());
        }
    };
    private static final Property<CircularIndeterminateAnimatorDelegate, Float> INDICATOR_IN_CYCLE_OFFSET = new Property<CircularIndeterminateAnimatorDelegate, Float>(Float.class, "indicatorInCycleOffset") { // from class: com.google.android.material.progressindicator.CircularIndeterminateAnimatorDelegate.4
        @Override // android.util.Property
        public Float get(CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate) {
            return Float.valueOf(circularIndeterminateAnimatorDelegate.getIndicatorInCycleOffset());
        }

        @Override // android.util.Property
        public void set(CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate, Float f) {
            circularIndeterminateAnimatorDelegate.setIndicatorInCycleOffset(f.floatValue());
        }
    };
    private static final Property<CircularIndeterminateAnimatorDelegate, Float> INDICATOR_HEAD_CHANGE_FRACTION = new Property<CircularIndeterminateAnimatorDelegate, Float>(Float.class, "indicatorHeadChangeFraction") { // from class: com.google.android.material.progressindicator.CircularIndeterminateAnimatorDelegate.5
        @Override // android.util.Property
        public Float get(CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate) {
            return Float.valueOf(circularIndeterminateAnimatorDelegate.getIndicatorHeadChangeFraction());
        }

        @Override // android.util.Property
        public void set(CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate, Float f) {
            circularIndeterminateAnimatorDelegate.setIndicatorHeadChangeFraction(f.floatValue());
        }
    };
    private static final Property<CircularIndeterminateAnimatorDelegate, Float> INDICATOR_TAIL_CHANGE_FRACTION = new Property<CircularIndeterminateAnimatorDelegate, Float>(Float.class, "indicatorTailChangeFraction") { // from class: com.google.android.material.progressindicator.CircularIndeterminateAnimatorDelegate.6
        @Override // android.util.Property
        public Float get(CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate) {
            return Float.valueOf(circularIndeterminateAnimatorDelegate.getIndicatorTailChangeFraction());
        }

        @Override // android.util.Property
        public void set(CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate, Float f) {
            circularIndeterminateAnimatorDelegate.setIndicatorTailChangeFraction(f.floatValue());
        }
    };

    public CircularIndeterminateAnimatorDelegate() {
        super(1);
        this.animatorCompleteEndRequested = false;
        this.animatorCompleteCallback = null;
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    protected void registerDrawable(IndeterminateDrawable indeterminateDrawable) {
        super.registerDrawable(indeterminateDrawable);
        ObjectAnimator ofObject = ObjectAnimator.ofObject(this, (Property<CircularIndeterminateAnimatorDelegate, V>) DISPLAYED_INDICATOR_COLOR, (TypeEvaluator) new ArgbEvaluatorCompat(), (Object[]) new Integer[]{Integer.valueOf(indeterminateDrawable.combinedIndicatorColorArray[this.indicatorColorIndex]), Integer.valueOf(indeterminateDrawable.combinedIndicatorColorArray[getNextIndicatorColorIndex()])});
        this.colorFadingAnimator = ofObject;
        ofObject.setDuration(333L);
        this.colorFadingAnimator.setStartDelay(1000L);
        this.colorFadingAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.playTogether(this.colorFadingAnimator);
        }
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    void startAnimator() {
        maybeInitializeAnimators();
        this.animatorSet.start();
    }

    private void maybeInitializeAnimators() {
        if (this.animatorSet == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, INDICATOR_IN_CYCLE_OFFSET, 0.0f, INDICATOR_OFFSET_PER_COLOR_DEGREES);
            ofFloat.setDuration(1333L);
            ofFloat.setInterpolator(null);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, INDICATOR_HEAD_CHANGE_FRACTION, 0.0f, 1.0f);
            ofFloat2.setDuration(666L);
            ofFloat2.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.progressindicator.CircularIndeterminateAnimatorDelegate.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (CircularIndeterminateAnimatorDelegate.this.animatorCompleteEndRequested) {
                        CircularIndeterminateAnimatorDelegate.this.indicatorCollapsingAnimator.setFloatValues(0.0f, 1.08f);
                    }
                }
            });
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this, INDICATOR_TAIL_CHANGE_FRACTION, 0.0f, 1.0f);
            this.indicatorCollapsingAnimator = ofFloat3;
            ofFloat3.setDuration(666L);
            this.indicatorCollapsingAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            AnimatorSet animatorSet = new AnimatorSet();
            this.animatorSet = animatorSet;
            animatorSet.playSequentially(ofFloat2, this.indicatorCollapsingAnimator);
            this.animatorSet.playTogether(ofFloat);
            ObjectAnimator objectAnimator = this.colorFadingAnimator;
            if (objectAnimator != null) {
                this.animatorSet.playTogether(objectAnimator);
            }
            this.animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.progressindicator.CircularIndeterminateAnimatorDelegate.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (CircularIndeterminateAnimatorDelegate.this.animatorCompleteEndRequested && CircularIndeterminateAnimatorDelegate.this.segmentPositions[0] == CircularIndeterminateAnimatorDelegate.this.segmentPositions[1]) {
                        CircularIndeterminateAnimatorDelegate.this.animatorCompleteCallback.onAnimationEnd(CircularIndeterminateAnimatorDelegate.this.drawable);
                        CircularIndeterminateAnimatorDelegate.this.animatorCompleteEndRequested = false;
                    } else if (CircularIndeterminateAnimatorDelegate.this.drawable.isVisible()) {
                        CircularIndeterminateAnimatorDelegate.this.resetPropertiesForNextCycle();
                        CircularIndeterminateAnimatorDelegate.this.startAnimator();
                    }
                }
            });
        }
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    void cancelAnimatorImmediately() {
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    void requestCancelAnimatorAfterCurrentCycle() {
        if (this.animatorCompleteEndRequested) {
            return;
        }
        if (this.drawable.isVisible()) {
            this.animatorCompleteEndRequested = true;
        } else {
            cancelAnimatorImmediately();
        }
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    void resetPropertiesForNewStart() {
        setIndicatorHeadChangeFraction(0.0f);
        setIndicatorTailChangeFraction(0.0f);
        setIndicatorStartOffset(0.0f);
        ObjectAnimator objectAnimator = this.indicatorCollapsingAnimator;
        if (objectAnimator != null) {
            objectAnimator.setFloatValues(0.0f, 1.0f);
        }
        resetSegmentColors();
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    void resetPropertiesForNextCycle() {
        setIndicatorHeadChangeFraction(0.0f);
        setIndicatorTailChangeFraction(0.0f);
        setIndicatorStartOffset(MathUtils.floorMod(getIndicatorStartOffset() + INDICATOR_OFFSET_PER_COLOR_DEGREES + INDICATOR_DELTA_DEGREES, 360));
        shiftSegmentColors();
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

    private int getNextIndicatorColorIndex() {
        return (this.indicatorColorIndex + 1) % this.drawable.combinedIndicatorColorArray.length;
    }

    private void updateSegmentPositions() {
        this.segmentPositions[0] = (((getIndicatorStartOffset() + getIndicatorInCycleOffset()) - INDICATOR_MIN_DEGREES) + (getIndicatorTailChangeFraction() * INDICATOR_DELTA_DEGREES)) / INDICATOR_OFFSET_PER_COLOR_DEGREES;
        this.segmentPositions[1] = ((getIndicatorStartOffset() + getIndicatorInCycleOffset()) + (getIndicatorHeadChangeFraction() * INDICATOR_DELTA_DEGREES)) / INDICATOR_OFFSET_PER_COLOR_DEGREES;
    }

    private void shiftSegmentColors() {
        this.indicatorColorIndex = getNextIndicatorColorIndex();
        this.colorFadingAnimator.setIntValues(this.drawable.combinedIndicatorColorArray[this.indicatorColorIndex], this.drawable.combinedIndicatorColorArray[getNextIndicatorColorIndex()]);
        setDisplayedIndicatorColor(this.drawable.combinedIndicatorColorArray[this.indicatorColorIndex]);
    }

    private void resetSegmentColors() {
        this.indicatorColorIndex = 0;
        this.colorFadingAnimator.setIntValues(this.drawable.combinedIndicatorColorArray[this.indicatorColorIndex], this.drawable.combinedIndicatorColorArray[getNextIndicatorColorIndex()]);
        setDisplayedIndicatorColor(this.drawable.combinedIndicatorColorArray[this.indicatorColorIndex]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDisplayedIndicatorColor() {
        return this.displayedIndicatorColor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDisplayedIndicatorColor(int i) {
        this.displayedIndicatorColor = i;
        this.segmentColors[0] = i;
        this.drawable.invalidateSelf();
    }

    private float getIndicatorStartOffset() {
        return this.indicatorStartOffset;
    }

    void setIndicatorStartOffset(float f) {
        this.indicatorStartOffset = f;
        updateSegmentPositions();
        this.drawable.invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getIndicatorInCycleOffset() {
        return this.indicatorInCycleOffset;
    }

    void setIndicatorInCycleOffset(float f) {
        this.indicatorInCycleOffset = f;
        updateSegmentPositions();
        this.drawable.invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getIndicatorHeadChangeFraction() {
        return this.indicatorHeadChangeFraction;
    }

    void setIndicatorHeadChangeFraction(float f) {
        this.indicatorHeadChangeFraction = f;
        updateSegmentPositions();
        this.drawable.invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getIndicatorTailChangeFraction() {
        return this.indicatorTailChangeFraction;
    }

    void setIndicatorTailChangeFraction(float f) {
        this.indicatorTailChangeFraction = f;
        updateSegmentPositions();
        this.drawable.invalidateSelf();
    }
}
