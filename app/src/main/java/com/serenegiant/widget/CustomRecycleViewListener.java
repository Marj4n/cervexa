package com.serenegiant.widget;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes2.dex */
public interface CustomRecycleViewListener<T> {
    void onItemClick(RecyclerView.Adapter<?> adapter, View view, int i, T t);

    boolean onItemLongClick(RecyclerView.Adapter<?> adapter, View view, int i, T t);
}
