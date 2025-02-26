package com.marjan.cervexa.core.usb;

import android.app.Activity;
import android.content.Intent;
import com.marjan.cervexa.core.ICamera;
import com.marjan.cervexa.core.OnCameraConnectedListener;

/* loaded from: classes.dex */
public class USBCamera implements ICamera {
    public String TAG = getClass().getSimpleName();
    public Activity context;
    public OnCameraConnectedListener listener;

    @Override // com.marjan.cervexa.core.ICamera
    public void checkDevice() {
    }

    @Override // com.marjan.cervexa.core.ICamera
    public void connectDevice() {
    }

    @Override // com.marjan.cervexa.core.ICamera
    public void destroyDevice() {
    }

    @Override // com.marjan.cervexa.core.ICamera
    public void onActivityResult(int i, int i2, Intent intent) {
    }

    @Override // com.marjan.cervexa.core.ICamera
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
    }

    @Override // com.marjan.cervexa.core.ICamera
    public void onStart() {
    }

    @Override // com.marjan.cervexa.core.ICamera
    public void setListener(OnCameraConnectedListener onCameraConnectedListener) {
        this.listener = onCameraConnectedListener;
    }

    @Override // com.marjan.cervexa.core.ICamera
    public void initDevice(Activity activity) {
        this.context = activity;
    }
}
