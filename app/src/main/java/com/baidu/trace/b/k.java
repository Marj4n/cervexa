package com.baidu.trace.b;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/* loaded from: classes.dex */
public final class k implements b {
    private DatagramSocket a = null;
    private DatagramPacket b = null;
    private InetAddress c = null;
    private int d;

    private k(int i) {
        this.d = 8300;
        this.d = i;
    }

    protected static k a(int i) {
        return new k(i);
    }

    @Override // com.baidu.trace.b.b
    public final void a() {
        try {
            this.b = null;
            this.c = null;
            if (this.a != null && !this.a.isClosed()) {
                this.a.close();
            }
        } catch (Exception unused) {
        } finally {
            this.a = null;
        }
    }

    @Override // com.baidu.trace.b.b
    public final void a(e eVar) {
        try {
            this.a = new DatagramSocket(this.d);
            this.c = InetAddress.getByName("gateway.yingyan.baidu.com");
            this.b = new DatagramPacket((byte[]) null, 0, this.c, 8300);
        } catch (IOException unused) {
            com.baidu.trace.a.a("udp socket connect failed");
        }
    }

    @Override // com.baidu.trace.b.b
    public final void a(byte[] bArr) throws IOException {
        DatagramPacket datagramPacket = this.b;
        if (datagramPacket == null) {
            this.b = new DatagramPacket(bArr, bArr.length, this.c, 8300);
        } else {
            datagramPacket.setData(bArr);
            this.b.setLength(bArr.length);
        }
        this.a.send(this.b);
    }

    @Override // com.baidu.trace.b.b
    public final boolean b() {
        return true;
    }

    @Override // com.baidu.trace.b.b
    public final int c() {
        return this.d;
    }

    @Override // com.baidu.trace.b.b
    public final InputStream d() throws IOException {
        return null;
    }
}
