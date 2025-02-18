package org.apache.commons.net.ftp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.net.MalformedServerReplyException;
import org.apache.commons.net.ProtocolCommandSupport;
import org.apache.commons.net.SocketClient;
import org.apache.commons.net.io.CRLFLineReader;

/* loaded from: classes2.dex */
public class FTP extends SocketClient {
    public static final int ASCII_FILE_TYPE = 0;
    public static final int BINARY_FILE_TYPE = 2;
    public static final int BLOCK_TRANSFER_MODE = 11;
    public static final int CARRIAGE_CONTROL_TEXT_FORMAT = 6;
    public static final int COMPRESSED_TRANSFER_MODE = 12;
    public static final String DEFAULT_CONTROL_ENCODING = "ISO-8859-1";
    public static final int DEFAULT_DATA_PORT = 20;
    public static final int DEFAULT_PORT = 21;
    public static final int EBCDIC_FILE_TYPE = 1;
    public static final int FILE_STRUCTURE = 7;
    public static final int LOCAL_FILE_TYPE = 3;
    public static final int NON_PRINT_TEXT_FORMAT = 4;
    public static final int PAGE_STRUCTURE = 9;
    public static final int RECORD_STRUCTURE = 8;
    public static final int REPLY_CODE_LEN = 3;
    public static final int STREAM_TRANSFER_MODE = 10;
    public static final int TELNET_TEXT_FORMAT = 5;
    private static final String __modes = "AEILNTCFRPSBC";
    protected ProtocolCommandSupport _commandSupport_;
    protected String _controlEncoding;
    protected BufferedReader _controlInput_;
    protected BufferedWriter _controlOutput_;
    protected boolean _newReplyString;
    protected int _replyCode;
    protected ArrayList<String> _replyLines;
    protected String _replyString;
    protected boolean strictMultilineParsing = false;

    public FTP() {
        setDefaultPort(21);
        this._replyLines = new ArrayList<>();
        this._newReplyString = false;
        this._replyString = null;
        this._controlEncoding = DEFAULT_CONTROL_ENCODING;
        this._commandSupport_ = new ProtocolCommandSupport(this);
    }

    private boolean __strictCheck(String str, String str2) {
        return (str.startsWith(str2) && str.charAt(3) == ' ') ? false : true;
    }

    private boolean __lenientCheck(String str) {
        return str.length() <= 3 || str.charAt(3) == '-' || !Character.isDigit(str.charAt(0));
    }

    private void __getReply() throws IOException {
        __getReply(true);
    }

    protected void __getReplyNoReport() throws IOException {
        __getReply(false);
    }

    private void __getReply(boolean z) throws IOException {
        this._newReplyString = true;
        this._replyLines.clear();
        String readLine = this._controlInput_.readLine();
        if (readLine == null) {
            throw new FTPConnectionClosedException("Connection closed without indication.");
        }
        int length = readLine.length();
        if (length < 3) {
            throw new MalformedServerReplyException("Truncated server reply: " + readLine);
        }
        try {
            String substring = readLine.substring(0, 3);
            this._replyCode = Integer.parseInt(substring);
            this._replyLines.add(readLine);
            if (length > 3 && readLine.charAt(3) == '-') {
                while (true) {
                    String readLine2 = this._controlInput_.readLine();
                    if (readLine2 == null) {
                        throw new FTPConnectionClosedException("Connection closed without indication.");
                    }
                    this._replyLines.add(readLine2);
                    if (isStrictMultilineParsing()) {
                        if (!__strictCheck(readLine2, substring)) {
                            break;
                        }
                    } else if (!__lenientCheck(readLine2)) {
                        break;
                    }
                }
            }
            if (z) {
                fireReplyReceived(this._replyCode, getReplyString());
            }
            if (this._replyCode == 421) {
                throw new FTPConnectionClosedException("FTP response 421 received.  Server closed connection.");
            }
        } catch (NumberFormatException unused) {
            throw new MalformedServerReplyException("Could not parse response code.\nServer Reply: " + readLine);
        }
    }

    @Override // org.apache.commons.net.SocketClient
    protected void _connectAction_() throws IOException {
        _connectAction_(null);
    }

    protected void _connectAction_(Reader reader) throws IOException {
        super._connectAction_();
        if (reader == null) {
            this._controlInput_ = new CRLFLineReader(new InputStreamReader(this._input_, getControlEncoding()));
        } else {
            this._controlInput_ = new CRLFLineReader(reader);
        }
        this._controlOutput_ = new BufferedWriter(new OutputStreamWriter(this._output_, getControlEncoding()));
        if (this.connectTimeout > 0) {
            int soTimeout = this._socket_.getSoTimeout();
            this._socket_.setSoTimeout(this.connectTimeout);
            try {
                try {
                    __getReply();
                    if (FTPReply.isPositivePreliminary(this._replyCode)) {
                        __getReply();
                    }
                    return;
                } catch (SocketTimeoutException e) {
                    IOException iOException = new IOException("Timed out waiting for initial connect reply");
                    iOException.initCause(e);
                    throw iOException;
                }
            } finally {
                this._socket_.setSoTimeout(soTimeout);
            }
        }
        __getReply();
        if (FTPReply.isPositivePreliminary(this._replyCode)) {
            __getReply();
        }
    }

