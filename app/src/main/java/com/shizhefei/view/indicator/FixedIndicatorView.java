package com.shizhefei.view.indicator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import androidx.core.view.ViewCompat;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import java.util.LinkedList;
import java.util.List;
import tv.danmaku.ijk.media.player.IjkMediaCodecInfo;

/* loaded from: classes2.dex */
public class FixedIndicatorView extends LinearLayout implements Indicator {
    public static final int SPLITMETHOD_EQUALS = 0;
    public static final int SPLITMETHOD_WEIGHT = 1;
    public static final int SPLITMETHOD_WRAP = 2;
    private Bitmap cacheBitmap;
    private Canvas cacheCanvas;
    private Matrix cacheMatrix;
    private View centerView;
    private LinearLayout.LayoutParams centerViewLayoutParams;
    private Indicator.DataSetObserver dataSetObserver;
    private InRun inRun;
    private boolean itemClickable;
    private Indicator.IndicatorAdapter mAdapter;
    private int mPosition;
    private float mPositionOffset;
    private int mPositionOffsetPixels;
    private int mPreSelectedTabIndex;
    private int mSelectedTabIndex;
    private View.OnClickListener onClickListener;
    private Indicator.OnItemSelectedListener onItemSelectedListener;
    private Indicator.OnTransitionListener onTransitionListener;
    private int[] prePositions;
    private ScrollBar scrollBar;
    private int splitMethod;
    private int state;
    private List<ViewGroup> views;

