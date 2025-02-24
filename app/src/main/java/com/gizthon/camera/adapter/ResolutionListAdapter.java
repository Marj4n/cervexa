package com.gizthon.camera.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.weioa.GoPlusDrone.R;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ResolutionListAdapter extends RecyclerView.Adapter<ResolutionListAdapter.RecyclerHolder> {
    private List<String> dataList = new ArrayList();
    private Context mContext;
    private OnClickItem onClickItem;
    private int position;

    public interface OnClickItem {
        void onClick(int i);
    }

    public ResolutionListAdapter(Context context) {
        this.mContext = context;
    }

    public void selected(int i) {
        this.position = i;
        notifyDataSetChanged();
    }

    public void setData(List<String> list) {
        if (list != null) {
            this.dataList.clear();
            this.dataList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecyclerHolder(LayoutInflater.from(this.mContext).inflate(R.layout.item_resolution, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(final RecyclerHolder recyclerHolder, @SuppressLint("RecyclerView") final int i) {
        recyclerHolder.bind(this.dataList.get(i), this.position == i);
        recyclerHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.gizthon.camera.adapter.ResolutionListAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ResolutionListAdapter.this.selected(recyclerHolder.getAdapterPosition());
                ResolutionListAdapter.this.onClickItem.onClick(i);
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.dataList.size();
    }

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public static class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView textView;

        private RecyclerHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.tv_name);
        }

        public void bind(String str, boolean z) {
            this.textView.setText(str);
            this.textView.setBackgroundColor(Color.parseColor(!z ? "#B1B1B3" : "#494949"));
        }
    }
}
