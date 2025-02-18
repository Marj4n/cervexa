package com.serenegiant.widget;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import androidx.appcompat.widget.AppCompatImageButton;

/* loaded from: classes2.dex */
public class CheckableImageButton extends AppCompatImageButton implements Checkable {
    private static final boolean DEBUG = false;
    private boolean mIsChecked;
    private static final String TAG = CheckableImageButton.class.getSimpleName();
    private static final int[] CHECKED_STATE_SET = {R.attr.state_checked};

    public CheckableImageButton(Context context) {
        this(context, null, 0);
    }

    public CheckableImageButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CheckableImageButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z) {
        if (this.mIsChecked != z) {
            this.mIsChecked = z;
            refreshDrawableState();
        }
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return this.mIsChecked;
    }

    @Override // android.widget.Checkable
    public void toggle() {
        setChecked(!this.mIsChecked);
    }

    @Override // android.widget.ImageView, android.view.View
    public int[] onCreateDrawableState(int i) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i + 1);
        if (isChecked()) {
            mergeDrawableStates(onCreateDrawableState, CHECKED_STATE_SET);
        }
        return onCreateDrawableState;
    }
}
