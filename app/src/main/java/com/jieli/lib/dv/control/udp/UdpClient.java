package com.jieli.lib.dv.control.udp;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import com.jieli.lib.dv.control.ClientImpl;
import com.jieli.lib.dv.control.connect.listener.OnConnectStateListener;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.json.bean.CmdInfo;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.utils.ClientContext;
import com.jieli.lib.dv.control.utils.Constants;
import com.jieli.lib.dv.control.utils.Dlog;
import com.jieli.lib.dv.control.utils.ListenerHelper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/* loaded from: classes.dex */
public class UdpClient implements Handler.Callback {
    public static final int UDP_CLIENT_PORT = 2228;
    private static String a = "UdpClient";
    private static UdpClient b;
    private HandlerThread c;
    private Handler d;
    private DatagramSocket e;
    private InetAddress f;
    private UdpSocketReceiver g;
    private String h;
    private int i;

    public static UdpClient getInstance() {
        if (b == null) {
            synchronized (UdpClient.class) {
                if (b == null) {
                    b = new UdpClient();
                }
            }
        }
        return b;
    }

    public String getServerAddress() {
        return this.h;
    }

    public int getServerPort() {
        return this.i;
    }

    public void createClient(String str, int i) {
        if (TextUtils.isEmpty(str)) {
            Dlog.w(a, "address is null.");
            return;
        }
        Dlog.i(a, "create udp channel.");
        this.i = i;
        this.h = str;
        try {
            this.e = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            this.f = InetAddress.getByName(this.h);
        } catch (UnknownHostException e2) {
            e2.printStackTrace();
        }
        if (this.e == null || this.f == null) {
            return;
        }
        Dlog.w(a, "init udp channel ok.");
        UdpSocketReceiver udpSocketReceiver = this.g;
        if (udpSocketReceiver == null || !udpSocketReceiver.isReceiver()) {
            this.g = new UdpSocketReceiver(this.e);
            Dlog.w(a, "start Receiver thread ok.");
            this.g.start();
        }
        if (this.c == null) {
            HandlerThread handlerThread = new HandlerThread(a);
            this.c = handlerThread;
            handlerThread.start();
            this.d = new Handler(this.c.getLooper(), this);
        }
    }

    public void registerConnectStateListener(OnConnectStateListener onConnectStateListener) {
        ListenerHelper.getInstance().addConnectStateListener(onConnectStateListener);
    }

    public void unregisterConnectStateListener(OnConnectStateListener onConnectStateListener) {
        ListenerHelper.getInstance().removeConnectStateListener(onConnectStateListener);
    }

    public void registerNotifyListener(OnNotifyListener onNotifyListener) {
        ListenerHelper.getInstance().addNotifyListener(onNotifyListener);
    }

    public void unregisterNotifyListener(OnNotifyListener onNotifyListener) {
        ListenerHelper.getInstance().removeNotifyListener(onNotifyListener);
    }

    public void send(CmdInfo cmdInfo, SendResponse sendResponse) {
        Handler handler = this.d;
        if (handler == null) {
            a(sendResponse, -1);
            return;
        }
        handler.removeMessages(516);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_CMD_INFO, cmdInfo);
        Message obtain = Message.obtain();
        obtain.setData(bundle);
        obtain.obj = sendResponse;
        obtain.what = 516;
        this.d.sendMessage(obtain);
    }

    private synchronized void a(CmdInfo cmdInfo, SendResponse sendResponse) {
        if (cmdInfo != null) {
            byte[] bArr = ClientImpl.getPackage(cmdInfo);
            if (bArr == null) {
                a(sendResponse, -1);
                return;
            }
            if (this.e != null) {
                try {
                    this.e.send(new DatagramPacket(bArr, bArr.length, this.f, this.i));
                    a(sendResponse, 1);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            a(sendResponse, -1);
        } else {
            a(sendResponse, -1);
        }
    }

    private void a(final SendResponse sendResponse, final int i) {
        ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.udp.UdpClient.1
            @Override // java.lang.Runnable
            public void run() {
                SendResponse sendResponse2 = sendResponse;
                if (sendResponse2 != null) {
                    sendResponse2.onResponse(Integer.valueOf(i));
                }
            }
        });
    }

    public void closeClient(boolean z) {
        b = null;
        UdpSocketReceiver udpSocketReceiver = this.g;
        if (udpSocketReceiver != null) {
            if (udpSocketReceiver.isReceiver()) {
                this.g.stopReceiver();
            }
            this.g = null;
        }
        DatagramSocket datagramSocket = this.e;
        if (datagramSocket != null) {
            datagramSocket.close();
            this.e = null;
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
        if (z) {
            ListenerHelper.getInstance().release();
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (message == null || message.what != 516) {
            return false;
        }
        SendResponse sendResponse = (SendResponse) message.obj;
        Bundle data = message.getData();
        if (data != null) {
            a((CmdInfo) data.getParcelable(Constants.EXTRA_CMD_INFO), sendResponse);
            return false;
        }
        a(sendResponse, -1);
        return false;
    }
}
