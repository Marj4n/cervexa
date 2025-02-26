package com.marjan.cervexa.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/* loaded from: classes.dex */
public class BindAdapter<T> extends RecyclerView.Adapter<BindAdapter.ViewHolder<T>> {
    public List<T> dataModelList;
    public int layoutId;

    public BindAdapter(List<T> list, int i) {
        this.dataModelList = list;
        this.layoutId = i;
    }

    public void setDataModelList(List<T> list) {
        this.dataModelList = list;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), this.layoutId, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.bind(this.dataModelList.get(i), this);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.dataModelList.size();
    }

    public static class ViewHolder<T> extends RecyclerView.ViewHolder {
        public ViewDataBinding itemRowBinding;

        public ViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            this.itemRowBinding = viewDataBinding;
        }

        public void bind(T t, Object obj) {
            if (obj != null) {
                this.itemRowBinding.setVariable(3, obj);
            }
            if (t != null) {
                this.itemRowBinding.setVariable(10, t);
            }
            this.itemRowBinding.executePendingBindings();
        }
    }
}
