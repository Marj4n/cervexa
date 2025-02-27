package com.serenegiant.widget;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.view.ViewCompat;

import com.serenegiant.graphics.BitmapHelper;
import com.serenegiant.graphics.ShaderDrawable;

/* loaded from: classes2.dex */
public class ColorPickerView extends View {
    private static final int BORDER_COLOR = -9539986;
    private static final float BORDER_WIDTH_PX = 1.0f;
    private static final int DEFAULT_HEIGHT = 100;
    private static final int DEFAULT_SELECTED_RADIUS = 8;
    private static final int DEFAULT_WIDTH = 100;
    private static final float PI = 3.1415927f;
    private static final float RECTANGLE_TRACKER_OFFSET_DP = 2.0f;
    private static final float SQRT2 = (float) Math.sqrt(2.0d);
    private static final int STATE_ALPHA = 2;
    private static final int STATE_COLOR = 1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_VAL = 3;
    private static final int TRACKER_COLOR = -14935012;
    private final int[] COLORS;
    private final float DENSITY;
    private final float[] HSV;
    private final float RECTANGLE_TRACKER_OFFSET;
    private final float SELECTED_RADIUS;
    private int center_x;
    private int center_y;
    private int mAlpha;
    private final ShaderDrawable mAlphaDrawable;
    private final Paint mAlphaPaint;
    private final RectF mAlphaRect;
    private final Shader mAlphaShader;
    private final Paint mBorderPaint;
    private int mColor;
    private ColorPickerListener mColorPickerListener;
    private final RectF mDrawingRect;
    private final Paint mGradientPaint;
    private float mHue;
    private final Paint mPaint;
    private float mSat;
    private final PointF mSelected;
    private final Paint mSelectionPaint;
    private final RectF mSelectionRect;
    private boolean mShowAlphaSlider;
    private boolean mShowSelectedColor;
    private boolean mShowValSlider;
    private final RectF mSliderRect;
    private int mState;
    private final Paint mTrackerPaint;
    private float mVal;
    private final Paint mValPaint;
    private final RectF mValRect;
    private int radius;
    private int radius2;
    private int slider_width;

    public interface ColorPickerListener {
        void onColorChanged(ColorPickerView colorPickerView, int i);
    }

    public ColorPickerView(Context context) {
        this(context, null, 0);
    }

