package com.generalplus.GoPlusDrone.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.viewpager.widget.ViewPager;
import com.generalplus.GoPlusDrone.Fragment.BitmapUtils;
import com.github.mikephil.charting.utils.Utils;

/* loaded from: classes.dex */
public abstract class BaseZoomableImageView extends View {
    private static final float MAX_IMAGE_RATIO_LARGE = 5.0f;
    private static final float MAX_IMAGE_RATIO_WIDTH_LARGE_LANDSCAPE = 2.0f;
    public static final int MIN_SDK_ENABLE_LAYER_TYPE_HARDWARE = 14;
    static final int sAnimationDelay = 500;
    static final int sPaintDelay = 250;
    static final float sPanRate = 7.0f;
    static final float sScaleRate = 1.25f;
    private boolean adjustLongImageEnable;
    private boolean fling;
    private boolean landscape;
    private Matrix mBaseMatrix;
    protected Bitmap mBitmap;
    private Matrix mDisplayMatrix;
    private Runnable mFling;
    protected ImageGestureListener mImageGestureListener;
    private double mLastDraw;
    private Matrix mMatrix;
    private float[] mMatrixValues;
    private float mMaxZoom;
    private Runnable mOnLayoutRunnable;
    private Paint mPaint;
    private Runnable mRefresh;
    private Matrix mSuppMatrix;
    private int mThisHeight;
    private int mThisWidth;
    protected ViewPager mViewPager;

    /* JADX INFO: Access modifiers changed from: private */
    public float easeOut(float f, float f2, float f3, float f4) {
        float f5 = (f / f4) - 1.0f;
        return (f3 * ((f5 * f5 * f5) + 1.0f)) + f2;
    }

    protected void onScrollFinish() {
    }

    protected Rect updateSelection() {
        return null;
    }

    public BaseZoomableImageView(Context context) {
        super(context);
        this.mBaseMatrix = new Matrix();
        this.mSuppMatrix = new Matrix();
        this.mDisplayMatrix = new Matrix();
        this.mMatrix = new Matrix();
        this.mMatrixValues = new float[9];
        this.mThisWidth = -1;
        this.mThisHeight = -1;
        this.mOnLayoutRunnable = null;
        this.mRefresh = null;
        this.mLastDraw = Utils.DOUBLE_EPSILON;
        this.mFling = null;
        this.fling = false;
        this.landscape = false;
        this.adjustLongImageEnable = true;
        initBaseZoomableImageView(context);
    }

