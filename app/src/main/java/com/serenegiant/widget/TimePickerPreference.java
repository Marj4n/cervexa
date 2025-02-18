package com.serenegiant.widget;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/* loaded from: classes2.dex */
public class TimePickerPreference extends DialogPreference {
    private final Calendar calendar;
    private final long mDefaultValue;
    private TimePicker picker;

    public TimePickerPreference(Context context) {
        this(context, null);
    }

    public TimePickerPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.dialogPreferenceStyle);
    }

    public TimePickerPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.picker = null;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.serenegiant.common.R.styleable.TimePicker, i, 0);
        this.mDefaultValue = (long) obtainStyledAttributes.getFloat(com.serenegiant.common.R.styleable.TimePicker_TimePickerDefaultValue, -1.0f);
        obtainStyledAttributes.recycle();
        setPositiveButtonText(R.string.ok);
        setNegativeButtonText(R.string.cancel);
        this.calendar = new GregorianCalendar();
    }

    @Override // android.preference.DialogPreference
    protected View onCreateDialogView() {
        TimePicker timePicker = new TimePicker(getContext());
        this.picker = timePicker;
        timePicker.setIs24HourView(true);
        return this.picker;
    }

    @Override // android.preference.DialogPreference
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        this.picker.setCurrentHour(Integer.valueOf(this.calendar.get(11)));
        this.picker.setCurrentMinute(Integer.valueOf(this.calendar.get(12)));
    }

    @Override // android.preference.DialogPreference
    protected void onDialogClosed(boolean z) {
        super.onDialogClosed(z);
        if (z) {
            this.calendar.set(11, this.picker.getCurrentHour().intValue());
            this.calendar.set(12, this.picker.getCurrentMinute().intValue());
            setSummary(getSummary());
            if (callChangeListener(Long.valueOf(this.calendar.getTimeInMillis()))) {
                persistLong(this.calendar.getTimeInMillis());
                notifyChanged();
            }
        }
    }

    @Override // android.preference.Preference
    protected Object onGetDefaultValue(TypedArray typedArray, int i) {
        return typedArray.getString(i);
    }

    @Override // android.preference.Preference
    protected void onSetInitialValue(boolean z, Object obj) {
        long j = this.mDefaultValue;
        if (j <= 0) {
            j = System.currentTimeMillis();
        }
        if (z) {
            try {
                j = getPersistedLong(j);
            } catch (Exception unused) {
            }
            this.calendar.setTimeInMillis(j);
        } else if (obj != null) {
            this.calendar.setTimeInMillis(Long.parseLong((String) obj));
        } else {
            this.calendar.setTimeInMillis(j);
        }
        setSummary(getSummary());
    }

    @Override // android.preference.Preference
    public CharSequence getSummary() {
        if (this.calendar == null) {
            return null;
        }
        return DateFormat.getTimeFormat(getContext()).format(new Date(this.calendar.getTimeInMillis()));
    }
}
