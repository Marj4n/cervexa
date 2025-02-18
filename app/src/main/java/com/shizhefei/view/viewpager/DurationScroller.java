package com.shizhefei.view.viewpager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/* loaded from: classes2.dex */
public class DurationScroller extends Scroller {
    private int mScrollDuration;

    public DurationScroller(Context context) {
        super(context);
        this.mScrollDuration = 800;
    }

    public DurationScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
        this.mScrollDuration = 800;
    }

    public DurationScroller(Context context, Interpolator interpolator, boolean z) {
        super(context, interpolator, z);
        this.mScrollDuration = 800;
    }

    @Override // android.widget.Scroller
    public void startScroll(int i, int i2, int i3, int i4, int i5) {
        super.startScroll(i, i2, i3, i4, this.mScrollDuration);
    }

    @Override // android.widget.Scroller
    public void startScroll(int i, int i2, int i3, int i4) {
        super.startScroll(i, i2, i3, i4, this.mScrollDuration);
    }

    public int getScrollDuration() {
        return this.mScrollDuration;
    }

    public void setScrollDuration(int i) {
        this.mScrollDuration = i;
    }
}
