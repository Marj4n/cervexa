package org.apache.commons.net.tftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.commons.net.io.FromNetASCIIOutputStream;

/* loaded from: classes2.dex */
public class TFTPClient extends TFTP {
    public static final int DEFAULT_MAX_TIMEOUTS = 5;
    private int __maxTimeouts = 5;

    public void setMaxTimeouts(int i) {
        if (i < 1) {
            this.__maxTimeouts = 1;
        } else {
            this.__maxTimeouts = i;
        }
    }

    public int getMaxTimeouts() {
        return this.__maxTimeouts;
    }

    public int receiveFile(String str, int i, OutputStream outputStream, InetAddress inetAddress, int i2) throws IOException {
        TFTPPacket bufferedReceive;
        InetAddress inetAddress2 = inetAddress;
        TFTPAckPacket tFTPAckPacket = new TFTPAckPacket(inetAddress2, i2, 0);
        beginBufferedOps();
        OutputStream fromNetASCIIOutputStream = i == 0 ? new FromNetASCIIOutputStream(outputStream) : outputStream;
        TFTPPacket tFTPReadRequestPacket = new TFTPReadRequestPacket(inetAddress2, i2, str, i);
        int i3 = 0;
        int i4 = 1;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        do {
            bufferedSend(tFTPReadRequestPacket);
            while (true) {
                try {
                    bufferedReceive = bufferedReceive();
                    if (i3 == 0) {
                        i5 = bufferedReceive.getPort();
                        tFTPAckPacket.setPort(i5);
                        if (!inetAddress2.equals(bufferedReceive.getAddress())) {
                            inetAddress2 = bufferedReceive.getAddress();
                            tFTPAckPacket.setAddress(inetAddress2);
                            tFTPReadRequestPacket.setAddress(inetAddress2);
                        }
                    }
                    if (!inetAddress2.equals(bufferedReceive.getAddress()) || bufferedReceive.getPort() != i5) {
                        break;
                    }
                    int type = bufferedReceive.getType();
                    if (type != 3) {
                        if (type == 5) {
                            TFTPErrorPacket tFTPErrorPacket = (TFTPErrorPacket) bufferedReceive;
                            endBufferedOps();
                            throw new IOException("Error code " + tFTPErrorPacket.getError() + " received: " + tFTPErrorPacket.getMessage());
                        }
                        endBufferedOps();
                        throw new IOException("Received unexpected packet type.");
                    }
                    TFTPDataPacket tFTPDataPacket = (TFTPDataPacket) bufferedReceive;
                    int dataLength = tFTPDataPacket.getDataLength();
                    int blockNumber = tFTPDataPacket.getBlockNumber();
                    if (blockNumber == i4) {
                        try {
                            fromNetASCIIOutputStream.write(tFTPDataPacket.getData(), tFTPDataPacket.getDataOffset(), dataLength);
                            i4++;
                            if (i4 > 65535) {
                                i4 = 0;
                            }
                            tFTPAckPacket.setBlockNumber(blockNumber);
                            i6 += dataLength;
                            i7 = dataLength;
                            tFTPReadRequestPacket = tFTPAckPacket;
                            i3 = blockNumber;
                        } catch (IOException e) {
                            bufferedSend(new TFTPErrorPacket(inetAddress2, i5, 3, "File write failed."));
                            endBufferedOps();
                            throw e;
                        }
                    } else {
                        discardPackets();
                        i7 = dataLength;
                        i3 = blockNumber;
                        if (blockNumber == (i4 != 0 ? i4 - 1 : 65535)) {
                            break;
                        }
                    }
                } catch (InterruptedIOException unused) {
                    if (1 >= this.__maxTimeouts) {
                        endBufferedOps();
                        throw new IOException("Connection timed out.");
                    }
                } catch (SocketException unused2) {
                    if (1 >= this.__maxTimeouts) {
                        endBufferedOps();
                        throw new IOException("Connection timed out.");
                    }
                } catch (TFTPPacketException e2) {
                    endBufferedOps();
                    throw new IOException("Bad packet: " + e2.getMessage());
                }
            }
            bufferedSend(new TFTPErrorPacket(bufferedReceive.getAddress(), bufferedReceive.getPort(), 5, "Unexpected host or port."));
        } while (i7 == 512);
        bufferedSend(tFTPReadRequestPacket);
        endBufferedOps();
        return i6;
    }

    public int receiveFile(String str, int i, OutputStream outputStream, String str2, int i2) throws UnknownHostException, IOException {
        return receiveFile(str, i, outputStream, InetAddress.getByName(str2), i2);
    }

    public int receiveFile(String str, int i, OutputStream outputStream, InetAddress inetAddress) throws IOException {
        return receiveFile(str, i, outputStream, inetAddress, 69);
    }

    public int receiveFile(String str, int i, OutputStream outputStream, String str2) throws UnknownHostException, IOException {
        return receiveFile(str, i, outputStream, InetAddress.getByName(str2), 69);
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x012e, code lost:
    
        if (r12 > 0) goto L68;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0130, code lost:
    
        if (r11 != false) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00e6, code lost:
    
        bufferedSend(new org.apache.commons.net.tftp.TFTPErrorPacket(r13.getAddress(), r13.getPort(), 5, "Unexpected host or port."));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void sendFile(java.lang.String r17, int r18, java.io.InputStream r19, java.net.InetAddress r20, int r21) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 319
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.net.tftp.TFTPClient.sendFile(java.lang.String, int, java.io.InputStream, java.net.InetAddress, int):void");
    }

    public void sendFile(String str, int i, InputStream inputStream, String str2, int i2) throws UnknownHostException, IOException {
        sendFile(str, i, inputStream, InetAddress.getByName(str2), i2);
    }

    public void sendFile(String str, int i, InputStream inputStream, InetAddress inetAddress) throws IOException {
        sendFile(str, i, inputStream, inetAddress, 69);
    }

    public void sendFile(String str, int i, InputStream inputStream, String str2) throws UnknownHostException, IOException {
        sendFile(str, i, inputStream, InetAddress.getByName(str2), 69);
    }
}
