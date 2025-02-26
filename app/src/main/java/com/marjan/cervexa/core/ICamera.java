package com.marjan.cervexa.core;

import android.app.Activity;
import android.content.Intent;

/* loaded from: classes.dex */
public interface ICamera {
    void checkDevice();

    void connectDevice();

    void destroyDevice();

    void initDevice(Activity activity);

    void onActivityResult(int i, int i2, Intent intent);

    void onRequestPermissionsResult(int i, String[] strArr, int[] iArr);

    void onStart();

    void setListener(OnCameraConnectedListener onCameraConnectedListener);
}
