package com.generalplus.GoPlusDrone.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.GLES10;

import com.baidu.trace.model.Point;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/* loaded from: classes.dex */
public class BitmapUtils {
    public static Bitmap temp;
    private static int textureSize;

    public static Bitmap bitmapZoomByHeight(Bitmap bitmap, float f) {
        float height = f / bitmap.getHeight();
        return bitmapZoomByScale(bitmap, height, height);
    }

    public static Bitmap bitmapZoomByHeight(Drawable drawable, float f) {
        Point r1 = null;
        float height = (float) (f / r1.getHeight());
        return bitmapZoomByScale(drawableToBitmap(drawable), height, height);
    }

    public static Bitmap bitmapZoomByScale(Bitmap bitmap, float f, float f2) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(f, f2);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return createBitmap != null ? createBitmap : bitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, drawable.getOpacity() != PixelFormat.UNKNOWN ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return createBitmap;
    }

    public static Bitmap drawableToBitmap2(Drawable drawable) {
        return ((BitmapDrawable) drawable).getBitmap();
    }

    public static void saveBitmapToSDCard(Bitmap bitmap, String str) {
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            System.out.println("----------save success-------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromSDCard(String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            return BitmapFactory.decodeStream(fileInputStream, null, options);
        } catch (Exception unused) {
            return null;
        }
    }

    public static void compressSize(Bitmap bitmap, File file, int i, int i2) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, (Rect) null, new Rect(0, 0, i, i2), (Paint) null);
        compressQuality(createBitmap, 100, file);
    }

    public static void compressQuality(Bitmap bitmap, int i, File file) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, i, byteArrayOutputStream);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outWidth;
        int i4 = options.outHeight;
        int i5 = 1;
        if (i4 > i2 || i3 > i2) {
            int i6 = i4 / 2;
            int i7 = i3 / 2;
            while (i7 / i5 >= i2 && i6 / i5 >= i) {
                i5 *= 2;
            }
        }
        return i5;
    }

    public static Bitmap getZoomImage(Bitmap bitmap, int i, int i2) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[width * height];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        int[] iArr2 = new int[i2];
        int[] iArr3 = new int[i];
        int i3 = height > i2 ? height : i2;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        for (int i8 = 0; i8 <= i3; i8++) {
            iArr2[i4] = i5;
            i6 += height;
            i7 += i2;
            if (i6 > i3) {
                i6 -= i3;
                i5++;
            }
            if (i7 > i3) {
                i7 -= i3;
                i4++;
            }
        }
        int i9 = width > i ? width : i;
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        int i13 = 0;
        for (int i14 = 0; i14 <= i9; i14++) {
            iArr3[i10] = (short) i11;
            i12 += width;
            i13 += i;
            if (i12 > i9) {
                i12 -= i9;
                i11++;
            }
            if (i13 > i9) {
                i13 -= i9;
                i10++;
            }
        }
        int[] iArr4 = new int[i * i2];
        int i15 = -1;
        int i16 = 0;
        int i17 = 0;
        for (int i18 = 0; i18 < i2; i18++) {
            if (i15 == iArr2[i18]) {
                System.arraycopy(iArr4, i16 - i, iArr4, i16, i);
            } else {
                int i19 = 0;
                for (int i20 = 0; i20 < i; i20++) {
                    iArr4[i16 + i19] = iArr[iArr3[i20] + i17];
                    i19++;
                }
                i17 += (iArr2[i18] - i15) * width;
            }
            i15 = iArr2[i18];
            i16 += i;
        }
        return Bitmap.createBitmap(iArr4, i, i2, Bitmap.Config.ARGB_8888);
    }

    public static final int getTextureSize() {
        int i = textureSize;
        if (i > 0) {
            return i;
        }
        int[] iArr = new int[1];
        GLES10.glGetIntegerv(3379, iArr, 0);
        int i2 = iArr[0];
        textureSize = i2;
        return i2;
    }
}
