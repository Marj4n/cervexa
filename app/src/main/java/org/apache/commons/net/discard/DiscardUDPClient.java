package org.apache.commons.net.discard;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import org.apache.commons.net.DatagramSocketClient;

/* loaded from: classes2.dex */
public class DiscardUDPClient extends DatagramSocketClient {
    public static final int DEFAULT_PORT = 9;
    DatagramPacket _sendPacket = new DatagramPacket(new byte[0], 0);

    public void send(byte[] bArr, int i, InetAddress inetAddress, int i2) throws IOException {
        this._sendPacket.setData(bArr);
        this._sendPacket.setLength(i);
        this._sendPacket.setAddress(inetAddress);
        this._sendPacket.setPort(i2);
        this._socket_.send(this._sendPacket);
    }

    public void send(byte[] bArr, int i, InetAddress inetAddress) throws IOException {
        send(bArr, i, inetAddress, 9);
    }

    public void send(byte[] bArr, InetAddress inetAddress) throws IOException {
        send(bArr, bArr.length, inetAddress, 9);
    }
}
