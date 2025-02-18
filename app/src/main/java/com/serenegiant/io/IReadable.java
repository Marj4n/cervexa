package com.serenegiant.io;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public interface IReadable extends Closeable {
    int available() throws IOException;

    int read(ByteBuffer byteBuffer) throws IOException;

    int read(byte[] bArr, int i, int i2) throws IOException;

    long skip(long j) throws IOException;
}
