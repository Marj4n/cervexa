package com.jieli.stream.dv.running2.ui.fragment.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.utils.Code;
import com.jieli.lib.dv.control.utils.Topic;
import com.jieli.lib.dv.control.utils.TopicKey;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.ui.dialog.NotifyDialog;
import com.jieli.stream.dv.running2.ui.dialog.WaitingDialog;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IActions;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import com.jieli.stream.dv.running2.util.ToastUtil;
import com.jieli.stream.dv.running2.util.WifiHelper;
import java.io.UnsupportedEncodingException;

/* loaded from: classes.dex */
public class DeviceNameFragment extends BaseFragment {
    private static final int NAME_MAX_LEN = 31;
    private String mCurrentPwd;
    private String mCurrentSsid;
    private EditText nameEditText;
    private NotifyDialog notifyDialog;
    private Button saveBtn;
    private String tipString;
    private TextView tipTv;
    private WaitingDialog waitingDialog;
    private String tag = getClass().getSimpleName();
    private final int prefixLen = IConstant.WIFI_PREFIX.getBytes().length;
    private int mInputCount = 0;
    private boolean isModified = false;
    private final OnNotifyListener onNotifyListener = new OnNotifyListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceNameFragment.7
        @Override // com.jieli.lib.dv.control.receiver.listener.NotifyResponse
        public void onNotify(NotifyInfo notifyInfo) {
            if (notifyInfo.getErrorType() != 0) {
                Dbug.e(DeviceNameFragment.this.tag, Code.getCodeDescription(notifyInfo.getErrorType()));
                if (DeviceNameFragment.this.notifyDialog != null && DeviceNameFragment.this.notifyDialog.isShowing()) {
                    DeviceNameFragment.this.notifyDialog.dismiss();
                }
                ToastUtil.showToastShort(DeviceNameFragment.this.getString(R.string.setting_failed));
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
            String str = "";
            if (DeviceNameFragment.this.isModified) {
                DeviceNameFragment.this.isModified = false;
                ToastUtil.showToastShort(DeviceNameFragment.this.getString(R.string.setting_successed));
                WifiHelper.getInstance(DeviceNameFragment.this.getContext()).removeSavedNetWork(PreferencesHelper.getSharedPreferences(DeviceNameFragment.this.getContext()).getString(IConstant.CURRENT_WIFI_SSID, ""));
                PreferencesHelper.putStringValue(DeviceNameFragment.this.getActivity().getApplicationContext(), IConstant.CURRENT_WIFI_SSID, IConstant.WIFI_PREFIX + DeviceNameFragment.this.nameEditText.getText().toString().trim());
                return;
            }
            String str2 = notifyInfo.getParams().get(TopicKey.SSID);
            String str3 = notifyInfo.getParams().get(TopicKey.PWD);
            if ((str3 == null || !str3.contains(IConstant.DEFAULT_PATH)) && str3 != null) {
                str = str3;
            }
            DeviceNameFragment.this.mCurrentSsid = str2;
            DeviceNameFragment.this.mCurrentPwd = str;
        }
    };

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_device_setting_name, viewGroup, false);
        this.saveBtn = (Button) inflate.findViewById(R.id.device_setting_name_save_btn);
        this.nameEditText = (EditText) inflate.findViewById(R.id.device_setting_name_et);
        this.tipTv = (TextView) inflate.findViewById(R.id.tip_tv);
        ((TextView) inflate.findViewById(R.id.wifi_ssid_prefix_tv)).setText(IConstant.WIFI_PREFIX);
        String formatSSID = WifiHelper.formatSSID(this.mWifiHelper.getWifiConnectionInfo().getSSID());
        if (!TextUtils.isEmpty(formatSSID)) {
            this.nameEditText.setText(formatSSID.substring(this.prefixLen));
        }
        ClientManager.getClient().tryToRequestApInfo(new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceNameFragment.1
            @Override // com.jieli.lib.dv.control.connect.response.Response
            public void onResponse(Integer num) {
                if (num.intValue() != 1) {
                    Dbug.e(DeviceNameFragment.this.tag, "send failed");
                }
            }
        });
        try {
            String format = String.format(getString(R.string.device_name_input_limit), Integer.valueOf(31 - this.prefixLen), Integer.valueOf(this.nameEditText.getText().toString().getBytes("utf-8").length));
            this.tipString = format;
            this.tipTv.setText(format);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.nameEditText.addTextChangedListener(new TextWatcher() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceNameFragment.2
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                DeviceNameFragment.this.mInputCount = i3;
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                int i;
                if (!editable.toString().contains(" ") && !editable.toString().contains("\n")) {
                    int curLength = DeviceNameFragment.this.getCurLength(editable);
                    try {
                        i = DeviceNameFragment.this.nameEditText.getText().toString().getBytes("utf-8").length + DeviceNameFragment.this.prefixLen;
                    } catch (UnsupportedEncodingException e2) {
                        e2.printStackTrace();
                        i = 0;
                    }
                    if (i > 31) {
                        int i2 = 31 - DeviceNameFragment.this.prefixLen;
                        if (i2 > editable.length()) {
                            if (curLength > 31 - DeviceNameFragment.this.prefixLen) {
                                editable.delete(editable.length() - DeviceNameFragment.this.mInputCount, editable.length());
                            }
                        } else {
                            editable.delete(i2, editable.length());
                        }
                    } else {
                        DeviceNameFragment.this.tipTv.setText(R.string.device_name_input_limit);
                        DeviceNameFragment.this.tipTv.setTextColor(DeviceNameFragment.this.getResources().getColor(R.color.text_press_gray));
                    }
                    try {
                        DeviceNameFragment.this.tipString = String.format(DeviceNameFragment.this.getString(R.string.device_name_input_limit), Integer.valueOf(31 - DeviceNameFragment.this.prefixLen), Integer.valueOf(editable.toString().getBytes("utf-8").length));
                        DeviceNameFragment.this.tipTv.setText(DeviceNameFragment.this.tipString);
                        return;
                    } catch (UnsupportedEncodingException e3) {
                        e3.printStackTrace();
                        return;
                    }
                }
                String replaceAll = editable.toString().replaceAll(" ", "").replaceAll("\n", "");
                DeviceNameFragment.this.nameEditText.setText(replaceAll);
                DeviceNameFragment.this.nameEditText.setSelection(replaceAll.length());
            }
        });
        this.saveBtn.setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceNameFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int length = DeviceNameFragment.this.nameEditText.getText().toString().length();
                if (length >= 1 && length <= 31 - DeviceNameFragment.this.prefixLen) {
                    DeviceNameFragment.this.showChoseDialog();
                } else {
                    ToastUtil.showToastShort(DeviceNameFragment.this.getString(R.string.name_format_error));
                }
            }
        });
        return inflate;
    }

    private class CustomNameFilter implements InputFilter {
        private int maxLength;

        CustomNameFilter(int i) {
            this.maxLength = i;
        }

        @Override // android.text.InputFilter
        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            if (TextUtils.isEmpty(charSequence)) {
                return null;
            }
            int curLength = DeviceNameFragment.this.getCurLength(charSequence);
            int curLength2 = spanned.length() != 0 ? DeviceNameFragment.this.getCurLength(spanned) : 0;
            int i5 = this.maxLength;
            if (curLength2 > i5 || curLength > i5) {
                return "";
            }
            int i6 = curLength + curLength2;
            if (spanned.length() != 0) {
                try {
                    int length = DeviceNameFragment.this.nameEditText.getText().toString().getBytes("utf-8").length;
                    int unused = DeviceNameFragment.this.prefixLen;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                DeviceNameFragment deviceNameFragment = DeviceNameFragment.this;
                int curLength3 = deviceNameFragment.getCurLength(deviceNameFragment.nameEditText.getText().toString().trim()) + curLength;
                int i7 = this.maxLength;
                if (curLength3 <= i7) {
                    return null;
                }
                CharSequence sub = DeviceNameFragment.this.sub(charSequence, i7 - curLength2);
                Dbug.i(DeviceNameFragment.this.tag, "end sub=" + ((Object) sub));
                return "";
            }
            int i8 = this.maxLength;
            if (i6 <= i8) {
                return null;
            }
            CharSequence sub2 = DeviceNameFragment.this.sub(charSequence, i8);
            Dbug.i(DeviceNameFragment.this.tag, "sub=" + ((Object) sub2));
            return sub2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CharSequence sub(CharSequence charSequence, int i) {
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < charSequence.length(); i4++) {
            i2 = charSequence.charAt(i4) < 128 ? i2 + 1 : i2 + 3;
            i3++;
            if (i <= i2) {
                return charSequence.subSequence(0, i3);
            }
        }
        return charSequence;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurLength(CharSequence charSequence) {
        if (charSequence == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < charSequence.length(); i2++) {
            i = charSequence.charAt(i2) < 128 ? i + 1 : i + 3;
        }
        return i;
    }

    /* renamed from: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceNameFragment$4, reason: invalid class name */
    class AnonymousClass4 implements SendResponse {
        final /* synthetic */ boolean val$immediateEffect;

        AnonymousClass4(boolean z) {
            this.val$immediateEffect = z;
        }

        /* JADX WARN: Type inference failed for: r3v8, types: [com.jieli.stream.dv.running2.ui.fragment.settings.DeviceNameFragment$4$1] */
        @Override // com.jieli.lib.dv.control.connect.response.Response
        public void onResponse(Integer num) {
            if (num.intValue() == 1) {
                DeviceNameFragment.this.isModified = true;
                if (this.val$immediateEffect) {
                    DeviceNameFragment.this.getActivity().sendBroadcast(new Intent(IActions.ACTION_ACCOUT_CHANGE));
                    PreferencesHelper.remove(DeviceNameFragment.this.getContext(), DeviceNameFragment.this.mCurrentSsid);
                    new Thread() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceNameFragment.4.1
                        @Override // java.lang.Thread, java.lang.Runnable
                        public void run() {
                            super.run();
                            try {
                                Thread.sleep(10000L);
                                WifiHelper.getInstance(DeviceNameFragment.this.getContext()).removeSavedNetWork(DeviceNameFragment.this.mCurrentSsid);
                                DeviceNameFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceNameFragment.4.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (DeviceNameFragment.this.waitingDialog != null && DeviceNameFragment.this.waitingDialog.isShowing()) {
                                            DeviceNameFragment.this.waitingDialog.dismiss();
                                        }
                                        DeviceNameFragment.this.getActivity().finish();
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
            ToastUtil.showToastShort(DeviceNameFragment.this.getString(R.string.save_fail));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeName(boolean z) {
        ClientManager.getClient().tryToSetApAccount(WIFI_PREFIX + this.nameEditText.getText().toString().trim(), this.mCurrentPwd, z, new AnonymousClass4(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showChoseDialog() {
        if (this.notifyDialog == null) {
            this.notifyDialog = NotifyDialog.newInstance(R.string.dialog_tips, R.string.immediate_effect, R.string.dialog_no, R.string.dialog_yes, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceNameFragment.5
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                public void onClick() {
                    DeviceNameFragment.this.changeName(false);
                    DeviceNameFragment.this.notifyDialog.dismiss();
                }
            }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceNameFragment.6
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                public void onClick() {
                    DeviceNameFragment.this.changeName(true);
                    DeviceNameFragment.this.notifyDialog.dismiss();
                    if (DeviceNameFragment.this.waitingDialog == null) {
                        DeviceNameFragment.this.waitingDialog = new WaitingDialog();
                        DeviceNameFragment.this.waitingDialog.setNotifyContent(DeviceNameFragment.this.getString(R.string.dialod_wait));
                    }
                    DeviceNameFragment.this.waitingDialog.show(DeviceNameFragment.this.getFragmentManager(), "change_name_wait_dialog");
                }
            });
        }
        this.notifyDialog.show(getFragmentManager(), "change_name_dialog");
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
}
