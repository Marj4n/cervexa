package org.apache.commons.net.tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;

/* loaded from: classes2.dex */
public abstract class TFTPPacket {
    public static final int ACKNOWLEDGEMENT = 4;
    public static final int DATA = 3;
    public static final int ERROR = 5;
    static final int MIN_PACKET_SIZE = 4;
    public static final int READ_REQUEST = 1;
    public static final int SEGMENT_SIZE = 512;
    public static final int WRITE_REQUEST = 2;
    InetAddress _address;
    int _port;
    int _type;

    abstract DatagramPacket _newDatagram(DatagramPacket datagramPacket, byte[] bArr);

    public abstract DatagramPacket newDatagram();

    public static final TFTPPacket newTFTPPacket(DatagramPacket datagramPacket) throws TFTPPacketException {
        if (datagramPacket.getLength() < 4) {
            throw new TFTPPacketException("Bad packet. Datagram data length is too short.");
        }
        byte b = datagramPacket.getData()[1];
        if (b == 1) {
            return new TFTPReadRequestPacket(datagramPacket);
        }
        if (b == 2) {
            return new TFTPWriteRequestPacket(datagramPacket);
        }
        if (b == 3) {
            return new TFTPDataPacket(datagramPacket);
        }
        if (b == 4) {
            return new TFTPAckPacket(datagramPacket);
        }
        if (b == 5) {
            return new TFTPErrorPacket(datagramPacket);
        }
        throw new TFTPPacketException("Bad packet.  Invalid TFTP operator code.");
    }

    TFTPPacket(int i, InetAddress inetAddress, int i2) {
        this._type = i;
        this._address = inetAddress;
        this._port = i2;
    }

    public final int getType() {
        return this._type;
    }

    public final InetAddress getAddress() {
        return this._address;
    }

    public final int getPort() {
        return this._port;
    }

    public final void setPort(int i) {
        this._port = i;
    }

    public final void setAddress(InetAddress inetAddress) {
        this._address = inetAddress;
    }
}
