package com.serenegiant.glutils.es1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.opengl.GLES10;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import com.serenegiant.glutils.ShaderConst;
import com.serenegiant.utils.BuildCheck;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public final class GLHelper {
    private static final String TAG = "GLHelper";
    private static final float[] sScratch = new float[32];

    public static String gluErrorString(int i) {
        if (i == 0) {
            return "no error";
        }
        switch (i) {
            case 1280:
                return "invalid enum";
            case 1281:
                return "invalid value";
            case 1282:
                return "invalid operation";
            case 1283:
                return "stack overflow";
            case 1284:
                return "stack underflow";
            case 1285:
                return "out of memory";
            default:
                return null;
        }
    }

    public static void checkGlError(String str) {
        int glGetError = GLES10.glGetError();
        if (glGetError != 0) {
            String str2 = str + ": glError 0x" + Integer.toHexString(glGetError);
            Log.e(TAG, str2);
            new Throwable(str2).printStackTrace();
        }
    }

    public static void checkGlError(GL10 gl10, String str) {
        int glGetError = gl10.glGetError();
        if (glGetError != 0) {
            String str2 = str + ": glError 0x" + Integer.toHexString(glGetError);
            Log.e(TAG, str2);
            new Throwable(str2).printStackTrace();
        }
    }

    public static int initTex(int i, int i2) {
        return initTex(i, 33984, i2, i2, 33071);
    }

    public static int initTex(int i, int i2, int i3, int i4, int i5) {
        int[] iArr = new int[1];
        GLES10.glActiveTexture(i2);
        GLES10.glGenTextures(1, iArr, 0);
        GLES10.glBindTexture(i, iArr[0]);
        GLES10.glTexParameterx(i, 10242, i5);
        GLES10.glTexParameterx(i, 10243, i5);
        GLES10.glTexParameterx(i, 10241, i3);
        GLES10.glTexParameterx(i, 10240, i4);
        return iArr[0];
    }

    public static int initTex(GL10 gl10, int i, int i2) {
        int[] iArr = new int[1];
        gl10.glActiveTexture(33984);
        gl10.glGenTextures(1, iArr, 0);
        gl10.glBindTexture(i, iArr[0]);
        gl10.glTexParameterx(i, 10242, 33071);
        gl10.glTexParameterx(i, 10243, 33071);
        gl10.glTexParameterx(i, 10241, i2);
        gl10.glTexParameterx(i, 10240, i2);
        return iArr[0];
    }

    public static void deleteTex(int i) {
        GLES10.glDeleteTextures(1, new int[]{i}, 0);
    }

    public static void deleteTex(GL10 gl10, int i) {
        gl10.glDeleteTextures(1, new int[]{i}, 0);
    }

    public static int loadTextureFromResource(Context context, int i) {
        Bitmap createBitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawARGB(0, 0, 255, 0);
        Drawable drawable = context.getResources().getDrawable(i);
        drawable.setBounds(0, 0, 256, 256);
        drawable.draw(canvas);
        int[] iArr = new int[1];
        GLES10.glGenTextures(1, iArr, 0);
        GLES10.glBindTexture(ShaderConst.GL_TEXTURE_2D, iArr[0]);
        GLES10.glTexParameterx(ShaderConst.GL_TEXTURE_2D, 10241, 9728);
        GLES10.glTexParameterx(ShaderConst.GL_TEXTURE_2D, 10240, 9729);
        GLES10.glTexParameterx(ShaderConst.GL_TEXTURE_2D, 10242, 10497);
        GLES10.glTexParameterx(ShaderConst.GL_TEXTURE_2D, 10243, 10497);
        GLUtils.texImage2D(ShaderConst.GL_TEXTURE_2D, 0, createBitmap, 0);
        createBitmap.recycle();
        return iArr[0];
    }

    public static int createTextureWithTextContent(String str) {
        Bitmap createBitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawARGB(0, 0, 255, 0);
        Paint paint = new Paint();
        paint.setTextSize(32.0f);
        paint.setAntiAlias(true);
        paint.setARGB(255, 255, 255, 255);
        canvas.drawText(str, 16.0f, 112.0f, paint);
        int initTex = initTex(ShaderConst.GL_TEXTURE_2D, 33984, 9728, 9729, 10497);
        GLUtils.texImage2D(ShaderConst.GL_TEXTURE_2D, 0, createBitmap, 0);
        createBitmap.recycle();
        return initTex;
    }

    public static void checkLocation(int i, String str) {
        if (i >= 0) {
            return;
        }
        throw new RuntimeException("Unable to locate '" + str + "' in program");
    }

    public static void logVersionInfo() {
        Log.i(TAG, "vendor  : " + GLES10.glGetString(7936));
        Log.i(TAG, "renderer: " + GLES10.glGetString(7937));
        Log.i(TAG, "version : " + GLES10.glGetString(7938));
        if (BuildCheck.isAndroid4_3()) {
            int[] iArr = new int[1];
            GLES30.glGetIntegerv(33307, iArr, 0);
            int i = iArr[0];
            GLES30.glGetIntegerv(33308, iArr, 0);
            int i2 = iArr[0];
            if (GLES30.glGetError() == 0) {
                Log.i(TAG, "version: " + i + "." + i2);
            }
        }
    }

    public static void gluLookAt(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        float[] fArr = sScratch;
        synchronized (fArr) {
            Matrix.setLookAtM(fArr, 0, f, f2, f3, f4, f5, f6, f7, f8, f9);
            GLES10.glMultMatrixf(fArr, 0);
        }
    }

    public static void gluOrtho2D(float f, float f2, float f3, float f4) {
        GLES10.glOrthof(f, f2, f3, f4, -1.0f, 1.0f);
    }

    public static void gluPerspective(float f, float f2, float f3, float f4) {
        float tan = f3 * ((float) Math.tan(f * 0.008726646259971648d));
        float f5 = -tan;
        GLES10.glFrustumf(f5 * f2, tan * f2, f5, tan, f3, f4);
    }

    public static int gluProject(float f, float f2, float f3, float[] fArr, int i, float[] fArr2, int i2, int[] iArr, int i3, float[] fArr3, int i4) {
        float[] fArr4 = sScratch;
        synchronized (fArr4) {
            Matrix.multiplyMM(fArr4, 0, fArr2, i2, fArr, i);
            fArr4[16] = f;
            fArr4[17] = f2;
            fArr4[18] = f3;
            fArr4[19] = 1.0f;
            Matrix.multiplyMV(fArr4, 20, fArr4, 0, fArr4, 16);
            float f4 = fArr4[23];
            if (f4 == 0.0f) {
                return 0;
            }
            float f5 = 1.0f / f4;
            fArr3[i4] = iArr[i3] + (iArr[i3 + 2] * ((fArr4[20] * f5) + 1.0f) * 0.5f);
            fArr3[i4 + 1] = iArr[i3 + 1] + (iArr[i3 + 3] * ((fArr4[21] * f5) + 1.0f) * 0.5f);
            fArr3[i4 + 2] = ((fArr4[22] * f5) + 1.0f) * 0.5f;
            return 1;
        }
    }

    public static int gluUnProject(float f, float f2, float f3, float[] fArr, int i, float[] fArr2, int i2, int[] iArr, int i3, float[] fArr3, int i4) {
        float[] fArr4 = sScratch;
        synchronized (fArr4) {
            Matrix.multiplyMM(fArr4, 0, fArr2, i2, fArr, i);
            if (!Matrix.invertM(fArr4, 16, fArr4, 0)) {
                return 0;
            }
            fArr4[0] = (((f - iArr[i3 + 0]) * 2.0f) / iArr[i3 + 2]) - 1.0f;
            fArr4[1] = (((f2 - iArr[i3 + 1]) * 2.0f) / iArr[i3 + 3]) - 1.0f;
            fArr4[2] = (f3 * 2.0f) - 1.0f;
            fArr4[3] = 1.0f;
            Matrix.multiplyMV(fArr3, i4, fArr4, 16, fArr4, 0);
            return 1;
        }
    }
}
