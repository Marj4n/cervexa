package com.shizhefei.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/* loaded from: classes2.dex */
public class LazyFragment extends BaseFragment {
    public static final String INTENT_BOOLEAN_LAZYLOAD = "intent_boolean_lazyLoad";
    private static final int VISIBLE_STATE_GONE = 0;
    private static final int VISIBLE_STATE_NOTSET = -1;
    private static final int VISIBLE_STATE_VISIABLE = 1;
    private FrameLayout layout;
    private Bundle savedInstanceState;
    private boolean isInit = false;
    private boolean isLazyLoad = true;
    private int isVisibleToUserState = -1;
    private boolean isStart = false;

    protected View getPreviewLayout(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return null;
    }

    protected void onCreateViewLazy(Bundle bundle) {
    }

    protected void onDestroyViewLazy() {
    }

    protected void onFragmentStartLazy() {
    }

    protected void onFragmentStopLazy() {
    }

    protected void onPauseLazy() {
    }

    protected void onResumeLazy() {
    }

    @Override // com.shizhefei.fragment.BaseFragment
    @Deprecated
    protected final void onCreateView(Bundle bundle) {
        boolean z;
        super.onCreateView(bundle);
        this.savedInstanceState = bundle;
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.isLazyLoad = arguments.getBoolean(INTENT_BOOLEAN_LAZYLOAD, this.isLazyLoad);
        }
        int i = this.isVisibleToUserState;
        if (i == -1) {
            z = getUserVisibleHint();
        } else {
            z = i == 1;
        }
        if (this.isLazyLoad) {
            if (z && !this.isInit) {
                this.isInit = true;
                onCreateViewLazy(bundle);
                return;
            }
            LayoutInflater layoutInflater = this.inflater;
            if (layoutInflater == null) {
                layoutInflater = LayoutInflater.from(getApplicationContext());
            }
            FrameLayout frameLayout = new FrameLayout(layoutInflater.getContext());
            this.layout = frameLayout;
            View previewLayout = getPreviewLayout(layoutInflater, frameLayout);
            if (previewLayout != null) {
                this.layout.addView(previewLayout);
            }
            this.layout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            super.setContentView(this.layout);
            return;
        }
        this.isInit = true;
        onCreateViewLazy(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        this.isVisibleToUserState = z ? 1 : 0;
        if (z && !this.isInit && getContentView() != null) {
            this.isInit = true;
            onCreateViewLazy(this.savedInstanceState);
            onResumeLazy();
        }
        if (!this.isInit || getContentView() == null) {
            return;
        }
        if (z) {
            this.isStart = true;
            onFragmentStartLazy();
        } else {
            this.isStart = false;
            onFragmentStopLazy();
        }
    }

    @Override // androidx.fragment.app.Fragment
    @Deprecated
    public final void onStart() {
        super.onStart();
        if (this.isInit && !this.isStart && getUserVisibleHint()) {
            this.isStart = true;
            onFragmentStartLazy();
        }
    }

    @Override // androidx.fragment.app.Fragment
    @Deprecated
    public final void onStop() {
        super.onStop();
        if (this.isInit && this.isStart && getUserVisibleHint()) {
            this.isStart = false;
            onFragmentStopLazy();
        }
    }

    @Override // com.shizhefei.fragment.BaseFragment
    public void setContentView(int i) {
        if (this.isLazyLoad && getContentView() != null && getContentView().getParent() != null) {
            this.layout.removeAllViews();
            this.layout.addView(this.inflater.inflate(i, (ViewGroup) this.layout, false));
            return;
        }
        super.setContentView(i);
    }

    @Override // com.shizhefei.fragment.BaseFragment
    public void setContentView(View view) {
        if (this.isLazyLoad && getContentView() != null && getContentView().getParent() != null) {
            this.layout.removeAllViews();
            this.layout.addView(view);
        } else {
            super.setContentView(view);
        }
    }

    @Override // androidx.fragment.app.Fragment
    @Deprecated
    public final void onResume() {
        super.onResume();
        if (this.isInit) {
            onResumeLazy();
        }
    }

    @Override // androidx.fragment.app.Fragment
    @Deprecated
    public final void onPause() {
        super.onPause();
        if (this.isInit) {
            onPauseLazy();
        }
    }

    @Override // com.shizhefei.fragment.BaseFragment, androidx.fragment.app.Fragment
    @Deprecated
    public final void onDestroyView() {
        super.onDestroyView();
        if (this.isInit) {
            onDestroyViewLazy();
        }
        this.isInit = false;
    }
}
