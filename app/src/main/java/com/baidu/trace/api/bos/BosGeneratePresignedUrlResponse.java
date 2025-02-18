package com.baidu.trace.api.bos;

/* loaded from: classes.dex */
public final class BosGeneratePresignedUrlResponse extends BosObjectResponse {
    private String c;

    public BosGeneratePresignedUrlResponse() {
    }

    public BosGeneratePresignedUrlResponse(int i, int i2, String str) {
        super(i, i2, str);
    }

    public final String getUrl() {
        return this.c;
    }

    public final void setUrl(String str) {
        this.c = str;
    }

    @Override // com.baidu.trace.api.bos.BosObjectResponse
    public final String toString() {
        return "BosGeneratePresignedUrlResponse [tag=" + this.tag + ", status=" + this.status + ", message=" + this.message + ", objectType=" + this.a + ", objectKey=" + this.b + ", url=" + this.c + "]";
    }
}
