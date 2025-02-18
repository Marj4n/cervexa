package com.jieli.lib.dv.control.projection.protocol;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.jieli.lib.dv.control.projection.OnSendStateListener;
import com.jieli.lib.dv.control.projection.beans.StreamData;
import com.jieli.lib.dv.control.projection.tools.Utils;
import com.jieli.lib.dv.control.utils.Dlog;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Calendar;

/* loaded from: classes.dex */
public final class TcpStreaming extends BaseStreaming implements Handler.Callback {
    private Socket b;
    private OnSendStateListener d;
    private HandlerThread g;
    private Handler h;
    private String a = getClass().getSimpleName();
    private volatile OutputStream c = null;
    private int e = 0;
    private int f = 0;
    private final int i = 100;

    public TcpStreaming() {
        HandlerThread handlerThread = new HandlerThread(this.a);
        this.g = handlerThread;
        handlerThread.start();
        this.h = new Handler(this.g.getLooper(), this);
    }

    @Override // com.jieli.lib.dv.control.projection.IPushStream
    public void create(String str) {
        create(str, 2230);
    }

    @Override // com.jieli.lib.dv.control.projection.IPushStream
    public void create(final String str, final int i) {
        new Thread(new Runnable() { // from class: com.jieli.lib.dv.control.projection.protocol.TcpStreaming.1
            @Override // java.lang.Runnable
            public void run() {
                TcpStreaming.this.a(str, i);
            }
        }).start();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.jieli.lib.dv.control.projection.IPushStream
    public void close() {
        synchronized (this) {
            if (this.b != null) {
                if (this.c != null) {
                    try {
                        this.c.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    try {
                        this.b.close();
                        if (this.c != null) {
                            try {
                                this.c.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        this.c = null;
                    } finally {
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                    if (this.c != null) {
                        try {
                            this.c.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    this.c = null;
                }
                this.b = null;
            }
            if (this.g != null) {
                if (Build.VERSION.SDK_INT >= 18) {
                    this.g.quitSafely();
                } else {
                    this.g.quit();
                }
                this.g = null;
            }
            if (this.h != null) {
                this.h.removeCallbacksAndMessages(null);
            }
            this.h = null;
        }
    }

    @Override // com.jieli.lib.dv.control.projection.IPushStream
    public boolean send(int i, byte[] bArr) {
        Handler handler = this.h;
        if (handler != null) {
            handler.removeMessages(100);
        }
        Bundle bundle = new Bundle();
        Message obtain = Message.obtain();
        obtain.setData(bundle);
        obtain.arg1 = i;
        obtain.obj = bArr;
        obtain.what = 100;
        Handler handler2 = this.h;
        if (handler2 == null) {
            return true;
        }
        handler2.sendMessage(obtain);
        return true;
    }

    @Override // com.jieli.lib.dv.control.projection.IPushStream
    public void setOnSendStateListener(OnSendStateListener onSendStateListener) {
        this.d = onSendStateListener;
    }

    private void a(byte[] bArr) {
        if (this.c != null && bArr != null) {
            try {
                this.c.write(bArr, 0, bArr.length);
            } catch (IOException e) {
                OnSendStateListener onSendStateListener = this.d;
                if (onSendStateListener != null) {
                    onSendStateListener.onState(-1, e.getMessage());
                }
                e.printStackTrace();
            }
            OnSendStateListener onSendStateListener2 = this.d;
            if (onSendStateListener2 != null) {
                onSendStateListener2.onState(1, "Successful");
                return;
            }
            return;
        }
        Dlog.w(this.a, "fail to send data");
        OnSendStateListener onSendStateListener3 = this.d;
        if (onSendStateListener3 != null) {
            onSendStateListener3.onState(-1, "Socket maybe has been closed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, int i) {
        try {
            this.b = new Socket(str, i);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Socket socket = this.b;
        if (socket != null) {
            try {
                this.c = socket.getOutputStream();
                return;
            } catch (IOException e2) {
                e2.printStackTrace();
                return;
            }
        }
        OnSendStateListener onSendStateListener = this.d;
        if (onSendStateListener != null) {
            onSendStateListener.onState(-2, "Create socket failure");
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        int i;
        if (message.what == 100) {
            int i2 = message.arg1;
            byte[] bArr = (byte[]) message.obj;
            if (i2 == 2 || i2 == 3) {
                i = this.f + 1;
                this.f = i;
            } else if (i2 == 1) {
                i = this.e + 1;
                this.e = i;
            } else {
                i = 0;
            }
            StreamData streamData = new StreamData();
            streamData.setType(i2);
            streamData.setSave(0);
            streamData.setSeq(i);
            streamData.setFrameSize(bArr.length);
            streamData.setOffset(0);
            streamData.setPayload(bArr);
            streamData.setPayloadLen(bArr.length);
            streamData.setDateFlag((int) Calendar.getInstance().getTimeInMillis());
            byte[] mergeDataPacket = Utils.mergeDataPacket(streamData);
            synchronized (this) {
                a(mergeDataPacket);
            }
        }
        return false;
    }
}
