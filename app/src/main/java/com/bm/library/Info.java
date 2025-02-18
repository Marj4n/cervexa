package com.bm.library;

import android.graphics.PointF;
import android.graphics.RectF;
import android.widget.ImageView;

/* loaded from: classes.dex */
public class Info {
    float mDegrees;
    float mScale;
    ImageView.ScaleType mScaleType;
    RectF mRect = new RectF();
    RectF mImgRect = new RectF();
    RectF mWidgetRect = new RectF();
    RectF mBaseRect = new RectF();
    PointF mScreenCenter = new PointF();

    public Info(RectF rectF, RectF rectF2, RectF rectF3, RectF rectF4, PointF pointF, float f, float f2, ImageView.ScaleType scaleType) {
        this.mRect.set(rectF);
        this.mImgRect.set(rectF2);
        this.mWidgetRect.set(rectF3);
        this.mScale = f;
        this.mScaleType = scaleType;
        this.mDegrees = f2;
        this.mBaseRect.set(rectF4);
        this.mScreenCenter.set(pointF);
    }
}
