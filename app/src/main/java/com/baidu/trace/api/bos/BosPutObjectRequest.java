package com.baidu.trace.api.bos;

import com.jieli.stream.dv.running2.util.IConstant;
import java.io.File;
import java.io.InputStream;

/* loaded from: classes.dex */
public final class BosPutObjectRequest extends BosObjectRequest {
    private File a;
    private InputStream b;
    private byte[] c;
    private String d;
    private long e;

    private BosPutObjectRequest(int i, long j, String str, BosObjectType bosObjectType, File file, InputStream inputStream, byte[] bArr, String str2) {
        super(i, j, str, bosObjectType);
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.e = 0L;
        this.a = file;
        this.b = inputStream;
        this.c = bArr;
        this.d = str2;
    }

    public static BosPutObjectRequest buildByteArrayRequest(int i, long j, String str, BosObjectType bosObjectType, byte[] bArr) {
        return new BosPutObjectRequest(i, j, str, bosObjectType, null, null, bArr, null);
    }

    public static BosPutObjectRequest buildFileRequest(int i, long j, String str, BosObjectType bosObjectType, File file) {
        return new BosPutObjectRequest(i, j, str, bosObjectType, file, null, null, null);
    }

    public static BosPutObjectRequest buildStreamReqeust(int i, long j, String str, BosObjectType bosObjectType, InputStream inputStream) {
        return new BosPutObjectRequest(i, j, str, bosObjectType, null, inputStream, null, null);
    }

    public static BosPutObjectRequest buildStringRequest(int i, long j, String str, BosObjectType bosObjectType, String str2) {
        return new BosPutObjectRequest(i, j, str, bosObjectType, null, null, null, str2);
    }

    public final byte[] getByteArray() {
        return this.c;
    }

    public final File getFile() {
        return this.a;
    }

    public final long getObjectSize() {
        return this.e;
    }

    public final InputStream getStreamData() {
        return this.b;
    }

    public final String getStringData() {
        return this.d;
    }

    public final void setByteArray(byte[] bArr) {
        this.c = bArr;
    }

    public final void setFile(File file) {
        this.a = file;
    }

    public final void setObjectSize(long j) {
        this.e = j;
    }

    public final void setStreamData(InputStream inputStream) {
        this.b = inputStream;
    }

    public final void setStringData(String str) {
        this.d = str;
    }

    @Override // com.baidu.trace.api.bos.BosObjectRequest
    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("BosPutObjectRequest{");
        stringBuffer.append("file=");
        stringBuffer.append(this.a);
        stringBuffer.append(", streamData=");
        stringBuffer.append(this.b);
        stringBuffer.append(", byteArray=");
        if (this.c == null) {
            stringBuffer.append(IConstant.DEFAULT_PATH);
        } else {
            stringBuffer.append('[');
            int i = 0;
            while (i < this.c.length) {
                stringBuffer.append(i == 0 ? "" : ", ");
                stringBuffer.append((int) this.c[i]);
                i++;
            }
            stringBuffer.append(']');
        }
        stringBuffer.append(", stringData='");
        stringBuffer.append(this.d);
        stringBuffer.append('\'');
        stringBuffer.append(", objectSize=");
        stringBuffer.append(this.e);
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
