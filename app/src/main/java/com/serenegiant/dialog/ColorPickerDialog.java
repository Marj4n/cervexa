package com.serenegiant.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.serenegiant.common.R;
import com.serenegiant.widget.ColorPickerView;

@Deprecated
/* loaded from: classes.dex */
public class ColorPickerDialog extends DialogFragment {
    private static final boolean DEBUG = false;
    private static final int DEFAULT_COLOR = -1;
    private static final String KEY_COLOR_CURRENT = "current_color";
    private static final String KEY_COLOR_INIT = "initial_color";
    private static final String KEY_TITLE_ID = "title_id";
    private static final String TAG = "ColorPickerDialog";
    private boolean isCanceled;
    private OnColorChangedListener mListener;
    private int mTitleResId;
    private int mInitialColor = -1;
    private int mCurrentColor = -1;
    private final ColorPickerView.ColorPickerListener mColorPickerListener = new ColorPickerView.ColorPickerListener() { // from class: com.serenegiant.dialog.ColorPickerDialog.1
        @Override // com.serenegiant.widget.ColorPickerView.ColorPickerListener
        public void onColorChanged(ColorPickerView colorPickerView, int i) {
            if (ColorPickerDialog.this.mCurrentColor != i) {
                ColorPickerDialog.this.mCurrentColor = i;
                if (ColorPickerDialog.this.mListener != null) {
                    ColorPickerDialog.this.mListener.onColorChanged(ColorPickerDialog.this, i);
                }
            }
        }
    };
    private final DialogInterface.OnClickListener mOnClickListner = new DialogInterface.OnClickListener() { // from class: com.serenegiant.dialog.ColorPickerDialog.2
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == -2) {
                dialogInterface.cancel();
            } else {
                if (i != -1) {
                    return;
                }
                dialogInterface.dismiss();
            }
        }
    };

    public interface OnColorChangedListener {
        void onCancel(ColorPickerDialog colorPickerDialog);

        void onColorChanged(ColorPickerDialog colorPickerDialog, int i);

        void onDismiss(ColorPickerDialog colorPickerDialog, int i);
    }

    public static ColorPickerDialog show(Activity activity, int i, int i2) {
        ColorPickerDialog newInstance = newInstance(i, i2);
        newInstance.show(activity.getFragmentManager(), TAG);
        return newInstance;
    }

    public static ColorPickerDialog show(Fragment fragment, int i, int i2) {
        ColorPickerDialog newInstance = newInstance(i, i2);
        newInstance.setTargetFragment(fragment, 0);
        newInstance.show(fragment.getFragmentManager(), TAG);
        return newInstance;
    }

    public static ColorPickerDialog newInstance(int i, int i2) {
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
        colorPickerDialog.setArguments(i, i2);
        return colorPickerDialog;
    }

    public void setArguments(int i, int i2) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TITLE_ID, i);
        bundle.putInt(KEY_COLOR_INIT, i2);
        bundle.remove(KEY_COLOR_CURRENT);
        setArguments(bundle);
    }

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mTitleResId = arguments.getInt(KEY_TITLE_ID);
            int i = arguments.getInt(KEY_COLOR_INIT, -1);
            this.mInitialColor = i;
            this.mCurrentColor = i;
        }
        if (bundle != null) {
            this.mCurrentColor = bundle.getInt(KEY_COLOR_CURRENT, this.mInitialColor);
        }
    }

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle != null) {
            bundle.putInt(KEY_COLOR_CURRENT, this.mCurrentColor);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.isCanceled = false;
        try {
            this.mListener = (OnColorChangedListener) getTargetFragment();
        } catch (ClassCastException | NullPointerException unused) {
        }
        if (this.mListener == null) {
            try {
                this.mListener = (OnColorChangedListener) getParentFragment();
            } catch (ClassCastException | NullPointerException unused2) {
            }
        }
        if (this.mListener == null) {
            try {
                this.mListener = (OnColorChangedListener) activity;
            } catch (ClassCastException | NullPointerException unused3) {
            }
        }
        if (this.mListener == null) {
            Log.w(TAG, "must implement OnColorChangedListener");
        }
    }

    @Override // android.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        Activity activity = getActivity();
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(activity).inflate(R.layout.color_picker, (ViewGroup) null);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        ColorPickerView colorPickerView = new ColorPickerView(getActivity());
        colorPickerView.setColor(this.mCurrentColor);
        colorPickerView.setColorPickerListener(this.mColorPickerListener);
        frameLayout.addView(colorPickerView, layoutParams);
        AlertDialog.Builder negativeButton = new AlertDialog.Builder(activity).setPositiveButton(R.string.color_picker_select, this.mOnClickListner).setNegativeButton(R.string.color_picker_cancel, this.mOnClickListner);
        int i = this.mTitleResId;
        if (i == 0) {
            i = R.string.color_picker_default_title;
        }
        AlertDialog create = negativeButton.setTitle(i).setView(frameLayout).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    @Override // android.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        this.isCanceled = true;
    }

    @Override // android.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        OnColorChangedListener onColorChangedListener = this.mListener;
        if (onColorChangedListener != null) {
            if (this.isCanceled) {
                onColorChangedListener.onCancel(this);
            } else {
                onColorChangedListener.onDismiss(this, this.mCurrentColor);
            }
        }
    }
}
