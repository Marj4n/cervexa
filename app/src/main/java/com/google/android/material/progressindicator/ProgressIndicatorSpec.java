package com.google.android.material.progressindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;

/* loaded from: classes.dex */
public final class ProgressIndicatorSpec {
    public int circularInset;
    public int circularRadius;
    public int growMode;
    public int[] indicatorColors;
    public int indicatorCornerRadius;
    public int indicatorSize;
    public int indicatorType;
    public boolean inverse;
    public boolean linearSeamless;
    public int trackColor;

    public void loadFromAttributes(Context context, AttributeSet attributeSet, int i) {
        loadFromAttributes(context, attributeSet, i, ProgressIndicator.DEF_STYLE_RES);
    }

    public void loadFromAttributes(Context context, AttributeSet attributeSet, int i, int i2) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ProgressIndicator, i, i2);
        this.indicatorType = obtainStyledAttributes.getInt(R.styleable.ProgressIndicator_indicatorType, 0);
        this.indicatorSize = getDimensionPixelSize(context, obtainStyledAttributes, R.styleable.ProgressIndicator_indicatorSize, R.dimen.mtrl_progress_indicator_size);
        this.circularInset = getDimensionPixelSize(context, obtainStyledAttributes, R.styleable.ProgressIndicator_circularInset, R.dimen.mtrl_progress_circular_inset);
        this.circularRadius = getDimensionPixelSize(context, obtainStyledAttributes, R.styleable.ProgressIndicator_circularRadius, R.dimen.mtrl_progress_circular_radius);
        this.inverse = obtainStyledAttributes.getBoolean(R.styleable.ProgressIndicator_inverse, false);
        this.growMode = obtainStyledAttributes.getInt(R.styleable.ProgressIndicator_growMode, 0);
        loadIndicatorColors(context, obtainStyledAttributes);
        loadTrackColor(context, obtainStyledAttributes);
        this.linearSeamless = obtainStyledAttributes.getBoolean(R.styleable.ProgressIndicator_linearSeamless, true) && this.indicatorType == 0 && this.indicatorColors.length >= 3;
        this.indicatorCornerRadius = Math.min(obtainStyledAttributes.getDimensionPixelSize(R.styleable.ProgressIndicator_indicatorCornerRadius, 0), this.indicatorSize / 2);
        obtainStyledAttributes.recycle();
        validate();
    }

    public void validate() {
        if (this.indicatorType == 1 && this.circularRadius < this.indicatorSize / 2) {
            throw new IllegalArgumentException("The circularRadius cannot be less than half of the indicatorSize.");
        }
        if (this.linearSeamless && this.indicatorCornerRadius > 0) {
            throw new IllegalArgumentException("Rounded corners are not supported in linear seamless mode.");
        }
    }

    private static int getDimensionPixelSize(Context context, TypedArray typedArray, int i, int i2) {
        return typedArray.getDimensionPixelSize(i, context.getResources().getDimensionPixelSize(i2));
    }

    private void loadIndicatorColors(Context context, TypedArray typedArray) {
        int color;
        if (typedArray.hasValue(R.styleable.ProgressIndicator_indicatorColors)) {
            this.indicatorColors = context.getResources().getIntArray(typedArray.getResourceId(R.styleable.ProgressIndicator_indicatorColors, -1));
            if (typedArray.hasValue(R.styleable.ProgressIndicator_indicatorColor)) {
                throw new IllegalArgumentException("Attributes indicatorColors and indicatorColor cannot be used at the same time.");
            }
            if (this.indicatorColors.length == 0) {
                throw new IllegalArgumentException("indicatorColors cannot be empty when indicatorColor is not used.");
            }
            return;
        }
        int[] iArr = new int[1];
        if (typedArray.hasValue(R.styleable.ProgressIndicator_indicatorColor)) {
            color = typedArray.getColor(R.styleable.ProgressIndicator_indicatorColor, -1);
        } else {
            color = MaterialColors.getColor(context, R.attr.colorPrimary, -1);
        }
        iArr[0] = color;
        this.indicatorColors = iArr;
    }

    private void loadTrackColor(Context context, TypedArray typedArray) {
        if (typedArray.hasValue(R.styleable.ProgressIndicator_trackColor)) {
            this.trackColor = typedArray.getColor(R.styleable.ProgressIndicator_trackColor, -1);
            return;
        }
        this.trackColor = this.indicatorColors[0];
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.disabledAlpha});
        float f = obtainStyledAttributes.getFloat(0, 0.2f);
        obtainStyledAttributes.recycle();
        this.trackColor = MaterialColors.compositeARGBWithAlpha(this.trackColor, (int) (f * 255.0f));
    }
}
