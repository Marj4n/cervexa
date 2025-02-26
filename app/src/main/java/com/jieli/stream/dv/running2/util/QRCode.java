package com.jieli.stream.dv.running2.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.Hashtable;

/* loaded from: classes.dex */
public class QRCode {
    private static int IMAGE_HALFWIDTH = 50;

    public static Bitmap createQRCode(String str) {
        return createQRCode(str, 500);
    }

    public static Bitmap createQRCode(String str, int i) {
        try {
            Hashtable hashtable = new Hashtable();
            hashtable.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix encode = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, i, i, hashtable);
            int[] iArr = new int[i * i];
            for (int i2 = 0; i2 < i; i2++) {
                for (int i3 = 0; i3 < i; i3++) {
                    if (encode.get(i3, i2)) {
                        iArr[(i2 * i) + i3] = -16777216;
                    } else {
                        iArr[(i2 * i) + i3] = -1;
                    }
                }
            }
            Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
            createBitmap.setPixels(iArr, 0, i, 0, 0, i, i);
            return createBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createQRCodeWithLogo2(String str, int i, Bitmap bitmap) {
        try {
            IMAGE_HALFWIDTH = i / 10;
            Hashtable hashtable = new Hashtable();
            hashtable.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hashtable.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix encode = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, i, i, hashtable);
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, i, i, false);
            int[] iArr = new int[i * i];
            for (int i2 = 0; i2 < i; i2++) {
                for (int i3 = 0; i3 < i; i3++) {
                    if (encode.get(i3, i2)) {
                        iArr[(i2 * i) + i3] = createScaledBitmap.getPixel(i3, i2);
                    } else {
                        iArr[(i2 * i) + i3] = -1;
                    }
                }
            }
            Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
            createBitmap.setPixels(iArr, 0, i, 0, 0, i, i);
            return createBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createQRCodeWithLogo3(String str, int i, Bitmap bitmap) {
        try {
            IMAGE_HALFWIDTH = i / 10;
            Hashtable hashtable = new Hashtable();
            hashtable.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hashtable.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix encode = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, i, i, hashtable);
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, i, i, false);
            int[] iArr = new int[i * i];
            for (int i2 = 0; i2 < i; i2++) {
                for (int i3 = 0; i3 < i; i3++) {
                    if (encode.get(i3, i2)) {
                        iArr[(i2 * i) + i3] = -448714;
                    } else {
                        iArr[(i2 * i) + i3] = createScaledBitmap.getPixel(i3, i2) & 1728053247;
                    }
                }
            }
            Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
            createBitmap.setPixels(iArr, 0, i, 0, 0, i, i);
            return createBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createQRCodeWithLogo4(String str, int i, Bitmap bitmap) {
        try {
            IMAGE_HALFWIDTH = i / 10;
            Hashtable hashtable = new Hashtable();
            hashtable.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hashtable.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix encode = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, i, i, hashtable);
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, i, i, false);
            int[] iArr = new int[i * i];
            boolean z = true;
            for (int i2 = 0; i2 < i; i2++) {
                for (int i3 = 0; i3 < i; i3++) {
                    if (!encode.get(i3, i2)) {
                        iArr[(i2 * i) + i3] = -1;
                    } else if (z) {
                        iArr[(i2 * i) + i3] = -16777216;
                        z = false;
                    } else {
                        iArr[(i2 * i) + i3] = createScaledBitmap.getPixel(i3, i2);
                        z = true;
                    }
                }
            }
            Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
            createBitmap.setPixels(iArr, 0, i, 0, 0, i, i);
            return createBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createQRCodeWithLogo5(String str, int i, Bitmap bitmap) {
        try {
            IMAGE_HALFWIDTH = i / 10;
            Hashtable hashtable = new Hashtable();
            hashtable.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hashtable.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix encode = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, i, i, hashtable);
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, i, i, false);
            int width = encode.getWidth();
            int i2 = width / 2;
            int height = encode.getHeight() / 2;
            Matrix matrix = new Matrix();
            matrix.setScale((IMAGE_HALFWIDTH * 2.0f) / createScaledBitmap.getWidth(), (IMAGE_HALFWIDTH * 2.0f) / createScaledBitmap.getHeight());
            Bitmap createBitmap = Bitmap.createBitmap(createScaledBitmap, 0, 0, createScaledBitmap.getWidth(), createScaledBitmap.getHeight(), matrix, false);
            int[] iArr = new int[i * i];
            for (int i3 = 0; i3 < i; i3++) {
                for (int i4 = 0; i4 < i; i4++) {
                    if (i4 > i2 - IMAGE_HALFWIDTH && i4 < IMAGE_HALFWIDTH + i2 && i3 > height - IMAGE_HALFWIDTH && i3 < IMAGE_HALFWIDTH + height) {
                        iArr[(i3 * width) + i4] = createBitmap.getPixel((i4 - i2) + IMAGE_HALFWIDTH, (i3 - height) + IMAGE_HALFWIDTH);
                    } else if (encode.get(i4, i3)) {
                        iArr[(i3 * i) + i4] = -13127266;
                    } else {
                        iArr[(i3 * i) + i4] = -1;
                    }
                }
            }
            Bitmap createBitmap2 = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
            createBitmap2.setPixels(iArr, 0, i, 0, 0, i, i);
            return createBitmap2;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createQRCodeWithLogo6(String str, int i, Bitmap bitmap) {
        try {
            IMAGE_HALFWIDTH = i / 10;
            Hashtable hashtable = new Hashtable();
            hashtable.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hashtable.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix encode = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, i, i, hashtable);
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, i, i, false);
            int width = encode.getWidth();
            int i2 = width / 2;
            int height = encode.getHeight() / 2;
            Matrix matrix = new Matrix();
            matrix.setScale((IMAGE_HALFWIDTH * 2.0f) / createScaledBitmap.getWidth(), (IMAGE_HALFWIDTH * 2.0f) / createScaledBitmap.getHeight());
            Bitmap createBitmap = Bitmap.createBitmap(createScaledBitmap, 0, 0, createScaledBitmap.getWidth(), createScaledBitmap.getHeight(), matrix, false);
            int[] iArr = new int[i * i];
            for (int i3 = 0; i3 < i; i3++) {
                for (int i4 = 0; i4 < i; i4++) {
                    if (i4 > i2 - IMAGE_HALFWIDTH && i4 < IMAGE_HALFWIDTH + i2 && i3 > height - IMAGE_HALFWIDTH && i3 < IMAGE_HALFWIDTH + height) {
                        iArr[(i3 * width) + i4] = createBitmap.getPixel((i4 - i2) + IMAGE_HALFWIDTH, (i3 - height) + IMAGE_HALFWIDTH);
                    } else if (encode.get(i4, i3)) {
                        int i5 = (i3 * i) + i4;
                        iArr[i5] = -15658735;
                        if ((i4 < 115 && (i3 < 115 || i3 >= i - 115)) || (i3 < 115 && i4 >= i - 115)) {
                            iArr[i5] = -448714;
                        }
                    } else {
                        iArr[(i3 * i) + i4] = -1;
                    }
                }
            }
            Bitmap createBitmap2 = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
            createBitmap2.setPixels(iArr, 0, i, 0, 0, i, i);
            return createBitmap2;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
