package com.gizthon.camera.core;

/* loaded from: classes.dex */
public interface OnCameraConnectedListener {
    public static final int ADMIN_ACTIVE = 20000;
    public static final int FAILED_DEVICE_ACTIVE = 10001;
    public static final int FAILED_DEVICE_DETACHED = 20002;
    public static final int FAILED_DEVICE_DISCONNECTED = 20004;
    public static final int FAILED_UN_SUPPORT = 10000;
    public static final int SUCCESS_DEVICE_ATTACHED = 20001;
    public static final int SUCCESS_DEVICE_CONNECTED = 20003;

    void onConnectedFailed(int i);

    void onConnectedSuccess(int i);
}
