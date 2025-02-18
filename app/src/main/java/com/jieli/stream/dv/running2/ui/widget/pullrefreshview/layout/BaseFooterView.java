package com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Loadable;

/* loaded from: classes.dex */
public abstract class BaseFooterView extends RelativeLayout implements Loadable {
    public static final int LOADING = 3;
    public static final int LOAD_CLONE = 4;
    public static final int LOOSENT_O_LOAD = 2;
    public static final int NONE = 0;
    public static final int PULLING = 1;
    private boolean isLockState;
    private OnLoadListener onLoadListener;
    private PullRefreshLayout pullRefreshLayout;
    private int scrollState;
    private int stateType;

    public interface OnLoadListener {
        void onLoad(BaseFooterView baseFooterView);
    }

    public int getLayoutType() {
        return 0;
    }

    public abstract float getSpanHeight();

    protected abstract void onStateChange(int i);

    public BaseFooterView(Context context) {
        this(context, null);
    }

    public BaseFooterView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BaseFooterView(Context context, AttributeSet attributeSet, int i) {
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
        Log.i("BaseFooterView", "" + i);
        this.stateType = i;
        if (i == 3) {
            this.isLockState = true;
            OnLoadListener onLoadListener = this.onLoadListener;
            if (onLoadListener != null) {
                onLoadListener.onLoad(this);
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
            if (moveY < 0.0f) {
                this.pullRefreshLayout.startMoveTo(moveY, 0.0f);
                setState(0);
            }
        }
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Loadable
    public void setPullRefreshLayout(PullRefreshLayout pullRefreshLayout) {
        this.pullRefreshLayout = pullRefreshLayout;
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Loadable
    public void startLoad() {
        PullRefreshLayout pullRefreshLayout = this.pullRefreshLayout;
        if (pullRefreshLayout == null || pullRefreshLayout.getMoveY() != 0.0f) {
            return;
        }
        this.pullRefreshLayout.startMoveTo(0.0f, -getSpanHeight());
        setState(3);
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Loadable
    public void stopLoad() {
        this.isLockState = false;
        setState(4);
        postDelayed(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.BaseFooterView.1
            @Override // java.lang.Runnable
            public void run() {
                BaseFooterView.this.close();
            }
        }, 400L);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0030  */
    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Loadable
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
            if (r0 != r2) goto L13
            int r0 = r4.getMeasuredHeight()
            int r0 = -r0
            float r0 = (float) r0
            androidx.core.view.ViewCompat.setTranslationY(r4, r0)
            goto L27
        L13:
            if (r0 != r1) goto L24
            androidx.core.view.ViewCompat.setTranslationY(r4, r5)
            com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.PullRefreshLayout r0 = r4.pullRefreshLayout
            android.view.View r0 = r0.getPullView()
            r2 = 0
            androidx.core.view.ViewCompat.setTranslationY(r0, r2)
            r0 = 1
            goto L28
        L24:
            androidx.core.view.ViewCompat.setTranslationY(r4, r5)
        L27:
            r0 = 0
        L28:
            float r2 = r4.getSpanHeight()
            int r3 = r4.scrollState
            if (r3 != r1) goto L3d
            float r2 = -r2
            int r5 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
            if (r5 > 0) goto L3a
            r5 = 2
            r4.setState(r5)
            goto L3d
        L3a:
            r4.setState(r1)
        L3d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.BaseFooterView.onScroll(float):boolean");
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Loadable
    public void onScrollChange(int i) {
        this.scrollState = i;
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Loadable
    public boolean onStartFling(float f) {
        float f2 = -getSpanHeight();
        if (f <= f2) {
            this.pullRefreshLayout.startMoveTo(f, f2);
            setState(3);
            return true;
        }
        this.pullRefreshLayout.startMoveTo(f, 0.0f);
        setState(0);
        return false;
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }
}
