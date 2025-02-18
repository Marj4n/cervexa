package com.serenegiant.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import com.serenegiant.graphics.BitmapHelper;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: classes.dex */
public class SQLiteBlobHelper {
    public static byte[] floatArrayToByteArray(float[] fArr, int i, int i2) {
        ByteBuffer allocate = ByteBuffer.allocate((i2 * 32) / 8);
        allocate.order(ByteOrder.nativeOrder());
        int i3 = (i2 % 8) + i;
        int i4 = i2 + i;
        while (i < i3) {
            allocate.putFloat(fArr[i]);
            i++;
        }
        while (i3 < i4) {
            allocate.putFloat(fArr[i3]);
            allocate.putFloat(fArr[i3 + 1]);
            allocate.putFloat(fArr[i3 + 2]);
            allocate.putFloat(fArr[i3 + 3]);
            allocate.putFloat(fArr[i3 + 4]);
            allocate.putFloat(fArr[i3 + 5]);
            allocate.putFloat(fArr[i3 + 6]);
            allocate.putFloat(fArr[i3 + 7]);
            i3 += 8;
        }
        allocate.flip();
        return allocate.array();
    }

    public static float[] byteArrayToFloatArray(byte[] bArr) {
        if (bArr == null || bArr.length < 4) {
            return null;
        }
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        wrap.order(ByteOrder.nativeOrder());
        int limit = wrap.limit() / 4;
        float[] fArr = new float[limit];
        int i = limit % 8;
        for (int i2 = 0; i2 < i; i2++) {
            fArr[i2] = wrap.getFloat();
        }
        while (i < limit) {
            fArr[i] = wrap.getFloat();
            fArr[i + 1] = wrap.getFloat();
            fArr[i + 2] = wrap.getFloat();
            fArr[i + 3] = wrap.getFloat();
            fArr[i + 4] = wrap.getFloat();
            fArr[i + 5] = wrap.getFloat();
            fArr[i + 6] = wrap.getFloat();
            fArr[i + 7] = wrap.getFloat();
            i += 8;
        }
        return fArr;
    }

    public static byte[] doubleArrayToByteArray(double[] dArr, int i, int i2) {
        ByteBuffer allocate = ByteBuffer.allocate((i2 * 64) / 8);
        allocate.order(ByteOrder.nativeOrder());
        int i3 = (i2 % 8) + i;
        int i4 = i2 + i;
        while (i < i3) {
            allocate.putDouble(dArr[i]);
            i++;
        }
        while (i3 < i4) {
            allocate.putDouble(dArr[i3]);
            allocate.putDouble(dArr[i3 + 1]);
            allocate.putDouble(dArr[i3 + 2]);
            allocate.putDouble(dArr[i3 + 3]);
            allocate.putDouble(dArr[i3 + 4]);
            allocate.putDouble(dArr[i3 + 5]);
            allocate.putDouble(dArr[i3 + 6]);
            allocate.putDouble(dArr[i3 + 7]);
            i3 += 8;
        }
        allocate.flip();
        return allocate.array();
    }

    public static double[] byteArrayToDoubleArray(byte[] bArr) {
        if (bArr == null || bArr.length < 8) {
            return null;
        }
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        wrap.order(ByteOrder.nativeOrder());
        int limit = wrap.limit() / 8;
        double[] dArr = new double[limit];
        int i = limit % 8;
        for (int i2 = 0; i2 < i; i2++) {
            dArr[i2] = wrap.getDouble();
        }
        while (i < limit) {
            dArr[i] = wrap.getDouble();
            dArr[i + 1] = wrap.getDouble();
            dArr[i + 2] = wrap.getDouble();
            dArr[i + 3] = wrap.getDouble();
            dArr[i + 4] = wrap.getDouble();
            dArr[i + 5] = wrap.getDouble();
            dArr[i + 6] = wrap.getDouble();
            dArr[i + 7] = wrap.getDouble();
            i += 8;
        }
        return dArr;
    }

    public static byte[] intArrayToByteArray(int[] iArr, int i, int i2) {
        ByteBuffer allocate = ByteBuffer.allocate((i2 * 32) / 8);
        allocate.order(ByteOrder.nativeOrder());
        int i3 = (i2 % 8) + i;
        int i4 = i2 + i;
        while (i < i3) {
            allocate.putInt(iArr[i]);
            i++;
        }
        while (i3 < i4) {
            allocate.putInt(iArr[i3]);
            allocate.putInt(iArr[i3 + 1]);
            allocate.putInt(iArr[i3 + 2]);
            allocate.putInt(iArr[i3 + 3]);
            allocate.putInt(iArr[i3 + 4]);
            allocate.putInt(iArr[i3 + 5]);
            allocate.putInt(iArr[i3 + 6]);
            allocate.putInt(iArr[i3 + 7]);
            i3 += 8;
        }
        allocate.flip();
        return allocate.array();
    }

