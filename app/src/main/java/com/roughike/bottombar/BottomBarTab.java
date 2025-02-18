package com.roughike.bottombar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;

/* loaded from: classes.dex */
public class BottomBarTab extends LinearLayout {
    private static final float ACTIVE_TITLE_SCALE = 1.0f;
    private static final long ANIMATION_DURATION = 150;
    private static final float INACTIVE_FIXED_TITLE_SCALE = 0.86f;
    static final String STATE_BADGE_COUNT = "STATE_BADGE_COUNT_FOR_TAB_";
    private float activeAlpha;
    private int activeColor;
    BottomBarBadge badge;
    private int badgeBackgroundColor;
    private boolean badgeHidesWhenActive;
    private int barColorWhenSelected;
    private final int eightDps;
    private int iconResId;
    private AppCompatImageView iconView;
    private float inActiveAlpha;
    private int inActiveColor;
    private int indexInContainer;
    private boolean isActive;
    private final int sixDps;
    private final int sixteenDps;
    private String title;
    private int titleTextAppearanceResId;
    private Typeface titleTypeFace;
    private TextView titleView;
    private Type type;

    enum Type {
        FIXED,
        SHIFTING,
        TABLET
    }

    BottomBarTab(Context context) {
        super(context);
        this.type = Type.FIXED;
        this.sixDps = MiscUtils.dpToPixel(context, 6.0f);
        this.eightDps = MiscUtils.dpToPixel(context, 8.0f);
        this.sixteenDps = MiscUtils.dpToPixel(context, 16.0f);
    }

    void setConfig(Config config) {
        setInActiveAlpha(config.inActiveTabAlpha);
        setActiveAlpha(config.activeTabAlpha);
        setInActiveColor(config.inActiveTabColor);
        setActiveColor(config.activeTabColor);
        setBarColorWhenSelected(config.barColorWhenSelected);
        setBadgeBackgroundColor(config.badgeBackgroundColor);
        setBadgeHidesWhenActive(config.badgeHidesWhenSelected);
        setTitleTextAppearance(config.titleTextAppearance);
        setTitleTypeface(config.titleTypeFace);
    }

    void prepareLayout() {
        inflate(getContext(), getLayoutResource(), this);
        setOrientation(1);
        setGravity(1);
        setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        AppCompatImageView appCompatImageView = (AppCompatImageView) findViewById(R.id.bb_bottom_bar_icon);
        this.iconView = appCompatImageView;
        appCompatImageView.setImageResource(this.iconResId);
        if (this.type != Type.TABLET) {
            this.titleView = (TextView) findViewById(R.id.bb_bottom_bar_title);
            updateTitle();
        }
        updateCustomTextAppearance();
        updateCustomTypeface();
    }

    /* renamed from: com.roughike.bottombar.BottomBarTab$5, reason: invalid class name */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$roughike$bottombar$BottomBarTab$Type;

