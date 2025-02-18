package com.baoyz.swipemenulistview;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class SwipeMenu {
    private Context mContext;
    private List<SwipeMenuItem> mItems = new ArrayList();
    private int mViewType;

    public SwipeMenu(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return this.mContext;
    }

    public void addMenuItem(SwipeMenuItem swipeMenuItem) {
        this.mItems.add(swipeMenuItem);
    }

    public void removeMenuItem(SwipeMenuItem swipeMenuItem) {
        this.mItems.remove(swipeMenuItem);
    }

    public List<SwipeMenuItem> getMenuItems() {
        return this.mItems;
    }

    public SwipeMenuItem getMenuItem(int i) {
        return this.mItems.get(i);
    }

    public int getViewType() {
        return this.mViewType;
    }

    public void setViewType(int i) {
        this.mViewType = i;
    }
}
