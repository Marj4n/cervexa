package com.baidu.trace.api.fence;

/* loaded from: classes.dex */
public abstract class Fence {
    protected long a;
    protected String b;
    protected String c;
    protected int d;
    protected FenceType e;

    protected Fence() {
    }

    protected Fence(long j, String str, String str2, int i, FenceType fenceType) {
        this.a = j;
        this.b = str;
        this.e = fenceType;
        this.d = i;
        this.c = str2;
    }

    public int getDenoise() {
        return this.d;
    }

    public long getFenceId() {
        return this.a;
    }

    public String getFenceName() {
        return this.b;
    }

    public FenceType getFenceType() {
        return this.e;
    }

    public String getMonitoredPerson() {
        return this.c;
    }

    public void setDenoise(int i) {
        this.d = i;
    }

    public void setFenceId(long j) {
        this.a = j;
    }

    public void setFenceName(String str) {
        this.b = str;
    }

    public void setMonitoredPerson(String str) {
        this.c = str;
    }

    public String toString() {
        return "Fence [fenceId=" + this.a + ", fenceName=" + this.b + ", monitoredPerson= " + this.c + ", denoise=" + this.d + ", fenceType=" + this.e + "]";
    }
}
