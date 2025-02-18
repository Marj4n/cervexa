package com.baidu.trace.api.entity;

import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class FilterCondition {
    private List<String> a;
    private long b;
    private long c;
    private Map<String, String> d;

    public FilterCondition() {
    }

    public FilterCondition(List<String> list, long j, long j2, Map<String, String> map) {
        this.a = list;
        this.b = j;
        this.c = j2;
        this.d = map;
    }

    public long getActiveTime() {
        return this.b;
    }

    public Map<String, String> getColumns() {
        return this.d;
    }

    public List<String> getEntityNames() {
        return this.a;
    }

    public long getInActiveTime() {
        return this.c;
    }

    public void setActiveTime(long j) {
        this.b = j;
    }

    public void setColumns(Map<String, String> map) {
        this.d = map;
    }

    public void setEntityNames(List<String> list) {
        this.a = list;
    }

    public void setInActiveTime(long j) {
        this.c = j;
    }

    public String toString() {
        return "FilterCondition [entityNames=" + this.a + ", activeTime=" + this.b + ", inActiveTime=" + this.c + ", columns=" + this.d + "]";
    }
}
