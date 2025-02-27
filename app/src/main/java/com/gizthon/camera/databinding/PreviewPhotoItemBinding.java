package com.gizthon.camera.databinding;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.gizthon.camera.R;
import com.gizthon.camera.view.TouchImageView;

/* loaded from: classes.dex */
public abstract class PreviewPhotoItemBinding extends ViewDataBinding {
    public final TouchImageView imgDisplay;

    protected PreviewPhotoItemBinding(Object obj, View view, int i, TouchImageView touchImageView) {
        super(obj, view, i);
        this.imgDisplay = touchImageView;
    }

    public static PreviewPhotoItemBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @SuppressLint("RestrictedApi")
    @Deprecated
    public static PreviewPhotoItemBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (PreviewPhotoItemBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.preview_photo_item, viewGroup, z, obj);
    }

    public static PreviewPhotoItemBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @SuppressLint("RestrictedApi")
    @Deprecated
    public static PreviewPhotoItemBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (PreviewPhotoItemBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.preview_photo_item, null, false, obj);
    }

    public static PreviewPhotoItemBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static PreviewPhotoItemBinding bind(View view, Object obj) {
        return (PreviewPhotoItemBinding) bind(obj, view, R.layout.preview_photo_item);
    }
}
