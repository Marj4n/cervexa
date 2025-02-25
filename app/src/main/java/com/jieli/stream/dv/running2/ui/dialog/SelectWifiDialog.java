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
import com.jieli.stream.dv.running2.ui.fragment.AddDeviceFragment;
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
    private AddDeviceFragment.WifiListAdapter mAdapter;
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
            if (mAdapter != null) {
                String item = (String) mAdapter.getItem(i);
                if (TextUtils.isEmpty(item) || item.equals(mSSID)) {
                    return;
                }
                mSSID = item;
                mEditPwd.setText("");
                mEditPwd.setSelection(0);
                mEditPwd.requestFocus();
            }
        }
    };
}