package com.jieli.lib.dv.control;

import android.content.Context;
import android.text.TextUtils;
import androidx.collection.ArrayMap;
import com.jieli.lib.dv.control.connect.listener.OnConnectStateListener;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.json.bean.CmdInfo;
import com.jieli.lib.dv.control.json.bean.RequestCmd;
import com.jieli.lib.dv.control.json.bean.settingCmd;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.utils.Operation;
import com.jieli.lib.dv.control.utils.Topic;
import com.jieli.lib.dv.control.utils.TopicKey;
import com.jieli.lib.dv.control.utils.proxy.ProxyUtils;
import java.util.List;

/* loaded from: classes.dex */
public class DeviceClient implements IClient {
    public static final int CAMERA_FRONT_VIEW = 1;
    public static final int CAMERA_REAR_VIEW = 2;
    public static final int DEV_PB_STOP = 2;
    public static final int PROTOCOL_TCP = 0;
    public static final int PROTOCOL_UDP = 1;
    public static final int RTS_H264 = 1;
    public static final int RTS_JPEG = 0;
    private String a;
    private IClient b;

    public DeviceClient(Context context) {
        this(context, 0);
    }

    public DeviceClient(Context context, int i) {
        this.a = getClass().getSimpleName();
        if (context == null) {
            throw new NullPointerException("Context null");
        }
        if (i == 0) {
            this.b = ClientImpl.getInstance(context);
        } else {
            if (i == 1) {
                this.b = UDPClientImpl.getInstance(context);
                return;
            }
            throw new IllegalArgumentException("unknown protocol " + i);
        }
    }

