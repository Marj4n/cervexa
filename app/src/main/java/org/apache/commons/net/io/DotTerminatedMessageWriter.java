package org.apache.commons.net.io;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.net.SocketClient;

/* loaded from: classes2.dex */
public final class DotTerminatedMessageWriter extends Writer {
    private static final int __LAST_WAS_CR_STATE = 1;
    private static final int __LAST_WAS_NL_STATE = 2;
    private static final int __NOTHING_SPECIAL_STATE = 0;
    private Writer __output;
    private int __state;

    public DotTerminatedMessageWriter(Writer writer) {
        super(writer);
        this.__output = writer;
        this.__state = 0;
    }

    @Override // java.io.Writer
    public void write(int i) throws IOException {
        synchronized (this.lock) {
            try {
                if (i == 10) {
                    if (this.__state != 1) {
                        this.__output.write(13);
                    }
                    this.__output.write(10);
                    this.__state = 2;
                    return;
                }
                if (i == 13) {
                    this.__state = 1;
                    this.__output.write(13);
                    return;
                }
                if (i == 46 && this.__state == 2) {
                    this.__output.write(46);
                }
                this.__state = 0;
                this.__output.write(i);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // java.io.Writer
    public void write(char[] cArr, int i, int i2) throws IOException {
        synchronized (this.lock) {
            while (true) {
                int i3 = i2 - 1;
                if (i2 > 0) {
                    int i4 = i + 1;
                    write(cArr[i]);
                    i = i4;
                    i2 = i3;
                }
            }
        }
    }

    @Override // java.io.Writer
    public void write(char[] cArr) throws IOException {
        write(cArr, 0, cArr.length);
    }

    @Override // java.io.Writer
    public void write(String str) throws IOException {
        write(str.toCharArray());
    }

    @Override // java.io.Writer
    public void write(String str, int i, int i2) throws IOException {
        write(str.toCharArray(), i, i2);
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        synchronized (this.lock) {
            this.__output.flush();
        }
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        synchronized (this.lock) {
            if (this.__output == null) {
                return;
            }
            if (this.__state == 1) {
                this.__output.write(10);
            } else if (this.__state != 2) {
                this.__output.write(SocketClient.NETASCII_EOL);
            }
            this.__output.write(".\r\n");
            this.__output.flush();
            this.__output = null;
        }
    }
}
