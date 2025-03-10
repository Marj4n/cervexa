package com.jieli.stream.dv.running2.ui.dialog;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jieli.stream.dv.running2.ui.base.BaseDialogFragment;
import com.jieli.stream.dv.running2.ui.fragment.AddDeviceFragment;
import com.jieli.stream.dv.running2.util.WifiHelper;

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