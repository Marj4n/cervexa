package org.apache.commons.net.ftp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;

/* loaded from: classes2.dex */
public class FTPSServerSocketFactory extends ServerSocketFactory {
    private final SSLContext context;

    public FTPSServerSocketFactory(SSLContext sSLContext) {
        this.context = sSLContext;
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket() throws IOException {
        return init(this.context.getServerSocketFactory().createServerSocket());
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket(int i) throws IOException {
        return init(this.context.getServerSocketFactory().createServerSocket(i));
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket(int i, int i2) throws IOException {
        return init(this.context.getServerSocketFactory().createServerSocket(i, i2));
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket(int i, int i2, InetAddress inetAddress) throws IOException {
        return init(this.context.getServerSocketFactory().createServerSocket(i, i2, inetAddress));
    }

    public ServerSocket init(ServerSocket serverSocket) {
        ((SSLServerSocket) serverSocket).setUseClientMode(true);
        return serverSocket;
    }
}
