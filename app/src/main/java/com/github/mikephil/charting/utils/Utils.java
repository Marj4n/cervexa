package com.github.mikephil.charting.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import java.util.List;

/* loaded from: classes.dex */
public abstract class Utils {
    public static final double DEG2RAD = 0.017453292519943295d;
    public static final float FDEG2RAD = 0.017453292f;
    private static int mMaximumFlingVelocity = 8000;
    private static DisplayMetrics mMetrics = null;
    private static int mMinimumFlingVelocity = 50;
    public static final double DOUBLE_EPSILON = Double.longBitsToDouble(1);
    public static final float FLOAT_EPSILON = Float.intBitsToFloat(1);
    private static Rect mCalcTextHeightRect = new Rect();
    private static Paint.FontMetrics mFontMetrics = new Paint.FontMetrics();
    private static Rect mCalcTextSizeRect = new Rect();
    private static final int[] POW_10 = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};
    private static IValueFormatter mDefaultValueFormatter = generateDefaultValueFormatter();
    private static Rect mDrawTextRectBuffer = new Rect();
    private static Paint.FontMetrics mFontMetricsBuffer = new Paint.FontMetrics();

    public static float getNormalizedAngle(float f) {
        while (f < 0.0f) {
            f += 360.0f;
        }
        return f % 360.0f;
    }

    public static void init(Context context) {
        if (context == null) {
            mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
            mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
            Log.e("MPChartLib-Utils", "Utils.init(...) PROVIDED CONTEXT OBJECT IS NULL");
        } else {
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
            mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
            mMetrics = context.getResources().getDisplayMetrics();
        }
    }

    @Deprecated
    public static void init(Resources resources) {
        mMetrics = resources.getDisplayMetrics();
        mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
        mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
    }

    public static float convertDpToPixel(float f) {
        if (mMetrics == null) {
            Log.e("MPChartLib-Utils", "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before calling Utils.convertDpToPixel(...). Otherwise conversion does not take place.");
            return f;
        }
        return f * (r0.densityDpi / 160.0f);
    }

    public static float convertPixelsToDp(float f) {
        if (mMetrics == null) {
            Log.e("MPChartLib-Utils", "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before calling Utils.convertPixelsToDp(...). Otherwise conversion does not take place.");
            return f;
        }
        return f / (r0.densityDpi / 160.0f);
    }

    public static int calcTextWidth(Paint paint, String str) {
        return (int) paint.measureText(str);
    }

    public static int calcTextHeight(Paint paint, String str) {
        Rect rect = mCalcTextHeightRect;
        rect.set(0, 0, 0, 0);
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.height();
    }

    public static float getLineHeight(Paint paint) {
        return getLineHeight(paint, mFontMetrics);
    }

    public static float getLineHeight(Paint paint, Paint.FontMetrics fontMetrics) {
        paint.getFontMetrics(fontMetrics);
        return fontMetrics.descent - fontMetrics.ascent;
    }

    public static float getLineSpacing(Paint paint) {
        return getLineSpacing(paint, mFontMetrics);
    }

    public static float getLineSpacing(Paint paint, Paint.FontMetrics fontMetrics) {
        paint.getFontMetrics(fontMetrics);
        return (fontMetrics.ascent - fontMetrics.top) + fontMetrics.bottom;
    }

    public static FSize calcTextSize(Paint paint, String str) {
        FSize fSize = FSize.getInstance(0.0f, 0.0f);
        calcTextSize(paint, str, fSize);
        return fSize;
    }

    public static void calcTextSize(Paint paint, String str, FSize fSize) {
        Rect rect = mCalcTextSizeRect;
        rect.set(0, 0, 0, 0);
        paint.getTextBounds(str, 0, str.length(), rect);
        fSize.width = rect.width();
        fSize.height = rect.height();
    }

    private static IValueFormatter generateDefaultValueFormatter() {
        return new DefaultValueFormatter(1);
    }

    public static IValueFormatter getDefaultValueFormatter() {
        return mDefaultValueFormatter;
    }

    public static String formatNumber(float f, int i, boolean z) {
        return formatNumber(f, i, z, '.');
    }

    public static String formatNumber(float f, int i, boolean z, char c) {
        boolean z2;
        float f2 = f;
        char[] cArr = new char[35];
        if (f2 == 0.0f) {
            return "0";
        }
        int i2 = 0;
        boolean z3 = f2 < 1.0f && f2 > -1.0f;
        if (f2 < 0.0f) {
            f2 = -f2;
            z2 = true;
        } else {
            z2 = false;
        }
        int[] iArr = POW_10;
        int length = i > iArr.length ? iArr.length - 1 : i;
        long round = Math.round(f2 * POW_10[length]);
        int i3 = 34;
        long j = round;
        boolean z4 = false;
        while (true) {
            if (j == 0 && i2 >= length + 1) {
                break;
            }
            int i4 = (int) (j % 10);
            j /= 10;
            int i5 = i3 - 1;
            cArr[i3] = (char) (i4 + 48);
            i2++;
            if (i2 == length) {
                i3 = i5 - 1;
                cArr[i5] = ',';
                i2++;
                z4 = true;
            } else {
                if (z && j != 0 && i2 > length) {
                    if (z4) {
                        if ((i2 - length) % 4 == 0) {
                            i3 = i5 - 1;
                            cArr[i5] = c;
                            i2++;
                        }
                    } else if ((i2 - length) % 4 == 3) {
                        i3 = i5 - 1;
                        cArr[i5] = c;
                        i2++;
                    }
                }
                i3 = i5;
            }
        }
        if (z3) {
            cArr[i3] = '0';
            i2++;
            i3--;
        }
        if (z2) {
            cArr[i3] = '-';
            i2++;
        }
        int i6 = 35 - i2;
        return String.valueOf(cArr, i6, 35 - i6);
    }

    public static float roundToNextSignificant(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d) || d == DOUBLE_EPSILON) {
            return 0.0f;
        }
        return Math.round(d * r0) / ((float) Math.pow(10.0d, 1 - ((int) Math.ceil((float) Math.log10(d < DOUBLE_EPSILON ? -d : d)))));
    }

    public static int getDecimals(float f) {
        float roundToNextSignificant = roundToNextSignificant(f);
        if (Float.isInfinite(roundToNextSignificant)) {
            return 0;
        }
        return ((int) Math.ceil(-Math.log10(roundToNextSignificant))) + 2;
    }

    public static int[] convertIntegers(List<Integer> list) {
        int[] iArr = new int[list.size()];
        copyIntegers(list, iArr);
        return iArr;
    }

    public static void copyIntegers(List<Integer> list, int[] iArr) {
        int length = iArr.length < list.size() ? iArr.length : list.size();
        for (int i = 0; i < length; i++) {
            iArr[i] = list.get(i).intValue();
        }
    }

    public static String[] convertStrings(List<String> list) {
        int size = list.size();
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            strArr[i] = list.get(i);
        }
        return strArr;
    }

    public static void copyStrings(List<String> list, String[] strArr) {
        int length = strArr.length < list.size() ? strArr.length : list.size();
        for (int i = 0; i < length; i++) {
            strArr[i] = list.get(i);
        }
    }

    public static double nextUp(double d) {
        if (d == Double.POSITIVE_INFINITY) {
            return d;
        }
        double d2 = d + DOUBLE_EPSILON;
        return Double.longBitsToDouble(Double.doubleToRawLongBits(d2) + (d2 >= DOUBLE_EPSILON ? 1L : -1L));
    }

    public static MPPointF getPosition(MPPointF mPPointF, float f, float f2) {
        MPPointF mPPointF2 = MPPointF.getInstance(0.0f, 0.0f);
        getPosition(mPPointF, f, f2, mPPointF2);
        return mPPointF2;
    }

    public static void getPosition(MPPointF mPPointF, float f, float f2, MPPointF mPPointF2) {
        double d = f;
        double d2 = f2;
        mPPointF2.x = (float) (mPPointF.x + (Math.cos(Math.toRadians(d2)) * d));
        mPPointF2.y = (float) (mPPointF.y + (d * Math.sin(Math.toRadians(d2))));
    }

    public static void velocityTrackerPointerUpCleanUpIfNecessary(MotionEvent motionEvent, VelocityTracker velocityTracker) {
        velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
        int actionIndex = motionEvent.getActionIndex();
        int pointerId = motionEvent.getPointerId(actionIndex);
        float xVelocity = velocityTracker.getXVelocity(pointerId);
        float yVelocity = velocityTracker.getYVelocity(pointerId);
        int pointerCount = motionEvent.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            if (i != actionIndex) {
                int pointerId2 = motionEvent.getPointerId(i);
                if ((velocityTracker.getXVelocity(pointerId2) * xVelocity) + (velocityTracker.getYVelocity(pointerId2) * yVelocity) < 0.0f) {
                    velocityTracker.clear();
                    return;
                }
            }
        }
    }

    public static void postInvalidateOnAnimation(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.postInvalidateOnAnimation();
        } else {
            view.postInvalidateDelayed(10L);
        }
    }

    public static int getMinimumFlingVelocity() {
        return mMinimumFlingVelocity;
    }

    public static int getMaximumFlingVelocity() {
        return mMaximumFlingVelocity;
    }

    public static void drawXAxisValue(Canvas canvas, String str, float f, float f2, Paint paint, MPPointF mPPointF, float f3) {
        float fontMetrics = paint.getFontMetrics(mFontMetricsBuffer);
        paint.getTextBounds(str, 0, str.length(), mDrawTextRectBuffer);
        float f4 = 0.0f - mDrawTextRectBuffer.left;
        float f5 = (-mFontMetricsBuffer.ascent) + 0.0f;
        Paint.Align textAlign = paint.getTextAlign();
        paint.setTextAlign(Paint.Align.LEFT);
        if (f3 != 0.0f) {
            float width = f4 - (mDrawTextRectBuffer.width() * 0.5f);
            float f6 = f5 - (fontMetrics * 0.5f);
            if (mPPointF.x != 0.5f || mPPointF.y != 0.5f) {
                FSize sizeOfRotatedRectangleByDegrees = getSizeOfRotatedRectangleByDegrees(mDrawTextRectBuffer.width(), fontMetrics, f3);
                f -= sizeOfRotatedRectangleByDegrees.width * (mPPointF.x - 0.5f);
                f2 -= sizeOfRotatedRectangleByDegrees.height * (mPPointF.y - 0.5f);
                FSize.recycleInstance(sizeOfRotatedRectangleByDegrees);
            }
            canvas.save();
            canvas.translate(f, f2);
            canvas.rotate(f3);
            canvas.drawText(str, width, f6, paint);
            canvas.restore();
        } else {
            if (mPPointF.x != 0.0f || mPPointF.y != 0.0f) {
                f4 -= mDrawTextRectBuffer.width() * mPPointF.x;
                f5 -= fontMetrics * mPPointF.y;
            }
            canvas.drawText(str, f4 + f, f5 + f2, paint);
        }
        paint.setTextAlign(textAlign);
    }

    public static void drawMultilineText(Canvas canvas, StaticLayout staticLayout, float f, float f2, TextPaint textPaint, MPPointF mPPointF, float f3) {
        float fontMetrics = textPaint.getFontMetrics(mFontMetricsBuffer);
        float width = staticLayout.getWidth();
        float lineCount = staticLayout.getLineCount() * fontMetrics;
        float f4 = 0.0f - mDrawTextRectBuffer.left;
        float f5 = lineCount + 0.0f;
        Paint.Align textAlign = textPaint.getTextAlign();
        textPaint.setTextAlign(Paint.Align.LEFT);
        if (f3 != 0.0f) {
            float f6 = f4 - (width * 0.5f);
            float f7 = f5 - (lineCount * 0.5f);
            if (mPPointF.x != 0.5f || mPPointF.y != 0.5f) {
                FSize sizeOfRotatedRectangleByDegrees = getSizeOfRotatedRectangleByDegrees(width, lineCount, f3);
                f -= sizeOfRotatedRectangleByDegrees.width * (mPPointF.x - 0.5f);
                f2 -= sizeOfRotatedRectangleByDegrees.height * (mPPointF.y - 0.5f);
                FSize.recycleInstance(sizeOfRotatedRectangleByDegrees);
            }
            canvas.save();
            canvas.translate(f, f2);
            canvas.rotate(f3);
            canvas.translate(f6, f7);
            staticLayout.draw(canvas);
            canvas.restore();
        } else {
            if (mPPointF.x != 0.0f || mPPointF.y != 0.0f) {
                f4 -= width * mPPointF.x;
                f5 -= lineCount * mPPointF.y;
            }
            canvas.save();
            canvas.translate(f4 + f, f5 + f2);
            staticLayout.draw(canvas);
            canvas.restore();
        }
        textPaint.setTextAlign(textAlign);
    }

    public static void drawMultilineText(Canvas canvas, String str, float f, float f2, TextPaint textPaint, FSize fSize, MPPointF mPPointF, float f3) {
        drawMultilineText(canvas, new StaticLayout(str, 0, str.length(), textPaint, (int) Math.max(Math.ceil(fSize.width), 1.0d), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false), f, f2, textPaint, mPPointF, f3);
    }

    public static FSize getSizeOfRotatedRectangleByDegrees(FSize fSize, float f) {
        return getSizeOfRotatedRectangleByRadians(fSize.width, fSize.height, f * 0.017453292f);
    }

    public static FSize getSizeOfRotatedRectangleByRadians(FSize fSize, float f) {
        return getSizeOfRotatedRectangleByRadians(fSize.width, fSize.height, f);
    }

    public static FSize getSizeOfRotatedRectangleByDegrees(float f, float f2, float f3) {
        return getSizeOfRotatedRectangleByRadians(f, f2, f3 * 0.017453292f);
    }

    public static FSize getSizeOfRotatedRectangleByRadians(float f, float f2, float f3) {
        double d = f3;
        return FSize.getInstance(Math.abs(((float) Math.cos(d)) * f) + Math.abs(((float) Math.sin(d)) * f2), Math.abs(f * ((float) Math.sin(d))) + Math.abs(f2 * ((float) Math.cos(d))));
    }

    public static int getSDKInt() {
        return Build.VERSION.SDK_INT;
    }
}
