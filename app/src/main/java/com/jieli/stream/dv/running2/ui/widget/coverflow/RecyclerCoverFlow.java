package com.jieli.stream.dv.running2.ui.widget.coverflow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.recyclerview.widget.RecyclerView;
import com.jieli.stream.dv.running2.ui.widget.coverflow.CoverFlowLayoutManger;

/* loaded from: classes.dex */
public class RecyclerCoverFlow extends RecyclerView {
    private float mDownX;
    private CoverFlowLayoutManger.Builder mManagerBuilder;

    public RecyclerCoverFlow(Context context) {
        super(context);
        init();
    }

    public RecyclerCoverFlow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public RecyclerCoverFlow(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        createManageBuilder();
        setLayoutManager(this.mManagerBuilder.build());
        setChildrenDrawingOrderEnabled(true);
        setOverScrollMode(2);
    }

    private void createManageBuilder() {
        if (this.mManagerBuilder == null) {
            this.mManagerBuilder = new CoverFlowLayoutManger.Builder();
        }
    }

    public void setFlatFlow(boolean z) {
        createManageBuilder();
        this.mManagerBuilder.setFlat(z);
        setLayoutManager(this.mManagerBuilder.build());
    }

    public void setGreyItem(boolean z) {
        createManageBuilder();
        this.mManagerBuilder.setGreyItem(z);
        setLayoutManager(this.mManagerBuilder.build());
    }

    public void setAlphaItem(boolean z) {
        createManageBuilder();
        this.mManagerBuilder.setAlphaItem(z);
        setLayoutManager(this.mManagerBuilder.build());
    }

    public void setIntervalRatio(float f) {
        createManageBuilder();
        this.mManagerBuilder.setIntervalRatio(f);
        setLayoutManager(this.mManagerBuilder.build());
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void setLayoutManager(LayoutManager layoutManager) {
        if (!(layoutManager instanceof CoverFlowLayoutManger)) {
            throw new IllegalArgumentException("The layout manager must be CoverFlowLayoutManger");
        }
        super.setLayoutManager(layoutManager);
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
    protected int getChildDrawingOrder(int i, int i2) {
        int centerPosition = getCoverFlowLayout().getCenterPosition() - getCoverFlowLayout().getFirstVisiblePosition();
        if (centerPosition < 0) {
            centerPosition = 0;
        } else if (centerPosition > i) {
            centerPosition = i;
        }
        return i2 == centerPosition ? i - 1 : i2 > centerPosition ? ((centerPosition + i) - 1) - i2 : i2;
    }

    public CoverFlowLayoutManger getCoverFlowLayout() {
        return (CoverFlowLayoutManger) getLayoutManager();
    }

    public int getSelectedPos() {
        return getCoverFlowLayout().getSelectedPos();
    }

    public void setOnItemSelectedListener(CoverFlowLayoutManger.OnSelected onSelected) {
        getCoverFlowLayout().setOnSelectedListener(onSelected);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mDownX = motionEvent.getX();
            getParent().requestDisallowInterceptTouchEvent(true);
        } else if (action == 2) {
            if ((motionEvent.getX() > this.mDownX && getCoverFlowLayout().getCenterPosition() == 0) || (motionEvent.getX() < this.mDownX && getCoverFlowLayout().getCenterPosition() == getCoverFlowLayout().getItemCount() - 1)) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void setSelectPositionByScroll(int i) {
        getCoverFlowLayout().smoothScrollToPosition(this, new State(), i);
    }

    public void setSelectPosition(int i) {
        getCoverFlowLayout().scrollToPosition(i);
    }
}