    public FixedIndicatorView(Context context) {
        super(context);
        this.mSelectedTabIndex = -1;
        this.splitMethod = 0;
        this.state = 0;
        this.itemClickable = true;
        this.mPreSelectedTabIndex = -1;
        this.views = new LinkedList();
        this.dataSetObserver = new Indicator.DataSetObserver() { // from class: com.shizhefei.view.indicator.FixedIndicatorView.1
            @Override // com.shizhefei.view.indicator.Indicator.DataSetObserver
            public void onChange() {
                View view;
                if (!FixedIndicatorView.this.inRun.isFinished()) {
                    FixedIndicatorView.this.inRun.stop();
                }
                int tabCountInLayout = FixedIndicatorView.this.getTabCountInLayout();
                int count = FixedIndicatorView.this.mAdapter.getCount();
                FixedIndicatorView.this.views.clear();
                for (int i = 0; i < tabCountInLayout && i < count; i++) {
                    FixedIndicatorView.this.views.add((ViewGroup) FixedIndicatorView.this.getItemOutView(i));
                }
                FixedIndicatorView.this.removeAllViews();
                int size = FixedIndicatorView.this.views.size();
                int i2 = 0;
                while (i2 < count) {
                    LinearLayout linearLayout = new LinearLayout(FixedIndicatorView.this.getContext());
                    if (i2 < size) {
                        View childAt = ((ViewGroup) FixedIndicatorView.this.views.get(i2)).getChildAt(0);
                        ((ViewGroup) FixedIndicatorView.this.views.get(i2)).removeView(childAt);
                        view = FixedIndicatorView.this.mAdapter.getView(i2, childAt, linearLayout);
                    } else {
                        view = FixedIndicatorView.this.mAdapter.getView(i2, null, linearLayout);
                    }
                    if (FixedIndicatorView.this.onTransitionListener != null) {
                        FixedIndicatorView.this.onTransitionListener.onTransition(view, i2, i2 == FixedIndicatorView.this.mSelectedTabIndex ? 1.0f : 0.0f);
                    }
                    linearLayout.addView(view);
                    linearLayout.setOnClickListener(FixedIndicatorView.this.onClickListener);
                    linearLayout.setTag(Integer.valueOf(i2));
                    FixedIndicatorView.this.addView(linearLayout, new LinearLayout.LayoutParams(-2, -1));
                    i2++;
                }
                if (FixedIndicatorView.this.centerView != null) {
                    FixedIndicatorView fixedIndicatorView = FixedIndicatorView.this;
                    fixedIndicatorView.setCenterView(fixedIndicatorView.centerView, FixedIndicatorView.this.centerViewLayoutParams);
                }
                FixedIndicatorView.this.mPreSelectedTabIndex = -1;
                FixedIndicatorView fixedIndicatorView2 = FixedIndicatorView.this;
                fixedIndicatorView2.setCurrentItem(fixedIndicatorView2.mSelectedTabIndex, false);
                FixedIndicatorView.this.measureTabs();
            }
        };
        this.onClickListener = new View.OnClickListener() { // from class: com.shizhefei.view.indicator.FixedIndicatorView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (FixedIndicatorView.this.itemClickable) {
                    int intValue = ((Integer) view.getTag()).intValue();
                    ViewGroup viewGroup = (ViewGroup) view;
                    FixedIndicatorView.this.setCurrentItem(intValue);
                    if (FixedIndicatorView.this.onItemSelectedListener != null) {
                        FixedIndicatorView.this.onItemSelectedListener.onItemSelected(viewGroup.getChildAt(0), intValue, FixedIndicatorView.this.mPreSelectedTabIndex);
                    }
                }
            }
        };
        this.cacheMatrix = new Matrix();
        this.cacheCanvas = new Canvas();
        this.prePositions = new int[]{-1, -1};
        init();
    }

    public FixedIndicatorView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSelectedTabIndex = -1;
        this.splitMethod = 0;
        this.state = 0;
        this.itemClickable = true;
        this.mPreSelectedTabIndex = -1;
        this.views = new LinkedList();
        this.dataSetObserver = new Indicator.DataSetObserver() { // from class: com.shizhefei.view.indicator.FixedIndicatorView.1
            @Override // com.shizhefei.view.indicator.Indicator.DataSetObserver
            public void onChange() {
                View view;
                if (!FixedIndicatorView.this.inRun.isFinished()) {
                    FixedIndicatorView.this.inRun.stop();
                }
                int tabCountInLayout = FixedIndicatorView.this.getTabCountInLayout();
                int count = FixedIndicatorView.this.mAdapter.getCount();
                FixedIndicatorView.this.views.clear();
                for (int i2 = 0; i2 < tabCountInLayout && i2 < count; i2++) {
                    FixedIndicatorView.this.views.add((ViewGroup) FixedIndicatorView.this.getItemOutView(i2));
                }
                FixedIndicatorView.this.removeAllViews();
                int size = FixedIndicatorView.this.views.size();
                int i22 = 0;
                while (i22 < count) {
                    LinearLayout linearLayout = new LinearLayout(FixedIndicatorView.this.getContext());
                    if (i22 < size) {
                        View childAt = ((ViewGroup) FixedIndicatorView.this.views.get(i22)).getChildAt(0);
                        ((ViewGroup) FixedIndicatorView.this.views.get(i22)).removeView(childAt);
                        view = FixedIndicatorView.this.mAdapter.getView(i22, childAt, linearLayout);
                    } else {
                        view = FixedIndicatorView.this.mAdapter.getView(i22, null, linearLayout);
                    }
                    if (FixedIndicatorView.this.onTransitionListener != null) {
                        FixedIndicatorView.this.onTransitionListener.onTransition(view, i22, i22 == FixedIndicatorView.this.mSelectedTabIndex ? 1.0f : 0.0f);
                    }
                    linearLayout.addView(view);
                    linearLayout.setOnClickListener(FixedIndicatorView.this.onClickListener);
                    linearLayout.setTag(Integer.valueOf(i22));
                    FixedIndicatorView.this.addView(linearLayout, new LinearLayout.LayoutParams(-2, -1));
                    i22++;
                }
                if (FixedIndicatorView.this.centerView != null) {
                    FixedIndicatorView fixedIndicatorView = FixedIndicatorView.this;
                    fixedIndicatorView.setCenterView(fixedIndicatorView.centerView, FixedIndicatorView.this.centerViewLayoutParams);
                }
                FixedIndicatorView.this.mPreSelectedTabIndex = -1;
                FixedIndicatorView fixedIndicatorView2 = FixedIndicatorView.this;
                fixedIndicatorView2.setCurrentItem(fixedIndicatorView2.mSelectedTabIndex, false);
                FixedIndicatorView.this.measureTabs();
            }
        };
        this.onClickListener = new View.OnClickListener() { // from class: com.shizhefei.view.indicator.FixedIndicatorView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (FixedIndicatorView.this.itemClickable) {
                    int intValue = ((Integer) view.getTag()).intValue();
                    ViewGroup viewGroup = (ViewGroup) view;
                    FixedIndicatorView.this.setCurrentItem(intValue);
                    if (FixedIndicatorView.this.onItemSelectedListener != null) {
                        FixedIndicatorView.this.onItemSelectedListener.onItemSelected(viewGroup.getChildAt(0), intValue, FixedIndicatorView.this.mPreSelectedTabIndex);
                    }
                }
            }
        };
        this.cacheMatrix = new Matrix();
        this.cacheCanvas = new Canvas();
        this.prePositions = new int[]{-1, -1};
        init();
    }

    public FixedIndicatorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSelectedTabIndex = -1;
        this.splitMethod = 0;
        this.state = 0;
        this.itemClickable = true;
        this.mPreSelectedTabIndex = -1;
        this.views = new LinkedList();
        this.dataSetObserver = new Indicator.DataSetObserver() { // from class: com.shizhefei.view.indicator.FixedIndicatorView.1
            @Override // com.shizhefei.view.indicator.Indicator.DataSetObserver
            public void onChange() {
                View view;
                if (!FixedIndicatorView.this.inRun.isFinished()) {
                    FixedIndicatorView.this.inRun.stop();
                }
                int tabCountInLayout = FixedIndicatorView.this.getTabCountInLayout();
                int count = FixedIndicatorView.this.mAdapter.getCount();
                FixedIndicatorView.this.views.clear();
                for (int i2 = 0; i2 < tabCountInLayout && i2 < count; i2++) {
                    FixedIndicatorView.this.views.add((ViewGroup) FixedIndicatorView.this.getItemOutView(i2));
                }
                FixedIndicatorView.this.removeAllViews();
                int size = FixedIndicatorView.this.views.size();
                int i22 = 0;
                while (i22 < count) {
                    LinearLayout linearLayout = new LinearLayout(FixedIndicatorView.this.getContext());
                    if (i22 < size) {
                        View childAt = ((ViewGroup) FixedIndicatorView.this.views.get(i22)).getChildAt(0);
                        ((ViewGroup) FixedIndicatorView.this.views.get(i22)).removeView(childAt);
                        view = FixedIndicatorView.this.mAdapter.getView(i22, childAt, linearLayout);
                    } else {
                        view = FixedIndicatorView.this.mAdapter.getView(i22, null, linearLayout);
                    }
                    if (FixedIndicatorView.this.onTransitionListener != null) {
                        FixedIndicatorView.this.onTransitionListener.onTransition(view, i22, i22 == FixedIndicatorView.this.mSelectedTabIndex ? 1.0f : 0.0f);
                    }
                    linearLayout.addView(view);
                    linearLayout.setOnClickListener(FixedIndicatorView.this.onClickListener);
                    linearLayout.setTag(Integer.valueOf(i22));
                    FixedIndicatorView.this.addView(linearLayout, new LinearLayout.LayoutParams(-2, -1));
                    i22++;
                }
                if (FixedIndicatorView.this.centerView != null) {
                    FixedIndicatorView fixedIndicatorView = FixedIndicatorView.this;
                    fixedIndicatorView.setCenterView(fixedIndicatorView.centerView, FixedIndicatorView.this.centerViewLayoutParams);
                }
                FixedIndicatorView.this.mPreSelectedTabIndex = -1;
                FixedIndicatorView fixedIndicatorView2 = FixedIndicatorView.this;
                fixedIndicatorView2.setCurrentItem(fixedIndicatorView2.mSelectedTabIndex, false);
                FixedIndicatorView.this.measureTabs();
            }
        };
        this.onClickListener = new View.OnClickListener() { // from class: com.shizhefei.view.indicator.FixedIndicatorView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (FixedIndicatorView.this.itemClickable) {
                    int intValue = ((Integer) view.getTag()).intValue();
                    ViewGroup viewGroup = (ViewGroup) view;
                    FixedIndicatorView.this.setCurrentItem(intValue);
                    if (FixedIndicatorView.this.onItemSelectedListener != null) {
                        FixedIndicatorView.this.onItemSelectedListener.onItemSelected(viewGroup.getChildAt(0), intValue, FixedIndicatorView.this.mPreSelectedTabIndex);
                    }
                }
            }
        };
        this.cacheMatrix = new Matrix();
        this.cacheCanvas = new Canvas();
        this.prePositions = new int[]{-1, -1};
        init();
    }

    private void init() {
        this.inRun = new InRun();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.inRun.stop();
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setAdapter(Indicator.IndicatorAdapter indicatorAdapter) {
        Indicator.IndicatorAdapter indicatorAdapter2 = this.mAdapter;
        if (indicatorAdapter2 != null) {
            indicatorAdapter2.unRegistDataSetObserver(this.dataSetObserver);
        }
        this.mAdapter = indicatorAdapter;
        indicatorAdapter.registDataSetObserver(this.dataSetObserver);
        indicatorAdapter.notifyDataSetChanged();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 1) {
            View childAt = getChildAt(0);
            this.centerView = childAt;
            this.centerViewLayoutParams = (LinearLayout.LayoutParams) childAt.getLayoutParams();
        }
    }

    public ScrollBar getScrollBar() {
        return this.scrollBar;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setOnItemSelectListener(Indicator.OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public Indicator.IndicatorAdapter getIndicatorAdapter() {
        return this.mAdapter;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setCurrentItem(int i) {
        setCurrentItem(i, true);
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setItemClickable(boolean z) {
        this.itemClickable = z;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public boolean isItemClickable() {
        return this.itemClickable;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setCurrentItem(int i, boolean z) {
        int i2;
        int count = getCount();
        if (count == 0) {
            return;
        }
        if (i < 0) {
            i = 0;
        } else {
            int i3 = count - 1;
            if (i > i3) {
                i = i3;
            }
        }
        int i4 = this.mSelectedTabIndex;
        if (i4 != i) {
            this.mPreSelectedTabIndex = i4;
            this.mSelectedTabIndex = i;
            if (!this.inRun.isFinished()) {
                this.inRun.stop();
            }
            if (this.state == 0) {
                updateTabSelectState(i);
                if (z && getMeasuredWidth() != 0 && getItemOutView(i).getMeasuredWidth() != 0 && (i2 = this.mPreSelectedTabIndex) >= 0 && i2 < getTabCountInLayout()) {
                    this.inRun.startScroll(getItemOutView(this.mPreSelectedTabIndex).getLeft(), getItemOutView(i).getLeft(), Math.min((int) (((Math.abs(r0 - r4) / getItemOutView(i).getMeasuredWidth()) + 1.0f) * 100.0f), IjkMediaCodecInfo.RANK_LAST_CHANCE));
                    return;
                }
                notifyPageScrolled(i, 0.0f, 0);
                return;
            }
            if (this.onTransitionListener == null) {
                updateTabSelectState(i);
            }
        }
    }

    private void updateTabSelectState(int i) {
        Indicator.IndicatorAdapter indicatorAdapter = this.mAdapter;
        if (indicatorAdapter == null) {
            return;
        }
        int count = indicatorAdapter.getCount();
        int i2 = 0;
        while (i2 < count) {
            View itemViewUnCheck = getItemViewUnCheck(i2);
            if (itemViewUnCheck != null) {
                itemViewUnCheck.setSelected(i == i2);
            }
            i2++;
        }
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public int getCurrentItem() {
        return this.mSelectedTabIndex;
    }

    public void setSplitMethod(int i) {
        this.splitMethod = i;
        measureTabs();
    }

    public int getSplitMethod() {
        return this.splitMethod;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setScrollBar(ScrollBar scrollBar) {
        int paddingBottom = getPaddingBottom();
        int paddingTop = getPaddingTop();
        if (this.scrollBar != null) {
            int i = AnonymousClass3.$SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity[this.scrollBar.getGravity().ordinal()];
            if (i == 1) {
                paddingBottom -= scrollBar.getHeight(getHeight());
            } else if (i == 2) {
                paddingTop -= scrollBar.getHeight(getHeight());
            }
        }
        this.scrollBar = scrollBar;
        int i2 = AnonymousClass3.$SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity[this.scrollBar.getGravity().ordinal()];
        if (i2 == 1) {
            paddingBottom += scrollBar.getHeight(getHeight());
        } else if (i2 == 2) {
            paddingTop += scrollBar.getHeight(getHeight());
        }
        setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), paddingBottom);
    }

    /* renamed from: com.shizhefei.view.indicator.FixedIndicatorView$3, reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity;

        static {
            int[] iArr = new int[ScrollBar.Gravity.values().length];
            $SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity = iArr;
            try {
                iArr[ScrollBar.Gravity.BOTTOM_FLOAT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity[ScrollBar.Gravity.TOP_FLOAT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity[ScrollBar.Gravity.CENTENT_BACKGROUND.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity[ScrollBar.Gravity.CENTENT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity[ScrollBar.Gravity.TOP.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity[ScrollBar.Gravity.BOTTOM.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    private class InRun implements Runnable {
        private Scroller scroller;
        private int updateTime = 20;
        private final Interpolator sInterpolator = new Interpolator() { // from class: com.shizhefei.view.indicator.FixedIndicatorView.InRun.1
            @Override // android.animation.TimeInterpolator
            public float getInterpolation(float f) {
                float f2 = f - 1.0f;
                return (f2 * f2 * f2 * f2 * f2) + 1.0f;
            }
        };

        public InRun() {
            this.scroller = new Scroller(FixedIndicatorView.this.getContext(), this.sInterpolator);
        }

        public void startScroll(int i, int i2, int i3) {
            this.scroller.startScroll(i, 0, i2 - i, 0, i3);
            ViewCompat.postInvalidateOnAnimation(FixedIndicatorView.this);
            FixedIndicatorView.this.post(this);
        }

        public boolean isFinished() {
            return this.scroller.isFinished();
        }

        public boolean computeScrollOffset() {
            return this.scroller.computeScrollOffset();
        }

        public int getCurrentX() {
            return this.scroller.getCurrX();
        }

        public void stop() {
            if (this.scroller.isFinished()) {
                this.scroller.abortAnimation();
            }
            FixedIndicatorView.this.removeCallbacks(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            ViewCompat.postInvalidateOnAnimation(FixedIndicatorView.this);
            if (this.scroller.isFinished()) {
                return;
            }
            FixedIndicatorView.this.postDelayed(this, this.updateTime);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
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

    /* JADX WARN: Removed duplicated region for block: B:38:0x0124  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00e3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void drawSlideBar(android.graphics.Canvas r15) {
        /*
            Method dump skipped, instructions count: 426
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shizhefei.view.indicator.FixedIndicatorView.drawSlideBar(android.graphics.Canvas):void");
    }

    private void notifyPageScrolled(int i, float f, int i2) {
        View itemView;
        if (i < 0 || i > getCount() - 1) {
            return;
        }
        ScrollBar scrollBar = this.scrollBar;
        if (scrollBar != null) {
            scrollBar.onPageScrolled(i, f, i2);
        }
        if (this.onTransitionListener != null) {
            for (int i3 : this.prePositions) {
                if (i3 != i && i3 != i + 1 && (itemView = getItemView(i3)) != null) {
                    this.onTransitionListener.onTransition(itemView, i3, 0.0f);
                }
            }
            int[] iArr = this.prePositions;
            iArr[0] = i;
            int i4 = i + 1;
            iArr[1] = i4;
            View itemView2 = getItemView(this.mPreSelectedTabIndex);
            if (itemView2 != null) {
                this.onTransitionListener.onTransition(itemView2, this.mPreSelectedTabIndex, 0.0f);
            }
            View itemView3 = getItemView(i);
            if (itemView3 != null) {
                this.onTransitionListener.onTransition(itemView3, i, 1.0f - f);
            }
            View itemView4 = getItemView(i4);
            if (itemView4 != null) {
                this.onTransitionListener.onTransition(itemView4, i4, f);
            }
        }
    }

    private int measureScrollBar(int i, float f, boolean z) {
        View itemOutView;
        ScrollBar scrollBar = this.scrollBar;
        if (scrollBar == null) {
            return 0;
        }
        View slideView = scrollBar.getSlideView();
        if (slideView.isLayoutRequested() || z) {
            View itemOutView2 = getItemOutView(i);
            int i2 = i + 1;
            if (i2 < this.mAdapter.getCount()) {
                itemOutView = getItemOutView(i2);
            } else {
                itemOutView = getItemOutView(0);
            }
            if (itemOutView2 != null) {
                int width = (int) ((itemOutView2.getWidth() * (1.0f - f)) + (itemOutView == null ? 0.0f : itemOutView.getWidth() * f));
                int width2 = this.scrollBar.getWidth(width);
                int height = this.scrollBar.getHeight(getHeight());
                slideView.measure(width2, height);
                slideView.layout(0, 0, width2, height);
                return width;
            }
        }
        return this.scrollBar.getSlideView().getWidth();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void measureTabs() {
        int tabCountInLayout = getTabCountInLayout();
        int i = this.splitMethod;
        int i2 = 0;
        if (i == 0) {
            for (int i3 = 0; i3 < tabCountInLayout; i3++) {
                View itemOutView = getItemOutView(i3);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) itemOutView.getLayoutParams();
                layoutParams.width = 0;
                layoutParams.weight = 1.0f;
                itemOutView.setLayoutParams(layoutParams);
            }
            return;
        }
        if (i == 1) {
            while (i2 < tabCountInLayout) {
                View itemOutView2 = getItemOutView(i2);
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) itemOutView2.getLayoutParams();
                layoutParams2.width = -2;
                layoutParams2.weight = 1.0f;
                itemOutView2.setLayoutParams(layoutParams2);
                i2++;
            }
            return;
        }
        if (i != 2) {
            return;
        }
        while (i2 < tabCountInLayout) {
            View itemOutView3 = getItemOutView(i2);
            LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) itemOutView3.getLayoutParams();
            layoutParams3.width = -2;
            layoutParams3.weight = 0.0f;
            itemOutView3.setLayoutParams(layoutParams3);
            i2++;
        }
    }

    public int getCount() {
        Indicator.IndicatorAdapter indicatorAdapter = this.mAdapter;
        if (indicatorAdapter == null) {
            return 0;
        }
        return indicatorAdapter.getCount();
    }

    @Override // android.view.ViewGroup
    protected void measureChildren(int i, int i2) {
        super.measureChildren(i, i2);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        measureScrollBar(this.mSelectedTabIndex, 1.0f, true);
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void onPageScrolled(int i, float f, int i2) {
        this.mPosition = i;
        this.mPositionOffset = f;
        this.mPositionOffsetPixels = i2;
        if (this.scrollBar != null) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            notifyPageScrolled(i, f, i2);
        }
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void onPageScrollStateChanged(int i) {
        this.state = i;
        if (i == 0) {
            updateTabSelectState(this.mSelectedTabIndex);
        }
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setOnTransitionListener(Indicator.OnTransitionListener onTransitionListener) {
        this.onTransitionListener = onTransitionListener;
        updateTabSelectState(this.mSelectedTabIndex);
        if (this.mAdapter != null) {
            int i = 0;
            while (i < this.mAdapter.getCount()) {
                View itemView = getItemView(i);
                if (itemView != null) {
                    onTransitionListener.onTransition(itemView, i, this.mSelectedTabIndex == i ? 1.0f : 0.0f);
                }
                i++;
            }
        }
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public View getItemView(int i) {
        if (i < 0 || i > this.mAdapter.getCount() - 1) {
            return null;
        }
        return getItemViewUnCheck(i);
    }

    private View getItemViewUnCheck(int i) {
        return ((ViewGroup) getItemOutView(i)).getChildAt(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public View getItemOutView(int i) {
        if (this.centerView != null && i >= (getChildCount() - 1) / 2) {
            i++;
        }
        return getChildAt(i);
    }

    public void setCenterView(View view, int i, int i2) {
        this.centerView = view;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(i, i2);
        layoutParams.gravity = 16;
        setCenterView(view, layoutParams);
    }

    public void setCenterView(View view) {
        setCenterView(view, view.getLayoutParams());
    }

    public void setCenterView(View view, ViewGroup.LayoutParams layoutParams) {
        LinearLayout.LayoutParams generateLayoutParams;
        removeCenterView();
        if (layoutParams == null) {
            generateLayoutParams = new LinearLayout.LayoutParams(-2, -1);
        } else {
            generateLayoutParams = generateLayoutParams(layoutParams);
        }
        this.centerViewLayoutParams = generateLayoutParams;
        this.centerView = view;
        addView(view, getChildCount() / 2, generateLayoutParams);
    }

    public View getCenterView() {
        return this.centerView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTabCountInLayout() {
        if (this.centerView != null) {
            return getChildCount() - 1;
        }
        return getChildCount();
    }

    public void removeCenterView() {
        View view = this.centerView;
        if (view != null) {
            removeView(view);
            this.centerView = null;
        }
        this.centerViewLayoutParams = null;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public Indicator.OnItemSelectedListener getOnItemSelectListener() {
        return this.onItemSelectedListener;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public Indicator.OnTransitionListener getOnTransitionListener() {
        return this.onTransitionListener;
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public int getPreSelectItem() {
        return this.mPreSelectedTabIndex;
    }
}
