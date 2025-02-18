package com.serenegiant.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.os.PowerManager;
import android.util.Log;

/* loaded from: classes2.dex */
public class PowerHelper {
    private static final String TAG = "PowerHelper";

    public static void wake(Activity activity, boolean z, long j) {
        try {
            PowerManager.WakeLock newWakeLock = ((PowerManager) activity.getSystemService("power")).newWakeLock(805306394, "PowerHelper:disableLock");
            if (j > 0) {
                newWakeLock.acquire(j);
            } else {
                newWakeLock.acquire();
            }
            try {
                ((KeyguardManager) activity.getSystemService("keyguard")).newKeyguardLock(TAG).disableKeyguard();
                newWakeLock.release();
                activity.getWindow().addFlags(524416);
            } catch (Throwable th) {
                newWakeLock.release();
                throw th;
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }
}
