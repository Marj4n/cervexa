package com.serenegiant.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.R;

/* loaded from: classes2.dex */
public class DialogPreferenceV7 extends Preference implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
    private AlertDialog.Builder mBuilder;
    private Dialog mDialog;
    private Drawable mDialogIcon;
    private int mDialogLayoutResId;
    private CharSequence mDialogMessage;
    private CharSequence mDialogTitle;
    private CharSequence mNegativeButtonText;
    private CharSequence mPositiveButtonText;
    private int mWhichButtonClicked;

    protected boolean needInputMethod() {
        return false;
    }

    protected void onDialogClosed(boolean z) {
    }

    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
    }

    public DialogPreferenceV7(Context context) {
        this(context, null, 0, 0);
    }

    public DialogPreferenceV7(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0);
    }

    public DialogPreferenceV7(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public DialogPreferenceV7(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.DialogPreference, i, i2);
        String string = obtainStyledAttributes.getString(R.styleable.DialogPreference_dialogTitle);
        this.mDialogTitle = string;
        if (string == null) {
            this.mDialogTitle = getTitle();
        }
        this.mDialogMessage = obtainStyledAttributes.getString(R.styleable.DialogPreference_dialogMessage);
        this.mDialogIcon = obtainStyledAttributes.getDrawable(R.styleable.DialogPreference_dialogIcon);
        this.mPositiveButtonText = obtainStyledAttributes.getString(R.styleable.DialogPreference_positiveButtonText);
        this.mNegativeButtonText = obtainStyledAttributes.getString(R.styleable.DialogPreference_negativeButtonText);
        this.mDialogLayoutResId = obtainStyledAttributes.getResourceId(R.styleable.DialogPreference_dialogLayout, this.mDialogLayoutResId);
        obtainStyledAttributes.recycle();
    }

    public void setDialogTitle(CharSequence charSequence) {
        this.mDialogTitle = charSequence;
    }

    public void setDialogTitle(int i) {
        setDialogTitle(getContext().getString(i));
    }

    public CharSequence getDialogTitle() {
        return this.mDialogTitle;
    }

    public void setDialogMessage(CharSequence charSequence) {
        this.mDialogMessage = charSequence;
    }

    public void setDialogMessage(int i) {
        setDialogMessage(getContext().getString(i));
    }

    public CharSequence getDialogMessage() {
        return this.mDialogMessage;
    }

    public void setDialogIcon(Drawable drawable) {
        this.mDialogIcon = drawable;
    }

    public void setDialogIcon(int i) {
        this.mDialogIcon = getContext().getResources().getDrawable(i);
    }

    public Drawable getDialogIcon() {
        return this.mDialogIcon;
    }

    public void setPositiveButtonText(CharSequence charSequence) {
        this.mPositiveButtonText = charSequence;
    }

    public void setPositiveButtonText(int i) {
        setPositiveButtonText(getContext().getString(i));
    }

    public CharSequence getPositiveButtonText() {
        return this.mPositiveButtonText;
    }

    public void setNegativeButtonText(CharSequence charSequence) {
        this.mNegativeButtonText = charSequence;
    }

    public void setNegativeButtonText(int i) {
        setNegativeButtonText(getContext().getString(i));
    }

    public CharSequence getNegativeButtonText() {
        return this.mNegativeButtonText;
    }

    public void setDialogLayoutResource(int i) {
        this.mDialogLayoutResId = i;
    }

    public int getDialogLayoutResource() {
        return this.mDialogLayoutResId;
    }

    protected void onClick() {
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            showDialog(null);
        }
    }

    protected void showDialog(Bundle bundle) {
        Context context = getContext();
        this.mWhichButtonClicked = -2;
        this.mBuilder = new AlertDialog.Builder(context).setTitle(this.mDialogTitle).setIcon(this.mDialogIcon).setPositiveButton(this.mPositiveButtonText, this).setNegativeButton(this.mNegativeButtonText, this);
        View onCreateDialogView = onCreateDialogView();
        if (onCreateDialogView != null) {
            onBindDialogView(onCreateDialogView);
            this.mBuilder.setView(onCreateDialogView);
        } else {
            this.mBuilder.setMessage(this.mDialogMessage);
        }
        onPrepareDialogBuilder(this.mBuilder);
        AlertDialog create = this.mBuilder.create();
        this.mDialog = create;
        if (bundle != null) {
            create.onRestoreInstanceState(bundle);
        }
        if (needInputMethod()) {
            requestInputMethod(create);
        }
        create.setOnDismissListener(this);
        create.show();
    }

    private void requestInputMethod(Dialog dialog) {
        dialog.getWindow().setSoftInputMode(5);
    }

    protected View onCreateDialogView() {
        if (this.mDialogLayoutResId == 0) {
            return null;
        }
        return LayoutInflater.from(this.mBuilder.getContext()).inflate(this.mDialogLayoutResId, (ViewGroup) null);
    }

    protected void onBindDialogView(View view) {
        View findViewById = view.findViewById(com.serenegiant.common.R.id.message);
        if (findViewById != null) {
            CharSequence dialogMessage = getDialogMessage();
            int i = 8;
            if (!TextUtils.isEmpty(dialogMessage)) {
                if (findViewById instanceof TextView) {
                    ((TextView) findViewById).setText(dialogMessage);
                }
                i = 0;
            }
            if (findViewById.getVisibility() != i) {
                findViewById.setVisibility(i);
            }
        }
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        this.mWhichButtonClicked = i;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        this.mDialog = null;
        onDialogClosed(this.mWhichButtonClicked == -1);
    }

    public void onAttached() {
        super.onAttached();
    }

    public void onDetached() {
        onActivityDestroy();
        super.onDetached();
    }

    public Dialog getDialog() {
        return this.mDialog;
    }

    public void onActivityDestroy() {
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        this.mDialog.dismiss();
    }

    protected Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            return onSaveInstanceState;
        }
        SavedState savedState = new SavedState(onSaveInstanceState);
        savedState.isDialogShowing = true;
        savedState.dialogBundle = this.mDialog.onSaveInstanceState();
        return savedState;
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable == null || !parcelable.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (savedState.isDialogShowing) {
            showDialog(savedState.dialogBundle);
        }
    }

    private static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.serenegiant.widget.DialogPreferenceV7.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        Bundle dialogBundle;
        boolean isDialogShowing;

        public SavedState(Parcel parcel) {
            super(parcel);
            this.isDialogShowing = parcel.readInt() == 1;
            this.dialogBundle = parcel.readBundle();
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.isDialogShowing ? 1 : 0);
            parcel.writeBundle(this.dialogBundle);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }
}
