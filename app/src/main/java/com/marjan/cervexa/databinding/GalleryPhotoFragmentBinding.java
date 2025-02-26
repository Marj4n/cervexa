package com.marjan.cervexa.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.weioa.GoPlusDrone.R;

/* loaded from: classes.dex */
public abstract class GalleryPhotoFragmentBinding extends ViewDataBinding {
    public final RecyclerView rcPhoto;

    protected GalleryPhotoFragmentBinding(Object obj, View view, int i, RecyclerView recyclerView) {
        super(obj, view, i);
        this.rcPhoto = recyclerView;
    }

    public static GalleryPhotoFragmentBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static GalleryPhotoFragmentBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (GalleryPhotoFragmentBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.gallery_photo_fragment, viewGroup, z, obj);
    }

    public static GalleryPhotoFragmentBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static GalleryPhotoFragmentBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (GalleryPhotoFragmentBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.gallery_photo_fragment, null, false, obj);
    }

    public static GalleryPhotoFragmentBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static GalleryPhotoFragmentBinding bind(View view, Object obj) {
        return (GalleryPhotoFragmentBinding) bind(obj, view, R.layout.gallery_photo_fragment);
    }
}
