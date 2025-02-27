package com.gizthon.camera.databinding;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.gizthon.camera.R;

/* loaded from: classes.dex */
public abstract class HelpActivityBinding extends ViewDataBinding {
    public final TextView back;
    public final ImageView ivHelp1;
    public final ImageView ivHelp2;
    public final ImageView ivL;
    public final ImageView ivR;
    public final LinearLayout llLayout1;
    public final LinearLayout llLayout2;

    protected HelpActivityBinding(Object obj, View view, int i, TextView textView, ImageView imageView, ImageView imageView2, ImageView imageView3, ImageView imageView4, LinearLayout linearLayout, LinearLayout linearLayout2) {
        super(obj, view, i);
        this.back = textView;
        this.ivHelp1 = imageView;
        this.ivHelp2 = imageView2;
        this.ivL = imageView3;
        this.ivR = imageView4;
        this.llLayout1 = linearLayout;
        this.llLayout2 = linearLayout2;
    }

    public static HelpActivityBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @SuppressLint("RestrictedApi")
    @Deprecated
    public static HelpActivityBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (HelpActivityBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.help_activity, viewGroup, z, obj);
    }

    public static HelpActivityBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @SuppressLint("RestrictedApi")
    @Deprecated
    public static HelpActivityBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (HelpActivityBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.help_activity, null, false, obj);
    }

    public static HelpActivityBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static HelpActivityBinding bind(View view, Object obj) {
        return (HelpActivityBinding) bind(obj, view, R.layout.help_activity);
    }
}
