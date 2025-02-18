package com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl;

import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.PullRefreshLayout;

/* loaded from: classes.dex */
public interface Refreshable {
    boolean onScroll(float f);

    void onScrollChange(int i);

    boolean onStartFling(float f);

    void setPullRefreshLayout(PullRefreshLayout pullRefreshLayout);

    void startRefresh();

    void stopRefresh();
}
