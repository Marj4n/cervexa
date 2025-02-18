package com.jieli.lib.dv.control.receiver.task;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import androidx.collection.ArrayMap;
import com.jieli.lib.dv.control.ClientImpl;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.utils.BufChangeHex;
import com.jieli.lib.dv.control.utils.Constants;
import com.jieli.lib.dv.control.utils.Dlog;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CommandReceiver extends Thread {
    private static final String CTP = "CTP:";
    static String tag = CommandReceiver.class.getSimpleName();
    private WeakReference<Handler> mHandlerWeakReference;
    private WeakReference<Socket> mSocketWeakReference;
    private boolean isReceiverTaskRunning = false;
    private int mReceiveErrorTime = 0;

    public void stopRunning() {
        this.isReceiverTaskRunning = false;
    }

    public CommandReceiver(Socket socket, Handler handler) {
        this.mSocketWeakReference = new WeakReference<>(socket);
        this.mHandlerWeakReference = new WeakReference<>(handler);
    }

    private synchronized void onDataReceiver(byte[] bArr) {
        int i;
        if (bArr != null) {
            if (bArr.length >= 6) {
                if (bArr[0] == 67 && bArr[1] == 84 && bArr[2] == 80 && bArr[3] == 58) {
                    NotifyInfo notifyInfo = new NotifyInfo();
                    ByteBuffer order = ByteBuffer.wrap(bArr, 4, 2).order(ByteOrder.nativeOrder());
                    int i2 = order.getShort();
                    order.clear();
                    byte[] bArr2 = new byte[i2];
                    System.arraycopy(bArr, 6, bArr2, 0, i2);
                    notifyInfo.setTopic(new String(bArr2));
                    int i3 = 6 + i2;
                    if (bArr.length >= 4) {
                        order = ByteBuffer.wrap(bArr, i3, 4).order(ByteOrder.nativeOrder());
                        i = order.getInt();
                    } else {
                        i = 0;
                    }
                    int i4 = i3 + 4;
                    if (i > 0) {
                        byte[] bArr3 = new byte[i];
                        System.arraycopy(bArr, i4, bArr3, 0, i);
                        try {
                            JSONObject jSONObject = new JSONObject(new String(bArr3));
                            notifyInfo.setErrorType(jSONObject.optInt(Constants.JSON_ERROR_NUMBER));
                            notifyInfo.setOperation(jSONObject.optString(Constants.JSON_OP));
                            if (!jSONObject.isNull(Constants.JSON_PARAM)) {
                                JSONObject optJSONObject = jSONObject.optJSONObject(Constants.JSON_PARAM);
                                Iterator<String> keys = optJSONObject.keys();
                                ArrayMap<String, String> arrayMap = new ArrayMap<>();
                                while (keys.hasNext()) {
                                    String next = keys.next();
                                    arrayMap.put(next, optJSONObject.optString(next));
                                }
                                notifyInfo.setParams(arrayMap);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        notifyInfo.setParams(null);
                        notifyInfo.setErrorType(0);
                        notifyInfo.setOperation(null);
                    }
                    if (this.mHandlerWeakReference.get() != null) {
                        Message obtain = Message.obtain();
                        obtain.what = ClientImpl.MSG_RECEIVED_CTP_DATA;
                        obtain.obj = notifyInfo;
                        this.mHandlerWeakReference.get().sendMessage(obtain);
                    }
                    order.clear();
                    return;
                }
                Dlog.e(tag, "'CTP:' signature not match");
                return;
            }
        }
        Dlog.e(tag, "onDataReceiver:buffer == null || buffer.length <6");
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        super.run();
        Dlog.i(tag, "Receiver thread is running...");
        this.isReceiverTaskRunning = true;
        while (this.isReceiverTaskRunning) {
            Socket socket = this.mSocketWeakReference.get();
            if (socket != null && !socket.isClosed()) {
                try {
                    InputStream inputStream = socket.getInputStream();
                    if (inputStream != null && inputStream.available() > 0) {
                        byte[] bArr = new byte[4];
                        if (inputStream.read(bArr) == 4 && "CTP:".equals(new String(bArr))) {
                            NotifyInfo notifyInfo = new NotifyInfo();
                            byte[] bArr2 = new byte[2];
                            if (inputStream.read(bArr2) == 2) {
                                int byteArrayToInt = BufChangeHex.byteArrayToInt(bArr2);
                                byte[] bArr3 = new byte[byteArrayToInt];
                                if (inputStream.read(bArr3) == byteArrayToInt) {
                                    String str = new String(bArr3);
                                    if (!TextUtils.isEmpty(str)) {
                                        notifyInfo.setTopic(str);
                                        byte[] bArr4 = new byte[4];
                                        if (inputStream.read(bArr4) == 4) {
                                            int byteArrayToInt2 = BufChangeHex.byteArrayToInt(bArr4);
                                            if (byteArrayToInt2 > 0 && byteArrayToInt2 < 5242880) {
                                                byte[] bArr5 = new byte[byteArrayToInt2];
                                                if (inputStream.read(bArr5) == byteArrayToInt2) {
                                                    String str2 = new String(bArr5);
                                                    if (!TextUtils.isEmpty(str2)) {
                                                        try {
                                                            JSONObject jSONObject = new JSONObject(str2);
                                                            notifyInfo.setErrorType(jSONObject.optInt(Constants.JSON_ERROR_NUMBER));
                                                            notifyInfo.setOperation(jSONObject.optString(Constants.JSON_OP));
                                                            if (!jSONObject.isNull(Constants.JSON_PARAM)) {
                                                                JSONObject optJSONObject = jSONObject.optJSONObject(Constants.JSON_PARAM);
                                                                Iterator<String> keys = optJSONObject.keys();
                                                                ArrayMap<String, String> arrayMap = new ArrayMap<>();
                                                                while (keys.hasNext()) {
                                                                    String next = keys.next();
                                                                    arrayMap.put(next, optJSONObject.optString(next));
                                                                }
                                                                notifyInfo.setParams(arrayMap);
                                                            } else {
                                                                Dlog.w(tag, "JSON_PARAM is null");
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        Dlog.w(tag, "Payload empty");
                                                    }
                                                } else {
                                                    Dlog.w(tag, "Payload len error");
                                                }
                                            } else {
                                                notifyInfo.setParams(null);
                                                notifyInfo.setErrorType(0);
                                                notifyInfo.setOperation(null);
                                            }
                                            if (this.mHandlerWeakReference.get() != null) {
                                                Message obtain = Message.obtain();
                                                obtain.what = ClientImpl.MSG_RECEIVED_CTP_DATA;
                                                obtain.obj = notifyInfo;
                                                this.mHandlerWeakReference.get().sendMessage(obtain);
                                            }
                                        } else {
                                            Dlog.w(tag, "Content len error");
                                        }
                                    } else {
                                        Dlog.w(tag, "topic is empty");
                                    }
                                } else {
                                    Dlog.w(tag, "Read topic len error");
                                }
                            } else {
                                Dlog.w(tag, "Topic len error");
                            }
                        } else {
                            Dlog.w(tag, "CTP content error");
                        }
                    }
                    this.mReceiveErrorTime = 0;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    SystemClock.sleep(1000L);
                    int i = this.mReceiveErrorTime + 1;
                    this.mReceiveErrorTime = i;
                    if (i > 5) {
                        this.mReceiveErrorTime = 0;
                        if (this.mHandlerWeakReference.get() != null) {
                            this.mHandlerWeakReference.get().obtainMessage(257, 4).sendToTarget();
                        }
                        Dlog.w(tag, "Receiver error time:" + this.mReceiveErrorTime);
                        return;
                    }
                }
            } else {
                SystemClock.sleep(1000L);
            }
            SystemClock.sleep(10L);
        }
    }
}
