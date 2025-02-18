package com.serenegiant.glutils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;
import com.serenegiant.utils.AssetsHelper;
import com.serenegiant.utils.BuildCheck;
import java.io.IOException;

/* loaded from: classes.dex */
public final class GLHelper {
    private static final String TAG = "GLHelper";

    public static void checkGlError(String str) {
        int glGetError = GLES20.glGetError();
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
        GLES20.glActiveTexture(i2);
        GLES20.glGenTextures(1, iArr, 0);
        GLES20.glBindTexture(i, iArr[0]);
        GLES20.glTexParameteri(i, 10242, i5);
        GLES20.glTexParameteri(i, 10243, i5);
        GLES20.glTexParameteri(i, 10241, i3);
        GLES20.glTexParameteri(i, 10240, i4);
        return iArr[0];
    }

    public static int[] initTexes(int i, int i2, int i3) {
        return initTexes(new int[i], i2, i3, i3, 33071);
    }

    public static int[] initTexes(int[] iArr, int i, int i2) {
        return initTexes(iArr, i, i2, i2, 33071);
    }

    public static int[] initTexes(int i, int i2, int i3, int i4, int i5) {
        return initTexes(new int[i], i2, i3, i4, i5);
    }

    public static int[] initTexes(int[] iArr, int i, int i2, int i3, int i4) {
        int[] iArr2 = new int[1];
        GLES20.glGetIntegerv(34930, iArr2, 0);
        Log.v(TAG, "GL_MAX_TEXTURE_IMAGE_UNITS=" + iArr2[0]);
        int length = iArr.length > iArr2[0] ? iArr2[0] : iArr.length;
        for (int i5 = 0; i5 < length; i5++) {
            iArr[i5] = initTex(i, ShaderConst.TEX_NUMBERS[i5], i2, i3, i4);
        }
        return iArr;
    }

    public static int[] initTexes(int i, int i2, int i3, int i4, int i5, int i6) {
        return initTexes(new int[i], i2, i3, i4, i5, i6);
    }

    public static int[] initTexes(int[] iArr, int i, int i2, int i3) {
        return initTexes(iArr, i, i2, i3, i3, 33071);
    }

    public static int[] initTexes(int[] iArr, int i, int i2, int i3, int i4, int i5) {
        int[] iArr2 = new int[1];
        GLES20.glGetIntegerv(34930, iArr2, 0);
        int length = iArr.length > iArr2[0] ? iArr2[0] : iArr.length;
        for (int i6 = 0; i6 < length; i6++) {
            iArr[i6] = initTex(i, i2, i3, i4, i5);
        }
        return iArr;
    }

    public static void deleteTex(int i) {
        GLES20.glDeleteTextures(1, new int[]{i}, 0);
    }

    public static void deleteTex(int[] iArr) {
        GLES20.glDeleteTextures(iArr.length, iArr, 0);
    }

    public static int loadTextureFromResource(Context context, int i) {
        return loadTextureFromResource(context, i, null);
    }

    public static int loadTextureFromResource(Context context, int i, Resources.Theme theme) {
        Drawable drawable;
        Bitmap createBitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawARGB(0, 0, 255, 0);
        if (BuildCheck.isAndroid5()) {
            drawable = context.getResources().getDrawable(i, theme);
        } else {
            drawable = context.getResources().getDrawable(i);
        }
        drawable.setBounds(0, 0, 256, 256);
        drawable.draw(canvas);
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        GLES20.glBindTexture(ShaderConst.GL_TEXTURE_2D, iArr[0]);
        GLES20.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10241, 9728.0f);
        GLES20.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10240, 9729.0f);
        GLES20.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10242, 10497.0f);
        GLES20.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10243, 10497.0f);
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

    public static int loadShader(Context context, String str, String str2) {
        try {
            return loadShader(AssetsHelper.loadString(context.getAssets(), str), AssetsHelper.loadString(context.getAssets(), str));
        } catch (IOException unused) {
            return 0;
        }
    }

    public static int loadShader(String str, String str2) {
        int loadShader;
        int loadShader2 = loadShader(35633, str);
        if (loadShader2 == 0 || (loadShader = loadShader(35632, str2)) == 0) {
            return 0;
        }
        int glCreateProgram = GLES20.glCreateProgram();
        checkGlError("glCreateProgram");
        if (glCreateProgram == 0) {
            Log.e(TAG, "Could not create program");
        }
        GLES20.glAttachShader(glCreateProgram, loadShader2);
        checkGlError("glAttachShader");
        GLES20.glAttachShader(glCreateProgram, loadShader);
        checkGlError("glAttachShader");
        GLES20.glLinkProgram(glCreateProgram);
        int[] iArr = new int[1];
        GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
        if (iArr[0] == 1) {
            return glCreateProgram;
        }
        Log.e(TAG, "Could not link program: ");
        Log.e(TAG, GLES20.glGetProgramInfoLog(glCreateProgram));
        GLES20.glDeleteProgram(glCreateProgram);
        return 0;
    }

    public static int loadShader(int i, String str) {
        int glCreateShader = GLES20.glCreateShader(i);
        checkGlError("glCreateShader type=" + i);
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        int[] iArr = new int[1];
        GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] != 0) {
            return glCreateShader;
        }
        Log.e(TAG, "Could not compile shader " + i + ":");
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        sb.append(GLES20.glGetShaderInfoLog(glCreateShader));
        Log.e(TAG, sb.toString());
        GLES20.glDeleteShader(glCreateShader);
        return 0;
    }

    public static void checkLocation(int i, String str) {
        if (i >= 0) {
            return;
        }
        throw new RuntimeException("Unable to locate '" + str + "' in program");
    }

    public static void logVersionInfo() {
        Log.i(TAG, "vendor  : " + GLES20.glGetString(7936));
        Log.i(TAG, "renderer: " + GLES20.glGetString(7937));
        Log.i(TAG, "version : " + GLES20.glGetString(7938));
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
}
