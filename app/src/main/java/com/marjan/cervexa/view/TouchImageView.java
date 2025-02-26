package com.marjan.cervexa.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

/* loaded from: classes.dex */
public class TouchImageView extends AppCompatImageView {
    static final int CLICK = 3;
    static final int DRAG = 1;
    static final int NONE = 0;
    static final int ZOOM = 2;
    final int DOUBLE_CLICK_TIMEOUT;
    private boolean bAllowSlidePage;
    private boolean bCenterFlag;
    private boolean bDoubleClickFlag;
    private boolean bPointerDownFlag;
    Context context;
    protected float fOrigHeight;
    protected float fOrigWidth;
    PointF last;
    private float[] m;
    private MotionEvent mCurrentDownEvent;
    private MotionEvent mPreviousUpEvent;
    ScaleGestureDetector mScaleDetector;
    Matrix matrix;
    private float maxScale;
    private float minScale;
    int mode;
    private int oldMeasuredHeight;
    private int oldMeasuredWidth;
    private float saveScale;
    PointF start;
    private int viewHeight;
    private int viewWidth;

    float getFixDragTrans(float f, float f2, float f3) {
        if (f3 <= f2) {
            return 0.0f;
        }
        return f;
    }

    float getFixTrans(float f, float f2, float f3) {
        float f4;
        float f5;
        if (f3 <= f2) {
            f5 = f2 - f3;
            f4 = 0.0f;
        } else {
            f4 = f2 - f3;
            f5 = 0.0f;
        }
        if (f < f4) {
            return (-f) + f4;
        }
        if (f > f5) {
            return (-f) + f5;
        }
        return 0.0f;
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return super.dispatchTouchEvent(motionEvent);
    }

    public TouchImageView(Context context) {
        super(context);
        this.mode = 0;
        this.last = new PointF();
        this.start = new PointF();
        this.minScale = 1.0f;
        this.maxScale = 3.0f;
        this.saveScale = 1.0f;
        this.bAllowSlidePage = true;
        this.bCenterFlag = false;
        this.bPointerDownFlag = false;
        this.bDoubleClickFlag = false;
        this.DOUBLE_CLICK_TIMEOUT = 200;
        sharedConstructing(context);
    }

