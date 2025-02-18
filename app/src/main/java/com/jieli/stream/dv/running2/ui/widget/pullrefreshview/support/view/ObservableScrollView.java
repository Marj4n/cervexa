package com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.OnScrollListener;

/* loaded from: classes.dex */
public class ObservableScrollView extends ScrollView {
    private OnScrollListener onScrollListener;

    public ObservableScrollView(Context context) {
        super(context);
        this.onScrollListener = null;
    }

    public ObservableScrollView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.onScrollListener = null;
    }

    public ObservableScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.onScrollListener = null;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override // android.view.View
    protected void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        OnScrollListener onScrollListener = this.onScrollListener;
        if (onScrollListener != null) {
            onScrollListener.onScrollChanged(this, i, i2, i3, i4);
        }
    }
}
