package com.shizhefei.view.indicator.transition;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.utils.ColorGradient;

/* loaded from: classes2.dex */
public class OnTransitionTextListener implements Indicator.OnTransitionListener {
    private ColorGradient gradient;
    private float selectSize = -1.0f;
    private float unSelectSize = -1.0f;
    private float dFontFize = -1.0f;
    private boolean isPxSize = false;

    public OnTransitionTextListener() {
    }

    public OnTransitionTextListener(float f, float f2, int i, int i2) {
        setColor(i, i2);
        setSize(f, f2);
    }

    public final OnTransitionTextListener setSize(float f, float f2) {
        this.isPxSize = false;
        this.selectSize = f;
        this.unSelectSize = f2;
        this.dFontFize = f - f2;
        return this;
    }

    public final OnTransitionTextListener setValueFromRes(Context context, int i, int i2, int i3, int i4) {
        setColorId(context, i, i2);
        setSizeId(context, i3, i4);
        return this;
    }

    public final OnTransitionTextListener setColorId(Context context, int i, int i2) {
        Resources resources = context.getResources();
        setColor(resources.getColor(i), resources.getColor(i2));
        return this;
    }

    public final OnTransitionTextListener setSizeId(Context context, int i, int i2) {
        Resources resources = context.getResources();
        setSize(resources.getDimensionPixelSize(i), resources.getDimensionPixelSize(i2));
        this.isPxSize = true;
        return this;
    }

    public final OnTransitionTextListener setColor(int i, int i2) {
        this.gradient = new ColorGradient(i2, i, 100);
        return this;
    }

    public TextView getTextView(View view, int i) {
        return (TextView) view;
    }

    @Override // com.shizhefei.view.indicator.Indicator.OnTransitionListener
    public void onTransition(View view, int i, float f) {
        TextView textView = getTextView(view, i);
        ColorGradient colorGradient = this.gradient;
        if (colorGradient != null) {
            textView.setTextColor(colorGradient.getColor((int) (100.0f * f)));
        }
        float f2 = this.unSelectSize;
        if (f2 <= 0.0f || this.selectSize <= 0.0f) {
            return;
        }
        if (this.isPxSize) {
            textView.setTextSize(0, f2 + (this.dFontFize * f));
        } else {
            textView.setTextSize(f2 + (this.dFontFize * f));
        }
    }
}
