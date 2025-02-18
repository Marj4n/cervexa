package com.serenegiant.math;

/* loaded from: classes.dex */
public class CylinderBounds extends BaseBounds {
    private static final long serialVersionUID = -2875851852923460432L;
    protected float height;
    protected float outer_r;
    private final Vector w1;
    private final Vector w2;

    public CylinderBounds(float f, float f2, float f3, float f4, float f5) {
        this.w1 = new Vector();
        this.w2 = new Vector();
        this.position.set(f, f2, f3);
        this.radius = (float) Math.sqrt((f5 * f5) + ((f4 * f4) / 4.0f));
        this.outer_r = f5;
        this.height = f4 / 2.0f;
    }

    public CylinderBounds(Vector vector, float f, float f2) {
        this(vector.x, vector.y, vector.z, f, f2);
    }

    protected boolean ptInCylinder(float f, float f2, float f3, float f4) {
        this.w1.set(f, f2, f3).sub(this.position).rotate(this.angle, -1.0f);
        this.w2.set(this.w1);
        this.w2.y = 0.0f;
        if (this.w2.distSquared(this.position.x, 0.0f, this.position.z) < f4 * f4) {
            return this.w1.x >= this.position.x - f4 && this.w1.x <= this.position.x + f4 && this.w1.y >= this.position.y - this.height && this.w1.y <= this.position.y + this.height;
        }
        return false;
    }

    @Override // com.serenegiant.math.BaseBounds
    public boolean ptInBounds(float f, float f2, float f3) {
        boolean ptInBoundsSphere = ptInBoundsSphere(f, f2, f3, this.radius);
        return ptInBoundsSphere ? ptInCylinder(f, f2, f3, this.outer_r) : ptInBoundsSphere;
    }
}