    public void setControlEncoding(String str) {
        this._controlEncoding = str;
    }

    public String getControlEncoding() {
        return this._controlEncoding;
    }

    @Override // org.apache.commons.net.SocketClient
    public void disconnect() throws IOException {
        super.disconnect();
        this._controlInput_ = null;
        this._controlOutput_ = null;
        this._newReplyString = false;
        this._replyString = null;
    }

    public int sendCommand(String str, String str2) throws IOException {
        if (this._controlOutput_ == null) {
            throw new IOException("Connection is not open");
        }
        String __buildMessage = __buildMessage(str, str2);
        __send(__buildMessage);
        fireCommandSent(str, __buildMessage);
        __getReply();
        return this._replyCode;
    }

    private String __buildMessage(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        if (str2 != null) {
            sb.append(' ');
            sb.append(str2);
        }
        sb.append(SocketClient.NETASCII_EOL);
        return sb.toString();
    }

    private void __send(String str) throws IOException, FTPConnectionClosedException, SocketException {
        try {
            this._controlOutput_.write(str);
            this._controlOutput_.flush();
        } catch (SocketException e) {
            if (!isConnected()) {
                throw new FTPConnectionClosedException("Connection unexpectedly closed.");
            }
            throw e;
        }
    }

    protected void __noop() throws IOException {
        __send(__buildMessage(FTPCmd.NOOP.getCommand(), null));
        __getReplyNoReport();
    }

    @Deprecated
    public int sendCommand(int i, String str) throws IOException {
        return sendCommand(FTPCommand.getCommand(i), str);
    }

    public int sendCommand(FTPCmd fTPCmd) throws IOException {
        return sendCommand(fTPCmd, (String) null);
    }

    public int sendCommand(FTPCmd fTPCmd, String str) throws IOException {
        return sendCommand(fTPCmd.getCommand(), str);
    }

    public int sendCommand(String str) throws IOException {
        return sendCommand(str, (String) null);
    }

    public int sendCommand(int i) throws IOException {
        return sendCommand(i, (String) null);
    }

    public int getReplyCode() {
        return this._replyCode;
    }

    public int getReply() throws IOException {
        __getReply();
        return this._replyCode;
    }

    public String[] getReplyStrings() {
        ArrayList<String> arrayList = this._replyLines;
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public String getReplyString() {
        if (!this._newReplyString) {
            return this._replyString;
        }
        StringBuilder sb = new StringBuilder(256);
        Iterator<String> it = this._replyLines.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            sb.append(SocketClient.NETASCII_EOL);
        }
        this._newReplyString = false;
        String sb2 = sb.toString();
        this._replyString = sb2;
        return sb2;
    }

    public int user(String str) throws IOException {
        return sendCommand(FTPCmd.USER, str);
    }

    public int pass(String str) throws IOException {
        return sendCommand(FTPCmd.PASS, str);
    }

    public int acct(String str) throws IOException {
        return sendCommand(FTPCmd.ACCT, str);
    }

    public int abor() throws IOException {
        return sendCommand(FTPCmd.ABOR);
    }

    public int cwd(String str) throws IOException {
        return sendCommand(FTPCmd.CWD, str);
    }

    public int cdup() throws IOException {
        return sendCommand(FTPCmd.CDUP);
    }

    public int quit() throws IOException {
        return sendCommand(FTPCmd.QUIT);
    }

    public int rein() throws IOException {
        return sendCommand(FTPCmd.REIN);
    }

    public int smnt(String str) throws IOException {
        return sendCommand(FTPCmd.SMNT, str);
    }

    public int port(InetAddress inetAddress, int i) throws IOException {
        StringBuilder sb = new StringBuilder(24);
        sb.append(inetAddress.getHostAddress().replace('.', ','));
        sb.append(',');
        sb.append(i >>> 8);
        sb.append(',');
        sb.append(i & 255);
        return sendCommand(FTPCmd.PORT, sb.toString());
    }

    public int eprt(InetAddress inetAddress, int i) throws IOException {
        StringBuilder sb = new StringBuilder();
        String hostAddress = inetAddress.getHostAddress();
        int indexOf = hostAddress.indexOf("%");
        if (indexOf > 0) {
            hostAddress = hostAddress.substring(0, indexOf);
        }
        sb.append("|");
        if (inetAddress instanceof Inet4Address) {
            sb.append("1");
        } else if (inetAddress instanceof Inet6Address) {
            sb.append("2");
        }
        sb.append("|");
        sb.append(hostAddress);
        sb.append("|");
        sb.append(i);
        sb.append("|");
        return sendCommand(FTPCmd.EPRT, sb.toString());
    }

