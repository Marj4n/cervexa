package com.shizhefei.view.indicator.slidebar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.shizhefei.view.indicator.slidebar.ScrollBar;

/* loaded from: classes2.dex */
public class LayoutBar implements ScrollBar {
    protected Context context;
    protected ScrollBar.Gravity gravity;
    protected int height;
    protected int layoutId;
    private ViewGroup.LayoutParams layoutParams;
    protected View view;
    protected int width;

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public void onPageScrolled(int i, float f, int i2) {
    }

    public LayoutBar(Context context, int i) {
        this(context, i, ScrollBar.Gravity.BOTTOM);
    }

    public LayoutBar(Context context, int i, ScrollBar.Gravity gravity) {
        this.context = context;
        this.layoutId = i;
        View inflate = LayoutInflater.from(context).inflate(i, (ViewGroup) new LinearLayout(context), false);
        this.view = inflate;
        this.layoutParams = inflate.getLayoutParams();
        this.height = this.view.getLayoutParams().height;
        this.width = this.view.getLayoutParams().width;
        this.gravity = gravity;
    }

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public int getHeight(int i) {
        int i2 = this.height;
        if (i2 > 0) {
            return i2;
        }
        this.layoutParams.height = i;
        return i;
    }

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public int getWidth(int i) {
        int i2 = this.width;
        if (i2 > 0) {
            return i2;
        }
        this.layoutParams.width = i;
        return i;
    }

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public View getSlideView() {
        return this.view;
    }

    @Override // com.shizhefei.view.indicator.slidebar.ScrollBar
    public ScrollBar.Gravity getGravity() {
        return this.gravity;
    }
}
