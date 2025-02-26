package com.jieli.stream.dv.running2.ui.widget.pullrefreshview.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;

import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.BaseFooterView;
import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.PullRefreshLayout;
import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.utils.AnimUtil;
import org.apache.commons.net.ftp.FTPReply;

/* loaded from: classes.dex */
public class ExpandFooterView extends BaseFooterView {
    private int layoutType;
    private View loadBox;
    private View progress;
    private int state;
    private View stateImg;

    public ExpandFooterView(Context context) {
        this(context, null);
    }

    public ExpandFooterView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ExpandFooterView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.state = 0;
        this.layoutType = 1;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_footer_expand, (ViewGroup) this, true);
        this.progress = findViewById(R.id.progress);
        this.stateImg = findViewById(R.id.state);
        this.loadBox = findViewById(R.id.load_box);
        setLayoutParams(new ViewGroup.LayoutParams(-1, FTPReply.FILE_ACTION_PENDING));
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.BaseFooterView, com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Loadable
    public void setPullRefreshLayout(PullRefreshLayout pullRefreshLayout) {
        super.setPullRefreshLayout(pullRefreshLayout);
        pullRefreshLayout.setMaxDistance(FTPReply.FILE_ACTION_PENDING);
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.BaseFooterView
    protected void onStateChange(int i) {
        this.state = i;
        ObjectAnimator.ofFloat(this.progress, "rotation", ViewCompat.getRotation(this.progress), 0.0f).start();
        this.stateImg.setVisibility(4);
        this.progress.setVisibility(0);
        ViewCompat.setAlpha(this.progress, 1.0f);
        if (i == 3) {
            View view = this.progress;
            AnimUtil.startRotation(view, 359.99f + ViewCompat.getRotation(view), 500L, 0L, -1);
        } else {
            if (i != 4) {
                return;
            }
            AnimUtil.startShow(this.stateImg, 0.1f, 400L, 200L);
            AnimUtil.startHide(this.progress);
        }
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.BaseFooterView
    public float getSpanHeight() {
        return this.loadBox.getHeight();
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.BaseFooterView
    public int getLayoutType() {
        return this.layoutType;
    }

    @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout.BaseFooterView, com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Loadable
    public boolean onScroll(float f) {
        boolean onScroll = super.onScroll(f);
        if (!isLockState()) {
            ViewCompat.setRotation(this.progress, ((f * f) * 48.0f) / 31250.0f);
        }
        return onScroll;
    }
}
