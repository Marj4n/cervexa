package com.serenegiant.net;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Collections;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class UdpSocket {
    private final InetSocketAddress broadcast;
    private DatagramChannel channel;
    private String localAddress;
    private String remoteAddress;
    private int remotePort;
    private SocketAddress sender;

    public UdpSocket(int i) throws SocketException {
        InetAddress inetAddress = null;
        try {
            DatagramChannel open = DatagramChannel.open();
            this.channel = open;
            open.configureBlocking(false);
            DatagramSocket socket = this.channel.socket();
            socket.setBroadcast(true);
            socket.setReuseAddress(true);
            Iterator it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
            while (it.hasNext()) {
                NetworkInterface networkInterface = (NetworkInterface) it.next();
                if (!networkInterface.isLoopback()) {
                    Iterator it2 = Collections.list(networkInterface.getInetAddresses()).iterator();
                    while (it2.hasNext()) {
                        InetAddress inetAddress2 = (InetAddress) it2.next();
                        if (inetAddress2 instanceof Inet4Address) {
                            inetAddress = inetAddress2;
                        }
                    }
                }
            }
            this.localAddress = inetAddress.getHostAddress();
            socket.bind(new InetSocketAddress(InetAddress.getByAddress(new byte[]{0, 0, 0, 0}), i));
            byte[] address = inetAddress.getAddress();
            address[3] = -1;
            this.broadcast = new InetSocketAddress(InetAddress.getByAddress(address), i);
        } catch (Exception e) {
            throw new SocketException("UdpSocket#constructor:" + e);
        }
    }

    public void release() {
        if (this.channel != null) {
            try {
                setSoTimeout(200);
            } catch (Exception unused) {
            }
            try {
                this.channel.close();
            } catch (Exception unused2) {
            }
        }
        this.channel = null;
    }

    public DatagramChannel channel() {
        return this.channel;
    }

    public DatagramSocket socket() {
        return this.channel.socket();
    }

    public void setReceiveBufferSize(int i) throws SocketException {
        DatagramChannel datagramChannel = this.channel;
        DatagramSocket socket = datagramChannel != null ? datagramChannel.socket() : null;
        if (socket != null) {
            socket.setReceiveBufferSize(i);
        }
    }

    public void setReuseAddress(boolean z) throws SocketException {
        DatagramChannel datagramChannel = this.channel;
        DatagramSocket socket = datagramChannel != null ? datagramChannel.socket() : null;
        if (socket != null) {
            socket.setReuseAddress(z);
        }
    }

    public boolean getReuseAddress() throws SocketException, IllegalStateException {
        DatagramChannel datagramChannel = this.channel;
        DatagramSocket socket = datagramChannel != null ? datagramChannel.socket() : null;
        if (socket != null) {
            return socket.getReuseAddress();
        }
        throw new IllegalStateException("already released");
    }

    public void setBroadcast(boolean z) throws SocketException {
        DatagramChannel datagramChannel = this.channel;
        DatagramSocket socket = datagramChannel != null ? datagramChannel.socket() : null;
        if (socket != null) {
            socket.setBroadcast(z);
        }
    }

    public boolean getBroadcast() throws SocketException, IllegalStateException {
        DatagramChannel datagramChannel = this.channel;
        DatagramSocket socket = datagramChannel != null ? datagramChannel.socket() : null;
        if (socket != null) {
            return socket.getBroadcast();
        }
        throw new IllegalStateException("already released");
    }

    public void setSoTimeout(int i) throws SocketException {
        DatagramChannel datagramChannel = this.channel;
        DatagramSocket socket = datagramChannel != null ? datagramChannel.socket() : null;
        if (socket != null) {
            socket.setSoTimeout(i);
        }
    }

    public int getSoTimeout() throws SocketException, IllegalStateException {
        DatagramChannel datagramChannel = this.channel;
        DatagramSocket socket = datagramChannel != null ? datagramChannel.socket() : null;
        if (socket != null) {
            return socket.getSoTimeout();
        }
        throw new IllegalStateException("already released");
    }

    public String local() {
        return this.localAddress;
    }

    public String remote() {
        return this.remoteAddress;
    }

    public int remotePort() {
        return this.remotePort;
    }

    public void broadcast(ByteBuffer byteBuffer) throws IOException, IllegalStateException {
        DatagramChannel datagramChannel = this.channel;
        if (datagramChannel == null) {
            throw new IllegalStateException("already released");
        }
        datagramChannel.send(byteBuffer, this.broadcast);
    }

    public void broadcast(byte[] bArr) throws IOException, IllegalStateException {
        broadcast(ByteBuffer.wrap(bArr));
    }

    public int receive(ByteBuffer byteBuffer) throws IOException, IllegalStateException {
        if (this.channel == null) {
            throw new IllegalStateException("already released");
        }
        int remaining = byteBuffer.remaining();
        SocketAddress receive = this.channel.receive(byteBuffer);
        this.sender = receive;
        if (receive == null) {
            return -1;
        }
        InetSocketAddress inetSocketAddress = (InetSocketAddress) receive;
        this.remoteAddress = inetSocketAddress.getAddress().getHostAddress();
        this.remotePort = inetSocketAddress.getPort();
        return remaining - byteBuffer.remaining();
    }
}
