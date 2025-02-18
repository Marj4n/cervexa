package com.jieli.lib.dv.control.projection;

import android.text.TextUtils;
import com.jieli.lib.dv.control.projection.protocol.TcpStreaming;
import com.jieli.lib.dv.control.projection.protocol.UdpStreaming;

/* loaded from: classes.dex */
public final class StreamingPush implements IPushStream {
    private IPushStream a;

    public enum Protocol {
        TCP,
        UDP
    }

    public StreamingPush() {
        this(Protocol.UDP);
    }

    public StreamingPush(Protocol protocol) {
        if (protocol == Protocol.UDP) {
            this.a = new UdpStreaming();
        } else {
            this.a = new TcpStreaming();
        }
    }

    @Override // com.jieli.lib.dv.control.projection.IPushStream
    public void create(String str) {
        this.a.create(str);
    }

    @Override // com.jieli.lib.dv.control.projection.IPushStream
    public void create(String str, int i) {
        if (TextUtils.isEmpty(str)) {
            throw new NullPointerException("Destination IP is null");
        }
        this.a.create(str, i);
    }

    @Override // com.jieli.lib.dv.control.projection.IPushStream
    public void close() {
        this.a.close();
    }

    @Override // com.jieli.lib.dv.control.projection.IPushStream
    public boolean send(int i, byte[] bArr) {
        return this.a.send(i, bArr);
    }

    @Override // com.jieli.lib.dv.control.projection.IPushStream
    public void setOnSendStateListener(OnSendStateListener onSendStateListener) {
        this.a.setOnSendStateListener(onSendStateListener);
    }
}
