package org.apache.commons.net.smtp;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.net.io.DotTerminatedMessageWriter;

/* loaded from: classes2.dex */
public class SMTPClient extends SMTP {
    public SMTPClient() {
    }

    public SMTPClient(String str) {
        super(str);
    }

    public boolean completePendingCommand() throws IOException {
        return SMTPReply.isPositiveCompletion(getReply());
    }

    public boolean login(String str) throws IOException {
        return SMTPReply.isPositiveCompletion(helo(str));
    }

    public boolean login() throws IOException {
        String hostName = getLocalAddress().getHostName();
        if (hostName == null) {
            return false;
        }
        return SMTPReply.isPositiveCompletion(helo(hostName));
    }

    public boolean setSender(RelayPath relayPath) throws IOException {
        return SMTPReply.isPositiveCompletion(mail(relayPath.toString()));
    }

    public boolean setSender(String str) throws IOException {
        return SMTPReply.isPositiveCompletion(mail("<" + str + ">"));
    }

    public boolean addRecipient(RelayPath relayPath) throws IOException {
        return SMTPReply.isPositiveCompletion(rcpt(relayPath.toString()));
    }

    public boolean addRecipient(String str) throws IOException {
        return SMTPReply.isPositiveCompletion(rcpt("<" + str + ">"));
    }

    public Writer sendMessageData() throws IOException {
        if (SMTPReply.isPositiveIntermediate(data())) {
            return new DotTerminatedMessageWriter(this._writer);
        }
        return null;
    }

    public boolean sendShortMessageData(String str) throws IOException {
        Writer sendMessageData = sendMessageData();
        if (sendMessageData == null) {
            return false;
        }
        sendMessageData.write(str);
        sendMessageData.close();
        return completePendingCommand();
    }

    public boolean sendSimpleMessage(String str, String str2, String str3) throws IOException {
        if (setSender(str) && addRecipient(str2)) {
            return sendShortMessageData(str3);
        }
        return false;
    }

    public boolean sendSimpleMessage(String str, String[] strArr, String str2) throws IOException {
        if (!setSender(str)) {
            return false;
        }
        boolean z = false;
        for (String str3 : strArr) {
            if (addRecipient(str3)) {
                z = true;
            }
        }
        if (z) {
            return sendShortMessageData(str2);
        }
        return false;
    }

    public boolean logout() throws IOException {
        return SMTPReply.isPositiveCompletion(quit());
    }

    public boolean reset() throws IOException {
        return SMTPReply.isPositiveCompletion(rset());
    }

    public boolean verify(String str) throws IOException {
        int vrfy = vrfy(str);
        return vrfy == 250 || vrfy == 251;
    }

    public String listHelp() throws IOException {
        if (SMTPReply.isPositiveCompletion(help())) {
            return getReplyString();
        }
        return null;
    }

    public String listHelp(String str) throws IOException {
        if (SMTPReply.isPositiveCompletion(help(str))) {
            return getReplyString();
        }
        return null;
    }

    public boolean sendNoOp() throws IOException {
        return SMTPReply.isPositiveCompletion(noop());
    }
}
