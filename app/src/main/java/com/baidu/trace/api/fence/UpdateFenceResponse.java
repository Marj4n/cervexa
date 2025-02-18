package com.baidu.trace.api.fence;

import com.baidu.trace.model.BaseResponse;
import java.util.List;

/* loaded from: classes.dex */
public final class UpdateFenceResponse extends BaseResponse {
    private long a;
    private String b;
    private FenceType c;
    private FenceShape d;
    private String e;
    private List<String> f;

    public UpdateFenceResponse() {
    }

    public UpdateFenceResponse(int i, int i2, String str, long j, String str2, FenceType fenceType) {
        super(i, i2, str);
        this.a = j;
        this.b = str2;
        this.c = fenceType;
    }

    public UpdateFenceResponse(int i, int i2, String str, FenceType fenceType) {
        super(i, i2, str);
        this.c = fenceType;
    }

    public final String getDistrict() {
        return this.e;
    }

    public final List<String> getDistrictList() {
        return this.f;
    }

    public final long getFenceId() {
        return this.a;
    }

    public final String getFenceName() {
        return this.b;
    }

    public final FenceShape getFenceShape() {
        return this.d;
    }

    public final FenceType getFenceType() {
        return this.c;
    }

    public final void setDistrict(String str) {
        this.e = str;
    }

    public final void setDistrictList(List<String> list) {
        this.f = list;
    }

    public final void setFenceId(long j) {
        this.a = j;
    }

    public final void setFenceName(String str) {
        this.b = str;
    }

    public final void setFenceShape(FenceShape fenceShape) {
        this.d = fenceShape;
    }

    public final void setFenceType(FenceType fenceType) {
        this.c = fenceType;
    }

    public final String toString() {
        StringBuilder sb;
        Object obj;
        if (FenceShape.district != this.d) {
            sb = new StringBuilder("UpdateFenceResponse [tag=");
            sb.append(this.tag);
            sb.append(", status=");
            sb.append(this.status);
            sb.append(", message=");
            sb.append(this.message);
            sb.append(", fenceId=");
            sb.append(this.a);
            sb.append(", fenceName=");
            sb.append(this.b);
            sb.append(", fenceType=");
            sb.append(this.c);
            sb.append(", fenceShape=");
            obj = this.d;
        } else {
            sb = new StringBuilder("UpdateFenceResponse [tag=");
            sb.append(this.tag);
            sb.append(", status=");
            sb.append(this.status);
            sb.append(", message=");
            sb.append(this.message);
            sb.append(", fenceId=");
            sb.append(this.a);
            sb.append(", fenceName=");
            sb.append(this.b);
            sb.append(", fenceType=");
            sb.append(this.c);
            sb.append(", fenceShape=");
            sb.append(this.d);
            sb.append(", district=");
            sb.append(this.e);
            sb.append(", districtList=");
            obj = this.f;
        }
        sb.append(obj);
        sb.append("]");
        return sb.toString();
    }
}
