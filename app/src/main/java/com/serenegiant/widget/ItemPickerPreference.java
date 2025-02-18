package com.serenegiant.widget;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.serenegiant.widget.ItemPicker;

/* loaded from: classes2.dex */
public final class ItemPickerPreference extends Preference {
    private static final boolean DEBUG = false;
    private static final String TAG = ItemPickerPreference.class.getSimpleName();
    private ItemPicker mItemPicker;
    private int mMaxValue;
    private int mMinValue;
    private final ItemPicker.OnChangedListener mOnChangeListener;
    private int preferenceValue;

    public ItemPickerPreference(Context context) {
        super(context);
        this.mMinValue = 1;
        this.mMaxValue = 100;
        this.mOnChangeListener = new ItemPicker.OnChangedListener() { // from class: com.serenegiant.widget.ItemPickerPreference.1
            @Override // com.serenegiant.widget.ItemPicker.OnChangedListener
            public void onChanged(ItemPicker itemPicker, int i, int i2) {
                ItemPickerPreference.this.callChangeListener(Integer.valueOf(i2));
                ItemPickerPreference.this.preferenceValue = i2;
                ItemPickerPreference itemPickerPreference = ItemPickerPreference.this;
                itemPickerPreference.persistInt(itemPickerPreference.preferenceValue);
            }
        };
    }

    public ItemPickerPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMinValue = 1;
        this.mMaxValue = 100;
        this.mOnChangeListener = new ItemPicker.OnChangedListener() { // from class: com.serenegiant.widget.ItemPickerPreference.1
            @Override // com.serenegiant.widget.ItemPicker.OnChangedListener
            public void onChanged(ItemPicker itemPicker, int i, int i2) {
                ItemPickerPreference.this.callChangeListener(Integer.valueOf(i2));
                ItemPickerPreference.this.preferenceValue = i2;
                ItemPickerPreference itemPickerPreference = ItemPickerPreference.this;
                itemPickerPreference.persistInt(itemPickerPreference.preferenceValue);
            }
        };
    }

    public ItemPickerPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mMinValue = 1;
        this.mMaxValue = 100;
        this.mOnChangeListener = new ItemPicker.OnChangedListener() { // from class: com.serenegiant.widget.ItemPickerPreference.1
            @Override // com.serenegiant.widget.ItemPicker.OnChangedListener
            public void onChanged(ItemPicker itemPicker, int i2, int i22) {
                ItemPickerPreference.this.callChangeListener(Integer.valueOf(i22));
                ItemPickerPreference.this.preferenceValue = i22;
                ItemPickerPreference itemPickerPreference = ItemPickerPreference.this;
                itemPickerPreference.persistInt(itemPickerPreference.preferenceValue);
            }
        };
    }

    @Override // android.preference.Preference
    protected void onBindView(View view) {
        RelativeLayout relativeLayout;
        super.onBindView(view);
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount() - 1;
        while (true) {
            if (childCount < 0) {
                relativeLayout = null;
                break;
            }
            View childAt = viewGroup.getChildAt(childCount);
            if (childAt instanceof RelativeLayout) {
                relativeLayout = (RelativeLayout) childAt;
                break;
            }
            childCount--;
        }
        this.mItemPicker = new ItemPicker(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(3, R.id.summary);
        relativeLayout.addView(this.mItemPicker, layoutParams);
        this.mItemPicker.setRange(this.mMinValue, this.mMaxValue);
        this.mItemPicker.setValue(this.preferenceValue);
        int value = this.mItemPicker.getValue();
        this.preferenceValue = value;
        persistInt(value);
        this.mItemPicker.setOnChangeListener(this.mOnChangeListener);
    }

    @Override // android.preference.Preference
    protected Object onGetDefaultValue(TypedArray typedArray, int i) {
        return Integer.valueOf(typedArray.getInt(i, 0));
    }

    @Override // android.preference.Preference
    protected void onSetInitialValue(boolean z, Object obj) {
        int i = this.preferenceValue;
        if (obj instanceof Integer) {
            i = ((Integer) obj).intValue();
        } else if (obj instanceof String) {
            try {
                i = Integer.parseInt((String) obj);
            } catch (Exception unused) {
            }
        }
        if (z) {
            this.preferenceValue = getPersistedInt(i);
        } else {
            this.preferenceValue = i;
            persistInt(i);
        }
    }

    public void setRange(int i, int i2) {
        if (i > i2) {
            i2 = i;
            i = i2;
        }
        if (this.mMinValue == i && this.mMaxValue == i2) {
            return;
        }
        this.mMaxValue = i2;
        this.mMinValue = i;
        ItemPicker itemPicker = this.mItemPicker;
        if (itemPicker != null) {
            itemPicker.setRange(i, i2);
            this.mItemPicker.setValue(this.preferenceValue);
            int value = this.mItemPicker.getValue();
            this.preferenceValue = value;
            persistInt(value);
        }
    }
}
