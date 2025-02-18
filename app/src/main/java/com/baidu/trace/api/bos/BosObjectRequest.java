package com.baidu.trace.api.bos;

import com.baidu.trace.model.BaseRequest;

/* loaded from: classes.dex */
public class BosObjectRequest extends BaseRequest {
    private String a;
    private BosObjectType b;

    public BosObjectRequest() {
        this.a = null;
        this.b = BosObjectType.image;
    }

    public BosObjectRequest(int i, long j) {
        super(i, j);
        this.a = null;
        this.b = BosObjectType.image;
    }

    public BosObjectRequest(int i, long j, String str, BosObjectType bosObjectType) {
        super(i, j);
        this.a = null;
        this.b = BosObjectType.image;
        this.a = str;
        this.b = bosObjectType;
    }

    public String getObjectKey() {
        return this.a;
    }

    public BosObjectType getObjectType() {
        return this.b;
    }

    public void setObjectKey(String str) {
        this.a = str;
    }

    public void setObjectType(BosObjectType bosObjectType) {
        this.b = bosObjectType;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("BosObjectRequest{");
        stringBuffer.append("objectKey='");
        stringBuffer.append(this.a);
        stringBuffer.append('\'');
        stringBuffer.append(", objectType=");
        stringBuffer.append(this.b);
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
