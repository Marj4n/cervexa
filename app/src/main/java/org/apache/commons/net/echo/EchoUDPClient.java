package org.apache.commons.net.echo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import org.apache.commons.net.discard.DiscardUDPClient;

/* loaded from: classes2.dex */
public final class EchoUDPClient extends DiscardUDPClient {
    public static final int DEFAULT_PORT = 7;
    private final DatagramPacket __receivePacket = new DatagramPacket(new byte[0], 0);

    @Override // org.apache.commons.net.discard.DiscardUDPClient
    public void send(byte[] bArr, int i, InetAddress inetAddress) throws IOException {
        send(bArr, i, inetAddress, 7);
    }

    @Override // org.apache.commons.net.discard.DiscardUDPClient
    public void send(byte[] bArr, InetAddress inetAddress) throws IOException {
        send(bArr, bArr.length, inetAddress, 7);
    }

    public int receive(byte[] bArr, int i) throws IOException {
        this.__receivePacket.setData(bArr);
        this.__receivePacket.setLength(i);
        this._socket_.receive(this.__receivePacket);
        return this.__receivePacket.getLength();
    }

    public int receive(byte[] bArr) throws IOException {
        return receive(bArr, bArr.length);
    }
}
