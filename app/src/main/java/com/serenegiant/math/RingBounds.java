package com.serenegiant.math;

/* loaded from: classes.dex */
public class RingBounds extends CylinderBounds {
    private static final long serialVersionUID = -5157039256747626240L;
    protected float height;
    protected float inner_r;

    public RingBounds(float f, float f2, float f3, float f4, float f5, float f6) {
        super(f, f2, f3, f4, f5);
        this.inner_r = f6;
    }

    public RingBounds(Vector vector, float f, float f2, float f3) {
        this(vector.x, vector.y, vector.z, f, f2, f3);
    }

    @Override // com.serenegiant.math.CylinderBounds, com.serenegiant.math.BaseBounds
    public boolean ptInBounds(float f, float f2, float f3) {
        boolean ptInBounds = super.ptInBounds(f, f2, f3);
        return ptInBounds ? !ptInCylinder(f, f2, f3, this.inner_r) : ptInBounds;
    }
}
