package com.baidu.trace.api.entity;

import java.util.List;

/* loaded from: classes.dex */
public final class PolygonSearchResponse extends CommonResponse {
    public PolygonSearchResponse() {
    }

    public PolygonSearchResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public PolygonSearchResponse(int i, int i2, String str, int i3, int i4, List<EntityInfo> list) {
        super(i, i2, str, i3, i4, list);
    }

    @Override // com.baidu.trace.api.entity.CommonResponse
    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("PolygonSearchResponse{");
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
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
