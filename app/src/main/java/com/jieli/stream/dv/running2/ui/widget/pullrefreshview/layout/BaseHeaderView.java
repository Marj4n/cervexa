package com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Refreshable;

/* loaded from: classes.dex */
public abstract class BaseHeaderView extends RelativeLayout implements Refreshable {
    public static final int LOOSENT_O_REFRESH = 2;
    public static final int NONE = 0;
    public static final int PULLING = 1;
    public static final int REFRESHING = 3;
    public static final int REFRESH_CLONE = 4;
    private boolean isLockState;
    OnRefreshListener onRefreshListener;
    private PullRefreshLayout pullRefreshLayout;
    private int scrollState;
    private int stateType;

    public interface OnRefreshListener {
        void onRefresh(BaseHeaderView baseHeaderView);
    }

    public int getLayoutType() {
        return 0;
    }

    public abstract float getSpanHeight();

    protected abstract void onStateChange(int i);

    public BaseHeaderView(Context context) {
        this(context, null);
    }

    public BaseHeaderView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BaseHeaderView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.stateType = 0;
        this.isLockState = false;
        this.scrollState = 0;
        init();
    }

    private void init() {
        setFocusable(false);
        setFocusableInTouchMode(false);
    }

    protected boolean isLockState() {
        return this.isLockState;
    }

    private void setState(int i) {
        if (this.isLockState || this.stateType == i) {
            return;
        }
        Log.i("BaseHeaderView", "" + i);
        this.stateType = i;
        if (i == 3) {
            this.isLockState = true;
            OnRefreshListener onRefreshListener = this.onRefreshListener;
            if (onRefreshListener != null) {
                onRefreshListener.onRefresh(this);
            }
        }
        onStateChange(i);
    }

    public int getType() {
        return this.stateType;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void close() {
        PullRefreshLayout pullRefreshLayout = this.pullRefreshLayout;
        if (pullRefreshLayout != null) {
            float moveY = pullRefreshLayout.getMoveY();
            if (moveY > 0.0f) {
                this.pullRefreshLayout.startMoveTo(moveY, 0.0f);
                setState(0);
            }
        }
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Refreshable
    public void setPullRefreshLayout(PullRefreshLayout pullRefreshLayout) {
        this.pullRefreshLayout = pullRefreshLayout;
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Refreshable
    public void startRefresh() {
        PullRefreshLayout pullRefreshLayout = this.pullRefreshLayout;
        if (pullRefreshLayout == null || pullRefreshLayout.getMoveY() != 0.0f) {
            return;
        }
        this.pullRefreshLayout.startMoveTo(0.0f, getSpanHeight());
        setState(3);
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Refreshable
    public void stopRefresh() {
        this.isLockState = false;
        setState(4);
        postDelayed(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.BaseHeaderView.1
            @Override // java.lang.Runnable
            public void run() {
                BaseHeaderView.this.close();
            }
        }, 400L);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x002f  */
    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Refreshable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onScroll(float r5) {
        /*
            r4 = this;
            int r0 = r4.getLayoutType()
            r1 = 1
            r2 = 16
            if (r0 != r2) goto L12
            int r0 = r4.getMeasuredHeight()
            float r0 = (float) r0
            androidx.core.view.ViewCompat.setTranslationY(r4, r0)
            goto L26
        L12:
            if (r0 != r1) goto L23
            androidx.core.view.ViewCompat.setTranslationY(r4, r5)
            com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.PullRefreshLayout r0 = r4.pullRefreshLayout
            android.view.View r0 = r0.getPullView()
            r2 = 0
            androidx.core.view.ViewCompat.setTranslationY(r0, r2)
            r0 = 1
            goto L27
        L23:
            androidx.core.view.ViewCompat.setTranslationY(r4, r5)
        L26:
            r0 = 0
        L27:
            float r2 = r4.getSpanHeight()
            int r3 = r4.scrollState
            if (r3 != r1) goto L3b
            int r5 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
            if (r5 < 0) goto L38
            r5 = 2
            r4.setState(r5)
            goto L3b
        L38:
            r4.setState(r1)
        L3b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.BaseHeaderView.onScroll(float):boolean");
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Refreshable
    public void onScrollChange(int i) {
        this.scrollState = i;
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Refreshable
    public boolean onStartFling(float f) {
        float spanHeight = getSpanHeight();
        if (f >= spanHeight) {
            this.pullRefreshLayout.startMoveTo(f, spanHeight);
            setState(3);
            return true;
        }
        this.pullRefreshLayout.startMoveTo(f, 0.0f);
        setState(0);
        return false;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }
}
