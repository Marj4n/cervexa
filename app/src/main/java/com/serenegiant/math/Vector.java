package com.serenegiant.math;

import android.opengl.Matrix;
import java.io.Serializable;
import java.util.Locale;

/* loaded from: classes.dex */
public class Vector implements Serializable, Cloneable {
    public static final float TO_DEGREE = 57.29578f;
    public static final float TO_RADIAN = 0.017453292f;
    private static final long serialVersionUID = 1620440892067002860L;
    public float x;
    public float y;
    public float z;
    public static final Vector zeroVector = new Vector();
    public static final Vector normVector = new Vector(1.0f, 1.0f, 1.0f);
    private static final float[] matrix = new float[16];
    private static final float[] inVec = new float[4];
    private static final float[] outVec = new float[4];

    public Vector() {
    }

    public Vector(float f, float f2) {
        this(f, f2, 0.0f);
    }

    public Vector(Vector vector) {
        this(vector.x, vector.y, vector.z);
    }

    public Vector(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.z = f3;
    }

    public static Vector vector(float f, float f2, float f3) {
        return new Vector(f, f2, f3);
    }

    public static Vector vector(Vector vector) {
        return new Vector(vector.x, vector.y, vector.z);
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Vector m15clone() throws CloneNotSupportedException {
        return (Vector) super.clone();
    }

    public Vector clear(float f) {
        this.z = f;
        this.y = f;
        this.x = f;
        return this;
    }

    public Vector set(float f, float f2) {
        return set(f, f2, 0.0f);
    }

    public Vector set(Vector vector) {
        return set(vector.x, vector.y, vector.z);
    }

    public Vector set(Vector vector, float f) {
        return set(vector.x, vector.y, vector.z, f);
    }

    public Vector set(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.z = f3;
        return this;
    }

    public Vector set(float f, float f2, float f3, float f4) {
        this.x = f * f4;
        this.y = f2 * f4;
        this.z = f3 * f4;
        return this;
    }

    public float x() {
        return this.x;
    }

    public void x(float f) {
        this.x = f;
    }

    public float y() {
        return this.y;
    }

    public void y(float f) {
        this.y = f;
    }

    public float z() {
        return this.z;
    }

    public void z(float f) {
        this.z = f;
    }

    public Vector add(float f, float f2) {
        return add(f, f2, 0.0f);
    }

    public Vector add(float f, float f2, float f3) {
        this.x += f;
        this.y += f2;
        this.z += f3;
        return this;
    }

    public Vector add(float f, float f2, float f3, float f4) {
        this.x += f * f4;
        this.y += f2 * f4;
        this.z += f3 * f4;
        return this;
    }

    public Vector add(Vector vector) {
        return add(vector.x, vector.y, vector.z);
    }

    public Vector add(Vector vector, float f) {
        return add(vector.x, vector.y, vector.z, f);
    }

    public Vector sub(float f, float f2) {
        return add(-f, -f2, 0.0f);
    }

    public Vector sub(Vector vector) {
        return add(-vector.x, -vector.y, -vector.z);
    }

    public Vector sub(Vector vector, float f) {
        return add(-vector.x, -vector.y, -vector.z, f);
    }

    public Vector sub(float f, float f2, float f3) {
        return add(-f, -f2, -f3);
    }

    public Vector sub(float f, float f2, float f3, float f4) {
        return add(-f, -f2, -f3, f4);
    }

    public Vector mult(Vector vector) {
        this.x *= vector.x;
        this.y *= vector.y;
        this.z *= vector.z;
        return this;
    }

    public Vector mult(float f) {
        this.x *= f;
        this.y *= f;
        this.z *= f;
        return this;
    }

    public Vector mult(float f, float f2) {
        this.x *= f;
        this.y *= f2;
        return this;
    }

    public Vector mult(float f, float f2, float f3) {
        this.x *= f;
        this.y *= f2;
        this.z *= f3;
        return this;
    }

    public Vector div(Vector vector) {
        this.x /= vector.x;
        this.y /= vector.y;
        this.z /= vector.z;
        return this;
    }

    public Vector div(float f) {
        this.x /= f;
        this.y /= f;
        this.z /= f;
        return this;
    }

    public Vector div(float f, float f2) {
        this.x /= f;
        this.y /= f2;
        return this;
    }

    public Vector div(float f, float f2, float f3) {
        this.x /= f;
        this.y /= f2;
        this.z /= f3;
        return this;
    }

    public Vector mod(float f) {
        this.x %= f;
        this.y %= f;
        this.z %= f;
        return this;
    }

    public Vector toRadian() {
        return mult(0.017453292f);
    }

    public Vector toDegree() {
        return mult(57.29578f);
    }

    public Vector limit(float f) {
        float f2;
        float f3 = this.x;
        if (f3 >= f) {
            f3 = f;
        } else {
            float f4 = -f;
            if (f3 < f4) {
                f3 = f4;
            }
        }
        this.x = f3;
        float f5 = this.y;
        if (f5 >= f) {
            f5 = f;
        } else {
            float f6 = -f;
            if (f5 < f6) {
                f5 = f6;
            }
        }
        this.y = f5;
        float f7 = this.z;
        if (f7 >= f) {
            f7 = f;
        } else {
            float f8 = -f;
            if (f7 < f8) {
                f7 = f8;
            }
        }
        this.z = f7;
        while (true) {
            float f9 = this.x;
            if (f9 < f) {
                break;
            }
            this.x = f9 - f;
        }
        while (true) {
            float f10 = this.x;
            f2 = -f;
            if (f10 >= f2) {
                break;
            }
            this.x = f10 + f;
        }
        while (true) {
            float f11 = this.y;
            if (f11 < f) {
                break;
            }
            this.y = f11 - f;
        }
        while (true) {
            float f12 = this.y;
            if (f12 >= f2) {
                break;
            }
            this.y = f12 + f;
        }
        while (true) {
            float f13 = this.z;
            if (f13 < f) {
                break;
            }
            this.z = f13 - f;
        }
        while (true) {
            float f14 = this.z;
            if (f14 >= f2) {
                return this;
            }
            this.z = f14 + f;
        }
    }

    public Vector limit(float f, float f2) {
        float f3 = this.x;
        if (f3 >= f2) {
            f3 = f2;
        } else if (f3 < f) {
            f3 = f;
        }
        this.x = f3;
        float f4 = this.y;
        if (f4 >= f2) {
            f4 = f2;
        } else if (f4 < f) {
            f4 = f;
        }
        this.y = f4;
        float f5 = this.z;
        if (f5 >= f2) {
            f = f2;
        } else if (f5 >= f) {
            f = f5;
        }
        this.z = f;
        return this;
    }

    public float len() {
        float f = this.x;
        float f2 = this.y;
        float f3 = (f * f) + (f2 * f2);
        float f4 = this.z;
        return (float) Math.sqrt(f3 + (f4 * f4));
    }

    public float lenSquared() {
        float f = this.x;
        float f2 = this.y;
        float f3 = (f * f) + (f2 * f2);
        float f4 = this.z;
        return f3 + (f4 * f4);
    }

    public Vector normalize() {
        float len = len();
        if (len != 0.0f) {
            this.x /= len;
            this.y /= len;
            this.z /= len;
        }
        return this;
    }

    public float dot(Vector vector) {
        return (this.x * vector.x) + (this.y * vector.y) + (this.z * vector.z);
    }

    public float dotProduct(Vector vector) {
        return (this.x * vector.x) + (this.y * vector.y) + (this.z * vector.z);
    }

    public float dot(float f, float f2, float f3) {
        return (this.x * f) + (this.y * f2) + (this.z * f3);
    }

    public float dotProduct(float f, float f2, float f3) {
        return (this.x * f) + (this.y * f2) + (this.z * f3);
    }

    public float cross2(Vector vector) {
        return (this.x * vector.y) - (vector.x * this.y);
    }

    public float crossProduct2(Vector vector) {
        return (this.x * vector.y) - (vector.x * this.y);
    }

    public Vector cross(Vector vector) {
        return crossProduct(this, this, vector);
    }

    public Vector crossProduct(Vector vector) {
        return crossProduct(this, this, vector);
    }

    public static Vector cross(Vector vector, Vector vector2, Vector vector3) {
        float f = vector2.y;
        float f2 = vector3.z;
        float f3 = vector2.z;
        float f4 = vector3.y;
        float f5 = (f * f2) - (f3 * f4);
        float f6 = vector3.x;
        float f7 = vector2.x;
        vector.x = f5;
        vector.y = (f3 * f6) - (f2 * f7);
        vector.z = (f7 * f4) - (f * f6);
        return vector;
    }

    public static Vector crossProduct(Vector vector, Vector vector2, Vector vector3) {
        float f = vector2.y;
        float f2 = vector3.z;
        float f3 = vector2.z;
        float f4 = vector3.y;
        float f5 = (f * f2) - (f3 * f4);
        float f6 = vector3.x;
        float f7 = vector2.x;
        vector.x = f5;
        vector.y = (f3 * f6) - (f2 * f7);
        vector.z = (f7 * f4) - (f * f6);
        return vector;
    }

    public float angleXY() {
        float atan2 = ((float) Math.atan2(this.y, this.x)) * 57.29578f;
        return atan2 < 0.0f ? atan2 + 360.0f : atan2;
    }

    public float angleXZ() {
        float atan2 = ((float) Math.atan2(this.z, this.x)) * 57.29578f;
        return atan2 < 0.0f ? atan2 + 360.0f : atan2;
    }

    public float angleYZ() {
        float atan2 = ((float) Math.atan2(this.z, this.y)) * 57.29578f;
        return atan2 < 0.0f ? atan2 + 360.0f : atan2;
    }

    public float getAngle(Vector vector) {
        return ((float) Math.acos(dotProduct(vector) / ((float) Math.sqrt(lenSquared() * vector.lenSquared())))) * 57.29578f;
    }

    public Vector rotateXY(float f) {
        double d = f * 0.017453292f;
        float cos = (float) Math.cos(d);
        float sin = (float) Math.sin(d);
        float f2 = this.x;
        float f3 = this.y;
        this.x = (f2 * cos) - (f3 * sin);
        this.y = (f2 * sin) + (f3 * cos);
        return this;
    }

    public Vector rotateXZ(float f) {
        double d = f * 0.017453292f;
        float cos = (float) Math.cos(d);
        float sin = (float) Math.sin(d);
        float f2 = this.x;
        float f3 = this.z;
        this.x = (f2 * cos) - (f3 * sin);
        this.z = (f2 * sin) + (f3 * cos);
        return this;
    }

    public Vector rotateYZ(float f) {
        double d = f * 0.017453292f;
        float cos = (float) Math.cos(d);
        float sin = (float) Math.sin(d);
        float f2 = this.y;
        float f3 = this.z;
        this.y = (f2 * cos) - (f3 * sin);
        this.z = (f2 * sin) + (f3 * cos);
        return this;
    }

    public Vector rotate(float f, float f2, float f3, float f4) {
        float[] fArr = inVec;
        fArr[0] = this.x;
        fArr[1] = this.y;
        fArr[2] = this.z;
        fArr[3] = 1.0f;
        Matrix.setIdentityM(matrix, 0);
        Matrix.rotateM(matrix, 0, f, f2, f3, f4);
        Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);
        float[] fArr2 = outVec;
        this.x = fArr2[0];
        this.y = fArr2[1];
        this.z = fArr2[2];
        return this;
    }

