package com.gizthon.camera.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.adapters.CompoundButtonBindingAdapter;
import com.gizthon.camera.adapter.BindAdapter;
import com.gizthon.camera.adapter.PhotoAdapter;
import com.gizthon.camera.generated.callback.OnClickListener;
import com.gizthon.camera.generated.callback.OnLongClickListener;
import com.gizthon.camera.model.PhotoBean;

/* loaded from: classes.dex */
public class GalleryPhotoItemBindingImpl extends GalleryPhotoItemBinding implements OnLongClickListener.Listener, OnClickListener.Listener {
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;
    private static final SparseIntArray sViewsWithIds = null;
    private final View.OnClickListener mCallback4;
    private final View.OnLongClickListener mCallback5;
    private final View.OnClickListener mCallback6;
    private final View.OnLongClickListener mCallback7;
    private long mDirtyFlags;
    private final RelativeLayout mboundView0;
    private final ImageView mboundView1;
    private final ImageView mboundView2;
    private final CheckBox mboundView3;
    private InverseBindingListener mboundView3androidCheckedAttrChanged;

    public GalleryPhotoItemBindingImpl(DataBindingComponent dataBindingComponent, View view) {
        this(dataBindingComponent, view, mapBindings(dataBindingComponent, view, 4, sIncludes, sViewsWithIds));
    }

    private GalleryPhotoItemBindingImpl(DataBindingComponent dataBindingComponent, View view, Object[] objArr) {
        super(dataBindingComponent, view, 1);
        this.mboundView3androidCheckedAttrChanged = new InverseBindingListener() { // from class: com.gizthon.camera.databinding.GalleryPhotoItemBindingImpl.1
            @Override // androidx.databinding.InverseBindingListener
            public void onChange() {
                boolean isChecked = GalleryPhotoItemBindingImpl.this.mboundView3.isChecked();
                PhotoBean photoBean = GalleryPhotoItemBindingImpl.this.mViewModel;
                if (photoBean != null) {
                    photoBean.setSelected(isChecked);
                }
            }
        };
        this.mDirtyFlags = -1L;
        RelativeLayout relativeLayout = (RelativeLayout) objArr[0];
        this.mboundView0 = relativeLayout;
        relativeLayout.setTag(null);
        ImageView imageView = (ImageView) objArr[1];
        this.mboundView1 = imageView;
        imageView.setTag(null);
        ImageView imageView2 = (ImageView) objArr[2];
        this.mboundView2 = imageView2;
        imageView2.setTag(null);
        CheckBox checkBox = (CheckBox) objArr[3];
        this.mboundView3 = checkBox;
        checkBox.setTag(null);
        setRootTag(view);
        this.mCallback7 = new OnLongClickListener(this, 4);
        this.mCallback5 = new OnLongClickListener(this, 2);
        this.mCallback6 = new OnClickListener(this, 3);
        this.mCallback4 = new OnClickListener(this, 1);
        invalidateAll();
    }

