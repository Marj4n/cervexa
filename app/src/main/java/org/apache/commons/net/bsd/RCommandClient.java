package org.apache.commons.net.bsd;

import com.bumptech.glide.load.Key;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.commons.net.io.SocketInputStream;

/* loaded from: classes2.dex */
public class RCommandClient extends RExecClient {
    public static final int DEFAULT_PORT = 514;
    public static final int MAX_CLIENT_PORT = 1023;
    public static final int MIN_CLIENT_PORT = 512;

    @Override // org.apache.commons.net.bsd.RExecClient
    InputStream _createErrorStream() throws IOException {
        ServerSocket serverSocket;
        int i = MAX_CLIENT_PORT;
        while (true) {
            if (i < 512) {
                serverSocket = null;
                break;
            }
            try {
                serverSocket = this._serverSocketFactory_.createServerSocket(i, 1, getLocalAddress());
                break;
            } catch (SocketException unused) {
                i--;
            }
        }
        if (serverSocket == null) {
            throw new BindException("All ports in use.");
        }
        this._output_.write(Integer.toString(serverSocket.getLocalPort()).getBytes(Key.STRING_CHARSET_NAME));
        this._output_.write(0);
        this._output_.flush();
        Socket accept = serverSocket.accept();
        serverSocket.close();
        if (isRemoteVerificationEnabled() && !verifyRemote(accept)) {
            accept.close();
            throw new IOException("Security violation: unexpected connection attempt by " + accept.getInetAddress().getHostAddress());
        }
        return new SocketInputStream(accept, accept.getInputStream());
    }

    public RCommandClient() {
        setDefaultPort(514);
    }

    public void connect(InetAddress inetAddress, int i, InetAddress inetAddress2) throws SocketException, BindException, IOException {
        int i2 = MAX_CLIENT_PORT;
        while (i2 >= 512) {
            try {
                this._socket_ = this._socketFactory_.createSocket(inetAddress, i, inetAddress2, i2);
                break;
            } catch (BindException | SocketException unused) {
                i2--;
            }
        }
        if (i2 < 512) {
            throw new BindException("All ports in use or insufficient permssion.");
        }
        _connectAction_();
    }

    @Override // org.apache.commons.net.SocketClient
    public void connect(InetAddress inetAddress, int i) throws SocketException, IOException {
        connect(inetAddress, i, InetAddress.getLocalHost());
    }

    @Override // org.apache.commons.net.SocketClient
    public void connect(String str, int i) throws SocketException, IOException, UnknownHostException {
        connect(InetAddress.getByName(str), i, InetAddress.getLocalHost());
    }

    public void connect(String str, int i, InetAddress inetAddress) throws SocketException, IOException {
        connect(InetAddress.getByName(str), i, inetAddress);
    }

    @Override // org.apache.commons.net.SocketClient
    public void connect(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws SocketException, IOException, IllegalArgumentException {
        if (i2 < 512 || i2 > 1023) {
            throw new IllegalArgumentException("Invalid port number " + i2);
        }
        super.connect(inetAddress, i, inetAddress2, i2);
    }

    @Override // org.apache.commons.net.SocketClient
    public void connect(String str, int i, InetAddress inetAddress, int i2) throws SocketException, IOException, IllegalArgumentException, UnknownHostException {
        if (i2 < 512 || i2 > 1023) {
            throw new IllegalArgumentException("Invalid port number " + i2);
        }
        super.connect(str, i, inetAddress, i2);
    }

    public void rcommand(String str, String str2, String str3, boolean z) throws IOException {
        rexec(str, str2, str3, z);
    }

    public void rcommand(String str, String str2, String str3) throws IOException {
        rcommand(str, str2, str3, false);
    }
}
