package com.jieli.stream.dv.running2.ui.fragment.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import com.jieli.stream.dv.running2.util.ToastUtil;

/* loaded from: classes.dex */
public class DeviceStaModeFragment extends BaseFragment {
    private Button btnSwitch;
    private EditText editWifiPwd;
    private EditText editWifiSSID;
    private boolean isSaveMsg;
    private boolean isShowPwd;
    private ImageView ivShowOrHidePwd;
    private Handler mHandler = new Handler();
    private View.OnClickListener mOnClickListener = new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceStaModeFragment.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (DeviceStaModeFragment.this.isDetached() || view == null) {
                return;
            }
            int id = view.getId();
            if (id == R.id.switch_sta_btn) {
                DeviceStaModeFragment.this.sendRouterInformation();
            } else if (id == R.id.show_or_hide_pwd) {
                DeviceStaModeFragment.this.isShowPwd = !DeviceStaModeFragment.this.isShowPwd;
                DeviceStaModeFragment.this.handlerPwdUI();
            }
        }
    };
    private CheckBox mSaveSTAMsgCheckbox;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_device_sta_mode, viewGroup, false);
        this.editWifiSSID = (EditText) inflate.findViewById(R.id.edit_wifi_ssid);
        this.editWifiPwd = (EditText) inflate.findViewById(R.id.edit_wifi_pwd);
        this.ivShowOrHidePwd = (ImageView) inflate.findViewById(R.id.show_or_hide_pwd);
        this.mSaveSTAMsgCheckbox = (CheckBox) inflate.findViewById(R.id.save_sta_msg);
        this.btnSwitch = (Button) inflate.findViewById(R.id.switch_sta_btn);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.isSaveMsg = this.mSaveSTAMsgCheckbox.isChecked();
        this.ivShowOrHidePwd.setOnClickListener(this.mOnClickListener);
        this.btnSwitch.setOnClickListener(this.mOnClickListener);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendRouterInformation() {
        final String trim = this.editWifiSSID.getText().toString().trim();
        String trim2 = this.editWifiPwd.getText().toString().trim();
        this.isSaveMsg = this.mSaveSTAMsgCheckbox.isChecked();
        if (TextUtils.isEmpty(trim)) {
            ToastUtil.showToastShort(getString(R.string.wifi_ssid_empty_tip));
            return;
        }
        if (TextUtils.isEmpty(trim2)) {
            trim2 = "";
        } else if (trim2.length() < 8) {
            ToastUtil.showToastShort(getString(R.string.pwd_lenth_limits));
            return;
        }
        String finalTrim = trim2;
        ClientManager.getClient().tryToSetSTAAccount(trim, trim2, this.isSaveMsg, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceStaModeFragment.2
            @Override // com.jieli.lib.dv.control.connect.response.Response
            public void onResponse(Integer num) {
                if (num.intValue() == 1) {
                    Dbug.i(DeviceStaModeFragment.this.TAG, "send set sta cmd ok");
                    ClientManager.getClient().disconnect();
                    DeviceStaModeFragment.this.saveApMsg();
                    if (DeviceStaModeFragment.this.mHandler != null) {
                        DeviceStaModeFragment.this.mHandler.postDelayed(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceStaModeFragment.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                DeviceStaModeFragment.this.mApplication.setSearchMode(1);
                                DeviceStaModeFragment.this.mWifiHelper.connectWifi(DeviceStaModeFragment.this.mApplication, trim, finalTrim);
                                if (DeviceStaModeFragment.this.getActivity() != null) {
                                    DeviceStaModeFragment.this.getActivity().finish();
                                }
                            }
                        }, 1000L);
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlerPwdUI() {
        if (this.isShowPwd) {
            this.editWifiPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            this.ivShowOrHidePwd.setImageResource(R.drawable.dbg_show_pwd_selector);
        } else {
            this.editWifiPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.editWifiPwd.requestFocus();
            this.ivShowOrHidePwd.setImageResource(R.mipmap.ic_hide_pwd);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveApMsg() {
        SharedPreferences sharedPreferences = PreferencesHelper.getSharedPreferences(MainApplication.getApplication());
        String string = sharedPreferences.getString(IConstant.CURRENT_WIFI_SSID, null);
        String uuid = MainApplication.getApplication().getUUID();
        if (TextUtils.isEmpty(string) || TextUtils.isEmpty(uuid)) {
            return;
        }
        String string2 = sharedPreferences.getString(string, null);
        PreferencesHelper.putStringValue(MainApplication.getApplication(), uuid, string);
        PreferencesHelper.putStringValue(MainApplication.getApplication(), string, string2);
    }
}
