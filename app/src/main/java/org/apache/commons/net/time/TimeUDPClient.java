package org.apache.commons.net.time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Date;
import kotlin.UByte;
import org.apache.commons.net.DatagramSocketClient;

/* loaded from: classes2.dex */
public final class TimeUDPClient extends DatagramSocketClient {
    public static final int DEFAULT_PORT = 37;
    public static final long SECONDS_1900_TO_1970 = 2208988800L;
    private final byte[] __dummyData = new byte[1];
    private final byte[] __timeData = new byte[4];

    public long getTime(InetAddress inetAddress, int i) throws IOException {
        byte[] bArr = this.__dummyData;
        DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length, inetAddress, i);
        byte[] bArr2 = this.__timeData;
        DatagramPacket datagramPacket2 = new DatagramPacket(bArr2, bArr2.length);
        this._socket_.send(datagramPacket);
        this._socket_.receive(datagramPacket2);
        byte[] bArr3 = this.__timeData;
        return (bArr3[3] & UByte.MAX_VALUE & 4294967295L) | (((bArr3[0] & UByte.MAX_VALUE) << 24) & 4294967295L) | 0 | (((bArr3[1] & UByte.MAX_VALUE) << 16) & 4294967295L) | (((bArr3[2] & UByte.MAX_VALUE) << 8) & 4294967295L);
    }

    public long getTime(InetAddress inetAddress) throws IOException {
        return getTime(inetAddress, 37);
    }

    public Date getDate(InetAddress inetAddress, int i) throws IOException {
        return new Date((getTime(inetAddress, i) - 2208988800L) * 1000);
    }

    public Date getDate(InetAddress inetAddress) throws IOException {
        return new Date((getTime(inetAddress, 37) - 2208988800L) * 1000);
    }
}
