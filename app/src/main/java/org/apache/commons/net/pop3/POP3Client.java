package org.apache.commons.net.pop3;

import java.io.IOException;
import java.io.Reader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ListIterator;
import java.util.StringTokenizer;
import kotlin.UByte;
import org.apache.commons.net.io.DotTerminatedMessageReader;

/* loaded from: classes2.dex */
public class POP3Client extends POP3 {
    private static POP3MessageInfo __parseStatus(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str);
        if (!stringTokenizer.hasMoreElements()) {
            return null;
        }
        try {
            int parseInt = Integer.parseInt(stringTokenizer.nextToken());
            if (stringTokenizer.hasMoreElements()) {
                return new POP3MessageInfo(parseInt, Integer.parseInt(stringTokenizer.nextToken()));
            }
            return null;
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    private static POP3MessageInfo __parseUID(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str);
        if (!stringTokenizer.hasMoreElements()) {
            return null;
        }
        try {
            int parseInt = Integer.parseInt(stringTokenizer.nextToken());
            if (stringTokenizer.hasMoreElements()) {
                return new POP3MessageInfo(parseInt, stringTokenizer.nextToken());
            }
            return null;
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    public boolean capa() throws IOException {
        if (sendCommand(12) != 0) {
            return false;
        }
        getAdditionalReply();
        return true;
    }

    public boolean login(String str, String str2) throws IOException {
        if (getState() != 0 || sendCommand(0, str) != 0 || sendCommand(1, str2) != 0) {
            return false;
        }
        setState(1);
        return true;
    }

    public boolean login(String str, String str2, String str3) throws IOException, NoSuchAlgorithmException {
        if (getState() != 0) {
            return false;
        }
        byte[] digest = MessageDigest.getInstance("MD5").digest((str2 + str3).getBytes(getCharset()));
        StringBuilder sb = new StringBuilder(128);
        for (byte b : digest) {
            int i = b & UByte.MAX_VALUE;
            if (i <= 15) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(i));
        }
        StringBuilder sb2 = new StringBuilder(256);
        sb2.append(str);
        sb2.append(' ');
        sb2.append(sb.toString());
        if (sendCommand(9, sb2.toString()) != 0) {
            return false;
        }
        setState(1);
        return true;
    }

    public boolean logout() throws IOException {
        if (getState() == 1) {
            setState(2);
        }
        sendCommand(2);
        return this._replyCode == 0;
    }

    public boolean noop() throws IOException {
        return getState() == 1 && sendCommand(7) == 0;
    }

    public boolean deleteMessage(int i) throws IOException {
        return getState() == 1 && sendCommand(6, Integer.toString(i)) == 0;
    }

    public boolean reset() throws IOException {
        return getState() == 1 && sendCommand(8) == 0;
    }

    public POP3MessageInfo status() throws IOException {
        if (getState() == 1 && sendCommand(3) == 0) {
            return __parseStatus(this._lastReplyLine.substring(3));
        }
        return null;
    }

    public POP3MessageInfo listMessage(int i) throws IOException {
        if (getState() == 1 && sendCommand(4, Integer.toString(i)) == 0) {
            return __parseStatus(this._lastReplyLine.substring(3));
        }
        return null;
    }

    public POP3MessageInfo[] listMessages() throws IOException {
        if (getState() != 1 || sendCommand(4) != 0) {
            return null;
        }
        getAdditionalReply();
        int size = this._replyLines.size() - 2;
        POP3MessageInfo[] pOP3MessageInfoArr = new POP3MessageInfo[size];
        ListIterator<String> listIterator = this._replyLines.listIterator(1);
        for (int i = 0; i < size; i++) {
            pOP3MessageInfoArr[i] = __parseStatus(listIterator.next());
        }
        return pOP3MessageInfoArr;
    }

    public POP3MessageInfo listUniqueIdentifier(int i) throws IOException {
        if (getState() == 1 && sendCommand(11, Integer.toString(i)) == 0) {
            return __parseUID(this._lastReplyLine.substring(3));
        }
        return null;
    }

    public POP3MessageInfo[] listUniqueIdentifiers() throws IOException {
        if (getState() != 1 || sendCommand(11) != 0) {
            return null;
        }
        getAdditionalReply();
        int size = this._replyLines.size() - 2;
        POP3MessageInfo[] pOP3MessageInfoArr = new POP3MessageInfo[size];
        ListIterator<String> listIterator = this._replyLines.listIterator(1);
        for (int i = 0; i < size; i++) {
            pOP3MessageInfoArr[i] = __parseUID(listIterator.next());
        }
        return pOP3MessageInfoArr;
    }

    public Reader retrieveMessage(int i) throws IOException {
        if (getState() == 1 && sendCommand(5, Integer.toString(i)) == 0) {
            return new DotTerminatedMessageReader(this._reader);
        }
        return null;
    }

    public Reader retrieveMessageTop(int i, int i2) throws IOException {
        if (i2 < 0 || getState() != 1) {
            return null;
        }
        if (sendCommand(10, Integer.toString(i) + " " + Integer.toString(i2)) != 0) {
            return null;
        }
        return new DotTerminatedMessageReader(this._reader);
    }
}
