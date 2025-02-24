package com.serenegiant.bluetooth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.serenegiant.common.R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes.dex */
public class BluetoothDeviceInfoRecyclerAdapter extends RecyclerView.Adapter<BluetoothDeviceInfoRecyclerAdapter.ViewHolder> {
    private static final boolean DEBUG = true;
    private static final String TAG = BluetoothDeviceInfoRecyclerAdapter.class.getSimpleName();
    private final int mLayoutId;
    private final OnItemClickListener mListener;
    private final List<BluetoothDeviceInfo> mValues;

    public interface OnItemClickListener {
        void onClick(int i, BluetoothDeviceInfo bluetoothDeviceInfo);
    }

    public BluetoothDeviceInfoRecyclerAdapter(int i, OnItemClickListener onItemClickListener) {
        this(i, null, onItemClickListener);
    }

    public BluetoothDeviceInfoRecyclerAdapter(int i, List<BluetoothDeviceInfo> list, OnItemClickListener onItemClickListener) {
        this.mLayoutId = i;
        this.mValues = list == null ? new ArrayList<>() : list;
        this.mListener = onItemClickListener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(this.mLayoutId, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        viewHolder.position = i;
        viewHolder.mItem = this.mValues.get(i);
        if (viewHolder.addressTv != null) {
            viewHolder.addressTv.setText(this.mValues.get(i).address);
        }
        if (viewHolder.nameTv != null) {
            viewHolder.nameTv.setText(this.mValues.get(i).name);
        }
        viewHolder.mView.setOnClickListener(new View.OnClickListener() { // from class: com.serenegiant.bluetooth.BluetoothDeviceInfoRecyclerAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (BluetoothDeviceInfoRecyclerAdapter.this.mListener != null) {
                    BluetoothDeviceInfoRecyclerAdapter.this.mListener.onClick(viewHolder.position, viewHolder.mItem);
                }
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mValues.size();
    }

    public BluetoothDeviceInfo getItem(int i) throws IndexOutOfBoundsException {
        return this.mValues.get(i);
    }

    public void add(BluetoothDeviceInfo bluetoothDeviceInfo) {
        this.mValues.add(bluetoothDeviceInfo);
        notifyDataSetChanged();
    }

    public void add(int i, BluetoothDeviceInfo bluetoothDeviceInfo) {
        this.mValues.add(i, bluetoothDeviceInfo);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends BluetoothDeviceInfo> collection) {
        this.mValues.addAll(collection);
        notifyDataSetChanged();
    }

    public void remove(BluetoothDeviceInfo bluetoothDeviceInfo) {
        this.mValues.remove(bluetoothDeviceInfo);
        notifyDataSetChanged();
    }

    public void remove(int i) {
        this.mValues.remove(i);
        notifyDataSetChanged();
    }

    public void removeAll(Collection<? extends BluetoothDeviceInfo> collection) {
        this.mValues.removeAll(collection);
        notifyDataSetChanged();
    }

    public void retainAll(Collection<? extends BluetoothDeviceInfo> collection) {
        this.mValues.retainAll(collection);
        notifyDataSetChanged();
    }

    public void clear() {
        this.mValues.clear();
        notifyDataSetChanged();
    }

    public void sort(Comparator<? super BluetoothDeviceInfo> comparator) {
        Collections.sort(this.mValues, comparator);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView addressTv;
        public final ImageView icon;
        public BluetoothDeviceInfo mItem;
        public final View mView;
        public final TextView nameTv;
        public int position;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.nameTv = (TextView) view.findViewById(R.id.name);
            this.addressTv = (TextView) view.findViewById(R.id.address);
            this.icon = (ImageView) view.findViewById(R.id.icon);
        }
    }
}
