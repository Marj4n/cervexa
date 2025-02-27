package com.gizthon.camera.databinding;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.viewpager.widget.ViewPager;

import com.gizthon.camera.R;

/* loaded from: classes.dex */
public abstract class PreviewPhotoActivityBinding extends ViewDataBinding {
    public final ImageView ivShare;
    public final ViewPager pager;

    protected PreviewPhotoActivityBinding(Object obj, View view, int i, ImageView imageView, ViewPager viewPager) {
        super(obj, view, i);
        this.ivShare = imageView;
        this.pager = viewPager;
    }

    public static PreviewPhotoActivityBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @SuppressLint("RestrictedApi")
    @Deprecated
    public static PreviewPhotoActivityBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (PreviewPhotoActivityBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.preview_photo_activity, viewGroup, z, obj);
    }

    public static PreviewPhotoActivityBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @SuppressLint("RestrictedApi")
    @Deprecated
    public static PreviewPhotoActivityBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (PreviewPhotoActivityBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.preview_photo_activity, null, false, obj);
    }

    public static PreviewPhotoActivityBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static PreviewPhotoActivityBinding bind(View view, Object obj) {
        return (PreviewPhotoActivityBinding) bind(obj, view, R.layout.preview_photo_activity);
    }
}
