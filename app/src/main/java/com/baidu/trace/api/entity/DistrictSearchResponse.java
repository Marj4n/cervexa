package com.baidu.trace.api.entity;

import java.util.List;

/* loaded from: classes.dex */
public final class DistrictSearchResponse extends CommonResponse {
    private List<String> d;

    public DistrictSearchResponse() {
    }

    public DistrictSearchResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public DistrictSearchResponse(int i, int i2, String str, int i3, int i4, List<EntityInfo> list) {
        super(i, i2, str, i3, i4, list);
    }

    public final List<String> getDistrictList() {
        return this.d;
    }

    public final void setDistrictList(List<String> list) {
        this.d = list;
    }

    @Override // com.baidu.trace.api.entity.CommonResponse
    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("DistrictSearchResponse{");
        stringBuffer.append("tag=");
        stringBuffer.append(this.tag);
        stringBuffer.append(", status=");
        stringBuffer.append(this.status);
        stringBuffer.append(", message='");
        stringBuffer.append(this.message);
        stringBuffer.append('\'');
        stringBuffer.append(", total=");
        stringBuffer.append(this.a);
        stringBuffer.append(", size=");
        stringBuffer.append(this.b);
        stringBuffer.append(", entities=");
        stringBuffer.append(this.c);
        stringBuffer.append(", districtList=");
        stringBuffer.append(this.d);
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
