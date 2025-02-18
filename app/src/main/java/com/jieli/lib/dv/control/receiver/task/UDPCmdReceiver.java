package com.jieli.lib.dv.control.receiver.task;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import androidx.collection.ArrayMap;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.utils.BufChangeHex;
import com.jieli.lib.dv.control.utils.Constants;
import com.jieli.lib.dv.control.utils.Dlog;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class UDPCmdReceiver extends Thread {
    private static final String CTP = "CTP:";
    static String tag = UDPCmdReceiver.class.getSimpleName();
    private boolean isReceiverTaskRunning = false;
    private WeakReference<Handler> mHandlerWeakReference;
    private WeakReference<DatagramSocket> mUDPSocketWeakReference;

    public void stopRunning() {
        this.isReceiverTaskRunning = false;
    }

    public UDPCmdReceiver(DatagramSocket datagramSocket, Handler handler) {
        this.mUDPSocketWeakReference = new WeakReference<>(datagramSocket);
        this.mHandlerWeakReference = new WeakReference<>(handler);
    }

    private boolean handleUDPReceiver() {
        DatagramSocket datagramSocket = this.mUDPSocketWeakReference.get();
        if (datagramSocket != null) {
            DatagramPacket datagramPacket = new DatagramPacket(new byte[2048], 2048);
            try {
                datagramSocket.receive(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int length = datagramPacket.getLength();
            if (length > 0) {
                byte[] data = datagramPacket.getData();
                if (data != null && data.length > 0 && length < data.length) {
                    byte[] bArr = new byte[length];
                    System.arraycopy(data, 0, bArr, 0, length);
                    NotifyInfo parseCmdData = parseCmdData(bArr);
                    if (this.mHandlerWeakReference.get() != null) {
                        Message obtain = Message.obtain();
                        obtain.what = 101;
                        obtain.obj = parseCmdData;
                        this.mHandlerWeakReference.get().sendMessage(obtain);
                        return true;
                    }
                    Dlog.w(tag, "mHandler is null or error.");
                    return true;
                }
                Dlog.w(tag, "receive message is null or error.");
                return true;
            }
            Dlog.w(tag, "tempBuf is null or error.");
            return true;
        }
        Dlog.w(tag, "udpSocket is null");
        stopRunning();
        return false;
    }

    private NotifyInfo parseCmdData(byte[] bArr) {
        int length;
        int i;
        int i2;
        if (bArr == null || 10 >= (length = bArr.length)) {
            return null;
        }
        byte[] bArr2 = new byte[4];
        System.arraycopy(bArr, 0, bArr2, 0, 4);
        if (!"CTP:".equals(new String(bArr2))) {
            return null;
        }
        byte[] bArr3 = new byte[2];
        System.arraycopy(bArr, 4, bArr3, 0, 2);
        int byteArrayToInt = BufChangeHex.byteArrayToInt(bArr3);
        if (byteArrayToInt < 0 || (i = byteArrayToInt + 6) > length) {
            return null;
        }
        byte[] bArr4 = new byte[byteArrayToInt];
        System.arraycopy(bArr, 6, bArr4, 0, byteArrayToInt);
        String str = new String(bArr4);
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        NotifyInfo notifyInfo = new NotifyInfo();
        notifyInfo.setTopic(str);
        int i3 = i + 4;
        if (i3 <= length) {
            byte[] bArr5 = new byte[4];
            System.arraycopy(bArr, i, bArr5, 0, 4);
            int byteArrayToInt2 = BufChangeHex.byteArrayToInt(bArr5);
            if (byteArrayToInt2 >= 0 && (i2 = i3 + byteArrayToInt2) <= length) {
                byte[] bArr6 = new byte[byteArrayToInt2];
                System.arraycopy(bArr, i3, bArr6, 0, byteArrayToInt2);
                String str2 = new String(bArr6);
                if (i2 == length) {
                    if (!TextUtils.isEmpty(str2)) {
                        try {
                            JSONObject jSONObject = new JSONObject(str2);
                            notifyInfo.setErrorType(jSONObject.optInt(Constants.JSON_ERROR_NUMBER));
                            notifyInfo.setOperation(jSONObject.optString(Constants.JSON_OP));
                            if (jSONObject.has(Constants.JSON_PARAM)) {
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
                }
            }
        }
        return notifyInfo;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        super.run();
        this.isReceiverTaskRunning = true;
        while (this.isReceiverTaskRunning && handleUDPReceiver()) {
        }
    }
}
