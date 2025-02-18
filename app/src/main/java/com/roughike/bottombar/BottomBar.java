package com.roughike.bottombar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import com.roughike.bottombar.BatchTabPropertyApplier;
import com.roughike.bottombar.BottomBarTab;
import java.util.List;

/* loaded from: classes.dex */
public class BottomBar extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {
    private static final int BEHAVIOR_DRAW_UNDER_NAV = 4;
    private static final int BEHAVIOR_NONE = 0;
    private static final int BEHAVIOR_SHIFTING = 1;
    private static final int BEHAVIOR_SHY = 2;
    private static final float DEFAULT_INACTIVE_SHIFTING_TAB_ALPHA = 0.6f;
    private static final String STATE_CURRENT_SELECTED_TAB = "STATE_CURRENT_SELECTED_TAB";
    private int activeShiftingItemWidth;
    private float activeTabAlpha;
    private int activeTabColor;
    private View backgroundOverlay;
    private int badgeBackgroundColor;
    private BatchTabPropertyApplier batchPropertyApplier;
    private int behaviors;
    private int currentBackgroundColor;
    private int currentTabPosition;
    private BottomBarTab[] currentTabs;
    private int defaultBackgroundColor;
    private boolean hideBadgeWhenActive;
    private boolean ignoreTabReselectionListener;
    private int inActiveShiftingItemWidth;
    private float inActiveTabAlpha;
    private int inActiveTabColor;
    private boolean isComingFromRestoredState;
    private boolean isTabletMode;
    private int maxFixedItemWidth;
    private boolean navBarAccountedHeightCalculated;
    private OnTabReselectListener onTabReselectListener;
    private OnTabSelectListener onTabSelectListener;
    private ViewGroup outerContainer;
    private int primaryColor;
    private int screenWidth;
    private View shadowView;
    private boolean showShadow;
    private boolean shyHeightAlreadyCalculated;
    private ViewGroup tabContainer;
    private TabSelectionInterceptor tabSelectionInterceptor;
    private int tabXmlResource;
    private int tenDp;
    private int titleTextAppearance;
    private Typeface titleTypeFace;

    public BottomBar(Context context) {
        super(context);
        this.defaultBackgroundColor = -1;
        init(context, null);
    }

