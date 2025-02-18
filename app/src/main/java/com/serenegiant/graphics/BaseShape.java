package com.serenegiant.graphics;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.shapes.Shape;

/* loaded from: classes.dex */
public class BaseShape extends Shape {
    private static final boolean DEBUG = false;
    protected float mPivotX;
    protected float mPivotY;
    protected float mRotation;
    protected float mScaleX;
    protected float mScaleY;
    protected final float mStdHeight;
    protected final float mStdHeight2;
    protected final float mStdWidth;
    protected final float mStdWidth2;
    protected final String TAG = getClass().getSimpleName();
    protected final RectF mBoundsRect = new RectF();
    final Paint debugPaint = new Paint();

    public BaseShape(float f, float f2) {
        this.mStdWidth = f;
        this.mStdHeight = f2;
        this.mStdWidth2 = f / 2.0f;
        this.mStdHeight2 = f2 / 2.0f;
    }

    @Override // android.graphics.drawable.shapes.Shape
    public void getOutline(Outline outline) {
        RectF boundsRect = boundsRect();
        outline.setRect((int) Math.ceil(boundsRect.left), (int) Math.ceil(boundsRect.top), (int) Math.floor(boundsRect.right), (int) Math.floor(boundsRect.bottom));
    }

    @Override // android.graphics.drawable.shapes.Shape
    protected void onResize(float f, float f2) {
        this.mBoundsRect.set(0.0f, 0.0f, f, f2);
        this.mScaleX = f / this.mStdWidth;
        this.mScaleY = f2 / this.mStdHeight;
        this.mPivotX = f / 2.0f;
        this.mPivotY = f2 / 2.0f;
    }

    protected final RectF boundsRect() {
        return this.mBoundsRect;
    }

    @Override // android.graphics.drawable.shapes.Shape
    public BaseShape clone() throws CloneNotSupportedException {
        BaseShape baseShape = (BaseShape) super.clone();
        baseShape.mBoundsRect.set(this.mBoundsRect);
        return baseShape;
    }

    public float getScaleX() {
        return this.mScaleX;
    }

    public float getScaleY() {
        return this.mScaleY;
    }

    public void setRotation(float f) {
        this.mRotation = f;
    }

    public float getRotation() {
        return this.mRotation;
    }

    @Override // android.graphics.drawable.shapes.Shape
    public void draw(Canvas canvas, Paint paint) {
        int save = canvas.save();
        canvas.translate(this.mPivotX, this.mPivotY);
        canvas.rotate(this.mRotation);
        canvas.scale(this.mScaleX, this.mScaleY);
        canvas.translate(-this.mStdWidth2, -this.mStdHeight2);
        doDraw(canvas, paint);
        canvas.restoreToCount(save);
    }

    protected void doDraw(Canvas canvas, Paint paint) {
        canvas.drawRect(this.mBoundsRect, paint);
    }
}
