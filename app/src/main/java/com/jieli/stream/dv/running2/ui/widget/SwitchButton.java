package com.jieli.stream.dv.running2.ui.widget;

import android.R;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;
import androidx.core.content.ContextCompat;

import java.util.IllegalFormatWidthException;

/* loaded from: classes.dex */
public class SwitchButton extends CompoundButton {
    public static final int DEFAULT_ANIMATION_DURATION = 250;
    public static final float DEFAULT_BACK_MEASURE_RATIO = 1.8f;
    public static final int DEFAULT_TEXT_MARGIN_DP = 2;
    public static final int DEFAULT_THUMB_MARGIN_DP = 2;
    public static final int DEFAULT_THUMB_SIZE_DP = 30;
    public static final int DEFAULT_TINT_COLOR = 3309506;
    private long mAnimationDuration;
    private ColorStateList mBackColor;
    private Drawable mBackDrawable;
    private float mBackMeasureRatio;
    private float mBackRadius;
    private RectF mBackRectF;
    private OnCheckedChangeListener mChildOnCheckedChangeListener;
    private int mClickTimeout;
    private int mCurrBackColor;
    private int mCurrThumbColor;
    private Drawable mCurrentBackDrawable;
    private boolean mDrawDebugRect;
    private boolean mFadeBack;
    private boolean mIsBackUseDrawable;
    private boolean mIsThumbUseDrawable;
    private float mLastX;
    private int mNextBackColor;
    private Drawable mNextBackDrawable;
    private Layout mOffLayout;
    private int mOffTextColor;
    private Layout mOnLayout;
    private int mOnTextColor;
    private Paint mPaint;
    private RectF mPresentThumbRectF;
    private float mProcess;
    private ObjectAnimator mProcessAnimator;
    private Paint mRectPaint;
    private RectF mSafeRectF;
    private float mStartX;
    private float mStartY;
    private float mTextHeight;
    private float mTextMarginH;
    private CharSequence mTextOff;
    private RectF mTextOffRectF;
    private CharSequence mTextOn;
    private RectF mTextOnRectF;
    private TextPaint mTextPaint;
    private float mTextWidth;
    private ColorStateList mThumbColor;
    private Drawable mThumbDrawable;
    private RectF mThumbMargin;
    private float mThumbRadius;
    private RectF mThumbRectF;
    private PointF mThumbSizeF;
    private int mTintColor;
    private int mTouchSlop;
    private static int[] CHECKED_PRESSED_STATE = {R.attr.state_checked, R.attr.state_enabled, R.attr.state_pressed};
    private static int[] UNCHECKED_PRESSED_STATE = {-16842912, R.attr.state_enabled, R.attr.state_pressed};

