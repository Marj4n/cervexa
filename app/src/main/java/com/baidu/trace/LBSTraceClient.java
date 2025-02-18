package com.baidu.trace;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import com.baidu.lbsapi.auth.tracesdk.LBSAuthManager;
import com.baidu.trace.IListener;
import com.baidu.trace.api.analysis.DrivingBehaviorRequest;
import com.baidu.trace.api.analysis.OnAnalysisListener;
import com.baidu.trace.api.analysis.StayPointRequest;
import com.baidu.trace.api.bos.BosGeneratePresignedUrlRequest;
import com.baidu.trace.api.bos.BosGetObjectRequest;
import com.baidu.trace.api.bos.BosPutObjectRequest;
import com.baidu.trace.api.bos.OnBosListener;
import com.baidu.trace.api.entity.AddEntityRequest;
import com.baidu.trace.api.entity.AroundSearchRequest;
import com.baidu.trace.api.entity.BoundSearchRequest;
import com.baidu.trace.api.entity.DeleteEntityRequest;
import com.baidu.trace.api.entity.DistrictSearchRequest;
import com.baidu.trace.api.entity.EntityListRequest;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.entity.PolygonSearchRequest;
import com.baidu.trace.api.entity.SearchRequest;
import com.baidu.trace.api.entity.UpdateEntityRequest;
import com.baidu.trace.api.fence.AlarmPoint;
import com.baidu.trace.api.fence.CreateFenceRequest;
import com.baidu.trace.api.fence.DeleteFenceRequest;
import com.baidu.trace.api.fence.FenceAlarmPushInfo;
import com.baidu.trace.api.fence.FenceListRequest;
import com.baidu.trace.api.fence.HistoryAlarmRequest;
import com.baidu.trace.api.fence.MonitoredAction;
import com.baidu.trace.api.fence.MonitoredStatusByLocationRequest;
import com.baidu.trace.api.fence.MonitoredStatusRequest;
import com.baidu.trace.api.fence.OnFenceListener;
import com.baidu.trace.api.fence.UpdateFenceRequest;
import com.baidu.trace.api.track.AddPointRequest;
import com.baidu.trace.api.track.AddPointsRequest;
import com.baidu.trace.api.track.ClearCacheTrackRequest;
import com.baidu.trace.api.track.ClearCacheTrackResponse;
import com.baidu.trace.api.track.DistanceRequest;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.LatestPointRequest;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.QueryCacheTrackRequest;
import com.baidu.trace.api.track.QueryCacheTrackResponse;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import com.baidu.trace.model.LocType;
import com.baidu.trace.model.LocationMode;
import com.baidu.trace.model.OnCustomAttributeListener;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.ProtocolType;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TraceLocation;
import com.github.mikephil.charting.utils.Utils;
import com.jieli.stream.dv.running2.util.IConstant;
import com.tencent.tauth.AuthActivity;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class LBSTraceClient {
    protected static LBSTraceClient a;
    private Context b;
    private ClientListener e;
    private Intent c = null;
    private IService d = null;
    private a f = new a(this);
    private LocationMode g = LocationMode.High_Accuracy;
    private OnTraceListener h = null;
    private OnCustomAttributeListener i = null;
    private boolean j = false;
    private ar k = null;
    private ServiceConnection l = new ap(this);

    static class ClientListener extends IListener.Stub {
        private WeakReference<LBSTraceClient> traceClient;

        public ClientListener(LBSTraceClient lBSTraceClient) {
            this.traceClient = null;
            this.traceClient = new WeakReference<>(lBSTraceClient);
        }

        private void sendMessage(int i) {
            Message message = new Message();
            message.what = i;
            WeakReference<LBSTraceClient> weakReference = this.traceClient;
            if (weakReference == null || weakReference.get().f == null) {
                return;
            }
            this.traceClient.get().f.sendMessage(message);
        }

        @Override // com.baidu.trace.IListener
        public void extendedOperationCallback(int i, String str) throws RemoteException {
            int i2 = 172;
            if (i != 0) {
                sendMessage(172);
                return;
            }
            try {
                JSONObject jSONObject = new JSONObject(str);
                g.a = jSONObject.getString("accessKey");
                g.c = jSONObject.getString("secretKey");
                g.d = jSONObject.getString("token");
                g.b = jSONObject.getLong("expireTime");
                h.a();
                i2 = 171;
            } catch (JSONException unused) {
            }
            sendMessage(i2);
        }

        @Override // com.baidu.trace.IListener
        public Map<String, String> gatherAttribute(long j) throws RemoteException {
            LBSTraceClient lBSTraceClient = this.traceClient.get();
            if (lBSTraceClient == null || lBSTraceClient.i == null) {
                return null;
            }
            com.baidu.trace.a.a("LC-GPS locTime is: " + j);
            Map<String, String> onTrackAttributeCallback = lBSTraceClient.i.onTrackAttributeCallback(j);
            return onTrackAttributeCallback == null ? lBSTraceClient.i.onTrackAttributeCallback() : onTrackAttributeCallback;
        }

        @Override // com.baidu.trace.IListener
        public void pushCallback(byte b, String str) throws RemoteException {
            Message message = new Message();
            message.arg1 = b;
            message.obj = str;
            message.what = IConstant.OP_EXIT_EDIT_MODE;
            WeakReference<LBSTraceClient> weakReference = this.traceClient;
            if (weakReference == null || weakReference.get().f == null) {
                return;
            }
            this.traceClient.get().f.sendMessage(message);
        }

        @Override // com.baidu.trace.IListener
        public void startGatherCallback(int i) throws RemoteException {
            sendMessage(i);
        }

        @Override // com.baidu.trace.IListener
        public void startTraceCallback(int i) throws RemoteException {
            sendMessage(i);
        }

        @Override // com.baidu.trace.IListener
        public void stopGatherCallback(int i) throws RemoteException {
            sendMessage(i);
        }

        @Override // com.baidu.trace.IListener
        public void stopTraceCallback(int i) throws RemoteException {
            sendMessage(i);
        }
    }

    static class a extends Handler {
        private WeakReference<LBSTraceClient> a;

        public a(LBSTraceClient lBSTraceClient) {
            this.a = null;
            this.a = new WeakReference<>(lBSTraceClient);
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            PushMessage pushMessage;
            JSONObject jSONObject;
            String str;
            CoordType coordType;
            Class cls;
            MonitoredAction valueOf;
            super.handleMessage(message);
            LBSTraceClient lBSTraceClient = this.a.get();
            if (lBSTraceClient == null) {
                com.baidu.trace.a.a("LBSTraceClient instance is null");
                return;
            }
            int i = message.what;
            if (i == 18) {
                if (lBSTraceClient.h != null) {
                    lBSTraceClient.h.onStartTraceCallback(10003, StatusCodes.MSG_START_TRACE_NETWORK_CONNECT_FAILED);
                    return;
                }
                return;
            }
            if (i == 19) {
                if (lBSTraceClient.h != null) {
                    lBSTraceClient.h.onStartTraceCallback(10004, StatusCodes.MSG_START_TRACE_NETWORK_CLOSED);
                    return;
                }
                return;
            }
            if (i == 22) {
                if (lBSTraceClient.h != null) {
                    lBSTraceClient.h.onStartTraceCallback(10005, StatusCodes.MSG_TRACE_STARTING);
                    return;
                }
                return;
            }
            if (i == 25) {
                if (lBSTraceClient.h != null) {
                    lBSTraceClient.h.onStartTraceCallback(10006, StatusCodes.MSG_TRACE_STARTED);
                    return;
                }
                return;
            }
            if (i == 27) {
                if (lBSTraceClient.h != null) {
                    lBSTraceClient.h.onStopTraceCallback(StatusCodes.NOT_START_TRACE, "服务未开启");
                    return;
                }
                return;
            }
            if (i == 124) {
                if (lBSTraceClient.h != null) {
                    lBSTraceClient.h.onStartTraceCallback(0, StatusCodes.MSG_SUCCESS);
                    return;
                }
                return;
            }
            if (i == 152) {
                if (lBSTraceClient.h != null) {
                    lBSTraceClient.h.onStopTraceCallback(StatusCodes.STOP_TRACE_FAILED, StatusCodes.MSG_STOP_TRACE_FAILED);
                    return;
                }
                return;
            }
            if (i == 162) {
                if (lBSTraceClient.h == null) {
                    return;
                }
                byte b = (byte) message.arg1;
                String str2 = (String) message.obj;
                PushMessage pushMessage2 = new PushMessage();
                FenceAlarmPushInfo fenceAlarmPushInfo = new FenceAlarmPushInfo();
                try {
                    jSONObject = new JSONObject(str2);
                    if (jSONObject.has("fence_id")) {
                        str = "longitude";
                        fenceAlarmPushInfo.setFenceId(jSONObject.getLong("fence_id"));
                    } else {
                        str = "longitude";
                    }
                    if (jSONObject.has("fence")) {
                        fenceAlarmPushInfo.setFenceName(jSONObject.getString("fence"));
                    }
                    if (jSONObject.has("monitored_person")) {
                        fenceAlarmPushInfo.setMonitoredPerson(jSONObject.getString("monitored_person"));
                    }
                } catch (Exception unused) {
                    pushMessage = pushMessage2;
                }
                if (jSONObject.has(AuthActivity.ACTION_KEY)) {
                    if (3 == b) {
                        int i2 = jSONObject.getInt(AuthActivity.ACTION_KEY);
                        if (i2 == 1) {
                            valueOf = MonitoredAction.enter;
                        } else if (i2 == 2) {
                            valueOf = MonitoredAction.exit;
                        }
                        fenceAlarmPushInfo.setMonitoredAction(valueOf);
                    } else if (4 == b) {
                        valueOf = MonitoredAction.valueOf(jSONObject.getString(AuthActivity.ACTION_KEY));
                        fenceAlarmPushInfo.setMonitoredAction(valueOf);
                    }
                    pushMessage = pushMessage2;
                    pushMessage.setFenceAlarmPushInfo(fenceAlarmPushInfo);
                    lBSTraceClient.h.onPushCallback(b, pushMessage);
                    return;
                }
                AlarmPoint alarmPoint = new AlarmPoint();
                AlarmPoint alarmPoint2 = new AlarmPoint();
                if (jSONObject.has("time")) {
                    alarmPoint.setLocTime(jSONObject.getLong("time"));
                }
                if (jSONObject.has("create_time")) {
                    alarmPoint.setCreateTime(jSONObject.getLong("create_time"));
                }
                if (jSONObject.has("latitude") && jSONObject.has(str)) {
                    pushMessage = pushMessage2;
                    try {
                        alarmPoint.setLocation(new LatLng(jSONObject.getDouble("latitude"), jSONObject.getDouble(str)));
                    } catch (Exception unused2) {
                    }
                } else {
                    pushMessage = pushMessage2;
                }
                if (jSONObject.has("coord_type")) {
                    CoordType coordType2 = CoordType.bd09ll;
                    int i3 = jSONObject.getInt("coord_type");
                    if (i3 == 1) {
                        coordType2 = CoordType.wgs84;
                    } else if (i3 == 2) {
                        coordType2 = CoordType.gcj02;
                    } else if (i3 == 3) {
                        coordType2 = CoordType.bd09ll;
                    }
                    alarmPoint.setCoordType(coordType2);
                }
                if (jSONObject.has("radius")) {
                    alarmPoint.setRadius(jSONObject.getDouble("radius"));
                }
                if (jSONObject.has("cur_point")) {
                    com.baidu.trace.a.a(jSONObject.getJSONObject("cur_point"), CoordType.bd09ll, alarmPoint, String.class);
                }
                fenceAlarmPushInfo.setCurrentPoint(alarmPoint);
                if (jSONObject.has("pre_point")) {
                    JSONObject jSONObject2 = jSONObject.getJSONObject("pre_point");
                    if (3 == b) {
                        coordType = CoordType.bd09ll;
                        cls = Integer.class;
                    } else {
                        if (4 == b) {
                            coordType = CoordType.bd09ll;
                            cls = String.class;
                        }
                        fenceAlarmPushInfo.setPrePoint(alarmPoint2);
                    }
                    com.baidu.trace.a.a(jSONObject2, coordType, alarmPoint2, cls);
                    fenceAlarmPushInfo.setPrePoint(alarmPoint2);
                }
                pushMessage.setFenceAlarmPushInfo(fenceAlarmPushInfo);
                lBSTraceClient.h.onPushCallback(b, pushMessage);
                return;
            }
            if (i == 1241) {
                if (lBSTraceClient.h != null) {
                    lBSTraceClient.h.onStartTraceCallback(10001, StatusCodes.MSG_START_TRACE_FAILED);
                    return;
                }
                return;
            }
            if (i == 10000) {
                if (lBSTraceClient.h != null) {
                    lBSTraceClient.h.onStartTraceCallback(10000, "请求发送失败");
                    return;
                }
                return;
            }
            if (i == 142 || i == 143) {
                try {
                    try {
                        lBSTraceClient.d.unregisterListener();
                        lBSTraceClient.b.unbindService(lBSTraceClient.l);
                        lBSTraceClient.b.stopService(lBSTraceClient.c);
                        if (lBSTraceClient.h != null) {
                            if (142 == message.what) {
                                lBSTraceClient.h.onStopTraceCallback(0, StatusCodes.MSG_SUCCESS);
                            } else {
                                lBSTraceClient.h.onStopTraceCallback(StatusCodes.CACHE_TRACK_NOT_UPLOAD, StatusCodes.MSG_CACHE_TRACK_NOT_UPLOAD_);
                            }
                        }
                        lBSTraceClient.d = null;
                    } catch (Exception e) {
                        StringWriter stringWriter = new StringWriter();
                        e.printStackTrace(new PrintWriter(stringWriter));
                        com.baidu.trace.a.a("unbind and stop LBSTraceService failed, Exception : " + stringWriter.toString());
                        if (lBSTraceClient.h != null) {
                            lBSTraceClient.h.onStopTraceCallback(StatusCodes.STOP_TRACE_FAILED, StatusCodes.MSG_STOP_TRACE_FAILED);
                        }
                    }
                    return;
                } finally {
                    com.baidu.trace.c.h.a();
                }
            }
            if (i == 171) {
                if (lBSTraceClient.h != null) {
                    lBSTraceClient.h.onInitBOSCallback(0, StatusCodes.MSG_SUCCESS);
                    return;
                }
                return;
            }
            if (i == 172) {
                if (lBSTraceClient.h != null) {
                    lBSTraceClient.h.onInitBOSCallback(1, StatusCodes.MSG_FAILED);
                    return;
                }
                return;
            }
            if (i == 181) {
                if (lBSTraceClient.h != null) {
                    lBSTraceClient.h.onStartTraceCallback(10007, "服务正在停止");
                    return;
                }
                return;
            }
            if (i == 182) {
                if (lBSTraceClient.h != null) {
                    lBSTraceClient.h.onStopTraceCallback(StatusCodes.STOPPING_TRACE, "服务正在停止");
                    return;
                }
                return;
            }
            switch (i) {
                case 52:
                    if (lBSTraceClient.h != null) {
                        lBSTraceClient.h.onStartGatherCallback(0, StatusCodes.MSG_SUCCESS);
                        return;
                    }
                    return;
                case 53:
                    if (lBSTraceClient.h != null) {
                        lBSTraceClient.h.onStartGatherCallback(StatusCodes.START_GATHER_FAILED, StatusCodes.MSG_START_GATHER_FAILED);
                        return;
                    }
                    return;
                case 54:
                    if (lBSTraceClient.h != null) {
                        lBSTraceClient.h.onStartGatherCallback(StatusCodes.GATHER_STARTED, StatusCodes.MSG_GATHER_STARTED);
                        return;
                    }
                    return;
                case 55:
                    if (lBSTraceClient.h != null) {
                        lBSTraceClient.h.onStopGatherCallback(StatusCodes.GATHER_STOPPED, StatusCodes.MSG_GATHER_STOPPED);
                        return;
                    }
                    return;
                case 56:
                    if (lBSTraceClient.h != null) {
                        lBSTraceClient.h.onStopGatherCallback(StatusCodes.STOP_GATHER_FAILED, StatusCodes.MSG_STOP_GATHER_FAILED);
                        return;
                    }
                    return;
                case 57:
                    if (lBSTraceClient.h != null) {
                        lBSTraceClient.h.onStopGatherCallback(0, StatusCodes.MSG_SUCCESS);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public LBSTraceClient(Context context) {
        this.e = null;
        this.b = context.getApplicationContext();
        this.e = new ClientListener(this);
        c();
    }

    private void b() {
        try {
            this.b.startService(this.c);
            this.b.bindService(this.c, this.l, 1);
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            com.baidu.trace.a.a("start and bind service failed, Exception : " + stringWriter.toString());
            OnTraceListener onTraceListener = this.h;
            if (onTraceListener != null) {
                onTraceListener.onBindServiceCallback(1, StatusCodes.MSG_FAILED);
            }
        }
    }

    private void c() {
        if (this.j) {
            return;
        }
        this.j = true;
        av.a(this.b);
        d.b = LBSAuthManager.getInstance(this.b);
        q.a(this.b, d.b);
        com.baidu.trace.c.e.a(this.b);
        com.baidu.trace.c.a.a(this.b, LBSTraceClient.class);
        am.a(this.b);
        String str = com.baidu.trace.c.e.c;
        com.baidu.trace.c.h.b();
        d.a();
    }

    protected final void a() {
        try {
            this.d.handleExtendedOperate(0);
        } catch (Exception unused) {
        }
    }

    public final void addEntity(AddEntityRequest addEntityRequest, OnEntityListener onEntityListener) {
        if (com.baidu.trace.a.a("addEntity", addEntityRequest, onEntityListener)) {
            com.baidu.trace.a.a(addEntityRequest, onEntityListener);
        }
    }

    public final void addPoint(AddPointRequest addPointRequest, OnTrackListener onTrackListener) {
        if (com.baidu.trace.a.a("addPoint", addPointRequest, onTrackListener)) {
            com.baidu.trace.a.a(addPointRequest, onTrackListener);
        }
    }

    public final void addPoints(AddPointsRequest addPointsRequest, OnTrackListener onTrackListener) {
        if (com.baidu.trace.a.a("addPoints", addPointsRequest, onTrackListener)) {
            com.baidu.trace.a.a(addPointsRequest, onTrackListener);
        }
    }

    public final void aroundSearchEntity(AroundSearchRequest aroundSearchRequest, OnEntityListener onEntityListener) {
        if (com.baidu.trace.a.a("aroundSearchEntity", aroundSearchRequest, onEntityListener)) {
            com.baidu.trace.a.a(aroundSearchRequest, onEntityListener);
        }
    }

    public final void boundSearchEntity(BoundSearchRequest boundSearchRequest, OnEntityListener onEntityListener) {
        if (com.baidu.trace.a.a("boundSearchEntity", boundSearchRequest, onEntityListener)) {
            com.baidu.trace.a.a(boundSearchRequest, onEntityListener);
        }
    }

    public final void clear() {
        am.a();
        com.baidu.trace.c.a.a();
        d.b();
        ar arVar = this.k;
        if (arVar != null) {
            arVar.c();
            this.k = null;
        }
        a = null;
        this.j = false;
    }

    public final void clearCacheTrack(ClearCacheTrackRequest clearCacheTrackRequest, OnTrackListener onTrackListener) {
        if (com.baidu.trace.a.a("clearCacheTrack", clearCacheTrackRequest, onTrackListener)) {
            Context context = this.b;
            a aVar = this.f;
            ClearCacheTrackResponse clearCacheTrackResponse = new ClearCacheTrackResponse(clearCacheTrackRequest.getTag(), 0, StatusCodes.MSG_SUCCESS);
            com.baidu.trace.c.a.a.execute(new bg(context, clearCacheTrackRequest.getEntityNames(), clearCacheTrackRequest.getCacheTrackInfos(), clearCacheTrackResponse, aVar, onTrackListener));
        }
    }

    public final void createFence(CreateFenceRequest createFenceRequest, OnFenceListener onFenceListener) {
        if (com.baidu.trace.a.a("createFence", createFenceRequest, onFenceListener)) {
            com.baidu.trace.a.a(this.b, this.d, this.f, createFenceRequest, onFenceListener);
        }
    }

    public final void deleteEntity(DeleteEntityRequest deleteEntityRequest, OnEntityListener onEntityListener) {
        if (com.baidu.trace.a.a("deleteEntity", deleteEntityRequest, onEntityListener)) {
            com.baidu.trace.a.a(deleteEntityRequest, onEntityListener);
        }
    }

    public final void deleteFence(DeleteFenceRequest deleteFenceRequest, OnFenceListener onFenceListener) {
        if (com.baidu.trace.a.a("deleteFence", deleteFenceRequest, onFenceListener)) {
            com.baidu.trace.a.a(this.b, this.d, this.f, deleteFenceRequest, onFenceListener);
        }
    }

    public final void districtSearchEntity(DistrictSearchRequest districtSearchRequest, OnEntityListener onEntityListener) {
        if (com.baidu.trace.a.a("districtSearchRequest", districtSearchRequest, onEntityListener)) {
            com.baidu.trace.a.a(districtSearchRequest, onEntityListener);
        }
    }

    public final void generatePresignedUrl(BosGeneratePresignedUrlRequest bosGeneratePresignedUrlRequest, OnBosListener onBosListener) {
        if (com.baidu.trace.a.a("generatePresignedUrl", bosGeneratePresignedUrlRequest, onBosListener)) {
            com.baidu.trace.a.a(bosGeneratePresignedUrlRequest, onBosListener);
        }
    }

    public final void getObject(BosGetObjectRequest bosGetObjectRequest, OnBosListener onBosListener) {
        if (com.baidu.trace.a.a("getObject", bosGetObjectRequest, onBosListener)) {
            com.baidu.trace.a.a(bosGetObjectRequest, onBosListener);
        }
    }

    public final boolean initThreadPoolConfig(int i, int i2, int i3) {
        return com.baidu.trace.c.a.a(i, i2, i3);
    }

    public final void polygonSearchEntity(PolygonSearchRequest polygonSearchRequest, OnEntityListener onEntityListener) {
        if (com.baidu.trace.a.a("polygonSearchRequest", polygonSearchRequest, onEntityListener)) {
            com.baidu.trace.a.a(polygonSearchRequest, onEntityListener);
        }
    }

    public final void putObject(BosPutObjectRequest bosPutObjectRequest, OnBosListener onBosListener) {
        if (com.baidu.trace.a.a("putObject", bosPutObjectRequest, onBosListener)) {
            com.baidu.trace.a.a(bosPutObjectRequest, onBosListener);
        }
    }

    public final void queryCacheTrack(QueryCacheTrackRequest queryCacheTrackRequest, OnTrackListener onTrackListener) {
        if (com.baidu.trace.a.a("queryCacheTrack", queryCacheTrackRequest, onTrackListener)) {
            Context context = this.b;
            a aVar = this.f;
            com.baidu.trace.c.a.a.execute(new be(context, queryCacheTrackRequest, new QueryCacheTrackResponse(queryCacheTrackRequest.getTag(), 0, StatusCodes.MSG_SUCCESS), aVar, onTrackListener));
        }
    }

    public final void queryDistance(DistanceRequest distanceRequest, OnTrackListener onTrackListener) {
        if (com.baidu.trace.a.a("queryDistance", distanceRequest, onTrackListener)) {
            com.baidu.trace.a.a(distanceRequest, onTrackListener);
        }
    }

    public final void queryDrivingBehavior(DrivingBehaviorRequest drivingBehaviorRequest, OnAnalysisListener onAnalysisListener) {
        if (com.baidu.trace.a.a("queryDrivingBehavior", drivingBehaviorRequest, onAnalysisListener)) {
            com.baidu.trace.a.a(drivingBehaviorRequest, onAnalysisListener);
        }
    }

    public final void queryEntityList(EntityListRequest entityListRequest, OnEntityListener onEntityListener) {
        if (com.baidu.trace.a.a("queryEntityList", entityListRequest, onEntityListener)) {
            com.baidu.trace.a.a(entityListRequest, onEntityListener);
        }
    }

    public final void queryFenceHistoryAlarmInfo(HistoryAlarmRequest historyAlarmRequest, OnFenceListener onFenceListener) {
        if (com.baidu.trace.a.a("queryFenceHistoryAlarmInfo", historyAlarmRequest, onFenceListener)) {
            com.baidu.trace.a.a(this.b, this.f, historyAlarmRequest, onFenceListener);
        }
    }

    public final void queryFenceList(FenceListRequest fenceListRequest, OnFenceListener onFenceListener) {
        if (com.baidu.trace.a.a("queryFenceList", fenceListRequest, onFenceListener)) {
            com.baidu.trace.a.a(this.b, this.f, fenceListRequest, onFenceListener);
        }
    }

    public final void queryHistoryTrack(HistoryTrackRequest historyTrackRequest, OnTrackListener onTrackListener) {
        if (com.baidu.trace.a.a("queryHistoryTrack", historyTrackRequest, onTrackListener)) {
            com.baidu.trace.a.a(historyTrackRequest, onTrackListener);
        }
    }

    public final void queryLatestPoint(LatestPointRequest latestPointRequest, OnTrackListener onTrackListener) {
        if (com.baidu.trace.a.a("queryLatestPoint", latestPointRequest, onTrackListener)) {
            com.baidu.trace.a.a(latestPointRequest, onTrackListener);
        }
    }

    public final void queryMonitoredStatus(MonitoredStatusRequest monitoredStatusRequest, OnFenceListener onFenceListener) {
        if (com.baidu.trace.a.a("queryMonitoredStatus", monitoredStatusRequest, onFenceListener)) {
            com.baidu.trace.a.a(this.b, this.f, monitoredStatusRequest, onFenceListener);
        }
    }

    public final void queryMonitoredStatusByLocation(MonitoredStatusByLocationRequest monitoredStatusByLocationRequest, OnFenceListener onFenceListener) {
        if (com.baidu.trace.a.a("queryMonitoredStatusByLocation", monitoredStatusByLocationRequest, onFenceListener)) {
            com.baidu.trace.a.a(this.b, this.f, monitoredStatusByLocationRequest, onFenceListener);
        }
    }

    public final void queryRealTimeLoc(LocRequest locRequest, OnEntityListener onEntityListener) {
        if (com.baidu.trace.a.a("queryRealTimeLoc", locRequest, onEntityListener)) {
            if (this.k == null) {
                synchronized (LBSTraceClient.class) {
                    if (this.k == null) {
                        this.k = new ar(this.b, this.f);
                    }
                }
            }
            TraceLocation traceLocation = new TraceLocation(locRequest.getTag(), 0, StatusCodes.MSG_SUCCESS, "", "", "", Utils.DOUBLE_EPSILON, Utils.DOUBLE_EPSILON, CoordType.bd09ll, Utils.DOUBLE_EPSILON, 0.0f, 0.0f, 0, "");
            if (!this.k.a(this.b, this.f, traceLocation)) {
                aa.a(this.b, locRequest, onEntityListener);
            } else {
                if (locRequest.isCanceled()) {
                    return;
                }
                traceLocation.setLocType(LocType.GPS);
                onEntityListener.onReceiveLocation(traceLocation);
            }
        }
    }

    public final void queryStayPoint(StayPointRequest stayPointRequest, OnAnalysisListener onAnalysisListener) {
        if (com.baidu.trace.a.a("queryStayPoint", stayPointRequest, onAnalysisListener)) {
            com.baidu.trace.a.a(stayPointRequest, onAnalysisListener);
        }
    }

    public final void searchEntity(SearchRequest searchRequest, OnEntityListener onEntityListener) {
        if (com.baidu.trace.a.a("searchEntity", searchRequest, onEntityListener)) {
            com.baidu.trace.a.a(searchRequest, onEntityListener);
        }
    }

    public final boolean setCacheSize(int i) {
        if (i < 50) {
            return false;
        }
        IService iService = this.d;
        if (iService == null) {
            if (!Trace.a(i)) {
                return false;
            }
            am.a(i);
            return true;
        }
        try {
            if (iService.setCacheSize(i)) {
                am.a(i);
                return true;
            }
        } catch (Exception unused) {
        }
        return false;
    }

    public final boolean setInterval(int i, int i2) {
        if (i > 0 && i <= 300 && i2 >= i && i2 >= 2 && i2 <= 300 && i2 % i == 0) {
            boolean a2 = Trace.a(i, i2);
            IService iService = this.d;
            if (iService == null) {
                return a2;
            }
            try {
                return iService.setInterval(i, i2);
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public final boolean setLocTimeOffset(int i) {
        if (i <= 0) {
            return false;
        }
        boolean b = Trace.b(i);
        IService iService = this.d;
        if (iService == null) {
            return b;
        }
        try {
            return iService.setLocTimeOffset(i);
        } catch (Exception unused) {
            return false;
        }
    }

    public final LBSTraceClient setLocationMode(LocationMode locationMode) {
        if (locationMode == null) {
            locationMode = LocationMode.High_Accuracy;
        }
        this.g = locationMode;
        IService iService = this.d;
        if (iService == null) {
            return this;
        }
        try {
            iService.setLocationMode(locationMode.ordinal());
        } catch (Exception unused) {
        }
        return this;
    }

    public final void setOnCustomAttributeListener(OnCustomAttributeListener onCustomAttributeListener) {
        this.i = onCustomAttributeListener;
    }

    public final void setOnTraceListener(OnTraceListener onTraceListener) {
        this.h = onTraceListener;
    }

    public final LBSTraceClient setProtocolType(ProtocolType protocolType) {
        if (protocolType == null) {
            protocolType = ProtocolType.HTTPS;
        }
        com.baidu.trace.c.a.b = protocolType;
        IService iService = this.d;
        if (iService == null) {
            return this;
        }
        try {
            iService.setProtocolType(com.baidu.trace.c.a.b.ordinal());
        } catch (Exception unused) {
        }
        return this;
    }

    public final void startGather(OnTraceListener onTraceListener) {
        OnTraceListener onTraceListener2;
        int i;
        String str;
        if (onTraceListener != null) {
            this.h = onTraceListener;
        }
        IService iService = this.d;
        if (iService == null) {
            if (com.baidu.trace.c.e.a(this.b, LBSTraceService.class.getName())) {
                if (this.c == null) {
                    this.c = new Intent(this.b, (Class<?>) LBSTraceService.class);
                }
                this.c.putExtra("operateType", 1);
                b();
                return;
            }
            OnTraceListener onTraceListener3 = this.h;
            if (onTraceListener3 != null) {
                onTraceListener3.onStartGatherCallback(StatusCodes.START_GATHER_NOT_STARTED, "服务未开启");
                return;
            }
            return;
        }
        try {
            int startGather = iService.startGather();
            if (this.h == null) {
                return;
            }
            if (52 == startGather) {
                onTraceListener2 = this.h;
                i = 0;
                str = StatusCodes.MSG_SUCCESS;
            } else if (54 != startGather) {
                this.h.onStartGatherCallback(StatusCodes.START_GATHER_FAILED, StatusCodes.MSG_START_GATHER_FAILED);
                return;
            } else {
                onTraceListener2 = this.h;
                i = StatusCodes.GATHER_STARTED;
                str = StatusCodes.MSG_GATHER_STARTED;
            }
            onTraceListener2.onStartGatherCallback(i, str);
        } catch (Exception unused) {
            if (this.h == null) {
                return;
            }
            if (com.baidu.trace.c.e.a(this.b, LBSTraceService.class.getName())) {
                this.h.onStartGatherCallback(StatusCodes.START_GATHER_REQUEST_FAILED, "请求发送失败");
            } else {
                this.h.onStartGatherCallback(StatusCodes.START_GATHER_NOT_STARTED, "服务未开启");
            }
        }
    }

    public final void startTrace(Trace trace, OnTraceListener onTraceListener) {
        String str;
        if (trace == null) {
            com.baidu.trace.a.a("BaiduTraceSDK", "Trace instance is null");
            return;
        }
        if (onTraceListener != null) {
            this.h = onTraceListener;
        }
        try {
            c();
            if (a == null) {
                a = this;
            }
            long serviceId = trace.getServiceId();
            if (serviceId > 0) {
                z.b = serviceId;
                String key = d.b.getKey();
                if (!TextUtils.isEmpty(key)) {
                    String entityName = trace.getEntityName();
                    if (com.baidu.trace.c.e.a(entityName)) {
                        if (this.c == null) {
                            this.c = new Intent(this.b, (Class<?>) LBSTraceService.class);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putInt("locationMode", this.g.ordinal());
                        bundle.putInt("protocolType", com.baidu.trace.c.a.b.ordinal());
                        bundle.putInt("gatherInterval", Trace.a);
                        bundle.putInt("packInterval", Trace.b);
                        bundle.putInt("locTimeOffset", Trace.d);
                        bundle.putLong("serviceId", serviceId);
                        bundle.putBoolean("isNeedObjectStorage", trace.isNeedObjectStorage());
                        bundle.putString("ak", key);
                        bundle.putString("entityName", entityName);
                        bundle.putString("mcode", d.b.getMCode());
                        bundle.putString("pcn", com.baidu.trace.c.e.c(this.b));
                        if (50 <= Trace.c) {
                            bundle.putInt("cacheSize", Trace.c);
                        }
                        if (trace.getNotification() != null) {
                            bundle.putParcelable("notification", trace.getNotification());
                        }
                        this.c.putExtra("initData", bundle);
                        this.c.putExtra("operateType", 0);
                        this.c.putExtra("processID", Process.myPid());
                        b();
                        return;
                    }
                    if (this.h == null) {
                        return;
                    }
                    this.h.onStartTraceCallback(10002, "entity_name参数错误");
                    str = "entityName is empty string or null";
                } else {
                    if (this.h == null) {
                        return;
                    }
                    this.h.onStartTraceCallback(10002, "ak参数错误");
                    str = "ak is empty string or null";
                }
            } else {
                if (this.h == null) {
                    return;
                }
                this.h.onStartTraceCallback(10002, "service_id参数错误");
                str = "serviceId is invalid";
            }
            com.baidu.trace.a.a("BaiduTraceSDK", str);
        } catch (Exception unused) {
            OnTraceListener onTraceListener2 = this.h;
            if (onTraceListener2 != null) {
                onTraceListener2.onStartTraceCallback(10000, "请求发送失败");
            }
        }
    }

    public final void stopGather(OnTraceListener onTraceListener) {
        OnTraceListener onTraceListener2;
        int i;
        String str;
        if (onTraceListener != null) {
            this.h = onTraceListener;
        }
        IService iService = this.d;
        if (iService == null) {
            if (com.baidu.trace.c.e.a(this.b, LBSTraceService.class.getName())) {
                if (this.c == null) {
                    this.c = new Intent(this.b, (Class<?>) LBSTraceService.class);
                }
                this.c.putExtra("operateType", 2);
                b();
                return;
            }
            OnTraceListener onTraceListener3 = this.h;
            if (onTraceListener3 != null) {
                onTraceListener3.onStopGatherCallback(StatusCodes.STOP_GATHER_NOT_STARTED, "服务未开启");
                return;
            }
            return;
        }
        try {
            int stopGather = iService.stopGather();
            if (this.h == null) {
                return;
            }
            if (55 == stopGather) {
                onTraceListener2 = this.h;
                i = 0;
                str = StatusCodes.MSG_SUCCESS;
            } else if (57 != stopGather) {
                this.h.onStopGatherCallback(StatusCodes.STOP_GATHER_FAILED, StatusCodes.MSG_STOP_GATHER_FAILED);
                return;
            } else {
                onTraceListener2 = this.h;
                i = StatusCodes.GATHER_STOPPED;
                str = StatusCodes.MSG_GATHER_STOPPED;
            }
            onTraceListener2.onStopGatherCallback(i, str);
        } catch (Exception unused) {
            if (this.h == null) {
                return;
            }
            if (com.baidu.trace.c.e.a(this.b, LBSTraceService.class.getName())) {
                this.h.onStartGatherCallback(StatusCodes.STOP_GATHER_REQUEST_FAILED, "请求发送失败");
            } else {
                this.h.onStopGatherCallback(StatusCodes.STOP_GATHER_NOT_STARTED, "服务未开启");
            }
        }
    }

    public final void stopRealTimeLoc() {
        ar arVar = this.k;
        if (arVar != null) {
            arVar.b();
        }
    }

    public final void stopTrace(Trace trace, OnTraceListener onTraceListener) {
        if (trace == null) {
            com.baidu.trace.a.a("BaiduTraceSDK", "Trace instance is null");
            return;
        }
        if (onTraceListener != null) {
            this.h = onTraceListener;
        }
        IService iService = this.d;
        if (iService == null) {
            OnTraceListener onTraceListener2 = this.h;
            if (onTraceListener2 != null) {
                onTraceListener2.onStopTraceCallback(StatusCodes.NOT_START_TRACE, "服务未开启");
                return;
            }
            return;
        }
        try {
            iService.stopTrace(trace.getServiceId(), trace.getEntityName());
        } catch (Exception unused) {
            OnTraceListener onTraceListener3 = this.h;
            if (onTraceListener3 != null) {
                onTraceListener3.onStopTraceCallback(StatusCodes.STOP_TRACE_REQUEST_FAILED, "请求发送失败");
            }
        }
    }

    public final void updateEntity(UpdateEntityRequest updateEntityRequest, OnEntityListener onEntityListener) {
        if (com.baidu.trace.a.a("updateEntity", updateEntityRequest, onEntityListener)) {
            com.baidu.trace.a.a(updateEntityRequest, onEntityListener);
        }
    }

    public final void updateFence(UpdateFenceRequest updateFenceRequest, OnFenceListener onFenceListener) {
        if (com.baidu.trace.a.a("updateFence", updateFenceRequest, onFenceListener)) {
            com.baidu.trace.a.a(this.b, this.d, this.f, updateFenceRequest, onFenceListener);
        }
    }
}
