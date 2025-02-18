package com.baidu.trace;

import com.baidu.trace.api.fence.AlarmPoint;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;

/* loaded from: classes.dex */
final class bi extends AlarmPoint {
    public bi(LatLng latLng, CoordType coordType, long j, double d) {
        super(latLng, coordType, j, d);
    }

    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        bi biVar = (bi) obj;
        boolean z = this.c == biVar.c;
        double d = this.a.latitude;
        double d2 = this.a.longitude;
        double d3 = biVar.a.latitude;
        double d4 = biVar.a.longitude;
        if (Double.doubleToLongBits(d) != Double.doubleToLongBits(d3)) {
            z = false;
        }
        if (Double.doubleToLongBits(d2) != Double.doubleToLongBits(d4)) {
            return false;
        }
        return z;
    }

    public final int hashCode() {
        return super.hashCode();
    }

    @Override // com.baidu.trace.api.fence.AlarmPoint
    public final String toString() {
        return "TrackPoint [location=" + this.a + ", coordType=" + this.b + ", locTime=" + this.c + ", radius=" + this.d + "]";
    }
}