    public SwitchButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDrawDebugRect = false;
        init(attributeSet);
    }

    public SwitchButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDrawDebugRect = false;
        init(attributeSet);
    }

    public SwitchButton(Context context) {
        super(context);
        this.mDrawDebugRect = false;
        init(null);
    }

    private void init(AttributeSet attributeSet) {
        float f;
        float f2;
        float f3;
        String str;
        String str2;
        Drawable drawable;
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        Drawable drawable2;
        float f4;
        int i;
        float f5;
        float f6;
        float f7;
        float f8;
        boolean z;
        int i2;
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.mClickTimeout = ViewConfiguration.getPressedStateDuration() + ViewConfiguration.getTapTimeout();
        this.mPaint = new Paint(1);
        Paint paint = new Paint(1);
        this.mRectPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.mRectPaint.setStrokeWidth(getResources().getDisplayMetrics().density);
        this.mTextPaint = getPaint();
        this.mThumbRectF = new RectF();
        this.mBackRectF = new RectF();
        this.mSafeRectF = new RectF();
        this.mThumbSizeF = new PointF();
        this.mThumbMargin = new RectF();
        this.mTextOnRectF = new RectF();
        this.mTextOffRectF = new RectF();
        ObjectAnimator duration = ObjectAnimator.ofFloat(this, "process", 0.0f, 0.0f).setDuration(250L);
        this.mProcessAnimator = duration;
        duration.setInterpolator(new AccelerateDecelerateInterpolator());
        this.mPresentThumbRectF = new RectF();
        float f9 = getResources().getDisplayMetrics().density;
        float f10 = f9 * 2.0f;
        float f11 = f9 * 30.0f;
        float f12 = f11 / 2.0f;
        TypedArray obtainStyledAttributes = attributeSet == null ? null : getContext().obtainStyledAttributes(attributeSet, com.jieli.stream.dv.running2.R.styleable.SwitchButton);
        if (obtainStyledAttributes != null) {
            Drawable drawable3 = obtainStyledAttributes.getDrawable(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswThumbDrawable);
            ColorStateList colorStateList3 = obtainStyledAttributes.getColorStateList(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswThumbColor);
            float dimension = obtainStyledAttributes.getDimension(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswThumbMargin, f10);
            float dimension2 = obtainStyledAttributes.getDimension(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswThumbMarginLeft, dimension);
            float dimension3 = obtainStyledAttributes.getDimension(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswThumbMarginRight, dimension);
            float dimension4 = obtainStyledAttributes.getDimension(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswThumbMarginTop, dimension);
            float dimension5 = obtainStyledAttributes.getDimension(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswThumbMarginBottom, dimension);
            float dimension6 = obtainStyledAttributes.getDimension(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswThumbWidth, f11);
            float dimension7 = obtainStyledAttributes.getDimension(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswThumbHeight, f11);
            float dimension8 = obtainStyledAttributes.getDimension(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswThumbRadius, Math.min(dimension6, dimension7) / 2.0f);
            float dimension9 = obtainStyledAttributes.getDimension(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswBackRadius, dimension8 + f10);
            Drawable drawable4 = obtainStyledAttributes.getDrawable(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswBackDrawable);
            ColorStateList colorStateList4 = obtainStyledAttributes.getColorStateList(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswBackColor);
            float f13 = obtainStyledAttributes.getFloat(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswBackMeasureRatio, 1.8f);
            f2 = dimension9;
            int integer = obtainStyledAttributes.getInteger(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswAnimationDuration, 250);
            boolean z2 = obtainStyledAttributes.getBoolean(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswFadeBack, true);
            int color = obtainStyledAttributes.getColor(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswTintColor, Integer.MIN_VALUE);
            String string = obtainStyledAttributes.getString(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswTextOn);
            String string2 = obtainStyledAttributes.getString(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswTextOff);
            f10 = obtainStyledAttributes.getDimension(com.jieli.stream.dv.running2.R.styleable.SwitchButton_kswTextMarginH, f10);
            obtainStyledAttributes.recycle();
            f11 = dimension6;
            f = dimension7;
            colorStateList2 = colorStateList4;
            i2 = integer;
            z = z2;
            f3 = dimension8;
            colorStateList = colorStateList3;
            f5 = dimension4;
            i = color;
            f8 = f13;
            drawable = drawable3;
            f6 = dimension3;
            f7 = dimension5;
            str2 = string2;
            str = string;
            f4 = dimension2;
            drawable2 = drawable4;
        } else {
            f = f11;
            f2 = f12;
            f3 = f2;
            str = null;
            str2 = null;
            drawable = null;
            colorStateList = null;
            colorStateList2 = null;
            drawable2 = null;
            f4 = 0.0f;
            i = Integer.MIN_VALUE;
            f5 = 0.0f;
            f6 = 0.0f;
            f7 = 0.0f;
            f8 = 1.8f;
            z = true;
            i2 = 250;
        }
        this.mTextOn = str;
        this.mTextOff = str2;
        this.mTextMarginH = f10;
        this.mThumbDrawable = drawable;
        this.mThumbColor = colorStateList;
        this.mIsThumbUseDrawable = drawable != null;
        this.mTintColor = i;
        if (i == Integer.MIN_VALUE) {
            this.mTintColor = DEFAULT_TINT_COLOR;
        }
        if (!this.mIsThumbUseDrawable && this.mThumbColor == null) {
            ColorStateList generateThumbColorWithTintColor = ColorUtils.generateThumbColorWithTintColor(this.mTintColor);
            this.mThumbColor = generateThumbColorWithTintColor;
            this.mCurrThumbColor = generateThumbColorWithTintColor.getDefaultColor();
        }
        if (this.mIsThumbUseDrawable) {
            f11 = Math.max(f11, this.mThumbDrawable.getMinimumWidth());
            f = Math.max(f, this.mThumbDrawable.getMinimumHeight());
        }
        this.mThumbSizeF.set(f11, f);
        this.mBackDrawable = drawable2;
        this.mBackColor = colorStateList2;
        boolean z3 = drawable2 != null;
        this.mIsBackUseDrawable = z3;
        if (!z3 && this.mBackColor == null) {
            ColorStateList generateBackColorWithTintColor = ColorUtils.generateBackColorWithTintColor(this.mTintColor);
            this.mBackColor = generateBackColorWithTintColor;
            int defaultColor = generateBackColorWithTintColor.getDefaultColor();
            this.mCurrBackColor = defaultColor;
            this.mNextBackColor = this.mBackColor.getColorForState(CHECKED_PRESSED_STATE, defaultColor);
        }
        this.mThumbMargin.set(f4, f5, f6, f7);
        if (this.mThumbMargin.width() >= 0.0f) {
            f8 = Math.max(f8, 1.0f);
        }
        this.mBackMeasureRatio = f8;
        this.mThumbRadius = f3;
        this.mBackRadius = f2;
        long j = i2;
        this.mAnimationDuration = j;
        this.mFadeBack = z;
        this.mProcessAnimator.setDuration(j);
        setFocusable(true);
        setClickable(true);
        if (isChecked()) {
            setProcess(1.0f);
        }
    }

    private Layout makeLayout(CharSequence charSequence) {
        TextPaint r2 = null;
        return new StaticLayout(charSequence, this.mTextPaint, (int) Math.ceil(Layout.getDesiredWidth(charSequence, r2)), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i, int i2) {
        CharSequence charSequence;
        CharSequence charSequence2;
        if (this.mOnLayout == null && (charSequence2 = this.mTextOn) != null) {
            this.mOnLayout = makeLayout(charSequence2);
        }
        if (this.mOffLayout == null && (charSequence = this.mTextOff) != null) {
            this.mOffLayout = makeLayout(charSequence);
        }
        setMeasuredDimension(measureWidth(i), measureHeight(i2));
    }

    private int measureWidth(int i) {
        int size = MeasureSpec.getSize(i);
        int mode = MeasureSpec.getMode(i);
        int ceil = ceil(this.mThumbSizeF.x * this.mBackMeasureRatio);
        if (this.mIsBackUseDrawable) {
            ceil = Math.max(ceil, this.mBackDrawable.getMinimumWidth());
        }
        IllegalFormatWidthException r2 = null;
        float width = this.mOnLayout != null ? r2.getWidth() : 0.0f;
        IllegalFormatWidthException r4 = null;
        float width2 = this.mOffLayout != null ? r4.getWidth() : 0.0f;
        if (width != 0.0f || width2 != 0.0f) {
            this.mTextWidth = Math.max(width, width2) + (this.mTextMarginH * 2.0f);
            float f = ceil;
            float f2 = f - this.mThumbSizeF.x;
            float f3 = this.mTextWidth;
            if (f2 < f3) {
                ceil = (int) (f + (f3 - f2));
            }
        }
        int max = Math.max(ceil, ceil(ceil + this.mThumbMargin.left + this.mThumbMargin.right));
        int max2 = Math.max(Math.max(max, getPaddingLeft() + max + getPaddingRight()), getSuggestedMinimumWidth());
        if (mode == 1073741824) {
            return Math.max(max2, size);
        }
        return mode == Integer.MIN_VALUE ? Math.min(max2, size) : max2;
    }

    private int measureHeight(int i) {
        int mode = MeasureSpec.getMode(i);
        int size = MeasureSpec.getSize(i);
        int ceil = ceil(Math.max(this.mThumbSizeF.y, this.mThumbSizeF.y + this.mThumbMargin.top + this.mThumbMargin.right));
        float height = this.mOnLayout != null ? this.mOnLayout.getHeight() : 0.0f;
        float height2 = this.mOffLayout != null ? this.mOffLayout.getHeight() : 0.0f;
        if (height != 0.0f || height2 != 0.0f) {
            this.mTextHeight = Math.max(height, height2);
            int r2 = 0;
            ceil = ceil(Math.max(ceil, r2));
        }
        int max = Math.max(ceil, getSuggestedMinimumHeight());
        int max2 = Math.max(max, getPaddingTop() + max + getPaddingBottom());
        if (mode == 1073741824) {
            return Math.max(max2, size);
        }
        return mode == Integer.MIN_VALUE ? Math.min(max2, size) : max2;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i == i3 && i2 == i4) {
            return;
        }
        setup();
    }

    private int ceil(double d) {
        return (int) Math.ceil(d);
    }

    private void setup() {
        float paddingTop = getPaddingTop() + Math.max(0.0f, this.mThumbMargin.top);
        float paddingLeft = getPaddingLeft() + Math.max(0.0f, this.mThumbMargin.left);
        if (this.mOnLayout != null && this.mOffLayout != null && this.mThumbMargin.top + this.mThumbMargin.bottom > 0.0f) {
            paddingTop += (((((getMeasuredHeight() - getPaddingBottom()) - getPaddingTop()) - this.mThumbSizeF.y) - this.mThumbMargin.top) - this.mThumbMargin.bottom) / 2.0f;
        }
        if (this.mIsThumbUseDrawable) {
            PointF pointF = this.mThumbSizeF;
            pointF.x = Math.max(pointF.x, this.mThumbDrawable.getMinimumWidth());
            PointF pointF2 = this.mThumbSizeF;
            pointF2.y = Math.max(pointF2.y, this.mThumbDrawable.getMinimumHeight());
        }
        this.mThumbRectF.set(paddingLeft, paddingTop, this.mThumbSizeF.x + paddingLeft, this.mThumbSizeF.y + paddingTop);
        float f = this.mThumbRectF.left - this.mThumbMargin.left;
        float min = Math.min(0.0f, ((Math.max(this.mThumbSizeF.x * this.mBackMeasureRatio, this.mThumbSizeF.x + this.mTextWidth) - this.mThumbRectF.width()) - this.mTextWidth) / 2.0f);
        float min2 = Math.min(0.0f, (((this.mThumbRectF.height() + this.mThumbMargin.top) + this.mThumbMargin.bottom) - this.mTextHeight) / 2.0f);
        this.mBackRectF.set(f + min, (this.mThumbRectF.top - this.mThumbMargin.top) + min2, (((f + this.mThumbMargin.left) + Math.max(this.mThumbSizeF.x * this.mBackMeasureRatio, this.mThumbSizeF.x + this.mTextWidth)) + this.mThumbMargin.right) - min, (this.mThumbRectF.bottom + this.mThumbMargin.bottom) - min2);
        this.mSafeRectF.set(this.mThumbRectF.left, 0.0f, (this.mBackRectF.right - this.mThumbMargin.right) - this.mThumbRectF.width(), 0.0f);
        this.mBackRadius = Math.min(Math.min(this.mBackRectF.width(), this.mBackRectF.height()) / 2.0f, this.mBackRadius);
        Drawable drawable = this.mBackDrawable;
        if (drawable != null) {
            drawable.setBounds((int) this.mBackRectF.left, (int) this.mBackRectF.top, ceil(this.mBackRectF.right), ceil(this.mBackRectF.bottom));
        }
        if (this.mOnLayout != null) {
            float width = ((this.mBackRectF.left + (((this.mBackRectF.width() - this.mThumbRectF.width()) - this.mOnLayout.getWidth()) / 2.0f)) - this.mThumbMargin.left) + (this.mTextMarginH * (this.mThumbMargin.left > 0.0f ? 1 : -1));
            float height = this.mBackRectF.top + ((this.mBackRectF.height() - this.mOnLayout.getHeight()) / 2.0f);
            this.mTextOnRectF.set(width, height, this.mOnLayout.getWidth() + width, this.mOnLayout.getHeight() + height);
        }
        if (this.mOffLayout != null) {
            float width2 = (((this.mBackRectF.right - (((this.mBackRectF.width() - this.mThumbRectF.width()) - this.mOffLayout.getWidth()) / 2.0f)) + this.mThumbMargin.right) - this.mOffLayout.getWidth()) - (this.mTextMarginH * (this.mThumbMargin.right <= 0.0f ? -1 : 1));
            float height2 = this.mBackRectF.top + ((this.mBackRectF.height() - this.mOffLayout.getHeight()) / 2.0f);
            this.mTextOffRectF.set(width2, height2, this.mOffLayout.getWidth() + width2, this.mOffLayout.getHeight() + height2);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x012c  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x012f  */
    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onDraw(android.graphics.Canvas r15) {
        /*
            Method dump skipped, instructions count: 499
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.ui.widget.SwitchButton.onDraw(android.graphics.Canvas):void");
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        super.drawableStateChanged();
        if (!this.mIsThumbUseDrawable && (colorStateList2 = this.mThumbColor) != null) {
            this.mCurrThumbColor = colorStateList2.getColorForState(getDrawableState(), this.mCurrThumbColor);
        } else {
            setDrawableState(this.mThumbDrawable);
        }
        int[] iArr = isChecked() ? UNCHECKED_PRESSED_STATE : CHECKED_PRESSED_STATE;
        ColorStateList textColors = getTextColors();
        if (textColors != null) {
            int defaultColor = textColors.getDefaultColor();
            this.mOnTextColor = textColors.getColorForState(CHECKED_PRESSED_STATE, defaultColor);
            this.mOffTextColor = textColors.getColorForState(UNCHECKED_PRESSED_STATE, defaultColor);
        }
        if (!this.mIsBackUseDrawable && (colorStateList = this.mBackColor) != null) {
            int colorForState = colorStateList.getColorForState(getDrawableState(), this.mCurrBackColor);
            this.mCurrBackColor = colorForState;
            this.mNextBackColor = this.mBackColor.getColorForState(iArr, colorForState);
            return;
        }
        Drawable drawable = this.mBackDrawable;
        if ((drawable instanceof StateListDrawable) && this.mFadeBack) {
            drawable.setState(iArr);
            this.mNextBackDrawable = this.mBackDrawable.getCurrent().mutate();
        } else {
            this.mNextBackDrawable = null;
        }
        setDrawableState(this.mBackDrawable);
        Drawable drawable2 = this.mBackDrawable;
        if (drawable2 != null) {
            this.mCurrentBackDrawable = drawable2.getCurrent().mutate();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x002a, code lost:
    
        if (r0 != 3) goto L28;
     */
    @Override // android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r10) {
        /*
            r9 = this;
            boolean r0 = r9.isEnabled()
            r1 = 0
            if (r0 == 0) goto L97
            boolean r0 = r9.isClickable()
            if (r0 != 0) goto Lf
            goto L97
        Lf:
            int r0 = r10.getAction()
            float r2 = r10.getX()
            float r3 = r9.mStartX
            float r2 = r2 - r3
            float r3 = r10.getY()
            float r4 = r9.mStartY
            float r3 = r3 - r4
            r4 = 1
            if (r0 == 0) goto L80
            if (r0 == r4) goto L47
            r5 = 2
            if (r0 == r5) goto L2d
            r5 = 3
            if (r0 == r5) goto L47
            goto L96
        L2d:
            float r10 = r10.getX()
            float r0 = r9.getProcess()
            float r1 = r9.mLastX
            float r1 = r10 - r1
            android.graphics.RectF r2 = r9.mSafeRectF
            float r2 = r2.width()
            float r1 = r1 / r2
            float r0 = r0 + r1
            r9.setProcess(r0)
            r9.mLastX = r10
            goto L96
        L47:
            r9.setPressed(r1)
            boolean r0 = r9.getStatusBasedOnPos()
            long r5 = r10.getEventTime()
            long r7 = r10.getDownTime()
            long r5 = r5 - r7
            float r10 = (float) r5
            int r5 = r9.mTouchSlop
            float r6 = (float) r5
            int r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r2 >= 0) goto L6f
            float r2 = (float) r5
            int r2 = (r3 > r2 ? 1 : (r3 == r2 ? 0 : -1))
            if (r2 >= 0) goto L6f
            int r2 = r9.mClickTimeout
            float r2 = (float) r2
            int r10 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1))
            if (r10 >= 0) goto L6f
            r9.performClick()
            goto L96
        L6f:
            boolean r10 = r9.isChecked()
            if (r0 == r10) goto L7c
            r9.playSoundEffect(r1)
            r9.setChecked(r0)
            goto L96
        L7c:
            r9.animateToState(r0)
            goto L96
        L80:
            r9.catchView()
            float r0 = r10.getX()
            r9.mStartX = r0
            float r10 = r10.getY()
            r9.mStartY = r10
            float r10 = r9.mStartX
            r9.mLastX = r10
            r9.setPressed(r4)
        L96:
            return r4
        L97:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.ui.widget.SwitchButton.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private boolean getStatusBasedOnPos() {
        return getProcess() > 0.5f;
    }

    public final float getProcess() {
        return this.mProcess;
    }

    public final void setProcess(float f) {
        if (f > 1.0f) {
            f = 1.0f;
        } else if (f < 0.0f) {
            f = 0.0f;
        }
        this.mProcess = f;
        invalidate();
    }

    @Override // android.widget.CompoundButton, android.view.View
    public boolean performClick() {
        return super.performClick();
    }

    protected void animateToState(boolean z) {
        ObjectAnimator objectAnimator = this.mProcessAnimator;
        if (objectAnimator == null) {
            return;
        }
        if (objectAnimator.isRunning()) {
            this.mProcessAnimator.cancel();
        }
        this.mProcessAnimator.setDuration(this.mAnimationDuration);
        if (z) {
            this.mProcessAnimator.setFloatValues(this.mProcess, 1.0f);
        } else {
            this.mProcessAnimator.setFloatValues(this.mProcess, 0.0f);
        }
        this.mProcessAnimator.start();
    }

    private void catchView() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z) {
        if (isChecked() != z) {
            animateToState(z);
        }
        super.setChecked(z);
    }

    public void setCheckedNoEvent(boolean z) {
        if (this.mChildOnCheckedChangeListener == null) {
            setChecked(z);
            return;
        }
        super.setOnCheckedChangeListener(null);
        setChecked(z);
        setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
    }

    public void setCheckedImmediatelyNoEvent(boolean z) {
        if (this.mChildOnCheckedChangeListener == null) {
            setCheckedImmediately(z);
            return;
        }
        super.setOnCheckedChangeListener(null);
        setCheckedImmediately(z);
        setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
    }

    public void toggleNoEvent() {
        if (this.mChildOnCheckedChangeListener == null) {
            toggle();
            return;
        }
        super.setOnCheckedChangeListener(null);
        toggle();
        setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
    }

    public void toggleImmediatelyNoEvent() {
        if (this.mChildOnCheckedChangeListener == null) {
            toggleImmediately();
            return;
        }
        super.setOnCheckedChangeListener(null);
        toggleImmediately();
        setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
    }

    @Override // android.widget.CompoundButton
    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        super.setOnCheckedChangeListener(onCheckedChangeListener);
        this.mChildOnCheckedChangeListener = onCheckedChangeListener;
    }

    public void setCheckedImmediately(boolean z) {
        super.setChecked(z);
        ObjectAnimator objectAnimator = this.mProcessAnimator;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            this.mProcessAnimator.cancel();
        }
        setProcess(z ? 1.0f : 0.0f);
        invalidate();
    }

    public void toggleImmediately() {
        setCheckedImmediately(!isChecked());
    }

    private void setDrawableState(Drawable drawable) {
        if (drawable != null) {
            drawable.setState(getDrawableState());
            invalidate();
        }
    }

    public boolean isDrawDebugRect() {
        return this.mDrawDebugRect;
    }

    public void setDrawDebugRect(boolean z) {
        this.mDrawDebugRect = z;
        invalidate();
    }

    public long getAnimationDuration() {
        return this.mAnimationDuration;
    }

    public void setAnimationDuration(long j) {
        this.mAnimationDuration = j;
    }

    public Drawable getThumbDrawable() {
        return this.mThumbDrawable;
    }

    public void setThumbDrawable(Drawable drawable) {
        this.mThumbDrawable = drawable;
        this.mIsThumbUseDrawable = drawable != null;
        setup();
        refreshDrawableState();
        requestLayout();
        invalidate();
    }

    public void setThumbDrawableRes(int i) {
        setThumbDrawable(ContextCompat.getDrawable(getContext(), i));
    }

    public Drawable getBackDrawable() {
        return this.mBackDrawable;
    }

    public void setBackDrawable(Drawable drawable) {
        this.mBackDrawable = drawable;
        this.mIsBackUseDrawable = drawable != null;
        setup();
        refreshDrawableState();
        requestLayout();
        invalidate();
    }

    public void setBackDrawableRes(int i) {
        setBackDrawable(ContextCompat.getDrawable(getContext(), i));
    }

    public ColorStateList getBackColor() {
        return this.mBackColor;
    }

    public void setBackColor(ColorStateList colorStateList) {
        this.mBackColor = colorStateList;
        if (colorStateList != null) {
            setBackDrawable(null);
        }
        invalidate();
    }

    public ColorStateList getThumbColor() {
        return this.mThumbColor;
    }

    public void setThumbColor(ColorStateList colorStateList) {
        this.mThumbColor = colorStateList;
        if (colorStateList != null) {
            setThumbDrawable(null);
        }
    }

    public float getBackMeasureRatio() {
        return this.mBackMeasureRatio;
    }

    public void setBackMeasureRatio(float f) {
        this.mBackMeasureRatio = f;
        requestLayout();
    }

    public RectF getThumbMargin() {
        return this.mThumbMargin;
    }

    public void setThumbMargin(RectF rectF) {
        if (rectF == null) {
            setThumbMargin(0.0f, 0.0f, 0.0f, 0.0f);
        } else {
            setThumbMargin(rectF.left, rectF.top, rectF.right, rectF.bottom);
        }
    }

    public void setThumbMargin(float f, float f2, float f3, float f4) {
        this.mThumbMargin.set(f, f2, f3, f4);
        requestLayout();
    }

    public void setThumbSize(float f, float f2) {
        this.mThumbSizeF.set(f, f2);
        setup();
        requestLayout();
    }

    public float getThumbWidth() {
        return this.mThumbSizeF.x;
    }

    public float getThumbHeight() {
        return this.mThumbSizeF.y;
    }

    public void setThumbSize(PointF pointF) {
        if (pointF == null) {
            float f = getResources().getDisplayMetrics().density * 30.0f;
            setThumbSize(f, f);
        } else {
            setThumbSize(pointF.x, pointF.y);
        }
    }

    public PointF getThumbSizeF() {
        return this.mThumbSizeF;
    }

    public float getThumbRadius() {
        return this.mThumbRadius;
    }

    public void setThumbRadius(float f) {
        this.mThumbRadius = f;
        if (this.mIsThumbUseDrawable) {
            return;
        }
        invalidate();
    }

    public PointF getBackSizeF() {
        return new PointF(this.mBackRectF.width(), this.mBackRectF.height());
    }

    public float getBackRadius() {
        return this.mBackRadius;
    }

    public void setBackRadius(float f) {
        this.mBackRadius = f;
        if (this.mIsBackUseDrawable) {
            return;
        }
        invalidate();
    }

    public boolean isFadeBack() {
        return this.mFadeBack;
    }

    public void setFadeBack(boolean z) {
        this.mFadeBack = z;
    }

    public int getTintColor() {
        return this.mTintColor;
    }

    public void setTintColor(int i) {
        this.mTintColor = i;
        this.mThumbColor = ColorUtils.generateThumbColorWithTintColor(i);
        this.mBackColor = ColorUtils.generateBackColorWithTintColor(this.mTintColor);
        this.mIsBackUseDrawable = false;
        this.mIsThumbUseDrawable = false;
        refreshDrawableState();
        invalidate();
    }

    public void setText(CharSequence charSequence, CharSequence charSequence2) {
        this.mTextOn = charSequence;
        this.mTextOff = charSequence2;
        this.mOnLayout = null;
        this.mOffLayout = null;
        requestLayout();
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.onText = this.mTextOn;
        savedState.offText = this.mTextOff;
        return savedState;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        setText(savedState.onText, savedState.offText);
        super.onRestoreInstanceState(savedState.getSuperState());
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() { // from class: com.jieli.stream.dv.running2.ui.widget.SwitchButton.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        CharSequence offText;
        CharSequence onText;

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.onText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.offText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            TextUtils.writeToParcel(this.onText, parcel, i);
            TextUtils.writeToParcel(this.offText, parcel, i);
        }
    }
}
