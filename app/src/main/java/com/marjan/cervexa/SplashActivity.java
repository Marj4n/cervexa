package com.marjan.cervexa;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import com.marjan.cervexa.activity.CameraBaseActivity;
import com.marjan.cervexa.activity.GalleryListActivity;
import com.marjan.cervexa.activity.HelpActivity;
import com.marjan.cervexa.activity.UVCUSBCameraActivity;
import com.marjan.cervexa.databinding.SplashActivityBinding;
import com.jaeger.library.StatusBarUtil;
import permissions.dispatcher.PermissionRequest;

/* loaded from: classes.dex */
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends CameraBaseActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private SplashActivityBinding binding;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        binding = DataBindingUtil.setContentView(this, R.layout.splash_activity);

        if (binding != null) {
            binding.setEventHandler(this);
        } else {
            Log.e(TAG, "Binding is null. Check if splash_activity.xml is correctly set up for Data Binding.");
        }

        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#202226"));

        if (cameraManager != null && cameraManager.getWifiCamera() != null) {
            cameraManager.getWifiCamera().initDevice(this);
        } else {
            Log.e(TAG, "CameraManager or WifiCamera is null. Ensure it's properly initialized.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraManager != null && cameraManager.getWifiCamera() != null) {
            cameraManager.getWifiCamera().initDevice(this);
        } else {
            Log.e(TAG, "CameraManager or WifiCamera is null in onResume.");
        }
    }

    public void onClickUsb() {
        SplashActivityPermissionsDispatcher.connectDeviceWithPermissionCheck(this, false);
    }

    public void onClickWifi() {
        SplashActivityPermissionsDispatcher.connectDeviceWithPermissionCheck(this, false);
    }

    @Override
    public void onClickGallery() {
        GalleryListActivity.start(this);
    }

    public void onClickHelp() {
        HelpActivity.start(this);
    }

    public void connectDevice(boolean z) {
        if (cameraManager != null && cameraManager.getWifiCamera() != null) {
            cameraManager.getWifiCamera().initDevice(this);
            if (!cameraManager.getWifiCamera().isWifi) {
                UVCUSBCameraActivity.start(this);
            } else {
                cameraManager.getWifiCamera().startWifi1Activity(this);
            }
        } else {
            Log.e(TAG, "CameraManager or WifiCamera is null in connectDevice.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, i, iArr);
    }

    public void showRationaleForCamera(PermissionRequest permissionRequest) {
        Log.i(TAG, "showRationaleForCamera");
        permissionRequest.proceed();
    }

    public void onCameraDenied() {
        Log.i(TAG, "onCameraDenied");
    }

    public void onCameraNeverAskAgain() {
        Log.i(TAG, "onCameraNeverAskAgain");
    }
}
