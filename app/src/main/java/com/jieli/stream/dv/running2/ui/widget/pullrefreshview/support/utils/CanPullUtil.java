package com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.utils;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Pullable;

/* loaded from: classes.dex */
public class CanPullUtil {
    /* JADX WARN: Multi-variable type inference failed */
    public static Pullable getPullAble(View view) {
        if (view == null) {
            return null;
        }
        if (view instanceof Pullable) {
            return (Pullable) view;
        }
        if (view instanceof AbsListView) {
            return new AbsListViewCanPull((AbsListView) view);
        }
        if ((view instanceof ScrollView) || (view instanceof NestedScrollView)) {
            return new ScrollViewCanPull((ViewGroup) view);
        }
        if (view instanceof WebView) {
            return new WebViewCanPull((WebView) view);
        }
        if (view instanceof RecyclerView) {
            return new RecyclerViewCanPull((RecyclerView) view);
        }
        return null;
    }

    private static class AbsListViewCanPull implements Pullable {
        AbsListView absListView;

        public AbsListViewCanPull(AbsListView absListView) {
            this.absListView = absListView;
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Pullable
        public boolean isGetTop() {
            View childAt;
            AbsListView absListView = this.absListView;
            if (absListView != null) {
                if (absListView.getCount() == 0) {
                    return true;
                }
                if (this.absListView.getFirstVisiblePosition() == 0 && (childAt = this.absListView.getChildAt(0)) != null && childAt.getTop() >= this.absListView.getPaddingTop()) {
                    return true;
                }
            }
            return false;
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Pullable
        public boolean isGetBottom() {
            View childAt;
            AbsListView absListView = this.absListView;
            if (absListView == null) {
                return false;
            }
            int firstVisiblePosition = absListView.getFirstVisiblePosition();
            int lastVisiblePosition = this.absListView.getLastVisiblePosition();
            int count = this.absListView.getCount();
            if (count == 0) {
                return true;
            }
            return lastVisiblePosition == count - 1 && (childAt = this.absListView.getChildAt(lastVisiblePosition - firstVisiblePosition)) != null && childAt.getBottom() <= this.absListView.getMeasuredHeight() - this.absListView.getPaddingBottom();
        }
    }

    private static class ScrollViewCanPull implements Pullable {
        ViewGroup scrollView;

        public ScrollViewCanPull(ViewGroup viewGroup) {
            this.scrollView = viewGroup;
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Pullable
        public boolean isGetTop() {
            ViewGroup viewGroup = this.scrollView;
            return viewGroup != null && viewGroup.getScrollY() <= 0;
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Pullable
        public boolean isGetBottom() {
            ViewGroup viewGroup = this.scrollView;
            if (viewGroup != null) {
                return viewGroup.getChildCount() == 0 || (this.scrollView.getChildAt(0) != null && this.scrollView.getScrollY() >= this.scrollView.getChildAt(0).getHeight() - this.scrollView.getMeasuredHeight());
            }
            return false;
        }
    }

    private static class RecyclerViewCanPull implements Pullable {
        LinearLayoutManager layoutManager;
        RecyclerView recyclerView;

        public RecyclerViewCanPull(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        private void initLayoutManager() {
            RecyclerView.LayoutManager layoutManager;
            if (this.layoutManager == null && (layoutManager = this.recyclerView.getLayoutManager()) != null && (layoutManager instanceof LinearLayoutManager)) {
                this.layoutManager = (LinearLayoutManager) layoutManager;
            }
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Pullable
        public boolean isGetTop() {
            initLayoutManager();
            LinearLayoutManager linearLayoutManager = this.layoutManager;
            if (linearLayoutManager != null) {
                if (linearLayoutManager.getItemCount() == 0) {
                    return true;
                }
                if (this.layoutManager.findFirstVisibleItemPosition() == 0 && this.recyclerView.getChildAt(0) != null && this.recyclerView.getChildAt(0).getTop() >= this.recyclerView.getPaddingTop()) {
                    return true;
                }
            }
            return false;
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Pullable
        public boolean isGetBottom() {
            initLayoutManager();
            LinearLayoutManager linearLayoutManager = this.layoutManager;
            if (linearLayoutManager == null) {
                return false;
            }
            int itemCount = linearLayoutManager.getItemCount();
            return itemCount == 0 || this.layoutManager.findLastCompletelyVisibleItemPosition() == itemCount - 1;
        }
    }

    private static class WebViewCanPull implements Pullable {
        WebView webView;

        public WebViewCanPull(WebView webView) {
            this.webView = webView;
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Pullable
        public boolean isGetBottom() {
            return ((float) this.webView.getScrollY()) >= (((float) this.webView.getContentHeight()) * this.webView.getScale()) - ((float) this.webView.getMeasuredHeight());
        }

        @Override // com.jieli.stream.dv.running2.ui.widget.pullrefreshview.support.impl.Pullable
        public boolean isGetTop() {
            return this.webView.getScrollY() <= 0;
        }
    }
}
