package com.jieli.stream.dv.running2.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.SettingItem;
import com.jieli.stream.dv.running2.ui.widget.SwitchButton;
import java.util.List;

/* loaded from: classes.dex */
public class SettingAdapter extends BaseAdapter {
    private List<SettingItem> items;
    private Context mContext;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 3;
    }

    public SettingAdapter(Context context, List<SettingItem> list) {
        this.mContext = context;
        this.items = list;
    }

    public void setList(List<SettingItem> list) {
        this.items = list;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        List<SettingItem> list = this.items;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        List<SettingItem> list = this.items;
        if (list == null || i >= list.size()) {
            return null;
        }
        return this.items.get(i);
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int i) {
        SettingItem settingItem = (SettingItem) getItem(i);
        if (settingItem != null) {
            return settingItem.getType();
        }
        return 0;
    }

    @Override // android.widget.Adapter
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHelper viewHelper;
        final ViewHelper1 viewHelper1;
        ViewHelper viewHelper2;
        int itemViewType = getItemViewType(i);
        final SettingItem settingItem = (SettingItem) getItem(i);
        if (itemViewType == 0) {
            if (view == null) {
                view = LayoutInflater.from(this.mContext).inflate(R.layout.item_device_setting_one, viewGroup, false);
                view.setPadding(0, settingItem.getMarginTop(), 0, 0);
                viewHelper = new ViewHelper();
                viewHelper.tv1 = (TextView) view.findViewById(R.id.item_setting_one_tv1);
                viewHelper.tv2 = (TextView) view.findViewById(R.id.item_setting_one_tv2);
                view.setTag(viewHelper);
            } else {
                viewHelper = (ViewHelper) view.getTag();
            }
            if (!TextUtils.isEmpty(settingItem.getName())) {
                viewHelper.tv1.setText(settingItem.getName());
                viewHelper.tv2.setText((String) settingItem.getValue());
            }
        } else if (itemViewType == 1) {
            if (view == null) {
                view = LayoutInflater.from(this.mContext).inflate(R.layout.item_device_setting_two, viewGroup, false);
                view.setPadding(0, settingItem.getMarginTop(), 0, 0);
                viewHelper1 = new ViewHelper1();
                viewHelper1.tv1 = (TextView) view.findViewById(R.id.item_setting_three_tv1);
                viewHelper1.switchButton = (SwitchButton) view.findViewById(R.id.item_setting_three_icon);
                view.setTag(viewHelper1);
                viewHelper1.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.jieli.stream.dv.running2.ui.adapter.SettingAdapter.1
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                        if (settingItem.getOnSwitchListener() != null) {
                            settingItem.setValue(Boolean.valueOf(z));
                            settingItem.getOnSwitchListener().onSwitchListener(viewHelper1.switchButton, (SettingItem) SettingAdapter.this.getItem(i), z);
                        }
                    }
                });
            } else {
                viewHelper1 = (ViewHelper1) view.getTag();
            }
            if (!TextUtils.isEmpty(settingItem.getName())) {
                viewHelper1.tv1.setText(settingItem.getName());
                viewHelper1.switchButton.setCheckedImmediatelyNoEvent(((Boolean) settingItem.getValue()).booleanValue());
            }
        } else if (itemViewType == 2) {
            if (view == null) {
                view = LayoutInflater.from(this.mContext).inflate(R.layout.item_device_setting_three, viewGroup, false);
                view.setPadding(0, settingItem.getMarginTop(), 0, 0);
                viewHelper2 = new ViewHelper();
                viewHelper2.tv1 = (TextView) view.findViewById(R.id.item_setting_three_tv1);
                viewHelper2.tv2 = (TextView) view.findViewById(R.id.item_setting_three_tv2);
                view.setTag(viewHelper2);
            } else {
                viewHelper2 = (ViewHelper) view.getTag();
            }
            if (!TextUtils.isEmpty(settingItem.getName())) {
                viewHelper2.tv1.setText(settingItem.getName());
                viewHelper2.tv2.setText((String) settingItem.getValue());
            }
        }
        return view;
    }

    private class ViewHelper {
        private TextView tv1;
        private TextView tv2;

        private ViewHelper() {
        }
    }

    private class ViewHelper1 {
        private SwitchButton switchButton;
        private TextView tv1;

        private ViewHelper1() {
        }
    }
}
