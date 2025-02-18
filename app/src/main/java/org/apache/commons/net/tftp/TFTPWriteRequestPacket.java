package org.apache.commons.net.tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;

/* loaded from: classes2.dex */
public final class TFTPWriteRequestPacket extends TFTPRequestPacket {
    public TFTPWriteRequestPacket(InetAddress inetAddress, int i, String str, int i2) {
        super(inetAddress, i, 2, str, i2);
    }

    TFTPWriteRequestPacket(DatagramPacket datagramPacket) throws TFTPPacketException {
        super(2, datagramPacket);
    }
}
