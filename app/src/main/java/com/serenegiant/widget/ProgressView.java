package com.serenegiant.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.internal.view.SupportMenu;

/* loaded from: classes2.dex */
public class ProgressView extends View {
    protected static final int GRAVITY_BOTTOM = 80;
    protected static final int GRAVITY_CENTER = 17;
    protected static final int GRAVITY_CENTER_HORIZONTAL = 1;
    protected static final int GRAVITY_CENTER_VERTICAL = 16;
    protected static final int GRAVITY_CLIP_HORIZONTAL = 8;
    protected static final int GRAVITY_CLIP_VERTICAL = 128;
    protected static final int GRAVITY_END = 8388613;
    protected static final int GRAVITY_FILL = 119;
    protected static final int GRAVITY_FILL_HORIZONTAL = 7;
    protected static final int GRAVITY_FILL_VERTICAL = 112;
    protected static final int GRAVITY_LEFT = 3;
    protected static final int GRAVITY_RIGHT = 5;
    protected static final int GRAVITY_START = 8388611;
    protected static final int GRAVITY_TOP = 48;
    private ClipDrawable mClipDrawable;
    private int mColor;
    private Drawable mDrawable;
    private int mMax;
    private int mMin;
    private volatile int mProgress;
    private int mRotation;
    private float mScale;
    private final Runnable mUpdateProgressTask;

    public ProgressView(Context context) {
        super(context);
        this.mRotation = 90;
        this.mMin = 0;
        this.mMax = 100;
        this.mScale = 100.0f;
        this.mProgress = 40;
        this.mColor = SupportMenu.CATEGORY_MASK;
        this.mUpdateProgressTask = new Runnable() { // from class: com.serenegiant.widget.ProgressView.1
            @Override // java.lang.Runnable
            public void run() {
                if (ProgressView.this.mClipDrawable != null) {
                    int i = ((int) (ProgressView.this.mProgress * ProgressView.this.mScale)) + ProgressView.this.mMin;
                    if (i < 0) {
                        i = 0;
                    }
                    if (i > 10000) {
                        i = 10000;
                    }
                    ProgressView.this.mClipDrawable.setLevel(i);
                }
                ProgressView.this.invalidate();
            }
        };
    }

    public ProgressView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRotation = 90;
        this.mMin = 0;
        this.mMax = 100;
        this.mScale = 100.0f;
        this.mProgress = 40;
        this.mColor = SupportMenu.CATEGORY_MASK;
        this.mUpdateProgressTask = new Runnable() { // from class: com.serenegiant.widget.ProgressView.1
            @Override // java.lang.Runnable
            public void run() {
                if (ProgressView.this.mClipDrawable != null) {
                    int i = ((int) (ProgressView.this.mProgress * ProgressView.this.mScale)) + ProgressView.this.mMin;
                    if (i < 0) {
                        i = 0;
                    }
                    if (i > 10000) {
                        i = 10000;
                    }
                    ProgressView.this.mClipDrawable.setLevel(i);
                }
                ProgressView.this.invalidate();
            }
        };
    }

    public ProgressView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRotation = 90;
        this.mMin = 0;
        this.mMax = 100;
        this.mScale = 100.0f;
        this.mProgress = 40;
        this.mColor = SupportMenu.CATEGORY_MASK;
        this.mUpdateProgressTask = new Runnable() { // from class: com.serenegiant.widget.ProgressView.1
            @Override // java.lang.Runnable
            public void run() {
                if (ProgressView.this.mClipDrawable != null) {
                    int i2 = ((int) (ProgressView.this.mProgress * ProgressView.this.mScale)) + ProgressView.this.mMin;
                    if (i2 < 0) {
                        i2 = 0;
                    }
                    if (i2 > 10000) {
                        i2 = 10000;
                    }
                    ProgressView.this.mClipDrawable.setLevel(i2);
                }
                ProgressView.this.invalidate();
            }
        };
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mClipDrawable.draw(canvas);
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        resize();
    }

    public void setMinMax(int i, int i2) {
        if ((this.mMin == i && this.mMax == i2) || i == i2) {
            return;
        }
        this.mMin = Math.min(i, i2);
        this.mMax = Math.max(i, i2);
        resize();
    }

    public void setProgress(int i) {
        if (this.mProgress != i) {
            this.mProgress = i;
            removeCallbacks(this.mUpdateProgressTask);
            post(this.mUpdateProgressTask);
        }
    }

    public void setRotation(int i) {
        int i2 = ((i / 90) * 90) % 360;
        if (this.mRotation != i2) {
            this.mRotation = i2;
            resize();
        }
    }

    public void setColor(int i) {
        if (this.mColor != i) {
            this.mColor = i;
            refreshDrawable(null);
        }
    }

    public void setDrawable(Drawable drawable) {
        if (this.mDrawable != drawable) {
            refreshDrawable(drawable);
        }
    }

    protected void resize() {
        float f = this.mProgress * this.mScale;
        int i = this.mMin;
        float f2 = 10000.0f / (this.mMax - i);
        this.mScale = f2;
        this.mProgress = (int) (((f + i) - i) / f2);
        refreshDrawable(this.mDrawable);
    }

    protected void refreshDrawable(Drawable drawable) {
        this.mDrawable = drawable;
        if (drawable == null) {
            this.mDrawable = new ColorDrawable(this.mColor);
        }
        int i = 115;
        int i2 = this.mRotation;
        int i3 = 2;
        if (i2 != 90) {
            if (i2 == 180) {
                i = 117;
            } else if (i2 == 270) {
                i = 55;
            }
            i3 = 1;
        } else {
            i = 87;
        }
        this.mClipDrawable = new ClipDrawable(this.mDrawable, i, i3);
        Rect rect = new Rect();
        getDrawingRect(rect);
        this.mClipDrawable.setBounds(rect);
        this.mClipDrawable.setLevel(((int) (this.mProgress * this.mScale)) + this.mMin);
        postInvalidate();
    }
}
