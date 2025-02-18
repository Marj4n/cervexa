package com.baidu.trace.api.fence;

import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import java.util.List;

/* loaded from: classes.dex */
public final class MonitoredStatusByLocationRequest extends BaseRequest {
    private String a;
    private List<Long> b;
    private LatLng c;
    private CoordType d;
    private FenceType e;

    private MonitoredStatusByLocationRequest(int i, long j, String str, List<Long> list, LatLng latLng, CoordType coordType, FenceType fenceType) {
        super(i, j);
        this.a = str;
        this.b = list;
        this.c = latLng;
        this.d = coordType;
        this.e = fenceType;
    }

    public static MonitoredStatusByLocationRequest buildLocalRequest(int i, long j, String str, List<Long> list, LatLng latLng, CoordType coordType) {
        return new MonitoredStatusByLocationRequest(i, j, str, list, latLng, coordType, FenceType.local);
    }

    public static MonitoredStatusByLocationRequest buildServerRequest(int i, long j, String str, List<Long> list, LatLng latLng, CoordType coordType) {
        return new MonitoredStatusByLocationRequest(i, j, str, list, latLng, coordType, FenceType.server);
    }

    public final CoordType getCoordType() {
        return this.d;
    }

    public final List<Long> getFenceIds() {
        return this.b;
    }

    public final FenceType getFenceType() {
        return this.e;
    }

    public final LatLng getLatLng() {
        return this.c;
    }

    public final String getMonitoredPerson() {
        return this.a;
    }

    public final void setCoordType(CoordType coordType) {
        this.d = coordType;
    }

    public final void setFenceIds(List<Long> list) {
        this.b = list;
    }

    public final void setLatLng(LatLng latLng) {
        this.c = latLng;
    }

    public final void setMonitoredPerson(String str) {
        this.a = str;
    }

    public final String toString() {
        StringBuilder sb;
        if (FenceType.local == this.e) {
            sb = new StringBuilder("MonitoredStatusByLocationRequest [tag=");
            sb.append(this.tag);
            sb.append(", serviceId=");
            sb.append(this.serviceId);
        } else {
            sb = new StringBuilder("MonitoredStatusByLocationRequest [tag=");
            sb.append(this.tag);
            sb.append(", serviceId=");
            sb.append(this.serviceId);
            sb.append(", monitoredPerson=");
            sb.append(this.a);
        }
        sb.append(", fenceIds=");
        sb.append(this.b);
        sb.append(", latLng=");
        sb.append(this.c);
        sb.append(", coordType=");
        sb.append(this.d);
        sb.append(", fenceType=");
        sb.append(this.e);
        sb.append("]");
        return sb.toString();
    }
}
