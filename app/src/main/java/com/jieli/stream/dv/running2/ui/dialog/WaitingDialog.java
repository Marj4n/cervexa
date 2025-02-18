package com.jieli.stream.dv.running2.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.base.BaseDialogFragment;

/* loaded from: classes.dex */
public class WaitingDialog extends BaseDialogFragment implements DialogInterface.OnKeyListener {
    private String notifyContent;
    private OnWaitingDialog onWaitingDialog;
    private TextView tvNotifyContent;

    public interface OnWaitingDialog {
        void onCancelDialog();
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseDialogFragment, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.dimAmount = 0.5f;
        attributes.flags |= 2;
        attributes.windowAnimations = 0;
        window.setAttributes(attributes);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.dialog_waiting, viewGroup, false);
        if (getDialog() != null) {
            getDialog().requestWindowFeature(1);
        }
        this.tvNotifyContent = (TextView) inflate.findViewById(R.id.dialog_waiting_tv);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() == null || getDialog() == null || getDialog().getWindow() == null) {
            return;
        }
        TextView textView = this.tvNotifyContent;
        if (textView != null && textView.getVisibility() == 0) {
            this.tvNotifyContent.setText(this.notifyContent);
        }
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        attributes.width = -2;
        attributes.height = -2;
        attributes.gravity = 17;
        getDialog().getWindow().setAttributes(attributes);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        getDialog().setOnKeyListener(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
    }

    @Override // android.content.DialogInterface.OnKeyListener
    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i != 4) {
            return false;
        }
        OnWaitingDialog onWaitingDialog = this.onWaitingDialog;
        if (onWaitingDialog != null) {
            onWaitingDialog.onCancelDialog();
        }
        dismiss();
        return true;
    }

    public void setNotifyContent(String str) {
        this.notifyContent = str;
    }

    public void updateNotifyContent(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.notifyContent = str;
        TextView textView = this.tvNotifyContent;
        if (textView == null || textView.getVisibility() != 0) {
            return;
        }
        this.tvNotifyContent.setText(this.notifyContent);
    }

    public void setOnWaitingDialog(OnWaitingDialog onWaitingDialog) {
        this.onWaitingDialog = onWaitingDialog;
    }
}
