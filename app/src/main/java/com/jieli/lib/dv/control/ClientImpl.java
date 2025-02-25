package com.jieli.lib.dv.control;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import androidx.collection.ArrayMap;
import com.jieli.lib.dv.control.connect.listener.OnConnectStateListener;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.json.bean.CmdInfo;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.json.bean.SettingCmd;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.receiver.task.CommandReceiver;
import com.jieli.lib.dv.control.utils.ClientContext;
import com.jieli.lib.dv.control.utils.Constants;
import com.jieli.lib.dv.control.utils.Dlog;
import com.jieli.lib.dv.control.utils.Topic;
import com.jieli.lib.dv.control.utils.proxy.ProxyInterceptor;
import com.jieli.lib.dv.control.utils.proxy.ProxyUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class ClientImpl implements Handler.Callback, IClient, ProxyInterceptor {
    public static final int MSG_RECEIVED_CTP_DATA = 261;
    public static final int MSG_SOCKET_STATE = 257;
    static String a = ClientImpl.class.getSimpleName();
    private static IClient b = null;
    private Socket c;
    private HandlerThread g;
    private Handler h;
    private int k;
    private String l;
    private volatile OutputStream d = null;
    private CommandReceiver i = null;
    private int j = -1;
    private HashSet<OnConnectStateListener> e = new HashSet<>();
    private CopyOnWriteArrayList<OnNotifyListener> f = new CopyOnWriteArrayList<>();

    @Override // com.jieli.lib.dv.control.utils.proxy.ProxyInterceptor
    public boolean onIntercept(Object obj, Method method, Object[] objArr) {
        return false;
    }

    private ClientImpl(Context context) {
        HandlerThread handlerThread = new HandlerThread(a);
        this.g = handlerThread;
        handlerThread.start();
        this.h = new Handler(this.g.getLooper(), this);
    }

    public static IClient getInstance(Context context) {
        if (b == null) {
            synchronized (ClientImpl.class) {
                if (b == null) {
                    ClientImpl clientImpl = new ClientImpl(context);
                    b = (IClient) ProxyUtils.getProxy(clientImpl, IClient.class, clientImpl);
                }
            }
        }
        return b;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        int i = message.what;
        if (i == 257) {
            b(message.arg1);
            return false;
        }
        if (i == 263) {
            c();
            return false;
        }
        if (i == 260) {
            a(message);
            return false;
        }
        if (i != 261) {
            return false;
        }
        a((NotifyInfo) message.obj);
        return false;
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void create(String str, int i) {
        a(str);
        a(str, i);
    }

    @Override // com.jieli.lib.dv.control.IClient
    public boolean isConnected() {
        return getState() == 0;
    }

    @Override // com.jieli.lib.dv.control.IClient
    public int getState() {
        return this.j;
    }

    private void a(int i) {
        this.j = i;
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void send(CmdInfo cmdInfo, SendResponse sendResponse) {
        if (getState() != 0) {
            if (sendResponse != null) {
                sendResponse.onResponse(-1);
            }
            Dlog.w(a, "Not connected:" + getState());
            return;
        }
        Handler handler = this.h;
        if (handler != null) {
            handler.removeMessages(260);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_CMD_INFO, cmdInfo);
        Message obtain = Message.obtain();
        obtain.setData(bundle);
        obtain.obj = sendResponse;
        obtain.what = 260;
        Handler handler2 = this.h;
        if (handler2 != null) {
            handler2.sendMessage(obtain);
        }
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void close() {
        a();
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void registerConnectStateListener(OnConnectStateListener onConnectStateListener) {
        HashSet<OnConnectStateListener> hashSet = this.e;
        if (hashSet == null || onConnectStateListener == null || hashSet.contains(onConnectStateListener)) {
            return;
        }
        this.e.add(onConnectStateListener);
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void unregisterConnectStateListener(OnConnectStateListener onConnectStateListener) {
        HashSet<OnConnectStateListener> hashSet = this.e;
        if (hashSet == null || onConnectStateListener == null) {
            return;
        }
        hashSet.remove(onConnectStateListener);
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void registerNotifyListener(OnNotifyListener onNotifyListener) {
        CopyOnWriteArrayList<OnNotifyListener> copyOnWriteArrayList = this.f;
        if (copyOnWriteArrayList == null || onNotifyListener == null || copyOnWriteArrayList.contains(onNotifyListener)) {
            return;
        }
        this.f.add(onNotifyListener);
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void unregisterNotifyListener(OnNotifyListener onNotifyListener) {
        CopyOnWriteArrayList<OnNotifyListener> copyOnWriteArrayList = this.f;
        if (copyOnWriteArrayList == null || onNotifyListener == null) {
            return;
        }
        copyOnWriteArrayList.remove(onNotifyListener);
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void release() {
        b();
    }

    @Override // com.jieli.lib.dv.control.IClient
    public String getAddress() {
        return this.l;
    }

    private void a(String str) {
        this.l = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(final int i) {
        a(i);
        if (i != 0 && i != 2) {
            a((String) null);
        }
        HashSet<OnConnectStateListener> hashSet = this.e;
        if (hashSet == null) {
            Dlog.w(a, "OnConnectStateListener is null");
            return;
        }
        Iterator<OnConnectStateListener> it = hashSet.iterator();
        while (it.hasNext()) {
            final OnConnectStateListener next = it.next();
        }
    }

    private synchronized void a(final NotifyInfo notifyInfo) {
        if (this.f == null) {
            return;
        }
        Iterator it = ((CopyOnWriteArrayList) this.f.clone()).iterator();
        while (it.hasNext()) {
            final OnNotifyListener onNotifyListener = (OnNotifyListener) it.next();
            ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.ClientImpl.2
                @Override // java.lang.Runnable
                public void run() {
                    onNotifyListener.onNotify(notifyInfo);
                }
            });
        }
    }

    private void a(final String str, final int i) {
        if (getState() == 2) {
            Dlog.w(a, "It is connecting ...");
            return;
        }
        CommandReceiver commandReceiver = this.i;
        if (commandReceiver != null) {
            commandReceiver.stopRunning();
            this.i = null;
        }
        new Thread(new Runnable() { // from class: com.jieli.lib.dv.control.ClientImpl.3
            @Override // java.lang.Runnable
            public void run() {
                ClientImpl.this.b(2);
                int i2 = 0;
                while (true) {
                    try {
                        ClientImpl.this.c = new Socket(str, i);
                    } catch (IOException unused) {
                        i2++;
                        ClientImpl.this.c = null;
                        if (i2 >= 5) {
                            ClientImpl.this.b(3);
                            Dlog.w(ClientImpl.a, "ERROR_CONNECTION_EXCEPTION： tryToConnect " + i2);
                            return;
                        }
                    }
                    if (ClientImpl.this.c != null) {
                        try {
                            ClientImpl.this.d = ClientImpl.this.c.getOutputStream();
                        } catch (IOException unused2) {
                            Dlog.w(ClientImpl.a, "getOutputStream exception： 4");
                            ClientImpl.this.b(4);
                        }
                        if (ClientImpl.this.d != null) {
                            ClientImpl.this.b(0);
                            ClientImpl.this.d();
                            return;
                        } else {
                            Dlog.w(ClientImpl.a, "ERROR_CONNECTION_EXCEPTION： mOutputStream is null");
                            ClientImpl.this.b(4);
                            return;
                        }
                    }
                    SystemClock.sleep(1000L);
                }
            }
        }).start();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void a() {
        Socket socket = this.c;
        if (socket != null) {
            try {
                try {
                    socket.close();
                    b(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                this.d = null;
                this.c = null;
                a(-1);
            }
        }
        CommandReceiver commandReceiver = this.i;
        if (commandReceiver != null) {
            commandReceiver.stopRunning();
            this.i = null;
        }
    }

    private void b() {
        a();
        HashSet<OnConnectStateListener> hashSet = this.e;
        if (hashSet != null) {
            hashSet.clear();
        }
        CopyOnWriteArrayList<OnNotifyListener> copyOnWriteArrayList = this.f;
        if (copyOnWriteArrayList != null) {
            copyOnWriteArrayList.clear();
            this.f = null;
        }
        if (this.g != null) {
            if (Build.VERSION.SDK_INT >= 18) {
                this.g.quitSafely();
            } else {
                this.g.quit();
            }
            this.g = null;
        }
        Handler handler = this.h;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        this.h = null;
        b = null;
    }

    private synchronized void a(Message message) {
        SendResponse sendResponse = (SendResponse) message.obj;
        CmdInfo cmdInfo = (CmdInfo) message.getData().getParcelable(Constants.EXTRA_CMD_INFO);
        if (cmdInfo == null) {
            throw new NullPointerException("cmdInfo is null");
        }
        byte[] bArr = getPackage(cmdInfo);
        if (bArr == null) {
            throw new NullPointerException("Data is null");
        }
        try {
            if (this.d != null) {
                this.d.write(bArr, 0, bArr.length);
                a(sendResponse, 1);
                this.k = 0;
            } else {
                Dlog.w(a, "OutputStream is null");
            }
        } catch (IOException unused) {
            a(sendResponse, -1);
            int i = this.k + 1;
            this.k = i;
            if (i >= 3) {
                this.k = 0;
                Dlog.e(a, "Send Error :retryNum=" + this.k);
                b(4);
            } else {
                a(message);
            }
        }
    }

    private void c() {
        if (this.d != null) {
            SettingCmd settingCmd = new SettingCmd();
            settingCmd.setTopic(Topic.KEEP_ALIVE);
            byte[] bArr = getPackage(settingCmd);
            try {
                if (this.d != null) {
                    this.d.write(bArr, 0, bArr.length);
                }
                a(0);
                this.k = 0;
            } catch (IOException unused) {
                int i = this.k + 1;
                this.k = i;
                if (i >= 3) {
                    Dlog.e(a, "checkSocketAlive error : retryNum=" + this.k);
                    a(4);
                    this.k = 0;
                    return;
                }
                c();
            }
        }
    }

    private void a(final SendResponse sendResponse, final int i) {
        ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.ClientImpl.4
            @Override // java.lang.Runnable
            public void run() {
                SendResponse sendResponse2 = sendResponse;
                if (sendResponse2 != null) {
                    sendResponse2.onResponse(Integer.valueOf(i));
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        CommandReceiver commandReceiver = this.i;
        if (commandReceiver == null || commandReceiver.isInterrupted()) {
            CommandReceiver commandReceiver2 = new CommandReceiver(this.c, this.h);
            this.i = commandReceiver2;
            commandReceiver2.start();
        }
    }

    public static byte[] getPackage(CmdInfo cmdInfo) {
        byte[] bArr;
        int i;
        byte[] bytes = Constants.CTP_SIGNATURE.getBytes();
        byte[] bytes2 = cmdInfo.getTopic().getBytes();
        short length = (short) bytes2.length;
        int i2 = 6 + length + 4;
        if (TextUtils.isEmpty(cmdInfo.getOperation())) {
            bArr = null;
            i = 0;
        } else {
            bArr = a(cmdInfo).getBytes();
            i = bArr.length;
            i2 += i;
        }
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bytes, 0, bArr2, 0, bytes.length);
        int length2 = bytes.length + 0;
        ByteBuffer order = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        order.putShort(length);
        System.arraycopy(order.array(), 0, bArr2, length2, 2);
        order.clear();
        int i3 = length2 + 2;
        System.arraycopy(bytes2, 0, bArr2, i3, length);
        int i4 = i3 + length;
        ByteBuffer order2 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        order2.putInt(i);
        System.arraycopy(order2.array(), 0, bArr2, i4, 4);
        order2.clear();
        int i5 = i4 + 4;
        if (bArr != null) {
            System.arraycopy(bArr, 0, bArr2, i5, i);
        }
        return bArr2;
    }

    private static String a(CmdInfo cmdInfo) {
        if (cmdInfo == null) {
            throw new NullPointerException("cmd info is null");
        }
        String str = (cmdInfo.getErrorType() != -100 ? "{\"errno\":" + cmdInfo.getErrorType() + "," : "{") + "\"op\":\"" + cmdInfo.getOperation() + "\"";
        ArrayMap<String, String> params = cmdInfo.getParams();
        if (params != null) {
            String str2 = ((str + ",") + "\"param\":") + "{";
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                str2 = str2 + "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"";
                if (i == params.size() - 1) {
                    break;
                }
                str2 = str2 + ",";
                i++;
            }
            str = str2 + "}";
        }
        return str + "}";
    }
}
