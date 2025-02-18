package com.serenegiant.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

/* loaded from: classes.dex */
public class ByteChannelWrapper implements IReadable, IWritable {
    private final ByteChannel mChannel;

    @Override // com.serenegiant.io.IWritable
    public void flush() throws IOException {
    }

    public ByteChannelWrapper(ByteChannel byteChannel) {
        this.mChannel = byteChannel;
    }

    @Override // com.serenegiant.io.IReadable
    public int read(ByteBuffer byteBuffer) throws IOException {
        return this.mChannel.read(byteBuffer);
    }

    @Override // com.serenegiant.io.IReadable
    public int read(byte[] bArr, int i, int i2) throws IOException {
        return this.mChannel.read(ByteBuffer.wrap(bArr, i, i2));
    }

    @Override // com.serenegiant.io.IReadable
    public int available() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // com.serenegiant.io.IReadable
    public long skip(long j) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // com.serenegiant.io.IWritable
    public void write(ByteBuffer byteBuffer) throws IOException {
        this.mChannel.write(byteBuffer);
    }

    @Override // com.serenegiant.io.IWritable
    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.mChannel.write(ByteBuffer.wrap(bArr, i, i2));
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.mChannel.close();
    }
}
