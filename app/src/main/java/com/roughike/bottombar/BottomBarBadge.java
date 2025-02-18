package com.roughike.bottombar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;

/* loaded from: classes.dex */
class BottomBarBadge extends TextView {
    private int count;
    private boolean isVisible;

    BottomBarBadge(Context context) {
        super(context);
        this.isVisible = false;
    }

    void setCount(int i) {
        this.count = i;
        setText(String.valueOf(i));
    }

    int getCount() {
        return this.count;
    }

    void show() {
        this.isVisible = true;
        ViewCompat.animate(this).setDuration(150L).alpha(1.0f).scaleX(1.0f).scaleY(1.0f).start();
    }

    void hide() {
        this.isVisible = false;
        ViewCompat.animate(this).setDuration(150L).alpha(0.0f).scaleX(0.0f).scaleY(0.0f).start();
    }

    boolean isVisible() {
        return this.isVisible;
    }

    void attachToTab(BottomBarTab bottomBarTab, int i) {
        setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        setGravity(17);
        MiscUtils.setTextAppearance(this, R.style.BB_BottomBarBadge_Text);
        setColoredCircleBackground(i);
        wrapTabAndBadgeInSameContainer(bottomBarTab);
    }

    void setColoredCircleBackground(int i) {
        int dpToPixel = MiscUtils.dpToPixel(getContext(), 1.0f);
        ShapeDrawable make = BadgeCircle.make(dpToPixel * 3, i);
        setPadding(dpToPixel, dpToPixel, dpToPixel, dpToPixel);
        setBackgroundCompat(make);
    }

    private void wrapTabAndBadgeInSameContainer(final BottomBarTab bottomBarTab) {
        ViewGroup viewGroup = (ViewGroup) bottomBarTab.getParent();
        viewGroup.removeView(bottomBarTab);
        final BadgeContainer badgeContainer = new BadgeContainer(getContext());
        badgeContainer.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        badgeContainer.addView(bottomBarTab);
        badgeContainer.addView(this);
        viewGroup.addView(badgeContainer, bottomBarTab.getIndexInTabContainer());
        badgeContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.roughike.bottombar.BottomBarBadge.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                badgeContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                BottomBarBadge.this.adjustPositionAndSize(bottomBarTab);
            }
        });
    }

    void removeFromTab(BottomBarTab bottomBarTab) {
        BadgeContainer badgeContainer = (BadgeContainer) getParent();
        ViewGroup viewGroup = (ViewGroup) badgeContainer.getParent();
        badgeContainer.removeView(bottomBarTab);
        viewGroup.removeView(badgeContainer);
        viewGroup.addView(bottomBarTab, bottomBarTab.getIndexInTabContainer());
    }

    void adjustPositionAndSize(BottomBarTab bottomBarTab) {
        AppCompatImageView iconView = bottomBarTab.getIconView();
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        int max = Math.max(getWidth(), getHeight());
        setX(iconView.getX() + ((float) (iconView.getWidth() / 1.25d)));
        setTranslationY(10.0f);
        if (layoutParams.width == max && layoutParams.height == max) {
            return;
        }
        layoutParams.width = max;
        layoutParams.height = max;
        setLayoutParams(layoutParams);
    }

    private void setBackgroundCompat(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }
}
