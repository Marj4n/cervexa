package org.apache.commons.net.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/* loaded from: classes2.dex */
public final class CRLFLineReader extends BufferedReader {
    private static final char CR = '\r';
    private static final char LF = '\n';

    public CRLFLineReader(Reader reader) {
        super(reader);
    }

    @Override // java.io.BufferedReader
    public String readLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        synchronized (this.lock) {
            boolean z = false;
            while (true) {
                int read = read();
                if (read == -1) {
                    String sb2 = sb.toString();
                    if (sb2.length() == 0) {
                        return null;
                    }
                    return sb2;
                }
                if (z && read == 10) {
                    return sb.substring(0, sb.length() - 1);
                }
                z = read == 13;
                sb.append((char) read);
            }
        }
    }
}
