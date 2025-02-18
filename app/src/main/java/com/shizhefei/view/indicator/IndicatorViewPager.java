package com.shizhefei.view.indicator;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.shizhefei.view.viewpager.RecyclingPagerAdapter;
import com.shizhefei.view.viewpager.SViewPager;

/* loaded from: classes2.dex */
public class IndicatorViewPager {
    private IndicatorPagerAdapter adapter;
    private boolean anim;
    protected Indicator indicatorView;
    protected OnIndicatorPageChangeListener onIndicatorPageChangeListener;
    protected ViewPager viewPager;

    public interface IndicatorPagerAdapter {
        Indicator.IndicatorAdapter getIndicatorAdapter();

        PagerAdapter getPagerAdapter();

        void notifyDataSetChanged();
    }

    public interface OnIndicatorPageChangeListener {
        void onIndicatorPageChange(int i, int i2);
    }

    public IndicatorViewPager(Indicator indicator, ViewPager viewPager) {
        this(indicator, viewPager, true);
    }

    public IndicatorViewPager(Indicator indicator, ViewPager viewPager, boolean z) {
        this.anim = true;
        this.indicatorView = indicator;
        this.viewPager = viewPager;
        indicator.setItemClickable(z);
        iniOnItemSelectedListener();
        iniOnPageChangeListener();
    }

    public void setClickIndicatorAnim(boolean z) {
        this.anim = z;
    }

    protected void iniOnItemSelectedListener() {
        this.indicatorView.setOnItemSelectListener(new Indicator.OnItemSelectedListener() { // from class: com.shizhefei.view.indicator.IndicatorViewPager.1
            @Override // com.shizhefei.view.indicator.Indicator.OnItemSelectedListener
            public void onItemSelected(View view, int i, int i2) {
                if (!(IndicatorViewPager.this.viewPager instanceof SViewPager)) {
                    IndicatorViewPager.this.viewPager.setCurrentItem(i, IndicatorViewPager.this.anim);
                } else {
                    IndicatorViewPager.this.viewPager.setCurrentItem(i, ((SViewPager) IndicatorViewPager.this.viewPager).isCanScroll());
                }
            }
        });
    }

