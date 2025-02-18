package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/* loaded from: classes.dex */
final class LinearDrawingDelegate implements DrawingDelegate {
    private float trackLength = 300.0f;

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public int getPreferredWidth(ProgressIndicatorSpec progressIndicatorSpec) {
        return -1;
    }

    LinearDrawingDelegate() {
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public int getPreferredHeight(ProgressIndicatorSpec progressIndicatorSpec) {
        return progressIndicatorSpec.indicatorSize;
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public void adjustCanvas(Canvas canvas, ProgressIndicatorSpec progressIndicatorSpec, float f) {
        Rect clipBounds = canvas.getClipBounds();
        this.trackLength = clipBounds.width();
        float f2 = progressIndicatorSpec.indicatorSize;
        canvas.translate(clipBounds.width() / 2.0f, (clipBounds.height() / 2.0f) + Math.max(0.0f, (clipBounds.height() - progressIndicatorSpec.indicatorSize) / 2.0f));
        if (progressIndicatorSpec.inverse) {
            canvas.scale(-1.0f, 1.0f);
        }
        if (progressIndicatorSpec.growMode == 2) {
            canvas.scale(1.0f, -1.0f);
        }
        if (progressIndicatorSpec.growMode == 1 || progressIndicatorSpec.growMode == 2) {
            canvas.translate(0.0f, (progressIndicatorSpec.indicatorSize * (f - 1.0f)) / 2.0f);
        }
        float f3 = this.trackLength;
        canvas.clipRect((-f3) / 2.0f, (-f2) / 2.0f, f3 / 2.0f, f2 / 2.0f);
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public void fillTrackWithColor(Canvas canvas, Paint paint, int i, float f, float f2, float f3, float f4) {
        if (f == f2) {
            return;
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(i);
        float f5 = this.trackLength;
        float f6 = f4 * 2.0f;
        float f7 = (-f3) / 2.0f;
        PointF pointF = new PointF(((-f5) / 2.0f) + f4 + ((f5 - f6) * f), f7 + f4);
        float f8 = this.trackLength;
        float f9 = f3 / 2.0f;
        PointF pointF2 = new PointF(((-f8) / 2.0f) + f4 + ((f8 - f6) * f2), f9 - f4);
        if (f4 > 0.0f) {
            float f10 = -f4;
            RectF rectF = new RectF(f10, f10, f4, f4);
            drawRoundedCorner(canvas, paint, pointF.x, pointF.y, 180.0f, 90.0f, rectF);
            drawRoundedCorner(canvas, paint, pointF.x, pointF2.y, 180.0f, -90.0f, rectF);
            drawRoundedCorner(canvas, paint, pointF2.x, pointF.y, 0.0f, -90.0f, rectF);
            drawRoundedCorner(canvas, paint, pointF2.x, pointF2.y, 0.0f, 90.0f, rectF);
            if (f3 > f6) {
                canvas.drawRect(pointF.x - f4, pointF.y, pointF.x, pointF2.y, paint);
                canvas.drawRect(pointF2.x, pointF.y, pointF2.x + f4, pointF2.y, paint);
            }
        }
        canvas.drawRect(pointF.x, f7, pointF2.x, f9, paint);
    }

    private static void drawRoundedCorner(Canvas canvas, Paint paint, float f, float f2, float f3, float f4, RectF rectF) {
        canvas.save();
        canvas.translate(f, f2);
        canvas.drawArc(rectF, f3, f4, true, paint);
        canvas.restore();
    }
}
