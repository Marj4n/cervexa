package com.baidu.trace.api.bos;

import com.baidubce.services.bos.model.ObjectMetadata;
import java.io.ByteArrayOutputStream;

/* loaded from: classes.dex */
public final class BosGetObjectResponse extends BosObjectResponse {
    private String c;
    private ObjectMetadata d;
    private ByteArrayOutputStream e;

    public BosGetObjectResponse() {
        this.d = null;
        this.e = null;
    }

    public BosGetObjectResponse(int i, int i2, String str) {
        super(i, i2, str);
        this.d = null;
        this.e = null;
    }

    public final String getETag() {
        return this.c;
    }

    public final ObjectMetadata getMetaData() {
        return this.d;
    }

    public final ByteArrayOutputStream getObjectContent() {
        return this.e;
    }

    public final void setETag(String str) {
        this.c = str;
    }

    public final void setMetaData(ObjectMetadata objectMetadata) {
        this.d = objectMetadata;
    }

    public final void setObjectContent(ByteArrayOutputStream byteArrayOutputStream) {
        this.e = byteArrayOutputStream;
    }

    @Override // com.baidu.trace.api.bos.BosObjectResponse
    public final String toString() {
        ObjectMetadata objectMetadata = this.d;
        String objectMetadata2 = objectMetadata != null ? objectMetadata.toString() : "";
        ByteArrayOutputStream byteArrayOutputStream = this.e;
        return "BosGetObjectResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + ", objectType=" + this.a + ", objectKey=" + this.b + ", eTag=" + this.c + ", metaData=" + objectMetadata2 + ", objectContent size=" + (byteArrayOutputStream != null ? byteArrayOutputStream.size() : 0) + "]";
    }
}
