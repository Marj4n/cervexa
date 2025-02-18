package com.jieli.stream.dv.running2.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.core.internal.view.SupportMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.ThumbnailInfo;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.util.Dbug;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes.dex */
public class CoverAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private List<ThumbnailInfo> mDataList = new ArrayList();
    private String tag = getClass().getSimpleName();
    private int contentFlag = -1;

    public interface OnItemClickListener {
        void onItemClick(View view, int i);

        void onItemLongClick(View view, int i);
    }

    public void setContentThumbnailFlag(int i) {
        if (i < this.mDataList.size()) {
            this.contentFlag = i;
        }
    }

    public void clearContentThumbnail() {
        int i = this.contentFlag;
        if (i <= -1 || i >= this.mDataList.size()) {
            return;
        }
        this.mDataList.get(this.contentFlag).setBitmap(null);
        this.contentFlag = -1;
    }

    public void clear() {
        this.mDataList.clear();
        notifyDataSetChanged();
    }

    public CoverAdapter(Context context) {
        this.mContext = context;
    }

    public void addData(ThumbnailInfo thumbnailInfo) {
        if (thumbnailInfo == null || this.mDataList.contains(thumbnailInfo)) {
            Dbug.i(this.tag, "add data failed. info : " + thumbnailInfo);
            return;
        }
        this.mDataList.add(thumbnailInfo);
        if (this.mDataList.size() <= 1 || thumbnailInfo.getStartTime() == null) {
            return;
        }
        if (this.mDataList.get(r0.size() - 2).getStartTime().compareTo(thumbnailInfo.getStartTime()) < 0) {
            Collections.sort(this.mDataList, new Comparator<ThumbnailInfo>() { // from class: com.jieli.stream.dv.running2.ui.adapter.CoverAdapter.1
                @Override // java.util.Comparator
                public int compare(ThumbnailInfo thumbnailInfo2, ThumbnailInfo thumbnailInfo3) {
                    return thumbnailInfo3.getStartTime().compareTo(thumbnailInfo2.getStartTime());
                }
            });
        }
    }

    public void remove(int i) {
        ThumbnailInfo remove;
        List<ThumbnailInfo> list = this.mDataList;
        if (list == null || i < 0 || i >= list.size() || (remove = this.mDataList.remove(i)) == null) {
            return;
        }
        Dbug.i(this.tag, "mDataList del file -> name= " + remove.getName() + "   time=" + remove.getCreateTime());
    }

    public ThumbnailInfo getItem(int i) {
        List<ThumbnailInfo> list = this.mDataList;
        if (list == null || i < 0 || i >= list.size()) {
            return null;
        }
        return this.mDataList.get(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.cover_flow_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        Bitmap bitmap = this.mDataList.get(i).getBitmap();
        if (this.mDataList.get(i).getType() == 2) {
            viewHolder.img.setBackgroundColor(SupportMenu.CATEGORY_MASK);
        } else {
            viewHolder.img.setBackgroundColor(-1);
        }
        if (bitmap == null || bitmap.isRecycled()) {
            Glide.with(MainApplication.getApplication()).setDefaultRequestOptions(new RequestOptions().placeholder(R.mipmap.bg_thumbnail_default)).load(this.mDataList.get(i).getSaveUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(viewHolder.img);
        } else {
            viewHolder.img.setImageBitmap(bitmap);
        }
        if (this.mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.adapter.CoverAdapter.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    CoverAdapter.this.mOnItemClickListener.onItemClick(view, viewHolder.getLayoutPosition());
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.jieli.stream.dv.running2.ui.adapter.CoverAdapter.3
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    CoverAdapter.this.mOnItemClickListener.onItemLongClick(view, viewHolder.getLayoutPosition());
                    return true;
                }
            });
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<ThumbnailInfo> list = this.mDataList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public ViewHolder(View view) {
            super(view);
            this.img = (ImageView) view.findViewById(R.id.cover_image);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
