package com.serenegiant.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.serenegiant.common.R;

/* loaded from: classes2.dex */
public class RecycleViewWithEmptyView extends RecyclerView {
    private static final boolean DEBUG = false;
    private static final String TAG = RecycleViewWithEmptyView.class.getSimpleName();
    private final RecyclerView.AdapterDataObserver mAdapterDataObserver;
    private View mEmptyView;

    public RecycleViewWithEmptyView(Context context) {
        this(context, null, 0);
    }

    public RecycleViewWithEmptyView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RecycleViewWithEmptyView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mAdapterDataObserver = new RecyclerView.AdapterDataObserver() { // from class: com.serenegiant.widget.RecycleViewWithEmptyView.2
            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onChanged() {
                super.onChanged();
                RecycleViewWithEmptyView.this.updateEmptyView();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeChanged(int i2, int i3) {
                super.onItemRangeChanged(i2, i3);
                RecycleViewWithEmptyView.this.updateEmptyView();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeRemoved(int i2, int i3) {
                super.onItemRangeRemoved(i2, i3);
                RecycleViewWithEmptyView.this.updateEmptyView();
            }
        };
        Drawable drawable = null;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RecycleViewWithEmptyView, i, 0);
            try {
                if (obtainStyledAttributes.hasValue(R.styleable.RecycleViewWithEmptyView_listDivider)) {
                    drawable = obtainStyledAttributes.getDrawable(R.styleable.RecycleViewWithEmptyView_listDivider);
                }
            } catch (Exception unused) {
            }
            obtainStyledAttributes.recycle();
        }
        int orientation = getLayoutManager() instanceof LinearLayoutManager ? ((LinearLayoutManager) getLayoutManager()).getOrientation() : 1;
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, drawable);
        dividerItemDecoration.setOrientation(orientation);
        addItemDecoration(dividerItemDecoration);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (getAdapter() != adapter) {
            try {
                if (getAdapter() != null) {
                    getAdapter().unregisterAdapterDataObserver(this.mAdapterDataObserver);
                }
            } catch (Exception unused) {
            }
            super.setAdapter(adapter);
            if (adapter != null) {
                adapter.registerAdapterDataObserver(this.mAdapterDataObserver);
            }
        }
        updateEmptyView();
    }

    public void setEmptyView(View view) {
        if (this.mEmptyView != view) {
            this.mEmptyView = view;
            updateEmptyView();
        }
    }

    protected void updateEmptyView() {
        if (this.mEmptyView != null) {
            final RecyclerView.Adapter adapter = getAdapter();
            post(new Runnable() { // from class: com.serenegiant.widget.RecycleViewWithEmptyView.1
                @Override // java.lang.Runnable
                public void run() {
                    View view = RecycleViewWithEmptyView.this.mEmptyView;
                    RecyclerView.Adapter adapter2 = adapter;
                    view.setVisibility((adapter2 == null || adapter2.getItemCount() == 0) ? 0 : 8);
                }
            });
        }
    }
}
