package com.baidu.trace.api.bos;

import com.baidu.trace.model.BaseResponse;

/* loaded from: classes.dex */
public class BosObjectResponse extends BaseResponse {
    protected BosObjectType a;
    protected String b;

    public BosObjectResponse() {
        this.a = BosObjectType.image;
        this.b = "";
    }

    public BosObjectResponse(int i, int i2, String str) {
        super(i, i2, str);
        this.a = BosObjectType.image;
        this.b = "";
    }

    public String getObjectKey() {
        return this.b;
    }

    public BosObjectType getObjectType() {
        return this.a;
    }

    public void setObjectKey(String str) {
        this.b = str;
    }

    public void setObjectType(BosObjectType bosObjectType) {
        this.a = bosObjectType;
    }

    public String toString() {
        return "BosObjectResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + ", objectType=" + this.a + ", objectKey=" + this.b + "]";
    }
}
