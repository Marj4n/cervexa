package com.jieli.stream.dv.running2.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.baidu.mapapi.UIMsg;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.bean.UpgradeStep;
import com.jieli.stream.dv.running2.interfaces.OnWifiCallBack;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.ui.service.CommunicationService;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.FTPClientUtil;
import com.jieli.stream.dv.running2.util.IActions;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import com.jieli.stream.dv.running2.util.ToastUtil;
import com.jieli.stream.dv.running2.util.WifiHelper;
import com.serenegiant.net.SocketChannelDataLink;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
public class UpgradeFragment extends BaseFragment {
    private static final int LIMIT_TIME = 60000;
    private static final int MSG_UPDATE_STEP_UI = 4097;
    private static final int MSG_UPGRADE_APK = 4098;
    private static final int MSG_UPGRADE_RESULT = 4100;
    private static final int MSG_UPGRADE_SDK = 4099;
    private List<UpgradeStep> dataList;
    private boolean isConnectDev;
    private StateAdapter mAdapter;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private ProgressBar mUploadPb;
    private ExecutorService service;
    private SharedPreferences sharedPreferences;
    private TextView tvTip;
    private String upgradePath;
    private List<String> upgradePathList;
    private UploadFileThread uploadFileThread;
    private int upgradeType = 1;
    private Handler mHandler = new Handler(new Handler.Callback() { // from class: com.jieli.stream.dv.running2.ui.fragment.UpgradeFragment.1
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (UpgradeFragment.this.getActivity() != null && message != null) {
                int i = message.what;
                if (i == 21862) {
                    int i2 = message.arg1;
                    if (UpgradeFragment.this.mProgressBar != null) {
                        UpgradeFragment.this.mProgressBar.setProgress(i2);
                    }
                } else if (i != 21863) {
                    switch (i) {
                        case 4097:
                            int i3 = message.arg1;
                            int i4 = message.arg2;
                            if (i3 < UpgradeFragment.this.dataList.size()) {
                                ((UpgradeStep) UpgradeFragment.this.dataList.get(i3)).setState(i4);
                                UpgradeFragment.this.tvTip.setVisibility(0);
                                UpgradeFragment.this.tvTip.setText(UpgradeFragment.this.getString(R.string.executing_step, ((UpgradeStep) UpgradeFragment.this.dataList.get(i3)).getDescription()));
                                if (UpgradeFragment.this.mAdapter != null) {
                                    UpgradeFragment.this.mAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                            break;
                        case 4098:
                            List list = (List) message.obj;
                            if (list != null && list.size() > 1) {
                                UpgradeFragment.this.upgradePathList = list;
                                UpgradeFragment upgradeFragment = UpgradeFragment.this;
                                upgradeFragment.sendHandlerMsg(upgradeFragment.mHandler, 2, 2, null, 0L);
                                Intent intent = new Intent("android.intent.action.VIEW");
                                intent.setDataAndType(Uri.parse("file://" + ((String) UpgradeFragment.this.upgradePathList.get(1))), "application/vnd.android.package-archive");
                                UpgradeFragment.this.startActivityForResult(intent, 4104);
                                UpgradeFragment.this.getActivity().finish();
                                break;
                            }
                            break;
                        case 4099:
                            List list2 = (List) message.obj;
                            if (list2 != null && list2.size() > 1) {
                                UpgradeFragment.this.upgradePathList = list2;
                                String string = UpgradeFragment.this.sharedPreferences.getString(IConstant.CURRENT_WIFI_SSID, null);
                                if (!TextUtils.isEmpty(string)) {
                                    UpgradeFragment.this.mWifiHelper.connectWifi(UpgradeFragment.this.mApplication, string, UpgradeFragment.this.sharedPreferences.getString(string, null));
                                    UpgradeFragment.this.isConnectDev = true;
                                    break;
                                }
                            }
                            break;
                        case 4100:
                            if (message.arg1 == 1) {
                                if (UpgradeFragment.this.dataList != null && UpgradeFragment.this.dataList.size() > 5) {
                                    UpgradeFragment upgradeFragment2 = UpgradeFragment.this;
                                    upgradeFragment2.sendHandlerMsg(upgradeFragment2.mHandler, 4, 2, null, 0L);
                                    UpgradeFragment upgradeFragment3 = UpgradeFragment.this;
                                    upgradeFragment3.sendHandlerMsg(upgradeFragment3.mHandler, 5, 2, null, 100L);
                                }
                                UpgradeFragment.this.getActivity().sendBroadcast(new Intent(IActions.ACTION_SDK_UPGRADE_SUCCESS));
                            } else {
                                ToastUtil.showToastLong(UpgradeFragment.this.getString(R.string.upgrade_failed_tip));
                            }
                            UpgradeFragment.this.getActivity().finish();
                            break;
                    }
                } else {
                    int i5 = message.arg1;
                    if (UpgradeFragment.this.mUploadPb != null) {
                        UpgradeFragment.this.mUploadPb.setProgress(i5);
                    }
                }
            }
            return false;
        }
    });
    private OnWifiCallBack wifiCallBack = new OnWifiCallBack() { // from class: com.jieli.stream.dv.running2.ui.fragment.UpgradeFragment.2
        @Override // com.jieli.stream.dv.running2.interfaces.OnWifiCallBack
        public void onError(int i) {
        }

        @Override // com.jieli.stream.dv.running2.interfaces.OnWifiCallBack
        public void onConnected(WifiInfo wifiInfo) {
            String string = UpgradeFragment.this.sharedPreferences.getString(IConstant.CURRENT_WIFI_SSID, null);
            String formatSSID = WifiHelper.formatSSID(wifiInfo.getSSID());
            if (TextUtils.isEmpty(string) || !string.equals(formatSSID)) {
                if (!UpgradeFragment.this.isConnectDev || TextUtils.isEmpty(string)) {
                    return;
                }
                UpgradeFragment.this.mWifiHelper.connectWifi(UpgradeFragment.this.mApplication, string, UpgradeFragment.this.sharedPreferences.getString(string, null));
                return;
            }
            if (((UpgradeStep) UpgradeFragment.this.dataList.get(2)).getState() == 1) {
                UpgradeFragment upgradeFragment = UpgradeFragment.this;
                upgradeFragment.sendHandlerMsg(upgradeFragment.mHandler, 2, 2, null, 0L);
                UpgradeFragment upgradeFragment2 = UpgradeFragment.this;
                upgradeFragment2.sendHandlerMsg(upgradeFragment2.mHandler, 3, 1, null, 100L);
                UpgradeFragment.this.isConnectDev = false;
                UpgradeFragment.this.sendCommandToService();
                if (UpgradeFragment.this.upgradePathList == null || UpgradeFragment.this.upgradePathList.size() <= 1 || UpgradeFragment.this.service == null || UpgradeFragment.this.uploadFileThread != null) {
                    return;
                }
                UpgradeFragment upgradeFragment3 = UpgradeFragment.this;
                UpgradeFragment upgradeFragment4 = UpgradeFragment.this;
                upgradeFragment3.uploadFileThread = upgradeFragment4.new UploadFileThread("JL_AC54.bfu", (String) upgradeFragment4.upgradePathList.get(1), UpgradeFragment.this.mHandler);
                UpgradeFragment.this.service.submit(UpgradeFragment.this.uploadFileThread);
            }
        }
    };

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_upgrade, viewGroup, false);
        TextView textView = (TextView) inflate.findViewById(R.id.upgrade_tip);
        this.tvTip = textView;
        textView.setVisibility(8);
        ListView listView = (ListView) inflate.findViewById(R.id.upgrade_list_view);
        this.mListView = listView;
        listView.setEnabled(false);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() != null) {
            this.service = Executors.newFixedThreadPool(2);
            this.sharedPreferences = PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext());
            this.mApplication.setUpgrading(true);
            Bundle bundle2 = getBundle();
            if (bundle2 != null) {
                this.upgradeType = bundle2.getInt(IConstant.UPDATE_TYPE);
                ArrayList<String> stringArrayList = bundle2.getStringArrayList(IConstant.UPDATE_PATH);
                if (stringArrayList != null && stringArrayList.size() > 0) {
                    this.upgradePath = stringArrayList.get(0);
                }
            }
            WifiHelper.getInstance(getActivity().getApplicationContext()).registerOnWifiCallback(this.wifiCallBack);
            initListView();
            if (this.upgradeType == 1) {
                this.service.submit(new UpgradeAPK(this.mHandler));
            } else if (this.mApplication.isSdcardExist()) {
                this.service.submit(new UpgradeSDK(this.mHandler));
            } else {
                ToastUtil.showToastShort(getString(R.string.sdcard_online));
                getActivity().finish();
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        ExecutorService executorService = this.service;
        if (executorService != null) {
            executorService.shutdownNow();
        }
        WifiHelper.getInstance(getActivity().getApplicationContext()).unregisterOnWifiCallback(this.wifiCallBack);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mApplication.setUpgrading(false);
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        UploadFileThread uploadFileThread = this.uploadFileThread;
        if (uploadFileThread != null) {
            uploadFileThread.interrupt();
            this.uploadFileThread = null;
        }
        ExecutorService executorService = this.service;
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 4104) {
            Dbug.w(this.TAG, "-resultCode- " + i2);
        }
    }

    private void initListView() {
        String[] stringArray;
        this.dataList = new ArrayList();
        if (this.upgradeType == 1) {
            stringArray = getResources().getStringArray(R.array.upgrade_apk_steps);
        } else {
            stringArray = getResources().getStringArray(R.array.upgrade_sdk_steps);
        }
        for (int i = 0; i < stringArray.length; i++) {
            String str = stringArray[i];
            UpgradeStep upgradeStep = new UpgradeStep();
            upgradeStep.setNum(i);
            upgradeStep.setDescription(str);
            if (i == 1) {
                upgradeStep.setNeedPb(true);
            } else if (this.upgradeType == 2 && i == 3) {
                upgradeStep.setNeedPb(true);
            }
            this.dataList.add(upgradeStep);
        }
        StateAdapter stateAdapter = new StateAdapter(getActivity().getApplicationContext());
        this.mAdapter = stateAdapter;
        this.mListView.setAdapter((ListAdapter) stateAdapter);
        this.mAdapter.setDataList(this.dataList);
        this.mAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendCommandToService() {
        Intent intent = new Intent(this.mApplication, (Class<?>) CommunicationService.class);
        intent.putExtra(IConstant.SERVICE_CMD, 1);
        this.mApplication.startService(intent);
    }

    private class StateAdapter extends BaseAdapter {
        private List<UpgradeStep> dataList;
        private Context mContext;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        StateAdapter(Context context) {
            this.mContext = context;
        }

        void setDataList(List<UpgradeStep> list) {
            this.dataList = list;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            List<UpgradeStep> list = this.dataList;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            List<UpgradeStep> list = this.dataList;
            if (list == null || i >= list.size()) {
                return null;
            }
            return this.dataList.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(this.mContext).inflate(R.layout.item_upgrade, viewGroup, false);
                viewHolder = new ViewHolder(view);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            UpgradeStep upgradeStep = (UpgradeStep) getItem(i);
            if (upgradeStep != null) {
                viewHolder.tvStep.setText(upgradeStep.getDescription());
                int state = upgradeStep.getState();
                if (state == 0) {
                    viewHolder.ivState.setImageResource(R.mipmap.ic_gary_dot);
                    viewHolder.tvStep.setTextColor(UpgradeFragment.this.getResources().getColor(R.color.text_gray));
                } else if (state == 1) {
                    viewHolder.ivState.setImageResource(R.mipmap.ic_refresh_green);
                    viewHolder.tvStep.setTextColor(UpgradeFragment.this.getResources().getColor(R.color.text_black));
                    Animation loadAnimation = AnimationUtils.loadAnimation(this.mContext, R.anim.rotate_forevery);
                    loadAnimation.setInterpolator(new LinearInterpolator());
                    viewHolder.ivState.startAnimation(loadAnimation);
                } else if (state == 2) {
                    viewHolder.tvStep.setTextColor(UpgradeFragment.this.getResources().getColor(R.color.text_black));
                    viewHolder.ivState.clearAnimation();
                    viewHolder.ivState.setImageResource(R.mipmap.ic_complete);
                }
                if (upgradeStep.isNeedPb()) {
                    viewHolder.progressBar.setVisibility(0);
                    if (i == 1) {
                        UpgradeFragment.this.mProgressBar = viewHolder.progressBar;
                    } else {
                        UpgradeFragment.this.mUploadPb = viewHolder.progressBar;
                    }
                } else {
                    viewHolder.progressBar.setVisibility(8);
                }
            }
            return view;
        }

        private class ViewHolder {
            private ImageView ivState;
            private ProgressBar progressBar;
            private TextView tvStep;

            ViewHolder(View view) {
                this.ivState = (ImageView) view.findViewById(R.id.item_upgrade_state);
                this.tvStep = (TextView) view.findViewById(R.id.item_upgrade_step);
                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.item_upgrade_pb);
                this.progressBar = progressBar;
                progressBar.setMax(100);
                view.setTag(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendHandlerMsg(Handler handler, int i, int i2, Object obj, long j) {
        if (handler != null) {
            if (j > 0) {
                handler.sendMessageDelayed(handler.obtainMessage(4097, i, i2, obj), j);
            } else {
                handler.sendMessage(handler.obtainMessage(4097, i, i2, obj));
            }
        }
    }

    private class UpgradeSDK extends Thread {
        private SoftReference<Handler> softReference;

        UpgradeSDK(Handler handler) {
            this.softReference = new SoftReference<>(handler);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            List<String> downLoadUpdateFile;
            super.run();
            Handler handler = this.softReference.get();
            ((UpgradeStep) UpgradeFragment.this.dataList.get(0)).setState(1);
            int i = 0;
            while (!AppUtils.checkNetworkIsAvailable()) {
                UpgradeFragment.this.mApplication.switchWifi();
                SystemClock.sleep(3000L);
                i += PathInterpolatorCompat.MAX_NUM_POINTS;
                if (i > 60000) {
                    break;
                }
            }
            if (!AppUtils.checkNetworkIsAvailable()) {
                if (handler != null) {
                    handler.sendMessage(handler.obtainMessage(4100, 0, 0));
                    return;
                }
                return;
            }
            UpgradeFragment.this.sendHandlerMsg(handler, 0, 2, null, 0L);
            UpgradeFragment.this.sendHandlerMsg(handler, 1, 1, null, 100L);
            int i2 = 0;
            do {
                SystemClock.sleep(2000L);
                UpgradeFragment upgradeFragment = UpgradeFragment.this;
                upgradeFragment.upgradePath = AppUtils.checkUpdateFilePath(upgradeFragment.getActivity().getApplicationContext(), UpgradeFragment.this.upgradeType);
                if (TextUtils.isEmpty(UpgradeFragment.this.upgradePath) && (i2 = i2 + UIMsg.m_AppUI.MSG_APP_DATA_OK) > 60000) {
                    break;
                }
            } while (TextUtils.isEmpty(UpgradeFragment.this.upgradePath));
            if (TextUtils.isEmpty(UpgradeFragment.this.upgradePath)) {
                if (handler != null) {
                    handler.sendMessage(handler.obtainMessage(4100, 0, 0));
                    return;
                }
                return;
            }
            int i3 = 0;
            while (true) {
                downLoadUpdateFile = FTPClientUtil.getInstance().downLoadUpdateFile(UpgradeFragment.this.upgradePath, UpgradeFragment.this.upgradeType, 2, handler);
                if (downLoadUpdateFile == null || downLoadUpdateFile.size() < 1) {
                    SystemClock.sleep(2000L);
                    i3 += UIMsg.m_AppUI.MSG_APP_DATA_OK;
                    if (i3 > 60000) {
                        break;
                    }
                }
                if (downLoadUpdateFile != null && downLoadUpdateFile.size() >= 1) {
                    break;
                }
            }
            if (downLoadUpdateFile == null || downLoadUpdateFile.size() <= 1) {
                if (handler != null) {
                    handler.sendMessage(handler.obtainMessage(4100, 0, 0));
                }
            } else {
                UpgradeFragment.this.sendHandlerMsg(handler, 1, 2, null, 0L);
                UpgradeFragment.this.sendHandlerMsg(handler, 2, 1, null, 100L);
                if (handler != null) {
                    handler.sendMessageDelayed(handler.obtainMessage(4099, downLoadUpdateFile), 200L);
                }
            }
        }
    }

    private class UpgradeAPK extends Thread {
        private SoftReference<Handler> softReference;

        UpgradeAPK(Handler handler) {
            this.softReference = new SoftReference<>(handler);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            Handler handler = this.softReference.get();
            UpgradeFragment.this.sendHandlerMsg(handler, 0, 1, null, 0L);
            int i = 0;
            while (!AppUtils.checkNetworkIsAvailable()) {
                UpgradeFragment.this.mApplication.switchWifi();
                SystemClock.sleep(6000L);
                i += SocketChannelDataLink.DEFAULT_SERVER_PORT;
                if (i > 60000) {
                    break;
                }
            }
            if (!AppUtils.checkNetworkIsAvailable()) {
                if (handler != null) {
                    handler.sendMessage(handler.obtainMessage(4100, 0, 0));
                    return;
                }
                return;
            }
            UpgradeFragment.this.sendHandlerMsg(handler, 0, 2, null, 0L);
            UpgradeFragment.this.sendHandlerMsg(handler, 1, 1, null, 100L);
            if (TextUtils.isEmpty(UpgradeFragment.this.upgradePath)) {
                UpgradeFragment upgradeFragment = UpgradeFragment.this;
                upgradeFragment.upgradePath = AppUtils.checkUpdateFilePath(upgradeFragment.getActivity().getApplicationContext(), UpgradeFragment.this.upgradeType);
            }
            List<String> downLoadUpdateFile = FTPClientUtil.getInstance().downLoadUpdateFile(UpgradeFragment.this.upgradePath, UpgradeFragment.this.upgradeType, 2, handler);
            if (downLoadUpdateFile == null || downLoadUpdateFile.size() <= 1) {
                if (handler != null) {
                    handler.sendMessage(handler.obtainMessage(4100, 0, 0));
                }
            } else {
                UpgradeFragment.this.sendHandlerMsg(handler, 1, 2, null, 0L);
                UpgradeFragment.this.sendHandlerMsg(handler, 2, 1, null, 100L);
                if (handler != null) {
                    handler.sendMessageDelayed(handler.obtainMessage(4098, downLoadUpdateFile), 200L);
                }
            }
        }
    }

    private class UploadFileThread extends Thread {
        private String localFilePath;
        private String remoteFile;
        private SoftReference<Handler> softReference;

        UploadFileThread(String str, String str2, Handler handler) {
            this.remoteFile = str;
            this.localFilePath = str2;
            this.softReference = new SoftReference<>(handler);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            Handler handler = this.softReference.get();
            SystemClock.sleep(3000L);
            boolean uploadFile = FTPClientUtil.getInstance().uploadFile(this.remoteFile, this.localFilePath, handler);
            Dbug.e(UpgradeFragment.this.TAG, "-UploadFileThread- uploadFile ret = " + uploadFile);
            if (!uploadFile) {
                SystemClock.sleep(3000L);
                uploadFile = FTPClientUtil.getInstance().uploadFile(this.remoteFile, this.localFilePath, handler);
            }
            if (uploadFile) {
                UpgradeFragment upgradeFragment = UpgradeFragment.this;
                upgradeFragment.sendHandlerMsg(upgradeFragment.mHandler, 3, 2, null, 0L);
                UpgradeFragment upgradeFragment2 = UpgradeFragment.this;
                upgradeFragment2.sendHandlerMsg(upgradeFragment2.mHandler, 4, 1, null, 100L);
                ClientManager.getClient().tryToResetDev(new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.fragment.UpgradeFragment.UploadFileThread.1
                    @Override // com.jieli.lib.dv.control.connect.response.Response
                    public void onResponse(Integer num) {
                        if (num.intValue() != 1) {
                            Dbug.e(UpgradeFragment.this.TAG, "-UploadFileThread- send reset cmd failed!");
                            return;
                        }
                        UpgradeFragment.this.mApplication.switchWifi();
                        if (UpgradeFragment.this.mHandler != null) {
                            UpgradeFragment.this.mHandler.sendMessageDelayed(UpgradeFragment.this.mHandler.obtainMessage(4100, 1, 0), 10000L);
                        }
                    }
                });
            }
            UpgradeFragment.this.uploadFileThread = null;
        }
    }
}
