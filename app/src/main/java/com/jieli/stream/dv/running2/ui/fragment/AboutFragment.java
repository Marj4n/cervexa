package com.jieli.stream.dv.running2.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.utils.Code;
import com.jieli.lib.dv.control.utils.Topic;
import com.jieli.lib.dv.control.utils.VersionHelper;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.SettingItem;
import com.jieli.stream.dv.running2.interfaces.OnSelectedListener;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.ui.activity.GenericActivity;
import com.jieli.stream.dv.running2.ui.adapter.SettingAdapter;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.ui.dialog.BrowseFirmwareDialog;
import com.jieli.stream.dv.running2.ui.dialog.NotifyDialog;
import com.jieli.stream.dv.running2.ui.dialog.WaitingDialog;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.FTPClientUtil;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import com.jieli.stream.dv.running2.util.ToastUtil;
import com.serenegiant.net.SocketChannelDataLink;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class AboutFragment extends BaseFragment {
    private static final int LIMIT_TIME = 60000;
    private static final int MSG_UPGRADE_FILE = 4660;
    private static final int MSG_UPLOAD_FAILED = 257;
    private static final int MSG_UPLOAD_FINISH = 256;
    private static final int TIME_INTERVAL = 2000;
    private SettingAdapter mAdapter;
    private long mBackPressedTimes;
    private CheckAppUpgrade mCheckAppUpgrade;
    private ListView mListView;
    private NotifyDialog mUpgradeCompleteDialog;
    private NotifyDialog mUploadDialog;
    private WaitingDialog mWaitingDialog;
    private TextView tvAppVersionName;
    private NotifyDialog upgradeNotifyDialog;
    private String tag = getClass().getSimpleName();
    private int pressCount = 0;
    private Handler mHandler = new Handler(new Handler.Callback() { // from class: com.jieli.stream.dv.running2.ui.fragment.AboutFragment.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            ArrayList<String> stringArrayList;
            if (message == null || AboutFragment.this.getActivity() == null || AboutFragment.this.getActivity().isDestroyed()) {
                return false;
            }
            int i = message.what;
            if (i == 256) {
                if (AboutFragment.this.mUploadDialog == null || !AboutFragment.this.mUploadDialog.isShowing()) {
                    return false;
                }
                AboutFragment.this.mUploadDialog.dismiss();
                return false;
            }
            if (i == 257) {
                if (AboutFragment.this.mUploadDialog != null && AboutFragment.this.mUploadDialog.isShowing()) {
                    AboutFragment.this.mUploadDialog.dismiss();
                }
                ToastUtil.showToastLong(AboutFragment.this.getString(R.string.upload_failed));
                return false;
            }
            if (i != AboutFragment.MSG_UPGRADE_FILE) {
                if (i != 21863) {
                    return false;
                }
                int i2 = message.arg1;
                if (AboutFragment.this.mUploadDialog == null) {
                    return false;
                }
                AboutFragment.this.mUploadDialog.setProgress(i2);
                return false;
            }
            Bundle data = message.getData();
            if (data == null || (stringArrayList = data.getStringArrayList(IConstant.UPDATE_PATH)) == null || stringArrayList.size() <= 0) {
                return false;
            }
            String str = null;
            if (data.getInt(IConstant.UPDATE_TYPE) == 1) {
                if (stringArrayList.size() > 1) {
                    str = AppUtils.readTxtFile(stringArrayList.get(1));
                }
            } else {
                str = AboutFragment.this.getString(R.string.firmware_upgrade_tip);
            }
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            AboutFragment.this.showNotifyDialog(str, data);
            return false;
        }
    });
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.AboutFragment.3
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            SettingItem settingItem;
            if (AboutFragment.this.mAdapter == null || (settingItem = (SettingItem) AboutFragment.this.mAdapter.getItem(i)) == null) {
                return;
            }
            String name = settingItem.getName();
            if (AboutFragment.this.getString(R.string.check_app_upgrade).equals(name)) {
                if (AboutFragment.this.mCheckAppUpgrade == null) {
                    AboutFragment.this.mCheckAppUpgrade = new CheckAppUpgrade();
                    AboutFragment.this.mCheckAppUpgrade.execute(new Void[0]);
                    return;
                }
                return;
            }
            if (AboutFragment.this.getString(R.string.upload_firmware).equals(name)) {
                if (ClientManager.getClient().isConnected()) {
                    BrowseFirmwareDialog browseFirmwareDialog = new BrowseFirmwareDialog();
                    browseFirmwareDialog.setOnSelectedListener(AboutFragment.this.onSelectedFileListener);
                    browseFirmwareDialog.show(AboutFragment.this.getFragmentManager(), "browse_firmware_file_dialog");
                    return;
                }
                ToastUtil.showToastShort(AboutFragment.this.getString(R.string.please_connect_device_to_use));
            }
        }
    };
    private OnSelectedListener<String> onSelectedFileListener = new AnonymousClass4();
    private final OnNotifyListener onNotifyListener = new OnNotifyListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.AboutFragment.5
        @Override // com.jieli.lib.dv.control.receiver.listener.NotifyResponse
        public void onNotify(NotifyInfo notifyInfo) {
            if (notifyInfo.getErrorType() != 0) {
                Dbug.e(AboutFragment.this.tag, Code.getCodeDescription(notifyInfo.getErrorType()));
                return;
            }
            String topic = notifyInfo.getTopic();
            char c = 65535;
            if (topic.hashCode() == 1950931127 && topic.equals(Topic.DEVICE_UPGRADE_SUCCESS)) {
                c = 0;
            }
            if (c != 0) {
                return;
            }
            AboutFragment.this.showUpgradeCompleteDialog();
        }
    };
    private View.OnClickListener mOnClickListener = new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.AboutFragment.10
        @SuppressLint("StringFormatInvalid")
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view == AboutFragment.this.tvAppVersionName) {
                if (AboutFragment.this.mBackPressedTimes + 2000 <= System.currentTimeMillis()) {
                    if (AboutFragment.this.pressCount != 0) {
                        AboutFragment.this.pressCount = 0;
                    }
                    AboutFragment.access$1508(AboutFragment.this);
                    ToastUtil.showToastShort(String.format(Locale.getDefault(), AboutFragment.this.getString(R.string.open_debug_tip), Integer.valueOf(3 - AboutFragment.this.pressCount)));
                } else {
                    AboutFragment.access$1508(AboutFragment.this);
                    if (AboutFragment.this.pressCount == 3) {
                        AboutFragment.this.pressCount = 0;
                        PreferencesHelper.putBooleanValue(AboutFragment.this.mApplication, IConstant.DEBUG_SETTINGS, true ^ PreferencesHelper.getSharedPreferences(AboutFragment.this.mApplication).getBoolean(IConstant.DEBUG_SETTINGS, false));
                        ToastUtil.showToastShort(AboutFragment.this.getString(R.string.open_debug_ok));
                    } else {
                        ToastUtil.showToastShort(String.format(Locale.getDefault(), AboutFragment.this.getString(R.string.open_debug_tip), Integer.valueOf(3 - AboutFragment.this.pressCount)));
                    }
                }
                AboutFragment.this.mBackPressedTimes = System.currentTimeMillis();
            }
        }
    };

    static /* synthetic */ int access$1508(AboutFragment aboutFragment) {
        int i = aboutFragment.pressCount;
        aboutFragment.pressCount = i + 1;
        return i;
    }

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_about, viewGroup, false);
        this.tvAppVersionName = (TextView) inflate.findViewById(R.id.about_app_version);
        this.mListView = (ListView) inflate.findViewById(R.id.about_list_view);
        this.tvAppVersionName.setText(getResources().getString(R.string.app_version) + " " + MainApplication.getApplication().getAppVersionName());
        String sdkVersionName = VersionHelper.getSdkVersionName(MainApplication.getApplication());
        if (!TextUtils.isEmpty(sdkVersionName)) {
            ((TextView) inflate.findViewById(R.id.about_sdk_version)).setText(getResources().getString(R.string.sdk_version) + " " + sdkVersionName);
        }
        String firmware_version = this.mApplication.getDeviceDesc() != null ? this.mApplication.getDeviceDesc().getFirmware_version() : null;
        if (!TextUtils.isEmpty(firmware_version)) {
            ((TextView) inflate.findViewById(R.id.about_fw_version)).setText(getResources().getString(R.string.firmware_version) + " " + firmware_version);
        }
        ((ImageView) inflate.findViewById(R.id.about_return)).setOnClickListener(new View.OnClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.AboutFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (AboutFragment.this.getActivity() != null) {
                    AboutFragment.this.getActivity().onBackPressed();
                }
            }
        });
        this.mListView.setOnItemClickListener(this.mOnItemClickListener);
        this.tvAppVersionName.setOnClickListener(this.mOnClickListener);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        initVersion();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        dismissWaitingDialog();
        dismissNotifyDialog();
        CheckAppUpgrade checkAppUpgrade = this.mCheckAppUpgrade;
        if (checkAppUpgrade != null) {
            checkAppUpgrade.cancel(true);
            this.mCheckAppUpgrade = null;
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void initVersion() {
        String[] stringArray = getResources().getStringArray(R.array.about_list);
        ArrayList arrayList = new ArrayList();
        for (String str : stringArray) {
            if (!TextUtils.isEmpty(str)) {
                SettingItem settingItem = new SettingItem();
                settingItem.setType(0);
                settingItem.setName(str);
                arrayList.add(settingItem);
            }
        }
        SettingAdapter settingAdapter = new SettingAdapter(getContext(), arrayList);
        this.mAdapter = settingAdapter;
        this.mListView.setAdapter((ListAdapter) settingAdapter);
        this.mAdapter.notifyDataSetChanged();
    }

    /* renamed from: com.jieli.stream.dv.running2.ui.fragment.AboutFragment$4, reason: invalid class name */
    class AnonymousClass4 implements OnSelectedListener<String> {
        AnonymousClass4() {
        }

        @Override // com.jieli.stream.dv.running2.interfaces.OnSelectedListener
        public void onSelected(final String str) {
            Dbug.i(AboutFragment.this.tag, "path=" + str);
            if (TextUtils.isEmpty(str)) {
                return;
            }
            if (AboutFragment.this.mUploadDialog == null) {
                AboutFragment.this.mUploadDialog = NotifyDialog.newInstance(R.string.dialog_tips, 2, R.string.uploading);
            }
            if (AboutFragment.this.mUploadDialog != null && !AboutFragment.this.mUploadDialog.isShowing()) {
                AboutFragment.this.mUploadDialog.show(AboutFragment.this.getFragmentManager(), "Upload_Firmware");
            }
            new Thread(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.fragment.AboutFragment.4.1
                @Override // java.lang.Runnable
                public void run() {
                    File file = new File(str);
                    if (!file.exists()) {
                        AboutFragment.this.mHandler.sendEmptyMessage(257);
                    } else if (FTPClientUtil.getInstance().uploadFile(file.getName(), str, AboutFragment.this.mHandler)) {
                        AboutFragment.this.mHandler.sendEmptyMessage(256);
                        ClientManager.getClient().tryToResetDev(new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.AboutFragment.4.1.1
                            @Override // com.jieli.lib.dv.control.connect.response.Response
                            public void onResponse(Integer num) {
                                if (num.intValue() == 1) {
                                    AboutFragment.this.mHandler.sendEmptyMessage(256);
                                } else {
                                    Dbug.e(AboutFragment.this.TAG, "send reset cmd failed!");
                                }
                            }
                        });
                    } else {
                        AboutFragment.this.mHandler.sendEmptyMessage(257);
                    }
                }
            }).start();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        NotifyDialog notifyDialog = this.mUploadDialog;
        if (notifyDialog != null && notifyDialog.isShowing()) {
            this.mUploadDialog.dismiss();
            this.mUploadDialog = null;
        }
        if (MainApplication.isFactoryMode) {
            ClientManager.getClient().unregisterNotifyListener(this.onNotifyListener);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (MainApplication.isFactoryMode) {
            ClientManager.getClient().registerNotifyListener(this.onNotifyListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showUpgradeCompleteDialog() {
        if (this.mUpgradeCompleteDialog == null) {
            this.mUpgradeCompleteDialog = NotifyDialog.newInstance(R.string.dialog_tips, R.string.upgrade_step_6, R.string.comfirm, new NotifyDialog.OnConfirmClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.AboutFragment.6
                @Override
                // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnConfirmClickListener
                public void onClick() {
                    AboutFragment.this.mUpgradeCompleteDialog.dismiss();
                }
            });
        }
        if (this.mUpgradeCompleteDialog.isShowing()) {
            return;
        }
        this.mUpgradeCompleteDialog.show(getActivity().getSupportFragmentManager(), "mUpgradeCompleteDialog");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showNotifyDialog(String str, Bundle bundle) {
        if (this.upgradeNotifyDialog == null) {
            NotifyDialog newInstance = NotifyDialog.newInstance(getString(R.string.upgrade_desc), str, R.string.dialog_cancel, R.string.dialog_confirm, new NotifyDialog.OnNegativeClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.AboutFragment.7
                @Override
                // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnNegativeClickListener
                public void onClick() {
                    AboutFragment.this.dismissNotifyDialog();
                }
            }, new NotifyDialog.OnPositiveClickListener() { // from class: com.jieli.stream.dv.running2.ui.fragment.AboutFragment.8
                @Override
                // com.jieli.stream.dv.running2.ui.dialog.NotifyDialog.OnPositiveClickListener
                public void onClick() {
                    Bundle bundle2 = AboutFragment.this.upgradeNotifyDialog.getBundle();
                    if (bundle2 != null && AboutFragment.this.getActivity() != null) {
                        Intent intent = new Intent(AboutFragment.this.getActivity(), (Class<?>) GenericActivity.class);
                        intent.putExtra(IConstant.KEY_FRAGMENT_TAG, 5);
                        intent.putExtra(IConstant.KEY_DATA, bundle2);
                        AboutFragment.this.startActivity(intent);
                    }
                    AboutFragment.this.dismissNotifyDialog();
                }
            });
            this.upgradeNotifyDialog = newInstance;
            newInstance.setContentTextLeft(true);
        }
        if (!TextUtils.isEmpty(str)) {
            this.upgradeNotifyDialog.setContent(str);
        }
        if (bundle != null) {
            this.upgradeNotifyDialog.setBundle(bundle);
        }
        if (this.upgradeNotifyDialog.isShowing()) {
            return;
        }
        this.upgradeNotifyDialog.show(getFragmentManager(), "notify_dialog");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissNotifyDialog() {
        NotifyDialog notifyDialog = this.upgradeNotifyDialog;
        if (notifyDialog != null) {
            if (notifyDialog.isShowing() && !isDetached()) {
                this.upgradeNotifyDialog.dismiss();
            }
            this.upgradeNotifyDialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showWaitingDialog() {
        if (this.mWaitingDialog == null) {
            WaitingDialog waitingDialog = new WaitingDialog();
            this.mWaitingDialog = waitingDialog;
            waitingDialog.setCancelable(false);
            this.mWaitingDialog.setNotifyContent(getString(R.string.check_app_upgrade));
            this.mWaitingDialog.setOnWaitingDialog(new WaitingDialog.OnWaitingDialog() { // from class: com.jieli.stream.dv.running2.ui.fragment.AboutFragment.9
                @Override // com.jieli.stream.dv.running2.ui.dialog.WaitingDialog.OnWaitingDialog
                public void onCancelDialog() {
                    AboutFragment.this.dismissWaitingDialog();
                }
            });
        }
        if (this.mWaitingDialog.isShowing()) {
            return;
        }
        this.mWaitingDialog.show(getFragmentManager(), "waiting_dialog");
    }

    public void dismissWaitingDialog() {
        WaitingDialog waitingDialog = this.mWaitingDialog;
        if (waitingDialog != null) {
            if (waitingDialog.isShowing()) {
                this.mWaitingDialog.dismiss();
            }
            this.mWaitingDialog = null;
        }
    }

    private class CheckAppUpgrade extends AsyncTask<Void, Void, ArrayList<String>> {
        private CheckAppUpgrade() {
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
            AboutFragment.this.showWaitingDialog();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(ArrayList<String> arrayList) {
            super.onPostExecute(arrayList);
            AboutFragment.this.dismissWaitingDialog();
            AboutFragment.this.mCheckAppUpgrade = null;
            if (arrayList != null && arrayList.size() > 0) {
                if (arrayList.size() > 1) {
                    if (AboutFragment.this.mHandler != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(IConstant.UPDATE_TYPE, 1);
                        bundle.putStringArrayList(IConstant.UPDATE_PATH, arrayList);
                        Message obtainMessage = AboutFragment.this.mHandler.obtainMessage(AboutFragment.MSG_UPGRADE_FILE);
                        obtainMessage.setData(bundle);
                        AboutFragment.this.mHandler.sendMessage(obtainMessage);
                        return;
                    }
                    return;
                }
                ToastUtil.showToastShort(arrayList.get(0));
                return;
            }
            ToastUtil.showToastShort(AboutFragment.this.getString(R.string.upgrade_failed_tip));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public ArrayList<String> doInBackground(Void... voidArr) {
            if (AboutFragment.this.getActivity() == null) {
                return null;
            }
            int i = 0;
            while (!AppUtils.checkNetworkIsAvailable()) {
                AboutFragment.this.mApplication.switchWifi();
                SystemClock.sleep(6000L);
                i += SocketChannelDataLink.DEFAULT_SERVER_PORT;
                if (i > 60000) {
                    break;
                }
            }
            if (!AppUtils.checkNetworkIsAvailable()) {
                Dbug.w(AboutFragment.this.tag, "Network is unavailable");
                return null;
            }
            String checkUpdateFilePath = AppUtils.checkUpdateFilePath(AboutFragment.this.mApplication, 1);
            if (TextUtils.isEmpty(checkUpdateFilePath)) {
                Dbug.w(AboutFragment.this.tag, "upgradePath=" + checkUpdateFilePath);
                return null;
            }
            ArrayList<String> arrayList = new ArrayList<>();
            if (checkUpdateFilePath.equals(AboutFragment.this.getString(R.string.latest_version))) {
                arrayList.add(checkUpdateFilePath);
            } else {
                Dbug.w(AboutFragment.this.TAG, "有APK更新,更新路径：" + checkUpdateFilePath);
                arrayList.add(checkUpdateFilePath);
                List<String> downLoadUpdateFile = FTPClientUtil.getInstance().downLoadUpdateFile(checkUpdateFilePath, 1, 1, null);
                if (downLoadUpdateFile != null && downLoadUpdateFile.size() > 1) {
                    arrayList.add(downLoadUpdateFile.get(1));
                }
            }
            return arrayList;
        }
    }
}
