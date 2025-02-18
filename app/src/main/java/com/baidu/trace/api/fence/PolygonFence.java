package com.baidu.trace.api.fence;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import java.util.List;

/* loaded from: classes.dex */
public final class PolygonFence extends Fence {
    private List<LatLng> f;
    private CoordType g;

    private PolygonFence(long j, String str, FenceType fenceType, String str2, List<LatLng> list, int i, CoordType coordType) {
        super(j, str, str2, i, fenceType);
        this.g = CoordType.bd09ll;
        this.f = list;
        this.g = coordType;
    }

    public static PolygonFence buildServerFence(long j, String str, String str2, List<LatLng> list, int i, CoordType coordType) {
        return new PolygonFence(j, str, FenceType.server, str2, list, i, coordType);
    }

    public final CoordType getCoordType() {
        return this.g;
    }

    public final List<LatLng> getVertexes() {
        return this.f;
    }

    public final void setCoordType(CoordType coordType) {
        this.g = coordType;
    }

    public final void setVertexes(List<LatLng> list) {
        this.f = list;
    }

    @Override // com.baidu.trace.api.fence.Fence
    public final String toString() {
        return "PolygonFence [fenceId=" + this.a + ", fenceName=" + this.b + ", fenceType=" + this.e + ", monitoredPerson=" + this.c + ", vertexes=" + this.f + ", denoise=" + this.d + ", coordType=" + this.g + "]";
    }
}
