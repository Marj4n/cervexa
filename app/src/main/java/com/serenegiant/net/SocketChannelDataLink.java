package com.serenegiant.net;

import android.util.Log;
import com.serenegiant.net.AbstractChannelDataLink;
import com.serenegiant.utils.BuildCheck;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.ByteChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/* loaded from: classes2.dex */
public class SocketChannelDataLink extends AbstractChannelDataLink {
    private static final boolean DEBUG = false;
    public static final int DEFAULT_SERVER_PORT = 6000;
    private static final String TAG = SocketChannelDataLink.class.getSimpleName();
    private ServerTask mServerTask;

    public SocketChannelDataLink() {
    }

    public SocketChannelDataLink(Callback callback) {
        super(callback);
    }

    @Override // com.serenegiant.net.AbstractChannelDataLink
    public void release() {
        stop();
        super.release();
    }

    public Client connectTo(String str) throws IOException {
        return connectTo(str, DEFAULT_SERVER_PORT);
    }

    public Client connectTo(String str, int i) throws IOException {
        try {
            InetAddress.getByName(str);
            Client client = new Client(this, str, i);
            add(client);
            return client;
        } catch (UnknownHostException e) {
            throw new IOException(e.getMessage());
        }
    }

    public synchronized boolean isRunning() {
        return this.mServerTask != null;
    }

    public void start() throws IllegalStateException {
        start(DEFAULT_SERVER_PORT, null);
    }

    public void start(Callback callback) throws IllegalStateException {
        start(DEFAULT_SERVER_PORT, callback);
    }

    public synchronized void start(int i, Callback callback) throws IllegalStateException {
        add(callback);
        if (this.mServerTask == null) {
            this.mServerTask = new ServerTask(i);
            new Thread(this.mServerTask).start();
        } else {
            Log.d(TAG, "already started");
        }
    }

    public synchronized void stop() {
        if (this.mServerTask != null) {
            this.mServerTask.release();
            this.mServerTask = null;
        }
    }

    public static class Client extends AbstractClient {
        private String mAddr;
        private int mPort;

        public Client(SocketChannelDataLink socketChannelDataLink, ByteChannel byteChannel) {
            super(socketChannelDataLink, byteChannel);
            internalStart();
        }

        public Client(SocketChannelDataLink socketChannelDataLink, String str, int i) {
            super(socketChannelDataLink, null);
            this.mAddr = str;
            this.mPort = i;
            internalStart();
        }

        public synchronized String getAddress() {
            InetAddress inetAddress;
            Socket socket = this.mChannel instanceof SocketChannel ? ((SocketChannel) this.mChannel).socket() : null;
            inetAddress = socket != null ? socket.getInetAddress() : null;
            return inetAddress != null ? inetAddress.getHostAddress() : null;
        }

        public synchronized int getPort() {
            Socket socket;
            socket = this.mChannel instanceof SocketChannel ? ((SocketChannel) this.mChannel).socket() : null;
            return socket != null ? socket.getPort() : 0;
        }

        public synchronized boolean isConnected() {
            boolean z = false;
            if (this.mChannel instanceof SocketChannel) {
                z = ((SocketChannel) this.mChannel).isConnected();
            }
            return z;
        }

        @Override // com.serenegiant.net.AbstractChannelDataLink.AbstractClient
        protected synchronized void init() throws IOException {
            try {
                if (this.mChannel == null) {
                    this.mChannel = SocketChannel.open(new InetSocketAddress(this.mAddr, this.mPort));
                }
                setInit(true);
            } finally {
                notifyAll();
            }
        }
    }

    private class ServerTask implements Runnable {
        private volatile boolean mIsRunning;
        private final int mPort;
        private ServerSocketChannel mServerChannel;

        public ServerTask(int i) {
            this.mPort = i;
        }

        public synchronized void release() {
            this.mIsRunning = false;
            if (this.mServerChannel != null) {
                try {
                    this.mServerChannel.close();
                } catch (IOException unused) {
                }
                this.mServerChannel = null;
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                try {
                    init();
                    while (this.mIsRunning) {
                        serverLoop();
                    }
                } catch (Exception e) {
                    Log.w(SocketChannelDataLink.TAG, e);
                }
            } finally {
                release();
            }
        }

        private synchronized void init() throws IOException {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(NetworkHelper.getLocalIPv4Address(), this.mPort);
            this.mServerChannel = ServerSocketChannel.open();
            if (BuildCheck.isNougat()) {
                this.mServerChannel.bind((SocketAddress) inetSocketAddress);
            } else {
                this.mServerChannel.socket().bind(inetSocketAddress);
            }
            this.mIsRunning = true;
        }

        private void serverLoop() {
            while (this.mIsRunning) {
                try {
                    SocketChannelDataLink.this.add(new Client(SocketChannelDataLink.this, this.mServerChannel.accept()));
                } catch (IOException unused) {
                    return;
                }
            }
        }
    }
}
