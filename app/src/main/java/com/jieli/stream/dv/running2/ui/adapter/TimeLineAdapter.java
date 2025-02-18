package com.jieli.stream.dv.running2.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.FileInfo;
import com.jieli.stream.dv.running2.bean.ItemBean;
import com.jieli.stream.dv.running2.ui.widget.NoScrollGridView;
import com.jieli.stream.dv.running2.util.AppUtils;
import java.util.List;

/* loaded from: classes.dex */
public class TimeLineAdapter extends BaseAdapter {
    private boolean isCancelTask;
    private boolean isEditMode;
    private OnSubViewItemClickListener listener;
    private Context mContext;
    private List<ItemBean> mDataList;

    public interface OnSubViewItemClickListener {
        void onSubItemClick(int i, int i2, FileInfo fileInfo);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public TimeLineAdapter(Context context) {
        this.mContext = context;
    }

    public void setDataList(List<ItemBean> list) {
        this.mDataList = AppUtils.mergeList(this.mDataList, list);
        notifyDataSetChanged();
    }

    public void setOnSubViewItemClickListener(OnSubViewItemClickListener onSubViewItemClickListener) {
        this.listener = onSubViewItemClickListener;
    }

    public boolean isEditMode() {
        return this.isEditMode;
    }

    public void setEditMode(boolean z) {
        this.isEditMode = z;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        List<ItemBean> list = this.mDataList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        List<ItemBean> list = this.mDataList;
        if (list == null || i < 0 || i >= list.size()) {
            return null;
        }
        return this.mDataList.get(i);
    }

    public void clear() {
        List<ItemBean> list = this.mDataList;
        if (list != null) {
            list.clear();
        }
        cancelTasks();
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        ItemBean itemBean = (ItemBean) getItem(i);
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.item_time_line, viewGroup, false);
            viewHolder = new ViewHolder(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (itemBean != null) {
            String data = itemBean.getData();
            if (!TextUtils.isEmpty(data)) {
                viewHolder.tvDate.setText(AppUtils.formatDate(data));
            }
            List<FileInfo> infoList = itemBean.getInfoList();
            if (infoList != null) {
                viewHolder.mAdapter.setDataList(i, infoList);
            }
            viewHolder.mAdapter.setEditMode(this.isEditMode);
        }
        if (this.isCancelTask) {
            viewHolder.mAdapter.cancelAllTasks();
            this.isCancelTask = false;
        }
        return view;
    }

    public void cancelTasks() {
        this.isCancelTask = true;
        notifyDataSetChanged();
    }

    private class ViewHolder implements AdapterView.OnItemClickListener {
        private SubGridViewAdapter mAdapter;
        private NoScrollGridView subGridView;
        private TextView tvDate;

        ViewHolder(View view) {
            this.tvDate = (TextView) view.findViewById(R.id.item_time_line_tv);
            this.subGridView = (NoScrollGridView) view.findViewById(R.id.item_time_line_grid_view);
            SubGridViewAdapter subGridViewAdapter = new SubGridViewAdapter(TimeLineAdapter.this.mContext, this.subGridView);
            this.mAdapter = subGridViewAdapter;
            this.subGridView.setAdapter((ListAdapter) subGridViewAdapter);
            this.subGridView.setOnItemClickListener(this);
            view.setTag(this);
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            FileInfo fileInfo;
            if (view == null || this.mAdapter == null || TimeLineAdapter.this.listener == null || (fileInfo = (FileInfo) this.mAdapter.getItem(i)) == null) {
                return;
            }
            TimeLineAdapter.this.listener.onSubItemClick(this.mAdapter.getParentPost(), i, fileInfo);
        }
    }
}
