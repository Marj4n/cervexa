package org.apache.commons.net.telnet;

/* loaded from: classes2.dex */
public class InvalidTelnetOptionException extends Exception {
    private static final long serialVersionUID = -2516777155928793597L;
    private final String msg;
    private final int optionCode;

    public InvalidTelnetOptionException(String str, int i) {
        this.optionCode = i;
        this.msg = str;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return this.msg + ": " + this.optionCode;
    }
}
