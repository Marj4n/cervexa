package com.jieli.stream.dv.running2.ui.dialog;

import android.R;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.jieli.stream.dv.running2.ui.base.BaseDialogFragment;
import com.jieli.stream.dv.running2.util.ToastUtil;
import org.apache.commons.net.ftp.FTPReply;

/* loaded from: classes.dex */
public class InputContentDialog extends BaseDialogFragment implements View.OnClickListener {
    private EditText mEditText;
    private OnContentListener onContentListener;

    public interface OnContentListener {
        void onInput(String str);
    }

    public void setOnContentListener(OnContentListener onContentListener) {
        this.onContentListener = onContentListener;
    }


    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(com.jieli.stream.dv.running2.R.layout.dialog_input_content, viewGroup, false);
        this.mEditText = (EditText) inflate.findViewById(com.jieli.stream.dv.running2.R.id.dialog_edit_text);
        Button button = (Button) inflate.findViewById(com.jieli.stream.dv.running2.R.id.dialog_input_cancel_btn);
        Button button2 = (Button) inflate.findViewById(com.jieli.stream.dv.running2.R.id.dialog_input_confirm_btn);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() == null || getActivity().getWindow() == null || getDialog().getWindow() == null) {
            return;
        }
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        attributes.width = FTPReply.FILE_STATUS_OK;
        attributes.height = 50;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (getResources().getConfiguration().orientation == 2) {
            attributes.width = (displayMetrics.heightPixels * 4) / 6;
            attributes.height = (displayMetrics.heightPixels * 2) / 5;
        } else if (getResources().getConfiguration().orientation == 1) {
            attributes.width = (displayMetrics.widthPixels * 4) / 6;
            attributes.height = (displayMetrics.widthPixels * 2) / 5;
        }
        attributes.gravity = 17;
        getDialog().getWindow().setAttributes(attributes);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        EditText editText;
        if (getActivity() == null || getActivity().getWindow() == null || getDialog().getWindow() == null) {
            return;
        }
        int id = view.getId();
        if (id == com.jieli.stream.dv.running2.R.id.dialog_input_cancel_btn) {
            dismiss();
            return;
        }
        if (id != com.jieli.stream.dv.running2.R.id.dialog_input_confirm_btn || (editText = this.mEditText) == null) {
            return;
        }
        String trim = editText.getText().toString().trim();
        if (!TextUtils.isEmpty(trim)) {
            OnContentListener onContentListener = this.onContentListener;
            if (onContentListener != null) {
                onContentListener.onInput(trim);
            }
            dismiss();
            return;
        }
        ToastUtil.showToastShort(getString(com.jieli.stream.dv.running2.R.string.input_content_empty));
    }
}
