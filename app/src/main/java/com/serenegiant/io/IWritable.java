package com.serenegiant.io;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public interface IWritable extends Closeable {
    void flush() throws IOException;

    void write(ByteBuffer byteBuffer) throws IOException;

    void write(byte[] bArr, int i, int i2) throws IOException;
}
