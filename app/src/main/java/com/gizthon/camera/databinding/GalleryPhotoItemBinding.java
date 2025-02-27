package com.gizthon.camera.databinding;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.gizthon.camera.R;
import com.gizthon.camera.adapter.PhotoAdapter;
import com.gizthon.camera.model.PhotoBean;

/* loaded from: classes.dex */
public abstract class GalleryPhotoItemBinding extends ViewDataBinding {

    @Bindable
    protected PhotoAdapter mEventHandler;

    @Bindable
    protected PhotoBean mViewModel;

    public abstract void setEventHandler(PhotoAdapter photoAdapter);

    public abstract void setViewModel(PhotoBean photoBean);

    protected GalleryPhotoItemBinding(Object obj, View view, int i) {
        super(obj, view, i);
    }

    public PhotoAdapter getEventHandler() {
        return this.mEventHandler;
    }

    public PhotoBean getViewModel() {
        return this.mViewModel;
    }

    public static GalleryPhotoItemBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @SuppressLint("RestrictedApi")
    @Deprecated
    public static GalleryPhotoItemBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (GalleryPhotoItemBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.gallery_photo_item, viewGroup, z, obj);
    }

    public static GalleryPhotoItemBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @SuppressLint("RestrictedApi")
    @Deprecated
    public static GalleryPhotoItemBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (GalleryPhotoItemBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.gallery_photo_item, null, false, obj);
    }

    public static GalleryPhotoItemBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static GalleryPhotoItemBinding bind(View view, Object obj) {
        return (GalleryPhotoItemBinding) bind(obj, view, R.layout.gallery_photo_item);
    }
}
