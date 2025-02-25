package com.jieli.stream.dv.running2.ui.widget.pullrefreshview.layout;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Pullable;
import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.utils.CanPullUtil;
import com.nineoldandroids.view.ViewHelper;

/* loaded from: classes.dex */
public class FlingLayout extends FrameLayout implements NestedScrollingChild, NestedScrollingParent {
    private static final int MAX_DURATION = 600;
    private static final int MIN_DURATION = 300;
    public static final int SCROLL_STATE_FLING = 2;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_TOUCH_SCROLL = 1;
    protected int MAXDISTANCE;
    private boolean canPullDown;
    private boolean canPullUp;
    protected float downX;
    protected float downY;
    private boolean isScrolling;
    private NestedScrollingChildHelper mChildHelper;
    protected OnScrollListener mOnScrollListener;
    private NestedScrollingParentHelper mParentHelper;
    int mPointerId;
    protected View mPullView;
    private Scroller mScroller;
    private int mTouchSlop;
    protected int maxDistance;
    float moveY;
    protected Pullable pullable;
    private int stateType;
    private int tempStateType;
    protected float tepmX;
    protected float tepmY;
    protected int version;

    public interface OnScrollListener {
        void onScroll(FlingLayout flingLayout, float f);

        void onScrollChange(FlingLayout flingLayout, int i);
    }

    protected boolean onScroll(float f) {
        return false;
    }

    protected void onScrollChange(int i) {
    }

    protected boolean onStartFling(float f) {
        return false;
    }

    public View getPullView() {
        return this.mPullView;
    }

    public FlingLayout(Context context) {
        this(context, null);
    }

