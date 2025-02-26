package com.marjan.cervexa.databinding;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.weioa.GoPlusDrone.R;

/* loaded from: classes.dex */
public abstract class PreviewVideoActivityBinding extends ViewDataBinding {
    public final View videoView;

    protected PreviewVideoActivityBinding(Object obj, View view, int i, View ijkVideoView) {
        super(obj, view, i);
        this.videoView = ijkVideoView;
    }

    public static PreviewVideoActivityBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @SuppressLint("RestrictedApi")
    @Deprecated
    public static PreviewVideoActivityBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (PreviewVideoActivityBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.preview_video_activity, viewGroup, z, obj);
    }

    public static PreviewVideoActivityBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static PreviewVideoActivityBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (PreviewVideoActivityBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.preview_video_activity, null, false, obj);
    }

    public static PreviewVideoActivityBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static PreviewVideoActivityBinding bind(View view, Object obj) {
        return (PreviewVideoActivityBinding) bind(obj, view, R.layout.preview_video_activity);
    }
}
