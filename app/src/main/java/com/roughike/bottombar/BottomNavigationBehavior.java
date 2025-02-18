package com.roughike.bottombar;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import com.google.android.material.snackbar.Snackbar;

/* loaded from: classes.dex */
class BottomNavigationBehavior<V extends View> extends VerticalScrollingBehavior<V> {
    private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();
    private final int bottomNavHeight;
    private final int defaultOffset;
    private boolean isTablet;
    private boolean mScrollingEnabled;
    private ViewPropertyAnimatorCompat mTranslationAnimator;
    private final BottomNavigationWithSnackbar mWithSnackBarImpl;
    private boolean hidden = false;
    private int mSnackbarHeight = -1;

    private interface BottomNavigationWithSnackbar {
        void updateSnackbar(CoordinatorLayout coordinatorLayout, View view, View view2);
    }

    @Override // com.roughike.bottombar.VerticalScrollingBehavior
    public void onNestedVerticalOverScroll(CoordinatorLayout coordinatorLayout, V v, int i, int i2, int i3) {
    }

    BottomNavigationBehavior(int i, int i2, boolean z) {
        this.isTablet = false;
        this.mWithSnackBarImpl = Build.VERSION.SDK_INT >= 21 ? new LollipopBottomNavWithSnackBarImpl() : new PreLollipopBottomNavWithSnackBarImpl();
        this.mScrollingEnabled = true;
        this.bottomNavHeight = i;
        this.defaultOffset = i2;
        this.isTablet = z;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, V v, View view) {
        this.mWithSnackBarImpl.updateSnackbar(coordinatorLayout, view, v);
        return view instanceof Snackbar.SnackbarLayout;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onDependentViewRemoved(CoordinatorLayout coordinatorLayout, V v, View view) {
        updateScrollingForSnackbar(view, true);
        super.onDependentViewRemoved(coordinatorLayout, v, view);
    }

    private void updateScrollingForSnackbar(View view, boolean z) {
        if (this.isTablet || !(view instanceof Snackbar.SnackbarLayout)) {
            return;
        }
        this.mScrollingEnabled = z;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, V v, View view) {
        updateScrollingForSnackbar(view, false);
        return super.onDependentViewChanged(coordinatorLayout, v, view);
    }

    @Override // com.roughike.bottombar.VerticalScrollingBehavior
    public void onDirectionNestedPreScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i, int i2, int[] iArr, int i3) {
        handleDirection(v, i3);
    }

    private void handleDirection(V v, int i) {
        if (this.mScrollingEnabled) {
            if (i == -1 && this.hidden) {
                this.hidden = false;
                animateOffset(v, this.defaultOffset);
            } else {
                if (i != 1 || this.hidden) {
                    return;
                }
                this.hidden = true;
                animateOffset(v, this.bottomNavHeight + this.defaultOffset);
            }
        }
    }

    @Override // com.roughike.bottombar.VerticalScrollingBehavior
    protected boolean onNestedDirectionFling(CoordinatorLayout coordinatorLayout, V v, View view, float f, float f2, int i) {
        handleDirection(v, i);
        return true;
    }

    private void animateOffset(V v, int i) {
        ensureOrCancelAnimator(v);
        this.mTranslationAnimator.translationY(i).start();
    }

    private void ensureOrCancelAnimator(V v) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.mTranslationAnimator;
        if (viewPropertyAnimatorCompat == null) {
            ViewPropertyAnimatorCompat animate = ViewCompat.animate(v);
            this.mTranslationAnimator = animate;
            animate.setDuration(300L);
            this.mTranslationAnimator.setInterpolator(INTERPOLATOR);
            return;
        }
        viewPropertyAnimatorCompat.cancel();
    }

    void setHidden(V v, boolean z) {
        if (!z && this.hidden) {
            animateOffset(v, this.defaultOffset);
        } else if (z && !this.hidden) {
            animateOffset(v, this.bottomNavHeight + this.defaultOffset);
        }
        this.hidden = z;
    }

    static <V extends View> BottomNavigationBehavior<V> from(V v) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (!(layoutParams instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
        if (!(behavior instanceof BottomNavigationBehavior)) {
            throw new IllegalArgumentException("The view is not associated with BottomNavigationBehavior");
        }
        return (BottomNavigationBehavior) behavior;
    }

    private class PreLollipopBottomNavWithSnackBarImpl implements BottomNavigationWithSnackbar {
        private PreLollipopBottomNavWithSnackBarImpl() {
        }

        @Override // com.roughike.bottombar.BottomNavigationBehavior.BottomNavigationWithSnackbar
        public void updateSnackbar(CoordinatorLayout coordinatorLayout, View view, View view2) {
            if (BottomNavigationBehavior.this.isTablet || !(view instanceof Snackbar.SnackbarLayout)) {
                return;
            }
            if (BottomNavigationBehavior.this.mSnackbarHeight == -1) {
                BottomNavigationBehavior.this.mSnackbarHeight = view.getHeight();
            }
            if (ViewCompat.getTranslationY(view2) != 0.0f) {
                return;
            }
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).bottomMargin = (BottomNavigationBehavior.this.bottomNavHeight + BottomNavigationBehavior.this.mSnackbarHeight) - BottomNavigationBehavior.this.defaultOffset;
            view2.bringToFront();
            view2.getParent().requestLayout();
            if (Build.VERSION.SDK_INT < 19) {
                ((View) view2.getParent()).invalidate();
            }
        }
    }

    private class LollipopBottomNavWithSnackBarImpl implements BottomNavigationWithSnackbar {
        private LollipopBottomNavWithSnackBarImpl() {
        }

        @Override // com.roughike.bottombar.BottomNavigationBehavior.BottomNavigationWithSnackbar
        public void updateSnackbar(CoordinatorLayout coordinatorLayout, View view, View view2) {
            if (BottomNavigationBehavior.this.isTablet || !(view instanceof Snackbar.SnackbarLayout)) {
                return;
            }
            if (BottomNavigationBehavior.this.mSnackbarHeight == -1) {
                BottomNavigationBehavior.this.mSnackbarHeight = view.getHeight();
            }
            if (ViewCompat.getTranslationY(view2) != 0.0f) {
                return;
            }
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), (BottomNavigationBehavior.this.mSnackbarHeight + BottomNavigationBehavior.this.bottomNavHeight) - BottomNavigationBehavior.this.defaultOffset);
        }
    }
}
