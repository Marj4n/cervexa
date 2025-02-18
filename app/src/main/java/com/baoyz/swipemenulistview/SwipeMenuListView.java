package com.baoyz.swipemenulistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ListAdapter;
import android.widget.ListView;

/* loaded from: classes.dex */
public class SwipeMenuListView extends ListView {
    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = -1;
    private static final int TOUCH_STATE_NONE = 0;
    private static final int TOUCH_STATE_X = 1;
    private static final int TOUCH_STATE_Y = 2;
    private int MAX_X;
    private int MAX_Y;
    private Interpolator mCloseInterpolator;
    private int mDirection;
    private float mDownX;
    private float mDownY;
    private SwipeMenuCreator mMenuCreator;
    private OnMenuItemClickListener mOnMenuItemClickListener;
    private OnMenuStateChangeListener mOnMenuStateChangeListener;
    private OnSwipeListener mOnSwipeListener;
    private Interpolator mOpenInterpolator;
    private int mTouchPosition;
    private int mTouchState;
    private SwipeMenuLayout mTouchView;

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(int i, SwipeMenu swipeMenu, int i2);
    }

    public interface OnMenuStateChangeListener {
        void onMenuClose(int i);

        void onMenuOpen(int i);
    }

    public interface OnSwipeListener {
        void onSwipeEnd(int i);

        void onSwipeStart(int i);
    }

    public SwipeMenuListView(Context context) {
        super(context);
        this.mDirection = 1;
        this.MAX_Y = 5;
        this.MAX_X = 3;
        init();
    }

    public SwipeMenuListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDirection = 1;
        this.MAX_Y = 5;
        this.MAX_X = 3;
        init();
    }

    public SwipeMenuListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDirection = 1;
        this.MAX_Y = 5;
        this.MAX_X = 3;
        init();
    }

    private void init() {
        this.MAX_X = dp2px(this.MAX_X);
        this.MAX_Y = dp2px(this.MAX_Y);
        this.mTouchState = 0;
    }

    @Override // android.widget.AdapterView
    public void setAdapter(ListAdapter listAdapter) {
        super.setAdapter((ListAdapter) new SwipeMenuAdapter(getContext(), listAdapter) { // from class: com.baoyz.swipemenulistview.SwipeMenuListView.1
            @Override // com.baoyz.swipemenulistview.SwipeMenuAdapter
            public void createMenu(SwipeMenu swipeMenu) {
                if (SwipeMenuListView.this.mMenuCreator != null) {
                    SwipeMenuListView.this.mMenuCreator.create(swipeMenu);
                }
            }

            @Override // com.baoyz.swipemenulistview.SwipeMenuAdapter, com.baoyz.swipemenulistview.SwipeMenuView.OnSwipeItemClickListener
            public void onItemClick(SwipeMenuView swipeMenuView, SwipeMenu swipeMenu, int i) {
                boolean onMenuItemClick = SwipeMenuListView.this.mOnMenuItemClickListener != null ? SwipeMenuListView.this.mOnMenuItemClickListener.onMenuItemClick(swipeMenuView.getPosition(), swipeMenu, i) : false;
                if (SwipeMenuListView.this.mTouchView == null || onMenuItemClick) {
                    return;
                }
                SwipeMenuListView.this.mTouchView.smoothCloseMenu();
            }
        });
    }

    public void setCloseInterpolator(Interpolator interpolator) {
        this.mCloseInterpolator = interpolator;
    }

    public void setOpenInterpolator(Interpolator interpolator) {
        this.mOpenInterpolator = interpolator;
    }

    public Interpolator getOpenInterpolator() {
        return this.mOpenInterpolator;
    }

    public Interpolator getCloseInterpolator() {
        return this.mCloseInterpolator;
    }

    @Override // android.widget.AbsListView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.widget.AbsListView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        SwipeMenuLayout swipeMenuLayout;
        OnMenuStateChangeListener onMenuStateChangeListener;
        if (motionEvent.getAction() != 0 && this.mTouchView == null) {
            return super.onTouchEvent(motionEvent);
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            int i = this.mTouchPosition;
            this.mDownX = motionEvent.getX();
            this.mDownY = motionEvent.getY();
            this.mTouchState = 0;
            int pointToPosition = pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());
            this.mTouchPosition = pointToPosition;
            if (pointToPosition == i && (swipeMenuLayout = this.mTouchView) != null && swipeMenuLayout.isOpen()) {
                this.mTouchState = 1;
                this.mTouchView.onSwipe(motionEvent);
                return true;
            }
            View childAt = getChildAt(this.mTouchPosition - getFirstVisiblePosition());
            SwipeMenuLayout swipeMenuLayout2 = this.mTouchView;
            if (swipeMenuLayout2 != null && swipeMenuLayout2.isOpen()) {
                this.mTouchView.smoothCloseMenu();
                this.mTouchView = null;
                MotionEvent obtain = MotionEvent.obtain(motionEvent);
                obtain.setAction(3);
                onTouchEvent(obtain);
                OnMenuStateChangeListener onMenuStateChangeListener2 = this.mOnMenuStateChangeListener;
                if (onMenuStateChangeListener2 != null) {
                    onMenuStateChangeListener2.onMenuClose(i);
                }
                return true;
            }
            if (childAt instanceof SwipeMenuLayout) {
                SwipeMenuLayout swipeMenuLayout3 = (SwipeMenuLayout) childAt;
                this.mTouchView = swipeMenuLayout3;
                swipeMenuLayout3.setSwipeDirection(this.mDirection);
            }
            SwipeMenuLayout swipeMenuLayout4 = this.mTouchView;
            if (swipeMenuLayout4 != null) {
                swipeMenuLayout4.onSwipe(motionEvent);
            }
        } else if (action != 1) {
            if (action == 2) {
                float abs = Math.abs(motionEvent.getY() - this.mDownY);
                float abs2 = Math.abs(motionEvent.getX() - this.mDownX);
                int i2 = this.mTouchState;
                if (i2 == 1) {
                    SwipeMenuLayout swipeMenuLayout5 = this.mTouchView;
                    if (swipeMenuLayout5 != null) {
                        swipeMenuLayout5.onSwipe(motionEvent);
                    }
                    getSelector().setState(new int[]{0});
                    motionEvent.setAction(3);
                    super.onTouchEvent(motionEvent);
                    return true;
                }
                if (i2 == 0) {
                    if (Math.abs(abs) > this.MAX_Y) {
                        this.mTouchState = 2;
                    } else if (abs2 > this.MAX_X) {
                        this.mTouchState = 1;
                        OnSwipeListener onSwipeListener = this.mOnSwipeListener;
                        if (onSwipeListener != null) {
                            onSwipeListener.onSwipeStart(this.mTouchPosition);
                        }
                    }
                }
            }
        } else if (this.mTouchState == 1) {
            SwipeMenuLayout swipeMenuLayout6 = this.mTouchView;
            if (swipeMenuLayout6 != null) {
                boolean isOpen = swipeMenuLayout6.isOpen();
                this.mTouchView.onSwipe(motionEvent);
                boolean isOpen2 = this.mTouchView.isOpen();
                if (isOpen != isOpen2 && (onMenuStateChangeListener = this.mOnMenuStateChangeListener) != null) {
                    if (isOpen2) {
                        onMenuStateChangeListener.onMenuOpen(this.mTouchPosition);
                    } else {
                        onMenuStateChangeListener.onMenuClose(this.mTouchPosition);
                    }
                }
                if (!isOpen2) {
                    this.mTouchPosition = -1;
                    this.mTouchView = null;
                }
            }
            OnSwipeListener onSwipeListener2 = this.mOnSwipeListener;
            if (onSwipeListener2 != null) {
                onSwipeListener2.onSwipeEnd(this.mTouchPosition);
            }
            motionEvent.setAction(3);
            super.onTouchEvent(motionEvent);
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void smoothOpenMenu(int i) {
        if (i < getFirstVisiblePosition() || i > getLastVisiblePosition()) {
            return;
        }
        View childAt = getChildAt(i - getFirstVisiblePosition());
        if (childAt instanceof SwipeMenuLayout) {
            this.mTouchPosition = i;
            SwipeMenuLayout swipeMenuLayout = this.mTouchView;
            if (swipeMenuLayout != null && swipeMenuLayout.isOpen()) {
                this.mTouchView.smoothCloseMenu();
            }
            SwipeMenuLayout swipeMenuLayout2 = (SwipeMenuLayout) childAt;
            this.mTouchView = swipeMenuLayout2;
            swipeMenuLayout2.setSwipeDirection(this.mDirection);
            this.mTouchView.smoothOpenMenu();
        }
    }

    public void smoothCloseMenu() {
        SwipeMenuLayout swipeMenuLayout = this.mTouchView;
        if (swipeMenuLayout == null || !swipeMenuLayout.isOpen()) {
            return;
        }
        this.mTouchView.smoothCloseMenu();
    }

    private int dp2px(int i) {
        return (int) TypedValue.applyDimension(1, i, getContext().getResources().getDisplayMetrics());
    }

    public void setMenuCreator(SwipeMenuCreator swipeMenuCreator) {
        this.mMenuCreator = swipeMenuCreator;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.mOnSwipeListener = onSwipeListener;
    }

    public void setOnMenuStateChangeListener(OnMenuStateChangeListener onMenuStateChangeListener) {
        this.mOnMenuStateChangeListener = onMenuStateChangeListener;
    }

    public void setSwipeDirection(int i) {
        this.mDirection = i;
    }
}
