package com.marjan.cervexa.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.weioa.GoPlusDrone.R;

/* loaded from: classes.dex */
public abstract class DialogShareBinding extends ViewDataBinding {
    public final TextView tvCancel;
    public final ImageView tvFecebook;
    public final ImageView tvQq;
    public final ImageView tvTwitter;
    public final ImageView tvWchat;
    public final ImageView tvYoutube;

    protected DialogShareBinding(Object obj, View view, int i, TextView textView, ImageView imageView, ImageView imageView2, ImageView imageView3, ImageView imageView4, ImageView imageView5) {
        super(obj, view, i);
        this.tvCancel = textView;
        this.tvFecebook = imageView;
        this.tvQq = imageView2;
        this.tvTwitter = imageView3;
        this.tvWchat = imageView4;
        this.tvYoutube = imageView5;
    }

    public static DialogShareBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static DialogShareBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (DialogShareBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.dialog_share, viewGroup, z, obj);
    }

    public static DialogShareBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static DialogShareBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (DialogShareBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.dialog_share, null, false, obj);
    }

    public static DialogShareBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static DialogShareBinding bind(View view, Object obj) {
        return (DialogShareBinding) bind(obj, view, R.layout.dialog_share);
    }
}
