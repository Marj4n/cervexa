package com.baidu.trace.api.fence;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import java.util.List;

/* loaded from: classes.dex */
public final class PolylineFence extends Fence {
    private List<LatLng> f;
    private int g;
    private CoordType h;

    private PolylineFence(long j, String str, FenceType fenceType, String str2, List<LatLng> list, int i, int i2, CoordType coordType) {
        super(j, str, str2, i2, fenceType);
        this.h = CoordType.bd09ll;
        this.f = list;
        this.g = i;
        this.h = coordType;
    }

    public static PolylineFence buildServerFence(long j, String str, String str2, List<LatLng> list, int i, int i2, CoordType coordType) {
        return new PolylineFence(j, str, FenceType.server, str2, list, i, i2, coordType);
    }

    public final CoordType getCoordType() {
        return this.h;
    }

    public final int getOffset() {
        return this.g;
    }

    public final List<LatLng> getVertexes() {
        return this.f;
    }

    public final void setCoordType(CoordType coordType) {
        this.h = coordType;
    }

    public final void setOffset(int i) {
        this.g = i;
    }

    public final void setVertexes(List<LatLng> list) {
        this.f = list;
    }

    @Override // com.baidu.trace.api.fence.Fence
    public final String toString() {
        return "PolylineFence [fenceId=" + this.a + ", fenceName=" + this.b + ", fenceType=" + this.e + ", monitoredPerson=" + this.c + ", vertexes=" + this.f + ", offset=" + this.g + ", denoise=" + this.d + ", coordType=" + this.h + "]";
    }
}
