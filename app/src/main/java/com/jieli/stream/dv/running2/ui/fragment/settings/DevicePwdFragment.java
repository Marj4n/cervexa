package com.jieli.stream.dv.running2.ui.fragment.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.utils.Code;
import com.jieli.lib.dv.control.utils.Topic;
import com.jieli.lib.dv.control.utils.TopicKey;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.ui.dialog.NotifyDialog;
import com.jieli.stream.dv.running2.ui.dialog.PasswordConfirmDialog;
import com.jieli.stream.dv.running2.ui.dialog.WaitingDialog;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IActions;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import com.jieli.stream.dv.running2.util.ToastUtil;
import com.jieli.stream.dv.running2.util.WifiHelper;

/* loaded from: classes.dex */
public class DevicePwdFragment extends BaseFragment {
    private PasswordConfirmDialog changePwdDialog;
    private String currentPwd;
    private String currentSsid;
    private NotifyDialog notifyDialog;
    private EditText pwdConfirmEditText;
    private EditText pwdEditText;
    private WaitingDialog waitingDialog;
    private String tag = getClass().getSimpleName();
    private boolean isChanged = false;
    private final OnNotifyListener onNotifyListener = new OnNotifyListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DevicePwdFragment.6
        @Override // com.jieli.lib.dv.control.receiver.listener.NotifyResponse
        public void onNotify(NotifyInfo notifyInfo) {
            if (notifyInfo.getErrorType() != 0) {
                Dbug.e(DevicePwdFragment.this.tag, Code.getCodeDescription(notifyInfo.getErrorType()));
                ToastUtil.showToastShort(DevicePwdFragment.this.getString(R.string.setting_failed));
                return;
            }
            String topic = notifyInfo.getTopic();
            char c = 65535;
            if (topic.hashCode() == 1913840354 && topic.equals(Topic.AP_SSID_INFO)) {
                c = 0;
            }
            if (c != 0) {
                return;
            }
            if (DevicePwdFragment.this.isChanged) {
                DevicePwdFragment.this.isChanged = false;
                ToastUtil.showToastShort(DevicePwdFragment.this.getString(R.string.setting_successed));
            }
            DevicePwdFragment.this.currentSsid = notifyInfo.getParams().get(TopicKey.SSID);
            DevicePwdFragment.this.currentPwd = notifyInfo.getParams().get(TopicKey.PWD);
            if (DevicePwdFragment.this.currentPwd == null || !DevicePwdFragment.this.currentPwd.contains(IConstant.DEFAULT_PATH)) {
                if (DevicePwdFragment.this.currentPwd == null) {
                    DevicePwdFragment.this.currentPwd = "";
                }
            } else {
                DevicePwdFragment.this.currentPwd = "";
            }
            if (TextUtils.isEmpty(DevicePwdFragment.this.currentSsid)) {
                return;
            }
            PreferencesHelper.putStringValue(DevicePwdFragment.this.mApplication, DevicePwdFragment.this.currentSsid, DevicePwdFragment.this.currentPwd);
        }
    };

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_device_setting_pwd, viewGroup, false);
        Button button = (Button) inflate.findViewById(R.id.pwd_setting_save_btn);
        this.pwdEditText = (EditText) inflate.findViewById(R.id.pwd_setting_et);
        this.pwdConfirmEditText = (EditText) inflate.findViewById(R.id.pwd_setting_comfir_et);
        ClientManager.getClient().tryToRequestApInfo(new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DevicePwdFragment.1
            @Override // com.jieli.lib.dv.control.connect.response.Response
            public void onResponse(Integer num) {
                if (num.intValue() != 1) {
                    Dbug.e(DevicePwdFragment.this.tag, "send failed");
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DevicePwdFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String obj = DevicePwdFragment.this.pwdEditText.getText().toString();
                String obj2 = DevicePwdFragment.this.pwdConfirmEditText.getText().toString();
                if (obj.length() == 0 && obj2.length() == 0) {
                    if (!TextUtils.isEmpty(DevicePwdFragment.this.currentPwd)) {
                        DevicePwdFragment.this.showChoseDialog();
                        return;
                    } else {
                        ToastUtil.showToastShort(DevicePwdFragment.this.getString(R.string.new_and_old_pwd_consistent));
                        return;
                    }
                }
                if (obj.equals(obj2)) {
                    if (DevicePwdFragment.this.currentPwd != null && DevicePwdFragment.this.currentPwd.equals(obj)) {
                        ToastUtil.showToastShort(DevicePwdFragment.this.getString(R.string.new_and_old_pwd_consistent));
                        return;
                    } else if (obj.length() >= 8) {
                        DevicePwdFragment.this.showChoseDialog();
                        return;
                    } else {
                        ToastUtil.showToastShort(DevicePwdFragment.this.getString(R.string.pwd_lenth_limits));
                        return;
                    }
                }
                ToastUtil.showToastShort(DevicePwdFragment.this.getString(R.string.pwd_check_fail));
            }
        });
        return inflate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPwdInputDialog(boolean z) {
        if (this.changePwdDialog == null) {
            PasswordConfirmDialog newInstance = PasswordConfirmDialog.newInstance(getString(R.string.input_old_pwd));
            this.changePwdDialog = newInstance;
            newInstance.setOnInputCompletionListener(new AnonymousClass3(z));
        }
        this.changePwdDialog.show(getFragmentManager(), "changePwdDialog");
    }

    /* renamed from: com.jieli.stream.dv.running2.ui.fragment.settings.DevicePwdFragment$3, reason: invalid class name */
    class AnonymousClass3 implements PasswordConfirmDialog.OnInputCompletionListener {
        final /* synthetic */ boolean val$immediateEffect;

        AnonymousClass3(boolean z) {
            this.val$immediateEffect = z;
        }

        @Override // com.jieli.stream.dv.running2.ui.dialog.PasswordConfirmDialog.OnInputCompletionListener
        public void onCompletion(String str, String str2) {
            if ((!TextUtils.isEmpty(str2) || !TextUtils.isEmpty(DevicePwdFragment.this.currentPwd)) && (TextUtils.isEmpty(str2) || !str2.equals(DevicePwdFragment.this.currentPwd))) {
                Dbug.e(DevicePwdFragment.this.tag, "Error:current pwd=" + DevicePwdFragment.this.currentPwd + ", password=" + str2);
                ToastUtil.showToastShort(DevicePwdFragment.this.getString(R.string.input_pld_pws_error));
                return;
            }
            if (this.val$immediateEffect) {
                if (DevicePwdFragment.this.waitingDialog == null) {
                    DevicePwdFragment.this.waitingDialog = new WaitingDialog();
                    DevicePwdFragment.this.waitingDialog.setNotifyContent(DevicePwdFragment.this.getString(R.string.dialod_wait));
                }
                DevicePwdFragment.this.waitingDialog.show(DevicePwdFragment.this.getFragmentManager(), "change_name_wait_dialog");
            }
            ClientManager.getClient().tryToSetApAccount(DevicePwdFragment.this.currentSsid, DevicePwdFragment.this.pwdEditText.getText().toString().trim(), this.val$immediateEffect, new AnonymousClass1());
        }

        /* renamed from: com.jieli.stream.dv.running2.ui.fragment.settings.DevicePwdFragment$3$1, reason: invalid class name */
        class AnonymousClass1 implements SendResponse {
            AnonymousClass1() {
            }

            /* JADX WARN: Type inference failed for: r3v12, types: [com.jieli.stream.dv.running2.ui.fragment.settings.DevicePwdFragment$3$1$1] */
            @Override // com.jieli.lib.dv.control.connect.response.Response
            public void onResponse(Integer num) {
                if (num.intValue() == 1) {
                    DevicePwdFragment.this.isChanged = true;
                    if (AnonymousClass3.this.val$immediateEffect) {
                        DevicePwdFragment.this.mApplication.sendBroadcast(new Intent(IActions.ACTION_ACCOUT_CHANGE));
                        PreferencesHelper.remove(DevicePwdFragment.this.mApplication, DevicePwdFragment.this.currentSsid);
                        new Thread() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DevicePwdFragment.3.1.1
                            @Override // java.lang.Thread, java.lang.Runnable
                            public void run() {
                                super.run();
                                try {
                                    DevicePwdFragment.this.mApplication.switchWifi();
                                    Thread.sleep(10000L);
                                    WifiHelper.getInstance(DevicePwdFragment.this.mApplication).removeSavedNetWork(DevicePwdFragment.this.currentSsid);
                                    DevicePwdFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DevicePwdFragment.3.1.1.1
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            if (DevicePwdFragment.this.waitingDialog != null && DevicePwdFragment.this.waitingDialog.isShowing()) {
                                                DevicePwdFragment.this.waitingDialog.dismiss();
                                            }
                                            DevicePwdFragment.this.getActivity().finish();
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        return;
                    }
                    return;
                }
                ToastUtil.showToastShort(DevicePwdFragment.this.getString(R.string.setting_failed));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showChoseDialog() {
        if (this.notifyDialog == null) {
            this.notifyDialog = NotifyDialog.newInstance(R.string.dialog_tips, R.string.immediate_effect, R.string.dialog_no, R.string.dialog_yes, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DevicePwdFragment.4
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                public void onClick() {
                    DevicePwdFragment.this.showPwdInputDialog(false);
                    DevicePwdFragment.this.notifyDialog.dismiss();
                }
            }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DevicePwdFragment.5
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                public void onClick() {
                    DevicePwdFragment.this.showPwdInputDialog(true);
                    DevicePwdFragment.this.notifyDialog.dismiss();
                }
            });
        }
        this.notifyDialog.show(getFragmentManager(), "change_name_dialog");
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        ClientManager.getClient().registerNotifyListener(this.onNotifyListener);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        ClientManager.getClient().unregisterNotifyListener(this.onNotifyListener);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        NotifyDialog notifyDialog = this.notifyDialog;
        if (notifyDialog != null && notifyDialog.isShowing()) {
            this.notifyDialog.dismiss();
        }
        WaitingDialog waitingDialog = this.waitingDialog;
        if (waitingDialog != null && waitingDialog.isShowing()) {
            this.waitingDialog.dismiss();
        }
        this.notifyDialog = null;
        this.waitingDialog = null;
        PasswordConfirmDialog passwordConfirmDialog = this.changePwdDialog;
        if (passwordConfirmDialog != null && passwordConfirmDialog.isShowing()) {
            this.changePwdDialog.dismiss();
        }
        this.changePwdDialog = null;
    }
}
