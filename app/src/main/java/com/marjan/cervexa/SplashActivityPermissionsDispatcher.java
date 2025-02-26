package com.marjan.cervexa;

import androidx.core.app.ActivityCompat;
import java.lang.ref.WeakReference;
import permissions.dispatcher.GrantableRequest;
import permissions.dispatcher.PermissionUtils;

/* loaded from: classes.dex */
final class SplashActivityPermissionsDispatcher {
    private static GrantableRequest PENDING_CONNECTDEVICE;
    private static final String[] PERMISSION_CONNECTDEVICE = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO", "android.permission.ACCESS_FINE_LOCATION"};
    private static final int REQUEST_CONNECTDEVICE = 0;

    private SplashActivityPermissionsDispatcher() {
    }

    static void connectDeviceWithPermissionCheck(SplashActivity splashActivity, boolean z) {
        if (PermissionUtils.hasSelfPermissions(splashActivity, PERMISSION_CONNECTDEVICE)) {
            splashActivity.connectDevice(z);
            return;
        }
        PENDING_CONNECTDEVICE = new SplashActivityConnectDevicePermissionRequest(splashActivity, z);
        if (PermissionUtils.shouldShowRequestPermissionRationale(splashActivity, PERMISSION_CONNECTDEVICE)) {
            splashActivity.showRationaleForCamera(PENDING_CONNECTDEVICE);
        } else {
            ActivityCompat.requestPermissions(splashActivity, PERMISSION_CONNECTDEVICE, 0);
        }
    }

    static void onRequestPermissionsResult(SplashActivity splashActivity, int i, int[] iArr) {
        GrantableRequest grantableRequest;
        if (i != 0) {
            return;
        }
        if (PermissionUtils.verifyPermissions(iArr) && (grantableRequest = PENDING_CONNECTDEVICE) != null) {
            grantableRequest.grant();
        }
        PENDING_CONNECTDEVICE = null;
    }

    private static final class SplashActivityConnectDevicePermissionRequest implements GrantableRequest {
        private final boolean isUSB;
        private final WeakReference<SplashActivity> weakTarget;

        @Override // permissions.dispatcher.PermissionRequest
        public void cancel() {
        }

        private SplashActivityConnectDevicePermissionRequest(SplashActivity splashActivity, boolean z) {
            this.weakTarget = new WeakReference<>(splashActivity);
            this.isUSB = z;
        }

        @Override // permissions.dispatcher.PermissionRequest
        public void proceed() {
            SplashActivity splashActivity = this.weakTarget.get();
            if (splashActivity == null) {
                return;
            }
            ActivityCompat.requestPermissions(splashActivity, SplashActivityPermissionsDispatcher.PERMISSION_CONNECTDEVICE, 0);
        }

        @Override // permissions.dispatcher.GrantableRequest
        public void grant() {
            SplashActivity splashActivity = this.weakTarget.get();
            if (splashActivity == null) {
                return;
            }
            splashActivity.connectDevice(this.isUSB);
        }
    }
}
