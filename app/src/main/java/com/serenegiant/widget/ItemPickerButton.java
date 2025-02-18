package com.serenegiant.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatImageButton;
import com.serenegiant.common.R;

/* loaded from: classes2.dex */
public final class ItemPickerButton extends AppCompatImageButton {
    private ItemPicker mNumberPicker;

    public ItemPickerButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public ItemPickerButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ItemPickerButton(Context context) {
        super(context);
    }

    public void setNumberPicker(ItemPicker itemPicker) {
        this.mNumberPicker = itemPicker;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        cancelLongpressIfRequired(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent motionEvent) {
        cancelLongpressIfRequired(motionEvent);
        return super.onTrackballEvent(motionEvent);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i == 23 || i == 66) {
            cancelLongpress();
        }
        return super.onKeyUp(i, keyEvent);
    }

    private void cancelLongpressIfRequired(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
            cancelLongpress();
        }
    }

    private void cancelLongpress() {
        if (R.id.increment == getId()) {
            this.mNumberPicker.cancelIncrement();
        } else if (R.id.decrement == getId()) {
            this.mNumberPicker.cancelDecrement();
        }
    }
}
