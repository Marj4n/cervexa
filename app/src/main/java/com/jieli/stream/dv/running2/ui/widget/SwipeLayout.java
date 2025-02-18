package com.jieli.stream.dv.running2.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

/* loaded from: classes.dex */
public class SwipeLayout extends LinearLayout {
    private View actionView;
    private View contentView;
    private int dragDistance;
    private int draggedX;
    private ViewDragHelper viewDragHelper;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public SwipeLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.viewDragHelper = ViewDragHelper.create(this, new DragHelperCallback());
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    public void init() {
        this.contentView = getChildAt(0);
        View childAt = getChildAt(1);
        this.actionView = childAt;
        childAt.setVisibility(8);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.dragDistance = this.actionView.getMeasuredWidth();
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {
        private DragHelperCallback() {
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public boolean tryCaptureView(View view, int i) {
            return view == SwipeLayout.this.contentView || view == SwipeLayout.this.actionView;
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
            SwipeLayout.this.draggedX = i;
            if (view == SwipeLayout.this.contentView) {
                SwipeLayout.this.actionView.offsetLeftAndRight(i3);
            } else {
                SwipeLayout.this.contentView.offsetLeftAndRight(i3);
            }
            if (SwipeLayout.this.actionView.getVisibility() == 8) {
                SwipeLayout.this.actionView.setVisibility(0);
            }
            SwipeLayout.this.invalidate();
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int clampViewPositionHorizontal(View view, int i, int i2) {
            if (view != SwipeLayout.this.contentView) {
                int paddingLeft = (SwipeLayout.this.getPaddingLeft() + SwipeLayout.this.contentView.getMeasuredWidth()) - SwipeLayout.this.dragDistance;
                return Math.min(Math.max(i, paddingLeft), SwipeLayout.this.getPaddingLeft() + SwipeLayout.this.contentView.getMeasuredWidth() + SwipeLayout.this.getPaddingRight());
            }
            return Math.min(Math.max((-SwipeLayout.this.getPaddingLeft()) - SwipeLayout.this.dragDistance, i), 0);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int getViewHorizontalDragRange(View view) {
            return SwipeLayout.this.dragDistance;
        }

        /* JADX WARN: Removed duplicated region for block: B:5:0x0039  */
        /* JADX WARN: Removed duplicated region for block: B:9:0x0041  */
        @Override // androidx.customview.widget.ViewDragHelper.Callback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onViewReleased(android.view.View r5, float r6, float r7) {
            /*
                r4 = this;
                super.onViewReleased(r5, r6, r7)
                double r5 = (double) r6
                r7 = 1
                r0 = 0
                r1 = 4650248090236747776(0x4089000000000000, double:800.0)
                int r3 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
                if (r3 <= 0) goto Le
            Lc:
                r7 = 0
                goto L37
            Le:
                r1 = -4573123946618028032(0xc089000000000000, double:-800.0)
                int r3 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
                if (r3 >= 0) goto L15
                goto L37
            L15:
                com.jieli.stream.dv.running2.ui.widget.SwipeLayout r5 = com.jieli.stream.dv.running2.ui.widget.SwipeLayout.this
                int r5 = com.jieli.stream.dv.running2.ui.widget.SwipeLayout.access$300(r5)
                com.jieli.stream.dv.running2.ui.widget.SwipeLayout r6 = com.jieli.stream.dv.running2.ui.widget.SwipeLayout.this
                int r6 = com.jieli.stream.dv.running2.ui.widget.SwipeLayout.access$400(r6)
                int r6 = -r6
                int r6 = r6 / 2
                if (r5 > r6) goto L27
                goto L37
            L27:
                com.jieli.stream.dv.running2.ui.widget.SwipeLayout r5 = com.jieli.stream.dv.running2.ui.widget.SwipeLayout.this
                int r5 = com.jieli.stream.dv.running2.ui.widget.SwipeLayout.access$300(r5)
                com.jieli.stream.dv.running2.ui.widget.SwipeLayout r6 = com.jieli.stream.dv.running2.ui.widget.SwipeLayout.this
                int r6 = com.jieli.stream.dv.running2.ui.widget.SwipeLayout.access$400(r6)
                int r6 = -r6
                int r6 = r6 / 2
                goto Lc
            L37:
                if (r7 == 0) goto L41
                com.jieli.stream.dv.running2.ui.widget.SwipeLayout r5 = com.jieli.stream.dv.running2.ui.widget.SwipeLayout.this
                int r5 = com.jieli.stream.dv.running2.ui.widget.SwipeLayout.access$400(r5)
                int r5 = -r5
                goto L42
            L41:
                r5 = 0
            L42:
                com.jieli.stream.dv.running2.ui.widget.SwipeLayout r6 = com.jieli.stream.dv.running2.ui.widget.SwipeLayout.this
                androidx.customview.widget.ViewDragHelper r6 = com.jieli.stream.dv.running2.ui.widget.SwipeLayout.access$500(r6)
                com.jieli.stream.dv.running2.ui.widget.SwipeLayout r7 = com.jieli.stream.dv.running2.ui.widget.SwipeLayout.this
                android.view.View r7 = com.jieli.stream.dv.running2.ui.widget.SwipeLayout.access$100(r7)
                r6.smoothSlideViewTo(r7, r5, r0)
                com.jieli.stream.dv.running2.ui.widget.SwipeLayout r5 = com.jieli.stream.dv.running2.ui.widget.SwipeLayout.this
                androidx.core.view.ViewCompat.postInvalidateOnAnimation(r5)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.ui.widget.SwipeLayout.DragHelperCallback.onViewReleased(android.view.View, float, float):void");
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.viewDragHelper.shouldInterceptTouchEvent(motionEvent) || super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.viewDragHelper.processTouchEvent(motionEvent);
        return true;
    }

    @Override // android.view.View
    public void computeScroll() {
        super.computeScroll();
        if (this.viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
