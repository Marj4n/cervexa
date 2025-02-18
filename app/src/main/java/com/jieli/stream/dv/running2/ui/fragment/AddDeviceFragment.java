package com.jieli.stream.dv.running2.ui.fragment;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.WifiBean;
import com.jieli.stream.dv.running2.interfaces.OnWifiCallBack;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import com.jieli.stream.dv.running2.util.ToastUtil;
import com.jieli.stream.dv.running2.util.WifiHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AddDeviceFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, OnWifiCallBack {
    private WifiListAdapter adapter;
    private boolean isAddDev = false;
    private ScanResult scanResult;
    private ListView searchWifiListView;
    private TextView selectWifiSSIDTv;
    private List<WifiBean> wifiBeanList;
    private EditText wifiPwdEdit;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_add_device, viewGroup, false);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.add_dev_return_btn);
        ImageView imageView2 = (ImageView) inflate.findViewById(R.id.add_dev_refresh_btn);
        this.selectWifiSSIDTv = (TextView) inflate.findViewById(R.id.add_dev_wifi_name);
        this.wifiPwdEdit = (EditText) inflate.findViewById(R.id.add_dev_wifi_pwd_edit);
        TextView textView = (TextView) inflate.findViewById(R.id.add_dev_btn);
        this.searchWifiListView = (ListView) inflate.findViewById(R.id.add_dev_list_view);
        imageView.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        textView.setOnClickListener(this);
        this.searchWifiListView.setOnItemClickListener(this);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() == null) {
            return;
        }
        refresh();
        WifiHelper.getInstance(getActivity().getApplicationContext()).registerOnWifiCallback(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.isAddDev = false;
        WifiHelper.getInstance(getActivity().getApplicationContext()).unregisterOnWifiCallback(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (getActivity() == null) {
            return;
        }
        int id = view.getId();
        if (id == R.id.add_dev_return_btn) {
            getActivity().onBackPressed();
            return;
        }
        if (id == R.id.add_dev_refresh_btn) {
            refresh();
            return;
        }
        if (id == R.id.add_dev_btn) {
            String trim = this.selectWifiSSIDTv.getText().toString().trim();
            String trim2 = this.wifiPwdEdit.getText().toString().trim();
            this.isAddDev = false;
            if (!TextUtils.isEmpty(trim)) {
                Log.w(this.TAG, "ssid : " + trim + " ,pwd : " + trim2);
                PreferencesHelper.putStringValue(getActivity().getApplicationContext(), IConstant.CURRENT_WIFI_SSID, trim);
                if (TextUtils.isEmpty(trim2)) {
                    this.mWifiHelper.addNetWorkAndConnect(trim, "0", WifiHelper.WifiCipherType.NONE);
                    PreferencesHelper.putStringValue(getActivity().getApplicationContext(), trim, null);
                    this.isAddDev = true;
                    return;
                } else {
                    if (trim2.length() >= 8) {
                        this.mWifiHelper.addNetWorkAndConnect(trim, trim2, WifiHelper.WifiCipherType.WPA);
                        PreferencesHelper.putStringValue(getActivity().getApplicationContext(), trim, trim2);
                        this.isAddDev = true;
                        return;
                    }
                    ToastUtil.showToastShort(getString(R.string.wifi_pwd_length_not_allow));
                    return;
                }
            }
            ToastUtil.showToastShort(getString(R.string.wifi_pwd_not_empty));
        }
    }

    private void refresh() {
        WifiBean wifiBean;
        if (this.mWifiHelper != null) {
            this.mWifiHelper.startScan();
            List<ScanResult> wifiScanResult = this.mWifiHelper.getWifiScanResult();
            if (wifiScanResult != null) {
                List<WifiBean> list = this.wifiBeanList;
                if (list == null) {
                    this.wifiBeanList = new ArrayList();
                } else {
                    list.clear();
                }
                for (ScanResult scanResult : wifiScanResult) {
                    if (scanResult != null) {
                        String formatSSID = WifiHelper.formatSSID(scanResult.SSID);
                        if (!TextUtils.isEmpty(formatSSID) && formatSSID.startsWith(WIFI_PREFIX)) {
                            if (this.wifiBeanList.size() == 0) {
                                this.scanResult = scanResult;
                            }
                            WifiBean wifiBean2 = new WifiBean();
                            wifiBean2.setSSID(formatSSID);
                            wifiBean2.setSelect(false);
                            wifiBean2.setState(0);
                            this.wifiBeanList.add(wifiBean2);
                        }
                    }
                }
                List<WifiBean> list2 = this.wifiBeanList;
                if (list2 != null && list2.size() > 0 && (wifiBean = this.wifiBeanList.get(0)) != null) {
                    this.selectWifiSSIDTv.setText(wifiBean.getSSID());
                    wifiBean.setSelect(true);
                }
                WifiListAdapter wifiListAdapter = this.adapter;
                if (wifiListAdapter != null) {
                    wifiListAdapter.notifyDataSetChanged();
                } else {
                    this.adapter = new WifiListAdapter(this.wifiBeanList);
                }
                this.searchWifiListView.setAdapter((ListAdapter) this.adapter);
                updateEditView(this.scanResult);
            }
        }
    }

    private void updateEditView(ScanResult scanResult) {
        if (scanResult != null) {
            if (scanResult.capabilities.equals("WPA")) {
                this.wifiPwdEdit.setEnabled(true);
            } else {
                this.wifiPwdEdit.setText("");
                this.wifiPwdEdit.setEnabled(false);
            }
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        WifiListAdapter wifiListAdapter;
        if (getActivity() == null || (wifiListAdapter = this.adapter) == null) {
            return;
        }
        WifiBean wifiBean = (WifiBean) wifiListAdapter.getItem(i);
        if (wifiBean != null) {
            wifiBean.setSelect(true);
            this.selectWifiSSIDTv.setText(wifiBean.getSSID());
            this.adapter.notifyDataSetChanged();
            ScanResult scanResultForList = getScanResultForList(wifiBean);
            if (scanResultForList != null) {
                this.scanResult = scanResultForList;
            }
            updateEditView(this.scanResult);
            return;
        }
        refresh();
    }

    @Override // com.jieli.stream.dv.running2.interfaces.OnWifiCallBack
    public void onConnected(WifiInfo wifiInfo) {
        if (wifiInfo != null) {
            String formatSSID = WifiHelper.formatSSID(wifiInfo.getSSID());
            String formatSSID2 = WifiHelper.formatSSID(PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getString(IConstant.CURRENT_WIFI_SSID, null));
            if (this.isAddDev && !TextUtils.isEmpty(formatSSID2) && formatSSID2.equals(formatSSID)) {
                ToastUtil.showToastShort(getString(R.string.connected_wifi_tip));
                this.isAddDev = false;
            }
        }
    }

    @Override // com.jieli.stream.dv.running2.interfaces.OnWifiCallBack
    public void onError(int i) {
        if (this.isAddDev) {
            this.isAddDev = false;
        }
    }

    private ScanResult getScanResultForList(WifiBean wifiBean) {
        if (getActivity() != null && wifiBean != null) {
            String ssid = wifiBean.getSSID();
            if (!TextUtils.isEmpty(ssid)) {
                if (this.mWifiHelper == null) {
                    this.mWifiHelper = WifiHelper.getInstance(getActivity().getApplicationContext());
                }
                List<ScanResult> wifiScanResult = this.mWifiHelper.getWifiScanResult();
                if (wifiScanResult != null) {
                    for (ScanResult scanResult : wifiScanResult) {
                        if (scanResult != null) {
                            String formatSSID = WifiHelper.formatSSID(scanResult.SSID);
                            if (!TextUtils.isEmpty(formatSSID) && formatSSID.equals(ssid)) {
                                return scanResult;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private class WifiListAdapter extends BaseAdapter {
        private List<WifiBean> wifiList;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        WifiListAdapter(List<WifiBean> list) {
            this.wifiList = list;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            List<WifiBean> list = this.wifiList;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            List<WifiBean> list = this.wifiList;
            if (list == null || i >= list.size()) {
                return null;
            }
            return this.wifiList.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (AddDeviceFragment.this.getActivity() == null) {
                return view;
            }
            if (view == null) {
                view = LayoutInflater.from(AddDeviceFragment.this.getActivity()).inflate(R.layout.item_wifi_list, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.wifiIcon = (ImageView) view.findViewById(R.id.item_wifi_icon);
                viewHolder.wifiNameTv = (TextView) view.findViewById(R.id.item_wifi_name);
                viewHolder.wifiStateTv = (TextView) view.findViewById(R.id.item_wifi_state);
                viewHolder.wifiSelectIcon = (ImageView) view.findViewById(R.id.item_wifi_select_state);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            WifiBean wifiBean = (WifiBean) getItem(i);
            if (wifiBean != null) {
                String ssid = wifiBean.getSSID();
                int state = wifiBean.getState();
                boolean isSelect = wifiBean.isSelect();
                if (!TextUtils.isEmpty(ssid)) {
                    viewHolder.wifiNameTv.setText(ssid);
                }
                if (state == 1) {
                    viewHolder.wifiStateTv.setText(R.string.saved);
                }
                if (isSelect) {
                    viewHolder.wifiSelectIcon.setImageResource(R.mipmap.ic_check_round_blue);
                } else {
                    viewHolder.wifiSelectIcon.setImageResource(R.mipmap.ic_uncheck_round_blue);
                }
            }
            return view;
        }

        private class ViewHolder {
            private ImageView wifiIcon;
            private TextView wifiNameTv;
            private ImageView wifiSelectIcon;
            private TextView wifiStateTv;

            private ViewHolder() {
            }
        }
    }
}
