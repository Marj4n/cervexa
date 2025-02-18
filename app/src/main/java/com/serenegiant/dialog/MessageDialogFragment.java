package com.serenegiant.dialog;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ComponentCallbacks2;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import com.serenegiant.utils.BuildCheck;

@Deprecated
/* loaded from: classes.dex */
public class MessageDialogFragment extends DialogFragment {
    private static final String TAG = MessageDialogFragment.class.getSimpleName();
    private MessageDialogListener mDialogListener;

    public interface MessageDialogListener {
        void onMessageDialogResult(MessageDialogFragment messageDialogFragment, int i, String[] strArr, boolean z);
    }

    public static MessageDialogFragment showDialog(Activity activity, int i, int i2, int i3, String[] strArr) {
        MessageDialogFragment newInstance = newInstance(i, i2, i3, strArr);
        newInstance.show(activity.getFragmentManager(), TAG);
        return newInstance;
    }

    public static MessageDialogFragment showDialog(Fragment fragment, int i, int i2, int i3, String[] strArr) {
        MessageDialogFragment newInstance = newInstance(i, i2, i3, strArr);
        newInstance.setTargetFragment(fragment, fragment.getId());
        newInstance.show(fragment.getFragmentManager(), TAG);
        return newInstance;
    }

    public static MessageDialogFragment newInstance(int i, int i2, int i3, String[] strArr) {
        MessageDialogFragment messageDialogFragment = new MessageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("requestCode", i);
        bundle.putInt("title", i2);
        bundle.putInt("message", i3);
        if (strArr == null) {
            strArr = new String[0];
        }
        bundle.putStringArray("permissions", strArr);
        messageDialogFragment.setArguments(bundle);
        return messageDialogFragment;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MessageDialogListener) {
            this.mDialogListener = (MessageDialogListener) activity;
        }
        if (this.mDialogListener == null) {
            ComponentCallbacks2 targetFragment = getTargetFragment();
            if (targetFragment instanceof MessageDialogListener) {
                this.mDialogListener = (MessageDialogListener) targetFragment;
            }
        }
        if (this.mDialogListener == null && BuildCheck.isAndroid4_2()) {
            ComponentCallbacks2 parentFragment = getParentFragment();
            if (parentFragment instanceof MessageDialogListener) {
                this.mDialogListener = (MessageDialogListener) parentFragment;
            }
        }
        if (this.mDialogListener == null) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Override // android.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        if (bundle == null) {
            bundle = getArguments();
        }
        final int i = getArguments().getInt("requestCode");
        int i2 = getArguments().getInt("title");
        int i3 = getArguments().getInt("message");
        final String[] stringArray = bundle.getStringArray("permissions");
        return new AlertDialog.Builder(getActivity()).setIcon(R.drawable.ic_dialog_alert).setTitle(i2).setMessage(i3).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.serenegiant.dialog.MessageDialogFragment.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i4) {
                try {
                    MessageDialogFragment.this.mDialogListener.onMessageDialogResult(MessageDialogFragment.this, i, stringArray, true);
                } catch (Exception e) {
                    Log.w(MessageDialogFragment.TAG, e);
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.serenegiant.dialog.MessageDialogFragment.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i4) {
                try {
                    MessageDialogFragment.this.mDialogListener.onMessageDialogResult(MessageDialogFragment.this, i, stringArray, false);
                } catch (Exception e) {
                    Log.w(MessageDialogFragment.TAG, e);
                }
            }
        }).create();
    }
}
