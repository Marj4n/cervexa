package com.jieli.stream.dv.running2.ui.fragment.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.utils.Code;
import com.jieli.lib.dv.control.utils.Topic;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.SettingItem;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.ui.activity.me.DeviceSettingActivity;
import com.jieli.stream.dv.running2.ui.adapter.SettingAdapter;
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
import java.util.ArrayList;

/* loaded from: classes.dex */
public class DeviceSettingFragment extends BaseFragment {
    private static final int MSG_FINISH_FRAGMENT = 25426;
    private ListView listView;
    private NotifyDialog mNotifyDialog;
    private NotifyDialog mResetDialog;
    private WaitingDialog mWaitingDialog;
    private NotifyDialog notifyDialog;
    private String tag = getClass().getSimpleName();
    private final int DEV_ADVANCED_SETTING = 0;
    private final int FACTORY_RESET = 1;
    private final int RESOLUTION_SETTING = 2;
    private Handler mHandler = new Handler(new Handler.Callback() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceSettingFragment.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (message == null || message.what != DeviceSettingFragment.MSG_FINISH_FRAGMENT) {
                return false;
            }
            DeviceSettingFragment.this.dismissWaitingDialog();
            if (DeviceSettingFragment.this.getActivity() == null) {
                return false;
            }
            DeviceSettingFragment.this.getActivity().finish();
            return false;
        }
    });
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceSettingFragment.3
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            SettingItem settingItem = (SettingItem) DeviceSettingFragment.this.listView.getAdapter().getItem(i);
            if (settingItem.getType() == 0) {
                DeviceSettingActivity deviceSettingActivity = (DeviceSettingActivity) DeviceSettingFragment.this.getActivity();
                if (settingItem.getName().equals(DeviceSettingFragment.this.getString(R.string.device_name))) {
                    deviceSettingActivity.toDeviceNameFragment();
                    return;
                }
                if (settingItem.getName().equals(DeviceSettingFragment.this.getString(R.string.device_password))) {
                    if (TextUtils.isEmpty(DeviceSettingFragment.this.getDeviceName())) {
                        ToastUtil.showToastShort(DeviceSettingFragment.this.getString(R.string.name_empty_error));
                        return;
                    } else {
                        deviceSettingActivity.toDevicePwdFragment();
                        return;
                    }
                }
                if (settingItem.getName().equals(DeviceSettingFragment.this.getString(R.string.volume_adjustment))) {
                    deviceSettingActivity.toDeviceVolumeFragment();
                    return;
                }
                if (settingItem.getName().equals(DeviceSettingFragment.this.getString(R.string.photo_quality))) {
                    deviceSettingActivity.toDevicePictureQualityFragment();
                    return;
                }
                if (settingItem.getName().equals(DeviceSettingFragment.this.getString(R.string.setting_record_quality))) {
                    if (DeviceSettingFragment.this.mApplication.getDeviceDesc() != null) {
                        String[] recordFrontSupport = DeviceSettingFragment.this.mApplication.getDeviceDesc().getRecordFrontSupport();
                        String[] recordRearSupport = DeviceSettingFragment.this.mApplication.getDeviceDesc().getRecordRearSupport();
                        if ((recordFrontSupport == null || recordFrontSupport.length <= 0) && (recordRearSupport == null || recordRearSupport.length > 0)) {
                            ToastUtil.showToastLong(DeviceSettingFragment.this.getString(R.string.curr_dev_not_support));
                            return;
                        }
                    }
                    if (1 == DeviceSettingFragment.this.mApplication.getDeviceSettingInfo().getRecordState()) {
                        DeviceSettingFragment.this.showStopRecordingDialog(deviceSettingActivity, 2);
                        return;
                    } else {
                        deviceSettingActivity.toDeviceRecordQualityFragment();
                        return;
                    }
                }
                if (settingItem.getName().equals(DeviceSettingFragment.this.getString(R.string.camera_model))) {
                    deviceSettingActivity.toDeviceCameraModeFragment();
                    return;
                }
                if (settingItem.getName().equals(DeviceSettingFragment.this.getString(R.string.advanced_settings))) {
                    if (1 == DeviceSettingFragment.this.mApplication.getDeviceSettingInfo().getRecordState()) {
                        DeviceSettingFragment.this.showStopRecordingDialog(deviceSettingActivity, 0);
                        return;
                    } else {
                        deviceSettingActivity.toDeviceAdvancedSettingFragment();
                        return;
                    }
                }
                if (settingItem.getName().equals(DeviceSettingFragment.this.getString(R.string.device_storage_manage))) {
                    if (DeviceSettingFragment.this.mApplication.isSdcardExist()) {
                        deviceSettingActivity.toDeviceStorageManageFragment();
                        return;
                    } else {
                        ToastUtil.showToastShort(DeviceSettingFragment.this.getString(R.string.sdcard_offline));
                        return;
                    }
                }
                if (settingItem.getName().equals(DeviceSettingFragment.this.getString(R.string.switch_sta_mode))) {
                    deviceSettingActivity.toDeviceStaModeFragment();
                    return;
                }
                if (settingItem.getName().equals(DeviceSettingFragment.this.getString(R.string.switch_ap_mode))) {
                    DeviceSettingFragment.this.showNotifyDialog();
                } else if (settingItem.getName().equals(DeviceSettingFragment.this.getString(R.string.factory_reset))) {
                    if (1 == DeviceSettingFragment.this.mApplication.getDeviceSettingInfo().getRecordState()) {
                        DeviceSettingFragment.this.showStopRecordingDialog(deviceSettingActivity, 1);
                    } else {
                        DeviceSettingFragment.this.showFactoryResetDialog();
                    }
                }
            }
        }
    };
    private final OnNotifyListener onNotifyListener = new OnNotifyListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceSettingFragment.9
        @Override // com.jieli.lib.dv.control.receiver.listener.NotifyResponse
        public void onNotify(NotifyInfo notifyInfo) {
            if (notifyInfo.getErrorType() != 0) {
                Dbug.e(DeviceSettingFragment.this.tag, Code.getCodeDescription(notifyInfo.getErrorType()));
                return;
            }
            String topic = notifyInfo.getTopic();
            char c = 65535;
            if (topic.hashCode() == -174141373 && topic.equals(Topic.VIDEO_MIC)) {
                c = 0;
            }
            if (c != 0) {
                return;
            }
            ToastUtil.showToastShort(DeviceSettingFragment.this.getString(R.string.setting_successed));
        }
    };

    @Override // com.jieli.stream.dv.running2.ui.base.BaseFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ClientManager.getClient().tryToRequestTFCardCapacity(new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceSettingFragment.2
            @Override // com.jieli.lib.dv.control.connect.response.Response
            public void onResponse(Integer num) {
                if (1 != num.intValue()) {
                    Dbug.e(DeviceSettingFragment.this.tag, "Send failed!!");
                }
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_device_setting, viewGroup, false);
        this.listView = (ListView) inflate.findViewById(R.id.device_setting_list_view);
        initUI();
        return inflate;
    }

    private void initUI() {
        String[] stringArray;
        if (this.mApplication.getSearchMode() == 1) {
            stringArray = getResources().getStringArray(R.array.device_setting_list_sta);
        } else {
            stringArray = getResources().getStringArray(R.array.device_setting_list);
        }
        ArrayList arrayList = new ArrayList();
        int dimension = (int) getResources().getDimension(R.dimen.list_marginTop);
        for (int i = 0; i < stringArray.length; i++) {
            String str = stringArray[i];
            if (!TextUtils.isEmpty(str)) {
                SettingItem settingItem = new SettingItem();
                if (!str.equals(getString(R.string.device_password)) && !str.equals(getString(R.string.photo_quality)) && !str.equals(getString(R.string.setting_record_quality)) && !str.equals(getString(R.string.advanced_settings)) && !str.equals(getString(R.string.device_storage_manage)) && !str.equals(getString(R.string.factory_reset)) && !str.equals(getString(R.string.switch_sta_mode)) && !str.equals(getString(R.string.switch_ap_mode)) && str.equals(getString(R.string.device_name))) {
                    settingItem.setType(0);
                    settingItem.setValue(getDeviceName());
                }
                if (i == 0 || i == 2 || i == 5) {
                    settingItem.setMarginTop(dimension);
                }
                settingItem.setName(str);
                arrayList.add(settingItem);
            }
        }
        this.listView.setAdapter((ListAdapter) new SettingAdapter(getActivity().getApplicationContext(), arrayList));
        this.listView.setOnItemClickListener(this.onItemClickListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDeviceName() {
        return WifiHelper.formatSSID(this.mWifiHelper.getWifiConnectionInfo().getSSID());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryToSendFactoryReset() {
        ClientManager.getClient().tryToFactoryReset(new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceSettingFragment.4
            @Override // com.jieli.lib.dv.control.connect.response.Response
            public void onResponse(Integer num) {
                if (num.intValue() != 1) {
                    Dbug.e(DeviceSettingFragment.this.tag, "Send failed");
                    return;
                }
                DeviceSettingFragment.this.mResetDialog.dismiss();
                DeviceSettingFragment.this.removeDeviceWifiMsg();
                if (DeviceSettingFragment.this.getActivity() != null) {
                    DeviceSettingFragment.this.getActivity().sendBroadcast(new Intent(IActions.ACTION_ACCOUT_CHANGE));
                }
                DeviceSettingFragment.this.showWaitingDialog();
                if (DeviceSettingFragment.this.mHandler != null) {
                    DeviceSettingFragment.this.mHandler.sendEmptyMessageDelayed(DeviceSettingFragment.MSG_FINISH_FRAGMENT, 10000L);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showFactoryResetDialog() {
        if (this.mResetDialog == null) {
            NotifyDialog newInstance = NotifyDialog.newInstance(R.string.dialog_tips, R.string.factory_reset_tips, R.string.dialog_cancel, R.string.dialog_confirm, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceSettingFragment.5
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                public void onClick() {
                    DeviceSettingFragment.this.mResetDialog.dismiss();
                }
            }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceSettingFragment.6
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                public void onClick() {
                    DeviceSettingFragment.this.tryToSendFactoryReset();
                }
            });
            this.mResetDialog = newInstance;
            newInstance.setCancelable(false);
        }
        if (this.mResetDialog.isShowing()) {
            return;
        }
        this.mResetDialog.show(getActivity().getSupportFragmentManager(), "notify_dialog");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showStopRecordingDialog(final DeviceSettingActivity deviceSettingActivity, final int i) {
        NotifyDialog newInstance = NotifyDialog.newInstance(R.string.dialog_tips, R.string.stop_recording_tips, R.string.dialog_cancel, R.string.dialog_confirm, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceSettingFragment.7
            @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
            public void onClick() {
                if (DeviceSettingFragment.this.mNotifyDialog != null) {
                    DeviceSettingFragment.this.mNotifyDialog.dismiss();
                }
            }
        }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceSettingFragment.8
            @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
            public void onClick() {
                ClientManager.getClient().tryToRecordVideo(false, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceSettingFragment.8.1
                    @Override // com.jieli.lib.dv.control.connect.response.Response
                    public void onResponse(Integer num) {
                        if (num.intValue() != 1) {
                            Dbug.e(DeviceSettingFragment.this.tag, "Send failed");
                            return;
                        }
                        if (DeviceSettingFragment.this.mNotifyDialog != null) {
                            DeviceSettingFragment.this.mNotifyDialog.dismiss();
                        }
                        int i2 = i;
                        if (i2 == 0) {
                            if (deviceSettingActivity != null) {
                                deviceSettingActivity.toDeviceAdvancedSettingFragment();
                            }
                        } else {
                            if (i2 == 1) {
                                DeviceSettingFragment.this.showFactoryResetDialog();
                                return;
                            }
                            if (i2 != 2) {
                                Dbug.e(DeviceSettingFragment.this.tag, "Unknown OP:" + i);
                                return;
                            }
                            if (deviceSettingActivity != null) {
                                deviceSettingActivity.toDeviceRecordQualityFragment();
                            }
                        }
                    }
                });
            }
        });
        this.mNotifyDialog = newInstance;
        newInstance.setCancelable(false);
        if (this.mNotifyDialog.isShowing()) {
            return;
        }
        this.mNotifyDialog.show(getActivity().getSupportFragmentManager(), "notify_dialog");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeDeviceWifiMsg() {
        String string = PreferencesHelper.getSharedPreferences(MainApplication.getApplication()).getString(IConstant.CURRENT_WIFI_SSID, null);
        if (TextUtils.isEmpty(string)) {
            return;
        }
        if (!TextUtils.isEmpty(string) && string.startsWith(WIFI_PREFIX)) {
            WifiHelper.getInstance(MainApplication.getApplication()).removeSavedNetWork(string);
        }
        PreferencesHelper.remove(MainApplication.getApplication(), string);
        PreferencesHelper.remove(MainApplication.getApplication(), IConstant.CURRENT_WIFI_SSID);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        ClientManager.getClient().registerNotifyListener(this.onNotifyListener);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        dismissNotifyDialog();
        NotifyDialog notifyDialog = this.mNotifyDialog;
        if (notifyDialog != null && notifyDialog.isShowing()) {
            this.mNotifyDialog.dismiss();
        }
        this.mNotifyDialog = null;
        NotifyDialog notifyDialog2 = this.mResetDialog;
        if (notifyDialog2 != null && notifyDialog2.isShowing()) {
            this.mResetDialog.dismiss();
        }
        this.mResetDialog = null;
        super.onStop();
        ClientManager.getClient().unregisterNotifyListener(this.onNotifyListener);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        dismissWaitingDialog();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onDetach();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showNotifyDialog() {
        if (this.notifyDialog == null) {
            NotifyDialog newInstance = NotifyDialog.newInstance(R.string.dialog_warning, R.string.waring_operation_tip, R.string.dialog_cancel, R.string.dialog_confirm, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceSettingFragment.10
                @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                public void onClick() {
                    DeviceSettingFragment.this.dismissNotifyDialog();
                }
            }, new AnonymousClass11());
            this.notifyDialog = newInstance;
            newInstance.setCancelable(false);
        }
        if (this.notifyDialog.isShowing() || isDetached()) {
            return;
        }
        this.notifyDialog.show(getFragmentManager(), "notify_dialog");
    }

    /* renamed from: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceSettingFragment$11, reason: invalid class name */
    class AnonymousClass11 implements NotifyDialog.OnPositiveClickListener {
        AnonymousClass11() {
        }

        @Override // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
        public void onClick() {
            SharedPreferences sharedPreferences = PreferencesHelper.getSharedPreferences(DeviceSettingFragment.this.mApplication);
            String uuid = DeviceSettingFragment.this.mApplication.getUUID();
            if (!TextUtils.isEmpty(uuid)) {
                String string = sharedPreferences.getString(uuid, "");
                ClientManager.getClient().tryToSetApAccount(string, sharedPreferences.getString(string, ""), true, new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceSettingFragment.11.1
                    @Override // com.jieli.lib.dv.control.connect.response.Response
                    public void onResponse(Integer num) {
                        if (num.intValue() == 1) {
                            new Handler().postDelayed(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.settings.DeviceSettingFragment.11.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    DeviceSettingFragment.this.searchApMode();
                                }
                            }, 500L);
                        }
                    }
                });
            }
            DeviceSettingFragment.this.dismissNotifyDialog();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissNotifyDialog() {
        NotifyDialog notifyDialog = this.notifyDialog;
        if (notifyDialog != null) {
            if (notifyDialog.isShowing() && !isDetached()) {
                this.notifyDialog.dismiss();
            }
            this.notifyDialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void searchApMode() {
        dismissNotifyDialog();
        this.mApplication.setSearchMode(0);
        ClientManager.getClient().disconnect();
        DeviceSettingActivity deviceSettingActivity = (DeviceSettingActivity) getActivity();
        if (deviceSettingActivity != null) {
            deviceSettingActivity.onBackPressed();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showWaitingDialog() {
        if (this.mWaitingDialog == null) {
            WaitingDialog waitingDialog = new WaitingDialog();
            this.mWaitingDialog = waitingDialog;
            waitingDialog.setNotifyContent(getString(R.string.dialod_wait));
        }
        if (this.mWaitingDialog.isShowing()) {
            return;
        }
        this.mWaitingDialog.show(getFragmentManager(), "wait_dialog");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissWaitingDialog() {
        WaitingDialog waitingDialog = this.mWaitingDialog;
        if (waitingDialog != null) {
            if (waitingDialog.isShowing()) {
                this.mWaitingDialog.dismiss();
            }
            this.mWaitingDialog = null;
        }
    }
}
