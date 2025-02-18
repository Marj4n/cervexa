package com.jieli.stream.dv.running2.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.util.ActivityManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IActions;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import com.jieli.stream.dv.running2.util.WifiHelper;
import com.serenegiant.net.NetworkChangedReceiver;

/* loaded from: classes.dex */
public abstract class BaseActivity extends FragmentActivity implements IConstant, IActions {
    private final String TAG = getClass().getSimpleName();
    public MainApplication mApplication;
    private BaseWifiBroadcastReceiver mReceiver;
    public WifiHelper mWifiHelper;

    private class BaseWifiBroadcastReceiver extends BroadcastReceiver {
        private BaseWifiBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            char c;
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            int hashCode = action.hashCode();
            if (hashCode == -1172645946) {
                if (action.equals(NetworkChangedReceiver.ACTION_GLOBAL_CONNECTIVITY_CHANGE)) {
                    c = 2;
                }
                c = 65535;
            } else if (hashCode != -343630553) {
                if (hashCode == 233521600 && action.equals("android.net.wifi.supplicant.STATE_CHANGE")) {
                    c = 1;
                }
                c = 65535;
            } else {
                if (action.equals("android.net.wifi.STATE_CHANGE")) {
                    c = 0;
                }
                c = 65535;
            }
            int i = IConstant.ERROR_NETWORK_TYPE_NOT_WIFI;
            if (c == 0) {
                if (BaseActivity.this.mWifiHelper == null) {
                    BaseActivity baseActivity = BaseActivity.this;
                    baseActivity.mWifiHelper = WifiHelper.getInstance(baseActivity.getApplicationContext());
                }
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                WifiInfo wifiInfo = (WifiInfo) intent.getParcelableExtra("wifiInfo");
                if (networkInfo == null) {
                    Dbug.e(BaseActivity.this.TAG, "networkInfo is null");
                    BaseActivity.this.mWifiHelper.notifyWifiError(IConstant.ERROR_NETWORK_INFO_EMPTY);
                    return;
                }
                if (networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                    if (networkInfo.getType() != 1) {
                        Dbug.e(BaseActivity.this.TAG, "networkType is not TYPE_WIFI");
                        BaseActivity.this.mWifiHelper.notifyWifiError(IConstant.ERROR_NETWORK_TYPE_NOT_WIFI);
                        return;
                    } else if (wifiInfo == null || TextUtils.isEmpty(wifiInfo.getSSID())) {
                        Dbug.e(BaseActivity.this.TAG, "wifiInfo is  empty or ssid is null");
                        BaseActivity.this.mWifiHelper.notifyWifiError(IConstant.ERROR_WIFI_INFO_EMPTY);
                        return;
                    } else {
                        BaseActivity.this.mWifiHelper.notifyWifiConnect(wifiInfo);
                        return;
                    }
                }
                return;
            }
            if (c == 1) {
                if (BaseActivity.this.mWifiHelper == null) {
                    BaseActivity baseActivity2 = BaseActivity.this;
                    baseActivity2.mWifiHelper = WifiHelper.getInstance(baseActivity2.getApplicationContext());
                }
                SupplicantState supplicantState = (SupplicantState) intent.getParcelableExtra("newState");
                int intExtra = intent.getIntExtra("supplicantError", -1);
                Dbug.i(BaseActivity.this.TAG, "supplicantError=" + intExtra + ", state=" + supplicantState);
                if (SupplicantState.DISCONNECTED.equals(supplicantState) && intExtra == 1) {
                    BaseActivity.this.mWifiHelper.notifyWifiError(IConstant.ERROR_WIFI_PWD_NOT_MATCH);
                    String string = PreferencesHelper.getSharedPreferences(BaseActivity.this.getApplicationContext()).getString(IConstant.CURRENT_WIFI_SSID, null);
                    if (TextUtils.isEmpty(string)) {
                        return;
                    }
                    BaseActivity.this.mWifiHelper.removeSavedNetWork(string);
                    PreferencesHelper.remove(BaseActivity.this.getApplicationContext(), string);
                    PreferencesHelper.remove(BaseActivity.this.getApplicationContext(), IConstant.CURRENT_WIFI_SSID);
                    return;
                }
                return;
            }
            if (c != 2) {
                return;
            }
            if (BaseActivity.this.mWifiHelper == null) {
                BaseActivity baseActivity3 = BaseActivity.this;
                baseActivity3.mWifiHelper = WifiHelper.getInstance(baseActivity3.getApplicationContext());
            }
            if (!BaseActivity.this.mWifiHelper.isWifiOpen()) {
                if (!WifiHelper.isNetWorkConnectedType(BaseActivity.this.getApplicationContext(), 0)) {
                    i = IConstant.ERROR_NETWORK_NOT_OPEN;
                }
                BaseActivity.this.mWifiHelper.notifyWifiError(i);
                return;
            }
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
            if (activeNetworkInfo == null || activeNetworkInfo.getType() != 1) {
                Dbug.e(BaseActivity.this.TAG, "Don't have Wifi Connection");
                return;
            }
            WifiInfo wifiConnectionInfo = BaseActivity.this.mWifiHelper.getWifiConnectionInfo();
            if (wifiConnectionInfo == null) {
                return;
            }
            Dbug.i(BaseActivity.this.TAG, "Have Wifi Connection:" + wifiConnectionInfo.getSSID());
            BaseActivity.this.mWifiHelper.notifyWifiConnect(wifiConnectionInfo);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        MainApplication application = MainApplication.getApplication();
        this.mApplication = application;
        this.mWifiHelper = WifiHelper.getInstance(application);
        ActivityManager.getInstance().pushActivity(this);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        registerBroadCastReceiver();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        if (this.mReceiver != null) {
            MainApplication.getApplication().unregisterReceiver(this.mReceiver);
            this.mReceiver = null;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().popActivity(this);
    }

    private void registerBroadCastReceiver() {
        if (this.mReceiver == null) {
            this.mReceiver = new BaseWifiBroadcastReceiver();
        }
        IntentFilter intentFilter = new IntentFilter("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
        intentFilter.addAction(NetworkChangedReceiver.ACTION_GLOBAL_CONNECTIVITY_CHANGE);
        MainApplication.getApplication().registerReceiver(this.mReceiver, intentFilter);
    }

    public void changeFragment(int i, Fragment fragment, String str) {
        FragmentManager supportFragmentManager;
        FragmentTransaction beginTransaction;
        if (fragment == null || isFinishing() || isDestroyed() || (supportFragmentManager = getSupportFragmentManager()) == null || (beginTransaction = supportFragmentManager.beginTransaction()) == null) {
            return;
        }
        if (!TextUtils.isEmpty(str)) {
            beginTransaction.replace(i, fragment, str);
        } else {
            beginTransaction.replace(i, fragment);
        }
        beginTransaction.addToBackStack(null);
        beginTransaction.commitAllowingStateLoss();
    }

    public void changeFragment(int i, Fragment fragment) {
        changeFragment(i, fragment, null);
    }
}
