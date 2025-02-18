package com.jieli.stream.dv.running2.ui.widget.pullrefreshview.extras;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;
import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.OnScrollListener;
import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Pullable;

/* loaded from: classes.dex */
public class PullableTextView extends TextView implements Pullable {
    private OnScrollListener onScrollListener;

    public PullableTextView(Context context) {
        super(context);
        this.onScrollListener = null;
        setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public PullableTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.onScrollListener = null;
        setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public PullableTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.onScrollListener = null;
        setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Pullable
    public boolean isGetTop() {
        return getScrollY() == 0;
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Pullable
    public boolean isGetBottom() {
        return getScrollY() >= ((getLayout().getHeight() - getMeasuredHeight()) + getCompoundPaddingBottom()) + getCompoundPaddingTop();
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        OnScrollListener onScrollListener = this.onScrollListener;
        if (onScrollListener != null) {
            onScrollListener.onScrollChanged(this, i, i2, i3, i4);
        }
    }
}
