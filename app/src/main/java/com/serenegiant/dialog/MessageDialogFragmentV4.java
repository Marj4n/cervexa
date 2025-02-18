package com.serenegiant.dialog;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.result.ActivityResultCaller;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.serenegiant.utils.BuildCheck;

/* loaded from: classes.dex */
public class MessageDialogFragmentV4 extends DialogFragmentEx {
    private static final String ARGS_KEY_PERMISSIONS = "permissions";
    private static final String TAG = MessageDialogFragmentV4.class.getSimpleName();
    private MessageDialogListener mDialogListener;
    private final DialogInterface.OnClickListener mOnClickListener = new DialogInterface.OnClickListener() { // from class: com.serenegiant.dialog.MessageDialogFragmentV4.1
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            MessageDialogFragmentV4.this.callOnMessageDialogResult(i == -1);
        }
    };

    public interface MessageDialogListener {
        void onMessageDialogResult(MessageDialogFragmentV4 messageDialogFragmentV4, int i, String[] strArr, boolean z);
    }

    public static MessageDialogFragmentV4 showDialog(FragmentActivity fragmentActivity, int i, int i2, int i3, String[] strArr) throws IllegalStateException {
        MessageDialogFragmentV4 newInstance = newInstance(i, i2, i3, strArr);
        newInstance.show(fragmentActivity.getSupportFragmentManager(), TAG);
        return newInstance;
    }

    public static MessageDialogFragmentV4 showDialog(Fragment fragment, int i, int i2, int i3, String[] strArr) throws IllegalStateException {
        MessageDialogFragmentV4 newInstance = newInstance(i, i2, i3, strArr);
        newInstance.setTargetFragment(fragment, fragment.getId());
        newInstance.show(fragment.requireFragmentManager(), TAG);
        return newInstance;
    }

    public static MessageDialogFragmentV4 newInstance(int i, int i2, int i3, String[] strArr) {
        MessageDialogFragmentV4 messageDialogFragmentV4 = new MessageDialogFragmentV4();
        Bundle bundle = new Bundle();
        bundle.putInt("requestCode", i);
        bundle.putInt("title", i2);
        bundle.putInt("message", i3);
        bundle.putStringArray(ARGS_KEY_PERMISSIONS, strArr);
        messageDialogFragmentV4.setArguments(bundle);
        return messageDialogFragmentV4;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MessageDialogListener) {
            this.mDialogListener = (MessageDialogListener) context;
        }
        if (this.mDialogListener == null) {
            ActivityResultCaller targetFragment = getTargetFragment();
            if (targetFragment instanceof MessageDialogListener) {
                this.mDialogListener = (MessageDialogListener) targetFragment;
            }
        }
        if (this.mDialogListener == null && BuildCheck.isAndroid4_2()) {
            ActivityResultCaller parentFragment = getParentFragment();
            if (parentFragment instanceof MessageDialogListener) {
                this.mDialogListener = (MessageDialogListener) parentFragment;
            }
        }
        if (this.mDialogListener == null) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        if (bundle == null) {
            bundle = requireArguments();
        }
        int i = bundle.getInt("title");
        return new AlertDialog.Builder(requireActivity()).setIcon(R.drawable.ic_dialog_alert).setTitle(i).setMessage(bundle.getInt("message")).setPositiveButton(R.string.ok, this.mOnClickListener).setNegativeButton(R.string.cancel, this.mOnClickListener).create();
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        callOnMessageDialogResult(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callOnMessageDialogResult(boolean z) throws IllegalStateException {
        Bundle requireArguments = requireArguments();
        try {
            this.mDialogListener.onMessageDialogResult(this, requireArguments.getInt("requestCode"), requireArguments.getStringArray(ARGS_KEY_PERMISSIONS), z);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }
}
