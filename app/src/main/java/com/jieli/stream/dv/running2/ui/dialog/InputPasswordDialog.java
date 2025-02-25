package com.jieli.stream.dv.running2.ui.dialog;

import android.R;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import com.jieli.stream.dv.running2.ui.base.BaseDialogFragment;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.ToastUtil;

/* loaded from: classes.dex */
public class InputPasswordDialog extends BaseDialogFragment implements IConstant {
    private TextView mCancel;
    private TextView mConfirm;
    private EditText mContent;
    private OnInputCompletionListener mOnInputCompletionListener;
    private String mTextTitle;
    private TextView mTitle;
    private final int MAX_WIFI_PWD_LENGTH = 63;
    private final int MIN_WIFI_PWD_LENGTH = 8;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.dialog.InputPasswordDialog.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (InputPasswordDialog.this.mConfirm == view) {
                InputPasswordDialog.this.commitPassword();
            } else if (InputPasswordDialog.this.mCancel == view) {
                InputPasswordDialog.this.dismiss();
            }
        }
    };

    public interface OnInputCompletionListener {
        void onCompletion(String str, String str2);
    }

    public static InputPasswordDialog newInstance(String str) {
        InputPasswordDialog inputPasswordDialog = new InputPasswordDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", str);
        inputPasswordDialog.setArguments(bundle);
        return inputPasswordDialog;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(com.jieli.stream.dv.running2.R.layout.input_password_dialog, viewGroup, false);
        this.mContent = (EditText) inflate.findViewById(com.jieli.stream.dv.running2.R.id.et_password);
        this.mConfirm = (TextView) inflate.findViewById(com.jieli.stream.dv.running2.R.id.tv_confirm);
        this.mCancel = (TextView) inflate.findViewById(com.jieli.stream.dv.running2.R.id.tv_cancel);
        this.mTitle = (TextView) inflate.findViewById(com.jieli.stream.dv.running2.R.id.tv_title);
        this.mConfirm.setOnClickListener(this.mOnClickListener);
        this.mCancel.setOnClickListener(this.mOnClickListener);
        this.mTitle.setText(this.mTextTitle);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getDialog() == null || getDialog().getWindow() == null) {
            return;
        }
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        attributes.width = 100;
        attributes.height = 50;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (getResources().getConfiguration().orientation == 2) {
            attributes.width = (displayMetrics.heightPixels * 4) / 5;
            attributes.height = (displayMetrics.heightPixels * 3) / 5;
        } else if (getResources().getConfiguration().orientation == 1) {
            attributes.width = (displayMetrics.widthPixels * 4) / 5;
            attributes.height = (displayMetrics.widthPixels * 3) / 5;
        }
        attributes.gravity = 17;
        getDialog().getWindow().setAttributes(attributes);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void commitPassword() {
        String trim = this.mContent.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            ToastUtil.showToastLong(getString(com.jieli.stream.dv.running2.R.string.name_empty_error));
            trim = "";
        }
        if (trim.length() < 8 || trim.length() > 63) {
            ToastUtil.showToastLong(getString(com.jieli.stream.dv.running2.R.string.input_pwd_error));
            return;
        }
        dismiss();
        OnInputCompletionListener onInputCompletionListener = this.mOnInputCompletionListener;
        if (onInputCompletionListener != null) {
            onInputCompletionListener.onCompletion(this.mTextTitle, trim);
        }
    }

    public void setOnInputCompletionListener(OnInputCompletionListener onInputCompletionListener) {
        this.mOnInputCompletionListener = onInputCompletionListener;
    }
}
