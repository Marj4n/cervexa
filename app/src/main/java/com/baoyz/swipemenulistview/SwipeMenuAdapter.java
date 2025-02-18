package com.baoyz.swipemenulistview;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;
import androidx.core.internal.view.SupportMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuView;
import tv.danmaku.ijk.media.player.IjkMediaCodecInfo;

/* loaded from: classes.dex */
public class SwipeMenuAdapter implements WrapperListAdapter, SwipeMenuView.OnSwipeItemClickListener {
    private ListAdapter mAdapter;
    private Context mContext;
    private SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener;

    public SwipeMenuAdapter(Context context, ListAdapter listAdapter) {
        this.mAdapter = listAdapter;
        this.mContext = context;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mAdapter.getCount();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.mAdapter.getItem(i);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return this.mAdapter.getItemId(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            View view2 = this.mAdapter.getView(i, view, viewGroup);
            SwipeMenu swipeMenu = new SwipeMenu(this.mContext);
            swipeMenu.setViewType(this.mAdapter.getItemViewType(i));
            createMenu(swipeMenu);
            SwipeMenuListView swipeMenuListView = (SwipeMenuListView) viewGroup;
            SwipeMenuView swipeMenuView = new SwipeMenuView(swipeMenu, swipeMenuListView);
            swipeMenuView.setOnSwipeItemClickListener(this);
            SwipeMenuLayout swipeMenuLayout = new SwipeMenuLayout(view2, swipeMenuView, swipeMenuListView.getCloseInterpolator(), swipeMenuListView.getOpenInterpolator());
            swipeMenuLayout.setPosition(i);
            return swipeMenuLayout;
        }
        SwipeMenuLayout swipeMenuLayout2 = (SwipeMenuLayout) view;
        swipeMenuLayout2.closeMenu();
        swipeMenuLayout2.setPosition(i);
        this.mAdapter.getView(i, swipeMenuLayout2.getContentView(), viewGroup);
        return swipeMenuLayout2;
    }

    public void createMenu(SwipeMenu swipeMenu) {
        SwipeMenuItem swipeMenuItem = new SwipeMenuItem(this.mContext);
        swipeMenuItem.setTitle("Item 1");
        swipeMenuItem.setBackground(new ColorDrawable(-7829368));
        swipeMenuItem.setWidth(IjkMediaCodecInfo.RANK_SECURE);
        swipeMenu.addMenuItem(swipeMenuItem);
        SwipeMenuItem swipeMenuItem2 = new SwipeMenuItem(this.mContext);
        swipeMenuItem2.setTitle("Item 2");
        swipeMenuItem2.setBackground(new ColorDrawable(SupportMenu.CATEGORY_MASK));
        swipeMenuItem2.setWidth(IjkMediaCodecInfo.RANK_SECURE);
        swipeMenu.addMenuItem(swipeMenuItem2);
    }

    @Override // com.baoyz.swipemenulistview.SwipeMenuView.OnSwipeItemClickListener
    public void onItemClick(SwipeMenuView swipeMenuView, SwipeMenu swipeMenu, int i) {
        SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener = this.onMenuItemClickListener;
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.onMenuItemClick(swipeMenuView.getPosition(), swipeMenu, i);
        }
    }

    public void setOnMenuItemClickListener(SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    @Override // android.widget.Adapter
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        this.mAdapter.registerDataSetObserver(dataSetObserver);
    }

    @Override // android.widget.Adapter
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        this.mAdapter.unregisterDataSetObserver(dataSetObserver);
    }

    @Override // android.widget.ListAdapter
    public boolean areAllItemsEnabled() {
        return this.mAdapter.areAllItemsEnabled();
    }

    @Override // android.widget.ListAdapter
    public boolean isEnabled(int i) {
        return this.mAdapter.isEnabled(i);
    }

    @Override // android.widget.Adapter
    public boolean hasStableIds() {
        return this.mAdapter.hasStableIds();
    }

    @Override // android.widget.Adapter
    public int getItemViewType(int i) {
        return this.mAdapter.getItemViewType(i);
    }

    @Override // android.widget.Adapter
    public int getViewTypeCount() {
        return this.mAdapter.getViewTypeCount();
    }

    @Override // android.widget.Adapter
    public boolean isEmpty() {
        return this.mAdapter.isEmpty();
    }

    @Override // android.widget.WrapperListAdapter
    public ListAdapter getWrappedAdapter() {
        return this.mAdapter;
    }
}
