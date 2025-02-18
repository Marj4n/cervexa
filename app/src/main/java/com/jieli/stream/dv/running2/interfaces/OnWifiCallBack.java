package com.jieli.stream.dv.running2.interfaces;

import android.net.wifi.WifiInfo;

/* loaded from: classes.dex */
public interface OnWifiCallBack {
    void onConnected(WifiInfo wifiInfo);

    void onError(int i);
}
