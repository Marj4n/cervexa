package com.shizhefei.view.indicator.slidebar;

import android.content.Context;
import android.graphics.Rect;
import android.widget.TextView;
import com.shizhefei.view.indicator.Indicator;

/* loaded from: classes2.dex */
public class TextWidthColorBar extends ColorBar {
    private Indicator indicator;
    private int realWidth;

    public TextWidthColorBar(Context context, Indicator indicator, int i, int i2) {
        super(context, i, i2);
        this.realWidth = 0;
        this.indicator = indicator;
    }

    @Override // com.shizhefei.view.indicator.slidebar.ColorBar, com.shizhefei.view.indicator.slidebar.ScrollBar
    public void onPageScrolled(int i, float f, int i2) {
        this.realWidth = (int) ((getTextWidth(getTextView(i)) * (1.0f - f)) + (getTextWidth(getTextView(i + 1)) * f));
    }

    @Override // com.shizhefei.view.indicator.slidebar.ColorBar, com.shizhefei.view.indicator.slidebar.ScrollBar
    public int getWidth(int i) {
        TextView textView;
        if (this.realWidth == 0 && this.indicator.getIndicatorAdapter() != null && (textView = getTextView(this.indicator.getCurrentItem())) != null) {
            this.realWidth = getTextWidth(textView);
        }
        return this.realWidth;
    }

    protected TextView getTextView(int i) {
        return (TextView) this.indicator.getItemView(i);
    }

    private int getTextWidth(TextView textView) {
        if (textView == null) {
            return 0;
        }
        Rect rect = new Rect();
        String charSequence = textView.getText().toString();
        textView.getPaint().getTextBounds(charSequence, 0, charSequence.length(), rect);
        return rect.left + rect.width();
    }
}
