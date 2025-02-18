package org.apache.commons.net.pop3;

/* loaded from: classes2.dex */
public final class POP3MessageInfo {
    public String identifier;
    public int number;
    public int size;

    public POP3MessageInfo() {
        this(0, null, 0);
    }

    public POP3MessageInfo(int i, int i2) {
        this(i, null, i2);
    }

    public POP3MessageInfo(int i, String str) {
        this(i, str, -1);
    }

    private POP3MessageInfo(int i, String str, int i2) {
        this.number = i;
        this.size = i2;
        this.identifier = str;
    }
}
