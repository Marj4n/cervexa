package com.jieli.stream.dv.running2.ui.widget.coverflow;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import com.jieli.stream.dv.running2.util.Dbug;

/* loaded from: classes.dex */
public class CoverFlowLayoutManger extends RecyclerView.LayoutManager {
    private static int SCROLL_LEFT = 1;
    private static int SCROLL_RIGHT = 2;
    private ValueAnimator mAnimation;
    private float mIntervalRatio;
    private boolean mIsFlatFlow;
    private boolean mItemGradualAlpha;
    private boolean mItemGradualGrey;
    private RecyclerView.Recycler mRecycle;
    private OnSelected mSelectedListener;
    private RecyclerView.State mState;
    String tag = getClass().getSimpleName();
    private int mOffsetAll = 0;
    private int mDecoratedChildWidth = 0;
    private int mDecoratedChildHeight = 0;
    private int mStartX = 0;
    private int mStartY = 0;
    private SparseArray<Rect> mAllItemFrames = new SparseArray<>();
    private SparseBooleanArray mHasAttachedItems = new SparseBooleanArray();
    private int mSelectPosition = 0;
    private int mLastSelectPosition = 0;

    public interface OnSelected {
        void onItemSelected(int i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean canScrollHorizontally() {
        return true;
    }

    public CoverFlowLayoutManger(boolean z, boolean z2, boolean z3, float f) {
        this.mIntervalRatio = 0.5f;
        this.mIsFlatFlow = false;
        this.mItemGradualGrey = false;
        this.mItemGradualAlpha = false;
        this.mIsFlatFlow = z;
        this.mItemGradualGrey = z2;
        this.mItemGradualAlpha = z3;
        if (f >= 0.0f) {
            this.mIntervalRatio = f;
        } else if (z) {
            this.mIntervalRatio = 1.1f;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(-2, -2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int i;
        if (getItemCount() <= 0 || state.isPreLayout()) {
            this.mOffsetAll = 0;
            return;
        }
        this.mAllItemFrames.clear();
        this.mHasAttachedItems.clear();
        View viewForPosition = recycler.getViewForPosition(0);
        addView(viewForPosition);
        measureChildWithMargins(viewForPosition, 0, 0);
        this.mDecoratedChildWidth = getDecoratedMeasuredWidth(viewForPosition);
        this.mDecoratedChildHeight = getDecoratedMeasuredHeight(viewForPosition);
        this.mStartX = Math.round(((getHorizontalSpace() - this.mDecoratedChildWidth) * 1.0f) / 2.0f);
        this.mStartY = Math.round(((getVerticalSpace() - this.mDecoratedChildHeight) * 1.0f) / 2.0f);
        float f = this.mStartX;
        for (int i2 = 0; i2 < getItemCount(); i2++) {
            Rect rect = this.mAllItemFrames.get(i2);
            if (rect == null) {
                rect = new Rect();
            }
            rect.set(Math.round(f), this.mStartY, Math.round(this.mDecoratedChildWidth + f), this.mStartY + this.mDecoratedChildHeight);
            this.mAllItemFrames.put(i2, rect);
            this.mHasAttachedItems.put(i2, false);
            f += getIntervalDistance();
        }
        detachAndScrapAttachedViews(recycler);
        if ((this.mRecycle == null || this.mState == null) && (i = this.mSelectPosition) != 0) {
            this.mOffsetAll = calculateOffsetForPosition(i);
            onSelectedCallBack();
        }
        layoutItems(recycler, state, SCROLL_RIGHT);
        this.mRecycle = recycler;
        this.mState = state;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollHorizontallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int maxOffset;
        ValueAnimator valueAnimator = this.mAnimation;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mAnimation.cancel();
        }
        int i2 = this.mOffsetAll;
        if (i + i2 < 0) {
            maxOffset = -i2;
        } else {
            maxOffset = ((float) (i2 + i)) > getMaxOffset() ? (int) (getMaxOffset() - this.mOffsetAll) : i;
        }
        this.mOffsetAll += maxOffset;
        layoutItems(recycler, state, i > 0 ? SCROLL_RIGHT : SCROLL_LEFT);
        return maxOffset;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void layoutItems(RecyclerView.Recycler recycler, RecyclerView.State state, int i) {
        if (state == null || state.isPreLayout()) {
            return;
        }
        int i2 = this.mOffsetAll;
        Rect rect = new Rect(i2, 0, getHorizontalSpace() + i2, getVerticalSpace());
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            int position = getPosition(childAt);
            if (this.mAllItemFrames.get(position) != null && !Rect.intersects(rect, this.mAllItemFrames.get(position))) {
                removeAndRecycleView(childAt, recycler);
                this.mHasAttachedItems.put(position, false);
            } else {
                layoutItem(childAt, this.mAllItemFrames.get(position));
                this.mHasAttachedItems.put(position, true);
            }
        }
        for (int i4 = 0; i4 < getItemCount(); i4++) {
            if (this.mAllItemFrames.get(i4) != null && Rect.intersects(rect, this.mAllItemFrames.get(i4)) && !this.mHasAttachedItems.get(i4)) {
                View viewForPosition = recycler.getViewForPosition(i4);
                measureChildWithMargins(viewForPosition, 0, 0);
                if (i == SCROLL_LEFT || this.mIsFlatFlow) {
                    addView(viewForPosition, 0);
                } else {
                    addView(viewForPosition);
                }
                layoutItem(viewForPosition, this.mAllItemFrames.get(i4));
                this.mHasAttachedItems.put(i4, true);
            }
        }
    }

    private void layoutItem(View view, Rect rect) {
        layoutDecorated(view, rect.left - this.mOffsetAll, rect.top, rect.right - this.mOffsetAll, rect.bottom);
        if (!this.mIsFlatFlow) {
            view.setScaleX(computeScale(rect.left - this.mOffsetAll));
            view.setScaleY(computeScale(rect.left - this.mOffsetAll));
        }
        if (this.mItemGradualAlpha) {
            view.setAlpha(computeAlpha(rect.left - this.mOffsetAll));
        }
        if (this.mItemGradualGrey) {
            greyItem(view, rect);
        }
    }

    private void greyItem(View view, Rect rect) {
        float computeGreyScale = computeGreyScale(rect.left - this.mOffsetAll);
        float f = 1.0f - computeGreyScale;
        float f2 = 120.0f * f;
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{computeGreyScale, 0.0f, 0.0f, 0.0f, f2, 0.0f, computeGreyScale, 0.0f, 0.0f, f2, 0.0f, 0.0f, computeGreyScale, 0.0f, f2, 0.0f, 0.0f, 0.0f, 1.0f, f * 250.0f});
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        view.setLayerType(2, paint);
        if (computeGreyScale >= 1.0f) {
            view.setLayerType(0, null);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onScrollStateChanged(int i) {
        super.onScrollStateChanged(i);
        if (i == 0) {
            Dbug.i(this.tag, "scroll_state_idle");
            fixOffsetWhenFinishScroll();
        } else if (i == 1) {
            Dbug.i(this.tag, "scroll_state_dragginf");
        } else {
            if (i != 2) {
                return;
            }
            Dbug.i(this.tag, "scroll_state_settling");
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void scrollToPosition(int i) {
        RecyclerView.State state;
        if (i < 0 || i > getItemCount() - 1) {
            return;
        }
        this.mOffsetAll = calculateOffsetForPosition(i);
        RecyclerView.Recycler recycler = this.mRecycle;
        if (recycler == null || (state = this.mState) == null) {
            this.mSelectPosition = i;
        } else {
            layoutItems(recycler, state, i > this.mSelectPosition ? SCROLL_RIGHT : SCROLL_LEFT);
            onSelectedCallBack();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
        int calculateOffsetForPosition = calculateOffsetForPosition(i);
        if (this.mRecycle == null || this.mState == null) {
            this.mSelectPosition = i;
        } else {
            startScroll(this.mOffsetAll, calculateOffsetForPosition);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onAdapterChanged(RecyclerView.Adapter adapter, RecyclerView.Adapter adapter2) {
        removeAllViews();
        this.mRecycle = null;
        this.mState = null;
        this.mOffsetAll = 0;
        this.mSelectPosition = 0;
        this.mLastSelectPosition = 0;
        this.mHasAttachedItems.clear();
        this.mAllItemFrames.clear();
    }

    private int getHorizontalSpace() {
        return (getWidth() - getPaddingRight()) - getPaddingLeft();
    }

    private int getVerticalSpace() {
        return (getHeight() - getPaddingBottom()) - getPaddingTop();
    }

    private float getMaxOffset() {
        return (getItemCount() - 1) * getIntervalDistance();
    }

    private float computeScale(int i) {
        float abs = 1.0f - ((Math.abs(i - this.mStartX) * 1.0f) / Math.abs(this.mStartX + (this.mDecoratedChildWidth / this.mIntervalRatio)));
        if (abs < 0.0f) {
            abs = 0.0f;
        }
        if (abs > 1.0f) {
            return 1.0f;
        }
        return abs;
    }

    private float computeGreyScale(int i) {
        float abs = 1.0f - ((Math.abs((i + (this.mDecoratedChildWidth / 2)) - (getHorizontalSpace() / 2)) * 1.0f) / (getHorizontalSpace() / 2));
        if (abs < 0.1d) {
            abs = 0.1f;
        }
        return (float) Math.pow(abs <= 1.0f ? abs : 1.0f, 0.8d);
    }

    private float computeAlpha(int i) {
        float abs = 1.0f - ((Math.abs(i - this.mStartX) * 1.0f) / Math.abs(this.mStartX + (this.mDecoratedChildWidth / this.mIntervalRatio)));
        if (abs < 0.3f) {
            abs = 0.3f;
        }
        if (abs > 1.0f) {
            return 1.0f;
        }
        return abs;
    }

    private int calculateOffsetForPosition(int i) {
        return Math.round(getIntervalDistance() * i);
    }

    private void fixOffsetWhenFinishScroll() {
        int intervalDistance = (int) ((this.mOffsetAll * 1.0f) / getIntervalDistance());
        if (this.mOffsetAll % getIntervalDistance() > getIntervalDistance() * 0.5d) {
            intervalDistance++;
        }
        int intervalDistance2 = (int) (intervalDistance * getIntervalDistance());
        startScroll(this.mOffsetAll, intervalDistance2);
        this.mSelectPosition = Math.round((intervalDistance2 * 1.0f) / getIntervalDistance());
    }

    private void startScroll(int i, int i2) {
        ValueAnimator valueAnimator = this.mAnimation;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mAnimation.cancel();
        }
        final int i3 = i < i2 ? SCROLL_RIGHT : SCROLL_LEFT;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(i, i2);
        this.mAnimation = ofFloat;
        ofFloat.setDuration(500L);
        this.mAnimation.setInterpolator(new DecelerateInterpolator());
        this.mAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.jieli.stream.dv.running2.ui.widget.coverflow.CoverFlowLayoutManger.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                CoverFlowLayoutManger.this.mOffsetAll = Math.round(((Float) valueAnimator2.getAnimatedValue()).floatValue());
                CoverFlowLayoutManger coverFlowLayoutManger = CoverFlowLayoutManger.this;
                coverFlowLayoutManger.layoutItems(coverFlowLayoutManger.mRecycle, CoverFlowLayoutManger.this.mState, i3);
            }
        });
        this.mAnimation.addListener(new Animator.AnimatorListener() { // from class: com.jieli.stream.dv.running2.ui.widget.coverflow.CoverFlowLayoutManger.2
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                CoverFlowLayoutManger.this.onSelectedCallBack();
            }
        });
        this.mAnimation.start();
    }

    private float getIntervalDistance() {
        return this.mDecoratedChildWidth * this.mIntervalRatio;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSelectedCallBack() {
        int abs = Math.abs(Math.round(this.mOffsetAll / getIntervalDistance()));
        this.mSelectPosition = abs;
        OnSelected onSelected = this.mSelectedListener;
        if (onSelected != null && abs != this.mLastSelectPosition) {
            onSelected.onItemSelected(abs);
        }
        this.mLastSelectPosition = this.mSelectPosition;
    }

    public int getFirstVisiblePosition() {
        int i = 0;
        for (int i2 = 0; i2 < this.mHasAttachedItems.size() && !this.mHasAttachedItems.get(i2); i2++) {
            i++;
        }
        return i;
    }

    public int getLastVisiblePosition() {
        int size = this.mHasAttachedItems.size() - 1;
        for (int size2 = this.mHasAttachedItems.size() - 1; size2 > 0 && !this.mHasAttachedItems.get(size2); size2--) {
            size--;
        }
        return size;
    }

    public int getMaxVisibleCount() {
        return (((int) ((getHorizontalSpace() - this.mStartX) / getIntervalDistance())) * 2) + 1;
    }

    public int getCenterPosition() {
        int intervalDistance = (int) (this.mOffsetAll / getIntervalDistance());
        return ((float) ((int) (((float) this.mOffsetAll) % getIntervalDistance()))) > getIntervalDistance() * 0.5f ? intervalDistance + 1 : intervalDistance;
    }

    public void setOnSelectedListener(OnSelected onSelected) {
        this.mSelectedListener = onSelected;
    }

    public int getSelectedPos() {
        return this.mSelectPosition;
    }

    public void setSelectPosition(int i) {
        startScroll(this.mSelectPosition, i);
    }

    public static class Builder {
        boolean isFlat = false;
        boolean isGreyItem = false;
        boolean isAlphaItem = false;
        float cstIntervalRatio = -1.0f;

        public Builder setFlat(boolean z) {
            this.isFlat = z;
            return this;
        }

        public Builder setGreyItem(boolean z) {
            this.isGreyItem = z;
            return this;
        }

        public Builder setAlphaItem(boolean z) {
            this.isAlphaItem = z;
            return this;
        }

        public Builder setIntervalRatio(float f) {
            this.cstIntervalRatio = f;
            return this;
        }

        public CoverFlowLayoutManger build() {
            return new CoverFlowLayoutManger(this.isFlat, this.isGreyItem, this.isAlphaItem, this.cstIntervalRatio);
        }
    }
}
