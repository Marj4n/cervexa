package com.google.android.material.progressindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import androidx.core.view.ViewCompat;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.google.android.material.R;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public final class ProgressIndicator extends ProgressBar {
    public static final int CIRCULAR = 1;
    protected static final float DEFAULT_OPACITY = 0.2f;
    protected static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_ProgressIndicator_Linear_Determinate;
    public static final int GROW_MODE_BIDIRECTIONAL = 3;
    public static final int GROW_MODE_INCOMING = 1;
    public static final int GROW_MODE_NONE = 0;
    public static final int GROW_MODE_OUTGOING = 2;
    public static final int LINEAR = 0;
    protected static final int MAX_ALPHA = 255;
    private static final int MAX_HIDE_DELAY = 1000;
    private AnimatorDurationScaleProvider animatorDurationScaleProvider;
    private final Runnable delayedHide;
    private final Runnable delayedShow;
    private final Animatable2Compat.AnimationCallback hideAnimationCallback;
    private boolean isIndeterminateModeChangeRequested;
    private boolean isParentDoneInitializing;
    private long lastShowStartTime;
    private int minHideDelay;
    private int showDelay;
    private final ProgressIndicatorSpec spec;
    private int storedProgress;
    private boolean storedProgressAnimated;
    private final Animatable2Compat.AnimationCallback switchIndeterminateModeCallback;
    private int visibilityAfterHide;

    @Retention(RetentionPolicy.SOURCE)
    public @interface GrowMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface IndicatorType {
    }

    public ProgressIndicator(Context context) {
        this(context, null);
    }

    public ProgressIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.progressIndicatorStyle);
    }

    public ProgressIndicator(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, DEF_STYLE_RES);
    }

    public ProgressIndicator(Context context, AttributeSet attributeSet, int i, int i2) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, i, DEF_STYLE_RES), attributeSet, i);
        this.lastShowStartTime = -1L;
        this.isIndeterminateModeChangeRequested = false;
        this.visibilityAfterHide = 4;
        this.delayedShow = new Runnable() { // from class: com.google.android.material.progressindicator.ProgressIndicator.1
            @Override // java.lang.Runnable
            public void run() {
                ProgressIndicator.this.internalShow();
            }
        };
        this.delayedHide = new Runnable() { // from class: com.google.android.material.progressindicator.ProgressIndicator.2
            @Override // java.lang.Runnable
            public void run() {
                ProgressIndicator.this.internalHide();
                ProgressIndicator.this.lastShowStartTime = -1L;
            }
        };
        this.switchIndeterminateModeCallback = new Animatable2Compat.AnimationCallback() { // from class: com.google.android.material.progressindicator.ProgressIndicator.3
            @Override // androidx.vectordrawable.graphics.drawable.Animatable2Compat.AnimationCallback
            public void onAnimationEnd(Drawable drawable) {
                ProgressIndicator.this.setIndeterminate(false);
                ProgressIndicator.this.setProgressCompat(0, false);
                ProgressIndicator progressIndicator = ProgressIndicator.this;
                progressIndicator.setProgressCompat(progressIndicator.storedProgress, ProgressIndicator.this.storedProgressAnimated);
            }
        };
        this.hideAnimationCallback = new Animatable2Compat.AnimationCallback() { // from class: com.google.android.material.progressindicator.ProgressIndicator.4
            @Override // androidx.vectordrawable.graphics.drawable.Animatable2Compat.AnimationCallback
            public void onAnimationEnd(Drawable drawable) {
                super.onAnimationEnd(drawable);
                if (ProgressIndicator.this.isIndeterminateModeChangeRequested || !ProgressIndicator.this.visibleToUser()) {
                    return;
                }
                ProgressIndicator progressIndicator = ProgressIndicator.this;
                progressIndicator.setVisibility(progressIndicator.visibilityAfterHide);
            }
        };
        this.animatorDurationScaleProvider = new AnimatorDurationScaleProvider();
        this.isParentDoneInitializing = true;
        Context context2 = getContext();
        ProgressIndicatorSpec progressIndicatorSpec = new ProgressIndicatorSpec();
        this.spec = progressIndicatorSpec;
        progressIndicatorSpec.loadFromAttributes(context2, attributeSet, i, i2);
        loadExtraAttributes(context2, attributeSet, i, i2);
        initializeDrawables();
    }

    private void loadExtraAttributes(Context context, AttributeSet attributeSet, int i, int i2) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ProgressIndicator, i, i2);
        this.showDelay = obtainStyledAttributes.getInt(R.styleable.ProgressIndicator_showDelay, -1);
        this.minHideDelay = Math.min(obtainStyledAttributes.getInt(R.styleable.ProgressIndicator_minHideDelay, -1), 1000);
        obtainStyledAttributes.recycle();
    }

    private void initializeDrawables() {
        setIndeterminateDrawable(new IndeterminateDrawable(getContext(), this.spec));
        setProgressDrawable(new DeterminateDrawable(getContext(), this.spec));
        applyNewVisibility();
    }

    private void registerAnimationCallbacks() {
        if (getProgressDrawable() != null && getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().getAnimatorDelegate().registerAnimatorsCompleteCallback(this.switchIndeterminateModeCallback);
        }
        if (getProgressDrawable() != null) {
            getProgressDrawable().registerAnimationCallback(this.hideAnimationCallback);
        }
        if (getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().registerAnimationCallback(this.hideAnimationCallback);
        }
    }

    private void unregisterAnimationCallbacks() {
        if (getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().unregisterAnimationCallback(this.hideAnimationCallback);
            getIndeterminateDrawable().getAnimatorDelegate().unregisterAnimatorsCompleteCallback();
        }
        if (getProgressDrawable() != null) {
            getProgressDrawable().unregisterAnimationCallback(this.hideAnimationCallback);
        }
    }

    public void show() {
        if (this.showDelay > 0) {
            removeCallbacks(this.delayedShow);
            postDelayed(this.delayedShow, this.showDelay);
        } else {
            this.delayedShow.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalShow() {
        if (this.minHideDelay > 0) {
            this.lastShowStartTime = SystemClock.uptimeMillis();
        }
        setVisibility(0);
    }

    public void hide() {
        if (getVisibility() != 0) {
            removeCallbacks(this.delayedShow);
            return;
        }
        removeCallbacks(this.delayedHide);
        long uptimeMillis = SystemClock.uptimeMillis() - this.lastShowStartTime;
        if (uptimeMillis >= ((long) this.minHideDelay)) {
            this.delayedHide.run();
        } else {
            postDelayed(this.delayedHide, this.minHideDelay - uptimeMillis);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalHide() {
        getCurrentDrawable().setVisible(false, false);
        if (isNoLongerNeedToBeVisible()) {
            setVisibility(4);
        }
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        applyNewVisibility();
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        applyNewVisibility();
    }

    private void applyNewVisibility() {
        if (this.isParentDoneInitializing) {
            getCurrentDrawable().setVisible(visibleToUser(), false);
        }
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        registerAnimationCallbacks();
        if (visibleToUser()) {
            internalShow();
        }
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected void onDetachedFromWindow() {
        removeCallbacks(this.delayedHide);
        removeCallbacks(this.delayedShow);
        getCurrentDrawable().hideNow();
        unregisterAnimationCallbacks();
        super.onDetachedFromWindow();
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected synchronized void onDraw(Canvas canvas) {
        int save = canvas.save();
        if (getPaddingLeft() != 0 || getPaddingTop() != 0) {
            canvas.translate(getPaddingLeft(), getPaddingTop());
        }
        if (getPaddingRight() != 0 || getPaddingBottom() != 0) {
            canvas.clipRect(0, 0, getWidth() - (getPaddingLeft() + getPaddingRight()), getHeight() - (getPaddingTop() + getPaddingBottom()));
        }
        getCurrentDrawable().draw(canvas);
        canvas.restoreToCount(save);
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected synchronized void onMeasure(int i, int i2) {
        int paddingLeft;
        int paddingTop;
        super.onMeasure(i, i2);
        DrawingDelegate currentDrawingDelegate = getCurrentDrawingDelegate();
        int preferredWidth = currentDrawingDelegate.getPreferredWidth(this.spec);
        int preferredHeight = currentDrawingDelegate.getPreferredHeight(this.spec);
        if (preferredWidth < 0) {
            paddingLeft = getMeasuredWidth();
        } else {
            paddingLeft = preferredWidth + getPaddingLeft() + getPaddingRight();
        }
        if (preferredHeight < 0) {
            paddingTop = getMeasuredHeight();
        } else {
            paddingTop = preferredHeight + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(paddingLeft, paddingTop);
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        if (this.spec.indicatorType == 0) {
            int paddingLeft = i - (getPaddingLeft() + getPaddingRight());
            int paddingTop = i2 - (getPaddingTop() + getPaddingBottom());
            IndeterminateDrawable indeterminateDrawable = getIndeterminateDrawable();
            if (indeterminateDrawable != null) {
                indeterminateDrawable.setBounds(0, 0, paddingLeft, paddingTop);
            }
            DeterminateDrawable progressDrawable = getProgressDrawable();
            if (progressDrawable != null) {
                progressDrawable.setBounds(0, 0, paddingLeft, paddingTop);
                return;
            }
            return;
        }
        super.onSizeChanged(i, i2, i3, i4);
    }

    @Override // android.view.View
    public void invalidate() {
        super.invalidate();
        if (getCurrentDrawable() != null) {
            getCurrentDrawable().invalidateSelf();
        }
    }

    private boolean isEligibleToSeamless() {
        return isIndeterminate() && this.spec.indicatorType == 0 && this.spec.indicatorColors.length >= 3;
    }

    @Override // android.widget.ProgressBar
    public DrawableWithAnimatedVisibilityChange getCurrentDrawable() {
        return isIndeterminate() ? getIndeterminateDrawable() : getProgressDrawable();
    }

    public DrawingDelegate getCurrentDrawingDelegate() {
        if (isIndeterminate()) {
            return getIndeterminateDrawable().getDrawingDelegate();
        }
        return getProgressDrawable().getDrawingDelegate();
    }

    @Override // android.widget.ProgressBar
    public void setProgressDrawable(Drawable drawable) {
        if (drawable == null) {
            super.setProgressDrawable(null);
        } else {
            if (drawable instanceof DeterminateDrawable) {
                DeterminateDrawable determinateDrawable = (DeterminateDrawable) drawable;
                determinateDrawable.hideNow();
                super.setProgressDrawable(determinateDrawable);
                determinateDrawable.setLevelByFraction(getProgress() / getMax());
                return;
            }
            throw new IllegalArgumentException("Cannot set framework drawable as progress drawable.");
        }
    }

    @Override // android.widget.ProgressBar
    public void setIndeterminateDrawable(Drawable drawable) {
        if (drawable == null) {
            super.setIndeterminateDrawable(null);
        } else {
            if (drawable instanceof IndeterminateDrawable) {
                ((IndeterminateDrawable) drawable).hideNow();
                super.setIndeterminateDrawable(drawable);
                return;
            }
            throw new IllegalArgumentException("Cannot set framework drawable as indeterminate drawable.");
        }
    }

    @Override // android.widget.ProgressBar
    public DeterminateDrawable getProgressDrawable() {
        return (DeterminateDrawable) super.getProgressDrawable();
    }

    @Override // android.widget.ProgressBar
    public IndeterminateDrawable getIndeterminateDrawable() {
        return (IndeterminateDrawable) super.getIndeterminateDrawable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean visibleToUser() {
        return ViewCompat.isAttachedToWindow(this) && getWindowVisibility() == 0 && isEffectivelyVisible();
    }

    protected boolean isEffectivelyVisible() {
        View view = this;
        while (view.getVisibility() == 0) {
            Object parent = view.getParent();
            if (parent == null) {
                return getWindowVisibility() == 0;
            }
            if (!(parent instanceof View)) {
                return true;
            }
            view = (View) parent;
        }
        return false;
    }

    private void updateColorsInDrawables() {
        getProgressDrawable().recalculateColors();
        getIndeterminateDrawable().recalculateColors();
    }

    private boolean isNoLongerNeedToBeVisible() {
        return (getProgressDrawable() == null || !getProgressDrawable().isVisible()) && (getIndeterminateDrawable() == null || !getIndeterminateDrawable().isVisible());
    }

    public ProgressIndicatorSpec getSpec() {
        return this.spec;
    }

    public int getIndicatorType() {
        return this.spec.indicatorType;
    }

    public void setIndicatorType(int i) {
        if (visibleToUser() && this.spec.indicatorType != i) {
            throw new IllegalStateException("Cannot change indicatorType while the progress indicator is visible.");
        }
        this.spec.indicatorType = i;
        initializeDrawables();
        requestLayout();
    }

    @Override // android.widget.ProgressBar
    public synchronized void setIndeterminate(boolean z) {
        if (z == isIndeterminate()) {
            return;
        }
        if (z || !isLinearSeamless()) {
            if (visibleToUser() && z) {
                throw new IllegalStateException("Cannot switch to indeterminate mode while the progress indicator is visible.");
            }
            DrawableWithAnimatedVisibilityChange currentDrawable = getCurrentDrawable();
            if (currentDrawable != null) {
                currentDrawable.hideNow();
            }
            super.setIndeterminate(z);
            DrawableWithAnimatedVisibilityChange currentDrawable2 = getCurrentDrawable();
            if (currentDrawable2 != null) {
                currentDrawable2.setVisible(visibleToUser(), false, false);
            }
            this.isIndeterminateModeChangeRequested = false;
        }
    }

    public int getIndicatorSize() {
        return this.spec.indicatorSize;
    }

    public void setIndicatorSize(int i) {
        if (this.spec.indicatorSize != i) {
            this.spec.indicatorSize = i;
            requestLayout();
        }
    }

    public int[] getIndicatorColors() {
        return this.spec.indicatorColors;
    }

    public void setIndicatorColors(int[] iArr) {
        this.spec.indicatorColors = iArr;
        updateColorsInDrawables();
        if (!isEligibleToSeamless()) {
            this.spec.linearSeamless = false;
        }
        invalidate();
    }

    public int getTrackColor() {
        return this.spec.trackColor;
    }

    public void setTrackColor(int i) {
        if (this.spec.trackColor != i) {
            this.spec.trackColor = i;
            updateColorsInDrawables();
            invalidate();
        }
    }

    public boolean isInverse() {
        return this.spec.inverse;
    }

    public void setInverse(boolean z) {
        if (this.spec.inverse != z) {
            this.spec.inverse = z;
            invalidate();
        }
    }

    public int getGrowMode() {
        return this.spec.growMode;
    }

    public void setGrowMode(int i) {
        if (this.spec.growMode != i) {
            this.spec.growMode = i;
            invalidate();
        }
    }

    public boolean isLinearSeamless() {
        return this.spec.linearSeamless;
    }

    public void setLinearSeamless(boolean z) {
        if (this.spec.linearSeamless == z) {
            return;
        }
        if (visibleToUser() && isIndeterminate()) {
            throw new IllegalStateException("Cannot change linearSeamless while the progress indicator is shown in indeterminate mode.");
        }
        if (isEligibleToSeamless()) {
            this.spec.linearSeamless = z;
            if (z) {
                this.spec.indicatorCornerRadius = 0;
            }
            if (z) {
                getIndeterminateDrawable().setAnimatorDelegate(new LinearIndeterminateSeamlessAnimatorDelegate());
            } else {
                getIndeterminateDrawable().setAnimatorDelegate(new LinearIndeterminateNonSeamlessAnimatorDelegate(getContext()));
            }
        } else {
            this.spec.linearSeamless = false;
        }
        invalidate();
    }

    public int getIndicatorCornerRadius() {
        return this.spec.indicatorCornerRadius;
    }

    public void setIndicatorCornerRadius(int i) {
        if (this.spec.indicatorCornerRadius != i) {
            ProgressIndicatorSpec progressIndicatorSpec = this.spec;
            progressIndicatorSpec.indicatorCornerRadius = Math.min(i, progressIndicatorSpec.indicatorSize / 2);
            if (this.spec.linearSeamless && i > 0) {
                throw new IllegalArgumentException("Rounded corners are not supported in linear seamless mode.");
            }
        }
    }

    public int getCircularInset() {
        return this.spec.circularInset;
    }

    public void setCircularInset(int i) {
        if (this.spec.indicatorType != 1 || this.spec.circularInset == i) {
            return;
        }
        this.spec.circularInset = i;
        invalidate();
    }

    public int getCircularRadius() {
        return this.spec.circularRadius;
    }

    public void setCircularRadius(int i) {
        if (this.spec.indicatorType != 1 || this.spec.circularRadius == i) {
            return;
        }
        this.spec.circularRadius = i;
        invalidate();
    }

    @Override // android.widget.ProgressBar
    public synchronized void setProgress(int i) {
        if (isIndeterminate()) {
            return;
        }
        setProgressCompat(i, false);
    }

    public void setProgressCompat(int i, boolean z) {
        if (isIndeterminate()) {
            if (getProgressDrawable() == null || isLinearSeamless()) {
                return;
            }
            this.storedProgress = i;
            this.storedProgressAnimated = z;
            this.isIndeterminateModeChangeRequested = true;
            if (this.animatorDurationScaleProvider.getSystemAnimatorDurationScale(getContext().getContentResolver()) == 0.0f) {
                this.switchIndeterminateModeCallback.onAnimationEnd(getIndeterminateDrawable());
                return;
            } else {
                getIndeterminateDrawable().getAnimatorDelegate().requestCancelAnimatorAfterCurrentCycle();
                return;
            }
        }
        super.setProgress(i);
        if (getProgressDrawable() == null || z) {
            return;
        }
        getProgressDrawable().jumpToCurrentState();
    }

    public void setVisibilityAfterHide(int i) {
        if (i != 0 && i != 4 && i != 8) {
            throw new IllegalArgumentException("The component's visibility must be one of VISIBLE, INVISIBLE, and GONE defined in View.");
        }
        this.visibilityAfterHide = i;
    }

    public void setAnimatorDurationScaleProvider(AnimatorDurationScaleProvider animatorDurationScaleProvider) {
        this.animatorDurationScaleProvider = animatorDurationScaleProvider;
        if (getProgressDrawable() != null) {
            getProgressDrawable().animatorDurationScaleProvider = animatorDurationScaleProvider;
        }
        if (getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().animatorDurationScaleProvider = animatorDurationScaleProvider;
        }
    }
}
