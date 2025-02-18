package com.jieli.stream.dv.running2.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.base.BaseDialogFragment;
import com.jieli.stream.dv.running2.util.Dbug;

/* loaded from: classes.dex */
public class NotifyDialog extends BaseDialogFragment {
    private static final String KEY_CHECK_BOX = "check_box";
    public static final int PB_STYLE_HORIZONTAL = 2;
    public static final int PB_STYLE_ROUND = 1;
    private Bundle bundle;
    private CheckBox mCheckBox;
    private TextView mConfirmButtonText;
    private TextView mContent;
    private View mDividerView;
    private ProgressBar mHorizontalProgressBar;
    private TextView mNegativeButtonText;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnConfirmClickListener mOnConfirmClickListener;
    private OnDismissDialogListener mOnDismissDialogListener;
    private OnNegativeClickListener mOnNegativeClickListener;
    private OnPositiveClickListener mOnPositiveClickListener;
    private TextView mPositiveButtonText;
    private ProgressBar mProgressBar;
    private TextView mTitle;
    private final String tag = getClass().getSimpleName();
    private int mProgressBarStyle = 0;
    private boolean isLeftGravity = false;
    private int mTitleId = 0;
    private int mContentId = 0;
    private int mNegativeButtonId = 0;
    private int mConfirmButtonId = 0;
    private int mPositiveButtonId = 0;
    private String title = "";
    private String content = "";
    private boolean isShowCheckBox = false;
    private final CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.4
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            if (NotifyDialog.this.mOnCheckedChangeListener != null) {
                NotifyDialog.this.mOnCheckedChangeListener.onCheckedChanged(z);
            }
        }
    };

    public interface OnCheckedChangeListener {
        void onCheckedChanged(boolean z);
    }

    public interface OnConfirmClickListener {
        void onClick();
    }

    public interface OnDismissDialogListener {
        void onDismiss();
    }

    public interface OnNegativeClickListener {
        void onClick();
    }

    public interface OnPositiveClickListener {
        void onClick();
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseDialogFragment
    public Bundle getBundle() {
        return this.bundle;
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseDialogFragment
    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public static NotifyDialog newInstance(int i, int i2, int i3) {
        Bundle bundle = new Bundle();
        bundle.putInt("mTitleId", i);
        bundle.putInt("mProgressBarStyle", i2);
        bundle.putInt("mContentId", i3);
        NotifyDialog notifyDialog = new NotifyDialog();
        notifyDialog.setArguments(bundle);
        return notifyDialog;
    }

    public static NotifyDialog newInstance(int i, boolean z, int i2) {
        Bundle bundle = new Bundle();
        bundle.putInt("mTitleId", i);
        if (z) {
            bundle.putInt("mProgressBarStyle", 1);
        } else {
            bundle.putInt("mProgressBarStyle", 0);
        }
        bundle.putInt("mContentId", i2);
        NotifyDialog notifyDialog = new NotifyDialog();
        notifyDialog.setArguments(bundle);
        return notifyDialog;
    }

    public static NotifyDialog newInstance(int i, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putInt("mTitleId", i);
        if (z) {
            bundle.putInt("mProgressBarStyle", 1);
        } else {
            bundle.putInt("mProgressBarStyle", 0);
        }
        NotifyDialog notifyDialog = new NotifyDialog();
        notifyDialog.setArguments(bundle);
        return notifyDialog;
    }

    public static NotifyDialog newInstance(String str, boolean z, String str2) {
        Bundle bundle = new Bundle();
        bundle.putInt("mTitleId", -1);
        bundle.putString("title", str);
        if (z) {
            bundle.putInt("mProgressBarStyle", 1);
        } else {
            bundle.putInt("mProgressBarStyle", 0);
        }
        bundle.putString("content", str2);
        bundle.putInt("mContentId", -1);
        NotifyDialog notifyDialog = new NotifyDialog();
        notifyDialog.setArguments(bundle);
        return notifyDialog;
    }

    public static NotifyDialog newInstance(int i, boolean z, int i2, int i3, OnConfirmClickListener onConfirmClickListener) {
        Bundle bundle = new Bundle();
        bundle.putInt("mTitleId", i);
        if (z) {
            bundle.putInt("mProgressBarStyle", 1);
        } else {
            bundle.putInt("mProgressBarStyle", 0);
        }
        bundle.putInt("mContentId", i2);
        bundle.putInt("mConfirmButtonId", i3);
        NotifyDialog notifyDialog = new NotifyDialog();
        notifyDialog.mOnConfirmClickListener = onConfirmClickListener;
        notifyDialog.setArguments(bundle);
        return notifyDialog;
    }

    public static NotifyDialog newInstance(int i, int i2, int i3, OnConfirmClickListener onConfirmClickListener) {
        Bundle bundle = new Bundle();
        bundle.putInt("mTitleId", i);
        bundle.putInt("mContentId", i2);
        bundle.putInt("mConfirmButtonId", i3);
        NotifyDialog notifyDialog = new NotifyDialog();
        notifyDialog.mOnConfirmClickListener = onConfirmClickListener;
        notifyDialog.setArguments(bundle);
        return notifyDialog;
    }

    public static NotifyDialog newInstance(String str, String str2, int i, OnConfirmClickListener onConfirmClickListener) {
        Bundle bundle = new Bundle();
        bundle.putInt("mTitleId", -1);
        bundle.putString("title", str);
        bundle.putInt("mContentId", -1);
        bundle.putString("content", str2);
        bundle.putInt("mConfirmButtonId", i);
        NotifyDialog notifyDialog = new NotifyDialog();
        notifyDialog.mOnConfirmClickListener = onConfirmClickListener;
        notifyDialog.setArguments(bundle);
        return notifyDialog;
    }

    public static NotifyDialog newInstance(int i, int i2, int i3, int i4, OnNegativeClickListener onNegativeClickListener, OnPositiveClickListener onPositiveClickListener) {
        Bundle bundle = new Bundle();
        bundle.putInt("mTitleId", i);
        bundle.putInt("mContentId", i2);
        bundle.putInt("mNegativeButtonId", i3);
        bundle.putInt("mPositiveButtonId", i4);
        NotifyDialog notifyDialog = new NotifyDialog();
        notifyDialog.mOnNegativeClickListener = onNegativeClickListener;
        notifyDialog.mOnPositiveClickListener = onPositiveClickListener;
        notifyDialog.setArguments(bundle);
        return notifyDialog;
    }

    public static NotifyDialog newInstance(String str, String str2, int i, int i2, OnNegativeClickListener onNegativeClickListener, OnPositiveClickListener onPositiveClickListener) {
        Bundle bundle = new Bundle();
        bundle.putInt("mTitleId", -1);
        bundle.putString("title", str);
        bundle.putInt("mContentId", -1);
        bundle.putString("content", str2);
        bundle.putInt("mNegativeButtonId", i);
        bundle.putInt("mPositiveButtonId", i2);
        NotifyDialog notifyDialog = new NotifyDialog();
        notifyDialog.mOnNegativeClickListener = onNegativeClickListener;
        notifyDialog.mOnPositiveClickListener = onPositiveClickListener;
        notifyDialog.setArguments(bundle);
        return notifyDialog;
    }

    public static NotifyDialog newInstance(int i, int i2, boolean z, int i3, int i4, OnNegativeClickListener onNegativeClickListener, OnPositiveClickListener onPositiveClickListener) {
        Bundle bundle = new Bundle();
        bundle.putInt("mTitleId", i);
        bundle.putBoolean(KEY_CHECK_BOX, z);
        bundle.putInt("mContentId", i2);
        bundle.putInt("mNegativeButtonId", i3);
        bundle.putInt("mPositiveButtonId", i4);
        NotifyDialog notifyDialog = new NotifyDialog();
        notifyDialog.mOnNegativeClickListener = onNegativeClickListener;
        notifyDialog.mOnPositiveClickListener = onPositiveClickListener;
        notifyDialog.setArguments(bundle);
        return notifyDialog;
    }

    public void setContentTextLeft(boolean z) {
        this.isLeftGravity = z;
    }

    public void setContent(int i) {
        this.mContentId = i;
        TextView textView = this.mContent;
        if (textView != null) {
            textView.setText(i);
        }
    }

    public void setContent(String str) {
        this.mContentId = -1;
        this.content = str;
        TextView textView = this.mContent;
        if (textView != null) {
            textView.setText(str);
        }
    }

    public void setProgressBarVisibility(int i) {
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar != null) {
            progressBar.setVisibility(i);
        }
    }

    public void setProgress(int i) {
        ProgressBar progressBar = this.mHorizontalProgressBar;
        if (progressBar == null || progressBar.getVisibility() != 0) {
            return;
        }
        this.mHorizontalProgressBar.setProgress(i);
    }

    public void setProgressMax(int i) {
        ProgressBar progressBar = this.mHorizontalProgressBar;
        if (progressBar == null || progressBar.getVisibility() != 0) {
            return;
        }
        this.mHorizontalProgressBar.setMax(i);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setCancelable(false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mProgressBarStyle = arguments.getInt("mProgressBarStyle", 0);
            this.isShowCheckBox = arguments.getBoolean(KEY_CHECK_BOX, false);
            this.mContentId = arguments.getInt("mContentId");
            this.content = arguments.getString("content");
            this.mConfirmButtonId = arguments.getInt("mConfirmButtonId");
            this.mTitleId = arguments.getInt("mTitleId");
            this.title = arguments.getString("title");
            this.mNegativeButtonId = arguments.getInt("mNegativeButtonId");
            this.mPositiveButtonId = arguments.getInt("mPositiveButtonId");
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.notify_dialog, viewGroup, false);
        if (getDialog() != null) {
            getDialog().requestWindowFeature(1);
        }
        this.mTitle = (TextView) inflate.findViewById(R.id.tv_title);
        this.mContent = (TextView) inflate.findViewById(R.id.tv_content);
        this.mProgressBar = (ProgressBar) inflate.findViewById(R.id.progressBar);
        this.mHorizontalProgressBar = (ProgressBar) inflate.findViewById(R.id.progressBar_horizon);
        this.mNegativeButtonText = (TextView) inflate.findViewById(R.id.tv_left);
        this.mConfirmButtonText = (TextView) inflate.findViewById(R.id.tv_middle);
        this.mPositiveButtonText = (TextView) inflate.findViewById(R.id.tv_right);
        this.mDividerView = inflate.findViewById(R.id.divider_id);
        CheckBox checkBox = (CheckBox) inflate.findViewById(R.id.checkbox);
        this.mCheckBox = checkBox;
        checkBox.setOnCheckedChangeListener(this.onCheckedChangeListener);
        View findViewById = inflate.findViewById(R.id.line_id);
        this.mContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        int i = this.mTitleId;
        if (i != 0) {
            if (i == -1) {
                this.mTitle.setText(this.title);
            } else {
                this.mTitle.setText(getResources().getString(this.mTitleId));
            }
        }
        if (this.isShowCheckBox) {
            this.mCheckBox.setVisibility(0);
        } else {
            this.mCheckBox.setVisibility(8);
        }
        int i2 = this.mProgressBarStyle;
        if (i2 <= 0) {
            this.mProgressBar.setVisibility(8);
            this.mHorizontalProgressBar.setVisibility(8);
        } else if (i2 == 1) {
            this.mProgressBar.setVisibility(0);
            this.mHorizontalProgressBar.setVisibility(8);
        } else if (i2 == 2) {
            this.mProgressBar.setVisibility(8);
            this.mHorizontalProgressBar.setVisibility(0);
        }
        if (this.mContentId != 0) {
            this.mContent.setVisibility(0);
            if (this.mContentId == -1) {
                this.mContent.setText(this.content);
            } else {
                this.mContent.setText(getResources().getString(this.mContentId));
            }
            if (this.isLeftGravity) {
                this.mContent.setGravity(3);
            } else {
                this.mContent.setGravity(17);
            }
        } else {
            this.mContent.setVisibility(8);
        }
        if (this.mNegativeButtonId != 0) {
            this.mNegativeButtonText.setVisibility(0);
            this.mNegativeButtonText.setText(getResources().getString(this.mNegativeButtonId));
        } else {
            this.mNegativeButtonText.setVisibility(8);
        }
        if (this.mConfirmButtonId != 0) {
            findViewById.setVisibility(0);
            this.mDividerView.setVisibility(8);
            this.mConfirmButtonText.setVisibility(0);
            this.mConfirmButtonText.setText(getResources().getString(this.mConfirmButtonId));
        } else {
            this.mConfirmButtonText.setVisibility(8);
            if (this.mProgressBarStyle > 0) {
                findViewById.setVisibility(8);
                this.mDividerView.setVisibility(8);
            } else {
                findViewById.setVisibility(0);
                this.mDividerView.setVisibility(0);
            }
        }
        if (this.mPositiveButtonId != 0) {
            this.mPositiveButtonText.setVisibility(0);
            this.mPositiveButtonText.setText(getResources().getString(this.mPositiveButtonId));
        } else {
            this.mPositiveButtonText.setVisibility(8);
        }
        this.mConfirmButtonText.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (NotifyDialog.this.mOnConfirmClickListener != null) {
                    NotifyDialog.this.mOnConfirmClickListener.onClick();
                }
            }
        });
        this.mNegativeButtonText.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (NotifyDialog.this.mOnNegativeClickListener != null) {
                    NotifyDialog.this.mOnNegativeClickListener.onClick();
                }
            }
        });
        this.mPositiveButtonText.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (NotifyDialog.this.mOnPositiveClickListener != null) {
                    NotifyDialog.this.mOnPositiveClickListener.onClick();
                }
            }
        });
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Dbug.d(this.tag, "onActivityCreated.............:");
        if (getDialog() == null || getDialog().getWindow() == null) {
            return;
        }
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        attributes.width = 100;
        attributes.height = 50;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (getResources().getConfiguration().orientation == 2) {
            attributes.width = (displayMetrics.heightPixels * 4) / 5;
            attributes.height = (displayMetrics.heightPixels * 3) / 4;
        } else if (getResources().getConfiguration().orientation == 1) {
            attributes.width = (displayMetrics.widthPixels * 4) / 5;
            attributes.height = (displayMetrics.widthPixels * 3) / 4;
        }
        attributes.gravity = 17;
        getDialog().getWindow().setAttributes(attributes);
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseDialogFragment, androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (this.isShowCheckBox) {
            this.mCheckBox.setChecked(false);
        }
        OnDismissDialogListener onDismissDialogListener = this.mOnDismissDialogListener;
        if (onDismissDialogListener != null) {
            onDismissDialogListener.onDismiss();
        }
    }

    public void setOnDismissDialogListener(OnDismissDialogListener onDismissDialogListener) {
        this.mOnDismissDialogListener = onDismissDialogListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.mOnCheckedChangeListener = onCheckedChangeListener;
    }
}