    protected void iniOnPageChangeListener() {
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.shizhefei.view.indicator.IndicatorViewPager.2
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                IndicatorViewPager.this.indicatorView.setCurrentItem(i, true);
                if (IndicatorViewPager.this.onIndicatorPageChangeListener != null) {
                    IndicatorViewPager.this.onIndicatorPageChangeListener.onIndicatorPageChange(IndicatorViewPager.this.indicatorView.getPreSelectItem(), i);
                }
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
                IndicatorViewPager.this.indicatorView.onPageScrolled(i, f, i2);
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
                IndicatorViewPager.this.indicatorView.onPageScrollStateChanged(i);
            }
        });
    }

    public void setAdapter(IndicatorPagerAdapter indicatorPagerAdapter) {
        this.adapter = indicatorPagerAdapter;
        this.viewPager.setAdapter(indicatorPagerAdapter.getPagerAdapter());
        this.indicatorView.setAdapter(indicatorPagerAdapter.getIndicatorAdapter());
    }

    public void setCurrentItem(int i, boolean z) {
        this.viewPager.setCurrentItem(i, z);
        this.indicatorView.setCurrentItem(i, z);
    }

    public void setOnIndicatorPageChangeListener(OnIndicatorPageChangeListener onIndicatorPageChangeListener) {
        this.onIndicatorPageChangeListener = onIndicatorPageChangeListener;
    }

    public void setIndicatorOnTransitionListener(Indicator.OnTransitionListener onTransitionListener) {
        this.indicatorView.setOnTransitionListener(onTransitionListener);
    }

    public void setIndicatorScrollBar(ScrollBar scrollBar) {
        this.indicatorView.setScrollBar(scrollBar);
    }

    public void setPageOffscreenLimit(int i) {
        this.viewPager.setOffscreenPageLimit(i);
    }

    public void setPageMargin(int i) {
        this.viewPager.setPageMargin(i);
    }

    public void setPageMarginDrawable(Drawable drawable) {
        this.viewPager.setPageMarginDrawable(drawable);
    }

    public void setPageMarginDrawable(int i) {
        this.viewPager.setPageMarginDrawable(i);
    }

    public int getPreSelectItem() {
        return this.indicatorView.getPreSelectItem();
    }

    public int getCurrentItem() {
        return this.indicatorView.getCurrentItem();
    }

    public IndicatorPagerAdapter getAdapter() {
        return this.adapter;
    }

    public OnIndicatorPageChangeListener getOnIndicatorPageChangeListener() {
        return this.onIndicatorPageChangeListener;
    }

    public Indicator getIndicatorView() {
        return this.indicatorView;
    }

    public ViewPager getViewPager() {
        return this.viewPager;
    }

    public void notifyDataSetChanged() {
        IndicatorPagerAdapter indicatorPagerAdapter = this.adapter;
        if (indicatorPagerAdapter != null) {
            indicatorPagerAdapter.notifyDataSetChanged();
        }
    }

    static abstract class LoopAdapter implements IndicatorPagerAdapter {
        abstract int getCount();

        abstract int getRealPosition(int i);

        abstract void setLoop(boolean z);

        LoopAdapter() {
        }
    }

    public static abstract class IndicatorViewPagerAdapter extends LoopAdapter {
        private boolean loop;
        private RecyclingPagerAdapter pagerAdapter = new RecyclingPagerAdapter() { // from class: com.shizhefei.view.indicator.IndicatorViewPager.IndicatorViewPagerAdapter.1
            @Override // androidx.viewpager.widget.PagerAdapter
            public int getCount() {
                if (IndicatorViewPagerAdapter.this.getCount() == 0) {
                    return 0;
                }
                if (IndicatorViewPagerAdapter.this.loop) {
                    return 2147483547;
                }
                return IndicatorViewPagerAdapter.this.getCount();
            }

            @Override // com.shizhefei.view.viewpager.RecyclingPagerAdapter
            public View getView(int i, View view, ViewGroup viewGroup) {
                IndicatorViewPagerAdapter indicatorViewPagerAdapter = IndicatorViewPagerAdapter.this;
                return indicatorViewPagerAdapter.getViewForPage(indicatorViewPagerAdapter.getRealPosition(i), view, viewGroup);
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public float getPageWidth(int i) {
                IndicatorViewPagerAdapter indicatorViewPagerAdapter = IndicatorViewPagerAdapter.this;
                return indicatorViewPagerAdapter.getPageRatio(indicatorViewPagerAdapter.getRealPosition(i));
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public int getItemPosition(Object obj) {
                return IndicatorViewPagerAdapter.this.getItemPosition(obj);
            }

            @Override // com.shizhefei.view.viewpager.RecyclingPagerAdapter
            public int getItemViewType(int i) {
                IndicatorViewPagerAdapter indicatorViewPagerAdapter = IndicatorViewPagerAdapter.this;
                return indicatorViewPagerAdapter.getPageViewType(indicatorViewPagerAdapter.getRealPosition(i));
            }

            @Override // com.shizhefei.view.viewpager.RecyclingPagerAdapter
            public int getViewTypeCount() {
                return IndicatorViewPagerAdapter.this.getPageViewTypeCount();
            }
        };
        private Indicator.IndicatorAdapter indicatorAdapter = new Indicator.IndicatorAdapter() { // from class: com.shizhefei.view.indicator.IndicatorViewPager.IndicatorViewPagerAdapter.2
            @Override // com.shizhefei.view.indicator.Indicator.IndicatorAdapter
            public View getView(int i, View view, ViewGroup viewGroup) {
                return IndicatorViewPagerAdapter.this.getViewForTab(i, view, viewGroup);
            }

            @Override // com.shizhefei.view.indicator.Indicator.IndicatorAdapter
            public int getCount() {
                return IndicatorViewPagerAdapter.this.getCount();
            }
        };

        @Override // com.shizhefei.view.indicator.IndicatorViewPager.LoopAdapter
        public abstract int getCount();

        public int getItemPosition(Object obj) {
            return -1;
        }

        public float getPageRatio(int i) {
            return 1.0f;
        }

        public int getPageViewType(int i) {
            return 0;
        }

        public int getPageViewTypeCount() {
            return 1;
        }

        public abstract View getViewForPage(int i, View view, ViewGroup viewGroup);

        public abstract View getViewForTab(int i, View view, ViewGroup viewGroup);

        @Override // com.shizhefei.view.indicator.IndicatorViewPager.LoopAdapter
        int getRealPosition(int i) {
            if (getCount() == 0) {
                return 0;
            }
            return i % getCount();
        }

        @Override // com.shizhefei.view.indicator.IndicatorViewPager.LoopAdapter
        void setLoop(boolean z) {
            this.loop = z;
            this.indicatorAdapter.setIsLoop(z);
        }

        @Override // com.shizhefei.view.indicator.IndicatorViewPager.IndicatorPagerAdapter
        public void notifyDataSetChanged() {
            this.indicatorAdapter.notifyDataSetChanged();
            this.pagerAdapter.notifyDataSetChanged();
        }

        @Override // com.shizhefei.view.indicator.IndicatorViewPager.IndicatorPagerAdapter
        public PagerAdapter getPagerAdapter() {
            return this.pagerAdapter;
        }

        @Override // com.shizhefei.view.indicator.IndicatorViewPager.IndicatorPagerAdapter
        public Indicator.IndicatorAdapter getIndicatorAdapter() {
            return this.indicatorAdapter;
        }
    }

    public static abstract class IndicatorFragmentPagerAdapter extends LoopAdapter {
        private Indicator.IndicatorAdapter indicatorAdapter = new Indicator.IndicatorAdapter() { // from class: com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter.2
            @Override // com.shizhefei.view.indicator.Indicator.IndicatorAdapter
            public View getView(int i, View view, ViewGroup viewGroup) {
                return IndicatorFragmentPagerAdapter.this.getViewForTab(i, view, viewGroup);
            }

            @Override // com.shizhefei.view.indicator.Indicator.IndicatorAdapter
            public int getCount() {
                return IndicatorFragmentPagerAdapter.this.getCount();
            }
        };
        private boolean loop;
        private FragmentListPageAdapter pagerAdapter;

        public abstract int getCount();

        public abstract Fragment getFragmentForPage(int i);

        public int getItemPosition(Object obj) {
            return -1;
        }

        public float getPageRatio(int i) {
            return 1.0f;
        }

        public abstract View getViewForTab(int i, View view, ViewGroup viewGroup);

        @Override // com.shizhefei.view.indicator.IndicatorViewPager.LoopAdapter
        int getRealPosition(int i) {
            return i % getCount();
        }

        @Override // com.shizhefei.view.indicator.IndicatorViewPager.LoopAdapter
        void setLoop(boolean z) {
            this.loop = z;
            this.indicatorAdapter.setIsLoop(z);
        }

        public IndicatorFragmentPagerAdapter(FragmentManager fragmentManager) {
            this.pagerAdapter = new FragmentListPageAdapter(fragmentManager) { // from class: com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter.1
                @Override // androidx.viewpager.widget.PagerAdapter
                public int getCount() {
                    if (IndicatorFragmentPagerAdapter.this.getCount() == 0) {
                        return 0;
                    }
                    if (IndicatorFragmentPagerAdapter.this.loop) {
                        return 2147483547;
                    }
                    return IndicatorFragmentPagerAdapter.this.getCount();
                }

                @Override // com.shizhefei.view.indicator.FragmentListPageAdapter
                public Fragment getItem(int i) {
                    IndicatorFragmentPagerAdapter indicatorFragmentPagerAdapter = IndicatorFragmentPagerAdapter.this;
                    return indicatorFragmentPagerAdapter.getFragmentForPage(indicatorFragmentPagerAdapter.getRealPosition(i));
                }

                @Override // androidx.viewpager.widget.PagerAdapter
                public float getPageWidth(int i) {
                    IndicatorFragmentPagerAdapter indicatorFragmentPagerAdapter = IndicatorFragmentPagerAdapter.this;
                    return indicatorFragmentPagerAdapter.getPageRatio(indicatorFragmentPagerAdapter.getRealPosition(i));
                }

                @Override // androidx.viewpager.widget.PagerAdapter
                public int getItemPosition(Object obj) {
                    return IndicatorFragmentPagerAdapter.this.getItemPosition(obj);
                }
            };
        }

        public Fragment getExitFragment(int i) {
            return this.pagerAdapter.getExitFragment(i);
        }

        public Fragment getCurrentFragment() {
            return this.pagerAdapter.getCurrentFragment();
        }

        @Override // com.shizhefei.view.indicator.IndicatorViewPager.IndicatorPagerAdapter
        public void notifyDataSetChanged() {
            this.indicatorAdapter.notifyDataSetChanged();
            this.pagerAdapter.notifyDataSetChanged();
        }

        @Override // com.shizhefei.view.indicator.IndicatorViewPager.IndicatorPagerAdapter
        public PagerAdapter getPagerAdapter() {
            return this.pagerAdapter;
        }

        @Override // com.shizhefei.view.indicator.IndicatorViewPager.IndicatorPagerAdapter
        public Indicator.IndicatorAdapter getIndicatorAdapter() {
            return this.indicatorAdapter;
        }
    }
}
