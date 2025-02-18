package vi.com.gdi.bgl.android.java;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.SparseArray;
import com.baidu.mapapi.common.SysOSUtil;

/* loaded from: classes2.dex */
public class EnvDrawText {
    private static final String a = EnvDrawText.class.getSimpleName();
    public static Paint pt = null;
    public static int iWordWidthMax = 0;
    public static int iWordHightMax = 0;
    public static boolean bBmpChange = false;
    public static Bitmap bmp = null;
    public static Canvas canvasTemp = null;
    public static int[] buffer = null;
    public static SparseArray<a> fontCache = null;

    public static synchronized int[] drawText(String str, int i, int i2, int[] iArr, int i3, int i4, int i5, int i6) {
        Typeface create;
        Paint paint;
        int measureText;
        int[] iArr2;
        a aVar;
        synchronized (EnvDrawText.class) {
            if (pt == null) {
                pt = new Paint();
            } else {
                pt.reset();
            }
            String phoneType = SysOSUtil.getPhoneType();
            int i7 = (phoneType == null || !phoneType.equals("vivo X3L")) ? i2 : 0;
            if (i7 == 1) {
                create = Typeface.create(Typeface.DEFAULT, 1);
                paint = pt;
            } else if (i7 != 2) {
                create = Typeface.create(Typeface.DEFAULT, 0);
                paint = pt;
            } else {
                create = Typeface.create(Typeface.DEFAULT, 2);
                paint = pt;
            }
            paint.setTypeface(create);
            pt.setSubpixelText(true);
            pt.setAntiAlias(true);
            if (i7 != 0 && fontCache != null && (aVar = fontCache.get(i7)) != null) {
                pt.setTypeface(aVar.a);
            }
            pt.setTextSize(i);
            int i8 = 92;
            int indexOf = str.indexOf(92, 0);
            if (indexOf == -1) {
                Paint.FontMetrics fontMetrics = pt.getFontMetrics();
                int measureText2 = (int) pt.measureText(str);
                int ceil = (int) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
                iArr[0] = measureText2;
                iArr[1] = ceil;
                int pow = (int) Math.pow(2.0d, (int) Math.ceil(Math.log(measureText2) / Math.log(2.0d)));
                int pow2 = (int) Math.pow(2.0d, (int) Math.ceil(Math.log(ceil) / Math.log(2.0d)));
                if (iWordWidthMax < pow || iWordHightMax < pow2) {
                    bBmpChange = true;
                    iWordWidthMax = pow;
                    iWordHightMax = pow2;
                }
                iArr[2] = iWordWidthMax;
                iArr[3] = iWordHightMax;
                if (bBmpChange) {
                    if (bmp != null && !bmp.isRecycled()) {
                        bmp.recycle();
                    }
                    bmp = Bitmap.createBitmap(iWordWidthMax, iWordHightMax, Bitmap.Config.ARGB_8888);
                    if (canvasTemp == null) {
                        canvasTemp = new Canvas();
                    }
                    canvasTemp.setBitmap(bmp);
                } else {
                    bmp.eraseColor(0);
                }
                if (((-16777216) & i5) == 0) {
                    canvasTemp.drawColor(33554431);
                } else {
                    canvasTemp.drawColor(i5);
                }
                if (i6 != 0) {
                    pt.setStrokeWidth(i6);
                    pt.setStrokeCap(Paint.Cap.ROUND);
                    pt.setStrokeJoin(Paint.Join.ROUND);
                    pt.setStyle(Paint.Style.STROKE);
                    pt.setColor(i4);
                    canvasTemp.drawText(str, 0.0f, 0.0f - fontMetrics.ascent, pt);
                }
                pt.setStyle(Paint.Style.FILL);
                pt.setColor(i3);
                canvasTemp.drawText(str, 0.0f, 0.0f - fontMetrics.ascent, pt);
            } else {
                int i9 = indexOf + 1;
                int measureText3 = (int) pt.measureText(str.substring(0, indexOf));
                int i10 = 2;
                while (true) {
                    int indexOf2 = str.indexOf(i8, i9);
                    if (indexOf2 <= 0) {
                        break;
                    }
                    int measureText4 = (int) pt.measureText(str.substring(i9, indexOf2));
                    if (measureText4 > measureText3) {
                        measureText3 = measureText4;
                    }
                    i9 = indexOf2 + 1;
                    i10++;
                    i8 = 92;
                }
                if (i9 != str.length() && (measureText = (int) pt.measureText(str.substring(i9, str.length()))) > measureText3) {
                    measureText3 = measureText;
                }
                Paint.FontMetrics fontMetrics2 = pt.getFontMetrics();
                int ceil2 = i10 * ((int) Math.ceil(fontMetrics2.descent - fontMetrics2.ascent));
                iArr[0] = measureText3;
                iArr[1] = ceil2;
                int pow3 = (int) Math.pow(2.0d, (int) Math.ceil(Math.log(measureText3) / Math.log(2.0d)));
                int pow4 = (int) Math.pow(2.0d, (int) Math.ceil(Math.log(ceil2) / Math.log(2.0d)));
                if (iWordWidthMax < pow3 || iWordHightMax < pow4) {
                    bBmpChange = true;
                    iWordWidthMax = pow3;
                    iWordHightMax = pow4;
                }
                iArr[2] = iWordWidthMax;
                iArr[3] = iWordHightMax;
                if (bBmpChange) {
                    if (bmp != null && !bmp.isRecycled()) {
                        bmp.recycle();
                    }
                    bmp = Bitmap.createBitmap(iWordWidthMax, iWordHightMax, Bitmap.Config.ARGB_8888);
                    if (canvasTemp == null) {
                        canvasTemp = new Canvas();
                    }
                    canvasTemp.setBitmap(bmp);
                } else {
                    bmp.eraseColor(0);
                }
                if (((-16777216) & i5) == 0) {
                    canvasTemp.drawColor(33554431);
                } else {
                    canvasTemp.drawColor(i5);
                }
                int i11 = 0;
                int i12 = 92;
                int i13 = 0;
                while (true) {
                    int indexOf3 = str.indexOf(i12, i11);
                    if (indexOf3 <= 0) {
                        break;
                    }
                    String substring = str.substring(i11, indexOf3);
                    int measureText5 = (int) pt.measureText(substring);
                    int i14 = indexOf3 + 1;
                    if (i6 != 0) {
                        pt.setStrokeWidth(i6);
                        pt.setStrokeCap(Paint.Cap.ROUND);
                        pt.setStrokeJoin(Paint.Join.ROUND);
                        pt.setStyle(Paint.Style.STROKE);
                        pt.setColor(i4);
                        canvasTemp.drawText(substring, (iArr[0] - measureText5) / 2, (i13 * r11) - fontMetrics2.ascent, pt);
                    }
                    pt.setStyle(Paint.Style.FILL);
                    pt.setColor(i3);
                    canvasTemp.drawText(substring, (iArr[0] - measureText5) / 2, (i13 * r11) - fontMetrics2.ascent, pt);
                    i13++;
                    i11 = i14;
                    i12 = 92;
                }
                if (i11 != str.length()) {
                    String substring2 = str.substring(i11, str.length());
                    int measureText6 = (int) pt.measureText(substring2);
                    if (i6 != 0) {
                        pt.setStrokeWidth(i6);
                        pt.setStrokeCap(Paint.Cap.ROUND);
                        pt.setStrokeJoin(Paint.Join.ROUND);
                        pt.setStyle(Paint.Style.STROKE);
                        pt.setColor(i4);
                        canvasTemp.drawText(substring2, (iArr[0] - measureText6) / 2, (i13 * r11) - fontMetrics2.ascent, pt);
                    }
                    pt.setStyle(Paint.Style.FILL);
                    pt.setColor(i3);
                    canvasTemp.drawText(substring2, (iArr[0] - measureText6) / 2, (i13 * r11) - fontMetrics2.ascent, pt);
                }
            }
            int i15 = iWordWidthMax * iWordHightMax;
            if (bBmpChange) {
                buffer = new int[i15];
            }
            bmp.getPixels(buffer, 0, iWordWidthMax, 0, 0, iWordWidthMax, iWordHightMax);
            bBmpChange = false;
            iArr2 = buffer;
        }
        return iArr2;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0033 A[LOOP:0: B:11:0x0031->B:12:0x0033, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static short[] getTextSize(java.lang.String r5, int r6, int r7) {
        /*
            int r0 = r5.length()
            if (r0 != 0) goto L8
            r5 = 0
            return r5
        L8:
            android.graphics.Paint r1 = new android.graphics.Paint
            r1.<init>()
            r2 = 1
            r1.setSubpixelText(r2)
            r1.setAntiAlias(r2)
            float r6 = (float) r6
            r1.setTextSize(r6)
            r6 = 0
            if (r7 == r2) goto L25
            r2 = 2
            if (r7 == r2) goto L25
            android.graphics.Typeface r7 = android.graphics.Typeface.DEFAULT
            android.graphics.Typeface r7 = android.graphics.Typeface.create(r7, r6)
            goto L2b
        L25:
            android.graphics.Typeface r7 = android.graphics.Typeface.DEFAULT
            android.graphics.Typeface r7 = android.graphics.Typeface.create(r7, r2)
        L2b:
            r1.setTypeface(r7)
            short[] r7 = new short[r0]
            r2 = 0
        L31:
            if (r2 >= r0) goto L43
            int r3 = r2 + 1
            java.lang.String r4 = r5.substring(r6, r3)
            float r4 = r1.measureText(r4)
            int r4 = (int) r4
            short r4 = (short) r4
            r7[r2] = r4
            r2 = r3
            goto L31
        L43:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: vi.com.gdi.bgl.android.java.EnvDrawText.getTextSize(java.lang.String, int, int):short[]");
    }

    public static synchronized void registFontCache(int i, Typeface typeface) {
        synchronized (EnvDrawText.class) {
            if (i == 0 || typeface == null) {
                return;
            }
            if (fontCache == null) {
                fontCache = new SparseArray<>();
            }
            a aVar = fontCache.get(i);
            if (aVar == null) {
                a aVar2 = new a();
                aVar2.a = typeface;
                aVar2.b++;
                fontCache.put(i, aVar2);
            } else {
                aVar.b++;
            }
        }
    }

    public static synchronized void removeFontCache(int i) {
        synchronized (EnvDrawText.class) {
            a aVar = fontCache.get(i);
            if (aVar == null) {
                return;
            }
            aVar.b--;
            if (aVar.b == 0) {
                fontCache.remove(i);
            }
        }
    }
}
