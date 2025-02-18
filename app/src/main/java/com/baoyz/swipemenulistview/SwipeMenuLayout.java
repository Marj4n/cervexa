package com.baoyz.swipemenulistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.widget.ScrollerCompat;
import org.apache.commons.net.ftp.FTPReply;

/* loaded from: classes.dex */
public class SwipeMenuLayout extends FrameLayout {
    private static final int CONTENT_VIEW_ID = 1;
    private static final int MENU_VIEW_ID = 2;
    private static final int STATE_CLOSE = 0;
    private static final int STATE_OPEN = 1;
    private int MAX_VELOCITYX;
    private int MIN_FLING;
    private boolean isFling;
    private int mBaseX;
    private Interpolator mCloseInterpolator;
    private ScrollerCompat mCloseScroller;
    private View mContentView;
    private int mDownX;
    private GestureDetectorCompat mGestureDetector;
    private GestureDetector.OnGestureListener mGestureListener;
    private SwipeMenuView mMenuView;
    private Interpolator mOpenInterpolator;
    private ScrollerCompat mOpenScroller;
    private int mSwipeDirection;
    private int position;
    private int state;

    public SwipeMenuLayout(View view, SwipeMenuView swipeMenuView) {
        this(view, swipeMenuView, null, null);
    }

    public SwipeMenuLayout(View view, SwipeMenuView swipeMenuView, Interpolator interpolator, Interpolator interpolator2) {
        super(view.getContext());
        this.state = 0;
        this.MIN_FLING = dp2px(15);
        this.MAX_VELOCITYX = -dp2px(500);
        this.mCloseInterpolator = interpolator;
        this.mOpenInterpolator = interpolator2;
        this.mContentView = view;
        this.mMenuView = swipeMenuView;
        swipeMenuView.setLayout(this);
        init();
    }

