package com.shizhefei.view.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

/* loaded from: classes2.dex */
public class SViewPager extends ViewPager {
    private boolean canScroll;

    public SViewPager(Context context) {
        super(context);
        this.canScroll = false;
    }

    public SViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.canScroll = false;
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.canScroll) {
            try {
                return super.onInterceptTouchEvent(motionEvent);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.canScroll) {
            return super.onTouchEvent(motionEvent);
        }
        return false;
    }

    public void toggleLock() {
        this.canScroll = !this.canScroll;
    }

    public void setCanScroll(boolean z) {
        this.canScroll = z;
    }

    public boolean isCanScroll() {
        return this.canScroll;
    }
}
