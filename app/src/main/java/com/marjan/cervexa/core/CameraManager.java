package com.marjan.cervexa.core;

import android.app.Activity;
import android.content.Intent;
import com.marjan.cervexa.core.usb.UVCUSBCamera;
import com.marjan.cervexa.core.wifi.WifiCamera;

/* loaded from: classes.dex */
public class CameraManager {
    public static final int TYPE_USB_1 = 1;
    public static final int TYPE_USB_2 = 2;
    public static final int TYPE_WIFI_1 = 3;
    public static final int TYPE_WIFI_2 = 4;
    public static int deviceType;
    private Activity context;
    private ICamera camera = new UVCUSBCamera();
    private WifiCamera wifiCamera = new WifiCamera();

    public CameraManager(Activity activity) {
        this.context = activity;
    }

    public WifiCamera getWifiCamera() {
        return this.wifiCamera;
    }

    public UVCUSBCamera getUVCUSBCamera() {
        return (UVCUSBCamera) this.camera;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        this.camera.onActivityResult(i, i2, intent);
        this.wifiCamera.onActivityResult(i, i2, intent);
    }

    public static void setDeviceType(int i) {
        deviceType = i;
    }
}
