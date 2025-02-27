package com.gizthon.camera;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import com.gizthon.camera.activity.CameraBaseActivity;
import com.gizthon.camera.activity.GalleryListActivity;
import com.gizthon.camera.activity.HelpActivity;
import com.gizthon.camera.activity.UVCUSBCameraActivity;
import com.gizthon.camera.databinding.ActivitySplashBinding;
import com.jaeger.library.StatusBarUtil;
import permissions.dispatcher.PermissionRequest;

/* loaded from: classes.dex */
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends CameraBaseActivity {
    private String TAG = getClass().getSimpleName();
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        binding.setEventHandler(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#202226"));
        cameraManager.getWifiCamera().initDevice(this);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.cameraManager.getWifiCamera().initDevice(this);
    }

    public void onClickUsb() {
        SplashActivityPermissionsDispatcher.connectDeviceWithPermissionCheck(this, false);
    }

    public void onClickWifi() {
        SplashActivityPermissionsDispatcher.connectDeviceWithPermissionCheck(this, false);
    }

    @Override // com.gizthon.camera.activity.CameraBaseActivity
    public void onClickGallery() {
        GalleryListActivity.start(this);
    }

    public void onClickHelp() {
        HelpActivity.start(this);
    }

    public void connectDevice(boolean z) {
        this.cameraManager.getWifiCamera().initDevice(this);
        if (!this.cameraManager.getWifiCamera().isWifi) {
            UVCUSBCameraActivity.start(this);
        } else {
            this.cameraManager.getWifiCamera().startWifi1Activity(this);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, i, iArr);
    }

    public void showRationaleForCamera(PermissionRequest permissionRequest) {
        Log.i(this.TAG, "showRationaleForCamera");
        permissionRequest.proceed();
    }

    public void onCameraDenied() {
        Log.i(this.TAG, "onCameraDenied");
    }

    public void onCameraNeverAskAgain() {
        Log.i(this.TAG, "onCameraNeverAskAgain");
    }
}
