package com.jieli.lib.dv.control.projection;

import android.text.TextUtils;
import com.jieli.lib.dv.control.projection.beans.StreamData;
import com.jieli.lib.dv.control.projection.tools.Utils;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingDeque;

@Deprecated
/* loaded from: classes.dex */
public class UDPSocketManager {
    private static UDPSocketManager b;
    private DatagramSocket c;
    private InetAddress d;
    private String e;
    private a f;
    private int i;
    private OnSocketErrorListener j;
    private String a = "UDPSocketManager";
    private int g = 0;
    private int h = 0;

    public interface OnSocketErrorListener {
        void onError(int i);
    }

    private UDPSocketManager(String str) {
        createUDPSocket(str);
    }

    public static UDPSocketManager getInstance(String str) {
        if (b == null) {
            synchronized (UDPSocketManager.class) {
                if (b == null) {
                    b = new UDPSocketManager(str);
                }
            }
        }
        return b;
    }

    public void createUDPSocket(String str) {
        try {
            this.c = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.e = str;
        try {
            this.d = InetAddress.getByName(str);
        } catch (UnknownHostException e2) {
            e2.printStackTrace();
        }
    }

    public boolean isSendThreadRunning() {
        a aVar = this.f;
        return aVar != null && aVar.c;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int a(byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            return 0;
        }
        try {
            DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length, this.d, 2230);
            if (this.c != null) {
                this.c.send(datagramPacket);
                return 1;
            }
            createUDPSocket(this.e);
            if (this.c == null) {
                return 0;
            }
            this.c.send(datagramPacket);
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            int i = this.i + 1;
            this.i = i;
            if (i >= 3) {
                OnSocketErrorListener onSocketErrorListener = this.j;
                if (onSocketErrorListener == null) {
                    return -1;
                }
                onSocketErrorListener.onError(1);
                return -1;
            }
            a(bArr);
            return 0;
        }
    }

    public void initSendThread() {
        a aVar = this.f;
        if (aVar == null) {
            a aVar2 = new a();
            this.f = aVar2;
            aVar2.c = true;
            this.f.start();
            return;
        }
        if (!aVar.isAlive()) {
            a aVar3 = new a();
            this.f = aVar3;
            aVar3.c = true;
            this.f.start();
            return;
        }
        if (this.f.c) {
            return;
        }
        this.f.c = true;
    }

    public void writeData(int i, byte[] bArr) {
        int i2;
        int i3;
        byte[] bArr2;
        a aVar = this.f;
        if (aVar == null || !aVar.c || bArr == null || bArr.length <= 0) {
            return;
        }
        int length = bArr.length;
        if (length % 1451 == 0) {
            i2 = length / 1451;
        } else {
            i2 = (length / 1451) + 1;
        }
        if (i == 2 || i == 3) {
            i3 = this.h + 1;
            this.h = i3;
        } else if (i == 1) {
            i3 = this.g + 1;
            this.g = i3;
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
                this.f.a(streamData);
            }
        }
    }

    private class a extends Thread {
        private final LinkedBlockingDeque<StreamData> b;
        private boolean c;

        private a() {
            this.b = new LinkedBlockingDeque<>();
            this.c = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(StreamData streamData) {
            if (this.b.remainingCapacity() <= 0) {
                this.b.poll();
                return;
            }
            try {
                this.b.put(streamData);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override // java.lang.Thread
        public synchronized void start() {
            super.start();
            UDPSocketManager.this.i = 0;
        }

        public void a() {
            UDPSocketManager.this.i = 0;
            UDPSocketManager.this.g = 0;
            UDPSocketManager.this.h = 0;
            this.c = false;
            this.b.clear();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            byte[] mergeDataPacket;
            while (this.c) {
                StreamData poll = this.b.poll();
                if (poll != null && (mergeDataPacket = Utils.mergeDataPacket(poll)) != null && UDPSocketManager.this.a(mergeDataPacket) < 0) {
                    a();
                    return;
                }
            }
        }
    }

    public void stopSendDataThread() {
        a aVar = this.f;
        if (aVar != null) {
            if (aVar.c) {
                this.f.a();
            }
            this.f.interrupt();
            this.f = null;
        }
    }

    public void release() {
        b = null;
        stopSendDataThread();
        DatagramSocket datagramSocket = this.c;
        if (datagramSocket != null) {
            datagramSocket.disconnect();
            this.c = null;
        }
        if (this.d != null) {
            this.d = null;
        }
    }

    public void setOnSocketErrorListener(OnSocketErrorListener onSocketErrorListener) {
        this.j = onSocketErrorListener;
    }
}
