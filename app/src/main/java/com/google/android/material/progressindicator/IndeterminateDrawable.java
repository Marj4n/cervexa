package com.google.android.material.progressindicator;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

/* loaded from: classes.dex */
public final class IndeterminateDrawable extends DrawableWithAnimatedVisibilityChange {
    private IndeterminateAnimatorDelegate<AnimatorSet> animatorDelegate;
    private final DrawingDelegate drawingDelegate;

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, androidx.vectordrawable.graphics.drawable.Animatable2Compat
    public /* bridge */ /* synthetic */ void clearAnimationCallbacks() {
        super.clearAnimationCallbacks();
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ int getAlpha() {
        return super.getAlpha();
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ int getOpacity() {
        return super.getOpacity();
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange
    public /* bridge */ /* synthetic */ boolean hideNow() {
        return super.hideNow();
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Animatable
    public /* bridge */ /* synthetic */ boolean isRunning() {
        return super.isRunning();
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, androidx.vectordrawable.graphics.drawable.Animatable2Compat
    public /* bridge */ /* synthetic */ void registerAnimationCallback(Animatable2Compat.AnimationCallback animationCallback) {
        super.registerAnimationCallback(animationCallback);
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void setAlpha(int i) {
        super.setAlpha(i);
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void setColorFilter(ColorFilter colorFilter) {
        super.setColorFilter(colorFilter);
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange
    public /* bridge */ /* synthetic */ void setInternalAnimationCallback(Animatable2Compat.AnimationCallback animationCallback) {
        super.setInternalAnimationCallback(animationCallback);
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ boolean setVisible(boolean z, boolean z2) {
        return super.setVisible(z, z2);
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Animatable
    public /* bridge */ /* synthetic */ void start() {
        super.start();
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Animatable
    public /* bridge */ /* synthetic */ void stop() {
        super.stop();
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, androidx.vectordrawable.graphics.drawable.Animatable2Compat
    public /* bridge */ /* synthetic */ boolean unregisterAnimationCallback(Animatable2Compat.AnimationCallback animationCallback) {
        return super.unregisterAnimationCallback(animationCallback);
    }

    public IndeterminateDrawable(Context context, ProgressIndicatorSpec progressIndicatorSpec) {
        super(context, progressIndicatorSpec);
        Pair<DrawingDelegate, IndeterminateAnimatorDelegate<AnimatorSet>> initializeDelegates = initializeDelegates(progressIndicatorSpec.indicatorType, progressIndicatorSpec.linearSeamless);
        this.drawingDelegate = (DrawingDelegate) initializeDelegates.first;
        setAnimatorDelegate((IndeterminateAnimatorDelegate) initializeDelegates.second);
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange
    public boolean setVisible(boolean z, boolean z2, boolean z3) {
        boolean visible = super.setVisible(z, z2, z3);
        if (!isRunning()) {
            this.animatorDelegate.cancelAnimatorImmediately();
            this.animatorDelegate.resetPropertiesForNewStart();
        }
        if (z && z3) {
            this.animatorDelegate.startAnimator();
        }
        return visible;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.drawingDelegate.getPreferredWidth(this.spec);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.drawingDelegate.getPreferredHeight(this.spec);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect rect = new Rect();
        if (!getBounds().isEmpty() && isVisible() && canvas.getClipBounds(rect)) {
            canvas.save();
            this.drawingDelegate.adjustCanvas(canvas, this.spec, getGrowFraction());
            float growFraction = this.spec.indicatorSize * getGrowFraction();
            float growFraction2 = this.spec.indicatorCornerRadius * getGrowFraction();
            this.drawingDelegate.fillTrackWithColor(canvas, this.paint, this.combinedTrackColor, 0.0f, 1.0f, growFraction, growFraction2);
            for (int i = 0; i < this.animatorDelegate.segmentColors.length; i++) {
                int i2 = i * 2;
                this.drawingDelegate.fillTrackWithColor(canvas, this.paint, this.animatorDelegate.segmentColors[i], this.animatorDelegate.segmentPositions[i2], this.animatorDelegate.segmentPositions[i2 + 1], growFraction, growFraction2);
            }
            canvas.restore();
        }
    }

    private Pair<DrawingDelegate, IndeterminateAnimatorDelegate<AnimatorSet>> initializeDelegates(int i, boolean z) {
        if (i == 1) {
            return new Pair<>(new CircularDrawingDelegate(), new CircularIndeterminateAnimatorDelegate());
        }
        return new Pair<>(new LinearDrawingDelegate(), z ? new LinearIndeterminateSeamlessAnimatorDelegate() : new LinearIndeterminateNonSeamlessAnimatorDelegate(this.context));
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange
    protected void recalculateColors() {
        super.recalculateColors();
        IndeterminateAnimatorDelegate<AnimatorSet> indeterminateAnimatorDelegate = this.animatorDelegate;
        if (indeterminateAnimatorDelegate != null) {
            indeterminateAnimatorDelegate.invalidateSpecValues();
        }
    }

    public IndeterminateAnimatorDelegate<AnimatorSet> getAnimatorDelegate() {
        return this.animatorDelegate;
    }

    public void setAnimatorDelegate(IndeterminateAnimatorDelegate<AnimatorSet> indeterminateAnimatorDelegate) {
        this.animatorDelegate = indeterminateAnimatorDelegate;
        indeterminateAnimatorDelegate.registerDrawable(this);
        setInternalAnimationCallback(new Animatable2Compat.AnimationCallback() { // from class: com.google.android.material.progressindicator.IndeterminateDrawable.1
            @Override // androidx.vectordrawable.graphics.drawable.Animatable2Compat.AnimationCallback
            public void onAnimationEnd(Drawable drawable) {
                super.onAnimationEnd(drawable);
                IndeterminateDrawable.this.animatorDelegate.cancelAnimatorImmediately();
                IndeterminateDrawable.this.animatorDelegate.resetPropertiesForNewStart();
            }
        });
        setGrowFraction(1.0f);
    }

    public DrawingDelegate getDrawingDelegate() {
        return this.drawingDelegate;
    }
}
