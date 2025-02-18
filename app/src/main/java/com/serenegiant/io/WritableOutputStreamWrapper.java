package com.serenegiant.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class WritableOutputStreamWrapper extends OutputStream implements IWritable {
    private final OutputStream mParent;

    public WritableOutputStreamWrapper(OutputStream outputStream) {
        if (outputStream == null) {
            throw null;
        }
        this.mParent = outputStream;
    }

    @Override // com.serenegiant.io.IWritable
    public void write(ByteBuffer byteBuffer) throws IOException {
        int remaining = byteBuffer.remaining();
        if (remaining > 0) {
            byte[] bArr = new byte[remaining];
            byteBuffer.get(bArr);
            this.mParent.write(bArr, 0, remaining);
        }
    }

    @Override // java.io.OutputStream
    public void write(int i) throws IOException {
        this.mParent.write(i);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        this.mParent.write(bArr);
    }

    @Override // java.io.OutputStream, com.serenegiant.io.IWritable
    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.mParent.write(bArr, i, i2);
    }

    @Override // java.io.OutputStream, java.io.Flushable, com.serenegiant.io.IWritable
    public void flush() throws IOException {
        this.mParent.flush();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.mParent.close();
    }
}