    public Vector rotate(float f, float f2, float f3) {
        return rotate(this, f, f2, f3);
    }

    public static Vector rotate(Vector vector, float f, float f2, float f3) {
        float[] fArr = inVec;
        fArr[0] = vector.x;
        fArr[1] = vector.y;
        fArr[2] = vector.z;
        fArr[3] = 1.0f;
        Matrix.setIdentityM(matrix, 0);
        if (f != 0.0f) {
            Matrix.rotateM(matrix, 0, f, 1.0f, 0.0f, 0.0f);
        }
        if (f2 != 0.0f) {
            Matrix.rotateM(matrix, 0, f2, 0.0f, 1.0f, 0.0f);
        }
        if (f3 != 0.0f) {
            Matrix.rotateM(matrix, 0, f3, 0.0f, 0.0f, 1.0f);
        }
        Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);
        float[] fArr2 = outVec;
        vector.x = fArr2[0];
        vector.y = fArr2[1];
        vector.z = fArr2[2];
        return vector;
    }

    public static Vector[] rotate(Vector[] vectorArr, float f, float f2, float f3) {
        Matrix.setIdentityM(matrix, 0);
        if (f != 0.0f) {
            Matrix.rotateM(matrix, 0, f, 1.0f, 0.0f, 0.0f);
        }
        if (f2 != 0.0f) {
            Matrix.rotateM(matrix, 0, f2, 0.0f, 1.0f, 0.0f);
        }
        if (f3 != 0.0f) {
            Matrix.rotateM(matrix, 0, f3, 0.0f, 0.0f, 1.0f);
        }
        int length = vectorArr != null ? vectorArr.length : 0;
        for (int i = 0; i < length; i++) {
            if (vectorArr[i] != null) {
                float[] fArr = inVec;
                fArr[0] = vectorArr[i].x;
                fArr[1] = vectorArr[i].y;
                fArr[2] = vectorArr[i].z;
                fArr[3] = 1.0f;
                Matrix.multiplyMV(outVec, 0, matrix, 0, fArr, 0);
                Vector vector = vectorArr[i];
                float[] fArr2 = outVec;
                vector.x = fArr2[0];
                vectorArr[i].y = fArr2[1];
                vectorArr[i].z = fArr2[2];
            }
        }
        return vectorArr;
    }

    public Vector rotate(Vector vector, float f) {
        rotate(vector.x * f, vector.y * f, vector.z * f);
        return this;
    }

    public Vector rotate(Vector vector) {
        return rotate(vector.x, vector.y, vector.z);
    }

    public Vector rotate_inv(float f, float f2, float f3) {
        float[] fArr = inVec;
        fArr[0] = this.x;
        fArr[1] = this.y;
        fArr[2] = this.z;
        fArr[3] = 1.0f;
        Matrix.setIdentityM(matrix, 0);
        if (f3 != 0.0f) {
            Matrix.rotateM(matrix, 0, f3, 0.0f, 0.0f, 1.0f);
        }
        if (f2 != 0.0f) {
            Matrix.rotateM(matrix, 0, f2, 0.0f, 1.0f, 0.0f);
        }
        if (f != 0.0f) {
            Matrix.rotateM(matrix, 0, f, 1.0f, 0.0f, 0.0f);
        }
        Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);
        float[] fArr2 = outVec;
        this.x = fArr2[0];
        this.y = fArr2[1];
        this.z = fArr2[2];
        return this;
    }

    public Vector rotate_inv(Vector vector, float f) {
        rotate_inv(vector.x * f, vector.y * f, vector.z * f);
        return this;
    }

    public Vector rotate_inv(Vector vector) {
        rotate_inv(vector, -1.0f);
        return this;
    }

    public float[] getQuat() {
        return new float[]{this.x, this.y, this.z, 1.0f};
    }

    public Vector setQuat(float[] fArr) {
        this.x = fArr[0];
        this.y = fArr[1];
        this.z = fArr[2];
        return this;
    }

    public float distance(Vector vector) {
        return distance(vector.x, vector.y, vector.z);
    }

    public float distance(float f, float f2) {
        return distance(f, f2, this.z);
    }

    public float distance(float f, float f2, float f3) {
        return (float) Math.sqrt(distSquared(f, f2, f3));
    }

    public float distSquared(Vector vector) {
        return distSquared(vector.x, vector.y, vector.z);
    }

    public float distSquared(float f, float f2) {
        return distSquared(f, f2, this.z);
    }

    public float distSquared(float f, float f2, float f3) {
        float f4 = this.x - f;
        float f5 = this.y - f2;
        float f6 = this.z - f3;
        return (f4 * f4) + (f5 * f5) + (f6 * f6);
    }

    public Vector swap(Vector vector) {
        float f = this.x;
        this.x = vector.x;
        vector.x = f;
        float f2 = this.y;
        this.y = vector.y;
        vector.y = f2;
        float f3 = this.z;
        this.z = vector.z;
        vector.z = f3;
        return this;
    }

    public Vector swapXY() {
        float f = this.x;
        this.x = this.y;
        this.y = f;
        return this;
    }

    public float slope(Vector vector) {
        float f = vector.x;
        float f2 = this.x;
        if (f != f2) {
            return (vector.y - this.y) / (f - f2);
        }
        return vector.y - this.y >= 0.0f ? Float.MAX_VALUE : Float.MIN_VALUE;
    }

    public float slope() {
        float f = this.x;
        if (f != 0.0f) {
            return this.y / f;
        }
        return this.y >= 0.0f ? Float.MAX_VALUE : Float.MIN_VALUE;
    }

    public Vector sign() {
        this.x = Math.signum(this.x);
        this.y = Math.signum(this.y);
        this.z = Math.signum(this.z);
        return this;
    }

    public String toString() {
        return String.format(Locale.US, "(%f,%f,%f)", Float.valueOf(this.x), Float.valueOf(this.y), Float.valueOf(this.z));
    }

    public String toString(String str) {
        return String.format(Locale.US, str, Float.valueOf(this.x), Float.valueOf(this.y), Float.valueOf(this.z));
    }
}
