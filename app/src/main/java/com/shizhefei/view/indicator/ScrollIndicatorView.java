package com.shizhefei.view.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.slidebar.ScrollBar;

/* loaded from: classes2.dex */
public class ScrollIndicatorView extends HorizontalScrollView implements Indicator {
    private Drawable customShadowDrawable;
    private Indicator.DataSetObserver dataSetObserver;
    private Paint defaultShadowPaint;
    private SFixedIndicatorView fixedIndicatorView;
    private boolean isPinnedTabView;
    private boolean mActionDownHappened;
    private Runnable mTabSelector;
    private Drawable pinnedTabBgDrawable;
    private View pinnedTabView;
    private float positionOffset;
    private final ProxyOnItemSelectListener proxyOnItemSelectListener;
    private int shadowWidth;
    private int state;
    private int unScrollPosition;

    public ScrollIndicatorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isPinnedTabView = false;
        this.defaultShadowPaint = null;
        this.state = 0;
        this.dataSetObserver = new Indicator.DataSetObserver() { // from class: com.shizhefei.view.indicator.ScrollIndicatorView.1
            @Override // com.shizhefei.view.indicator.Indicator.DataSetObserver
            public void onChange() {
                if (ScrollIndicatorView.this.mTabSelector != null) {
                    ScrollIndicatorView scrollIndicatorView = ScrollIndicatorView.this;
                    scrollIndicatorView.removeCallbacks(scrollIndicatorView.mTabSelector);
                }
                ScrollIndicatorView.this.positionOffset = 0.0f;
                ScrollIndicatorView scrollIndicatorView2 = ScrollIndicatorView.this;
                scrollIndicatorView2.setCurrentItem(scrollIndicatorView2.fixedIndicatorView.getCurrentItem(), false);
                if (!ScrollIndicatorView.this.isPinnedTabView || ScrollIndicatorView.this.fixedIndicatorView.getChildCount() <= 0) {
                    return;
                }
                ScrollIndicatorView scrollIndicatorView3 = ScrollIndicatorView.this;
                scrollIndicatorView3.pinnedTabView = scrollIndicatorView3.fixedIndicatorView.getChildAt(0);
            }
        };
        this.unScrollPosition = -1;
        SFixedIndicatorView sFixedIndicatorView = new SFixedIndicatorView(context);
        this.fixedIndicatorView = sFixedIndicatorView;
        addView(sFixedIndicatorView, new FrameLayout.LayoutParams(-2, -1));
        setHorizontalScrollBarEnabled(false);
        setSplitAuto(true);
        Paint paint = new Paint();
        this.defaultShadowPaint = paint;
        paint.setAntiAlias(true);
        this.defaultShadowPaint.setColor(866822826);
        int dipToPix = dipToPix(3.0f);
        this.shadowWidth = dipToPix;
        this.defaultShadowPaint.setShadowLayer(dipToPix, 0.0f, 0.0f, ViewCompat.MEASURED_STATE_MASK);
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(1, null);
        }
        SFixedIndicatorView sFixedIndicatorView2 = this.fixedIndicatorView;
        ProxyOnItemSelectListener proxyOnItemSelectListener = new ProxyOnItemSelectListener();
        this.proxyOnItemSelectListener = proxyOnItemSelectListener;
        sFixedIndicatorView2.setOnItemSelectListener(proxyOnItemSelectListener);
    }

    public void setSplitAuto(boolean z) {
        setFillViewport(z);
        this.fixedIndicatorView.setSplitAuto(z);
    }

    public boolean isSplitAuto() {
        return this.fixedIndicatorView.isSplitAuto();
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setAdapter(Indicator.IndicatorAdapter indicatorAdapter) {
        if (getIndicatorAdapter() != null) {
            getIndicatorAdapter().unRegistDataSetObserver(this.dataSetObserver);
        }
        this.fixedIndicatorView.setAdapter(indicatorAdapter);
        indicatorAdapter.registDataSetObserver(this.dataSetObserver);
        this.dataSetObserver.onChange();
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setOnItemSelectListener(Indicator.OnItemSelectedListener onItemSelectedListener) {
        this.proxyOnItemSelectListener.setOnItemSelectedListener(onItemSelectedListener);
    }

    private class ProxyOnItemSelectListener implements Indicator.OnItemSelectedListener {
        private Indicator.OnItemSelectedListener onItemSelectedListener;

        private ProxyOnItemSelectListener() {
        }

        public void setOnItemSelectedListener(Indicator.OnItemSelectedListener onItemSelectedListener) {
            this.onItemSelectedListener = onItemSelectedListener;
        }

        public Indicator.OnItemSelectedListener getOnItemSelectedListener() {
            return this.onItemSelectedListener;
        }

        @Override // com.shizhefei.view.indicator.Indicator.OnItemSelectedListener
        public void onItemSelected(View view, int i, int i2) {
            if (ScrollIndicatorView.this.state == 0) {
                ScrollIndicatorView.this.animateToTab(i);
            }
            Indicator.OnItemSelectedListener onItemSelectedListener = this.onItemSelectedListener;
            if (onItemSelectedListener != null) {
                onItemSelectedListener.onItemSelected(view, i, i2);
            }
        }
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public Indicator.IndicatorAdapter getIndicatorAdapter() {
        return this.fixedIndicatorView.getIndicatorAdapter();
    }

    public void setPinnedTabView(boolean z) {
        this.isPinnedTabView = z;
        if (z && this.fixedIndicatorView.getChildCount() > 0) {
            this.pinnedTabView = this.fixedIndicatorView.getChildAt(0);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setPinnedShadow(Drawable drawable, int i) {
        this.customShadowDrawable = drawable;
        this.shadowWidth = i;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setPinnedTabBg(Drawable drawable) {
        this.pinnedTabBgDrawable = drawable;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setPinnedTabBgColor(int i) {
        setPinnedTabBg(new ColorDrawable(i));
    }

    public void setPinnedTabBgId(int i) {
        setPinnedTabBg(ContextCompat.getDrawable(getContext(), i));
    }

    public void setPinnedShadow(int i, int i2) {
        setPinnedShadow(ContextCompat.getDrawable(getContext(), i), i2);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            post(runnable);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.fixedIndicatorView.getCount() > 0) {
            animateToTab(this.fixedIndicatorView.getCurrentItem());
        }
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        View childAt;
        int left;
        super.onLayout(z, i, i2, i3, i4);
        int i5 = this.unScrollPosition;
        if (i5 == -1 || (childAt = this.fixedIndicatorView.getChildAt(i5)) == null || (left = childAt.getLeft() - ((getMeasuredWidth() - childAt.getMeasuredWidth()) / 2)) < 0) {
            return;
        }
        smoothScrollTo(left, 0);
        this.unScrollPosition = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateToTab(int i) {
        if (i < 0 || i > this.fixedIndicatorView.getCount() - 1) {
            return;
        }
        final View childAt = this.fixedIndicatorView.getChildAt(i);
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
        Runnable runnable2 = new Runnable() { // from class: com.shizhefei.view.indicator.ScrollIndicatorView.2
            @Override // java.lang.Runnable
            public void run() {
                ScrollIndicatorView.this.smoothScrollTo(childAt.getLeft() - ((ScrollIndicatorView.this.getWidth() - childAt.getWidth()) / 2), 0);
                ScrollIndicatorView.this.mTabSelector = null;
            }
        };
        this.mTabSelector = runnable2;
        post(runnable2);
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setCurrentItem(int i) {
        setCurrentItem(i, true);
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setCurrentItem(int i, boolean z) {
        int count = this.fixedIndicatorView.getCount();
        if (count == 0) {
            return;
        }
        if (i < 0) {
            i = 0;
        } else {
            int i2 = count - 1;
            if (i > i2) {
                i = i2;
            }
        }
        this.unScrollPosition = -1;
        if (this.state == 0) {
            if (z) {
                animateToTab(i);
            } else {
                View childAt = this.fixedIndicatorView.getChildAt(i);
                scrollTo(childAt.getLeft() - ((getWidth() - childAt.getWidth()) / 2), 0);
                this.unScrollPosition = i;
            }
        }
        this.fixedIndicatorView.setCurrentItem(i, z);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.isPinnedTabView) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (this.pinnedTabView != null && y >= r2.getTop() && y <= this.pinnedTabView.getBottom() && x > this.pinnedTabView.getLeft() && x < this.pinnedTabView.getRight()) {
                if (motionEvent.getAction() == 0) {
                    this.mActionDownHappened = true;
                } else if (motionEvent.getAction() == 1 && this.mActionDownHappened) {
                    this.pinnedTabView.performClick();
                    invalidate(new Rect(0, 0, this.pinnedTabView.getMeasuredWidth(), this.pinnedTabView.getMeasuredHeight()));
                    this.mActionDownHappened = false;
                }
                return true;
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public int getCurrentItem() {
        return this.fixedIndicatorView.getCurrentItem();
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public Indicator.OnItemSelectedListener getOnItemSelectListener() {
        return this.proxyOnItemSelectListener.getOnItemSelectedListener();
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setOnTransitionListener(Indicator.OnTransitionListener onTransitionListener) {
        this.fixedIndicatorView.setOnTransitionListener(onTransitionListener);
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public Indicator.OnTransitionListener getOnTransitionListener() {
        return this.fixedIndicatorView.getOnTransitionListener();
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setScrollBar(ScrollBar scrollBar) {
        this.fixedIndicatorView.setScrollBar(scrollBar);
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void onPageScrolled(int i, float f, int i2) {
        this.positionOffset = f;
        if (this.fixedIndicatorView.getChildAt(i) == null) {
            return;
        }
        scrollTo((int) ((r0.getLeft() - ((getWidth() - r0.getWidth()) / 2)) + (((r0.getWidth() + (this.fixedIndicatorView.getChildAt(i + 1) == null ? r0.getWidth() : r1.getWidth())) / 2) * f)), 0);
        this.fixedIndicatorView.onPageScrolled(i, f, i2);
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void onPageScrollStateChanged(int i) {
        this.state = i;
        this.fixedIndicatorView.onPageScrollStateChanged(i);
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public void setItemClickable(boolean z) {
        this.fixedIndicatorView.setItemClickable(z);
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public boolean isItemClickable() {
        return this.fixedIndicatorView.isItemClickable();
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public int getPreSelectItem() {
        return this.fixedIndicatorView.getPreSelectItem();
    }

    @Override // com.shizhefei.view.indicator.Indicator
    public View getItemView(int i) {
        return this.fixedIndicatorView.getItemView(i);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.isPinnedTabView) {
            int scrollX = getScrollX();
            if (this.pinnedTabView == null || scrollX <= 0) {
                return;
            }
            int save = canvas.save();
            canvas.translate(scrollX + getPaddingLeft(), getPaddingTop());
            Drawable drawable = this.pinnedTabBgDrawable;
            if (drawable != null) {
                drawable.setBounds(0, 0, this.pinnedTabView.getWidth(), this.pinnedTabView.getHeight());
                this.pinnedTabBgDrawable.draw(canvas);
            }
            ScrollBar scrollBar = this.fixedIndicatorView.getScrollBar();
            if (scrollBar != null && scrollBar.getGravity() == ScrollBar.Gravity.CENTENT_BACKGROUND) {
                drawScrollBar(canvas);
            }
            this.pinnedTabView.draw(canvas);
            if (scrollBar != null && scrollBar.getGravity() != ScrollBar.Gravity.CENTENT_BACKGROUND) {
                drawScrollBar(canvas);
            }
            canvas.translate(this.pinnedTabView.getWidth(), 0.0f);
            int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
            Drawable drawable2 = this.customShadowDrawable;
            if (drawable2 != null) {
                drawable2.setBounds(0, 0, this.shadowWidth, height);
                this.customShadowDrawable.draw(canvas);
            } else {
                canvas.clipRect(0, 0, this.shadowWidth + dipToPix(1.0f), height);
                canvas.drawRect(0.0f, 0.0f, dipToPix(1.0f), height, this.defaultShadowPaint);
            }
            canvas.restoreToCount(save);
        }
    }

    private void drawScrollBar(Canvas canvas) {
        int height;
        ScrollBar scrollBar = this.fixedIndicatorView.getScrollBar();
        if (scrollBar == null || this.fixedIndicatorView.getCurrentItem() != 0) {
            return;
        }
        int save = canvas.save();
        int i = AnonymousClass3.$SwitchMap$com$shizhefei$view$indicator$slidebar$ScrollBar$Gravity[scrollBar.getGravity().ordinal()];
        if (i == 1 || i == 2) {
            height = (getHeight() - scrollBar.getHeight(getHeight())) / 2;
        } else {
            height = (i == 3 || i == 4) ? 0 : getHeight() - scrollBar.getHeight(getHeight());
        }
        int width = scrollBar.getWidth(this.pinnedTabView.getWidth());
        int height2 = scrollBar.getHeight(this.pinnedTabView.getHeight());
        scrollBar.getSlideView().measure(width, height2);
        scrollBar.getSlideView().layout(0, 0, width, height2);
        canvas.translate((this.pinnedTabView.getWidth() - width) / 2, height);
        canvas.clipRect(0, 0, width, height2);
        scrollBar.getSlideView().draw(canvas);
        canvas.restoreToCount(save);
    }

    /* renamed from: com.shizhefei.view.indicator.ScrollIndicatorView$3, reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
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

    private static class SFixedIndicatorView extends FixedIndicatorView {
        private boolean isAutoSplit;

        public SFixedIndicatorView(Context context) {
            super(context);
        }

        public boolean isSplitAuto() {
            return this.isAutoSplit;
        }

        public void setSplitAuto(boolean z) {
            if (this.isAutoSplit != z) {
                this.isAutoSplit = z;
                if (!z) {
                    setSplitMethod(2);
                }
                requestLayout();
                invalidate();
            }
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            ScrollIndicatorView scrollIndicatorView;
            int measuredWidth;
            if (this.isAutoSplit && (measuredWidth = (scrollIndicatorView = (ScrollIndicatorView) getParent()).getMeasuredWidth()) != 0) {
                int childCount = getChildCount();
                int i3 = 0;
                int i4 = 0;
                for (int i5 = 0; i5 < childCount; i5++) {
                    int measureChildWidth = measureChildWidth(getChildAt(i5), i, i2);
                    if (i4 < measureChildWidth) {
                        i4 = measureChildWidth;
                    }
                    i3 += measureChildWidth;
                }
                if (i3 > measuredWidth) {
                    scrollIndicatorView.setFillViewport(false);
                    setSplitMethod(2);
                } else if (i4 * childCount > measuredWidth) {
                    scrollIndicatorView.setFillViewport(true);
                    setSplitMethod(1);
                } else {
                    scrollIndicatorView.setFillViewport(true);
                    setSplitMethod(0);
                }
            }
            super.onMeasure(i, i2);
        }

        private int measureChildWidth(View view, int i, int i2) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            view.measure(ViewGroup.getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight(), -2), ViewGroup.getChildMeasureSpec(i2, getPaddingTop() + getPaddingBottom(), layoutParams.height));
            return view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
        }
    }

    private int dipToPix(float f) {
        return (int) TypedValue.applyDimension(1, f, getResources().getDisplayMetrics());
    }
}
