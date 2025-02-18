package com.shizhefei.view.indicator.slidebar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import com.shizhefei.view.indicator.slidebar.ScrollBar;

/* loaded from: classes2.dex */
public class DrawableBar implements ScrollBar {
    protected Drawable drawable;
    protected int drawableId;
    protected ScrollBar.Gravity gravity;
    protected View view;

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public void onPageScrolled(int i, float f, int i2) {
    }

    public DrawableBar(Context context, int i) {
        this(context, i, ScrollBar.Gravity.BOTTOM);
    }

    public DrawableBar(Context context, int i, ScrollBar.Gravity gravity) {
        this(context, context.getResources().getDrawable(i), gravity);
    }

    public DrawableBar(Context context, Drawable drawable) {
        this(context, drawable, ScrollBar.Gravity.BOTTOM);
    }

    public DrawableBar(Context context, Drawable drawable, ScrollBar.Gravity gravity) {
        this.view = new View(context);
        this.drawable = drawable;
        if (Build.VERSION.SDK_INT >= 16) {
            this.view.setBackground(drawable);
        } else {
            this.view.setBackgroundDrawable(drawable);
        }
        this.gravity = gravity;
    }

    public int getColor() {
        return this.drawableId;
    }

    public void setColor(int i) {
        this.drawableId = i;
        this.view.setBackgroundColor(i);
    }

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public int getHeight(int i) {
        return this.drawable.getIntrinsicHeight();
    }

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public int getWidth(int i) {
        return this.drawable.getIntrinsicWidth();
    }

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public View getSlideView() {
        return this.view;
    }

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public ScrollBar.Gravity getGravity() {
        return this.gravity;
    }

    public DrawableBar setGravity(ScrollBar.Gravity gravity) {
        this.gravity = gravity;
        return this;
    }
}
