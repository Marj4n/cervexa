package com.serenegiant.math;

import android.graphics.Rect;

/* loaded from: classes.dex */
public class RectangleBounds extends BaseBounds {
    private static final long serialVersionUID = 260429282595037220L;
    private final Rect boundsRect;
    public final Vector box;
    private final Vector w;

    public RectangleBounds(float f, float f2, float f3, float f4, float f5, float f6) {
        this.box = new Vector();
        this.boundsRect = new Rect();
        this.w = new Vector();
        this.position.set(f, f2, f3);
        this.box.set(f4 / 2.0f, f5 / 2.0f, f6 / 2.0f);
        this.radius = this.box.len();
    }

    public RectangleBounds(float f, float f2, float f3, float f4) {
        this(f, f2, 0.0f, f3, f4, 0.0f);
    }

    public RectangleBounds(Vector vector, float f, float f2) {
        this(vector.x, vector.y, vector.z, f, f2, 0.0f);
    }

    public RectangleBounds(Vector vector, Vector vector2) {
        this.box = new Vector();
        this.boundsRect = new Rect();
        this.w = new Vector();
        if (vector.x > vector2.x) {
            float f = vector.x;
            vector.x = vector2.x;
            vector2.x = f;
        }
        if (vector.y > vector2.y) {
            float f2 = vector.y;
            vector.y = vector2.y;
            vector2.y = f2;
        }
        if (vector.z > vector2.z) {
            float f3 = vector.z;
            vector.z = vector2.z;
            vector2.z = f3;
        }
        setPosition((vector2.x - vector.x) / 2.0f, (vector2.y - vector.y) / 2.0f, (vector2.z - vector.z) / 2.0f);
        this.box.set(this.position).sub(vector);
        this.radius = this.box.len();
    }

    public RectangleBounds(Rect rect) {
        this(rect.centerX(), rect.centerY(), rect.width(), rect.height());
    }

    @Override // com.serenegiant.math.BaseBounds
    public boolean ptInBounds(float f, float f2, float f3) {
        boolean ptInBoundsSphere = ptInBoundsSphere(f, f2, f3, this.radius);
        if (!ptInBoundsSphere) {
            return ptInBoundsSphere;
        }
        this.w.set(f, f2, f3).sub(this.position).rotate(this.angle, -1.0f);
        return this.w.x >= this.position.x - this.box.x && this.w.x <= this.position.x + this.box.x && this.w.y >= this.position.y - this.box.y && this.w.y <= this.position.y + this.box.y && this.w.z >= this.position.z - this.box.z && this.w.z <= this.position.z + this.box.z;
    }

    public Rect boundsRect() {
        this.boundsRect.set((int) (this.position.x - this.box.x), (int) (this.position.y - this.box.y), (int) (this.position.x + this.box.x), (int) (this.position.y + this.box.y));
        return this.boundsRect;
    }

    public Rect boundsRect(float f) {
        this.boundsRect.set((int) (this.position.x - (this.box.x * f)), (int) (this.position.y - (this.box.y * f)), (int) (this.position.x + (this.box.x * f)), (int) (this.position.y + (this.box.y * f)));
        return this.boundsRect;
    }
}
