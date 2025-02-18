package com.gizthon.camera.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.gizthon.camera.activity.CameraBaseActivity;
import com.serenegiant.usb.widget.UVCCameraTextureView;
import com.weioa.GoPlusDrone.R;

/* loaded from: classes.dex */
public abstract class UsbPreviewActivityBinding extends ViewDataBinding {
    public final ImageView back;
    public final UVCCameraTextureView cameraView;
    public final TextView ivBroken;
    public final ImageView ivFocusImg;
    public final ImageView ivPicture;
    public final ImageView ivRecord;
    public final ImageView ivResolution;
    public final ImageView ivZoomFocus;
    public final ImageView ivZoomIn;
    public final ImageView ivZoomOut;
    public final LinearLayout llConent;
    public final LinearLayout llControl;
    public final LinearLayout llResolution;

    @Bindable
    protected CameraBaseActivity mEventHandler;
    public final RecyclerView rcResolution;
    public final ImageView takePhoto;
    public final TextView tvRecordTime;

    public abstract void setEventHandler(CameraBaseActivity cameraBaseActivity);

    protected UsbPreviewActivityBinding(Object obj, View view, int i, ImageView imageView, UVCCameraTextureView uVCCameraTextureView, TextView textView, ImageView imageView2, ImageView imageView3, ImageView imageView4, ImageView imageView5, ImageView imageView6, ImageView imageView7, ImageView imageView8, LinearLayout linearLayout, LinearLayout linearLayout2, LinearLayout linearLayout3, RecyclerView recyclerView, ImageView imageView9, TextView textView2) {
        super(obj, view, i);
        this.back = imageView;
        this.cameraView = uVCCameraTextureView;
        this.ivBroken = textView;
        this.ivFocusImg = imageView2;
        this.ivPicture = imageView3;
        this.ivRecord = imageView4;
        this.ivResolution = imageView5;
        this.ivZoomFocus = imageView6;
        this.ivZoomIn = imageView7;
        this.ivZoomOut = imageView8;
        this.llConent = linearLayout;
        this.llControl = linearLayout2;
        this.llResolution = linearLayout3;
        this.rcResolution = recyclerView;
        this.takePhoto = imageView9;
        this.tvRecordTime = textView2;
    }

    public CameraBaseActivity getEventHandler() {
        return this.mEventHandler;
    }

    public static UsbPreviewActivityBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static UsbPreviewActivityBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (UsbPreviewActivityBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.usb_preview_activity, viewGroup, z, obj);
    }

    public static UsbPreviewActivityBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static UsbPreviewActivityBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (UsbPreviewActivityBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.usb_preview_activity, null, false, obj);
    }

    public static UsbPreviewActivityBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static UsbPreviewActivityBinding bind(View view, Object obj) {
        return (UsbPreviewActivityBinding) bind(obj, view, R.layout.usb_preview_activity);
    }
}
