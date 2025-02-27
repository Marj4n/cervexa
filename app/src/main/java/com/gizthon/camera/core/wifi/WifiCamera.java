package com.gizthon.camera.core.wifi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.generalplus.GoPlusDrone.Activity.CameraNewActivity;
import com.gizthon.camera.core.ICamera;
import com.gizthon.camera.core.OnCameraConnectedListener;
import com.jieli.stream.dv.running2.ui.activity.MainActivity;

/* loaded from: classes.dex */
public class WifiCamera implements ICamera {
    private OnCameraConnectedListener listener;
    public boolean isWifi = false;
    private boolean isJieli = false;

    @Override // com.gizthon.camera.core.ICamera
    public void checkDevice() {
    }

    @Override // com.gizthon.camera.core.ICamera
    public void connectDevice() {
    }

    @Override // com.gizthon.camera.core.ICamera
    public void destroyDevice() {
    }

    @Override // com.gizthon.camera.core.ICamera
    public void onActivityResult(int i, int i2, Intent intent) {
    }

    @Override // com.gizthon.camera.core.ICamera
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
    }

    @Override // com.gizthon.camera.core.ICamera
    public void onStart() {
    }

    @Override // com.gizthon.camera.core.ICamera
    public void setListener(OnCameraConnectedListener onCameraConnectedListener) {
        this.listener = onCameraConnectedListener;
    }

    @Override // com.gizthon.camera.core.ICamera
    public void initDevice(Activity activity) {
        WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        String intToIp = intToIp(connectionInfo.getIpAddress());
        Log.i("TAG", "wifi IP " + intToIp);
        Log.i("TAG", "wifi SSID " + connectionInfo.getSSID());
        String ssid = TextUtils.isEmpty(connectionInfo.getSSID()) ? "" : connectionInfo.getSSID();
        if (TextUtils.equals(intToIp, "192.168.1.2") && ssid.contains("wifi_camera")) {
            this.isWifi = true;
            this.isJieli = false;
        } else if (TextUtils.equals(intToIp, "192.168.25.101") && ssid.contains("Cam-")) {
            this.isWifi = true;
            this.isJieli = true;
        } else {
            this.isWifi = false;
            this.isJieli = false;
        }
    }

    public void startWifi1Activity(Context context) {
        if (this.isJieli) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putBoolean("IsCard", false);
            intent.putExtras(bundle);
            intent.setClass(context, CameraNewActivity.class);
            context.startActivity(intent);
            return;
        }
        MainActivity.start(context);
    }

    private String intToIp(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }
}
