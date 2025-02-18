package com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Loadable;
import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Refreshable;

/* loaded from: classes.dex */
public class PullRefreshLayout extends FlingLayout {
    protected boolean hasFooter;
    protected boolean hasHeader;
    protected Loadable mFooter;
    protected Refreshable mHeader;

    public PullRefreshLayout(Context context) {
        this(context, null);
    }

    public PullRefreshLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PullRefreshLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.hasHeader = true;
        this.hasFooter = true;
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.FlingLayout
    protected boolean onScroll(float f) {
        Refreshable refreshable = this.mHeader;
        if (refreshable != null && this.hasHeader && f >= 0.0f) {
            boolean onScroll = refreshable.onScroll(f);
            if (f != 0.0f) {
                return onScroll;
            }
        }
        Loadable loadable = this.mFooter;
        if (loadable == null || !this.hasFooter || f > 0.0f) {
            return false;
        }
        boolean onScroll2 = loadable.onScroll(f);
        if (f != 0.0f) {
            return onScroll2;
        }
        return false;
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.FlingLayout
    protected void onScrollChange(int i) {
        Refreshable refreshable = this.mHeader;
        if (refreshable != null && this.hasHeader) {
            refreshable.onScrollChange(i);
        }
        Loadable loadable = this.mFooter;
        if (loadable == null || !this.hasFooter) {
            return;
        }
        loadable.onScrollChange(i);
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.FlingLayout
    protected boolean onStartFling(float f) {
        Refreshable refreshable = this.mHeader;
        if (refreshable != null && f > 0.0f && this.hasHeader) {
            return refreshable.onStartFling(f);
        }
        Loadable loadable = this.mFooter;
        if (loadable == null || f >= 0.0f || !this.hasFooter) {
            return false;
        }
        return loadable.onStartFling(f);
    }

    public void startRefresh() {
        Refreshable refreshable = this.mHeader;
        if (refreshable == null || !this.hasHeader) {
            return;
        }
        refreshable.startRefresh();
    }

    public void startLoad() {
        Loadable loadable = this.mFooter;
        if (loadable == null || !this.hasFooter) {
            return;
        }
        loadable.startLoad();
    }

    public void stopRefresh() {
        Refreshable refreshable = this.mHeader;
        if (refreshable == null || !this.hasHeader) {
            return;
        }
        refreshable.stopRefresh();
    }

    public void stopLoad() {
        Loadable loadable = this.mFooter;
        if (loadable == null || !this.hasFooter) {
            return;
        }
        loadable.stopLoad();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.FlingLayout, android.view.ViewGroup
    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        if ((view instanceof Refreshable) && this.mHeader == null) {
            Refreshable refreshable = (Refreshable) view;
            this.mHeader = refreshable;
            refreshable.setPullRefreshLayout(this);
        } else if ((view instanceof Loadable) && this.mFooter == null) {
            Loadable loadable = (Loadable) view;
            this.mFooter = loadable;
            loadable.setPullRefreshLayout(this);
        }
        super.addView(view, i, layoutParams);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        int height = getHeight();
        Object obj = this.mHeader;
        if (obj != null) {
            View view = (View) obj;
            view.layout(view.getLeft(), -view.getMeasuredHeight(), view.getRight(), 0);
        }
        Object obj2 = this.mFooter;
        if (obj2 != null) {
            View view2 = (View) obj2;
            view2.layout(view2.getLeft(), height, view2.getRight(), view2.getMeasuredHeight() + height);
        }
    }

    public void setHasFooter(boolean z) {
        this.hasFooter = z;
    }

    public void setHasHeader(boolean z) {
        this.hasHeader = z;
    }
}
