package com.serenegiant.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/* loaded from: classes.dex */
public class ShaderDrawable extends Drawable {
    private final DrawFilter mDrawFilter;
    private final Paint mPaint;
    private Shader mShader;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    public ShaderDrawable() {
        this(0, 0);
    }

    public ShaderDrawable(int i) {
        this(i, 0);
    }

    public ShaderDrawable(int i, int i2) {
        this.mPaint = new Paint();
        this.mDrawFilter = new PaintFlagsDrawFilter(i, i2);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (this.mShader != null) {
            int save = canvas.save();
            DrawFilter drawFilter = canvas.getDrawFilter();
            canvas.setDrawFilter(this.mDrawFilter);
            this.mPaint.setShader(this.mShader);
            canvas.drawPaint(this.mPaint);
            canvas.setDrawFilter(drawFilter);
            canvas.restoreToCount(save);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public ColorFilter getColorFilter() {
        return this.mPaint.getColorFilter();
    }

    public void setBounds(RectF rectF) {
        super.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
    }

    public void setBounds(float f, float f2, float f3, float f4) {
        super.setBounds((int) f, (int) f2, (int) f3, (int) f4);
    }

    public Shader setShader(Shader shader) {
        if (this.mShader != shader) {
            this.mShader = shader;
        }
        return shader;
    }

    public Shader getShader() {
        return this.mShader;
    }
}
