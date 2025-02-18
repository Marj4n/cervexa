package com.serenegiant.widget;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Checkable;
import android.widget.LinearLayout;

/* loaded from: classes2.dex */
public class CheckableLinearLayout extends LinearLayout implements Checkable, Touchable {
    private static final int[] CHECKED_STATE_SET = {R.attr.state_checked};
    private boolean mChecked;
    private float mTouchX;
    private float mTouchY;

    public CheckableLinearLayout(Context context) {
        this(context, null);
    }

    public CheckableLinearLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CheckableLinearLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return this.mChecked;
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z) {
        if (this.mChecked != z) {
            this.mChecked = z;
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                KeyEvent.Callback childAt = getChildAt(i);
                if (childAt instanceof Checkable) {
                    ((Checkable) childAt).setChecked(z);
                }
            }
            refreshDrawableState();
        }
    }

    @Override // android.widget.Checkable
    public void toggle() {
        setChecked(!this.mChecked);
    }

    @Override // android.view.ViewGroup, android.view.View
    public int[] onCreateDrawableState(int i) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i + 1);
        if (isChecked()) {
            mergeDrawableStates(onCreateDrawableState, CHECKED_STATE_SET);
        }
        return onCreateDrawableState;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        this.mTouchX = motionEvent.getX();
        this.mTouchY = motionEvent.getY();
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // com.serenegiant.widget.Touchable
    public float touchX() {
        return this.mTouchX;
    }

    @Override // com.serenegiant.widget.Touchable
    public float touchY() {
        return this.mTouchY;
    }
}
