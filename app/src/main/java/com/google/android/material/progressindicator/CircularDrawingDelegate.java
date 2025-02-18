package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/* loaded from: classes.dex */
final class CircularDrawingDelegate implements DrawingDelegate {
    private RectF arcBound = new RectF();
    private int arcInverseFactor = 1;

    CircularDrawingDelegate() {
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public int getPreferredWidth(ProgressIndicatorSpec progressIndicatorSpec) {
        return getSize(progressIndicatorSpec);
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public int getPreferredHeight(ProgressIndicatorSpec progressIndicatorSpec) {
        return getSize(progressIndicatorSpec);
    }

    private static int getSize(ProgressIndicatorSpec progressIndicatorSpec) {
        return (progressIndicatorSpec.circularRadius * 2) + progressIndicatorSpec.indicatorSize + (progressIndicatorSpec.circularInset * 2);
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public void adjustCanvas(Canvas canvas, ProgressIndicatorSpec progressIndicatorSpec, float f) {
        int i = progressIndicatorSpec.circularRadius + (progressIndicatorSpec.indicatorSize / 2) + progressIndicatorSpec.circularInset;
        float f2 = i;
        canvas.translate(f2, f2);
        canvas.rotate(-90.0f);
        int i2 = -i;
        canvas.clipRect(i2, i2, i, i);
        float f3 = progressIndicatorSpec.circularRadius;
        if (progressIndicatorSpec.growMode == 1) {
            f3 += ((1.0f - f) * progressIndicatorSpec.indicatorSize) / 2.0f;
        } else if (progressIndicatorSpec.growMode == 2) {
            f3 -= ((1.0f - f) * progressIndicatorSpec.indicatorSize) / 2.0f;
        }
        float f4 = -f3;
        this.arcBound = new RectF(f4, f4, f3, f3);
        this.arcInverseFactor = progressIndicatorSpec.inverse ? -1 : 1;
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public void fillTrackWithColor(Canvas canvas, Paint paint, int i, float f, float f2, float f3, float f4) {
        if (f == f2) {
            return;
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setAntiAlias(true);
        paint.setColor(i);
        paint.setStrokeWidth(f3);
        int i2 = this.arcInverseFactor;
        float f5 = f * 360.0f * i2;
        float f6 = (f2 >= f ? f2 - f : (f2 + 1.0f) - f) * 360.0f * i2;
        canvas.drawArc(this.arcBound, f5, f6, false, paint);
        if (f4 <= 0.0f || Math.abs(f6) >= 360.0f) {
            return;
        }
        paint.setStyle(Paint.Style.FILL);
        float f7 = -f4;
        RectF rectF = new RectF(f7, f7, f4, f4);
        drawRoundedEnd(canvas, paint, f3, f4, f5, true, rectF);
        drawRoundedEnd(canvas, paint, f3, f4, f5 + f6, false, rectF);
    }

    private void drawRoundedEnd(Canvas canvas, Paint paint, float f, float f2, float f3, boolean z, RectF rectF) {
        float f4 = z ? -1.0f : 1.0f;
        canvas.save();
        canvas.rotate(f3);
        float f5 = f / 2.0f;
        float f6 = f4 * f2;
        canvas.drawRect((this.arcBound.right - f5) + f2, Math.min(0.0f, this.arcInverseFactor * f6), (this.arcBound.right + f5) - f2, Math.max(0.0f, f6 * this.arcInverseFactor), paint);
        canvas.translate((this.arcBound.right - f5) + f2, 0.0f);
        canvas.drawArc(rectF, 180.0f, (-f4) * 90.0f * this.arcInverseFactor, true, paint);
        canvas.translate(f - (f2 * 2.0f), 0.0f);
        canvas.drawArc(rectF, 0.0f, f4 * 90.0f * this.arcInverseFactor, true, paint);
        canvas.restore();
    }
}
