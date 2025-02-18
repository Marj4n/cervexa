package com.serenegiant.widget;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.RelativeLayout;

/* loaded from: classes2.dex */
public class CheckableRelativeLayout extends RelativeLayout implements Checkable, Touchable {
    private static final int[] CHECKED_STATE_SET = {R.attr.state_checked};
    private boolean mIsChecked;
    private float mTouchX;
    private float mTouchY;

    public CheckableRelativeLayout(Context context) {
        this(context, null);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return this.mIsChecked;
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z) {
        if (this.mIsChecked != z) {
            this.mIsChecked = z;
            updateChildState(this, z);
            refreshDrawableState();
        }
    }

    protected void updateChildState(ViewGroup viewGroup, boolean z) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            KeyEvent.Callback childAt = viewGroup.getChildAt(i);
            if (childAt instanceof Checkable) {
                ((Checkable) childAt).setChecked(z);
            }
        }
    }

    @Override // android.widget.Checkable
    public void toggle() {
        setChecked(!this.mIsChecked);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected int[] onCreateDrawableState(int i) {
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
