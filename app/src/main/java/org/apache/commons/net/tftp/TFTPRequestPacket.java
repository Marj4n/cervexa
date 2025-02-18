package org.apache.commons.net.tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Locale;

/* loaded from: classes2.dex */
public abstract class TFTPRequestPacket extends TFTPPacket {
    private final String _filename;
    private final int _mode;
    static final String[] _modeStrings = {"netascii", "octet"};
    private static final byte[][] _modeBytes = {new byte[]{110, 101, 116, 97, 115, 99, 105, 105, 0}, new byte[]{111, 99, 116, 101, 116, 0}};

    TFTPRequestPacket(InetAddress inetAddress, int i, int i2, String str, int i3) {
        super(i2, inetAddress, i);
        this._filename = str;
        this._mode = i3;
    }

    TFTPRequestPacket(int i, DatagramPacket datagramPacket) throws TFTPPacketException {
        super(i, datagramPacket.getAddress(), datagramPacket.getPort());
        byte[] data = datagramPacket.getData();
        if (getType() != data[1]) {
            throw new TFTPPacketException("TFTP operator code does not match type.");
        }
        StringBuilder sb = new StringBuilder();
        int i2 = 2;
        int length = datagramPacket.getLength();
        while (i2 < length && data[i2] != 0) {
            sb.append((char) data[i2]);
            i2++;
        }
        this._filename = sb.toString();
        if (i2 >= length) {
            throw new TFTPPacketException("Bad filename and mode format.");
        }
        int i3 = 0;
        sb.setLength(0);
        for (int i4 = i2 + 1; i4 < length && data[i4] != 0; i4++) {
            sb.append((char) data[i4]);
        }
        String lowerCase = sb.toString().toLowerCase(Locale.ENGLISH);
        int length2 = _modeStrings.length;
        int i5 = 0;
        while (true) {
            if (i5 >= length2) {
                break;
            }
            if (lowerCase.equals(_modeStrings[i5])) {
                i3 = i5;
                break;
            }
            i5++;
        }
        this._mode = i3;
        if (i5 < length2) {
            return;
        }
        throw new TFTPPacketException("Unrecognized TFTP transfer mode: " + lowerCase);
    }

    @Override // org.apache.commons.net.tftp.TFTPPacket
    final DatagramPacket _newDatagram(DatagramPacket datagramPacket, byte[] bArr) {
        int length = this._filename.length();
        int length2 = _modeBytes[this._mode].length;
        bArr[0] = 0;
        bArr[1] = (byte) this._type;
        System.arraycopy(this._filename.getBytes(), 0, bArr, 2, length);
        bArr[length + 2] = 0;
        System.arraycopy(_modeBytes[this._mode], 0, bArr, length + 3, length2);
        datagramPacket.setAddress(this._address);
        datagramPacket.setPort(this._port);
        datagramPacket.setData(bArr);
        datagramPacket.setLength(length + length2 + 3);
        return datagramPacket;
    }

    @Override // org.apache.commons.net.tftp.TFTPPacket
    public final DatagramPacket newDatagram() {
        int length = this._filename.length();
        int length2 = _modeBytes[this._mode].length;
        int i = length + length2 + 4;
        byte[] bArr = new byte[i];
        bArr[0] = 0;
        bArr[1] = (byte) this._type;
        System.arraycopy(this._filename.getBytes(), 0, bArr, 2, length);
        bArr[length + 2] = 0;
        System.arraycopy(_modeBytes[this._mode], 0, bArr, length + 3, length2);
        return new DatagramPacket(bArr, i, this._address, this._port);
    }

    public final int getMode() {
        return this._mode;
    }

    public final String getFilename() {
        return this._filename;
    }
}
