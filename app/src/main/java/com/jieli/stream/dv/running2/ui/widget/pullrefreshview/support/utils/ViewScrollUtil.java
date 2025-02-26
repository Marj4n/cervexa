package com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.utils;

import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes.dex */
public class ViewScrollUtil {

    public interface ScrollGeter {
        int getScrollX();

        int getScrollY();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static ScrollGeter getScrollGeter(View view) {
        if (view == null) {
            return null;
        }
        if (view instanceof ScrollGeter) {
            return (ScrollGeter) view;
        }
        if (view instanceof AbsListView) {
            return new AbsListViewScrollGeter((AbsListView) view);
        }
        if (view instanceof RecyclerView) {
            return new RecyclerViewScrollGeter((RecyclerView) view);
        }
        return null;
    }

    private static class AbsListViewScrollGeter implements ScrollGeter {
        AbsListView absListView;

        @Override
        // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.utils.ViewScrollUtil.ScrollGeter
        public int getScrollX() {
            return 0;
        }

        public AbsListViewScrollGeter(AbsListView absListView) {
            this.absListView = absListView;
        }

        @Override
        // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.utils.ViewScrollUtil.ScrollGeter
        public int getScrollY() {
            int firstVisiblePosition = this.absListView.getFirstVisiblePosition();
            View childAt = this.absListView.getChildAt(0);
            if (childAt == null) {
                return 0;
            }
            return (-(childAt.getTop() - this.absListView.getPaddingTop())) + (firstVisiblePosition * childAt.getHeight());
        }
    }

    private static class RecyclerViewScrollGeter implements ScrollGeter {
        LinearLayoutManager layoutManager;
        RecyclerView recyclerView;

        @Override
        // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.utils.ViewScrollUtil.ScrollGeter
        public int getScrollX() {
            return 0;
        }

        public RecyclerViewScrollGeter(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager != null && (layoutManager instanceof LinearLayoutManager)) {
                this.layoutManager = (LinearLayoutManager) layoutManager;
            } else {
                Log.w("RecyclerViewScrollGeter", "LayoutManager is null or Not is LinearLayoutManager");
            }
        }

        @Override
        // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.utils.ViewScrollUtil.ScrollGeter
        public int getScrollY() {
            int findFirstVisibleItemPosition;
            View findViewByPosition;
            LinearLayoutManager linearLayoutManager = this.layoutManager;
            if (linearLayoutManager == null || (findViewByPosition = this.layoutManager.findViewByPosition((findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()))) == null) {
                return 0;
            }
            int top = findViewByPosition.getTop() - this.recyclerView.getPaddingTop();
            LinearLayoutManager linearLayoutManager2 = this.layoutManager;
            if (linearLayoutManager2 instanceof GridLayoutManager) {
                findFirstVisibleItemPosition /= ((GridLayoutManager) linearLayoutManager2).getSpanCount();
            }
            return (-top) + (findFirstVisibleItemPosition * findViewByPosition.getHeight());
        }
    }
}
