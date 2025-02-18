package com.jieli.stream.dv.running2.util;

import com.jieli.lib.dv.control.DeviceClient;
import com.jieli.stream.dv.running2.ui.MainApplication;

/* loaded from: classes.dex */
public class UDPClientManager {
    private static DeviceClient instance;
    private String tag = getClass().getSimpleName();

    public static DeviceClient getClient() {
        if (instance == null) {
            synchronized (UDPClientManager.class) {
                if (instance == null) {
                    instance = new DeviceClient(MainApplication.getApplication(), 1);
                }
            }
        }
        return instance;
    }

    public static void release() {
        DeviceClient deviceClient = instance;
        if (deviceClient != null) {
            deviceClient.release();
            instance = null;
        }
    }
}
