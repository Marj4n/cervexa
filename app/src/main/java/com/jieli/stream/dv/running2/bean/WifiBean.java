package com.jieli.stream.dv.running2.bean;

/* loaded from: classes.dex */
public class WifiBean {
    private String SSID;
    private boolean isSelect = false;
    private String password;
    private int state;

    public String getSSID() {
        return this.SSID;
    }

    public void setSSID(String str) {
        this.SSID = str;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String str) {
        this.password = str;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int i) {
        this.state = i;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean z) {
        this.isSelect = z;
    }
}
