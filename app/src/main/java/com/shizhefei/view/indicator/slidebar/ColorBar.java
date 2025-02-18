package com.shizhefei.view.indicator.slidebar;

import android.content.Context;
import android.view.View;
import com.shizhefei.view.indicator.slidebar.ScrollBar;

/* loaded from: classes2.dex */
public class ColorBar implements ScrollBar {
    protected int color;
    protected ScrollBar.Gravity gravity;
    protected int height;
    protected View view;
    protected int width;

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public void onPageScrolled(int i, float f, int i2) {
    }

    public ColorBar(Context context, int i, int i2) {
        this(context, i, i2, ScrollBar.Gravity.BOTTOM);
    }

    public ColorBar(Context context, int i, int i2, ScrollBar.Gravity gravity) {
        View view = new View(context);
        this.view = view;
        this.color = i;
        view.setBackgroundColor(i);
        this.height = i2;
        this.gravity = gravity;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int i) {
        this.color = i;
        this.view.setBackgroundColor(i);
    }

    public void setHeight(int i) {
        this.height = i;
    }

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public int getHeight(int i) {
        int i2 = this.height;
        return i2 == 0 ? i : i2;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public int getWidth(int i) {
        int i2 = this.width;
        return i2 == 0 ? i : i2;
    }

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public View getSlideView() {
        return this.view;
    }

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public ScrollBar.Gravity getGravity() {
        return this.gravity;
    }

    public void setGravity(ScrollBar.Gravity gravity) {
        this.gravity = gravity;
    }
}
