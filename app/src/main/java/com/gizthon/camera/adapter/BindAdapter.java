package com.gizthon.camera.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gizthon.camera.model.PhotoBean;
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

    public static void localSrc(ImageView imageView, PhotoBean photoBean) {
        if (photoBean.getPath().endsWith("avi") || photoBean.getPath().endsWith("mp4")) {
            Glide.with(imageView.getContext()).load(photoBean.getAviPngName()).into(imageView);
        } else {
            Glide.with(imageView.getContext()).setDefaultRequestOptions(new RequestOptions().frame(1L).centerCrop()).load(photoBean.getPath()).into(imageView);
        }
    }
}
