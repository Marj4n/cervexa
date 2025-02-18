package com.gizthon.camera.core.usb;

import android.app.Activity;
import android.content.Intent;
import com.gizthon.camera.core.ICamera;
import com.gizthon.camera.core.OnCameraConnectedListener;

/* loaded from: classes.dex */
public class USBCamera implements ICamera {
    public String TAG = getClass().getSimpleName();
    public Activity context;
    public OnCameraConnectedListener listener;

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
        this.context = activity;
    }
}
