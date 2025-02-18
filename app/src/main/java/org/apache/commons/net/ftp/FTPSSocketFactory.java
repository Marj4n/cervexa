package org.apache.commons.net.ftp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;

/* loaded from: classes2.dex */
public class FTPSSocketFactory extends SocketFactory {
    private final SSLContext context;

    public FTPSSocketFactory(SSLContext sSLContext) {
        this.context = sSLContext;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket() throws IOException {
        return this.context.getSocketFactory().createSocket();
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String str, int i) throws UnknownHostException, IOException {
        return this.context.getSocketFactory().createSocket(str, i);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return this.context.getSocketFactory().createSocket(inetAddress, i);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws UnknownHostException, IOException {
        return this.context.getSocketFactory().createSocket(str, i, inetAddress, i2);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        return this.context.getSocketFactory().createSocket(inetAddress, i, inetAddress2, i2);
    }

    @Deprecated
    public ServerSocket createServerSocket(int i) throws IOException {
        return init(this.context.getServerSocketFactory().createServerSocket(i));
    }

    @Deprecated
    public ServerSocket createServerSocket(int i, int i2) throws IOException {
        return init(this.context.getServerSocketFactory().createServerSocket(i, i2));
    }

    @Deprecated
    public ServerSocket createServerSocket(int i, int i2, InetAddress inetAddress) throws IOException {
        return init(this.context.getServerSocketFactory().createServerSocket(i, i2, inetAddress));
    }

    @Deprecated
    public ServerSocket init(ServerSocket serverSocket) throws IOException {
        ((SSLServerSocket) serverSocket).setUseClientMode(true);
        return serverSocket;
    }
}
