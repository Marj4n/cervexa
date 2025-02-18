package okhttp3.internal.cache2;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import okio.Buffer;

/* loaded from: classes2.dex */
final class FileOperator {
    private static final int BUFFER_SIZE = 8192;
    private final byte[] byteArray;
    private final ByteBuffer byteBuffer;
    private final FileChannel fileChannel;

    FileOperator(FileChannel fileChannel) {
        byte[] bArr = new byte[8192];
        this.byteArray = bArr;
        this.byteBuffer = ByteBuffer.wrap(bArr);
        this.fileChannel = fileChannel;
    }

    public void write(long j, Buffer buffer, long j2) throws IOException {
        if (j2 < 0 || j2 > buffer.size()) {
            throw new IndexOutOfBoundsException();
        }
        while (j2 > 0) {
            try {
                int min = (int) Math.min(8192L, j2);
                buffer.read(this.byteArray, 0, min);
                this.byteBuffer.limit(min);
                do {
                    j += this.fileChannel.write(this.byteBuffer, j);
                } while (this.byteBuffer.hasRemaining());
                j2 -= min;
            } finally {
                this.byteBuffer.clear();
            }
        }
    }

    public void read(long j, Buffer buffer, long j2) throws IOException {
        if (j2 < 0) {
            throw new IndexOutOfBoundsException();
        }
        while (j2 > 0) {
            try {
                this.byteBuffer.limit((int) Math.min(8192L, j2));
                if (this.fileChannel.read(this.byteBuffer, j) == -1) {
                    throw new EOFException();
                }
                int position = this.byteBuffer.position();
                buffer.write(this.byteArray, 0, position);
                long j3 = position;
                j += j3;
                j2 -= j3;
            } finally {
                this.byteBuffer.clear();
            }
        }
    }
}
