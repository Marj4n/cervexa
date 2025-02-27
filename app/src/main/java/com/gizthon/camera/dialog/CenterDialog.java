package com.gizthon.camera.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.gizthon.camera.R;

import java.lang.reflect.Field;

/* loaded from: classes.dex */
public abstract class CenterDialog extends DialogFragment {
    public int getGravity() {
        return 17;
    }

    public abstract int getLayoutId();

    public int getScreenHeight() {
        return -2;
    }

    public abstract void initView(ViewDataBinding viewDataBinding);

    public boolean isBottom() {
        return false;
    }

    public boolean isCancele() {
        return true;
    }

    @Override // android.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setDimAmount(0.5f);
        }
        ViewDataBinding layoutBind = getLayoutBind();
        View root = layoutBind.getRoot();
        dialog.setCancelable(isCancele());
        dialog.setCanceledOnTouchOutside(isCancele());
        dialog.setContentView(root);
        initView(layoutBind);
        initWindowParams(dialog);
        return dialog;
    }

    public int getWindowWidth() {
        return (getScreenWidth(getActivity()) * 5) / 6;
    }

    @Override // android.app.DialogFragment
    public void show(FragmentManager fragmentManager, String str) {
        try {
            super.show(fragmentManager, str);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ResourceType")
    protected void initWindowParams(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            if (isBottom()) {
                window.setWindowAnimations(2131886321);
                attributes.width = getScreenWidth(getActivity());
                attributes.gravity = 80;
            } else {
                attributes.gravity = 17;
                attributes.width = (int) (getScreenWidth(getActivity()) * 0.73300004f);
            }
            attributes.height = -2;
            window.setAttributes(attributes);
        }
    }

    public ViewDataBinding getLayoutBind() {
        return DataBindingUtil.inflate(getActivity().getLayoutInflater(), getLayoutId(), null, false);
    }

    public void show(Object obj) {
        show(obj, toString());
    }

    public void show(Object obj, String str) {
        FragmentManager fragmentManager;
        if (obj instanceof Activity) {
            fragmentManager = ((Activity) obj).getFragmentManager();
            if (isAdded()) {
                dismiss();
            }
        } else if (obj instanceof Fragment) {
            fragmentManager = ((Fragment) obj).getActivity().getFragmentManager();
            if (isAdded()) {
                dismiss();
            }
        } else {
            fragmentManager = null;
        }
        try {
            Field declaredField = DialogFragment.class.getDeclaredField("mDismissed");
            declaredField.setAccessible(true);
            declaredField.set(this, false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        }
        try {
            Field declaredField2 = DialogFragment.class.getDeclaredField("mShownByMe");
            declaredField2.setAccessible(true);
            declaredField2.set(this, true);
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        } catch (NoSuchFieldException e4) {
            e4.printStackTrace();
        }
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.add(this, str);
        beginTransaction.commitAllowingStateLoss();
    }

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    public int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
