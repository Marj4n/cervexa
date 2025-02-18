package org.apache.commons.net.tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import kotlin.UByte;

/* loaded from: classes2.dex */
public final class TFTPErrorPacket extends TFTPPacket {
    public static final int ACCESS_VIOLATION = 2;
    public static final int FILE_EXISTS = 6;
    public static final int FILE_NOT_FOUND = 1;
    public static final int ILLEGAL_OPERATION = 4;
    public static final int NO_SUCH_USER = 7;
    public static final int OUT_OF_SPACE = 3;
    public static final int UNDEFINED = 0;
    public static final int UNKNOWN_TID = 5;
    int _error;
    String _message;

    public TFTPErrorPacket(InetAddress inetAddress, int i, int i2, String str) {
        super(5, inetAddress, i);
        this._error = i2;
        this._message = str;
    }

    TFTPErrorPacket(DatagramPacket datagramPacket) throws TFTPPacketException {
        super(5, datagramPacket.getAddress(), datagramPacket.getPort());
        byte[] data = datagramPacket.getData();
        int length = datagramPacket.getLength();
        if (getType() != data[1]) {
            throw new TFTPPacketException("TFTP operator code does not match type.");
        }
        this._error = ((data[2] & UByte.MAX_VALUE) << 8) | (data[3] & UByte.MAX_VALUE);
        if (length < 5) {
            throw new TFTPPacketException("Bad error packet. No message.");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 4; i < length && data[i] != 0; i++) {
            sb.append((char) data[i]);
        }
        this._message = sb.toString();
    }

    @Override // org.apache.commons.net.tftp.TFTPPacket
    DatagramPacket _newDatagram(DatagramPacket datagramPacket, byte[] bArr) {
        int length = this._message.length();
        bArr[0] = 0;
        bArr[1] = (byte) this._type;
        int i = this._error;
        bArr[2] = (byte) ((65535 & i) >> 8);
        bArr[3] = (byte) (i & 255);
        System.arraycopy(this._message.getBytes(), 0, bArr, 4, length);
        int i2 = length + 4;
        bArr[i2] = 0;
        datagramPacket.setAddress(this._address);
        datagramPacket.setPort(this._port);
        datagramPacket.setData(bArr);
        datagramPacket.setLength(i2);
        return datagramPacket;
    }

    @Override // org.apache.commons.net.tftp.TFTPPacket
    public DatagramPacket newDatagram() {
        int length = this._message.length();
        int i = length + 5;
        byte[] bArr = new byte[i];
        bArr[0] = 0;
        bArr[1] = (byte) this._type;
        int i2 = this._error;
        bArr[2] = (byte) ((65535 & i2) >> 8);
        bArr[3] = (byte) (i2 & 255);
        System.arraycopy(this._message.getBytes(), 0, bArr, 4, length);
        bArr[length + 4] = 0;
        return new DatagramPacket(bArr, i, this._address, this._port);
    }

    public int getError() {
        return this._error;
    }

    public String getMessage() {
        return this._message;
    }
}