    public FlingLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FlingLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.stateType = 0;
        this.tempStateType = 0;
        this.isScrolling = false;
        this.canPullUp = true;
        this.canPullDown = true;
        this.maxDistance = 0;
        this.MAXDISTANCE = 0;
        this.moveY = 0.0f;
        init(context);
    }

    public void init(Context context) {
        this.version = Build.VERSION.SDK_INT;
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mScroller = new Scroller(context, new AccelerateDecelerateInterpolator());
        this.mParentHelper = new NestedScrollingParentHelper(this);
        this.mChildHelper = new NestedScrollingChildHelper(this);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.MAXDISTANCE = (getMeasuredHeight() * 3) / 5;
    }

    @Override // android.view.View
    public void computeScroll() {
        if (!this.mScroller.isFinished()) {
            if (this.mScroller.computeScrollOffset()) {
                moveTo(this.mScroller.getCurrY());
                ViewCompat.postInvalidateOnAnimation(this);
            } else if (this.stateType == 2) {
                setScrollState(0);
            }
        } else if (this.stateType == 2) {
            setScrollState(0);
        }
        super.computeScroll();
    }

    private boolean canPullUp() {
        Pullable pullable;
        if (this.mPullView == null || (pullable = this.pullable) == null) {
            return this.canPullUp;
        }
        return this.canPullUp && pullable.isGetBottom();
    }

    private boolean canPullDown() {
        Pullable pullable;
        if (this.mPullView == null || (pullable = this.pullable) == null) {
            return this.canPullDown;
        }
        return this.canPullDown && pullable.isGetTop();
    }

    private void moveTo(float f) {
        setMoveY(f);
        setScrollState(this.tempStateType);
        boolean onScroll = onScroll(f);
        OnScrollListener onScrollListener = this.mOnScrollListener;
        if (onScrollListener != null) {
            onScrollListener.onScroll(this, f);
        }
        if (onScroll) {
            return;
        }
        setViewTranslationY(this.mPullView, f);
    }

    private void setScrollState(int i) {
        if (this.stateType != i) {
            this.stateType = i;
            this.tempStateType = i;
            onScrollChange(i);
            OnScrollListener onScrollListener = this.mOnScrollListener;
            if (onScrollListener != null) {
                onScrollListener.onScrollChange(this, i);
            }
        }
    }

    private void moveBy(float f) {
        moveTo(getMoveY() + f);
    }

    protected static void setViewTranslationY(View view, float f) {
        if (view == null) {
            return;
        }
        ViewHelper.setTranslationY(view, f);
    }

    private void setMoveY(float f) {
        this.moveY = f;
    }

    public float getMoveY() {
        return this.moveY;
    }

    public int startMoveBy(float f, float f2) {
        setScrollState(2);
        int max = Math.max(300, Math.min(600, (int) Math.abs(f2)));
        this.mScroller.startScroll(0, (int) f, 0, (int) f2, max);
        invalidate();
        return max;
    }

    public int startMoveTo(float f, float f2) {
        return startMoveBy(f, f2 - f);
    }

    private void startFling() {
        float moveY = getMoveY();
        if (moveY != 0.0f) {
            if (onStartFling(moveY)) {
                return;
            }
            startMoveTo(moveY, 0.0f);
            return;
        }
        setScrollState(0);
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        if (this.mPullView == null) {
            Pullable pullAble = CanPullUtil.getPullAble(view);
            this.pullable = pullAble;
            if (pullAble != null) {
                this.mPullView = view;
            }
        }
        super.addView(view, i, layoutParams);
    }

    public void setMaxDistance(int i) {
        this.maxDistance = i;
    }

    public void setCanPullDown(boolean z) {
        this.canPullDown = z;
        if (z || getMoveY() <= 0.0f) {
            return;
        }
        moveTo(0.0f);
    }

    public void setCanPullUp(boolean z) {
        this.canPullUp = z;
        if (z || getMoveY() >= 0.0f) {
            return;
        }
        moveTo(0.0f);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        float x;
        float y;
        float abs;
        int i;
        View view = this.mPullView;
        if (view != null && !ViewCompat.isNestedScrollingEnabled(view)) {
            float moveY = getMoveY();
            int pointerCount = motionEvent.getPointerCount();
            int actionIndex = motionEvent.getActionIndex();
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
            }
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                this.mPointerId = motionEvent.getPointerId(actionIndex);
                float x2 = motionEvent.getX(actionIndex);
                float y2 = motionEvent.getY(actionIndex);
                this.downY = y2;
                this.tepmY = y2;
                this.downX = x2;
                this.tepmX = x2;
                this.tempStateType = 1;
                if (moveY != 0.0f) {
                    return true;
                }
            } else {
                if (actionMasked != 1) {
                    if (actionMasked == 2) {
                        int findPointerIndex = motionEvent.findPointerIndex(this.mPointerId);
                        if (pointerCount > findPointerIndex && findPointerIndex >= 0) {
                            x = motionEvent.getX(findPointerIndex);
                            y = motionEvent.getY(findPointerIndex);
                        } else {
                            x = motionEvent.getX();
                            y = motionEvent.getY();
                        }
                        int i2 = (int) (x - this.tepmX);
                        int i3 = (int) (y - this.tepmY);
                        this.tepmX = x;
                        this.tepmY = y;
                        if (this.isScrolling || Math.abs(i3) > Math.abs(i2)) {
                            this.isScrolling = true;
                            if (moveY == 0.0f) {
                                if ((i3 < 0 && canPullUp()) || (i3 > 0 && canPullDown())) {
                                    moveBy(i3);
                                    return true;
                                }
                            } else {
                                motionEvent.setAction(3);
                                if ((moveY < 0.0f && i3 + moveY >= 0.0f) || (moveY > 0.0f && i3 + moveY <= 0.0f)) {
                                    motionEvent.setAction(0);
                                    moveTo(0.0f);
                                } else if ((moveY > 0.0f && i3 > 0) || (moveY < 0.0f && i3 < 0)) {
                                    if (this.maxDistance != 0) {
                                        float abs2 = Math.abs(moveY);
                                        int i4 = this.maxDistance;
                                        if (abs2 >= i4) {
                                            if (moveY > i4) {
                                                moveTo(i4);
                                            } else if (moveY < (-i4)) {
                                                moveTo(-i4);
                                            }
                                        }
                                    }
                                    int i5 = i3 / 2;
                                    if (this.maxDistance == 0) {
                                        abs = (-i5) * Math.abs(moveY);
                                        i = this.MAXDISTANCE;
                                    } else {
                                        abs = (-i5) * Math.abs(moveY);
                                        i = this.maxDistance;
                                    }
                                    moveBy((((int) (abs / i)) - i5) + i3);
                                } else {
                                    moveBy(i3);
                                }
                            }
                        } else {
                            motionEvent.setLocation(x, this.downY);
                        }
                    } else if (actionMasked != 3) {
                        if (actionMasked == 5) {
                            this.mPointerId = motionEvent.getPointerId(actionIndex);
                            this.tepmX = motionEvent.getX(actionIndex);
                            this.tepmY = motionEvent.getY(actionIndex);
                        } else if (actionMasked == 6) {
                            if (this.mPointerId == motionEvent.getPointerId(actionIndex)) {
                                int i6 = actionIndex == 0 ? 1 : 0;
                                this.mPointerId = motionEvent.getPointerId(i6);
                                this.tepmY = motionEvent.getY(i6);
                            }
                        }
                    }
                }
                startFling();
                this.isScrolling = false;
            }
            return super.dispatchTouchEvent(motionEvent) || this.isScrolling;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        View view = this.mPullView;
        if (view == null || ViewCompat.isNestedScrollingEnabled(view) || motionEvent.getAction() != 0) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public boolean onStartNestedScroll(View view, View view2, int i) {
        if (!isNestedScrollingEnabled()) {
            setNestedScrollingEnabled(true);
        }
        return (i & 2) != 0;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public void onNestedScrollAccepted(View view, View view2, int i) {
        this.mParentHelper.onNestedScrollAccepted(view, view2, i);
        startNestedScroll(2);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public void onStopNestedScroll(View view) {
        startFling();
        stopNestedScroll();
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public void onNestedScroll(View view, int i, int i2, int i3, int i4) {
        dispatchNestedScroll(0, i2, 0, i4, new int[2]);
        moveBy((-i4) - i2);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public void onNestedPreScroll(View view, int i, int i2, int[] iArr) {
        float abs;
        int i3;
        this.tempStateType = 1;
        float moveY = getMoveY();
        if (this.mPullView == null || moveY == 0.0f) {
            dispatchNestedPreScroll(0, i2, iArr, null);
            return;
        }
        iArr[0] = 0;
        stopNestedScroll();
        int i4 = -i2;
        if ((moveY < 0.0f && i4 + moveY >= 0.0f) || (moveY > 0.0f && i4 + moveY <= 0.0f)) {
            moveTo(0.0f);
            startNestedScroll(2);
            iArr[1] = (int) (moveY - 0.0f);
            int[] iArr2 = new int[2];
            dispatchNestedPreScroll(0, i2 - iArr[1], iArr2, null);
            iArr[1] = iArr[1] + iArr2[1];
            return;
        }
        if ((moveY > 0.0f && i4 > 0) || (moveY < 0.0f && i4 < 0)) {
            if (this.maxDistance != 0) {
                float abs2 = Math.abs(moveY);
                int i5 = this.maxDistance;
                if (abs2 >= i5) {
                    if (moveY > i5) {
                        moveTo(i5);
                    } else if (moveY < (-i5)) {
                        moveTo(-i5);
                    }
                    iArr[1] = i2;
                    return;
                }
            }
            int i6 = i4 / 2;
            if (this.maxDistance == 0) {
                abs = (-i6) * Math.abs(moveY);
                i3 = this.MAXDISTANCE;
            } else {
                abs = (-i6) * Math.abs(moveY);
                i3 = this.maxDistance;
            }
            moveBy((((int) (abs / i3)) - i6) + i4);
            iArr[1] = i2;
            return;
        }
        moveBy(i4);
        iArr[1] = i2;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public boolean onNestedFling(View view, float f, float f2, boolean z) {
        return dispatchNestedFling(f, f2, z);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public boolean onNestedPreFling(View view, float f, float f2) {
        if (dispatchNestedPreFling(f, f2)) {
            return true;
        }
        Pullable pullAble = CanPullUtil.getPullAble(view);
        if (pullAble == null) {
            return false;
        }
        if (!pullAble.isGetBottom() || f2 >= 0.0f) {
            return pullAble.isGetTop() && f2 > 0.0f;
        }
        return true;
    }

    @Override // android.view.ViewGroup, androidx.core.view.NestedScrollingParent
    public int getNestedScrollAxes() {
        return this.mParentHelper.getNestedScrollAxes();
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public void setNestedScrollingEnabled(boolean z) {
        this.mChildHelper.setNestedScrollingEnabled(z);
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean isNestedScrollingEnabled() {
        return this.mChildHelper.isNestedScrollingEnabled();
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean startNestedScroll(int i) {
        return this.mChildHelper.startNestedScroll(i);
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public void stopNestedScroll() {
        this.mChildHelper.stopNestedScroll();
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean dispatchNestedScroll(int i, int i2, int i3, int i4, int[] iArr) {
        return this.mChildHelper.dispatchNestedScroll(i, i2, i3, i4, iArr);
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2) {
        return this.mChildHelper.dispatchNestedPreScroll(i, i2, iArr, iArr2);
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean dispatchNestedFling(float f, float f2, boolean z) {
        return this.mChildHelper.dispatchNestedFling(f, f2, z);
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public boolean dispatchNestedPreFling(float f, float f2) {
        return this.mChildHelper.dispatchNestedPreFling(f, f2);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mChildHelper.onDetachedFromWindow();
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }
}