    @Deprecated
    public void connect(String str, int i) {
        create(str, i);
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void create(String str, int i) {
        this.b.create(str, i);
    }

    @Override // com.jieli.lib.dv.control.IClient
    public boolean isConnected() {
        return this.b.isConnected();
    }

    @Override // com.jieli.lib.dv.control.IClient
    public int getState() {
        return this.b.getState();
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void send(CmdInfo cmdInfo, SendResponse sendResponse) {
        this.b.send(cmdInfo, (SendResponse) ProxyUtils.getUIProxy(sendResponse));
    }

    @Deprecated
    public void disconnect() {
        close();
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void close() {
        this.b.close();
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void registerConnectStateListener(OnConnectStateListener onConnectStateListener) {
        this.b.registerConnectStateListener(onConnectStateListener);
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void unregisterConnectStateListener(OnConnectStateListener onConnectStateListener) {
        this.b.unregisterConnectStateListener(onConnectStateListener);
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void registerNotifyListener(OnNotifyListener onNotifyListener) {
        this.b.registerNotifyListener(onNotifyListener);
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void unregisterNotifyListener(OnNotifyListener onNotifyListener) {
        this.b.unregisterNotifyListener(onNotifyListener);
    }

    @Override // com.jieli.lib.dv.control.IClient
    public void release() {
        this.b.release();
    }

    @Override // com.jieli.lib.dv.control.IClient
    public String getAddress() {
        return this.b.getAddress();
    }

    @Deprecated
    public String getConnectedIP() {
        return getAddress();
    }

    public void tryToPut(settingCmd settingCmd, SendResponse sendResponse) {
        if (TextUtils.isEmpty(settingCmd.getOperation())) {
            settingCmd.setOperation(Operation.TYPE_PUT);
        }
        send(settingCmd, sendResponse);
    }

    public void tryToGet(RequestCmd requestCmd, SendResponse sendResponse) {
        if (TextUtils.isEmpty(requestCmd.getOperation())) {
            requestCmd.setOperation("GET");
        }
        send(requestCmd, sendResponse);
    }

    public void tryToRecordVideo(boolean z, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.VIDEO_CTRL);
        settingCmd.setOperation(Operation.TYPE_PUT);
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        if (z) {
            arrayMap.put("status", "1");
        } else {
            arrayMap.put("status", "0");
        }
        settingCmd.setParams(arrayMap);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToRequestRecordState(SendResponse sendResponse) {
        RequestCmd requestCmd = new RequestCmd();
        requestCmd.setTopic(Topic.VIDEO_CTRL);
        requestCmd.setOperation("GET");
        tryToGet(requestCmd, sendResponse);
    }

    public void tryToTakePhoto(SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.PHOTO_CTRL);
        settingCmd.setOperation(Operation.TYPE_PUT);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToOpenRTStream(int i, int i2, int i3, int i4, int i5, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        if (i == 2) {
            settingCmd.setTopic(Topic.OPEN_REAR_RTS);
        } else {
            settingCmd.setTopic(Topic.OPEN_FRONT_RTS);
        }
        settingCmd.setOperation(Operation.TYPE_PUT);
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put("format", String.valueOf(i2));
        arrayMap.put(TopicKey.WIDTH, String.valueOf(i3));
        arrayMap.put(TopicKey.HEIGHT, String.valueOf(i4));
        arrayMap.put(TopicKey.FRAME_RATE, String.valueOf(i5));
        settingCmd.setParams(arrayMap);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToCloseRTStream(int i, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(i == 2 ? Topic.CLOSE_PULL_RT_STREAM : Topic.CLOSE_RT_STREAM);
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams("status", "1");
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToKeepAlive(SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.KEEP_ALIVE);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToAccessDevice(String str, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.APP_ACCESS);
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams("type", "0", TopicKey.VERSION, str);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToRequestMediaFiles(int i, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        if (i == 2) {
            settingCmd.setTopic(Topic.REAR_MEDIA_FILE_LIST);
        } else {
            settingCmd.setTopic(Topic.FRONT_MEDIA_FILE_LIST);
        }
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams("type", "1");
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToRequestMediaFiles(int i, int i2, SendResponse sendResponse) {
        if (i2 <= 0) {
            tryToRequestMediaFiles(i, sendResponse);
            return;
        }
        settingCmd settingCmd = new settingCmd();
        if (i == 2) {
            settingCmd.setTopic(Topic.REAR_MEDIA_FILE_LIST);
        } else {
            settingCmd.setTopic(Topic.FRONT_MEDIA_FILE_LIST);
        }
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams("type", "1", TopicKey.NUMBER, String.valueOf(i2));
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToRequestPictureFiles(int i, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        if (i == 2) {
            settingCmd.setTopic(Topic.REAR_MEDIA_FILE_LIST);
        } else {
            settingCmd.setTopic(Topic.FRONT_MEDIA_FILE_LIST);
        }
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams("type", "3");
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToRequestVideoFiles(int i, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        if (i == 2) {
            settingCmd.setTopic(Topic.REAR_MEDIA_FILE_LIST);
        } else {
            settingCmd.setTopic(Topic.FRONT_MEDIA_FILE_LIST);
        }
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams("type", "2");
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToRequestDevUUID(SendResponse sendResponse) {
        RequestCmd requestCmd = new RequestCmd();
        requestCmd.setTopic(Topic.DEVICE_UUID);
        requestCmd.setOperation("GET");
        tryToGet(requestCmd, sendResponse);
    }

    public void tryToSyncDevDate(String str, SendResponse sendResponse) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.DATE_TIME);
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams(TopicKey.KEY_DATE, str);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToRequestDevDate(SendResponse sendResponse) {
        RequestCmd requestCmd = new RequestCmd();
        requestCmd.setTopic(Topic.DATE_TIME);
        requestCmd.setOperation("GET");
        tryToGet(requestCmd, sendResponse);
    }

    public void tryToFormatTFCard(SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.FORMAT_TF_CARD);
        settingCmd.setOperation(Operation.TYPE_PUT);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToRequestTFCardCapacity(SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.TF_CARD_CAPACITY);
        settingCmd.setOperation("GET");
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToRequestVideoCover(List<String> list, SendResponse sendResponse) {
        if (list == null) {
            throw new NullPointerException("Param list is null");
        }
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        for (int i = 0; i < list.size(); i++) {
            arrayMap.put(TopicKey.FILE_NUM + i, list.get(i));
        }
        RequestCmd requestCmd = new RequestCmd();
        requestCmd.setTopic("MULTI_COVER_FIGURE");
        requestCmd.setOperation(Operation.TYPE_PUT);
        requestCmd.setParams(arrayMap);
        tryToGet(requestCmd, sendResponse);
    }

    public void tryToRequestVideoContentThumbnail(String str, int i, int i2, int i3, SendResponse sendResponse) {
        if (TextUtils.isEmpty(str)) {
            throw new NullPointerException("Param path is null");
        }
        if (i < 0) {
            i = 0;
        }
        if (i3 < 0) {
            i3 = 1;
        }
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.VIDEO_CONTENT_THUMBNAILS);
        settingCmd.setOperation(Operation.TYPE_PUT);
        arrayMap.put(TopicKey.PATH, str);
        arrayMap.put("offset", String.valueOf(i));
        arrayMap.put(TopicKey.TIME_INTERVAL, String.valueOf(i2));
        arrayMap.put(TopicKey.NUMBER, String.valueOf(i3));
        settingCmd.setParams(arrayMap);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToControlThumbnails(int i, SendResponse sendResponse) {
        if (i != 0) {
            i = 1;
        }
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.VIDEO_THUMBNAILS_CTRL);
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams("status", String.valueOf(i));
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToDeleteFile(List<String> list, SendResponse sendResponse) {
        if (list == null) {
            return;
        }
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        for (int i = 0; i < list.size(); i++) {
            arrayMap.put(TopicKey.FILE_NUM + i, list.get(i));
        }
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.FILES_DELETE);
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams(arrayMap);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToStartPlayback(String str, int i, SendResponse sendResponse) {
        if (TextUtils.isEmpty(str)) {
            throw new NullPointerException("param path is null");
        }
        if (i < 0) {
            i = 0;
        }
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.PLAYBACK);
        settingCmd.setOperation(Operation.TYPE_PUT);
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put(TopicKey.PATH, str);
        arrayMap.put("offset", String.valueOf(i));
        settingCmd.setParams(arrayMap);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToChangePlaybackState(int i, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.PLAYBACK_CTRL);
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams("status", String.valueOf(i));
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToFastForward(int i, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.PLAYBACK_FAST_FORWARD);
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams(TopicKey.LEVEL, String.valueOf(i));
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToResetDev(SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.RESET);
        settingCmd.setOperation(Operation.TYPE_PUT);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToSetLanguage(int i, SendResponse sendResponse) {
        tryToSetLanguage(String.valueOf(i), sendResponse);
    }

    public void tryToSetLanguage(String str, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.LANGUAGE);
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams(TopicKey.LAG, str);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToSetVideoMic(boolean z, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.VIDEO_MIC);
        settingCmd.setOperation(Operation.TYPE_PUT);
        if (z) {
            settingCmd.setParams(TopicKey.MIC, "1");
        } else {
            settingCmd.setParams(TopicKey.MIC, "0");
        }
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToSetApAccount(String str, String str2, boolean z, SendResponse sendResponse) {
        if (TextUtils.isEmpty(str2)) {
            str2 = "";
        }
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.AP_SSID_INFO);
        settingCmd.setOperation(Operation.TYPE_PUT);
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put("status", z ? "1" : "0");
        arrayMap.put(TopicKey.PWD, str2);
        arrayMap.put(TopicKey.SSID, str);
        settingCmd.setParams(arrayMap);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToSetSTAAccount(String str, String str2, boolean z, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.STA_SSID_INFO);
        settingCmd.setOperation(Operation.TYPE_PUT);
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put("status", z ? "1" : "0");
        arrayMap.put(TopicKey.PWD, str2);
        arrayMap.put(TopicKey.SSID, str);
        settingCmd.setParams(arrayMap);
        tryToPut(settingCmd, sendResponse);
    }

    @Deprecated
    public void tryToScreenShotTask(boolean z, int i, int i2, int i3, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.APP_SET_PROJECTION);
        settingCmd.setOperation(Operation.TYPE_PUT);
        if (z) {
            ArrayMap<String, String> arrayMap = new ArrayMap<>();
            arrayMap.put("status", "1");
            arrayMap.put(TopicKey.WIDTH, String.valueOf(i));
            arrayMap.put(TopicKey.HEIGHT, String.valueOf(i2));
            arrayMap.put(TopicKey.FRAME_RATE, String.valueOf(i3));
            settingCmd.setParams(arrayMap);
        } else {
            ArrayMap<String, String> arrayMap2 = new ArrayMap<>();
            arrayMap2.put("status", "0");
            settingCmd.setParams(arrayMap2);
        }
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToStreamingPush(boolean z, int i, int i2, int i3, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.NET_SCR);
        settingCmd.setOperation(Operation.TYPE_PUT);
        if (z) {
            ArrayMap<String, String> arrayMap = new ArrayMap<>();
            arrayMap.put("status", "1");
            arrayMap.put(TopicKey.WIDTH, String.valueOf(i));
            arrayMap.put(TopicKey.HEIGHT, String.valueOf(i2));
            arrayMap.put(TopicKey.FRAME_RATE, String.valueOf(i3));
            settingCmd.setParams(arrayMap);
        } else {
            ArrayMap<String, String> arrayMap2 = new ArrayMap<>();
            arrayMap2.put("status", "0");
            settingCmd.setParams(arrayMap2);
        }
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToSetPhotoQuality(int i, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.PHOTO_QUALITY);
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams(TopicKey.QUA, String.valueOf(i));
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToSetVideoParkCar(int i, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.VIDEO_PAR_CAR);
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams(TopicKey.PAR, String.valueOf(i));
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToSetGravitySenor(int i, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.GRA_SEN);
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams(TopicKey.GRA, String.valueOf(i));
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToSetTimeWatermark(boolean z, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.VIDEO_DATE);
        settingCmd.setOperation(Operation.TYPE_PUT);
        if (z) {
            settingCmd.setParams(TopicKey.DAT, "1");
        } else {
            settingCmd.setParams(TopicKey.DAT, "0");
        }
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToCollisionVideo(SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.COLLISION_DETECTION_VIDEO);
        settingCmd.setOperation(Operation.TYPE_PUT);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToRTIntercom(boolean z, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.RT_TALK_CTL);
        settingCmd.setOperation(Operation.TYPE_PUT);
        if (z) {
            settingCmd.setParams("status", "1");
        } else {
            settingCmd.setParams("status", "0");
        }
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToSetRecordParam(int i, int i2, int i3, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.VIDEO_PARAM);
        settingCmd.setOperation(Operation.TYPE_PUT);
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put("format", String.valueOf(i));
        arrayMap.put(TopicKey.WIDTH, String.valueOf(i2));
        arrayMap.put(TopicKey.HEIGHT, String.valueOf(i3));
        settingCmd.setParams(arrayMap);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToFileLock(String str, boolean z, SendResponse sendResponse) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.FILE_LOCK);
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams(TopicKey.PATH, str, "status", z ? "1" : "0");
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToRequestRearVideoStatus(SendResponse sendResponse) {
        RequestCmd requestCmd = new RequestCmd();
        requestCmd.setTopic(Topic.PULL_VIDEO_STATUS);
        requestCmd.setOperation("GET");
        tryToGet(requestCmd, sendResponse);
    }

    public void tryToSetFrontVideoParams(int i, int i2, int i3, int i4, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.VIDEO_PARAM);
        settingCmd.setOperation(Operation.TYPE_PUT);
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put(TopicKey.WIDTH, String.valueOf(i));
        arrayMap.put(TopicKey.HEIGHT, String.valueOf(i2));
        arrayMap.put("format", String.valueOf(i3));
        arrayMap.put(TopicKey.FRAME_RATE, String.valueOf(i4));
        settingCmd.setParams(arrayMap);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToSetRearVideoParams(int i, int i2, int i3, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.PULL_VIDEO_PARAM);
        settingCmd.setOperation(Operation.TYPE_PUT);
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put(TopicKey.WIDTH, String.valueOf(i));
        arrayMap.put(TopicKey.HEIGHT, String.valueOf(i2));
        arrayMap.put("format", String.valueOf(i3));
        settingCmd.setParams(arrayMap);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToSaveCycVideo(SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.CYC_SAVE_VIDEO);
        settingCmd.setOperation(Operation.TYPE_PUT);
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToResetSystem(SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic("SYSTEM_DEFAULT");
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams(TopicKey.DEF, String.valueOf(1));
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToToggleBootSound(boolean z, SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic(Topic.BOARD_VOICE);
        settingCmd.setOperation(Operation.TYPE_PUT);
        if (z) {
            settingCmd.setParams(TopicKey.BVO, "1");
        } else {
            settingCmd.setParams(TopicKey.BVO, "0");
        }
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToFactoryReset(SendResponse sendResponse) {
        settingCmd settingCmd = new settingCmd();
        settingCmd.setTopic("SYSTEM_DEFAULT");
        settingCmd.setOperation(Operation.TYPE_PUT);
        settingCmd.setParams(TopicKey.DEF, "1");
        tryToPut(settingCmd, sendResponse);
    }

    public void tryToRequestApInfo(SendResponse sendResponse) {
        RequestCmd requestCmd = new RequestCmd();
        requestCmd.setTopic(Topic.AP_SSID_INFO);
        requestCmd.setOperation("GET");
        tryToGet(requestCmd, sendResponse);
    }
}
