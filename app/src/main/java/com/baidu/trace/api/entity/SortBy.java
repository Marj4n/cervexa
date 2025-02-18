package com.baidu.trace.api.entity;

import com.baidu.trace.model.SortType;

/* loaded from: classes.dex */
public class SortBy {
    private String a;
    private SortType b;

    private SortBy(String str, SortType sortType) {
        this.a = str;
        this.b = sortType;
    }

    public static SortBy buildCustomKey(String str, SortType sortType) {
        return new SortBy(str, sortType);
    }

    public static SortBy buildEntityDesc(SortType sortType) {
        return new SortBy("entity_desc", sortType);
    }

    public static SortBy buildEntityName(SortType sortType) {
        return new SortBy("entity_name", sortType);
    }

    public static SortBy buildLocTime(SortType sortType) {
        return new SortBy("loc_time", sortType);
    }

    public String getFieldName() {
        return this.a;
    }

    public SortType getSortType() {
        return this.b;
    }

    public void setSortType(SortType sortType) {
        this.b = sortType;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("SortBy{");
        stringBuffer.append("fieldName='");
        stringBuffer.append(this.a);
        stringBuffer.append('\'');
        stringBuffer.append(", sortType=");
        stringBuffer.append(this.b);
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
