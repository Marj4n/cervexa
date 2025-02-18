package com.jieli.lib.dv.control.intercom;

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
import java.util.concurrent.LinkedBlockingDeque;

/* loaded from: classes.dex */
public class IntercomManager {
    private static IntercomManager b;
    private String a = "IntercomManager";
    private DatagramSocket c;
    private InetAddress d;
    private a e;
    private OnSendStateListener f;
    private int g;
    private int h;
    private long i;
    private int j;
    private long k;

    static /* synthetic */ int e(IntercomManager intercomManager) {
        int i = intercomManager.j;
        intercomManager.j = i + 1;
        return i;
    }

    private IntercomManager(String str) {
        createClient(str);
    }

    public static IntercomManager getInstance(String str) {
        if (b == null) {
            synchronized (IntercomManager.class) {
                if (b == null) {
                    b = new IntercomManager(str);
                }
            }
        }
        return b;
    }

    public void createClient(String str) {
        try {
            this.c = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            this.d = InetAddress.getByName(str);
        } catch (UnknownHostException e2) {
            e2.printStackTrace();
        }
    }

    public boolean isSendThreadRunning() {
        a aVar = this.e;
        return aVar != null && aVar.c;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int a(byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            return 0;
        }
        try {
            DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length, this.d, 2231);
            if (this.c == null) {
                return 0;
            }
            this.c.send(datagramPacket);
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            int i = this.h + 1;
            this.h = i;
            if (i >= 3) {
                OnSendStateListener onSendStateListener = this.f;
                if (onSendStateListener == null) {
                    return -1;
                }
                onSendStateListener.onState(1, "Sending failed after try times");
                return -1;
            }
            a(bArr);
            return 0;
        }
    }

    public void initSendThread() {
        a aVar = this.e;
        if (aVar == null) {
            a aVar2 = new a();
            this.e = aVar2;
            aVar2.c = true;
            this.e.start();
            return;
        }
        if (!aVar.isAlive()) {
            a aVar3 = new a();
            this.e = aVar3;
            aVar3.c = true;
            this.e.start();
            return;
        }
        if (this.e.c) {
            return;
        }
        this.e.c = true;
    }

    public void send(byte[] bArr) {
        int i;
        byte[] bArr2;
        a aVar = this.e;
        if (aVar == null || !aVar.c || bArr == null || bArr.length <= 0) {
            return;
        }
        int length = bArr.length;
        if (length % 1448 == 0) {
            i = length / 1448;
        } else {
            i = (length / 1448) + 1;
        }
        int i2 = this.g + 1;
        this.g = i2;
        if (i2 > 0) {
            for (int i3 = 0; i3 < i; i3++) {
                int i4 = i3 * 1448;
                int i5 = length - i4;
                if (i5 >= 1448) {
                    bArr2 = new byte[1448];
                    System.arraycopy(bArr, i4, bArr2, 0, 1448);
                } else {
                    byte[] bArr3 = new byte[i5];
                    System.arraycopy(bArr, i4, bArr3, 0, i5);
                    bArr2 = bArr3;
                }
                StreamData streamData = new StreamData();
                streamData.setType(1);
                streamData.setSave(0);
                streamData.setSeq(this.g);
                streamData.setFrameSize(length);
                streamData.setOffset(i4);
                streamData.setPayload(bArr2);
                streamData.setPayloadLen(bArr2.length);
                streamData.setDateFlag((int) Calendar.getInstance().getTimeInMillis());
                this.e.a(streamData);
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
            IntercomManager.this.h = 0;
        }

        public void a() {
            IntercomManager.this.h = 0;
            IntercomManager.this.g = 0;
            this.c = false;
            this.b.clear();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            byte[] mergeDataPacket;
            while (this.c) {
                StreamData poll = this.b.poll();
                if (poll != null && (mergeDataPacket = Utils.mergeDataPacket(poll)) != null) {
                    int a = IntercomManager.this.a(mergeDataPacket);
                    if (a == 1) {
                        b(poll);
                    }
                    if (a < 0) {
                        a();
                        return;
                    }
                }
            }
        }

        private void b(StreamData streamData) {
            if (streamData == null) {
                return;
            }
            long timeInMillis = Calendar.getInstance().getTimeInMillis();
            if (IntercomManager.this.k == 0) {
                IntercomManager.this.k = timeInMillis;
                return;
            }
            if (timeInMillis - IntercomManager.this.k >= 1000) {
                Dlog.w(IntercomManager.this.a, "frameNum = " + IntercomManager.this.j + ", totalSize = " + IntercomManager.this.i);
                IntercomManager.this.k = 0L;
                IntercomManager.this.j = 0;
                IntercomManager.this.i = 0L;
                return;
            }
            if (streamData.getOffset() == 0) {
                IntercomManager.e(IntercomManager.this);
            }
            IntercomManager.this.i += streamData.getPayloadLen();
        }
    }

    public void stopSendDataThread() {
        a aVar = this.e;
        if (aVar != null) {
            if (aVar.c) {
                this.e.a();
            }
            this.e.interrupt();
            this.e = null;
        }
    }

    public void closeClient() {
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

    public void setOnSendStateListener(OnSendStateListener onSendStateListener) {
        this.f = onSendStateListener;
    }
}
