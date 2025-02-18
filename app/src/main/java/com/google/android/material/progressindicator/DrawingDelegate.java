package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;

/* loaded from: classes.dex */
interface DrawingDelegate {
    void adjustCanvas(Canvas canvas, ProgressIndicatorSpec progressIndicatorSpec, float f);

    void fillTrackWithColor(Canvas canvas, Paint paint, int i, float f, float f2, float f3, float f4);

    int getPreferredHeight(ProgressIndicatorSpec progressIndicatorSpec);

    int getPreferredWidth(ProgressIndicatorSpec progressIndicatorSpec);
}
