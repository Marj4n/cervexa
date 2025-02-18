package com.serenegiant.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import androidx.recyclerview.widget.RecyclerView;
import com.serenegiant.common.R;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class CustomRecycleViewAdapter<T> extends RecyclerView.Adapter<ViewHolder<T>> {
    private static final boolean DEBUG = false;
    private static final String TAG = CustomRecycleViewAdapter.class.getSimpleName();
    private CustomRecycleViewListener<T> mCustomRecycleViewListener;
    private final int mItemViewId;
    private final List<T> mItems;
    private LayoutInflater mLayoutInflater;
    private RecyclerView mRecycleView;
    private Handler mUIHandler = new Handler(Looper.getMainLooper());
    protected final View.OnClickListener mOnClickListener = new View.OnClickListener() { // from class: com.serenegiant.widget.CustomRecycleViewAdapter.1
        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.view.View.OnClickListener
        public void onClick(final View view) {
            if (CustomRecycleViewAdapter.this.mRecycleView != null) {
                if (view instanceof Checkable) {
                    ((Checkable) view).setChecked(true);
                    CustomRecycleViewAdapter.this.mUIHandler.postDelayed(new Runnable() { // from class: com.serenegiant.widget.CustomRecycleViewAdapter.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ((Checkable) view).setChecked(false);
                        }
                    }, 100L);
                }
                if (CustomRecycleViewAdapter.this.mCustomRecycleViewListener != null) {
                    Integer num = (Integer) view.getTag(R.id.position);
                    if (num != null) {
                        try {
                            CustomRecycleViewAdapter.this.mCustomRecycleViewListener.onItemClick(CustomRecycleViewAdapter.this, view, num.intValue(), CustomRecycleViewAdapter.this.getItem(num.intValue()));
                            return;
                        } catch (Exception e) {
                            Log.w(CustomRecycleViewAdapter.TAG, e);
                        }
                    }
                    try {
                        int childAdapterPosition = CustomRecycleViewAdapter.this.mRecycleView.getChildAdapterPosition(view);
                        CustomRecycleViewAdapter.this.mCustomRecycleViewListener.onItemClick(CustomRecycleViewAdapter.this, view, childAdapterPosition, CustomRecycleViewAdapter.this.getItem(childAdapterPosition));
                    } catch (Exception e2) {
                        Log.w(CustomRecycleViewAdapter.TAG, e2);
                    }
                }
            }
        }
    };
    protected final View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() { // from class: com.serenegiant.widget.CustomRecycleViewAdapter.2
        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            if (CustomRecycleViewAdapter.this.mRecycleView == null) {
                return false;
            }
            try {
                if (CustomRecycleViewAdapter.this.mCustomRecycleViewListener == null) {
                    return false;
                }
                int childAdapterPosition = CustomRecycleViewAdapter.this.mRecycleView.getChildAdapterPosition(view);
                return CustomRecycleViewAdapter.this.mCustomRecycleViewListener.onItemLongClick(CustomRecycleViewAdapter.this, view, childAdapterPosition, CustomRecycleViewAdapter.this.getItem(childAdapterPosition));
            } catch (Exception e) {
                Log.w(CustomRecycleViewAdapter.TAG, e);
                return false;
            }
        }
    };

    protected abstract void registerDataSetObserver(List<T> list);

    protected abstract void unregisterDataSetObserver(List<T> list);

    public CustomRecycleViewAdapter(int i, List<T> list) {
        this.mItemViewId = i;
        this.mItems = list;
        synchronized (list) {
            registerDataSetObserver(this.mItems);
        }
    }

    protected void finalize() throws Throwable {
        synchronized (this.mItems) {
            unregisterDataSetObserver(this.mItems);
        }
        super.finalize();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecycleView = recyclerView;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.mRecycleView = null;
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder<T> onCreateViewHolder(ViewGroup viewGroup, int i) {
        View onCreateItemView = onCreateItemView(getLayoutInflater(viewGroup.getContext()), viewGroup, i);
        onCreateItemView.setOnClickListener(this.mOnClickListener);
        onCreateItemView.setOnLongClickListener(this.mOnLongClickListener);
        return onCreateViewHolder(onCreateItemView);
    }

    protected View onCreateItemView(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return layoutInflater.inflate(this.mItemViewId, viewGroup, false);
    }

    protected ViewHolder<T> onCreateViewHolder(View view) {
        return new ViewHolder<>(view);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mItems.size();
    }

    public T getItem(int i) {
        if (i < 0 || i >= this.mItems.size()) {
            return null;
        }
        return this.mItems.get(i);
    }

    public void setOnItemClickListener(CustomRecycleViewListener<T> customRecycleViewListener) {
        this.mCustomRecycleViewListener = customRecycleViewListener;
    }

    public RecyclerView getParent() {
        return this.mRecycleView;
    }

    public void clear() {
        synchronized (this.mItems) {
            unregisterDataSetObserver(this.mItems);
            this.mItems.clear();
        }
    }

    public void addAll(Collection<? extends T> collection) {
        synchronized (this.mItems) {
            unregisterDataSetObserver(this.mItems);
            this.mItems.clear();
            this.mItems.addAll(collection);
            registerDataSetObserver(this.mItems);
        }
    }

    public void sort(Comparator<? super T> comparator) {
        synchronized (this.mItems) {
            Collections.sort(this.mItems, comparator);
        }
    }

    protected LayoutInflater getLayoutInflater(Context context) {
        if (this.mLayoutInflater == null) {
            this.mLayoutInflater = LayoutInflater.from(context);
        }
        return this.mLayoutInflater;
    }

    public static class ViewHolder<T> extends RecyclerView.ViewHolder {
        public T mItem;
        public final View mView;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ViewHolder
        public String toString() {
            return super.toString() + " '" + this.mItem + "'";
        }

        public void setEnable(boolean z) {
            this.mView.setEnabled(z);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void hasDivider(boolean z) {
            View view = this.mView;
            if (view instanceof Dividable) {
                ((Dividable) view).hasDivider(z);
            } else {
                view.setTag(R.id.has_divider, Boolean.valueOf(z));
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public boolean hasDivider() {
            View view = this.mView;
            if (view instanceof Dividable) {
                return ((Dividable) view).hasDivider();
            }
            Boolean bool = (Boolean) view.getTag(R.id.has_divider);
            return bool != null && bool.booleanValue();
        }
    }
}