    private SwipeMenuLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.state = 0;
        this.MIN_FLING = dp2px(15);
        this.MAX_VELOCITYX = -dp2px(500);
    }

    private SwipeMenuLayout(Context context) {
        super(context);
        this.state = 0;
        this.MIN_FLING = dp2px(15);
        this.MAX_VELOCITYX = -dp2px(500);
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int i) {
        this.position = i;
        this.mMenuView.setPosition(i);
    }

    public void setSwipeDirection(int i) {
        this.mSwipeDirection = i;
    }

    private void init() {
        setLayoutParams(new AbsListView.LayoutParams(-1, -2));
        this.mGestureListener = new GestureDetector.SimpleOnGestureListener() { // from class: com.baoyz.swipemenulistview.SwipeMenuLayout.1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                SwipeMenuLayout.this.isFling = false;
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (Math.abs(motionEvent.getX() - motionEvent2.getX()) > SwipeMenuLayout.this.MIN_FLING && f < SwipeMenuLayout.this.MAX_VELOCITYX) {
                    SwipeMenuLayout.this.isFling = true;
                }
                return super.onFling(motionEvent, motionEvent2, f, f2);
            }
        };
        this.mGestureDetector = new GestureDetectorCompat(getContext(), this.mGestureListener);
        if (this.mCloseInterpolator != null) {
            this.mCloseScroller = ScrollerCompat.create(getContext(), this.mCloseInterpolator);
        } else {
            this.mCloseScroller = ScrollerCompat.create(getContext());
        }
        if (this.mOpenInterpolator != null) {
            this.mOpenScroller = ScrollerCompat.create(getContext(), this.mOpenInterpolator);
        } else {
            this.mOpenScroller = ScrollerCompat.create(getContext());
        }
        this.mContentView.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        if (this.mContentView.getId() < 1) {
            this.mContentView.setId(1);
        }
        this.mMenuView.setId(2);
        this.mMenuView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        addView(this.mContentView);
        addView(this.mMenuView);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    public boolean onSwipe(MotionEvent motionEvent) {
        this.mGestureDetector.onTouchEvent(motionEvent);
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mDownX = (int) motionEvent.getX();
            this.isFling = false;
        } else if (action != 1) {
            if (action == 2) {
                int x = (int) (this.mDownX - motionEvent.getX());
                if (this.state == 1) {
                    x += this.mMenuView.getWidth() * this.mSwipeDirection;
                }
                swipe(x);
            }
        } else if ((this.isFling || Math.abs(this.mDownX - motionEvent.getX()) > this.mMenuView.getWidth() / 2) && Math.signum(this.mDownX - motionEvent.getX()) == this.mSwipeDirection) {
            smoothOpenMenu();
        } else {
            smoothCloseMenu();
            return false;
        }
        return true;
    }

    public boolean isOpen() {
        return this.state == 1;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    private void swipe(int i) {
        if (Math.signum(i) != this.mSwipeDirection) {
            i = 0;
        } else if (Math.abs(i) > this.mMenuView.getWidth()) {
            i = this.mMenuView.getWidth() * this.mSwipeDirection;
        }
        View view = this.mContentView;
        int i2 = -i;
        view.layout(i2, view.getTop(), this.mContentView.getWidth() - i, getMeasuredHeight());
        if (this.mSwipeDirection == 1) {
            this.mMenuView.layout(this.mContentView.getWidth() - i, this.mMenuView.getTop(), (this.mContentView.getWidth() + this.mMenuView.getWidth()) - i, this.mMenuView.getBottom());
        } else {
            SwipeMenuView swipeMenuView = this.mMenuView;
            swipeMenuView.layout((-swipeMenuView.getWidth()) - i, this.mMenuView.getTop(), i2, this.mMenuView.getBottom());
        }
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.state == 1) {
            if (this.mOpenScroller.computeScrollOffset()) {
                swipe(this.mOpenScroller.getCurrX() * this.mSwipeDirection);
                postInvalidate();
                return;
            }
            return;
        }
        if (this.mCloseScroller.computeScrollOffset()) {
            swipe((this.mBaseX - this.mCloseScroller.getCurrX()) * this.mSwipeDirection);
            postInvalidate();
        }
    }

    public void smoothCloseMenu() {
        this.state = 0;
        if (this.mSwipeDirection == 1) {
            this.mBaseX = -this.mContentView.getLeft();
            this.mCloseScroller.startScroll(0, 0, this.mMenuView.getWidth(), 0, FTPReply.FILE_ACTION_PENDING);
        } else {
            this.mBaseX = this.mMenuView.getRight();
            this.mCloseScroller.startScroll(0, 0, this.mMenuView.getWidth(), 0, FTPReply.FILE_ACTION_PENDING);
        }
        postInvalidate();
    }

    public void smoothOpenMenu() {
        this.state = 1;
        if (this.mSwipeDirection == 1) {
            this.mOpenScroller.startScroll(-this.mContentView.getLeft(), 0, this.mMenuView.getWidth(), 0, FTPReply.FILE_ACTION_PENDING);
        } else {
            this.mOpenScroller.startScroll(this.mContentView.getLeft(), 0, this.mMenuView.getWidth(), 0, FTPReply.FILE_ACTION_PENDING);
        }
        postInvalidate();
    }

    public void closeMenu() {
        if (this.mCloseScroller.computeScrollOffset()) {
            this.mCloseScroller.abortAnimation();
        }
        if (this.state == 1) {
            this.state = 0;
            swipe(0);
        }
    }

    public void openMenu() {
        if (this.state == 0) {
            this.state = 1;
            swipe(this.mMenuView.getWidth() * this.mSwipeDirection);
        }
    }

    public View getContentView() {
        return this.mContentView;
    }

    public SwipeMenuView getMenuView() {
        return this.mMenuView;
    }

    private int dp2px(int i) {
        return (int) TypedValue.applyDimension(1, i, getContext().getResources().getDisplayMetrics());
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mMenuView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), BasicMeasure.EXACTLY));
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mContentView.layout(0, 0, getMeasuredWidth(), this.mContentView.getMeasuredHeight());
        if (this.mSwipeDirection == 1) {
            this.mMenuView.layout(getMeasuredWidth(), 0, getMeasuredWidth() + this.mMenuView.getMeasuredWidth(), this.mContentView.getMeasuredHeight());
        } else {
            SwipeMenuView swipeMenuView = this.mMenuView;
            swipeMenuView.layout(-swipeMenuView.getMeasuredWidth(), 0, 0, this.mContentView.getMeasuredHeight());
        }
    }

    public void setMenuHeight(int i) {
        Log.i("byz", "pos = " + this.position + ", height = " + i);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mMenuView.getLayoutParams();
        if (layoutParams.height != i) {
            layoutParams.height = i;
            SwipeMenuView swipeMenuView = this.mMenuView;
            swipeMenuView.setLayoutParams(swipeMenuView.getLayoutParams());
        }
    }
}
