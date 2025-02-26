package com.marjan.cervexa.core.usb;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/* loaded from: classes.dex */
public class UVCScreenOffAdminReceiver extends DeviceAdminReceiver {
    private void showToast(Context context, String str) {
        Toast.makeText(context, str, 0).show();
    }

    @Override // android.app.admin.DeviceAdminReceiver
    public void onEnabled(Context context, Intent intent) {
        showToast(context, "设备管理器使用");
    }

    @Override // android.app.admin.DeviceAdminReceiver
    public void onDisabled(Context context, Intent intent) {
        showToast(context, "设备管理器没有使用");
    }
}
