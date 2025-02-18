package com.serenegiant.math;

/* loaded from: classes.dex */
public class CircleBounds extends BaseBounds {
    private static final long serialVersionUID = -6571630061846420508L;

    public CircleBounds(float f, float f2, float f3, float f4) {
        this.position.set(f, f2, f3);
        this.radius = f4;
    }

    public CircleBounds(float f, float f2, float f3) {
        this(f, f2, 0.0f, f3);
    }

    public CircleBounds(Vector vector, float f) {
        this(vector.x, vector.y, 0.0f, f);
    }

    @Override // com.serenegiant.math.BaseBounds
    public boolean ptInBounds(float f, float f2, float f3) {
        return ptInBoundsSphere(f, f2, f3, this.radius);
    }
}
