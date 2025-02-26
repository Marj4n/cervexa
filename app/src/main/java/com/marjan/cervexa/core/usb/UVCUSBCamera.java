package com.marjan.cervexa.core.usb;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.os.PowerManager;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;
import com.marjan.cervexa.core.OnCameraConnectedListener;
import com.jiangdg.usbcamera.UVCCameraHelper;
import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.IButtonCallback;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.widget.CameraViewInterface;
import com.serenegiant.usb.widget.UVCCameraTextureView;

/* loaded from: classes.dex */
public class UVCUSBCamera extends USBCamera implements CameraViewInterface.Callback, CameraDialog.CameraDialogParent {
    private int REQUEST_CODE_REGISTER = 101;
    private ComponentName adminReceiver;
    private IButtonCallback iButtonCallback;
    private boolean isPreview;
    private boolean isRequest;
    private UVCCameraHelper mCameraHelper;
    private PowerManager mPowerManager;
    private UVCCameraTextureView mUVCCameraView;
    private DevicePolicyManager policyManager;

    @Override // com.serenegiant.usb.widget.CameraViewInterface.Callback
    public void onSurfaceChanged(CameraViewInterface cameraViewInterface, Surface surface, int i, int i2) {
    }

    @Override // com.marjan.cervexa.core.usb.USBCamera, com.marjan.cervexa.core.ICamera
    public void initDevice(Activity activity) {
        super.initDevice(activity);
        this.policyManager = (DevicePolicyManager) activity.getSystemService("device_policy");
        this.adminReceiver = new ComponentName(activity, (Class<?>) UVCScreenOffAdminReceiver.class);
        this.mPowerManager = (PowerManager) activity.getSystemService("power");
    }

    public void checkAdmin() {
        Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
        intent.putExtra("android.app.extra.DEVICE_ADMIN", this.adminReceiver);
        intent.putExtra("android.app.extra.ADD_EXPLANATION", "开启后就可以使用锁屏功能了...");
        this.context.startActivityForResult(intent, this.REQUEST_CODE_REGISTER);
    }

    @Override // com.marjan.cervexa.core.usb.USBCamera, com.marjan.cervexa.core.ICamera
    public void checkDevice() {
        checkSuccess();
    }

    public void checkSuccess() {
        if (this.listener != null) {
            this.listener.onConnectedSuccess(OnCameraConnectedListener.ADMIN_ACTIVE);
        }
    }

    @Override // com.marjan.cervexa.core.usb.USBCamera, com.marjan.cervexa.core.ICamera
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == this.REQUEST_CODE_REGISTER) {
            checkDevice();
        }
    }

    @Override // com.marjan.cervexa.core.usb.USBCamera, com.marjan.cervexa.core.ICamera
    public void connectDevice() {
        UVCCameraHelper uVCCameraHelper = UVCCameraHelper.getInstance();
        this.mCameraHelper = uVCCameraHelper;
        uVCCameraHelper.initUSBMonitor(this.context, this.mUVCCameraView, new UVCCameraHelper.OnMyDevConnectListener() { // from class: com.marjan.cervexa.core.usb.UVCUSBCamera.1
            @Override // com.jiangdg.usbcamera.UVCCameraHelper.OnMyDevConnectListener
            public void onAttachDev(UsbDevice usbDevice) {
                if (UVCUSBCamera.this.isRequest) {
                    return;
                }
                UVCUSBCamera.this.isRequest = true;
                if (UVCUSBCamera.this.mCameraHelper != null) {
                    UVCUSBCamera.this.mCameraHelper.requestPermission(0);
                }
            }

            @Override // com.jiangdg.usbcamera.UVCCameraHelper.OnMyDevConnectListener
            public void onDettachDev(UsbDevice usbDevice) {
                if (UVCUSBCamera.this.isRequest) {
                    UVCUSBCamera.this.isRequest = false;
                    UVCUSBCamera.this.mCameraHelper.closeCamera();
                    UVCUSBCamera.this.showShortMsg(usbDevice.getDeviceName() + " is out");
                }
            }

            @Override // com.jiangdg.usbcamera.UVCCameraHelper.OnMyDevConnectListener
            public void onConnectDev(UsbDevice usbDevice, boolean z) {
                if (!z) {
                    UVCUSBCamera.this.isPreview = false;
                } else {
                    UVCUSBCamera.this.isPreview = true;
                    UVCUSBCamera.this.listener.onConnectedSuccess(20003);
                }
            }

            @Override // com.jiangdg.usbcamera.UVCCameraHelper.OnMyDevConnectListener
            public void onDisConnectDev(UsbDevice usbDevice) {
                UVCUSBCamera.this.listener.onConnectedSuccess(20004);
            }
        });
        this.mCameraHelper.setIButtonCallback(this.iButtonCallback);
    }

    public void setCameraView(UVCCameraTextureView uVCCameraTextureView) {
        this.mUVCCameraView = uVCCameraTextureView;
    }

    public void setIButtonCallback(IButtonCallback iButtonCallback) {
        this.iButtonCallback = iButtonCallback;
    }

    @Override // com.marjan.cervexa.core.usb.USBCamera, com.marjan.cervexa.core.ICamera
    public void onStart() {
        UVCCameraHelper uVCCameraHelper = this.mCameraHelper;
        if (uVCCameraHelper != null) {
            uVCCameraHelper.registerUSB();
        }
    }

    @Override // com.marjan.cervexa.core.usb.USBCamera, com.marjan.cervexa.core.ICamera
    public void destroyDevice() {
        UVCCameraHelper uVCCameraHelper = this.mCameraHelper;
        if (uVCCameraHelper != null) {
            uVCCameraHelper.release();
        }
    }

    public UVCCameraHelper getCameraHelper() {
        return this.mCameraHelper;
    }

    @Override // com.serenegiant.usb.widget.CameraViewInterface.Callback
    public void onSurfaceCreated(CameraViewInterface cameraViewInterface, Surface surface) {
        if (!this.isPreview && this.mCameraHelper.isCameraOpened()) {
            this.mCameraHelper.startPreview(this.mUVCCameraView);
            this.isPreview = true;
        } else if (this.listener != null) {
            this.listener.onConnectedFailed(10000);
        }
    }

    @Override // com.serenegiant.usb.widget.CameraViewInterface.Callback
    public void onSurfaceDestroy(CameraViewInterface cameraViewInterface, Surface surface) {
        if (this.isPreview && this.mCameraHelper.isCameraOpened()) {
            this.mCameraHelper.stopPreview();
            this.isPreview = false;
        }
    }

    @Override // com.serenegiant.usb.CameraDialog.CameraDialogParent
    public USBMonitor getUSBMonitor() {
        return getCameraHelper().getUSBMonitor();
    }

    @Override // com.serenegiant.usb.CameraDialog.CameraDialogParent
    public void onDialogResult(boolean z) {
        if (z) {
            Log.i(this.TAG, "取消操作");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showShortMsg(String str) {
        Toast.makeText(this.context, str, 0).show();
    }
}
