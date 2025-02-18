package com.serenegiant.net;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import java.util.List;

/* loaded from: classes2.dex */
public interface WiFiP2pListener {
    void onConnect(WifiP2pInfo wifiP2pInfo);

    void onDisconnect();

    void onError(Exception exc);

    void onStateChanged(boolean z);

    void onUpdateDevices(List<WifiP2pDevice> list);
}
