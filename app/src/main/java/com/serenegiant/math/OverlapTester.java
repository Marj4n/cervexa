package com.serenegiant.math;

/* loaded from: classes.dex */
public class OverlapTester {
    private static final Vector r1L = new Vector();
    private static final Vector r2L = new Vector();

    public static boolean check(BaseBounds baseBounds, BaseBounds baseBounds2) {
        float distSquared = baseBounds.position.distSquared(baseBounds2.position);
        float f = baseBounds.radius + baseBounds2.radius;
        return distSquared <= f * f;
    }

    public static boolean check(CircleBounds circleBounds, CircleBounds circleBounds2) {
        float distSquared = circleBounds.position.distSquared(circleBounds2.position);
        float f = circleBounds.radius + circleBounds2.radius;
        return distSquared <= f * f;
    }

    public static boolean check(RectangleBounds rectangleBounds, RectangleBounds rectangleBounds2) {
        r1L.set(rectangleBounds.position).sub(rectangleBounds.box);
        float f = rectangleBounds.box.x * 2.0f;
        float f2 = rectangleBounds.box.y * 2.0f;
        float f3 = rectangleBounds.box.z * 2.0f;
        r2L.set(rectangleBounds2.position).sub(rectangleBounds2.box);
        return r1L.x < r2L.x + (rectangleBounds2.box.x * 2.0f) && r1L.x + f > r2L.x && r1L.y < r2L.y + (rectangleBounds2.box.y * 2.0f) && r1L.y + f2 > r2L.y && r1L.z < r2L.z + (rectangleBounds2.box.z * 2.0f) && r1L.z + f3 > r2L.z;
    }

    public static boolean check(CircleBounds circleBounds, RectangleBounds rectangleBounds) {
        float f = circleBounds.position.x;
        float f2 = circleBounds.position.y;
        float f3 = circleBounds.position.z;
        r1L.set(rectangleBounds.position).sub(rectangleBounds.box);
        float f4 = rectangleBounds.box.x * 2.0f;
        float f5 = rectangleBounds.box.y * 2.0f;
        float f6 = rectangleBounds.box.z * 2.0f;
        if (circleBounds.position.x < r1L.x) {
            f = r1L.x;
        } else if (circleBounds.position.x > r1L.x + f4) {
            f = r1L.x + f4;
        }
        if (circleBounds.position.y < r1L.y) {
            f2 = r1L.y;
        } else if (circleBounds.position.y > r1L.y + f5) {
            f2 = r1L.y + f5;
        }
        if (circleBounds.position.z < r1L.z) {
            f3 = r1L.z;
        } else if (circleBounds.position.z > r1L.z + f6) {
            f3 = r1L.z + f6;
        }
        return circleBounds.position.distSquared(f, f2, f3) < circleBounds.radius * circleBounds.radius;
    }

    public static boolean check(CircleBounds circleBounds, Vector vector) {
        return circleBounds.position.distSquared(vector) < circleBounds.radius * circleBounds.radius;
    }

    public static boolean check(CircleBounds circleBounds, float f, float f2, float f3) {
        return circleBounds.position.distSquared(f, f2, f3) < circleBounds.radius * circleBounds.radius;
    }

    public static boolean check(CircleBounds circleBounds, float f, float f2) {
        return circleBounds.position.distSquared(f, f2) < circleBounds.radius * circleBounds.radius;
    }

    public static boolean check(RectangleBounds rectangleBounds, Vector vector) {
        return check(rectangleBounds, vector.x, vector.y, vector.z);
    }

    public static boolean check(RectangleBounds rectangleBounds, float f, float f2) {
        return check(rectangleBounds, f, f2, 0.0f);
    }

    public static boolean check(RectangleBounds rectangleBounds, float f, float f2, float f3) {
        r1L.set(rectangleBounds.position).sub(rectangleBounds.box);
        return r1L.x <= f && r1L.x + (rectangleBounds.box.x * 2.0f) >= f && r1L.y <= f2 && r1L.y + (rectangleBounds.box.y * 2.0f) >= f2 && r1L.z <= f3 && r1L.z + (rectangleBounds.box.z * 2.0f) >= f3;
    }

    public static boolean check(SphereBounds sphereBounds, SphereBounds sphereBounds2) {
        return sphereBounds.position.distance(sphereBounds2.position) <= sphereBounds.radius + sphereBounds2.radius;
    }

    public static boolean check(SphereBounds sphereBounds, Vector vector) {
        return sphereBounds.position.distSquared(vector) < sphereBounds.radius * sphereBounds.radius;
    }

    public static boolean check(SphereBounds sphereBounds, float f, float f2, float f3) {
        return sphereBounds.position.distance(f, f2, f3) < sphereBounds.radius * sphereBounds.radius;
    }
}
