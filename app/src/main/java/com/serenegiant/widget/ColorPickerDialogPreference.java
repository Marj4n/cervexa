package com.serenegiant.widget;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.internal.view.SupportMenu;
import com.serenegiant.widget.ColorPickerView;

/* loaded from: classes2.dex */
public class ColorPickerDialogPreference extends DialogPreference {
    private static final String TAG = ColorPickerDialogPreference.class.getSimpleName();
    private boolean changed;
    private int mColor;
    private final ColorPickerView.ColorPickerListener mColorPickerListener;

    public ColorPickerDialogPreference(Context context) {
        this(context, null, 0);
    }

    public ColorPickerDialogPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ColorPickerDialogPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mColor = SupportMenu.CATEGORY_MASK;
        this.mColorPickerListener = new ColorPickerView.ColorPickerListener() { // from class: com.serenegiant.widget.ColorPickerDialogPreference.1
            @Override // com.serenegiant.widget.ColorPickerView.ColorPickerListener
            public void onColorChanged(ColorPickerView colorPickerView, int i2) {
                if (ColorPickerDialogPreference.this.mColor != i2) {
                    ColorPickerDialogPreference.this.mColor = i2;
                    ColorPickerDialogPreference.this.changed = true;
                }
            }
        };
    }

    @Override // android.preference.Preference
    protected void onBindView(View view) {
        super.onBindView(view);
        this.mColor = getPersistedInt(this.mColor);
    }

    @Override // android.preference.DialogPreference
    protected View onCreateDialogView() {
        ColorPickerView colorPickerView = new ColorPickerView(getContext());
        colorPickerView.setColorPickerListener(this.mColorPickerListener);
        return colorPickerView;
    }

    @Override // android.preference.DialogPreference
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        int persistedInt = getPersistedInt(this.mColor);
        this.mColor = persistedInt;
        this.changed = false;
        if (view instanceof ColorPickerView) {
            ((ColorPickerView) view).setColor(persistedInt);
        }
    }

    @Override // android.preference.DialogPreference
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

    @Override // android.preference.Preference
    protected void onSetInitialValue(boolean z, Object obj) {
        if (z) {
            this.mColor = getPersistedInt(this.mColor);
            return;
        }
        int intValue = ((Integer) obj).intValue();
        this.mColor = intValue;
        persistInt(intValue);
    }

    public int getValue() {
        return this.mColor;
    }
}
