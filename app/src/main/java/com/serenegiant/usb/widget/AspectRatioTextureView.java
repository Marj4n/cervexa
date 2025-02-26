package com.serenegiant.usb.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;

import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;

import com.serenegiant.widget.IAspectRatioView;

/* loaded from: classes2.dex */
public class AspectRatioTextureView extends TextureView implements IAspectRatioView {
    private static final boolean DEBUG = true;
    private static final String TAG = "AbstractCameraView";
    private CameraViewInterface.Callback mCallback;
    private double mRequestedAspect;

    public AspectRatioTextureView(Context context) {
        this(context, null, 0);
    }

    public AspectRatioTextureView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AspectRatioTextureView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRequestedAspect = -1.0d;
    }

    @Override // com.serenegiant.widget.IAspectRatioView
    public void setAspectRatio(double d) {
        if (d < 1e-10) {
            throw new IllegalArgumentException();
        }
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

    @Override // android.view.View
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
}
