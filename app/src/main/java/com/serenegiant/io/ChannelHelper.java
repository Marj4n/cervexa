package com.serenegiant.io;

import com.bumptech.glide.load.Key;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;

/* loaded from: classes.dex */
public class ChannelHelper {
    private static final Charset UTF8 = Charset.forName(Key.STRING_CHARSET_NAME);

    public static boolean readBoolean(ByteChannel byteChannel) throws IOException {
        return readBoolean(byteChannel, null);
    }

    public static boolean readBoolean(ByteChannel byteChannel, ByteBuffer byteBuffer) throws IOException {
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, 1);
        if (byteChannel.read(checkBuffer) != 1) {
            throw new IOException();
        }
        checkBuffer.clear();
        return checkBuffer.get() != 0;
    }

    public static byte readByte(ByteChannel byteChannel) throws IOException {
        return readByte(byteChannel, null);
    }

    public static byte readByte(ByteChannel byteChannel, ByteBuffer byteBuffer) throws IOException {
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, 1);
        if (byteChannel.read(checkBuffer) != 1) {
            throw new IOException();
        }
        checkBuffer.clear();
        return checkBuffer.get();
    }

    public static char readChar(ByteChannel byteChannel) throws IOException {
        return readChar(byteChannel, null);
    }

    public static char readChar(ByteChannel byteChannel, ByteBuffer byteBuffer) throws IOException {
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, 2);
        if (byteChannel.read(checkBuffer) != 2) {
            throw new IOException();
        }
        checkBuffer.clear();
        return checkBuffer.getChar();
    }

    public static short readShort(ByteChannel byteChannel) throws IOException {
        return readShort(byteChannel, null);
    }

    public static short readShort(ByteChannel byteChannel, ByteBuffer byteBuffer) throws IOException {
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, 2);
        if (byteChannel.read(checkBuffer) != 2) {
            throw new IOException();
        }
        checkBuffer.clear();
        return checkBuffer.getShort();
    }

    public static int readInt(ByteChannel byteChannel) throws IOException {
        return readInt(byteChannel, null);
    }

    public static int readInt(ByteChannel byteChannel, ByteBuffer byteBuffer) throws IOException {
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, 4);
        if (byteChannel.read(checkBuffer) != 4) {
            throw new IOException();
        }
        checkBuffer.clear();
        return checkBuffer.getInt();
    }

    public static long readLong(ByteChannel byteChannel) throws IOException {
        return readLong(byteChannel, null);
    }

    public static long readLong(ByteChannel byteChannel, ByteBuffer byteBuffer) throws IOException {
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, 8);
        if (byteChannel.read(checkBuffer) != 8) {
            throw new IOException();
        }
        checkBuffer.clear();
        return checkBuffer.getLong();
    }

    public static float readFloat(ByteChannel byteChannel) throws IOException {
        ByteBuffer allocate = ByteBuffer.allocate(4);
        if (byteChannel.read(allocate) != 4) {
            throw new IOException();
        }
        allocate.clear();
        return allocate.getFloat();
    }

    public static float readFloat(ByteChannel byteChannel, ByteBuffer byteBuffer) throws IOException {
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, 4);
        if (byteChannel.read(checkBuffer) != 4) {
            throw new IOException();
        }
        checkBuffer.clear();
        return checkBuffer.getFloat();
    }

    public static double readDouble(ByteChannel byteChannel) throws IOException {
        return readDouble(byteChannel, null);
    }

    public static double readDouble(ByteChannel byteChannel, ByteBuffer byteBuffer) throws IOException {
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, 8);
        if (byteChannel.read(checkBuffer) != 8) {
            throw new IOException();
        }
        checkBuffer.clear();
        return checkBuffer.getDouble();
    }

    public static String readString(ByteChannel byteChannel) throws IOException {
        int readInt = readInt(byteChannel);
        byte[] bArr = new byte[readInt];
        if (byteChannel.read(ByteBuffer.wrap(bArr)) != readInt) {
            throw new IOException();
        }
        return new String(bArr, UTF8);
    }

    public static boolean[] readBooleanArray(ByteChannel byteChannel) throws IOException {
        int readInt = readInt(byteChannel);
        ByteBuffer allocate = ByteBuffer.allocate(readInt);
        if (byteChannel.read(allocate) != readInt) {
            throw new IOException();
        }
        allocate.clear();
        boolean[] zArr = new boolean[readInt];
        for (int i = 0; i < readInt; i++) {
            zArr[i] = allocate.get() != 0;
        }
        return zArr;
    }

    public static byte[] readByteArray(ByteChannel byteChannel) throws IOException {
        int readInt = readInt(byteChannel);
        byte[] bArr = new byte[readInt];
        if (byteChannel.read(ByteBuffer.wrap(bArr)) == readInt) {
            return bArr;
        }
        throw new IOException();
    }

    public static char[] readCharArray(ByteChannel byteChannel) throws IOException {
        int readInt = readInt(byteChannel);
        int i = readInt * 2;
        ByteBuffer order = ByteBuffer.allocate(i).order(ByteOrder.BIG_ENDIAN);
        if (byteChannel.read(order) != i) {
            throw new IOException();
        }
        order.clear();
        CharBuffer asCharBuffer = order.asCharBuffer();
        if (asCharBuffer.hasArray()) {
            return asCharBuffer.array();
        }
        char[] cArr = new char[readInt];
        asCharBuffer.get(cArr);
        return cArr;
    }

    public static short[] readShortArray(ByteChannel byteChannel) throws IOException {
        int readInt = readInt(byteChannel);
        int i = readInt * 2;
        ByteBuffer order = ByteBuffer.allocate(i).order(ByteOrder.BIG_ENDIAN);
        if (byteChannel.read(order) != i) {
            throw new IOException();
        }
        order.clear();
        ShortBuffer asShortBuffer = order.asShortBuffer();
        if (asShortBuffer.hasArray()) {
            return asShortBuffer.array();
        }
        short[] sArr = new short[readInt];
        asShortBuffer.get(sArr);
        return sArr;
    }

    public static int[] readIntArray(ByteChannel byteChannel) throws IOException {
        int readInt = readInt(byteChannel);
        int i = readInt * 4;
        ByteBuffer order = ByteBuffer.allocate(i).order(ByteOrder.BIG_ENDIAN);
        if (byteChannel.read(order) != i) {
            throw new IOException();
        }
        order.clear();
        IntBuffer asIntBuffer = order.asIntBuffer();
        if (asIntBuffer.hasArray()) {
            return asIntBuffer.array();
        }
        int[] iArr = new int[readInt];
        asIntBuffer.get(iArr);
        return iArr;
    }

    public static long[] readLongArray(ByteChannel byteChannel) throws IOException {
        int readInt = readInt(byteChannel);
        int i = readInt * 8;
        ByteBuffer order = ByteBuffer.allocate(i).order(ByteOrder.BIG_ENDIAN);
        if (byteChannel.read(order) != i) {
            throw new IOException();
        }
        order.clear();
        LongBuffer asLongBuffer = order.asLongBuffer();
        if (asLongBuffer.hasArray()) {
            return asLongBuffer.array();
        }
        long[] jArr = new long[readInt];
        asLongBuffer.get(jArr);
        return jArr;
    }

    public static float[] readFloatArray(ByteChannel byteChannel) throws IOException {
        int readInt = readInt(byteChannel);
        int i = readInt * 4;
        ByteBuffer order = ByteBuffer.allocate(i).order(ByteOrder.BIG_ENDIAN);
        if (byteChannel.read(order) != i) {
            throw new IOException();
        }
        order.clear();
        FloatBuffer asFloatBuffer = order.asFloatBuffer();
        if (asFloatBuffer.hasArray()) {
            return asFloatBuffer.array();
        }
        float[] fArr = new float[readInt];
        asFloatBuffer.get(fArr);
        return fArr;
    }

    public static double[] readDoubleArray(ByteChannel byteChannel) throws IOException {
        int readInt = readInt(byteChannel);
        int i = readInt * 8;
        ByteBuffer order = ByteBuffer.allocate(i).order(ByteOrder.BIG_ENDIAN);
        if (byteChannel.read(order) != i) {
            throw new IOException();
        }
        order.clear();
        DoubleBuffer asDoubleBuffer = order.asDoubleBuffer();
        if (asDoubleBuffer.hasArray()) {
            return asDoubleBuffer.array();
        }
        double[] dArr = new double[readInt];
        asDoubleBuffer.get(dArr);
        return dArr;
    }

    public static ByteBuffer readByteBuffer(ByteChannel byteChannel) throws IOException {
        return readByteBuffer(byteChannel, null, true);
    }

    public static ByteBuffer readByteBuffer(ByteChannel byteChannel, ByteBuffer byteBuffer) throws IOException {
        return readByteBuffer(byteChannel, byteBuffer, false);
    }

    public static ByteBuffer readByteBuffer(ByteChannel byteChannel, ByteBuffer byteBuffer, boolean z) throws IOException {
        int readInt = readInt(byteChannel);
        int position = byteBuffer != null ? byteBuffer.position() : 0;
        if (byteBuffer == null || byteBuffer.remaining() < readInt) {
            if (!z) {
                throw new IOException();
            }
            if (byteBuffer == null) {
                byteBuffer = ByteBuffer.allocateDirect(readInt);
            } else {
                ByteBuffer allocateDirect = ByteBuffer.allocateDirect(byteBuffer.limit() + readInt);
                byteBuffer.flip();
                allocateDirect.put(byteBuffer);
                byteBuffer = allocateDirect;
            }
        }
        int i = position + readInt;
        byteBuffer.limit(i);
        if (byteChannel.read(byteBuffer) != readInt) {
            throw new IOException();
        }
        byteBuffer.position(position);
        byteBuffer.limit(i);
        return byteBuffer;
    }

    public static void write(ByteChannel byteChannel, boolean z) throws IOException {
        write(byteChannel, z, (ByteBuffer) null);
    }

    public static void write(ByteChannel byteChannel, boolean z, ByteBuffer byteBuffer) throws IOException {
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, 1);
        checkBuffer.put(z ? (byte) 1 : (byte) 0);
        checkBuffer.flip();
        byteChannel.write(checkBuffer);
    }

    public static void write(ByteChannel byteChannel, byte b) throws IOException {
        write(byteChannel, b, (ByteBuffer) null);
    }

    public static void write(ByteChannel byteChannel, byte b, ByteBuffer byteBuffer) throws IOException {
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, 1);
        checkBuffer.put(b);
        checkBuffer.flip();
        byteChannel.write(checkBuffer);
    }

    public static void write(ByteChannel byteChannel, char c) throws IOException {
        write(byteChannel, c, (ByteBuffer) null);
    }

    public static void write(ByteChannel byteChannel, char c, ByteBuffer byteBuffer) throws IOException {
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, 2);
        checkBuffer.putChar(c);
        checkBuffer.flip();
        byteChannel.write(checkBuffer);
    }

    public static void write(ByteChannel byteChannel, short s) throws IOException {
        write(byteChannel, s, (ByteBuffer) null);
    }

    public static void write(ByteChannel byteChannel, short s, ByteBuffer byteBuffer) throws IOException {
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, 2);
        checkBuffer.putShort(s);
        checkBuffer.flip();
        byteChannel.write(checkBuffer);
    }

    public static void write(ByteChannel byteChannel, int i) throws IOException {
        write(byteChannel, i, (ByteBuffer) null);
    }

    public static void write(ByteChannel byteChannel, int i, ByteBuffer byteBuffer) throws IOException {
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, 4);
        checkBuffer.putInt(i);
        checkBuffer.flip();
        byteChannel.write(checkBuffer);
    }

    public static void write(ByteChannel byteChannel, long j) throws IOException {
        write(byteChannel, j, (ByteBuffer) null);
    }

    public static void write(ByteChannel byteChannel, long j, ByteBuffer byteBuffer) throws IOException {
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, 8);
        checkBuffer.putLong(j);
        checkBuffer.flip();
        byteChannel.write(checkBuffer);
    }

    public static void write(ByteChannel byteChannel, float f) throws IOException {
        write(byteChannel, f, (ByteBuffer) null);
    }

    public static void write(ByteChannel byteChannel, float f, ByteBuffer byteBuffer) throws IOException {
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, 4);
        checkBuffer.putFloat(f);
        checkBuffer.flip();
        byteChannel.write(checkBuffer);
    }

    public static void write(ByteChannel byteChannel, double d) throws IOException {
        write(byteChannel, d, (ByteBuffer) null);
    }

    public static void write(ByteChannel byteChannel, double d, ByteBuffer byteBuffer) throws IOException {
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, 8);
        checkBuffer.putDouble(d);
        checkBuffer.flip();
        byteChannel.write(checkBuffer);
    }

    public static void write(ByteChannel byteChannel, String str) throws IOException {
        byte[] bytes = str.getBytes(UTF8);
        write(byteChannel, bytes.length);
        byteChannel.write(ByteBuffer.wrap(bytes));
    }

    public static void write(ByteChannel byteChannel, boolean[] zArr) throws IOException {
        write(byteChannel, zArr, (ByteBuffer) null);
    }

    public static void write(ByteChannel byteChannel, boolean[] zArr, ByteBuffer byteBuffer) throws IOException {
        int length = zArr.length;
        write(byteChannel, length, byteBuffer);
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, length);
        for (boolean z : zArr) {
            checkBuffer.put(z ? (byte) 1 : (byte) 0);
        }
        checkBuffer.flip();
        byteChannel.write(checkBuffer);
    }

    public static void write(ByteChannel byteChannel, byte[] bArr) throws IOException {
        write(byteChannel, bArr.length);
        byteChannel.write(ByteBuffer.wrap(bArr));
    }

    public static void write(ByteChannel byteChannel, char[] cArr) throws IOException {
        write(byteChannel, cArr, (ByteBuffer) null);
    }

    public static void write(ByteChannel byteChannel, char[] cArr, ByteBuffer byteBuffer) throws IOException {
        int length = cArr.length;
        write(byteChannel, length, byteBuffer);
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, length * 2);
        for (char c : cArr) {
            checkBuffer.putChar(c);
        }
        checkBuffer.flip();
        byteChannel.write(checkBuffer);
    }

    public static void write(ByteChannel byteChannel, short[] sArr) throws IOException {
        write(byteChannel, sArr, (ByteBuffer) null);
    }

    public static void write(ByteChannel byteChannel, short[] sArr, ByteBuffer byteBuffer) throws IOException {
        int length = sArr.length;
        write(byteChannel, length, byteBuffer);
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, length * 2);
        for (short s : sArr) {
            checkBuffer.putShort(s);
        }
        checkBuffer.flip();
        byteChannel.write(checkBuffer);
    }

    public static void write(ByteChannel byteChannel, int[] iArr) throws IOException {
        write(byteChannel, iArr, (ByteBuffer) null);
    }

    public static void write(ByteChannel byteChannel, int[] iArr, ByteBuffer byteBuffer) throws IOException {
        int length = iArr.length;
        write(byteChannel, length, byteBuffer);
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, length * 4);
        for (int i : iArr) {
            checkBuffer.putInt(i);
        }
        checkBuffer.flip();
        byteChannel.write(checkBuffer);
    }

    public static void write(ByteChannel byteChannel, long[] jArr) throws IOException {
        write(byteChannel, jArr, (ByteBuffer) null);
    }

    public static void write(ByteChannel byteChannel, long[] jArr, ByteBuffer byteBuffer) throws IOException {
        int length = jArr.length;
        write(byteChannel, length, byteBuffer);
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, length * 8);
        for (long j : jArr) {
            checkBuffer.putLong(j);
        }
        checkBuffer.flip();
        byteChannel.write(checkBuffer);
    }

    public static void write(ByteChannel byteChannel, float[] fArr) throws IOException {
        write(byteChannel, fArr, (ByteBuffer) null);
    }

    public static void write(ByteChannel byteChannel, float[] fArr, ByteBuffer byteBuffer) throws IOException {
        int length = fArr.length;
        write(byteChannel, length, byteBuffer);
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, length * 4);
        for (float f : fArr) {
            checkBuffer.putFloat(f);
        }
        checkBuffer.flip();
        byteChannel.write(checkBuffer);
    }

    public static void write(ByteChannel byteChannel, double[] dArr) throws IOException {
        write(byteChannel, dArr, (ByteBuffer) null);
    }

    public static void write(ByteChannel byteChannel, double[] dArr, ByteBuffer byteBuffer) throws IOException {
        int length = dArr.length;
        write(byteChannel, length, byteBuffer);
        ByteBuffer checkBuffer = checkBuffer(byteBuffer, length * 8);
        for (double d : dArr) {
            checkBuffer.putDouble(d);
        }
        checkBuffer.flip();
        byteChannel.write(checkBuffer);
    }

    public static void write(ByteChannel byteChannel, ByteBuffer byteBuffer) throws IOException {
        write(byteChannel, byteBuffer.remaining());
        byteChannel.write(byteBuffer);
    }

    private static ByteBuffer checkBuffer(ByteBuffer byteBuffer, int i) {
        if (byteBuffer == null || byteBuffer.capacity() < i) {
            byteBuffer = ByteBuffer.allocateDirect(i);
        }
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        byteBuffer.clear();
        byteBuffer.limit(i);
        return byteBuffer;
    }
}