    public BottomBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.defaultBackgroundColor = -1;
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        this.batchPropertyApplier = new BatchTabPropertyApplier(this);
        populateAttributes(context, attributeSet);
        initializeViews();
        determineInitialBackgroundColor();
        int i = this.tabXmlResource;
        if (i != 0) {
            setItems(i);
        }
    }

    private void populateAttributes(Context context, AttributeSet attributeSet) {
        this.primaryColor = MiscUtils.getColor(getContext(), R.attr.colorPrimary);
        this.screenWidth = MiscUtils.getScreenWidth(getContext());
        this.tenDp = MiscUtils.dpToPixel(getContext(), 10.0f);
        this.maxFixedItemWidth = MiscUtils.dpToPixel(getContext(), 168.0f);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.BottomBar, 0, 0);
        try {
            this.tabXmlResource = obtainStyledAttributes.getResourceId(R.styleable.BottomBar_bb_tabXmlResource, 0);
            this.isTabletMode = obtainStyledAttributes.getBoolean(R.styleable.BottomBar_bb_tabletMode, false);
            this.behaviors = obtainStyledAttributes.getInteger(R.styleable.BottomBar_bb_behavior, 0);
            this.inActiveTabAlpha = obtainStyledAttributes.getFloat(R.styleable.BottomBar_bb_inActiveTabAlpha, isShiftingMode() ? DEFAULT_INACTIVE_SHIFTING_TAB_ALPHA : 1.0f);
            this.activeTabAlpha = obtainStyledAttributes.getFloat(R.styleable.BottomBar_bb_activeTabAlpha, 1.0f);
            int i = -1;
            int color = isShiftingMode() ? -1 : ContextCompat.getColor(context, R.color.bb_inActiveBottomBarItemColor);
            if (!isShiftingMode()) {
                i = this.primaryColor;
            }
            this.inActiveTabColor = obtainStyledAttributes.getColor(R.styleable.BottomBar_bb_inActiveTabColor, color);
            this.activeTabColor = obtainStyledAttributes.getColor(R.styleable.BottomBar_bb_activeTabColor, i);
            this.badgeBackgroundColor = obtainStyledAttributes.getColor(R.styleable.BottomBar_bb_badgeBackgroundColor, SupportMenu.CATEGORY_MASK);
            this.hideBadgeWhenActive = obtainStyledAttributes.getBoolean(R.styleable.BottomBar_bb_badgesHideWhenActive, true);
            this.titleTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.BottomBar_bb_titleTextAppearance, 0);
            this.titleTypeFace = getTypeFaceFromAsset(obtainStyledAttributes.getString(R.styleable.BottomBar_bb_titleTypeFace));
            this.showShadow = obtainStyledAttributes.getBoolean(R.styleable.BottomBar_bb_showShadow, true);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    private boolean isShiftingMode() {
        return !this.isTabletMode && hasBehavior(1);
    }

    private boolean drawUnderNav() {
        return !this.isTabletMode && hasBehavior(4) && NavbarUtils.shouldDrawBehindNavbar(getContext());
    }

    private boolean isShy() {
        return !this.isTabletMode && hasBehavior(2);
    }

    private boolean hasBehavior(int i) {
        int i2 = this.behaviors;
        return (i | i2) == i2;
    }

    private Typeface getTypeFaceFromAsset(String str) {
        if (str != null) {
            return Typeface.createFromAsset(getContext().getAssets(), str);
        }
        return null;
    }

    private void initializeViews() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this.isTabletMode ? -2 : -1, this.isTabletMode ? -1 : -2);
        setLayoutParams(layoutParams);
        setOrientation(!this.isTabletMode ? 1 : 0);
        ViewCompat.setElevation(this, MiscUtils.dpToPixel(getContext(), 8.0f));
        View inflate = inflate(getContext(), this.isTabletMode ? R.layout.bb_bottom_bar_item_container_tablet : R.layout.bb_bottom_bar_item_container, this);
        inflate.setLayoutParams(layoutParams);
        this.backgroundOverlay = inflate.findViewById(R.id.bb_bottom_bar_background_overlay);
        this.outerContainer = (ViewGroup) inflate.findViewById(R.id.bb_bottom_bar_outer_container);
        this.tabContainer = (ViewGroup) inflate.findViewById(R.id.bb_bottom_bar_item_container);
        View findViewById = inflate.findViewById(R.id.bb_bottom_bar_shadow);
        this.shadowView = findViewById;
        if (this.showShadow) {
            return;
        }
        findViewById.setVisibility(8);
    }

    private void determineInitialBackgroundColor() {
        if (isShiftingMode()) {
            this.defaultBackgroundColor = this.primaryColor;
        }
        Drawable background = getBackground();
        if (background != null && (background instanceof ColorDrawable)) {
            this.defaultBackgroundColor = ((ColorDrawable) background).getColor();
            setBackgroundColor(0);
        }
    }

    public void setItems(int i) {
        setItems(i, null);
    }

    public void setItems(int i, BottomBarTab.Config config) {
        if (i == 0) {
            throw new RuntimeException("No items specified for the BottomBar!");
        }
        if (config == null) {
            config = getTabConfig();
        }
        updateItems(new TabParser(getContext(), config, i).parseTabs());
    }

    private BottomBarTab.Config getTabConfig() {
        return new BottomBarTab.Config.Builder().inActiveTabAlpha(this.inActiveTabAlpha).activeTabAlpha(this.activeTabAlpha).inActiveTabColor(this.inActiveTabColor).activeTabColor(this.activeTabColor).barColorWhenSelected(this.defaultBackgroundColor).badgeBackgroundColor(this.badgeBackgroundColor).hideBadgeWhenSelected(this.hideBadgeWhenActive).titleTextAppearance(this.titleTextAppearance).titleTypeFace(this.titleTypeFace).build();
    }

    private void updateItems(List<BottomBarTab> list) {
        BottomBarTab.Type type;
        this.tabContainer.removeAllViews();
        BottomBarTab[] bottomBarTabArr = new BottomBarTab[list.size()];
        int i = 0;
        int i2 = 0;
        for (BottomBarTab bottomBarTab : list) {
            if (isShiftingMode()) {
                type = BottomBarTab.Type.SHIFTING;
            } else if (this.isTabletMode) {
                type = BottomBarTab.Type.TABLET;
            } else {
                type = BottomBarTab.Type.FIXED;
            }
            bottomBarTab.setType(type);
            bottomBarTab.prepareLayout();
            if (i == this.currentTabPosition) {
                bottomBarTab.select(false);
                handleBackgroundColorChange(bottomBarTab, false);
            } else {
                bottomBarTab.deselect(false);
            }
            if (!this.isTabletMode) {
                if (bottomBarTab.getWidth() > i2) {
                    i2 = bottomBarTab.getWidth();
                }
                bottomBarTabArr[i] = bottomBarTab;
            } else {
                this.tabContainer.addView(bottomBarTab);
            }
            bottomBarTab.setOnClickListener(this);
            bottomBarTab.setOnLongClickListener(this);
            i++;
        }
        this.currentTabs = bottomBarTabArr;
        if (this.isTabletMode) {
            return;
        }
        resizeTabsToCorrectSizes(bottomBarTabArr);
    }

    private void resizeTabsToCorrectSizes(BottomBarTab[] bottomBarTabArr) {
        int pixelToDp = MiscUtils.pixelToDp(getContext(), getWidth());
        if (pixelToDp <= 0 || pixelToDp > this.screenWidth) {
            pixelToDp = this.screenWidth;
        }
        int min = Math.min(MiscUtils.dpToPixel(getContext(), pixelToDp / bottomBarTabArr.length), this.maxFixedItemWidth);
        double d = min;
        this.inActiveShiftingItemWidth = (int) (0.9d * d);
        this.activeShiftingItemWidth = (int) (d + ((bottomBarTabArr.length - 1) * 0.1d * d));
        int round = Math.round(getContext().getResources().getDimension(R.dimen.bb_height));
        for (BottomBarTab bottomBarTab : bottomBarTabArr) {
            ViewGroup.LayoutParams layoutParams = bottomBarTab.getLayoutParams();
            layoutParams.height = round;
            if (isShiftingMode()) {
                if (bottomBarTab.isActive()) {
                    layoutParams.width = this.activeShiftingItemWidth;
                } else {
                    layoutParams.width = this.inActiveShiftingItemWidth;
                }
            } else {
                layoutParams.width = min;
            }
            if (bottomBarTab.getParent() == null) {
                this.tabContainer.addView(bottomBarTab);
            }
            bottomBarTab.requestLayout();
        }
    }

    public void setTabSelectionInterceptor(TabSelectionInterceptor tabSelectionInterceptor) {
        this.tabSelectionInterceptor = tabSelectionInterceptor;
    }

    public void removeOverrideTabSelectionListener() {
        this.tabSelectionInterceptor = null;
    }

    public void setOnTabSelectListener(OnTabSelectListener onTabSelectListener) {
        setOnTabSelectListener(onTabSelectListener, true);
    }

    public void setOnTabSelectListener(OnTabSelectListener onTabSelectListener, boolean z) {
        this.onTabSelectListener = onTabSelectListener;
        if (!z || getTabCount() <= 0) {
            return;
        }
        onTabSelectListener.onTabSelected(getCurrentTabId());
    }

    public void removeOnTabSelectListener() {
        this.onTabSelectListener = null;
    }

    public void setOnTabReselectListener(OnTabReselectListener onTabReselectListener) {
        this.onTabReselectListener = onTabReselectListener;
    }

    public void removeOnTabReselectListener() {
        this.onTabReselectListener = null;
    }

    public void setDefaultTab(int i) {
        setDefaultTabPosition(findPositionForTabWithId(i));
    }

    public void setDefaultTabPosition(int i) {
        if (this.isComingFromRestoredState) {
            return;
        }
        selectTabAtPosition(i);
    }

    public void selectTabWithId(int i) {
        selectTabAtPosition(findPositionForTabWithId(i));
    }

    public void selectTabAtPosition(int i) {
        selectTabAtPosition(i, false);
    }

    public void selectTabAtPosition(int i, boolean z) {
        if (i > getTabCount() - 1 || i < 0) {
            throw new IndexOutOfBoundsException("Can't select tab at position " + i + ". This BottomBar has no items at that position.");
        }
        BottomBarTab currentTab = getCurrentTab();
        BottomBarTab tabAtPosition = getTabAtPosition(i);
        currentTab.deselect(z);
        tabAtPosition.select(z);
        updateSelectedTab(i);
        shiftingMagic(currentTab, tabAtPosition, z);
        handleBackgroundColorChange(tabAtPosition, z);
    }

    public int getTabCount() {
        return this.tabContainer.getChildCount();
    }

    public BottomBarTab getCurrentTab() {
        return getTabAtPosition(getCurrentTabPosition());
    }

    public BottomBarTab getTabAtPosition(int i) {
        View childAt = this.tabContainer.getChildAt(i);
        if (childAt instanceof BadgeContainer) {
            return findTabInLayout((BadgeContainer) childAt);
        }
        return (BottomBarTab) childAt;
    }

    public int getCurrentTabId() {
        return getCurrentTab().getId();
    }

    public int getCurrentTabPosition() {
        return this.currentTabPosition;
    }

    public int findPositionForTabWithId(int i) {
        return getTabWithId(i).getIndexInTabContainer();
    }

    public BottomBarTab getTabWithId(int i) {
        return (BottomBarTab) this.tabContainer.findViewById(i);
    }

    public void setInActiveTabAlpha(float f) {
        this.inActiveTabAlpha = f;
        this.batchPropertyApplier.applyToAllTabs(new BatchTabPropertyApplier.TabPropertyUpdater() { // from class: com.roughike.bottombar.BottomBar.1
            @Override // com.roughike.bottombar.BatchTabPropertyApplier.TabPropertyUpdater
            public void update(BottomBarTab bottomBarTab) {
                bottomBarTab.setInActiveAlpha(BottomBar.this.inActiveTabAlpha);
            }
        });
    }

    public void setActiveTabAlpha(float f) {
        this.activeTabAlpha = f;
        this.batchPropertyApplier.applyToAllTabs(new BatchTabPropertyApplier.TabPropertyUpdater() { // from class: com.roughike.bottombar.BottomBar.2
            @Override // com.roughike.bottombar.BatchTabPropertyApplier.TabPropertyUpdater
            public void update(BottomBarTab bottomBarTab) {
                bottomBarTab.setActiveAlpha(BottomBar.this.activeTabAlpha);
            }
        });
    }

    public void setInActiveTabColor(int i) {
        this.inActiveTabColor = i;
        this.batchPropertyApplier.applyToAllTabs(new BatchTabPropertyApplier.TabPropertyUpdater() { // from class: com.roughike.bottombar.BottomBar.3
            @Override // com.roughike.bottombar.BatchTabPropertyApplier.TabPropertyUpdater
            public void update(BottomBarTab bottomBarTab) {
                bottomBarTab.setInActiveColor(BottomBar.this.inActiveTabColor);
            }
        });
    }

    public void setActiveTabColor(int i) {
        this.activeTabColor = i;
        this.batchPropertyApplier.applyToAllTabs(new BatchTabPropertyApplier.TabPropertyUpdater() { // from class: com.roughike.bottombar.BottomBar.4
            @Override // com.roughike.bottombar.BatchTabPropertyApplier.TabPropertyUpdater
            public void update(BottomBarTab bottomBarTab) {
                bottomBarTab.setActiveColor(BottomBar.this.activeTabColor);
            }
        });
    }

    public void setBadgeBackgroundColor(int i) {
        this.badgeBackgroundColor = i;
        this.batchPropertyApplier.applyToAllTabs(new BatchTabPropertyApplier.TabPropertyUpdater() { // from class: com.roughike.bottombar.BottomBar.5
            @Override // com.roughike.bottombar.BatchTabPropertyApplier.TabPropertyUpdater
            public void update(BottomBarTab bottomBarTab) {
                bottomBarTab.setBadgeBackgroundColor(BottomBar.this.badgeBackgroundColor);
            }
        });
    }

    public void setBadgesHideWhenActive(final boolean z) {
        this.hideBadgeWhenActive = z;
        this.batchPropertyApplier.applyToAllTabs(new BatchTabPropertyApplier.TabPropertyUpdater() { // from class: com.roughike.bottombar.BottomBar.6
            @Override // com.roughike.bottombar.BatchTabPropertyApplier.TabPropertyUpdater
            public void update(BottomBarTab bottomBarTab) {
                bottomBarTab.setBadgeHidesWhenActive(z);
            }
        });
    }

    public void setTabTitleTextAppearance(int i) {
        this.titleTextAppearance = i;
        this.batchPropertyApplier.applyToAllTabs(new BatchTabPropertyApplier.TabPropertyUpdater() { // from class: com.roughike.bottombar.BottomBar.7
            @Override // com.roughike.bottombar.BatchTabPropertyApplier.TabPropertyUpdater
            public void update(BottomBarTab bottomBarTab) {
                bottomBarTab.setTitleTextAppearance(BottomBar.this.titleTextAppearance);
            }
        });
    }

    public void setTabTitleTypeface(String str) {
        setTabTitleTypeface(getTypeFaceFromAsset(str));
    }

    public void setTabTitleTypeface(Typeface typeface) {
        this.titleTypeFace = typeface;
        this.batchPropertyApplier.applyToAllTabs(new BatchTabPropertyApplier.TabPropertyUpdater() { // from class: com.roughike.bottombar.BottomBar.8
            @Override // com.roughike.bottombar.BatchTabPropertyApplier.TabPropertyUpdater
            public void update(BottomBarTab bottomBarTab) {
                bottomBarTab.setTitleTypeface(BottomBar.this.titleTypeFace);
            }
        });
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            if (!this.isTabletMode) {
                resizeTabsToCorrectSizes(this.currentTabs);
            }
            updateTitleBottomPadding();
            if (isShy()) {
                initializeShyBehavior();
            }
            if (drawUnderNav()) {
                resizeForDrawingUnderNavbar();
            }
        }
    }

    private void updateTitleBottomPadding() {
        int tabCount = getTabCount();
        if (this.tabContainer == null || tabCount == 0 || !isShiftingMode()) {
            return;
        }
        for (int i = 0; i < tabCount; i++) {
            TextView titleView = getTabAtPosition(i).getTitleView();
            if (titleView != null) {
                int height = this.tenDp - (titleView.getHeight() - titleView.getBaseline());
                if (height > 0) {
                    titleView.setPadding(titleView.getPaddingLeft(), titleView.getPaddingTop(), titleView.getPaddingRight(), height + titleView.getPaddingBottom());
                }
            }
        }
    }

    private void initializeShyBehavior() {
        int height;
        ViewParent parent = getParent();
        if (!(parent != null && (parent instanceof CoordinatorLayout))) {
            throw new RuntimeException("In order to have shy behavior, the BottomBar must be a direct child of a CoordinatorLayout.");
        }
        if (this.shyHeightAlreadyCalculated || (height = getHeight()) == 0) {
            return;
        }
        updateShyHeight(height);
        this.shyHeightAlreadyCalculated = true;
    }

    private void updateShyHeight(int i) {
        ((CoordinatorLayout.LayoutParams) getLayoutParams()).setBehavior(new BottomNavigationBehavior(i, 0, false));
    }

    private void resizeForDrawingUnderNavbar() {
        int height;
        if (Build.VERSION.SDK_INT < 19 || (height = getHeight()) == 0 || this.navBarAccountedHeightCalculated) {
            return;
        }
        this.navBarAccountedHeightCalculated = true;
        this.tabContainer.getLayoutParams().height = height;
        int navbarHeight = height + NavbarUtils.getNavbarHeight(getContext());
        getLayoutParams().height = navbarHeight;
        if (isShy()) {
            updateShyHeight(navbarHeight);
        }
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        Bundle saveState = saveState();
        saveState.putParcelable("superstate", super.onSaveInstanceState());
        return saveState;
    }

    Bundle saveState() {
        Bundle bundle = new Bundle();
        bundle.putInt(STATE_CURRENT_SELECTED_TAB, this.currentTabPosition);
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
        if (bundle != null) {
            this.isComingFromRestoredState = true;
            this.ignoreTabReselectionListener = true;
            selectTabAtPosition(bundle.getInt(STATE_CURRENT_SELECTED_TAB, this.currentTabPosition), false);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view instanceof BottomBarTab) {
            handleClick((BottomBarTab) view);
        }
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        return !(view instanceof BottomBarTab) || handleLongClick((BottomBarTab) view);
    }

    private BottomBarTab findTabInLayout(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof BottomBarTab) {
                return (BottomBarTab) childAt;
            }
        }
        return null;
    }

    private void handleClick(BottomBarTab bottomBarTab) {
        BottomBarTab currentTab = getCurrentTab();
        TabSelectionInterceptor tabSelectionInterceptor = this.tabSelectionInterceptor;
        if (tabSelectionInterceptor == null || !tabSelectionInterceptor.shouldInterceptTabSelection(currentTab.getId(), bottomBarTab.getId())) {
            currentTab.deselect(true);
            bottomBarTab.select(true);
            shiftingMagic(currentTab, bottomBarTab, true);
            handleBackgroundColorChange(bottomBarTab, true);
            updateSelectedTab(bottomBarTab.getIndexInTabContainer());
        }
    }

    private boolean handleLongClick(BottomBarTab bottomBarTab) {
        if ((!isShiftingMode() && !this.isTabletMode) || bottomBarTab.isActive()) {
            return true;
        }
        Toast.makeText(getContext(), bottomBarTab.getTitle(), 0).show();
        return true;
    }

    private void updateSelectedTab(int i) {
        int id = getTabAtPosition(i).getId();
        if (i != this.currentTabPosition) {
            OnTabSelectListener onTabSelectListener = this.onTabSelectListener;
            if (onTabSelectListener != null) {
                onTabSelectListener.onTabSelected(id);
            }
        } else {
            OnTabReselectListener onTabReselectListener = this.onTabReselectListener;
            if (onTabReselectListener != null && !this.ignoreTabReselectionListener) {
                onTabReselectListener.onTabReSelected(id);
            }
        }
        this.currentTabPosition = i;
        if (this.ignoreTabReselectionListener) {
            this.ignoreTabReselectionListener = false;
        }
    }

    private void shiftingMagic(BottomBarTab bottomBarTab, BottomBarTab bottomBarTab2, boolean z) {
        if (isShiftingMode()) {
            bottomBarTab.updateWidth(this.inActiveShiftingItemWidth, z);
            bottomBarTab2.updateWidth(this.activeShiftingItemWidth, z);
        }
    }

    private void handleBackgroundColorChange(BottomBarTab bottomBarTab, boolean z) {
        int barColorWhenSelected = bottomBarTab.getBarColorWhenSelected();
        if (this.currentBackgroundColor == barColorWhenSelected) {
            return;
        }
        if (!z) {
            this.outerContainer.setBackgroundColor(barColorWhenSelected);
            return;
        }
        boolean hasActiveBadge = bottomBarTab.hasActiveBadge();
        ViewGroup viewGroup = bottomBarTab;
        if (hasActiveBadge) {
            viewGroup = bottomBarTab.getOuterView();
        }
        animateBGColorChange(viewGroup, barColorWhenSelected);
        this.currentBackgroundColor = barColorWhenSelected;
    }

    private void animateBGColorChange(View view, int i) {
        prepareForBackgroundColorAnimation(i);
        if (Build.VERSION.SDK_INT >= 21) {
            if (this.outerContainer.isAttachedToWindow()) {
                backgroundCircularRevealAnimation(view, i);
                return;
            }
            return;
        }
        backgroundCrossfadeAnimation(i);
    }

    private void prepareForBackgroundColorAnimation(int i) {
        this.outerContainer.clearAnimation();
        this.backgroundOverlay.clearAnimation();
        this.backgroundOverlay.setBackgroundColor(i);
        this.backgroundOverlay.setVisibility(0);
    }

    private void backgroundCircularRevealAnimation(View view, final int i) {
        Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this.backgroundOverlay, (int) (ViewCompat.getX(view) + (view.getMeasuredWidth() / 2)), (this.isTabletMode ? (int) ViewCompat.getY(view) : 0) + (view.getMeasuredHeight() / 2), 0, this.isTabletMode ? this.outerContainer.getHeight() : this.outerContainer.getWidth());
        if (this.isTabletMode) {
            createCircularReveal.setDuration(500L);
        }
        createCircularReveal.addListener(new AnimatorListenerAdapter() { // from class: com.roughike.bottombar.BottomBar.9
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                onEnd();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                onEnd();
            }

            private void onEnd() {
                BottomBar.this.outerContainer.setBackgroundColor(i);
                BottomBar.this.backgroundOverlay.setVisibility(4);
                ViewCompat.setAlpha(BottomBar.this.backgroundOverlay, 1.0f);
            }
        });
        createCircularReveal.start();
    }

    private void backgroundCrossfadeAnimation(final int i) {
        ViewCompat.setAlpha(this.backgroundOverlay, 0.0f);
        ViewCompat.animate(this.backgroundOverlay).alpha(1.0f).setListener(new ViewPropertyAnimatorListenerAdapter() { // from class: com.roughike.bottombar.BottomBar.10
            @Override // androidx.core.view.ViewPropertyAnimatorListenerAdapter, androidx.core.view.ViewPropertyAnimatorListener
            public void onAnimationEnd(View view) {
                onEnd();
            }

            @Override // androidx.core.view.ViewPropertyAnimatorListenerAdapter, androidx.core.view.ViewPropertyAnimatorListener
            public void onAnimationCancel(View view) {
                onEnd();
            }

            private void onEnd() {
                BottomBar.this.outerContainer.setBackgroundColor(i);
                BottomBar.this.backgroundOverlay.setVisibility(4);
                ViewCompat.setAlpha(BottomBar.this.backgroundOverlay, 1.0f);
            }
        }).start();
    }

    private void toggleShyVisibility(boolean z) {
        BottomNavigationBehavior from = BottomNavigationBehavior.from(this);
        if (from != null) {
            from.setHidden(this, z);
        }
    }
}
