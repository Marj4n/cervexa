package com.shizhefei.view.indicator;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

/* loaded from: classes2.dex */
public abstract class FragmentListPageAdapter extends PagerAdapter {
    private static final boolean DEBUG = false;
    private static final String TAG = "FragmentStatePagerAdapter";
    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private SparseArray<Fragment.SavedState> mSavedState = new SparseArray<>();
    private SparseArray<Fragment> mFragments = new SparseArray<>();
    private Fragment mCurrentPrimaryItem = null;

    public abstract Fragment getItem(int i);

    @Override // androidx.viewpager.widget.PagerAdapter
    public void startUpdate(ViewGroup viewGroup) {
    }

    public FragmentListPageAdapter(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    public Fragment getExitFragment(int i) {
        return this.mFragments.get(i);
    }

    public Fragment getCurrentFragment() {
        return this.mCurrentPrimaryItem;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        Fragment fragment = this.mFragments.get(i);
        if (fragment != null) {
            return fragment;
        }
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        Fragment item = getItem(i);
        Fragment.SavedState savedState = this.mSavedState.get(i);
        if (savedState != null) {
            item.setInitialSavedState(savedState);
        }
        item.setMenuVisibility(false);
        item.setUserVisibleHint(false);
        this.mFragments.put(i, item);
        this.mCurTransaction.add(viewGroup.getId(), item);
        return item;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        Fragment fragment = (Fragment) obj;
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        this.mSavedState.put(i, this.mFragmentManager.saveFragmentInstanceState(fragment));
        this.mFragments.remove(i);
        this.mCurTransaction.remove(fragment);
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
        Fragment fragment = (Fragment) obj;
        Fragment fragment2 = this.mCurrentPrimaryItem;
        if (fragment != fragment2) {
            if (fragment2 != null) {
                fragment2.setMenuVisibility(false);
                this.mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            this.mCurrentPrimaryItem = fragment;
        }
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void finishUpdate(ViewGroup viewGroup) {
        FragmentTransaction fragmentTransaction = this.mCurTransaction;
        if (fragmentTransaction != null) {
            fragmentTransaction.commitAllowingStateLoss();
            this.mCurTransaction = null;
            this.mFragmentManager.executePendingTransactions();
        }
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(View view, Object obj) {
        return ((Fragment) obj).getView() == view;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public Parcelable saveState() {
        Bundle bundle;
        if (this.mSavedState.size() > 0) {
            bundle = new Bundle();
            bundle.putSparseParcelableArray("states", this.mSavedState.clone());
        } else {
            bundle = null;
        }
        int size = this.mFragments.size();
        for (int i = 0; i < size; i++) {
            int keyAt = this.mFragments.keyAt(i);
            Fragment valueAt = this.mFragments.valueAt(i);
            if (valueAt != null && valueAt.isAdded()) {
                if (bundle == null) {
                    bundle = new Bundle();
                }
                this.mFragmentManager.putFragment(bundle, "f" + keyAt, valueAt);
            }
        }
        return bundle;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
        if (parcelable != null) {
            Bundle bundle = (Bundle) parcelable;
            bundle.setClassLoader(classLoader);
            this.mSavedState.clear();
            this.mFragments.clear();
            if (bundle.containsKey("states")) {
                this.mSavedState = bundle.getSparseParcelableArray("states");
            }
            for (String str : bundle.keySet()) {
                if (str.startsWith("f")) {
                    int parseInt = Integer.parseInt(str.substring(1));
                    Fragment fragment = this.mFragmentManager.getFragment(bundle, str);
                    if (fragment != null) {
                        fragment.setMenuVisibility(false);
                        this.mFragments.put(parseInt, fragment);
                    }
                }
            }
        }
    }
}
