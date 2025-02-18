package org.apache.commons.net.ftp;

import com.bumptech.glide.load.Key;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Inet6Address;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.net.SocketClient;
import org.apache.commons.net.util.Base64;

/* loaded from: classes2.dex */
public class FTPHTTPClient extends FTPClient {
    private static final byte[] CRLF = {13, 10};
    private final Base64 base64;
    private final String proxyHost;
    private final String proxyPassword;
    private final int proxyPort;
    private final String proxyUsername;
    private String tunnelHost;

    public FTPHTTPClient(String str, int i, String str2, String str3) {
        this.base64 = new Base64();
        this.proxyHost = str;
        this.proxyPort = i;
        this.proxyUsername = str2;
        this.proxyPassword = str3;
        this.tunnelHost = null;
    }

    public FTPHTTPClient(String str, int i) {
        this(str, i, null, null);
    }

    @Override // org.apache.commons.net.ftp.FTPClient
    @Deprecated
    protected Socket _openDataConnection_(int i, String str) throws IOException {
        return super._openDataConnection_(i, str);
    }

    @Override // org.apache.commons.net.ftp.FTPClient
    protected Socket _openDataConnection_(String str, String str2) throws IOException {
        String passiveHost;
        if (getDataConnectionMode() != 2) {
            throw new IllegalStateException("Only passive connection mode supported");
        }
        boolean z = getRemoteAddress() instanceof Inet6Address;
        if ((isUseEPSVwithIPv4() || z) && epsv() == 229) {
            _parseExtendedPassiveModeReply(this._replyLines.get(0));
            passiveHost = this.tunnelHost;
        } else {
            if (z || pasv() != 227) {
                return null;
            }
            _parsePassiveModeReply(this._replyLines.get(0));
            passiveHost = getPassiveHost();
        }
        Socket createSocket = this._socketFactory_.createSocket(this.proxyHost, this.proxyPort);
        tunnelHandshake(passiveHost, getPassivePort(), createSocket.getInputStream(), createSocket.getOutputStream());
        if (getRestartOffset() > 0 && !restart(getRestartOffset())) {
            createSocket.close();
            return null;
        }
        if (FTPReply.isPositivePreliminary(sendCommand(str, str2))) {
            return createSocket;
        }
        createSocket.close();
        return null;
    }

    @Override // org.apache.commons.net.SocketClient
    public void connect(String str, int i) throws SocketException, IOException {
        this._socket_ = this._socketFactory_.createSocket(this.proxyHost, this.proxyPort);
        this._input_ = this._socket_.getInputStream();
        this._output_ = this._socket_.getOutputStream();
        try {
            super._connectAction_(tunnelHandshake(str, i, this._input_, this._output_));
        } catch (Exception e) {
            IOException iOException = new IOException("Could not connect to " + str + " using port " + i);
            iOException.initCause(e);
            throw iOException;
        }
    }

    private BufferedReader tunnelHandshake(String str, int i, InputStream inputStream, OutputStream outputStream) throws IOException, UnsupportedEncodingException {
        String str2 = "CONNECT " + str + ":" + i + " HTTP/1.1";
        this.tunnelHost = str;
        outputStream.write(str2.getBytes(Key.STRING_CHARSET_NAME));
        outputStream.write(CRLF);
        outputStream.write(("Host: " + str + ":" + i).getBytes(Key.STRING_CHARSET_NAME));
        outputStream.write(CRLF);
        if (this.proxyUsername != null && this.proxyPassword != null) {
            outputStream.write(("Proxy-Authorization: Basic " + this.base64.encodeToString((this.proxyUsername + ":" + this.proxyPassword).getBytes(Key.STRING_CHARSET_NAME))).getBytes(Key.STRING_CHARSET_NAME));
        }
        outputStream.write(CRLF);
        ArrayList arrayList = new ArrayList();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, getCharset()));
        for (String readLine = bufferedReader.readLine(); readLine != null && readLine.length() > 0; readLine = bufferedReader.readLine()) {
            arrayList.add(readLine);
        }
        if (arrayList.size() == 0) {
            throw new IOException("No response from proxy");
        }
        String str3 = (String) arrayList.get(0);
        if (!str3.startsWith("HTTP/") || str3.length() < 12) {
            throw new IOException("Invalid response from proxy: " + str3);
        }
        if ("200".equals(str3.substring(9, 12))) {
            return bufferedReader;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("HTTPTunnelConnector: connection failed\r\n");
        sb.append("Response received from the proxy:\r\n");
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            sb.append((String) it.next());
            sb.append(SocketClient.NETASCII_EOL);
        }
        throw new IOException(sb.toString());
    }
}
