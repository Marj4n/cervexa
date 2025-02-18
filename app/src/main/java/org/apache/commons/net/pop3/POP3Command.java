package org.apache.commons.net.pop3;

/* loaded from: classes2.dex */
public final class POP3Command {
    public static final int APOP = 9;
    public static final int AUTH = 13;
    public static final int CAPA = 12;
    public static final int DELE = 6;
    public static final int LIST = 4;
    public static final int NOOP = 7;
    public static final int PASS = 1;
    public static final int QUIT = 2;
    public static final int RETR = 5;
    public static final int RSET = 8;
    public static final int STAT = 3;
    public static final int TOP = 10;
    public static final int UIDL = 11;
    public static final int USER = 0;
    private static final int _NEXT_ = 14;
    static final String[] _commands;

    static {
        String[] strArr = {"USER", "PASS", "QUIT", "STAT", "LIST", "RETR", "DELE", "NOOP", "RSET", "APOP", "TOP", "UIDL", "CAPA", "AUTH"};
        _commands = strArr;
        if (strArr.length != 14) {
            throw new RuntimeException("Error in array definition");
        }
    }

    private POP3Command() {
    }

    public static final String getCommand(int i) {
        return _commands[i];
    }
}
