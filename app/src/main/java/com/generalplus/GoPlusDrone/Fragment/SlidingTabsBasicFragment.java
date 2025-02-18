package com.generalplus.GoPlusDrone.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.generalplus.GoPlusDrone.R;
import com.generalplus.GoPlusDrone.View.SlidingTabLayout;

/* loaded from: classes.dex */
public class SlidingTabsBasicFragment extends Fragment {
    static final String LOG_TAG = "SlidingBasicFragment";
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_sample, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        this.mViewPager = viewPager;
        viewPager.setAdapter(new SamplePagerAdapter());
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        this.mSlidingTabLayout = slidingTabLayout;
        slidingTabLayout.setViewPager(this.mViewPager);
    }

    class SamplePagerAdapter extends PagerAdapter {
        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return 2;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return obj == view;
        }

        SamplePagerAdapter() {
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public CharSequence getPageTitle(int i) {
            return "Item " + (i + 1);
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            View inflate = SlidingTabsBasicFragment.this.getActivity().getLayoutInflater().inflate(R.layout.pager_item, viewGroup, false);
            viewGroup.addView(inflate);
            ((TextView) inflate.findViewById(R.id.item_title)).setText(String.valueOf(i + 1));
            Log.i(SlidingTabsBasicFragment.LOG_TAG, "instantiateItem() [position: " + i + "]");
            return inflate;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
            Log.i(SlidingTabsBasicFragment.LOG_TAG, "destroyItem() [position: " + i + "]");
        }
    }
}
