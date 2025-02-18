package com.baidu.trace.api.fence;

import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import java.util.List;

/* loaded from: classes.dex */
public final class UpdateFenceRequest extends BaseRequest {
    private Fence a;

    private UpdateFenceRequest(int i, long j, Fence fence) {
        super(i, j);
        this.a = fence;
    }

    public static UpdateFenceRequest buildLocalCircleRequest(int i, long j, long j2, String str, String str2, LatLng latLng, double d, int i2, CoordType coordType) {
        return new UpdateFenceRequest(i, j, CircleFence.buildLocalFence(j2, str, str2, latLng, d, i2, coordType));
    }

    public static UpdateFenceRequest buildServerCircleRequest(int i, long j, long j2, String str, String str2, LatLng latLng, double d, int i2, CoordType coordType) {
        return new UpdateFenceRequest(i, j, CircleFence.buildServerFence(j2, str, str2, latLng, d, i2, coordType));
    }

    public static UpdateFenceRequest buildServerDistrictRequest(int i, long j, long j2, String str, String str2, String str3, int i2) {
        return new UpdateFenceRequest(i, j, DistrictFence.buildServerFence(j2, str, str2, i2, str3));
    }

    public static UpdateFenceRequest buildServerPolygonRequest(int i, long j, long j2, String str, String str2, List<LatLng> list, int i2, CoordType coordType) {
        return new UpdateFenceRequest(i, j, PolygonFence.buildServerFence(j2, str, str2, list, i2, coordType));
    }

    public static UpdateFenceRequest buildServerPolylineRequest(int i, long j, long j2, String str, String str2, List<LatLng> list, int i2, int i3, CoordType coordType) {
        return new UpdateFenceRequest(i, j, PolylineFence.buildServerFence(j2, str, str2, list, i2, i3, coordType));
    }

    public final Fence getFence() {
        return this.a;
    }

    public final String toString() {
        StringBuilder sb;
        String str;
        Fence fence = this.a;
        if (fence instanceof CircleFence) {
            sb = new StringBuilder("UpdateFenceRequest [tag=");
            sb.append(this.tag);
            sb.append(", serviceId=");
            sb.append(this.serviceId);
            str = ", circleFence=";
        } else if (fence instanceof PolygonFence) {
            sb = new StringBuilder("UpdateFenceRequest [tag=");
            sb.append(this.tag);
            sb.append(", serviceId=");
            sb.append(this.serviceId);
            str = ", polygonFence=";
        } else if (fence instanceof PolylineFence) {
            sb = new StringBuilder("UpdateFenceRequest [tag=");
            sb.append(this.tag);
            sb.append(", serviceId=");
            sb.append(this.serviceId);
            str = ", polylineFence=";
        } else if (fence instanceof DistrictFence) {
            sb = new StringBuilder("UpdateFenceRequest [tag=");
            sb.append(this.tag);
            sb.append(", serviceId=");
            sb.append(this.serviceId);
            str = ", districtFence=";
        } else {
            sb = new StringBuilder("UpdateFenceRequest [tag=");
            sb.append(this.tag);
            sb.append(", serviceId=");
            sb.append(this.serviceId);
            str = ", fence=";
        }
        sb.append(str);
        sb.append(this.a.toString());
        sb.append("]");
        return sb.toString();
    }
}
