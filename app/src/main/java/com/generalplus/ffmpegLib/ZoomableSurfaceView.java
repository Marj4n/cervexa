package com.generalplus.ffmpegLib;

import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/* loaded from: classes.dex */
public class ZoomableSurfaceView extends GLSurfaceView {
    private ScaleGestureDetector SGD;
    int bottom;
    private Context context;
    private float height;
    private boolean isSingleTouch;
    int left;
    private float maxScale;
    private float minScale;
    int right;
    private float scale;
    int top;
    private float width;

    public ZoomableSurfaceView(Context context) {
        super(context);
        this.height = 0.0f;
        this.scale = 1.0f;
        this.minScale = 1.0f;
        this.maxScale = 5.0f;
        this.context = context;
        init();
    }

    public ZoomableSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.height = 0.0f;
        this.scale = 1.0f;
        this.minScale = 1.0f;
        this.maxScale = 5.0f;
        this.context = context;
        init();
    }

    private void init() {
        setOnTouchListener(new MyTouchListeners());
        this.SGD = new ScaleGestureDetector(this.context, new ScaleListener());
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.generalplus.ffmpegLib.ZoomableSurfaceView.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
            }
        });
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.width == 0.0f && this.height == 0.0f) {
            this.width = getWidth();
            this.height = getHeight();
            this.left = i;
            this.right = i3;
            this.top = i2;
            this.bottom = i4;
        }
        Log.e("onLayout", "width:" + this.width + "height" + this.height + ",left:" + i + "top" + i2);
    }

    private class MyTouchListeners implements View.OnTouchListener {
        float dX;
        float dY;

        MyTouchListeners() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            ZoomableSurfaceView.this.SGD.onTouchEvent(motionEvent);
            if (motionEvent.getPointerCount() > 1) {
                ZoomableSurfaceView.this.isSingleTouch = false;
            } else if (motionEvent.getAction() == 1) {
                ZoomableSurfaceView.this.isSingleTouch = true;
            }
            int action = motionEvent.getAction();
            if (action != 0) {
                if (action == 2 && ZoomableSurfaceView.this.isSingleTouch) {
                    ZoomableSurfaceView.this.animate().x(motionEvent.getRawX() + this.dX).y(motionEvent.getRawY() + this.dY).setDuration(0L).start();
                    ZoomableSurfaceView zoomableSurfaceView = ZoomableSurfaceView.this;
                    zoomableSurfaceView.checkDimension(zoomableSurfaceView);
                }
            } else {
                this.dX = ZoomableSurfaceView.this.getX() - motionEvent.getRawX();
                this.dY = ZoomableSurfaceView.this.getY() - motionEvent.getRawY();
            }
            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            Log.e("onGlobalLayout: ", ZoomableSurfaceView.this.scale + " " + ZoomableSurfaceView.this.width + " " + ZoomableSurfaceView.this.height);
            ZoomableSurfaceView zoomableSurfaceView = ZoomableSurfaceView.this;
            zoomableSurfaceView.scale = zoomableSurfaceView.scale * scaleGestureDetector.getScaleFactor();
            ZoomableSurfaceView zoomableSurfaceView2 = ZoomableSurfaceView.this;
            zoomableSurfaceView2.scale = Math.max(zoomableSurfaceView2.minScale, Math.min(ZoomableSurfaceView.this.scale, ZoomableSurfaceView.this.maxScale));
            ZoomableSurfaceView.this.setLayoutParams(new FrameLayout.LayoutParams((int) (ZoomableSurfaceView.this.width * ZoomableSurfaceView.this.scale), (int) (ZoomableSurfaceView.this.height * ZoomableSurfaceView.this.scale)));
            ZoomableSurfaceView zoomableSurfaceView3 = ZoomableSurfaceView.this;
            zoomableSurfaceView3.checkDimension(zoomableSurfaceView3);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkDimension(View view) {
        if (view.getX() > this.left) {
            view.animate().x(this.left).y(view.getY()).setDuration(0L).start();
        }
        if (view.getWidth() + view.getX() < this.right) {
            view.animate().x(this.right - view.getWidth()).y(view.getY()).setDuration(0L).start();
        }
        if (view.getY() > this.top) {
            view.animate().x(view.getX()).y(this.top).setDuration(0L).start();
        }
        if (view.getHeight() + view.getY() < this.bottom) {
            view.animate().x(view.getX()).y(this.bottom - view.getHeight()).setDuration(0L).start();
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        Log.e("onDraw", "onDrawonDrawonDraw1111");
        super.onDraw(canvas);
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onMeasure(int i, int i2) {
        Log.e("onMeasure", "onMeasure1111");
        super.onMeasure(i, i2);
    }
}
