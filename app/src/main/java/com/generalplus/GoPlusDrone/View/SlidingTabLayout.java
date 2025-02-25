package com.generalplus.GoPlusDrone.View;

import android.R;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import androidx.viewpager.widget.ViewPager;

import com.baoyz.swipemenulistview.SwipeMenuItem;

/* loaded from: classes.dex */
public class SlidingTabLayout extends HorizontalScrollView {
    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;
    private static final int TITLE_OFFSET_DIPS = 24;
    private final SlidingTabStrip mTabStrip;
    private int mTabViewIconId;
    private int mTabViewLayoutId;
    private int mTabViewTextViewId;
    private int mTitleOffset;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    public interface TabColorizer {
        int getDividerColor(int i);

        int getIndicatorColor(int i);
    }

    public SlidingTabLayout(Context context) {
        this(context, null);
    }

    public SlidingTabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);
        this.mTitleOffset = (int) (getResources().getDisplayMetrics().density * 24.0f);
        SlidingTabStrip slidingTabStrip = new SlidingTabStrip(context);
        this.mTabStrip = slidingTabStrip;
        addView(slidingTabStrip, -1, -2);
    }

    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        this.mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    public void setSelectedIndicatorColors(int... iArr) {
        this.mTabStrip.setSelectedIndicatorColors(iArr);
    }

    public void setDividerColors(int... iArr) {
        this.mTabStrip.setDividerColors(iArr);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mViewPagerPageChangeListener = onPageChangeListener;
    }

    public void setCustomTabView(int i, int i2, int i3) {
        this.mTabViewLayoutId = i;
        this.mTabViewTextViewId = i2;
        this.mTabViewIconId = i3;
    }

    public void setViewPager(ViewPager viewPager) {
        this.mTabStrip.removeAllViews();
        this.mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(17);
        textView.setTextSize(2, 12.0f);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        if (Build.VERSION.SDK_INT >= 11) {
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
            textView.setBackgroundResource(typedValue.resourceId);
        }
        if (Build.VERSION.SDK_INT >= 14) {
            textView.setAllCaps(true);
        }
        int i = (int) (getResources().getDisplayMetrics().density * 16.0f);
        textView.setPadding(i, i, i, i);
        return textView;
    }

    private void populateTabStrip() {
        View view;
        TextView textView;
        TabFragmentPagerAdapter tabFragmentPagerAdapter = (TabFragmentPagerAdapter) this.mViewPager.getAdapter();
        TabClickListener tabClickListener = new TabClickListener();
        int count = getResources().getDisplayMetrics().widthPixels / tabFragmentPagerAdapter.getCount();
        for (int i = 0; i < tabFragmentPagerAdapter.getCount(); i++) {
            if (this.mTabViewLayoutId != 0) {
                view = LayoutInflater.from(getContext()).inflate(this.mTabViewLayoutId, (ViewGroup) this.mTabStrip, false);
                textView = (TextView) view.findViewById(this.mTabViewTextViewId);
            } else {
                view = null;
                textView = null;
            }
            if (view == null) {
                view = createDefaultTabView(getContext());
            }
            if (textView == null && TextView.class.isInstance(view)) {
                textView = (TextView) view;
            }
            textView.setText(tabFragmentPagerAdapter.getPageTitle(i));
            view.setOnClickListener(tabClickListener);
            view.setLayoutParams(new LayoutParams(count, -2));
            this.mTabStrip.addView(view);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewPager viewPager = this.mViewPager;
        if (viewPager != null) {
            scrollToTab(viewPager.getCurrentItem(), 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scrollToTab(int i, int i2) {
        View childAt;
        int childCount = this.mTabStrip.getChildCount();
        if (childCount == 0 || i < 0 || i >= childCount || (childAt = this.mTabStrip.getChildAt(i)) == null) {
            return;
        }
        int left = childAt.getLeft() + i2;
        if (i > 0 || i2 > 0) {
            left -= this.mTitleOffset;
        }
        scrollTo(left, 0);
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        private InternalViewPagerListener() {
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrolled(int i, float f, int i2) {
            int childCount = SlidingTabLayout.this.mTabStrip.getChildCount();
            if (childCount == 0 || i < 0 || i >= childCount) {
                return;
            }
            SlidingTabLayout.this.mTabStrip.onViewPagerPageChanged(i, f);
            SwipeMenuItem r0 = null;
            SlidingTabLayout.this.scrollToTab(i, SlidingTabLayout.this.mTabStrip.getChildAt(i) != null ? (int) (r0.getWidth() * f) : 0);
            if (SlidingTabLayout.this.mViewPagerPageChangeListener != null) {
                SlidingTabLayout.this.mViewPagerPageChangeListener.onPageScrolled(i, f, i2);
            }
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrollStateChanged(int i) {
            this.mScrollState = i;
            if (SlidingTabLayout.this.mViewPagerPageChangeListener != null) {
                SlidingTabLayout.this.mViewPagerPageChangeListener.onPageScrollStateChanged(i);
            }
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageSelected(int i) {
            if (this.mScrollState == 0) {
                SlidingTabLayout.this.mTabStrip.onViewPagerPageChanged(i, 0.0f);
                SlidingTabLayout.this.scrollToTab(i, 0);
            }
            if (SlidingTabLayout.this.mViewPagerPageChangeListener != null) {
                SlidingTabLayout.this.mViewPagerPageChangeListener.onPageSelected(i);
            }
        }
    }

    private class TabClickListener implements OnClickListener {
        private TabClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            for (int i = 0; i < SlidingTabLayout.this.mTabStrip.getChildCount(); i++) {
                if (view == SlidingTabLayout.this.mTabStrip.getChildAt(i)) {
                    SlidingTabLayout.this.mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }
}
