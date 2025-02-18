package com.jieli.lib.dv.control.mssdp;

import android.os.SystemClock;
import android.text.TextUtils;
import com.jieli.lib.dv.control.utils.Dlog;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes.dex */
public class Discovery {
    public static final String BROADCAST_IP = "255.255.255.255";
    public static final String BROADCAST_REPLY = "MSSDP_NOTIFY";
    public static final String DISCOVERY_MSG = "MSSDP_SEARCH ";
    public static final int DISCOVERY_PORT = 3889;
    private static final String a = Discovery.class.getSimpleName();
    private static Discovery b;
    private DatagramSocket c;
    private InetAddress d;
    private Set<OnDiscoveryListener> e;
    private a f;
    private boolean j;
    private int g = DISCOVERY_PORT;
    private int h = 50;
    private int i = 3;
    private String l = DISCOVERY_MSG;
    private String m = BROADCAST_REPLY;
    private String k = d();

    public interface OnDiscoveryListener {
        void onDiscovery(String str, String str2);

        void onError(int i, String str);
    }

    public static Discovery newInstance() {
        if (b == null) {
            synchronized (Discovery.class) {
                if (b == null) {
                    b = new Discovery();
                }
            }
        }
        return b;
    }

    private Discovery() {
    }

    public void setBroadCastFlag(String str) {
        if (TextUtils.isEmpty(str) || str.equals(this.m)) {
            return;
        }
        this.m = str;
    }

    public void setInterval(int i) {
        if (i > 0) {
            this.h = i;
        }
    }

    public int getInterval() {
        return this.h;
    }

    public void setRepeatNumber(int i) {
        if (i > 0) {
            this.i = i;
        }
    }

    public int getRepeatNumber() {
        return this.i;
    }

    public void setFilter(boolean z) {
        this.j = z;
    }

    public boolean isFilter() {
        return this.j;
    }

    public boolean registerOnDiscoveryListener(OnDiscoveryListener onDiscoveryListener) {
        if (this.e == null) {
            this.e = new HashSet();
        }
        return this.e.add(onDiscoveryListener);
    }

    public boolean unregisterOnDiscoveryListener(OnDiscoveryListener onDiscoveryListener) {
        Set<OnDiscoveryListener> set = this.e;
        return set != null && set.remove(onDiscoveryListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, String str2) {
        Set<OnDiscoveryListener> set = this.e;
        if (set != null) {
            for (OnDiscoveryListener onDiscoveryListener : set) {
                if (onDiscoveryListener != null) {
                    onDiscoveryListener.onDiscovery(str, str2);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i, String str) {
        Set<OnDiscoveryListener> set = this.e;
        if (set != null) {
            for (OnDiscoveryListener onDiscoveryListener : set) {
                if (onDiscoveryListener != null) {
                    onDiscoveryListener.onError(i, str);
                }
            }
        }
    }

    private void b() {
        try {
            DatagramSocket datagramSocket = new DatagramSocket(this.g);
            this.c = datagramSocket;
            datagramSocket.setBroadcast(true);
            this.d = InetAddress.getByName(this.k);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InetAddress c() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface nextElement = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = nextElement.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress nextElement2 = inetAddresses.nextElement();
                    if (!nextElement2.isLoopbackAddress() && (nextElement.getDisplayName().contains("wlan0") || nextElement.getDisplayName().contains("eth0") || nextElement.getDisplayName().contains("ap0"))) {
                        Dlog.i(a, "Current IP Address:" + nextElement2);
                        return nextElement2;
                    }
                }
            }
            return null;
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String d() {
        InetAddress broadcast;
        InetAddress c = c();
        if (c == null) {
            return null;
        }
        try {
            for (InterfaceAddress interfaceAddress : NetworkInterface.getByInetAddress(c).getInterfaceAddresses()) {
                if (interfaceAddress != null && (broadcast = interfaceAddress.getBroadcast()) != null) {
                    String hostAddress = broadcast.getHostAddress();
                    Dlog.i(a, "myAddress=" + hostAddress);
                    return hostAddress;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void doDiscovery() {
        doDiscovery(DISCOVERY_PORT, this.k, DISCOVERY_MSG);
    }

    public synchronized void doDiscovery(int i, String str, String str2) {
        if (i >= 0) {
            if (this.g != i) {
                this.g = i;
            }
        }
        if (!TextUtils.isEmpty(str) && !str.equals(this.k)) {
            this.k = str;
        }
        if (!TextUtils.isEmpty(str2) && !str2.equals(this.l)) {
            this.l = str2;
        }
        if (this.c == null) {
            b();
        }
        if (this.c != null && (this.f == null || !this.f.a())) {
            a aVar = new a(this.c);
            this.f = aVar;
            aVar.start();
        }
        if (!TextUtils.isEmpty(this.l)) {
            for (int i2 = 0; i2 < this.i; i2++) {
                a(this.l.getBytes());
                SystemClock.sleep(this.h);
            }
        }
    }

    private synchronized void a(byte[] bArr) {
        if (bArr != null) {
            try {
                DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length, this.d, this.g);
                if (this.c != null) {
                    this.c.send(datagramPacket);
                }
            } catch (IOException e) {
                e.printStackTrace();
                a(e.hashCode(), e.getMessage());
            }
        }
    }

    public void release() {
        b = null;
        DatagramSocket datagramSocket = this.c;
        if (datagramSocket != null) {
            datagramSocket.close();
            this.c = null;
        }
        a aVar = this.f;
        if (aVar != null) {
            aVar.b();
            this.f = null;
        }
        this.d = null;
        Set<OnDiscoveryListener> set = this.e;
        if (set != null) {
            set.clear();
            this.e = null;
        }
    }

    private class a extends Thread {
        private boolean b;
        private DatagramSocket c;
        private Set<String> d;
        private String e;

        a(DatagramSocket datagramSocket) {
            this.c = datagramSocket;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean a() {
            return this.b;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void b() {
            this.b = false;
            interrupt();
        }

        @Override // java.lang.Thread
        public synchronized void start() {
            this.b = true;
            this.d = new HashSet();
            super.start();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            Dlog.i(Discovery.a, "ReceiverThread start...");
            while (this.b) {
                try {
                    DatagramPacket datagramPacket = new DatagramPacket(new byte[20480], 20480);
                    if (this.c != null) {
                        this.c.receive(datagramPacket);
                    }
                    if (datagramPacket.getLength() > 0) {
                        String trim = new String(datagramPacket.getData()).trim();
                        if (!TextUtils.isEmpty(trim) && trim.startsWith(Discovery.this.m) && datagramPacket.getAddress() != null) {
                            String hostAddress = datagramPacket.getAddress().getHostAddress();
                            String[] split = trim.split(" ", 2);
                            String str = split.length > 1 ? split[1] : null;
                            if (!Discovery.this.j) {
                                Discovery.this.a(hostAddress, str);
                            } else if (this.d.add(hostAddress)) {
                                this.e = str;
                                Discovery.this.a(hostAddress, str);
                            } else if (TextUtils.isEmpty(str) || str.equals(this.e)) {
                                Dlog.w(Discovery.a, "Maybe data is repeat");
                            } else {
                                this.e = str;
                                Discovery.this.a(hostAddress, str);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    this.b = false;
                    Discovery.this.a(e.hashCode(), e.getMessage());
                }
            }
            this.d.clear();
            this.b = false;
            Dlog.i(Discovery.a, "ReceiverThread stop...");
            Discovery.this.f = null;
        }
    }
}
