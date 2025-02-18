package com.jieli.stream.dv.running2.ui;

import android.app.Application;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.text.TextUtils;
import com.baidu.mapapi.SDKInitializer;
import com.jieli.stream.dv.running2.bean.DeviceDesc;
import com.jieli.stream.dv.running2.bean.DeviceSettingInfo;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.HandlerUtil;
import com.jieli.stream.dv.running2.util.IConstant;
import com.jieli.stream.dv.running2.util.PreferencesHelper;
import com.jieli.stream.dv.running2.util.ToastUtil;
import com.jieli.stream.dv.running2.util.WifiHelper;

/* loaded from: classes.dex */
public class MainApplication extends Application {
    public static boolean isFactoryMode = false;
    private static MainApplication sMyApplication;
    private String UUID;
    private String appName;
    private int appVersion;
    private String appVersionName;
    private DeviceDesc deviceDesc;
    private DeviceSettingInfo deviceSettingInfo;
    private boolean isUpgrading;
    private boolean isWifiDirectGO = false;
    private boolean sdcardExist;
    private int searchMode;

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        sMyApplication = this;
        ToastUtil.init(this);
        SDKInitializer.initialize(sMyApplication);
        this.appName = PreferencesHelper.getSharedPreferences(getApplicationContext()).getString(IConstant.KEY_ROOT_PATH_NAME, null);
        PackageManager packageManager = getPackageManager();
        if (TextUtils.isEmpty(this.appName)) {
            this.appName = getApplicationInfo().loadLabel(packageManager).toString();
        }
        try {
            this.appVersion = packageManager.getPackageInfo(getPackageName(), 0).versionCode;
            this.appVersionName = packageManager.getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        this.deviceDesc = new DeviceDesc();
        this.deviceSettingInfo = new DeviceSettingInfo();
    }

    public static synchronized MainApplication getApplication() {
        MainApplication mainApplication;
        synchronized (MainApplication.class) {
            mainApplication = sMyApplication;
        }
        return mainApplication;
    }

    public String getUUID() {
        return this.UUID;
    }

    public void setUUID(String str) {
        this.UUID = str;
    }

    public boolean isSdcardExist() {
        return this.sdcardExist;
    }

    public void setSdcardExist(boolean z) {
        this.sdcardExist = z;
    }

    public DeviceSettingInfo getDeviceSettingInfo() {
        return this.deviceSettingInfo;
    }

    public String getAppName() {
        return this.appName;
    }

    public int getAppVersion() {
        return this.appVersion;
    }

    public String getAppVersionName() {
        return this.appVersionName;
    }

    public void setDeviceDesc(DeviceDesc deviceDesc) {
        if (deviceDesc != null) {
            this.deviceDesc = deviceDesc;
        }
    }

    public DeviceDesc getDeviceDesc() {
        return this.deviceDesc;
    }

    public boolean isUpgrading() {
        return this.isUpgrading;
    }

    public void setUpgrading(boolean z) {
        this.isUpgrading = z;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public String getCameraDir() {
        return this.deviceSettingInfo.getCameraType() == 2 ? IConstant.DIR_REAR : IConstant.DIR_FRONT;
    }

    public int getSearchMode() {
        return this.searchMode;
    }

    public void setSearchMode(int i) {
        this.searchMode = i;
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Dbug.w(getClass().getSimpleName(), "onConfigurationChanged orientation : " + configuration.orientation);
        PreferencesHelper.getSharedPreferences(getApplication()).getString(IConstant.KEY_APP_LANGUAGE_CODE, "-1");
    }

    public void switchWifi() {
        ClientManager.getClient().close();
        HandlerUtil.postDelayed(new Runnable() { // from class: com.jieli.stream.dv.running2.ui.MainApplication.1
            @Override // java.lang.Runnable
            public void run() {
                WifiHelper.getInstance(MainApplication.sMyApplication).removeCurrentNetwork(MainApplication.sMyApplication);
            }
        }, 1000L);
    }

    public boolean isWifiDirectGO() {
        return this.isWifiDirectGO;
    }
}
