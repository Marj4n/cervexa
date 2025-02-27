package com.jieli.stream.dv.running2.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import com.jieli.lib.dv.control.connect.listener.OnConnectStateListener;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.utils.Topic;
import com.jieli.lib.dv.control.utils.TopicKey;
import com.jieli.stream.dv.running2.task.ClearThumbTask;
import com.jieli.stream.dv.running2.task.DeviceDescription;
import com.jieli.stream.dv.running2.task.HeartbeatTask;
import com.jieli.stream.dv.running2.task.SDPServer;
import com.jieli.stream.dv.running2.ui.MainApplication;
import com.jieli.stream.dv.running2.util.AppUtils;
import com.jieli.stream.dv.running2.util.ClientManager;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.IActions;
import com.jieli.stream.dv.running2.util.IConstant;

/* loaded from: classes.dex */
public class CommunicationService extends Service implements IConstant, IActions {
    public static final String SNAP_STATUS = "SNAP_STATUS";
    public static final String snap_status_key = "snap_status";
    private static final String tag = CommunicationService.class.getSimpleName();
    private ClearThumbTask clearThumbTask;
    private DeviceDescription loadDeviceDesTxt;
    private MainApplication mApplication;
    private SDPServer mLiveServer;
    private HeartbeatTask mHeartbeatTask = null;
    private final OnNotifyListener onNotifyResponse = new OnNotifyListener() { // from class: com.jieli.stream.dv.running2.ui.service.CommunicationService.1
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:400:0x0265, code lost:
        
            if (r0.equals(com.jieli.lib.dv.control.utils.Topic.VIDEO_DATE) != false) goto L154;
         */
        @Override // com.jieli.lib.dv.control.receiver.listener.NotifyResponse
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onNotify(NotifyInfo r12) {
            /*
                Method dump skipped, instructions count: 2958
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.ui.service.CommunicationService.AnonymousClass1.onNotify(com.jieli.lib.dv.control.json.bean.NotifyInfo):void");
        }
    };
    private OnConnectStateListener connectStateListener = new OnConnectStateListener() { // from class: com.jieli.stream.dv.running2.ui.service.CommunicationService.2
        @Override // com.jieli.lib.dv.control.connect.listener.ConnectStateListener
        public void onStateChanged(Integer num) {
            int intValue = num.intValue();
            if ((intValue == -1 || intValue == 1 || intValue == 3 || intValue == 4) && CommunicationService.this.mHeartbeatTask != null) {
                Dbug.e(CommunicationService.tag, "stop mHeartbeatTask");
                if (CommunicationService.this.mHeartbeatTask.isHeartbeatTaskRunning()) {
                    CommunicationService.this.mHeartbeatTask.stopRunning();
                }
                CommunicationService.this.mHeartbeatTask = null;
            }
        }
    };

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        ClientManager.getClient().registerNotifyListener(this.onNotifyResponse);
        ClientManager.getClient().registerConnectStateListener(this.connectStateListener);
        this.mApplication = MainApplication.getApplication();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent == null) {
            return 1;
        }
        int intExtra = intent.getIntExtra(IConstant.SERVICE_CMD, -1);
        Dbug.i(tag, "onStartCommand==========cmd=" + intExtra);
        if (intExtra == 1) {
            String stringExtra = intent.getStringExtra(IConstant.KEY_CONNECT_IP);
            if (TextUtils.isEmpty(stringExtra)) {
                stringExtra = IConstant.DEFAULT_DEV_IP;
            }
            if (!ClientManager.getClient().isConnected()) {
                ClientManager.getClient().create(stringExtra, IConstant.CTP_TCP_PORT);
            }
        } else if (intExtra == 2) {
            ClientManager.getClient().disconnect();
        }
        return 1;
    }

    @Override // android.app.Service
    public void onDestroy() {
        release();
        super.onDestroy();
    }

    @Override // android.app.Service
    public void onTaskRemoved(Intent intent) {
        release();
        stopSelf();
        super.onTaskRemoved(intent);
    }

    private void release() {
        Dbug.e(tag, "======= (( release )) =====");
        ClientManager.getClient().unregisterNotifyListener(this.onNotifyResponse);
        ClientManager.getClient().unregisterConnectStateListener(this.connectStateListener);
        ClearThumbTask clearThumbTask = this.clearThumbTask;
        if (clearThumbTask != null) {
            clearThumbTask.stopClear();
            this.clearThumbTask = null;
        }
        DeviceDescription deviceDescription = this.loadDeviceDesTxt;
        if (deviceDescription != null) {
            deviceDescription.interrupt();
            this.loadDeviceDesTxt = null;
        }
        HeartbeatTask heartbeatTask = this.mHeartbeatTask;
        if (heartbeatTask != null) {
            heartbeatTask.stopRunning();
            this.mHeartbeatTask = null;
        }
        stopSdpServer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSdpServer(NotifyInfo notifyInfo) {
        if (notifyInfo.getParams() == null) {
            Dbug.e(tag, "cachePlaybackVideoParam is null");
            return;
        }
        if (this.mLiveServer == null) {
            int i = 30;
            int i2 = IConstant.AUDIO_SAMPLE_RATE_DEFAULT;
            int i3 = -1;
            String str = notifyInfo.getParams().get("format");
            String str2 = notifyInfo.getParams().get(TopicKey.FRAME_RATE);
            String str3 = notifyInfo.getParams().get(TopicKey.SAMPLE);
            if (!TextUtils.isEmpty(str3) && TextUtils.isDigitsOnly(str3)) {
                i2 = Integer.valueOf(str3).intValue();
            }
            if (!TextUtils.isEmpty(str2) && TextUtils.isDigitsOnly(str2)) {
                i = Integer.valueOf(str2).intValue();
            }
            if (!TextUtils.isEmpty(str) && TextUtils.isDigitsOnly(str)) {
                i3 = Integer.valueOf(str).intValue();
            }
            SDPServer sDPServer = new SDPServer(IConstant.SDP_PORT, i3);
            this.mLiveServer = sDPServer;
            sDPServer.setFrameRate(i);
            this.mLiveServer.setSampleRate(i2);
            Dbug.i(tag, "format " + i3 + ", frameRate=" + i + ", sampleRate=" + i2);
            this.mLiveServer.setRtpVideoPort(IConstant.RTP_VIDEO_PORT1);
            this.mLiveServer.setRtpAudioPort(IConstant.RTP_AUDIO_PORT1);
            this.mLiveServer.start();
        }
    }

    private void stopSdpServer() {
        SDPServer sDPServer = this.mLiveServer;
        if (sDPServer != null) {
            sDPServer.stopRunning();
            this.mLiveServer = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cachePlaybackVideoParam(NotifyInfo notifyInfo) {
        int i;
        if (notifyInfo.getParams() == null) {
            Dbug.e(tag, "cachePlaybackVideoParam is null");
            return;
        }
        String str = notifyInfo.getParams().get(TopicKey.WIDTH);
        String str2 = notifyInfo.getParams().get(TopicKey.HEIGHT);
        String str3 = notifyInfo.getParams().get("format");
        String str4 = notifyInfo.getParams().get(TopicKey.FRAME_RATE);
        String str5 = notifyInfo.getParams().get(TopicKey.SAMPLE);
        if (!TextUtils.isEmpty(str) && TextUtils.isDigitsOnly(str) && !TextUtils.isEmpty(str2) && TextUtils.isDigitsOnly(str2)) {
            int adjustRtsResolution = AppUtils.adjustRtsResolution(Integer.valueOf(str).intValue(), Integer.valueOf(str2).intValue());
            if (Topic.VIDEO_PARAM.equals(notifyInfo.getTopic())) {
                this.mApplication.getDeviceSettingInfo().setFrontRecordLevel(adjustRtsResolution);
                this.mApplication.getDeviceSettingInfo().setFrontLevel(adjustRtsResolution);
            } else {
                this.mApplication.getDeviceSettingInfo().setRearRecordLevel(adjustRtsResolution);
                this.mApplication.getDeviceSettingInfo().setRearLevel(adjustRtsResolution);
            }
        }
        if (!TextUtils.isEmpty(str3) && TextUtils.isDigitsOnly(str3)) {
            try {
                i = Integer.valueOf(str3).intValue();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                i = 1;
            }
            if (i == 0) {
                if (Topic.VIDEO_PARAM.equals(notifyInfo.getTopic())) {
                    this.mApplication.getDeviceSettingInfo().setFrontFormat(0);
                } else {
                    this.mApplication.getDeviceSettingInfo().setRearFormat(0);
                }
            } else if (Topic.VIDEO_PARAM.equals(notifyInfo.getTopic())) {
                this.mApplication.getDeviceSettingInfo().setFrontFormat(1);
            } else {
                this.mApplication.getDeviceSettingInfo().setRearFormat(1);
            }
        }
        if (!TextUtils.isEmpty(str4) && TextUtils.isDigitsOnly(str4)) {
            int i2 = 30;
            try {
                i2 = Integer.valueOf(str4).intValue();
            } catch (NumberFormatException e2) {
                e2.printStackTrace();
            }
            if (Topic.VIDEO_PARAM.equals(notifyInfo.getTopic())) {
                this.mApplication.getDeviceSettingInfo().setFrontRate(i2);
            } else {
                this.mApplication.getDeviceSettingInfo().setRearRate(i2);
            }
        }
        if (TextUtils.isEmpty(str5) || !TextUtils.isDigitsOnly(str5)) {
            return;
        }
        int i3 = IConstant.AUDIO_SAMPLE_RATE_DEFAULT;
        try {
            i3 = Integer.valueOf(str5).intValue();
        } catch (NumberFormatException e3) {
            e3.printStackTrace();
        }
        if (Topic.VIDEO_PARAM.equals(notifyInfo.getTopic())) {
            this.mApplication.getDeviceSettingInfo().setFrontSampleRate(i3);
        } else {
            this.mApplication.getDeviceSettingInfo().setRearSampleRate(i3);
        }
    }
}
