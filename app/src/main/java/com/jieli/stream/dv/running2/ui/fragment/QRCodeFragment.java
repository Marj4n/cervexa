package com.jieli.stream.dv.running2.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.activity.MainActivity;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.ui.dialog.QRCodeDialog;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.ToastUtil;
import com.jieli.stream.dv.running2.util.json.listener.OnCompletedListener;

/* loaded from: classes.dex */
public class QRCodeFragment extends BaseFragment implements View.OnClickListener {
    private boolean isShowPwd;
    private Button mBackToSearchButton;
    private EditText mEditWifiPwd;
    private EditText mEditWifiSSID;
    private Button mGenerateQRCodeButton;
    private ImageView mPasswordView;
    private Button mReturnButton;
    private CheckBox mSaveInfoCheckbox;
    private String tag = getClass().getSimpleName();

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_sta_qr_code, viewGroup, false);
        this.mEditWifiSSID = (EditText) inflate.findViewById(R.id.edit_hot_spot_wifi);
        this.mEditWifiPwd = (EditText) inflate.findViewById(R.id.edit_hot_spot_pwd);
        this.mGenerateQRCodeButton = (Button) inflate.findViewById(R.id.generate_qr_code_btn);
        this.mPasswordView = (ImageView) inflate.findViewById(R.id.show_or_hide_pwd);
        this.mReturnButton = (Button) inflate.findViewById(R.id.hot_spot_return_btn);
        this.mSaveInfoCheckbox = (CheckBox) inflate.findViewById(R.id.save_sta_msg);
        this.mBackToSearchButton = (Button) inflate.findViewById(R.id.back_to_search_btn);
        this.mGenerateQRCodeButton.setOnClickListener(this);
        this.mPasswordView.setOnClickListener(this);
        this.mReturnButton.setOnClickListener(this);
        this.mBackToSearchButton.setOnClickListener(this);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.mGenerateQRCodeButton) {
            showQRCodeDialog(this.mEditWifiSSID.getText().toString().trim(), this.mEditWifiPwd.getText().toString().trim());
            return;
        }
        if (view == this.mPasswordView) {
            this.isShowPwd = !this.isShowPwd;
            handlerPwdUI();
        } else if (view == this.mReturnButton) {
            toDeviceListFragment(null);
        } else if (view == this.mBackToSearchButton) {
            toStaDeviceListFragment();
        }
    }

    private void handlerPwdUI() {
        if (this.isShowPwd) {
            this.mEditWifiPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            this.mPasswordView.setImageResource(R.drawable.dbg_show_pwd_selector);
        } else {
            this.mEditWifiPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.mEditWifiPwd.requestFocus();
            this.mPasswordView.setImageResource(R.mipmap.ic_hide_pwd);
        }
    }

    private void showQRCodeDialog(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            ToastUtil.showToastShort(getString(R.string.wifi_ssid_empty_tip));
            return;
        }
        if (!TextUtils.isEmpty(str2) && str2.length() < 8) {
            ToastUtil.showToastShort(getString(R.string.pwd_lenth_limits));
            return;
        }
        QRCodeDialog newInstance = QRCodeDialog.newInstance(str, str2, this.mSaveInfoCheckbox.isChecked());
        newInstance.setOnCompletedListener(new OnCompletedListener<Boolean>() { // from class: com.jieli.stream.dv.running2.ui.fragment.QRCodeFragment.1
            @Override // com.jieli.stream.dv.running2.util.json.listener.OnCompletedListener
            public void onCompleted(Boolean bool) {
                if (bool.booleanValue()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(IConstant.KEY_SEARCH_MODE, 1);
                    QRCodeFragment.this.toDeviceListFragment(bundle);
                }
            }
        });
        if (newInstance.isShowing()) {
            return;
        }
        newInstance.show(getActivity().getSupportFragmentManager(), "QRDialog");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toDeviceListFragment(Bundle bundle) {
        Fragment findFragmentById = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
        if (!(findFragmentById instanceof DeviceListFragment)) {
            findFragmentById = new DeviceListFragment();
        }
        findFragmentById.setArguments(bundle);
        ((MainActivity) getActivity()).changeFragment(R.id.container, findFragmentById, findFragmentById.getClass().getSimpleName());
    }

    private void toStaDeviceListFragment() {
        Fragment fragment = (BaseFragment) getActivity().getSupportFragmentManager().findFragmentByTag(StaDeviceListFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = new StaDeviceListFragment();
        }
        ((MainActivity) getActivity()).changeFragment(R.id.container, fragment, fragment.getClass().getSimpleName());
    }
}