    public BaseZoomableImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mBaseMatrix = new Matrix();
        this.mSuppMatrix = new Matrix();
        this.mDisplayMatrix = new Matrix();
        this.mMatrix = new Matrix();
        this.mMatrixValues = new float[9];
        this.mThisWidth = -1;
        this.mThisHeight = -1;
        this.mOnLayoutRunnable = null;
        this.mRefresh = null;
        this.mLastDraw = Utils.DOUBLE_EPSILON;
        this.mFling = null;
        this.fling = false;
        this.landscape = false;
        this.adjustLongImageEnable = true;
        initBaseZoomableImageView(context);
    }

    protected void initBaseZoomableImageView(Context context) {
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setDither(true);
        this.mPaint.setFilterBitmap(true);
        this.mPaint.setAntiAlias(true);
        if (context.getResources().getConfiguration().orientation == 2) {
            this.landscape = true;
        } else {
            this.landscape = false;
        }
        this.mRefresh = new Runnable() { // from class: com.generalplus.GoPlusDrone.View.BaseZoomableImageView.1
            @Override // java.lang.Runnable
            public void run() {
                BaseZoomableImageView.this.postInvalidate();
            }
        };
    }

    public void setImageGestureListener(ImageGestureListener imageGestureListener) {
        this.mImageGestureListener = imageGestureListener;
    }

    public void setViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
    }

    public Bitmap getImageBitmap() {
        return this.mBitmap;
    }

    public void clear() {
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.mBitmap.recycle();
        }
        this.mBitmap = null;
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mThisWidth = i3 - i;
        this.mThisHeight = i4 - i2;
        Runnable runnable = this.mOnLayoutRunnable;
        if (runnable != null) {
            this.mOnLayoutRunnable = null;
            runnable.run();
        }
    }

    protected static void translatePoint(Matrix matrix, float[] fArr) {
        matrix.mapPoints(fArr);
    }

    public void setImageMatrix(Matrix matrix) {
        if (matrix != null && matrix.isIdentity()) {
            matrix = null;
        }
        if ((matrix != null || this.mMatrix.isIdentity()) && (matrix == null || this.mMatrix.equals(matrix))) {
            return;
        }
        this.mMatrix.set(matrix);
        invalidate();
    }

    public void setImageBitmap(Bitmap bitmap) {
        setImageBitmap(bitmap, true);
    }

    public void setImageBitmap(final Bitmap bitmap, final boolean z) {
        if (Build.VERSION.SDK_INT >= 14) {
            if (bitmap != null && (bitmap.getHeight() > BitmapUtils.getTextureSize() || bitmap.getWidth() > BitmapUtils.getTextureSize())) {
                setLayerType(1, null);
            } else {
                setLayerType(2, null);
            }
        }
        if (getWidth() <= 0) {
            this.mOnLayoutRunnable = new Runnable() { // from class: com.generalplus.GoPlusDrone.View.BaseZoomableImageView.2
                @Override // java.lang.Runnable
                public void run() {
                    BaseZoomableImageView.this.setImageBitmap(bitmap, z);
                }
            };
            return;
        }
        Bitmap bitmap2 = this.mBitmap;
        if (bitmap != null) {
            setBaseMatrix(bitmap, this.mBaseMatrix);
            this.mBitmap = bitmap;
        } else {
            this.mBaseMatrix.reset();
            this.mBitmap = bitmap;
        }
        if (bitmap2 != null && bitmap2 != this.mBitmap && !bitmap2.isRecycled()) {
            bitmap2.recycle();
        }
        this.mSuppMatrix.reset();
        setImageMatrix(getImageViewMatrix());
        this.mMaxZoom = maxZoom();
        if (z) {
            zoomToScreen();
        }
    }

    public void setImageBitmap(final Bitmap bitmap, Rect rect) {
        if (getWidth() <= 0) {
            this.mOnLayoutRunnable = new Runnable() { // from class: com.generalplus.GoPlusDrone.View.BaseZoomableImageView.3
                @Override // java.lang.Runnable
                public void run() {
                    BaseZoomableImageView baseZoomableImageView = BaseZoomableImageView.this;
                    baseZoomableImageView.setImageBitmap(bitmap, baseZoomableImageView.updateSelection());
                }
            };
            return;
        }
        Bitmap bitmap2 = this.mBitmap;
        if (bitmap != null) {
            setBaseMatrix(bitmap, this.mBaseMatrix, rect);
            this.mBitmap = bitmap;
        } else {
            this.mBaseMatrix.reset();
            this.mBitmap = bitmap;
        }
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            bitmap2.recycle();
        }
        this.mSuppMatrix.reset();
        setImageMatrix(getImageViewMatrix());
        this.mMaxZoom = maxZoom();
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x008c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void center(boolean r10, boolean r11, boolean r12) {
        /*
            r9 = this;
            android.graphics.Bitmap r0 = r9.mBitmap
            if (r0 != 0) goto L5
            return
        L5:
            android.graphics.Matrix r0 = r9.getImageViewMatrix()
            r1 = 2
            float[] r2 = new float[r1]
            r2 = {x00aa: FILL_ARRAY_DATA , data: [0, 0} // fill-array
            float[] r1 = new float[r1]
            android.graphics.Bitmap r3 = r9.mBitmap
            int r3 = r3.getWidth()
            float r3 = (float) r3
            r4 = 0
            r1[r4] = r3
            android.graphics.Bitmap r3 = r9.mBitmap
            int r3 = r3.getHeight()
            float r3 = (float) r3
            r5 = 1
            r1[r5] = r3
            translatePoint(r0, r2)
            translatePoint(r0, r1)
            r0 = r1[r5]
            r3 = r2[r5]
            float r0 = r0 - r3
            r3 = r1[r4]
            r6 = r2[r4]
            float r3 = r3 - r6
            r6 = 1073741824(0x40000000, float:2.0)
            r7 = 0
            if (r10 == 0) goto L61
            int r10 = r9.getHeight()
            float r10 = (float) r10
            int r8 = (r0 > r10 ? 1 : (r0 == r10 ? 0 : -1))
            if (r8 >= 0) goto L49
            float r10 = r10 - r0
            float r10 = r10 / r6
            r0 = r2[r5]
        L47:
            float r10 = r10 - r0
            goto L62
        L49:
            r0 = r2[r5]
            int r0 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1))
            if (r0 <= 0) goto L53
            r10 = r2[r5]
            float r10 = -r10
            goto L62
        L53:
            r0 = r1[r5]
            int r10 = (r0 > r10 ? 1 : (r0 == r10 ? 0 : -1))
            if (r10 >= 0) goto L61
            int r10 = r9.getHeight()
            float r10 = (float) r10
            r0 = r1[r5]
            goto L47
        L61:
            r10 = 0
        L62:
            if (r11 == 0) goto L86
            int r11 = r9.getWidth()
            float r11 = (float) r11
            int r0 = (r3 > r11 ? 1 : (r3 == r11 ? 0 : -1))
            if (r0 >= 0) goto L73
            float r11 = r11 - r3
            float r11 = r11 / r6
            r0 = r2[r4]
        L71:
            float r11 = r11 - r0
            goto L87
        L73:
            r0 = r2[r4]
            int r0 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1))
            if (r0 <= 0) goto L7d
            r11 = r2[r4]
            float r11 = -r11
            goto L87
        L7d:
            r0 = r1[r4]
            int r0 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1))
            if (r0 >= 0) goto L86
            r0 = r1[r4]
            goto L71
        L86:
            r11 = 0
        L87:
            r9.postTranslate(r11, r10)
            if (r12 == 0) goto La2
            android.view.animation.TranslateAnimation r12 = new android.view.animation.TranslateAnimation
            float r11 = -r11
            float r10 = -r10
            r12.<init>(r11, r7, r10, r7)
            long r10 = android.os.SystemClock.elapsedRealtime()
            r12.setStartTime(r10)
            r10 = 250(0xfa, double:1.235E-321)
            r12.setDuration(r10)
            r9.setAnimation(r12)
        La2:
            android.graphics.Matrix r10 = r9.getImageViewMatrix()
            r9.setImageMatrix(r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.generalplus.GoPlusDrone.View.BaseZoomableImageView.center(boolean, boolean, boolean):void");
    }

    protected float getValue(Matrix matrix, int i) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[i];
    }

    protected float getScale(Matrix matrix) {
        if (this.mBitmap != null) {
            return getValue(matrix, 0);
        }
        return 1.0f;
    }

    public float getScale() {
        return getScale(this.mSuppMatrix);
    }

    private void setBaseMatrix(Bitmap bitmap, Matrix matrix) {
        float width = getWidth();
        float height = getHeight();
        matrix.reset();
        float min = Math.min(width / bitmap.getWidth(), 1.0f);
        float min2 = Math.min(height / bitmap.getHeight(), 1.0f);
        if (min > min2) {
            min = min2;
        }
        matrix.setScale(min, min);
        matrix.postTranslate((width - (bitmap.getWidth() * min)) / MAX_IMAGE_RATIO_WIDTH_LARGE_LANDSCAPE, (height - (bitmap.getHeight() * min)) / MAX_IMAGE_RATIO_WIDTH_LARGE_LANDSCAPE);
    }

    private void setBaseMatrix(Bitmap bitmap, Matrix matrix, Rect rect) {
        if (rect == null) {
            return;
        }
        float f = rect.right - rect.left;
        float f2 = rect.bottom - rect.top;
        matrix.reset();
        float width = f / bitmap.getWidth();
        float height = f2 / bitmap.getHeight();
        if (width <= height) {
            width = height;
        }
        matrix.setScale(width, width);
        matrix.postTranslate((getWidth() - (bitmap.getWidth() * width)) / MAX_IMAGE_RATIO_WIDTH_LARGE_LANDSCAPE, (getHeight() - (bitmap.getHeight() * width)) / MAX_IMAGE_RATIO_WIDTH_LARGE_LANDSCAPE);
    }

    protected Matrix getImageViewMatrix() {
        this.mDisplayMatrix.set(this.mBaseMatrix);
        this.mDisplayMatrix.postConcat(this.mSuppMatrix);
        return this.mDisplayMatrix;
    }

    protected float maxZoom() {
        if (this.mBitmap == null) {
            return 1.0f;
        }
        float max = Math.max(r0.getWidth() / this.mThisWidth, this.mBitmap.getHeight() / this.mThisHeight) * 16.0f;
        if (max < 1.0f) {
            return 1.0f;
        }
        return max;
    }

    public float zoomDefault() {
        if (this.mBitmap == null) {
            return 1.0f;
        }
        return Math.max(Math.min(this.mThisWidth / r0.getWidth(), this.mThisHeight / this.mBitmap.getHeight()), 1.0f);
    }

    protected void zoomTo(float f, float f2, float f3) {
        float f4 = this.mMaxZoom;
        if (f > f4) {
            f = f4;
        }
        float scale = f / getScale();
        this.mSuppMatrix.postScale(scale, scale, f2, f3);
        setImageMatrix(getImageViewMatrix());
        center(true, true, false);
    }

    protected void zoomTo(float f, final float f2, final float f3, final float f4) {
        final float scale = (f - getScale()) / f4;
        final float scale2 = getScale();
        final long currentTimeMillis = System.currentTimeMillis();
        post(new Runnable() { // from class: com.generalplus.GoPlusDrone.View.BaseZoomableImageView.4
            @Override // java.lang.Runnable
            public void run() {
                float min = Math.min(f4, System.currentTimeMillis() - currentTimeMillis);
                BaseZoomableImageView.this.zoomTo(scale2 + (scale * min), f2, f3);
                if (min < f4) {
                    BaseZoomableImageView.this.post(this);
                }
            }
        });
    }

    public void setAdjustLongImageEnable(boolean z) {
        this.adjustLongImageEnable = z;
    }

    public void zoomToScreen() {
        if (this.mBitmap == null) {
            return;
        }
        float f = 1.0f;
        float width = this.mThisWidth / r0.getWidth();
        boolean z = false;
        if (this.adjustLongImageEnable && (this.mBitmap.getHeight() / this.mBitmap.getWidth() > MAX_IMAGE_RATIO_LARGE || (this.landscape && this.mBitmap.getHeight() / this.mBitmap.getWidth() > MAX_IMAGE_RATIO_WIDTH_LARGE_LANDSCAPE))) {
            f = width;
            z = true;
        }
        if (z) {
            float scale = f / getScale();
            this.mBaseMatrix.reset();
            this.mSuppMatrix.postScale(scale, scale, 0.0f, 0.0f);
            setImageMatrix(getImageViewMatrix());
            return;
        }
        zoomTo(zoomDefault());
    }

    public void zoomTo(float f) {
        zoomTo(f, getWidth() / MAX_IMAGE_RATIO_WIDTH_LARGE_LANDSCAPE, getHeight() / MAX_IMAGE_RATIO_WIDTH_LARGE_LANDSCAPE);
    }

    public void zoomIn() {
        zoomIn(sScaleRate);
    }

    public void zoomOut() {
        zoomOut(sScaleRate);
    }

    protected void zoomIn(float f) {
        if (getScale() < this.mMaxZoom && this.mBitmap != null) {
            this.mSuppMatrix.postScale(f, f, getWidth() / MAX_IMAGE_RATIO_WIDTH_LARGE_LANDSCAPE, getHeight() / MAX_IMAGE_RATIO_WIDTH_LARGE_LANDSCAPE);
            setImageMatrix(getImageViewMatrix());
        }
    }

    protected void zoomOut(float f) {
        if (this.mBitmap == null) {
            return;
        }
        float width = getWidth();
        float height = getHeight();
        Matrix matrix = new Matrix(this.mSuppMatrix);
        float f2 = width / MAX_IMAGE_RATIO_WIDTH_LARGE_LANDSCAPE;
        float f3 = height / MAX_IMAGE_RATIO_WIDTH_LARGE_LANDSCAPE;
        matrix.postScale(0.8f, 0.8f, f2, f3);
        if (getScale(matrix) < 1.0f) {
            this.mSuppMatrix.setScale(1.0f, 1.0f, f2, f3);
        } else {
            float f4 = 1.0f / f;
            this.mSuppMatrix.postScale(f4, f4, f2, f3);
        }
        setImageMatrix(getImageViewMatrix());
        center(true, true, false);
    }

    protected boolean postTranslate(float f, float f2) {
        return this.mSuppMatrix.postTranslate(f, f2);
    }

    protected void scrollBy(final float f, final float f2, final float f3) {
        final long currentTimeMillis = System.currentTimeMillis();
        Runnable runnable = new Runnable() { // from class: com.generalplus.GoPlusDrone.View.BaseZoomableImageView.5
            float old_x = 0.0f;
            float old_y = 0.0f;

            @Override // java.lang.Runnable
            public void run() {
                float min = Math.min(f3, System.currentTimeMillis() - currentTimeMillis);
                float easeOut = BaseZoomableImageView.this.easeOut(min, 0.0f, f, f3);
                float easeOut2 = BaseZoomableImageView.this.easeOut(min, 0.0f, f2, f3);
                BaseZoomableImageView.this.postTranslate(easeOut - this.old_x, easeOut2 - this.old_y);
                BaseZoomableImageView.this.center(true, true, false);
                this.old_x = easeOut;
                this.old_y = easeOut2;
                if (min < f3) {
                    BaseZoomableImageView baseZoomableImageView = BaseZoomableImageView.this;
                    baseZoomableImageView.fling = baseZoomableImageView.post(this);
                } else {
                    BaseZoomableImageView.this.stopFling();
                }
            }
        };
        this.mFling = runnable;
        this.fling = post(runnable);
    }

    protected void stopFling() {
        removeCallbacks(this.mFling);
        if (this.fling) {
            this.fling = false;
            onScrollFinish();
        }
    }

    protected boolean fling() {
        return this.fling;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = this.mBitmap;
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 14 && getLayerType() == 2) {
            canvas.drawBitmap(this.mBitmap, this.mMatrix, null);
            return;
        }
        if (System.currentTimeMillis() - this.mLastDraw > 250.0d) {
            canvas.drawBitmap(this.mBitmap, this.mMatrix, this.mPaint);
            this.mLastDraw = System.currentTimeMillis();
        } else {
            canvas.drawBitmap(this.mBitmap, this.mMatrix, null);
            removeCallbacks(this.mRefresh);
            postDelayed(this.mRefresh, 250L);
        }
    }

    protected boolean isScrollOver(float f) {
        try {
            if (this.mDisplayMatrix != null) {
                float value = getValue(this.mDisplayMatrix, 2);
                float width = getWidth() - value;
                if ((value == 0.0f && f <= 0.0f) || (width == this.mBitmap.getWidth() * getValue(this.mDisplayMatrix, 0) && f >= 0.0f)) {
                    System.out.println("ScrollOver");
                    return true;
                }
            }
        } catch (IllegalArgumentException e) {
            Log.v("Vincent", "isScrollOver");
            e.printStackTrace();
        }
        return false;
    }
}