        static {
            int[] iArr = new int[Type.values().length];
            $SwitchMap$com$roughike$bottombar$BottomBarTab$Type = iArr;
            try {
                iArr[Type.FIXED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$roughike$bottombar$BottomBarTab$Type[Type.SHIFTING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$roughike$bottombar$BottomBarTab$Type[Type.TABLET.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    int getLayoutResource() {
        int i = AnonymousClass5.$SwitchMap$com$roughike$bottombar$BottomBarTab$Type[this.type.ordinal()];
        if (i == 1) {
            return R.layout.bb_bottom_bar_item_fixed;
        }
        if (i == 2) {
            return R.layout.bb_bottom_bar_item_shifting;
        }
        if (i == 3) {
            return R.layout.bb_bottom_bar_item_fixed_tablet;
        }
        throw new RuntimeException("Unknown BottomBarTab type.");
    }

    private void updateTitle() {
        TextView textView = this.titleView;
        if (textView != null) {
            textView.setText(this.title);
        }
    }

    private void updateCustomTextAppearance() {
        if (this.titleView == null || this.titleTextAppearanceResId == 0) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            this.titleView.setTextAppearance(this.titleTextAppearanceResId);
        } else {
            this.titleView.setTextAppearance(getContext(), this.titleTextAppearanceResId);
        }
        this.titleView.setTag(Integer.valueOf(this.titleTextAppearanceResId));
    }

    private void updateCustomTypeface() {
        TextView textView;
        Typeface typeface = this.titleTypeFace;
        if (typeface == null || (textView = this.titleView) == null) {
            return;
        }
        textView.setTypeface(typeface);
    }

    Type getType() {
        return this.type;
    }

    void setType(Type type) {
        this.type = type;
    }

    public ViewGroup getOuterView() {
        return (ViewGroup) getParent();
    }

    AppCompatImageView getIconView() {
        return this.iconView;
    }

    int getIconResId() {
        return this.iconResId;
    }

    void setIconResId(int i) {
        this.iconResId = i;
    }

    TextView getTitleView() {
        return this.titleView;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
        updateTitle();
    }

    public float getInActiveAlpha() {
        return this.inActiveAlpha;
    }

    public void setInActiveAlpha(float f) {
        this.inActiveAlpha = f;
        if (this.isActive) {
            return;
        }
        setAlphas(f);
    }

    public float getActiveAlpha() {
        return this.activeAlpha;
    }

    public void setActiveAlpha(float f) {
        this.activeAlpha = f;
        if (this.isActive) {
            setAlphas(f);
        }
    }

    public int getInActiveColor() {
        return this.inActiveColor;
    }

    public void setInActiveColor(int i) {
        this.inActiveColor = i;
        if (this.isActive) {
            return;
        }
        setColors(i);
    }

    public int getActiveColor() {
        return this.activeColor;
    }

    public void setActiveColor(int i) {
        this.activeColor = i;
        if (this.isActive) {
            setColors(i);
        }
    }

    public int getBarColorWhenSelected() {
        return this.barColorWhenSelected;
    }

    public void setBarColorWhenSelected(int i) {
        this.barColorWhenSelected = i;
    }

    public int getBadgeBackgroundColor() {
        return this.badgeBackgroundColor;
    }

    public void setBadgeBackgroundColor(int i) {
        this.badgeBackgroundColor = i;
        BottomBarBadge bottomBarBadge = this.badge;
        if (bottomBarBadge != null) {
            bottomBarBadge.setColoredCircleBackground(i);
        }
    }

    public boolean getBadgeHidesWhenActive() {
        return this.badgeHidesWhenActive;
    }

    public void setBadgeHidesWhenActive(boolean z) {
        this.badgeHidesWhenActive = z;
    }

    int getCurrentDisplayedIconColor() {
        if (this.iconView.getTag() instanceof Integer) {
            return ((Integer) this.iconView.getTag()).intValue();
        }
        return 0;
    }

    int getCurrentDisplayedTitleColor() {
        TextView textView = this.titleView;
        if (textView != null) {
            return textView.getCurrentTextColor();
        }
        return 0;
    }

    int getCurrentDisplayedTextAppearance() {
        Object tag = this.titleView.getTag();
        TextView textView = this.titleView;
        if (textView == null || !(tag instanceof Integer)) {
            return 0;
        }
        return ((Integer) textView.getTag()).intValue();
    }

    public void setBadgeCount(int i) {
        if (i <= 0) {
            BottomBarBadge bottomBarBadge = this.badge;
            if (bottomBarBadge != null) {
                bottomBarBadge.removeFromTab(this);
                this.badge = null;
                return;
            }
            return;
        }
        if (this.badge == null) {
            BottomBarBadge bottomBarBadge2 = new BottomBarBadge(getContext());
            this.badge = bottomBarBadge2;
            bottomBarBadge2.attachToTab(this, this.badgeBackgroundColor);
        }
        this.badge.setCount(i);
        if (this.isActive && this.badgeHidesWhenActive) {
            this.badge.hide();
        }
    }

    public void removeBadge() {
        setBadgeCount(0);
    }

    boolean isActive() {
        return this.isActive;
    }

    boolean hasActiveBadge() {
        return this.badge != null;
    }

    int getIndexInTabContainer() {
        return this.indexInContainer;
    }

    void setIndexInContainer(int i) {
        this.indexInContainer = i;
    }

    void setIconTint(int i) {
        this.iconView.setColorFilter(i);
    }

    public int getTitleTextAppearance() {
        return this.titleTextAppearanceResId;
    }

    void setTitleTextAppearance(int i) {
        this.titleTextAppearanceResId = i;
        updateCustomTextAppearance();
    }

    public void setTitleTypeface(Typeface typeface) {
        this.titleTypeFace = typeface;
        updateCustomTypeface();
    }

    public Typeface getTitleTypeFace() {
        return this.titleTypeFace;
    }

    void select(boolean z) {
        this.isActive = true;
        if (z) {
            setTopPaddingAnimated(this.iconView.getPaddingTop(), this.sixDps);
            animateIcon(this.activeAlpha);
            animateTitle(1.0f, this.activeAlpha);
            animateColors(this.inActiveColor, this.activeColor);
        } else {
            setTitleScale(1.0f);
            setTopPadding(this.sixDps);
            setColors(this.activeColor);
            setAlphas(this.activeAlpha);
        }
        setSelected(true);
        BottomBarBadge bottomBarBadge = this.badge;
        if (bottomBarBadge == null || !this.badgeHidesWhenActive) {
            return;
        }
        bottomBarBadge.hide();
    }

    void deselect(boolean z) {
        BottomBarBadge bottomBarBadge;
        this.isActive = false;
        boolean z2 = this.type == Type.SHIFTING;
        float f = z2 ? 0.0f : INACTIVE_FIXED_TITLE_SCALE;
        int i = z2 ? this.sixteenDps : this.eightDps;
        if (z) {
            setTopPaddingAnimated(this.iconView.getPaddingTop(), i);
            animateTitle(f, this.inActiveAlpha);
            animateIcon(this.inActiveAlpha);
            animateColors(this.activeColor, this.inActiveColor);
        } else {
            setTitleScale(f);
            setTopPadding(i);
            setColors(this.inActiveColor);
            setAlphas(this.inActiveAlpha);
        }
        setSelected(false);
        if (z2 || (bottomBarBadge = this.badge) == null || bottomBarBadge.isVisible()) {
            return;
        }
        this.badge.show();
    }

    private void animateColors(int i, int i2) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(i, i2);
        valueAnimator.setEvaluator(new ArgbEvaluator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.roughike.bottombar.BottomBarTab.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                BottomBarTab.this.setColors(((Integer) valueAnimator2.getAnimatedValue()).intValue());
            }
        });
        valueAnimator.setDuration(150L);
        valueAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setColors(int i) {
        AppCompatImageView appCompatImageView = this.iconView;
        if (appCompatImageView != null) {
            appCompatImageView.setColorFilter(i);
            this.iconView.setTag(Integer.valueOf(i));
        }
        TextView textView = this.titleView;
        if (textView != null) {
            textView.setTextColor(i);
        }
    }

    private void setAlphas(float f) {
        AppCompatImageView appCompatImageView = this.iconView;
        if (appCompatImageView != null) {
            ViewCompat.setAlpha(appCompatImageView, f);
        }
        TextView textView = this.titleView;
        if (textView != null) {
            ViewCompat.setAlpha(textView, f);
        }
    }

    void updateWidth(float f, boolean z) {
        BottomBarBadge bottomBarBadge;
        if (!z) {
            getLayoutParams().width = (int) f;
            if (this.isActive || (bottomBarBadge = this.badge) == null) {
                return;
            }
            bottomBarBadge.adjustPositionAndSize(this);
            this.badge.show();
            return;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(getWidth(), f);
        ofFloat.setDuration(150L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.roughike.bottombar.BottomBarTab.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = BottomBarTab.this.getLayoutParams();
                if (layoutParams == null) {
                    return;
                }
                layoutParams.width = Math.round(((Float) valueAnimator.getAnimatedValue()).floatValue());
                BottomBarTab.this.setLayoutParams(layoutParams);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.roughike.bottombar.BottomBarTab.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (BottomBarTab.this.isActive || BottomBarTab.this.badge == null) {
                    return;
                }
                BottomBarTab.this.badge.adjustPositionAndSize(BottomBarTab.this);
                BottomBarTab.this.badge.show();
            }
        });
        ofFloat.start();
    }

    private void updateBadgePosition() {
        BottomBarBadge bottomBarBadge = this.badge;
        if (bottomBarBadge != null) {
            bottomBarBadge.adjustPositionAndSize(this);
        }
    }

    private void setTopPaddingAnimated(int i, int i2) {
        if (this.type == Type.TABLET) {
            return;
        }
        ValueAnimator ofInt = ValueAnimator.ofInt(i, i2);
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.roughike.bottombar.BottomBarTab.4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                BottomBarTab.this.iconView.setPadding(BottomBarTab.this.iconView.getPaddingLeft(), ((Integer) valueAnimator.getAnimatedValue()).intValue(), BottomBarTab.this.iconView.getPaddingRight(), BottomBarTab.this.iconView.getPaddingBottom());
            }
        });
        ofInt.setDuration(150L);
        ofInt.start();
    }

    private void animateTitle(float f, float f2) {
        if (this.type == Type.TABLET) {
            return;
        }
        ViewPropertyAnimatorCompat scaleY = ViewCompat.animate(this.titleView).setDuration(150L).scaleX(f).scaleY(f);
        scaleY.alpha(f2);
        scaleY.start();
    }

    private void animateIcon(float f) {
        ViewCompat.animate(this.iconView).setDuration(150L).alpha(f).start();
    }

    private void setTopPadding(int i) {
        if (this.type == Type.TABLET) {
            return;
        }
        AppCompatImageView appCompatImageView = this.iconView;
        appCompatImageView.setPadding(appCompatImageView.getPaddingLeft(), i, this.iconView.getPaddingRight(), this.iconView.getPaddingBottom());
    }

    private void setTitleScale(float f) {
        if (this.type == Type.TABLET) {
            return;
        }
        ViewCompat.setScaleX(this.titleView, f);
        ViewCompat.setScaleY(this.titleView, f);
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        if (this.badge != null) {
            Bundle saveState = saveState();
            saveState.putParcelable("superstate", super.onSaveInstanceState());
            return saveState;
        }
        return super.onSaveInstanceState();
    }

    Bundle saveState() {
        Bundle bundle = new Bundle();
        bundle.putInt(STATE_BADGE_COUNT + getIndexInTabContainer(), this.badge.getCount());
        return bundle;
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            restoreState(bundle);
            parcelable = bundle.getParcelable("superstate");
        }
        super.onRestoreInstanceState(parcelable);
    }

    void restoreState(Bundle bundle) {
        setBadgeCount(bundle.getInt(STATE_BADGE_COUNT + getIndexInTabContainer()));
    }

    public static class Config {
        private final float activeTabAlpha;
        private final int activeTabColor;
        private final int badgeBackgroundColor;
        private boolean badgeHidesWhenSelected;
        private final int barColorWhenSelected;
        private final float inActiveTabAlpha;
        private final int inActiveTabColor;
        private final int titleTextAppearance;
        private final Typeface titleTypeFace;

        private Config(Builder builder) {
            this.badgeHidesWhenSelected = true;
            this.inActiveTabAlpha = builder.inActiveTabAlpha;
            this.activeTabAlpha = builder.activeTabAlpha;
            this.inActiveTabColor = builder.inActiveTabColor;
            this.activeTabColor = builder.activeTabColor;
            this.barColorWhenSelected = builder.barColorWhenSelected;
            this.badgeBackgroundColor = builder.badgeBackgroundColor;
            this.badgeHidesWhenSelected = builder.hidesBadgeWhenSelected;
            this.titleTextAppearance = builder.titleTextAppearance;
            this.titleTypeFace = builder.titleTypeFace;
        }

        public static class Builder {
            private float activeTabAlpha;
            private int activeTabColor;
            private int badgeBackgroundColor;
            private int barColorWhenSelected;
            private boolean hidesBadgeWhenSelected = true;
            private float inActiveTabAlpha;
            private int inActiveTabColor;
            private int titleTextAppearance;
            private Typeface titleTypeFace;

            public Builder inActiveTabAlpha(float f) {
                this.inActiveTabAlpha = f;
                return this;
            }

            public Builder activeTabAlpha(float f) {
                this.activeTabAlpha = f;
                return this;
            }

            public Builder inActiveTabColor(int i) {
                this.inActiveTabColor = i;
                return this;
            }

            public Builder activeTabColor(int i) {
                this.activeTabColor = i;
                return this;
            }

            public Builder barColorWhenSelected(int i) {
                this.barColorWhenSelected = i;
                return this;
            }

            public Builder badgeBackgroundColor(int i) {
                this.badgeBackgroundColor = i;
                return this;
            }

            public Builder hideBadgeWhenSelected(boolean z) {
                this.hidesBadgeWhenSelected = z;
                return this;
            }

            public Builder titleTextAppearance(int i) {
                this.titleTextAppearance = i;
                return this;
            }

            public Builder titleTypeFace(Typeface typeface) {
                this.titleTypeFace = typeface;
                return this;
            }

            public Config build() {
                return new Config(this);
            }
        }
    }
}
