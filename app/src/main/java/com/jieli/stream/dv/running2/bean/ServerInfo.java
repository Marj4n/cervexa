package com.jieli.stream.dv.running2.bean;

import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class ServerInfo {
    private Map<String, List<Integer>> androidVersionMap;
    private Map<String, List<String>> firmwareVersionMap;

    public Map<String, List<Integer>> getAndroidVersionMap() {
        return this.androidVersionMap;
    }

    public void setAndroidVersionMap(Map<String, List<Integer>> map) {
        this.androidVersionMap = map;
    }

    public Map<String, List<String>> getFirmwareVersionMap() {
        return this.firmwareVersionMap;
    }

    public void setFirmwareVersionMap(Map<String, List<String>> map) {
        this.firmwareVersionMap = map;
    }

    public String toString() {
        String str;
        Map<String, List<Integer>> map = this.androidVersionMap;
        if (map != null) {
            str = "androidVersionMap :{\n";
            for (String str2 : map.keySet()) {
                str = str + "\"" + str2 + "\":[ ";
                List<Integer> list = this.androidVersionMap.get(str2);
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        Integer num = list.get(i);
                        str = i == list.size() - 1 ? str + num + "],\n" : str + num + ", ";
                    }
                }
            }
        } else {
            str = "";
        }
        Map<String, List<String>> map2 = this.firmwareVersionMap;
        if (map2 != null) {
            str = "firmwareVersionMap :{\n";
            for (String str3 : map2.keySet()) {
                str = str + "\"" + str3 + "\":[ ";
                List<String> list2 = this.firmwareVersionMap.get(str3);
                if (list2 != null) {
                    for (int i2 = 0; i2 < list2.size(); i2++) {
                        String str4 = list2.get(i2);
                        str = i2 == list2.size() - 1 ? str + str4 + "],\n" : str + str4 + ", ";
                    }
                }
            }
        }
        return str;
    }
}
