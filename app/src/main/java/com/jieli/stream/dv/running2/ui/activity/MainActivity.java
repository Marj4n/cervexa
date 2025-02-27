package com.jieli.stream.dv.running2.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.jieli.lib.dv.control.connect.listener.OnConnectStateListener;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.utils.Constants;
import com.jieli.stream.dv.running2.R;
import com.jieli.stream.dv.running2.interfaces.OnWifiCallBack;
import com.jieli.stream.dv.running2.ui.base.BaseActivity;
import com.jieli.stream.dv.running2.ui.base.BaseFragment;
import com.jieli.stream.dv.running2.ui.fragment.DeviceListFragment;
import com.jieli.stream.dv.running2.ui.fragment.SettingFragment;
import com.jieli.stream.dv.running2.ui.fragment.StaDeviceListFragment;
import com.jieli.stream.dv.running2.ui.fragment.VideoFragment;
import com.jieli.stream.dv.running2.ui.fragment.browse.BrowseFileFragment;
import com.jieli.stream.dv.running2.ui.service.CommunicationService;
import com.jieli.stream.dv.running2.util.ActivityManager;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IActions;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.IWifiDirectListener;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import com.jieli.stream.dv.running2.util.ThumbLoader;
import com.jieli.stream.dv.running2.util.ThumbnailManager;
import com.jieli.stream.dv.running2.util.WifiHelper;
import com.jieli.stream.dv.running2.util.WifiP2pHelper;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class MainActivity extends BaseActivity implements OnWifiCallBack {
    private static final int MSG_CONNECT_CTP = 3;
    private static final int MSG_RECONNECTION_DEVICE = 0;
    private static final int MSG_STOP_RECONNECTION_DEVICE = 1;
    private ActivityManager mActivityManager;
    private WifiP2pHelper mWifiP2pHelper;
    private int reConnectNum;
    String tag = getClass().getSimpleName();
    private boolean isReConnectDev = false;
    private Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.jieli.stream.dv.running2.ui.activity.MainActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i != 0) {
                if (i == 1) {
                    MainActivity.this.mHandler.removeMessages(0);
                    MainActivity.this.reConnectNum = 0;
                    MainActivity.this.isReConnectDev = false;
                    MainActivity.this.removeDeviceWifiMsg();
                    MainActivity.this.mApplication.switchWifi();
                    return;
                }
                if (i != 3) {
                    return;
                }
                String str = (String) message.obj;
                Intent intent = new Intent(MainActivity.this.getApplicationContext(), (Class<?>) CommunicationService.class);
                intent.putExtra(IConstant.SERVICE_CMD, 1);
                if (!TextUtils.isEmpty(str)) {
                    intent.putExtra(IConstant.KEY_CONNECT_IP, str);
                }
                MainActivity.this.getApplicationContext().startService(intent);
                return;
            }
            Dbug.i(MainActivity.this.tag, "reconnecting reConnectNum=" + MainActivity.this.reConnectNum);
            if (MainActivity.this.mWifiHelper.isWifiOpen()) {
                MainActivity.access$108(MainActivity.this);
                if (MainActivity.this.reConnectNum < 3) {
                    SharedPreferences sharedPreferences = PreferencesHelper.getSharedPreferences(MainActivity.this.getApplicationContext());
                    String string = sharedPreferences.getString(IConstant.CURRENT_WIFI_SSID, null);
                    if (!TextUtils.isEmpty(string)) {
                        MainActivity.this.isReConnectDev = true;
                        String string2 = sharedPreferences.getString(string, null);
                        if (sharedPreferences.getInt(IConstant.RECONNECT_TYPE, 0) == 1) {
                            MainActivity.this.mHandler.sendEmptyMessage(1);
                            return;
                        } else {
                            MainActivity.this.mWifiHelper.connectWifi(MainActivity.this.mApplication, string, string2);
                            return;
                        }
                    }
                    MainActivity.this.mHandler.sendEmptyMessage(1);
                    return;
                }
                Dbug.i(MainActivity.this.tag, "stop reconnect ");
                MainActivity.this.mHandler.sendEmptyMessage(1);
            }
        }
    };
    private BroadcastReceiver mainReceiver = new BroadcastReceiver() { // from class: com.jieli.stream.dv.running2.ui.activity.MainActivity.2
        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (context == null || intent == null) {
                return;
            }
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            char c = 65535;
            switch (action.hashCode()) {
                case -1851849866:
                    if (action.equals(IActions.ACTION_DEV_ACCESS)) {
                        c = 0;
                        break;
                    }
                    break;
                case 367148202:
                    if (action.equals(IActions.ACTION_ACCOUT_CHANGE)) {
                        c = 1;
                        break;
                    }
                    break;
                case 644690856:
                    if (action.equals(IActions.ACTION_CONNECTION_TIMEOUT)) {
                        c = 4;
                        break;
                    }
                    break;
                case 1432870583:
                    if (action.equals(IActions.ACTION_TF_STATUS)) {
                        c = 3;
                        break;
                    }
                    break;
                case 1702481103:
                    if (action.equals(IActions.ACTION_LANGUAAGE_CHANGE)) {
                        c = 2;
                        break;
                    }
                    break;
            }
            if (c != 0) {
                if (c != 4) {
                    return;
                }
                ClientManager.getClient().disconnect();
                ActivityManager.getInstance().popActivityOnlyMain();
                return;
            }
            boolean booleanExtra = intent.getBooleanExtra(IActions.KEY_ALLOW_ACCESS, false);
            Dbug.w(MainActivity.this.tag, "isAllow : " + booleanExtra);
            if (booleanExtra) {
                BaseFragment baseFragment = (BaseFragment) MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.container);
                Dbug.w(MainActivity.this.tag, "ACTION_DEV_ACCESS : " + baseFragment);
                if ((baseFragment instanceof DeviceListFragment) || (baseFragment instanceof StaDeviceListFragment)) {
                    if (PreferencesHelper.getSharedPreferences(MainActivity.this.mApplication).getBoolean(AppUtils.getAutoRearCameraKey(MainActivity.this.mApplication.getUUID()), false)) {
                        MainActivity.this.mApplication.getDeviceSettingInfo().setCameraType(2);
                    } else {
                        MainActivity.this.mApplication.getDeviceSettingInfo().setCameraType(1);
                    }
                    new VideoFragment();
                    return;
                }
                Dbug.w(MainActivity.this.tag, "It isn't DeviceListFragment or StaDeviceListFragment");
                return;
            }
            MainActivity.this.mApplication.switchWifi();
        }
    };
    private final OnConnectStateListener deviceConnectStateListener = new OnConnectStateListener() { // from class: com.jieli.stream.dv.running2.ui.activity.MainActivity.3
        @Override // com.jieli.lib.dv.control.connect.listener.ConnectStateListener
        public void onStateChanged(Integer num) {
            Dbug.i(MainActivity.this.tag, "--onStateChanged-- " + Constants.getConnectDescription(num.intValue()));
            int intValue = num.intValue();
            if (intValue != -1) {
                if (intValue == 0) {
                    Dbug.i(MainActivity.this.tag, "Third, connect device success...");
                    MainActivity.this.mHandler.removeMessages(0);
                    MainActivity.this.mHandler.removeMessages(1);
                    MainActivity.this.isReConnectDev = false;
                    ClientManager.getClient().tryToAccessDevice(String.valueOf(MainActivity.this.mApplication.getAppVersion()), new SendResponse() { // from class: com.jieli.stream.dv.running2.ui.activity.MainActivity.3.1
                        @Override // com.jieli.lib.dv.control.connect.response.Response
                        public void onResponse(Integer num2) {
                            if (num2.intValue() != 1) {
                                Dbug.e(MainActivity.this.tag, "Send failed!!!");
                            }
                        }
                    });
                    return;
                }
                if (intValue != 1) {
                    if (intValue == 3 || intValue == 4) {
                        Dbug.w(MainActivity.this.tag, "error disconnected:WifiState=" + MainActivity.this.mWifiHelper.getWifiState());
                        MainActivity.this.mHandler.removeMessages(0);
                        MainActivity.this.mHandler.sendEmptyMessageDelayed(0, 100L);
                        return;
                    }
                    return;
                }
            }
            Dbug.e(MainActivity.this.tag, "Disconnect with device!!! Code=" + num);
            BaseFragment baseFragment = (BaseFragment) MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.container);
            Dbug.e(MainActivity.this.tag, "normal disconnected fragment=" + baseFragment);
            if ((baseFragment instanceof BrowseFileFragment) || (baseFragment instanceof SettingFragment)) {
                Dbug.w(MainActivity.this.tag, "Stay!!");
                return;
            }
            if (((BaseFragment) MainActivity.this.getSupportFragmentManager().findFragmentByTag(DeviceListFragment.class.getSimpleName())) == null) {
                new DeviceListFragment();
            }
            MainActivity.this.finish();
        }
    };
    private IWifiDirectListener mIWifiDirectListener = new IWifiDirectListener() { // from class: com.jieli.stream.dv.running2.ui.activity.MainActivity.4
        @Override // com.jieli.stream.dv.running2.util.IWifiDirectListener
        public void onCallP2pDeviceChanged(WifiP2pDevice wifiP2pDevice) {
        }

        @Override // com.jieli.stream.dv.running2.util.IWifiDirectListener
        public void onCallP2pStateChanged(int i) {
            if (i != 2) {
                MainActivity.this.mHandler.sendEmptyMessage(1);
            }
        }

        @Override // com.jieli.stream.dv.running2.util.IWifiDirectListener
        public void onCallP2pPeersChanged(List<WifiP2pDevice> list) {
            if (list != null && MainActivity.this.mApplication.getSearchMode() == 0 && list.size() > 0 && MainActivity.this.isReConnectDev) {
                WifiP2pDevice wifiP2pDevice = null;
                String string = PreferencesHelper.getSharedPreferences(MainActivity.this.mApplication).getString(IConstant.CURRENT_WIFI_SSID, null);
                if (TextUtils.isEmpty(string)) {
                    MainActivity.this.mHandler.sendEmptyMessage(1);
                    return;
                }
                Iterator<WifiP2pDevice> it = list.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    WifiP2pDevice next = it.next();
                    if (string.equals(next.deviceName)) {
                        wifiP2pDevice = next;
                        break;
                    }
                }
                if (wifiP2pDevice != null) {
                    MainActivity.this.mWifiP2pHelper.connectP2pDevice(wifiP2pDevice, new WifiP2pManager.ActionListener() { // from class: com.jieli.stream.dv.running2.ui.activity.MainActivity.4.1
                        @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                        public void onSuccess() {
                            Dbug.i(MainActivity.this.tag, "-onCallP2pPeersChanged- connectP2pDevice ok.");
                        }

                        @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                        public void onFailure(int i) {
                            MainActivity.this.mHandler.removeMessages(0);
                            MainActivity.this.mHandler.sendEmptyMessageDelayed(0, 100L);
                        }
                    });
                }
            }
            if (MainActivity.this.isReConnectDev) {
                MainActivity.this.mHandler.removeMessages(0);
                MainActivity.this.mHandler.sendEmptyMessageDelayed(0, 100L);
            }
        }

        @Override // com.jieli.stream.dv.running2.util.IWifiDirectListener
        public void onCallP2pConnectionChanged(WifiP2pInfo wifiP2pInfo) {
            InetAddress inetAddress;
            if (wifiP2pInfo != null) {
                Dbug.i(MainActivity.this.tag, "-onCallP2pConnectionChanged- : " + wifiP2pInfo);
                if (MainActivity.this.isReConnectDev && MainActivity.this.mApplication.isWifiDirectGO() && wifiP2pInfo.groupFormed && (inetAddress = wifiP2pInfo.groupOwnerAddress) != null && !ClientManager.getClient().isConnected()) {
                    MainActivity.this.connectDevice(inetAddress.getHostAddress());
                }
            }
        }
    };

    static /* synthetic */ int access$108(MainActivity mainActivity) {
        int i = mainActivity.reConnectNum;
        mainActivity.reConnectNum = i + 1;
        return i;
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, (Class<?>) MainActivity.class));
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, (Class<?>) CommunicationService.class));
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Dbug.i(this.tag, "main activity running....");
        this.mActivityManager = ActivityManager.getInstance();
        setContentView(R.layout.activity_main);
        WifiP2pHelper wifiP2pHelper = WifiP2pHelper.getInstance(this.mApplication.getApplicationContext());
        this.mWifiP2pHelper = wifiP2pHelper;
        if (wifiP2pHelper != null) {
            wifiP2pHelper.registerBroadcastReceiver(this.mIWifiDirectListener);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IActions.ACTION_DEV_ACCESS);
        intentFilter.addAction(IActions.ACTION_CONNECTION_TIMEOUT);
        getApplicationContext().registerReceiver(this.mainReceiver, intentFilter);
        this.mWifiHelper.registerOnWifiCallback(this);
        Locale.getDefault().getLanguage();
        changeFragment(R.id.container, new VideoFragment(), VideoFragment.class.getSimpleName());
    }

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        WifiP2pHelper wifiP2pHelper = this.mWifiP2pHelper;
        if (wifiP2pHelper != null) {
            wifiP2pHelper.release();
            this.mWifiP2pHelper = null;
        }
        stopService(new Intent(this, (Class<?>) CommunicationService.class));
        System.gc();
        removeDeviceWifiMsg();
        getApplicationContext().unregisterReceiver(this.mainReceiver);
        this.mWifiHelper.unregisterOnWifiCallback(this);
        ClientManager.getClient().unregisterConnectStateListener(this.deviceConnectStateListener);
        ClientManager.release();
        ThumbnailManager.release();
        ThumbLoader.getInstance().release();
        super.onDestroy();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void connectDevice(String str) {
        Dbug.i(this.tag, "Second, connect device IP=" + str + ", isConnected=" + ClientManager.getClient().isConnected());
        if (!ClientManager.getClient().isConnected()) {
            this.mHandler.removeMessages(3);
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(3, str), 300L);
            ClientManager.getClient().registerConnectStateListener(this.deviceConnectStateListener);
            return;
        }
        if (this.mActivityManager.getTopActivity() instanceof MainActivity) {
            BaseFragment baseFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.container);
            Dbug.e(this.tag, "Current fragment=" + baseFragment);
            if (baseFragment == null) {
                VideoFragment videoFragment = new VideoFragment();
                changeFragment(R.id.container, videoFragment, videoFragment.getClass().getSimpleName());
                return;
            }
            return;
        }
        Dbug.e(this.tag, "connectDevice: unknown case");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeDeviceWifiMsg() {
        String string = PreferencesHelper.getSharedPreferences(getApplicationContext()).getString(IConstant.CURRENT_WIFI_SSID, null);
        if (TextUtils.isEmpty(string)) {
            return;
        }
        PreferencesHelper.remove(getApplicationContext(), string);
        PreferencesHelper.remove(getApplicationContext(), IConstant.CURRENT_WIFI_SSID);
    }

    @Override // com.jieli.stream.dv.running2.interfaces.OnWifiCallBack
    public void onConnected(WifiInfo wifiInfo) {
        String formatSSID = WifiHelper.formatSSID(wifiInfo.getSSID());
        if (!TextUtils.isEmpty(formatSSID) && formatSSID.contains(WIFI_PREFIX)) {
            this.isReConnectDev = false;
            this.reConnectNum = 0;
            connectDevice(this.mWifiHelper.getGateWay(this.mApplication));
        } else if (this.isReConnectDev) {
            this.mHandler.removeMessages(0);
            this.mHandler.sendEmptyMessageDelayed(0, 100L);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override // com.jieli.stream.dv.running2.interfaces.OnWifiCallBack
    public void onError(int i) {
        Dbug.e(this.tag, "onError >>> errCode = " + i);
        switch (i) {
            case IConstant.ERROR_WIFI_PWD_NOT_MATCH /* 61171 */:
                PreferencesHelper.getSharedPreferences(getApplicationContext()).getString(IConstant.CURRENT_WIFI_SSID, "").contains(WIFI_PREFIX);
                break;
            case IConstant.ERROR_NETWORK_NOT_OPEN /* 61172 */:
                Dbug.e(this.tag, "Wi-Fi is disable !!");
                break;
            case IConstant.ERROR_WIFI_IS_CONNECTED /* 61173 */:
                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService("connectivity");
                NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
                if (activeNetworkInfo != null) {
                    String extraInfo = activeNetworkInfo.getExtraInfo();
                    if (!TextUtils.isEmpty(extraInfo) && extraInfo.contains(WIFI_PREFIX) && activeNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                        connectDevice(this.mWifiHelper.getGateWay(this.mApplication));
                        break;
                    } else {
                        Dbug.e(this.tag, "getExtraInfo is null");
                        break;
                    }
                }
                break;
        }
    }
}
