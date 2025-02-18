package org.apache.commons.net.tftp;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import org.apache.commons.net.DatagramSocketClient;

/* loaded from: classes2.dex */
public class TFTP extends DatagramSocketClient {
    public static final int ASCII_MODE = 0;
    public static final int BINARY_MODE = 1;
    public static final int DEFAULT_PORT = 69;
    public static final int DEFAULT_TIMEOUT = 5000;
    public static final int IMAGE_MODE = 1;
    public static final int NETASCII_MODE = 0;
    public static final int OCTET_MODE = 1;
    static final int PACKET_SIZE = 516;
    private byte[] __receiveBuffer;
    private DatagramPacket __receiveDatagram;
    private DatagramPacket __sendDatagram;
    byte[] _sendBuffer;

    public static final String getModeName(int i) {
        return TFTPRequestPacket._modeStrings[i];
    }

    public TFTP() {
        setDefaultTimeout(5000);
        this.__receiveBuffer = null;
        this.__receiveDatagram = null;
    }

    public final void discardPackets() throws IOException {
        DatagramPacket datagramPacket = new DatagramPacket(new byte[516], 516);
        int soTimeout = getSoTimeout();
        setSoTimeout(1);
        while (true) {
            try {
                this._socket_.receive(datagramPacket);
            } catch (InterruptedIOException | SocketException unused) {
                setSoTimeout(soTimeout);
                return;
            }
        }
    }

    public final TFTPPacket bufferedReceive() throws IOException, InterruptedIOException, SocketException, TFTPPacketException {
        this.__receiveDatagram.setData(this.__receiveBuffer);
        this.__receiveDatagram.setLength(this.__receiveBuffer.length);
        this._socket_.receive(this.__receiveDatagram);
        return TFTPPacket.newTFTPPacket(this.__receiveDatagram);
    }

    public final void bufferedSend(TFTPPacket tFTPPacket) throws IOException {
        this._socket_.send(tFTPPacket._newDatagram(this.__sendDatagram, this._sendBuffer));
    }

    public final void beginBufferedOps() {
        this.__receiveBuffer = new byte[516];
        byte[] bArr = this.__receiveBuffer;
        this.__receiveDatagram = new DatagramPacket(bArr, bArr.length);
        this._sendBuffer = new byte[516];
        byte[] bArr2 = this._sendBuffer;
        this.__sendDatagram = new DatagramPacket(bArr2, bArr2.length);
    }

    public final void endBufferedOps() {
        this.__receiveBuffer = null;
        this.__receiveDatagram = null;
        this._sendBuffer = null;
        this.__sendDatagram = null;
    }

    public final void send(TFTPPacket tFTPPacket) throws IOException {
        this._socket_.send(tFTPPacket.newDatagram());
    }

    public final TFTPPacket receive() throws IOException, InterruptedIOException, SocketException, TFTPPacketException {
        DatagramPacket datagramPacket = new DatagramPacket(new byte[516], 516);
        this._socket_.receive(datagramPacket);
        return TFTPPacket.newTFTPPacket(datagramPacket);
    }
}
