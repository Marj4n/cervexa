package com.serenegiant.media;

import android.media.MediaCodec;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: classes2.dex */
public class MediaData {
    int flags;
    ByteBuffer mBuffer;
    long presentationTimeUs;
    int size;

    public MediaData() {
    }

    public MediaData(int i) {
        resize(i);
    }

    public void set(ByteBuffer byteBuffer, int i, long j) {
        set(byteBuffer, 0, i, j, 0);
    }

    public void set(ByteBuffer byteBuffer, int i, int i2, long j, int i3) {
        this.presentationTimeUs = j;
        this.size = i2;
        this.flags = i3;
        resize(i2);
        if (byteBuffer == null || i2 <= i) {
            return;
        }
        byteBuffer.position(i2 + i);
        byteBuffer.flip();
        byteBuffer.position(i);
        this.mBuffer.put(byteBuffer);
        this.mBuffer.flip();
    }

    public void set(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        int i;
        this.presentationTimeUs = bufferInfo.presentationTimeUs;
        this.size = byteBuffer != null ? bufferInfo.size : 0;
        this.flags = bufferInfo.flags;
        int i2 = bufferInfo.offset;
        resize(this.size);
        if (byteBuffer == null || (i = this.size) <= i2) {
            return;
        }
        byteBuffer.position(i + i2);
        byteBuffer.flip();
        byteBuffer.position(i2);
        this.mBuffer.put(byteBuffer);
        this.mBuffer.flip();
    }

    public MediaData resize(int i) {
        ByteBuffer byteBuffer = this.mBuffer;
        if (byteBuffer == null || byteBuffer.capacity() < i) {
            this.mBuffer = ByteBuffer.allocateDirect(i).order(ByteOrder.nativeOrder());
        }
        this.mBuffer.clear();
        return this;
    }

    public void clear() {
        this.flags = 0;
        this.size = 0;
        this.mBuffer.clear();
    }

    public int size() {
        return this.size;
    }

    public long presentationTimeUs() {
        return this.presentationTimeUs;
    }

    public void get(byte[] bArr) throws ArrayIndexOutOfBoundsException {
        if (bArr == null || bArr.length < this.size) {
            throw new ArrayIndexOutOfBoundsException("");
        }
        this.mBuffer.clear();
        this.mBuffer.position(this.size);
        this.mBuffer.flip();
        this.mBuffer.get(bArr);
    }

    public void get(ByteBuffer byteBuffer) throws ArrayIndexOutOfBoundsException {
        if (byteBuffer == null || byteBuffer.remaining() < this.size) {
            throw new ArrayIndexOutOfBoundsException("");
        }
        this.mBuffer.clear();
        this.mBuffer.position(this.size);
        this.mBuffer.flip();
        byteBuffer.put(this.mBuffer);
    }

    public void get(MediaCodec.BufferInfo bufferInfo) {
        bufferInfo.set(0, this.size, this.presentationTimeUs, this.flags);
    }

    public ByteBuffer get() {
        this.mBuffer.clear();
        this.mBuffer.position(this.size);
        this.mBuffer.flip();
        return this.mBuffer;
    }
}
