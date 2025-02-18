package com.gizthon.camera.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.gizthon.camera.SplashActivity;
import com.gizthon.camera.generated.callback.OnClickListener;
import com.serenegiant.usb.widget.UVCCameraTextureView;
import com.weioa.GoPlusDrone.R;

/* loaded from: classes.dex */
public class SplashActivityBindingImpl extends SplashActivityBinding implements OnClickListener.Listener {
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;
    private static final SparseIntArray sViewsWithIds;
    private final View.OnClickListener mCallback1;
    private final View.OnClickListener mCallback2;
    private final View.OnClickListener mCallback3;
    private long mDirtyFlags;
    private final LinearLayout mboundView0;

    @Override // androidx.databinding.ViewDataBinding
    protected boolean onFieldChange(int i, Object obj, int i2) {
        return false;
    }

    static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        sViewsWithIds = sparseIntArray;
        sparseIntArray.put(R.id.camera_view, 4);
    }

    public SplashActivityBindingImpl(DataBindingComponent dataBindingComponent, View view) {
        this(dataBindingComponent, view, mapBindings(dataBindingComponent, view, 5, sIncludes, sViewsWithIds));
    }

    private SplashActivityBindingImpl(DataBindingComponent dataBindingComponent, View view, Object[] objArr) {
        super(dataBindingComponent, view, 0, (UVCCameraTextureView) objArr[4], (TextView) objArr[1], (TextView) objArr[2], (TextView) objArr[3]);
        this.mDirtyFlags = -1L;
        LinearLayout linearLayout = (LinearLayout) objArr[0];
        this.mboundView0 = linearLayout;
        linearLayout.setTag(null);
        this.tvCamera.setTag(null);
        this.tvConnect.setTag(null);
        this.tvHelp.setTag(null);
        setRootTag(view);
        this.mCallback3 = new OnClickListener(this, 3);
        this.mCallback1 = new OnClickListener(this, 1);
        this.mCallback2 = new OnClickListener(this, 2);
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
        setEventHandler((SplashActivity) obj);
        return true;
    }

    @Override // com.gizthon.camera.databinding.SplashActivityBinding
    public void setEventHandler(SplashActivity splashActivity) {
        this.mEventHandler = splashActivity;
        synchronized (this) {
            this.mDirtyFlags |= 1;
        }
        notifyPropertyChanged(3);
        super.requestRebind();
    }

    @Override // androidx.databinding.ViewDataBinding
    protected void executeBindings() {
        long j;
        synchronized (this) {
            j = this.mDirtyFlags;
            this.mDirtyFlags = 0L;
        }
        SplashActivity splashActivity = this.mEventHandler;
        if ((j & 2) != 0) {
            this.tvCamera.setOnClickListener(this.mCallback1);
            this.tvConnect.setOnClickListener(this.mCallback2);
            this.tvHelp.setOnClickListener(this.mCallback3);
        }
    }

    @Override // com.gizthon.camera.generated.callback.OnClickListener.Listener
    public final void _internalCallbackOnClick(int i, View view) {
        if (i == 1) {
            SplashActivity splashActivity = this.mEventHandler;
            if (splashActivity != null) {
                splashActivity.onClickGallery();
                return;
            }
            return;
        }
        if (i == 2) {
            SplashActivity splashActivity2 = this.mEventHandler;
            if (splashActivity2 != null) {
                splashActivity2.onClickUsb();
                return;
            }
            return;
        }
        if (i != 3) {
            return;
        }
        SplashActivity splashActivity3 = this.mEventHandler;
        if (splashActivity3 != null) {
            splashActivity3.onClickHelp();
        }
    }
}
