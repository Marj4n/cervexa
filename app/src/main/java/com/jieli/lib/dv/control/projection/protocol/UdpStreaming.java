package com.jieli.lib.dv.control.projection.protocol;

import android.text.TextUtils;
import com.jieli.lib.dv.control.projection.OnSendStateListener;
import com.jieli.lib.dv.control.projection.beans.StreamData;
import com.jieli.lib.dv.control.projection.tools.Utils;
import com.jieli.lib.dv.control.utils.Dlog;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;

/* loaded from: classes.dex */
public final class UdpStreaming extends BaseStreaming {
    private String a = getClass().getSimpleName();
    private int b = 0;
    private int c = 0;
    private DatagramSocket d;
    private InetAddress e;
    private int f;
    private OnSendStateListener g;

    @Override // com.jieli.lib.dv.control.projection.IPushStream
    public void create(String str) {
        create(str, 2230);
    }

    @Override // com.jieli.lib.dv.control.projection.IPushStream
    public void create(String str, int i) {
        this.f = i;
        if (TextUtils.isEmpty(str)) {
            throw new NullPointerException("Destination IP is null");
        }
        try {
            this.d = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            this.e = InetAddress.getByName(str);
        } catch (UnknownHostException e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.jieli.lib.dv.control.projection.IPushStream
    public void close() {
        synchronized (this) {
            if (this.d != null) {
                this.d.disconnect();
                this.d.close();
                this.d = null;
            }
            if (this.e != null) {
                this.e = null;
            }
            this.g = null;
        }
    }

    @Override // com.jieli.lib.dv.control.projection.IPushStream
    public boolean send(int i, byte[] bArr) {
        if (a(i, bArr)) {
            OnSendStateListener onSendStateListener = this.g;
            if (onSendStateListener != null) {
                onSendStateListener.onState(1, "Successful");
            }
            return true;
        }
        OnSendStateListener onSendStateListener2 = this.g;
        if (onSendStateListener2 == null) {
            return false;
        }
        onSendStateListener2.onState(-1, "Failed: data incorrect");
        return false;
    }

    @Override // com.jieli.lib.dv.control.projection.IPushStream
    public void setOnSendStateListener(OnSendStateListener onSendStateListener) {
        this.g = onSendStateListener;
    }

    private void a(byte[] bArr) {
        if (bArr != null && bArr.length > 0) {
            synchronized (this) {
                try {
                    DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length, this.e, this.f);
                    if (this.d != null) {
                        this.d.send(datagramPacket);
                    } else if (this.g != null) {
                        this.g.onState(-1, "Socket was destroy");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (this.g != null) {
                        this.g.onState(-1, e.getMessage());
                    }
                }
            }
            return;
        }
        Dlog.e(this.a, "data maybe is null");
    }

    private boolean a(int i, byte[] bArr) {
        int i2;
        int i3;
        byte[] bArr2;
        if (bArr != null && bArr.length > 0) {
            int length = bArr.length;
            if (length % 1451 == 0) {
                i2 = length / 1451;
            } else {
                i2 = (length / 1451) + 1;
            }
            if (i == 2 || i == 3) {
                i3 = this.c + 1;
                this.c = i3;
            } else if (i == 1) {
                i3 = this.b + 1;
                this.b = i3;
            } else {
                i3 = 0;
            }
            if (i3 > 0) {
                for (int i4 = 0; i4 < i2; i4++) {
                    int i5 = i4 * 1451;
                    int i6 = length - i5;
                    if (i6 >= 1451) {
                        bArr2 = new byte[1451];
                        System.arraycopy(bArr, i5, bArr2, 0, 1451);
                    } else {
                        byte[] bArr3 = new byte[i6];
                        System.arraycopy(bArr, i5, bArr3, 0, i6);
                        bArr2 = bArr3;
                    }
                    StreamData streamData = new StreamData();
                    streamData.setType(i);
                    streamData.setSave(0);
                    streamData.setSeq(i3);
                    streamData.setFrameSize(length);
                    streamData.setOffset(i5);
                    streamData.setPayload(bArr2);
                    streamData.setPayloadLen(bArr2.length);
                    streamData.setDateFlag((int) Calendar.getInstance().getTimeInMillis());
                    byte[] mergeDataPacket = Utils.mergeDataPacket(streamData);
                    if (mergeDataPacket != null) {
                        a(mergeDataPacket);
                    }
                }
                return true;
            }
        }
        return false;
    }
}
