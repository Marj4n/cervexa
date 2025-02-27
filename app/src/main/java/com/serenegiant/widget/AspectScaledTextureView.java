package com.serenegiant.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;

import com.serenegiant.common.R;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/* loaded from: classes2.dex */
public class AspectScaledTextureView extends TextureView implements TextureView.SurfaceTextureListener, IAspectRatioView, IScaledView, ITextureView {
    private static final String TAG = AspectScaledTextureView.class.getSimpleName();
    private volatile boolean mHasSurface;
    protected final Matrix mImageMatrix;
    private final Set<SurfaceTextureListener> mListeners;
    private double mRequestedAspect;
    private int mScaleMode;
    private SurfaceTextureListener mSurfaceTextureListener;
    private int prevHeight;
    private int prevWidth;

    protected void onResize(int i, int i2) {
    }

    public AspectScaledTextureView(Context context) {
        this(context, null, 0);
    }

    public AspectScaledTextureView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AspectScaledTextureView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mImageMatrix = new Matrix();
        this.mScaleMode = 0;
        this.mRequestedAspect = -1.0d;
        this.mListeners = new CopyOnWriteArraySet();
        this.prevWidth = -1;
        this.prevHeight = -1;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.AspectScaledTextureView, i, 0);
        try {
            this.mRequestedAspect = obtainStyledAttributes.getFloat(R.styleable.AspectScaledTextureView_aspect_ratio, -1.0f);
            this.mScaleMode = obtainStyledAttributes.getInt(R.styleable.AspectScaledTextureView_scale_mode, 0);
            obtainStyledAttributes.recycle();
            super.setSurfaceTextureListener(this);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        int i4;
        if (this.mRequestedAspect > 1e-10 && this.mScaleMode == 0) {
            int size = MeasureSpec.getSize(i);
            int size2 = MeasureSpec.getSize(i2);
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
                i3 = MeasureSpec.makeMeasureSpec(i5 + paddingLeft, BasicMeasure.EXACTLY);
                i4 = MeasureSpec.makeMeasureSpec(i6 + paddingTop, BasicMeasure.EXACTLY);
                super.onMeasure(i3, i4);
            }
        }
        i3 = i;
        i4 = i2;
        super.onMeasure(i3, i4);
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        if (this.prevWidth != getWidth() || this.prevHeight != getHeight()) {
            this.prevWidth = getWidth();
            int height = getHeight();
            this.prevHeight = height;
            onResize(this.prevWidth, height);
        }
        init();
    }

    @Override // android.view.TextureView
    public final void setSurfaceTextureListener(SurfaceTextureListener surfaceTextureListener) {
        unregister(this.mSurfaceTextureListener);
        this.mSurfaceTextureListener = surfaceTextureListener;
        register(surfaceTextureListener);
    }

    @Override // com.serenegiant.widget.ITextureView
    public void register(SurfaceTextureListener surfaceTextureListener) {
        if (surfaceTextureListener != null) {
            this.mListeners.add(surfaceTextureListener);
        }
    }

    @Override // com.serenegiant.widget.ITextureView
    public void unregister(SurfaceTextureListener surfaceTextureListener) {
        this.mListeners.remove(surfaceTextureListener);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        this.mHasSurface = true;
        init();
        for (SurfaceTextureListener surfaceTextureListener : this.mListeners) {
            try {
                surfaceTextureListener.onSurfaceTextureAvailable(surfaceTexture, i, i2);
            } catch (Exception e) {
                this.mListeners.remove(surfaceTextureListener);
                Log.w(TAG, e);
            }
        }
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
        for (SurfaceTextureListener surfaceTextureListener : this.mListeners) {
            try {
                surfaceTextureListener.onSurfaceTextureSizeChanged(surfaceTexture, i, i2);
            } catch (Exception e) {
                this.mListeners.remove(surfaceTextureListener);
                Log.w(TAG, e);
            }
        }
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        this.mHasSurface = false;
        for (SurfaceTextureListener surfaceTextureListener : this.mListeners) {
            try {
                surfaceTextureListener.onSurfaceTextureDestroyed(surfaceTexture);
            } catch (Exception e) {
                this.mListeners.remove(surfaceTextureListener);
                Log.w(TAG, e);
            }
        }
        return false;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        for (SurfaceTextureListener surfaceTextureListener : this.mListeners) {
            try {
                surfaceTextureListener.onSurfaceTextureUpdated(surfaceTexture);
            } catch (Exception e) {
                this.mListeners.remove(surfaceTextureListener);
                Log.w(TAG, e);
            }
        }
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

    @Override // com.serenegiant.widget.IScaledView
    public void setScaleMode(int i) {
        if (this.mScaleMode != i) {
            this.mScaleMode = i;
            requestLayout();
        }
    }

    @Override // com.serenegiant.widget.IScaledView
    public int getScaleMode() {
        return this.mScaleMode;
    }

    protected void init() {
        int width = getWidth();
        int height = getHeight();
        this.mImageMatrix.reset();
        if (this.mScaleMode == 2) {
            double d = height;
            double d2 = this.mRequestedAspect * d;
            double d3 = width;
            double max = Math.max(d3 / d2, d / d);
            this.mImageMatrix.postScale((float) ((d2 * max) / d3), (float) ((max * d) / d), width / 2, height / 2);
        }
        setTransform(this.mImageMatrix);
    }
}
