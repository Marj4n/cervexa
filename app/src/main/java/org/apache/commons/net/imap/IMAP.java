package org.apache.commons.net.imap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.net.SocketClient;
import org.apache.commons.net.io.CRLFLineReader;

/* loaded from: classes2.dex */
public class IMAP extends SocketClient {
    public static final int DEFAULT_PORT = 143;
    public static final IMAPChunkListener TRUE_CHUNK_LISTENER = new IMAPChunkListener() { // from class: org.apache.commons.net.imap.IMAP.1
        @Override // org.apache.commons.net.imap.IMAP.IMAPChunkListener
        public boolean chunkReceived(IMAP imap) {
            return true;
        }
    };
    protected static final String __DEFAULT_ENCODING = "ISO-8859-1";
    private volatile IMAPChunkListener __chunkListener;
    private IMAPState __state;
    protected BufferedWriter __writer;
    private final char[] _initialID = {'A', 'A', 'A', 'A'};
    protected BufferedReader _reader;
    private int _replyCode;
    private final List<String> _replyLines;

    public interface IMAPChunkListener {
        boolean chunkReceived(IMAP imap);
    }

    public enum IMAPState {
        DISCONNECTED_STATE,
        NOT_AUTH_STATE,
        AUTH_STATE,
        LOGOUT_STATE
    }

    public IMAP() {
        setDefaultPort(DEFAULT_PORT);
        this.__state = IMAPState.DISCONNECTED_STATE;
        this._reader = null;
        this.__writer = null;
        this._replyLines = new ArrayList();
        createCommandSupport();
    }

    private void __getReply() throws IOException {
        __getReply(true);
    }

    private void __getReply(boolean z) throws IOException {
        IMAPChunkListener iMAPChunkListener;
        this._replyLines.clear();
        String readLine = this._reader.readLine();
        if (readLine == null) {
            throw new EOFException("Connection closed without indication.");
        }
        this._replyLines.add(readLine);
        if (z) {
            while (IMAPReply.isUntagged(readLine)) {
                int literalCount = IMAPReply.literalCount(readLine);
                boolean z2 = literalCount >= 0;
                while (literalCount >= 0) {
                    String readLine2 = this._reader.readLine();
                    if (readLine2 == null) {
                        throw new EOFException("Connection closed without indication.");
                    }
                    this._replyLines.add(readLine2);
                    literalCount -= readLine2.length() + 2;
                }
                if (z2 && (iMAPChunkListener = this.__chunkListener) != null && iMAPChunkListener.chunkReceived(this)) {
                    fireReplyReceived(3, getReplyString());
                    this._replyLines.clear();
                }
                readLine = this._reader.readLine();
                if (readLine == null) {
                    throw new EOFException("Connection closed without indication.");
                }
                this._replyLines.add(readLine);
            }
            this._replyCode = IMAPReply.getReplyCode(readLine);
        } else {
            this._replyCode = IMAPReply.getUntaggedReplyCode(readLine);
        }
        fireReplyReceived(this._replyCode, getReplyString());
    }

    @Override // org.apache.commons.net.SocketClient
    protected void fireReplyReceived(int i, String str) {
        if (getCommandSupport().getListenerCount() > 0) {
            getCommandSupport().fireReplyReceived(i, getReplyString());
        }
    }

    @Override // org.apache.commons.net.SocketClient
    protected void _connectAction_() throws IOException {
        super._connectAction_();
        this._reader = new CRLFLineReader(new InputStreamReader(this._input_, "ISO-8859-1"));
        this.__writer = new BufferedWriter(new OutputStreamWriter(this._output_, "ISO-8859-1"));
        int soTimeout = getSoTimeout();
        if (soTimeout <= 0) {
            setSoTimeout(this.connectTimeout);
        }
        __getReply(false);
        if (soTimeout <= 0) {
            setSoTimeout(soTimeout);
        }
        setState(IMAPState.NOT_AUTH_STATE);
    }

    protected void setState(IMAPState iMAPState) {
        this.__state = iMAPState;
    }

    public IMAPState getState() {
        return this.__state;
    }

    @Override // org.apache.commons.net.SocketClient
    public void disconnect() throws IOException {
        super.disconnect();
        this._reader = null;
        this.__writer = null;
        this._replyLines.clear();
        setState(IMAPState.DISCONNECTED_STATE);
    }

    private int sendCommandWithID(String str, String str2, String str3) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (str != null) {
            sb.append(str);
            sb.append(' ');
        }
        sb.append(str2);
        if (str3 != null) {
            sb.append(' ');
            sb.append(str3);
        }
        sb.append(SocketClient.NETASCII_EOL);
        String sb2 = sb.toString();
        this.__writer.write(sb2);
        this.__writer.flush();
        fireCommandSent(str2, sb2);
        __getReply();
        return this._replyCode;
    }

    public int sendCommand(String str, String str2) throws IOException {
        return sendCommandWithID(generateCommandID(), str, str2);
    }

    public int sendCommand(String str) throws IOException {
        return sendCommand(str, (String) null);
    }

    public int sendCommand(IMAPCommand iMAPCommand, String str) throws IOException {
        return sendCommand(iMAPCommand.getIMAPCommand(), str);
    }

    public boolean doCommand(IMAPCommand iMAPCommand, String str) throws IOException {
        return IMAPReply.isSuccess(sendCommand(iMAPCommand, str));
    }

    public int sendCommand(IMAPCommand iMAPCommand) throws IOException {
        return sendCommand(iMAPCommand, (String) null);
    }

    public boolean doCommand(IMAPCommand iMAPCommand) throws IOException {
        return IMAPReply.isSuccess(sendCommand(iMAPCommand));
    }

    public int sendData(String str) throws IOException {
        return sendCommandWithID(null, str, null);
    }

    public String[] getReplyStrings() {
        List<String> list = this._replyLines;
        return (String[]) list.toArray(new String[list.size()]);
    }

    public String getReplyString() {
        StringBuilder sb = new StringBuilder(256);
        Iterator<String> it = this._replyLines.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            sb.append(SocketClient.NETASCII_EOL);
        }
        return sb.toString();
    }

    public void setChunkListener(IMAPChunkListener iMAPChunkListener) {
        this.__chunkListener = iMAPChunkListener;
    }

    protected String generateCommandID() {
        String str = new String(this._initialID);
        boolean z = true;
        for (int length = this._initialID.length - 1; z && length >= 0; length--) {
            char[] cArr = this._initialID;
            if (cArr[length] == 'Z') {
                cArr[length] = 'A';
            } else {
                cArr[length] = (char) (cArr[length] + 1);
                z = false;
            }
        }
        return str;
    }
}
