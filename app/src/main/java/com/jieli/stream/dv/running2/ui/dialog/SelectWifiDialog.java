package com.jieli.stream.dv.running2.ui.dialog;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.base.BaseDialogFragment;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.ToastUtil;
import com.jieli.stream.dv.running2.util.WifiHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class SelectWifiDialog extends BaseDialogFragment {
    private boolean isShowPwd;
    private ImageView ivShowOrHidePwd;
    private OnConnectWifiListener listener;
    private WifiListAdapter mAdapter;
    private EditText mEditPwd;
    private TextView mLeftBtn;
    private TextView mRightBtn;
    private String mSSID;
    private Spinner mSpinner;
    private WifiHelper mWifiHelper;
    private TextView tvTitle;
    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() { // from class: com.jieli.stream.dv.running2.ui.dialog.SelectWifiDialog.2
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (SelectWifiDialog.this.mAdapter != null) {
                String item = SelectWifiDialog.this.mAdapter.getItem(i);
                if (TextUtils.isEmpty(item) || item.equals(SelectWifiDialog.this.mSSID)) {
                    return;
                }
                SelectWifiDialog.this.mSSID = item;
                SelectWifiDialog.this.mEditPwd.setText("");
                SelectWifiDialog.this.mEditPwd.setSelection(0);
                SelectWifiDialog.this.mEditPwd.requestFocus();
            }
        }
    };
    private View.OnClickListener mOnClickListener = new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.dialog.SelectWifiDialog.3
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view != null) {
                if (view == SelectWifiDialog.this.mLeftBtn) {
                    if (SelectWifiDialog.this.listener != null) {
                        SelectWifiDialog.this.listener.onCancel();
                    }
                    SelectWifiDialog.this.dismiss();
                    return;
                }
                if (view == SelectWifiDialog.this.mRightBtn) {
                    String trim = SelectWifiDialog.this.mEditPwd.getText().toString().trim();
                    if (TextUtils.isEmpty(SelectWifiDialog.this.mSSID)) {
                        ToastUtil.showToastShort(SelectWifiDialog.this.getString(R.string.wifi_ssid_empty_tip));
                        return;
                    }
                    if (TextUtils.isEmpty(trim) || trim.length() >= 8) {
                        if (SelectWifiDialog.this.listener != null) {
                            SelectWifiDialog.this.listener.onSelectWifi(SelectWifiDialog.this.mSSID, trim);
                        }
                        SelectWifiDialog.this.dismiss();
                        return;
                    }
                    ToastUtil.showToastShort(SelectWifiDialog.this.getString(R.string.wifi_pwd_length_not_allow));
                    return;
                }
                if (view == SelectWifiDialog.this.ivShowOrHidePwd) {
                    SelectWifiDialog.this.isShowPwd = !r3.isShowPwd;
                    if (SelectWifiDialog.this.isShowPwd) {
                        SelectWifiDialog.this.mEditPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        SelectWifiDialog.this.ivShowOrHidePwd.setImageResource(R.drawable.dbg_show_pwd_selector);
                    } else {
                        SelectWifiDialog.this.mEditPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        SelectWifiDialog.this.mEditPwd.requestFocus();
                        SelectWifiDialog.this.ivShowOrHidePwd.setImageResource(R.mipmap.ic_hide_pwd);
                    }
                }
            }
        }
    };

    public interface OnConnectWifiListener {
        void onCancel();

        void onSelectWifi(String str, String str2);
    }

    public void setOnConnectWifiListener(OnConnectWifiListener onConnectWifiListener) {
        this.listener = onConnectWifiListener;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mWifiHelper = WifiHelper.getInstance(getContext());
        setCancelable(false);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.dialog_select_wifi, viewGroup);
        if (getDialog() != null) {
            getDialog().requestWindowFeature(1);
        }
        this.tvTitle = (TextView) inflate.findViewById(R.id.dialog_title);
        this.mSpinner = (Spinner) inflate.findViewById(R.id.dialog_wifi_ssid_spinner);
        this.mEditPwd = (EditText) inflate.findViewById(R.id.dialog_edit_wifi_pwd);
        this.ivShowOrHidePwd = (ImageView) inflate.findViewById(R.id.dialog_show_or_hide_pwd);
        this.mLeftBtn = (TextView) inflate.findViewById(R.id.dialog_left);
        this.mRightBtn = (TextView) inflate.findViewById(R.id.dialog_right);
        this.mSpinner.setOnItemSelectedListener(this.onItemSelectedListener);
        this.mLeftBtn.setOnClickListener(this.mOnClickListener);
        this.mRightBtn.setOnClickListener(this.mOnClickListener);
        this.ivShowOrHidePwd.setOnClickListener(this.mOnClickListener);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getDialog() == null || getDialog().getWindow() == null) {
            return;
        }
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        attributes.width = 100;
        attributes.height = 100;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (getResources().getConfiguration().orientation == 2) {
            attributes.width = (displayMetrics.heightPixels * 4) / 5;
            attributes.height = (displayMetrics.heightPixels * 3) / 4;
        } else if (getResources().getConfiguration().orientation == 1) {
            attributes.width = (displayMetrics.widthPixels * 4) / 5;
            attributes.height = (displayMetrics.widthPixels * 3) / 4;
        }
        attributes.gravity = 17;
        getDialog().getWindow().setAttributes(attributes);
        initSpinner();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
    }

    private void initSpinner() {
        List<ScanResult> wifiScanResult = this.mWifiHelper.getWifiScanResult();
        if (wifiScanResult != null) {
            ArrayList arrayList = new ArrayList();
            for (ScanResult scanResult : wifiScanResult) {
                String formatSSID = WifiHelper.formatSSID(scanResult.SSID);
                if (!TextUtils.isEmpty(formatSSID) && !formatSSID.startsWith(IConstant.WIFI_PREFIX)) {
                    arrayList.add(scanResult);
                }
            }
            if (arrayList.size() > 0) {
                Collections.sort(arrayList, new Comparator<ScanResult>() { // from class: com.jieli.stream.dv.running2.ui.dialog.SelectWifiDialog.1
                    @Override // java.util.Comparator
                    public int compare(ScanResult scanResult2, ScanResult scanResult3) {
                        if (scanResult3.level > scanResult2.level) {
                            return 1;
                        }
                        return scanResult3.level == scanResult2.level ? 0 : -1;
                    }
                });
                ArrayList arrayList2 = new ArrayList();
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    arrayList2.add(((ScanResult) it.next()).SSID);
                }
                WifiListAdapter wifiListAdapter = new WifiListAdapter(getContext());
                this.mAdapter = wifiListAdapter;
                wifiListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                this.mAdapter.addAll(arrayList2);
                this.mSpinner.setAdapter((SpinnerAdapter) this.mAdapter);
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    private class WifiListAdapter extends ArrayAdapter<String> {
        private WifiListAdapter(Context context) {
            super(context, 0);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_select_wifi, viewGroup, false);
                viewHolder = new ViewHolder(view);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.textView.setText(getItem(i));
            return view;
        }

        private class ViewHolder {
            private TextView textView;

            ViewHolder(View view) {
                this.textView = (TextView) view.findViewById(R.id.item_select_wifi_tv);
                view.setTag(this);
            }
        }
    }
}
