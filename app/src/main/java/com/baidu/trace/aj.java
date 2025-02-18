package com.baidu.trace;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.baidu.trace.api.fence.CircleFence;
import com.baidu.trace.api.fence.Fence;
import com.baidu.trace.api.fence.MonitoredAction;
import com.baidu.trace.api.fence.MonitoredStatus;
import com.baidu.trace.api.fence.MonitoredStatusInfo;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import com.jieli.stream.dv.running2.util.IConstant;
import com.tencent.tauth.AuthActivity;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class aj {
    private Map<Long, Fence> a = new ConcurrentHashMap();
    private Map<Long, MonitoredStatus> b = new ConcurrentHashMap();
    private bi c;

    public aj(Context context) {
        try {
            File file = new File("/data/data/" + com.baidu.trace.c.e.c(context) + "/shared_prefs", "fenceStatus.xml");
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception unused) {
        }
    }

    private void a(CircleFence circleFence, bi biVar, MonitoredAction monitoredAction, Handler handler) {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        JSONObject jSONObject3 = new JSONObject();
        try {
            jSONObject.put("fence_id", circleFence.getFenceId());
            jSONObject.put("fence", circleFence.getFenceName());
            jSONObject.put("monitored_person", circleFence.getMonitoredPerson());
            jSONObject.put(AuthActivity.ACTION_KEY, monitoredAction.name());
            jSONObject2.put("time", com.baidu.trace.c.e.b());
            jSONObject2.put("latitude", biVar.getLocation().latitude);
            jSONObject2.put("longitude", biVar.getLocation().longitude);
            jSONObject2.put("radius", biVar.getRadius());
            jSONObject2.put("coord_type", CoordType.bd09ll.name());
            jSONObject.put("cur_point", jSONObject2);
            if (this.c == null) {
                this.c = biVar;
            }
            jSONObject3.put("time", this.c.getLocTime());
            jSONObject3.put("latitude", this.c.getLocation().latitude);
            jSONObject3.put("longitude", this.c.getLocation().longitude);
            jSONObject3.put("radius", this.c.getRadius());
            jSONObject3.put("coord_type", CoordType.bd09ll.name());
            jSONObject.put("pre_point", jSONObject3);
            am.a(circleFence, monitoredAction, jSONObject2.toString(), jSONObject3.toString());
            Message obtainMessage = handler.obtainMessage(IConstant.OP_DOWNLOAD_FILES);
            obtainMessage.obj = jSONObject.toString();
            handler.sendMessage(obtainMessage);
        } catch (JSONException unused) {
        }
    }

    private void b(long j, MonitoredStatus monitoredStatus) {
        this.b.put(Long.valueOf(j), monitoredStatus);
        am.a(j, monitoredStatus);
    }

    protected final synchronized void a(long j) {
        this.a.remove(Long.valueOf(j));
    }

    protected final synchronized void a(long j, Fence fence) {
        this.a.put(Long.valueOf(j), fence);
    }

    protected final synchronized void a(long j, MonitoredStatus monitoredStatus) {
        this.b.put(Long.valueOf(j), monitoredStatus);
    }

    protected final synchronized void a(bi biVar, Handler handler) {
        MonitoredAction monitoredAction;
        for (Fence fence : this.a.values()) {
            if (fence instanceof CircleFence) {
                CircleFence circleFence = (CircleFence) fence;
                if (z.c.equals(circleFence.getMonitoredPerson()) && (circleFence.getDenoise() <= 0 || biVar.getRadius() <= circleFence.getDenoise())) {
                    LatLng center = circleFence.getCenter();
                    MonitoredStatus monitoredStatus = MonitoredStatus.unknown;
                    if (this.b.containsKey(Long.valueOf(circleFence.getFenceId()))) {
                        monitoredStatus = this.b.get(Long.valueOf(circleFence.getFenceId()));
                    }
                    if (circleFence.getRadius() > com.baidu.trace.c.e.a(center, biVar.getLocation())) {
                        if (MonitoredStatus.unknown == monitoredStatus || MonitoredStatus.out == monitoredStatus) {
                            b(circleFence.getFenceId(), MonitoredStatus.in);
                            if (MonitoredStatus.out == monitoredStatus) {
                                monitoredAction = MonitoredAction.enter;
                                a(circleFence, biVar, monitoredAction, handler);
                            }
                        }
                    } else if (MonitoredStatus.in == monitoredStatus) {
                        b(circleFence.getFenceId(), MonitoredStatus.out);
                        monitoredAction = MonitoredAction.exit;
                        a(circleFence, biVar, monitoredAction, handler);
                    } else if (MonitoredStatus.unknown == monitoredStatus) {
                        b(circleFence.getFenceId(), MonitoredStatus.out);
                    }
                }
            }
        }
        this.c = biVar;
    }

    protected final synchronized void a(String str, LatLng latLng, List<MonitoredStatusInfo> list) {
        Iterator<Fence> it = this.a.values().iterator();
        while (it.hasNext()) {
            a.a(it.next(), str, latLng, list);
        }
    }

    protected final synchronized boolean a() {
        boolean z;
        if (this.a != null) {
            z = this.a.isEmpty();
        }
        return z;
    }

    protected final synchronized Fence b(long j) {
        if (this.a == null) {
            return null;
        }
        return this.a.get(Long.valueOf(j));
    }

    protected final synchronized void b() {
        c();
        this.a = null;
        this.b = null;
    }

    protected final synchronized void c() {
        if (this.a != null) {
            this.a.clear();
        }
        if (this.b != null) {
            this.b.clear();
        }
    }
}
