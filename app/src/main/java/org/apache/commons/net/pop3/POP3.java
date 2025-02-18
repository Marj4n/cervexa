package org.apache.commons.net.pop3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.net.MalformedServerReplyException;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ProtocolCommandSupport;
import org.apache.commons.net.SocketClient;
import org.apache.commons.net.io.CRLFLineReader;

/* loaded from: classes2.dex */
public class POP3 extends SocketClient {
    public static final int AUTHORIZATION_STATE = 0;
    public static final int DEFAULT_PORT = 110;
    public static final int DISCONNECTED_STATE = -1;
    public static final int TRANSACTION_STATE = 1;
    public static final int UPDATE_STATE = 2;
    static final String _DEFAULT_ENCODING = "ISO-8859-1";
    static final String _ERROR = "-ERR";
    static final String _OK = "+OK";
    static final String _OK_INT = "+ ";
    private int __popState;
    protected ProtocolCommandSupport _commandSupport_;
    String _lastReplyLine;
    BufferedReader _reader;
    int _replyCode;
    List<String> _replyLines;
    BufferedWriter _writer;

    public POP3() {
        setDefaultPort(110);
        this.__popState = -1;
        this._reader = null;
        this._writer = null;
        this._replyLines = new ArrayList();
        this._commandSupport_ = new ProtocolCommandSupport(this);
    }

    private void __getReply() throws IOException {
        this._replyLines.clear();
        String readLine = this._reader.readLine();
        if (readLine == null) {
            throw new EOFException("Connection closed without indication.");
        }
        if (readLine.startsWith(_OK)) {
            this._replyCode = 0;
        } else if (readLine.startsWith(_ERROR)) {
            this._replyCode = 1;
        } else if (readLine.startsWith(_OK_INT)) {
            this._replyCode = 2;
        } else {
            throw new MalformedServerReplyException("Received invalid POP3 protocol response from server." + readLine);
        }
        this._replyLines.add(readLine);
        this._lastReplyLine = readLine;
        fireReplyReceived(this._replyCode, getReplyString());
    }

    @Override // org.apache.commons.net.SocketClient
    protected void _connectAction_() throws IOException {
        super._connectAction_();
        this._reader = new CRLFLineReader(new InputStreamReader(this._input_, "ISO-8859-1"));
        this._writer = new BufferedWriter(new OutputStreamWriter(this._output_, "ISO-8859-1"));
        __getReply();
        setState(0);
    }

    public void setState(int i) {
        this.__popState = i;
    }

    public int getState() {
        return this.__popState;
    }

    public void getAdditionalReply() throws IOException {
        String readLine = this._reader.readLine();
        while (readLine != null) {
            this._replyLines.add(readLine);
            if (readLine.equals(".")) {
                return;
            } else {
                readLine = this._reader.readLine();
            }
        }
    }

    @Override // org.apache.commons.net.SocketClient
    public void disconnect() throws IOException {
        super.disconnect();
        this._reader = null;
        this._writer = null;
        this._lastReplyLine = null;
        this._replyLines.clear();
        setState(-1);
    }

    public int sendCommand(String str, String str2) throws IOException {
        if (this._writer == null) {
            throw new IllegalStateException("Socket is not connected");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        if (str2 != null) {
            sb.append(' ');
            sb.append(str2);
        }
        sb.append(SocketClient.NETASCII_EOL);
        String sb2 = sb.toString();
        this._writer.write(sb2);
        this._writer.flush();
        fireCommandSent(str, sb2);
        __getReply();
        return this._replyCode;
    }

    public int sendCommand(String str) throws IOException {
        return sendCommand(str, (String) null);
    }

    public int sendCommand(int i, String str) throws IOException {
        return sendCommand(POP3Command._commands[i], str);
    }

    public int sendCommand(int i) throws IOException {
        return sendCommand(POP3Command._commands[i], (String) null);
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

    public void removeProtocolCommandistener(ProtocolCommandListener protocolCommandListener) {
        removeProtocolCommandListener(protocolCommandListener);
    }

    @Override // org.apache.commons.net.SocketClient
    protected ProtocolCommandSupport getCommandSupport() {
        return this._commandSupport_;
    }
}
