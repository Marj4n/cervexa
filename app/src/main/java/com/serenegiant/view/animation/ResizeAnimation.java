package com.serenegiant.view.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/* loaded from: classes2.dex */
public class ResizeAnimation extends Animation {
    private final int mDiffHeight;
    private final int mDiffWidth;
    private final int mStartHeight;
    private final int mStartWidth;
    private final View mTargetView;

    public ResizeAnimation(View view, int i, int i2, int i3, int i4) {
        this.mTargetView = view;
        this.mStartWidth = i;
        this.mStartHeight = i2;
        this.mDiffWidth = i3 - i;
        this.mDiffHeight = i4 - i2;
    }

    @Override // android.view.animation.Animation
    protected void applyTransformation(float f, Transformation transformation) {
        super.applyTransformation(f, transformation);
        int i = (int) (this.mStartWidth + (this.mDiffWidth * f));
        this.mTargetView.getLayoutParams().width = i;
        this.mTargetView.getLayoutParams().height = (int) (this.mStartHeight + (this.mDiffHeight * f));
        this.mTargetView.requestLayout();
    }

    @Override // android.view.animation.Animation
    public void initialize(int i, int i2, int i3, int i4) {
        super.initialize(i, i2, i3, i4);
    }

    @Override // android.view.animation.Animation
    public boolean willChangeBounds() {
        return (this.mDiffWidth == 0 && this.mDiffHeight == 0) ? false : true;
    }
}
