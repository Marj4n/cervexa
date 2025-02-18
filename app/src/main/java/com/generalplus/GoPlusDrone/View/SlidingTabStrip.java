package com.generalplus.GoPlusDrone.View;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import com.generalplus.GoPlusDrone.View.SlidingTabLayout;

/* loaded from: classes.dex */
class SlidingTabStrip extends LinearLayout {
    private static final byte DEFAULT_BOTTOM_BORDER_COLOR_ALPHA = 38;
    private static final int DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 2;
    private static final byte DEFAULT_DIVIDER_COLOR_ALPHA = 32;
    private static final float DEFAULT_DIVIDER_HEIGHT = 0.5f;
    private static final int DEFAULT_DIVIDER_THICKNESS_DIPS = 1;
    private static final int DEFAULT_SELECTED_INDICATOR_COLOR = -13388315;
    private static final int SELECTED_INDICATOR_THICKNESS_DIPS = 8;
    private final Paint mBottomBorderPaint;
    private final int mBottomBorderThickness;
    private SlidingTabLayout.TabColorizer mCustomTabColorizer;
    private final int mDefaultBottomBorderColor;
    private final SimpleTabColorizer mDefaultTabColorizer;
    private final float mDividerHeight;
    private final Paint mDividerPaint;
    private final Paint mSelectedIndicatorPaint;
    private final int mSelectedIndicatorThickness;
    private int mSelectedPosition;
    private float mSelectionOffset;

    SlidingTabStrip(Context context) {
        this(context, null);
    }

    SlidingTabStrip(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setWillNotDraw(false);
        float f = getResources().getDisplayMetrics().density;
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorForeground, typedValue, true);
        int i = typedValue.data;
        this.mDefaultBottomBorderColor = setColorAlpha(i, DEFAULT_BOTTOM_BORDER_COLOR_ALPHA);
        SimpleTabColorizer simpleTabColorizer = new SimpleTabColorizer();
        this.mDefaultTabColorizer = simpleTabColorizer;
        simpleTabColorizer.setIndicatorColors(DEFAULT_SELECTED_INDICATOR_COLOR);
        this.mDefaultTabColorizer.setDividerColors(setColorAlpha(i, DEFAULT_DIVIDER_COLOR_ALPHA));
        this.mBottomBorderThickness = (int) (2.0f * f);
        Paint paint = new Paint();
        this.mBottomBorderPaint = paint;
        paint.setColor(this.mDefaultBottomBorderColor);
        this.mSelectedIndicatorThickness = (int) (8.0f * f);
        this.mSelectedIndicatorPaint = new Paint();
        this.mDividerHeight = 0.5f;
        Paint paint2 = new Paint();
        this.mDividerPaint = paint2;
        paint2.setStrokeWidth((int) (f * 1.0f));
    }

    void setCustomTabColorizer(SlidingTabLayout.TabColorizer tabColorizer) {
        this.mCustomTabColorizer = tabColorizer;
        invalidate();
    }

    void setSelectedIndicatorColors(int... iArr) {
        this.mCustomTabColorizer = null;
        this.mDefaultTabColorizer.setIndicatorColors(iArr);
        invalidate();
    }

    void setDividerColors(int... iArr) {
        this.mCustomTabColorizer = null;
        this.mDefaultTabColorizer.setDividerColors(iArr);
        invalidate();
    }

    void onViewPagerPageChanged(int i, float f) {
        this.mSelectedPosition = i;
        this.mSelectionOffset = f;
        invalidate();
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int childCount = getChildCount();
        float f = height;
        int min = (int) (Math.min(Math.max(0.0f, this.mDividerHeight), 1.0f) * f);
        SlidingTabLayout.TabColorizer tabColorizer = this.mCustomTabColorizer;
        if (tabColorizer == null) {
            tabColorizer = this.mDefaultTabColorizer;
        }
        SlidingTabLayout.TabColorizer tabColorizer2 = tabColorizer;
        if (childCount > 0) {
            View childAt = getChildAt(this.mSelectedPosition);
            int left = childAt.getLeft();
            int right = childAt.getRight();
            int indicatorColor = tabColorizer2.getIndicatorColor(this.mSelectedPosition);
            if (this.mSelectionOffset > 0.0f && this.mSelectedPosition < getChildCount() - 1) {
                int indicatorColor2 = tabColorizer2.getIndicatorColor(this.mSelectedPosition + 1);
                if (indicatorColor != indicatorColor2) {
                    indicatorColor = blendColors(indicatorColor2, indicatorColor, this.mSelectionOffset);
                }
                View childAt2 = getChildAt(this.mSelectedPosition + 1);
                float left2 = this.mSelectionOffset * childAt2.getLeft();
                float f2 = this.mSelectionOffset;
                left = (int) (left2 + ((1.0f - f2) * left));
                right = (int) ((f2 * childAt2.getRight()) + ((1.0f - this.mSelectionOffset) * right));
            }
            this.mSelectedIndicatorPaint.setColor(indicatorColor);
            canvas.drawRect(left, height - this.mSelectedIndicatorThickness, right, f, this.mSelectedIndicatorPaint);
        }
        canvas.drawRect(0.0f, height - this.mBottomBorderThickness, getWidth(), f, this.mBottomBorderPaint);
        int i = (height - min) / 2;
        for (int i2 = 0; i2 < childCount - 1; i2++) {
            View childAt3 = getChildAt(i2);
            this.mDividerPaint.setColor(tabColorizer2.getDividerColor(i2));
            canvas.drawLine(childAt3.getRight(), i, childAt3.getRight(), i + min, this.mDividerPaint);
        }
    }

    private static int setColorAlpha(int i, byte b) {
        return Color.argb((int) b, Color.red(i), Color.green(i), Color.blue(i));
    }

    private static int blendColors(int i, int i2, float f) {
        float f2 = 1.0f - f;
        return Color.rgb((int) ((Color.red(i) * f) + (Color.red(i2) * f2)), (int) ((Color.green(i) * f) + (Color.green(i2) * f2)), (int) ((Color.blue(i) * f) + (Color.blue(i2) * f2)));
    }

    private static class SimpleTabColorizer implements SlidingTabLayout.TabColorizer {
        private int[] mDividerColors;
        private int[] mIndicatorColors;

        private SimpleTabColorizer() {
        }

        @Override // com.generalplus.GoPlusDrone.View.SlidingTabLayout.TabColorizer
        public final int getIndicatorColor(int i) {
            int[] iArr = this.mIndicatorColors;
            return iArr[i % iArr.length];
        }

        @Override // com.generalplus.GoPlusDrone.View.SlidingTabLayout.TabColorizer
        public final int getDividerColor(int i) {
            int[] iArr = this.mDividerColors;
            return iArr[i % iArr.length];
        }

        void setIndicatorColors(int... iArr) {
            this.mIndicatorColors = iArr;
        }

        void setDividerColors(int... iArr) {
            this.mDividerColors = iArr;
        }
    }
}
