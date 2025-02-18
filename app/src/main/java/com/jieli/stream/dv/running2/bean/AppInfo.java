package com.jieli.stream.dv.running2.bean;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class AppInfo {
    private String appName;
    private String appPlatform;
    private int appVersion;
    private Map<String, List<String>> dev_list;
    private List<String> dev_type;

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public String getAppPlatform() {
        return this.appPlatform;
    }

    public void setAppPlatform(String str) {
        this.appPlatform = str;
    }

    public int getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(int i) {
        this.appVersion = i;
    }

    public List<String> getDev_type() {
        return this.dev_type;
    }

    public void setDev_type(List<String> list) {
        this.dev_type = list;
    }

    public Map<String, List<String>> getDev_list() {
        return this.dev_list;
    }

    public void setDev_list(Map<String, List<String>> map) {
        this.dev_list = map;
    }

    public String toString() {
        String str;
        List<String> list = this.dev_type;
        String str2 = "";
        if (list != null) {
            str = "";
            for (String str3 : list) {
                String str4 = str2 + str3 + ",\t";
                Iterator<String> it = this.dev_list.get(str3).iterator();
                while (it.hasNext()) {
                    str = str + it.next() + ",\t";
                }
                str2 = str4;
            }
        } else {
            str = "";
        }
        return "{\nappName : " + this.appName + "\nappPlatform : " + this.appPlatform + "\nappVersion : " + this.appVersion + "\ndev_type : " + str2 + "\ndev_list : " + str + "\n}";
    }
}
