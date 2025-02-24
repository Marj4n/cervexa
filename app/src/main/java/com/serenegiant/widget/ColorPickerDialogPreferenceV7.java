package com.serenegiant.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.internal.view.SupportMenu;
import androidx.preference.PreferenceViewHolder;
import com.serenegiant.widget.ColorPickerView;

/* loaded from: classes2.dex */
public class ColorPickerDialogPreferenceV7 extends DialogPreferenceV7 {
    private static final String TAG = ColorPickerDialogPreferenceV7.class.getSimpleName();
    private boolean changed;
    private int mColor;
    private final ColorPickerView.ColorPickerListener mColorPickerListener;

    public ColorPickerDialogPreferenceV7(Context context) {
        this(context, null, 0);
    }

    public ColorPickerDialogPreferenceV7(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @SuppressLint("RestrictedApi")
    public ColorPickerDialogPreferenceV7(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mColor = SupportMenu.CATEGORY_MASK;
        this.mColorPickerListener = new ColorPickerView.ColorPickerListener() { // from class: com.serenegiant.widget.ColorPickerDialogPreferenceV7.1
            @Override // com.serenegiant.widget.ColorPickerView.ColorPickerListener
            public void onColorChanged(ColorPickerView colorPickerView, int i2) {
                if (ColorPickerDialogPreferenceV7.this.mColor != i2) {
                    ColorPickerDialogPreferenceV7.this.mColor = i2;
                    ColorPickerDialogPreferenceV7.this.changed = true;
                }
            }
        };
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mColor = getPersistedInt(this.mColor);
    }

    @Override // com.serenegiant.widget.DialogPreferenceV7
    protected View onCreateDialogView() {
        ColorPickerView colorPickerView = new ColorPickerView(getContext());
        colorPickerView.setColorPickerListener(this.mColorPickerListener);
        return colorPickerView;
    }

    @Override // com.serenegiant.widget.DialogPreferenceV7
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        int persistedInt = getPersistedInt(this.mColor);
        this.mColor = persistedInt;
        this.changed = false;
        if (view instanceof ColorPickerView) {
            ((ColorPickerView) view).setColor(persistedInt);
        }
    }

    @Override // com.serenegiant.widget.DialogPreferenceV7
    protected void onDialogClosed(boolean z) {
        if (z || this.changed) {
            setSummary(getSummary());
            if (callChangeListener(Integer.valueOf(this.mColor))) {
                persistInt(this.mColor);
                notifyChanged();
            }
        }
        super.onDialogClosed(z || this.changed);
    }

    protected void onSetInitialValue(Object obj) {
        if (obj != null) {
            this.mColor = ((Integer) obj).intValue();
        }
        persistInt(this.mColor);
    }

    public int getValue() {
        return this.mColor;
    }
}
