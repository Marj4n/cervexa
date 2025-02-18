package com.serenegiant.io;

import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class ByteBufferOutputStream extends OutputStream {
    private final boolean autoEnlarge;
    private ByteBuffer wrappedBuffer;

    public ByteBufferOutputStream(ByteBuffer byteBuffer, boolean z) {
        this.wrappedBuffer = byteBuffer;
        this.autoEnlarge = z;
    }

    public ByteBuffer toByteBuffer() {
        ByteBuffer duplicate = this.wrappedBuffer.duplicate();
        duplicate.flip();
        return duplicate.asReadOnlyBuffer();
    }

    public void reset() {
        this.wrappedBuffer.rewind();
    }

    public int size() {
        return this.wrappedBuffer.position();
    }

    private void growTo(int i) {
        int capacity = this.wrappedBuffer.capacity() << 1;
        if (capacity - i < 0) {
            capacity = i;
        }
        if (capacity < 0) {
            if (i < 0) {
                throw new OutOfMemoryError();
            }
            capacity = Integer.MAX_VALUE;
        }
        ByteBuffer byteBuffer = this.wrappedBuffer;
        if (byteBuffer.isDirect()) {
            this.wrappedBuffer = ByteBuffer.allocateDirect(capacity);
        } else {
            this.wrappedBuffer = ByteBuffer.allocate(capacity);
        }
        byteBuffer.flip();
        this.wrappedBuffer.put(byteBuffer);
    }

    @Override // java.io.OutputStream
    public void write(int i) {
        try {
            this.wrappedBuffer.put((byte) i);
        } catch (BufferOverflowException e) {
            if (this.autoEnlarge) {
                growTo(this.wrappedBuffer.capacity() * 2);
                write(i);
                return;
            }
            throw e;
        }
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) {
        int i = 0;
        try {
            i = this.wrappedBuffer.position();
            this.wrappedBuffer.put(bArr);
        } catch (BufferOverflowException e) {
            if (this.autoEnlarge) {
                growTo(Math.max(this.wrappedBuffer.capacity() * 2, i + bArr.length));
                write(bArr);
                return;
            }
            throw e;
        }
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) {
        int i3 = 0;
        try {
            i3 = this.wrappedBuffer.position();
            this.wrappedBuffer.put(bArr, i, i2);
        } catch (BufferOverflowException e) {
            if (this.autoEnlarge) {
                growTo(Math.max(this.wrappedBuffer.capacity() * 2, i3 + i2));
                write(bArr, i, i2);
                return;
            }
            throw e;
        }
    }
}
