package com.baidu.trace.api.fence;

/* loaded from: classes.dex */
public final class FenceInfo {
    private FenceShape a;
    private CircleFence b;
    private PolygonFence c;
    private PolylineFence d;
    private DistrictFence e;
    private String f;
    private String g;

    public FenceInfo() {
    }

    public FenceInfo(FenceShape fenceShape, CircleFence circleFence, PolygonFence polygonFence, PolylineFence polylineFence, DistrictFence districtFence, String str, String str2) {
        this.a = fenceShape;
        this.b = circleFence;
        this.c = polygonFence;
        this.d = polylineFence;
        this.e = districtFence;
        this.f = str;
        this.g = str2;
    }

    public FenceInfo(FenceShape fenceShape, CircleFence circleFence, PolygonFence polygonFence, PolylineFence polylineFence, String str, String str2) {
        this.a = fenceShape;
        this.b = circleFence;
        this.c = polygonFence;
        this.d = polylineFence;
        this.f = str;
        this.g = str2;
    }

    public final CircleFence getCircleFence() {
        return this.b;
    }

    public final String getCreateTime() {
        return this.f;
    }

    public final DistrictFence getDistrictFence() {
        return this.e;
    }

    public final FenceShape getFenceShape() {
        return this.a;
    }

    public final String getModifyTime() {
        return this.g;
    }

    public final PolygonFence getPolygonFence() {
        return this.c;
    }

    public final PolylineFence getPolylineFence() {
        return this.d;
    }

    public final void setCircleFence(CircleFence circleFence) {
        this.b = circleFence;
    }

    public final void setCreateTime(String str) {
        this.f = str;
    }

    public final void setDistrictFence(DistrictFence districtFence) {
        this.e = districtFence;
    }

    public final void setFenceShape(FenceShape fenceShape) {
        this.a = fenceShape;
    }

    public final void setModifyTime(String str) {
        this.g = str;
    }

    public final void setPolygonFence(PolygonFence polygonFence) {
        this.c = polygonFence;
    }

    public final void setPolylineFence(PolylineFence polylineFence) {
        this.d = polylineFence;
    }

    public final String toString() {
        StringBuilder sb;
        Object obj;
        Object obj2;
        if (FenceShape.circle == this.a) {
            sb = new StringBuilder("FenceInfo [fenceShape=");
            sb.append(this.a);
            sb.append(", circleFence=");
            obj2 = this.b;
        } else if (FenceShape.polygon == this.a) {
            sb = new StringBuilder("FenceInfo [fenceShape=");
            sb.append(this.a);
            sb.append(", polygonFence=");
            obj2 = this.c;
        } else if (FenceShape.polyline == this.a) {
            sb = new StringBuilder("FenceInfo [fenceShape=");
            sb.append(this.a);
            sb.append(", polylineFence=");
            obj2 = this.d;
        } else {
            if (FenceShape.district == this.a) {
                sb = new StringBuilder("FenceInfo [fenceShape=");
                obj = this.a;
            } else {
                sb = new StringBuilder("FenceInfo [fenceShape=");
                sb.append(this.a);
                sb.append(", circleFence=");
                sb.append(this.b);
                sb.append(", polygonFence=");
                sb.append(this.c);
                sb.append(", polylineFence=");
                obj = this.d;
            }
            sb.append(obj);
            sb.append(", districtFence=");
            obj2 = this.e;
        }
        sb.append(obj2);
        sb.append(", createTime=");
        sb.append(this.f);
        sb.append(", modifyTime=");
        sb.append(this.g);
        sb.append("]");
        return sb.toString();
    }
}
