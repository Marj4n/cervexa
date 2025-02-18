package com.jieli.stream.dv.running2.bean;

import java.util.List;

/* loaded from: classes.dex */
public class DeviceDesc {
    private AppListBean app_list;
    private String firmware_version;
    private String[] front_support;
    private String match_app_type;
    private String product_type;
    private String[] rear_support;
    private String[] recordFrontSupport;
    private String[] recordRearSupport;
    private String[] rtspFrontSupport;
    private String[] rtspRearSupport;
    private boolean support_bumping;
    private boolean support_projection;
    private String uuid;
    private String device_type = "1";
    private int videoType = 1;
    private int netMode = -1;

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String str) {
        this.uuid = str;
    }

    public String getProduct_type() {
        return this.product_type;
    }

    public void setProduct_type(String str) {
        this.product_type = str;
    }

    public String getMatch_app_type() {
        return this.match_app_type;
    }

    public void setMatch_app_type(String str) {
        this.match_app_type = str;
    }

    public String getFirmware_version() {
        return this.firmware_version;
    }

    public void setFirmware_version(String str) {
        this.firmware_version = str;
    }

    public AppListBean getApp_list() {
        return this.app_list;
    }

    public void setApp_list(AppListBean appListBean) {
        this.app_list = appListBean;
    }

    public String getDevice_type() {
        return this.device_type;
    }

    public void setDevice_type(String str) {
        this.device_type = str;
    }

    public boolean isSupport_bumping() {
        return this.support_bumping;
    }

    public void setSupport_bumping(boolean z) {
        this.support_bumping = z;
    }

    public String[] getFront_support() {
        return this.front_support;
    }

    public void setFront_support(String[] strArr) {
        this.front_support = strArr;
    }

    public String[] getRear_support() {
        return this.rear_support;
    }

    public void setRear_support(String[] strArr) {
        this.rear_support = strArr;
    }

    public void setSupport_projection(boolean z) {
        this.support_projection = z;
    }

    public boolean isSupport_projection() {
        return this.support_projection;
    }

    public int getVideoType() {
        return this.videoType;
    }

    public void setVideoType(int i) {
        this.videoType = i;
    }

    public int getNetMode() {
        return this.netMode;
    }

    public void setNetMode(int i) {
        this.netMode = i;
    }

    public String[] getRtspFrontSupport() {
        return this.rtspFrontSupport;
    }

    public void setRtspFrontSupport(String[] strArr) {
        this.rtspFrontSupport = strArr;
    }

    public String[] getRtspRearSupport() {
        return this.rtspRearSupport;
    }

    public void setRtspRearSupport(String[] strArr) {
        this.rtspRearSupport = strArr;
    }

    public String[] getRecordFrontSupport() {
        return this.recordFrontSupport;
    }

    public void setRecordFrontSupport(String[] strArr) {
        this.recordFrontSupport = strArr;
    }

    public String[] getRecordRearSupport() {
        return this.recordRearSupport;
    }

    public void setRecordRearSupport(String[] strArr) {
        this.recordRearSupport = strArr;
    }

    public static class AppListBean {
        private List<String> match_android_ver;
        private List<String> match_ios_ver;

        public List<String> getMatch_android_ver() {
            return this.match_android_ver;
        }

        public void setMatch_android_ver(List<String> list) {
            this.match_android_ver = list;
        }

        public List<String> getMatch_ios_ver() {
            return this.match_ios_ver;
        }

        public void setMatch_ios_ver(List<String> list) {
            this.match_ios_ver = list;
        }
    }
}
