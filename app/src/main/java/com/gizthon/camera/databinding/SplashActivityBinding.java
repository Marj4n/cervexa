package com.gizthon.camera.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.gizthon.camera.SplashActivity;
import com.serenegiant.usb.widget.UVCCameraTextureView;
import com.weioa.GoPlusDrone.R;

/* loaded from: classes.dex */
public abstract class SplashActivityBinding extends ViewDataBinding {
    public final UVCCameraTextureView cameraView;

    @Bindable
    protected SplashActivity mEventHandler;
    public final TextView tvCamera;
    public final TextView tvConnect;
    public final TextView tvHelp;

    public abstract void setEventHandler(SplashActivity splashActivity);

    protected SplashActivityBinding(Object obj, View view, int i, UVCCameraTextureView uVCCameraTextureView, TextView textView, TextView textView2, TextView textView3) {
        super(obj, view, i);
        this.cameraView = uVCCameraTextureView;
        this.tvCamera = textView;
        this.tvConnect = textView2;
        this.tvHelp = textView3;
    }

    public SplashActivity getEventHandler() {
        return this.mEventHandler;
    }

    public static SplashActivityBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static SplashActivityBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (SplashActivityBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.splash_activity, viewGroup, z, obj);
    }

    public static SplashActivityBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static SplashActivityBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (SplashActivityBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.splash_activity, null, false, obj);
    }

    public static SplashActivityBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static SplashActivityBinding bind(View view, Object obj) {
        return (SplashActivityBinding) bind(obj, view, R.layout.splash_activity);
    }
}