    public int pasv() throws IOException {
        return sendCommand(FTPCmd.PASV);
    }

    public int epsv() throws IOException {
        return sendCommand(FTPCmd.EPSV);
    }

    public int type(int i, int i2) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(__modes.charAt(i));
        sb.append(' ');
        if (i == 3) {
            sb.append(i2);
        } else {
            sb.append(__modes.charAt(i2));
        }
        return sendCommand(FTPCmd.TYPE, sb.toString());
    }

    public int type(int i) throws IOException {
        return sendCommand(FTPCmd.TYPE, __modes.substring(i, i + 1));
    }

    public int stru(int i) throws IOException {
        return sendCommand(FTPCmd.STRU, __modes.substring(i, i + 1));
    }

    public int mode(int i) throws IOException {
        return sendCommand(FTPCmd.MODE, __modes.substring(i, i + 1));
    }

    public int retr(String str) throws IOException {
        return sendCommand(FTPCmd.RETR, str);
    }

    public int stor(String str) throws IOException {
        return sendCommand(FTPCmd.STOR, str);
    }

    public int stou() throws IOException {
        return sendCommand(FTPCmd.STOU);
    }

    public int stou(String str) throws IOException {
        return sendCommand(FTPCmd.STOU, str);
    }

    public int appe(String str) throws IOException {
        return sendCommand(FTPCmd.APPE, str);
    }

    public int allo(int i) throws IOException {
        return sendCommand(FTPCmd.ALLO, Integer.toString(i));
    }

    public int feat() throws IOException {
        return sendCommand(FTPCmd.FEAT);
    }

    public int allo(int i, int i2) throws IOException {
        return sendCommand(FTPCmd.ALLO, Integer.toString(i) + " R " + Integer.toString(i2));
    }

    public int rest(String str) throws IOException {
        return sendCommand(FTPCmd.REST, str);
    }

    public int mdtm(String str) throws IOException {
        return sendCommand(FTPCmd.MDTM, str);
    }

    public int mfmt(String str, String str2) throws IOException {
        return sendCommand(FTPCmd.MFMT, str2 + " " + str);
    }

    public int rnfr(String str) throws IOException {
        return sendCommand(FTPCmd.RNFR, str);
    }

    public int rnto(String str) throws IOException {
        return sendCommand(FTPCmd.RNTO, str);
    }

    public int dele(String str) throws IOException {
        return sendCommand(FTPCmd.DELE, str);
    }

    public int rmd(String str) throws IOException {
        return sendCommand(FTPCmd.RMD, str);
    }

    public int mkd(String str) throws IOException {
        return sendCommand(FTPCmd.MKD, str);
    }

    public int pwd() throws IOException {
        return sendCommand(FTPCmd.PWD);
    }

    public int list() throws IOException {
        return sendCommand(FTPCmd.LIST);
    }

    public int list(String str) throws IOException {
        return sendCommand(FTPCmd.LIST, str);
    }

    public int mlsd() throws IOException {
        return sendCommand(FTPCmd.MLSD);
    }

    public int mlsd(String str) throws IOException {
        return sendCommand(FTPCmd.MLSD, str);
    }

    public int mlst() throws IOException {
        return sendCommand(FTPCmd.MLST);
    }

    public int mlst(String str) throws IOException {
        return sendCommand(FTPCmd.MLST, str);
    }

    public int nlst() throws IOException {
        return sendCommand(FTPCmd.NLST);
    }

    public int nlst(String str) throws IOException {
        return sendCommand(FTPCmd.NLST, str);
    }

    public int site(String str) throws IOException {
        return sendCommand(FTPCmd.SITE, str);
    }

    public int syst() throws IOException {
        return sendCommand(FTPCmd.SYST);
    }

    public int stat() throws IOException {
        return sendCommand(FTPCmd.STAT);
    }

    public int stat(String str) throws IOException {
        return sendCommand(FTPCmd.STAT, str);
    }

    public int help() throws IOException {
        return sendCommand(FTPCmd.HELP);
    }

    public int help(String str) throws IOException {
        return sendCommand(FTPCmd.HELP, str);
    }

    public int noop() throws IOException {
        return sendCommand(FTPCmd.NOOP);
    }

    public boolean isStrictMultilineParsing() {
        return this.strictMultilineParsing;
    }

    public void setStrictMultilineParsing(boolean z) {
        this.strictMultilineParsing = z;
    }

    @Override // org.apache.commons.net.SocketClient
    protected ProtocolCommandSupport getCommandSupport() {
        return this._commandSupport_;
    }
}