    public ColorPickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ColorPickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mShowAlphaSlider = true;
        this.mShowValSlider = true;
        this.mShowSelectedColor = true;
        this.COLORS = new int[360];
        this.HSV = new float[3];
        this.mDrawingRect = new RectF();
        this.mBorderPaint = new Paint();
        this.mTrackerPaint = new Paint();
        this.mPaint = new Paint(1);
        this.mGradientPaint = new Paint(1);
        this.mSelectionPaint = new Paint(1);
        this.mSelectionRect = new RectF();
        this.mSelected = new PointF();
        this.mSliderRect = new RectF();
        this.mAlphaPaint = new Paint();
        this.mAlphaRect = new RectF();
        this.mValPaint = new Paint();
        this.mValRect = new RectF();
        this.mState = 0;
        this.slider_width = 32;
        this.mColor = -1;
        this.mAlpha = 255;
        this.mVal = 0.0f;
        this.mHue = 360.0f;
        this.mSat = 0.0f;
        float f = context.getResources().getDisplayMetrics().density;
        this.DENSITY = f;
        this.RECTANGLE_TRACKER_OFFSET = f * RECTANGLE_TRACKER_OFFSET_DP;
        this.SELECTED_RADIUS = f * 8.0f;
        this.mAlphaShader = new BitmapShader(BitmapHelper.makeCheckBitmap(), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        ShaderDrawable shaderDrawable = new ShaderDrawable(6, 0);
        this.mAlphaDrawable = shaderDrawable;
        shaderDrawable.setShader(this.mAlphaShader);
        this.radius = 0;
        internalSetColor(this.mColor, false);
        setHueColorArray(this.mAlpha, this.COLORS);
        this.mPaint.setShader(new SweepGradient(0.0f, 0.0f, this.COLORS, (float[]) null));
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setStrokeWidth(0.0f);
        this.mSelectionPaint.setColor(this.mColor);
        this.mSelectionPaint.setStrokeWidth(5.0f);
        this.mTrackerPaint.setColor(TRACKER_COLOR);
        this.mTrackerPaint.setStyle(Paint.Style.STROKE);
        this.mTrackerPaint.setStrokeWidth(this.DENSITY * RECTANGLE_TRACKER_OFFSET_DP);
        this.mTrackerPaint.setAntiAlias(true);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        this.mSelectionPaint.setStyle(Paint.Style.FILL);
        if (this.mShowSelectedColor) {
            this.mSelectionPaint.setShader(this.mAlphaShader);
            canvas.drawArc(this.mSelectionRect, 0.0f, 90.0f, true, this.mSelectionPaint);
            this.mSelectionPaint.setShader(null);
            this.mSelectionPaint.setColor(this.mColor);
            canvas.drawArc(this.mSelectionRect, 0.0f, 90.0f, true, this.mSelectionPaint);
        }
        int save = canvas.save();
        try {
            canvas.translate(this.center_x, this.center_y);
            this.mSelectionPaint.setShader(this.mAlphaShader);
            canvas.drawCircle(0.0f, 0.0f, this.radius, this.mSelectionPaint);
            canvas.drawCircle(0.0f, 0.0f, this.radius, this.mPaint);
            canvas.drawCircle(0.0f, 0.0f, this.radius, this.mGradientPaint);
            canvas.restoreToCount(save);
            this.mSelectionPaint.setShader(null);
            this.mSelectionPaint.setColor((~this.mColor) | ViewCompat.MEASURED_STATE_MASK);
            this.mSelectionPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(this.mSelected.x, this.mSelected.y, this.SELECTED_RADIUS, this.mSelectionPaint);
            drawAlphaPanel(canvas);
            drawValPanel(canvas);
        } catch (Throwable th) {
            canvas.restoreToCount(save);
            throw th;
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        if (mode == 0) {
            size = 100;
        }
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(size, mode);
        if (mode2 == 0) {
            size2 = 100;
        }
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(size2, mode2);
        setMeasuredDimension(makeMeasureSpec, makeMeasureSpec2);
        super.onMeasure(makeMeasureSpec, makeMeasureSpec2);
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        int width = getWidth();
        int height = getHeight();
        this.mDrawingRect.set(0.0f, 0.0f, width, height);
        int min = Math.min(width, height);
        int i5 = min / 10;
        this.slider_width = i5;
        if (i5 < 32) {
            this.slider_width = (int) (this.DENSITY * 32.0f);
        }
        int i6 = this.slider_width;
        int i7 = (min - (((int) (this.DENSITY * 16.0f)) + i6)) >>> 1;
        this.radius = i7;
        this.radius2 = i7 * i7;
        if (!this.mShowValSlider) {
            i6 = 0;
        }
        this.center_x = (width - i6) >>> 1;
        this.center_y = (height - (this.mShowAlphaSlider ? this.slider_width : 0)) >>> 1;
        int i8 = this.center_y;
        int sqrt = ((int) Math.sqrt((i8 * i8) + (this.center_x * this.center_x))) - this.radius;
        float f = -sqrt;
        float f2 = sqrt;
        this.mSelectionRect.set(f, f, f2, f2);
        this.mGradientPaint.setShader(new RadialGradient(0.0f, 0.0f, this.radius, -1, ViewCompat.MEASURED_SIZE_MASK, Shader.TileMode.CLAMP));
        setupAlphaRect();
        setUpValRect();
        setColor(this.mAlpha, this.mHue, this.mSat, this.mVal, true);
    }

    public int getColor() {
        return this.mColor;
    }

    public float getHue() {
        return this.mHue;
    }

    public float getSat() {
        return this.mSat;
    }

    public float getVal() {
        return this.mVal;
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x002d, code lost:
    
        if (r15 != 2) goto L43;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r15) {
        /*
            Method dump skipped, instructions count: 209
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.widget.ColorPickerView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public void setColor(int i) {
        internalSetColor(i, true);
    }

    protected void internalSetColor(int i, boolean z) {
        int alpha = Color.alpha(i);
        Color.RGBToHSV(Color.red(i), Color.green(i), Color.blue(i), this.HSV);
        float[] fArr = this.HSV;
        setColor(alpha, fArr[0], fArr[1], fArr[2], z);
    }

    protected void setColor(int i, float f, float f2, float f3, boolean z) {
        if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        if (!z && this.mAlpha == i && this.mHue == f && this.mSat == f2 && this.mVal == f3) {
            return;
        }
        this.mAlpha = i;
        this.mHue = f;
        this.mSat = f2;
        this.mVal = f3;
        this.mColor = HSVToColor(i, f, f2, f3);
        int i2 = this.radius;
        if (i2 > 0) {
            float f4 = i2 * f2;
            double d = (f / 180.0f) * PI;
            this.mSelected.set(this.center_x + (((float) Math.cos(d)) * f4), this.center_y - (f4 * ((float) Math.sin(d))));
            postInvalidate();
        }
    }

    public void setColorPickerListener(ColorPickerListener colorPickerListener) {
        this.mColorPickerListener = colorPickerListener;
    }

    public ColorPickerListener getColorPickerListener() {
        return this.mColorPickerListener;
    }

    public void setShowSelectedColor(boolean z) {
        this.mShowSelectedColor = z;
    }

    public boolean getShowSelectedColor() {
        return this.mShowSelectedColor;
    }

    public void showAlpha(boolean z) {
        if (this.mShowAlphaSlider != z) {
            this.mShowAlphaSlider = z;
            postInvalidate();
        }
    }

    public boolean isShowAlpha() {
        return this.mShowAlphaSlider;
    }

    public void setShowVal(boolean z) {
        if (this.mShowValSlider != z) {
            this.mShowValSlider = z;
            postInvalidate();
        }
    }

    public boolean isShowVal() {
        return this.mShowValSlider;
    }

    private final int HSVToColor(int i, float f, float f2, float f3) {
        if (f >= 360.0f) {
            f = 359.99f;
        } else if (f < 0.0f) {
            f = 0.0f;
        }
        if (f2 > 1.0f) {
            f2 = 1.0f;
        } else if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        if (f3 > 1.0f) {
            f3 = 1.0f;
        } else if (f3 < 0.0f) {
            f3 = 0.0f;
        }
        float[] fArr = this.HSV;
        fArr[0] = f;
        fArr[1] = f2;
        fArr[2] = f3;
        return Color.HSVToColor(i, fArr);
    }

    private final int[] setHueColorArray(int i, int[] iArr) {
        int length = iArr.length;
        float f = 360.0f;
        float f2 = 360.0f / length;
        float[] fArr = this.HSV;
        fArr[1] = 1.0f;
        fArr[2] = this.mVal;
        for (int i2 = 0; f >= 0.0000001 && i2 < length; i2++) {
            float[] fArr2 = this.HSV;
            fArr2[0] = f;
            iArr[i2] = Color.HSVToColor(i, fArr2);
            f -= f2;
        }
        float[] fArr3 = this.HSV;
        fArr3[0] = 0.0f;
        iArr[length - 1] = Color.HSVToColor(i, fArr3);
        return iArr;
    }

    private final void drawTrackerHorizontal(Canvas canvas, float f, float f2, float f3) {
        float f4 = (this.DENSITY * 4.0f) / RECTANGLE_TRACKER_OFFSET_DP;
        this.mSliderRect.left = f - f4;
        this.mSliderRect.right = f + f4;
        this.mSliderRect.top = f2 - this.RECTANGLE_TRACKER_OFFSET;
        this.mSliderRect.bottom = f2 + f3 + this.RECTANGLE_TRACKER_OFFSET;
        this.mTrackerPaint.setColor(-1842205);
        this.mTrackerPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(this.mSliderRect, RECTANGLE_TRACKER_OFFSET_DP, RECTANGLE_TRACKER_OFFSET_DP, this.mTrackerPaint);
        this.mTrackerPaint.setColor(TRACKER_COLOR);
        this.mTrackerPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(this.mSliderRect, RECTANGLE_TRACKER_OFFSET_DP, RECTANGLE_TRACKER_OFFSET_DP, this.mTrackerPaint);
    }

    private final void drawTrackerVertical(Canvas canvas, float f, float f2, float f3) {
        float f4 = (this.DENSITY * 4.0f) / RECTANGLE_TRACKER_OFFSET_DP;
        this.mSliderRect.left = f - this.RECTANGLE_TRACKER_OFFSET;
        this.mSliderRect.right = f + f3 + this.RECTANGLE_TRACKER_OFFSET;
        this.mSliderRect.top = f2 - f4;
        this.mSliderRect.bottom = f2 + f4;
        this.mTrackerPaint.setColor(-1842205);
        this.mTrackerPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(this.mSliderRect, RECTANGLE_TRACKER_OFFSET_DP, RECTANGLE_TRACKER_OFFSET_DP, this.mTrackerPaint);
        this.mTrackerPaint.setColor(TRACKER_COLOR);
        this.mTrackerPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(this.mSliderRect, RECTANGLE_TRACKER_OFFSET_DP, RECTANGLE_TRACKER_OFFSET_DP, this.mTrackerPaint);
    }

    private void setupAlphaRect() {
        if (this.mShowAlphaSlider) {
            RectF rectF = this.mDrawingRect;
            this.mAlphaRect.set(rectF.left + 1.0f, (rectF.bottom - this.slider_width) + 1.0f, (rectF.right - this.slider_width) - 1.0f, (rectF.bottom - 1.0f) - this.RECTANGLE_TRACKER_OFFSET);
        }
    }

    private final void drawAlphaPanel(Canvas canvas) {
        if (this.mShowAlphaSlider) {
            RectF rectF = this.mAlphaRect;
            this.mBorderPaint.setColor(BORDER_COLOR);
            canvas.drawRect(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f, this.mBorderPaint);
            this.mAlphaPaint.setShader(this.mAlphaShader);
            canvas.drawRect(rectF, this.mAlphaPaint);
            this.mAlphaPaint.setShader(new LinearGradient(rectF.left, rectF.top, rectF.right, rectF.top, HSVToColor(255, this.mHue, this.mSat, this.mVal), HSVToColor(0, this.mHue, this.mSat, this.mVal), Shader.TileMode.CLAMP));
            canvas.drawRect(rectF, this.mAlphaPaint);
            Point alphaToPoint = alphaToPoint(this.mAlpha);
            drawTrackerHorizontal(canvas, alphaToPoint.x, alphaToPoint.y, rectF.height());
        }
    }

    private final boolean trackAlpha(float f, float f2) {
        int pointToAlpha;
        if (!this.mShowAlphaSlider || this.mAlpha == (pointToAlpha = pointToAlpha((int) f))) {
            return false;
        }
        setColor(pointToAlpha, this.mHue, this.mSat, this.mVal, true);
        return true;
    }

    private final Point alphaToPoint(int i) {
        RectF rectF = this.mAlphaRect;
        float width = rectF.width();
        return new Point((int) ((width - ((i * width) / 255.0f)) + rectF.left), (int) rectF.top);
    }

    private final int pointToAlpha(int i) {
        int i2;
        RectF rectF = this.mAlphaRect;
        int width = (int) rectF.width();
        float f = i;
        if (f < rectF.left) {
            i2 = 0;
        } else {
            i2 = f > rectF.right ? width : i - ((int) rectF.left);
        }
        return 255 - ((i2 * 255) / width);
    }

    private final void setUpValRect() {
        if (this.mShowValSlider) {
            RectF rectF = this.mDrawingRect;
            this.mValRect.set((rectF.right - this.slider_width) + 1.0f, rectF.top + 1.0f + this.RECTANGLE_TRACKER_OFFSET, (rectF.right - 1.0f) - this.RECTANGLE_TRACKER_OFFSET, (rectF.bottom - 1.0f) - (this.mShowAlphaSlider ? this.slider_width + 16 : 0));
        }
    }

    private final void drawValPanel(Canvas canvas) {
        if (this.mShowValSlider) {
            RectF rectF = this.mValRect;
            this.mBorderPaint.setColor(BORDER_COLOR);
            canvas.drawRect(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f, this.mBorderPaint);
            this.mValPaint.setShader(new LinearGradient(rectF.left, rectF.top, rectF.left, rectF.bottom, HSVToColor(255, this.mHue, this.mSat, 1.0f), HSVToColor(255, this.mHue, this.mSat, 0.0f), Shader.TileMode.CLAMP));
            canvas.drawRect(rectF, this.mValPaint);
            Point valToPoint = valToPoint(this.mVal);
            drawTrackerVertical(canvas, valToPoint.x, valToPoint.y, rectF.width());
        }
    }

    private final boolean trackVal(float f, float f2) {
        float pointToVal = pointToVal(f2);
        if (this.mVal == pointToVal) {
            return false;
        }
        setColor(this.mAlpha, this.mHue, this.mSat, pointToVal, true);
        return true;
    }

    private final Point valToPoint(float f) {
        RectF rectF = this.mValRect;
        float height = rectF.height();
        Point point = new Point();
        point.y = (int) ((height - (f * height)) + rectF.top);
        point.x = (int) rectF.left;
        return point;
    }

    private final float pointToVal(float f) {
        float f2;
        RectF rectF = this.mValRect;
        float height = rectF.height();
        if (f < rectF.top) {
            f2 = 0.0f;
        } else {
            f2 = f > rectF.bottom ? height : f - rectF.top;
        }
        return 1.0f - ((f2 * 1.0f) / height);
    }
}
