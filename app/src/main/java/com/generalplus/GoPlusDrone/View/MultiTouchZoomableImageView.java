package com.generalplus.GoPlusDrone.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/* loaded from: classes.dex */
public class MultiTouchZoomableImageView extends BaseZoomableImageView {
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleDetector;
    private boolean scaleRecognized;
    protected boolean transIgnoreScale;

    public MultiTouchZoomableImageView(Context context) {
        super(context);
        this.transIgnoreScale = false;
        this.scaleRecognized = false;
        initMultiTouchZoomableImageView(context);
    }

    public MultiTouchZoomableImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.transIgnoreScale = false;
        this.scaleRecognized = false;
        initMultiTouchZoomableImageView(context);
    }

    protected void initMultiTouchZoomableImageView(Context context) {
        this.mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        this.mGestureDetector = new GestureDetector(context, new MyGestureListener());
    }

    class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        ScaleListener() {
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            if (scaleGestureDetector == null || !scaleGestureDetector.isInProgress()) {
                return false;
            }
            try {
                MultiTouchZoomableImageView.this.zoomTo(Math.min(MultiTouchZoomableImageView.this.maxZoom(), Math.max(MultiTouchZoomableImageView.this.getScale() * scaleGestureDetector.getScaleFactor(), 1.0f)), scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                MultiTouchZoomableImageView.this.invalidate();
                MultiTouchZoomableImageView.this.scaleRecognized = true;
                return true;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private MyGestureListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            if (MultiTouchZoomableImageView.this.mImageGestureListener != null) {
                MultiTouchZoomableImageView.this.mImageGestureListener.onImageGestureSingleTapConfirmed();
                return false;
            }
            return super.onSingleTapConfirmed(motionEvent);
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            if (MultiTouchZoomableImageView.this.mImageGestureListener == null || MultiTouchZoomableImageView.this.scaleRecognized) {
                return;
            }
            MultiTouchZoomableImageView.this.mImageGestureListener.onImageGestureLongPress();
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (motionEvent != null) {
                try {
                    if (motionEvent.getPointerCount() <= 1) {
                    }
                    return false;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            if ((motionEvent2 == null || motionEvent2.getPointerCount() <= 1) && (MultiTouchZoomableImageView.this.mScaleDetector == null || !MultiTouchZoomableImageView.this.mScaleDetector.isInProgress())) {
                if (!MultiTouchZoomableImageView.this.transIgnoreScale && MultiTouchZoomableImageView.this.getScale() <= MultiTouchZoomableImageView.this.zoomDefault()) {
                    if (MultiTouchZoomableImageView.this.mViewPager != null) {
                        MultiTouchZoomableImageView.this.mViewPager.requestDisallowInterceptTouchEvent(false);
                    }
                    return true;
                }
                MultiTouchZoomableImageView.this.stopFling();
                MultiTouchZoomableImageView.this.postTranslate(-f, -f2);
                if (MultiTouchZoomableImageView.this.isScrollOver(f)) {
                    if (MultiTouchZoomableImageView.this.mViewPager != null) {
                        MultiTouchZoomableImageView.this.mViewPager.requestDisallowInterceptTouchEvent(false);
                    }
                } else if (MultiTouchZoomableImageView.this.mViewPager != null) {
                    MultiTouchZoomableImageView.this.mViewPager.requestDisallowInterceptTouchEvent(true);
                }
                MultiTouchZoomableImageView.this.center(true, true, false);
                return true;
            }
            return false;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent motionEvent) {
            if (MultiTouchZoomableImageView.this.getScale() != MultiTouchZoomableImageView.this.zoomDefault()) {
                MultiTouchZoomableImageView multiTouchZoomableImageView = MultiTouchZoomableImageView.this;
                multiTouchZoomableImageView.zoomTo(multiTouchZoomableImageView.zoomDefault());
                return true;
            }
            MultiTouchZoomableImageView multiTouchZoomableImageView2 = MultiTouchZoomableImageView.this;
            multiTouchZoomableImageView2.zoomTo(multiTouchZoomableImageView2.zoomDefault() * 3.0f, motionEvent.getX(), motionEvent.getY(), 200.0f);
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if ((motionEvent != null && motionEvent.getPointerCount() > 1) || ((motionEvent2 != null && motionEvent2.getPointerCount() > 1) || MultiTouchZoomableImageView.this.mScaleDetector.isInProgress())) {
                return false;
            }
            if (motionEvent.getX() - motionEvent2.getX() > 100.0f && Math.abs(f) > 200.0f) {
                Log.i("MultiTouchZoomableImageView", "Fling Left");
            } else if (motionEvent2.getX() - motionEvent.getX() > 100.0f && Math.abs(f) > 200.0f) {
                Log.i("MultiTouchZoomableImageView", "Fling Right");
            } else if (motionEvent.getY() - motionEvent2.getY() > 100.0f && Math.abs(f2) > 200.0f) {
                Log.i("MultiTouchZoomableImageView", "Fling Up");
            } else if (motionEvent2.getY() - motionEvent.getY() > 100.0f && Math.abs(f2) > 200.0f) {
                Log.i("MultiTouchZoomableImageView", "Fling Down");
                if (!MultiTouchZoomableImageView.this.transIgnoreScale && MultiTouchZoomableImageView.this.getScale() <= MultiTouchZoomableImageView.this.zoomDefault()) {
                    MultiTouchZoomableImageView.this.mImageGestureListener.onImageGestureFlingDown();
                    return true;
                }
            }
            try {
                float x = motionEvent2.getX() - motionEvent.getX();
                float y = motionEvent2.getY() - motionEvent.getY();
                if (Math.abs(f) > 800.0f || Math.abs(f2) > 800.0f) {
                    MultiTouchZoomableImageView.this.scrollBy(x / 2.0f, y / 2.0f, 300.0f);
                    MultiTouchZoomableImageView.this.invalidate();
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NullPointerException unused) {
            }
            return super.onFling(motionEvent, motionEvent2, f, f2);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        try {
            if (this.mViewPager != null) {
                int action = motionEvent.getAction();
                if (action != 1) {
                    if (action == 2) {
                        this.mViewPager.requestDisallowInterceptTouchEvent(true);
                    } else if (action != 3) {
                    }
                }
                this.mViewPager.requestDisallowInterceptTouchEvent(false);
                this.scaleRecognized = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.mBitmap != null) {
            this.mScaleDetector.onTouchEvent(motionEvent);
            if (!this.mScaleDetector.isInProgress()) {
                this.mGestureDetector.onTouchEvent(motionEvent);
            }
            return true;
        }
        this.mImageGestureListener.onImageGestureSingleTapConfirmed();
        return false;
    }
}
