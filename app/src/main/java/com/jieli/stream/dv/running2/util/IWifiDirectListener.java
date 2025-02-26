package com.jieli.stream.dv.running2.util;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import java.util.List;

/* loaded from: classes.dex */
public interface IWifiDirectListener {
    void onCallP2pConnectionChanged(WifiP2pInfo wifiP2pInfo);

    void onCallP2pDeviceChanged(WifiP2pDevice wifiP2pDevice);

    void onCallP2pPeersChanged(List<WifiP2pDevice> list);

    void onCallP2pStateChanged(int i);
}
