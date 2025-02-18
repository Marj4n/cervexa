package com.jieli.stream.dv.running2.bean;

import android.text.TextUtils;

/* loaded from: classes.dex */
public class DeviceBean {
    private int mode;
    private double version;
    private String versionName;
    private String wifiIP;
    private String wifiSSID;
    private String wifiType;

    public String getWifiSSID() {
        return this.wifiSSID;
    }

    public void setWifiSSID(String str) {
        this.wifiSSID = str;
    }

    public String getWifiIP() {
        return this.wifiIP;
    }

    public void setWifiIP(String str) {
        this.wifiIP = str;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setVersionName(String str) {
        this.versionName = str;
    }

    public double getVersion() {
        return this.version;
    }

    public void setVersion(double d) {
        this.version = d;
    }

    public String getWifiType() {
        return this.wifiType;
    }

    public void setWifiType(String str) {
        this.wifiType = str;
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int i) {
        this.mode = i;
    }

    public String toString() {
        String str = "";
        if (!TextUtils.isEmpty(this.wifiSSID)) {
            str = "\"wifiSSID\" : \"" + this.wifiSSID + "\",\n";
        }
        if (!TextUtils.isEmpty(this.wifiIP)) {
            str = str + "\"wifiIP\" : \"" + this.wifiIP + "\",\n";
        }
        if (!TextUtils.isEmpty(this.wifiType)) {
            str = str + "\"wifiType\" : \"" + this.wifiType + "\",\n";
        }
        String str2 = str + "\"mode\":" + this.mode;
        if (!TextUtils.isEmpty(this.versionName)) {
            str2 = str2 + "\"versionName\" : \"" + this.versionName + "\",\n";
        }
        return str2 + "\"version\":" + this.version;
    }
}
