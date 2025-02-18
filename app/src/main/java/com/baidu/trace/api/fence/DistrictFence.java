package com.baidu.trace.api.fence;

/* loaded from: classes.dex */
public class DistrictFence extends Fence {
    private String f;
    private String g;

    private DistrictFence(long j, String str, String str2, int i, FenceType fenceType, String str3) {
        super(j, str, str2, i, fenceType);
        this.f = str3;
    }

    public static DistrictFence buildServerFence(long j, String str, String str2, int i, String str3) {
        return new DistrictFence(j, str, str2, i, FenceType.server, str3);
    }

    public String getDistrict() {
        return this.g;
    }

    public String getKeyword() {
        return this.f;
    }

    public void setDistrict(String str) {
        this.g = str;
    }

    public void setKeyword(String str) {
        this.f = str;
    }

    @Override // com.baidu.trace.api.fence.Fence
    public String toString() {
        return "DistrictFence [fenceId=" + this.a + ", fenceName=" + this.b + ", fenceType=" + this.e + ", monitoredPerson=" + this.c + ", keyword=" + this.f + ", district=" + this.g + ", denoise=" + this.d + "]";
    }
}