    public static int[] byteArrayToIntArray(byte[] bArr) {
        if (bArr == null || bArr.length < 4) {
            return null;
        }
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        wrap.order(ByteOrder.nativeOrder());
        int limit = wrap.limit() / 4;
        int[] iArr = new int[limit];
        int i = limit % 8;
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = wrap.getInt();
        }
        while (i < limit) {
            iArr[i] = wrap.getInt();
            iArr[i + 1] = wrap.getInt();
            iArr[i + 2] = wrap.getInt();
            iArr[i + 3] = wrap.getInt();
            iArr[i + 4] = wrap.getInt();
            iArr[i + 5] = wrap.getInt();
            iArr[i + 6] = wrap.getInt();
            iArr[i + 7] = wrap.getInt();
            i += 8;
        }
        return iArr;
    }

    public static byte[] shortArrayToByteArray(short[] sArr, int i, int i2) {
        ByteBuffer allocate = ByteBuffer.allocate((i2 * 16) / 8);
        allocate.order(ByteOrder.nativeOrder());
        int i3 = (i2 % 8) + i;
        int i4 = i2 + i;
        while (i < i3) {
            allocate.putShort(sArr[i]);
            i++;
        }
        while (i3 < i4) {
            allocate.putShort(sArr[i3]);
            allocate.putShort(sArr[i3 + 1]);
            allocate.putShort(sArr[i3 + 2]);
            allocate.putShort(sArr[i3 + 3]);
            allocate.putShort(sArr[i3 + 4]);
            allocate.putShort(sArr[i3 + 5]);
            allocate.putShort(sArr[i3 + 6]);
            allocate.putShort(sArr[i3 + 7]);
            i3 += 8;
        }
        allocate.flip();
        return allocate.array();
    }

    public static short[] byteArrayToShortArray(byte[] bArr) {
        if (bArr == null || bArr.length < 2) {
            return null;
        }
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        wrap.order(ByteOrder.nativeOrder());
        int limit = wrap.limit() / 2;
        short[] sArr = new short[limit];
        int i = limit % 8;
        for (int i2 = 0; i2 < i; i2++) {
            sArr[i2] = wrap.getShort();
        }
        while (i < limit) {
            sArr[i] = wrap.getShort();
            sArr[i + 1] = wrap.getShort();
            sArr[i + 2] = wrap.getShort();
            sArr[i + 3] = wrap.getShort();
            sArr[i + 4] = wrap.getShort();
            sArr[i + 5] = wrap.getShort();
            sArr[i + 6] = wrap.getShort();
            sArr[i + 7] = wrap.getShort();
            i += 8;
        }
        return sArr;
    }

    public static byte[] longArrayToByteArray(long[] jArr, int i, int i2) {
        ByteBuffer allocate = ByteBuffer.allocate((i2 * 64) / 8);
        allocate.order(ByteOrder.nativeOrder());
        int i3 = (i2 % 8) + i;
        int i4 = i2 + i;
        while (i < i3) {
            allocate.putLong(jArr[i]);
            i++;
        }
        while (i3 < i4) {
            allocate.putLong(jArr[i3]);
            allocate.putLong(jArr[i3 + 1]);
            allocate.putLong(jArr[i3 + 2]);
            allocate.putLong(jArr[i3 + 3]);
            allocate.putLong(jArr[i3 + 4]);
            allocate.putLong(jArr[i3 + 5]);
            allocate.putLong(jArr[i3 + 6]);
            allocate.putLong(jArr[i3 + 7]);
            i3 += 8;
        }
        allocate.flip();
        return allocate.array();
    }

    public static long[] byteArrayToLongArray(byte[] bArr) {
        if (bArr == null || bArr.length < 8) {
            return null;
        }
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        wrap.order(ByteOrder.nativeOrder());
        int limit = wrap.limit() / 8;
        long[] jArr = new long[limit];
        int i = limit % 8;
        for (int i2 = 0; i2 < i; i2++) {
            jArr[i2] = wrap.getLong();
        }
        while (i < limit) {
            jArr[i] = wrap.getLong();
            jArr[i + 1] = wrap.getLong();
            jArr[i + 2] = wrap.getLong();
            jArr[i + 3] = wrap.getLong();
            jArr[i + 4] = wrap.getLong();
            jArr[i + 5] = wrap.getLong();
            jArr[i + 6] = wrap.getLong();
            jArr[i + 7] = wrap.getLong();
            i += 8;
        }
        return jArr;
    }

    public static void bindBlobFloatArray(SQLiteStatement sQLiteStatement, int i, float[] fArr) {
        sQLiteStatement.bindBlob(i, floatArrayToByteArray(fArr, 0, fArr.length));
    }

    public static void bindBlobFloatArray(SQLiteStatement sQLiteStatement, int i, float[] fArr, int i2, int i3) {
        sQLiteStatement.bindBlob(i, floatArrayToByteArray(fArr, i2, i3));
    }

    public static void bindBlobDoubleArray(SQLiteStatement sQLiteStatement, int i, double[] dArr) {
        sQLiteStatement.bindBlob(i, doubleArrayToByteArray(dArr, 0, dArr.length));
    }

    public static void bindBlobDoubleArray(SQLiteStatement sQLiteStatement, int i, double[] dArr, int i2, int i3) {
        sQLiteStatement.bindBlob(i, doubleArrayToByteArray(dArr, i2, i3));
    }

    public static void bindBlobIntArray(SQLiteStatement sQLiteStatement, int i, int[] iArr) {
        sQLiteStatement.bindBlob(i, intArrayToByteArray(iArr, 0, iArr.length));
    }

    public static void bindBlobIntArray(SQLiteStatement sQLiteStatement, int i, int[] iArr, int i2, int i3) {
        sQLiteStatement.bindBlob(i, intArrayToByteArray(iArr, i2, i3));
    }

    public static void bindBlobShortArray(SQLiteStatement sQLiteStatement, int i, short[] sArr) {
        sQLiteStatement.bindBlob(i, shortArrayToByteArray(sArr, 0, sArr.length));
    }

    public static void bindBlobShortArray(SQLiteStatement sQLiteStatement, int i, short[] sArr, int i2, int i3) {
        sQLiteStatement.bindBlob(i, shortArrayToByteArray(sArr, i2, i3));
    }

    public static void bindBlobLongArray(SQLiteStatement sQLiteStatement, int i, long[] jArr) {
        sQLiteStatement.bindBlob(i, longArrayToByteArray(jArr, 0, jArr.length));
    }

    public static void bindBlobLongArray(SQLiteStatement sQLiteStatement, int i, long[] jArr, int i2, int i3) {
        sQLiteStatement.bindBlob(i, longArrayToByteArray(jArr, i2, i3));
    }

    public static void bindBlobBitmap(SQLiteStatement sQLiteStatement, int i, Bitmap bitmap) {
        sQLiteStatement.bindBlob(i, BitmapHelper.BitmapToByteArray(bitmap));
    }

    public static float[] getBlobFloatArray(Cursor cursor, int i) {
        return byteArrayToFloatArray(cursor.getBlob(i));
    }

    public static float[] getBlobFloatArray(Cursor cursor, String str, float[] fArr) {
        float[] byteArrayToFloatArray = byteArrayToFloatArray(getBlob(cursor, str, null));
        return byteArrayToFloatArray == null ? fArr : byteArrayToFloatArray;
    }

    public static double[] getBlobDoubleArray(Cursor cursor, int i) {
        return byteArrayToDoubleArray(cursor.getBlob(i));
    }

    public static double[] getBlobDoubleArray(Cursor cursor, String str, double[] dArr) {
        double[] byteArrayToDoubleArray = byteArrayToDoubleArray(getBlob(cursor, str, null));
        return byteArrayToDoubleArray == null ? dArr : byteArrayToDoubleArray;
    }

    public static byte[] getBlob(Cursor cursor, String str, byte[] bArr) {
        try {
            return cursor.getBlob(cursor.getColumnIndexOrThrow(str));
        } catch (Exception unused) {
            return bArr;
        }
    }

    public static int[] getBlobIntArray(Cursor cursor, int i) {
        return byteArrayToIntArray(cursor.getBlob(i));
    }

    public static int[] getBlobIntArray(Cursor cursor, String str, int[] iArr) {
        int[] byteArrayToIntArray = byteArrayToIntArray(getBlob(cursor, str, null));
        return byteArrayToIntArray == null ? iArr : byteArrayToIntArray;
    }

    public static short[] getBlobShortArray(Cursor cursor, int i) {
        return byteArrayToShortArray(cursor.getBlob(i));
    }

    public static short[] getBlobShortArray(Cursor cursor, String str, short[] sArr) {
        short[] byteArrayToShortArray = byteArrayToShortArray(getBlob(cursor, str, null));
        return byteArrayToShortArray == null ? sArr : byteArrayToShortArray;
    }

    public static long[] getBlobLongArray(Cursor cursor, int i) {
        return byteArrayToLongArray(cursor.getBlob(i));
    }

    public static long[] getBlobLongArray(Cursor cursor, String str, long[] jArr) {
        long[] byteArrayToLongArray = byteArrayToLongArray(getBlob(cursor, str, null));
        return byteArrayToLongArray == null ? jArr : byteArrayToLongArray;
    }

    public static Bitmap getBlobBitmap(Cursor cursor, int i) {
        return BitmapHelper.asBitmap(cursor.getBlob(i));
    }

    public static Bitmap getBlobBitmap(Cursor cursor, String str) {
        return BitmapHelper.asBitmap(getBlob(cursor, str, null));
    }

    public static Bitmap getBlobBitmap(Cursor cursor, int i, int i2, int i3) {
        return BitmapHelper.asBitmap(cursor.getBlob(i), i2, i3);
    }

    public static Bitmap getBlobBitmap(Cursor cursor, String str, int i, int i2) {
        return BitmapHelper.asBitmap(getBlob(cursor, str, null), i, i2);
    }

    public static Bitmap getBlobBitmapStrictSize(Cursor cursor, int i, int i2, int i3) {
        return BitmapHelper.asBitmapStrictSize(cursor.getBlob(i), i2, i3);
    }

    public static Bitmap getBlobBitmapStrictSize(Cursor cursor, String str, int i, int i2) {
        return BitmapHelper.asBitmapStrictSize(getBlob(cursor, str, null), i, i2);
    }
}
