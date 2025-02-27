package com.serenegiant.usb.common;

import android.app.Activity;
import com.serenegiant.usb.common.AbstractUVCCameraHandler;
import com.serenegiant.usb.widget.CameraViewInterface;

/* loaded from: classes2.dex */
public class UVCCameraHandler extends AbstractUVCCameraHandler {
    public static final UVCCameraHandler createHandler(Activity activity, CameraViewInterface cameraViewInterface, int i, int i2) {
        return createHandler(activity, cameraViewInterface, 1, i, i2, 1, 1.0f);
    }

    public static final UVCCameraHandler createHandler(Activity activity, CameraViewInterface cameraViewInterface, int i, int i2, float f) {
        return createHandler(activity, cameraViewInterface, 1, i, i2, 1, f);
    }

    public static final UVCCameraHandler createHandler(Activity activity, CameraViewInterface cameraViewInterface, int i, int i2, int i3) {
        return createHandler(activity, cameraViewInterface, i, i2, i3, 1, 1.0f);
    }

    public static final UVCCameraHandler createHandler(Activity activity, CameraViewInterface cameraViewInterface, int i, int i2, int i3, int i4) {
        return createHandler(activity, cameraViewInterface, i, i2, i3, i4, 1.0f);
    }

    public static final UVCCameraHandler createHandler(Activity activity, CameraViewInterface cameraViewInterface, int i, int i2, int i3, int i4, float f) {
        CameraThread cameraThread = new CameraThread(UVCCameraHandler.class, activity, cameraViewInterface, i, i2, i3, i4, f);
        cameraThread.start();
        return (UVCCameraHandler) cameraThread.getHandler();
    }

    protected UVCCameraHandler(CameraThread cameraThread) {
        super(cameraThread);
    }

    @Override // com.serenegiant.usb.common.AbstractUVCCameraHandler
    public void startPreview(Object obj) {
        super.startPreview(obj);
    }

    @Override // com.serenegiant.usb.common.AbstractUVCCameraHandler
    public void captureStill(String str, OnCaptureListener onCaptureListener) {
        super.captureStill(str, onCaptureListener);
    }

    @Override // com.serenegiant.usb.common.AbstractUVCCameraHandler
    public void startCameraFoucs() {
        super.startCameraFoucs();
    }
}
