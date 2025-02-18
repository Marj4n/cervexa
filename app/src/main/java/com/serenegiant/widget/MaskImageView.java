package com.serenegiant.widget;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

/* loaded from: classes2.dex */
public class MaskImageView extends AppCompatImageView {
    private final Paint mCopyPaint;
    private final Rect mMaskBounds;
    private Drawable mMaskDrawable;
    private final Paint mMaskedPaint;
    private final RectF mViewBoundsF;

    public MaskImageView(Context context) {
        this(context, null, 0);
    }

    public MaskImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MaskImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mMaskedPaint = new Paint();
        this.mCopyPaint = new Paint();
        this.mMaskBounds = new Rect();
        this.mViewBoundsF = new RectF();
        this.mMaskedPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        this.mMaskDrawable = null;
    }

    public synchronized void setMaskDrawable(Drawable drawable) {
        if (this.mMaskDrawable != drawable) {
            this.mMaskDrawable = drawable;
            if (drawable != null) {
                drawable.setBounds(this.mMaskBounds);
            }
            postInvalidate();
        }
    }

    @Override // android.view.View
    protected synchronized void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i != 0 && i2 != 0) {
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int min = Math.min((i - paddingLeft) - getPaddingRight(), (i2 - paddingTop) - getPaddingBottom());
            int i5 = ((i - min) / 2) + paddingLeft;
            int i6 = ((i2 - min) / 2) + paddingTop;
            this.mMaskBounds.set(i5, i6, i5 + min, i6 + min);
            if (min > 3) {
                this.mMaskedPaint.setMaskFilter(new BlurMaskFilter((min * 2) / 3.0f, BlurMaskFilter.Blur.NORMAL));
            }
            this.mViewBoundsF.set(0.0f, 0.0f, i, i2);
            if (this.mMaskDrawable != null) {
                this.mMaskDrawable.setBounds(this.mMaskBounds);
            }
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected synchronized void onDraw(Canvas canvas) {
        int saveLayer = canvas.saveLayer(this.mViewBoundsF, this.mCopyPaint, 31);
        try {
            if (this.mMaskDrawable != null) {
                this.mMaskDrawable.draw(canvas);
                canvas.saveLayer(this.mViewBoundsF, this.mMaskedPaint, 0);
            }
            super.onDraw(canvas);
        } finally {
            canvas.restoreToCount(saveLayer);
        }
    }
}
