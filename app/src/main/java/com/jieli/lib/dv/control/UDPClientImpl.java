package com.jieli.lib.dv.control;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.jieli.lib.dv.control.connect.listener.OnConnectStateListener;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.json.bean.CmdInfo;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.receiver.task.UDPCmdReceiver;
import com.jieli.lib.dv.control.utils.ClientContext;
import com.jieli.lib.dv.control.utils.Constants;
import com.jieli.lib.dv.control.utils.Dlog;
import com.jieli.lib.dv.control.utils.proxy.ProxyInterceptor;
import com.jieli.lib.dv.control.utils.proxy.ProxyUtils;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class UDPClientImpl implements Handler.Callback, IClient, ProxyInterceptor {
    public static final int MSG_RECEIVED_DATA = 101;
    private static String a = UDPClientImpl.class.getSimpleName();
    private static IClient b = null;
    private HandlerThread c;
    private Handler d;
    private DatagramSocket e;
    private String f;
    private UDPCmdReceiver h;
    private CopyOnWriteArrayList<OnNotifyListener> i;
    private CopyOnWriteArrayList<OnConnectStateListener> j;
    private int g = -1;
    private int k = -1;

    @Override // com.jieli.lib.dv.control.utils.proxy.ProxyInterceptor
    public boolean onIntercept(Object obj, Method method, Object[] objArr) {
        return false;
    }

    private UDPClientImpl(Context context) {
        HandlerThread handlerThread = new HandlerThread(a);
        this.c = handlerThread;
        handlerThread.start();
        this.d = new Handler(this.c.getLooper(), this);
        this.j = new CopyOnWriteArrayList<>();
        this.i = new CopyOnWriteArrayList<>();
    }

    public static IClient getInstance(Context context) {
        if (b == null) {
            synchronized (UDPClientImpl.class) {
                if (b == null) {
                    UDPClientImpl uDPClientImpl = new UDPClientImpl(context);
                    b = (IClient) ProxyUtils.getProxy(uDPClientImpl, IClient.class, uDPClientImpl);
                }
            }
        }
        return b;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 100:
                a(message);
                break;
            case 101:
                a((NotifyInfo) message.obj);
                break;
            case 102:
                a();
                break;
            case 103:
                b();
                break;
        }
        return false;
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void create(final String str, final int i) {
        Dlog.e(a, "Create host " + str + ", port " + i);
        UDPCmdReceiver uDPCmdReceiver = this.h;
        if (uDPCmdReceiver != null) {
            uDPCmdReceiver.stopRunning();
            this.h = null;
        }
        new Thread(new Runnable() { // from class: com.jieli.lib.dv.control.UDPClientImpl.1
            @Override // java.lang.Runnable
            public void run() {
                UDPClientImpl.this.f = str;
                UDPClientImpl.this.g = i;
                if (UDPClientImpl.this.e != null) {
                    UDPClientImpl.this.e.close();
                    UDPClientImpl.this.e = null;
                }
                try {
                    UDPClientImpl.this.e = new DatagramSocket(UDPClientImpl.this.g);
                    UDPClientImpl.this.e.setReuseAddress(true);
                    UDPClientImpl.this.a(0);
                    if (UDPClientImpl.this.h == null || UDPClientImpl.this.h.isInterrupted()) {
                        UDPClientImpl.this.h = new UDPCmdReceiver(UDPClientImpl.this.e, UDPClientImpl.this.d);
                        UDPClientImpl.this.h.start();
                    }
                } catch (SocketException e) {
                    UDPClientImpl.this.a(4);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override // com.jieli.lib.dv.control.IClient
    public boolean isConnected() {
        return getState() == 0;
    }

    @Override // com.jieli.lib.dv.control.IClient
    public int getState() {
        return this.k;
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void send(CmdInfo cmdInfo, SendResponse sendResponse) {
        this.d.removeMessages(100);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_CMD_INFO, cmdInfo);
        Message obtain = Message.obtain();
        obtain.setData(bundle);
        obtain.obj = sendResponse;
        obtain.what = 100;
        this.d.sendMessage(obtain);
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void close() {
        Handler handler = this.d;
        if (handler != null) {
            handler.removeMessages(102);
            this.d.sendEmptyMessage(102);
        }
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void registerConnectStateListener(OnConnectStateListener onConnectStateListener) {
        if (onConnectStateListener == null || this.j.contains(onConnectStateListener)) {
            return;
        }
        this.j.add(onConnectStateListener);
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void unregisterConnectStateListener(OnConnectStateListener onConnectStateListener) {
        CopyOnWriteArrayList<OnConnectStateListener> copyOnWriteArrayList = this.j;
        if (copyOnWriteArrayList == null || onConnectStateListener == null) {
            return;
        }
        copyOnWriteArrayList.remove(onConnectStateListener);
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void registerNotifyListener(OnNotifyListener onNotifyListener) {
        CopyOnWriteArrayList<OnNotifyListener> copyOnWriteArrayList = this.i;
        if (copyOnWriteArrayList == null || onNotifyListener == null || copyOnWriteArrayList.contains(onNotifyListener)) {
            return;
        }
        this.i.add(onNotifyListener);
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void unregisterNotifyListener(OnNotifyListener onNotifyListener) {
        CopyOnWriteArrayList<OnNotifyListener> copyOnWriteArrayList;
        if (onNotifyListener == null || (copyOnWriteArrayList = this.i) == null) {
            return;
        }
        copyOnWriteArrayList.remove(onNotifyListener);
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void release() {
        Handler handler = this.d;
        if (handler != null) {
            handler.removeMessages(103);
            this.d.sendEmptyMessage(103);
        }
    }

    @Override // com.jieli.lib.dv.control.IClient
    public String getAddress() {
        return this.f;
    }

    private synchronized void a(Message message) {
        CmdInfo cmdInfo = (CmdInfo) message.getData().getParcelable(Constants.EXTRA_CMD_INFO);
        if (cmdInfo == null) {
            throw new NullPointerException("cmdInfo is null");
        }
        SendResponse sendResponse = (SendResponse) message.obj;
        byte[] bArr = ClientImpl.getPackage(cmdInfo);
        if (bArr == null) {
            throw new NullPointerException("Data is null");
        }
        if (this.e == null) {
            a(sendResponse, -1);
        }
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(this.f);
        } catch (UnknownHostException e) {
            a(sendResponse, -1);
            e.printStackTrace();
        }
        if (inetAddress != null && this.g > 0) {
            DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length, inetAddress, this.g);
            try {
                if (this.e != null) {
                    this.e.send(datagramPacket);
                    a(sendResponse, 1);
                } else {
                    a(sendResponse, -1);
                }
            } catch (IOException e2) {
                a(sendResponse, -1);
                e2.printStackTrace();
            }
        } else {
            if (inetAddress == null) {
                throw new IllegalArgumentException("No device IP is specified!");
            }
            if (this.g < 0) {
                throw new IllegalArgumentException("No device's port is specified:" + this.g);
            }
        }
    }

    private void a(final SendResponse sendResponse, final int i) {
        ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.UDPClientImpl.2
            @Override // java.lang.Runnable
            public void run() {
                SendResponse sendResponse2 = sendResponse;
                if (sendResponse2 != null) {
                    sendResponse2.onResponse(Integer.valueOf(i));
                }
            }
        });
    }

    private void a() {
        DatagramSocket datagramSocket = this.e;
        if (datagramSocket != null) {
            datagramSocket.close();
            this.e = null;
        }
        a(1);
        UDPCmdReceiver uDPCmdReceiver = this.h;
        if (uDPCmdReceiver != null) {
            uDPCmdReceiver.stopRunning();
            this.h = null;
        }
    }

    private void b() {
        a();
        CopyOnWriteArrayList<OnNotifyListener> copyOnWriteArrayList = this.i;
        if (copyOnWriteArrayList != null) {
            copyOnWriteArrayList.clear();
            this.i = null;
        }
        CopyOnWriteArrayList<OnConnectStateListener> copyOnWriteArrayList2 = this.j;
        if (copyOnWriteArrayList2 != null) {
            copyOnWriteArrayList2.clear();
            this.j = null;
        }
        if (this.c != null) {
            if (Build.VERSION.SDK_INT >= 18) {
                this.c.quitSafely();
            } else {
                this.c.quit();
            }
            this.c = null;
        }
        Handler handler = this.d;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        this.d = null;
    }

    private void a(final NotifyInfo notifyInfo) {
        Iterator<OnNotifyListener> it = this.i.iterator();
        while (it.hasNext()) {
            final OnNotifyListener next = it.next();
            ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.UDPClientImpl.3
                @Override // java.lang.Runnable
                public void run() {
                    next.onNotify(notifyInfo);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(final int i) {
        this.k = i;
        CopyOnWriteArrayList<OnConnectStateListener> copyOnWriteArrayList = this.j;
        if (copyOnWriteArrayList == null) {
            Dlog.w(a, "OnConnectStateListener is null");
            return;
        }
        Iterator<OnConnectStateListener> it = copyOnWriteArrayList.iterator();
        while (it.hasNext()) {
            final OnConnectStateListener next = it.next();
            ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.UDPClientImpl.4
                @Override // java.lang.Runnable
                public void run() {
                    next.onStateChanged(Integer.valueOf(i));
                }
            });
        }
    }
}
