package com.serenegiant.graphics;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import com.jieli.stream.dv.running2.ui.widget.verticalseekbar.VerticalSeekBar;
import com.serenegiant.utils.BitsHelper;
import com.serenegiant.utils.UriHelper;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public final class BitmapHelper {
    public static final int OPTIONS_RECYCLE_INPUT = 2;
    private static final int OPTIONS_SCALE_UP = 1;

    public static byte[] BitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)) {
            return byteArrayOutputStream.toByteArray();
        }
        return null;
    }

    public static Bitmap asBitmap(byte[] bArr) {
        if (bArr != null) {
            return BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
        }
        return null;
    }

    public static Bitmap asBitmap(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calcSampleSize(options, i, i2);
        return BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
    }

    public static Bitmap asBitmapStrictSize(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        int calcSampleSize = calcSampleSize(options, i, i2);
        int MSB = 1 << BitsHelper.MSB(calcSampleSize);
        options.inSampleSize = MSB;
        options.inJustDecodeBounds = false;
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        if (MSB == calcSampleSize && decodeByteArray.getWidth() == i && decodeByteArray.getHeight() == i2) {
            return decodeByteArray;
        }
        Bitmap scaleBitmap = scaleBitmap(decodeByteArray, i, i2);
        decodeByteArray.recycle();
        return scaleBitmap;
    }

    public static Bitmap asBitmap(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return BitmapFactory.decodeFile(str);
    }

    public static Bitmap asBitmap(String str, int i, int i2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calcSampleSize(options, i, i2);
        return BitmapFactory.decodeFile(str, options);
    }

    public static Bitmap asBitmapStrictSize(String str, int i, int i2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        int calcSampleSize = calcSampleSize(options, i, i2);
        int MSB = 1 << BitsHelper.MSB(calcSampleSize);
        options.inSampleSize = MSB;
        options.inJustDecodeBounds = false;
        Bitmap decodeFile = BitmapFactory.decodeFile(str, options);
        if (MSB == calcSampleSize && decodeFile.getWidth() == i && decodeFile.getHeight() == i2) {
            return decodeFile;
        }
        Bitmap scaleBitmap = scaleBitmap(decodeFile, i, i2);
        decodeFile.recycle();
        return scaleBitmap;
    }

    public static Bitmap asBitmap(FileDescriptor fileDescriptor) {
        if (fileDescriptor == null || !fileDescriptor.valid()) {
            return null;
        }
        return BitmapFactory.decodeFileDescriptor(fileDescriptor);
    }

    public static Bitmap asBitmap(FileDescriptor fileDescriptor, int i, int i2) {
        if (fileDescriptor == null || !fileDescriptor.valid()) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calcSampleSize(options, i, i2);
        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }

    public static Bitmap asBitmapStrictSize(FileDescriptor fileDescriptor, int i, int i2) {
        if (fileDescriptor == null || !fileDescriptor.valid()) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        int calcSampleSize = calcSampleSize(options, i, i2);
        int MSB = 1 << BitsHelper.MSB(calcSampleSize);
        options.inSampleSize = MSB;
        options.inJustDecodeBounds = false;
        Bitmap decodeFileDescriptor = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        if (MSB == calcSampleSize && decodeFileDescriptor.getWidth() == i && decodeFileDescriptor.getHeight() == i2) {
            return decodeFileDescriptor;
        }
        Bitmap scaleBitmap = scaleBitmap(decodeFileDescriptor, i, i2);
        decodeFileDescriptor.recycle();
        return scaleBitmap;
    }

    public static Bitmap asBitmap(ContentResolver contentResolver, Uri uri) throws FileNotFoundException, IOException {
        ParcelFileDescriptor openFileDescriptor;
        if (uri == null || (openFileDescriptor = contentResolver.openFileDescriptor(uri, "r")) == null) {
            return null;
        }
        Bitmap decodeFileDescriptor = BitmapFactory.decodeFileDescriptor(openFileDescriptor.getFileDescriptor());
        int orientation = getOrientation(contentResolver, uri);
        if (orientation == 0) {
            return decodeFileDescriptor;
        }
        Bitmap rotateBitmap = rotateBitmap(decodeFileDescriptor, orientation);
        decodeFileDescriptor.recycle();
        return rotateBitmap;
    }

    public static Bitmap asBitmap(ContentResolver contentResolver, Uri uri, int i, int i2) throws FileNotFoundException, IOException {
        ParcelFileDescriptor openFileDescriptor;
        if (uri == null || (openFileDescriptor = contentResolver.openFileDescriptor(uri, "r")) == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(openFileDescriptor.getFileDescriptor(), null, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calcSampleSize(options, i, i2);
        Bitmap decodeFileDescriptor = BitmapFactory.decodeFileDescriptor(openFileDescriptor.getFileDescriptor(), null, options);
        int orientation = getOrientation(contentResolver, uri);
        if (orientation == 0) {
            return decodeFileDescriptor;
        }
        Bitmap rotateBitmap = rotateBitmap(decodeFileDescriptor, orientation);
        decodeFileDescriptor.recycle();
        return rotateBitmap;
    }

    public static Bitmap asBitmapStrictSize(ContentResolver contentResolver, Uri uri, int i, int i2) throws FileNotFoundException, IOException {
        ParcelFileDescriptor openFileDescriptor;
        if (uri == null || (openFileDescriptor = contentResolver.openFileDescriptor(uri, "r")) == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(openFileDescriptor.getFileDescriptor(), null, options);
        int calcSampleSize = calcSampleSize(options, i, i2);
        int MSB = 1 << BitsHelper.MSB(calcSampleSize);
        options.inSampleSize = MSB;
        options.inJustDecodeBounds = false;
        Bitmap decodeFileDescriptor = BitmapFactory.decodeFileDescriptor(openFileDescriptor.getFileDescriptor(), null, options);
        int orientation = getOrientation(contentResolver, uri);
        if (MSB == calcSampleSize && orientation == 0 && decodeFileDescriptor.getWidth() == i && decodeFileDescriptor.getHeight() == i2) {
            return decodeFileDescriptor;
        }
        Bitmap scaleRotateBitmap = scaleRotateBitmap(decodeFileDescriptor, i, i2, orientation);
        decodeFileDescriptor.recycle();
        return scaleRotateBitmap;
    }

    public static Bitmap asBitmap(InputStream inputStream) {
        if (inputStream != null) {
            return BitmapFactory.decodeStream(inputStream);
        }
        return null;
    }

    @Deprecated
    public static Bitmap asBitmap(InputStream inputStream, int i, int i2) {
        if (inputStream == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        Rect rect = new Rect();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, rect, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calcSampleSize(options, i, i2);
        return BitmapFactory.decodeStream(inputStream, rect, options);
    }

    @Deprecated
    public static Bitmap asBitmapStrictSize(InputStream inputStream, int i, int i2) {
        if (inputStream == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        Rect rect = new Rect();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, rect, options);
        int calcSampleSize = calcSampleSize(options, i, i2);
        int MSB = 1 << BitsHelper.MSB(calcSampleSize);
        options.inSampleSize = MSB;
        options.inJustDecodeBounds = false;
        Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, rect, options);
        if (MSB == calcSampleSize && decodeStream.getWidth() == i && decodeStream.getHeight() == i2) {
            return decodeStream;
        }
        Bitmap scaleBitmap = scaleBitmap(decodeStream, i, i2);
        decodeStream.recycle();
        return scaleBitmap;
    }

    public static int getOrientation(ContentResolver contentResolver, Uri uri) {
        try {
            int attributeInt = new ExifInterface(UriHelper.getAbsolutePath(contentResolver, uri)).getAttributeInt("Orientation", 0);
            if (attributeInt == 3) {
                return 180;
            }
            if (attributeInt == 6) {
                return 90;
            }
            if (attributeInt != 8) {
                return 0;
            }
            return VerticalSeekBar.ROTATION_ANGLE_CW_270;
        } catch (Exception unused) {
            return 0;
        }
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(width / i, height / i2);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int i) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(i);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap scaleRotateBitmap(Bitmap bitmap, int i, int i2, int i3) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(width / i, height / i2);
        matrix.postRotate(i3);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap extractBitmap(Bitmap bitmap, int i, int i2) {
        float f;
        int height;
        if (bitmap == null) {
            return null;
        }
        if (bitmap.getWidth() < bitmap.getHeight()) {
            f = i;
            height = bitmap.getWidth();
        } else {
            f = i2;
            height = bitmap.getHeight();
        }
        float f2 = f / height;
        Matrix matrix = new Matrix();
        matrix.setScale(f2, f2);
        return transform(matrix, bitmap, i, i2, 3);
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00c6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static Bitmap transform(Matrix r15, Bitmap r16, int r17, int r18, int r19) {
        /*
            Method dump skipped, instructions count: 242
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.graphics.BitmapHelper.transform(android.graphics.Matrix, android.graphics.Bitmap, int, int, int):android.graphics.Bitmap");
    }

    public static int calcSampleSize(BitmapFactory.Options options, int i, int i2) {
        double floor;
        int i3 = options.outWidth;
        int i4 = options.outHeight;
        if (i4 <= i2 && i3 <= i) {
            return 1;
        }
        if (i3 > i4) {
            floor = Math.floor(i4 / i2);
        } else {
            floor = Math.floor(i3 / i);
        }
        return (int) floor;
    }

    public static Bitmap copyBitmap(Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null) {
            throw new NullPointerException("src bitmap should not be null.");
        }
        if (bitmap2 == null) {
            return Bitmap.createBitmap(bitmap);
        }
        if (bitmap.equals(bitmap2)) {
            return bitmap2;
        }
        new Canvas(bitmap2).setBitmap(bitmap);
        return bitmap2;
    }

    public static Bitmap makeCheckBitmap() {
        Bitmap createBitmap = Bitmap.createBitmap(40, 40, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(-1);
        Paint paint = new Paint();
        paint.setColor(-3355444);
        canvas.drawRect(0.0f, 0.0f, 20.0f, 20.0f, paint);
        canvas.drawRect(20.0f, 20.0f, 40.0f, 40.0f, paint);
        return createBitmap;
    }
}
