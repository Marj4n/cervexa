package com.baidu.trace.api.fence;

import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import java.util.List;

/* loaded from: classes.dex */
public final class CreateFenceRequest extends BaseRequest {
    private Fence a;

    private CreateFenceRequest(int i, long j, Fence fence) {
        super(i, j);
        this.a = fence;
    }

    public static CreateFenceRequest buildLocalCircleRequest(int i, long j, String str, String str2, LatLng latLng, double d, int i2, CoordType coordType) {
        return new CreateFenceRequest(i, j, CircleFence.buildLocalFence(0L, str, str2, latLng, d, i2, coordType));
    }

    public static CreateFenceRequest buildServerCircleRequest(int i, long j, String str, String str2, LatLng latLng, double d, int i2, CoordType coordType) {
        return new CreateFenceRequest(i, j, CircleFence.buildServerFence(0L, str, str2, latLng, d, i2, coordType));
    }

    public static CreateFenceRequest buildServerDistrictRequest(int i, long j, String str, String str2, String str3, int i2) {
        return new CreateFenceRequest(i, j, DistrictFence.buildServerFence(0L, str, str2, i2, str3));
    }

    public static CreateFenceRequest buildServerPolygonRequest(int i, long j, String str, String str2, List<LatLng> list, int i2, CoordType coordType) {
        return new CreateFenceRequest(i, j, PolygonFence.buildServerFence(0L, str, str2, list, i2, coordType));
    }

    public static CreateFenceRequest buildServerPolylineRequest(int i, long j, String str, String str2, List<LatLng> list, int i2, int i3, CoordType coordType) {
        return new CreateFenceRequest(i, j, PolylineFence.buildServerFence(0L, str, str2, list, i2, i3, coordType));
    }

    public final Fence getFence() {
        return this.a;
    }

    public final String toString() {
        StringBuilder sb;
        String str;
        Fence fence = this.a;
        if (fence instanceof CircleFence) {
            sb = new StringBuilder("CreateFenceRequest [tag=");
            sb.append(this.tag);
            sb.append(", serviceId=");
            sb.append(this.serviceId);
            str = ", circleFence=";
        } else if (fence instanceof PolygonFence) {
            sb = new StringBuilder("CreateFenceRequest [tag=");
            sb.append(this.tag);
            sb.append(", serviceId=");
            sb.append(this.serviceId);
            str = ", polygonFence=";
        } else if (fence instanceof PolylineFence) {
            sb = new StringBuilder("CreateFenceRequest [tag=");
            sb.append(this.tag);
            sb.append(", serviceId=");
            sb.append(this.serviceId);
            str = ", polylineFence=";
        } else if (fence instanceof DistrictFence) {
            sb = new StringBuilder("CreateFenceRequest [tag=");
            sb.append(this.tag);
            sb.append(", serviceId=");
            sb.append(this.serviceId);
            str = ", districtFence=";
        } else {
            sb = new StringBuilder("CreateFenceRequest [tag=");
            sb.append(this.tag);
            sb.append(", serviceId=");
            sb.append(this.serviceId);
            str = ", Fence=";
        }
        sb.append(str);
        sb.append(this.a.toString());
        sb.append("]");
        return sb.toString();
    }
}