    @Override // androidx.databinding.ViewDataBinding
    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 32L;
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
        if (3 == i) {
            setEventHandler((PhotoAdapter) obj);
        } else {
            if (10 != i) {
                return false;
            }
            setViewModel((PhotoBean) obj);
        }
        return true;
    }

    @Override // com.gizthon.camera.databinding.GalleryPhotoItemBinding
    public void setEventHandler(PhotoAdapter photoAdapter) {
        this.mEventHandler = photoAdapter;
        synchronized (this) {
            this.mDirtyFlags |= 2;
        }
        notifyPropertyChanged(3);
        super.requestRebind();
    }

    @Override // com.gizthon.camera.databinding.GalleryPhotoItemBinding
    public void setViewModel(PhotoBean photoBean) {
        updateRegistration(0, photoBean);
        this.mViewModel = photoBean;
        synchronized (this) {
            this.mDirtyFlags |= 1;
        }
        notifyPropertyChanged(10);
        super.requestRebind();
    }

    @Override // androidx.databinding.ViewDataBinding
    protected boolean onFieldChange(int i, Object obj, int i2) {
        if (i != 0) {
            return false;
        }
        return onChangeViewModel((PhotoBean) obj, i2);
    }

    private boolean onChangeViewModel(PhotoBean photoBean, int i) {
        if (i == 0) {
            synchronized (this) {
                this.mDirtyFlags |= 1;
            }
            return true;
        }
        if (i == 9) {
            synchronized (this) {
                this.mDirtyFlags |= 4;
            }
            return true;
        }
        if (i == 7) {
            synchronized (this) {
                this.mDirtyFlags |= 8;
            }
            return true;
        }
        if (i != 8) {
            return false;
        }
        synchronized (this) {
            this.mDirtyFlags |= 16;
        }
        return true;
    }

    @Override // androidx.databinding.ViewDataBinding
    protected void executeBindings() {
        long j;
        boolean z;
        int i;
        synchronized (this) {
            j = this.mDirtyFlags;
            this.mDirtyFlags = 0L;
        }
        PhotoAdapter photoAdapter = this.mEventHandler;
        PhotoBean photoBean = this.mViewModel;
        int i2 = 0;
        if ((61 & j) != 0) {
            int videoVisible = ((j & 37) == 0 || photoBean == null) ? 0 : photoBean.getVideoVisible();
            i = ((j & 49) == 0 || photoBean == null) ? 0 : photoBean.getSelectedCoverVisible();
            if ((j & 41) == 0 || photoBean == null) {
                i2 = videoVisible;
                z = false;
            } else {
                i2 = videoVisible;
                z = photoBean.isSelected();
            }
        } else {
            z = false;
            i = 0;
        }
        if ((32 & j) != 0) {
            this.mboundView1.setOnClickListener(this.mCallback4);
            this.mboundView1.setOnLongClickListener(this.mCallback5);
            this.mboundView2.setOnClickListener(this.mCallback6);
            this.mboundView2.setOnLongClickListener(this.mCallback7);
            CompoundButtonBindingAdapter.setListeners(this.mboundView3, (CompoundButton.OnCheckedChangeListener) null, this.mboundView3androidCheckedAttrChanged);
        }
        if ((33 & j) != 0) {
            BindAdapter.localSrc(this.mboundView1, photoBean);
        }
        if ((j & 37) != 0) {
            this.mboundView2.setVisibility(i2);
        }
        if ((41 & j) != 0) {
            CompoundButtonBindingAdapter.setChecked(this.mboundView3, z);
        }
        if ((j & 49) != 0) {
            this.mboundView3.setVisibility(i);
        }
    }

    @Override // com.gizthon.camera.generated.callback.OnLongClickListener.Listener
    public final boolean _internalCallbackOnLongClick(int i, View view) {
        if (i == 2) {
            PhotoAdapter photoAdapter = this.mEventHandler;
            PhotoBean photoBean = this.mViewModel;
            if (photoAdapter != null) {
                return photoAdapter.onLongClickPicture(view, photoBean);
            }
            return false;
        }
        if (i != 4) {
            return false;
        }
        PhotoAdapter photoAdapter2 = this.mEventHandler;
        PhotoBean photoBean2 = this.mViewModel;
        if (photoAdapter2 != null) {
            return photoAdapter2.onLongClickPicture(view, photoBean2);
        }
        return false;
    }

    @Override // com.gizthon.camera.generated.callback.OnClickListener.Listener
    public final void _internalCallbackOnClick(int i, View view) {
        if (i == 1) {
            PhotoAdapter photoAdapter = this.mEventHandler;
            PhotoBean photoBean = this.mViewModel;
            if (photoAdapter != null) {
                photoAdapter.onClickPicture(view, photoBean);
                return;
            }
            return;
        }
        if (i != 3) {
            return;
        }
        PhotoAdapter photoAdapter2 = this.mEventHandler;
        PhotoBean photoBean2 = this.mViewModel;
        if (photoAdapter2 != null) {
            photoAdapter2.onClickPicture(view, photoBean2);
        }
    }
}
