package com.serenegiant.widget;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

/* loaded from: classes2.dex */
public class MultilineLabelPreferenceV7 extends Preference {
    private static final boolean DEBUG = false;
    private static final String TAG = MultilineLabelPreferenceV7.class.getSimpleName();

    public MultilineLabelPreferenceV7(Context context) {
        super(context);
    }

    public MultilineLabelPreferenceV7(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MultilineLabelPreferenceV7(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        try {
            ((TextView) preferenceViewHolder.findViewById(R.id.summary)).setSingleLine(false);
        } catch (Exception unused) {
        }
    }
}
