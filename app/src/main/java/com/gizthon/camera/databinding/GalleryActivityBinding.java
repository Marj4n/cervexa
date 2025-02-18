package com.gizthon.camera.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.viewpager2.widget.ViewPager2;
import com.gizthon.camera.activity.GalleryListActivity;
import com.gizthon.camera.model.GalleryViewModel;
import com.google.android.material.tabs.TabLayout;
import com.weioa.GoPlusDrone.R;

/* loaded from: classes.dex */
public abstract class GalleryActivityBinding extends ViewDataBinding {
    public final TextView back;
    public final CheckBox done;

    @Bindable
    protected GalleryListActivity mEventHandler;

    @Bindable
    protected GalleryViewModel mViewModel;
    public final ViewPager2 pager;
    public final TabLayout tabLayout;
    public final TextView tvComplete;

    public abstract void setEventHandler(GalleryListActivity galleryListActivity);

    public abstract void setViewModel(GalleryViewModel galleryViewModel);

    protected GalleryActivityBinding(Object obj, View view, int i, TextView textView, CheckBox checkBox, ViewPager2 viewPager2, TabLayout tabLayout, TextView textView2) {
        super(obj, view, i);
        this.back = textView;
        this.done = checkBox;
        this.pager = viewPager2;
        this.tabLayout = tabLayout;
        this.tvComplete = textView2;
    }

    public GalleryListActivity getEventHandler() {
        return this.mEventHandler;
    }

    public GalleryViewModel getViewModel() {
        return this.mViewModel;
    }

    public static GalleryActivityBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static GalleryActivityBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (GalleryActivityBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.gallery_activity, viewGroup, z, obj);
    }

    public static GalleryActivityBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static GalleryActivityBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (GalleryActivityBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.gallery_activity, null, false, obj);
    }

    public static GalleryActivityBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static GalleryActivityBinding bind(View view, Object obj) {
        return (GalleryActivityBinding) bind(obj, view, R.layout.gallery_activity);
    }
}
