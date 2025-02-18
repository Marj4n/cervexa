package com.serenegiant.utils;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;

/* loaded from: classes2.dex */
public class DeviceAdminReceiverLock extends DeviceAdminReceiver {
    public static final String EXTRA_REQUEST_FINISH = "EXTRA_REQUEST_FINISH";
    private static final int REQ_SCREEN_LOCK = 412809;

    public static void requestScreenLock(Activity activity, boolean z) {
        if (checkScreenLock(activity, z)) {
            return;
        }
        Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
        intent.putExtra("android.app.extra.DEVICE_ADMIN", new ComponentName(activity, (Class<?>) DeviceAdminReceiverLock.class));
        intent.putExtra(EXTRA_REQUEST_FINISH, z);
        activity.startActivityForResult(intent, REQ_SCREEN_LOCK);
    }

    private static boolean checkScreenLock(Activity activity, boolean z) {
        ComponentName componentName = new ComponentName(activity, (Class<?>) DeviceAdminReceiverLock.class);
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) activity.getSystemService("device_policy");
        if (!devicePolicyManager.isAdminActive(componentName)) {
            return false;
        }
        devicePolicyManager.lockNow();
        if (!z) {
            return true;
        }
        activity.finish();
        return true;
    }

    public static boolean onActivityResult(Activity activity, int i, int i2, Intent intent) {
        boolean z = false;
        if (i != REQ_SCREEN_LOCK || i2 != -1) {
            return false;
        }
        if (intent != null && intent.getBooleanExtra(EXTRA_REQUEST_FINISH, false)) {
            z = true;
        }
        checkScreenLock(activity, z);
        return true;
    }
}
