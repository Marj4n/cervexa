package com.serenegiant.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import com.serenegiant.common.R;

/* loaded from: classes2.dex */
public class AspectRatioFrameLayout extends FrameLayout implements IAspectRatioView {
    private double mRequestedAspect;

    public AspectRatioFrameLayout(Context context) {
        this(context, null, 0);
    }

    public AspectRatioFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        this.mRequestedAspect = -1.0d;
    }

    public AspectRatioFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRequestedAspect = -1.0d;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.IAspectRatioView, i, 0);
        try {
            this.mRequestedAspect = obtainStyledAttributes.getFloat(R.styleable.IAspectRatioView_aspect_ratio, -1.0f);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        int i4;
        if (this.mRequestedAspect > 1e-10) {
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            int paddingLeft = getPaddingLeft() + getPaddingRight();
            int paddingTop = getPaddingTop() + getPaddingBottom();
            int i5 = size - paddingLeft;
            int i6 = size2 - paddingTop;
            double d = i5;
            double d2 = i6;
            double d3 = (this.mRequestedAspect / (d / d2)) - 1.0d;
            if (Math.abs(d3) > 0.01d) {
                if (d3 > 1e-10) {
                    i6 = (int) (d / this.mRequestedAspect);
                } else {
                    i5 = (int) (d2 * this.mRequestedAspect);
                }
                i3 = View.MeasureSpec.makeMeasureSpec(i5 + paddingLeft, BasicMeasure.EXACTLY);
                i4 = View.MeasureSpec.makeMeasureSpec(i6 + paddingTop, BasicMeasure.EXACTLY);
                super.onMeasure(i3, i4);
            }
        }
        i3 = i;
        i4 = i2;
        super.onMeasure(i3, i4);
    }

    @Override // com.serenegiant.widget.IAspectRatioView
    public void setAspectRatio(double d) {
        if (this.mRequestedAspect != d) {
            this.mRequestedAspect = d;
            requestLayout();
        }
    }

    @Override // com.serenegiant.widget.IAspectRatioView
    public void setAspectRatio(int i, int i2) {
        setAspectRatio(i / i2);
    }

    @Override // com.serenegiant.widget.IAspectRatioView
    public double getAspectRatio() {
        return this.mRequestedAspect;
    }
}
