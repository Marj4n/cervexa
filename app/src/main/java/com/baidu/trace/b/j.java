package com.baidu.trace.b;

import com.baidu.trace.b.a;
import com.jieli.stream.dv.running2.util.IConstant;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/* loaded from: classes.dex */
public final class j implements b {
    private Socket a = null;
    private DataOutputStream b = null;

    private j() {
    }

    protected static j e() {
        return new j();
    }

    @Override // com.baidu.trace.b.b
    public final void a() {
        try {
            if (this.b != null) {
                this.b.close();
            }
            if (this.a != null && !this.a.isClosed()) {
                this.a.close();
            }
        } catch (Exception unused) {
        } catch (Throwable th) {
            this.b = null;
            this.a = null;
            throw th;
        }
        this.b = null;
        this.a = null;
    }

    @Override // com.baidu.trace.b.b
    public final void a(e eVar) {
        try {
            Socket socket = new Socket();
            this.a = socket;
            socket.connect(new InetSocketAddress("gateway.yingyan.baidu.com", 8300), IConstant.AUDIO_SAMPLE_RATE_DEFAULT);
            this.a.setKeepAlive(true);
            if (b()) {
                eVar.a();
            } else {
                eVar.b();
            }
        } catch (Exception unused) {
            if (a.EnumC0016a.a == a.a()) {
                eVar.b();
            }
        } catch (Throwable unused2) {
            if (a.EnumC0016a.a == a.a()) {
                eVar.b();
            }
        }
    }

    @Override // com.baidu.trace.b.b
    public final void a(byte[] bArr) throws IOException {
        if (this.b == null) {
            this.b = new DataOutputStream(this.a.getOutputStream());
        }
        if (bArr != null) {
            this.b.write(bArr);
            this.b.flush();
        }
    }

    @Override // com.baidu.trace.b.b
    public final boolean b() {
        Socket socket = this.a;
        return (socket == null || !socket.isConnected() || this.a.isClosed() || this.a.isInputShutdown() || this.a.isOutputShutdown()) ? false : true;
    }

    @Override // com.baidu.trace.b.b
    public final int c() {
        Socket socket = this.a;
        if (socket != null) {
            return socket.getLocalPort();
        }
        return 0;
    }

    @Override // com.baidu.trace.b.b
    public final InputStream d() throws IOException {
        return this.a.getInputStream();
    }
}
