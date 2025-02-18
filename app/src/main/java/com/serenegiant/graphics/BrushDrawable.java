package com.serenegiant.graphics;

import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;

/* loaded from: classes.dex */
public class BrushDrawable extends ShapeDrawable {
    public static final int BRUSH_ELIPSE = 1;
    public static final int BRUSH_LINE = 2;
    public static final int BRUSH_RECTANGLE = 4;
    public static final int BRUSH_TRIANGLE = 3;
    private static final boolean DEBUG = false;
    public static final int ERASE_ELIPSE = -1;
    public static final int ERASE_LINE = -2;
    public static final int ERASE_RECTANGLE = -4;
    public static final int ERASE_TRIANGLE = -3;
    private static final String TAG = "BrushDrawable";
    private final Xfermode mClearXfermode;
    private final DrawFilter mDrawFilter;
    private final Paint mPaint;
    private final PointF mPivot;
    private float mRotation;
    private Shader mShader;

    public BrushDrawable(int i, int i2, int i3) {
        this(i, i2, i3, 0, 0);
    }

    public BrushDrawable(int i, int i2, int i3, int i4, int i5) {
        this.mPivot = new PointF();
        this.mRotation = 0.0f;
        this.mPaint = new Paint();
        this.mDrawFilter = new PaintFlagsDrawFilter(i4, i5);
        this.mClearXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        init(100, 100);
        setType(i, i2, i3);
    }

    private final void init(int i, int i2) {
        if (i <= 0) {
            i = 100;
        }
        setIntrinsicWidth(i);
        if (i2 <= 0) {
            i2 = 100;
        }
        setIntrinsicHeight(i2);
        this.mPivot.set(getIntrinsicWidth() / 2, getIntrinsicHeight() / 2);
    }

    @Override // android.graphics.drawable.ShapeDrawable
    protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
        canvas.rotate(this.mRotation, this.mPivot.x, this.mPivot.y);
        int save = canvas.save();
        try {
            this.mPaint.setShader(this.mShader);
            super.onDraw(shape, canvas, paint);
        } finally {
            canvas.restoreToCount(save);
        }
    }

    public void setPivot(float f, float f2) {
        if (this.mPivot.x == f && this.mPivot.y == f2) {
            return;
        }
        this.mPivot.set(f, f2);
        invalidateSelf();
    }

    public PointF getPivot() {
        return this.mPivot;
    }

    public float getPivotX() {
        return this.mPivot.x;
    }

    public float getPivotY() {
        return this.mPivot.y;
    }

    public float getRotation() {
        Shape shape = getShape();
        return shape instanceof BaseShape ? ((BaseShape) shape).getRotation() : this.mRotation;
    }

    public void setRotation(float f) {
        Shape shape = getShape();
        if (shape instanceof BaseShape) {
            ((BaseShape) shape).setRotation(f);
            this.mRotation = 0.0f;
        } else if (this.mRotation != f) {
            this.mRotation = f;
        }
        invalidateSelf();
    }

    public void setType(int i, int i2, int i3) {
        Shape ovalShape;
        if (i == 1) {
            ovalShape = new OvalShape();
        } else if (i == 3) {
            ovalShape = new IsoscelesTriangleShape(i2, i3);
        } else {
            ovalShape = i != 4 ? null : new BaseShape(i2, i3);
        }
        if (ovalShape != null) {
            this.mRotation = 0.0f;
            ovalShape.resize(i2, i3);
            setShape(ovalShape);
        }
    }

    public void setColor(int i) {
        getPaint().setColor(i);
        invalidateSelf();
    }

    public void setPaintAlpha(int i) {
        getPaint().setAlpha(i);
    }

    public int getPaintAlpha() {
        return getPaint().getAlpha();
    }

    public Shader getShader() {
        return getPaint().getShader();
    }

    public void setShader(Shader shader) {
        getPaint().setShader(shader);
        invalidateSelf();
    }

    public void setPaintStyle(Paint.Style style) {
        getPaint().setStyle(style);
        invalidateSelf();
    }
}
