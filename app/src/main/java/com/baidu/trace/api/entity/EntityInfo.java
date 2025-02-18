package com.baidu.trace.api.entity;

import java.util.Map;

/* loaded from: classes.dex */
public final class EntityInfo {
    private String a;
    private String b;
    private String c;
    private String d;
    private Map<String, String> e;
    private LatestLocation f;

    public EntityInfo() {
    }

    public EntityInfo(String str, String str2, String str3, String str4, Map<String, String> map, LatestLocation latestLocation) {
        this.a = str;
        this.b = str2;
        this.c = str3;
        this.d = str4;
        this.e = map;
        this.f = latestLocation;
    }

    public final Map<String, String> getColumns() {
        return this.e;
    }

    public final String getCreateTime() {
        return this.d;
    }

    public final String getEntityDesc() {
        return this.b;
    }

    public final String getEntityName() {
        return this.a;
    }

    public final LatestLocation getLatestLocation() {
        return this.f;
    }

    public final String getModifyTime() {
        return this.c;
    }

    public final void setColumns(Map<String, String> map) {
        this.e = map;
    }

    public final void setCreateTime(String str) {
        this.d = str;
    }

    public final void setEntityDesc(String str) {
        this.b = str;
    }

    public final void setEntityName(String str) {
        this.a = str;
    }

    public final void setLatestLocation(LatestLocation latestLocation) {
        this.f = latestLocation;
    }

    public final void setModifyTime(String str) {
        this.c = str;
    }

    public final String toString() {
        return "EntityInfo [entityName=" + this.a + ", entityDesc=" + this.b + ", modifyTime=" + this.c + ", createTime=" + this.d + ", columns=" + this.e + ", latestLocation=" + this.f + "]";
    }
}