    public TouchImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mode = 0;
        this.last = new PointF();
        this.start = new PointF();
        this.minScale = 1.0f;
        this.maxScale = 3.0f;
        this.saveScale = 1.0f;
        this.bAllowSlidePage = true;
        this.bCenterFlag = false;
        this.bPointerDownFlag = false;
        this.bDoubleClickFlag = false;
        this.DOUBLE_CLICK_TIMEOUT = 200;
        sharedConstructing(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean IsConsideredDoubleTap(MotionEvent motionEvent, MotionEvent motionEvent2, MotionEvent motionEvent3) {
        if (motionEvent3.getEventTime() - motionEvent2.getEventTime() > 200) {
            return false;
        }
        int x = ((int) motionEvent2.getX()) - ((int) motionEvent3.getX());
        int y = ((int) motionEvent2.getY()) - ((int) motionEvent3.getY());
        return (x * x) + (y * y) < 10000;
    }

    private void sharedConstructing(Context context) {
        super.setClickable(true);
        this.context = context;
        this.mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        Matrix matrix = new Matrix();
        this.matrix = matrix;
        this.m = new float[9];
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);
        setOnTouchListener(new OnTouchListener() { // from class: com.marjan.cervexa.view.TouchImageView.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                TouchImageView.this.mScaleDetector.onTouchEvent(motionEvent);
                PointF pointF = new PointF(motionEvent.getX(), motionEvent.getY());
                int action = motionEvent.getAction();
                if (action == 0) {
                    TouchImageView.this.mCurrentDownEvent = MotionEvent.obtain(motionEvent);
                    if (TouchImageView.this.mPreviousUpEvent != null && TouchImageView.this.mCurrentDownEvent != null && !TouchImageView.this.bPointerDownFlag) {
                        TouchImageView touchImageView = TouchImageView.this;
                        if (touchImageView.IsConsideredDoubleTap(touchImageView.mCurrentDownEvent, TouchImageView.this.mPreviousUpEvent, motionEvent)) {
                            TouchImageView.this.bDoubleClickFlag = true;
                            TouchImageView.this.last.set(pointF);
                            TouchImageView.this.start.set(TouchImageView.this.last);
                            TouchImageView.this.mode = 1;
                        }
                    }
                    TouchImageView.this.bDoubleClickFlag = false;
                    TouchImageView.this.last.set(pointF);
                    TouchImageView.this.start.set(TouchImageView.this.last);
                    TouchImageView.this.mode = 1;
                } else if (action == 1) {
                    TouchImageView.this.mPreviousUpEvent = MotionEvent.obtain(motionEvent);
                    TouchImageView.this.mode = 0;
                    int abs = (int) Math.abs(pointF.x - TouchImageView.this.start.x);
                    int abs2 = (int) Math.abs(pointF.y - TouchImageView.this.start.y);
                    if (abs < 3 && abs2 < 3) {
                        TouchImageView.this.performClick();
                    }
                } else if (action != 2) {
                    if (action == 5) {
                        TouchImageView.this.bPointerDownFlag = true;
                        TouchImageView.this.mode = 0;
                    } else if (action == 6) {
                        TouchImageView.this.bPointerDownFlag = false;
                        TouchImageView.this.mode = 0;
                    }
                } else if (TouchImageView.this.mode == 1) {
                    float f = pointF.x - TouchImageView.this.last.x;
                    float f2 = pointF.y - TouchImageView.this.last.y;
                    Object r1 = null;
                    TouchImageView.this.matrix.postTranslate(TouchImageView.this.getFixDragTrans(f, TouchImageView.this.viewWidth, TouchImageView.this.fOrigWidth * TouchImageView.this.saveScale), TouchImageView.this.getFixDragTrans(f2, TouchImageView.this.viewHeight, TouchImageView.this.fOrigHeight * TouchImageView.this.saveScale));
                    TouchImageView.this.fixTrans();
                    TouchImageView.this.fixTrans();
                    TouchImageView.this.last.set(pointF.x, pointF.y);
                }
                TouchImageView touchImageView2 = TouchImageView.this;
                touchImageView2.DetermineAllowSlidePage(touchImageView2.saveScale);
                TouchImageView touchImageView3 = TouchImageView.this;
                touchImageView3.setImageMatrix(touchImageView3.matrix);
                TouchImageView.this.invalidate();
                return true;
            }
        });
    }

    void DetermineAllowSlidePage(float f) {
        if (f <= 1.0f) {
            this.bAllowSlidePage = true;
        } else if (f > 1.0f) {
            this.bAllowSlidePage = false;
        }
        getParent().requestDisallowInterceptTouchEvent(true ^ this.bAllowSlidePage);
    }

    void fixTrans() {
        this.matrix.getValues(this.m);
        float[] fArr = this.m;
        float f = fArr[2];
        float f2 = fArr[5];
        float fixTrans = getFixTrans(f, this.viewWidth, this.fOrigWidth * this.saveScale);
        float fixTrans2 = getFixTrans(f2, this.viewHeight, this.fOrigHeight * this.saveScale);
        if (fixTrans != 0.0f || fixTrans2 != 0.0f) {
            this.matrix.postTranslate(fixTrans, fixTrans2);
        }
        if (f2 < 0.0f) {
            this.bCenterFlag = true;
        }
        if (this.bDoubleClickFlag) {
            this.saveScale = 1.0f;
            this.bCenterFlag = true;
            this.bDoubleClickFlag = false;
        }
        if (this.saveScale == 1.0f && this.bCenterFlag) {
            this.bCenterFlag = false;
            CenterImage();
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        int i4;
        super.onMeasure(i, i2);
        this.viewWidth = MeasureSpec.getSize(i);
        int size = MeasureSpec.getSize(i2);
        this.viewHeight = size;
        int i5 = this.oldMeasuredHeight;
        if ((i5 == this.viewWidth && i5 == size) || (i3 = this.viewWidth) == 0 || (i4 = this.viewHeight) == 0) {
            return;
        }
        this.oldMeasuredHeight = i4;
        this.oldMeasuredWidth = i3;
        CenterImage();
    }

    void CenterImage() {
        if (this.saveScale == 1.0f) {
            Drawable drawable = getDrawable();
            if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
                return;
            }
            float intrinsicWidth = drawable.getIntrinsicWidth();
            float intrinsicHeight = drawable.getIntrinsicHeight();
            float min = Math.min(this.viewWidth / intrinsicWidth, this.viewHeight / intrinsicHeight);
            this.matrix.setScale(min, min);
            float f = (this.viewHeight - (intrinsicHeight * min)) / 2.0f;
            float f2 = (this.viewWidth - (min * intrinsicWidth)) / 2.0f;
            this.matrix.postTranslate(f2, f);
            this.fOrigWidth = this.viewWidth - (f2 * 2.0f);
            this.fOrigHeight = this.viewHeight - (f * 2.0f);
            setImageMatrix(this.matrix);
        }
        fixTrans();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        @Override
        // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            TouchImageView.this.mode = 2;
            return true;
        }

        @Override
        // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float f;
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            float f2 = TouchImageView.this.saveScale;
            TouchImageView.this.saveScale *= scaleFactor;
            if (TouchImageView.this.saveScale > TouchImageView.this.maxScale) {
                TouchImageView touchImageView = TouchImageView.this;
                touchImageView.saveScale = touchImageView.maxScale;
                f = TouchImageView.this.maxScale;
            } else {
                if (TouchImageView.this.saveScale < TouchImageView.this.minScale) {
                    TouchImageView touchImageView2 = TouchImageView.this;
                    touchImageView2.saveScale = touchImageView2.minScale;
                    f = TouchImageView.this.minScale;
                }
                if (TouchImageView.this.fOrigWidth * TouchImageView.this.saveScale > TouchImageView.this.viewWidth || TouchImageView.this.fOrigHeight * TouchImageView.this.saveScale <= TouchImageView.this.viewHeight) {
                    TouchImageView.this.matrix.postScale(scaleFactor, scaleFactor, TouchImageView.this.viewWidth / 2, TouchImageView.this.viewHeight / 2);
                } else {
                    TouchImageView.this.matrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                }
                TouchImageView.this.fixTrans();
                return true;
            }
            scaleFactor = f / f2;
            if (TouchImageView.this.fOrigWidth * TouchImageView.this.saveScale > TouchImageView.this.viewWidth) {
            }
            TouchImageView.this.matrix.postScale(scaleFactor, scaleFactor, TouchImageView.this.viewWidth / 2, TouchImageView.this.viewHeight / 2);
            TouchImageView.this.fixTrans();
            return true;
        }
    }
}
