package com.serenegiant.graphics;

import android.graphics.Path;

/* loaded from: classes.dex */
public class TriangleShape extends PathShape {
    public TriangleShape() {
        this(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    public TriangleShape(float[] fArr) {
        this(fArr[0], fArr[1], fArr[2], fArr[3], fArr[4], fArr[5]);
    }

    public TriangleShape(float f, float f2, float f3, float f4, float f5, float f6) {
        super(null, delta(f, f3, f5), delta(f2, f4, f6));
        float min = min(f, f3, f5);
        float min2 = min(f2, f4, f6);
        Path path = new Path();
        path.moveTo(f - min, f2 - min2);
        path.lineTo(f3 - min, f4 - min2);
        path.lineTo(f5 - min, f6 - min2);
        path.close();
        setPath(path);
    }

    private static final float min(float f, float f2, float f3) {
        return Math.min(Math.min(f, f2), f3);
    }

    private static final float max(float f, float f2, float f3) {
        return Math.max(Math.max(f, f2), f3);
    }

    private static final float delta(float f, float f2, float f3) {
        return max(f, f2, f3) - min(f, f2, f3);
    }
}
