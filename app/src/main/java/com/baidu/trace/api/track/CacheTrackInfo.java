package com.baidu.trace.api.track;

/* loaded from: classes.dex */
public class CacheTrackInfo {
    private String a;
    private int b;
    private long c;
    private long d;

    public CacheTrackInfo() {
    }

    public CacheTrackInfo(String str, int i, long j, long j2) {
        this.a = str;
        this.b = i;
        this.c = j;
        this.d = j2;
    }

    public CacheTrackInfo(String str, long j, long j2) {
        this.a = str;
        this.c = j;
        this.d = j2;
    }

    public long getEndTime() {
        return this.d;
    }

    public String getEntityName() {
        return this.a;
    }

    public long getStartTime() {
        return this.c;
    }

    public int getTotal() {
        return this.b;
    }

    public void setEndTime(long j) {
        this.d = j;
    }

    public void setEntityName(String str) {
        this.a = str;
    }

    public void setStartTime(long j) {
        this.c = j;
    }

    public void setTotal(int i) {
        this.b = i;
    }

    public String toString() {
        return "CacheTrackInfo [entityName=" + this.a + ", total=" + this.b + ", startTime=" + this.c + ", endTime=" + this.d + "]";
    }
}
