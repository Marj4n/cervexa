package com.gizthon.camera.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import androidx.viewpager2.widget.ViewPager2;
import com.gizthon.camera.activity.GalleryListActivity;
import com.gizthon.camera.generated.callback.OnClickListener;
import com.gizthon.camera.model.GalleryViewModel;
import com.google.android.material.tabs.TabLayout;
import com.weioa.GoPlusDrone.R;

/* loaded from: classes.dex */
public class GalleryActivityBindingImpl extends GalleryActivityBinding implements OnClickListener.Listener {
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;
    private static final SparseIntArray sViewsWithIds;
    private final View.OnClickListener mCallback10;
    private final View.OnClickListener mCallback11;
    private final View.OnClickListener mCallback8;
    private final View.OnClickListener mCallback9;
    private long mDirtyFlags;
    private final LinearLayout mboundView0;
    private final TextView mboundView2;

    static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        sViewsWithIds = sparseIntArray;
        sparseIntArray.put(R.id.tab_layout, 5);
        sViewsWithIds.put(R.id.pager, 6);
    }

    public GalleryActivityBindingImpl(DataBindingComponent dataBindingComponent, View view) {
        this(dataBindingComponent, view, mapBindings(dataBindingComponent, view, 7, sIncludes, sViewsWithIds));
    }

    private GalleryActivityBindingImpl(DataBindingComponent dataBindingComponent, View view, Object[] objArr) {
        super(dataBindingComponent, view, 1, (TextView) objArr[1], (CheckBox) objArr[3], (ViewPager2) objArr[6], (TabLayout) objArr[5], (TextView) objArr[4]);
        this.mDirtyFlags = -1L;
        this.back.setTag(null);
        this.done.setTag(null);
        LinearLayout linearLayout = (LinearLayout) objArr[0];
        this.mboundView0 = linearLayout;
        linearLayout.setTag(null);
        TextView textView = (TextView) objArr[2];
        this.mboundView2 = textView;
        textView.setTag(null);
        this.tvComplete.setTag(null);
        setRootTag(view);
        this.mCallback10 = new OnClickListener(this, 3);
        this.mCallback11 = new OnClickListener(this, 4);
        this.mCallback9 = new OnClickListener(this, 2);
        this.mCallback8 = new OnClickListener(this, 1);
        invalidateAll();
    }

    @Override // androidx.databinding.ViewDataBinding
    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 8L;
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
            setEventHandler((GalleryListActivity) obj);
        } else {
            if (10 != i) {
                return false;
            }
            setViewModel((GalleryViewModel) obj);
        }
        return true;
    }

    @Override // com.gizthon.camera.databinding.GalleryActivityBinding
    public void setEventHandler(GalleryListActivity galleryListActivity) {
        this.mEventHandler = galleryListActivity;
        synchronized (this) {
            this.mDirtyFlags |= 2;
        }
        notifyPropertyChanged(3);
        super.requestRebind();
    }

    @Override // com.gizthon.camera.databinding.GalleryActivityBinding
    public void setViewModel(GalleryViewModel galleryViewModel) {
        updateRegistration(0, galleryViewModel);
        this.mViewModel = galleryViewModel;
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
        return onChangeViewModel((GalleryViewModel) obj, i2);
    }

    private boolean onChangeViewModel(GalleryViewModel galleryViewModel, int i) {
        if (i == 0) {
            synchronized (this) {
                this.mDirtyFlags |= 1;
            }
            return true;
        }
        if (i != 2) {
            return false;
        }
        synchronized (this) {
            this.mDirtyFlags |= 4;
        }
        return true;
    }

    @Override // androidx.databinding.ViewDataBinding
    protected void executeBindings() {
        long j;
        synchronized (this) {
            j = this.mDirtyFlags;
            this.mDirtyFlags = 0L;
        }
        int i = 0;
        GalleryListActivity galleryListActivity = this.mEventHandler;
        GalleryViewModel galleryViewModel = this.mViewModel;
        long j2 = 13 & j;
        if (j2 != 0 && galleryViewModel != null) {
            i = galleryViewModel.getEditVisible();
        }
        if ((j & 8) != 0) {
            this.back.setOnClickListener(this.mCallback8);
            this.done.setOnClickListener(this.mCallback10);
            this.mboundView2.setOnClickListener(this.mCallback9);
            this.tvComplete.setOnClickListener(this.mCallback11);
        }
        if (j2 != 0) {
            this.done.setVisibility(i);
            this.mboundView2.setVisibility(i);
            this.tvComplete.setVisibility(i);
        }
    }

    @Override // com.gizthon.camera.generated.callback.OnClickListener.Listener
    public final void _internalCallbackOnClick(int i, View view) {
        if (i == 1) {
            GalleryListActivity galleryListActivity = this.mEventHandler;
            if (galleryListActivity != null) {
                galleryListActivity.onClickBack();
                return;
            }
            return;
        }
        if (i == 2) {
            GalleryListActivity galleryListActivity2 = this.mEventHandler;
            if (galleryListActivity2 != null) {
                galleryListActivity2.onClickDelete();
                return;
            }
            return;
        }
        if (i == 3) {
            GalleryListActivity galleryListActivity3 = this.mEventHandler;
            if (galleryListActivity3 != null) {
                galleryListActivity3.onClickDone();
                return;
            }
            return;
        }
        if (i != 4) {
            return;
        }
        GalleryListActivity galleryListActivity4 = this.mEventHandler;
        if (galleryListActivity4 != null) {
            galleryListActivity4.onClickComplete();
        }
    }
}
