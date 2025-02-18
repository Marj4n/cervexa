package com.gizthon.camera.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.gizthon.camera.activity.CameraBaseActivity;
import com.serenegiant.usb.widget.UVCCameraTextureView;
import com.weioa.GoPlusDrone.R;

/* loaded from: classes.dex */
public class UsbPreviewActivityBindingImpl extends UsbPreviewActivityBinding {
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;
    private static final SparseIntArray sViewsWithIds;
    private long mDirtyFlags;
    private final RelativeLayout mboundView0;

    @Override // androidx.databinding.ViewDataBinding
    protected boolean onFieldChange(int i, Object obj, int i2) {
        return false;
    }

    static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        sViewsWithIds = sparseIntArray;
        sparseIntArray.put(R.id.ll_conent, 1);
        sViewsWithIds.put(R.id.camera_view, 2);
        sViewsWithIds.put(R.id.ivBroken, 3);
        sViewsWithIds.put(R.id.iv_focus_img, 4);
        sViewsWithIds.put(R.id.back, 5);
        sViewsWithIds.put(R.id.tv_record_time, 6);
        sViewsWithIds.put(R.id.ll_control, 7);
        sViewsWithIds.put(R.id.iv_zoom_in, 8);
        sViewsWithIds.put(R.id.iv_zoom_out, 9);
        sViewsWithIds.put(R.id.iv_zoom_focus, 10);
        sViewsWithIds.put(R.id.take_photo, 11);
        sViewsWithIds.put(R.id.iv_record, 12);
        sViewsWithIds.put(R.id.iv_picture, 13);
        sViewsWithIds.put(R.id.iv_resolution, 14);
        sViewsWithIds.put(R.id.ll_resolution, 15);
        sViewsWithIds.put(R.id.rc_resolution, 16);
    }

    public UsbPreviewActivityBindingImpl(DataBindingComponent dataBindingComponent, View view) {
        this(dataBindingComponent, view, mapBindings(dataBindingComponent, view, 17, sIncludes, sViewsWithIds));
    }

    private UsbPreviewActivityBindingImpl(DataBindingComponent dataBindingComponent, View view, Object[] objArr) {
        super(dataBindingComponent, view, 0, (ImageView) objArr[5], (UVCCameraTextureView) objArr[2], (TextView) objArr[3], (ImageView) objArr[4], (ImageView) objArr[13], (ImageView) objArr[12], (ImageView) objArr[14], (ImageView) objArr[10], (ImageView) objArr[8], (ImageView) objArr[9], (LinearLayout) objArr[1], (LinearLayout) objArr[7], (LinearLayout) objArr[15], (RecyclerView) objArr[16], (ImageView) objArr[11], (TextView) objArr[6]);
        this.mDirtyFlags = -1L;
        RelativeLayout relativeLayout = (RelativeLayout) objArr[0];
        this.mboundView0 = relativeLayout;
        relativeLayout.setTag(null);
        setRootTag(view);
        invalidateAll();
    }

    @Override // androidx.databinding.ViewDataBinding
    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 2L;
        }
        requestRebind();
    }

    @Override // androidx.databinding.ViewDataBinding
    public boolean hasPendingBindings() {
        synchronized (this) {
            return this.mDirtyFlags != 0;
        }
    }

    @Override // androidx.databinding.ViewDataBinding
    public boolean setVariable(int i, Object obj) {
        if (3 != i) {
            return false;
        }
        setEventHandler((CameraBaseActivity) obj);
        return true;
    }

    @Override // com.gizthon.camera.databinding.UsbPreviewActivityBinding
    public void setEventHandler(CameraBaseActivity cameraBaseActivity) {
        this.mEventHandler = cameraBaseActivity;
    }

    @Override // androidx.databinding.ViewDataBinding
    protected void executeBindings() {
        synchronized (this) {
            this.mDirtyFlags = 0L;
        }
    }
}
