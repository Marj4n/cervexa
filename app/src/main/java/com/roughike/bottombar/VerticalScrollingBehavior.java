package com.roughike.bottombar;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.WindowInsetsCompat;

/* loaded from: classes.dex */
abstract class VerticalScrollingBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    private int overScrollDirection;
    private int scrollDirection;
    private int totalDy;
    private int totalDyUnconsumed;

    abstract void onDirectionNestedPreScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i, int i2, int[] iArr, int i3);

    abstract boolean onNestedDirectionFling(CoordinatorLayout coordinatorLayout, V v, View view, float f, float f2, int i);

    abstract void onNestedVerticalOverScroll(CoordinatorLayout coordinatorLayout, V v, int i, int i2, int i3);

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, View view2, int i) {
        return (i & 2) != 0;
    }

    VerticalScrollingBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.totalDyUnconsumed = 0;
        this.totalDy = 0;
        this.overScrollDirection = 0;
        this.scrollDirection = 0;
    }

    VerticalScrollingBehavior() {
        this.totalDyUnconsumed = 0;
        this.totalDy = 0;
        this.overScrollDirection = 0;
        this.scrollDirection = 0;
    }

    int getOverScrollDirection() {
        return this.overScrollDirection;
    }

    int getScrollDirection() {
        return this.scrollDirection;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, V v, View view, View view2, int i) {
        super.onNestedScrollAccepted(coordinatorLayout, v, view, view2, i);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view) {
        super.onStopNestedScroll(coordinatorLayout, v, view);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i, int i2, int i3, int i4) {
        super.onNestedScroll(coordinatorLayout, v, view, i, i2, i3, i4);
        if (i4 > 0 && this.totalDyUnconsumed < 0) {
            this.totalDyUnconsumed = 0;
            this.overScrollDirection = 1;
        } else if (i4 < 0 && this.totalDyUnconsumed > 0) {
            this.totalDyUnconsumed = 0;
            this.overScrollDirection = -1;
        }
        int i5 = this.totalDyUnconsumed + i4;
        this.totalDyUnconsumed = i5;
        onNestedVerticalOverScroll(coordinatorLayout, v, this.overScrollDirection, i2, i5);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i, int i2, int[] iArr) {
        super.onNestedPreScroll(coordinatorLayout, v, view, i, i2, iArr);
        if (i2 > 0 && this.totalDy < 0) {
            this.totalDy = 0;
            this.scrollDirection = 1;
        } else if (i2 < 0 && this.totalDy > 0) {
            this.totalDy = 0;
            this.scrollDirection = -1;
        }
        this.totalDy += i2;
        onDirectionNestedPreScroll(coordinatorLayout, v, view, i, i2, iArr, this.scrollDirection);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, V v, View view, float f, float f2, boolean z) {
        super.onNestedFling(coordinatorLayout, v, view, f, f2, z);
        int i = f2 > 0.0f ? 1 : -1;
        this.scrollDirection = i;
        return onNestedDirectionFling(coordinatorLayout, v, view, f, f2, i);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V v, View view, float f, float f2) {
        return super.onNestedPreFling(coordinatorLayout, v, view, f, f2);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public WindowInsetsCompat onApplyWindowInsets(CoordinatorLayout coordinatorLayout, V v, WindowInsetsCompat windowInsetsCompat) {
        return super.onApplyWindowInsets(coordinatorLayout, v, windowInsetsCompat);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, V v) {
        return super.onSaveInstanceState(coordinatorLayout, v);
    }
}
