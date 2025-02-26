package com.baidu.trace.api.bos;

import java.io.ByteArrayOutputStream;

/* loaded from: classes.dex */
public final class BosGetObjectResponse extends BosObjectResponse {
    private String c;
    private ByteArrayOutputStream e;

    public final String getETag() {
        return this.c;
    }


    public final ByteArrayOutputStream getObjectContent() {
        return this.e;
    }

    public final void setETag(String str) {
        this.c = str;
    }

    public final void setObjectContent(ByteArrayOutputStream byteArrayOutputStream) {
        this.e = byteArrayOutputStream;
    }
}
