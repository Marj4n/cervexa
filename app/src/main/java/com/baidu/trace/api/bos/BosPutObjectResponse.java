package com.baidu.trace.api.bos;

import com.baidubce.services.bos.model.ObjectMetadata;

/* loaded from: classes.dex */
public final class BosPutObjectResponse extends BosObjectResponse {
    private String c;
    private ObjectMetadata d;

    public BosPutObjectResponse() {
        this.d = null;
    }

    public BosPutObjectResponse(int i, int i2, String str) {
        super(i, i2, str);
        this.d = null;
    }

    public final String getETag() {
        return this.c;
    }

    public final ObjectMetadata getMetaData() {
        return this.d;
    }

    public final void setETag(String str) {
        this.c = str;
    }

    public final void setMetaData(ObjectMetadata objectMetadata) {
        this.d = objectMetadata;
    }

    @Override // com.baidu.trace.api.bos.BosObjectResponse
    public final String toString() {
        ObjectMetadata objectMetadata = this.d;
        return "BosPutObjectResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + ", objectType=" + this.a + ", objectKey=" + this.b + ", eTag=" + this.c + ", metaData=" + (objectMetadata != null ? objectMetadata.toString() : "") + "]";
    }
}
