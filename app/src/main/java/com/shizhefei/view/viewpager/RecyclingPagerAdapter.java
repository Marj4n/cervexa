package com.shizhefei.view.viewpager;

import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;

/* loaded from: classes2.dex */
public abstract class RecyclingPagerAdapter extends PagerAdapter {
    public static final int IGNORE_ITEM_VIEW_TYPE = -1;
    private final RecycleBin recycleBin;

    public int getItemViewType(int i) {
        return 0;
    }

    public abstract View getView(int i, View view, ViewGroup viewGroup);

    public int getViewTypeCount() {
        return 1;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public final boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public RecyclingPagerAdapter() {
        this(new RecycleBin());
    }

    RecyclingPagerAdapter(RecycleBin recycleBin) {
        this.recycleBin = recycleBin;
        recycleBin.setViewTypeCount(getViewTypeCount());
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void notifyDataSetChanged() {
        this.recycleBin.scrapActiveViews();
        super.notifyDataSetChanged();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public final Object instantiateItem(ViewGroup viewGroup, int i) {
        int itemViewType = getItemViewType(i);
        View view = getView(i, itemViewType != -1 ? this.recycleBin.getScrapView(i, itemViewType) : null, viewGroup);
        viewGroup.addView(view);
        return view;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public final void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        View view = (View) obj;
        viewGroup.removeView(view);
        int itemViewType = getItemViewType(i);
        if (itemViewType != -1) {
            this.recycleBin.addScrapView(view, i, itemViewType);
        }
    }
}
