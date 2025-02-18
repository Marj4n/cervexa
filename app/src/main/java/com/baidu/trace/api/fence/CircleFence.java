package com.baidu.trace.api.fence;

import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;

/* loaded from: classes.dex */
public final class CircleFence extends Fence {
    private LatLng f;
    private double g;
    private CoordType h;

    private CircleFence(long j, String str, FenceType fenceType, String str2, LatLng latLng, double d, int i, CoordType coordType) {
        super(j, str, str2, i, fenceType);
        this.h = CoordType.bd09ll;
        this.f = latLng;
        this.g = d;
        this.h = coordType;
    }

    public static CircleFence buildLocalFence(long j, String str, String str2, LatLng latLng, double d, int i, CoordType coordType) {
        return new CircleFence(j, str, FenceType.local, str2, latLng, d, i, coordType);
    }

    public static CircleFence buildServerFence(long j, String str, String str2, LatLng latLng, double d, int i, CoordType coordType) {
        return new CircleFence(j, str, FenceType.server, str2, latLng, d, i, coordType);
    }

    public final LatLng getCenter() {
        return this.f;
    }

    public final CoordType getCoordType() {
        return this.h;
    }

    public final double getRadius() {
        return this.g;
    }

    public final void setCenter(LatLng latLng) {
        this.f = latLng;
    }

    public final void setCoordType(CoordType coordType) {
        this.h = coordType;
    }

    public final void setRadius(double d) {
        this.g = d;
    }

    @Override // com.baidu.trace.api.fence.Fence
    public final String toString() {
        return "CircleFence [fenceId=" + this.a + ", fenceName=" + this.b + ", fenceType=" + this.e + ", monitoredPerson=" + this.c + ", center=" + this.f + ", radius=" + this.g + ", denoise=" + this.d + ", coordType=" + this.h + "]";
    }
}
