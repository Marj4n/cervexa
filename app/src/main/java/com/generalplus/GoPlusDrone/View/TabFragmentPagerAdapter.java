package com.generalplus.GoPlusDrone.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.generalplus.GoPlusDrone.Fragment.BaseFragment;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    LinkedList<BaseFragment> fragments;

    public TabFragmentPagerAdapter(FragmentManager fragmentManager, LinkedList<BaseFragment> linkedList) {
        super(fragmentManager);
        this.fragments = null;
        if (linkedList == null) {
            this.fragments = new LinkedList<>();
        } else {
            this.fragments = linkedList;
        }
    }

    @Override // androidx.fragment.app.FragmentPagerAdapter
    public BaseFragment getItem(int i) {
        return this.fragments.get(i);
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return this.fragments.size();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public CharSequence getPageTitle(int i) {
        return this.fragments.get(i).getTitle();
    }

    public int getIconResId(int i) {
        return this.fragments.get(i).getIconResId();
    }
}
