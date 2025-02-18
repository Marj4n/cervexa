package com.jieli.lib.dv.control.udp;

import android.text.TextUtils;
import androidx.collection.ArrayMap;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.utils.BufChangeHex;
import com.jieli.lib.dv.control.utils.Constants;
import com.jieli.lib.dv.control.utils.Dlog;
import com.jieli.lib.dv.control.utils.ListenerHelper;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class UdpSocketReceiver extends Thread {
    private String a = "UdpSocketReceiver";
    private WeakReference<DatagramSocket> b;
    private volatile boolean c;

    public UdpSocketReceiver(DatagramSocket datagramSocket) {
        this.b = new WeakReference<>(datagramSocket);
    }

    public boolean isReceiver() {
        return this.c;
    }

    @Override // java.lang.Thread
    public synchronized void start() {
        this.c = true;
        super.start();
    }

    public synchronized void stopReceiver() {
        Dlog.w(this.a, "stop receive udp message");
        this.c = false;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Dlog.w(this.a, "receive udp message start, isReceiver : " + this.c);
        while (this.c) {
            try {
                a();
            } catch (IOException e) {
                e.printStackTrace();
                Dlog.w(this.a, "receive udp error: " + e.getMessage());
            }
        }
        Dlog.w(this.a, "receive udp close, isReceiver : " + this.c);
    }

    private void a() throws IOException {
        byte[] data;
        DatagramSocket datagramSocket = this.b.get();
        if (datagramSocket != null) {
            DatagramPacket datagramPacket = new DatagramPacket(new byte[2048], 2048);
            datagramSocket.receive(datagramPacket);
            int length = datagramPacket.getLength();
            Dlog.w(this.a, "receive udp message length : " + length);
            if (length > 0 && (data = datagramPacket.getData()) != null && data.length > 0 && length < data.length) {
                byte[] bArr = new byte[length];
                System.arraycopy(data, 0, bArr, 0, length);
                NotifyInfo a = a(bArr);
                if (a != null) {
                    ListenerHelper.getInstance().dispatchNotify(a);
                    return;
                }
            }
            Dlog.w(this.a, "receive message is null or error.");
            return;
        }
        Dlog.w(this.a, "udpSocket is null");
        ListenerHelper.getInstance().dispatchDeviceConnectState(5);
        stopReceiver();
    }

    private NotifyInfo a(byte[] bArr) {
        int length;
        int i;
        int i2;
        if (bArr == null || 10 >= (length = bArr.length)) {
            return null;
        }
        byte[] bArr2 = new byte[4];
        System.arraycopy(bArr, 0, bArr2, 0, 4);
        if (!Constants.CTP_SIGNATURE.equals(new String(bArr2))) {
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
}
