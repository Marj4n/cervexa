package com.baidu.trace.api.track;

import com.baidu.trace.model.BaseResponse;

/* loaded from: classes.dex */
public class AddPointResponse extends BaseResponse {
    public AddPointResponse() {
    }

    public AddPointResponse(int i) {
        this.tag = i;
    }

    public AddPointResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("AddPointResponse{");
        stringBuffer.append("tag=");
        stringBuffer.append(this.tag);
        stringBuffer.append(", status=");
        stringBuffer.append(this.status);
        stringBuffer.append(", message=");
        stringBuffer.append(this.message);
        stringBuffer.append('\'');
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
