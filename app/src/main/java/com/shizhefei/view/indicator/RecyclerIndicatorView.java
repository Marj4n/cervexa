package com.shizhefei.view.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.slidebar.ScrollBar;

/* loaded from: classes2.dex */
public class RecyclerIndicatorView extends RecyclerView implements Indicator {
    private Indicator.IndicatorAdapter adapter;
    private boolean itemClickable;
    private LinearLayoutManager linearLayoutManager;
    private Indicator.OnItemSelectedListener onItemSelectedListener;
    private Indicator.OnTransitionListener onTransitionListener;
    private int pageScrollPosition;
    private int pageScrollState;
    private float positionOffset;
    private int positionOffsetPixels;
    private int[] prePositions;
    private int preSelectItem;
    private ProxyAdapter proxyAdapter;
    private ScrollBar scrollBar;
    private int selectItem;
    private int unScrollPosition;

    @Override // com.shizhefei.view.indicator.Indicator
    public Indicator.OnTransitionListener getOnTransitionListener() {
        return null;
    }

    public RecyclerIndicatorView(Context context) {
        super(context);
        this.unScrollPosition = -1;
        this.prePositions = new int[]{-1, -1};
        this.itemClickable = true;
        init();
    }

    public RecyclerIndicatorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.unScrollPosition = -1;
        this.prePositions = new int[]{-1, -1};
        this.itemClickable = true;
        init();
    }

    public RecyclerIndicatorView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.unScrollPosition = -1;
        this.prePositions = new int[]{-1, -1};
        this.itemClickable = true;
        init();
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), 0, false);
        this.linearLayoutManager = linearLayoutManager;
        setLayoutManager(linearLayoutManager);
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setAdapter(Indicator.IndicatorAdapter indicatorAdapter) {
        this.adapter = indicatorAdapter;
        ProxyAdapter proxyAdapter = new ProxyAdapter(indicatorAdapter);
        this.proxyAdapter = proxyAdapter;
        setAdapter(proxyAdapter);
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setOnItemSelectListener(Indicator.OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public Indicator.OnItemSelectedListener getOnItemSelectListener() {
        return this.onItemSelectedListener;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setOnTransitionListener(Indicator.OnTransitionListener onTransitionListener) {
        this.onTransitionListener = onTransitionListener;
        updateSelectTab(this.selectItem);
        updateTabTransition(this.selectItem);
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setScrollBar(ScrollBar scrollBar) {
        this.scrollBar = scrollBar;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public Indicator.IndicatorAdapter getIndicatorAdapter() {
        return this.adapter;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setCurrentItem(int i) {
        setCurrentItem(i, true);
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setCurrentItem(int i, boolean z) {
        this.preSelectItem = this.selectItem;
        this.selectItem = i;
        if (this.pageScrollState == 0) {
            scrollToTab(i, 0.0f);
            updateSelectTab(i);
            this.unScrollPosition = i;
        } else if (this.onItemSelectedListener == null) {
            updateSelectTab(i);
        }
        Indicator.OnItemSelectedListener onItemSelectedListener = this.onItemSelectedListener;
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(getItemView(i), this.selectItem, this.preSelectItem);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        Indicator.IndicatorAdapter indicatorAdapter = this.adapter;
        if (indicatorAdapter == null || indicatorAdapter.getCount() <= 0) {
            return;
        }
        scrollToTab(this.selectItem, 0.0f);
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        int i5 = this.unScrollPosition;
        if (i5 != -1) {
            this.linearLayoutManager.findViewByPosition(i5);
            scrollToTab(this.unScrollPosition, 0.0f);
            this.unScrollPosition = -1;
        }
    }

    private void smoothScrollToPosition(int i, final int i2) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(getContext()) { // from class: com.shizhefei.view.indicator.RecyclerIndicatorView.1
            @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller
            public PointF computeScrollVectorForPosition(int i3) {
                PointF computeScrollVectorForPosition = RecyclerIndicatorView.this.linearLayoutManager.computeScrollVectorForPosition(i3);
                computeScrollVectorForPosition.x += i2;
                return computeScrollVectorForPosition;
            }
        };
        linearSmoothScroller.setTargetPosition(i);
        this.linearLayoutManager.startSmoothScroll(linearSmoothScroller);
    }

    private void updateSelectTab(int i) {
        View itemView = getItemView(this.preSelectItem);
        if (itemView != null) {
            itemView.setSelected(false);
        }
        View itemView2 = getItemView(i);
        if (itemView2 != null) {
            itemView2.setSelected(true);
        }
    }

    private void updateTabTransition(int i) {
        if (this.onTransitionListener == null) {
            return;
        }
        View itemView = getItemView(this.preSelectItem);
        if (itemView != null) {
            this.onTransitionListener.onTransition(itemView, this.preSelectItem, 0.0f);
        }
        View itemView2 = getItemView(i);
        if (itemView2 != null) {
            this.onTransitionListener.onTransition(itemView2, i, 1.0f);
        }
    }

    protected void scrollToTab(int i, float f) {
        int i2;
        View findViewByPosition = this.linearLayoutManager.findViewByPosition(i);
        int i3 = i + 1;
        View findViewByPosition2 = this.linearLayoutManager.findViewByPosition(i3);
        if (findViewByPosition != null) {
            float measuredWidth = getMeasuredWidth() / 2.0f;
            float measuredWidth2 = measuredWidth - (findViewByPosition.getMeasuredWidth() / 2.0f);
            if (findViewByPosition2 != null) {
                measuredWidth2 -= ((findViewByPosition.getMeasuredWidth() - (measuredWidth - (findViewByPosition2.getMeasuredWidth() / 2.0f))) + measuredWidth2) * f;
            }
            i2 = (int) measuredWidth2;
        } else {
            i2 = 0;
        }
        if (this.onTransitionListener != null) {
            for (int i4 : this.prePositions) {
                View itemView = getItemView(i4);
                if (i4 != i && i4 != i3 && itemView != null) {
                    this.onTransitionListener.onTransition(itemView, i4, 0.0f);
                }
            }
            View itemView2 = getItemView(this.preSelectItem);
            if (itemView2 != null) {
                this.onTransitionListener.onTransition(itemView2, this.preSelectItem, 0.0f);
            }
            this.linearLayoutManager.scrollToPositionWithOffset(i, i2);
            View itemView3 = getItemView(i);
            if (itemView3 != null) {
                this.onTransitionListener.onTransition(itemView3, i, 1.0f - f);
                this.prePositions[0] = i;
            }
            View itemView4 = getItemView(i3);
            if (itemView4 != null) {
                this.onTransitionListener.onTransition(itemView4, i3, f);
                this.prePositions[1] = i3;
            }
        }
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public View getItemView(int i) {
        LinearLayout linearLayout = (LinearLayout) this.linearLayoutManager.findViewByPosition(i);
        return linearLayout != null ? linearLayout.getChildAt(0) : linearLayout;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public int getCurrentItem() {
        return this.selectItem;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public int getPreSelectItem() {
        return this.preSelectItem;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void onPageScrolled(int i, float f, int i2) {
        this.positionOffsetPixels = i2;
        this.pageScrollPosition = i;
        this.positionOffset = f;
        ScrollBar scrollBar = this.scrollBar;
        if (scrollBar != null) {
            scrollBar.onPageScrolled(i, f, i2);
        }
        scrollToTab(i, f);
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void onPageScrollStateChanged(int i) {
        this.pageScrollState = i;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setItemClickable(boolean z) {
        this.itemClickable = z;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public boolean isItemClickable() {
        return this.itemClickable;
    }

    private class ProxyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Indicator.IndicatorAdapter adapter;
        private View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.shizhefei.view.indicator.RecyclerIndicatorView.ProxyAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (RecyclerIndicatorView.this.itemClickable) {
                    RecyclerIndicatorView.this.setCurrentItem(((Integer) view.getTag()).intValue(), true);
                }
            }
        };

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return 1;
        }

        public ProxyAdapter(Indicator.IndicatorAdapter indicatorAdapter) {
            this.adapter = indicatorAdapter;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LinearLayout linearLayout = new LinearLayout(viewGroup.getContext());
            linearLayout.setLayoutParams(new RecyclerView.LayoutParams(-2, -1));
            return new RecyclerView.ViewHolder(linearLayout) { // from class: com.shizhefei.view.indicator.RecyclerIndicatorView.ProxyAdapter.1
            };
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            LinearLayout linearLayout = (LinearLayout) viewHolder.itemView;
            View childAt = linearLayout.getChildAt(0);
            linearLayout.removeAllViews();
            linearLayout.addView(this.adapter.getView(i, childAt, linearLayout));
            linearLayout.setTag(Integer.valueOf(i));
            linearLayout.setOnClickListener(this.onClickListener);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            super.onViewAttachedToWindow(viewHolder);
            int layoutPosition = viewHolder.getLayoutPosition();
            View childAt = ((LinearLayout) viewHolder.itemView).getChildAt(0);
            childAt.setSelected(RecyclerIndicatorView.this.selectItem == layoutPosition);
            if (RecyclerIndicatorView.this.onTransitionListener != null) {
                if (RecyclerIndicatorView.this.selectItem == layoutPosition) {
                    RecyclerIndicatorView.this.onTransitionListener.onTransition(childAt, layoutPosition, 1.0f);
                } else {
                    RecyclerIndicatorView.this.onTransitionListener.onTransition(childAt, layoutPosition, 0.0f);
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.adapter.getCount();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        ScrollBar scrollBar = this.scrollBar;
        if (scrollBar != null && scrollBar.getGravity() == ScrollBar.Gravity.CENTENT_BACKGROUND) {
            drawSlideBar(canvas);
        }
        super.dispatchDraw(canvas);
        ScrollBar scrollBar2 = this.scrollBar;
        if (scrollBar2 == null || scrollBar2.getGravity() == ScrollBar.Gravity.CENTENT_BACKGROUND) {
            return;
        }
        drawSlideBar(canvas);
    }

    private void drawSlideBar(Canvas canvas) {
        int height;
        int measureScrollBar;
        float measuredWidth;
        ProxyAdapter proxyAdapter = this.proxyAdapter;
        if (proxyAdapter == null || this.scrollBar == null || proxyAdapter.getItemCount() == 0) {
            return;
        }
        int i = AnonymousClass2.$SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity[this.scrollBar.getGravity().ordinal()];
        if (i == 1 || i == 2) {
            height = (getHeight() - this.scrollBar.getHeight(getHeight())) / 2;
        } else {
            height = (i == 3 || i == 4) ? 0 : getHeight() - this.scrollBar.getHeight(getHeight());
        }
        if (this.pageScrollState == 0) {
            View findViewByPosition = this.linearLayoutManager.findViewByPosition(this.selectItem);
            measureScrollBar = measureScrollBar(this.selectItem, 0.0f, true);
            if (findViewByPosition == null) {
                return;
            } else {
                measuredWidth = findViewByPosition.getLeft();
            }
        } else {
            View findViewByPosition2 = this.linearLayoutManager.findViewByPosition(this.pageScrollPosition);
            measureScrollBar = measureScrollBar(this.pageScrollPosition, this.positionOffset, true);
            if (findViewByPosition2 == null) {
                return;
            } else {
                measuredWidth = (findViewByPosition2.getMeasuredWidth() * this.positionOffset) + findViewByPosition2.getLeft();
            }
        }
        int width = this.scrollBar.getSlideView().getWidth();
        int save = canvas.save();
        canvas.translate(measuredWidth + ((measureScrollBar - width) / 2), height);
        canvas.clipRect(0, 0, width, this.scrollBar.getSlideView().getHeight());
        this.scrollBar.getSlideView().draw(canvas);
        canvas.restoreToCount(save);
    }

    /* renamed from: com.shizhefei.view.indicator.RecyclerIndicatorView$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity;

        static {
            int[] iArr = new int[ScrollBar.Gravity.values().length];
            $SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity = iArr;
            try {
                iArr[ScrollBar.Gravity.CENTENT_BACKGROUND.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity[ScrollBar.Gravity.CENTENT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity[ScrollBar.Gravity.TOP.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity[ScrollBar.Gravity.TOP_FLOAT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity[ScrollBar.Gravity.BOTTOM.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity[ScrollBar.Gravity.BOTTOM_FLOAT.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    private int measureScrollBar(int i, float f, boolean z) {
        ScrollBar scrollBar = this.scrollBar;
        if (scrollBar == null) {
            return 0;
        }
        View slideView = scrollBar.getSlideView();
        if (slideView.isLayoutRequested() || z) {
            View findViewByPosition = this.linearLayoutManager.findViewByPosition(i);
            View findViewByPosition2 = this.linearLayoutManager.findViewByPosition(i + 1);
            if (findViewByPosition != null) {
                int width = (int) ((findViewByPosition.getWidth() * (1.0f - f)) + (findViewByPosition2 == null ? 0.0f : findViewByPosition2.getWidth() * f));
                int width2 = this.scrollBar.getWidth(width);
                int height = this.scrollBar.getHeight(getHeight());
                slideView.measure(width2, height);
                slideView.layout(0, 0, width2, height);
                return width;
            }
        }
        return this.scrollBar.getSlideView().getWidth();
    }
}
