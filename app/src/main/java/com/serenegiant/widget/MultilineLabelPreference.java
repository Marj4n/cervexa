package com.serenegiant.widget;

import android.R;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/* loaded from: classes2.dex */
public class MultilineLabelPreference extends Preference {
    private static final boolean DEBUG = false;
    private static final String TAG = MultilineLabelPreference.class.getSimpleName();

    public MultilineLabelPreference(Context context) {
        super(context);
    }

    public MultilineLabelPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MultilineLabelPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.preference.Preference
    protected void onBindView(View view) {
        super.onBindView(view);
        try {
            ((TextView) view.findViewById(R.id.summary)).setSingleLine(false);
        } catch (Exception unused) {
        }
    }
}
